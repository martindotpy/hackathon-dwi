package xyz.cupscoffee.hackathondwi.stats.application.dto;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.cupscoffee.hackathondwi.student.core.application.dto.StudentDto;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class StatsDto {
    private int passed;
    private int failed;
    private Map<Integer, Integer> scores;
    private ThreePositions threePositions;
    private Map<Integer, Map<Integer, Integer>> scoresByQuestion;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ThreePositions {
        private List<StudentDto> firstPositionCodes;
        private List<StudentDto> secondPositionCodes;
        private List<StudentDto> thirdPositionCodes;
    }
}
