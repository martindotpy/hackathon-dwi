package xyz.cupscoffee.hackathondwi.semester.course.adapter.in.response;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import xyz.cupscoffee.hackathondwi.semester.course.application.dto.CourseDto;
import xyz.cupscoffee.hackathondwi.shared.application.response.ContentResponse;

/**
 * Course content response.
 */
@SuperBuilder
@NoArgsConstructor
public final class CourseContentResponse extends ContentResponse<CourseDto> {
}
