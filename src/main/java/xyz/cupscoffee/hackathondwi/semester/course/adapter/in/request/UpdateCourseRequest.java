package xyz.cupscoffee.hackathondwi.semester.course.adapter.in.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.cupscoffee.hackathondwi.semester.course.domain.query.payload.UpdateCoursePayload;

/**
 * Update course request.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class UpdateCourseRequest implements UpdateCoursePayload {
    @NotNull(message = "course.update.request.id.not-null")
    @Positive(message = "course.update.request.id.positive")
    private Long id;

    @NotNull(message = "course.update.request.name.not-null")
    @NotEmpty(message = "course.update.request.name.not-empty")
    @NotBlank(message = "course.update.request.name.not-blank")
    @Size(min = 1, max = 50, message = "course.update.request.name.size")
    private String name;
}
