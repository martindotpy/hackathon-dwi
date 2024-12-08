package xyz.cupscoffee.hackathondwi.exam.answer.adapter.in.response;

import lombok.experimental.SuperBuilder;
import xyz.cupscoffee.hackathondwi.exam.answer.application.dto.AnswerDto;
import xyz.cupscoffee.hackathondwi.shared.application.response.PaginatedResponse;

/**
 * Paginated answer response.
 */
@SuperBuilder
public final class PaginatedAnswerResponse extends PaginatedResponse<AnswerDto> {
}
