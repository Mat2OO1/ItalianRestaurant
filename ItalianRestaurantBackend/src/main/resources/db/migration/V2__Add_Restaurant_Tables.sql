create table if not exists tables
(
    id      bigserial primary key,
    number  int,
    seats_nr   int
);

alter table orders
    add column table_id bigint constraint fk8w9m21riko8j8eit0yj47fi48
        references tables (id);