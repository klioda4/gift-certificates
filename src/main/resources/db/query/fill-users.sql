--liquibase formatted sql

--changeset klioda4:1
--validCheckSum ANY
INSERT INTO users (id, name)
VALUES (1, 'yoda'),
       (2, 'windu');
