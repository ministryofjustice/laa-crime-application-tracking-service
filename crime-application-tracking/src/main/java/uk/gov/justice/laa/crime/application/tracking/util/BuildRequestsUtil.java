package uk.gov.justice.laa.crime.application.tracking.util;

import lombok.experimental.UtilityClass;
import uk.gov.justice.laa.crime.application.tracking.model.*;

import java.time.LocalDate;
import java.util.Objects;

@UtilityClass
public class BuildRequestsUtil {
    public static final String WROTE_TO_RESULTS = "N";

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
                .assessmentType(applicationTrackingOutputResult.getAssessmentType().value())
                .wroteToResults(WROTE_TO_RESULTS);
        if (Objects.nonNull(ioj)) {
            eformsDecisionHistoryBuilder
                    .dateAppCreated(Objects.nonNull(ioj.getAppCreatedDate()) ? LocalDate.from(ioj.getAppCreatedDate()) : null)
                    .iojResult(ioj.getIojResult())
                    .iojAssessorName(ioj.getIojAssessorName())
                    .iojReason(ioj.getIojReason())
                    .iojAppealResult(ioj.getIojAppealResult().value());
        }
        if (Objects.nonNull(meansAssessment)) {
            eformsDecisionHistoryBuilder
                    .meansResult(meansAssessment.getMeansAssessmentResult().value())
                    .meansAssessorName(meansAssessment.getMeansAssessorName())
                    .dateMeansCreated(Objects.nonNull(meansAssessment.getMeansAssessmentCreatedDate()) ? LocalDate.from(meansAssessment.getMeansAssessmentCreatedDate()): null);
        }
        if (Objects.nonNull(passport)) {
            eformsDecisionHistoryBuilder
                    .passportResult(passport.getPassportResult().value())
                    .passportAssessorName(passport.getPassportAssessorName())
                    .datePassportCreated(Objects.nonNull(passport.getPassportCreatedDate()) ? LocalDate.from(passport.getPassportCreatedDate()) : null);
        }

        if (Objects.nonNull(hardship)) {
            eformsDecisionHistoryBuilder
                    .hardshipResult(hardship.getHardshipResult().value());
        }
        return eformsDecisionHistoryBuilder.build();
    }


    public EformResults buildEformResult(ApplicationTrackingOutputResult applicationTrackingOutputResult, String fundingDecision) {
        Ioj ioj = applicationTrackingOutputResult.getIoj();
        MeansAssessment meansAssessment = applicationTrackingOutputResult.getMeansAssessment();
        Passport passport = applicationTrackingOutputResult.getPassport();
        boolean nonNullMeansAssessment = Objects.nonNull(meansAssessment);
        boolean nonNullIoj = Objects.nonNull(ioj);
        boolean nonNullPassport = Objects.nonNull(passport);

        return EformResults.builder()
                .usn(applicationTrackingOutputResult.getUsn())
                .maatRef(applicationTrackingOutputResult.getMaatRef())
                .caseId(applicationTrackingOutputResult.getCaseId())
                .dateCreated(nonNullIoj ? ioj.getAppCreatedDate() : null)
                .iojResult(nonNullIoj ? ioj.getIojResult() : null)
                .iojAssessorName(nonNullIoj ? ioj.getIojAssessorName() : null)
                .meansResult(nonNullMeansAssessment ? meansAssessment.getMeansAssessmentResult().value() : null)
                .meansAssessorName(nonNullMeansAssessment ? meansAssessment.getMeansAssessorName() : null)
                .dateMeansCreated(nonNullMeansAssessment ? meansAssessment.getMeansAssessmentCreatedDate() : null)
                .fundingDecision(fundingDecision)
                .iojReason(nonNullIoj ? ioj.getIojReason() : null)
                .passportResult(nonNullPassport ? passport.getPassportResult().value() : null )
                .passportAssesorName(nonNullPassport ? passport.getPassportAssessorName() : null)
                .datePassportCreated(nonNullPassport ? passport.getPassportCreatedDate() : null)
                .dwpResult(applicationTrackingOutputResult.getDwpResult())
                .iojAppealResult(nonNullIoj ? ioj.getIojAppealResult().value() : null)
                .caseType(applicationTrackingOutputResult.getCaseType().value())
                .stage(null).build();
    }
}
