package uk.gov.justice.laa.crime.application.tracking.service;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.justice.laa.crime.application.tracking.client.MaatCourtDataApiClient;
import uk.gov.justice.laa.crime.application.tracking.model.AssessorDetails;
import uk.gov.justice.laa.crime.application.tracking.model.OutstandingAssessment;

@Service
@RequiredArgsConstructor
@Slf4j
public class AssessmentAssessorService {
    private static final String SERVICE_NAME = "assessmentAssessorService";
    private final MaatCourtDataApiClient maatCourtDataApiClient;
    private final ObservationRegistry observationRegistry;

    public OutstandingAssessment checkOutstandingAssessment(Integer maatRef) {
        log.info("Start - call to check outstanding assessments for  {}", maatRef);
        OutstandingAssessment outstandingAssessment = maatCourtDataApiClient.checkForOutstandingAssessments(maatRef);
        return Observation.createNotStarted(SERVICE_NAME, observationRegistry)
                .observe(() -> outstandingAssessment);
    }

    public AssessorDetails getIOJAssessor(Integer maatRef) {
        log.info("Start - call to get IOJ Assessor Name for  {}", maatRef);
        AssessorDetails iojAssessor = maatCourtDataApiClient.findIOJAssessorDetails(maatRef);
        return Observation.createNotStarted(SERVICE_NAME, observationRegistry)
                .observe(() -> iojAssessor);
    }

    public AssessorDetails getMeansAssessor(Integer meansAssessmentId) {
        log.info("Start - call to get Means Assessor Name for  {}", meansAssessmentId);
        AssessorDetails meansAssessor = maatCourtDataApiClient.findMeansAssessorDetails(meansAssessmentId);
        return Observation.createNotStarted(SERVICE_NAME, observationRegistry)
                .observe(() -> meansAssessor);
    }

    public AssessorDetails getPassportAssessor(Integer passportId) {
        log.info("Start - call to get Passported Assessor Name for  {}", passportId);
        AssessorDetails passportAssessor = maatCourtDataApiClient.findPassportAssessorDetails(passportId);
        return Observation.createNotStarted(SERVICE_NAME, observationRegistry)
                .observe(() -> passportAssessor);
    }
}
