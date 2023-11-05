create table if not exists reservations
(
    id                     bigserial primary key,
    reservation_date_start TIMESTAMP,
    reservation_date_end   TIMESTAMP,
    user_id                int
        constraint adfsadsf1321 REFERENCES users,
    table_id               int
        constraint sa312eqw142eqw REFERENCES tables
);