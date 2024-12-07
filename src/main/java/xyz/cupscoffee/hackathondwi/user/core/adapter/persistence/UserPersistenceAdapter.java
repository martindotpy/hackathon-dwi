package xyz.cupscoffee.hackathondwi.user.core.adapter.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetailsService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.cupscoffee.hackathondwi.shared.adapter.annotations.PersistenceAdapter;
import xyz.cupscoffee.hackathondwi.shared.domain.query.result.Result;
import xyz.cupscoffee.hackathondwi.user.core.adapter.persistence.entity.UserEntity;
import xyz.cupscoffee.hackathondwi.user.core.adapter.persistence.repository.SpringUserRepository;
import xyz.cupscoffee.hackathondwi.user.core.application.mapper.UserMapper;
import xyz.cupscoffee.hackathondwi.user.core.domain.model.User;
import xyz.cupscoffee.hackathondwi.user.core.domain.query.failure.UserFailure;
import xyz.cupscoffee.hackathondwi.user.core.domain.repository.UserRepository;

/**
 * Persistence adapter for the user entity.
 *
 * <p>
 * Implements the {@link UserRepository} interface, to be used by the domain
 * layer.
 * Also implements the {@link UserDetailsService} interface, to be used by
 * Spring Security.
 * </p>
 */
@Slf4j
@PersistenceAdapter
@AllArgsConstructor
public final class UserPersistenceAdapter implements UserDetailsService, UserRepository {
    private final SpringUserRepository springUserRepository;
    private final UserMapper userMapper;

    @Override
    public UserEntity loadUserByUsername(String code) {
        return userMapper.toEntity(findByCode(code).get());
    }

    @Override
    public Result<User, UserFailure> save(User entity) {
        try {
            return Result.ok(userMapper.toDomain(springUserRepository.save(userMapper.toEntity(entity))));
        } catch (DataIntegrityViolationException e) {
            if (e.getCause() instanceof org.hibernate.exception.ConstraintViolationException constraintViolationException) {
                String constraintName = constraintViolationException.getConstraintName();

                if (constraintName.contains("uk_user_code")) {
                    log.error("User with code {} already exists", entity.getCode());

                    return Result.failure(UserFailure.CODE_ALREADY_EXISTS);
                }
            }

            log.error("Error saving user", e);

            return Result.failure(UserFailure.UNEXPECTED_ERROR);
        } catch (Exception e) {
            log.error("Error saving user", e);

            return Result.failure(UserFailure.UNEXPECTED_ERROR);
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        return springUserRepository.findById(id)
                .map(userMapper::toDomain);
    }

    @Override
    public Optional<User> findByCode(String code) {
        return springUserRepository.findByCode(code)
                .map(userMapper::toDomain);
    }

    @Override
    public List<User> findAll() {
        return springUserRepository.findAll()
                .stream()
                .map(userMapper::toDomain)
                .toList();
    }

    @Override
    public Result<Void, UserFailure> deleteById(Long id) {
        try {
            springUserRepository.deleteById(id);

            return Result.ok();
        } catch (Exception e) {
            log.error("Error deleting user with id {}", id, e);

            return Result.failure(UserFailure.UNEXPECTED_ERROR);
        }
    }

}
