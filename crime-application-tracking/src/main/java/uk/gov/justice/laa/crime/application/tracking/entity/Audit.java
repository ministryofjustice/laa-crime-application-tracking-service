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
@Table(name = "APPLICATION_AUDIT", schema = "crime_application_tracking")
public class Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;
    
    @Column(name = "USN")
    @NotNull
    private Integer usn;
    
    @Column(name = "MAAT_REF")
    private Integer maatRef;
    
    @Column(name = "USER_CREATED")
    private String userCreated;

    @CreationTimestamp
    @Column(name = "DATE_CREATED", nullable = false, updatable = false)
    private LocalDateTime dateCreated;
    
    @Column(name = "STATUS_CODE")
    private String statusCode;
}
