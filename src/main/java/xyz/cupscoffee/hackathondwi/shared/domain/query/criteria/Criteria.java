package xyz.cupscoffee.hackathondwi.shared.domain.query.criteria;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * Criteria class.
 */
@ToString
@Getter
@AllArgsConstructor
public final class Criteria {
    private final List<Filter> filters;
    private final Order order;
    private final Integer size;
    private final Integer page;

    /**
     * Create a Criteria object with only filters to match one result.
     *
     * @param filters List of filters.
     * @return Criteria object
     */
    public static Criteria ofMatchOne(Filter... filters) {
        return new Criteria(List.of(filters), Order.none(), 2, 1);
    }
}
