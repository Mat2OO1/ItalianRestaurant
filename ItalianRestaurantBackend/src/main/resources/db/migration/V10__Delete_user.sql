alter table orders
    alter column user_id drop not null;

alter table orders
    drop constraint if exists fk32ql8ubntj5uh44ph9659tiih,
    add constraint fk32ql8ubntj5uh44ph9659tiih
        foreign key (user_id)
            references users (id)
            on delete set null;

alter table reservations
    add constraint fk32qljf84jfuuh44ph9659tiih foreign key (user_id)
            references users (id)
            on delete cascade;

alter table tokens
    add constraint fk32qljf8sdf34544ph9659tiih foreign key (user_id)
            references users (id)
            on delete cascade;