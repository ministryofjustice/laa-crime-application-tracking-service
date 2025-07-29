--liquibase formatted sql
--changeset Josh Hunt:06-create-application-history-table.sql

CREATE TABLE IF NOT EXISTS crime_application_tracking.APPLICATION_HISTORY
(	
    ID SERIAL PRIMARY KEY,
    USN INTEGER NOT NULL,
    REP_ID INTEGER,
    ACTION VARCHAR(250),
    KEY_ID INTEGER,
    DATE_CREATED DATE NOT NULL DEFAULT CURRENT_DATE,
    USER_CREATED VARCHAR(100) NOT NULL
);
    