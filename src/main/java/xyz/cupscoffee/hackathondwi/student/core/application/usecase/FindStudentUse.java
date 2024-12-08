package xyz.cupscoffee.hackathondwi.student.core.application.usecase;

import static org.fusesource.jansi.Ansi.ansi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.cupscoffee.hackathondwi.shared.adapter.annotations.UseCase;
import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.Criteria;
import xyz.cupscoffee.hackathondwi.shared.domain.query.paginated.Paginated;
import xyz.cupscoffee.hackathondwi.shared.domain.query.result.Result;
import xyz.cupscoffee.hackathondwi.student.core.application.dto.StudentDto;
import xyz.cupscoffee.hackathondwi.student.core.application.mapper.StudentMapper;
import xyz.cupscoffee.hackathondwi.student.core.application.port.in.FindStudentPort;
import xyz.cupscoffee.hackathondwi.student.core.domain.model.Student;
import xyz.cupscoffee.hackathondwi.student.core.domain.query.failure.StudentFailure;
import xyz.cupscoffee.hackathondwi.student.core.domain.repository.StudentRepository;

/**
 * Find student use case.
 */
@Slf4j
@UseCase
@RequiredArgsConstructor
public final class FindStudentUse implements FindStudentPort {
    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    @Override
    public Paginated<StudentDto> match(Criteria criteria) {
        Paginated<Student> students = studentRepository.match(criteria);

        log.info("Found {} students",
                ansi().fgBrightBlue().a(students.getContent().size()).reset());

        return students.map(studentMapper::toDto);
    }

    @Override
    public Result<StudentDto, StudentFailure> matchOne(Criteria criteria) {
        var result = studentRepository.matchOne(criteria);

        if (result.isFailure()) {
            StudentFailure failure = result.getFailure();

            if (failure == StudentFailure.NOT_FOUND) {
                log.error("Student not found");
            }

            return Result.failure(failure);
        }

        log.info("Found student with id {}",
                ansi().fgBrightBlue().a(result.getSuccess().getId()).reset());

        return Result.ok(studentMapper.toDto(result.getSuccess()));
    }
}
