package uk.gov.justice.laa.crime.application.tracking.service;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.justice.laa.crime.application.tracking.client.MaatCourtDataApiClient;
import uk.gov.justice.laa.crime.application.tracking.model.ApplicationTrackingOutputResult;
import uk.gov.justice.laa.crime.application.tracking.model.EformsHistory;

@Service
@RequiredArgsConstructor
@Slf4j
public class EformsHistoryService {
    private static final String SERVICE_NAME = "eformsHistoryService";
    private final MaatCourtDataApiClient maatCourtDataApiClient;
    private final ObservationRegistry observationRegistry;
    public void createEformHistory(ApplicationTrackingOutputResult applicationTrackingOutputResult) {
        Integer usn = applicationTrackingOutputResult.getUsn();
        log.info("Start - call to Create Eforms History API {}", usn);
        EformsHistory eformsHistory = EformsHistory.builder()
                .usn(usn)
                .repId(applicationTrackingOutputResult.getMaatRef())
                .action(String.valueOf(applicationTrackingOutputResult.getAction()))
                .keyId(applicationTrackingOutputResult.getActionKeyId())
                .userCreated(applicationTrackingOutputResult.getUserCreated()).build();
        maatCourtDataApiClient.createEformsHistoryRecord(eformsHistory);
        Observation.createNotStarted(SERVICE_NAME, observationRegistry)
                .observe(() -> log.info("Eform History Record is Created Successfully"));
    }
}
