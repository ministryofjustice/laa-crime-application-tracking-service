package uk.gov.justice.laa.crime.application.tracking.service;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.justice.laa.crime.application.tracking.client.MaatCourtDataApiClient;
import uk.gov.justice.laa.crime.application.tracking.model.EformsStaging;

@Service
@RequiredArgsConstructor
@Slf4j
public class EformStagingService {

    private static final String SERVICE_NAME = "eformStagingService";
    private final MaatCourtDataApiClient maatCourtDataApiClient;
    private final ObservationRegistry observationRegistry;
    private static final String PURGE ="PURGE";
    public void updateMaatId(Integer usn, Integer maatRef) {
        log.info("Start - call to update MAAT Id for {}", usn);
        EformsStaging eformsStaging = EformsStaging.builder().maatRef(maatRef).build();
        maatCourtDataApiClient.updateEformStagingRecord(usn, eformsStaging);
        Observation.createNotStarted(SERVICE_NAME, observationRegistry)
                .observe(() -> log.info("MAAT Id is updated Successfully"));
    }

    public void updateStatus(Integer usn) {
        log.info("Start - call to update MAAT Status for {}", usn);
        EformsStaging eformsStaging = EformsStaging.builder().maatStatus(PURGE).build();
        maatCourtDataApiClient.updateEformStagingRecord(usn, eformsStaging);
        Observation.createNotStarted(SERVICE_NAME, observationRegistry)
                .observe(() -> log.info("MAAT Status is updated Successfully"));
    }
}
