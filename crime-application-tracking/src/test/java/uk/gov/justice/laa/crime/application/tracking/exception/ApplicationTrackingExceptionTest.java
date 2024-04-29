package uk.gov.justice.laa.crime.application.tracking.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationTrackingExceptionTest {

    @Test
    void getHttpResponseCode() {
        // Arrange
        HttpStatus expectedStatus = HttpStatus.BAD_REQUEST; // Example HttpStatus
        String expectedMessage = "Test message";

        // Act
        ApplicationTrackingException exception = new ApplicationTrackingException(expectedStatus, expectedMessage);

        // Assert
        assertEquals(expectedMessage, exception.getMessage(), "The exception message should match the provided message");
        assertEquals(expectedStatus, exception.getHttpResponseCode(), "The HttpStatus should match the provided HttpStatus");

    }
}