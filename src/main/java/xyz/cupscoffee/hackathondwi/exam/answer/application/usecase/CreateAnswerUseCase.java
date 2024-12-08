package xyz.cupscoffee.hackathondwi.exam.answer.application.usecase;

import static org.fusesource.jansi.Ansi.ansi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.cupscoffee.hackathondwi.auth.application.port.out.GetAuthenticatedUserPort;
import xyz.cupscoffee.hackathondwi.exam.answer.application.dto.AnswerDto;
import xyz.cupscoffee.hackathondwi.exam.answer.application.mapper.AnswerMapper;
import xyz.cupscoffee.hackathondwi.exam.answer.application.port.in.CreateAnswerPort;
import xyz.cupscoffee.hackathondwi.exam.answer.domain.model.Answer;
import xyz.cupscoffee.hackathondwi.exam.answer.domain.query.failure.AnswerFailure;
import xyz.cupscoffee.hackathondwi.exam.answer.domain.query.payload.CreateAnswerPayload;
import xyz.cupscoffee.hackathondwi.exam.answer.domain.repository.AnswerRepository;
import xyz.cupscoffee.hackathondwi.exam.question.domain.model.Question;
import xyz.cupscoffee.hackathondwi.exam.question.domain.query.failure.QuestionFailure;
import xyz.cupscoffee.hackathondwi.exam.question.domain.repository.QuestionRepository;
import xyz.cupscoffee.hackathondwi.shared.adapter.annotations.UseCase;
import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.Criteria;
import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.Filter;
import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.FilterOperator;
import xyz.cupscoffee.hackathondwi.shared.domain.query.result.Result;
import xyz.cupscoffee.hackathondwi.student.core.domain.model.Student;
import xyz.cupscoffee.hackathondwi.student.core.domain.repository.StudentRepository;

/**
 * Create answer use case.
 */
@Slf4j
@UseCase
@RequiredArgsConstructor
public final class CreateAnswerUseCase implements CreateAnswerPort {
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final StudentRepository studentRepository;
    private final GetAuthenticatedUserPort getAuthenticatedUserPort;
    private final AnswerMapper answerMapper;

    @Override
    public Result<AnswerDto, AnswerFailure> create(CreateAnswerPayload payload) {
        // Verify if the question exists
        Long userId = getAuthenticatedUserPort.get().get().getId();

        log.debug("Creating answer with question id {}",
                ansi().fgBrightBlue().a(payload.getQuestionId()).reset());

        var resultQuestion = questionRepository.matchOne(
                Criteria.ofMatchOne(
                        Filter.of("id", FilterOperator.EQUAL, payload.getQuestionId()),
                        Filter.of("exam.course.semester.user.id", FilterOperator.EQUAL, userId)));

        if (resultQuestion.isFailure()) {
            QuestionFailure failure = resultQuestion.getFailure();

            if (failure == QuestionFailure.NOT_FOUND) {
                log.error("Failed to create answer: {}",
                        ansi().fgBrightRed().a(failure));

                return Result.failure(AnswerFailure.QUESTION_NOT_FOUND);
            }

            return Result.failure(AnswerFailure.UNEXPECTED_ERROR);
        }

        // Verify if the student exists
        Long studentId = payload.getStudentId();

        log.debug("Creating answer with student id {}",
                ansi().fgBrightBlue().a(studentId).reset());

        var resultStudent = studentRepository.matchOne(
                Criteria.ofMatchOne(
                        Filter.of("id", FilterOperator.EQUAL, studentId),
                        Filter.of("courses.semester.user.id", FilterOperator.EQUAL, userId)));

        if (resultStudent.isFailure()) {
            log.error("Failed to create answer: {}",
                    ansi().fgBrightRed().a(resultStudent.getFailure()));

            return Result.failure(AnswerFailure.STUDENT_NOT_FOUND);
        }

        // Verify that the answer is not greater than the maximum value
        Question question = resultQuestion.getSuccess();
        Integer value = payload.getValue();

        if (question.getMaxValue() < value) {
            log.error("Failed to create answer: {}",
                    ansi().fgBrightRed().a(AnswerFailure.CANNOT_OVERFLOW_MAX_VALUE));

            return Result.failure(AnswerFailure.CANNOT_OVERFLOW_MAX_VALUE);
        }

        // Create the new answer
        // `Question question` already defined above
        Student student = resultStudent.getSuccess();
        Answer answer = Answer.builder()
                .value(value)
                .question(question)
                .student(student)
                .build();
        var result = answerRepository.save(answer);

        if (result.isFailure()) {
            log.error("Failed to create answer: {}",
                    ansi().fgBrightRed().a(result.getFailure()));

            return Result.failure(result.getFailure());
        }

        log.info("Successfully created answer with id {}",
                ansi().fgBrightBlue().a(result.getSuccess().getId()).reset());

        return Result.ok(answerMapper.toDto(result.getSuccess()));
    }
}
