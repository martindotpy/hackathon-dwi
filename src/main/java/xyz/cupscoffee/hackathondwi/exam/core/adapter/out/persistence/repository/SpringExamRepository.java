package xyz.cupscoffee.hackathondwi.exam.core.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import xyz.cupscoffee.hackathondwi.exam.core.adapter.out.persistence.entity.ExamEntity;

/**
 * Spring JPA repository for exam entity.
 */
public interface SpringExamRepository extends JpaRepository<ExamEntity, Long> {
}
