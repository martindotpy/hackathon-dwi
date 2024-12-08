package xyz.cupscoffee.hackathondwi.exam.answer.domain.query.payload;

import xyz.cupscoffee.hackathondwi.shared.domain.payload.Payload;

/**
 * Update answer payload.
 */
public interface UpdateAnswerPayload extends Payload {
    Long getId();

    Integer getValue();
}
