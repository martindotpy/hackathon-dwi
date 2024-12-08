package xyz.cupscoffee.hackathondwi.exam.core.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Exam DTO.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamDto {
    private Long id;

    private String name;
}
