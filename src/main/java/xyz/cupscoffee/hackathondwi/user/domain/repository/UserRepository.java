package xyz.cupscoffee.hackathondwi.user.domain.repository;

import java.util.Optional;

import xyz.cupscoffee.hackathondwi.shared.domain.repository.BasicRepository;
import xyz.cupscoffee.hackathondwi.user.domain.model.User;
import xyz.cupscoffee.hackathondwi.user.domain.query.failure.UserFailure;

public interface UserRepository extends BasicRepository<User, Long, UserFailure> {
    Optional<User> findByCode(String code);
}
