package xyz.cupscoffee.hackathondwi.exam.answer.adapter.in.response;

import lombok.experimental.SuperBuilder;
import xyz.cupscoffee.hackathondwi.exam.answer.application.dto.AnswerDto;
import xyz.cupscoffee.hackathondwi.shared.application.response.ContentResponse;

/**
 * Answer content response.
 */
@SuperBuilder
public final class AnswerContentResponse extends ContentResponse<AnswerDto> {
}
