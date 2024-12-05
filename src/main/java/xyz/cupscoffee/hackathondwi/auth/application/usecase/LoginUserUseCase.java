package xyz.cupscoffee.hackathondwi.auth.application.usecase;

import static org.fusesource.jansi.Ansi.ansi;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.cupscoffee.hackathondwi.auth.application.dto.JwtDto;
import xyz.cupscoffee.hackathondwi.auth.application.port.in.LoginUserPort;
import xyz.cupscoffee.hackathondwi.auth.application.port.out.JwtAuthenticationPort;
import xyz.cupscoffee.hackathondwi.auth.domain.query.failure.AuthFailure;
import xyz.cupscoffee.hackathondwi.shared.adapter.annotations.UseCase;
import xyz.cupscoffee.hackathondwi.shared.domain.query.result.Result;
import xyz.cupscoffee.hackathondwi.user.domain.model.User;
import xyz.cupscoffee.hackathondwi.user.domain.payload.LoginUserPayload;
import xyz.cupscoffee.hackathondwi.user.domain.repository.UserRepository;

@Slf4j
@UseCase
@RequiredArgsConstructor
public final class LoginUserUseCase implements LoginUserPort {
    private final UserRepository userRepository;
    private final JwtAuthenticationPort jwtAuthenticationPort;

    @Override
    public Result<JwtDto, AuthFailure> login(LoginUserPayload payload) {
        Optional<User> user = userRepository.findByCode(payload.getCode());

        if (user.isEmpty()) {
            log.debug("User not found");

            return Result.failure(AuthFailure.INVALID_CREDENTIALS);
        }

        User userFound = user.get();
        String jwt = jwtAuthenticationPort.toJwt(userFound);

        log.info("User {} logged in",
                ansi().fgGreen().a(userFound.getCode()).reset());

        return Result.ok(
                JwtDto.builder()
                        .jwt(jwt)
                        .build());
    }
}
