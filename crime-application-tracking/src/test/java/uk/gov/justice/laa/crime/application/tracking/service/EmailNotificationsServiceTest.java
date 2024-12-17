package uk.gov.justice.laa.crime.application.tracking.service;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.justice.laa.crime.application.tracking.entity.EmailBounceReport;
import uk.gov.justice.laa.crime.application.tracking.model.emailnotification.BouncedRecipients;
import uk.gov.justice.laa.crime.application.tracking.model.emailnotification.Message;
import uk.gov.justice.laa.crime.application.tracking.repository.EmailBounceReportRepository;
import uk.gov.justice.laa.crime.application.tracking.testutils.JsonUtils;
import uk.gov.justice.laa.crime.application.tracking.testutils.TestData;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmailNotificationsServiceTest {
    @Mock
    private EmailBounceReportRepository emailBounceReportRepository;

    @InjectMocks
    private EmailNotificationsService emailNotificationsService;

    @Test
    void shouldProcessEmailBounceReport() {

        String finalMessage = TestData.getFinalMessage();
        Message message = JsonUtils.jsonToObject(finalMessage, Message.class);

        emailNotificationsService.processEmailNotification(message);

        verify(emailBounceReportRepository, atLeast(1)).saveAll(anyList());
    }

    @Test
    void shouldTruncateErrorMessageOnEmailBounceReport() {
        String finalMessage = TestData.getFinalMessage();
        String longErrorMessage = "a".repeat(1500);
        Message message = JsonUtils.jsonToObject(finalMessage, Message.class);
        List<BouncedRecipients> bouncedRecipientsList = message.getBounce().getBouncedRecipients();
        bouncedRecipientsList.get(0).setDiagnosticCode(longErrorMessage);

        emailNotificationsService.processEmailNotification(message);
        
        ArgumentCaptor<List<EmailBounceReport>> captor = ArgumentCaptor.forClass(List.class);
        verify(emailBounceReportRepository).saveAll(captor.capture());
        List<EmailBounceReport> savedReports = captor.getValue();
        assertEquals(1, savedReports.size());
        EmailBounceReport report = savedReports.get(0);
        assertEquals(1000, report.getErrorMessage().length());
    }
}
