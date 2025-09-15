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
import java.util.Objects;
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
            @NotNull
            @Override
            public MockResponse dispatch(@NotNull RecordedRequest recordedRequest) {
                String requestPath = recordedRequest.getPath();
                RequestPathResponseMapping responseMapping = PATH_TO_RESPONSE_MAPPING_MAP.get(requestPath);
                if (Objects.isNull(responseMapping)) {
                    return createNotFoundMockResponse(requestPath);
                }
                return responseMapping.getMockResponse();
            }
        };
    }

    private static MockResponse createNotFoundMockResponse(String requestPath) {
        String message = "Unable to find a request-response mapping for the path [%s]".formatted(requestPath);
        return new MockResponse()
                .addHeader("Content-Type", MediaType.TEXT_PLAIN_VALUE).
                setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .setBody(message);
    }

    enum RequestPathResponseMapping {
        OAUTH2(
                "/oauth2/token",
                "testdata/OAuth2Response.json",
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
