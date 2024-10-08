{{/* vim: set filetype=mustache: */}}
{{/*
Environment variables for service containers
*/}}
{{- define "laa-crime-application-tracking-service.env-vars" }}
env:
  - name: AWS_REGION
    value: {{ .Values.aws_region }}
  - name: SENTRY_DSN
    value: {{ .Values.sentry.dsn }}
  - name: SENTRY_ENV
    value: {{ .Values.java.host_env }}
  - name: SENTRY_SAMPLE_RATE
    value: {{ .Values.sentry.sampleRate | quote }}
  - name: JWT_ISSUER_URI
    value: {{ .Values.jwt.issuerUri }}
  - name: MAAT_API_BASE_URL
    value: {{ .Values.maatApi.baseUrl }}
  - name: MAAT_API_OAUTH_URL
    value: {{ .Values.maatApi.oauthUrl }}
  - name: MAAT_API_OAUTH_CLIENT_ID
    valueFrom:
      secretKeyRef:
        name: maat-api-oauth-client-credentials
        key: MAAT_API_OAUTH_CLIENT_ID
  - name: MAAT_API_OAUTH_CLIENT_SECRET
    valueFrom:
      secretKeyRef:
        name: maat-api-oauth-client-credentials
        key: MAAT_API_OAUTH_CLIENT_SECRET
  - name: MAAT_API_REGISTRATION_ID
    value: {{ .Values.maatApi.registrationId }}
  - name: MAAT_API_OAUTH_SCOPE
    value: {{ .Values.maatApi.oauthScope }}
  - name: DATASOURCE_HOST_PORT
    valueFrom:
      secretKeyRef:
        name: rds-postgresql-instance-output
        key: rds_instance_endpoint
  - name: DATASOURCE_DBNAME
    valueFrom:
      secretKeyRef:
        name: rds-postgresql-instance-output
        key: database_name
  - name: DATASOURCE_USERNAME
    valueFrom:
      secretKeyRef:
        name: rds-postgresql-instance-output
        key: database_username
  - name: DATASOURCE_PASSWORD
    valueFrom:
      secretKeyRef:
        name: rds-postgresql-instance-output
        key: database_password
  - name: EMAIL_NOTIFICATIONS_LISTENER_ENABLED
    value: {{ .Values.cloudPlatform.aws.sqs.enabled | quote }}
  {{- if .Values.cloudPlatform.aws.sqs.enabled }}
  - name: EMAIL_NOTIFICATIONS_QUEUE
    value: {{ .Values.cloudPlatform.aws.sqs.queue.email_notifications }}
  {{- end }}
{{- end -}}
