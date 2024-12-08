package xyz.cupscoffee.hackathondwi.semester.course.application.usecase;

import static org.fusesource.jansi.Ansi.ansi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.cupscoffee.hackathondwi.semester.course.application.dto.CourseDto;
import xyz.cupscoffee.hackathondwi.semester.course.application.mapper.CourseMapper;
import xyz.cupscoffee.hackathondwi.semester.course.application.port.in.FindCoursePort;
import xyz.cupscoffee.hackathondwi.semester.course.domain.model.Course;
import xyz.cupscoffee.hackathondwi.semester.course.domain.query.failure.CourseFailure;
import xyz.cupscoffee.hackathondwi.semester.course.domain.repository.CourseRepository;
import xyz.cupscoffee.hackathondwi.shared.adapter.annotations.UseCase;
import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.Criteria;
import xyz.cupscoffee.hackathondwi.shared.domain.query.paginated.Paginated;
import xyz.cupscoffee.hackathondwi.shared.domain.query.result.Result;

/**
 * Find course use case.
 */
@Slf4j
@UseCase
@RequiredArgsConstructor
public final class FindCourseUse implements FindCoursePort {
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    @Override
    public Paginated<CourseDto> match(Criteria criteria) {
        Paginated<Course> courses = courseRepository.match(criteria);

        log.info("Found {} courses",
                ansi().fgBrightBlue().a(courses.getContent().size()).reset());

        return courses.map(courseMapper::toDto);
    }

    @Override
    public Result<CourseDto, CourseFailure> matchOne(Criteria criteria) {
        var result = courseRepository.matchOne(criteria);

        if (result.isFailure()) {
            CourseFailure failure = result.getFailure();

            if (failure == CourseFailure.NOT_FOUND) {
                log.error("Course not found");
            }

            return Result.failure(failure);
        }

        log.info("Found course with id {}",
                ansi().fgBrightBlue().a(result.getSuccess().getId()).reset());

        return Result.ok(courseMapper.toDto(result.getSuccess()));
    }
}
