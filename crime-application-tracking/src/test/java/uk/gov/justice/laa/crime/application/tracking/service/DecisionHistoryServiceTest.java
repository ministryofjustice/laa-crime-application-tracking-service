package uk.gov.justice.laa.crime.application.tracking.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.justice.laa.crime.application.tracking.entity.DecisionHistory;
import uk.gov.justice.laa.crime.application.tracking.repository.DecisionHistoryRepository;

@ExtendWith(MockitoExtension.class)
class DecisionHistoryServiceTest {

    private static final String WROTE_TO_RESULTS = "Y";

    @Mock
    private DecisionHistoryRepository decisionHistoryRepository;

    @InjectMocks
    private DecisionHistoryService decisionHistoryService;

    @Test
    void shouldCreateDecisionHistoryRecordForGivenDecisionHistoryRequest() {
        DecisionHistory decisionHistory = DecisionHistory.builder()
            .usn(123456)
            .repId(654321)
            .fundingDecision("granted").build();
        decisionHistoryService.createDecisionHistoryRecord(decisionHistory);
        verify(decisionHistoryRepository, times(1)).save(decisionHistory);
    }

    @Test
    void shouldGetPreviousDecisionHistoryRecordForGivenUsnNumber() {
        Integer usn = 123456;
        DecisionHistory expectedRecord = DecisionHistory.builder()
            .usn(123456)
            .repId(654321)
            .fundingDecision("granted").build();
        when(decisionHistoryRepository.findFirstByUsnAndWroteToResultsOrderByIdDesc(usn, WROTE_TO_RESULTS)).thenReturn(
            Optional.ofNullable(expectedRecord));
        DecisionHistory response = decisionHistoryService.getPreviousDecisionResult(123456);
        verify(decisionHistoryRepository, times(1)).
            findFirstByUsnAndWroteToResultsOrderByIdDesc(usn, WROTE_TO_RESULTS);
        Assertions.assertEquals(expectedRecord.getFundingDecision(), response.getFundingDecision());
    }

    @Test
    void shouldUpdateWroteToResultForDecisionHistoryRecordForGivenUsnNumber() {
        Integer usn = 123456;
        DecisionHistory decisionHistory = DecisionHistory.builder()
            .usn(usn)
            .build();

        DecisionHistory updatedDecisionHistory = DecisionHistory.builder()
            .usn(usn)
            .wroteToResults(WROTE_TO_RESULTS)
            .build();

        when(decisionHistoryRepository.findTopByUsnOrderByIdDesc(usn)).thenReturn(decisionHistory);

        decisionHistoryService.updateWroteResult(usn);
        verify(decisionHistoryRepository, times(1)).findTopByUsnOrderByIdDesc(usn);
        verify(decisionHistoryRepository, times(1)).save(updatedDecisionHistory);
    }
}
