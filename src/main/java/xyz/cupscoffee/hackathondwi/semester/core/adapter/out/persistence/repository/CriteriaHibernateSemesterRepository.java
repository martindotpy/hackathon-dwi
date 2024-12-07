package xyz.cupscoffee.hackathondwi.semester.core.adapter.out.persistence.repository;

import org.springframework.stereotype.Component;

import xyz.cupscoffee.hackathondwi.semester.core.adapter.out.persistence.entity.SemesterEntity;
import xyz.cupscoffee.hackathondwi.shared.adapter.out.persistence.repository.CriteriaHibernateRepository;

/**
 * Criteria Hibernate repository for the semester entity.
 */
@Component
public final class CriteriaHibernateSemesterRepository extends CriteriaHibernateRepository<SemesterEntity> {
}
