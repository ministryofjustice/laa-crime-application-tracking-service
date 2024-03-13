package uk.gov.justice.laa.crime.application.tracking.service;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.justice.laa.crime.application.tracking.client.MaatCourtDataApiClient;
import uk.gov.justice.laa.crime.application.tracking.model.ApplicationTrackingOutputResult;
import uk.gov.justice.laa.crime.application.tracking.model.EformsAudit;

@Service
@RequiredArgsConstructor
@Slf4j
public class EformAuditService {
    private static final String SERVICE_NAME = "eformAuditService";
    private final MaatCourtDataApiClient maatCourtDataApiClient;
    private static final String STATUS_CODE = "Processing";
    private final ObservationRegistry observationRegistry;

    public void createAudit(ApplicationTrackingOutputResult applicationTrackingOutputResult) {
        Integer usn = applicationTrackingOutputResult.getUsn();
        log.info("Start - call to Create Eforms Audit record for {}", usn);
        EformsAudit eformsAudit = EformsAudit.builder()
                .usn(usn)
                .maatRef(applicationTrackingOutputResult.getMaatRef())
                .userCreated(applicationTrackingOutputResult.getUserCreated())
                .statusCode(STATUS_CODE).build();
        maatCourtDataApiClient.createEformsAuditRecord(eformsAudit);
        Observation.createNotStarted(SERVICE_NAME, observationRegistry)
                .observe(() -> log.info("Eform Audit Record is Created Successfully"));
    }
}
