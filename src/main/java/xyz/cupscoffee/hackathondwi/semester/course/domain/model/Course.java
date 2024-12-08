package xyz.cupscoffee.hackathondwi.semester.course.domain.model;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.cupscoffee.hackathondwi.semester.core.domain.model.Semester;

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
}
