package uk.gov.justice.laa.crime.application.tracking.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApplicationTrackingException extends RuntimeException {

    private final HttpStatus httpResponseCode;

    public ApplicationTrackingException(HttpStatus httpStatus, String message) {
        super(message);
        httpResponseCode = httpStatus;
    }
}
