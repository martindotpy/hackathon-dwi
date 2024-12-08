package xyz.cupscoffee.hackathondwi.semester.course.adapter.in.response;

import lombok.experimental.SuperBuilder;
import xyz.cupscoffee.hackathondwi.semester.course.application.dto.CourseDto;
import xyz.cupscoffee.hackathondwi.shared.application.response.PaginatedResponse;

/**
 * Paginated course response.
 */
@SuperBuilder
public final class PaginatedCourseResponse extends PaginatedResponse<CourseDto> {
}
