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
import xyz.cupscoffee.hackathondwi.student.core.domain.query.payload.CreateStudentPayload;

/**
 * Create student request.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class CreateStudentRequest implements CreateStudentPayload {
    private String firstName;
    private String lastName;
    @NotNull(message = "student.create.request.code.not-null")
    @NotEmpty(message = "student.create.request.code.not-empty")
    @NotBlank(message = "student.create.request.code.not-blank")
    @Size(min = 9, max = 9, message = "student.create.request.code.size")
    private String code;

    @NotNull(message = "student.create.request.course-id.not-null")
    @Positive(message = "student.create.request.course-id.positive")
    private Long courseId;
}
