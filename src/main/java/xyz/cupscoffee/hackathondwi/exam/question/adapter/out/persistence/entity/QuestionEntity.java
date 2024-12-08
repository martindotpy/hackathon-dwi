package xyz.cupscoffee.hackathondwi.exam.question.adapter.out.persistence.entity;

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
import xyz.cupscoffee.hackathondwi.exam.core.adapter.out.persistence.entity.ExamEntity;

/**
 * Question entity.
 *
 * <p>
 * This class represents a question in the database. It is the entity
 * representation of the
 * {@link xyz.cupscoffee.hackathondwi.exam.question.domain.model.Question
 * Question}
 * class.
 * </p>
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "question")
public class QuestionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT UNSIGNED")
    private Long id;

    @Column(name = "`order`", columnDefinition = "TINYINT UNSIGNED", nullable = false)
    private Integer order;
    @Column(columnDefinition = "TINYINT UNSIGNED", nullable = false)
    private Integer maxValue;

    @ManyToOne
    private ExamEntity exam;
}
