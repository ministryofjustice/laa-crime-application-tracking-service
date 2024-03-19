package uk.gov.justice.laa.crime.application.tracking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EformsAudit {
    private Integer id;
    private Integer usn;
    private Integer maatRef;
    private String userCreated;
    private LocalDateTime dateCreated;
    private String statusCode;
}
