package xyz.cupscoffee.hackathondwi.exam.core.adapter.in.controller;

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
import xyz.cupscoffee.hackathondwi.exam.core.adapter.in.request.CreateExamRequest;
import xyz.cupscoffee.hackathondwi.exam.core.adapter.in.request.UpdateExamRequest;
import xyz.cupscoffee.hackathondwi.exam.core.adapter.in.response.ExamContentResponse;
import xyz.cupscoffee.hackathondwi.exam.core.adapter.in.response.PaginatedExamResponse;
import xyz.cupscoffee.hackathondwi.exam.core.application.port.in.CreateExamPort;
import xyz.cupscoffee.hackathondwi.exam.core.application.port.in.DeleteExamPort;
import xyz.cupscoffee.hackathondwi.exam.core.application.port.in.FindExamPort;
import xyz.cupscoffee.hackathondwi.exam.core.application.port.in.UpdateExamPort;
import xyz.cupscoffee.hackathondwi.shared.adapter.annotations.RestControllerAdapter;
import xyz.cupscoffee.hackathondwi.shared.application.response.DetailedFailureResponse;
import xyz.cupscoffee.hackathondwi.shared.application.response.FailureResponse;
import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.Criteria;
import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.Filter;
import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.FilterOperator;
import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.Order;
import xyz.cupscoffee.hackathondwi.shared.domain.validation.SimpleValidation;
import xyz.cupscoffee.hackathondwi.shared.domain.validation.ValidationError;
import xyz.cupscoffee.hackathondwi.user.core.domain.model.User;

@Tag(name = "Exam", description = "Exam")
@RestControllerAdapter
@RequestMapping("/api/${spring.api.version}/exam")
@RequiredArgsConstructor
public final class ExamController {
    private final CreateExamPort createExamPort;
    private final FindExamPort findExamPort;
    private final UpdateExamPort updateExamPort;
    private final DeleteExamPort deleteExamPort;

    /**
     * Get all exams by user authenticated id.
     *
     * @param user authenticated user.
     * @return all exams
     */
    @Operation(summary = "Get all exams", description = "Get all exams by criteria", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved exams", content = @Content(schema = @Schema(implementation = PaginatedExamResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = DetailedFailureResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @GetMapping
    public ResponseEntity<?> getAllByCriteria(
            @RequestParam(name = "course_id") @Parameter(description = "Course id") Long courseId,
            @RequestParam(defaultValue = "10") @Parameter(description = "Limit (max 10)") Integer limit,
            @RequestParam(defaultValue = "1") @Parameter(description = "Page (min 1)") Integer page,
            @RequestParam(required = false) @Parameter(description = "Name of the exam") String name,
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
                                "Page must be greater than 0"),
                        new SimpleValidation(
                                courseId < 1,
                                "courseId",
                                "Course id must be greater than 0")));

        if (!violations.isEmpty()) {
            return toDetailedFailureResponse(violations);
        }

        var exams = findExamPort.match(
                new Criteria(
                        List.of(
                                new Filter("course.id", FilterOperator.EQUAL, courseId.toString()),
                                new Filter("course.semester.user.id", FilterOperator.EQUAL, user.getId().toString()),
                                new Filter("name", FilterOperator.LIKE, name)),
                        Order.none(),
                        limit,
                        page));

        return toPaginatedResponse(
                PaginatedExamResponse.class,
                exams,
                "exam.get.all.success",
                "Exams retrieved successfully");
    }

    /**
     * Get exam by id.
     *
     * @param id exam id.
     * @return exam
     */
    @Operation(summary = "Get exam by id", description = "Get exam by id", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved exam", content = @Content(schema = @Schema(implementation = ExamContentResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = DetailedFailureResponse.class))),
            @ApiResponse(responseCode = "404", description = "Exam not found", content = @Content(schema = @Schema(implementation = FailureResponse.class)))
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

        var result = findExamPort.matchOne(
                Criteria.ofMatchOne(
                        new Filter("id", FilterOperator.EQUAL, id.toString()),
                        new Filter("course.semester.user.id", FilterOperator.EQUAL, user.getId().toString())));

        if (result.isFailure()) {
            return toFailureResponse(result.getFailure());
        }

        return toOkResponse(
                ExamContentResponse.class,
                result.getSuccess(),
                "exam.get.success",
                "Exam retrieved successfully");
    }

    /**
     * Create exam.
     *
     * @param request create exam request.
     * @return created exam
     */
    @Operation(summary = "Create exam", description = "Create exam", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully created exam", content = @Content(schema = @Schema(implementation = ExamContentResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = DetailedFailureResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateExamRequest request, @AuthenticationPrincipal User user) {
        var violations = request.validate();

        if (!violations.isEmpty()) {
            return toDetailedFailureResponse(violations);
        }

        var result = createExamPort.create(request);

        if (result.isFailure()) {
            return toFailureResponse(result.getFailure());
        }

        return toOkResponse(
                ExamContentResponse.class,
                result.getSuccess(),
                "exam.post.success",
                "Exam created successfully");
    }

    /**
     * Update exam.
     *
     * @param id      exam id.
     * @param request update exam request.
     * @return updated exam
     */
    @Operation(summary = "Update exam", description = "Update exam", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully updated exam", content = @Content(schema = @Schema(implementation = ExamContentResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = DetailedFailureResponse.class))),
            @ApiResponse(responseCode = "404", description = "Exam not found", content = @Content(schema = @Schema(implementation = FailureResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody UpdateExamRequest request) {
        var violations = request.validate();

        if (!violations.isEmpty()) {
            return toDetailedFailureResponse(violations);
        }

        var result = updateExamPort.update(request);

        if (result.isFailure()) {
            return toFailureResponse(result.getFailure());
        }

        return toOkResponse(
                ExamContentResponse.class,
                result.getSuccess(),
                "exam.put.success",
                "Exam updated successfully");
    }

    /**
     * Delete exam by id.
     *
     * @param id exam id.
     * @return deleted exam
     */
    @Operation(summary = "Delete exam", description = "Delete exam by id", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted exam"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = DetailedFailureResponse.class))),
            @ApiResponse(responseCode = "404", description = "Exam not found", content = @Content(schema = @Schema(implementation = FailureResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        var violations = new CopyOnWriteArrayList<ValidationError>();

        violations.addAll(
                validate(
                        new SimpleValidation(
                                id > 0,
                                "id",
                                "Id must be greater than 0")));

        if (!violations.isEmpty()) {
            return toDetailedFailureResponse(violations);
        }

        var result = deleteExamPort.deleteById(id);

        if (result.isFailure()) {
            return toFailureResponse(result.getFailure());
        }

        return toOkResponse("exam.delete.success", "Exam deleted successfully");
    }
}
