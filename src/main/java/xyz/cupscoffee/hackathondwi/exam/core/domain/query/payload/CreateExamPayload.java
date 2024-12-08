package xyz.cupscoffee.hackathondwi.exam.core.domain.query.payload;

import xyz.cupscoffee.hackathondwi.shared.domain.payload.Payload;

/**
 * Create exam payload.
 */
public interface CreateExamPayload extends Payload {
    String getName();

    Long getCourseId();
}
