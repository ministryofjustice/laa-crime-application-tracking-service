package uk.gov.justice.laa.crime.application.tracking.service;

import io.github.resilience4j.retry.annotation.Retry;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.justice.laa.crime.application.tracking.entity.DecisionHistory;
import uk.gov.justice.laa.crime.application.tracking.exception.ApplicationTrackingException;
import uk.gov.justice.laa.crime.application.tracking.helper.ReflectionHelper;
import uk.gov.justice.laa.crime.application.tracking.repository.DecisionHistoryRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class DecisionHistoryService {
    private static final String SERVICE_NAME = "decisionHistoryService";
    private static final String WROTE_TO_RESULTS = "Y";
    private static final String USN_NOT_FOUND = "The USN [%d] not found in Decision History table";

    private final DecisionHistoryRepository decisionHistoryRepository;
    private final ObservationRegistry observationRegistry;

    @Retry(name = SERVICE_NAME)
    public void createDecisionHistoryRecord(DecisionHistory decisionHistory) {
        log.info("Start - call to create Decision History for {}", decisionHistory.getUsn());
        decisionHistoryRepository.save(decisionHistory);
        Observation.createNotStarted(SERVICE_NAME, observationRegistry)
            .observe(() -> log.info("Decision History record is created successfully"));
    }

    @Retry(name = SERVICE_NAME)
    public DecisionHistory getPreviousDecisionResult(Integer usn) {
        log.info("Start - call to get previous Decision History Wrote to Result for {}", usn);
        DecisionHistory decisionHistory = getPreviousDecisionHistoryRecordWroteToResult(usn);
        return Observation.createNotStarted(SERVICE_NAME, observationRegistry)
            .observe(() -> decisionHistory);
    }

    @Retry(name = SERVICE_NAME)
    public void updateWroteResult(Integer usn) {
        log.info("Start - call to update wrote to result in Decision History for {}", usn);
        DecisionHistory decisionHistory = DecisionHistory.builder().wroteToResults(WROTE_TO_RESULTS).build();
        updateDecisionHistoryFields(usn, decisionHistory);
        Observation.createNotStarted(SERVICE_NAME, observationRegistry)
            .observe(() -> log.info("Decision History record is updated successfully"));
    }

    @Transactional(readOnly = true)
    public DecisionHistory getPreviousDecisionHistoryRecordWroteToResult(Integer usn) {
        return decisionHistoryRepository.findFirstByUsnAndWroteToResultsOrderByIdDesc(usn, WROTE_TO_RESULTS)
            .orElse(DecisionHistory.builder().build());
    }

    @Transactional
    public void updateDecisionHistoryFields(Integer usn, DecisionHistory decisionHistory) {
        DecisionHistory latestDecisionHistory = Optional.ofNullable(decisionHistoryRepository.findTopByUsnOrderByIdDesc(usn))
            .orElseThrow(() -> new ApplicationTrackingException(
                HttpStatus.NOT_FOUND, String.format(USN_NOT_FOUND, usn)));

        ReflectionHelper.updateEntityFromObject(latestDecisionHistory, decisionHistory);
        decisionHistoryRepository.save(latestDecisionHistory);
    }
}
