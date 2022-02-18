DROP PROCEDURE IF EXISTS create_covid_cca_datatools_tables $$
CREATE PROCEDURE create_covid_cca_datatools_tables()
BEGIN
DECLARE script_id INT(11);

-- Log start time
INSERT INTO kenyaemr_etl.etl_script_status(script_name, start_time) VALUES('covid_cca_datatool_tables', NOW());
SET script_id = LAST_INSERT_ID();

UPDATE kenyaemr_etl.etl_script_status SET stop_time=NOW() where id= script_id;

END $$

