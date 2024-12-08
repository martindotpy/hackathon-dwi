package xyz.cupscoffee.hackathondwi.exam.core.application.port.in;

import xyz.cupscoffee.hackathondwi.exam.core.application.dto.ExamDto;
import xyz.cupscoffee.hackathondwi.exam.core.domain.query.failure.ExamFailure;
import xyz.cupscoffee.hackathondwi.exam.core.domain.query.payload.UpdateExamPayload;
import xyz.cupscoffee.hackathondwi.shared.domain.query.result.Result;

/**
 * Update exam port.
 */
public interface UpdateExamPort {
    /**
     * Update a exam.
     *
     * @param payload the payload to update a exam.
     * @return the result of the operation
     */
    Result<ExamDto, ExamFailure> update(UpdateExamPayload payload);
}
