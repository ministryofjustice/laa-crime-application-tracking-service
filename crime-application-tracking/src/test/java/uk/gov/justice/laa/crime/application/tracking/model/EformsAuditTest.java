package uk.gov.justice.laa.crime.application.tracking.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EformsAuditTest {
    @Test
    void testVariables(){
        EformsAudit testForm;
        testForm = new EformsAudit();
        testForm.setId(1);
        testForm.setUsn(12345);
        testForm.setMaatRef(67890);
        testForm.setUserCreated("JohnDoe");
        LocalDateTime now = LocalDateTime.now();
        testForm.setDateCreated(now);
        testForm.setStatusCode("Active");

        assertAll("Verify EformsAudit properties",
                () -> assertEquals(1, testForm.getId()),
                () -> assertEquals(12345, testForm.getUsn()),
                () -> assertEquals(67890, testForm.getMaatRef()),
                () -> assertEquals("JohnDoe", testForm.getUserCreated()),
                () -> assertEquals(now, testForm.getDateCreated()),
                () -> assertEquals("Active", testForm.getStatusCode())
        );
    }


}