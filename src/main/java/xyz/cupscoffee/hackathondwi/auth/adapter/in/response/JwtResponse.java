package xyz.cupscoffee.hackathondwi.auth.adapter.in.response;

import lombok.experimental.SuperBuilder;
import xyz.cupscoffee.hackathondwi.auth.application.dto.JwtDto;
import xyz.cupscoffee.hackathondwi.shared.application.response.ContentResponse;

@SuperBuilder
public final class JwtResponse extends ContentResponse<JwtDto> {
}
