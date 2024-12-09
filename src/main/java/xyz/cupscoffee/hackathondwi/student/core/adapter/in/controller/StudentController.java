package xyz.cupscoffee.hackathondwi.student.core.adapter.in.controller;

import static xyz.cupscoffee.hackathondwi.shared.adapter.in.util.ControllerShortcuts.toDetailedFailureResponse;
import static xyz.cupscoffee.hackathondwi.shared.adapter.in.util.ControllerShortcuts.toFailureResponse;
import static xyz.cupscoffee.hackathondwi.shared.adapter.in.util.ControllerShortcuts.toOkResponse;
import static xyz.cupscoffee.hackathondwi.shared.adapter.in.util.ControllerShortcuts.toPaginatedResponse;
import static xyz.cupscoffee.hackathondwi.shared.domain.validation.Validator.validate;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import xyz.cupscoffee.hackathondwi.shared.adapter.annotations.RestControllerAdapter;
import xyz.cupscoffee.hackathondwi.shared.application.response.DetailedFailureResponse;
import xyz.cupscoffee.hackathondwi.shared.application.response.FailureResponse;
import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.Criteria;
import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.Filter;
import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.FilterOperator;
import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.Order;
import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.OrderType;
import xyz.cupscoffee.hackathondwi.shared.domain.validation.SimpleValidation;
import xyz.cupscoffee.hackathondwi.shared.domain.validation.ValidationError;
import xyz.cupscoffee.hackathondwi.student.core.adapter.in.request.CreateStudentRequest;
import xyz.cupscoffee.hackathondwi.student.core.adapter.in.request.UpdateStudentRequest;
import xyz.cupscoffee.hackathondwi.student.core.adapter.in.response.PaginatedStudentResponse;
import xyz.cupscoffee.hackathondwi.student.core.adapter.in.response.StudentContentResponse;
import xyz.cupscoffee.hackathondwi.student.core.application.port.in.CreateStudentPort;
import xyz.cupscoffee.hackathondwi.student.core.application.port.in.DeleteStudentPort;
import xyz.cupscoffee.hackathondwi.student.core.application.port.in.FindStudentPort;
import xyz.cupscoffee.hackathondwi.student.core.application.port.in.UpdateStudentPort;
import xyz.cupscoffee.hackathondwi.user.core.domain.model.User;

@Tag(name = "Student", description = "Student")
@RestControllerAdapter
@RequestMapping("/api/${spring.api.version}/student")
@RequiredArgsConstructor
public final class StudentController {
    private final CreateStudentPort createStudentPort;
    private final FindStudentPort findStudentPort;
    private final UpdateStudentPort updateStudentPort;
    private final DeleteStudentPort deleteStudentPort;

    /**
     * Get all students by user authenticated id.
     *
     * @param user authenticated user.
     * @return all students
     */
    @Operation(summary = "Get all students", description = "Get all students by criteria", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved students", content = @Content(schema = @Schema(implementation = PaginatedStudentResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = DetailedFailureResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @GetMapping
    public ResponseEntity<?> getAllByCriteria(
            @RequestParam(defaultValue = "10") @Parameter(description = "Limit (max 10)") Integer limit,
            @RequestParam(defaultValue = "1") @Parameter(description = "Page (min 1)") Integer page,
            @RequestParam(name = "first_name", required = false) @Parameter(description = "First name of the student") String firstName,
            @RequestParam(name = "last_name", required = false) @Parameter(description = "Last name of the student") String lastName,
            @RequestParam(required = false) @Parameter(description = "Code of the student") String code,
            @RequestParam(required = false) @Parameter(description = "Id of the student") Long id,
            @AuthenticationPrincipal User user) {
        var violations = new CopyOnWriteArrayList<ValidationError>();

        violations.addAll(
                validate(
                        new SimpleValidation(
                                limit < 1,
                                "limit",
                                "Limit must be greater than 0"),
                        new SimpleValidation(
                                limit > 10,
                                "limit",
                                "Limit must be less than or equal to 10"),
                        new SimpleValidation(
                                page < 1,
                                "page",
                                "Page must be greater than 0")));

        if (!violations.isEmpty()) {
            return toDetailedFailureResponse(violations);
        }

        var students = findStudentPort.match(
                new Criteria(
                        List.of(
                                Filter.of("courses.semester.user.id", FilterOperator.EQUAL, user.getId().toString()),
                                Filter.of("firstName", FilterOperator.LIKE, firstName),
                                Filter.of("lastName", FilterOperator.LIKE, lastName),
                                Filter.of("code", FilterOperator.LIKE, code),
                                Filter.of("id", FilterOperator.LIKE, id)),
                        Order.of("id", OrderType.DESC),
                        limit,
                        page));

        return toPaginatedResponse(
                PaginatedStudentResponse.class,
                students,
                "student.get.all.success",
                "Students retrieved successfully");
    }

    /**
     * Get student by id.
     *
     * @param id student id.
     * @return student
     */
    @Operation(summary = "Get student by id", description = "Get student by id", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved student", content = @Content(schema = @Schema(implementation = StudentContentResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = DetailedFailureResponse.class))),
            @ApiResponse(responseCode = "404", description = "Student not found", content = @Content(schema = @Schema(implementation = FailureResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id, @AuthenticationPrincipal User user) {
        var violations = new CopyOnWriteArrayList<ValidationError>();

        violations.addAll(
                validate(
                        new SimpleValidation(
                                id < 1,
                                "id",
                                "Id must be greater than 0")));

        if (!violations.isEmpty()) {
            return toDetailedFailureResponse(violations);
        }

        var result = findStudentPort.matchOne(
                Criteria.ofMatchOne(
                        new Filter("id", FilterOperator.EQUAL, id.toString()),
                        new Filter("courses.semester.user.id", FilterOperator.EQUAL, user.getId().toString())));

        if (result.isFailure()) {
            return toFailureResponse(result.getFailure());
        }

        return toOkResponse(
                StudentContentResponse.class,
                result.getSuccess(),
                "student.get.success",
                "Student retrieved successfully");
    }

    /**
     * Create student.
     *
     * @param request create student request.
     * @return created student
     */
    @Operation(summary = "Create student", description = "Create student", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully created student", content = @Content(schema = @Schema(implementation = StudentContentResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = DetailedFailureResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateStudentRequest request, @AuthenticationPrincipal User user) {
        var violations = request.validate();

        if (!violations.isEmpty()) {
            return toDetailedFailureResponse(violations);
        }

        var result = createStudentPort.create(request);

        if (result.isFailure()) {
            return toFailureResponse(result.getFailure());
        }

        return toOkResponse(
                StudentContentResponse.class,
                result.getSuccess(),
                "student.post.success",
                "Student created successfully");
    }

    /**
     * Update student.
     *
     * @param id      student id.
     * @param request update student request.
     * @return updated student
     */
    @Operation(summary = "Update student", description = "Update student", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully updated student", content = @Content(schema = @Schema(implementation = StudentContentResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = DetailedFailureResponse.class))),
            @ApiResponse(responseCode = "404", description = "Student not found", content = @Content(schema = @Schema(implementation = FailureResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody UpdateStudentRequest request) {
        var violations = request.validate();

        if (!violations.isEmpty()) {
            return toDetailedFailureResponse(violations);
        }

        var result = updateStudentPort.update(request);

        if (result.isFailure()) {
            return toFailureResponse(result.getFailure());
        }

        return toOkResponse(
                StudentContentResponse.class,
                result.getSuccess(),
                "student.put.success",
                "Student updated successfully");
    }

    /**
     * Delete student by id.
     *
     * @param id student id.
     * @return deleted student
     */
    @Operation(summary = "Delete student", description = "Delete student by id", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted student"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = DetailedFailureResponse.class))),
            @ApiResponse(responseCode = "404", description = "Student not found", content = @Content(schema = @Schema(implementation = FailureResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        var violations = new CopyOnWriteArrayList<ValidationError>();

        violations.addAll(
                validate(
                        new SimpleValidation(
                                id < 1,
                                "id",
                                "Id must be greater than 0")));

        if (!violations.isEmpty()) {
            return toDetailedFailureResponse(violations);
        }

        var result = deleteStudentPort.deleteById(id);

        if (result.isFailure()) {
            return toFailureResponse(result.getFailure());
        }

        return toOkResponse("student.delete.success", "Student deleted successfully");
    }
}
