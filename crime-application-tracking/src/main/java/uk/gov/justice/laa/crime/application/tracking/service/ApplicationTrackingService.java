package uk.gov.justice.laa.crime.application.tracking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.justice.laa.crime.application.tracking.model.ApplicationTrackingOutputResult;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplicationTrackingService {

    private final EformAuditService eformAuditService;
    private final EformStagingService eformStagingService;
    private final EformsHistoryService eformsHistoryService;
    private final ApplicationOutputResultService applicationOutputResultService;

    public void processApplicationTrackingAndOutputResultData(ApplicationTrackingOutputResult applicationTrackingOutputResult) {

        if (Objects.nonNull(applicationTrackingOutputResult.getUsn())
                && Objects.nonNull(applicationTrackingOutputResult.getRequestSource())) {
            ApplicationTrackingOutputResult.RequestSource requestSource = applicationTrackingOutputResult.getRequestSource();
            switch (requestSource) {
                case CREATE_APPLICATION -> {
                    eformAuditService.createAudit(applicationTrackingOutputResult);
                    eformStagingService.updateMaatId(applicationTrackingOutputResult.getUsn(), applicationTrackingOutputResult.getMaatRef());
                    eformsHistoryService.createEformHistory(applicationTrackingOutputResult);
                    applicationOutputResultService.processOutputResult(applicationTrackingOutputResult);
                }
                case HARDSHIP, CROWN_COURT -> applicationOutputResultService.processOutputResult(applicationTrackingOutputResult);
                case CAPITAL_AND_EQUITY -> eformStagingService.updateStatus(applicationTrackingOutputResult.getUsn());
                case ASSESSMENTS, PASSPORT_IOJ -> {
                    eformsHistoryService.createEformHistory(applicationTrackingOutputResult);
                    applicationOutputResultService.processOutputResult(applicationTrackingOutputResult);
                }
            }
        }
    }
}
