package xyz.cupscoffee.hackathondwi.auth.adapter.in.controller;

import static xyz.cupscoffee.hackathondwi.shared.adapter.in.util.ControllerShortcuts.toDetailedFailureResponse;
import static xyz.cupscoffee.hackathondwi.shared.adapter.in.util.ControllerShortcuts.toFailureResponse;
import static xyz.cupscoffee.hackathondwi.shared.adapter.in.util.ControllerShortcuts.toOkResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import xyz.cupscoffee.hackathondwi.auth.adapter.in.request.LoginRequest;
import xyz.cupscoffee.hackathondwi.auth.adapter.in.request.RegisterRequest;
import xyz.cupscoffee.hackathondwi.auth.adapter.in.response.JwtResponse;
import xyz.cupscoffee.hackathondwi.auth.application.port.in.LoginUserPort;
import xyz.cupscoffee.hackathondwi.auth.application.port.in.RegisterUserPort;
import xyz.cupscoffee.hackathondwi.shared.application.response.DetailedFailureResponse;
import xyz.cupscoffee.hackathondwi.shared.application.response.FailureResponse;

@Tag(name = "Auth", description = "Authentication")
@RestController
@RequestMapping("/api/${spring.api.version}/auth")
@RequiredArgsConstructor
public final class AuthController {
    private final LoginUserPort loginUserPort;
    private final RegisterUserPort registerUserPort;

    @Operation(summary = "Login", description = "Login a user", responses = {
            @ApiResponse(responseCode = "200", description = "User logged in", content = @Content(schema = @Schema(implementation = JwtResponse.class))),
            @ApiResponse(responseCode = "403", description = "Invalid credentials", content = @Content(schema = @Schema(implementation = FailureResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = FailureResponse.class)))
    })
    @SecurityRequirements
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        var violations = request.validate();

        if (!violations.isEmpty()) {
            return toDetailedFailureResponse(violations);
        }

        var result = loginUserPort.login(request);

        if (result.isFailure()) {
            return toFailureResponse(result.getFailure());
        }

        return toOkResponse(JwtResponse.class, result.getSuccess(), "auth.logged_in", "User logged in");
    }

    @Operation(summary = "Register", description = "Register a user", responses = {
            @ApiResponse(responseCode = "200", description = "User registered", content = @Content(schema = @Schema(implementation = JwtResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = DetailedFailureResponse.class))),
            @ApiResponse(responseCode = "409", description = "User code already exists", content = @Content(schema = @Schema(implementation = FailureResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = FailureResponse.class)))
    })
    @SecurityRequirements
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        var violations = request.validate();

        if (!violations.isEmpty()) {
            return toDetailedFailureResponse(violations);
        }

        var result = registerUserPort.register(request);

        if (result.isFailure()) {
            return toFailureResponse(result.getFailure());
        }

        return toOkResponse(JwtResponse.class, result.getSuccess(), "auth.registered", "User registered");
    }
}
