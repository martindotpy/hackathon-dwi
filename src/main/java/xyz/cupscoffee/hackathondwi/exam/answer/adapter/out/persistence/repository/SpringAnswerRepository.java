package xyz.cupscoffee.hackathondwi.exam.answer.adapter.out.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import xyz.cupscoffee.hackathondwi.exam.answer.adapter.out.persistence.entity.AnswerEntity;

/**
 * Spring JPA repository for answer entity.
 */
public interface SpringAnswerRepository extends JpaRepository<AnswerEntity, Long> {
    List<AnswerEntity> findAllByQuestionExamIdAndStudentId(Long examId, Long studentId);
}
