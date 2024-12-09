package xyz.cupscoffee.hackathondwi.exam.answer.adapter.in.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.cupscoffee.hackathondwi.exam.answer.domain.query.payload.UpdateAnswerPayload;

/**
 * Update answer request.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class UpdateAnswerRequest implements UpdateAnswerPayload {
    @NotNull(message = "answer.update.request.id.not-null")
    @Positive(message = "answer.update.request.id.positive")
    private Long id;

    @NotNull(message = "answer.update.request.value.not-null")
    @PositiveOrZero(message = "answer.update.request.value.positive-or-zero")
    @Max(value = 20, message = "answer.update.request.value.max")
    private Integer value;
}
