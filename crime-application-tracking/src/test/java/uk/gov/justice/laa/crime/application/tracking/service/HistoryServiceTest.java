package uk.gov.justice.laa.crime.application.tracking.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.justice.laa.crime.application.tracking.repository.HistoryRepository;
import uk.gov.justice.laa.crime.application.tracking.testutils.TestData;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class HistoryServiceTest {

    @Mock
    private HistoryRepository historyRepository;

    @InjectMocks
    private HistoryService historyService;

    @Test
    void shouldCreateHistoryRecordForGivenATSRequest() {
        var atsRequest = TestData.getAtsRequest();
        historyService.createHistory(atsRequest);
        verify(historyRepository, times(1)).save(any());
    }
}
