package uk.gov.justice.laa.crime.application.tracking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.justice.laa.crime.application.tracking.model.ApplicationTrackingOutputResult;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplicationTrackingService {

    private final EformAuditService eformAuditService;
    private final EformStagingService eformStagingService;
    private final EformsHistoryService eformsHistoryService;
    private final ApplicationOutputResultService applicationOutputResultService;

    public void processApplicationTrackingAndOutputResultData(ApplicationTrackingOutputResult applicationTrackingOutputResult) {
        Integer usn = applicationTrackingOutputResult.getUsn();
        ApplicationTrackingOutputResult.RequestSource requestSource = applicationTrackingOutputResult.getRequestSource();
        log.info("Start process to handle Application Tracking and Output Result for {} with USN {}", requestSource.value(), usn);
        switch (requestSource) {
            case CREATE_APPLICATION -> {
                eformAuditService.createAudit(applicationTrackingOutputResult);
                eformStagingService.updateMaatId(usn, applicationTrackingOutputResult.getMaatRef());
                eformsHistoryService.createEformHistory(applicationTrackingOutputResult);
                applicationOutputResultService.processOutputResult(applicationTrackingOutputResult);
            }
            case HARDSHIP, CROWN_COURT -> applicationOutputResultService.processOutputResult(applicationTrackingOutputResult);
            case CAPITAL_AND_EQUITY -> eformStagingService.updateStatus(usn);
            case PASSPORT_IOJ, MEANS_ASSESSMENT -> {
                eformsHistoryService.createEformHistory(applicationTrackingOutputResult);
                applicationOutputResultService.processOutputResult(applicationTrackingOutputResult);
            }
        }
        log.info("Successfully processed Application Tracking and Output Result for {} with USN {}", requestSource.value(), usn);
    }
}
