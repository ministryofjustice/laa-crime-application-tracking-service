package uk.gov.justice.laa.crime.application.tracking.client;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PatchExchange;
import org.springframework.web.service.annotation.PostExchange;
import uk.gov.justice.laa.crime.application.tracking.model.*;

@HttpExchange()
public interface MaatCourtDataApiClient {

    @PostExchange("/eform/audit")
    void createEformsAuditRecord(@RequestBody EformsAudit eformsAudit);

    @GetExchange("/eform/audit/{usn}")
    EformsAudit getEformsAuditRecord(@PathVariable Integer usn);

    @PatchExchange("/eform/{usn}")
    void updateEformStagingRecord(@PathVariable Integer usn,
                                  @RequestBody EformsStaging eformsStaging);

    @PostExchange("/eform/history")
    void createEformsHistoryRecord(@RequestBody EformsHistory eformsHistory);

    @PostExchange("/eform/decision-history")
    void createEformsDecisionHistoryRecord(@RequestBody EformsDecisionHistory eformsDecisionHistory);

    @GetExchange("/eform/decision-history/{usn}/previous-wrote-to-result")
    EformsDecisionHistory getPreviousEformsDecisionHistoryRecordWroteToResult(@PathVariable Integer usn);

    @PatchExchange("/eform/decision-history/{usn}")
    void updateEformsDecisionHistoryRecord(@PathVariable Integer usn,
                                           @RequestBody EformsDecisionHistory eformsDecisionHistory);
    @PostExchange("/eform/results")
    void createEformResult(@RequestBody EformResults eformResults);

    @GetExchange
    OutstandingAssessment checkForOutstandingAssessments(@PathVariable Integer maatRef);

    @GetExchange("/internal/v1/assessment/{repId}/ioj-assessor-details")
    AssessorDetails findIOJAssessorDetails(@PathVariable int repId);

    @GetExchange("/internal/v1/assessment/{financialAssessmentId}/means-assessor-details")
    AssessorDetails findMeansAssessorDetails(@PathVariable int financialAssessmentId);

    @GetExchange("/internal/v1/assessment/{passportAssessmentId}/passport-assessor-details")
    AssessorDetails findPassportAssessorDetails(@PathVariable int passportAssessmentId);
}