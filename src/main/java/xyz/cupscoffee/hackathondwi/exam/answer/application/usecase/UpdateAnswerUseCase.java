package xyz.cupscoffee.hackathondwi.exam.answer.application.usecase;

import static org.fusesource.jansi.Ansi.ansi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.cupscoffee.hackathondwi.auth.application.port.out.GetAuthenticatedUserPort;
import xyz.cupscoffee.hackathondwi.exam.answer.application.dto.AnswerDto;
import xyz.cupscoffee.hackathondwi.exam.answer.application.mapper.AnswerMapper;
import xyz.cupscoffee.hackathondwi.exam.answer.application.port.in.UpdateAnswerPort;
import xyz.cupscoffee.hackathondwi.exam.answer.domain.model.Answer;
import xyz.cupscoffee.hackathondwi.exam.answer.domain.query.failure.AnswerFailure;
import xyz.cupscoffee.hackathondwi.exam.answer.domain.query.payload.UpdateAnswerPayload;
import xyz.cupscoffee.hackathondwi.exam.answer.domain.repository.AnswerRepository;
import xyz.cupscoffee.hackathondwi.exam.question.domain.model.Question;
import xyz.cupscoffee.hackathondwi.shared.adapter.annotations.UseCase;
import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.Criteria;
import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.Filter;
import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.FilterOperator;
import xyz.cupscoffee.hackathondwi.shared.domain.query.result.Result;

/**
 * Update answer use case.
 */
@Slf4j
@UseCase
@RequiredArgsConstructor
public final class UpdateAnswerUseCase implements UpdateAnswerPort {
    private final AnswerRepository answerRepository;
    private final AnswerMapper answerMapper;
    private final GetAuthenticatedUserPort getAuthenticatedUserPort;

    @Override
    public Result<AnswerDto, AnswerFailure> update(UpdateAnswerPayload payload) {
        // Get the answer
        Long userId = getAuthenticatedUserPort.get().get().getId();

        log.debug("Updating answer with id {}",
                ansi().fgBrightBlue().a(payload.getId()).reset());

        var resultAnswer = answerRepository.matchOne(
                Criteria.ofMatchOne(
                        Filter.of("id", FilterOperator.EQUAL, payload.getId()),
                        Filter.of("question.exam.course.semester.user.id", FilterOperator.EQUAL, userId)));

        if (resultAnswer.isFailure()) {
            return Result.failure(resultAnswer.getFailure());
        }

        Answer answerToUpdate = resultAnswer.getSuccess();
        Integer newValue = payload.getValue();

        // Verify that the answer is not greater than the maximum value
        Question question = answerToUpdate.getQuestion();

        if (question.getMaxValue() < newValue) {
            log.error("Failed to update answer with id {}: {}",
                    ansi().fgBrightRed().a(payload.getId()).reset().toString(),
                    AnswerFailure.CANNOT_OVERFLOW_MAX_VALUE);

            return Result.failure(AnswerFailure.CANNOT_OVERFLOW_MAX_VALUE);
        }

        // Update the answer
        Answer updatedAnswer = Answer.builder()
                .id(answerToUpdate.getId())
                .value(newValue)
                .question(question)
                .student(answerToUpdate.getStudent())
                .build();

        var result = answerRepository.save(updatedAnswer);

        if (result.isFailure()) {
            log.error("Failed to update answer with id {}: {}",
                    ansi().fgBrightRed().a(payload.getId()).reset().toString(),
                    result.getFailure());

            return Result.failure(result.getFailure());
        }

        log.info("Successfully updated answer with id {}",
                ansi().fgBrightBlue().a(payload.getId()).reset());

        return Result.ok(answerMapper.toDto(result.getSuccess()));
    }
}
