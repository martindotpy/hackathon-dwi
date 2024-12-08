package xyz.cupscoffee.hackathondwi.student.core.application.port.in;

import xyz.cupscoffee.hackathondwi.shared.domain.query.result.Result;
import xyz.cupscoffee.hackathondwi.student.core.application.dto.StudentDto;
import xyz.cupscoffee.hackathondwi.student.core.domain.query.failure.StudentFailure;
import xyz.cupscoffee.hackathondwi.student.core.domain.query.payload.UpdateStudentPayload;

/**
 * Update student port.
 */
public interface UpdateStudentPort {
    /**
     * Update a student.
     *
     * @param payload the payload to update a student.
     * @return the result of the operation
     */
    Result<StudentDto, StudentFailure> update(UpdateStudentPayload payload);
}
