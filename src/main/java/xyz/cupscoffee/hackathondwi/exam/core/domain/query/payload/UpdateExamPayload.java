package xyz.cupscoffee.hackathondwi.exam.core.domain.query.payload;

import xyz.cupscoffee.hackathondwi.shared.domain.payload.Payload;

/**
 * Update exam payload.
 */
public interface UpdateExamPayload extends Payload {
    Long getId();

    String getName();
}
