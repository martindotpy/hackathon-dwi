package xyz.cupscoffee.hackathondwi.semester.core.adapter.in.response;

import lombok.experimental.SuperBuilder;
import xyz.cupscoffee.hackathondwi.semester.core.application.dto.SemesterDto;
import xyz.cupscoffee.hackathondwi.shared.application.response.PaginatedResponse;

/**
 * Paginated semester response.
 */
@SuperBuilder
public final class PaginatedSemesterResponse extends PaginatedResponse<SemesterDto> {
}
