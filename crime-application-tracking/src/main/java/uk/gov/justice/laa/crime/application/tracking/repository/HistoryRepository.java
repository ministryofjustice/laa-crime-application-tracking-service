package uk.gov.justice.laa.crime.application.tracking.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.gov.justice.laa.crime.application.tracking.entity.History;

@Repository
public interface HistoryRepository extends JpaRepository<History, Integer> { 
    Optional<History> findByUsn(Integer usn);
}
