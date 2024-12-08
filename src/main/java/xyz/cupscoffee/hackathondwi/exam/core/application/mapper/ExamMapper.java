package xyz.cupscoffee.hackathondwi.exam.core.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import xyz.cupscoffee.hackathondwi.exam.core.adapter.out.persistence.entity.ExamEntity;
import xyz.cupscoffee.hackathondwi.exam.core.application.dto.ExamDto;
import xyz.cupscoffee.hackathondwi.exam.core.domain.model.Exam;
import xyz.cupscoffee.hackathondwi.semester.course.application.mapper.CourseMapper;

/**
 * Exam mapper.
 *
 * <p>
 * Maps the {@link Exam} domain model to the {@link ExamEntity} entity
 * and
 * vice
 * versa.
 * </p>
 *
 * <p>
 * Also maps the {@link Exam} domain model to the {@link ExamDto} DTO.
 * </p>
 *
 * @see Exam
 * @see ExamEntity
 * @see ExamDto
 */
@Mapper(componentModel = "spring", uses = { CourseMapper.class })
public interface ExamMapper {
    /**
     * Maps the {@link ExamEntity} entity to the {@link Exam} domain model.
     *
     * @param entity the entity to map
     * @return the domain model
     */
    Exam toDomain(ExamEntity entity);

    /**
     * Maps the {@link Exam} domain model to the {@link ExamEntity} entity.
     *
     * @param domain the domain model to map
     * @return the entity
     */
    ExamEntity toEntity(Exam domain);

    /**
     * Maps the {@link Exam} domain model to the {@link ExamDto} DTO.
     *
     * @param domain the domain model to map
     * @return the DTO
     */
    ExamDto toDto(Exam domain);

    /**
     * Maps the {@link ExamDto} DTO to the {@link Exam} domain model.
     *
     * @param dto the DTO to map
     * @return the domain model
     */
    @Mapping(target = "course", ignore = true)
    @Mapping(target = "questions", ignore = true)
    Exam toDomain(ExamDto dto);
}
