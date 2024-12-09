package xyz.cupscoffee.hackathondwi.student.core.adapter.in.response;

import java.util.List;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import xyz.cupscoffee.hackathondwi.shared.application.response.ContentResponse;
import xyz.cupscoffee.hackathondwi.student.core.application.dto.StudentDto;

/**
 * Students content response.
 */
@SuperBuilder
@NoArgsConstructor
public final class StudentsContentResponse extends ContentResponse<List<StudentDto>> {
}
