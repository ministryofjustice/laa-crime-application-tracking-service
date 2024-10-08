package uk.gov.justice.laa.crime.application.tracking.model.emailnotification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Mail {
    private String timestamp;
    private String source;
    private String sourceArn;
    private String sourceIp;
    private String callerIdentity;
    private String sendingAccountId;
    private String messageId;
    private List<String> destination;
    private boolean headersTruncated;
    private CommonHeaders commonHeaders;
}
