package uk.gov.justice.laa.crime.application.tracking.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.justice.laa.crime.application.tracking.entity.DecisionHistory;
import uk.gov.justice.laa.crime.application.tracking.model.AssessorDetails;
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
    private DecisionHistoryService decisionHistoryService;
    @Mock
    private ResultService resultService;
    @Mock
    private EformStagingService eformStagingService;

    @InjectMocks
    private ApplicationOutputResultService applicationOutputResultService;

    @Test
    void shouldProcessOutputResultForGivenValidRequest(){
        var atsRequest = TestData.getAtsRequest();
        OutstandingAssessment outstandingAssessment = OutstandingAssessment.builder().build();
        when(assessmentAssessorService.checkOutstandingAssessment(atsRequest.getMaatRef())).thenReturn(outstandingAssessment);
        DecisionHistory decisionHistory = DecisionHistory.builder().build();
        when(decisionHistoryService.getPreviousDecisionResult(atsRequest.getUsn())).thenReturn(decisionHistory);

        applicationOutputResultService.processOutputResult(atsRequest);

        verify(assessmentAssessorService, times(1)).checkOutstandingAssessment(atsRequest.getMaatRef());
        verify(assessmentAssessorService, times(0)).getIOJAssessor(atsRequest.getMaatRef());
        verify(assessmentAssessorService, times(0)).getMeansAssessor(atsRequest.getMeansAssessment().getMeansAssessmentId());
        verify(assessmentAssessorService, times(0)).getPassportAssessor(atsRequest.getPassport().getPassportId());
        verify(decisionHistoryService, times(1)).createDecisionHistoryRecord(any());
        verify(decisionHistoryService, times(1)).getPreviousDecisionResult(atsRequest.getUsn());
        verify(resultService, times(1)).createResult(atsRequest, FUNDING_DECISION);
        verify(decisionHistoryService, times(1)).updateWroteResult(atsRequest.getUsn());
        verify(eformStagingService, times(1)).updateStatus(atsRequest.getUsn());
    }

    @Test
    void shouldProcessOutputResultForGivenValidRequestWithSameAsPreviousResult(){
        var atsRequest = TestData.getAtsRequest();
        OutstandingAssessment outstandingAssessment = OutstandingAssessment.builder().build();
        when(assessmentAssessorService.checkOutstandingAssessment(atsRequest.getMaatRef())).thenReturn(outstandingAssessment);
        DecisionHistory decisionHistory = BuildRequestsUtil.buildDecisionHistory(atsRequest, FUNDING_DECISION);
        decisionHistory.setId(65412363);
        when(decisionHistoryService.getPreviousDecisionResult(atsRequest.getUsn())).thenReturn(decisionHistory);

        applicationOutputResultService.processOutputResult(atsRequest);

        verify(assessmentAssessorService, times(1)).checkOutstandingAssessment(atsRequest.getMaatRef());
        verify(assessmentAssessorService, times(0)).getIOJAssessor(atsRequest.getMaatRef());
        verify(assessmentAssessorService, times(0)).getMeansAssessor(atsRequest.getMeansAssessment().getMeansAssessmentId());
        verify(assessmentAssessorService, times(0)).getPassportAssessor(atsRequest.getPassport().getPassportId());
        verify(decisionHistoryService, times(1)).createDecisionHistoryRecord(any());
        verify(decisionHistoryService, times(1)).getPreviousDecisionResult(atsRequest.getUsn());
        verify(resultService, times(0)).createResult(atsRequest, FUNDING_DECISION);
        verify(decisionHistoryService, times(0)).updateWroteResult(atsRequest.getUsn());
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
        DecisionHistory decisionHistory = DecisionHistory.builder().build();
        when(decisionHistoryService.getPreviousDecisionResult(atsRequest.getUsn())).thenReturn(decisionHistory);
        when(assessmentAssessorService.getIOJAssessor(atsRequest.getMaatRef())).thenReturn(assessorDetails);
        when(assessmentAssessorService.getMeansAssessor(atsRequest.getMeansAssessment().getMeansAssessmentId())).thenReturn(assessorDetails);
        when(assessmentAssessorService.getPassportAssessor(atsRequest.getPassport().getPassportId())).thenReturn(assessorDetails);

        applicationOutputResultService.processOutputResult(atsRequest);

        verify(assessmentAssessorService, times(1)).checkOutstandingAssessment(atsRequest.getMaatRef());
        verify(assessmentAssessorService, times(1)).getIOJAssessor(atsRequest.getMaatRef());
        verify(assessmentAssessorService, times(1)).getMeansAssessor(atsRequest.getMeansAssessment().getMeansAssessmentId());
        verify(assessmentAssessorService, times(1)).getPassportAssessor(atsRequest.getPassport().getPassportId());
        verify(decisionHistoryService, times(1)).createDecisionHistoryRecord(any());
        verify(decisionHistoryService, times(1)).getPreviousDecisionResult(atsRequest.getUsn());
        verify(resultService, times(1)).createResult(atsRequest, FUNDING_DECISION);
        verify(decisionHistoryService, times(1)).updateWroteResult(atsRequest.getUsn());
        verify(eformStagingService, times(1)).updateStatus(atsRequest.getUsn());
    }

}
