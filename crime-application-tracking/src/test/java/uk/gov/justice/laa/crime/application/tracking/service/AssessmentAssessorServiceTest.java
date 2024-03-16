package uk.gov.justice.laa.crime.application.tracking.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.justice.laa.crime.application.tracking.client.MaatCourtDataApiClient;
import uk.gov.justice.laa.crime.application.tracking.model.AssessorDetails;
import uk.gov.justice.laa.crime.application.tracking.model.OutstandingAssessment;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AssessmentAssessorServiceTest {

    @Mock
    private MaatCourtDataApiClient maatCourtDataApiClient;

    @InjectMocks
    private AssessmentAssessorService assessmentAssessorService;

    @Test
    void shouldCheckOutstandingAssessmentforGivenMaatID() {
        Integer maat_id = 123456;
        OutstandingAssessment outstandingAssessment = OutstandingAssessment.builder().build();
        when(maatCourtDataApiClient.checkForOutstandingAssessments(maat_id)).thenReturn(outstandingAssessment);
        OutstandingAssessment response = assessmentAssessorService.checkOutstandingAssessment(maat_id);
        verify(maatCourtDataApiClient, Mockito.times(1)).checkForOutstandingAssessments(maat_id);
        Assertions.assertFalse(response.isOutstandingAssessments());
    }

    @Test
    void shouldGetIOJAssessorNameGivenMaatID() {
        int maat_id = 123456;
        AssessorDetails iojAssessor = AssessorDetails.builder().fullName("IOJ Test User").build();
        when(maatCourtDataApiClient.findIOJAssessorDetails(maat_id)).thenReturn(iojAssessor);
        AssessorDetails response = assessmentAssessorService.getIOJAssessor(maat_id);
        verify(maatCourtDataApiClient, Mockito.times(1)).findIOJAssessorDetails(maat_id);
        Assertions.assertEquals(iojAssessor.getFullName(), response.getFullName());
    }

    @Test
    void shouldGetPassportAssessorNameGivenPassportID() {
        int passportId = 123456;
        AssessorDetails passportAssessor = AssessorDetails.builder().fullName("Passport Test User").build();
        when(maatCourtDataApiClient.findPassportAssessorDetails(passportId)).thenReturn(passportAssessor);
        AssessorDetails response = assessmentAssessorService.getPassportAssessor(passportId);
        verify(maatCourtDataApiClient, Mockito.times(1)).findPassportAssessorDetails(passportId);
        Assertions.assertEquals(passportAssessor.getFullName(), response.getFullName());
    }

    @Test
    void shouldGetMeansAssessorNameGivenMeansId() {
        int maat_id = 123456;
        AssessorDetails meansAssessor = AssessorDetails.builder().fullName("Means Test User").build();
        when(maatCourtDataApiClient.findMeansAssessorDetails(maat_id)).thenReturn(meansAssessor);
        AssessorDetails response = assessmentAssessorService.getMeansAssessor(maat_id);
        verify(maatCourtDataApiClient, Mockito.times(1)).findMeansAssessorDetails(maat_id);
        Assertions.assertEquals(meansAssessor.getFullName(), response.getFullName());
    }


}
