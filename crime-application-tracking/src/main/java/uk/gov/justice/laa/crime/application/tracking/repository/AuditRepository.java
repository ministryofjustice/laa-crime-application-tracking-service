package uk.gov.justice.laa.crime.application.tracking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.gov.justice.laa.crime.application.tracking.entity.Audit;

@Repository
public interface AuditRepository extends JpaRepository<Audit, Integer> { }
