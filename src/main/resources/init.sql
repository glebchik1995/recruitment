DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS users
(
    id       BIGSERIAL PRIMARY KEY,
    name     varchar(255) not null,
    username VARCHAR(50)  NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE user_roles
(
    user_id BIGINT,
    role    VARCHAR(255),
    PRIMARY KEY (user_id, role),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE NO ACTION
);

CREATE TABLE IF NOT EXISTS job_request
(
    id           BIGSERIAL PRIMARY KEY,
    status       VARCHAR(50) NOT NULL,
    hr_id        INT         NOT NULL,
    candidate_id INT         NOT NULL,
    description  varchar(1000),
    FOREIGN KEY (hr_id) REFERENCES users (id) ON UPDATE CASCADE,
    FOREIGN KEY (candidate_id) REFERENCES candidate (id) ON DELETE CASCADE ON UPDATE NO ACTION

);

CREATE TABLE IF NOT EXISTS candidate
(
    id             BIGSERIAL PRIMARY KEY,
    job_request_id BIGINT       NOT NULL,
    name           VARCHAR(255) NOT NULL,
    surname        VARCHAR(255) NOT NULL,
    age            INT          NOT NULL,
    email          VARCHAR(255) NOT NULL,
    phone          VARCHAR(20)  NOT NULL,
    position       VARCHAR(255) NOT NULL,
    department     VARCHAR(255) NOT NULL,
    FOREIGN KEY (job_request_id) REFERENCES job_request (id) ON DELETE CASCADE ON UPDATE NO ACTION
);

CREATE TABLE IF NOT EXISTS job_request_files
(
    job_request_id BIGINT       NOT NULL,
    file           VARCHAR(255) NOT NULL,
    FOREIGN KEY (job_request_id) REFERENCES job_request (id) ON DELETE CASCADE
);