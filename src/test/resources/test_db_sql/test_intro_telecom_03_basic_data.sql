
-- db table "role" population:

INSERT INTO role(role_type)
VALUES ('ADMIN');

INSERT INTO role(role_type)
VALUES ('CUSTOMER');



-- db table "package_plan" population:


INSERT INTO package_plan (package_code, package_name, package_description, package_price)
VALUES ('00', 'admin', 'admin package for support', '0');

INSERT INTO package_plan (package_code, package_name, package_description, package_price)
VALUES ('01', 'prepaid1', E'- calls 200 min\n- SMS 200 msg', '200');

INSERT INTO package_plan (package_code, package_name, package_description, package_price)
VALUES ('02', 'prepaid2', E'- calls 200 min\n- SMS 200 msg\n- internet 10 GB', '400');

INSERT INTO package_plan (package_code, package_name, package_description, package_price)
VALUES ('11', 'postpaid1', E'- calls 300 min\n- SMS 300 msg\n- internet 10 GB\n- international calls 200,00\n- roaming 200,00', '800');

INSERT INTO package_plan (package_code, package_name, package_description, package_price)
VALUES ('12', 'postpaid2', E'- calls 400 min\n- SMS 400 msg\n- internet 10 GB\n- social media and app 5 GB\n- international calls 200,00\n- roaming 200,00', '1000');

INSERT INTO package_plan (package_code, package_name, package_description, package_price)
VALUES ('13', 'postpaid3', E'- calls unlim\n- SMS unlim\n- internet 15 GB\n- social media and app 5 GB\n- international calls 200,00\n- roaming 200,00', '1200');

INSERT INTO package_plan (package_code, package_name, package_description, package_price)
VALUES ('14', 'postpaid4', E'- calls unlim\n- SMS unlim\n- internet unlim\n- social media and app 10 GB\n- international calls 200,00\n- roaming 200,00', '1500');



-- db table "addon" population:


INSERT INTO addon (addon_code, addon_description, addon_price)
VALUES ('ADDCLS', 'calls 100 min', '100');

INSERT INTO addon (addon_code, addon_description, addon_price)
VALUES ('ADDSMS', 'SMS 100 msg', '100');

INSERT INTO addon (addon_code, addon_description, addon_price)
VALUES ('ADDINT', 'internet 5 GB', '200');

INSERT INTO addon (addon_code, addon_description, addon_price)
VALUES ('ADDASM', 'social media and app 5 GB', '200');

INSERT INTO addon (addon_code, addon_description, addon_price)
VALUES ('ADDICL', 'international calls', '200');

INSERT INTO addon (addon_code, addon_description, addon_price)
VALUES ('ADDRMG', 'roaming', '200');



-- db table "service" population:

INSERT INTO service (service_code, service_description, service_price)
VALUES ('SDRCLS', 'record for one call', '0');

INSERT INTO service (service_code, service_description, service_price)
VALUES ('SDRSMS', 'record for one sms', '0');

INSERT INTO service (service_code, service_description, service_price)
VALUES ('SDRINT', 'internet consumption for one session', '0');

INSERT INTO service (service_code, service_description, service_price)
VALUES ('SDRASM', 'social media and app consumption for one session', '0');

INSERT INTO service (service_code, service_description, service_price)
VALUES ('SDRICLCZ1', 'record for international call in zone 1', '15');

INSERT INTO service (service_code, service_description, service_price)
VALUES ('SDRICLCZ2', 'record for international call in zone 2', '20');

INSERT INTO service (service_code, service_description, service_price)
VALUES ('SDRICLSZ1', 'record for international sms in zone 1', '5');

INSERT INTO service (service_code, service_description, service_price)
VALUES ('SDRICLSZ2', 'record for international sms in zone 2', '8');

INSERT INTO service (service_code, service_description, service_price)
VALUES ('SDRRMGCZ1', 'record for outcoming call in roaming in zone 1', '4');

INSERT INTO service (service_code, service_description, service_price)
VALUES ('SDRRMGCZ2', 'record for outcoming call in roaming in zone 2', '8');

INSERT INTO service (service_code, service_description, service_price)
VALUES ('SDRRMGSZ1', 'record for outcoming sms in roaming in zone 1', '2');

INSERT INTO service (service_code, service_description, service_price)
VALUES ('SDRRMGSZ2', 'record for outcoming sms in roaming in zone 2', '4');

INSERT INTO service (service_code, service_description, service_price)
VALUES ('SDRRMGINT', 'internet consumption of 1MB in roaming', '0.1');
















