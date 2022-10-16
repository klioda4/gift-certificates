--liquibase formatted sql

--changeset klioda4:1
CREATE TABLE IF NOT EXISTS tag (
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(25) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS gift_certificate (
    id               BIGSERIAL PRIMARY KEY,
    name             VARCHAR(30)   NOT NULL,
    description      VARCHAR(255)  NOT NULL,
    price            DECIMAL(7, 2) NOT NULL,
    duration         INT           NOT NULL,
    create_date      TIMESTAMP     NOT NULL,
    last_update_date TIMESTAMP     NOT NULL,

    CHECK (price >= 0),
    CHECK (duration >= 0)
);

CREATE TABLE IF NOT EXISTS gift_certificate_tag (
    gift_certificate_id BIGINT NOT NULL,
    tag_id              BIGINT NOT NULL,

    PRIMARY KEY (gift_certificate_id, tag_id),
    FOREIGN KEY (gift_certificate_id) REFERENCES gift_certificate (id),
    FOREIGN KEY (tag_id) REFERENCES tag (id)
);
