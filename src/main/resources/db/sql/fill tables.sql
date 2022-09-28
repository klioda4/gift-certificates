INSERT INTO tags (id, name)
VALUES (1, 'halloween'),
       (2, 'sweetness'),
       (3, 'board game'),
       (4, 'cartoon'),
       (5, 'movie'),
       (6, 'perfume'),
       (7, 'meal'),
       (8, 'holiday');

INSERT INTO gift_certificates (id, name, description, price, duration)
VALUES (1, 'Board world', 'Для заказа тематической настольной игры на тему мультфильмов', 60, 90),
       (2, 'Trick or treat', 'Для заказа тематических сладостей на Хэллоуин', 80, 14),
       (3, 'Magic of smell', 'Парфюмерия на все случаи жизни', 30, 60),
       (4, 'Magic of smell', 'Парфюмерия на все случаи жизни', 50, 60),
       (5, 'Magic of smell', 'Парфюмерия на все случаи жизни', 100, 60);

INSERT INTO gift_certificates_tags(gift_certificate_id, tag_id)
VALUES (1, 3),
       (1, 4),
       (2, 2),
       (2, 1),
       (2, 8),
       (3, 6),
       (4, 6),
       (5, 6);
