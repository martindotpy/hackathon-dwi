package xyz.cupscoffee.hackathondwi.user.core.domain.payload;

import xyz.cupscoffee.hackathondwi.shared.domain.payload.Payload;

public interface LoginUserPayload extends Payload {
    String getCode();

    String getPassword();
}
