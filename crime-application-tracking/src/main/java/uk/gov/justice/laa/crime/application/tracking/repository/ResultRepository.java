package uk.gov.justice.laa.crime.application.tracking.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.gov.justice.laa.crime.application.tracking.entity.Result;

@Repository
public interface ResultRepository extends JpaRepository<Result, Integer> {
    Optional<Result> findByUsn(Integer usn);
}
