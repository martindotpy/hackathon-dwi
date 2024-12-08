package xyz.cupscoffee.hackathondwi.semester.course.domain.query.payload;

import xyz.cupscoffee.hackathondwi.shared.domain.payload.Payload;

/**
 * Update course payload.
 */
public interface UpdateCoursePayload extends Payload {
    Long getId();

    String getName();
}
