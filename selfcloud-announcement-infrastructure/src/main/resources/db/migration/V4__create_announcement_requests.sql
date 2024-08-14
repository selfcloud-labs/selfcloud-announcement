CREATE TABLE requests (
    id                      BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id             BIGINT NOT NULL,
    announcement_id         BIGINT NOT NULL,
    state                   varchar(255) not null,
    CONSTRAINT fk_announcement_id FOREIGN KEY (announcement_id) REFERENCES announcements(id)
);