--liquibase formatted sql
--changeset Josh Hunt:05-create-application-audit-table.sql

CREATE TABLE IF NOT EXISTS crime_application_tracking.APPLICATION_AUDIT
(
    ID SERIAL PRIMARY KEY,
    USN INTEGER NOT NULL,
    MAAT_REF INTEGER,
    DATE_CREATED DATE,
    USER_CREATED VARCHAR(100),
    STATUS_CODE VARCHAR(20)
);
    