--liquibase formatted sql

--changeset klioda4:1
--validCheckSum ANY
INSERT INTO tag (id, name)
VALUES (1, 'halloween'),
       (2, 'sweetness'),
       (3, 'board game'),
       (4, 'cartoon'),
       (5, 'movie'),
       (6, 'perfume'),
       (7, 'meal'),
       (8, 'holiday'),
       (9, 'christmas');

INSERT INTO gift_certificate (id, name, description, price, duration,
                              create_date, last_update_date)
VALUES (1, 'Board world', 'Для заказа тематической настольной игры на тему мультфильмов', 60, 90,
        current_timestamp, current_timestamp),
       (2, 'Trick or treat', 'Для заказа тематических сладостей на Хэллоуин', 80, 14,
        current_timestamp, current_timestamp),
       (3, 'Magic of smell', 'Парфюмерия на все случаи жизни', 30, 60,
        current_timestamp, current_timestamp),
       (4, 'Magic of smell', 'Парфюмерия на все случаи жизни', 50, 60,
        current_timestamp, current_timestamp),
       (5, 'Magic of smell', 'Парфюмерия на все случаи жизни', 100, 60,
        current_timestamp, current_timestamp),
       (6, 'Santa Claus sweets', 'На получениа рождественских сладостей', 100, 60,
        current_timestamp, current_timestamp),
       (7, 'Starship Inc.', 'Для заказа наоборов шоколадных шатлов', 15, 15,
        current_timestamp, current_timestamp);

INSERT INTO gift_certificate_tag (gift_certificate_id, tag_id)
VALUES (1, 3),
       (1, 4),
       (2, 1),
       (2, 2),
       (2, 8),
       (3, 6),
       (4, 6),
       (5, 6),
       (6, 2),
       (6, 8),
       (6, 9),
       (7, 2);

--changeset klioda4:2
--validCheckSum ANY
INSERT INTO users (id, name)
VALUES (1, 'yoda'),
       (2, 'windu');

INSERT INTO orders (id, cost, duration, purchase_date, user_id, gift_certificate_id)
VALUES (1, 100, 60, '2022-01-01T00:00:00.000', 1, 2),
       (2, 80, 14, '2022-01-01T01:00:00.000', 1, 7),
       (3, 50, 60, '2022-01-01T02:00:00.000', 2, 1),
       (4, 50, 15, '2022-01-01T01:00:00.000', 2, 5),
       (5, 50, 15, '2022-01-01T03:00:00.000', 2, 6);
