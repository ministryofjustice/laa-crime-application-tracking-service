package uk.gov.justice.laa.crime.application.tracking.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.justice.laa.crime.application.tracking.client.MaatCourtDataApiClient;
import uk.gov.justice.laa.crime.application.tracking.model.EformsDecisionHistory;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EformsDecisionHistoryServiceTest {

    @Mock
    private MaatCourtDataApiClient maatCourtDataApiClient;

    @InjectMocks
    private EformsDecisionHistoryService eformsDecisionHistoryService;

    @Test
    void shouldCreateEformsDecisionHistoryRecordForGivenEformsDecisionHistoryRequest() {
        EformsDecisionHistory eformsDecisionHistory = EformsDecisionHistory.builder()
                .usn(123456)
                .repId(654321)
                .fundingDecision("granted").build();
        eformsDecisionHistoryService.createEformsDecisionHistoryRecord(eformsDecisionHistory);
        verify(maatCourtDataApiClient, times(1)).createEformsDecisionHistoryRecord(eformsDecisionHistory);
    }

    @Test
    void shouldGetPreviousEformsDecisionHistoryRecordForGivenUsnNumber() {
        Integer usn = 123456;
        EformsDecisionHistory expectedRecord = EformsDecisionHistory.builder()
                .usn(123456)
                .repId(654321)
                .fundingDecision("granted").build();
        when(maatCourtDataApiClient.getPreviousEformsDecisionHistoryRecordWroteToResult(usn)).thenReturn(expectedRecord);
        EformsDecisionHistory response = eformsDecisionHistoryService.getPreviousDecisionResult(123456);
        verify(maatCourtDataApiClient, times(1)).getPreviousEformsDecisionHistoryRecordWroteToResult(usn);
        Assertions.assertEquals(expectedRecord.getFundingDecision(), response.getFundingDecision());
    }

    @Test
    void shouldUpdateWroteToResultForEformsDecisionHistoryRecordForGivenUsnNumber() {
        Integer usn = 123456;
        EformsDecisionHistory eformsDecisionHistory = EformsDecisionHistory.builder()
                .wroteToResults("Y")
                .build();
        eformsDecisionHistoryService.updateWroteResult(123456);
        verify(maatCourtDataApiClient, times(1)).updateEformsDecisionHistoryRecord(usn, eformsDecisionHistory);
    }
}
