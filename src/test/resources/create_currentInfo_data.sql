INSERT INTO package_frame (phone_number, packfr_cls, packfr_sms, packfr_int, packfr_asm, packfr_icl, packfr_rmg, packfr_startdatetime, packfr_enddatetime, packfr_status)
VALUES ('0769317426', 400, 400, '10000.00', '5000.00', '200.00', '200.00', date_trunc('month', current_timestamp(1)), date_trunc('month', current_timestamp(1)) + interval '1 month', 'ACTIVE');

INSERT INTO sdr (phone_number, service_code, called_number, sdr_startdatetime, sdr_enddatetime, duration, msg_amount, mb_amount, sdr_note)
VALUES ('0769317426', 'SDRASM', '-', date_trunc('month', current_timestamp(1)) + interval '2 hour', date_trunc('month', current_timestamp(1)) + interval '4 hour', 120, 0, '1300.00', '');





