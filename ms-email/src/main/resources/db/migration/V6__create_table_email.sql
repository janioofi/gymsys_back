CREATE TABLE email
(
    id_email        UUID NOT NULL,
    email_from      VARCHAR(255),
    email_to        VARCHAR(255),
    subject         VARCHAR(255),
    text            TEXT,
    send_date_email TIMESTAMP WITHOUT TIME ZONE,
    status_email    VARCHAR(255),
    CONSTRAINT pk_email PRIMARY KEY (id_email)
);