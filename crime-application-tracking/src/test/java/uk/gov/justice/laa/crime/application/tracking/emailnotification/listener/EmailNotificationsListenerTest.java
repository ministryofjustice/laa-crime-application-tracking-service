package uk.gov.justice.laa.crime.application.tracking.emailnotification.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.justice.laa.crime.application.tracking.model.emailnotification.Message;
import uk.gov.justice.laa.crime.application.tracking.service.EmailNotificationsService;
import uk.gov.justice.laa.crime.application.tracking.testutils.JsonUtils;
import uk.gov.justice.laa.crime.application.tracking.testutils.TestData;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SoftAssertionsExtension.class)
public class EmailNotificationsListenerTest {

    @InjectMocks
    private EmailNotificationsListener emailNotificationsListener;
    @Mock
    private EmailNotificationsService emailNotificationsService;

    @Test
    void givenJSONMessageIsReceived_whenEmailNotificationsListenerIsInvoked_thenReceiveIsCalled() throws JsonProcessingException {

        String finalMessage = TestData.getFinalMessage();
        Message message =JsonUtils.jsonToObject(finalMessage, Message.class);
        emailNotificationsListener.receive(TestData.getEmailNotificationMessage());
        verify(emailNotificationsService, times(1)).processEmailNotification(message);
    }
}
