package uk.gov.justice.laa.crime.application.tracking.service;

import io.github.resilience4j.retry.annotation.Retry;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.justice.laa.crime.application.tracking.entity.Result;
import uk.gov.justice.laa.crime.application.tracking.model.ApplicationTrackingOutputResult;
import uk.gov.justice.laa.crime.application.tracking.repository.ResultRepository;
import uk.gov.justice.laa.crime.application.tracking.util.BuildRequestsUtil;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResultService {
    private static final String SERVICE_NAME = "resultService";
    private final ResultRepository resultRepository;
    private final ObservationRegistry observationRegistry;

    @Retry(name = SERVICE_NAME)
    public void createResult(ApplicationTrackingOutputResult applicationTrackingOutputResult, String fundingDecision) {
        log.info("Start - call to create Result for {}", applicationTrackingOutputResult.getUsn());
        Result result = BuildRequestsUtil.buildResult(applicationTrackingOutputResult, fundingDecision);
        resultRepository.save(result);
        Observation.createNotStarted(SERVICE_NAME, observationRegistry)
            .observe(() -> log.info("Result record is created successfully"));
    }
}
