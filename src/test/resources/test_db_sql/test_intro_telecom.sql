
-- -----------------------------------------------------
-- Schema test_intro_telecom
-- -----------------------------------------------------
CREATE DATABASE test_intro_telecom 
   ENCODING='UTF8';
CREATE USER "test";
GRANT ALL ON DATABASE test_intro_telecom to test;

-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS package_plan (
  package_code VARCHAR(45) PRIMARY KEY NOT NULL,
  package_name VARCHAR(45) UNIQUE NOT NULL,			
  package_description VARCHAR(250) NOT NULL,
  package_price NUMERIC(10,2) NOT NULL
);

CREATE TABLE IF NOT EXISTS phone (
  phone_number VARCHAR(45) PRIMARY KEY NOT NULL,
  package_code VARCHAR(45) NOT NULL,		
  phonestart_datetime TIMESTAMP NOT NULL,
  phone_status VARCHAR(45) NOT NULL,
  note VARCHAR(250) NOT NULL,
  CONSTRAINT fk_package_plan
    FOREIGN KEY (package_code)
    REFERENCES package_plan (package_code)
    ON DELETE NO ACTION
);

CREATE SEQUENCE IF NOT EXISTS admin_seq;
CREATE TABLE IF NOT EXISTS admin (
  admin_id INT PRIMARY KEY NOT NULL DEFAULT nextval('admin_seq'),
  apersonal_number VARCHAR(45) UNIQUE NOT NULL,
  afirst_name VARCHAR(45) NOT NULL,
  alast_name VARCHAR(45) NOT NULL,
  a_email VARCHAR(45) UNIQUE NOT NULL,				
  phone_number VARCHAR(45) NOT NULL,
  CONSTRAINT fk_phone
    FOREIGN KEY (phone_number)
    REFERENCES phone (phone_number)
    ON DELETE NO ACTION
);

CREATE SEQUENCE IF NOT EXISTS customer_seq;
CREATE TABLE IF NOT EXISTS customer (
  customer_id INT PRIMARY KEY NOT NULL DEFAULT nextval('customer_seq'),
  cpersonal_number VARCHAR(45) UNIQUE NOT NULL,
  cfirst_name VARCHAR(45) NOT NULL,
  clast_name VARCHAR(45) NOT NULL,
  c_email VARCHAR(45) UNIQUE NOT NULL,				
  address VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS customer_phone (
  customer_id INT NOT NULL,
  phone_number VARCHAR(45) NOT NULL,
  CONSTRAINT fk_customer
    FOREIGN KEY (customer_id)
    REFERENCES customer (customer_id)
    ON DELETE NO ACTION,   
  CONSTRAINT fk_phone
    FOREIGN KEY (phone_number)
    REFERENCES phone (phone_number)
    ON DELETE NO ACTION
);

CREATE SEQUENCE IF NOT EXISTS role_seq;
CREATE TABLE IF NOT EXISTS role (
  role_id INT PRIMARY KEY NOT NULL DEFAULT nextval('role_seq'),
  role_type VARCHAR(45) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS user_data (
  phone_number VARCHAR(45) NOT NULL,
  username VARCHAR(45) UNIQUE NOT NULL,
  password VARCHAR(100) NOT NULL,
  role_id INT NOT NULL,
  user_status VARCHAR(45) NOT NULL,
  CONSTRAINT fk_phone
    FOREIGN KEY (phone_number)
    REFERENCES phone (phone_number)
    ON DELETE NO ACTION,
  CONSTRAINT fk_role
    FOREIGN KEY (role_id)
    REFERENCES role (role_id)
    ON DELETE NO ACTION    
);

CREATE SEQUENCE IF NOT EXISTS package_frame_seq;
CREATE TABLE IF NOT EXISTS package_frame (
  packfr_id INT PRIMARY KEY NOT NULL DEFAULT nextval('package_frame_seq'),
  phone_number VARCHAR(45) NOT NULL,
  packfr_cls INT NOT NULL,
  packfr_sms INT NOT NULL,
  packfr_int NUMERIC(8,2) NOT NULL,
  packfr_asm NUMERIC(8,2) NOT NULL,
  packfr_icl NUMERIC(10,2) NOT NULL,
  packfr_rmg NUMERIC(10,2) NOT NULL,
  packfr_startdatetime TIMESTAMP NOT NULL,
  packfr_enddatetime TIMESTAMP NOT NULL,
  packfr_status VARCHAR(45) NOT NULL,
  CONSTRAINT fk_phone
    FOREIGN KEY (phone_number)
    REFERENCES phone (phone_number)
    ON DELETE NO ACTION 
);

CREATE TABLE IF NOT EXISTS addon (
  addon_code VARCHAR(45) PRIMARY KEY NOT NULL,  
  addon_description VARCHAR(250) NOT NULL,
  addon_price NUMERIC(10,2) NOT NULL
);
 
CREATE SEQUENCE IF NOT EXISTS addon_frame_seq;	
CREATE TABLE IF NOT EXISTS addon_frame (
  addfr_id INT PRIMARY KEY NOT NULL DEFAULT nextval('addon_frame_seq'),
  phone_number VARCHAR(45) NOT NULL,
  addon_code VARCHAR(45) NOT NULL,
  addfr_cls INT NOT NULL,
  addfr_sms INT NOT NULL,
  addfr_int NUMERIC(8,2) NOT NULL,
  addfr_asm NUMERIC(8,2) NOT NULL,
  addfr_icl NUMERIC(10,2) NOT NULL,
  addfr_rmg NUMERIC(10,2) NOT NULL,
  addfr_startdatetime TIMESTAMP NOT NULL,
  addfr_enddatetime TIMESTAMP NOT NULL,
  addfr_status VARCHAR(45) NOT NULL,
  CONSTRAINT fk_phone
    FOREIGN KEY (phone_number)
    REFERENCES phone (phone_number)
    ON DELETE NO ACTION,
  CONSTRAINT fk_addon
    FOREIGN KEY (addon_code)
    REFERENCES addon (addon_code)
    ON DELETE NO ACTION      
);

CREATE TABLE IF NOT EXISTS service (
  service_code VARCHAR(45) PRIMARY KEY NOT NULL,  
  service_description VARCHAR(250) UNIQUE NOT NULL,
  service_price NUMERIC(10,2) NOT NULL
);

CREATE SEQUENCE IF NOT EXISTS sdr_seq;	
CREATE TABLE IF NOT EXISTS sdr (
  sdr_id INT PRIMARY KEY NOT NULL DEFAULT nextval('sdr_seq'),
  phone_number VARCHAR(45) NOT NULL,  
  service_code VARCHAR(45) NOT NULL,
  called_number VARCHAR(45) NOT NULL,
  sdr_startdatetime TIMESTAMP NOT NULL,
  sdr_enddatetime TIMESTAMP NOT NULL,
  duration INT NOT NULL,
  msg_amount INT NOT NULL,
  mb_amount NUMERIC(8,2) NOT NULL,
  sdr_note varchar(45) NOT NULL,
  CONSTRAINT fk_phone
    FOREIGN KEY (phone_number)
    REFERENCES phone (phone_number)
    ON DELETE NO ACTION,
  CONSTRAINT fk_service
    FOREIGN KEY (service_code)
    REFERENCES service (service_code)
    ON DELETE NO ACTION    
);

CREATE SEQUENCE IF NOT EXISTS monthlybill_facts_seq;
CREATE TABLE IF NOT EXISTS monthlybill_facts (  
  monthlybill_id INT PRIMARY KEY NOT NULL DEFAULT nextval('monthlybill_facts_seq'),
  phone_number VARCHAR(45) NOT NULL,
  year_month DATE NOT NULL,
  package_price NUMERIC(10,2) NOT NULL,
  addcls_price NUMERIC(10,2) NOT NULL,
  addsms_price NUMERIC(10,2) NOT NULL,
  addint_price NUMERIC(10,2) NOT NULL,
  addasm_price NUMERIC(10,2) NOT NULL,
  addicl_price NUMERIC(10,2) NOT NULL,
  addrmg_price NUMERIC(10,2) NOT NULL,
  monthlybill_totalprice NUMERIC(10,2) NOT NULL,
  monthlybill_datetime TIMESTAMP NOT NULL,
  CONSTRAINT fk_phone
    FOREIGN KEY (phone_number)
    REFERENCES phone (phone_number)
    ON DELETE NO ACTION
);
