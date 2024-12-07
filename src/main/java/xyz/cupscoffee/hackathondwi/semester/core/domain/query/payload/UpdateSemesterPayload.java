package xyz.cupscoffee.hackathondwi.semester.core.domain.query.payload;

import xyz.cupscoffee.hackathondwi.shared.domain.payload.Payload;

/**
 * Update semester payload.
 */
public interface UpdateSemesterPayload extends Payload {
    Long getId();

    String getName();
}
