package xyz.cupscoffee.hackathondwi.auth.adapter.out.service;

import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import xyz.cupscoffee.hackathondwi.auth.application.port.out.GetAuthenticatedUserPort;
import xyz.cupscoffee.hackathondwi.auth.application.port.out.JwtAuthenticationPort;
import xyz.cupscoffee.hackathondwi.user.domain.model.User;

/**
 * Service to get the authenticated user from the request.
 */
@Service
@RequiredArgsConstructor
public final class GetAuthenticatedUserService implements GetAuthenticatedUserPort {
    private final HttpServletRequest request;
    private final JwtAuthenticationPort jwtAuthenticationPort;

    @Override
    public Optional<User> get() {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);

            return Optional.ofNullable(jwtAuthenticationPort.fromJwt(token));
        }

        return Optional.empty();
    }
}
