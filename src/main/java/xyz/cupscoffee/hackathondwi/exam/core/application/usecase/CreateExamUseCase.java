package xyz.cupscoffee.hackathondwi.exam.core.application.usecase;

import static org.fusesource.jansi.Ansi.ansi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.cupscoffee.hackathondwi.auth.application.port.out.GetAuthenticatedUserPort;
import xyz.cupscoffee.hackathondwi.exam.core.application.dto.ExamDto;
import xyz.cupscoffee.hackathondwi.exam.core.application.mapper.ExamMapper;
import xyz.cupscoffee.hackathondwi.exam.core.application.port.in.CreateExamPort;
import xyz.cupscoffee.hackathondwi.exam.core.domain.model.Exam;
import xyz.cupscoffee.hackathondwi.exam.core.domain.query.failure.ExamFailure;
import xyz.cupscoffee.hackathondwi.exam.core.domain.query.payload.CreateExamPayload;
import xyz.cupscoffee.hackathondwi.exam.core.domain.repository.ExamRepository;
import xyz.cupscoffee.hackathondwi.semester.course.domain.model.Course;
import xyz.cupscoffee.hackathondwi.semester.course.domain.query.failure.CourseFailure;
import xyz.cupscoffee.hackathondwi.semester.course.domain.repository.CourseRepository;
import xyz.cupscoffee.hackathondwi.shared.adapter.annotations.UseCase;
import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.Criteria;
import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.Filter;
import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.FilterOperator;
import xyz.cupscoffee.hackathondwi.shared.domain.query.result.Result;

/**
 * Create exam use case.
 */
@Slf4j
@UseCase
@RequiredArgsConstructor
public final class CreateExamUseCase implements CreateExamPort {
    private final ExamRepository examRepository;
    private final CourseRepository courseRepository;
    private final GetAuthenticatedUserPort getAuthenticatedUserPort;
    private final ExamMapper examMapper;

    @Override
    public Result<ExamDto, ExamFailure> create(CreateExamPayload payload) {
        Long userId = getAuthenticatedUserPort.get().get().getId();

        log.debug("Creating exam with user id {}",
                ansi().fgBrightBlue().a(userId).reset());

        var resultCourse = courseRepository.matchOne(
                Criteria.ofMatchOne(
                        Filter.of("id", FilterOperator.EQUAL, payload.getCourseId()),
                        Filter.of("semester.user.id", FilterOperator.EQUAL, userId)));

        if (resultCourse.isFailure()) {
            CourseFailure failure = resultCourse.getFailure();

            if (failure == CourseFailure.NOT_FOUND) {
                log.error("Failed to create exam: {}",
                        ansi().fgBrightRed().a(failure.getMessage()));

                return Result.failure(ExamFailure.COURSE_NOT_FOUND);
            }

            return Result.failure(ExamFailure.UNEXPECTED_ERROR);
        }

        Course course = resultCourse.getSuccess();
        Exam newExam = Exam.builder()
                .name(payload.getName())
                .course(course)
                .build();

        var result = examRepository.save(newExam);

        if (result.isFailure()) {
            log.error("Failed to create exam: {}",
                    ansi().fgBrightRed().a(result.getFailure().getMessage()));

            return Result.failure(result.getFailure());
        }

        log.info("Successfully created exam with id {}",
                ansi().fgBrightBlue().a(result.getSuccess().getId()).reset());

        return Result.ok(examMapper.toDto(result.getSuccess()));
    }
}
