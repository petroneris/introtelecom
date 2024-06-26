-- db table "phone" population:


INSERT INTO phone (phone_number, package_code, phonestart_datetime, phone_status, note)
VALUES ('0770000001', '00', '2021-01-27 09:30:17', 'ACTIVE', 'Admin phone for support');

INSERT INTO phone (phone_number, package_code, phonestart_datetime, phone_status, note)
VALUES ('0770000002', '00', '2021-01-27 09:33:17', 'ACTIVE', 'Admin phone for support');


INSERT INTO phone (phone_number, package_code, phonestart_datetime, phone_status, note)
VALUES ('0720123763', '01', '2021-01-27 09:31:17', 'ACTIVE', '');

INSERT INTO phone (phone_number, package_code, phonestart_datetime, phone_status, note)
VALUES ('0739887657', '01', '2021-01-27 09:32:17', 'ACTIVE', '');

INSERT INTO phone (phone_number, package_code, phonestart_datetime, phone_status, note)
VALUES ('0747634418', '01', '2021-01-27 09:33:17', 'ACTIVE', '');

INSERT INTO phone (phone_number, package_code, phonestart_datetime, phone_status, note)
VALUES ('0718995435', '01', '2021-01-27 09:34:17', 'ACTIVE', '');

INSERT INTO phone (phone_number, package_code, phonestart_datetime, phone_status, note)
VALUES ('0729834565', '02', '2021-02-27 09:31:17', 'ACTIVE', '');

INSERT INTO phone (phone_number, package_code, phonestart_datetime, phone_status, note)
VALUES ('0759887657', '02', '2021-03-27 09:32:17', 'ACTIVE', '');

INSERT INTO phone (phone_number, package_code, phonestart_datetime, phone_status, note)
VALUES ('0767634418', '02', '2021-04-27 09:33:17', 'ACTIVE', '');

INSERT INTO phone (phone_number, package_code, phonestart_datetime, phone_status, note)
VALUES ('0788995435', '02', '2021-05-27 09:34:17', 'ACTIVE', '');

INSERT INTO phone (phone_number, package_code, phonestart_datetime, phone_status, note)
VALUES ('0739823365', '11', '2021-02-28 09:31:17', 'ACTIVE', '');

INSERT INTO phone (phone_number, package_code, phonestart_datetime, phone_status, note)
VALUES ('0719317657', '11', '2021-03-28 09:32:17', 'ACTIVE', '');

INSERT INTO phone (phone_number, package_code, phonestart_datetime, phone_status, note)
VALUES ('0757984718', '11', '2021-04-28 09:33:17', 'ACTIVE', '');

INSERT INTO phone (phone_number, package_code, phonestart_datetime, phone_status, note)
VALUES ('0798512435', '11', '2021-05-28 09:34:17', 'ACTIVE', '');

INSERT INTO phone (phone_number, package_code, phonestart_datetime, phone_status, note)
VALUES ('0759823125', '12', '2021-06-28 09:31:17', 'ACTIVE', '');

INSERT INTO phone (phone_number, package_code, phonestart_datetime, phone_status, note)
VALUES ('0769317426', '12', '2021-07-28 09:32:17', 'ACTIVE', '');

INSERT INTO phone (phone_number, package_code, phonestart_datetime, phone_status, note)
VALUES ('0797984987', '12', '2021-08-28 09:33:17', 'ACTIVE', '');

INSERT INTO phone (phone_number, package_code, phonestart_datetime, phone_status, note)
VALUES ('0738512283', '12', '2021-09-28 09:34:17', 'ACTIVE', '');

INSERT INTO phone (phone_number, package_code, phonestart_datetime, phone_status, note)
VALUES ('0719298125', '13', '2021-10-28 09:31:17', 'ACTIVE', '');

INSERT INTO phone (phone_number, package_code, phonestart_datetime, phone_status, note)
VALUES ('0742347426', '13', '2021-12-28 09:32:17', 'ACTIVE', '');

INSERT INTO phone (phone_number, package_code, phonestart_datetime, phone_status, note)
VALUES ('0787287987', '13', '2021-11-28 09:33:17', 'ACTIVE', '');

INSERT INTO phone (phone_number, package_code, phonestart_datetime, phone_status, note)
VALUES ('0728519883', '13', '2021-10-28 09:34:17', 'ACTIVE', '');

INSERT INTO phone (phone_number, package_code, phonestart_datetime, phone_status, note)
VALUES ('0729291275', '14', '2021-10-30 09:31:17', 'ACTIVE', '');

INSERT INTO phone (phone_number, package_code, phonestart_datetime, phone_status, note)
VALUES ('0732398726', '14', '2021-12-30 09:32:17', 'ACTIVE', '');

INSERT INTO phone (phone_number, package_code, phonestart_datetime, phone_status, note)
VALUES ('0767281877', '14', '2021-11-30 09:33:17', 'ACTIVE', '');

INSERT INTO phone (phone_number, package_code, phonestart_datetime, phone_status, note)
VALUES ('0758519203', '14', '2021-10-30 09:34:17', 'ACTIVE', '');


-- db table "admin" population:

INSERT INTO admin (apersonal_number, afirst_name, alast_name, a_email, phone_number)
VALUES ('9283478122', 'Mihailo', 'Maksić', 'mika@introtelecom.com', '0770000001');

INSERT INTO admin (apersonal_number, afirst_name, alast_name, a_email, phone_number)
VALUES ('7346552861', 'Živojin', 'Žikić', 'zika@introtelecom.com', '0770000002');



-- db table "customer" population:

INSERT INTO customer (cpersonal_number, cfirst_name, clast_name, c_email, address)
VALUES ('3277645392', 'Lana', 'Jovanović', 'lana@greenphone.com', 'Radnička 35, Beograd');

INSERT INTO customer (cpersonal_number, cfirst_name, clast_name, c_email, address)
VALUES ('899385763', 'Zoran', 'Stošić', 'zoka@bluephone.com', 'Cvetna 89, Beograd');

INSERT INTO customer (cpersonal_number, cfirst_name, clast_name, c_email, address)
VALUES ('1453276558', 'Vojislav', 'Panić', 'voja@redphone.com', 'Trgovačka 31, Požarevac');

INSERT INTO customer (cpersonal_number, cfirst_name, clast_name, c_email, address)
VALUES ('9457783234', 'Jasmina', 'Pantelić', 'jaca@yellowphone.com', 'Resavska 3, Novi Sad');

INSERT INTO customer (cpersonal_number, cfirst_name, clast_name, c_email, address)
VALUES ('5376351163', 'Tomislav', 'Kojić', 'toma@greenphone.com', 'Čegarska 6, Niš');

INSERT INTO customer (cpersonal_number, cfirst_name, clast_name, c_email, address)
VALUES ('6227863719', 'Slobodan', 'Grujić', 'boda@bluephone.com', 'Ustanička 56, Kragujevac');

INSERT INTO customer (cpersonal_number, cfirst_name, clast_name, c_email, address)
VALUES ('9388761527', 'Momir', 'Ivanović', 'moma@redphone.com', 'Žička 12, Novi Sad');

INSERT INTO customer (cpersonal_number, cfirst_name, clast_name, c_email, address)
VALUES ('8711728376', 'Danica', 'Ilić', 'daca@yellowphone.com', 'Cvijićeva 41, Niš');

INSERT INTO customer (cpersonal_number, cfirst_name, clast_name, c_email, address)
VALUES ('4566178236', 'Soja', 'Pavlović', 'soja@greenphone.com', 'Humska 18, Beograd');

INSERT INTO customer (cpersonal_number, cfirst_name, clast_name, c_email, address)
VALUES ('2273894715', 'Marinko', 'Jović', 'mare@bluephone.com', 'Krfska 32, Zrenjanin');

INSERT INTO customer (cpersonal_number, cfirst_name, clast_name, c_email, address)
VALUES ('3378298162', 'Sava', 'Branković', 'sava@redphone.com', 'Kosovska 5, Petrovac na Mlavi');

INSERT INTO customer (cpersonal_number, cfirst_name, clast_name, c_email, address)
VALUES ('7718392783', 'Branislav', 'Aćimović', 'bane@yellowphone.com', 'Resavska 41, Beograd');

INSERT INTO customer (cpersonal_number, cfirst_name, clast_name, c_email, address)
VALUES ('7231183945', 'Dragana', 'Vlajić', 'gaga@bluephone.com', 'Moravska 15, Požarevac');

INSERT INTO customer (cpersonal_number, cfirst_name, clast_name, c_email, address)
VALUES ('7712839438', 'Luka', 'Kovačević', 'luka@redphone.com', 'Vodovodska 37, Zrenjanin');

-- db table "user_data" population:


INSERT INTO user_data (phone_number, username, password, role_id, user_status)
VALUES ('0770000001', 'mika', '$2a$10$3a/Lri8V7i.FYJfFzPXOU.cL4bYGRl0sl0K2TB9YT525tVf2EYf8S', '1', 'ACTIVE');

INSERT INTO user_data (phone_number, username, password, role_id, user_status)
VALUES ('0770000002', 'zika', '$2a$10$fI.SMLMbj12HWyHjby9k.uPgCiQ0x1xgUablAXRlvdkIzGbxpC4Du', '1', 'ACTIVE');


INSERT INTO user_data (phone_number, username, password, role_id, user_status)
VALUES ('0720123763', 'lana1', '$2a$10$Fr8sTqI/gs41RZzWVvm.J.SKgPc/Lp44zhLvSaStNQ3Ha2nn0mg26', '2', 'ACTIVE');

INSERT INTO user_data (phone_number, username, password, role_id, user_status)
VALUES ('0739823365', 'lana2', '$2a$10$eIgWnlbiUwCzWzyeSKsKpemQEHHaCG6n5vcXvcrKeDYAzpyA/HYhm', '2', 'ACTIVE');

INSERT INTO user_data (phone_number, username, password, role_id, user_status)
VALUES ('0787287987', 'zoka1', '$2a$10$NIs9j9fVdcKQMlfeDfyPXOPyj.r7mI4G4fOhOANWvcRwoC6KqPce.', '2', 'ACTIVE');

INSERT INTO user_data (phone_number, username, password, role_id, user_status)
VALUES ('0767281877', 'voja1', '$2a$10$qDFulKIoH6AoOU4/QoQvZelpojcFUeXMbpsmJXH6x8jxpkrJZEWHK', '2', 'ACTIVE');

INSERT INTO user_data (phone_number, username, password, role_id, user_status)
VALUES ('0767634418', 'jaca1', '$2a$10$5w6btEPLd9J9lDLsf8CZTeld3V.2VFI/PJ9ByhQ8TbsmdkVKqimmW', '2', 'ACTIVE');

INSERT INTO user_data (phone_number, username, password, role_id, user_status)
VALUES ('0797984987', 'jaca2', '$2a$10$CO3ToTDQcgwOEfZDudOone5EzgkfIST/v5hpxLxsePAok.3J8HWkK', '2', 'ACTIVE');

INSERT INTO user_data (phone_number, username, password, role_id, user_status)
VALUES ('0732398726', 'jaca3', '$2a$10$qlCaAJ60DFw3gcDsc7rLIeO41mRp7hN/pYR14ujpZxVZjU9fMW7vC', '2', 'ACTIVE');

INSERT INTO user_data (phone_number, username, password, role_id, user_status)
VALUES ('0788995435', 'toma1', '$2a$10$kk1FSYyuRSJdEif.09wt9eQpiUfiPJqcrS1FQViqQQj9FCR7Q3dXC', '2', 'ACTIVE');

INSERT INTO user_data (phone_number, username, password, role_id, user_status)
VALUES ('0742347426', 'toma2', '$2a$10$xwYZix.hK.yWVletE8Uw9Os34yXUF4pLeheAYb5.b9I1n7a86DY8u', '2', 'ACTIVE');

INSERT INTO user_data (phone_number, username, password, role_id, user_status)
VALUES ('0738512283', 'boda1', '$2a$10$xdnPmpFxjLkzUEMkCOXA6.918qpBv28vVBBQohv5KHuTHLZMOZFMW', '2', 'ACTIVE');

INSERT INTO user_data (phone_number, username, password, role_id, user_status)
VALUES ('0729291275', 'moma1', '$2a$10$0qAsvUP0qmLfi4.xtpeQOOFv0.N7y.qiAdc1r5LSGE1zYtIL75EuG', '2', 'ACTIVE');

INSERT INTO user_data (phone_number, username, password, role_id, user_status)
VALUES ('0718995435', 'dana1', '$2a$10$5Iw56AH.fbVcDcy2jehWFOAubqdLB9SwVFYMVabb4E60KhJyOjQfy', '2', 'ACTIVE');

INSERT INTO user_data (phone_number, username, password, role_id, user_status)
VALUES ('0769317426', 'dana2', '$2a$10$fiYAFca/IsJask71QKehQ.3hn.pff5Yz30vgQEE7LUPr174ROAZ6i', '2', 'ACTIVE');

INSERT INTO user_data (phone_number, username, password, role_id, user_status)
VALUES ('0719317657', 'soja1', '$2a$10$pKRq9B6tyZcWHwEpVjdSH.bGZh7r/WcpFVdDjZvWKoQ8K7zVgT8hK', '2', 'ACTIVE');

INSERT INTO user_data (phone_number, username, password, role_id, user_status)
VALUES ('0798512435', 'mare1', '$2a$10$Jx7GOE55F8LGMRhKFMvDne/sbQJGgG7MPmTbGa8MuaLTgocuXs5Ve', '2', 'ACTIVE');

INSERT INTO user_data (phone_number, username, password, role_id, user_status)
VALUES ('0747634418', 'sava1', '$2a$10$LVt0fOB3AueFaObbmbN8dOdOFgb2VL8DtWmFBuNWtNHZSwA5nuyBO', '2', 'ACTIVE');

INSERT INTO user_data (phone_number, username, password, role_id, user_status)
VALUES ('0757984718', 'sava2', '$2a$10$RdMctK7x8H2.PhmgPSKQwu90wpNomwI9W9fXCggWqYQZEnQg2DPGq', '2', 'ACTIVE');

INSERT INTO user_data (phone_number, username, password, role_id, user_status)
VALUES ('0758519203', 'sava3', '$2a$10$8uf1WrqnVzxRpavBtngqdebOB7wTxGdhCwIZoY/GFvXPHD58StAnW', '2', 'ACTIVE');

INSERT INTO user_data (phone_number, username, password, role_id, user_status)
VALUES ('0759887657', 'bane1', '$2a$10$DKh3VH5ZlBg6nmGIo4.iRe/vlc.DQiDiB/EMLR5nsmsFlwXVkL4tu', '2', 'ACTIVE');

INSERT INTO user_data (phone_number, username, password, role_id, user_status)
VALUES ('0759823125', 'bane2', '$2a$10$SHPdkq1XXj0Mb5.RdMlJpOzBiIPPCep6o/DJaDVwUUuoN3vetgGIC', '2', 'ACTIVE');

INSERT INTO user_data (phone_number, username, password, role_id, user_status)
VALUES ('0739887657', 'gaga1', '$2a$10$IqsmXSeIOHfU3TuaM1HhAuZHZZfVciwcYumNDLTY1UjA/vv7OzjhK', '2', 'ACTIVE');

INSERT INTO user_data (phone_number, username, password, role_id, user_status)
VALUES ('0719298125', 'gaga2', '$2a$10$yAfy5ML3HIS3Xa6VnQL/Q.FVzRW08NctS3w5JYgUGnz8YFE4vbbxC', '2', 'ACTIVE');

INSERT INTO user_data (phone_number, username, password, role_id, user_status)
VALUES ('0729834565', 'luka1', '$2a$10$nPrnDkUsUZy/6QwFvbm/dO475oHsQXX5TX9946DKyqCmcJ8xjBSNe', '2', 'ACTIVE');

INSERT INTO user_data (phone_number, username, password, role_id, user_status)
VALUES ('0728519883', 'luka2', '$2a$10$Il5uKrYXrFO0QrD5GSQBM.l8E.lJDetvW30etzbSz.QutNdWudJoK', '2', 'ACTIVE');

-- db table "customer_phone" population:

INSERT INTO customer_phone (customer_id, phone_number)
VALUES (1, '0720123763');

INSERT INTO customer_phone (customer_id, phone_number)
VALUES (1, '0739823365');

INSERT INTO customer_phone (customer_id, phone_number)
VALUES (2, '0787287987');

INSERT INTO customer_phone (customer_id, phone_number)
VALUES (3, '0767281877');

INSERT INTO customer_phone (customer_id, phone_number)
VALUES (4, '0767634418');

INSERT INTO customer_phone (customer_id, phone_number)
VALUES (4, '0797984987');

INSERT INTO customer_phone (customer_id, phone_number)
VALUES (4, '0732398726');

INSERT INTO customer_phone (customer_id, phone_number)
VALUES (5, '0788995435');

INSERT INTO customer_phone (customer_id, phone_number)
VALUES (5, '0742347426');

INSERT INTO customer_phone (customer_id, phone_number)
VALUES (6, '0738512283');

INSERT INTO customer_phone (customer_id, phone_number)
VALUES (7, '0729291275');

INSERT INTO customer_phone (customer_id, phone_number)
VALUES (8, '0718995435');

INSERT INTO customer_phone (customer_id, phone_number)
VALUES (8, '0769317426');

INSERT INTO customer_phone (customer_id, phone_number)
VALUES (9, '0719317657');

INSERT INTO customer_phone (customer_id, phone_number)
VALUES (10, '0798512435');

INSERT INTO customer_phone (customer_id, phone_number)
VALUES (11, '0747634418');

INSERT INTO customer_phone (customer_id, phone_number)
VALUES (11, '0757984718');

INSERT INTO customer_phone (customer_id, phone_number)
VALUES (11, '0758519203');

INSERT INTO customer_phone (customer_id, phone_number)
VALUES (12, '0759887657');

INSERT INTO customer_phone (customer_id, phone_number)
VALUES (12, '0759823125');

INSERT INTO customer_phone (customer_id, phone_number)
VALUES (13, '0739887657');

INSERT INTO customer_phone (customer_id, phone_number)
VALUES (13, '0719298125');

INSERT INTO customer_phone (customer_id, phone_number)
VALUES (14, '0729834565');

INSERT INTO customer_phone (customer_id, phone_number)
VALUES (14, '0728519883');



-- db table "package_frame" population:

INSERT INTO package_frame (phone_number, packfr_cls, packfr_sms, packfr_int, packfr_asm, packfr_icl, packfr_rmg, packfr_startdatetime, packfr_enddatetime, packfr_status)
VALUES ('0747634418', 200, 200, '0.00', '0.00', '0.00', '0.00', '2023-01-01 00:00:00.0', '2023-02-01 00:00:00.0', 'ACTIVE');

INSERT INTO package_frame (phone_number, packfr_cls, packfr_sms, packfr_int, packfr_asm, packfr_icl, packfr_rmg, packfr_startdatetime, packfr_enddatetime, packfr_status)
VALUES ('0747634418', 200, 200, '0.00', '0.00', '0.00', '0.00', '2023-02-01 00:00:00.0', '2023-03-01 00:00:00.0', 'ACTIVE');

INSERT INTO package_frame (phone_number, packfr_cls, packfr_sms, packfr_int, packfr_asm, packfr_icl, packfr_rmg, packfr_startdatetime, packfr_enddatetime, packfr_status)
VALUES ('0759887657', 200, 200, '10000.00', '0.00', '0.00', '0.00', '2023-01-01 00:00:00.0', '2023-02-01 00:00:00.0', 'ACTIVE');

INSERT INTO package_frame (phone_number, packfr_cls, packfr_sms, packfr_int, packfr_asm, packfr_icl, packfr_rmg, packfr_startdatetime, packfr_enddatetime, packfr_status)
VALUES ('0759887657', 200, 200, '10000.00', '0.00', '0.00', '0.00', '2023-02-01 00:00:00.0', '2023-03-01 00:00:00.0', 'ACTIVE');

INSERT INTO package_frame (phone_number, packfr_cls, packfr_sms, packfr_int, packfr_asm, packfr_icl, packfr_rmg, packfr_startdatetime, packfr_enddatetime, packfr_status)
VALUES ('0798512435', 300, 300, '10000.00', '0.00', '200.00', '200.00', '2023-01-01 00:00:00.0', '2023-02-01 00:00:00.0', 'ACTIVE');

INSERT INTO package_frame (phone_number, packfr_cls, packfr_sms, packfr_int, packfr_asm, packfr_icl, packfr_rmg, packfr_startdatetime, packfr_enddatetime, packfr_status)
VALUES ('0798512435', 300, 300, '10000.00', '0.00', '200.00', '200.00', '2023-02-01 00:00:00.0', '2023-03-01 00:00:00.0', 'ACTIVE');

INSERT INTO package_frame (phone_number, packfr_cls, packfr_sms, packfr_int, packfr_asm, packfr_icl, packfr_rmg, packfr_startdatetime, packfr_enddatetime, packfr_status)
VALUES ('0769317426', 400, 400, '10000.00', '5000.00', '200.00', '200.00', '2023-01-01 00:00:00.0', '2023-02-01 00:00:00.0', 'ACTIVE');

INSERT INTO package_frame (phone_number, packfr_cls, packfr_sms, packfr_int, packfr_asm, packfr_icl, packfr_rmg, packfr_startdatetime, packfr_enddatetime, packfr_status)
VALUES ('0769317426', 400, 400, '10000.00', '5000.00', '200.00', '200.00', '2023-02-01 00:00:00.0', '2023-03-01 00:00:00.0', 'ACTIVE');

INSERT INTO package_frame (phone_number, packfr_cls, packfr_sms, packfr_int, packfr_asm, packfr_icl, packfr_rmg, packfr_startdatetime, packfr_enddatetime, packfr_status)
VALUES ('0787287987', -1, -1, '15000.00', '5000.00', '200.00', '200.00', '2023-01-01 00:00:00.0', '2023-02-01 00:00:00.0', 'ACTIVE');

INSERT INTO package_frame (phone_number, packfr_cls, packfr_sms, packfr_int, packfr_asm, packfr_icl, packfr_rmg, packfr_startdatetime, packfr_enddatetime, packfr_status)
VALUES ('0787287987', -1, -1, '15000.00', '5000.00', '200.00', '200.00', '2023-02-01 00:00:00.0', '2023-03-01 00:00:00.0', 'ACTIVE');

INSERT INTO package_frame (phone_number, packfr_cls, packfr_sms, packfr_int, packfr_asm, packfr_icl, packfr_rmg, packfr_startdatetime, packfr_enddatetime, packfr_status)
VALUES ('0729291275', -1, -1, '-1.00', '10000.00', '200.00', '200.00', '2023-01-01 00:00:00.0', '2023-02-01 00:00:00.0', 'ACTIVE');

INSERT INTO package_frame (phone_number, packfr_cls, packfr_sms, packfr_int, packfr_asm, packfr_icl, packfr_rmg, packfr_startdatetime, packfr_enddatetime, packfr_status)
VALUES ('0729291275', -1, -1, '-1.00', '10000.00', '200.00', '200.00', '2023-02-01 00:00:00.0', '2023-03-01 00:00:00.0', 'ACTIVE');






-- db table "addon_frame" population:

INSERT INTO addon_frame (phone_number, addon_code, addfr_cls, addfr_sms, addfr_int, addfr_asm, addfr_icl, addfr_rmg, addfr_startdatetime, addfr_enddatetime, addfr_status)
VALUES ('0747634418', 'ADDCLS', 100, 0, '0.00', '0.00', '0.00', '0.00', '2023-01-22 00:00:00.0', '2023-02-01 00:00:00.0', 'ACTIVE');

INSERT INTO addon_frame (phone_number, addon_code, addfr_cls, addfr_sms, addfr_int, addfr_asm, addfr_icl, addfr_rmg, addfr_startdatetime, addfr_enddatetime, addfr_status)
VALUES ('0747634418', 'ADDSMS', 0, 100, '0.00', '0.00', '0.00', '0.00', '2023-02-22 00:00:00.0', '2023-03-01 00:00:00.0', 'ACTIVE');

INSERT INTO addon_frame (phone_number, addon_code, addfr_cls, addfr_sms, addfr_int, addfr_asm, addfr_icl, addfr_rmg, addfr_startdatetime, addfr_enddatetime, addfr_status)
VALUES ('0729834565', 'ADDSMS', 0, 100, '0.00', '0.00', '0.00', '0.00', '2023-01-22 00:00:00.0', '2023-02-01 00:00:00.0', 'ACTIVE');

INSERT INTO addon_frame (phone_number, addon_code, addfr_cls, addfr_sms, addfr_int, addfr_asm, addfr_icl, addfr_rmg, addfr_startdatetime, addfr_enddatetime, addfr_status)
VALUES ('0729834565', 'ADDCLS', 100, 0, '0.00', '0.00', '0.00', '0.00', '2023-02-22 00:00:00.0', '2023-03-01 00:00:00.0', 'ACTIVE');

INSERT INTO addon_frame (phone_number, addon_code, addfr_cls, addfr_sms, addfr_int, addfr_asm, addfr_icl, addfr_rmg, addfr_startdatetime, addfr_enddatetime, addfr_status)
VALUES ('0798512435', 'ADDINT', 0, 0, '5000.00', '0.00', '0.00', '0.00', '2023-01-22 00:00:00.0', '2023-02-01 00:00:00.0', 'ACTIVE');

INSERT INTO addon_frame (phone_number, addon_code, addfr_cls, addfr_sms, addfr_int, addfr_asm, addfr_icl, addfr_rmg, addfr_startdatetime, addfr_enddatetime, addfr_status)
VALUES ('0798512435', 'ADDINT', 0, 0, '5000.00', '0.00', '0.00', '0.00', '2023-02-22 00:00:00.0', '2023-03-01 00:00:00.0', 'ACTIVE');

INSERT INTO addon_frame (phone_number, addon_code, addfr_cls, addfr_sms, addfr_int, addfr_asm, addfr_icl, addfr_rmg, addfr_startdatetime, addfr_enddatetime, addfr_status)
VALUES ('0797984987', 'ADDASM', 0, 0, '0.00', '5000.00', '0.00', '0.00', '2023-01-22 00:00:00.0', '2023-02-01 00:00:00.0', 'ACTIVE');

INSERT INTO addon_frame (phone_number, addon_code, addfr_cls, addfr_sms, addfr_int, addfr_asm, addfr_icl, addfr_rmg, addfr_startdatetime, addfr_enddatetime, addfr_status)
VALUES ('0797984987', 'ADDASM', 0, 0, '0.00', '5000.00', '0.00', '0.00', '2023-02-22 00:00:00.0', '2023-03-01 00:00:00.0', 'ACTIVE');

INSERT INTO addon_frame (phone_number, addon_code, addfr_cls, addfr_sms, addfr_int, addfr_asm, addfr_icl, addfr_rmg, addfr_startdatetime, addfr_enddatetime, addfr_status)
VALUES ('0728519883', 'ADDICL', 0, 0, '0.00', '0.00', '200.00', '0.00', '2023-01-22 00:00:00.0', '2023-02-01 00:00:00.0', 'ACTIVE');

INSERT INTO addon_frame (phone_number, addon_code, addfr_cls, addfr_sms, addfr_int, addfr_asm, addfr_icl, addfr_rmg, addfr_startdatetime, addfr_enddatetime, addfr_status)
VALUES ('0728519883', 'ADDRMG', 0, 0, '0.00', '0.00', '0.00', '200.00', '2023-02-22 00:00:00.0', '2023-03-01 00:00:00.0', 'ACTIVE');

INSERT INTO addon_frame (phone_number, addon_code, addfr_cls, addfr_sms, addfr_int, addfr_asm, addfr_icl, addfr_rmg, addfr_startdatetime, addfr_enddatetime, addfr_status)
VALUES ('0767281877', 'ADDRMG', 0, 0, '0.00', '0.00', '0.00', '200.00', '2023-01-22 00:00:00.0', '2023-02-01 00:00:00.0', 'ACTIVE');

INSERT INTO addon_frame (phone_number, addon_code, addfr_cls, addfr_sms, addfr_int, addfr_asm, addfr_icl, addfr_rmg, addfr_startdatetime, addfr_enddatetime, addfr_status)
VALUES ('0767281877', 'ADDICL', 0, 0, '0.00', '0.00', '200.00', '0.00', '2023-02-22 00:00:00.0', '2023-03-01 00:00:00.0', 'ACTIVE');






-- db table "sdr" population:

INSERT INTO sdr (phone_number, service_code, called_number, sdr_startdatetime, sdr_enddatetime, duration, msg_amount, mb_amount, sdr_note)
VALUES ('0769317426', 'SDRCLS', '0736336712', '2023-02-10 10:02:00.0', '2023-02-10 10:05:00.0', 3, 0, '0.00', '');

INSERT INTO sdr (phone_number, service_code, called_number, sdr_startdatetime, sdr_enddatetime, duration, msg_amount, mb_amount, sdr_note)
VALUES ('0769317426', 'SDRCLS', '0736336712', '2023-02-11 10:05:00.0', '2023-02-11 10:10:00.0', 5, 0, '0.00', '');

INSERT INTO sdr (phone_number, service_code, called_number, sdr_startdatetime, sdr_enddatetime, duration, msg_amount, mb_amount, sdr_note)
VALUES ('0769317426', 'SDRSMS', '0767281877', '2023-02-12 10:02:00.0', '2023-02-12 10:02:01.0', 1, 1, '0.00', '');

INSERT INTO sdr (phone_number, service_code, called_number, sdr_startdatetime, sdr_enddatetime, duration, msg_amount, mb_amount, sdr_note)
VALUES ('0769317426', 'SDRSMS', '0767281877', '2023-02-13 10:05:00.0', '2023-02-13 10:05:01.0', 1, 1, '0.00', '');

INSERT INTO sdr (phone_number, service_code, called_number, sdr_startdatetime, sdr_enddatetime, duration, msg_amount, mb_amount, sdr_note)
VALUES ('0769317426', 'SDRINT', '-', '2023-02-14 10:02:00.0', '2023-02-14 11:02:00.0', 60, 0, '1000.00', '');

INSERT INTO sdr (phone_number, service_code, called_number, sdr_startdatetime, sdr_enddatetime, duration, msg_amount, mb_amount, sdr_note)
VALUES ('0769317426', 'SDRINT', '-', '2023-02-15 10:02:00.0', '2023-02-15 11:02:00.0', 60, 0, '1000.00', '');

INSERT INTO sdr (phone_number, service_code, called_number, sdr_startdatetime, sdr_enddatetime, duration, msg_amount, mb_amount, sdr_note)
VALUES ('0769317426', 'SDRASM', '-', '2023-02-16 10:02:00.0', '2023-02-16 11:02:00.0', 60, 0, '1000.00', '');

INSERT INTO sdr (phone_number, service_code, called_number, sdr_startdatetime, sdr_enddatetime, duration, msg_amount, mb_amount, sdr_note)
VALUES ('0769317426', 'SDRASM', '-', '2023-02-17 10:02:00.0', '2023-02-17 11:02:00.0', 60, 0, '1000.00', '');



-- db table "monthlybill_facts" population:

INSERT INTO monthlybill_facts (phone_number, year_month, package_price, addcls_price, addsms_price, addint_price, addasm_price, addicl_price, addrmg_price, monthlybill_totalprice, monthlybill_datetime)
VALUES ('0769317426', '2023-12-01', '1000.00', '0.00', '0.00', '0.00', '0.00', '0.00', '0.00', '1000.00', '2024-01-01 00:00:00.0');

INSERT INTO monthlybill_facts (phone_number, year_month, package_price, addcls_price, addsms_price, addint_price, addasm_price, addicl_price, addrmg_price, monthlybill_totalprice, monthlybill_datetime)
VALUES ('0769317426', '2024-01-01', '1000.00', '0.00', '0.00', '0.00', '0.00', '0.00', '0.00', '1000.00', '2024-02-01 00:00:00.0');

INSERT INTO monthlybill_facts (phone_number, year_month, package_price, addcls_price, addsms_price, addint_price, addasm_price, addicl_price, addrmg_price, monthlybill_totalprice, monthlybill_datetime)
VALUES ('0769317426', '2024-02-01', '1000.00', '0.00', '0.00', '0.00', '0.00', '0.00', '0.00', '1000.00', '2024-03-01 00:00:00.0');






















