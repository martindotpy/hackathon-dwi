package xyz.cupscoffee.hackathondwi.shared.adapter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

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

        return objectMapper;
    }

    @Bean
    ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        messageSource.setDefaultEncoding("UTF-8");

        return messageSource;
    }

    @Bean
    RestTemplate restTemplate(ObjectMapper objectMapper) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(0, new MappingJackson2HttpMessageConverter(objectMapper));

        return restTemplate;
    }
}
