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

    public static final String Z = "Z";
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
        if (Objects.nonNull(applicationTrackingOutputResult.getMaatRef())
                && Objects.isNull(applicationTrackingOutputResult.getIoj().getIojAssessorName())) {
            AssessorDetails iojAssessor = assessmentAssessorService.getIOJAssessor(applicationTrackingOutputResult.getMaatRef());
            applicationTrackingOutputResult.getIoj().setIojAssessorName(iojAssessor.getFullName());
        }
    }

    private void mapMeansAssessorName(ApplicationTrackingOutputResult applicationTrackingOutputResult) {
        MeansAssessment meansAssessment = applicationTrackingOutputResult.getMeansAssessment();
        if (Objects.nonNull(meansAssessment.getMeansAssessmentId())
                && Objects.isNull(meansAssessment.getMeansAssessorName())) {
            AssessorDetails meansAssessor = assessmentAssessorService.getMeansAssessor(meansAssessment.getMeansAssessmentId());
            meansAssessment.setMeansAssessorName(meansAssessor.getFullName());
        }
    }

    private void mapPassportedAssessorName(ApplicationTrackingOutputResult applicationTrackingOutputResult) {
        Passport passport = applicationTrackingOutputResult.getPassport();
        if (Objects.nonNull(passport.getPassportId())
                && Objects.isNull(passport.getPassportAssessorName())) {
            AssessorDetails passportAssessor = assessmentAssessorService.getPassportAssessor(passport.getPassportId());
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
                && checkResultChanged(applicationTrackingOutputResult, fundingDecision)) {
            eformResultsService.createEformResult(applicationTrackingOutputResult, fundingDecision);
            Integer usn = applicationTrackingOutputResult.getUsn();
            eformsDecisionHistoryService.updateWroteResult(usn);
            eformStagingService.updateStatus(usn);
        }
    }

    private boolean checkResultChanged(ApplicationTrackingOutputResult applicationTrackingOutputResult, String fundingDecision) {
        EformsDecisionHistory previousResult = eformsDecisionHistoryService.getPreviousDecisionResult(applicationTrackingOutputResult.getUsn());
        if (Objects.nonNull(previousResult.getId())) {
            BiPredicate<ApplicationTrackingOutputResult, EformsDecisionHistory> hasResultChanged = (outputResult, previousRecord) ->
            {
                String iojResult = Objects.nonNull(outputResult.getIoj()) ? outputResult.getIoj().getIojResult() : Z;
                String meansResult = Objects.nonNull(outputResult.getMeansAssessment()) ? outputResult.getMeansAssessment().getMeansAssessmentResult().value() : Z;
                String passportResult = Objects.nonNull(outputResult.getPassport()) ? outputResult.getPassport().getPassportResult().value() : Z;

                return iojResult.equals(Objects.requireNonNullElse(previousRecord.getIojResult(), Z))
                        && meansResult.equals(Objects.requireNonNullElse(previousRecord.getMeansResult(), Z))
                        && passportResult.equals(Objects.requireNonNullElse(previousRecord.getPassportResult(), Z))
                        && Objects.requireNonNullElse(fundingDecision, Z).equals(Objects.requireNonNullElse(previousRecord.getFundingDecision(), Z));
            };
            return !hasResultChanged.test(applicationTrackingOutputResult, previousResult);
        }
        return true;
    }
}
