package xyz.cupscoffee.hackathondwi.student.core.adapter.in.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.cupscoffee.hackathondwi.student.core.domain.query.payload.UpdateStudentPayload;

/**
 * Update student request.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class UpdateStudentRequest implements UpdateStudentPayload {
    @NotNull(message = "student.update.request.id.not-null")
    @Positive(message = "student.update.request.id.positive")
    private Long id;

    private String firstName;
    private String lastName;
    @NotNull(message = "student.update.request.code.not-null")
    @NotEmpty(message = "student.update.request.code.not-empty")
    @NotBlank(message = "student.update.request.code.not-blank")
    @Size(min = 9, max = 9, message = "student.update.request.code.size")
    private String code;
}
