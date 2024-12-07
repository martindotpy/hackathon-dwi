package xyz.cupscoffee.hackathondwi.shared.domain.validation;

import java.util.List;

/**
 * Validator class.
 *
 * @see Validation
 */
public final class Validator {
    public static List<ValidationError> validate(Validation... validations) {
        return List.of(validations).stream()
                .map(Validation::validate)
                .flatMap(List::stream)
                .toList();
    }
}
