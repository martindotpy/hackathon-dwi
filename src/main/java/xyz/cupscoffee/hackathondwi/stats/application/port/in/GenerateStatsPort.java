package xyz.cupscoffee.hackathondwi.stats.application.port.in;

import java.util.Optional;

import xyz.cupscoffee.hackathondwi.stats.application.dto.StatsDto;

public interface GenerateStatsPort {
    Optional<StatsDto> generateStats(Long examId);
}
