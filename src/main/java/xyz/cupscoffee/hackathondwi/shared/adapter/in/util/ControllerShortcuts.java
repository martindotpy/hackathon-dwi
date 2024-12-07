package xyz.cupscoffee.hackathondwi.shared.adapter.in.util;

import static org.fusesource.jansi.Ansi.ansi;

import java.util.List;
import java.util.Objects;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import xyz.cupscoffee.hackathondwi.shared.adapter.in.status.HttpStatusCodeFailureProvider;
import xyz.cupscoffee.hackathondwi.shared.application.response.BasicResponse;
import xyz.cupscoffee.hackathondwi.shared.application.response.ContentResponse;
import xyz.cupscoffee.hackathondwi.shared.application.response.DetailedFailureResponse;
import xyz.cupscoffee.hackathondwi.shared.application.response.FailureResponse;
import xyz.cupscoffee.hackathondwi.shared.application.response.PaginatedResponse;
import xyz.cupscoffee.hackathondwi.shared.domain.query.failure.Failure;
import xyz.cupscoffee.hackathondwi.shared.domain.query.paginated.Paginated;
import xyz.cupscoffee.hackathondwi.shared.domain.validation.ValidationError;

/**
 * Utility class for controller shortcuts.
 */
@Slf4j
@Component
public final class ControllerShortcuts {
    private static MessageSource messageSource;
    private static ObjectMapper objectMapper;

    private ControllerShortcuts(MessageSource messageSource, ObjectMapper objectMapper) {
        ControllerShortcuts.messageSource = messageSource;
        ControllerShortcuts.objectMapper = objectMapper;
    }

    /**
     * Content response with a message.
     *
     * <p>
     * Use this method to define the type of content response and validate it in
     * compile time.
     * </p>
     *
     * @param <T>             the type of the body.
     * @param <R>             the type of the content response.
     * @param contentResponse the content response class.
     * @param body            the body.
     * @param code            the message code.
     * @param defaultMessage  the default message.
     * @return the response
     */
    public static <T, R extends ContentResponse<T>> ResponseEntity<?> toOkResponse(
            Class<R> contentResponse,
            T body,
            String code,
            String defaultMessage) {
        return toOkResponse(body, code, defaultMessage);
    }

    /**
     * Content response with a message.
     *
     * @param body           the body.
     * @param code           the message code.
     * @param defaultMessage the default message.
     * @return the response
     */
    public static ResponseEntity<?> toOkResponse(
            Object body,
            String code,
            String defaultMessage) {
        String message = messageSource.getMessage(
                code,
                null,
                null,
                LocaleContextHolder.getLocale());

        if (message == null) {
            log.warn("The message code is empty, using the default message `{}`", defaultMessage);

            message = defaultMessage;
        }

        log.info("Creating OK response with message `{}` and body `{}`", code, body.getClass().getSimpleName());

        logBody(body);

        return ResponseEntity.ok(
                ContentResponse.builder()
                        .message(message)
                        .content(body)
                        .build());
    }

    /**
     * Basic response with a simple message.
     *
     * @param code the message.
     * @return the response
     */
    public static ResponseEntity<?> toOkResponse(String code, String defaultMessage) {
        log.info("Creating OK response with message `{}`", code);

        return ResponseEntity.ok(
                BasicResponse.builder()
                        .message(code)
                        .build());
    }

    /**
     * Paginated response with a message.
     *
     * <p>
     * Use this method to define the type of paginated content response and validate
     * it in compile time.
     * </p>
     *
     * @param <T>                      the type of the entity.
     * @param <P>                      the type of the paginated content.
     * @param <R>                      the type of the paginated content response.
     * @param paginatedContentResponse the paginated content response class.
     * @param paginated                the paginated content.
     * @param message                  the message.
     * @return the response
     */
    public static <T, P extends Paginated<T>, R extends PaginatedResponse<T>> ResponseEntity<?> toPaginatedResponse(
            Class<R> paginatedContentResponse,
            P paginated, String message) {
        return toPaginatedResponse(paginated, message);
    }

    /**
     * Basic response with a simple message.
     *
     * @param paginated the paginated content.
     * @param message   the message.
     * @return the response
     */
    public static ResponseEntity<?> toPaginatedResponse(Paginated<?> paginated, String message) {
        log.info("Creating paginated response with message `{}` with entity `{}`", message,
                paginated.getContent().getClass().getSimpleName());

        return ResponseEntity.ok(
                PaginatedResponse.from(paginated, message));
    }

    /**
     * Create a basic failure response.
     *
     * @param message    the message.
     * @param httpStatus the HTTP status.
     * @throws IllegalArgumentException if the message or HTTP status is null.
     * @return the response.
     */
    public static ResponseEntity<?> toFailureResponse(String message, HttpStatus httpStatus) {
        Objects.requireNonNull(httpStatus, "The HTTP status cannot be null");
        Objects.requireNonNull(httpStatus, "The HTTP status cannot be null");

        log.info("Creating failure response with message `{}` and HTTP status `{}`", message, httpStatus);

        return ResponseEntity.status(httpStatus)
                .body(FailureResponse.builder()
                        .message(message)
                        .build());
    }

    /**
     * Create a detailed basic failure response.
     *
     * @param validationErrors the validation errors.
     * @return the response.
     */
    public static ResponseEntity<?> toDetailedFailureResponse(List<ValidationError> validationErrors) {
        Objects.requireNonNull(validationErrors, "The validation errors cannot be null");

        List<String> details = validationErrors.stream()
                .map(validationError -> validationError.getField() + ": " + validationError.getMessage())
                .toList();

        log.info("Creating validation failure response with validation errors `{}`", details);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(DetailedFailureResponse.builder()
                        .message("Errores en la consulta")
                        .details(details)
                        .build());
    }

    /**
     * Create a basic failure response.
     *
     * @param failure    the failure.
     * @param httpStatus the HTTP status.
     * @throws IllegalArgumentException if the failure or HTTP status is null.
     * @return the response.
     */
    public static ResponseEntity<?> toFailureResponse(Failure failure, HttpStatus httpStatus) {
        Objects.requireNonNull(failure, "The failure cannot be null");

        return toFailureResponse(failure.getMessage(), httpStatus);
    }

    /**
     * Create a basic failure response.
     *
     * @param failure    the failure.
     * @param httpStatus the HTTP status.
     * @throws IllegalArgumentException if the failure is null.
     * @return the response.
     */
    public static ResponseEntity<?> toFailureResponse(Failure failure, int httpStatus) {
        Objects.requireNonNull(failure, "The failure cannot be null");

        log.info("Creating failure response of `{}` and HTTP status `{}` for `{}`",
                failure,
                httpStatus,
                failure.getClass().getSimpleName());

        String failureName = cleanFailureName(failure.getClass().getSimpleName())
                + ".failure."
                + failure.toString().toLowerCase();

        String message = messageSource.getMessage(
                failureName,
                null,
                failure.getMessage(),
                LocaleContextHolder.getLocale());

        var failureResponse = FailureResponse.builder()
                .message(message)
                .build();

        logBody(failureResponse);

        return ResponseEntity.status(httpStatus)
                .body(failureResponse);
    }

    /**
     * Create a basic failure response.
     *
     * @param failure the failure.
     * @throws IllegalArgumentException if the failure is null.
     * @return the response.
     */
    public static ResponseEntity<?> toFailureResponse(Failure failure) {
        return toFailureResponse(failure, HttpStatusCodeFailureProvider.get(failure));
    }

    /**
     * Create a basic failure response.
     *
     * @param file     the file.
     * @param fileName the file name.
     * @return the response.
     */
    public static ResponseEntity<byte[]> toFileResponse(byte[] file, String fileName) {
        log.info("Creating file response with file name `{}`", fileName);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=" + fileName);

        return new ResponseEntity<>(file, headers, HttpStatus.OK);
    }

    private static String cleanFailureName(String failureName) {
        return failureName.replace("Failure", "").toLowerCase();
    }

    private static void logBody(Object body) {
        try {
            log.debug("Body: `{}`",
                    ansi().fgYellow().a(objectMapper.writeValueAsString(body)).reset());
        } catch (JsonProcessingException e) {
            log.error("Error while serializing body", e);
        }
    }
}
