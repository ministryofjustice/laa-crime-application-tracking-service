package uk.gov.justice.laa.crime.application.tracking.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "EMAIL_BOUNCE_REPORT", schema = "crime_application_tracking")
public class EmailBounceReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "TIME_STAMP")
    private String timeStamp;

    @Column(name = "MAAT_ID")
    private Integer maatId;

    @Column(name = "RECIPIENT_EMAIL_ADDRESS")
    private String recipientEmailAddress;

    @Column(name = "BOUNCE_TYPE")
    private String bounceType;

    @Column(name = "BOUNCE_SUB_TYPE")
    private String bounceSubType;

    @Column(name = "ERROR_MESSAGE")
    private String errorMessage;
}
