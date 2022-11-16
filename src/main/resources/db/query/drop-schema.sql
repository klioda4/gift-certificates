--liquibase formatted sql

--changeset klioda4:1 runAlways
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS gift_certificate_tag;
DROP TABLE IF EXISTS gift_certificate;
DROP TABLE IF EXISTS tag;
DROP TABLE IF EXISTS users;
