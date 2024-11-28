--liquibase formatted sql

--changeset yumeinaruu:4
--comment create uservice table
create table uservice
(
    id       bigint auto_increment,
    name     varchar(256) not null,
    price    double       not null,
    duration varchar(64)  null,
    url      varchar(256) null,
    created  timestamp    not null,
    changed  timestamp    null,
    constraint uservice_pk
        primary key (id)
);