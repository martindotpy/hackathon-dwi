package xyz.cupscoffee.hackathondwi.exam.answer.domain.model;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.cupscoffee.hackathondwi.exam.question.domain.model.Question;
import xyz.cupscoffee.hackathondwi.student.core.domain.model.Student;

/**
 * Answer.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Answer {
    private Long id;

    private Integer value;

    @Nullable
    private Student student;
    @Nullable
    private Question question;
}
