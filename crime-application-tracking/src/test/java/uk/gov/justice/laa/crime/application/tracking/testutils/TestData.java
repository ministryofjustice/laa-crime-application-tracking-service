package uk.gov.justice.laa.crime.application.tracking.testutils;

import uk.gov.justice.laa.crime.application.tracking.model.ApplicationTrackingOutputResult;

public class TestData {

    private TestData() {
    }

    public static ApplicationTrackingOutputResult getAtsRequest() {
        String atsJson = FileUtils.readFileToString("testdata/ApplicationTrackingOutputResult_default.json");
        return JsonUtils.jsonToObject(atsJson, ApplicationTrackingOutputResult.class);
    }
}
