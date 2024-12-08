package xyz.cupscoffee.hackathondwi.semester.course.domain.query.payload;

import xyz.cupscoffee.hackathondwi.shared.domain.payload.Payload;

/**
 * Create course payload.
 */
public interface CreateCoursePayload extends Payload {
    String getName();

    Long getSemesterId();
}
