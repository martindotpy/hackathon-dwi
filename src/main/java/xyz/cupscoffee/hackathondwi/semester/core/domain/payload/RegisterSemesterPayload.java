package xyz.cupscoffee.hackathondwi.semester.core.domain.payload;

import xyz.cupscoffee.hackathondwi.shared.domain.payload.Payload;

public interface RegisterSemesterPayload extends Payload {
    String getCode();

    String getName();

    String getPassword();
}
