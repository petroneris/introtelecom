delete from sdr
where sdr_id=(select max(sdr_id) from sdr);
