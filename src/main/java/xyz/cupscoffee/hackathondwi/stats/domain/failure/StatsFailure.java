package xyz.cupscoffee.hackathondwi.stats.domain.failure;

import lombok.AllArgsConstructor;
import lombok.Getter;
import xyz.cupscoffee.hackathondwi.shared.domain.query.failure.Failure;

@Getter
@AllArgsConstructor
public enum StatsFailure implements Failure {
    EXAM_NOT_FOUND("Exam not found"),;

    private final String message;
}
