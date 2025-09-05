package uk.gov.justice.laa.crime.application.tracking.service;

import io.github.resilience4j.retry.annotation.Retry;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.justice.laa.crime.application.tracking.entity.History;
import uk.gov.justice.laa.crime.application.tracking.model.ApplicationTrackingOutputResult;
import uk.gov.justice.laa.crime.application.tracking.repository.HistoryRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class HistoryService {
    private static final String SERVICE_NAME = "historyService";
    private final HistoryRepository historyRepository;
    private final ObservationRegistry observationRegistry;

    @Retry(name = SERVICE_NAME)
    public void createHistory(ApplicationTrackingOutputResult applicationTrackingOutputResult) {
        Integer usn = applicationTrackingOutputResult.getUsn();
        log.info("Start - call to create History record for {}", usn);

        History history = History.builder()
            .usn(usn)
            .repId(applicationTrackingOutputResult.getMaatRef())
            .action(String.valueOf(applicationTrackingOutputResult.getAction()))
            .keyId(applicationTrackingOutputResult.getActionKeyId())
            .userCreated(applicationTrackingOutputResult.getUserCreated()).build();

        historyRepository.save(history);

        Observation.createNotStarted(SERVICE_NAME, observationRegistry)
            .observe(() -> log.info("History record is created successfully"));
    }
}
