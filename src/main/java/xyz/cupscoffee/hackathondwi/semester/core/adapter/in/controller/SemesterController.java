package xyz.cupscoffee.hackathondwi.semester.core.adapter.in.controller;

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
import xyz.cupscoffee.hackathondwi.semester.core.adapter.in.request.CreateSemesterRequest;
import xyz.cupscoffee.hackathondwi.semester.core.adapter.in.request.UpdateSemesterRequest;
import xyz.cupscoffee.hackathondwi.semester.core.adapter.in.response.PaginatedSemesterResponse;
import xyz.cupscoffee.hackathondwi.semester.core.adapter.in.response.SemesterContentResponse;
import xyz.cupscoffee.hackathondwi.semester.core.application.port.in.CreateSemesterPort;
import xyz.cupscoffee.hackathondwi.semester.core.application.port.in.DeleteSemesterPort;
import xyz.cupscoffee.hackathondwi.semester.core.application.port.in.FindSemesterPort;
import xyz.cupscoffee.hackathondwi.semester.core.application.port.in.UpdateSemesterPort;
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
import xyz.cupscoffee.hackathondwi.user.core.domain.model.User;

@Tag(name = "Semester", description = "Semester")
@RestControllerAdapter
@RequestMapping("/api/${spring.api.version}/semester")
@RequiredArgsConstructor
public final class SemesterController {
    private final CreateSemesterPort createSemesterPort;
    private final FindSemesterPort findSemesterPort;
    private final UpdateSemesterPort updateSemesterPort;
    private final DeleteSemesterPort deleteSemesterPort;

    /**
     * Get all semesters by user authenticated id.
     *
     * @param user authenticated user.
     * @return all semesters
     */
    @Operation(summary = "Get all semesters", description = "Get all semesters by user authenticated id", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved semesters", content = @Content(schema = @Schema(implementation = PaginatedSemesterResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = DetailedFailureResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @GetMapping
    public ResponseEntity<?> getAllByCriteria(
            @RequestParam(defaultValue = "10") @Parameter(description = "Limit (max 10)") Integer limit,
            @RequestParam(defaultValue = "1") @Parameter(description = "Page (min 1)") Integer page,
            @RequestParam(required = false) @Parameter(description = "Name of the semester") String name,
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

        var semesters = findSemesterPort.match(
                new Criteria(
                        List.of(
                                new Filter("user.id", FilterOperator.EQUAL, user.getId().toString()),
                                new Filter("name", FilterOperator.LIKE, name)),
                        Order.of("id", OrderType.DESC),
                        limit,
                        page));

        return toPaginatedResponse(
                PaginatedSemesterResponse.class,
                semesters,
                "semester.get.all.success",
                "Semesters retrieved successfully");
    }

    /**
     * Get semester by id.
     *
     * @param id semester id.
     * @return semester
     */
    @Operation(summary = "Get semester by id", description = "Get semester by id", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved semester", content = @Content(schema = @Schema(implementation = SemesterContentResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = DetailedFailureResponse.class))),
            @ApiResponse(responseCode = "404", description = "Semester not found", content = @Content(schema = @Schema(implementation = FailureResponse.class)))
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

        var result = findSemesterPort.matchOne(
                Criteria.ofMatchOne(
                        new Filter("id", FilterOperator.EQUAL, id.toString()),
                        new Filter("user.id", FilterOperator.EQUAL, user.getId().toString())));

        if (result.isFailure()) {
            return toFailureResponse(result.getFailure());
        }

        return toOkResponse(
                SemesterContentResponse.class,
                result.getSuccess(),
                "semester.get.success",
                "Semester retrieved successfully");
    }

    /**
     * Create semester.
     *
     * @param request create semester request.
     * @return created semester
     */
    @Operation(summary = "Create semester", description = "Create semester", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully created semester", content = @Content(schema = @Schema(implementation = SemesterContentResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = DetailedFailureResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateSemesterRequest request, @AuthenticationPrincipal User user) {
        var violations = request.validate();

        if (!violations.isEmpty()) {
            return toDetailedFailureResponse(violations);
        }

        var result = createSemesterPort.create(request, user.getId());

        if (result.isFailure()) {
            return toFailureResponse(result.getFailure());
        }

        return toOkResponse(
                SemesterContentResponse.class,
                result.getSuccess(),
                "semester.post.success",
                "Semester created successfully");
    }

    /**
     * Update semester.
     *
     * @param id      semester id.
     * @param request update semester request.
     * @return updated semester
     */
    @Operation(summary = "Update semester", description = "Update semester", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully updated semester", content = @Content(schema = @Schema(implementation = SemesterContentResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = DetailedFailureResponse.class))),
            @ApiResponse(responseCode = "404", description = "Semester not found", content = @Content(schema = @Schema(implementation = FailureResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody UpdateSemesterRequest request) {
        var violations = request.validate();

        if (!violations.isEmpty()) {
            return toDetailedFailureResponse(violations);
        }

        var result = updateSemesterPort.update(request);

        if (result.isFailure()) {
            return toFailureResponse(result.getFailure());
        }

        return toOkResponse(
                SemesterContentResponse.class,
                result.getSuccess(),
                "semester.put.success",
                "Semester updated successfully");
    }

    /**
     * Delete semester by id.
     *
     * @param id semester id.
     * @return deleted semester
     */
    @Operation(summary = "Delete semester", description = "Delete semester by id", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted semester"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = DetailedFailureResponse.class))),
            @ApiResponse(responseCode = "404", description = "Semester not found", content = @Content(schema = @Schema(implementation = FailureResponse.class))),
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

        var result = deleteSemesterPort.deleteById(id);

        if (result.isFailure()) {
            return toFailureResponse(result.getFailure());
        }

        return toOkResponse("semester.delete.success", "Semester deleted successfully");
    }
}
