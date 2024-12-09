package xyz.cupscoffee.hackathondwi.exam.core.adapter.in.view;

import java.util.List;
import java.util.Map;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.springframework.util.StringUtils;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import xyz.cupscoffee.hackathondwi.exam.core.adapter.in.request.CreateExamRequest;
import xyz.cupscoffee.hackathondwi.exam.core.adapter.in.request.UpdateExamRequest;
import xyz.cupscoffee.hackathondwi.exam.core.adapter.in.response.ExamContentResponse;
import xyz.cupscoffee.hackathondwi.exam.core.adapter.in.response.PaginatedExamResponse;
import xyz.cupscoffee.hackathondwi.exam.core.application.dto.ExamDto;
import xyz.cupscoffee.hackathondwi.exam.core.domain.query.payload.UpdateExamPayload;
import xyz.cupscoffee.hackathondwi.shared.adapter.in.util.FaceShortcuts;
import xyz.cupscoffee.hackathondwi.shared.adapter.in.view.RestClient;

@Slf4j
@ViewScoped
@Named
@RequiredArgsConstructor
public final class ExamView {
    // Rest client
    private final RestClient restClient;

    @PostConstruct
    public void init() {
        try {
            courseId = Long.valueOf(FaceShortcuts.getQueryParam("course_id"));
        } catch (NumberFormatException e) {
            FaceShortcuts.redirect("/home.xhtml");
            return;
        }

        if (!loadExams()) {
            FaceShortcuts.redirect("/home.xhtml");
            return;
        }

        ExamLazyDataModel = new ExamLazyDataModel();
    }

    // Return to previous page or home
    @Getter
    @Setter
    private String semesterId;

    public void back() {
        try {
            Integer.parseInt(semesterId);
        } catch (NumberFormatException e) {
            FaceShortcuts.redirect("/home.xhtml");
            return;
        }

        FaceShortcuts.redirect("/course.xhtml?semester_id=" + semesterId);
    }

    // Table view
    @Getter
    @Setter
    private int page = 0;
    private final int size = 10;
    @Getter
    private PaginatedExamResponse Exams;
    @Getter
    private ExamLazyDataModel ExamLazyDataModel;

    // Search
    @Getter
    private Long courseId;
    @Getter
    @Setter
    private String criteria = "id";
    @Getter
    @Setter
    private String searchValue = "";

    public boolean loadExams() {
        String endpoint = String.format("/exam?course_id=%d&page=%d&size=%d", courseId, page + 1, size);
        String value = searchValue.trim();

        if (criteria.equals("id") && StringUtils.hasText(value)) {
            try {
                Long.parseLong(value);
            } catch (NumberFormatException e) {
                FaceShortcuts.showFailureMessage("home.action.search.criteria.id.failure", "Id can only be a number");
            }

            value = value.replaceAll("[^\\d]", "");
            searchValue = value;
        }

        if (StringUtils.hasText(value)) {
            endpoint += "&" + criteria + "=" + value;
        }

        var response = restClient.get(endpoint, PaginatedExamResponse.class);

        if (response.getStatus() == 200) {
            this.Exams = (PaginatedExamResponse) response.getBody();
            return true;
        }

        return false;
    }

    public void search() {
        page = 0;
        loadExams();
    }

    // Creation
    @Getter
    @Setter
    private String createExamName;

    public void create() {
        CreateExamRequest request = CreateExamRequest.builder()
                .courseId(courseId)
                .name(createExamName)
                .build();

        var response = restClient.post("/exam", request, ExamContentResponse.class);

        if (response.getStatus() != 200) {
            return;
        }

        search();
    }

    // Update
    @Getter
    @Setter
    private ExamDto selectedExam;
    @Getter
    @Setter
    private String updatedExamName;

    public void prepareUpdate(ExamDto Exam) {
        this.selectedExam = Exam;
        this.updatedExamName = Exam.getName();
    }

    public void update() {
        if (selectedExam == null) {
            log.error("Selected Exam is null");
            return;
        }

        UpdateExamPayload request = UpdateExamRequest.builder()
                .id(selectedExam.getId())
                .name(updatedExamName)
                .build();

        var response = restClient.put("/exam/" + selectedExam.getId(), request, ExamContentResponse.class);

        if (response.getStatus() == 200) {
            search();
        }
    }

    // Delete
    public void delete(Long ExamId) {
        var response = restClient.delete("/exam/" + ExamId);

        if (response.getStatus() == 200) {
            search();
        }
    }

    // Lazy data model
    public class ExamLazyDataModel extends LazyDataModel<ExamDto> {
        @Override
        public int count(Map<String, FilterMeta> filterBy) {
            return (int) Exams.getTotalElements();
        }

        @Override
        public List<ExamDto> load(int first, int pageSize, Map<String, SortMeta> sortBy,
                Map<String, FilterMeta> filterBy) {
            int prePage = page;
            if (first == 0) {
                page = 0;
            } else {
                page = first / pageSize;
            }

            if (prePage != page) {
                loadExams();
            }

            return Exams.getContent();
        }
    }
}
