package uk.gov.justice.laa.crime.application.tracking.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.justice.laa.crime.application.tracking.model.ApplicationTrackingOutputResult;
import uk.gov.justice.laa.crime.application.tracking.service.ApplicationTrackingService;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/internal/v1/application-tracking-output-result")
@Tag(name = "Application Tracking Service", description = "Rest API to process Application Tracking and Output Result")
public class ApplicationTrackingController {

    private final ApplicationTrackingService applicationTrackingService;

    @PostMapping
    @Operation(description = "Process Application Tracking and Output Result")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @ApiResponse(responseCode = "400",
            description = "Bad request.",
            content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                    schema = @Schema(implementation = ProblemDetail.class)
            )
    )
    @ApiResponse(responseCode = "500",
            description = "Server Error.",
            content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                    schema = @Schema(implementation = ProblemDetail.class)
            )
    )

    public ResponseEntity<Void> processApplicationTrackingAndOutputResult(@RequestBody ApplicationTrackingOutputResult applicationTrackingOutputResult) {
        applicationTrackingService.processApplicationTrackingAndOutputResultData(applicationTrackingOutputResult);
        return ResponseEntity.ok().build();
    }
}
