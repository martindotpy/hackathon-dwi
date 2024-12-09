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

    /**
     * Create a filter with a string value.
     *
     * @param field    The field to filter.
     * @param operator The operator to use.
     * @param value    The value to filter.
     * @return The filter
     */
    public static Filter of(String field, FilterOperator operator, String value) {
        return new Filter(field, operator, value);
    }

    /**
     * Create a filter with a long value.
     *
     * @param field    The field to filter.
     * @param operator The operator to use.
     * @param value    The value to filter.
     * @return The filter
     */
    public static Filter of(String field, FilterOperator operator, Long value) {
        return new Filter(field, operator, toString(value));
    }

    /**
     * Create a filter with an integer value.
     *
     * @param field    The field to filter.
     * @param operator The operator to use.
     * @param value    The value to filter.
     * @return
     */
    public static Filter of(String field, FilterOperator operator, Integer value) {
        return new Filter(field, operator, toString(value));
    }

    /**
     * Create a filter with a double value.
     *
     * @param field    The field to filter.
     * @param operator The operator to use.
     * @param value    The value to filter.
     * @return
     */
    public static Filter of(String field, FilterOperator operator, Double value) {
        return new Filter(field, operator, toString(value));
    }

    /**
     * Create a filter with a boolean value.
     *
     * @param field    The field to filter.
     * @param operator The operator to use.
     * @param value    The value to filter.
     * @return The filter
     */
    public static Filter of(String field, FilterOperator operator, Boolean value) {
        return new Filter(field, operator, toString(value));
    }

    private static String toString(Object value) {
        return value == null ? null : value.toString();
    }
}
