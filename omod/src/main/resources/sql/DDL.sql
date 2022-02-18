DROP PROCEDURE IF EXISTS create_cca_etl_tables $$
CREATE PROCEDURE create_cca_etl_tables()
BEGIN
DECLARE script_id INT(11);

-- I have included statements like SELECT "Successfully created covid screening table" after creation of every table
-- without them, execution of the sp fail with RESULTSET  is from UPDATE. No Data
-- Let's therefore use it as a convention to get the SQL executed by OpenMRS Context

-- Log start time
INSERT INTO kenyaemr_etl.etl_script_status(script_name, start_time) VALUES('create_covid_cca_tables', NOW());
SET script_id = LAST_INSERT_ID();

DROP TABLE IF EXISTS kenyaemr_etl.etl_cca_covid_screening;
DROP TABLE IF EXISTS kenyaemr_etl.etl_cca_covid_treatment_followup;
DROP TABLE IF EXISTS kenyaemr_etl.etl_cca_covid_rdt_test;
DROP TABLE IF EXISTS kenyaemr_etl.etl_cca_covid_clinical_review;
DROP TABLE IF EXISTS kenyaemr_etl.etl_cca_covid_treatment_enrollment;
DROP TABLE IF EXISTS kenyaemr_etl.etl_cca_covid_treatment_enrollment_outcome;

-------------- create table etl_covid_screening-----------------------
    CREATE TABLE kenyaemr_etl.etl_cca_covid_screening (
      uuid CHAR(38),
      encounter_id INT(11) NOT NULL PRIMARY KEY,
      visit_id INT(11) DEFAULT NULL,
      patient_id INT(11) NOT NULL ,
      location_id INT(11) DEFAULT NULL,
      visit_date DATE,
      encounter_provider INT(11),
      date_created DATE,
      onset_symptoms_date DATE,
      fever VARCHAR(10),
      cough VARCHAR(10),
      runny_nose VARCHAR(10),
      diarrhoea VARCHAR(10),
      headache VARCHAR(10),
      muscular_pain VARCHAR(10),
      abdominal_pain VARCHAR(10),
      general_weakness VARCHAR(10),
      sore_throat VARCHAR(10),
      breathing_difficulty VARCHAR(10),
      nausea_vomiting VARCHAR(10),
      altered_mental_status VARCHAR(10),
      chest_pain VARCHAR(10),
      joint_pain VARCHAR(10),
      loss_of_taste_smell varchar(10),
      other_symptom VARCHAR(10),
      specify_symptoms VARCHAR(255),
      recent_travel VARCHAR(10),
      contact_with_suspected_or_confirmed_case VARCHAR(10),
      attended_large_gathering VARCHAR(10),
      screening_department VARCHAR(1500),
      hiv_status varchar(20),
      in_tb_program varchar(10),
      pregnant varchar(10),
      vaccinated_for_covid varchar(10),
      covid_vaccination_status varchar(50),
      ever_tested_for_covid varchar(10),
      covid_test_date date,
      eligible_for_covid_test varchar(10),
      voided INT(11),
      CONSTRAINT FOREIGN KEY (patient_id) REFERENCES kenyaemr_etl.etl_patient_demographics(patient_id),
      CONSTRAINT unique_uuid UNIQUE(uuid),
      INDEX(visit_date),
      INDEX(visit_id),
      INDEX(encounter_id),
      INDEX(patient_id),
      INDEX(patient_id, visit_date)
    );
  SELECT "Successfully created covid screening table";
-------------- create table etl_covid_treatment followup-----------------------

    CREATE TABLE kenyaemr_etl.etl_cca_covid_treatment_followup (
      uuid CHAR(38),
      encounter_id INT(11) NOT NULL PRIMARY KEY,
      visit_id INT(11) DEFAULT NULL,
      patient_id INT(11) NOT NULL ,
      location_id INT(11) DEFAULT NULL,
      visit_date DATE,
      encounter_provider INT(11),
      date_created DATE,
      day_of_followup int(11),
      temp VARCHAR(10),
      fever VARCHAR(10),
      cough VARCHAR(10),
      difficulty_breathing VARCHAR(10),
      sore_throat VARCHAR(10),
      sneezing VARCHAR(10),
      headache VARCHAR(10),
      referred_to_hosp VARCHAR(10),
      case_classification VARCHAR(50),
      patient_admitted VARCHAR(10),
      admission_unit VARCHAR(50),
      treatment_received VARCHAR(100),
      voided INT(11),
      CONSTRAINT FOREIGN KEY (patient_id) REFERENCES kenyaemr_etl.etl_patient_demographics(patient_id),
      CONSTRAINT unique_uuid UNIQUE(uuid),
      INDEX(visit_date),
      INDEX(visit_id),
      INDEX(encounter_id),
      INDEX(patient_id),
      INDEX(patient_id, visit_date)
    );

  SELECT "Successfully created covid treatment followup table";
-------------- create table etl_cca_covid_rdt_test-----------------------

CREATE TABLE kenyaemr_etl.etl_cca_covid_rdt_test (
      uuid CHAR(38),
      encounter_id INT(11) NOT NULL PRIMARY KEY,
      visit_id INT(11) DEFAULT NULL,
      patient_id INT(11) NOT NULL ,
      location_id INT(11) DEFAULT NULL,
      visit_date DATE,
      encounter_provider INT(11),
      date_created DATE,
      nationality VARCHAR(50),
      passport_id_number VARCHAR(50),
      sample_type VARCHAR(50),
      test_reason VARCHAR(30),
      ag_rdt_test_done VARCHAR(10),
      ag_rdt_test_date DATE,
      case_type VARCHAR(20),
      assay_kit_name VARCHAR(40),
      ag_rdt_test_type_coded VARCHAR(30),
      ag_rdt_test_type_other VARCHAR(50),
      kit_lot_number VARCHAR(50),
      kit_expiry DATE,
      test_result VARCHAR(20),
      action_taken VARCHAR(50),
      voided INT(11),
      CONSTRAINT FOREIGN KEY (patient_id) REFERENCES kenyaemr_etl.etl_patient_demographics(patient_id),
      CONSTRAINT unique_uuid UNIQUE(uuid),
      INDEX(visit_date),
      INDEX(visit_id),
      INDEX(encounter_id),
      INDEX(patient_id),
      INDEX(patient_id, visit_date)
    );

  SELECT "Successfully created covid rdt test table";
    ------------- create table etl_covid_clinical_review-----------------------

    CREATE TABLE kenyaemr_etl.etl_cca_covid_clinical_review (
      uuid CHAR(38),
      encounter_id INT(11) NOT NULL PRIMARY KEY,
      visit_id INT(11) DEFAULT NULL,
      patient_id INT(11) NOT NULL ,
      location_id INT(11) DEFAULT NULL,
      visit_date DATE,
      encounter_provider INT(11),
      date_created DATE,
      ag_rdt_test_result VARCHAR(10),
      case_classification VARCHAR(30),
      action_taken VARCHAR(50),
      hospital_referred_to VARCHAR(50),
      case_id VARCHAR(10),
      email VARCHAR(50),
      case_type VARCHAR(20),
      pcr_sample_collection_date DATE,
      pcr_result_date DATE,
      pcr_result VARCHAR(40),
      case_classification_after_positive_pcr VARCHAR(30),
      action_taken_after_pcr_result VARCHAR(50),
      notes VARCHAR(1024),
      voided INT(11),
      CONSTRAINT FOREIGN KEY (patient_id) REFERENCES kenyaemr_etl.etl_patient_demographics(patient_id),
      CONSTRAINT unique_uuid UNIQUE(uuid),
      INDEX(visit_date),
      INDEX(visit_id),
      INDEX(encounter_id),
      INDEX(patient_id),
      INDEX(patient_id, visit_date)
    );

  SELECT "Successfully created covid clinical review table";
    ------------- create table etl_cca_covid_treatment_enrollment-----------------------

    CREATE TABLE kenyaemr_etl.etl_cca_covid_treatment_enrollment (
      uuid CHAR(38),
      encounter_id INT(11) NOT NULL PRIMARY KEY,
      visit_id INT(11) DEFAULT NULL,
      patient_id INT(11) NOT NULL ,
      location_id INT(11) DEFAULT NULL,
      visit_date DATE,
      encounter_provider INT(11),
      date_created DATE,
      passport_id_number VARCHAR(50),
      case_classification VARCHAR(30),
      patient_type VARCHAR(50),
      hospital_referred_from VARCHAR(50),
      date_tested_covid_positive DATE,
      action_taken VARCHAR(50),
      admission_date DATE,
      admission_unit VARCHAR(50),
      voided INT(11),
      CONSTRAINT FOREIGN KEY (patient_id) REFERENCES kenyaemr_etl.etl_patient_demographics(patient_id),
      CONSTRAINT unique_uuid UNIQUE(uuid),
      INDEX(visit_date),
      INDEX(visit_id),
      INDEX(encounter_id),
      INDEX(patient_id),
      INDEX(patient_id, visit_date)
    );

  SELECT "Successfully created covid treatment program enrollment table";
    ------------- create table etl_cca_covid_treatment_enrollment_outcome-----------------------

    CREATE TABLE kenyaemr_etl.etl_cca_covid_treatment_enrollment_outcome (
      uuid CHAR(38),
      encounter_id INT(11) NOT NULL PRIMARY KEY,
      visit_id INT(11) DEFAULT NULL,
      patient_id INT(11) NOT NULL ,
      location_id INT(11) DEFAULT NULL,
      visit_date DATE,
      encounter_provider INT(11),
      date_created DATE,
      outcome VARCHAR(50),
      facility_transferred VARCHAR(50),
      facility_referred VARCHAR(50),
      comment VARCHAR(50),
      voided INT(11),
      CONSTRAINT FOREIGN KEY (patient_id) REFERENCES kenyaemr_etl.etl_patient_demographics(patient_id),
      CONSTRAINT unique_uuid UNIQUE(uuid),
      INDEX(visit_date),
      INDEX(visit_id),
      INDEX(encounter_id),
      INDEX(patient_id),
      INDEX(patient_id, visit_date)
    );
  SELECT "Successfully created covid program outcome table";
  UPDATE kenyaemr_etl.etl_script_status SET stop_time=NOW() where id= script_id;

END $$
