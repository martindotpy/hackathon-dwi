package xyz.cupscoffee.hackathondwi.semester.course.application.usecase;

import static org.fusesource.jansi.Ansi.ansi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.cupscoffee.hackathondwi.auth.application.port.out.GetAuthenticatedUserPort;
import xyz.cupscoffee.hackathondwi.semester.course.application.port.in.DeleteCoursePort;
import xyz.cupscoffee.hackathondwi.semester.course.domain.query.failure.CourseFailure;
import xyz.cupscoffee.hackathondwi.semester.course.domain.repository.CourseRepository;
import xyz.cupscoffee.hackathondwi.shared.adapter.annotations.UseCase;
import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.Criteria;
import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.Filter;
import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.FilterOperator;
import xyz.cupscoffee.hackathondwi.shared.domain.query.result.Result;

/**
 * Delete course use case.
 */
@Slf4j
@UseCase
@RequiredArgsConstructor
public final class DeleteCourseUseCase implements DeleteCoursePort {
    private final CourseRepository courseRepository;
    private final GetAuthenticatedUserPort getAuthenticatedUserPort;

    @Override
    public Result<Void, CourseFailure> deleteById(Long id) {
        log.debug("Deleting course with id {}", id);

        Long userId = getAuthenticatedUserPort.get().get().getId();

        var resultCourse = courseRepository.matchOne(
                Criteria.ofMatchOne(
                        Filter.of("id", FilterOperator.EQUAL, id),
                        Filter.of("semester.user.id", FilterOperator.EQUAL, userId)));

        if (resultCourse.isFailure()) {
            return Result.failure(resultCourse.getFailure());
        }

        var result = courseRepository.deleteById(id);

        if (result.isFailure()) {
            log.error("Failed to delete course with id {}: {}",
                    ansi().fgBrightRed().a(id).reset().toString(),
                    result.getFailure().getMessage());

            return Result.failure(result.getFailure());
        }

        log.info("Successfully deleted course with id {}",
                ansi().fgBrightBlue().a(id).reset().toString());

        return Result.ok();
    }
}
