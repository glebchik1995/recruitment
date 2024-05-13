DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS users
(
    id       BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    username VARCHAR(50)  NOT NULL,
    password VARCHAR(255) NOT NULL,
    email    VARCHAR(255) NOT NULL,
    role     VARCHAR(255) NOT NULL
);