package xyz.cupscoffee.hackathondwi.semester.core.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import xyz.cupscoffee.hackathondwi.semester.core.adapter.out.persistence.entity.SemesterEntity;
import xyz.cupscoffee.hackathondwi.semester.core.application.dto.SemesterDto;
import xyz.cupscoffee.hackathondwi.semester.core.domain.model.Semester;

/**
 * Semester mapper.
 *
 * <p>
 * Maps the {@link Semester} domain model to the {@link SemesterEntity} entity
 * and
 * vice
 * versa.
 * </p>
 *
 * <p>
 * Also maps the {@link Semester} domain model to the {@link SemesterDto} DTO.
 * </p>
 *
 * @see Semester
 * @see SemesterEntity
 * @see SemesterDto
 */
@Mapper(componentModel = "spring")
public interface SemesterMapper {
    /**
     * Maps the {@link SemesterEntity} entity to the {@link Semester} domain model.
     *
     * @param entity the entity to map
     * @return the domain model
     */
    Semester toDomain(SemesterEntity entity);

    /**
     * Maps the {@link Semester} domain model to the {@link SemesterEntity} entity.
     *
     * @param domain the domain model to map
     * @return the entity
     */
    SemesterEntity toEntity(Semester domain);

    /**
     * Maps the {@link Semester} domain model to the {@link SemesterDto} DTO.
     *
     * @param domain the domain model to map
     * @return the DTO
     */
    SemesterDto toDto(Semester domain);

    /**
     * Maps the {@link SemesterDto} DTO to the {@link Semester} domain model.
     *
     * @param dto the DTO to map
     * @return the domain model
     */
    @Mapping(target = "user", ignore = true)
    Semester toDomain(SemesterDto dto);
}
