package xyz.cupscoffee.hackathondwi.stats.adpater.in.response;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import xyz.cupscoffee.hackathondwi.shared.application.response.ContentResponse;
import xyz.cupscoffee.hackathondwi.stats.application.dto.StatsDto;

@SuperBuilder
@NoArgsConstructor
public class StatsResponse extends ContentResponse<StatsDto> {
}
