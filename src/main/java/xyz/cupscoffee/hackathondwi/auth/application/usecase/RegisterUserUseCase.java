package xyz.cupscoffee.hackathondwi.auth.application.usecase;

import static org.fusesource.jansi.Ansi.ansi;

import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.cupscoffee.hackathondwi.auth.application.dto.JwtDto;
import xyz.cupscoffee.hackathondwi.auth.application.port.in.RegisterUserPort;
import xyz.cupscoffee.hackathondwi.auth.application.port.out.JwtAuthenticationPort;
import xyz.cupscoffee.hackathondwi.auth.domain.query.failure.AuthFailure;
import xyz.cupscoffee.hackathondwi.shared.adapter.annotations.UseCase;
import xyz.cupscoffee.hackathondwi.shared.domain.query.result.Result;
import xyz.cupscoffee.hackathondwi.user.domain.model.User;
import xyz.cupscoffee.hackathondwi.user.domain.payload.RegisterUserPayload;
import xyz.cupscoffee.hackathondwi.user.domain.query.failure.UserFailure;
import xyz.cupscoffee.hackathondwi.user.domain.repository.UserRepository;

@Slf4j
@UseCase
@RequiredArgsConstructor
public final class RegisterUserUseCase implements RegisterUserPort {
    private final UserRepository userRepository;
    private final JwtAuthenticationPort jwtAuthenticationPort;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Result<JwtDto, AuthFailure> register(RegisterUserPayload payload) {
        User newUser = User.builder()
                .code(payload.getCode())
                .name(payload.getName())
                .password(passwordEncoder.encode(payload.getPassword()))
                .build();

        var result = userRepository.save(newUser);

        if (result.isFailure()) {
            UserFailure failure = result.getFailure();

            if (failure == UserFailure.CODE_ALREADY_EXISTS) {
                log.debug("Someone tried to register with an existing code");

                return Result.failure(AuthFailure.CODE_ALREADY_EXISTS);
            }

            return Result.failure(AuthFailure.UNEXPECTED_ERROR);
        }

        User user = result.getSuccess();
        String jwt = jwtAuthenticationPort.toJwt(user);

        log.info("User {} registered",
                ansi().fgGreen().a(user.getCode()).reset());

        return Result.ok(
                JwtDto.builder()
                        .jwt(jwt)
                        .build());
    }
}
