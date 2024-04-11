delete from addon_frame
where addfr_id=(select max(addfr_id) from addon_frame);