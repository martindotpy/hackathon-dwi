package xyz.cupscoffee.hackathondwi.semester.core.application.usecase;

import static org.fusesource.jansi.Ansi.ansi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.cupscoffee.hackathondwi.semester.core.application.dto.SemesterDto;
import xyz.cupscoffee.hackathondwi.semester.core.application.mapper.SemesterMapper;
import xyz.cupscoffee.hackathondwi.semester.core.application.port.in.CreateSemesterPort;
import xyz.cupscoffee.hackathondwi.semester.core.domain.model.Semester;
import xyz.cupscoffee.hackathondwi.semester.core.domain.query.failure.SemesterFailure;
import xyz.cupscoffee.hackathondwi.semester.core.domain.query.payload.CreateSemesterPayload;
import xyz.cupscoffee.hackathondwi.semester.core.domain.repository.SemesterRepository;
import xyz.cupscoffee.hackathondwi.shared.adapter.annotations.UseCase;
import xyz.cupscoffee.hackathondwi.shared.domain.query.result.Result;
import xyz.cupscoffee.hackathondwi.user.core.domain.model.User;

/**
 * Create semester use case.
 */
@Slf4j
@UseCase
@RequiredArgsConstructor
public final class CreateSemesterUseCase implements CreateSemesterPort {
    private final SemesterRepository semesterRepository;
    private final SemesterMapper semesterMapper;

    @Override
    public Result<SemesterDto, SemesterFailure> create(CreateSemesterPayload payload, Long userId) {
        log.debug("Creating semester with user id {}",
                ansi().fgBrightBlue().a(userId).reset());

        Semester newSemester = Semester.builder()
                .name(payload.getName())
                .user(User.builder().id(userId).build())
                .build();

        var result = semesterRepository.save(newSemester);

        if (result.isFailure()) {
            log.error("Failed to create semester: {}",
                    ansi().fgBrightRed().a(result.getFailure().getMessage()));

            return Result.failure(result.getFailure());
        }

        log.info("Successfully created semester with id {}",
                ansi().fgBrightBlue().a(result.getSuccess().getId()).reset());

        return Result.ok(semesterMapper.toDto(result.getSuccess()));
    }
}
