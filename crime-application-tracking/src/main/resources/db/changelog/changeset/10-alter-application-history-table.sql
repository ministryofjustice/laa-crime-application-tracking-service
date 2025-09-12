--liquibase formatted sql
--changeset Josh Hunt:10-alter-application-history-table.sql

ALTER TABLE crime_application_tracking.APPLICATION_HISTORY
ALTER COLUMN DATE_CREATED TYPE TIMESTAMP;