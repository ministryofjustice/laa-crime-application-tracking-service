package uk.gov.justice.laa.crime.application.tracking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import uk.gov.justice.laa.crime.application.tracking.entity.EmailBounceReport;
import uk.gov.justice.laa.crime.application.tracking.model.emailnotification.BouncedRecipients;
import uk.gov.justice.laa.crime.application.tracking.model.emailnotification.Message;
import uk.gov.justice.laa.crime.application.tracking.repository.EmailBounceReportRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailNotificationsService {
    private static final String BOUNCE = "Bounce";
    private static final int MAX_ERROR_MESSAGE_LENGTH = 1000;

    private final EmailBounceReportRepository emailBounceReportRepository;

    public void processEmailNotification(Message message) {
        log.info("Processing email notification data");
        if (Objects.nonNull(message) && BOUNCE.equals(message.getNotificationType())) {
            List<EmailBounceReport> emailBounceReports = buildEmailBounceReports(message);
            if (!CollectionUtils.isEmpty(emailBounceReports)) {
                emailBounceReportRepository.saveAll(emailBounceReports);
                log.info("Inserted Email Bounce Reports into DB successfully");
            }
        }
    }

    private List<EmailBounceReport> buildEmailBounceReports(Message message) {

        EmailBounceReport.EmailBounceReportBuilder emailBounceReportBuilder = EmailBounceReport.builder().bounceType(message.getBounce().getBounceType())
                .bounceSubType(message.getBounce().getBounceSubType())
                .timeStamp(message.getMail().getTimestamp())
                .maatId(extractMaatId(message.getMail().getCommonHeaders().getSubject()));

        return buildEmailAddressAndDiagnosticCode(emailBounceReportBuilder, message.getBounce().getBouncedRecipients());
    }

    private List<EmailBounceReport> buildEmailAddressAndDiagnosticCode(EmailBounceReport.EmailBounceReportBuilder emailBounceReportBuilder, List<BouncedRecipients> bouncedRecipients) {
        List<EmailBounceReport> emailBounceReports = new ArrayList<>();
        bouncedRecipients.forEach(bouncedRecipient -> {
            emailBounceReportBuilder.recipientEmailAddress(bouncedRecipient.getEmailAddress());
            String errorMessage = bouncedRecipient.getDiagnosticCode();
            if (errorMessage.length() > MAX_ERROR_MESSAGE_LENGTH) {
                errorMessage = errorMessage.substring(0, MAX_ERROR_MESSAGE_LENGTH);
            }
            emailBounceReportBuilder.errorMessage(errorMessage);
            emailBounceReports.add(emailBounceReportBuilder.build());
        });
        return emailBounceReports;
    }

    private Integer extractMaatId(String subject) {
        String regex = "\\d+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(subject);
        return matcher.find() ? Integer.parseInt(matcher.group()) : 0;
    }
}
