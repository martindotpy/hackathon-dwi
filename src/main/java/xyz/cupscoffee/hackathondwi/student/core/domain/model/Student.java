package xyz.cupscoffee.hackathondwi.student.core.domain.model;

import java.util.List;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.cupscoffee.hackathondwi.semester.course.domain.model.Course;

/**
 * Student.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    private Long id;

    private String firstName;
    private String lastName;
    private String code;

    @Nullable
    private List<Course> courses;
}
