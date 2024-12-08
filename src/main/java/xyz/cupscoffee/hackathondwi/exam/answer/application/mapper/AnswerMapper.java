package xyz.cupscoffee.hackathondwi.exam.answer.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import xyz.cupscoffee.hackathondwi.exam.answer.adapter.out.persistence.entity.AnswerEntity;
import xyz.cupscoffee.hackathondwi.exam.answer.application.dto.AnswerDto;
import xyz.cupscoffee.hackathondwi.exam.answer.domain.model.Answer;

/**
 * Answer mapper.
 *
 * <p>
 * Maps the {@link Answer} domain model to the {@link AnswerEntity} entity
 * and
 * vice
 * versa.
 * </p>
 *
 * <p>
 * Also maps the {@link Answer} domain model to the {@link AnswerDto} DTO.
 * </p>
 *
 * @see Answer
 * @see AnswerEntity
 * @see AnswerDto
 */
@Mapper(componentModel = "spring")
public interface AnswerMapper {
    /**
     * Maps the {@link AnswerEntity} entity to the {@link Answer} domain model.
     *
     * @param entity the entity to map
     * @return the domain model
     */
    Answer toDomain(AnswerEntity entity);

    /**
     * Maps the {@link Answer} domain model to the {@link AnswerEntity} entity.
     *
     * @param domain the domain model to map
     * @return the entity
     */
    AnswerEntity toEntity(Answer domain);

    /**
     * Maps the {@link Answer} domain model to the {@link AnswerDto} DTO.
     *
     * @param domain the domain model to map
     * @return the DTO
     */
    AnswerDto toDto(Answer domain);

    /**
     * Maps the {@link AnswerDto} DTO to the {@link Answer} domain model.
     *
     * @param dto the DTO to map
     * @return the domain model
     */
    @Mapping(target = "student", ignore = true)
    @Mapping(target = "question", ignore = true)
    Answer toDomain(AnswerDto dto);
}
