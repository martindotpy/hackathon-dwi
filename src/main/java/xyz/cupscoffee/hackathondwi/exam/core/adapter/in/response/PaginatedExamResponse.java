package xyz.cupscoffee.hackathondwi.exam.core.adapter.in.response;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import xyz.cupscoffee.hackathondwi.exam.core.application.dto.ExamDto;
import xyz.cupscoffee.hackathondwi.shared.application.response.PaginatedResponse;

/**
 * Paginated exam response.
 */
@SuperBuilder
@NoArgsConstructor
public final class PaginatedExamResponse extends PaginatedResponse<ExamDto> {
}
