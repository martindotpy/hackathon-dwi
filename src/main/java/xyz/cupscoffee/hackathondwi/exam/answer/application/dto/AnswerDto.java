package xyz.cupscoffee.hackathondwi.exam.answer.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Answer DTO.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnswerDto {
    private Long id;

    private Integer value;
}
