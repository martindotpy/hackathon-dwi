package xyz.cupscoffee.hackathondwi.exam.answer.adapter.in.controller;

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
import xyz.cupscoffee.hackathondwi.exam.answer.adapter.in.request.CreateAnswerRequest;
import xyz.cupscoffee.hackathondwi.exam.answer.adapter.in.request.UpdateAnswerRequest;
import xyz.cupscoffee.hackathondwi.exam.answer.adapter.in.response.AnswerContentResponse;
import xyz.cupscoffee.hackathondwi.exam.answer.adapter.in.response.PaginatedAnswerResponse;
import xyz.cupscoffee.hackathondwi.exam.answer.application.port.in.CreateAnswerPort;
import xyz.cupscoffee.hackathondwi.exam.answer.application.port.in.DeleteAnswerPort;
import xyz.cupscoffee.hackathondwi.exam.answer.application.port.in.FindAnswerPort;
import xyz.cupscoffee.hackathondwi.exam.answer.application.port.in.UpdateAnswerPort;
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

@Tag(name = "Answer", description = "Answer")
@RestControllerAdapter
@RequestMapping("/api/${spring.api.version}/answer")
@RequiredArgsConstructor
public final class AnswerController {
    private final CreateAnswerPort createAnswerPort;
    private final FindAnswerPort findAnswerPort;
    private final UpdateAnswerPort updateAnswerPort;
    private final DeleteAnswerPort deleteAnswerPort;

    /**
     * Get all answers by user authenticated id.
     *
     * @param user authenticated user.
     * @return all answers
     */
    @Operation(summary = "Get all answers", description = "Get all answers by criteria", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved answers", content = @Content(schema = @Schema(implementation = PaginatedAnswerResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = DetailedFailureResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @GetMapping
    public ResponseEntity<?> getAllByCriteria(
            @RequestParam(name = "exam_id") @Parameter(description = "Exam id") Long examId,
            @RequestParam(defaultValue = "10") @Parameter(description = "Limit (max 10)") Integer limit,
            @RequestParam(defaultValue = "1") @Parameter(description = "Page (min 1)") Integer page,
            @RequestParam(required = false) @Parameter(description = "Name of the answer") String name,
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

        var answers = findAnswerPort.match(
                new Criteria(
                        List.of(
                                new Filter("exam.id", FilterOperator.EQUAL, examId.toString()),
                                new Filter("semester.exam.user.id", FilterOperator.EQUAL, user.getId().toString()),
                                new Filter("name", FilterOperator.LIKE, name)),
                        Order.none(),
                        limit,
                        page));

        return toPaginatedResponse(
                PaginatedAnswerResponse.class,
                answers,
                "answer.get.all.success",
                "Answers retrieved successfully");
    }

    /**
     * Get answer by id.
     *
     * @param id answer id.
     * @return answer
     */
    @Operation(summary = "Get answer by id", description = "Get answer by id", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved answer", content = @Content(schema = @Schema(implementation = AnswerContentResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = DetailedFailureResponse.class))),
            @ApiResponse(responseCode = "404", description = "Answer not found", content = @Content(schema = @Schema(implementation = FailureResponse.class)))
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

        var result = findAnswerPort.matchOne(
                Criteria.ofMatchOne(
                        new Filter("id", FilterOperator.EQUAL, id.toString()),
                        new Filter("exam.user.id", FilterOperator.EQUAL, user.getId().toString())));

        if (result.isFailure()) {
            return toFailureResponse(result.getFailure());
        }

        return toOkResponse(
                AnswerContentResponse.class,
                result.getSuccess(),
                "answer.get.success",
                "Answer retrieved successfully");
    }

    /**
     * Create answer.
     *
     * @param request create answer request.
     * @return created answer
     */
    @Operation(summary = "Create answer", description = "Create answer", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully created answer", content = @Content(schema = @Schema(implementation = AnswerContentResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = DetailedFailureResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateAnswerRequest request, @AuthenticationPrincipal User user) {
        var violations = request.validate();

        if (!violations.isEmpty()) {
            return toDetailedFailureResponse(violations);
        }

        var result = createAnswerPort.create(request);

        if (result.isFailure()) {
            return toFailureResponse(result.getFailure());
        }

        return toOkResponse(
                AnswerContentResponse.class,
                result.getSuccess(),
                "answer.post.success",
                "Answer created successfully");
    }

    /**
     * Update answer.
     *
     * @param id      answer id.
     * @param request update answer request.
     * @return updated answer
     */
    @Operation(summary = "Update answer", description = "Update answer", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully updated answer", content = @Content(schema = @Schema(implementation = AnswerContentResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = DetailedFailureResponse.class))),
            @ApiResponse(responseCode = "404", description = "Answer not found", content = @Content(schema = @Schema(implementation = FailureResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody UpdateAnswerRequest request) {
        var violations = request.validate();

        if (!violations.isEmpty()) {
            return toDetailedFailureResponse(violations);
        }

        var result = updateAnswerPort.update(request);

        if (result.isFailure()) {
            return toFailureResponse(result.getFailure());
        }

        return toOkResponse(
                AnswerContentResponse.class,
                result.getSuccess(),
                "answer.put.success",
                "Answer updated successfully");
    }

    /**
     * Delete answer by id.
     *
     * @param id answer id.
     * @return deleted answer
     */
    @Operation(summary = "Delete answer", description = "Delete answer by id", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted answer"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = DetailedFailureResponse.class))),
            @ApiResponse(responseCode = "404", description = "Answer not found", content = @Content(schema = @Schema(implementation = FailureResponse.class))),
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

        var result = deleteAnswerPort.deleteById(id);

        if (result.isFailure()) {
            return toFailureResponse(result.getFailure());
        }

        return toOkResponse("answer.delete.success", "Answer deleted successfully");
    }
}
