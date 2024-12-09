package xyz.cupscoffee.hackathondwi.exam.question.application.usecase;

import static org.fusesource.jansi.Ansi.ansi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.cupscoffee.hackathondwi.auth.application.port.out.GetAuthenticatedUserPort;
import xyz.cupscoffee.hackathondwi.exam.core.domain.model.Exam;
import xyz.cupscoffee.hackathondwi.exam.question.application.dto.QuestionDto;
import xyz.cupscoffee.hackathondwi.exam.question.application.mapper.QuestionMapper;
import xyz.cupscoffee.hackathondwi.exam.question.application.port.in.UpdateQuestionPort;
import xyz.cupscoffee.hackathondwi.exam.question.domain.model.Question;
import xyz.cupscoffee.hackathondwi.exam.question.domain.query.failure.QuestionFailure;
import xyz.cupscoffee.hackathondwi.exam.question.domain.query.payload.UpdateQuestionPayload;
import xyz.cupscoffee.hackathondwi.exam.question.domain.repository.QuestionRepository;
import xyz.cupscoffee.hackathondwi.shared.adapter.annotations.UseCase;
import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.Criteria;
import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.Filter;
import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.FilterOperator;
import xyz.cupscoffee.hackathondwi.shared.domain.query.result.Result;

/**
 * Update question use case.
 */
@Slf4j
@UseCase
@RequiredArgsConstructor
public final class UpdateQuestionUseCase implements UpdateQuestionPort {
    private final QuestionRepository questionRepository;
    private final QuestionMapper questionMapper;
    private final GetAuthenticatedUserPort getAuthenticatedUserPort;

    @Override
    public Result<QuestionDto, QuestionFailure> update(UpdateQuestionPayload payload) {
        Long userId = getAuthenticatedUserPort.get().get().getId();

        var resultQuestion = questionRepository.matchOne(
                Criteria.ofMatchOne(
                        Filter.of("id", FilterOperator.EQUAL, payload.getId()),
                        Filter.of("exam.course.semester.user.id", FilterOperator.EQUAL, userId)));

        if (resultQuestion.isFailure()) {
            return Result.failure(resultQuestion.getFailure());
        }

        Question questionToUpdate = resultQuestion.getSuccess();
        Exam exam = questionToUpdate.getExam();
        var questions = questionRepository.findAllByExamId(exam.getId()).stream()
                .filter(q -> !q.getId().equals(questionToUpdate.getId()))
                .toList();
        Integer maxValue = payload.getMaxValue();
        Integer examMaxValue = questions.stream()
                .map(Question::getMaxValue)
                .reduce(0, Integer::sum);

        if (examMaxValue + maxValue > 20) {
            return Result.failure(QuestionFailure.CANNOT_OVERFLOW_MAX_VALUE);
        }

        if (questions.stream().anyMatch(q -> q.getOrder().equals(payload.getOrder()))) {
            return Result.failure(QuestionFailure.CANNOT_OVERLAP_ORDER);
        }

        Question updatedQuestion = Question.builder()
                .id(questionToUpdate.getId())
                .order(payload.getOrder())
                .maxValue(payload.getMaxValue())
                .exam(exam)
                .build();

        var result = questionRepository.save(updatedQuestion);

        if (result.isFailure()) {
            log.error("Failed to update question with id {}: {}",
                    ansi().fgBrightRed().a(payload.getId()).reset().toString(),
                    result.getFailure().getMessage());

            return Result.failure(result.getFailure());
        }

        log.info("Successfully updated question with id {}",
                ansi().fgBrightBlue().a(payload.getId()).reset());

        return Result.ok(questionMapper.toDto(result.getSuccess()));
    }
}
