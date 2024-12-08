package xyz.cupscoffee.hackathondwi.exam.core.application.usecase;

import static org.fusesource.jansi.Ansi.ansi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.cupscoffee.hackathondwi.auth.application.port.out.GetAuthenticatedUserPort;
import xyz.cupscoffee.hackathondwi.exam.core.application.dto.ExamDto;
import xyz.cupscoffee.hackathondwi.exam.core.application.mapper.ExamMapper;
import xyz.cupscoffee.hackathondwi.exam.core.application.port.in.UpdateExamPort;
import xyz.cupscoffee.hackathondwi.exam.core.domain.model.Exam;
import xyz.cupscoffee.hackathondwi.exam.core.domain.query.failure.ExamFailure;
import xyz.cupscoffee.hackathondwi.exam.core.domain.query.payload.UpdateExamPayload;
import xyz.cupscoffee.hackathondwi.exam.core.domain.repository.ExamRepository;
import xyz.cupscoffee.hackathondwi.shared.adapter.annotations.UseCase;
import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.Criteria;
import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.Filter;
import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.FilterOperator;
import xyz.cupscoffee.hackathondwi.shared.domain.query.result.Result;

/**
 * Update exam use case.
 */
@Slf4j
@UseCase
@RequiredArgsConstructor
public final class UpdateExamUseCase implements UpdateExamPort {
    private final ExamRepository examRepository;
    private final ExamMapper examMapper;
    private final GetAuthenticatedUserPort getAuthenticatedUserPort;

    @Override
    public Result<ExamDto, ExamFailure> update(UpdateExamPayload payload) {
        Long userId = getAuthenticatedUserPort.get().get().getId();

        var resultExam = examRepository.matchOne(
                Criteria.ofMatchOne(
                        Filter.of("id", FilterOperator.EQUAL, payload.getId()),
                        Filter.of("course.semester.user.id", FilterOperator.EQUAL, userId)));

        if (resultExam.isFailure()) {
            return Result.failure(resultExam.getFailure());
        }

        Exam examToUpdate = resultExam.getSuccess();

        Exam updatedExam = Exam.builder()
                .id(examToUpdate.getId())
                .name(payload.getName())
                .course(examToUpdate.getCourse())
                .build();

        var result = examRepository.save(updatedExam);

        if (result.isFailure()) {
            log.error("Failed to update exam with id {}: {}",
                    ansi().fgBrightRed().a(payload.getId()).reset().toString(),
                    result.getFailure().getMessage());

            return Result.failure(result.getFailure());
        }

        log.info("Successfully updated exam with id {}",
                ansi().fgBrightBlue().a(payload.getId()).reset());

        return Result.ok(examMapper.toDto(result.getSuccess()));
    }
}
