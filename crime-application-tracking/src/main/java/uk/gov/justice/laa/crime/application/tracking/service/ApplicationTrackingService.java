package uk.gov.justice.laa.crime.application.tracking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.justice.laa.crime.application.tracking.model.ApplicationTrackingOutputResult;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplicationTrackingService {

    private final AuditService auditService;
    private final EformStagingService eformStagingService;
    private final HistoryService historyService;
    private final ApplicationOutputResultService applicationOutputResultService;

    @Transactional
    public void processApplicationTrackingAndOutputResultData(ApplicationTrackingOutputResult applicationTrackingOutputResult) {
        Integer usn = applicationTrackingOutputResult.getUsn();
        ApplicationTrackingOutputResult.RequestSource requestSource = applicationTrackingOutputResult.getRequestSource();
        log.info("Start process to handle Application Tracking and Output Result for {} with USN {}", requestSource.value(), usn);
        switch (requestSource) {
            case CREATE_APPLICATION -> {
                auditService.createAudit(applicationTrackingOutputResult);
                eformStagingService.updateMaatId(usn, applicationTrackingOutputResult.getMaatRef());
                historyService.createHistory(applicationTrackingOutputResult);
                applicationOutputResultService.processOutputResult(applicationTrackingOutputResult);
            }
            case HARDSHIP, CROWN_COURT -> applicationOutputResultService.processOutputResult(applicationTrackingOutputResult);
            case CAPITAL_AND_EQUITY -> eformStagingService.updateStatus(usn);
            case PASSPORT_IOJ, MEANS_ASSESSMENT -> {
                historyService.createHistory(applicationTrackingOutputResult);
                applicationOutputResultService.processOutputResult(applicationTrackingOutputResult);
            }
        }
        log.info("Successfully processed Application Tracking and Output Result for {} with USN {}", requestSource.value(), usn);
    }
}
