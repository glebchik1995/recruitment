DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS users
(
    id       BIGSERIAL PRIMARY KEY,
    username VARCHAR(50)  NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email    VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE user_roles
(
    user_id BIGINT,
    roles   VARCHAR(255),
    PRIMARY KEY (user_id, roles),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS hr
(
    id      BIGSERIAL PRIMARY KEY,
    name    VARCHAR(255),
    surname VARCHAR(255),
    email   VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS interviewer
(
    id      BIGSERIAL PRIMARY KEY,
    name    VARCHAR(255),
    surname VARCHAR(255),
    email   VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS candidate
(
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    age        INT          NOT NULL,
    email      VARCHAR(255) NOT NULL,
    phone      VARCHAR(20)  NOT NULL,
    position   VARCHAR(255) NOT NULL,
    department VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS job_requests
(
    id           BIGSERIAL PRIMARY KEY,
    status       VARCHAR(50) NOT NULL,
    hr_id        INT         NOT NULL,
    candidate_id INT         NOT NULL,
    description  varchar(1000),
    FOREIGN KEY (hr_id) REFERENCES hr (id) ON UPDATE CASCADE,
    FOREIGN KEY (candidate_id) REFERENCES candidate (id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS job_request_files
(
    id             BIGSERIAL PRIMARY KEY,
    name           VARCHAR(255) NOT NULL,
    path           VARCHAR(255) NOT NULL,
    size           INT          NOT NULL,
    fileType       VARCHAR(255) NOT NULL,
    job_request_id BIGINT       NOT NULL,
    FOREIGN KEY (job_request_id) REFERENCES job_requests (id)
);