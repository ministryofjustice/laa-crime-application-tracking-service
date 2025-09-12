package uk.gov.justice.laa.crime.application.tracking.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.justice.laa.crime.application.tracking.repository.AuditRepository;
import uk.gov.justice.laa.crime.application.tracking.testutils.TestData;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuditServiceTest {
    @Mock
    private AuditRepository auditRepository;

    @InjectMocks
    private AuditService auditService;

    @Test
    void shouldCreateAuditRecordForGivenATSRequest() {
        var atsRequest = TestData.getAtsRequest();
        auditService.createAudit(atsRequest);
        verify(auditRepository, times(1)).save(any());
    }
}
