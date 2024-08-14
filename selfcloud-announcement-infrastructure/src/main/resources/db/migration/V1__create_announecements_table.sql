create table announcements.announcements (
    id                            bigint auto_increment primary key,
    title                         varchar(255) not null,
    short_description             varchar(63) not null,
    long_description              varchar(255) not null,
    category                      varchar(255) not null,
    subcategory                   varchar(255) not null,
    price                         double not null,
    customer_id                   bigint not null,
    email                         varchar(255) not null,
    state                         varchar(255) not null,
    available_patches_number      bigint not null,
    estimated_delivery_Time       timestamp,
    support_after_implementation  timestamp,
    created_at                    timestamp,
    modified_at                   timestamp
);

