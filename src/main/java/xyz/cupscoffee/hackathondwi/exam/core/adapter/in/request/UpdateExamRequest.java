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
import xyz.cupscoffee.hackathondwi.exam.core.domain.query.payload.UpdateExamPayload;

/**
 * Update exam request.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class UpdateExamRequest implements UpdateExamPayload {
    @NotNull(message = "exam.update.request.id.not-null")
    @Positive(message = "exam.update.request.id.positive")
    private Long id;

    @NotNull(message = "exam.update.request.name.not-null")
    @NotEmpty(message = "exam.update.request.name.not-empty")
    @NotBlank(message = "exam.update.request.name.not-blank")
    @Size(min = 1, max = 50, message = "exam.update.request.name.size")
    private String name;
}
