package xyz.cupscoffee.hackathondwi.auth.application.dto;

import lombok.Builder;
import lombok.Getter;

/**
 * JWT DTO.
 */
@Getter
@Builder
public final class JwtDto {
    private String jwt;
}
