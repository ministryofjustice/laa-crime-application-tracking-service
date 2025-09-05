package uk.gov.justice.laa.crime.application.tracking.util;

import java.time.LocalDateTime;
import lombok.experimental.UtilityClass;
import uk.gov.justice.laa.crime.application.tracking.entity.DecisionHistory;
import uk.gov.justice.laa.crime.application.tracking.entity.Result;
import uk.gov.justice.laa.crime.application.tracking.model.*;

import java.util.Objects;

@UtilityClass
public class BuildRequestsUtil {
    private static final String WROTE_TO_RESULTS = "N";

    public DecisionHistory buildDecisionHistory(ApplicationTrackingOutputResult applicationTrackingOutputResult, String fundingDecision) {
        Ioj ioj = applicationTrackingOutputResult.getIoj();
        MeansAssessment meansAssessment = applicationTrackingOutputResult.getMeansAssessment();
        Passport passport = applicationTrackingOutputResult.getPassport();
        Hardship hardship = applicationTrackingOutputResult.getHardship();
        DecisionHistory.DecisionHistoryBuilder decisionHistoryBuilder = DecisionHistory.builder();
        decisionHistoryBuilder
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
        buildIoj(ioj, decisionHistoryBuilder);
        buildMeansAssessment(meansAssessment, decisionHistoryBuilder);
        buildPassport(passport, decisionHistoryBuilder);

        if (Objects.nonNull(hardship)) {
            decisionHistoryBuilder
                    .hardshipResult(Objects.nonNull(hardship.getHardshipResult()) ? hardship.getHardshipResult().value() : null);
        }
        return decisionHistoryBuilder.build();
    }

    private void buildPassport(Passport passport, DecisionHistory.DecisionHistoryBuilder decisionHistoryBuilder) {
        if (Objects.nonNull(passport)) {
            decisionHistoryBuilder
                    .passportResult(Objects.nonNull(passport.getPassportResult()) ? passport.getPassportResult().value() : null)
                    .passportAssessorName(passport.getPassportAssessorName())
                    .datePassportCreated(Objects.nonNull(passport.getPassportCreatedDate()) ? LocalDateTime.from(passport.getPassportCreatedDate()) : null);
        }
    }

    private void buildMeansAssessment(MeansAssessment meansAssessment, DecisionHistory.DecisionHistoryBuilder decisionHistoryBuilder) {
        if (Objects.nonNull(meansAssessment)) {
            decisionHistoryBuilder
                    .meansResult(Objects.nonNull(meansAssessment.getMeansAssessmentResult()) ? meansAssessment.getMeansAssessmentResult().value() : null)
                    .meansAssessorName(meansAssessment.getMeansAssessorName())
                    .dateMeansCreated(Objects.nonNull(meansAssessment.getMeansAssessmentCreatedDate()) ? LocalDateTime.from(meansAssessment.getMeansAssessmentCreatedDate()) : null);
        }
    }

    private void buildIoj(Ioj ioj, DecisionHistory.DecisionHistoryBuilder decisionHistoryBuilder) {
        if (Objects.nonNull(ioj)) {
            decisionHistoryBuilder
                    .dateAppCreated(Objects.nonNull(ioj.getAppCreatedDate()) ? LocalDateTime.from(ioj.getAppCreatedDate()) : null)
                    .iojResult(ioj.getIojResult())
                    .iojAssessorName(ioj.getIojAssessorName())
                    .iojReason(ioj.getIojReason())
                    .iojAppealResult(Objects.nonNull(ioj.getIojAppealResult()) ? ioj.getIojAppealResult().value() : null);
        }
    }


    public Result buildResult(ApplicationTrackingOutputResult applicationTrackingOutputResult, String fundingDecision) {
        Ioj ioj = applicationTrackingOutputResult.getIoj();
        MeansAssessment meansAssessment = applicationTrackingOutputResult.getMeansAssessment();
        Passport passport = applicationTrackingOutputResult.getPassport();
        return Result.builder()
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
                .caseType(Objects.nonNull(applicationTrackingOutputResult.getCaseType()) ? applicationTrackingOutputResult.getCaseType().value() : null)
                .stage(null).build();
    }
}
