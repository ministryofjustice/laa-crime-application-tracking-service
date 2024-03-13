package uk.gov.justice.laa.crime.application.tracking.service;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.justice.laa.crime.application.tracking.client.MaatCourtDataApiClient;
import uk.gov.justice.laa.crime.application.tracking.model.ApplicationTrackingOutputResult;
import uk.gov.justice.laa.crime.application.tracking.model.EformResults;
import uk.gov.justice.laa.crime.application.tracking.util.OutputResultUtil;

@Service
@RequiredArgsConstructor
@Slf4j
public class EformResultsService {
    private static final String SERVICE_NAME = "eformResultsService";
    private final MaatCourtDataApiClient maatCourtDataApiClient;
    private final ObservationRegistry observationRegistry;
    public void createEformResult(ApplicationTrackingOutputResult applicationTrackingOutputResult, String fundingDecision) {
        log.info("Start - call to Create Eforms Result for {}", applicationTrackingOutputResult.getUsn());
        EformResults eformResults = OutputResultUtil.buildEformResult(applicationTrackingOutputResult, fundingDecision);
        maatCourtDataApiClient.createEformResult(eformResults);
        Observation.createNotStarted(SERVICE_NAME, observationRegistry)
                .observe(() -> log.info("Eform Result Record is Created Successfully"));
    }
}
