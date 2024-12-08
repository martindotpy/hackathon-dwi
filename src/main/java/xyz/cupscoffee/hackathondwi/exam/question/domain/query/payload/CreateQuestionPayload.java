package xyz.cupscoffee.hackathondwi.exam.question.domain.query.payload;

import xyz.cupscoffee.hackathondwi.shared.domain.payload.Payload;

/**
 * Create question payload.
 */
public interface CreateQuestionPayload extends Payload {
    Integer getOrder();

    Integer getMaxValue();

    Long getExamId();
}
