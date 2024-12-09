package xyz.cupscoffee.hackathondwi.student.core.application.usecase;

import static org.fusesource.jansi.Ansi.ansi;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.cupscoffee.hackathondwi.auth.application.port.out.GetAuthenticatedUserPort;
import xyz.cupscoffee.hackathondwi.semester.course.domain.model.Course;
import xyz.cupscoffee.hackathondwi.semester.course.domain.repository.CourseRepository;
import xyz.cupscoffee.hackathondwi.shared.adapter.annotations.UseCase;
import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.Criteria;
import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.Filter;
import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.FilterOperator;
import xyz.cupscoffee.hackathondwi.shared.domain.query.result.Result;
import xyz.cupscoffee.hackathondwi.student.core.application.dto.StudentDto;
import xyz.cupscoffee.hackathondwi.student.core.application.mapper.StudentMapper;
import xyz.cupscoffee.hackathondwi.student.core.application.port.in.CreateStudentPort;
import xyz.cupscoffee.hackathondwi.student.core.domain.model.Student;
import xyz.cupscoffee.hackathondwi.student.core.domain.query.failure.StudentFailure;
import xyz.cupscoffee.hackathondwi.student.core.domain.query.payload.CreateStudentPayload;
import xyz.cupscoffee.hackathondwi.student.core.domain.repository.StudentRepository;

/**
 * Create student use case.
 */
@Slf4j
@UseCase
@RequiredArgsConstructor
public final class CreateStudentUseCase implements CreateStudentPort {
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final StudentMapper studentMapper;
    private final GetAuthenticatedUserPort getAuthenticatedUserPort;

    @Override
    public Result<StudentDto, StudentFailure> create(CreateStudentPayload payload) {
        // Verify if the user authenticated is the owner of the course
        // and if the course exists
        Long userId = getAuthenticatedUserPort.get().get().getId();

        log.debug("Creating student with course id {}",
                ansi().fgBrightBlue().a(payload.getCourseId()).reset());

        var resultCourse = courseRepository.matchOne(
                Criteria.ofMatchOne(
                        Filter.of("id", FilterOperator.EQUAL, payload.getCourseId()),
                        Filter.of("semester.user.id", FilterOperator.EQUAL, userId)));

        if (resultCourse.isFailure()) {
            log.error("Failed to create student: {}",
                    ansi().fgBrightRed().a(resultCourse.getFailure().getMessage()).reset());

            return Result.failure(StudentFailure.COURSE_NOT_FOUND);
        }

        // Create the new student
        Course course = resultCourse.getSuccess();
        Student newStudent = Student.builder()
                .firstName(payload.getFirstName())
                .lastName(payload.getLastName())
                .code(payload.getCode().toUpperCase())
                .courses(new CopyOnWriteArrayList<>(List.of(course)))
                .build();

        var result = studentRepository.save(newStudent);

        if (result.isFailure()) {
            log.error("Failed to create student: {}",
                    ansi().fgBrightRed().a(result.getFailure().getMessage()).reset());

            return Result.failure(result.getFailure());
        }

        log.info("Successfully created student with id {}",
                ansi().fgBrightBlue().a(result.getSuccess().getId()).reset());

        return Result.ok(studentMapper.toDto(result.getSuccess()));
    }
}
