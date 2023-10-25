alter table meal_orders drop column price;

create table if not exists payments (
    id bigserial primary key,
    session_id varchar(255) not null,
    is_paid boolean not null,
    amount bigint,
    payment_type varchar(255),
    created_at timestamp not null default now()
);

alter table orders add column payment_id integer references payments(id);