package xyz.cupscoffee.hackathondwi.student.core.adapter.in.response;

import lombok.experimental.SuperBuilder;
import xyz.cupscoffee.hackathondwi.shared.application.response.ContentResponse;
import xyz.cupscoffee.hackathondwi.student.core.application.dto.StudentDto;

/**
 * Student content response.
 */
@SuperBuilder
public final class StudentContentResponse extends ContentResponse<StudentDto> {
}
