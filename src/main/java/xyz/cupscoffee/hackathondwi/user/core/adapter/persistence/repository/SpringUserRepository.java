package xyz.cupscoffee.hackathondwi.user.core.adapter.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import xyz.cupscoffee.hackathondwi.user.core.adapter.persistence.entity.UserEntity;

/**
 * Spring JPA repository for user entity.
 */
public interface SpringUserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByCode(String code);
}
