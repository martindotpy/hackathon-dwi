package xyz.cupscoffee.hackathondwi.semester.core.adapter.out.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import xyz.cupscoffee.hackathondwi.semester.core.adapter.out.persistence.entity.SemesterEntity;

/**
 * Spring JPA repository for semester entity.
 */
public interface SpringSemesterRepository extends JpaRepository<SemesterEntity, Long> {
    List<SemesterEntity> findAllByUserId(Long userId);
}
