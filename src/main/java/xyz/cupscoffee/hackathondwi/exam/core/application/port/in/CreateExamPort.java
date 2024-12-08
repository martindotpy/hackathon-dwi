package xyz.cupscoffee.hackathondwi.exam.core.application.port.in;

import xyz.cupscoffee.hackathondwi.exam.core.application.dto.ExamDto;
import xyz.cupscoffee.hackathondwi.exam.core.domain.query.failure.ExamFailure;
import xyz.cupscoffee.hackathondwi.exam.core.domain.query.payload.CreateExamPayload;
import xyz.cupscoffee.hackathondwi.shared.domain.query.result.Result;

/**
 * Create exam port.
 */
public interface CreateExamPort {
    /**
     * Create a exam.
     *
     * @param payload the payload to create a exam.
     * @return the result of the operation
     */
    Result<ExamDto, ExamFailure> create(CreateExamPayload payload);
}
