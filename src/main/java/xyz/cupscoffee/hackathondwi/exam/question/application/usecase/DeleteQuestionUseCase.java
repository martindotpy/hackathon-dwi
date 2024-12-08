package xyz.cupscoffee.hackathondwi.exam.question.application.usecase;

import static org.fusesource.jansi.Ansi.ansi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.cupscoffee.hackathondwi.auth.application.port.out.GetAuthenticatedUserPort;
import xyz.cupscoffee.hackathondwi.exam.question.application.port.in.DeleteQuestionPort;
import xyz.cupscoffee.hackathondwi.exam.question.domain.query.failure.QuestionFailure;
import xyz.cupscoffee.hackathondwi.exam.question.domain.repository.QuestionRepository;
import xyz.cupscoffee.hackathondwi.shared.adapter.annotations.UseCase;
import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.Criteria;
import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.Filter;
import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.FilterOperator;
import xyz.cupscoffee.hackathondwi.shared.domain.query.result.Result;

/**
 * Delete question use case.
 */
@Slf4j
@UseCase
@RequiredArgsConstructor
public final class DeleteQuestionUseCase implements DeleteQuestionPort {
    private final QuestionRepository questionRepository;
    private final GetAuthenticatedUserPort getAuthenticatedUserPort;

    @Override
    public Result<Void, QuestionFailure> deleteById(Long id) {
        log.debug("Deleting question with id {}", id);

        Long userId = getAuthenticatedUserPort.get().get().getId();

        var resultQuestion = questionRepository.matchOne(
                Criteria.ofMatchOne(
                        Filter.of("id", FilterOperator.EQUAL, id),
                        Filter.of("exam.course.semester.user.id", FilterOperator.EQUAL, userId)));

        if (resultQuestion.isFailure()) {
            return Result.failure(resultQuestion.getFailure());
        }

        var result = questionRepository.deleteById(id);

        if (result.isFailure()) {
            log.error("Failed to delete question with id {}: {}",
                    ansi().fgBrightRed().a(id).reset().toString(),
                    result.getFailure().getMessage());

            return Result.failure(result.getFailure());
        }

        log.info("Successfully deleted question with id {}",
                ansi().fgBrightBlue().a(id).reset().toString());

        return Result.ok();
    }
}
