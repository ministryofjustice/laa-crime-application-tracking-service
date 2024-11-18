package uk.gov.justice.laa.crime.application.tracking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import io.sentry.Sentry;

@SpringBootApplication
@ConfigurationPropertiesScan
public class CrimeApplicationTrackingApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrimeApplicationTrackingApplication.class, args);
        for (int i = 0; i < 20; i++) {
            try {
                // Intentionally cause an error
                throw new RuntimeException("Test error " + (i + 1));
            } catch (RuntimeException e) {
                // Capture the error with Sentry
                Sentry.captureException(e);
                // Handle the error (e.g., log it)
                System.err.println("Caught error: " + e.getMessage());
            }
        }

    }

}
