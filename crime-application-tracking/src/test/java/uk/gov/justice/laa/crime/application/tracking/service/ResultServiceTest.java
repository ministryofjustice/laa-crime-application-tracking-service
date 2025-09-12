package uk.gov.justice.laa.crime.application.tracking.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.justice.laa.crime.application.tracking.repository.ResultRepository;
import uk.gov.justice.laa.crime.application.tracking.testutils.TestData;

@ExtendWith(MockitoExtension.class)
class ResultServiceTest {
    @Mock
    private ResultRepository resultRepository;

    @InjectMocks
    private ResultService resultService;

    @Test
    void shouldCreateResultRecordForGivenATSRequest() {
        var atsRequest = TestData.getAtsRequest();
        resultService.createResult(atsRequest, "Granted");
        verify(resultRepository, times(1)).save(any());
    }
}
