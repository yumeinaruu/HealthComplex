--liquibase formatted sql

--changeset yumeinaruu:2
--comment table security created
create table security
(
    id       bigint auto_increment
        primary key,
    login    varchar(255) not null,
    password varchar(255) not null,
    role     varchar(255) not null,
    user_id  bigint not null,
    foreign key (user_id) references users(id)
        on update cascade on delete cascade
);

