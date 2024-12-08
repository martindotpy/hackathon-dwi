package xyz.cupscoffee.hackathondwi.semester.course.application.usecase;

import static org.fusesource.jansi.Ansi.ansi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.cupscoffee.hackathondwi.auth.application.port.out.GetAuthenticatedUserPort;
import xyz.cupscoffee.hackathondwi.semester.core.domain.model.Semester;
import xyz.cupscoffee.hackathondwi.semester.core.domain.query.failure.SemesterFailure;
import xyz.cupscoffee.hackathondwi.semester.core.domain.repository.SemesterRepository;
import xyz.cupscoffee.hackathondwi.semester.course.application.dto.CourseDto;
import xyz.cupscoffee.hackathondwi.semester.course.application.mapper.CourseMapper;
import xyz.cupscoffee.hackathondwi.semester.course.application.port.in.CreateCoursePort;
import xyz.cupscoffee.hackathondwi.semester.course.domain.model.Course;
import xyz.cupscoffee.hackathondwi.semester.course.domain.query.failure.CourseFailure;
import xyz.cupscoffee.hackathondwi.semester.course.domain.query.payload.CreateCoursePayload;
import xyz.cupscoffee.hackathondwi.semester.course.domain.repository.CourseRepository;
import xyz.cupscoffee.hackathondwi.shared.adapter.annotations.UseCase;
import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.Criteria;
import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.Filter;
import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.FilterOperator;
import xyz.cupscoffee.hackathondwi.shared.domain.query.result.Result;

/**
 * Create course use case.
 */
@Slf4j
@UseCase
@RequiredArgsConstructor
public final class CreateCourseUseCase implements CreateCoursePort {
    private final CourseRepository courseRepository;
    private final SemesterRepository semesterRepository;
    private final GetAuthenticatedUserPort getAuthenticatedUserPort;
    private final CourseMapper courseMapper;

    @Override
    public Result<CourseDto, CourseFailure> create(CreateCoursePayload payload) {
        Long userId = getAuthenticatedUserPort.get().get().getId();

        log.debug("Creating course with user id {}",
                ansi().fgBrightBlue().a(userId).reset());

        var resultSemester = semesterRepository.matchOne(
                Criteria.ofMatchOne(
                        Filter.of("id", FilterOperator.EQUAL, payload.getSemesterId()),
                        Filter.of("user.id", FilterOperator.EQUAL, userId)));

        if (resultSemester.isFailure()) {
            SemesterFailure failure = resultSemester.getFailure();

            if (failure == SemesterFailure.NOT_FOUND) {
                log.error("Failed to create course: {}",
                        ansi().fgBrightRed().a(failure.getMessage()));

                return Result.failure(CourseFailure.SEMESTER_NOT_FOUND);
            }

            return Result.failure(CourseFailure.UNEXPECTED_ERROR);
        }

        Semester semester = resultSemester.getSuccess();
        Course newCourse = Course.builder()
                .name(payload.getName())
                .semester(semester)
                .build();

        var result = courseRepository.save(newCourse);

        if (result.isFailure()) {
            log.error("Failed to create course: {}",
                    ansi().fgBrightRed().a(result.getFailure().getMessage()));

            return Result.failure(result.getFailure());
        }

        log.info("Successfully created course with id {}",
                ansi().fgBrightBlue().a(result.getSuccess().getId()).reset());

        return Result.ok(courseMapper.toDto(result.getSuccess()));
    }
}
