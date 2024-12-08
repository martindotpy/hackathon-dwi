package xyz.cupscoffee.hackathondwi.student.core.application.usecase;

import static org.fusesource.jansi.Ansi.ansi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.cupscoffee.hackathondwi.auth.application.port.out.GetAuthenticatedUserPort;
import xyz.cupscoffee.hackathondwi.shared.adapter.annotations.UseCase;
import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.Criteria;
import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.Filter;
import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.FilterOperator;
import xyz.cupscoffee.hackathondwi.shared.domain.query.result.Result;
import xyz.cupscoffee.hackathondwi.student.core.application.port.in.DeleteStudentPort;
import xyz.cupscoffee.hackathondwi.student.core.domain.query.failure.StudentFailure;
import xyz.cupscoffee.hackathondwi.student.core.domain.repository.StudentRepository;

/**
 * Delete student use case.
 */
@Slf4j
@UseCase
@RequiredArgsConstructor
public final class DeleteStudentUseCase implements DeleteStudentPort {
    private final StudentRepository studentRepository;
    private final GetAuthenticatedUserPort getAuthenticatedUserPort;

    @Override
    public Result<Void, StudentFailure> deleteById(Long id) {
        log.debug("Deleting student with id {}", id);

        Long userId = getAuthenticatedUserPort.get().get().getId();

        var resultStudent = studentRepository.matchOne(
                Criteria.ofMatchOne(
                        Filter.of("id", FilterOperator.EQUAL, id),
                        Filter.of("course.semester.user.id", FilterOperator.EQUAL, userId)));

        if (resultStudent.isFailure()) {
            log.error("Failed to delete student: {}",
                    ansi().fgBrightRed().a(resultStudent.getFailure().getMessage()));

            return Result.failure(resultStudent.getFailure());
        }

        var result = studentRepository.deleteById(id);

        if (result.isFailure()) {
            log.error("Failed to delete student with id {}: {}",
                    ansi().fgBrightRed().a(id).reset().toString(),
                    result.getFailure().getMessage());

            return Result.failure(result.getFailure());
        }

        log.info("Successfully deleted student with id {}",
                ansi().fgBrightBlue().a(id).reset().toString());

        return Result.ok();
    }
}
