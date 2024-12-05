package xyz.cupscoffee.hackathondwi;

import org.fusesource.jansi.AnsiConsole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import xyz.cupscoffee.hackathondwi.shared.adapter.validator.JakartaValidator;
import xyz.cupscoffee.hackathondwi.shared.domain.validation.ExternalPayloadValidatorProvider;

/**
 * Main class for the Spring Boot application.
 */
@SpringBootApplication
public class HackathonDWIApplication implements CommandLineRunner {
    public static void main(String[] args) {
        AnsiConsole.systemInstall();

        SpringApplication.run(HackathonDWIApplication.class, args);

        AnsiConsole.systemUninstall();
    }

    @Autowired
    private JakartaValidator jakartaValidator;

    @Override
    public void run(String... args) throws Exception {
        ExternalPayloadValidatorProvider.set(jakartaValidator);
    }
}
