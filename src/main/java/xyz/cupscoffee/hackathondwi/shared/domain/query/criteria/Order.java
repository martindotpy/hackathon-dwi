package xyz.cupscoffee.hackathondwi.shared.domain.query.criteria;

import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Order class.
 */
@Slf4j
@Getter
@AllArgsConstructor
public final class Order {
    private final String field;
    private final OrderType type;

    /**
     * Create order.
     *
     * @param field The field.
     * @param type  The type.
     * @return The order
     */
    public static Order of(String field, OrderType type) {
        type = type != null ? type : OrderType.NONE;

        if (type != OrderType.NONE) {
            Objects.requireNonNull(field, "Field must not be null");
        }

        log.info("Creating order with field: {} and type: {}", field, type);

        return new Order(field, type);
    }

    /**
     * Create none order.
     *
     * @return The order
     */
    public static Order none() {
        return new Order(null, OrderType.NONE);
    }
}
