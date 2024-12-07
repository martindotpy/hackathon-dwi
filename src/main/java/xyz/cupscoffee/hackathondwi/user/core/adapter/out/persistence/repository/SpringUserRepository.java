package xyz.cupscoffee.hackathondwi.user.core.adapter.out.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import xyz.cupscoffee.hackathondwi.user.core.adapter.out.persistence.entity.UserEntity;

/**
 * Spring JPA repository for user entity.
 */
public interface SpringUserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByCode(String code);
}
