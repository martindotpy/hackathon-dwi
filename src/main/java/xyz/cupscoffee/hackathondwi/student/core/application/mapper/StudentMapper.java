package xyz.cupscoffee.hackathondwi.student.core.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import xyz.cupscoffee.hackathondwi.semester.course.application.mapper.CourseMapper;
import xyz.cupscoffee.hackathondwi.student.core.adapter.out.persistence.entity.StudentEntity;
import xyz.cupscoffee.hackathondwi.student.core.application.dto.StudentDto;
import xyz.cupscoffee.hackathondwi.student.core.domain.model.Student;

/**
 * Student mapper.
 *
 * <p>
 * Maps the {@link Student} domain model to the {@link StudentEntity} entity
 * and
 * vice
 * versa.
 * </p>
 *
 * <p>
 * Also maps the {@link Student} domain model to the {@link StudentDto} DTO.
 * </p>
 *
 * @see Student
 * @see StudentEntity
 * @see StudentDto
 */
@Mapper(componentModel = "spring", uses = { CourseMapper.class })
public interface StudentMapper {
    /**
     * Maps the {@link StudentEntity} entity to the {@link Student} domain model.
     *
     * @param entity the entity to map
     * @return the domain model
     */
    Student toDomain(StudentEntity entity);

    /**
     * Maps the {@link Student} domain model to the {@link StudentEntity} entity.
     *
     * @param domain the domain model to map
     * @return the entity
     */
    @Mapping(target = "courses", ignore = true)
    StudentEntity toEntity(Student domain);

    /**
     * Maps the {@link Student} domain model to the {@link StudentDto} DTO.
     *
     * @param domain the domain model to map
     * @return the DTO
     */
    StudentDto toDto(Student domain);

    /**
     * Maps the {@link StudentDto} DTO to the {@link Student} domain model.
     *
     * @param dto the DTO to map
     * @return the domain model
     */
    Student toDomain(StudentDto dto);
}
