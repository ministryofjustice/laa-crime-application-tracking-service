package uk.gov.justice.laa.crime.application.tracking.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
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
@Table(name = "APPLICATION_HISTORY", schema = "crime_application_tracking")
public class History {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "USN")
    @NotNull
    private Integer usn;

    @Column(name = "REP_ID")
    private Integer repId;
    
    @Column(name = "ACTION")
    private String action;

    @Column(name = "KEY_ID")
    private Integer keyId;

    @Column(name = "USER_CREATED")
    private String userCreated;

    @Column(name = "DATE_CREATED", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime dateCreated;
}