package xyz.cupscoffee.hackathondwi.shared.domain.query.paginated;

import java.util.List;
import java.util.function.Function;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

/**
 * Represents a paginated response.
 */
@Getter
@SuperBuilder
public class Paginated<T> {
    private final int page;
    private final int size;
    private final int totalPages;
    private final long totalElements;
    private final List<T> content;

    /**
     * Maps the content of the paginated response.
     *
     * @param mapper the mapper function
     * @param <NT>   the new type
     * @return the new paginated response
     */
    public <NT> Paginated<NT> map(Function<T, NT> mapper) {
        return Paginated.<NT>builder()
                .page(page)
                .size(size)
                .totalPages(totalPages)
                .totalElements(totalElements)
                .content(content.stream().map(mapper).toList())
                .build();
    }
}
