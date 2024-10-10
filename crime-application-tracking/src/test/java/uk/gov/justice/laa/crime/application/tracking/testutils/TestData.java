package uk.gov.justice.laa.crime.application.tracking.testutils;

import uk.gov.justice.laa.crime.application.tracking.model.ApplicationTrackingOutputResult;

public class TestData {

    private TestData() {
    }

    public static ApplicationTrackingOutputResult getAtsRequest() {
        String atsJson = FileUtils.readFileToString("testdata/ApplicationTrackingOutputResult_default.json");
        return JsonUtils.jsonToObject(atsJson, ApplicationTrackingOutputResult.class);
    }

    public static String getEmailNotificationMessage() {
        return """
                {
                       "Type" : "Notification",
                       "MessageId" : "405c7d61-f349-514e-98cf-221bb9586dda",
                       "TopicArn" : "arn:aws:sns:eu-west-1:140455166311:ses_complaint_events_uat",
                       "Message" : "{\\"notificationType\\":\\"Bounce\\",\\"bounce\\":{\\"feedbackId\\":\\"010201926b0a9d5e-55a4fac9-afd8-4104-b3dc-53e74e9b2df7-000000\\",\\"bounceType\\":\\"Permanent\\",\\"bounceSubType\\":\\"General\\",\\"bouncedRecipients\\":[{\\"emailAddress\\":\\"ga.nitta@uat.legalservices.gov.uk\\",\\"action\\":\\"failed\\",\\"status\\":\\"5.1.1\\",\\"diagnosticCode\\":\\"smtp; 550 5.1.1 Requested action not taken: mailbox unavailable\\"}],\\"timestamp\\":\\"2024-10-08T07:31:50.000Z\\",\\"remoteMtaIp\\":\\"18.200.203.69\\",\\"reportingMTA\\":\\"dns; a7-48.smtp-out.eu-west-1.amazonses.com\\"},\\"mail\\":{\\"timestamp\\":\\"2024-10-08T07:31:50.364Z\\",\\"source\\":\\"laareporders@uat.legalservices.gov.uk\\",\\"sourceArn\\":\\"arn:aws:ses:eu-west-1:140455166311:identity/laareporders@uat.legalservices.gov.uk\\",\\"sourceIp\\":\\"35.178.127.79\\",\\"callerIdentity\\":\\"LAA-MAATDB-uat-AppInfrastructureTemplate-1-SESUser-14APVMBG5M9D0\\",\\"sendingAccountId\\":\\"140455166311\\",\\"messageId\\":\\"010201926b0a9bdc-05b6ca87-1fc1-40fd-8184-63fa01b34a7e-000000\\",\\"destination\\":[\\"ganga.nitta@uat.legalservices.gov.uk\\",\\"ga.nitta@uat.legalservices.gov.uk\\"],\\"headersTruncated\\":false,\\"headers\\":[{\\"name\\":\\"Received\\",\\"value\\":\\"from null (ec2-35-178-127-79.eu-west-2.compute.amazonaws.com [35.178.127.79]) by email-smtp.amazonaws.com with SMTP (SimpleEmailService-d-6XTYVYXG8) id bi7TXur2JTy2vahe4vnD; Tue, 08 Oct 2024 07:31:50 +0000 (UTC)\\"},{\\"name\\":\\"From\\",\\"value\\":\\"laareporders@uat.legalservices.gov.uk\\"},{\\"name\\":\\"To\\",\\"value\\":\\"ganga.nitta@uat.legalservices.gov.uk\\"},{\\"name\\":\\"Cc\\",\\"value\\":\\"ga.nitta@uat.legalservices.gov.uk\\"},{\\"name\\":\\"Subject\\",\\"value\\":\\"Refusal Notice - Official - Client Name: Anthony LAAcaseA, MAAT ID: 6022301\\"},{\\"name\\":\\"Content-Type\\",\\"value\\":\\"multipart/mixed; boundary= \\\\\\"1234567890>>*<<0987654321\\\\\\"\\"}],\\"commonHeaders\\":{\\"from\\":[\\"laareporders@uat.legalservices.gov.uk\\"],\\"to\\":[\\"ganga.nitta@uat.legalservices.gov.uk\\"],\\"cc\\":[\\"ga.nitta@uat.legalservices.gov.uk\\"],\\"subject\\":\\"Refusal Notice - Official - Client Name: Anthony LAAcaseA, MAAT ID: 6022301\\"}}}",
                       "Timestamp" : "2024-10-04T19:53:53.103Z",
                       "SignatureVersion" : "1",
                       "Signature" : "dcdcdcdccdcdcdcdcdcdc",
                       "SigningCertURL" : "sdsdsdfsfdfffdg",
                       "UnsubscribeURL" : "rerrerererere"
                    }
               """;
    }

    public static String getFinalMessage() {

        return """
                {
                   "notificationType" : "Bounce",
                   "bounce" : {
                     "feedbackId" : "010201926b0a9d5e-55a4fac9-afd8-4104-b3dc-53e74e9b2df7-000000",
                     "bounceType" : "Permanent",
                     "bounceSubType" : "General",
                     "bouncedRecipients" : [ {
                       "emailAddress" : "ga.nitta@uat.legalservices.gov.uk",
                       "action" : "failed",
                       "status" : "5.1.1",
                       "diagnosticCode" : "smtp; 550 5.1.1 Requested action not taken: mailbox unavailable"
                     } ],
                     "timestamp" : "2024-10-08T07:31:50.000Z",
                     "remoteMtaIp" : "18.200.203.69",
                     "reportingMTA" : "dns; a7-48.smtp-out.eu-west-1.amazonses.com"
                   },
                   "mail" : {
                     "timestamp" : "2024-10-08T07:31:50.364Z",
                     "source" : "laareporders@uat.legalservices.gov.uk",
                     "sourceArn" : "arn:aws:ses:eu-west-1:140455166311:identity/laareporders@uat.legalservices.gov.uk",
                     "sourceIp" : "35.178.127.79",
                     "callerIdentity" : "LAA-MAATDB-uat-AppInfrastructureTemplate-1-SESUser-14APVMBG5M9D0",
                     "sendingAccountId" : "140455166311",
                     "messageId" : "010201926b0a9bdc-05b6ca87-1fc1-40fd-8184-63fa01b34a7e-000000",
                     "destination" : [ "ganga.nitta@uat.legalservices.gov.uk", "ga.nitta@uat.legalservices.gov.uk" ],
                     "headersTruncated" : false,
                     "commonHeaders" : {
                       "from" : [ "laareporders@uat.legalservices.gov.uk" ],
                       "to" : [ "ganga.nitta@uat.legalservices.gov.uk" ],
                       "cc" : [ "ga.nitta@uat.legalservices.gov.uk" ],
                       "subject" : "Refusal Notice - Official - Client Name: Anthony LAAcaseA, MAAT ID: 6022301"
                     }
                   }
                 }
                """;
    }
}
