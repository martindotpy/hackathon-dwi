package xyz.cupscoffee.hackathondwi.exam.question.adapter.in.response;

import lombok.experimental.SuperBuilder;
import xyz.cupscoffee.hackathondwi.exam.question.application.dto.QuestionDto;
import xyz.cupscoffee.hackathondwi.shared.application.response.PaginatedResponse;

/**
 * Paginated question response.
 */
@SuperBuilder
public final class PaginatedQuestionResponse extends PaginatedResponse<QuestionDto> {
}
