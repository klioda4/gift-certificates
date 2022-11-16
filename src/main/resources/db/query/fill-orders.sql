--liquibase formatted sql

--changeset klioda4:1
--validCheckSum ANY
INSERT INTO orders (id, cost, duration, purchase_date, user_id, gift_certificate_id)
VALUES (1, 100, 60, '2022-01-01T00:00:00.000', 1, 2),
       (2, 80, 14, '2022-01-01T01:00:00.000', 1, 7),
       (3, 50, 60, '2022-01-01T02:00:00.000', 2, 1),
       (4, 50, 15, '2022-01-01T01:00:00.000', 2, 5),
       (5, 50, 15, '2022-01-01T03:00:00.000', 2, 6);
