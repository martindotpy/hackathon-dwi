package xyz.cupscoffee.hackathondwi.stats.adpater.in.status;

import org.springframework.stereotype.Component;

import xyz.cupscoffee.hackathondwi.shared.adapter.in.status.HttpStatusCodeFailureHandler;
import xyz.cupscoffee.hackathondwi.stats.domain.failure.StatsFailure;

@Component
public class HttpStatusStatsFailureHandler implements HttpStatusCodeFailureHandler<StatsFailure> {
    @Override
    public int getHttpStatusCode(StatsFailure failure) {
        return switch (failure) {
            case EXAM_NOT_FOUND -> 404;
        };
    }
}
