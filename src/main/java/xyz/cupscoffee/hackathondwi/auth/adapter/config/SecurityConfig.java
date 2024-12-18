package xyz.cupscoffee.hackathondwi.auth.adapter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

import lombok.RequiredArgsConstructor;
import xyz.cupscoffee.hackathondwi.auth.adapter.in.filter.JwtRequestFilter;
import xyz.cupscoffee.hackathondwi.auth.adapter.in.filter.JwtViewFilter;
import xyz.cupscoffee.hackathondwi.shared.adapter.properties.SecurityProperties;
import xyz.cupscoffee.hackathondwi.user.core.adapter.out.persistence.UserPersistenceAdapter;

/**
 * Configuration class for security.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserPersistenceAdapter userPersistenceAdapter;
    private final SecurityProperties securityProperties;
    private final JwtRequestFilter jwtRequestFilter;
    private final JwtViewFilter jwtViewFilter;

    /**
     * Bean for SecurityFilterChain.
     *
     * <p>
     * This bean configure almost all the security properties of the application
     * provided by Spring Security.
     * </p>
     *
     * <p>
     * In this case, the SecurityFilterChain is configured with the properties
     * defined in the application file.
     * </p>
     *
     * @param httpSecurity The HttpSecurity object
     * @return A new instance of SecurityFilterChain
     * @throws Exception If an error occurs
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        authRequest -> {
                            authRequest
                                    .requestMatchers(securityProperties.getAllowedPublicRoutes()).permitAll()
                                    .anyRequest().authenticated();
                        })
                .addFilterBefore(jwtRequestFilter, AnonymousAuthenticationFilter.class)
                .addFilterBefore(jwtViewFilter, AnonymousAuthenticationFilter.class)
                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.NEVER))
                .build();
    }

    /**
     * Bean for PasswordEncoder.
     *
     * <p>
     * This bean configure the password encoder of the application provided by
     * Spring Security. In this case, the password encoder is
     * {@link BCryptPasswordEncoder}.
     * </p>
     *
     * @return A new instance of PasswordEncoder
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        return passwordEncoder;
    }

    /**
     * Bean for DaoAuthenticationProvider.
     *
     * <p>
     * This bean configure the DaoAuthenticationProvider of the application provided
     * by Spring Security.
     * </p>
     *
     * @return A new instance of DaoAuthenticationProvider
     */
    DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userPersistenceAdapter);

        return daoAuthenticationProvider;
    }
}