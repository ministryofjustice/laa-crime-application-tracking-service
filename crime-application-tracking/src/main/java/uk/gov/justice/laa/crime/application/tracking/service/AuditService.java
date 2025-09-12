package uk.gov.justice.laa.crime.application.tracking.service;

import io.github.resilience4j.retry.annotation.Retry;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.justice.laa.crime.application.tracking.entity.Audit;
import uk.gov.justice.laa.crime.application.tracking.model.ApplicationTrackingOutputResult;
import uk.gov.justice.laa.crime.application.tracking.repository.AuditRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditService {
    private static final String SERVICE_NAME = "auditService";
    private static final String STATUS_CODE = "Processing";
    private final AuditRepository auditRepository;
    private final ObservationRegistry observationRegistry;

    @Retry(name = SERVICE_NAME)
    public void createAudit(ApplicationTrackingOutputResult applicationTrackingOutputResult) {
        Integer usn = applicationTrackingOutputResult.getUsn();
        log.info("Start - call to create Audit record for {}", usn);

        Audit audit = Audit.builder()
            .usn(usn)
            .maatRef(applicationTrackingOutputResult.getMaatRef())
            .userCreated(applicationTrackingOutputResult.getUserCreated())
            .statusCode(STATUS_CODE).build();

        auditRepository.save(audit);

        Observation.createNotStarted(SERVICE_NAME, observationRegistry)
            .observe(() -> log.info("Audit record is created successfully"));
    }
}
