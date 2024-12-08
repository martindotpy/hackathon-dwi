package xyz.cupscoffee.hackathondwi.exam.core.domain.model;

import java.util.List;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.cupscoffee.hackathondwi.exam.question.domain.model.Question;
import xyz.cupscoffee.hackathondwi.semester.course.domain.model.Course;

/**
 * Exam.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Exam {
    private Long id;

    private String name;

    @Nullable
    private Course course;
    @Nullable
    private List<Question> questions;
}
