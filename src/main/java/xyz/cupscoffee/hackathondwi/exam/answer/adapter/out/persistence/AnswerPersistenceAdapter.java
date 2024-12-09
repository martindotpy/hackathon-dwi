package xyz.cupscoffee.hackathondwi.exam.answer.adapter.out.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.cupscoffee.hackathondwi.exam.answer.adapter.out.persistence.repository.CriteriaHibernateAnswerRepository;
import xyz.cupscoffee.hackathondwi.exam.answer.adapter.out.persistence.repository.SpringAnswerRepository;
import xyz.cupscoffee.hackathondwi.exam.answer.application.mapper.AnswerMapper;
import xyz.cupscoffee.hackathondwi.exam.answer.domain.model.Answer;
import xyz.cupscoffee.hackathondwi.exam.answer.domain.query.failure.AnswerFailure;
import xyz.cupscoffee.hackathondwi.exam.answer.domain.repository.AnswerRepository;
import xyz.cupscoffee.hackathondwi.shared.adapter.annotations.PersistenceAdapter;
import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.Criteria;
import xyz.cupscoffee.hackathondwi.shared.domain.query.paginated.Paginated;
import xyz.cupscoffee.hackathondwi.shared.domain.query.result.Result;

/**
 * Persistence adapter for the answer entity.
 *
 * <p>
 * Implements the {@link AnswerRepository} interface, to be used by the domain
 * layer.
 * </p>
 */
@Slf4j
@PersistenceAdapter
@AllArgsConstructor
public final class AnswerPersistenceAdapter implements AnswerRepository {
    private final SpringAnswerRepository springAnswerRepository;
    private final CriteriaHibernateAnswerRepository criteriaHibernateAnswerRepository;
    private final AnswerMapper answerMapper;

    @Override
    public Result<Answer, AnswerFailure> save(Answer entity) {
        try {
            return Result.ok(answerMapper.toDomain(springAnswerRepository.save(answerMapper.toEntity(entity))));
        } catch (DataIntegrityViolationException e) {
            log.error("Error saving answer", e);

            return Result.failure(AnswerFailure.UNEXPECTED_ERROR);
        } catch (Exception e) {
            log.error("Error saving answer", e);

            return Result.failure(AnswerFailure.UNEXPECTED_ERROR);
        }
    }

    @Override
    public Optional<Answer> findById(Long id) {
        return springAnswerRepository.findById(id)
                .map(answerMapper::toDomain);
    }

    @Override
    public List<Answer> findAll() {
        return springAnswerRepository.findAll()
                .stream()
                .map(answerMapper::toDomain)
                .toList();
    }

    @Override
    public Result<Void, AnswerFailure> deleteById(Long id) {
        try {
            springAnswerRepository.deleteById(id);

            return Result.ok();
        } catch (Exception e) {
            log.error("Error deleting answer with id {}", id, e);

            return Result.failure(AnswerFailure.UNEXPECTED_ERROR);
        }
    }

    @Override
    public Paginated<Answer> match(Criteria criteria) {
        return criteriaHibernateAnswerRepository.match(criteria).map(answerMapper::toDomain);
    }

    @Override
    public Result<Answer, AnswerFailure> matchOne(Criteria criteria) {
        Optional<Answer> answer;
        try {
            answer = criteriaHibernateAnswerRepository.matchOne(criteria)
                    .map(answerMapper::toDomain);
        } catch (Exception e) {
            log.error("Error matching answer", e);

            return Result.failure(AnswerFailure.UNEXPECTED_ERROR);
        }

        if (answer.isEmpty()) {
            return Result.failure(AnswerFailure.NOT_FOUND);
        }

        return Result.ok(answer.get());
    }

    @Override
    public List<Answer> findAllByExamIdAndStudentId(Long examId, Long studentId) {
        return springAnswerRepository.findAllByQuestionExamIdAndStudentId(examId, studentId)
                .stream()
                .map(answerMapper::toDomain)
                .toList();
    }
}
