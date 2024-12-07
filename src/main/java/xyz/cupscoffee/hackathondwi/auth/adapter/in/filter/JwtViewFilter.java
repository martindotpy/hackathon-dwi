package xyz.cupscoffee.hackathondwi.auth.adapter.in.filter;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import xyz.cupscoffee.hackathondwi.shared.adapter.properties.SecurityProperties;

/**
 * Filter to check if the user has a jwt cookie or authorization header.
 */
@Component
@RequiredArgsConstructor
public class JwtViewFilter extends OncePerRequestFilter {
    private final SecurityProperties securityProperties;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    @SuppressWarnings("null")
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String path = request.getRequestURI();

        boolean isExcludedPath = Arrays.stream(securityProperties.getAllowedPublicRoutes())
                .anyMatch(publicPath -> antPathMatcher.match(publicPath, path));

        if (!isExcludedPath && !hasJwtCookieOrAuthorizationHeader(request)) {
            response.sendRedirect("/login.xhtml");
            return;
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Check if the request has a jwt cookie or authorization header.
     *
     * @param request the request.
     * @return true if the request has a jwt cookie or authorization header, false
     *         otherwise.
     */
    private boolean hasJwtCookieOrAuthorizationHeader(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("jwt".equals(cookie.getName())) {
                    return true;
                }
            }
        }

        String authorizationHeader = request.getHeader("Authorization");

        return authorizationHeader != null && authorizationHeader.startsWith("Bearer ");
    }
}