--liquibase formatted sql
--changeset Ross Nation:04-alter-email-bounce-report-table.sql

ALTER TABLE crime_application_tracking.EMAIL_BOUNCE_REPORT
ALTER COLUMN ERROR_MESSAGE TYPE VARCHAR(1000);