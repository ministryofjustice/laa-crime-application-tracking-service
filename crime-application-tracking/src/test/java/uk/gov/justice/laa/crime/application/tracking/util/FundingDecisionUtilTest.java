package uk.gov.justice.laa.crime.application.tracking.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import uk.gov.justice.laa.crime.application.tracking.model.ApplicationTrackingOutputResult;
import uk.gov.justice.laa.crime.application.tracking.testutils.TestData;

class FundingDecisionUtilTest {

    @Test
    void shouldReturnFundingDecisionForIndictableCaseType(){
        var atsRequest = TestData.getAtsRequest();
        String expectedFundDecision = "Granted - Passed Means Test";
        String fundingDecision = FundingDecisionUtil.getFundingDecision(atsRequest);
        Assertions.assertEquals(expectedFundDecision, fundingDecision);
    }
    @Test
    void shouldReturnFundingDecisionForCommitalCaseType(){
        var atsRequest = TestData.getAtsRequest();
        atsRequest.setCaseType(ApplicationTrackingOutputResult.CaseType.COMMITAL);
        String expectedFundDecision = "Granted";
        String fundingDecision = FundingDecisionUtil.getFundingDecision(atsRequest);
        Assertions.assertEquals(expectedFundDecision, fundingDecision);
    }

    @Test
    void shouldReturnFundingDecisionForSummaryOnlyCaseType(){
        var atsRequest = TestData.getAtsRequest();
        atsRequest.setCaseType(ApplicationTrackingOutputResult.CaseType.SUMMARY_ONLY);
        String expectedFundDecision = "Granted";
        String fundingDecision = FundingDecisionUtil.getFundingDecision(atsRequest);
        Assertions.assertEquals(expectedFundDecision, fundingDecision);
    }
}
