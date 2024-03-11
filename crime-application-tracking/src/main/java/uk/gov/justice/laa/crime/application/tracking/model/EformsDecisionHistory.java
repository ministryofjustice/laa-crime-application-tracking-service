package uk.gov.justice.laa.crime.application.tracking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EformsDecisionHistory {
    private Integer id;
    private Integer usn;
    private Integer repId;
    private LocalDateTime dateResultCreated;
    private String caseId;
    private LocalDate dateAppCreated;
    private String iojResult;
    private String iojAssessorName;
    private String iojReason;
    private String meansResult;
    private String meansAssessorName;
    private LocalDate dateMeansCreated;
    private String fundingDecision;
    private String passportResult;
    private String passportAssessorName;
    private LocalDate datePassportCreated;
    private String dwpResult;
    private String iojAppealResult;
    private String hardshipResult;
    private String caseType;
    private String repDecision;
    private String ccRepDecision;
    private Integer assessmentId;
    private String assessmentType;
    private String wroteToResults;
    private String magsOutcome;
}
