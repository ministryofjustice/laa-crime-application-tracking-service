package uk.gov.justice.laa.crime.application.tracking.util;

import lombok.experimental.UtilityClass;
import uk.gov.justice.laa.crime.application.tracking.model.ApplicationTrackingOutputResult;
import uk.gov.justice.laa.crime.application.tracking.model.MeansAssessment;
import uk.gov.justice.laa.crime.application.tracking.model.Passport;

@UtilityClass
public class FundingDecisionUtil {
    public static final String COMMITTED_FOR_TRIAL = "COMMITTED FOR TRIAL";
    public static final String GRANTED_PASSPORTED = "Granted - Passported";
    public static final String GRANTED_PASSED_MEANS_TEST = "Granted - Passed Means Test";
    public static final String GRANTED = "Granted";
    public static final String FAILED_CF_S_FAILED_MEANS_TEST = "Failed - CfS Failed Means Test";
    public static final String REFUSED_INELIGIBLE = "Refused - Ineligible";
    public static final String PASS = "PASS";

    public String getFundingDecision(ApplicationTrackingOutputResult applicationTrackingOutputResult) {

        ApplicationTrackingOutputResult.AssessmentType assessmentType = applicationTrackingOutputResult.getAssessmentType();

        if (!(ApplicationTrackingOutputResult.AssessmentType.PASSPORT.equals(assessmentType) &&
                Passport.PassportResult.FAIL_CONTINUE.equals(applicationTrackingOutputResult.getPassport().getPassportResult()))) {
            ApplicationTrackingOutputResult.CaseType caseType = applicationTrackingOutputResult.getCaseType();
            switch (caseType) {
                case INDICTABLE, CC_ALREADY, APPEAL_CC -> {
                    return mapFundingDecision(applicationTrackingOutputResult);
                }
                case COMMITAL -> {
                    return mapFundingDecisionForCommittal(applicationTrackingOutputResult);
                }
                default -> {
                    if (!(ApplicationTrackingOutputResult.AssessmentType.MEANS_INIT.equals(assessmentType)
                            && MeansAssessment.MeansAssessmentResult.FAIL.equals(applicationTrackingOutputResult.getMeansAssessment().getMeansAssessmentResult()))
                            && !ApplicationTrackingOutputResult.CaseType.EITHER_WAY.equals(caseType)
                            && !COMMITTED_FOR_TRIAL.equals(applicationTrackingOutputResult.getMagsOutcome())) {
                        return applicationTrackingOutputResult.getRepDecision();
                    }
                }
            }
        }
        return null;
    }

    private String mapFundingDecisionForCommittal(ApplicationTrackingOutputResult applicationTrackingOutputResult) {
        applicationTrackingOutputResult.getIoj().setIojResult(PASS);
        String ccRepDecision = applicationTrackingOutputResult.getCcRepDecision();
        if (GRANTED_PASSPORTED.equals(ccRepDecision)

                || GRANTED_PASSED_MEANS_TEST.equals(ccRepDecision)) {
            return GRANTED;
        } else if (FAILED_CF_S_FAILED_MEANS_TEST.equals(ccRepDecision)) {
            return ccRepDecision;
        }
        return null;
    }

    private String mapFundingDecision(ApplicationTrackingOutputResult applicationTrackingOutputResult) {
        ApplicationTrackingOutputResult.AssessmentType assessmentType = applicationTrackingOutputResult.getAssessmentType();
        MeansAssessment.MeansAssessmentResult meansAssessmentResult = applicationTrackingOutputResult.getMeansAssessment().getMeansAssessmentResult();
        if (!ApplicationTrackingOutputResult.AssessmentType.MEANS_INIT.equals(assessmentType)
                && !MeansAssessment.MeansAssessmentResult.FAIL.equals(meansAssessmentResult)) {

            if (ApplicationTrackingOutputResult.AssessmentType.MEANS_FULL.equals(assessmentType)
                    && MeansAssessment.MeansAssessmentResult.INEL.equals(meansAssessmentResult)) {
                return REFUSED_INELIGIBLE;
            }

            return applicationTrackingOutputResult.getCcRepDecision();
        }
        return null;
    }
}
