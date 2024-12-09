package xyz.cupscoffee.hackathondwi.stats.adpater.in.view;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.bar.BarChartDataSet;
import org.primefaces.model.charts.bar.BarChartModel;
import org.primefaces.model.charts.pie.PieChartDataSet;
import org.primefaces.model.charts.pie.PieChartModel;

import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import xyz.cupscoffee.hackathondwi.shared.adapter.in.util.FaceShortcuts;
import xyz.cupscoffee.hackathondwi.shared.adapter.in.util.MessagesShortcuts;
import xyz.cupscoffee.hackathondwi.shared.adapter.in.view.RestClient;
import xyz.cupscoffee.hackathondwi.stats.adpater.in.response.StatsResponse;
import xyz.cupscoffee.hackathondwi.stats.application.dto.StatsDto;

@Slf4j
@ViewScoped
@Named
@RequiredArgsConstructor
public class StatsView {
    @Getter
    private StatsDto stats;
    @Getter
    @Setter
    private String examId;
    @Getter
    @Setter
    private String courseId;
    @Getter
    @Setter
    private String semesterId;
    private final RestClient restClient;

    @PostConstruct
    public void init() {
        examId = FaceShortcuts.getQueryParam("exam_id");
        courseId = FaceShortcuts.getQueryParam("course_id");
        semesterId = FaceShortcuts.getQueryParam("semester_id");

        try {
            Integer.parseInt(examId);
            Integer.parseInt(courseId);
            Integer.parseInt(semesterId);
        } catch (NumberFormatException e) {
            log.error("Invalid query params: examId={}, courseId={}, semesterId={}", examId, courseId, semesterId);

            FaceShortcuts.redirect("/home.xhtml");
            return;
        }

        if (!loadStats()) {
            FaceShortcuts.redirect("/home.xhtml");
            return;
        }

        createPassedFailedPieModel();
        createScoresBarChar();
        createScoresByQuestionModel();
    }

    public void back() {
        FaceShortcuts
                .redirect("/question.xhtml?exam_id=" + examId
                        + "&course_id=" + courseId
                        + "&semester_id=" + semesterId);
    }

    public boolean loadStats() {
        var response = restClient.get("/stats/exam/" + examId, StatsResponse.class);

        if (response.getStatus() != 200) {
            log.error("Failed to load stats: status={}, body={}", response.getStatus(), response.getBodyAsString());

            return false;
        }

        stats = ((StatsResponse) response.getBody()).getContent();

        return true;
    }

    // Load stats
    @Getter
    private PieChartModel passedFailedPieModel;

    private void createPassedFailedPieModel() {
        passedFailedPieModel = new PieChartModel();

        ChartData data = new ChartData();
        PieChartDataSet dataSet = new PieChartDataSet();

        List<Number> values = List.of(stats.getPassed(), stats.getFailed());

        dataSet.setData(values);
        data.addChartDataSet(dataSet);

        String passed = MessagesShortcuts.getMessage("stats.passed", "Passed",
                FacesContext.getCurrentInstance().getViewRoot().getLocale());
        String failed = MessagesShortcuts.getMessage("stats.failed", "Failed",
                FacesContext.getCurrentInstance().getViewRoot().getLocale());
        List<String> labels = List.of(passed, failed);

        data.setLabels(labels);

        passedFailedPieModel.setData(data);
    }

    @Getter
    private BarChartModel scoresBarModel;

    private void createScoresBarChar() {
        scoresBarModel = new BarChartModel();

        ChartData data = new ChartData();
        BarChartDataSet dataSet = new BarChartDataSet();

        List<Object> values = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        for (Integer score : stats.getScores().keySet().stream().sorted().toList()) {
            values.add(stats.getScores().get(score));
            labels.add(score.toString());
        }

        dataSet.setData(values);

        String scores = MessagesShortcuts.getMessage("stats.scores", "Scores",
                FacesContext.getCurrentInstance().getViewRoot().getLocale());
        dataSet.setLabel(scores);

        data.addChartDataSet(dataSet);

        data.setLabels(labels);

        scoresBarModel.setData(data);
    }

    @Getter
    private BarChartModel scoresByQuestionModel;

    private void createScoresByQuestionModel() {
        scoresByQuestionModel = new BarChartModel();
        ChartData data = new ChartData();

        String questionLabel = MessagesShortcuts.getMessage("stats.question", "Question",
                FacesContext.getCurrentInstance().getViewRoot().getLocale());
        stats.getScoresByQuestion().forEach((question, scores) -> {
            BarChartDataSet dataSet = new BarChartDataSet();
            dataSet.setLabel(questionLabel + " " + question);
            dataSet.setData(new ArrayList<>(scores.values()));
            data.addChartDataSet(dataSet);
        });

        data.setLabels(stats.getScoresByQuestion().values().stream()
                .flatMap(map -> map.keySet().stream())
                .distinct()
                .map(String::valueOf)
                .collect(Collectors.toList()));
        scoresByQuestionModel.setData(data);
    }
}
