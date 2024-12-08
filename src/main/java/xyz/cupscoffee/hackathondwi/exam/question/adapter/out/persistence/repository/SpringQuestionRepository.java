package xyz.cupscoffee.hackathondwi.exam.question.adapter.out.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import xyz.cupscoffee.hackathondwi.exam.question.adapter.out.persistence.entity.QuestionEntity;

/**
 * Spring JPA repository for question entity.
 */
public interface SpringQuestionRepository extends JpaRepository<QuestionEntity, Long> {
    List<QuestionEntity> findAllByExamId(Long examId);
}
