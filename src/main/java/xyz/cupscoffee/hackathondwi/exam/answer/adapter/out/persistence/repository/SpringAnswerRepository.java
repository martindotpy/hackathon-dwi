package xyz.cupscoffee.hackathondwi.exam.answer.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import xyz.cupscoffee.hackathondwi.exam.answer.adapter.out.persistence.entity.AnswerEntity;

/**
 * Spring JPA repository for answer entity.
 */
public interface SpringAnswerRepository extends JpaRepository<AnswerEntity, Long> {
}
