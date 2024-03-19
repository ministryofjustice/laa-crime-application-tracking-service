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
public class EformResults {
    private Integer id;
    private Integer usn;
    private Integer maatRef;
    private LocalDateTime dateCreated;
    private String caseId;
    private String iojResult;
    private String iojAssessorName;
    private String meansResult;
    private String meansAssessorName;
    private LocalDateTime dateMeansCreated;
    private String fundingDecision;
    private String iojReason;
    private String passportResult;
    private String passportAssesorName;
    private LocalDateTime datePassportCreated;
    private String dwpResult;
    private String iojAppealResult;
    private String caseType;
    private String stage;
}