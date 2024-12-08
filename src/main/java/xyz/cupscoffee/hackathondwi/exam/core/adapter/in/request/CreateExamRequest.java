package xyz.cupscoffee.hackathondwi.exam.core.adapter.in.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.cupscoffee.hackathondwi.exam.core.domain.query.payload.CreateExamPayload;

/**
 * Create exam request.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class CreateExamRequest implements CreateExamPayload {
    @NotNull(message = "exam.create.request.name.not-null")
    @NotEmpty(message = "exam.create.request.name.not-empty")
    @NotBlank(message = "exam.create.request.name.not-blank")
    @Size(min = 1, max = 50, message = "exam.create.request.name.size")
    private String name;

    @NotNull(message = "exam.create.request.course-id.not-null")
    @Positive(message = "exam.create.request.course-id.positive")
    private Long courseId;
}
