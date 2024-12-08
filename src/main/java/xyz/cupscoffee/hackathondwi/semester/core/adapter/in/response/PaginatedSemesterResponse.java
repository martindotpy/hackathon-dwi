package xyz.cupscoffee.hackathondwi.semester.core.adapter.in.response;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import xyz.cupscoffee.hackathondwi.semester.core.application.dto.SemesterDto;
import xyz.cupscoffee.hackathondwi.shared.application.response.PaginatedResponse;

/**
 * Paginated semester response.
 */
@SuperBuilder
@NoArgsConstructor
public final class PaginatedSemesterResponse extends PaginatedResponse<SemesterDto> {
}
