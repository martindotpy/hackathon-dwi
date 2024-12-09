package xyz.cupscoffee.hackathondwi.exam.question.adapter.in.controller;

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
import xyz.cupscoffee.hackathondwi.exam.question.adapter.in.request.CreateQuestionRequest;
import xyz.cupscoffee.hackathondwi.exam.question.adapter.in.request.UpdateQuestionRequest;
import xyz.cupscoffee.hackathondwi.exam.question.adapter.in.response.PaginatedQuestionResponse;
import xyz.cupscoffee.hackathondwi.exam.question.adapter.in.response.QuestionContentResponse;
import xyz.cupscoffee.hackathondwi.exam.question.application.port.in.CreateQuestionPort;
import xyz.cupscoffee.hackathondwi.exam.question.application.port.in.DeleteQuestionPort;
import xyz.cupscoffee.hackathondwi.exam.question.application.port.in.FindQuestionPort;
import xyz.cupscoffee.hackathondwi.exam.question.application.port.in.UpdateQuestionPort;
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

@Tag(name = "Question", description = "Question")
@RestControllerAdapter
@RequestMapping("/api/${spring.api.version}/question")
@RequiredArgsConstructor
public final class QuestionController {
    private final CreateQuestionPort createQuestionPort;
    private final FindQuestionPort findQuestionPort;
    private final UpdateQuestionPort updateQuestionPort;
    private final DeleteQuestionPort deleteQuestionPort;

    /**
     * Get all questions by user authenticated id.
     *
     * @param user authenticated user.
     * @return all questions
     */
    @Operation(summary = "Get all questions", description = "Get all questions by criteria", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved questions", content = @Content(schema = @Schema(implementation = PaginatedQuestionResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = DetailedFailureResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @GetMapping
    public ResponseEntity<?> getAllByCriteria(
            @RequestParam(name = "exam_id") @Parameter(description = "Exam id") Long examId,
            @RequestParam(defaultValue = "10") @Parameter(description = "Limit (max 10)") Integer limit,
            @RequestParam(defaultValue = "1") @Parameter(description = "Page (min 1)") Integer page,
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
                                examId < 1,
                                "examId",
                                "Exam id must be greater than 0")));

        if (!violations.isEmpty()) {
            return toDetailedFailureResponse(violations);
        }

        var questions = findQuestionPort.match(
                new Criteria(
                        List.of(
                                new Filter("exam.id", FilterOperator.EQUAL, examId.toString()),
                                new Filter("exam.course.semester.user.id", FilterOperator.EQUAL,
                                        user.getId().toString())),
                        Order.none(),
                        limit,
                        page));

        return toPaginatedResponse(
                PaginatedQuestionResponse.class,
                questions,
                "question.get.all.success",
                "Questions retrieved successfully");
    }

    /**
     * Get question by id.
     *
     * @param id question id.
     * @return question
     */
    @Operation(summary = "Get question by id", description = "Get question by id", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved question", content = @Content(schema = @Schema(implementation = QuestionContentResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = DetailedFailureResponse.class))),
            @ApiResponse(responseCode = "404", description = "Question not found", content = @Content(schema = @Schema(implementation = FailureResponse.class)))
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

        var result = findQuestionPort.matchOne(
                Criteria.ofMatchOne(
                        new Filter("id", FilterOperator.EQUAL, id.toString()),
                        new Filter("exam.course.semester.user.id", FilterOperator.EQUAL, user.getId().toString())));

        if (result.isFailure()) {
            return toFailureResponse(result.getFailure());
        }

        return toOkResponse(
                QuestionContentResponse.class,
                result.getSuccess(),
                "question.get.success",
                "Question retrieved successfully");
    }

    /**
     * Create question.
     *
     * @param request create question request.
     * @return created question
     */
    @Operation(summary = "Create question", description = "Create question", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully created question", content = @Content(schema = @Schema(implementation = QuestionContentResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = DetailedFailureResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateQuestionRequest request, @AuthenticationPrincipal User user) {
        var violations = request.validate();

        if (!violations.isEmpty()) {
            return toDetailedFailureResponse(violations);
        }

        var result = createQuestionPort.create(request);

        if (result.isFailure()) {
            return toFailureResponse(result.getFailure());
        }

        return toOkResponse(
                QuestionContentResponse.class,
                result.getSuccess(),
                "question.post.success",
                "Question created successfully");
    }

    /**
     * Update question.
     *
     * @param id      question id.
     * @param request update question request.
     * @return updated question
     */
    @Operation(summary = "Update question", description = "Update question", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully updated question", content = @Content(schema = @Schema(implementation = QuestionContentResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = DetailedFailureResponse.class))),
            @ApiResponse(responseCode = "404", description = "Question not found", content = @Content(schema = @Schema(implementation = FailureResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody UpdateQuestionRequest request) {
        var violations = request.validate();

        if (!violations.isEmpty()) {
            return toDetailedFailureResponse(violations);
        }

        var result = updateQuestionPort.update(request);

        if (result.isFailure()) {
            return toFailureResponse(result.getFailure());
        }

        return toOkResponse(
                QuestionContentResponse.class,
                result.getSuccess(),
                "question.put.success",
                "Question updated successfully");
    }

    /**
     * Delete question by id.
     *
     * @param id question id.
     * @return deleted question
     */
    @Operation(summary = "Delete question", description = "Delete question by id", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted question"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = DetailedFailureResponse.class))),
            @ApiResponse(responseCode = "404", description = "Question not found", content = @Content(schema = @Schema(implementation = FailureResponse.class))),
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

        var result = deleteQuestionPort.deleteById(id);

        if (result.isFailure()) {
            return toFailureResponse(result.getFailure());
        }

        return toOkResponse("question.delete.success", "Question deleted successfully");
    }
}
