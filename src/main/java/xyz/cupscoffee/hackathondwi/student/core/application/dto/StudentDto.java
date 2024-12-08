package xyz.cupscoffee.hackathondwi.student.core.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Student DTO.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentDto {
    private Long id;

    private String firstName;
    private String lastName;
    private String code;
}
