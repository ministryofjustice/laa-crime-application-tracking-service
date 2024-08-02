package uk.gov.justice.laa.crime.application.tracking.util;

import lombok.experimental.UtilityClass;
import uk.gov.justice.laa.crime.application.tracking.model.*;

import java.time.LocalDate;
import java.util.Objects;

@UtilityClass
public class BuildRequestsUtil {
    private static final String WROTE_TO_RESULTS = "N";

    public EformsDecisionHistory buildEformDecisionHistory(ApplicationTrackingOutputResult applicationTrackingOutputResult, String fundingDecision) {
        Ioj ioj = applicationTrackingOutputResult.getIoj();
        MeansAssessment meansAssessment = applicationTrackingOutputResult.getMeansAssessment();
        Passport passport = applicationTrackingOutputResult.getPassport();
        Hardship hardship = applicationTrackingOutputResult.getHardship();
        EformsDecisionHistory.EformsDecisionHistoryBuilder eformsDecisionHistoryBuilder = EformsDecisionHistory.builder();
        eformsDecisionHistoryBuilder
                .usn(applicationTrackingOutputResult.getUsn())
                .repId(applicationTrackingOutputResult.getMaatRef())
                .caseId(applicationTrackingOutputResult.getCaseId())
                .fundingDecision(fundingDecision)
                .dwpResult(applicationTrackingOutputResult.getDwpResult())
                .caseType(String.valueOf(applicationTrackingOutputResult.getCaseType()))
                .magsOutcome(applicationTrackingOutputResult.getMagsOutcome())
                .repDecision(applicationTrackingOutputResult.getRepDecision())
                .ccRepDecision(applicationTrackingOutputResult.getCcRepDecision())
                .assessmentId(applicationTrackingOutputResult.getAssessmentId())
                .assessmentType(Objects.nonNull(applicationTrackingOutputResult.getAssessmentType()) ? applicationTrackingOutputResult.getAssessmentType().value() : null)
                .wroteToResults(WROTE_TO_RESULTS);
        buildIoj(ioj, eformsDecisionHistoryBuilder);
        buildMeansAssessment(meansAssessment, eformsDecisionHistoryBuilder);
        buildPassport(passport, eformsDecisionHistoryBuilder);

        if (Objects.nonNull(hardship)) {
            eformsDecisionHistoryBuilder
                    .hardshipResult(hardship.getHardshipResult().value());
        }
        return eformsDecisionHistoryBuilder.build();
    }

    private void buildPassport(Passport passport, EformsDecisionHistory.EformsDecisionHistoryBuilder eformsDecisionHistoryBuilder) {
        if (Objects.nonNull(passport)) {
            eformsDecisionHistoryBuilder
                    .passportResult(passport.getPassportResult().value())
                    .passportAssessorName(passport.getPassportAssessorName())
                    .datePassportCreated(Objects.nonNull(passport.getPassportCreatedDate()) ? LocalDate.from(passport.getPassportCreatedDate()) : null);
        }
    }

    private void buildMeansAssessment(MeansAssessment meansAssessment, EformsDecisionHistory.EformsDecisionHistoryBuilder eformsDecisionHistoryBuilder) {
        if (Objects.nonNull(meansAssessment)) {
            eformsDecisionHistoryBuilder
                    .meansResult(meansAssessment.getMeansAssessmentResult().value())
                    .meansAssessorName(meansAssessment.getMeansAssessorName())
                    .dateMeansCreated(Objects.nonNull(meansAssessment.getMeansAssessmentCreatedDate()) ? LocalDate.from(meansAssessment.getMeansAssessmentCreatedDate()) : null);
        }
    }

    private void buildIoj(Ioj ioj, EformsDecisionHistory.EformsDecisionHistoryBuilder eformsDecisionHistoryBuilder) {
        if (Objects.nonNull(ioj)) {
            eformsDecisionHistoryBuilder
                    .dateAppCreated(Objects.nonNull(ioj.getAppCreatedDate()) ? LocalDate.from(ioj.getAppCreatedDate()) : null)
                    .iojResult(ioj.getIojResult())
                    .iojAssessorName(ioj.getIojAssessorName())
                    .iojReason(ioj.getIojReason())
                    .iojAppealResult(ioj.getIojAppealResult().value());
        }
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
                .meansResult(Objects.nonNull(meansAssessment.getMeansAssessmentResult()) ? meansAssessment.getMeansAssessmentResult().value() : null)
                .meansAssessorName(meansAssessment.getMeansAssessorName())
                .dateMeansCreated( meansAssessment.getMeansAssessmentCreatedDate())
                .fundingDecision(fundingDecision)
                .iojReason(ioj.getIojReason())
                .passportResult(Objects.nonNull(passport.getPassportResult()) ? passport.getPassportResult().value() : null)
                .passportAssesorName(passport.getPassportAssessorName())
                .datePassportCreated(passport.getPassportCreatedDate())
                .dwpResult(applicationTrackingOutputResult.getDwpResult())
                .iojAppealResult(Objects.nonNull(ioj.getIojAppealResult()) ? ioj.getIojAppealResult().value() : null)
                .caseType(applicationTrackingOutputResult.getCaseType().value())
                .stage(null).build();
    }
}
