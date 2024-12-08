package xyz.cupscoffee.hackathondwi.exam.core.application.usecase;

import static org.fusesource.jansi.Ansi.ansi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.cupscoffee.hackathondwi.exam.core.application.dto.ExamDto;
import xyz.cupscoffee.hackathondwi.exam.core.application.mapper.ExamMapper;
import xyz.cupscoffee.hackathondwi.exam.core.application.port.in.FindExamPort;
import xyz.cupscoffee.hackathondwi.exam.core.domain.model.Exam;
import xyz.cupscoffee.hackathondwi.exam.core.domain.query.failure.ExamFailure;
import xyz.cupscoffee.hackathondwi.exam.core.domain.repository.ExamRepository;
import xyz.cupscoffee.hackathondwi.shared.adapter.annotations.UseCase;
import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.Criteria;
import xyz.cupscoffee.hackathondwi.shared.domain.query.paginated.Paginated;
import xyz.cupscoffee.hackathondwi.shared.domain.query.result.Result;

/**
 * Find exam use case.
 */
@Slf4j
@UseCase
@RequiredArgsConstructor
public final class FindExamUse implements FindExamPort {
    private final ExamRepository examRepository;
    private final ExamMapper examMapper;

    @Override
    public Paginated<ExamDto> match(Criteria criteria) {
        Paginated<Exam> exams = examRepository.match(criteria);

        log.info("Found {} exams",
                ansi().fgBrightBlue().a(exams.getContent().size()).reset());

        return exams.map(examMapper::toDto);
    }

    @Override
    public Result<ExamDto, ExamFailure> matchOne(Criteria criteria) {
        var result = examRepository.matchOne(criteria);

        if (result.isFailure()) {
            ExamFailure failure = result.getFailure();

            if (failure == ExamFailure.NOT_FOUND) {
                log.error("Exam not found");
            }

            return Result.failure(failure);
        }

        log.info("Found exam with id {}",
                ansi().fgBrightBlue().a(result.getSuccess().getId()).reset());

        return Result.ok(examMapper.toDto(result.getSuccess()));
    }
}
