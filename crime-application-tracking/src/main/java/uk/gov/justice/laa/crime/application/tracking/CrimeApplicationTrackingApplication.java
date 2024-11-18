package uk.gov.justice.laa.crime.application.tracking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class CrimeApplicationTrackingApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrimeApplicationTrackingApplication.class, args);
    }

}
