package uk.gov.justice.laa.crime.application.tracking.integration;

import java.time.LocalDateTime;
import java.util.Optional;
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
import uk.gov.justice.laa.crime.application.tracking.entity.Audit;
import uk.gov.justice.laa.crime.application.tracking.entity.DecisionHistory;
import uk.gov.justice.laa.crime.application.tracking.entity.History;
import uk.gov.justice.laa.crime.application.tracking.entity.Result;
import uk.gov.justice.laa.crime.application.tracking.model.ApplicationTrackingOutputResult;
import uk.gov.justice.laa.crime.application.tracking.model.ApplicationTrackingOutputResult.RequestSource;
import uk.gov.justice.laa.crime.application.tracking.repository.AuditRepository;
import uk.gov.justice.laa.crime.application.tracking.repository.DecisionHistoryRepository;
import uk.gov.justice.laa.crime.application.tracking.repository.HistoryRepository;
import uk.gov.justice.laa.crime.application.tracking.repository.ResultRepository;
import uk.gov.justice.laa.crime.application.tracking.testutils.FileUtils;
import uk.gov.justice.laa.crime.application.tracking.testutils.JsonUtils;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = CrimeApplicationTrackingApplication.class, webEnvironment = DEFINED_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ApplicationTrackingServiceIntegrationTest {
    private MockMvc mvc;
    private static MockWebServer mockWebServer;
    private ApplicationTrackingOutputResult applicationTrackingOutputResultJson;
    private static final String WROTE_TO_RESULTS = "Y";
    private static final String STATUS_CODE = "Processing";
    private static final Integer NEW_APPLICATION_USN = 111;
    private static final Integer CROWN_COURT_USN = 222;
    private static final Integer HARDSHIP_USN = 333;
    private static final Integer MEANS_ASSESSMENT_USN = 444;
    private static final Integer PASSPORT_IOJ_USN = 555;
    private static final Integer NOT_FOUND_USN = 12345;

    @Autowired
    private AuditRepository auditRepository;

    @Autowired
    private HistoryRepository historyRepository;

    @Autowired
    private DecisionHistoryRepository decisionHistoryRepository;

    @Autowired
    private ResultRepository resultRepository;

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
        String content = createApplicationTrackingOutputResult(RequestSource.CREATE_APPLICATION, NEW_APPLICATION_USN);
        RequestBuilder request =
                MockMvcRequestBuilders.post(
                                "/api/internal/v1/application-tracking-output-result")
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON);

                mvc.perform(request)
                        .andExpect(status().isOk())
                        .andReturn();

        Optional<Audit> audit = auditRepository.findByUsn(NEW_APPLICATION_USN);
        assertPopulatesAudit(audit);

        Optional<History> history = historyRepository.findByUsn(NEW_APPLICATION_USN);
        assertPopulatesHistory(history);

        Optional<DecisionHistory> decisionHistory = decisionHistoryRepository.findByUsn(NEW_APPLICATION_USN);
        assertPopulatesDecisionHistory(decisionHistory);

        Optional<Result> result = resultRepository.findByUsn(NEW_APPLICATION_USN);
        assertPopulatesResult(result);

    }

    @Test
    void givenCreateApplicationRequest_shouldNotCreateAudit_andThrowError() throws Exception {
        String content = createApplicationTrackingOutputResult(RequestSource.CREATE_APPLICATION, NOT_FOUND_USN);
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
        String content = createApplicationTrackingOutputResult(RequestSource.HARDSHIP, HARDSHIP_USN);
        RequestBuilder request =
                MockMvcRequestBuilders.post(
                                "/api/internal/v1/application-tracking-output-result")
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                        .andExpect(status().isOk())
                        .andReturn();

        Optional<DecisionHistory> decisionHistory = decisionHistoryRepository.findByUsn(HARDSHIP_USN);
        assertPopulatesDecisionHistory(decisionHistory);

        Optional<Result> result = resultRepository.findByUsn(HARDSHIP_USN);
        assertPopulatesResult(result);

    }

    @Test
    void givenCreateApplicationRequest_shouldNotProcessHardship_andThrowError() throws Exception {
        String content = createApplicationTrackingOutputResult(RequestSource.HARDSHIP, NOT_FOUND_USN);
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
        String content = createApplicationTrackingOutputResult(RequestSource.CROWN_COURT, CROWN_COURT_USN);
        RequestBuilder request =
                MockMvcRequestBuilders.post(
                                "/api/internal/v1/application-tracking-output-result")
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                        .andExpect(status().isOk())
                        .andReturn();

        Optional<DecisionHistory> decisionHistory = decisionHistoryRepository.findByUsn(CROWN_COURT_USN);
        assertPopulatesDecisionHistory(decisionHistory);

        Optional<Result> result = resultRepository.findByUsn(CROWN_COURT_USN);
        assertPopulatesResult(result);

    }

    @Test
    void givenCreateApplicationRequest_shouldNotProcessCrownCourt_andThrowError() throws Exception {
        String content = createApplicationTrackingOutputResult(RequestSource.CROWN_COURT, NOT_FOUND_USN);
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
        String content = createApplicationTrackingOutputResult(RequestSource.CAPITAL_AND_EQUITY, NOT_FOUND_USN);
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
        String content = createApplicationTrackingOutputResult(RequestSource.PASSPORT_IOJ, PASSPORT_IOJ_USN);
        RequestBuilder request =
                MockMvcRequestBuilders.post(
                                "/api/internal/v1/application-tracking-output-result")
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                        .andExpect(status().isOk())
                        .andReturn();

        Optional<History> history = historyRepository.findByUsn(PASSPORT_IOJ_USN);
        assertPopulatesHistory(history);

        Optional<DecisionHistory> decisionHistory = decisionHistoryRepository.findByUsn(PASSPORT_IOJ_USN);
        assertPopulatesDecisionHistory(decisionHistory);

        Optional<Result> result = resultRepository.findByUsn(PASSPORT_IOJ_USN);
        assertPopulatesResult(result);

    }

    @Test
    void givenCreateApplicationRequest_shouldNotProcessPassportIOJ_andThrowError() throws Exception {
        String content = createApplicationTrackingOutputResult(RequestSource.PASSPORT_IOJ, NOT_FOUND_USN);
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
        String content = createApplicationTrackingOutputResult(RequestSource.MEANS_ASSESSMENT, MEANS_ASSESSMENT_USN);
        
        RequestBuilder request =
                MockMvcRequestBuilders.post(
                                "/api/internal/v1/application-tracking-output-result")
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                        .andExpect(status().isOk())
                        .andReturn();

        Optional<History> history = historyRepository.findByUsn(MEANS_ASSESSMENT_USN);
        assertPopulatesHistory(history);

        Optional<DecisionHistory> decisionHistory = decisionHistoryRepository.findByUsn(MEANS_ASSESSMENT_USN);
        assertPopulatesDecisionHistory(decisionHistory);

        Optional<Result> result = resultRepository.findByUsn(MEANS_ASSESSMENT_USN);
        assertPopulatesResult(result);

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

    private void assertPopulatesAudit(Optional<Audit> audit) {
        assertTrue(audit.isPresent());
        assertEquals(applicationTrackingOutputResultJson.getUsn(), audit.get().getUsn());
        assertEquals(applicationTrackingOutputResultJson.getMaatRef(), audit.get().getMaatRef());
        assertEquals(LocalDateTime.now().toLocalDate(), audit.get().getDateCreated().toLocalDate());
        assertEquals(applicationTrackingOutputResultJson.getUserCreated(), audit.get().getUserCreated());
        assertEquals(STATUS_CODE, audit.get().getStatusCode());
    }

    private void assertPopulatesHistory(Optional<History> history) {
        assertTrue(history.isPresent());
        assertEquals(applicationTrackingOutputResultJson.getUsn(), history.get().getUsn());
        assertEquals(applicationTrackingOutputResultJson.getMaatRef(), history.get().getRepId());
        assertEquals(applicationTrackingOutputResultJson.getAction().value(), history.get().getAction());
        assertEquals(applicationTrackingOutputResultJson.getActionKeyId(), history.get().getKeyId());
        assertEquals(LocalDateTime.now().toLocalDate(), history.get().getDateCreated().toLocalDate());
        assertEquals(applicationTrackingOutputResultJson.getUserCreated(), history.get().getUserCreated());
    }

    private void assertPopulatesDecisionHistory(Optional<DecisionHistory> decisionHistory) {
        assertTrue(decisionHistory.isPresent());
        assertEquals(applicationTrackingOutputResultJson.getUsn(), decisionHistory.get().getUsn());
        assertEquals(applicationTrackingOutputResultJson.getMaatRef(), decisionHistory.get().getRepId());
        assertEquals(LocalDateTime.now().toLocalDate(), decisionHistory.get().getDateResultCreated().toLocalDate());
        assertEquals(applicationTrackingOutputResultJson.getCaseId(), decisionHistory.get().getCaseId());
        assertEquals(applicationTrackingOutputResultJson.getCaseType().value(), decisionHistory.get().getCaseType());
        assertEquals(applicationTrackingOutputResultJson.getIoj().getAppCreatedDate(), decisionHistory.get().getDateAppCreated());
        assertEquals(applicationTrackingOutputResultJson.getIoj().getIojResult(), decisionHistory.get().getIojResult());
        assertEquals(applicationTrackingOutputResultJson.getIoj().getIojAssessorName(), decisionHistory.get().getIojAssessorName());
        assertEquals(applicationTrackingOutputResultJson.getIoj().getIojReason(), decisionHistory.get().getIojReason());
        assertEquals(applicationTrackingOutputResultJson.getIoj().getIojAppealResult().value(), decisionHistory.get().getIojAppealResult());
        assertEquals(applicationTrackingOutputResultJson.getMeansAssessment().getMeansAssessmentResult().value(), decisionHistory.get().getMeansResult());
        assertEquals(applicationTrackingOutputResultJson.getMeansAssessment().getMeansAssessorName(), decisionHistory.get().getMeansAssessorName());
        assertEquals(applicationTrackingOutputResultJson.getMeansAssessment().getMeansAssessmentCreatedDate(), decisionHistory.get().getDateMeansCreated());
        assertEquals(applicationTrackingOutputResultJson.getCcRepDecision(), decisionHistory.get().getFundingDecision());
        assertEquals(applicationTrackingOutputResultJson.getPassport().getPassportResult().value(), decisionHistory.get().getPassportResult());
        assertEquals(applicationTrackingOutputResultJson.getPassport().getPassportAssessorName(), decisionHistory.get().getPassportAssessorName());
        assertEquals(applicationTrackingOutputResultJson.getPassport().getPassportCreatedDate(), decisionHistory.get().getDatePassportCreated());
        assertEquals(applicationTrackingOutputResultJson.getDwpResult(), decisionHistory.get().getDwpResult());
        assertEquals(applicationTrackingOutputResultJson.getHardship().getHardshipResult().value(), decisionHistory.get().getHardshipResult());
        assertEquals(applicationTrackingOutputResultJson.getRepDecision(), decisionHistory.get().getRepDecision());
        assertEquals(applicationTrackingOutputResultJson.getCcRepDecision(), decisionHistory.get().getCcRepDecision());
        assertEquals(applicationTrackingOutputResultJson.getAssessmentType().value(), decisionHistory.get().getAssessmentType());
        assertEquals(WROTE_TO_RESULTS, decisionHistory.get().getWroteToResults());
        assertEquals(applicationTrackingOutputResultJson.getMagsOutcome(), decisionHistory.get().getMagsOutcome());
    }

    private void assertPopulatesResult(Optional<Result> result) {
        assertTrue(result.isPresent());
        assertEquals(applicationTrackingOutputResultJson.getUsn(), result.get().getUsn());
        assertEquals(applicationTrackingOutputResultJson.getMaatRef(), result.get().getMaatRef());
        assertEquals(applicationTrackingOutputResultJson.getCaseId(), result.get().getCaseId());
        assertEquals(applicationTrackingOutputResultJson.getCaseType().value(), result.get().getCaseType());
        assertEquals(applicationTrackingOutputResultJson.getIoj().getAppCreatedDate(), result.get().getDateCreated());
        assertEquals(applicationTrackingOutputResultJson.getIoj().getIojResult(), result.get().getIojResult());
        assertEquals(applicationTrackingOutputResultJson.getIoj().getIojAssessorName(), result.get().getIojAssessorName());
        assertEquals(applicationTrackingOutputResultJson.getIoj().getIojReason(), result.get().getIojReason());
        assertEquals(applicationTrackingOutputResultJson.getIoj().getIojAppealResult().value(), result.get().getIojAppealResult());
        assertEquals(applicationTrackingOutputResultJson.getMeansAssessment().getMeansAssessmentResult().value(), result.get().getMeansResult());
        assertEquals(applicationTrackingOutputResultJson.getMeansAssessment().getMeansAssessorName(), result.get().getMeansAssessorName());
        assertEquals(applicationTrackingOutputResultJson.getMeansAssessment().getMeansAssessmentCreatedDate(), result.get().getDateMeansCreated());
        assertEquals(applicationTrackingOutputResultJson.getCcRepDecision(), result.get().getFundingDecision());
        assertEquals(applicationTrackingOutputResultJson.getPassport().getPassportResult().value(), result.get().getPassportResult());
        assertEquals(applicationTrackingOutputResultJson.getPassport().getPassportAssessorName(), result.get().getPassportAssesorName());
        assertEquals(applicationTrackingOutputResultJson.getPassport().getPassportCreatedDate(), result.get().getDatePassportCreated());
        assertEquals(applicationTrackingOutputResultJson.getDwpResult(), result.get().getDwpResult());
    }
}
