package xyz.cupscoffee.hackathondwi.semester.course.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import xyz.cupscoffee.hackathondwi.semester.course.adapter.out.persistence.entity.CourseEntity;
import xyz.cupscoffee.hackathondwi.semester.course.application.dto.CourseDto;
import xyz.cupscoffee.hackathondwi.semester.course.domain.model.Course;

/**
 * Course mapper.
 *
 * <p>
 * Maps the {@link Course} domain model to the {@link CourseEntity} entity
 * and
 * vice
 * versa.
 * </p>
 *
 * <p>
 * Also maps the {@link Course} domain model to the {@link CourseDto} DTO.
 * </p>
 *
 * @see Course
 * @see CourseEntity
 * @see CourseDto
 */
@Mapper(componentModel = "spring")
public interface CourseMapper {
    /**
     * Maps the {@link CourseEntity} entity to the {@link Course} domain model.
     *
     * @param entity the entity to map
     * @return the domain model
     */
    Course toDomain(CourseEntity entity);

    /**
     * Maps the {@link Course} domain model to the {@link CourseEntity} entity.
     *
     * @param domain the domain model to map
     * @return the entity
     */
    CourseEntity toEntity(Course domain);

    /**
     * Maps the {@link Course} domain model to the {@link CourseDto} DTO.
     *
     * @param domain the domain model to map
     * @return the DTO
     */
    CourseDto toDto(Course domain);

    /**
     * Maps the {@link CourseDto} DTO to the {@link Course} domain model.
     *
     * @param dto the DTO to map
     * @return the domain model
     */
    @Mapping(target = "semester", ignore = true)
    Course toDomain(CourseDto dto);
}
