package xyz.cupscoffee.hackathondwi.student.core.adapter.in.view;

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
import xyz.cupscoffee.hackathondwi.shared.adapter.in.util.FaceShortcuts;
import xyz.cupscoffee.hackathondwi.shared.adapter.in.view.RestClient;
import xyz.cupscoffee.hackathondwi.student.core.adapter.in.request.CreateStudentRequest;
import xyz.cupscoffee.hackathondwi.student.core.adapter.in.request.UpdateStudentRequest;
import xyz.cupscoffee.hackathondwi.student.core.adapter.in.response.PaginatedStudentResponse;
import xyz.cupscoffee.hackathondwi.student.core.adapter.in.response.StudentContentResponse;
import xyz.cupscoffee.hackathondwi.student.core.application.dto.StudentDto;
import xyz.cupscoffee.hackathondwi.student.core.domain.query.payload.UpdateStudentPayload;

@Slf4j
@ViewScoped
@Named
@RequiredArgsConstructor
public final class StudentView {
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

        if (!loadStudents()) {
            FaceShortcuts.redirect("/home.xhtml");
            return;
        }

        studentLazyDataModel = new StudentLazyDataModel();
    }

    // Table view
    @Getter
    @Setter
    private int page = 0;
    private final int size = 10;
    @Getter
    private PaginatedStudentResponse students;
    @Getter
    private StudentLazyDataModel studentLazyDataModel;

    // Search
    @Getter
    private Long courseId;
    @Getter
    @Setter
    private String criteria = "id";
    @Getter
    @Setter
    private String searchValue = "";

    public boolean loadStudents() {
        String endpoint = String.format("/student?course_id=%d&page=%d&size=%d", courseId, page + 1, size);
        String value = searchValue.trim();

        if (criteria.equals("id")) {
            value = value.replaceAll("[^\\d]", "");
            searchValue = value;
        }

        if (StringUtils.hasText(value)) {
            endpoint += "&" + criteria + "=" + value;
        }

        var response = restClient.get(endpoint, PaginatedStudentResponse.class);

        if (response.getStatus() == 200) {
            this.students = (PaginatedStudentResponse) response.getBody();
            return true;
        }

        return false;
    }

    public void search() {
        page = 0;
        loadStudents();
    }

    // Creation
    @Getter
    @Setter
    private String createStudentCode;
    @Getter
    @Setter
    private String createStudentFirstName;
    @Getter
    @Setter
    private String createStudentLastName;

    public void create() {
        CreateStudentRequest request = CreateStudentRequest.builder()
                .courseId(courseId)
                .code(createStudentCode)
                .firstName(createStudentFirstName)
                .lastName(createStudentLastName)
                .build();

        var response = restClient.post("/student", request, StudentContentResponse.class);

        if (response.getStatus() != 200) {
            return;
        }

        search();
    }

    // Update
    @Getter
    @Setter
    private StudentDto selectedStudent;
    @Getter
    @Setter
    private String updatedStudentCode;
    @Getter
    @Setter
    private String updatedStudentFirstName;
    @Getter
    @Setter
    private String updatedStudentLastName;

    public void prepareUpdate(StudentDto student) {
        this.selectedStudent = student;
        this.updatedStudentCode = student.getCode();
        this.updatedStudentFirstName = student.getFirstName();
        this.updatedStudentLastName = student.getLastName();
    }

    public void update() {
        if (selectedStudent == null) {
            log.error("Selected student is null");
            return;
        }

        UpdateStudentPayload request = UpdateStudentRequest.builder()
                .id(selectedStudent.getId())
                .code(updatedStudentCode)
                .firstName(updatedStudentFirstName)
                .lastName(updatedStudentLastName)
                .build();

        var response = restClient.put("/student/" + selectedStudent.getId(), request, StudentContentResponse.class);

        if (response.getStatus() == 200) {
            search();
        }
    }

    // Delete
    public void delete(Long studentId) {
        var response = restClient.delete("/student/" + studentId);

        if (response.getStatus() == 200) {
            search();
        }
    }

    // Lazy data model
    public class StudentLazyDataModel extends LazyDataModel<StudentDto> {
        @Override
        public int count(Map<String, FilterMeta> filterBy) {
            return (int) students.getTotalElements();
        }

        @Override
        public List<StudentDto> load(int first, int pageSize, Map<String, SortMeta> sortBy,
                Map<String, FilterMeta> filterBy) {
            int prePage = page;
            if (first == 0) {
                page = 0;
            } else {
                page = first / pageSize;
            }

            if (prePage != page) {
                loadStudents();
            }

            return students.getContent();
        }
    }
}
