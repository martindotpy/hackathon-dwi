package xyz.cupscoffee.hackathondwi.shared.adapter.in.view;

import static org.fusesource.jansi.Ansi.ansi;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.annotation.SessionScope;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Rest client to make HTTP requests.
 */
@Slf4j
@Component
@SessionScope
@RequiredArgsConstructor
public class RestClient {
    @Value("${springdoc.api-docs.server-url}/api/${spring.api.version}")
    private String baseUrl;
    private String jwt;
    private final HttpServletRequest servletRequest;
    private final RestTemplate restTemplate;
    private final ClientHttpRequestInterceptor jwtInterceptor = new ClientHttpRequestInterceptor() {
        @Override
        @SuppressWarnings("null")
        public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
                throws IOException {
            if (jwt != null) {
                request.getHeaders().add("Authorization", "Bearer " + jwt);
                log.debug("JWT added to request headers");
            }

            return execution.execute(request, body);
        }
    };

    @PostConstruct
    public void init() {
        addJwtInterceptor();
    }

    @PreDestroy
    public void destroy() {
        restTemplate.getInterceptors().remove(jwtInterceptor);
    }

    /**
     * Load JWT from cookies.
     *
     * @param request the HTTP servlet request.
     */
    public void loadJwtFromCookies(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("jwt".equals(cookie.getName())) {
                    this.jwt = cookie.getValue();

                    log.debug("JWT loaded from cookies");
                    break;
                }
            }
        }
    }

    /**
     * Add JWT interceptor to RestTemplate.
     */
    private void addJwtInterceptor() {
        restTemplate.getInterceptors().add(jwtInterceptor);
    }

    /**
     * Get request.
     *
     * @param <T>          the type of the response.
     * @param path         the path.
     * @param responseType the response type.
     * @return the response
     */
    public <T> RestResponse<?> get(String path, Class<T> responseType) {
        try {
            loadJwtFromCookies(servletRequest);

            log.info("Making GET request to `{}`", ansi().fgYellow().a(baseUrl + path).reset());

            return RestResponse.of(200, restTemplate.getForObject(baseUrl + path, responseType));
        } catch (RestClientException e) {
            if (e.getMostSpecificCause() instanceof HttpClientErrorException.NotFound notFound) {
                log.info("Error while making GET request: status `{}` - body `{}`",
                        ansi().fgRed().a(notFound.getStatusCode()).reset(),
                        ansi().fgRed().a(notFound.getResponseBodyAsString()).reset());

                return RestResponse.of(404, notFound.getResponseBodyAsString());
            }

            log.error("Error while making GET request", e);

            return RestResponse.of(500);
        }
    }

    /**
     * Post request.
     *
     * @param <T>          the type of the response.
     * @param path         the path.
     * @param request      the request.
     * @param responseType the response type.
     * @return the response
     */
    public <T> RestResponse<? extends Object> post(String path, Object request, Class<T> responseType) {
        try {
            loadJwtFromCookies(servletRequest);

            log.info("Making POST request to `{}`", ansi().fgYellow().a(baseUrl + path).reset());

            return RestResponse.of(200, restTemplate.postForObject(baseUrl + path, request, responseType));
        } catch (HttpClientErrorException e) {
            int status = e.getStatusCode().value();
            String body = e.getResponseBodyAsString();

            log.info("Error while making POST request: status `{}` - body `{}`",
                    ansi().fgRed().a(status).reset(),
                    ansi().fgRed().a(body).reset());

            return RestResponse.of(status, body);
        } catch (RestClientException e) {
            log.error("Error while making POST request", e);

            return RestResponse.of(500);
        }
    }

    /**
     * Put request.
     *
     * @param <T>          the type of the response.
     * @param path         the path.
     * @param request      the request.
     * @param responseType the response type.
     */
    public <T> RestResponse<?> put(String path, Object request, Class<T> responseType) {
        loadJwtFromCookies(servletRequest);

        log.info("Making PUT request to `{}`", ansi().fgYellow().a(baseUrl + path).reset());

        restTemplate.put(baseUrl + path, request);

        return RestResponse.of(200);
    }

    /**
     * Delete request.
     *
     * @param path the path.
     */
    public RestResponse<?> delete(String path) {
        loadJwtFromCookies(servletRequest);

        log.info("Making DELETE request to `{}`", ansi().fgYellow().a(baseUrl + path).reset());

        restTemplate.delete(baseUrl + path);

        return RestResponse.of(200);
    }
}
