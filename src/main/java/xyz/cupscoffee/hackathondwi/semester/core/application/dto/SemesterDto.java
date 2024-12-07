package xyz.cupscoffee.hackathondwi.semester.core.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Semester DTO.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SemesterDto {
    private Long id;

    private String name;
}
