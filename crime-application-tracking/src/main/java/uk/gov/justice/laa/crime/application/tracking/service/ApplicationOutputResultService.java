package uk.gov.justice.laa.crime.application.tracking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.justice.laa.crime.application.tracking.model.ApplicationTrackingOutputResult;
import uk.gov.justice.laa.crime.application.tracking.model.EformsDecisionHistory;
import uk.gov.justice.laa.crime.application.tracking.model.OutstandingAssessment;
import uk.gov.justice.laa.crime.application.tracking.util.OutputResultUtil;

import java.util.Objects;
import java.util.function.BiPredicate;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplicationOutputResultService {

    private final AssessmentAssessorService assessmentAssessorService;
    private final EformsDecisionHistoryService eformsDecisionHistoryService;
    private final EformResultsService eformResultsService;
    private final EformStagingService eformStagingService;

    public void processOutputResult(ApplicationTrackingOutputResult applicationTrackingOutputResult) {
        if (isOutstandingAssessment(applicationTrackingOutputResult.getMaatRef())) {
            String fundingDecision = OutputResultUtil.getFundingDecision(applicationTrackingOutputResult);
            createEformDecisionHistoryRecord(applicationTrackingOutputResult, fundingDecision);
            createEformResultRecord(applicationTrackingOutputResult, fundingDecision);
        }

    }

    private boolean isOutstandingAssessment(Integer maatRef) {
        OutstandingAssessment outstandingAssessment = assessmentAssessorService.checkOutstandingAssessment(maatRef);
        return outstandingAssessment.isOutstandingAssessments();
    }

    private void createEformDecisionHistoryRecord(ApplicationTrackingOutputResult applicationTrackingOutputResult, String fundingDecision) {
        if (Objects.nonNull(applicationTrackingOutputResult.getRepDecision())
                || Objects.nonNull(applicationTrackingOutputResult.getCcRepDecision())){
            EformsDecisionHistory eformsDecisionHistory = OutputResultUtil.buildEformDecisionHistory(applicationTrackingOutputResult, fundingDecision);
            eformsDecisionHistoryService.createEformsDecisionHistoryRecord(eformsDecisionHistory);
        }
    }
    private void createEformResultRecord(ApplicationTrackingOutputResult applicationTrackingOutputResult, String fundingDecision) {
        if (Objects.nonNull(fundingDecision)
                && checkResultChanged(applicationTrackingOutputResult, fundingDecision)){
            eformResultsService.createEformResult(applicationTrackingOutputResult, fundingDecision);
            Integer usn = applicationTrackingOutputResult.getUsn();
            eformsDecisionHistoryService.updateWroteResult(usn);
            eformStagingService.updateStatus(usn);
        }
    }
    private boolean checkResultChanged(ApplicationTrackingOutputResult applicationTrackingOutputResult, String fundingDecision) {
        EformsDecisionHistory previousResult = eformsDecisionHistoryService.getPreviousDesionResult(applicationTrackingOutputResult.getUsn());
        if (!Objects.isNull(previousResult.getId())) {
            BiPredicate<ApplicationTrackingOutputResult, EformsDecisionHistory> filter = (outputResult, previousRecord) -> Objects.requireNonNullElse(outputResult.getIoj().getIojResult(), "Z").equals(Objects.requireNonNullElse(previousRecord.getIojResult(), "Z"))
                    && Objects.requireNonNullElse(outputResult.getMeansAssessment().getMeansAssessmentResult(), "Z").equals(Objects.requireNonNullElse(previousRecord.getMeansResult(), "Z"))
                    && Objects.requireNonNullElse(outputResult.getPassport().getPassportResult(), "Z").equals(Objects.requireNonNullElse(previousRecord.getPassportResult(), "Z"))
                    && Objects.requireNonNullElse(fundingDecision, "Z").equals(Objects.requireNonNullElse(previousRecord.getFundingDecision(), "Z"));
            return !filter.test(applicationTrackingOutputResult, previousResult);
        }
        return true;
    }
}
