create table if not exists deliveries
(
    id               bigserial
        primary key,
    address          varchar(255) not null,
    city             varchar(255) not null,
    delivery_options varchar(255),
    floor            varchar(255) not null,
    info             varchar(255),
    postal_code      varchar(255) not null
);

create table if not exists meal_categories
(
    id       bigserial
        primary key,
    image varchar(255),
    name     varchar(255) not null
);

create table if not exists meals
(
    id               bigserial
        primary key,
    description      varchar(255),
    image         varchar(255),
    name             varchar(255)     not null,
    price            double precision not null,
    meal_category_id bigint           not null
        constraint fksvgs7sy4exg0jqucoqn7tw390
            references meal_categories
);

create table if not exists users
(
    id             bigserial
        primary key,
    email          varchar(255) not null
        constraint uk6dotkott2kjsp8vw4d0m25fb7
            unique,
    email_verified boolean,
    first_name     varchar(255),
    image_url      varchar(255),
    last_name      varchar(255),
    password       varchar(255),
    provider       varchar(255) not null,
    provider_id    varchar(255),
    role           varchar(255),
    username       varchar(255)
);

create table if not exists orders
(
    id           bigserial
        primary key,
    order_date   timestamp(6),
    order_status varchar(255),
    delivery_date    timestamp(6),
    delivery_id  bigint
        constraint fk8w9m21riko8j8eit0yvog02nr
            references deliveries,
    user_id      bigint not null
        constraint fk32ql8ubntj5uh44ph9659tiih
            references users
);

create table if not exists meal_orders
(
    id       bigserial
        primary key,
    price    double precision not null,
    quantity integer          not null,
    meal_id  bigint           not null
        constraint fks28nuf3sou4u4j47m43fmd34h
            references meals,
    order_id bigint           not null
        constraint fk18b7yegx1ak7s7ti8i62tke1f
            references orders
);

create table if not exists tokens
(
    id          bigserial
        primary key,
    expiry_time bigint,
    token       varchar(255),
    user_id     bigint
        constraint fk2dylsfo39lgjyqml2tbe0b0ss
            references users
);

