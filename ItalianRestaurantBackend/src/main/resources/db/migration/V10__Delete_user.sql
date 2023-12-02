alter table orders
    alter column user_id drop not null;

alter table orders
    drop constraint if exists fk32ql8ubntj5uh44ph9659tiih,
    add constraint fk32ql8ubntj5uh44ph9659tiih
        foreign key (user_id)
            references users (id)
            on delete set null;

alter table reservations
    drop constraint if exists adfsadsf1321,
    add constraint adfsadsf1321 foreign key (user_id)
            references users (id)
            on delete cascade;

alter table tokens
    drop constraint if exists fk2dylsfo39lgjyqml2tbe0b0ss,
    add constraint fk2dylsfo39lgjyqml2tbe0b0ss foreign key (user_id)
            references users (id)
            on delete cascade;