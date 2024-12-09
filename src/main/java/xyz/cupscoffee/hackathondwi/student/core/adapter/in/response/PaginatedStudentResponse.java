package xyz.cupscoffee.hackathondwi.student.core.adapter.in.response;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import xyz.cupscoffee.hackathondwi.shared.application.response.PaginatedResponse;
import xyz.cupscoffee.hackathondwi.student.core.application.dto.StudentDto;

/**
 * Paginated student response.
 */
@SuperBuilder
@NoArgsConstructor
public final class PaginatedStudentResponse extends PaginatedResponse<StudentDto> {
}
