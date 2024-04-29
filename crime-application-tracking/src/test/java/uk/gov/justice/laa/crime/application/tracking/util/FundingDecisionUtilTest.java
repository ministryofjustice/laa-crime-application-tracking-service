package uk.gov.justice.laa.crime.application.tracking.util;

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

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FundingDecisionUtilTest {

    private static final String GRANTED_PASSED_MEANS_TEST = "Granted - Passed Means Test";
    private static final String GRANTED = "Granted";
    private static final Object REFUSED_INELIGIBLE = "Refused - Ineligible";
    private static final String FAILED_CF_S_FAILED_MEANS_TEST = "Failed - CfS Failed Means Test";


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
        if(ccRepDecision != null) {
            atsRequest.setCcRepDecision(ccRepDecision);
        }

        String actualFundingDecision = FundingDecisionUtil.getFundingDecision(atsRequest);

        assertEquals(expectedFundDecision, actualFundingDecision);
    }

    private static Stream<Arguments> testDataForFundingDecision() {
        return Stream.of(
                // Test cases for different AssessmentTypes and CaseTypes
                Arguments.of(
                        AssessmentType.MEANS_FULL,
                        PassportResult.FAIL,
                        ApplicationTrackingOutputResult.CaseType.INDICTABLE,
                        MeansAssessmentResult.FULL,
                        null,
                        GRANTED_PASSED_MEANS_TEST),
                Arguments.of(
                        AssessmentType.MEANS_FULL,
                        PassportResult.FAIL,
                        ApplicationTrackingOutputResult.CaseType.COMMITAL,
                        MeansAssessmentResult.FULL,
                        null,
                        GRANTED),
                Arguments.of(
                        AssessmentType.MEANS_FULL,
                        PassportResult.FAIL,
                        ApplicationTrackingOutputResult.CaseType.SUMMARY_ONLY,
                        MeansAssessmentResult.FULL,
                        null,
                        GRANTED),
                // Additional test cases for different combinations
                Arguments.of(
                        AssessmentType.MEANS_FULL,
                        PassportResult.PASS,
                        ApplicationTrackingOutputResult.CaseType.INDICTABLE,
                        MeansAssessmentResult.FULL,
                        null,
                        GRANTED_PASSED_MEANS_TEST),
                Arguments.of(
                        AssessmentType.MEANS_FULL,
                        PassportResult.PASS,
                        ApplicationTrackingOutputResult.CaseType.COMMITAL,
                        MeansAssessmentResult.FULL,
                        null,
                        GRANTED),
                Arguments.of(
                        AssessmentType.MEANS_FULL,
                        PassportResult.PASS,
                        ApplicationTrackingOutputResult.CaseType.SUMMARY_ONLY,
                        MeansAssessmentResult.FULL,
                        "CcRepDecision",
                        GRANTED),
                // Test cases for different MeansAssessmentResults
                Arguments.of(
                        AssessmentType.MEANS_FULL,
                        PassportResult.FAIL,
                        ApplicationTrackingOutputResult.CaseType.INDICTABLE,
                        MeansAssessmentResult.INEL,
                        "CcRepDecision",
                        REFUSED_INELIGIBLE),
                Arguments.of(
                        AssessmentType.MEANS_FULL,
                        PassportResult.FAIL,
                        ApplicationTrackingOutputResult.CaseType.COMMITAL,
                        MeansAssessmentResult.INEL,
                        "Failed - CfS Failed Means Test",
                        FAILED_CF_S_FAILED_MEANS_TEST),
                Arguments.of(
                        AssessmentType.MEANS_FULL,
                        PassportResult.FAIL,
                        ApplicationTrackingOutputResult.CaseType.SUMMARY_ONLY,
                        MeansAssessmentResult.INEL,
                        "CcRepDecision",
                        REFUSED_INELIGIBLE),
                // Test cases for different ccRepDecision values
                Arguments.of(
                        AssessmentType.MEANS_FULL,
                        PassportResult.FAIL,
                        ApplicationTrackingOutputResult.CaseType.INDICTABLE,
                        MeansAssessmentResult.FULL,
                        "Granted - Passed Means Test",
                        GRANTED_PASSED_MEANS_TEST),
                Arguments.of(
                        AssessmentType.MEANS_FULL,
                        PassportResult.FAIL,
                        ApplicationTrackingOutputResult.CaseType.COMMITAL,
                        MeansAssessmentResult.FULL,
                        "Granted - Passed Means Test",
                        GRANTED),
                Arguments.of(
                        AssessmentType.MEANS_FULL,
                        PassportResult.FAIL,
                        ApplicationTrackingOutputResult.CaseType.SUMMARY_ONLY,
                        MeansAssessmentResult.FULL,
                        "Granted - Passed Means Test",
                        GRANTED)
                // Additional test cases to cover more scenarios
                // ...
        );
    }
}
