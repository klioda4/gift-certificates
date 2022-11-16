--liquibase formatted sql

--changeset klioda4:1
CREATE TABLE tag (
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(25) NOT NULL UNIQUE
);

CREATE TABLE gift_certificate (
    id               BIGSERIAL PRIMARY KEY,
    name             VARCHAR(30)   NOT NULL,
    description      VARCHAR(255)  NOT NULL,
    price            DECIMAL(7, 2) NOT NULL,
    duration         INT           NOT NULL,
    create_date      TIMESTAMP     NOT NULL,
    last_update_date TIMESTAMP     NOT NULL
);

CREATE TABLE gift_certificate_tag (
    gift_certificate_id BIGINT NOT NULL,
    tag_id              BIGINT NOT NULL,

    PRIMARY KEY (gift_certificate_id, tag_id),
    CONSTRAINT gift_certificate_tag_gift_certificate_id_fkey
        FOREIGN KEY (gift_certificate_id) REFERENCES gift_certificate (id)
            ON DELETE CASCADE,
    CONSTRAINT gift_certificate_tag_tag_id_fkey
        FOREIGN KEY (tag_id) REFERENCES tag (id)
            ON DELETE CASCADE
);

CREATE TABLE users (
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(15) NOT NULL UNIQUE
);

CREATE TABLE orders (
    id                  BIGINT PRIMARY KEY,
    cost                DECIMAL(7, 2) NOT NULL,
    duration            INT           NOT NULL,
    purchase_date       TIMESTAMP     NOT NULL,
    gift_certificate_id BIGINT        NOT NULL,
    user_id             BIGINT        NOT NULL,

    FOREIGN KEY (gift_certificate_id) REFERENCES gift_certificate (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE commit_log (
    id          BIGSERIAL PRIMARY KEY,
    entity_id   BIGINT      NOT NULL,
    entity_name VARCHAR(20) NOT NULL,
    http_method VARCHAR(10) NOT NULL,
    body        VARCHAR(255),
    status      VARCHAR(10) NOT NULL
);

--changeset klioda4:2.1 dbms:postgresql
CREATE SEQUENCE order_id_seq
    INCREMENT BY 3
    OWNED BY orders.id;

SELECT setval('tag_id_seq', 1);
SELECT setval('gift_certificate_id_seq', 1);
SELECT setval('users_id_seq', 1);
SELECT setval('commit_log_id_seq', 1);
