--liquibase formatted sql

--changeset yumeinaruu:1
--comment table users created
CREATE TABLE users
(
    id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    name    VARCHAR(255) NOT NULL,
    created TIMESTAMP    NOT NULL,
    changed TIMESTAMP NULL
);

CREATE TABLE uservice
(
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    name     VARCHAR(255)   NOT NULL,
    price    DECIMAL(10, 2) NOT NULL,
    duration VARCHAR(255)   NOT NULL,
    url      VARCHAR(255) NULL,
    created  TIMESTAMP      NOT NULL,
    changed  TIMESTAMP NULL
);