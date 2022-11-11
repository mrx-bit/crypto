create table crypto_config
(
    id          serial
        constraint crypto_config_pk
            primary key,
    crypto_name varchar(128)
);

create unique index crypto_config_id_uindex
    on crypto_config (id);

create table crypto
(
    id     bigint not null
        primary key,
    date   bigint,
    price  double precision,
    symbol varchar(255)
);

alter table crypto
    owner to postgres;

create table crypto_type
(
    id           bigint not null
        primary key,
    active       boolean,
    file_path    varchar(255),
    last_updated bigint,
    type         varchar(255)
);

alter table crypto_type
    owner to postgres;

