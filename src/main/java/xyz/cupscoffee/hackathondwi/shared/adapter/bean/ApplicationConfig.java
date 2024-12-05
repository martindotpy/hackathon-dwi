package xyz.cupscoffee.hackathondwi.shared.adapter.bean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;

/**
 * Configuration class for the application.
 */
@Configuration
public class ApplicationConfig {
    /**
     * Bean for ObjectMapper.
     *
     * <p>
     * In this case, the ObjectMapper is configured with the following properties:
     * <ul>
     * <li>PropertyNamingStrategy: SNAKE_CASE</li>
     * <li>SerializationInclusion: CUSTOM</li>
     * </ul>
     *
     *
     * Helps to convert Java objects to JSON and vice versa.
     * </p>
     *
     * @return A new instance of ObjectMapper
     */
    @Bean
    ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        objectMapper.setSerializationInclusion(Include.CUSTOM);

        return objectMapper;
    }

    @Bean
    ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        messageSource.setDefaultEncoding("UTF-8");

        return messageSource;
    }
}
