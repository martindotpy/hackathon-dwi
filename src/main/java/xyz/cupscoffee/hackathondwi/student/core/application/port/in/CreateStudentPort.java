package xyz.cupscoffee.hackathondwi.student.core.application.port.in;

import xyz.cupscoffee.hackathondwi.shared.domain.query.result.Result;
import xyz.cupscoffee.hackathondwi.student.core.application.dto.StudentDto;
import xyz.cupscoffee.hackathondwi.student.core.domain.query.failure.StudentFailure;
import xyz.cupscoffee.hackathondwi.student.core.domain.query.payload.CreateStudentPayload;

/**
 * Create student port.
 */
public interface CreateStudentPort {
    /**
     * Create a student.
     *
     * @param payload the payload to create a student.
     * @return the result of the operation
     */
    Result<StudentDto, StudentFailure> create(CreateStudentPayload payload);
}
