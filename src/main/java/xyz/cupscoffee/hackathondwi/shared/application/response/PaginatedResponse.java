package xyz.cupscoffee.hackathondwi.shared.application.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import xyz.cupscoffee.hackathondwi.shared.domain.query.paginated.Paginated;

/**
 * Represents a paginated response.
 */
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedResponse<T> {
    private int page;
    private int size;
    private int totalPages;
    private long totalElements;
    private String message;
    private List<T> content;

    /**
     * Creates a paginated response from a paginated object.
     *
     * @param paginated the paginated object
     * @param <T>       the type of the content
     * @return the paginated response
     */
    public static <T> PaginatedResponse<T> from(Paginated<T> paginated, String message) {
        return PaginatedResponse.<T>builder()
                .page(paginated.getPage())
                .size(paginated.getSize())
                .totalPages(paginated.getTotalPages())
                .totalElements(paginated.getTotalElements())
                .content(paginated.getContent())
                .message(message)
                .build();
    }
}
