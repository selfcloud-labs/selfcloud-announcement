create table announcements.announcements_images (
    image_id           bigint unique not null,
    announcement_id    bigint not null,
    foreign key (image_id) references announcements.images(id),
    foreign key (announcement_id) references announcements.announcements(id)
);