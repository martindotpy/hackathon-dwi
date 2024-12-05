package xyz.cupscoffee.hackathondwi.shared.adapter.validator;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Component;

import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.cupscoffee.hackathondwi.shared.domain.payload.Payload;
import xyz.cupscoffee.hackathondwi.shared.domain.validation.ExternalPayloadValidator;
import xyz.cupscoffee.hackathondwi.shared.domain.validation.ValidationError;

@Slf4j
@Component
@RequiredArgsConstructor
public class JakartaValidator implements ExternalPayloadValidator {
    private final Validator validator;

    @Override
    public <T extends Payload> List<ValidationError> validate(T payload) {
        log.info("Validating payload `{}`", payload.getClass().getSimpleName());

        var violations = validator.validate(payload);

        if (violations.isEmpty()) {
            return new CopyOnWriteArrayList<>();
        }

        var validationErrors = new CopyOnWriteArrayList<ValidationError>();

        violations.forEach(violation -> {
            Objects.requireNonNull(violation, "Violation cannot be null");
            Objects.requireNonNull(violation.getPropertyPath(), "Property path cannot be null");
            Objects.requireNonNull(violation.getMessage(), "Message cannot be null");

            String path = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            String requestClassName = payload.getClass().getSimpleName();

            log.debug("Violation on path `{}` of type `{}`",
                    path,
                    requestClassName);

            validationErrors.add(new ValidationError(requestClassName + "." + path, message));
        });

        return validationErrors;
    }
}
