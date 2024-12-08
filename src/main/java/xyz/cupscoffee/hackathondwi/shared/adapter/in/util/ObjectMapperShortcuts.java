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
     * Maps a string to an object.
     *
     * @param value  The string to map.
     * @param clazz  The class to map to.
     * @param <T>    The type of the class.
     * @return The mapped object.
     */
    public static <T> T map(String value, Class<T> clazz) {
        try {
            return objectMapper.readValue(value, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
