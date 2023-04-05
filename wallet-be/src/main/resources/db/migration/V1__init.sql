create table if not exists wallets
(
    id              uuid not null primary key,
    balance         numeric(12, 2),
    currency        varchar(255),
    name            varchar(255),
    user_id         uuid not null
);

create table if not exists users
(
    id              uuid not null primary key,
    username        varchar(255),
    password        varchar(255),
    role            varchar(255)
);


create extension if not exists "uuid-ossp";

insert into users(id, username, password, role)
values (uuid_generate_v4(), 'test', '$2a$12$N6kEJ6nw87HLmtp0LX5VGufj5jVDCNF34JUkXe6qPmUAixnvVsSVG', 'SUPERUSER')