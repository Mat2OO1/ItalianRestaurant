create table if not exists tables
(
    id       bigserial primary key,
    number   int          not null unique,
    seats_nr int          not null,
    status   varchar(255) not null
);

alter table orders
    add column table_id bigint
        constraint fk8w9m21riko8j8eit0yj47fi48
            references tables (id);