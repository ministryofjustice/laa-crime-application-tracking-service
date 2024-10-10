package uk.gov.justice.laa.crime.application.tracking.model.emailnotification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BouncedRecipients {
    private String emailAddress;
    private String action;
    private String status;
    private String diagnosticCode;
}
