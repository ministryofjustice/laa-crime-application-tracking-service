package uk.gov.justice.laa.crime.application.tracking.emailnotification.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import uk.gov.justice.laa.crime.application.tracking.model.emailnotification.Message;
import uk.gov.justice.laa.crime.application.tracking.service.EmailNotificationsService;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(value = "feature.email_notifications.enabled", havingValue = "true")
public class EmailNotificationsListener {

    private static final String MESSAGE_KEY = "\"Message\" :";
    private static final String MESSAGE_END_KEY = "}}}\",";
    private static final String HEADERS_KEY = "\"headers\":[";
    private static final String HEADER_END_KEY = "],";
    private static final String MESSAGE_END = "}}}";
    private final EmailNotificationsService emailNotificationsService;

    @SqsListener(value = "${cloud-platform.aws.sqs.queue.email_notifications}")
    public void receive(@Payload final String emailNotificationsJson) {
        try {
            log.info("Received message and Processing");

            String messageJson = getMessageJson(emailNotificationsJson.trim());

            ObjectMapper objectMapper = new ObjectMapper();
            // Convert JSON string to POJO
            Message message = objectMapper.readValue(messageJson, Message.class);

            emailNotificationsService.processEmailNotification(message);
        } catch (Exception exception) {
            log.warn("Error processing email notification - {}", exception.getMessage());
        }
    }

    private String getMessageJson(String emailNotificationsJson) {

        int messageStartIndex = emailNotificationsJson.indexOf(MESSAGE_KEY) + MESSAGE_KEY.length();
        int messageEndIndex = emailNotificationsJson.indexOf(MESSAGE_END_KEY, messageStartIndex);

        // Extract and clean up the "Message" content
        String messageString = emailNotificationsJson.substring(messageStartIndex, messageEndIndex);
        messageString = messageString.replace("\\", "");

        int headerStartIndex = messageString.indexOf(HEADERS_KEY);
        int headerEndIndex = messageString.indexOf(HEADER_END_KEY, headerStartIndex);

        StringBuilder sb = new StringBuilder(messageString);
        //Deleting headers section as it is not required
        sb.delete(headerStartIndex, headerEndIndex + 2);

        return sb.append(MESSAGE_END).toString().trim().replace("\"{", "{");
    }
}
