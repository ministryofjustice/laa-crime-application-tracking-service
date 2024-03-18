package uk.gov.justice.laa.crime.application.tracking.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.justice.laa.crime.application.tracking.model.AssessorDetails;
import uk.gov.justice.laa.crime.application.tracking.model.EformsDecisionHistory;
import uk.gov.justice.laa.crime.application.tracking.model.OutstandingAssessment;
import uk.gov.justice.laa.crime.application.tracking.testutils.TestData;
import uk.gov.justice.laa.crime.application.tracking.util.BuildRequestsUtil;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationOutputResultServiceTest {
    private static final String FUNDING_DECISION = "Granted - Passed Means Test";
    @Mock
    private AssessmentAssessorService assessmentAssessorService;
    @Mock
    private EformsDecisionHistoryService eformsDecisionHistoryService;
    @Mock
    private EformResultsService eformResultsService;
    @Mock
    private EformStagingService eformStagingService;

    @InjectMocks
    private ApplicationOutputResultService applicationOutputResultService;

    @Test
    void shouldProcessOutputResultForGivenValidRequest(){
        var atsRequest = TestData.getAtsRequest();
        OutstandingAssessment outstandingAssessment = OutstandingAssessment.builder().build();
        when(assessmentAssessorService.checkOutstandingAssessment(atsRequest.getMaatRef())).thenReturn(outstandingAssessment);
        EformsDecisionHistory eformsDecisionHistory =EformsDecisionHistory.builder().build();
        when(eformsDecisionHistoryService.getPreviousDecisionResult(atsRequest.getUsn())).thenReturn(eformsDecisionHistory);

        applicationOutputResultService.processOutputResult(atsRequest);

        verify(assessmentAssessorService, times(1)).checkOutstandingAssessment(atsRequest.getMaatRef());
        verify(assessmentAssessorService, times(0)).getIOJAssessor(atsRequest.getMaatRef());
        verify(assessmentAssessorService, times(0)).getMeansAssessor(atsRequest.getMeansAssessment().getMeansAssessmentId());
        verify(assessmentAssessorService, times(0)).getPassportAssessor(atsRequest.getPassport().getPassportId());
        verify(eformsDecisionHistoryService, times(1)).createEformsDecisionHistoryRecord(any());
        verify(eformsDecisionHistoryService, times(1)).getPreviousDecisionResult(atsRequest.getUsn());
        verify(eformResultsService, times(1)).createEformResult(atsRequest, FUNDING_DECISION);
        verify(eformsDecisionHistoryService, times(1)).updateWroteResult(atsRequest.getUsn());
        verify(eformStagingService, times(1)).updateStatus(atsRequest.getUsn());
    }

    @Test
    void shouldProcessOutputResultForGivenValidRequestWithSameAsPreviousResult(){
        var atsRequest = TestData.getAtsRequest();
        OutstandingAssessment outstandingAssessment = OutstandingAssessment.builder().build();
        when(assessmentAssessorService.checkOutstandingAssessment(atsRequest.getMaatRef())).thenReturn(outstandingAssessment);
        EformsDecisionHistory eformsDecisionHistory = BuildRequestsUtil.buildEformDecisionHistory(atsRequest, FUNDING_DECISION);
        eformsDecisionHistory.setId(65412363);
        when(eformsDecisionHistoryService.getPreviousDecisionResult(atsRequest.getUsn())).thenReturn(eformsDecisionHistory);

        applicationOutputResultService.processOutputResult(atsRequest);

        verify(assessmentAssessorService, times(1)).checkOutstandingAssessment(atsRequest.getMaatRef());
        verify(assessmentAssessorService, times(0)).getIOJAssessor(atsRequest.getMaatRef());
        verify(assessmentAssessorService, times(0)).getMeansAssessor(atsRequest.getMeansAssessment().getMeansAssessmentId());
        verify(assessmentAssessorService, times(0)).getPassportAssessor(atsRequest.getPassport().getPassportId());
        verify(eformsDecisionHistoryService, times(1)).createEformsDecisionHistoryRecord(any());
        verify(eformsDecisionHistoryService, times(1)).getPreviousDecisionResult(atsRequest.getUsn());
        verify(eformResultsService, times(0)).createEformResult(atsRequest, FUNDING_DECISION);
        verify(eformsDecisionHistoryService, times(0)).updateWroteResult(atsRequest.getUsn());
        verify(eformStagingService, times(0)).updateStatus(atsRequest.getUsn());
    }

    @Test
    void shouldProcessOutputResultWithAssessorNameAPICallsForGivenValidRequest(){
        var atsRequest = TestData.getAtsRequest();
        atsRequest.getIoj().setIojAssessorName(null);
        atsRequest.getMeansAssessment().setMeansAssessorName(null);
        atsRequest.getPassport().setPassportAssessorName(null);

        AssessorDetails assessorDetails = AssessorDetails.builder().fullName("Test User").build();
        OutstandingAssessment outstandingAssessment = OutstandingAssessment.builder().build();
        when(assessmentAssessorService.checkOutstandingAssessment(atsRequest.getMaatRef())).thenReturn(outstandingAssessment);
        EformsDecisionHistory eformsDecisionHistory =EformsDecisionHistory.builder().build();
        when(eformsDecisionHistoryService.getPreviousDecisionResult(atsRequest.getUsn())).thenReturn(eformsDecisionHistory);
        when(assessmentAssessorService.getIOJAssessor(atsRequest.getMaatRef())).thenReturn(assessorDetails);
        when(assessmentAssessorService.getMeansAssessor(atsRequest.getMeansAssessment().getMeansAssessmentId())).thenReturn(assessorDetails);
        when(assessmentAssessorService.getPassportAssessor(atsRequest.getPassport().getPassportId())).thenReturn(assessorDetails);

        applicationOutputResultService.processOutputResult(atsRequest);

        verify(assessmentAssessorService, times(1)).checkOutstandingAssessment(atsRequest.getMaatRef());
        verify(assessmentAssessorService, times(1)).getIOJAssessor(atsRequest.getMaatRef());
        verify(assessmentAssessorService, times(1)).getMeansAssessor(atsRequest.getMeansAssessment().getMeansAssessmentId());
        verify(assessmentAssessorService, times(1)).getPassportAssessor(atsRequest.getPassport().getPassportId());
        verify(eformsDecisionHistoryService, times(1)).createEformsDecisionHistoryRecord(any());
        verify(eformsDecisionHistoryService, times(1)).getPreviousDecisionResult(atsRequest.getUsn());
        verify(eformResultsService, times(1)).createEformResult(atsRequest, FUNDING_DECISION);
        verify(eformsDecisionHistoryService, times(1)).updateWroteResult(atsRequest.getUsn());
        verify(eformStagingService, times(1)).updateStatus(atsRequest.getUsn());
    }

}
