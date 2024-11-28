--liquibase formatted sql

--changeset yumeinaruu:1
--comment table users created
create table users
(
    id      BIGINT AUTO_INCREMENT
        primary key,
    name    VARCHAR(255) not null,
    created TIMESTAMP not null,
    changed TIMESTAMP
);