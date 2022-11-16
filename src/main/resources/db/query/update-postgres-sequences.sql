SELECT setval('gift_certificate_id_seq', (SELECT MAX(id) FROM gift_certificate));
SELECT setval('tag_id_seq', (SELECT MAX(id) FROM tag));
SELECT setval('users_id_seq', (SELECT MAX(id) FROM users));
