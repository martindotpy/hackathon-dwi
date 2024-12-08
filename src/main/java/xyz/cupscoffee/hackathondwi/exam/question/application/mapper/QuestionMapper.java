package xyz.cupscoffee.hackathondwi.exam.question.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import xyz.cupscoffee.hackathondwi.exam.question.adapter.out.persistence.entity.QuestionEntity;
import xyz.cupscoffee.hackathondwi.exam.question.application.dto.QuestionDto;
import xyz.cupscoffee.hackathondwi.exam.question.domain.model.Question;

/**
 * Question mapper.
 *
 * <p>
 * Maps the {@link Question} domain model to the {@link QuestionEntity} entity
 * and
 * vice
 * versa.
 * </p>
 *
 * <p>
 * Also maps the {@link Question} domain model to the {@link QuestionDto} DTO.
 * </p>
 *
 * @see Question
 * @see QuestionEntity
 * @see QuestionDto
 */
@Mapper(componentModel = "spring")
public interface QuestionMapper {
    /**
     * Maps the {@link QuestionEntity} entity to the {@link Question} domain model.
     *
     * @param entity the entity to map
     * @return the domain model
     */
    Question toDomain(QuestionEntity entity);

    /**
     * Maps the {@link Question} domain model to the {@link QuestionEntity} entity.
     *
     * @param domain the domain model to map
     * @return the entity
     */
    QuestionEntity toEntity(Question domain);

    /**
     * Maps the {@link Question} domain model to the {@link QuestionDto} DTO.
     *
     * @param domain the domain model to map
     * @return the DTO
     */
    QuestionDto toDto(Question domain);

    /**
     * Maps the {@link QuestionDto} DTO to the {@link Question} domain model.
     *
     * @param dto the DTO to map
     * @return the domain model
     */
    @Mapping(target = "exam", ignore = true)
    Question toDomain(QuestionDto dto);
}
