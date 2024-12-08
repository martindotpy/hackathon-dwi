package xyz.cupscoffee.hackathondwi.exam.answer.application.usecase;

import static org.fusesource.jansi.Ansi.ansi;

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
}
