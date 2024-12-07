package xyz.cupscoffee.hackathondwi.auth.adapter.in.filter;

import static org.fusesource.jansi.Ansi.ansi;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.cupscoffee.hackathondwi.auth.application.port.out.JwtAuthenticationPort;
import xyz.cupscoffee.hackathondwi.user.domain.model.User;

@Slf4j
@Component
@RequiredArgsConstructor
public final class JwtRequestFilter extends OncePerRequestFilter {
    private final JwtAuthenticationPort jwtAuthenticationPort;

    @Override
    @SuppressWarnings("null")
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);

            filterJwt(token, request, response, filterChain);
            return;
        }

        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            filterChain.doFilter(request, response);
            return;
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("jwt")) {
                filterJwt(cookie.getValue(), request, response, filterChain);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Filter JWT token.
     *
     * @param token       JWT token.
     * @param request     HTTP request.
     * @param response    HTTP response.
     * @param filterChain Filter chain.
     * @throws IOException      If an I/O error occurs.
     * @throws ServletException If an error occurs.
     */
    private void filterJwt(
            final String token,
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws IOException, ServletException {
        User user = jwtAuthenticationPort.fromJwt(token);

        if (user == null) {
            log.warn("JWT token is invalid");

            filterChain.doFilter(request, response);
            return;
        }

        log.debug("JWT token is valid");
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                user,
                null,
                null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        log.info("User {} successfully authenticated", ansi().fgBrightGreen().a(user.getCode()).reset());

        filterChain.doFilter(request, response);
    }
}
