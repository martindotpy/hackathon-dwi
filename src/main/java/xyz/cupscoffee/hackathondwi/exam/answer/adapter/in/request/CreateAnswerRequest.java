package xyz.cupscoffee.hackathondwi.exam.answer.adapter.in.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.cupscoffee.hackathondwi.exam.answer.domain.query.payload.CreateAnswerPayload;

/**
 * Create answer request.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class CreateAnswerRequest implements CreateAnswerPayload {
    @NotNull(message = "answer.create.request.value.not-null")
    @PositiveOrZero(message = "answer.create.request.id.positive-or-zero")
    @Max(value = 20, message = "answer.create.request.value.max")
    private Integer value;

    @NotNull(message = "answer.create.request.question-id.not-null")
    @Positive(message = "answer.create.request.question-id.positive")
    private Long questionId;
    @NotNull(message = "answer.create.request.student-id.not-null")
    @Positive(message = "answer.create.request.student-id.positive")
    private Long studentId;
}
