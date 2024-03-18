package uk.gov.justice.laa.crime.application.tracking.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import uk.gov.justice.laa.crime.application.tracking.model.ApplicationTrackingOutputResult;
import uk.gov.justice.laa.crime.application.tracking.testutils.TestData;

import java.util.stream.Stream;

class FundingDecisionUtilTest {

    private static final String GRANTED_PASSED_MEANS_TEST = "Granted - Passed Means Test";
    private static final String GRANTED = "Granted";

    @ParameterizedTest
    @MethodSource("testDataForFundingDecision")
    void shouldReturnFundingDecisionForIndictableCaseType(ApplicationTrackingOutputResult.CaseType caseType, String expectedFundDecision){
        var atsRequest = TestData.getAtsRequest();
        atsRequest.setCaseType(caseType);
        String actualFundingDecision = FundingDecisionUtil.getFundingDecision(atsRequest);
        Assertions.assertEquals(expectedFundDecision, actualFundingDecision);

    }
    private static Stream<Arguments> testDataForFundingDecision() {
        return Stream.of(
                Arguments.of(ApplicationTrackingOutputResult.CaseType.INDICTABLE, GRANTED_PASSED_MEANS_TEST),
                Arguments.of(ApplicationTrackingOutputResult.CaseType.COMMITAL, GRANTED),
                Arguments.of(ApplicationTrackingOutputResult.CaseType.SUMMARY_ONLY, GRANTED)
        );
    }
}
