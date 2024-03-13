package uk.gov.justice.laa.crime.application.tracking.util;

import lombok.experimental.UtilityClass;
import uk.gov.justice.laa.crime.application.tracking.model.*;

import java.time.LocalDate;

@UtilityClass
public class OutputResultUtil {
    public static final String COMMITTED_FOR_TRIAL = "COMMITTED FOR TRIAL";
    public static final String GRANTED_PASSPORTED = "Granted - Passported";
    public static final String GRANTED_PASSED_MEANS_TEST = "Granted - Passed Means Test";
    public static final String GRANTED = "Granted";
    public static final String FAILED_CF_S_FAILED_MEANS_TEST = "Failed - CfS Failed Means Test";
    public static final String REFUSED_INELIGIBLE = "Refused - Ineligible";
    public static final String WROTE_TO_RESULTS = "N";

    public String getFundingDecision(ApplicationTrackingOutputResult applicationTrackingOutputResult) {
        String fundDecision = null;
        if (!applicationTrackingOutputResult.getAssessmentType().equals(ApplicationTrackingOutputResult.AssessmentType.PASSPORT) &&
                !applicationTrackingOutputResult.getPassport().getPassportResult().equals(Passport.PassportResult.FAIL_CONTINUE)) {
            ApplicationTrackingOutputResult.CaseType caseType = applicationTrackingOutputResult.getCaseType();
            switch (caseType) {
                case INDICTABLE, CC_ALREADY, APPEAL_CC ->
                        fundDecision = mapFundingDecision(applicationTrackingOutputResult);
                case COMMITAL -> fundDecision = mapFundingDecisionForCommital(applicationTrackingOutputResult);
                default -> {
                    if (!applicationTrackingOutputResult.getAssessmentType().equals(ApplicationTrackingOutputResult.AssessmentType.MEANS_INIT)
                            && !applicationTrackingOutputResult.getMeansAssessment().getMeansAssessmentResult().equals(MeansAssessment.MeansAssessmentResult.FAIL)
                            && !caseType.equals(ApplicationTrackingOutputResult.CaseType.EITHER_WAY)
                            && !applicationTrackingOutputResult.getMagsOutcome().equals(COMMITTED_FOR_TRIAL)) {

                        fundDecision = applicationTrackingOutputResult.getRepDecision();
                    }
                }
            }
        }
        return fundDecision;
    }

    private String mapFundingDecisionForCommital(ApplicationTrackingOutputResult applicationTrackingOutputResult) {
        String fundingDecision = null;
        String ccRepDecision = applicationTrackingOutputResult.getCcRepDecision();
        if (ccRepDecision.equals(GRANTED_PASSPORTED)
                || ccRepDecision.equals(GRANTED_PASSED_MEANS_TEST)) {
            fundingDecision = GRANTED;
        } else if (ccRepDecision.equals(FAILED_CF_S_FAILED_MEANS_TEST)) {
            fundingDecision = ccRepDecision;
        }
        return fundingDecision;
    }

    private String mapFundingDecision(ApplicationTrackingOutputResult applicationTrackingOutputResult) {
        String fundingDecision = null;
        ApplicationTrackingOutputResult.AssessmentType assessmentType = applicationTrackingOutputResult.getAssessmentType();
        MeansAssessment.MeansAssessmentResult meansAssessmentResult = applicationTrackingOutputResult.getMeansAssessment().getMeansAssessmentResult();
        if (!assessmentType.equals(ApplicationTrackingOutputResult.AssessmentType.MEANS_INIT)
                && !meansAssessmentResult.equals(MeansAssessment.MeansAssessmentResult.FAIL)) {
            if (assessmentType.equals(ApplicationTrackingOutputResult.AssessmentType.MEANS_FULL)
                    && meansAssessmentResult.equals(MeansAssessment.MeansAssessmentResult.INEL)) {
                fundingDecision = REFUSED_INELIGIBLE;
            } else {
                fundingDecision = applicationTrackingOutputResult.getCcRepDecision();
            }
        }
        return fundingDecision;
    }

    public EformsDecisionHistory buildEformDecisionHistory(ApplicationTrackingOutputResult applicationTrackingOutputResult, String fundingDecision) {
        Ioj ioj = applicationTrackingOutputResult.getIoj();
        MeansAssessment meansAssessment = applicationTrackingOutputResult.getMeansAssessment();
        Passport passport = applicationTrackingOutputResult.getPassport();
        return EformsDecisionHistory.builder()
                .usn(applicationTrackingOutputResult.getUsn())
                .repId(applicationTrackingOutputResult.getMaatRef())
                .caseId(applicationTrackingOutputResult.getCaseId())
                .dateAppCreated(LocalDate.from(ioj.getAppCreatedDate()))
                .iojResult(ioj.getIojResult())
                .iojAssessorName(ioj.getIojAssessorName())
                .iojReason(ioj.getIojReason())
                .meansResult(String.valueOf(meansAssessment.getMeansAssessmentResult()))
                .meansAssessorName(meansAssessment.getMeansAssessorName())
                .dateMeansCreated(LocalDate.from(meansAssessment.getMeansAssessmentCreatedDate()))
                .fundingDecision(fundingDecision)
                .passportResult(String.valueOf(passport.getPassportResult()))
                .passportAssessorName(passport.getPassportAssessorName())
                .datePassportCreated(LocalDate.from(passport.getPassportCreatedDate()))
                .dwpResult(applicationTrackingOutputResult.getDwpResult())
                .iojAppealResult(String.valueOf(ioj.getIojAppealResult()))
                .hardshipResult(String.valueOf(applicationTrackingOutputResult.getHardship().getHardshipResult()))
                .caseType(String.valueOf(applicationTrackingOutputResult.getCaseType()))
                .magsOutcome(applicationTrackingOutputResult.getMagsOutcome())
                .repDecision(applicationTrackingOutputResult.getRepDecision())
                .ccRepDecision(applicationTrackingOutputResult.getCcRepDecision())
                .assessmentId(applicationTrackingOutputResult.getAssessmentId())
                .assessmentType(String.valueOf(applicationTrackingOutputResult.getAssessmentType()))
                .wroteToResults(WROTE_TO_RESULTS).build();
    }

    public EformResults buildEformResult(ApplicationTrackingOutputResult applicationTrackingOutputResult, String fundingDecision) {
        Ioj ioj = applicationTrackingOutputResult.getIoj();
        MeansAssessment meansAssessment = applicationTrackingOutputResult.getMeansAssessment();
        Passport passport = applicationTrackingOutputResult.getPassport();
        return EformResults.builder()
                .usn(applicationTrackingOutputResult.getUsn())
                .maatRef(applicationTrackingOutputResult.getMaatRef())
                .caseId(applicationTrackingOutputResult.getCaseId())
                .dateCreated(ioj.getAppCreatedDate())
                .iojResult(ioj.getIojResult())
                .iojAssessorName(ioj.getIojAssessorName())
                .meansResult(String.valueOf(meansAssessment.getMeansAssessmentResult()))
                .meansAssessorName(meansAssessment.getMeansAssessorName())
                .dateMeansCreated(meansAssessment.getMeansAssessmentCreatedDate())
                .fundingDecision(fundingDecision)
                .iojReason(ioj.getIojReason())
                .passportResult(String.valueOf(passport.getPassportResult()))
                .passportAssesorName(passport.getPassportAssessorName())
                .datePassportCreated(passport.getPassportCreatedDate())
                .dwpResult(applicationTrackingOutputResult.getDwpResult())
                .iojAppealResult(String.valueOf(ioj.getIojAppealResult()))
                .caseType(String.valueOf(applicationTrackingOutputResult.getCaseType()))
                .stage(null).build();
    }
}
