package uk.gov.justice.laa.crime.application.tracking.model.emailnotification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Bounce {
    private String feedbackId;
    private String bounceType;
    private String bounceSubType;
    private List<BouncedRecipients> bouncedRecipients;
    private String timestamp;
    private String remoteMtaIp;
    private String reportingMTA;
}
