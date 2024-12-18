package xyz.cupscoffee.hackathondwi.semester.course.domain.model;

import java.util.List;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.cupscoffee.hackathondwi.semester.core.domain.model.Semester;
import xyz.cupscoffee.hackathondwi.student.core.domain.model.Student;

/**
 * Course.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    private Long id;

    private String name;

    @Nullable
    private Semester semester;
    @Nullable
    private List<Student> students;
}
