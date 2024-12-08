package xyz.cupscoffee.hackathondwi.exam.question.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Question DTO.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDto {
    private Long id;

    private Integer order;
    private Integer maxValue;
}
