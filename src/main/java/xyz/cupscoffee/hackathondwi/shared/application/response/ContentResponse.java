package xyz.cupscoffee.hackathondwi.shared.application.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Represents a response.
 */
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ContentResponse<T> {
    private String message;
    private T content;
}