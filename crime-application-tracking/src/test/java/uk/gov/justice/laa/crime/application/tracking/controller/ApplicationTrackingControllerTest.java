package uk.gov.justice.laa.crime.application.tracking.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import uk.gov.justice.laa.crime.application.tracking.model.ApplicationTrackingOutputResult;
import uk.gov.justice.laa.crime.application.tracking.service.ApplicationTrackingService;
import uk.gov.justice.laa.crime.application.tracking.testutils.JsonUtils;
import uk.gov.justice.laa.crime.application.tracking.testutils.TestData;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ApplicationTrackingController.class)
@AutoConfigureMockMvc(addFilters = false)
class ApplicationTrackingControllerTest {

    private static final String BASE_ENDPOINT_FORMAT = "/api/internal/v1/application-tracking-output-result";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ApplicationTrackingService applicationTrackingService;

    @Test
    void shouldSuccessfullyProcessApplicationTrackingAndOutputResultForValidaRequest() throws Exception {
        var atsRequest = TestData.getAtsRequest();
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_ENDPOINT_FORMAT).content(JsonUtils.objectToJson(atsRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(applicationTrackingService, times(1)).processApplicationTrackingAndOutputResultData(atsRequest);
    }

    @Test
    void shouldReturnBadRequestForInvalidProcessApplicationTrackingAndOutputResultRequest() throws Exception {
        var atsRequest = new ApplicationTrackingOutputResult();
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_ENDPOINT_FORMAT).content("")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(applicationTrackingService, times(0)).processApplicationTrackingAndOutputResultData(atsRequest);
    }

    @Test
    void shouldReturn_InternalServerError_whenAPIClientException() throws Exception {
        var atsRequest = new ApplicationTrackingOutputResult();
        doThrow(WebClientRequestException.class).when(applicationTrackingService).processApplicationTrackingAndOutputResultData(atsRequest);
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_ENDPOINT_FORMAT).content(JsonUtils.objectToJson(atsRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE));
    }
}
