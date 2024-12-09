package xyz.cupscoffee.hackathondwi.semester.course.adapter.in.controller;

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
import xyz.cupscoffee.hackathondwi.semester.core.application.port.in.FindSemesterPort;
import xyz.cupscoffee.hackathondwi.semester.course.adapter.in.request.CreateCourseRequest;
import xyz.cupscoffee.hackathondwi.semester.course.adapter.in.request.UpdateCourseRequest;
import xyz.cupscoffee.hackathondwi.semester.course.adapter.in.response.CourseContentResponse;
import xyz.cupscoffee.hackathondwi.semester.course.adapter.in.response.PaginatedCourseResponse;
import xyz.cupscoffee.hackathondwi.semester.course.application.port.in.CreateCoursePort;
import xyz.cupscoffee.hackathondwi.semester.course.application.port.in.DeleteCoursePort;
import xyz.cupscoffee.hackathondwi.semester.course.application.port.in.FindCoursePort;
import xyz.cupscoffee.hackathondwi.semester.course.application.port.in.UpdateCoursePort;
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
import xyz.cupscoffee.hackathondwi.student.core.adapter.in.response.StudentsContentResponse;
import xyz.cupscoffee.hackathondwi.student.core.application.port.in.FindStudentPort;
import xyz.cupscoffee.hackathondwi.user.core.domain.model.User;

@Tag(name = "Course", description = "Course")
@RestControllerAdapter
@RequestMapping("/api/${spring.api.version}/course")
@RequiredArgsConstructor
public final class CourseController {
    private final CreateCoursePort createCoursePort;
    private final FindCoursePort findCoursePort;
    private final FindSemesterPort findSemesterPort;
    private final FindStudentPort findStudentPort;
    private final UpdateCoursePort updateCoursePort;
    private final DeleteCoursePort deleteCoursePort;

    /**
     * Get all courses by user authenticated id.
     *
     * @param user authenticated user.
     * @return all courses
     */
    @Operation(summary = "Get all courses", description = "Get all courses by criteria", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved courses", content = @Content(schema = @Schema(implementation = PaginatedCourseResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = DetailedFailureResponse.class))),
            @ApiResponse(responseCode = "404", description = "Semester not found", content = @Content(schema = @Schema(implementation = FailureResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @GetMapping
    public ResponseEntity<?> getAllByCriteria(
            @RequestParam(name = "semester_id") @Parameter(description = "Semester id") Long semesterId,
            @RequestParam(defaultValue = "10") @Parameter(description = "Limit (max 10)") Integer limit,
            @RequestParam(defaultValue = "1") @Parameter(description = "Page (min 1)") Integer page,
            @RequestParam(required = false) @Parameter(description = "Id of the course") Long id,
            @RequestParam(required = false) @Parameter(description = "Name of the course") String name,
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
                                semesterId < 1,
                                "semesterId",
                                "Semester id must be greater than 0")));

        if (!violations.isEmpty()) {
            return toDetailedFailureResponse(violations);
        }

        // Verify if the user is the owner of the semester
        var semesterResult = findSemesterPort.matchOne(
                Criteria.ofMatchOne(
                        Filter.of("id", FilterOperator.EQUAL, semesterId),
                        Filter.of("user.id", FilterOperator.EQUAL, user.getId().toString())));

        if (semesterResult.isFailure()) {
            return toFailureResponse(semesterResult.getFailure());
        }

        var courses = findCoursePort.match(
                new Criteria(
                        List.of(
                                new Filter("semester.id", FilterOperator.EQUAL, semesterId.toString()),
                                new Filter("semester.user.id", FilterOperator.EQUAL, user.getId().toString()),
                                new Filter("name", FilterOperator.LIKE, name),
                                Filter.of("id", FilterOperator.LIKE, id)),
                        Order.of("id", OrderType.DESC),
                        limit,
                        page));

        return toPaginatedResponse(
                PaginatedCourseResponse.class,
                courses,
                "course.get.all.success",
                "Courses retrieved successfully");
    }

    /**
     * Get course by id.
     *
     * @param id course id.
     * @return course
     */
    @Operation(summary = "Get course by id", description = "Get course by id", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved course", content = @Content(schema = @Schema(implementation = CourseContentResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = DetailedFailureResponse.class))),
            @ApiResponse(responseCode = "404", description = "Course not found", content = @Content(schema = @Schema(implementation = FailureResponse.class)))
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

        var result = findCoursePort.matchOne(
                Criteria.ofMatchOne(
                        new Filter("id", FilterOperator.EQUAL, id.toString()),
                        new Filter("semester.user.id", FilterOperator.EQUAL, user.getId().toString())));

        if (result.isFailure()) {
            return toFailureResponse(result.getFailure());
        }

        return toOkResponse(
                CourseContentResponse.class,
                result.getSuccess(),
                "course.get.success",
                "Course retrieved successfully");
    }

    /**
     * Get all students by course id.
     *
     * @param id course id.
     * @return all students
     */
    @Operation(summary = "Get all students by course id", description = "Get all students by course id", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved students", content = @Content(schema = @Schema(implementation = StudentsContentResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = DetailedFailureResponse.class))),
    })
    @GetMapping("/{id}/student")
    public ResponseEntity<?> getAllStudentsByCourseId(@PathVariable Long id) {
        var students = findStudentPort.findAllByCourseId(id);

        return toOkResponse(
                StudentsContentResponse.class,
                students,
                "course.get.all.students.success",
                "Students retrieved successfully");
    }

    /**
     * Create course.
     *
     * @param request create course request.
     * @return created course
     */
    @Operation(summary = "Create course", description = "Create course", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully created course", content = @Content(schema = @Schema(implementation = CourseContentResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = DetailedFailureResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateCourseRequest request, @AuthenticationPrincipal User user) {
        var violations = request.validate();

        if (!violations.isEmpty()) {
            return toDetailedFailureResponse(violations);
        }

        var result = createCoursePort.create(request);

        if (result.isFailure()) {
            return toFailureResponse(result.getFailure());
        }

        return toOkResponse(
                CourseContentResponse.class,
                result.getSuccess(),
                "course.post.success",
                "Course created successfully");
    }

    /**
     * Update course.
     *
     * @param id      course id.
     * @param request update course request.
     * @return updated course
     */
    @Operation(summary = "Update course", description = "Update course", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully updated course", content = @Content(schema = @Schema(implementation = CourseContentResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = DetailedFailureResponse.class))),
            @ApiResponse(responseCode = "404", description = "Course not found", content = @Content(schema = @Schema(implementation = FailureResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody UpdateCourseRequest request) {
        var violations = request.validate();

        if (!violations.isEmpty()) {
            return toDetailedFailureResponse(violations);
        }

        var result = updateCoursePort.update(request);

        if (result.isFailure()) {
            return toFailureResponse(result.getFailure());
        }

        return toOkResponse(
                CourseContentResponse.class,
                result.getSuccess(),
                "course.put.success",
                "Course updated successfully");
    }

    /**
     * Delete course by id.
     *
     * @param id course id.
     * @return deleted course
     */
    @Operation(summary = "Delete course", description = "Delete course by id", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted course"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = DetailedFailureResponse.class))),
            @ApiResponse(responseCode = "404", description = "Course not found", content = @Content(schema = @Schema(implementation = FailureResponse.class))),
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

        var result = deleteCoursePort.deleteById(id);

        if (result.isFailure()) {
            return toFailureResponse(result.getFailure());
        }

        return toOkResponse("course.delete.success", "Course deleted successfully");
    }
}
