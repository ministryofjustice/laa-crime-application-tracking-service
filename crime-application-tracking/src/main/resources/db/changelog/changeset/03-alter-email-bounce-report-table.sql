--liquibase formatted sql
--changeset Gangadhar Nitta:03-alter-email-bounce-report-table.sql

ALTER TABLE crime_application_tracking.EMAIL_BOUNCE_REPORT
ALTER COLUMN ERROR_MESSAGE TYPE VARCHAR(400);