SELECT setval('gift_certificates_id_seq', (SELECT MAX(id) FROM gift_certificates));
SELECT setval('tags_id_seq', (SELECT MAX(id) FROM tags));