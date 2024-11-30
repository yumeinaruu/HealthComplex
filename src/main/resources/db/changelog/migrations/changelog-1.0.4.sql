--liquibase formatted sql

--changeset yumeinaruu:3
--comment date time model
CREATE TABLE date_time_model
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    start        TIMESTAMP NOT NULL,
    u_service_id BIGINT    NOT NULL,
    CONSTRAINT FK_u_service FOREIGN KEY (u_service_id) REFERENCES uservice (id) ON DELETE CASCADE
);