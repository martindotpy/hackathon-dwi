package xyz.cupscoffee.hackathondwi.exam.question.adapter.out.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.cupscoffee.hackathondwi.exam.question.adapter.out.persistence.repository.CriteriaHibernateQuestionRepository;
import xyz.cupscoffee.hackathondwi.exam.question.adapter.out.persistence.repository.SpringQuestionRepository;
import xyz.cupscoffee.hackathondwi.exam.question.application.mapper.QuestionMapper;
import xyz.cupscoffee.hackathondwi.exam.question.domain.model.Question;
import xyz.cupscoffee.hackathondwi.exam.question.domain.query.failure.QuestionFailure;
import xyz.cupscoffee.hackathondwi.exam.question.domain.repository.QuestionRepository;
import xyz.cupscoffee.hackathondwi.shared.adapter.annotations.PersistenceAdapter;
import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.Criteria;
import xyz.cupscoffee.hackathondwi.shared.domain.query.paginated.Paginated;
import xyz.cupscoffee.hackathondwi.shared.domain.query.result.Result;

/**
 * Persistence adapter for the question entity.
 *
 * <p>
 * Implements the {@link QuestionRepository} interface, to be used by the domain
 * layer.
 * </p>
 */
@Slf4j
@PersistenceAdapter
@AllArgsConstructor
public final class QuestionPersistenceAdapter implements QuestionRepository {
    private final SpringQuestionRepository springQuestionRepository;
    private final CriteriaHibernateQuestionRepository criteriaHibernateQuestionRepository;
    private final QuestionMapper questionMapper;

    @Override
    public Result<Question, QuestionFailure> save(Question entity) {
        try {
            return Result.ok(questionMapper.toDomain(springQuestionRepository.save(questionMapper.toEntity(entity))));
        } catch (DataIntegrityViolationException e) {
            log.error("Error saving question", e);

            return Result.failure(QuestionFailure.UNEXPECTED_ERROR);
        } catch (Exception e) {
            log.error("Error saving question", e);

            return Result.failure(QuestionFailure.UNEXPECTED_ERROR);
        }
    }

    @Override
    public Optional<Question> findById(Long id) {
        return springQuestionRepository.findById(id)
                .map(questionMapper::toDomain);
    }

    @Override
    public List<Question> findAll() {
        return springQuestionRepository.findAll()
                .stream()
                .map(questionMapper::toDomain)
                .toList();
    }

    @Override
    public Result<Void, QuestionFailure> deleteById(Long id) {
        try {
            springQuestionRepository.deleteById(id);

            return Result.ok();
        } catch (Exception e) {
            log.error("Error deleting question with id {}", id, e);

            return Result.failure(QuestionFailure.UNEXPECTED_ERROR);
        }
    }

    @Override
    public Paginated<Question> match(Criteria criteria) {
        return criteriaHibernateQuestionRepository.match(criteria).map(questionMapper::toDomain);
    }

    @Override
    public Result<Question, QuestionFailure> matchOne(Criteria criteria) {
        Optional<Question> question;
        try {
            question = criteriaHibernateQuestionRepository.matchOne(criteria)
                    .map(questionMapper::toDomain);
        } catch (Exception e) {
            log.error("Error matching question", e);

            return Result.failure(QuestionFailure.UNEXPECTED_ERROR);
        }

        if (question.isEmpty()) {
            return Result.failure(QuestionFailure.NOT_FOUND);
        }

        return Result.ok(question.get());
    }

    @Override
    public List<Question> findAllByExamId(Long examId) {
        return springQuestionRepository.findAllByExamId(examId)
                .stream()
                .map(questionMapper::toDomain)
                .toList();
    }
}
