create table announcements.images (
    id          bigint auto_increment primary key,
    image       blob not null,
    title       varchar(255) not null,
    extension   varchar(255) not null
);