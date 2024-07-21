-- -----------------------------------------------------
-- Schema test_intro_telecom
-- this script should be executed by superuser ("postgres")
-- db: PostgreSQL
-- -----------------------------------------------------
DROP DATABASE IF EXISTS test_intro_telecom;
DROP USER IF EXISTS test;
CREATE DATABASE test_intro_telecom
   ENCODING='UTF8';
CREATE USER "test";
GRANT ALL ON DATABASE test_intro_telecom to test;