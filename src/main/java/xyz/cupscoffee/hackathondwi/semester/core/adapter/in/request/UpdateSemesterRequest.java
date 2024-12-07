package xyz.cupscoffee.hackathondwi.semester.core.adapter.in.request;

import jakarta.validation.constraints.NegativeOrZero;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.cupscoffee.hackathondwi.semester.core.domain.query.payload.UpdateSemesterPayload;

/**
 * Update semester request.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class UpdateSemesterRequest implements UpdateSemesterPayload {
    @NotNull(message = "semester.update.request.id.not-null")
    @NegativeOrZero(message = "semester.update.request.id.negative-or-zero")
    private Long id;

    @NotNull(message = "semester.update.request.name.not-null")
    @NotEmpty(message = "semester.update.request.name.not-empty")
    @NotBlank(message = "semester.update.request.name.not-blank")
    @Size(min = 1, max = 50, message = "semester.update.request.name.size")
    private String name;
}
