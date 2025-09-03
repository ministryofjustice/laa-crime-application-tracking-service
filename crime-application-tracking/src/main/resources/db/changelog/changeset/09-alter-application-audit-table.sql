--liquibase formatted sql
--changeset Josh Hunt:09-alter-application-audit-table.sql

ALTER TABLE crime_application_tracking.APPLICATION_AUDIT
ALTER COLUMN DATE_CREATED TYPE TIMESTAMP;