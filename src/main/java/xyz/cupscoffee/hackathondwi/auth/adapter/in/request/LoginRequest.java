package xyz.cupscoffee.hackathondwi.auth.adapter.in.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.cupscoffee.hackathondwi.user.core.domain.payload.LoginUserPayload;

/**
 * Request for logging in a user.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class LoginRequest implements LoginUserPayload {
    @NotNull(message = "El código es requerido")
    @NotEmpty(message = "El código no puede estar vacío")
    @NotBlank(message = "El código no puede estar en blanco")
    @Size(min = 6, max = 6, message = "El código debe tener 6 caracteres")
    private String code;
    @NotNull(message = "La contraseña es requerida")
    @NotEmpty(message = "La contraseña no puede estar vacía")
    @NotBlank(message = "La contraseña no puede estar en blanco")
    private String password;
}
