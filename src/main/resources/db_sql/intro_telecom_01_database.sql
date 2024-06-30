-- -----------------------------------------------------
-- Schema intro_telecom
-- this script should be executed by superuser ("postgres")
-- db: PostgreSQL
-- -----------------------------------------------------
CREATE DATABASE intro_telecom 
   ENCODING='UTF8';
CREATE USER "telecom" WITH PASSWORD 'telecom';
GRANT ALL ON DATABASE intro_telecom to telecom;
