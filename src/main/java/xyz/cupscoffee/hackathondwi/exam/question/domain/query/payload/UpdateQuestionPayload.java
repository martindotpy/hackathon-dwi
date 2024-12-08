package xyz.cupscoffee.hackathondwi.exam.question.domain.query.payload;

import xyz.cupscoffee.hackathondwi.shared.domain.payload.Payload;

/**
 * Update question payload.
 */
public interface UpdateQuestionPayload extends Payload {
    Long getId();

    Integer getOrder();

    Integer getMaxValue();
}
