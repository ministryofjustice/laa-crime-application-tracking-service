package uk.gov.justice.laa.crime.application.tracking.service;

import io.github.resilience4j.retry.annotation.Retry;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.justice.laa.crime.application.tracking.client.MaatCourtDataApiClient;
import uk.gov.justice.laa.crime.application.tracking.model.EformsDecisionHistory;

@Service
@RequiredArgsConstructor
@Slf4j
public class EformsDecisionHistoryService {
    private static final String SERVICE_NAME = "eformsDecisionHistoryService";
    private static final String WROTE_TO_RESULTS = "Y";
    private final MaatCourtDataApiClient maatCourtDataApiClient;
    private final ObservationRegistry observationRegistry;

    @Retry(name = SERVICE_NAME)
    public void createEformsDecisionHistoryRecord(EformsDecisionHistory eformsDecisionHistory) {
        log.info("Start - call to Create Eforms Decision History for {}", eformsDecisionHistory.getUsn());
        maatCourtDataApiClient.createEformsDecisionHistoryRecord(eformsDecisionHistory);
        Observation.createNotStarted(SERVICE_NAME, observationRegistry)
                .observe(() -> log.info("Eform Decision History Record is Created Successfully"));
    }

    @Retry(name = SERVICE_NAME)
    public EformsDecisionHistory getPreviousDecisionResult(Integer usn) {
        log.info("Start - call to get previous Eforms Decision History Wrote to Result for {}", usn);
        EformsDecisionHistory eformsDecisionHistory = maatCourtDataApiClient.getPreviousEformsDecisionHistoryRecordWroteToResult(usn);
        return Observation.createNotStarted(SERVICE_NAME, observationRegistry)
                .observe(() -> eformsDecisionHistory);
    }

    @Retry(name = SERVICE_NAME)
    public void updateWroteResult(Integer usn) {
        log.info("Start - call to update wrote to result in Eforms Decision History for {}", usn);
        EformsDecisionHistory eformsDecisionHistory = EformsDecisionHistory.builder().wroteToResults(WROTE_TO_RESULTS).build();
        maatCourtDataApiClient.updateEformsDecisionHistoryRecord(usn, eformsDecisionHistory);
        Observation.createNotStarted(SERVICE_NAME, observationRegistry)
                .observe(() -> log.info("Eform Decision History Record is updated Successfully"));
    }
}
