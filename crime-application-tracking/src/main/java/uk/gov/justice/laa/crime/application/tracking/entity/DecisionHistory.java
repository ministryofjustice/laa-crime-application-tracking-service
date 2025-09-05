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
import org.hibernate.annotations.CreationTimestamp;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "APPLICATION_DECISION_HISTORY", schema = "crime_application_tracking")
public class DecisionHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "USN", nullable = false)
    private Integer usn;

    @Column(name = "REP_ID", nullable = false)
    private Integer repId;
    
    @Column(name = "DATE_RESULT_CREATED")
    @CreationTimestamp
    private LocalDateTime dateResultCreated;

    @Column(name = "CASE_ID")
    private String caseId;

    @Column(name = "DATE_APP_CREATED")
    private LocalDateTime dateAppCreated;

    @Column(name = "IOJ_RESULT")
    private String iojResult;

    @Column(name = "IOJ_ASSESSOR_NAME")
    private String iojAssessorName;

    @Column(name = "IOJ_REASON")
    private String iojReason;

    @Column(name = "MEANS_RESULT")
    private String meansResult;

    @Column(name = "MEANS_ASSESSOR_NAME")
    private String meansAssessorName;

    @Column(name = "DATE_MEANS_CREATED")
    private LocalDateTime dateMeansCreated;

    @Column(name = "FUNDING_DECISION")
    private String fundingDecision;

    @Column(name = "PASSPORT_RESULT")
    private String passportResult;

    @Column(name = "PASSPORT_ASSESSOR_NAME")
    private String passportAssessorName;

    @Column(name = "DATE_PASSPORT_CREATED")
    private LocalDateTime datePassportCreated;

    @Column(name = "DWP_RESULT")
    private String dwpResult;

    @Column(name = "IOJ_APPEAL_RESULT")
    private String iojAppealResult;

    @Column(name = "HARDSHIP_RESULT")
    private String hardshipResult;

    @Column(name = "CASE_TYPE")
    private String caseType;

    @Column(name = "REP_DECISION")
    private String repDecision;

    @Column(name = "CC_REP_DECISION")
    private String ccRepDecision;

    @Column(name = "ASSESSMENT_ID")
    private Integer assessmentId;

    @Column(name = "ASSESSMENT_TYPE")
    private String assessmentType;

    @Column(name = "WROTE_TO_RESULTS")
    private String wroteToResults;

    @Column(name = "MAGS_OUTCOME")
    private String magsOutcome;
}