package xyz.cupscoffee.hackathondwi.user.core.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import xyz.cupscoffee.hackathondwi.user.core.adapter.out.persistence.entity.UserEntity;
import xyz.cupscoffee.hackathondwi.user.core.application.dto.UserDto;
import xyz.cupscoffee.hackathondwi.user.core.domain.model.User;

/**
 * User mapper.
 *
 * <p>
 * Maps the {@link User} domain model to the {@link UserEntity} entity and vice
 * versa.
 * </p>
 *
 * <p>
 * Also maps the {@link User} domain model to the {@link UserDto} DTO.
 * </p>
 *
 * @see User
 * @see UserEntity
 * @see UserDto
 */
@Mapper(componentModel = "spring")
public interface UserMapper {
    /**
     * Maps the {@link UserEntity} entity to the {@link User} domain model.
     *
     * @param entity the entity to map
     * @return the domain model
     */
    User toDomain(UserEntity entity);

    /**
     * Maps the {@link User} domain model to the {@link UserEntity} entity.
     *
     * @param domain the domain model to map
     * @return the entity
     */
    UserEntity toEntity(User domain);

    /**
     * Maps the {@link User} domain model to the {@link UserDto} DTO.
     *
     * @param domain the domain model to map
     * @return the DTO
     */
    UserDto toDto(User domain);

    /**
     * Maps the {@link UserDto} DTO to the {@link User} domain model.
     *
     * @param dto the DTO to map
     * @return the domain model
     */
    @Mapping(target = "password", ignore = true)
    User toDomain(UserDto dto);
}
