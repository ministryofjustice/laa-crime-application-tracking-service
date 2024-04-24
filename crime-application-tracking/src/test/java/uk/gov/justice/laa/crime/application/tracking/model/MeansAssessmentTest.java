package uk.gov.justice.laa.crime.application.tracking.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MeansAssessmentTest {

    @Test
    void meansAssessment() {
        // Define the parameters for the constructor
        Integer meansAssessmentId = 1;
        MeansAssessment.MeansAssessmentType meansAssessmentType = null;
        String meansAssessmentStatus = "Status1";
        MeansAssessment.MeansAssessmentResult meansAssessmentResult = null;
        String meansAssessorName = "Assessor1";
        LocalDateTime meansAssessmentCreatedDate = LocalDateTime.now();

        // Create an instance of Assessment using the constructor
        MeansAssessment meansAssessment = new MeansAssessment(meansAssessmentId, meansAssessmentType, meansAssessmentStatus, meansAssessmentResult, meansAssessorName, meansAssessmentCreatedDate);

        // Verify that the constructor was called with the expected parameters
        assertAll(
                () -> assertEquals(meansAssessmentId, meansAssessment.getMeansAssessmentId()),
                () -> assertEquals(meansAssessmentType, meansAssessment.getMeansAssessmentType()),
                () -> assertEquals(meansAssessmentStatus, meansAssessment.getMeansAssessmentStatus()),
                () -> assertEquals(meansAssessmentResult, meansAssessment.getMeansAssessmentResult()),
                () -> assertEquals(meansAssessorName, meansAssessment.getMeansAssessorName()),
                () -> assertEquals(meansAssessmentCreatedDate, meansAssessment.getMeansAssessmentCreatedDate()));

    }

    @Test
    void withMeansAssessmentId() {
        // Define the parameters for the constructor
        Integer meansAssessmentId = 1;
        MeansAssessment.MeansAssessmentType meansAssessmentType = null;
        String meansAssessmentStatus = "Status1";
        MeansAssessment.MeansAssessmentResult meansAssessmentResult = null;
        String meansAssessorName = "Assessor1";
        LocalDateTime meansAssessmentCreatedDate = LocalDateTime.now();

        // Create an instance of Assessment using the constructor
        MeansAssessment meansAssessment = new MeansAssessment(meansAssessmentId, meansAssessmentType, meansAssessmentStatus, meansAssessmentResult, meansAssessorName, meansAssessmentCreatedDate);


        // Verify that the assessmentId field is set correctly
        // Assuming there's a getter method for assessmentId
        Integer expectedAssessmentId = 2;
        meansAssessment.withMeansAssessmentId(expectedAssessmentId);
        assertEquals(expectedAssessmentId, meansAssessment.getMeansAssessmentId());
    }

    @Test
    public void testToString() {
        // Create an instance of Assessment with specific values
        MeansAssessment.MeansAssessmentType meansAssessmentType = null;
        MeansAssessment.MeansAssessmentResult meansAssessmentResult = null;
        MeansAssessment meansAssessment = new MeansAssessment(
                1, // assessmentId
                meansAssessmentType, // assessmentType
                "Status1", // assessmentStatus
                meansAssessmentResult, // assessmentResult
                "Assessor1", // assessorName
                LocalDateTime.now() // assessmentCreatedDate
        );
        meansAssessment.withAdditionalProperty("testProperty", 2);
        // Define the expected string format
        String expected = MeansAssessment.class.getName() + '@' + Integer.toHexString(System.identityHashCode(meansAssessment)) + '[' +
                "meansAssessmentId=1," +
                "meansAssessmentType=<null>," +
                "meansAssessmentStatus=Status1," +
                "meansAssessmentResult=<null>," +
                "meansAssessorName=Assessor1," +
                "meansAssessmentCreatedDate=" + meansAssessment.getMeansAssessmentCreatedDate() + "," +
                "additionalProperties={testProperty=2}]";

        // Call the toString method
        String actual = meansAssessment.toString();

        // Verify that the actual string matches the expected format
        assertEquals(expected, actual);
    }
}