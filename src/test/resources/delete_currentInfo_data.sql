delete from sdr
where sdr_id=(select max(sdr_id) from sdr);

delete from package_frame
where packfr_id=(select max(packfr_id) from package_frame);
