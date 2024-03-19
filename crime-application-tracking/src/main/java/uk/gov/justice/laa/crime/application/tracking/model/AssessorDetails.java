package uk.gov.justice.laa.crime.application.tracking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssessorDetails {

        private String fullName;
        private String userName;
}
