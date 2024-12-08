package xyz.cupscoffee.hackathondwi.exam.answer.domain.query.payload;

import xyz.cupscoffee.hackathondwi.shared.domain.payload.Payload;

/**
 * Create answer payload.
 */
public interface CreateAnswerPayload extends Payload {
    Integer getValue();

    Long getStudentId();

    Long getQuestionId();
}
