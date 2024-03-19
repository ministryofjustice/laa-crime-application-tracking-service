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
{{- end -}}
