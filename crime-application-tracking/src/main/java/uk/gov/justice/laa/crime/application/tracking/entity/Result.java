package uk.gov.justice.laa.crime.application.tracking.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "APPLICATION_RESULT", schema = "crime_application_tracking")
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;
    
    @Column(name = "USN", nullable = false)
    private Integer usn;

    @Column(name = "MAAT_REF", nullable = false)
    private Integer maatRef;

    @Column(name = "DATE_APP_CREATED")
    private LocalDateTime dateCreated;

    @Column(name = "CASE_ID")
    private String caseId;

    @Column(name = "IOJ_RESULT")
    private String iojResult;

    @Column(name = "IOJ_ASSESSOR_NAME")
    private String iojAssessorName;

    @Column(name = "MEANS_RESULT")
    private String meansResult;

    @Column(name = "MEANS_ASSESSOR_NAME")
    private String meansAssessorName;

    @Column(name = "DATE_MEANS_CREATED")
    private LocalDateTime dateMeansCreated;

    @Column(name = "FUNDING_DECISION")
    private String fundingDecision;

    @Column(name = "IOJ_REASON")
    private String iojReason;

    @Column(name = "PASSPORT_RESULT")
    private String passportResult;

    @Column(name = "PASSPORT_ASSESSOR_NAME")
    private String passportAssesorName;

    @Column(name = "DATE_PASSPORT_CREATED")
    private LocalDateTime datePassportCreated;

    @Column(name = "DWP_RESULT")
    private String dwpResult;

    @Column(name = "IOJ_APPEAL_RESULT")
    private String iojAppealResult;

    @Column(name = "CASE_TYPE")
    private String caseType;

    @Column(name = "STAGE")
    private String stage;
}
