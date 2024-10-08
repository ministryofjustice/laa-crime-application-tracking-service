package uk.gov.justice.laa.crime.application.tracking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.justice.laa.crime.application.tracking.model.emailnotification.Message;
import uk.gov.justice.laa.crime.application.tracking.repository.EmailBounceReportRepository;
import uk.gov.justice.laa.crime.application.tracking.testutils.JsonUtils;
import uk.gov.justice.laa.crime.application.tracking.testutils.TestData;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmailNotificationsServiceTest {
    @Mock
    private EmailBounceReportRepository emailBounceReportRepository;

    @InjectMocks
    private EmailNotificationsService emailNotificationsService;

    @BeforeEach
    void setup() {

    }
    @Test
    void shouldProcessEmailBounceReport() {

        String finalMessage = TestData.getFinalMessage();
        Message message = JsonUtils.jsonToObject(finalMessage, Message.class);

        emailNotificationsService.processEmailNotification(message);

        verify(emailBounceReportRepository, atLeast(1)).saveAll(anyList());
    }
}
