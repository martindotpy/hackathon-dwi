package xyz.cupscoffee.hackathondwi.shared.adapter.in.filter;

import static org.fusesource.jansi.Ansi.ansi;

import java.io.IOException;

import org.fusesource.jansi.Ansi.Color;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public final class LoggingFilter extends OncePerRequestFilter {
    private final String[] ignoredPaths = {
            "/favicon.ico",
            "/jakarta.faces.resource/.*",
            "/assets/.*\\.(css|ttf)",
            "/assets/font/.*\\.(css|ttf)",
            "/api/v0/swagger-ui/.*\\.(css|js|png)"
    };

    @Override
    @SuppressWarnings("null")
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String method = request.getMethod();
        String realIp = request.getHeader("X-Real-IP");
        String path = request.getRequestURI();

        if (path.matches(String.join("|", ignoredPaths))) {
            filterChain.doFilter(request, response);
            return;
        }

        StringBuilder sb = new StringBuilder();

        sb.append(
                String.format(
                        "%s to %s",
                        ansi().fg(Color.MAGENTA).a(method).reset(),
                        ansi().fg(Color.MAGENTA).a(path).reset()));

        if (realIp != null) {
            sb.append(String.format(" from %s", ansi().fg(Color.CYAN).a(realIp).reset()));
        }

        log.info(sb.toString());

        filterChain.doFilter(request, response);
    }
}
