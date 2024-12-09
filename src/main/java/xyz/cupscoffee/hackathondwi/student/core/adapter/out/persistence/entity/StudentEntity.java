package xyz.cupscoffee.hackathondwi.student.core.adapter.out.persistence.entity;

import java.util.List;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.cupscoffee.hackathondwi.semester.course.adapter.out.persistence.entity.CourseEntity;

/**
 * Student entity.
 *
 * <p>
 * This class represents a student in the database. It is the entity
 * representation of the
 * {@link xyz.cupscoffee.hackathondwi.course.student.domain.model.Student
 * Student}
 * class.
 * </p>
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "student")
public class StudentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT UNSIGNED")
    private Long id;

    @Nullable
    @Column(columnDefinition = "VARCHAR(50)")
    private String firstName;
    @Nullable
    @Column(columnDefinition = "VARCHAR(50)")
    private String lastName;
    @Column(columnDefinition = "CHAR(9)", nullable = false)
    private String code;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<CourseEntity> courses;
}
