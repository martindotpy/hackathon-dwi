package xyz.cupscoffee.hackathondwi.semester.core.application.usecase;

import static org.fusesource.jansi.Ansi.ansi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.cupscoffee.hackathondwi.semester.core.application.dto.SemesterDto;
import xyz.cupscoffee.hackathondwi.semester.core.application.mapper.SemesterMapper;
import xyz.cupscoffee.hackathondwi.semester.core.application.port.in.FindSemesterPort;
import xyz.cupscoffee.hackathondwi.semester.core.domain.model.Semester;
import xyz.cupscoffee.hackathondwi.semester.core.domain.query.failure.SemesterFailure;
import xyz.cupscoffee.hackathondwi.semester.core.domain.repository.SemesterRepository;
import xyz.cupscoffee.hackathondwi.shared.adapter.annotations.UseCase;
import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.Criteria;
import xyz.cupscoffee.hackathondwi.shared.domain.query.paginated.Paginated;
import xyz.cupscoffee.hackathondwi.shared.domain.query.result.Result;

/**
 * Find semester use case.
 */
@Slf4j
@UseCase
@RequiredArgsConstructor
public final class FindSemesterUse implements FindSemesterPort {
    private final SemesterRepository semesterRepository;
    private final SemesterMapper semesterMapper;

    @Override
    public Paginated<SemesterDto> match(Criteria criteria) {
        Paginated<Semester> semesters = semesterRepository.match(criteria);

        log.info("Found {} semesters",
                ansi().fgBrightBlue().a(semesters.getContent().size()).reset());

        return semesters.map(semesterMapper::toDto);
    }

    @Override
    public Result<SemesterDto, SemesterFailure> matchOne(Criteria criteria) {
        var result = semesterRepository.matchOne(criteria);

        if (result.isFailure()) {
            SemesterFailure failure = result.getFailure();

            if (failure == SemesterFailure.NOT_FOUND) {
                log.error("Semester not found");
            }

            return Result.failure(failure);
        }

        log.info("Found semester with id {}",
                ansi().fgBrightBlue().a(result.getSuccess().getId()).reset());

        return Result.ok(semesterMapper.toDto(result.getSuccess()));
    }
}
