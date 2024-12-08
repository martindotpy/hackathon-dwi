package xyz.cupscoffee.hackathondwi.semester.core.application.usecase;

import static org.fusesource.jansi.Ansi.ansi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.cupscoffee.hackathondwi.auth.application.port.out.GetAuthenticatedUserPort;
import xyz.cupscoffee.hackathondwi.semester.core.application.dto.SemesterDto;
import xyz.cupscoffee.hackathondwi.semester.core.application.mapper.SemesterMapper;
import xyz.cupscoffee.hackathondwi.semester.core.application.port.in.UpdateSemesterPort;
import xyz.cupscoffee.hackathondwi.semester.core.domain.model.Semester;
import xyz.cupscoffee.hackathondwi.semester.core.domain.query.failure.SemesterFailure;
import xyz.cupscoffee.hackathondwi.semester.core.domain.query.payload.UpdateSemesterPayload;
import xyz.cupscoffee.hackathondwi.semester.core.domain.repository.SemesterRepository;
import xyz.cupscoffee.hackathondwi.shared.adapter.annotations.UseCase;
import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.Criteria;
import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.Filter;
import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.FilterOperator;
import xyz.cupscoffee.hackathondwi.shared.domain.query.result.Result;

/**
 * Update semester use case.
 */
@Slf4j
@UseCase
@RequiredArgsConstructor
public final class UpdateSemesterUseCase implements UpdateSemesterPort {
    private final SemesterRepository semesterRepository;
    private final SemesterMapper semesterMapper;
    private final GetAuthenticatedUserPort getAuthenticatedUserPort;

    @Override
    public Result<SemesterDto, SemesterFailure> update(UpdateSemesterPayload payload) {
        Long userId = getAuthenticatedUserPort.get().get().getId();

        var resultSemester = semesterRepository.matchOne(
                Criteria.ofMatchOne(
                        Filter.of("id", FilterOperator.EQUAL, payload.getId()),
                        Filter.of("user.id", FilterOperator.EQUAL, userId)));

        if (resultSemester.isFailure()) {
            return Result.failure(resultSemester.getFailure());
        }

        Semester semesterToUpdate = resultSemester.getSuccess();
        Semester updatedSemester = Semester.builder()
                .id(semesterToUpdate.getId())
                .name(payload.getName())
                .user(semesterToUpdate.getUser())
                .build();

        var result = semesterRepository.save(updatedSemester);

        if (result.isFailure()) {
            log.error("Failed to update semester with id {}: {}",
                    ansi().fgBrightRed().a(payload.getId()).reset().toString(),
                    result.getFailure().getMessage());

            return Result.failure(result.getFailure());
        }

        log.info("Successfully updated semester with id {}",
                ansi().fgBrightBlue().a(payload.getId()).reset());

        return Result.ok(semesterMapper.toDto(result.getSuccess()));
    }
}
