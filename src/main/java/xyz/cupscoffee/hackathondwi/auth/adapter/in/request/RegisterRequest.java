package xyz.cupscoffee.hackathondwi.auth.adapter.in.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.cupscoffee.hackathondwi.user.domain.payload.RegisterUserPayload;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class RegisterRequest implements RegisterUserPayload {
    @NotNull(message = "El código es requerido")
    @NotEmpty(message = "El código no puede estar vacío")
    @NotBlank(message = "El código no puede estar en blanco")
    @Size(min = 6, max = 6, message = "El código debe tener 6 caracteres")
    private String code;
    @NotNull(message = "El nombre es requerido")
    @NotEmpty(message = "El nombre no puede estar vacío")
    @NotBlank(message = "El nombre no puede estar en blanco")
    @Size(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
    private String name;
    @NotNull(message = "La contraseña es requerida")
    @NotEmpty(message = "La contraseña no puede estar vacía")
    @NotBlank(message = "La contraseña no puede estar en blanco")
    @Size(min = 8, max = 50, message = "La contraseña debe tener entre 8 y 50 caracteres")
    private String password;
}
