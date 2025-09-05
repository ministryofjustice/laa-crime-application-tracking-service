package uk.gov.justice.laa.crime.application.tracking.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.gov.justice.laa.crime.application.tracking.entity.DecisionHistory;

@Repository
public interface DecisionHistoryRepository extends JpaRepository<DecisionHistory, Integer> {
    Optional<DecisionHistory> findFirstByUsnAndWroteToResultsOrderByIdDesc(Integer usn, String wroteResult);

    DecisionHistory findTopByUsnOrderByIdDesc(Integer usn);
}
