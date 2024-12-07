package xyz.cupscoffee.hackathondwi.semester.core.domain.model;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.cupscoffee.hackathondwi.user.core.domain.model.User;

/**
 * Semester.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Semester {
    private Long id;

    private String name;

    @Nullable
    private User user;
}
