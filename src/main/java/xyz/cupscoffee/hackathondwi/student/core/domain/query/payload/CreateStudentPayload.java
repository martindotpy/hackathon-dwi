package xyz.cupscoffee.hackathondwi.student.core.domain.query.payload;

import xyz.cupscoffee.hackathondwi.shared.domain.payload.Payload;

/**
 * Create student payload.
 */
public interface CreateStudentPayload extends Payload {
    String getFirstName();

    String getLastName();

    String getCode();

    Long getCourseId();
}
