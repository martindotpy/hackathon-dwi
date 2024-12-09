package xyz.cupscoffee.hackathondwi.exam.answer.adapter.in.response;

import java.util.List;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import xyz.cupscoffee.hackathondwi.exam.answer.application.dto.AnswerDto;
import xyz.cupscoffee.hackathondwi.shared.application.response.ContentResponse;

/**
 * Answer content response.
 */
@SuperBuilder
@NoArgsConstructor
public final class AnswersContentResponse extends ContentResponse<List<AnswerDto>> {
}
