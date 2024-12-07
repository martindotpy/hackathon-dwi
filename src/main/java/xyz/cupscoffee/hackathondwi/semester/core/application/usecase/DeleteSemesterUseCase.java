package xyz.cupscoffee.hackathondwi.semester.core.application.usecase;

import static org.fusesource.jansi.Ansi.ansi;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.cupscoffee.hackathondwi.auth.application.port.out.GetAuthenticatedUserPort;
import xyz.cupscoffee.hackathondwi.semester.core.application.port.in.DeleteSemesterPort;
import xyz.cupscoffee.hackathondwi.semester.core.domain.model.Semester;
import xyz.cupscoffee.hackathondwi.semester.core.domain.query.failure.SemesterFailure;
import xyz.cupscoffee.hackathondwi.semester.core.domain.repository.SemesterRepository;
import xyz.cupscoffee.hackathondwi.shared.adapter.annotations.UseCase;
import xyz.cupscoffee.hackathondwi.shared.domain.query.result.Result;

/**
 * Delete semester use case.
 */
@Slf4j
@UseCase
@RequiredArgsConstructor
public final class DeleteSemesterUseCase implements DeleteSemesterPort {
    private final SemesterRepository semesterRepository;
    private final GetAuthenticatedUserPort getAuthenticatedUserPort;

    @Override
    public Result<Void, SemesterFailure> deleteById(Long id) {
        log.debug("Deleting semester with id {}", id);

        Optional<Semester> semester = semesterRepository.findById(id);

        if (semester.isEmpty()) {
            log.error("Semester with id {} not found",
                    ansi().fgBrightRed().a(id).reset());

            return Result.failure(SemesterFailure.NOT_FOUND);
        }

        Semester semesterToDelete = semester.get();

        if (!getAuthenticatedUserPort.get().get().getId().equals(semesterToDelete.getUser().getId())) {
            log.error("Semester with id {} does not belong to the authenticated user",
                    ansi().fgBrightRed().a(id).reset());

            return Result.failure(SemesterFailure.NOT_FOUND);
        }

        var result = semesterRepository.deleteById(id);

        if (result.isFailure()) {
            log.error("Failed to delete semester with id {}: {}",
                    ansi().fgBrightRed().a(id).reset().toString(),
                    result.getFailure().getMessage());

            return Result.failure(result.getFailure());
        }

        log.info("Successfully deleted semester with id {}",
                ansi().fgBrightBlue().a(id).reset().toString());

        return Result.ok();
    }
}
