package xyz.cupscoffee.hackathondwi.shared.adapter.in.view;

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
import xyz.cupscoffee.hackathondwi.semester.core.adapter.in.request.CreateSemesterRequest;
import xyz.cupscoffee.hackathondwi.semester.core.adapter.in.request.UpdateSemesterRequest;
import xyz.cupscoffee.hackathondwi.semester.core.adapter.in.response.PaginatedSemesterResponse;
import xyz.cupscoffee.hackathondwi.semester.core.adapter.in.response.SemesterContentResponse;
import xyz.cupscoffee.hackathondwi.semester.core.application.dto.SemesterDto;
import xyz.cupscoffee.hackathondwi.semester.core.domain.query.payload.UpdateSemesterPayload;
import xyz.cupscoffee.hackathondwi.shared.adapter.in.util.FaceShortcuts;

@Slf4j
@ViewScoped
@Named
@RequiredArgsConstructor
public class HomeView {
    // Rest client
    private final RestClient restClient;

    @PostConstruct
    public void init() {
        loadSemesters();
        semesterLazyDataModel = new SemesterLazyDataModel();
    }

    // Table view
    @Getter
    @Setter
    private int page = 0;
    private final int size = 10;
    @Getter
    private PaginatedSemesterResponse semesters;
    @Getter
    private SemesterLazyDataModel semesterLazyDataModel;

    // Search
    @Getter
    @Setter
    private String criteria = "id";
    @Getter
    @Setter
    private String searchValue = "";

    public void loadSemesters() {
        String endpoint = String.format("/semester?page=%d&size=%d", page + 1, size);
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
        var response = restClient.get(endpoint, PaginatedSemesterResponse.class);

        if (response.getStatus() == 200) {
            this.semesters = (PaginatedSemesterResponse) response.getBody();
        } else {
            this.semesters = null;
        }
    }

    public void search() {
        page = 0;
        loadSemesters();
    }

    // Creation
    @Getter
    @Setter
    private String createSemesterName;

    public void create() {
        CreateSemesterRequest request = CreateSemesterRequest.builder()
                .name(createSemesterName)
                .build();

        var response = restClient.post("/semester", request, SemesterContentResponse.class);

        if (response.getStatus() != 200) {
            return;
        }

        search();
    }

    // Update
    @Getter
    @Setter
    private SemesterDto selectedSemester;
    @Getter
    @Setter
    private String updatedSemesterName;

    public void prepareUpdate(SemesterDto semester) {
        this.selectedSemester = semester;
        this.updatedSemesterName = semester.getName();
    }

    public void update() {
        if (selectedSemester == null) {
            log.error("Selected semester is null");
            return;
        }

        UpdateSemesterPayload request = UpdateSemesterRequest.builder()
                .id(selectedSemester.getId())
                .name(updatedSemesterName)
                .build();

        var response = restClient.put("/semester/" + selectedSemester.getId(), request, SemesterContentResponse.class);

        if (response.getStatus() == 200) {
            search();
        }
    }

    // Delete
    public void delete(Long semesterId) {
        var response = restClient.delete("/semester/" + semesterId);

        if (response.getStatus() == 200) {
            search();
        }
    }

    // Lazy data model
    public class SemesterLazyDataModel extends LazyDataModel<SemesterDto> {
        @Override
        public int count(Map<String, FilterMeta> filterBy) {
            return (int) semesters.getTotalElements();
        }

        @Override
        public List<SemesterDto> load(int first, int pageSize, Map<String, SortMeta> sortBy,
                Map<String, FilterMeta> filterBy) {
            int prePage = page;
            if (first == 0) {
                page = 0;
            } else {
                page = first / pageSize;
            }

            if (prePage != page) {
                loadSemesters();
            }

            return semesters.getContent();
        }
    }
}
