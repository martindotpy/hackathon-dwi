package xyz.cupscoffee.hackathondwi.stats.adpater.in.controller;

import static xyz.cupscoffee.hackathondwi.shared.adapter.in.util.ControllerShortcuts.toFailureResponse;
import static xyz.cupscoffee.hackathondwi.shared.adapter.in.util.ControllerShortcuts.toOkResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import xyz.cupscoffee.hackathondwi.shared.adapter.annotations.RestControllerAdapter;
import xyz.cupscoffee.hackathondwi.stats.adpater.in.response.StatsResponse;
import xyz.cupscoffee.hackathondwi.stats.application.usecase.GenerateStatsUseCase;
import xyz.cupscoffee.hackathondwi.stats.domain.failure.StatsFailure;

@RestControllerAdapter
@RequestMapping("/api/${spring.api.version}/stats")
@RequiredArgsConstructor
public class StatsController {
    private final GenerateStatsUseCase generateStatsUseCase;

    @GetMapping("/exam/{examId}")
    public ResponseEntity<?> getStatsFromExam(@PathVariable Long examId) {
        var result = generateStatsUseCase.generateStats(examId);

        if (result.isEmpty()) {
            return toFailureResponse(StatsFailure.EXAM_NOT_FOUND);
        }

        return toOkResponse(
                StatsResponse.class,
                result.get(),
                "stats.exam.success",
                "Stats generated from the examen successfully");
    }
}
