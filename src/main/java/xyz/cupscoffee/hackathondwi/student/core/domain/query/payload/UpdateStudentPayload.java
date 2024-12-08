package xyz.cupscoffee.hackathondwi.student.core.domain.query.payload;

import xyz.cupscoffee.hackathondwi.shared.domain.payload.Payload;

/**
 * Update student payload.
 */
public interface UpdateStudentPayload extends Payload {
    Long getId();

    String getFirstName();

    String getLastName();

    String getCode();
}
