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
import xyz.cupscoffee.hackathondwi.semester.course.domain.query.payload.CreateCoursePayload;

/**
 * Create course request.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class CreateCourseRequest implements CreateCoursePayload {
    @NotNull(message = "course.create.request.name.not-null")
    @NotEmpty(message = "course.create.request.name.not-empty")
    @NotBlank(message = "course.create.request.name.not-blank")
    @Size(min = 1, max = 50, message = "course.create.request.name.size")
    private String name;

    @NotNull(message = "course.create.request.semester-id.not-null")
    @Positive(message = "course.create.request.semester-id.positive")
    private Long semesterId;
}
