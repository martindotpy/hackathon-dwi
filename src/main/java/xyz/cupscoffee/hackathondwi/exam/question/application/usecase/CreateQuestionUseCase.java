package xyz.cupscoffee.hackathondwi.exam.question.application.usecase;

import static org.fusesource.jansi.Ansi.ansi;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.cupscoffee.hackathondwi.auth.application.port.out.GetAuthenticatedUserPort;
import xyz.cupscoffee.hackathondwi.exam.core.domain.model.Exam;
import xyz.cupscoffee.hackathondwi.exam.core.domain.query.failure.ExamFailure;
import xyz.cupscoffee.hackathondwi.exam.core.domain.repository.ExamRepository;
import xyz.cupscoffee.hackathondwi.exam.question.application.dto.QuestionDto;
import xyz.cupscoffee.hackathondwi.exam.question.application.mapper.QuestionMapper;
import xyz.cupscoffee.hackathondwi.exam.question.application.port.in.CreateQuestionPort;
import xyz.cupscoffee.hackathondwi.exam.question.domain.model.Question;
import xyz.cupscoffee.hackathondwi.exam.question.domain.query.failure.QuestionFailure;
import xyz.cupscoffee.hackathondwi.exam.question.domain.query.payload.CreateQuestionPayload;
import xyz.cupscoffee.hackathondwi.exam.question.domain.repository.QuestionRepository;
import xyz.cupscoffee.hackathondwi.shared.adapter.annotations.UseCase;
import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.Criteria;
import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.Filter;
import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.FilterOperator;
import xyz.cupscoffee.hackathondwi.shared.domain.query.result.Result;

/**
 * Create question use case.
 */
@Slf4j
@UseCase
@RequiredArgsConstructor
public final class CreateQuestionUseCase implements CreateQuestionPort {
    private final QuestionRepository questionRepository;
    private final ExamRepository examRepository;
    private final GetAuthenticatedUserPort getAuthenticatedUserPort;
    private final QuestionMapper questionMapper;

    @Override
    public Result<QuestionDto, QuestionFailure> create(CreateQuestionPayload payload) {
        Long userId = getAuthenticatedUserPort.get().get().getId();

        log.debug("Creating question with user id {}",
                ansi().fgBrightBlue().a(userId).reset());

        var resultExam = examRepository.matchOne(
                Criteria.ofMatchOne(
                        Filter.of("id", FilterOperator.EQUAL, payload.getExamId()),
                        Filter.of("course.semester.user.id", FilterOperator.EQUAL, userId)));

        if (resultExam.isFailure()) {
            ExamFailure failure = resultExam.getFailure();

            if (failure == ExamFailure.NOT_FOUND) {
                log.error("Failed to create question: {}",
                        ansi().fgBrightRed().a(failure.getMessage()));

                return Result.failure(QuestionFailure.EXAM_NOT_FOUND);
            }

            return Result.failure(QuestionFailure.UNEXPECTED_ERROR);
        }

        Exam exam = resultExam.getSuccess();
        Integer maxValue = payload.getMaxValue();
        List<Question> questions = questionRepository.findAllByExamId(exam.getId());
        Integer examMaxValue = questions.stream()
                .map(Question::getMaxValue)
                .reduce(0, Integer::sum);

        if (examMaxValue + maxValue > 20) {
            return Result.failure(QuestionFailure.CANNOT_OVERFLOW_MAX_VALUE);
        }

        if (questions != null)
            if (questions.stream().anyMatch(q -> q.getOrder().equals(payload.getOrder()))) {
                return Result.failure(QuestionFailure.CANNOT_OVERLAP_ORDER);
            }

        Question newQuestion = Question.builder()
                .order(payload.getOrder())
                .maxValue(payload.getMaxValue())
                .exam(exam)
                .build();

        var result = questionRepository.save(newQuestion);

        if (result.isFailure()) {
            log.error("Failed to create question: {}",
                    ansi().fgBrightRed().a(result.getFailure().getMessage()));

            return Result.failure(result.getFailure());
        }

        log.info("Successfully created question with id {}",
                ansi().fgBrightBlue().a(result.getSuccess().getId()).reset());

        return Result.ok(questionMapper.toDto(result.getSuccess()));
    }
}
