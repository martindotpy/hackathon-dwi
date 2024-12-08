package xyz.cupscoffee.hackathondwi.exam.answer.application.usecase;

import static org.fusesource.jansi.Ansi.ansi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.cupscoffee.hackathondwi.auth.application.port.out.GetAuthenticatedUserPort;
import xyz.cupscoffee.hackathondwi.exam.answer.application.port.in.DeleteAnswerPort;
import xyz.cupscoffee.hackathondwi.exam.answer.domain.query.failure.AnswerFailure;
import xyz.cupscoffee.hackathondwi.exam.answer.domain.repository.AnswerRepository;
import xyz.cupscoffee.hackathondwi.shared.adapter.annotations.UseCase;
import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.Criteria;
import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.Filter;
import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.FilterOperator;
import xyz.cupscoffee.hackathondwi.shared.domain.query.result.Result;

/**
 * Delete answer use case.
 */
@Slf4j
@UseCase
@RequiredArgsConstructor
public final class DeleteAnswerUseCase implements DeleteAnswerPort {
    private final AnswerRepository answerRepository;
    private final GetAuthenticatedUserPort getAuthenticatedUserPort;

    @Override
    public Result<Void, AnswerFailure> deleteById(Long id) {
        log.debug("Deleting answer with id {}", id);

        Long userId = getAuthenticatedUserPort.get().get().getId();

        var resultAnswer = answerRepository.matchOne(
                Criteria.ofMatchOne(
                        Filter.of("id", FilterOperator.EQUAL, id),
                        Filter.of("question.exam.course.semester.user.id", FilterOperator.EQUAL, userId)));

        if (resultAnswer.isFailure()) {
            return Result.failure(resultAnswer.getFailure());
        }

        var result = answerRepository.deleteById(id);

        if (result.isFailure()) {
            log.error("Failed to delete answer with id {}: {}",
                    ansi().fgBrightRed().a(id).reset().toString(),
                    result.getFailure().getMessage());

            return Result.failure(result.getFailure());
        }

        log.info("Successfully deleted answer with id {}",
                ansi().fgBrightBlue().a(id).reset().toString());

        return Result.ok();
    }
}
