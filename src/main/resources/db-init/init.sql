DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS users
(
    id       BIGSERIAL PRIMARY KEY,
    name     varchar(255) not null,
    username VARCHAR(50)  NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role     VARCHAR(50)  NOT NULL
);

CREATE TABLE IF NOT EXISTS job_request
(
    id           BIGSERIAL PRIMARY KEY,
    status       VARCHAR(50) NOT NULL,
    hr_id        BIGINT      NOT NULL,
    candidate_id BIGINT      NOT NULL,
    vacancy_id   BIGINT      NOT NULL,
    description  VARCHAR(1000),
    FOREIGN KEY (hr_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (candidate_id) REFERENCES candidate (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (vacancy_id) REFERENCES vacancy (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS job_request_files
(
    job_request_id BIGINT       NOT NULL,
    file           VARCHAR(255) NOT NULL,
    FOREIGN KEY (job_request_id) REFERENCES job_request (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS candidate
(
    id              BIGSERIAL PRIMARY KEY,
    name            VARCHAR(255)  NOT NULL,
    surname         VARCHAR(255)  NOT NULL,
    age             INT           NOT NULL,
    email           VARCHAR(255)  NOT NULL,
    phone           VARCHAR(20)   NOT NULL,
    position        VARCHAR(255)  NOT NULL,
    exp             VARCHAR(1000) NOT NULL,
    tech_skill      VARCHAR(255)  NOT NULL,
    language_skill  VARCHAR(255)  NOT NULL,
    expected_salary INT           NOT NULL

);

CREATE TABLE IF NOT EXISTS vacancy
(
    id                BIGSERIAL PRIMARY KEY,
    requirement       VARCHAR(255) NOT NULL,
    description       TEXT         NOT NULL,
    start_working_day TIME         NOT NULL,
    end_working_day   VARCHAR(50)  NOT NULL,
    salary            VARCHAR(50)  NOT NULL,
    created_date      DATE         NOT NULL,
    created_time      TIMESTAMP    NOT NULL,
    active            BOOLEAN      NOT NULL,
    recruiter_id      BIGINT       NOT NULL
);


CREATE TABLE IF NOT EXISTS chat_message
(
    id          BIGSERIAL PRIMARY KEY,
    sender_id   BIGINT,
    receiver_id BIGINT,
    text        TEXT,
    sent_date   DATE NOT NULL,
    sent_time   TIME NOT NULL,
    FOREIGN KEY (sender_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (receiver_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE
);


CREATE INDEX idx_status ON candidate (position);
CREATE INDEX idx_status ON candidate (department);
CREATE INDEX idx_status ON candidate (age);
