--liquibase formatted sql

--changeset yumeinaruu:4
--comment create relation many to many table
create table user_service
(
    id         bigint auto_increment,
    user_id    bigint not null,
    service_id bigint not null,
    constraint user_service_pk
        primary key (id)
);