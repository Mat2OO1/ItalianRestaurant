create table deliveries
(
    id               bigserial primary key,
    address          varchar(255) not null,
    city             varchar(255) not null,
    delivery_date    timestamp(6),
    delivery_options varchar(255),
    floor            varchar(255) not null,
    info             varchar(255),
    postal_code      varchar(255) not null
);

create table meal_categories
(
    id       bigserial primary key,
    img_path varchar(255),
    name     varchar(255) not null
);

create table meals
(
    id               bigserial
        primary key,
    description      varchar(255),
    img_path         varchar(255),
    name             varchar(255)     not null,
    price            double precision not null,
    meal_category_id bigint           not null references meal_categories
);

create table users
(
    id         serial primary key,
    email      varchar(255) unique,
    first_name varchar(255),
    last_name  varchar(255),
    password   varchar(255),
    role       varchar(255)
);

create table orders
(
    id           bigserial primary key,
    order_date   timestamp(6),
    order_status varchar(255),
    delivery_id  bigint references deliveries,
    user_id      integer not null references users
);

create table meal_orders
(
    id       bigserial
        primary key,
    price    double precision not null,
    quantity integer          not null,
    meal_id  bigint           not null references meals,
    order_id bigint           not null references orders
);

create table tokens
(
    id          bigserial
        primary key,
    expiry_time bigint,
    token       varchar(255),
    user_id     integer references users
);
