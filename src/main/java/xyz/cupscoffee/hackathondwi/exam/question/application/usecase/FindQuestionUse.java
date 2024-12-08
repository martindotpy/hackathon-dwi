package xyz.cupscoffee.hackathondwi.exam.question.application.usecase;

import static org.fusesource.jansi.Ansi.ansi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.cupscoffee.hackathondwi.exam.question.application.dto.QuestionDto;
import xyz.cupscoffee.hackathondwi.exam.question.application.mapper.QuestionMapper;
import xyz.cupscoffee.hackathondwi.exam.question.application.port.in.FindQuestionPort;
import xyz.cupscoffee.hackathondwi.exam.question.domain.model.Question;
import xyz.cupscoffee.hackathondwi.exam.question.domain.query.failure.QuestionFailure;
import xyz.cupscoffee.hackathondwi.exam.question.domain.repository.QuestionRepository;
import xyz.cupscoffee.hackathondwi.shared.adapter.annotations.UseCase;
import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.Criteria;
import xyz.cupscoffee.hackathondwi.shared.domain.query.paginated.Paginated;
import xyz.cupscoffee.hackathondwi.shared.domain.query.result.Result;

/**
 * Find question use case.
 */
@Slf4j
@UseCase
@RequiredArgsConstructor
public final class FindQuestionUse implements FindQuestionPort {
    private final QuestionRepository questionRepository;
    private final QuestionMapper questionMapper;

    @Override
    public Paginated<QuestionDto> match(Criteria criteria) {
        Paginated<Question> questions = questionRepository.match(criteria);

        log.info("Found {} questions",
                ansi().fgBrightBlue().a(questions.getContent().size()).reset());

        return questions.map(questionMapper::toDto);
    }

    @Override
    public Result<QuestionDto, QuestionFailure> matchOne(Criteria criteria) {
        var result = questionRepository.matchOne(criteria);

        if (result.isFailure()) {
            QuestionFailure failure = result.getFailure();

            if (failure == QuestionFailure.NOT_FOUND) {
                log.error("Question not found");
            }

            return Result.failure(failure);
        }

        log.info("Found question with id {}",
                ansi().fgBrightBlue().a(result.getSuccess().getId()).reset());

        return Result.ok(questionMapper.toDto(result.getSuccess()));
    }
}
