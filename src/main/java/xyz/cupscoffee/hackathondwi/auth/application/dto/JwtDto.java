package xyz.cupscoffee.hackathondwi.auth.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * JWT DTO.
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public final class JwtDto {
    private String jwt;
}
