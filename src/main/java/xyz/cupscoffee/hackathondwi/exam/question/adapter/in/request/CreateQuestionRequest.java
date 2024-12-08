package xyz.cupscoffee.hackathondwi.exam.question.adapter.in.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.cupscoffee.hackathondwi.exam.question.domain.query.payload.CreateQuestionPayload;

/**
 * Create question request.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class CreateQuestionRequest implements CreateQuestionPayload {
    @NotNull(message = "question.create.request.order.not-null")
    @Positive(message = "question.create.request.order.positive")
    private Integer order;
    @NotNull(message = "question.create.request.max-value.not-null")
    @Positive(message = "question.create.request.max-value.positive")
    @Max(value = 20, message = "question.create.request.max-value.max")
    private Integer maxValue;

    @NotNull(message = "question.create.request.exam-id.not-null")
    @Positive(message = "question.create.request.exam-id.positive")
    private Long examId;
}
