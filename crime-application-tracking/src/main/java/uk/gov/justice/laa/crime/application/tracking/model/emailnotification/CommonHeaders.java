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
public class CommonHeaders {
    private List<String> from;
    private List<String> to;
    private List<String> cc;
    private String subject;
}
