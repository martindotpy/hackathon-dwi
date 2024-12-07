package xyz.cupscoffee.hackathondwi.shared.adapter.in.util;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Utility class to map objects.
 */
@Component
public final class ObjectMapperShortcuts {
    private static ObjectMapper objectMapper;

    public ObjectMapperShortcuts(ObjectMapper objectMapper) {
        ObjectMapperShortcuts.objectMapper = objectMapper;
    }

    /**
     * Maps an object to a class.
     *
     * @param object The object to map.
     * @param clazz  The class to map to.
     * @param <T>    The type of the class.
     * @return The mapped object.
     */
    public static <T> T map(Object object, Class<T> clazz) {
        return objectMapper.convertValue(object, clazz);
    }
}
