CREATE TABLE IF NOT EXISTS tags (
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(25) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS gift_certificates (
    id               BIGSERIAL PRIMARY KEY,
    name             VARCHAR(30)   NOT NULL,
    description      VARCHAR(255)  NOT NULL,
    price            DECIMAL(7, 2) NOT NULL,
    duration         INT           NOT NULL,
    create_date      TIMESTAMP     NOT NULL DEFAULT NOW(),
    last_update_date TIMESTAMP     NOT NULL DEFAULT NOW(),

    CHECK (price >= 0),
    CHECK (duration >= 0)
);

CREATE TABLE IF NOT EXISTS gift_certificates_tags (
    gift_certificate_id BIGINT NOT NULL,
    tag_id              BIGINT NOT NULL,

    PRIMARY KEY (gift_certificate_id, tag_id),
    FOREIGN KEY (gift_certificate_id) REFERENCES gift_certificates (id),
    FOREIGN KEY (tag_id) REFERENCES tags (id)
);
