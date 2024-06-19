DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS users
(
    id       BIGSERIAL PRIMARY KEY,
    name     varchar(255) NOT NULL,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role     VARCHAR      NOT NULL
);

CREATE TABLE IF NOT EXISTS candidate
(
    id              BIGSERIAL PRIMARY KEY,
    name            VARCHAR(255)  NOT NULL,
    surname         VARCHAR(255)  NOT NULL,
    age             INT           NOT NULL,
    email           VARCHAR(255)  NOT NULL UNIQUE,
    phone           VARCHAR(20)   NOT NULL,
    position        VARCHAR(255)  NOT NULL,
    exp             VARCHAR(1000) NOT NULL,
    tech_skill      VARCHAR(255)  NOT NULL,
    language_skill  VARCHAR(255)  NOT NULL,
    expected_salary INT           NOT NULL,
    hr_id           BIGINT        NOT NULL,
    FOREIGN KEY (hr_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE

);

CREATE TABLE IF NOT EXISTS vacancy
(
    id                BIGSERIAL PRIMARY KEY,
    requirement       TEXT                   NOT NULL,
    title             VARCHAR(255)           NOT NULL,
    position          VARCHAR(255)           NOT NULL,
    description       TEXT                   NOT NULL,
    start_working_day TIME WITH TIME ZONE  NOT NULL,
    end_working_day   TIME WITHOUT TIME ZONE NOT NULL,
    salary            INT                    NOT NULL,
    created_date      DATE                   NOT NULL,
    created_time      TIME                   NOT NULL,
    active            BOOLEAN                NOT NULL,
    recruiter_id      BIGINT                 NOT NULL,
    FOREIGN KEY (recruiter_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS chat_message
(
    id           BIGSERIAL PRIMARY KEY,
    sender_id    BIGINT,
    recipient_id BIGINT,
    text         TEXT,
    sent_date    DATE      NOT NULL,
    sent_time    TIMESTAMP NOT NULL,
    FOREIGN KEY (sender_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (recipient_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS job_request
(
    id           BIGSERIAL PRIMARY KEY,
    status       VARCHAR NOT NULL,
    hr_id        BIGINT  NOT NULL,
    candidate_id BIGINT  NOT NULL,
    recruiter_id BIGINT  NOT NULL,
    description  TEXT,
    FOREIGN KEY (hr_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (candidate_id) REFERENCES candidate (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (recruiter_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS job_request_files
(
    job_request_id BIGINT  NOT NULL,
    file           VARCHAR NOT NULL,
    FOREIGN KEY (job_request_id) REFERENCES job_request (id) ON DELETE CASCADE
);

