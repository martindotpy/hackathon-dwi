package xyz.cupscoffee.hackathondwi.exam.answer.application.usecase;

import static org.fusesource.jansi.Ansi.ansi;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.cupscoffee.hackathondwi.exam.answer.application.dto.AnswerDto;
import xyz.cupscoffee.hackathondwi.exam.answer.application.mapper.AnswerMapper;
import xyz.cupscoffee.hackathondwi.exam.answer.application.port.in.FindAnswerPort;
import xyz.cupscoffee.hackathondwi.exam.answer.domain.model.Answer;
import xyz.cupscoffee.hackathondwi.exam.answer.domain.query.failure.AnswerFailure;
import xyz.cupscoffee.hackathondwi.exam.answer.domain.repository.AnswerRepository;
import xyz.cupscoffee.hackathondwi.shared.adapter.annotations.UseCase;
import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.Criteria;
import xyz.cupscoffee.hackathondwi.shared.domain.query.paginated.Paginated;
import xyz.cupscoffee.hackathondwi.shared.domain.query.result.Result;

/**
 * Find answer use case.
 */
@Slf4j
@UseCase
@RequiredArgsConstructor
public final class FindAnswerUse implements FindAnswerPort {
    private final AnswerRepository answerRepository;
    private final AnswerMapper answerMapper;

    @Override
    public Paginated<AnswerDto> match(Criteria criteria) {
        Paginated<Answer> answers = answerRepository.match(criteria);

        log.info("Found {} answers",
                ansi().fgBrightBlue().a(answers.getContent().size()).reset());

        return answers.map(answerMapper::toDto);
    }

    @Override
    public Result<AnswerDto, AnswerFailure> matchOne(Criteria criteria) {
        var result = answerRepository.matchOne(criteria);

        if (result.isFailure()) {
            AnswerFailure failure = result.getFailure();

            if (failure == AnswerFailure.NOT_FOUND) {
                log.error("Answer not found");
            }

            return Result.failure(failure);
        }

        log.info("Found answer with id {}",
                ansi().fgBrightBlue().a(result.getSuccess().getId()).reset());

        return Result.ok(answerMapper.toDto(result.getSuccess()));
    }

    @Override
    public List<AnswerDto> findAllByExamIdAndStudentId(Long examId, Long studentId) {
        var answers = answerRepository.findAllByExamIdAndStudentId(examId, studentId);

        log.info("Found {} answers for exam with id {} and student with id {}",
                ansi().fgBrightBlue().a(answers.size()).reset(),
                ansi().fgBrightBlue().a(examId).reset(),
                ansi().fgBrightBlue().a(studentId).reset());

        return answers.stream()
                .map(answerMapper::toDto)
                .toList();
    }
}
