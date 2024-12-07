package xyz.cupscoffee.hackathondwi.semester.core.domain.query.payload;

import xyz.cupscoffee.hackathondwi.shared.domain.payload.Payload;

/**
 * Create semester payload.
 */
public interface CreateSemesterPayload extends Payload {
    String getName();
}
