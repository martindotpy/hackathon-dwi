package xyz.cupscoffee.hackathondwi.semester.core.adapter.in.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.cupscoffee.hackathondwi.semester.core.domain.query.payload.CreateSemesterPayload;

/**
 * Create semester request.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class CreateSemesterRequest implements CreateSemesterPayload {
    @NotNull(message = "semester.create.request.name.not-null")
    @NotEmpty(message = "semester.create.request.name.not-empty")
    @NotBlank(message = "semester.create.request.name.not-blank")
    @Size(min = 1, max = 50, message = "semester.create.request.name.size")
    private String name;
}
