package uk.gov.justice.laa.crime.application.tracking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.justice.laa.crime.application.tracking.model.*;
import uk.gov.justice.laa.crime.application.tracking.util.BuildRequestsUtil;
import uk.gov.justice.laa.crime.application.tracking.util.FundingDecisionUtil;

import java.util.Objects;
import java.util.function.BiPredicate;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplicationOutputResultService {

    private static final String DEFAULT_VALUE = "Z";
    private final AssessmentAssessorService assessmentAssessorService;
    private final EformsDecisionHistoryService eformsDecisionHistoryService;
    private final EformResultsService eformResultsService;
    private final EformStagingService eformStagingService;

    public void processOutputResult(ApplicationTrackingOutputResult applicationTrackingOutputResult) {
        if (!isOutstandingAssessment(applicationTrackingOutputResult.getMaatRef())) {
            String fundingDecision = FundingDecisionUtil.getFundingDecision(applicationTrackingOutputResult);
            getAssessorNames(applicationTrackingOutputResult);
            createEformDecisionHistoryRecord(applicationTrackingOutputResult, fundingDecision);
            createEformResultRecord(applicationTrackingOutputResult, fundingDecision);
        }
    }

    private boolean isOutstandingAssessment(Integer maatRef) {
        OutstandingAssessment outstandingAssessment = assessmentAssessorService.checkOutstandingAssessment(maatRef);
        return outstandingAssessment.isOutstandingAssessments();
    }

    private void getAssessorNames(ApplicationTrackingOutputResult applicationTrackingOutputResult) {
        mapIojAssessorName(applicationTrackingOutputResult);
        mapMeansAssessorName(applicationTrackingOutputResult);
        mapPassportedAssessorName(applicationTrackingOutputResult);
    }

    private void mapIojAssessorName(ApplicationTrackingOutputResult applicationTrackingOutputResult) {
        Integer maatRef = applicationTrackingOutputResult.getMaatRef();
        if (Objects.nonNull(maatRef)
                && Objects.isNull(applicationTrackingOutputResult.getIoj().getIojAssessorName())) {
            AssessorDetails iojAssessor = assessmentAssessorService.getIOJAssessor(maatRef);
            applicationTrackingOutputResult.getIoj().setIojAssessorName(iojAssessor.getFullName());
        }
    }


    private void mapMeansAssessorName(ApplicationTrackingOutputResult applicationTrackingOutputResult) {
        MeansAssessment meansAssessment = applicationTrackingOutputResult.getMeansAssessment();
        Integer meansAssessmentId = meansAssessment.getMeansAssessmentId();
        if (Objects.nonNull(meansAssessmentId)
                && Objects.isNull(meansAssessment.getMeansAssessorName())) {
            AssessorDetails meansAssessor = assessmentAssessorService.getMeansAssessor(meansAssessmentId);
            meansAssessment.setMeansAssessorName(meansAssessor.getFullName());
        }
    }


    private void mapPassportedAssessorName(ApplicationTrackingOutputResult applicationTrackingOutputResult) {
        Passport passport = applicationTrackingOutputResult.getPassport();
        Integer passportId = passport.getPassportId();
        if (Objects.nonNull(passportId)
                && Objects.isNull(passport.getPassportAssessorName())) {
            AssessorDetails passportAssessor = assessmentAssessorService.getPassportAssessor(passportId);
            passport.setPassportAssessorName(passportAssessor.getFullName());
        }
    }

    private void createEformDecisionHistoryRecord(ApplicationTrackingOutputResult applicationTrackingOutputResult, String fundingDecision) {
        if (Objects.nonNull(applicationTrackingOutputResult.getRepDecision())
                || Objects.nonNull(applicationTrackingOutputResult.getCcRepDecision())) {
            EformsDecisionHistory eformsDecisionHistory = BuildRequestsUtil.buildEformDecisionHistory(applicationTrackingOutputResult, fundingDecision);
            eformsDecisionHistoryService.createEformsDecisionHistoryRecord(eformsDecisionHistory);
        }
    }

    private void createEformResultRecord(ApplicationTrackingOutputResult applicationTrackingOutputResult, String fundingDecision) {
        if (Objects.nonNull(fundingDecision)
                && isOutputResultChanged(applicationTrackingOutputResult, fundingDecision)) {
            eformResultsService.createEformResult(applicationTrackingOutputResult, fundingDecision);
            Integer usn = applicationTrackingOutputResult.getUsn();
            eformsDecisionHistoryService.updateWroteResult(usn);
            eformStagingService.updateStatus(usn);
        }
    }

    private boolean isOutputResultChanged(ApplicationTrackingOutputResult applicationTrackingOutputResult, String fundingDecision) {
        EformsDecisionHistory previousResult = eformsDecisionHistoryService.getPreviousDecisionResult(applicationTrackingOutputResult.getUsn());
        if (Objects.nonNull(previousResult) && Objects.nonNull(previousResult.getId())) {
            BiPredicate<ApplicationTrackingOutputResult, EformsDecisionHistory> hasResultChanged = (outputResult, previousRecord) ->
            {
                String iojResult = Objects.nonNull(outputResult.getIoj()) ? outputResult.getIoj().getIojResult() : DEFAULT_VALUE;
                String meansResult = Objects.nonNull(outputResult.getMeansAssessment()) ? outputResult.getMeansAssessment().getMeansAssessmentResult().value() : DEFAULT_VALUE;
                String passportResult = Objects.nonNull(outputResult.getPassport()) ? outputResult.getPassport().getPassportResult().value() : DEFAULT_VALUE;

                return iojResult.equals(Objects.requireNonNullElse(previousRecord.getIojResult(), DEFAULT_VALUE))
                        && meansResult.equals(Objects.requireNonNullElse(previousRecord.getMeansResult(), DEFAULT_VALUE))
                        && passportResult.equals(Objects.requireNonNullElse(previousRecord.getPassportResult(), DEFAULT_VALUE))
                        && Objects.requireNonNullElse(fundingDecision, DEFAULT_VALUE).equals(Objects.requireNonNullElse(previousRecord.getFundingDecision(), DEFAULT_VALUE));
            };
            return !hasResultChanged.test(applicationTrackingOutputResult, previousResult);
        }
        return true;
    }
}
