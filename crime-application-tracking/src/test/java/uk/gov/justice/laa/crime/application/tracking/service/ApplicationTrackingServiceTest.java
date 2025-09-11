package uk.gov.justice.laa.crime.application.tracking.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.justice.laa.crime.application.tracking.model.ApplicationTrackingOutputResult;
import uk.gov.justice.laa.crime.application.tracking.testutils.TestData;

import java.util.stream.Stream;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ApplicationTrackingServiceTest {
    @Mock
    private EformAuditService eformAuditService;
    @Mock
    private EformsHistoryService eformsHistoryService;
    @Mock
    private ApplicationOutputResultService applicationOutputResultService;

    @InjectMocks
    private ApplicationTrackingService applicationTrackingService;

    @ParameterizedTest
    @MethodSource("requestSourceTestData")
    void shouldProcessApplicationTrackingAndOutputResultForCreateApplication(ApplicationTrackingOutputResult.RequestSource requestSource){
        var atsRequest = TestData.getAtsRequest();
        atsRequest.setRequestSource(requestSource);
        applicationTrackingService.processApplicationTrackingAndOutputResultData(atsRequest);
        switch (requestSource){
            case CREATE_APPLICATION -> {
                verify(eformAuditService, times(1)).createAudit(atsRequest);
                verify(eformsHistoryService, times(1)).createEformHistory(atsRequest);
                verify(applicationOutputResultService, times(1)).processOutputResult(atsRequest);
            }
            case HARDSHIP, CROWN_COURT -> {
                verify(eformAuditService, times(0)).createAudit(atsRequest);
                verify(eformsHistoryService, times(0)).createEformHistory(atsRequest);
                verify(applicationOutputResultService, times(1)).processOutputResult(atsRequest);
            }
            case MEANS_ASSESSMENT, PASSPORT_IOJ -> {
                verify(eformAuditService, times(0)).createAudit(atsRequest);
                verify(eformsHistoryService, times(1)).createEformHistory(atsRequest);
                verify(applicationOutputResultService, times(1)).processOutputResult(atsRequest);
            }
            case CAPITAL_AND_EQUITY -> {
                verify(eformAuditService, times(0)).createAudit(atsRequest);
                verify(eformsHistoryService, times(0)).createEformHistory(atsRequest);
                verify(applicationOutputResultService, times(0)).processOutputResult(atsRequest);

            }
        }
    }
    private static Stream<Arguments> requestSourceTestData() {
        return Stream.of(
                Arguments.of(ApplicationTrackingOutputResult.RequestSource.CREATE_APPLICATION),
                Arguments.of(ApplicationTrackingOutputResult.RequestSource.PASSPORT_IOJ),
                Arguments.of(ApplicationTrackingOutputResult.RequestSource.MEANS_ASSESSMENT),
                Arguments.of(ApplicationTrackingOutputResult.RequestSource.CROWN_COURT),
                Arguments.of(ApplicationTrackingOutputResult.RequestSource.HARDSHIP),
                Arguments.of(ApplicationTrackingOutputResult.RequestSource.CAPITAL_AND_EQUITY)
        );
    }
}
