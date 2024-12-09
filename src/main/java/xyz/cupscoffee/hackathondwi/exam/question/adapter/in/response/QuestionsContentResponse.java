package xyz.cupscoffee.hackathondwi.exam.question.adapter.in.response;

import java.util.List;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import xyz.cupscoffee.hackathondwi.exam.question.application.dto.QuestionDto;
import xyz.cupscoffee.hackathondwi.shared.application.response.ContentResponse;

/**
 * Questions content response.
 */
@SuperBuilder
@NoArgsConstructor
public final class QuestionsContentResponse extends ContentResponse<List<QuestionDto>> {
}
