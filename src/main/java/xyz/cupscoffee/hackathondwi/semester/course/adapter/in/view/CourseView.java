package xyz.cupscoffee.hackathondwi.semester.course.adapter.in.view;

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
import xyz.cupscoffee.hackathondwi.semester.course.adapter.in.request.CreateCourseRequest;
import xyz.cupscoffee.hackathondwi.semester.course.adapter.in.request.UpdateCourseRequest;
import xyz.cupscoffee.hackathondwi.semester.course.adapter.in.response.CourseContentResponse;
import xyz.cupscoffee.hackathondwi.semester.course.adapter.in.response.PaginatedCourseResponse;
import xyz.cupscoffee.hackathondwi.semester.course.application.dto.CourseDto;
import xyz.cupscoffee.hackathondwi.semester.course.domain.query.payload.UpdateCoursePayload;
import xyz.cupscoffee.hackathondwi.shared.adapter.in.util.FaceShortcuts;
import xyz.cupscoffee.hackathondwi.shared.adapter.in.view.RestClient;

@Slf4j
@ViewScoped
@Named
@RequiredArgsConstructor
public final class CourseView {
    // Rest client
    private final RestClient restClient;

    @PostConstruct
    public void init() {
        try {
            semesterId = Long.valueOf(FaceShortcuts.getQueryParam("semester_id"));
        } catch (NumberFormatException e) {
            FaceShortcuts.redirect("/home.xhtml");
            return;
        }

        if (!loadCourses()) {
            FaceShortcuts.redirect("/home.xhtml");
            return;
        }

        courseLazyDataModel = new CourseLazyDataModel();
    }

    // Return to home
    public void back() {
        FaceShortcuts.redirect("/home.xhtml");
    }

    // Table view
    @Getter
    @Setter
    private int page = 0;
    private final int size = 10;
    @Getter
    private PaginatedCourseResponse courses;
    @Getter
    private CourseLazyDataModel courseLazyDataModel;

    // Search
    @Getter
    private Long semesterId;
    @Getter
    @Setter
    private String criteria = "id";
    @Getter
    @Setter
    private String searchValue = "";

    public boolean loadCourses() {
        String endpoint = String.format("/course?semester_id=%d&page=%d&size=%d", semesterId, page + 1, size);
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

        var response = restClient.get(endpoint, PaginatedCourseResponse.class);

        if (response.getStatus() == 200) {
            this.courses = (PaginatedCourseResponse) response.getBody();
            return true;
        }

        return false;
    }

    public void search() {
        page = 0;
        loadCourses();
    }

    // Creation
    @Getter
    @Setter
    private String createCourseName;

    public void create() {
        CreateCourseRequest request = CreateCourseRequest.builder()
                .semesterId(semesterId)
                .name(createCourseName)
                .build();

        var response = restClient.post("/course", request, CourseContentResponse.class);

        if (response.getStatus() != 200) {
            return;
        }

        search();
    }

    // Update
    @Getter
    @Setter
    private CourseDto selectedCourse;
    @Getter
    @Setter
    private String updatedCourseName;

    public void prepareUpdate(CourseDto course) {
        this.selectedCourse = course;
        this.updatedCourseName = course.getName();
    }

    public void update() {
        if (selectedCourse == null) {
            log.error("Selected course is null");
            return;
        }

        UpdateCoursePayload request = UpdateCourseRequest.builder()
                .id(selectedCourse.getId())
                .name(updatedCourseName)
                .build();

        var response = restClient.put("/course/" + selectedCourse.getId(), request, CourseContentResponse.class);

        if (response.getStatus() == 200) {
            search();
        }
    }

    // Delete
    public void delete(Long courseId) {
        var response = restClient.delete("/course/" + courseId);

        if (response.getStatus() == 200) {
            search();
        }
    }

    // Lazy data model
    public class CourseLazyDataModel extends LazyDataModel<CourseDto> {
        @Override
        public int count(Map<String, FilterMeta> filterBy) {
            return (int) courses.getTotalElements();
        }

        @Override
        public List<CourseDto> load(int first, int pageSize, Map<String, SortMeta> sortBy,
                Map<String, FilterMeta> filterBy) {
            int prePage = page;
            if (first == 0) {
                page = 0;
            } else {
                page = first / pageSize;
            }

            if (prePage != page) {
                loadCourses();
            }

            return courses.getContent();
        }
    }
}
