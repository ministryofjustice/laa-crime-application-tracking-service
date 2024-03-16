package uk.gov.justice.laa.crime.application.tracking.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.justice.laa.crime.application.tracking.client.MaatCourtDataApiClient;
import uk.gov.justice.laa.crime.application.tracking.testutils.TestData;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class EformAuditServiceTest {
    @Mock
    private MaatCourtDataApiClient maatCourtDataApiClient;

    @InjectMocks
    private EformAuditService eformAuditService;

    @Test
    void shouldCreateEformsAuditRecordForGivenATSRequest() {
        var atsRequest = TestData.getAtsRequest();
        eformAuditService.createAudit(atsRequest);
        verify(maatCourtDataApiClient, times(1)).createEformsAuditRecord(any());
    }
}
