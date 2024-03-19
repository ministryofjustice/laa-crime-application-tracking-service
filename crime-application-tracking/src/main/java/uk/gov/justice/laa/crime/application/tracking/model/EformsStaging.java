package uk.gov.justice.laa.crime.application.tracking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EformsStaging {
    private Integer usn;
    private String type;
    private Integer maatRef;
    private String maatStatus;
    private String userCreated;
}
