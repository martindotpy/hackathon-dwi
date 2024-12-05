package xyz.cupscoffee.hackathondwi.shared.domain.payload;

import java.util.List;

import xyz.cupscoffee.hackathondwi.shared.domain.validation.ExternalPayloadValidatorProvider;
import xyz.cupscoffee.hackathondwi.shared.domain.validation.ValidationError;

/**
 * Marker interface for payloads.
 */
public interface Payload {
    default List<ValidationError> validate() {
        return ExternalPayloadValidatorProvider.get().validate(this);
    }
}
