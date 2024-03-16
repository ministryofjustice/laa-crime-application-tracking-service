package uk.gov.justice.laa.crime.application.tracking.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.justice.laa.crime.application.tracking.client.MaatCourtDataApiClient;
import uk.gov.justice.laa.crime.application.tracking.model.EformsStaging;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class EformStagingServiceTest {
    @Mock
    private MaatCourtDataApiClient maatCourtDataApiClient;

    @InjectMocks
    private EformStagingService eformStagingService;

    @Test
    void shouldUpdateMaatIDInEformsStagingForGivenUsnNumber() {
        Integer usn = 123456;
        Integer maatId = 654321;
        EformsStaging eformsStaging = EformsStaging.builder().maatRef(maatId).build();
        eformStagingService.updateMaatId(usn, maatId);
        verify(maatCourtDataApiClient, times(1)).updateEformStagingRecord(usn, eformsStaging);
    }

    @Test
    void shouldUpdateStatusInEformsStagingForGivenUsnNumber() {
        Integer usn = 123456;
        String status = "PURGE";
        EformsStaging eformsStaging = EformsStaging.builder().maatStatus(status).build();
        eformStagingService.updateStatus(usn);
        verify(maatCourtDataApiClient, times(1)).updateEformStagingRecord(usn, eformsStaging);
    }
}
