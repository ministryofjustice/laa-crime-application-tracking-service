package uk.gov.justice.laa.crime.application.tracking.model.emailnotification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    private String notificationType;
    private Bounce bounce;
    private Mail mail;
}
