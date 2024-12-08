package xyz.cupscoffee.hackathondwi.exam.core.application.usecase;

import static org.fusesource.jansi.Ansi.ansi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.cupscoffee.hackathondwi.auth.application.port.out.GetAuthenticatedUserPort;
import xyz.cupscoffee.hackathondwi.exam.core.application.port.in.DeleteExamPort;
import xyz.cupscoffee.hackathondwi.exam.core.domain.query.failure.ExamFailure;
import xyz.cupscoffee.hackathondwi.exam.core.domain.repository.ExamRepository;
import xyz.cupscoffee.hackathondwi.shared.adapter.annotations.UseCase;
import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.Criteria;
import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.Filter;
import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.FilterOperator;
import xyz.cupscoffee.hackathondwi.shared.domain.query.result.Result;

/**
 * Delete exam use case.
 */
@Slf4j
@UseCase
@RequiredArgsConstructor
public final class DeleteExamUseCase implements DeleteExamPort {
    private final ExamRepository examRepository;
    private final GetAuthenticatedUserPort getAuthenticatedUserPort;

    @Override
    public Result<Void, ExamFailure> deleteById(Long id) {
        log.debug("Deleting exam with id {}", id);

        Long userId = getAuthenticatedUserPort.get().get().getId();

        var resultExam = examRepository.matchOne(
                Criteria.ofMatchOne(
                        Filter.of("id", FilterOperator.EQUAL, id),
                        Filter.of("course.semester.user.id", FilterOperator.EQUAL, userId)));

        if (resultExam.isFailure()) {
            return Result.failure(resultExam.getFailure());
        }

        var result = examRepository.deleteById(id);

        if (result.isFailure()) {
            log.error("Failed to delete exam with id {}: {}",
                    ansi().fgBrightRed().a(id).reset().toString(),
                    result.getFailure().getMessage());

            return Result.failure(result.getFailure());
        }

        log.info("Successfully deleted exam with id {}",
                ansi().fgBrightBlue().a(id).reset().toString());

        return Result.ok();
    }
}
