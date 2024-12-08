package xyz.cupscoffee.hackathondwi.student.core.application.usecase;

import static org.fusesource.jansi.Ansi.ansi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.cupscoffee.hackathondwi.auth.application.port.out.GetAuthenticatedUserPort;
import xyz.cupscoffee.hackathondwi.shared.adapter.annotations.UseCase;
import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.Criteria;
import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.Filter;
import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.FilterOperator;
import xyz.cupscoffee.hackathondwi.shared.domain.query.result.Result;
import xyz.cupscoffee.hackathondwi.student.core.application.dto.StudentDto;
import xyz.cupscoffee.hackathondwi.student.core.application.mapper.StudentMapper;
import xyz.cupscoffee.hackathondwi.student.core.application.port.in.UpdateStudentPort;
import xyz.cupscoffee.hackathondwi.student.core.domain.model.Student;
import xyz.cupscoffee.hackathondwi.student.core.domain.query.failure.StudentFailure;
import xyz.cupscoffee.hackathondwi.student.core.domain.query.payload.UpdateStudentPayload;
import xyz.cupscoffee.hackathondwi.student.core.domain.repository.StudentRepository;

/**
 * Update student use case.
 */
@Slf4j
@UseCase
@RequiredArgsConstructor
public final class UpdateStudentUseCase implements UpdateStudentPort {
    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final GetAuthenticatedUserPort getAuthenticatedUserPort;

    @Override
    public Result<StudentDto, StudentFailure> update(UpdateStudentPayload payload) {
        // Verify if the user authenticated is the owner of the student
        // and if the student exists
        Long userId = getAuthenticatedUserPort.get().get().getId();
        var resultStudent = studentRepository.matchOne(
                Criteria.ofMatchOne(
                        Filter.of("id", FilterOperator.EQUAL, payload.getId()),
                        Filter.of("courses.semester.user.id", FilterOperator.EQUAL, userId)));

        if (resultStudent.isFailure()) {
            log.error("Failed to update student: {}",
                    ansi().fgBrightRed().a(resultStudent.getFailure().getMessage()));

            return Result.failure(StudentFailure.NOT_FOUND);
        }

        // Update the student
        Student studentToUpdate = resultStudent.getSuccess();
        Student updatedStudent = Student.builder()
                .id(studentToUpdate.getId())
                .firstName(payload.getFirstName())
                .lastName(payload.getLastName())
                .code(payload.getCode())
                .courses(studentToUpdate.getCourses())
                .build();

        var result = studentRepository.save(updatedStudent);

        if (result.isFailure()) {
            log.error("Failed to update student with id {}: {}",
                    ansi().fgBrightRed().a(payload.getId()).reset().toString(),
                    result.getFailure().getMessage());

            return Result.failure(result.getFailure());
        }

        log.info("Successfully updated student with id {}",
                ansi().fgBrightBlue().a(payload.getId()).reset());

        return Result.ok(studentMapper.toDto(result.getSuccess()));
    }
}
