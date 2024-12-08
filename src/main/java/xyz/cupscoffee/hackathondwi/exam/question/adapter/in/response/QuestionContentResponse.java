package xyz.cupscoffee.hackathondwi.exam.question.adapter.in.response;

import lombok.experimental.SuperBuilder;
import xyz.cupscoffee.hackathondwi.exam.question.application.dto.QuestionDto;
import xyz.cupscoffee.hackathondwi.shared.application.response.ContentResponse;

/**
 * Question content response.
 */
@SuperBuilder
public final class QuestionContentResponse extends ContentResponse<QuestionDto> {
}
