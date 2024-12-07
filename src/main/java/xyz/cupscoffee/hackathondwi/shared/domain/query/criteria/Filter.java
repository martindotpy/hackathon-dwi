package xyz.cupscoffee.hackathondwi.shared.domain.query.criteria;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * Filter class.
 */
@ToString
@Getter
@AllArgsConstructor
public final class Filter {
    private final String field;
    private final FilterOperator operator;
    private final String value;
}
