package uk.gov.justice.laa.crime.application.tracking.integration;

import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import uk.gov.justice.laa.crime.application.tracking.testutils.FileUtils;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class MockWebServerStubs {

  private static final Map<String, RequestPathResponseMapping> PATH_TO_RESPONSE_MAPPING_MAP =
      Arrays.stream(RequestPathResponseMapping.values())
          .collect(
              Collectors.toMap(
                  RequestPathResponseMapping::getRequestPath,
                  requestPathResponseMapping -> requestPathResponseMapping));

  public static Dispatcher forDownstreamApiCalls() {
    return new Dispatcher() {
      @NotNull @Override
      public MockResponse dispatch(@NotNull RecordedRequest recordedRequest) {
        String requestPath = recordedRequest.getPath();
        RequestPathResponseMapping responseMapping = PATH_TO_RESPONSE_MAPPING_MAP.get(requestPath);
        return responseMapping.getMockResponse();
      }
    };
  }

  enum RequestPathResponseMapping {
    OAUTH2(
            "/oauth2/token",
            "testdata/OAuth2Response.json",
            HttpStatus.OK
    ),
    EFORM_AUDIT(
            "/eform/audit",
            null,
            HttpStatus.OK
    ),
    EFORM(
            "/eform/7264893",
            null,
            HttpStatus.OK
    ),
    EFORM_HISTORY(
            "/eform/history",
            null,
            HttpStatus.OK
    ),
    EFORM_DECISION_HISTORY(
"/eform/decision-history",
            null,
            HttpStatus.OK
    ),
    EFORM_PREVIOUS_DECISION_HOSTORY(
            "/eform/decision-history/7264893/previous-wrote-to-result",
            null,
            HttpStatus.OK
    ),
    EFORM_DECISION_HISTORY_USN(
            "/eform/decision-history/7264893",
            null,
            HttpStatus.OK
    ),
    EFORM_RESULTS(
            "/eform/results",
            null,
            HttpStatus.OK
    ),
    INTERNAL_FINANCIAL_CHECK_OUTSTANDING(
            "/internal/v1/assessment/financial-assessments/check-outstanding/73856111",
            "testdata/OutstandingAssessment_default.json",
            HttpStatus.OK
    ),
    INTERNAL_REP_ORDER_IOJ_ASSESOR_DETAILS(
            "/internal/v1/assessment/rep-orders/73856111/ioj-assessor-details",
            null,
            HttpStatus.OK
    ),
    INTERNAL_FINANCIAL_MEANS_ASSESOR_DETAILS(
            "/internal/v1/assessment/financial-assessments/85478545/means-assessor-details",
            null,
            HttpStatus.OK
    ),
    INTERNAL_PASSPORT_ASSESOR_DETAILS(
            "/internal/v1/assessment/passport-assessments/98658658/passport-assessor-details",
            null,
            HttpStatus.OK
    );

    private final String requestPath;
    private final String responseBodyFilePath;
    private final HttpStatus httpResponseStatus;

    RequestPathResponseMapping(
        String requestPath, String responseBodyFilePath, HttpStatus httpResponseStatus) {
      this.requestPath = requestPath;
      this.responseBodyFilePath = responseBodyFilePath;
      this.httpResponseStatus = httpResponseStatus;
    }

    String getRequestPath() {
      return requestPath;
    }

    MockResponse getMockResponse() {
      MockResponse mockResponse =
          new MockResponse()
              .addHeader("Content-Type", MediaType.APPLICATION_JSON)
              .setResponseCode(httpResponseStatus.value());

      if (StringUtils.isNotBlank(responseBodyFilePath)) {
        mockResponse.setBody(FileUtils.readFileToString(responseBodyFilePath));
      }
      return mockResponse;
    }
  }
}
