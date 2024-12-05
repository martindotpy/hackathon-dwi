package xyz.cupscoffee.hackathondwi.shared.adapter.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;
import xyz.cupscoffee.hackathondwi.shared.adapter.annotations.Properties;

@Getter
@Setter
@Properties
@ConfigurationProperties(prefix = "security")
public final class SecurityProperties {
    private String[] allowedMethods;
    private String[] allowedHeaders;
    private String[] allowedPublicRoutes;
}
