package xyz.cupscoffee.hackathondwi.exam.answer.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.cupscoffee.hackathondwi.exam.question.adapter.out.persistence.entity.QuestionEntity;
import xyz.cupscoffee.hackathondwi.student.core.adapter.out.persistence.entity.StudentEntity;

/**
 * Answer entity.
 *
 * <p>
 * This class represents a answer in the database. It is the entity
 * representation of the
 * {@link xyz.cupscoffee.hackathondwi.exam.answer.domain.model.Answer
 * Answer}
 * class.
 * </p>
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "answer")
public class AnswerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT UNSIGNED")
    private Long id;

    @Column(columnDefinition = "TINYINT UNSIGNED", nullable = false)
    private Integer value;

    @ManyToOne(fetch = FetchType.EAGER)
    private QuestionEntity question;
    @ManyToOne(fetch = FetchType.EAGER)
    private StudentEntity student;
}
