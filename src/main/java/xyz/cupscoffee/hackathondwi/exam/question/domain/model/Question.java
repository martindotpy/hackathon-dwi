package xyz.cupscoffee.hackathondwi.exam.question.domain.model;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.cupscoffee.hackathondwi.exam.core.domain.model.Exam;

/**
 * Question.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Question {
    private Long id;

    private Integer order;
    private Integer maxValue;

    @Nullable
    private Exam exam;
}
