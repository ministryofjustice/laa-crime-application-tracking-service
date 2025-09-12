package uk.gov.justice.laa.crime.application.tracking.client;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import uk.gov.justice.laa.crime.application.tracking.model.*;

@HttpExchange()
public interface MaatCourtDataApiClient {

    @GetExchange("/internal/v1/assessment/financial-assessments/check-outstanding/{repId}")
    OutstandingAssessment findOutstandingAssessments(@PathVariable Integer repId);

    @GetExchange("/internal/v1/assessment/rep-orders/{repId}/ioj-assessor-details")
    AssessorDetails findIOJAssessorDetails(@PathVariable int repId);

    @GetExchange("/internal/v1/assessment/financial-assessments/{financialAssessmentId}/means-assessor-details")
    AssessorDetails findMeansAssessorDetails(@PathVariable int financialAssessmentId);

    @GetExchange("/internal/v1/assessment/passport-assessments/{passportAssessmentId}/passport-assessor-details")
    AssessorDetails findPassportAssessorDetails(@PathVariable int passportAssessmentId);
}
