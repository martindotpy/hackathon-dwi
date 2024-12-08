package xyz.cupscoffee.hackathondwi.semester.course.application.usecase;

import static org.fusesource.jansi.Ansi.ansi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.cupscoffee.hackathondwi.auth.application.port.out.GetAuthenticatedUserPort;
import xyz.cupscoffee.hackathondwi.semester.course.application.dto.CourseDto;
import xyz.cupscoffee.hackathondwi.semester.course.application.mapper.CourseMapper;
import xyz.cupscoffee.hackathondwi.semester.course.application.port.in.UpdateCoursePort;
import xyz.cupscoffee.hackathondwi.semester.course.domain.model.Course;
import xyz.cupscoffee.hackathondwi.semester.course.domain.query.failure.CourseFailure;
import xyz.cupscoffee.hackathondwi.semester.course.domain.query.payload.UpdateCoursePayload;
import xyz.cupscoffee.hackathondwi.semester.course.domain.repository.CourseRepository;
import xyz.cupscoffee.hackathondwi.shared.adapter.annotations.UseCase;
import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.Criteria;
import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.Filter;
import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.FilterOperator;
import xyz.cupscoffee.hackathondwi.shared.domain.query.result.Result;

/**
 * Update course use case.
 */
@Slf4j
@UseCase
@RequiredArgsConstructor
public final class UpdateCourseUseCase implements UpdateCoursePort {
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final GetAuthenticatedUserPort getAuthenticatedUserPort;

    @Override
    public Result<CourseDto, CourseFailure> update(UpdateCoursePayload payload) {
        Long userId = getAuthenticatedUserPort.get().get().getId();

        var resultCourse = courseRepository.matchOne(
                Criteria.ofMatchOne(
                        Filter.of("id", FilterOperator.EQUAL, payload.getId()),
                        Filter.of("semester.user.id", FilterOperator.EQUAL, userId)));

        if (resultCourse.isFailure()) {
            return Result.failure(resultCourse.getFailure());
        }

        Course courseToUpdate = resultCourse.getSuccess();

        Course updatedCourse = Course.builder()
                .id(courseToUpdate.getId())
                .name(payload.getName())
                .semester(courseToUpdate.getSemester())
                .build();

        var result = courseRepository.save(updatedCourse);

        if (result.isFailure()) {
            log.error("Failed to update course with id {}: {}",
                    ansi().fgBrightRed().a(payload.getId()).reset().toString(),
                    result.getFailure().getMessage());

            return Result.failure(result.getFailure());
        }

        log.info("Successfully updated course with id {}",
                ansi().fgBrightBlue().a(payload.getId()).reset());

        return Result.ok(courseMapper.toDto(result.getSuccess()));
    }
}
