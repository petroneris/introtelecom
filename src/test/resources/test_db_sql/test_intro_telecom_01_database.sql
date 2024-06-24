-- -----------------------------------------------------
-- Schema test_intro_telecom
-- this script should be executed by superuser ("postgres")
-- db: PostgreSQL
-- -----------------------------------------------------
CREATE DATABASE test_intro_telecom
   ENCODING='UTF8';
CREATE USER "test";
GRANT ALL ON DATABASE test_intro_telecom to test;