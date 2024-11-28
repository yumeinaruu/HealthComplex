--liquibase formatted sql

--changeset yumeinaruu:3
--comment security login unique
CREATE UNIQUE INDEX security_login_uindex
    ON security (login);