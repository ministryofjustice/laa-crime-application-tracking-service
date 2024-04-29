package uk.gov.justice.laa.crime.application.tracking.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import uk.gov.justice.laa.crime.application.tracking.model.ApplicationTrackingOutputResult;
import uk.gov.justice.laa.crime.application.tracking.model.ApplicationTrackingOutputResult.AssessmentType;
import uk.gov.justice.laa.crime.application.tracking.model.MeansAssessment;
import uk.gov.justice.laa.crime.application.tracking.model.MeansAssessment.MeansAssessmentResult;
import uk.gov.justice.laa.crime.application.tracking.model.Passport;
import uk.gov.justice.laa.crime.application.tracking.model.Passport.PassportResult;
import uk.gov.justice.laa.crime.application.tracking.testutils.TestData;

class FundingDecisionUtilTest {

    private static final String GRANTED_PASSED_MEANS_TEST = "Granted - Passed Means Test";
    private static final String GRANTED = "Granted";

    @ParameterizedTest
    @MethodSource("testDataForFundingDecision")
    void shouldReturnFundingDecisionForIndictableCaseType(
        ApplicationTrackingOutputResult.AssessmentType assessmentType,
        Passport.PassportResult passportResult,
        ApplicationTrackingOutputResult.CaseType caseType,
        MeansAssessment.MeansAssessmentResult meansAssessmentResult,
        String ccRepDecision,
        String expectedFundDecision) {


        var atsRequest = TestData.getAtsRequest();
        atsRequest.setCaseType(caseType);
        atsRequest.setAssessmentType(assessmentType);
        atsRequest.getPassport().setPassportResult(passportResult);
        atsRequest.getMeansAssessment().setMeansAssessmentResult(meansAssessmentResult);
        atsRequest.setCcRepDecision(ccRepDecision);

        String actualFundingDecision = FundingDecisionUtil.getFundingDecision(atsRequest);

        assertEquals(expectedFundDecision, actualFundingDecision);
    }
    private static Stream<Arguments> testDataForFundingDecision() {
        return Stream.of(
            Arguments.of(
                AssessmentType.MEANS_FULL,
                PassportResult.FAIL,
                ApplicationTrackingOutputResult.CaseType.INDICTABLE,
                MeansAssessmentResult.FULL,
                "CcRepDecision",
                GRANTED_PASSED_MEANS_TEST),
            Arguments.of(
                AssessmentType.MEANS_FULL,
                PassportResult.FAIL,
                ApplicationTrackingOutputResult.CaseType.COMMITAL,
                MeansAssessmentResult.FULL,
                "CcRepDecision",
                GRANTED),
            Arguments.of(AssessmentType.MEANS_FULL,
                PassportResult.FAIL,
                ApplicationTrackingOutputResult.CaseType.SUMMARY_ONLY,
                MeansAssessmentResult.FULL,
                "CcRepDecision",
                GRANTED)
        );
    }
}
