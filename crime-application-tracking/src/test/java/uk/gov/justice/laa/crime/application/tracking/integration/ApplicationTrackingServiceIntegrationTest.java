package uk.gov.justice.laa.crime.application.tracking.integration;

import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import uk.gov.justice.laa.crime.application.tracking.CrimeApplicationTrackingApplication;
import uk.gov.justice.laa.crime.application.tracking.model.ApplicationTrackingOutputResult;
import uk.gov.justice.laa.crime.application.tracking.model.ApplicationTrackingOutputResult.RequestSource;
import uk.gov.justice.laa.crime.application.tracking.testutils.FileUtils;
import uk.gov.justice.laa.crime.application.tracking.testutils.JsonUtils;

import java.io.IOException;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = CrimeApplicationTrackingApplication.class, webEnvironment = DEFINED_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ApplicationTrackingServiceIntegrationTest {
    private MockMvc mvc;

    private static MockWebServer mockWebServer;

    private ApplicationTrackingOutputResult applicationTrackingOutputResultJson;

    @Autowired private WebApplicationContext webApplicationContext;

    @BeforeAll
    public void initialiseMockWebServer() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.setDispatcher(MockWebServerStubs.forDownstreamApiCalls());
        mockWebServer.start(9999);
    }

    @AfterAll
    protected void shutdownMockWebServer() throws IOException {
        mockWebServer.shutdown();
    }

    @BeforeEach
    public void setup() {
        String content = FileUtils.readFileToString("testdata/ApplicationTrackingOutputResult_default.json");
        applicationTrackingOutputResultJson = JsonUtils.jsonToObject(content, ApplicationTrackingOutputResult.class);

        this.mvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    void givenCreateApplicationRequest_shouldCreateAudit_andDoRequiredUpdates() throws Exception {
        String content = createApplicationTrackingOutputResult(RequestSource.CREATE_APPLICATION);
        RequestBuilder request =
                MockMvcRequestBuilders.post(
                                "/api/internal/v1/application-tracking-output-result")
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON);

                mvc.perform(request)
                        .andExpect(status().isOk())
                        .andReturn();

    }

    @Test
    void givenCreateApplicationRequest_shouldNotCreateAudit_andThrowError() throws Exception {
        String content = createApplicationTrackingOutputResult(RequestSource.CREATE_APPLICATION, 123456);
        RequestBuilder request =
                MockMvcRequestBuilders.post(
                                "/api/internal/v1/application-tracking-output-result")
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                        .andExpect(status().is4xxClientError())
                        .andReturn();

    }

    @Test
    void givenCreateApplicationRequest_shouldProcessHardship_andDoRequiredUpdates() throws Exception {
        String content = createApplicationTrackingOutputResult(RequestSource.HARDSHIP);
        RequestBuilder request =
                MockMvcRequestBuilders.post(
                                "/api/internal/v1/application-tracking-output-result")
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                        .andExpect(status().isOk())
                        .andReturn();

    }

    @Test
    void givenCreateApplicationRequest_shouldNotProcessHardship_andThrowError() throws Exception {
        String content = createApplicationTrackingOutputResult(RequestSource.HARDSHIP, 123456);
        RequestBuilder request =
                MockMvcRequestBuilders.post(
                                "/api/internal/v1/application-tracking-output-result")
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                        .andExpect(status().is4xxClientError())
                        .andReturn();

    }

    @Test
    void givenCreateApplicationRequest_shouldProcessCrownCourt_andDoRequiredUpdates() throws Exception {
        String content = createApplicationTrackingOutputResult(RequestSource.CROWN_COURT);
        RequestBuilder request =
                MockMvcRequestBuilders.post(
                                "/api/internal/v1/application-tracking-output-result")
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                        .andExpect(status().isOk())
                        .andReturn();

    }

    @Test
    void givenCreateApplicationRequest_shouldNotProcessCrownCourt_andThrowError() throws Exception {
        String content = createApplicationTrackingOutputResult(RequestSource.CROWN_COURT, 123456);
        RequestBuilder request =
                MockMvcRequestBuilders.post(
                                "/api/internal/v1/application-tracking-output-result")
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                        .andExpect(status().is4xxClientError())
                        .andReturn();

    }

    @Test
    void givenCreateApplicationRequest_shouldUpdateCapitalAndEquity_andDoRequiredUpdates() throws Exception {
        String content = createApplicationTrackingOutputResult(RequestSource.CAPITAL_AND_EQUITY);
        RequestBuilder request =
                MockMvcRequestBuilders.post(
                                "/api/internal/v1/application-tracking-output-result")
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                        .andExpect(status().isOk())
                        .andReturn();

    }

    @Test
    void givenCreateApplicationRequest_shouldNotUpdateCapitalAndEquity_andThrowError() throws Exception {
        String content = createApplicationTrackingOutputResult(RequestSource.CAPITAL_AND_EQUITY, 12345);
        RequestBuilder request =
                MockMvcRequestBuilders.post(
                                "/api/internal/v1/application-tracking-output-result")
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                        .andExpect(status().is4xxClientError())
                        .andReturn();

    }

    @Test
    void givenCreateApplicationRequest_shouldProcessPassportIOJ_andDoRequiredUpdates() throws Exception {
        String content = createApplicationTrackingOutputResult(RequestSource.PASSPORT_IOJ);
        RequestBuilder request =
                MockMvcRequestBuilders.post(
                                "/api/internal/v1/application-tracking-output-result")
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                        .andExpect(status().isOk())
                        .andReturn();

    }

    @Test
    void givenCreateApplicationRequest_shouldNotProcessPassportIOJ_andThrowError() throws Exception {
        String content = createApplicationTrackingOutputResult(RequestSource.PASSPORT_IOJ, 12345);
        RequestBuilder request =
                MockMvcRequestBuilders.post(
                                "/api/internal/v1/application-tracking-output-result")
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                        .andExpect(status().is4xxClientError())
                        .andReturn();

    }

    @Test
    void givenCreateApplicationRequest_shouldProcessMeansAssessment_andDoRequiredUpdates() throws Exception {
        String content = createApplicationTrackingOutputResult(RequestSource.MEANS_ASSESSMENT);
        RequestBuilder request =
                MockMvcRequestBuilders.post(
                                "/api/internal/v1/application-tracking-output-result")
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                        .andExpect(status().isOk())
                        .andReturn();

    }

    @Test
    void givenCreateApplicationRequestWithUnknownUSN_shouldNotProcessMeansAssessment_andThrowError() throws Exception {
        String content = createApplicationTrackingOutputResult(RequestSource.MEANS_ASSESSMENT, 40400404);
        RequestBuilder request =
                MockMvcRequestBuilders.post(
                                "/api/internal/v1/application-tracking-output-result")
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                        .andExpect(status().is5xxServerError())
                        .andReturn();
    }

    private String createApplicationTrackingOutputResult(RequestSource requestSource, Integer usn) {
        applicationTrackingOutputResultJson.setRequestSource(requestSource);
        applicationTrackingOutputResultJson.setUsn(usn);
        return JsonUtils.objectToJson(applicationTrackingOutputResultJson);
    }


    private String createApplicationTrackingOutputResult(RequestSource requestSource) {
        applicationTrackingOutputResultJson.setRequestSource(requestSource);
        return JsonUtils.objectToJson(applicationTrackingOutputResultJson);
    }
}
