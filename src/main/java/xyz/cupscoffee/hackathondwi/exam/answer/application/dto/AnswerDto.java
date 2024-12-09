package xyz.cupscoffee.hackathondwi.exam.answer.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import xyz.cupscoffee.hackathondwi.exam.question.application.dto.QuestionDto;

/**
 * Answer DTO.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnswerDto {
    private Long id;

    @Setter
    private Integer value;

    private QuestionDto question;
}
