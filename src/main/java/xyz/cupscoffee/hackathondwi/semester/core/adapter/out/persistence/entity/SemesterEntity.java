package xyz.cupscoffee.hackathondwi.semester.core.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.cupscoffee.hackathondwi.user.core.adapter.out.persistence.entity.UserEntity;

/**
 * Semester entity.
 *
 * <p>
 * This class represents a semester in the database. It is the entity
 * representation of the
 * {@link xyz.cupscoffee.hackathondwi.semester.core.domain.model.Semester
 * Semester}
 * class.
 * </p>
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "semester")
public class SemesterEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT UNSIGNED")
    private Long id;

    @Column(columnDefinition = "VARCHAR(50)", nullable = false)
    private String name;

    @ManyToOne
    private UserEntity user;
}
