
SET @OLD_SQL_MODE=@@SQL_MODE $$
SET SQL_MODE='' $$

--Populate etl covid screening: CCA
DROP PROCEDURE IF EXISTS sp_populate_etl_cca_covid_screening$$
CREATE PROCEDURE sp_populate_etl_cca_covid_screening()
BEGIN
SELECT "Processing CCA covid screening data", CONCAT("Time: ", NOW());
insert into kenyaemr_etl.etl_cca_covid_screening(
uuid,
encounter_id,
visit_id,
patient_id,
location_id,
visit_date,
encounter_provider,
date_created,
onset_symptoms_date,
fever,
cough,
runny_nose,
diarrhoea,
headache,
muscular_pain,
abdominal_pain,
general_weakness,
sore_throat,
breathing_difficulty,
nausea_vomiting,
altered_mental_status,
chest_pain,
joint_pain,
loss_of_taste_smell,
other_symptom,
specify_symptoms,
recent_travel,
contact_with_suspected_or_confirmed_case,
attended_large_gathering,
screening_department,
hiv_status,
in_tb_program,
pregnant,
vaccinated_for_covid,
covid_vaccination_status,
ever_tested_for_covid,
covid_test_date,
eligible_for_covid_test,
voided
)
select
	e.uuid,
	e.encounter_id as encounter_id,
	e.visit_id as visit_id,
	e.patient_id,
	e.location_id,
	date(e.encounter_datetime) as visit_date,
	e.creator as encounter_provider,
	e.date_created as date_created,
  max(if(o.concept_id=1730,date(o.value_datetime),null)) as onset_symptoms_date,
  max(if(o.concept_id=140238,(case o.value_coded when 1065 then "Yes" when 1066 then "No" else "" end),null)) as fever,
  max(if(o.concept_id=143264,(case o.value_coded when 1065 then "Yes" when 1066 then "No" else "" end),null)) as cough,
  max(if(o.concept_id=163336,(case o.value_coded when 113224 then "Yes" when 1066 then "No" else "" end),null)) as runny_nose,
  max(if(o.concept_id=142412,(case o.value_coded when 1065 then "Yes" when 1066 then "No" else "" end),null)) as diarrhoea,
  max(if(o.concept_id=5219,(case o.value_coded when 139084 then "Yes" when 1066 then "No" else "" end),null)) as headache,
  max(if(o.concept_id=160388,(case o.value_coded when 133632 then "Yes" when 1066 then "No" else "" end),null)) as muscular_pain,
  max(if(o.concept_id=1125,(case o.value_coded when 151 then "Yes" when 1066 then "No" else "" end),null)) as abdominal_pain,
  max(if(o.concept_id=122943,(case o.value_coded when 5226 then "Yes" when 1066 then "No" else "" end),null)) as general_weakness,

  max(if(o.concept_id=163741,(case o.value_coded when 158843 then "Yes" when 1066 then "No" else "" end),null)) as sore_throat,
  max(if(o.concept_id=164441,(case o.value_coded when 1065 then "Yes" when 1066 then "No" else "" end),null)) as breathing_difficulty,
  max(if(o.concept_id=122983,(case o.value_coded when 1065 then "Yes" when 1066 then "No" else "" end),null)) as nausea_vomiting,

  max(if(o.concept_id=6023,(case o.value_coded when 1065 then "Yes" when 1066 then "No" else "" end),null)) as altered_mental_status,
  max(if(o.concept_id=1123,(case o.value_coded when 120749 then "Yes" when 1066 then "No" else "" end),null)) as chest_pain,
  max(if(o.concept_id=160687,(case o.value_coded when 80 then "Yes" when 1066 then "No" else "" end),null)) as joint_pain,
  max(if(o.concept_id=1729,(case o.value_coded when 135589 then "Yes" when 1066 then "No" else "" end),null)) as loss_of_taste_smell,

  max(if(o.concept_id=1838,(case o.value_coded when 139548 then "Yes" else "" end),null)) as other_symptom,
	max(if(o.concept_id=160632,o.value_text,null)) as specify_symptoms,
  max(if(o.concept_id=162619,(case o.value_coded when 1065 then "Yes" when 1066 then "No" when 1067 then "Unknown" else "" end),null)) as recent_travel,
	max(if(o.concept_id=162633,(case o.value_coded when 1065 then "Yes" when 1066 then "No" when 1067 then "Unknown" else "" end),null)) as contact_with_suspected_or_confirmed_case,
  max(if(o.concept_id=165163,(case o.value_coded when 1065 then "Yes" when 1066 then "No" else "" end),null)) as attended_large_gathering,
  max(if(o.concept_id=164918,(case o.value_coded when 1622 then "ANC" when 1623 then "PNC" when 5483 then "FP" when 162049 then "CWC" when 160542 then "OPD" when 162050 then "CCC" when 160541 then "TB" when 160545 then "Community" else "" end),null)) as screening_department,
  max(if(o.concept_id=1169,(case o.value_coded when 703 then "Positive" when 664 then "Negative" else "" end),null)) as hiv_status,
  max(if(o.concept_id=162309,(case o.value_coded when 1065 then "Yes" when 1066 then "No" else "" end),null)) as in_tb_program,
  max(if(o.concept_id=5272,(case o.value_coded when 1065 then "Yes" when 1066 then "No" else "" end),null)) as pregnant,

  max(if(o.concept_id=163100,(case o.value_coded when 1065 then "Yes" when 1066 then "No" else "" end),null)) as vaccinated_for_covid,
  max(if(o.concept_id=164134,(case o.value_coded when 166192 then "Partially vaccinated" when 5585 then "Fully vaccinated" else "" end),null)) as covid_vaccination_status,
  max(if(o.concept_id=165852,(case o.value_coded when 1065 then "Yes" when 1066 then "No" else "" end),null)) as ever_tested_for_covid,
  max(if(o.concept_id=159948,date(o.value_datetime),null)) as covid_test_date,
  max(if(o.concept_id=165087,(case o.value_coded when 1065 then "Yes" when 1066 then "No" else "" end),null)) as eligible_for_covid_test,
	e.voided as voided
from encounter e
	inner join person p on p.person_id=e.patient_id and p.voided=0
	inner join
	(
		select form_id from form where
			uuid in('117092aa-5355-11ec-bf63-0242ac130002')
	) f on f.form_id=e.form_id
	left outer join obs o on o.encounter_id=e.encounter_id and o.voided=0
													 and o.concept_id in (159948,1730,1729,140238,122943,143264,163741,163336,164441,142412,122983,5219,6023,160388,
                                                1123,1125,160687,1838,160632,5272,162619,162633,164918,1169,162309,163100,164134,159948,165163,165087)
where e.voided=0
group by e.patient_id, e.encounter_id;

SELECT "Completed processing CCA covid screening data ", CONCAT("Time: ", NOW());
END$$


--Populate etl covid treatment followup: CCA
DROP PROCEDURE IF EXISTS sp_populate_etl_cca_covid_treatment_followup$$
CREATE PROCEDURE sp_populate_etl_cca_covid_treatment_followup()
BEGIN
SELECT "Processing CCA covid treatment followup data", CONCAT("Time: ", NOW());
insert into kenyaemr_etl.etl_cca_covid_treatment_followup(
uuid,
encounter_id,
visit_id,
patient_id,
location_id,
visit_date,
encounter_provider,
date_created,
day_of_followup,
temp,
fever,
cough,
difficulty_breathing,
sore_throat,
sneezing,
headache,
referred_to_hosp,
case_classification,
patient_admitted,
admission_unit,
treatment_received,
voided
)
select
	e.uuid,
	e.encounter_id as encounter_id,
	e.visit_id as visit_id,
	e.patient_id,
	e.location_id,
	date(e.encounter_datetime) as visit_date,
	e.creator as encounter_provider,
	e.date_created as date_created,
	max(if(o.concept_id=165416,o.value_numeric,null)) as day_of_followup,
	max(if(o.concept_id=5088,o.value_numeric,null)) as temp,
	max(if(o.concept_id=140238,(case o.value_coded when 1065 then "Yes" when 1066 then "No" else "" end),null)) as fever,
	max(if(o.concept_id=143264,(case o.value_coded when 1065 then "Yes" when 1066 then "No" else "" end),null)) as cough,
	max(if(o.concept_id=164441,(case o.value_coded when 1065 then "Yes" when 1066 then "No" else "" end),null)) as diffiulty_breathing,
	max(if(o.concept_id=162737,(case o.value_coded when 158843 then "Yes" when 1066 then "No" else "" end),null)) as sore_throat,
  max(if(o.concept_id=163336,(case o.value_coded when 126319 then "Yes" when 1066 then "No" else "" end),null)) as sneezing,
	max(if(o.concept_id=5219,(case o.value_coded when 139084 then "Yes" when 1066 then "No" else "" end),null)) as headache,
	max(if(o.concept_id=1788,(case o.value_coded when 140238 then "Yes" when 1066 then "No" when 1175 then "N/A" else "" end),null)) as referred_to_hosp,
  max(if(o.concept_id=159640,(case o.value_coded when 5006 then "Asymptomatic" when 1498 then "Mild" when 1499 then "Moderate" when 1500 then "Severe" when 164830 then "Critical" else "" end),null)) as case_classification,
	max(if(o.concept_id=162477,(case o.value_coded when 1065 then "Yes" when 1066 then "No" else "" end),null)) as patient_admitted,
	max(if(o.concept_id=161010,(case o.value_coded when 165994 then "Isolation" when 165995 then "HDU" when 161936 then "ICU" else "" end),null)) as admission_unit,
  group_concat(if(o.concept_id=159369,o.value_coded,null)) as treatment_received,  e.voided as voided
from encounter e
	inner join person p on p.person_id=e.patient_id and p.voided=0
	inner join
	(
		select form_id, uuid,name from form where
			uuid in('33a3aab6-73ae-11ea-bc55-0242ac130003')
	) f on f.form_id=e.form_id
	left outer join obs o on o.encounter_id=e.encounter_id and o.voided=0 and o.concept_id in (165416,5088,140238,143264,164441,162737,163336,5219,1788,159640,162477,161010,159369)
where e.voided=0
group by e.patient_id, e.encounter_id;


SELECT "Completed processing CCA covid treatment followup data ", CONCAT("Time: ", NOW());
END$$


--Populate etl covid ag-rdt test: CCA
DROP PROCEDURE IF EXISTS sp_populate_etl_cca_covid_rdt_test$$
CREATE PROCEDURE sp_populate_etl_cca_covid_rdt_test()
BEGIN
SELECT "Processing CCA covid ag-rdt test data", CONCAT("Time: ", NOW());
insert into kenyaemr_etl.etl_cca_covid_rdt_test(
	uuid,
	encounter_id,
	visit_id,
	patient_id,
	location_id,
	visit_date,
	encounter_provider,
	date_created,
	nationality,
	passport_id_number,
	sample_type,
	test_reason,
	ag_rdt_test_done,
	ag_rdt_test_date,
	case_type,
	assay_kit_name,
	ag_rdt_test_type_coded,
	ag_rdt_test_type_other,
	kit_lot_number,
	kit_expiry,
	test_result,
	action_taken,
	voided
)
select
	e.uuid,
	e.encounter_id as encounter_id,
	e.visit_id as visit_id,
	e.patient_id,
	e.location_id,
	date(e.encounter_datetime) as visit_date,
	e.creator as encounter_provider,
	e.date_created as date_created,
	max(if(o.concept_id=165847,cn.name,null)) as nationality,
	max(if(o.concept_id=163084,o.value_text,null)) as passport_id_number,
	max(if(o.concept_id=159959,(case o.value_coded when 163364 then "NP swab" when 163362 then "OP swab" when 162614 then "NP/OP swab" when 1004 then "Sputum" when 1001 then "Serum" else "" end),null)) as sample_type,
	max(if(o.concept_id=164126,(case o.value_coded when 1068 then "Symptomatic" when 165850 then "Contact with confirmed case" when 5619 then "Health care worker" when 162277 then "Prison remand" when 165631 then "High risk client in health facility" when 1143 then "Outbreak investigation" else "" end),null)) as test_reason,
	max(if(o.concept_id=165852,(case o.value_coded when 1065 then "Yes" when 1066 then "No" else "" end),null)) as ag_rdt_test_done,
	ag_rdt.ag_rdt_test_date,
	ag_rdt.case_type,
	ag_rdt.assay_kit_name,
	ag_rdt.ag_rdt_test_type_coded,
	ag_rdt.ag_rdt_test_type_other,
	ag_rdt.kit_lot_number,
	ag_rdt.kit_expiry,
	ag_rdt.test_result,
	ag_rdt.action_taken,
  e.voided
from encounter e
	inner join person p on p.person_id=e.patient_id and p.voided=0
	inner join form f on f.form_id=e.form_id and f.uuid in ('820cbf10-54cd-11ec-bf63-0242ac130002')
	left outer join obs o on o.encounter_id=e.encounter_id and o.voided=0 and o.concept_id in (165847,163084,159959,164126,165852)
	left join concept_name cn on cn.concept_id = o.value_coded and o.concept_id = 165847 and cn.concept_name_type='FULLY_SPECIFIED'
	left join (
		select
		o.obs_group_id obs_group_id,
		o.encounter_id,
		max(if(o.concept_id = 162078,date(o.value_datetime), null)) as ag_rdt_test_date,
		max(if(o.concept_id = 162084, case o.value_coded when 162080 then "Initial" when 162081 then "Repeat" else "" end, null)) as case_type,
		max(if(o.concept_id = 164963,o.value_text, null)) as assay_kit_name,
		max(if(o.concept_id = 1271,case o.value_coded when 165859 then "Panbio" when 165611 then "Standard" when 5622 then "Other" else "" end, null)) as ag_rdt_test_type_coded,
		max(if(o.concept_id = 165398,o.value_text, null)) as ag_rdt_test_type_other,
		max(if(o.concept_id = 166455, o.value_text, null)) as kit_lot_number,
		max(if(o.concept_id = 162502, date(o.value_datetime), null)) as kit_expiry,
		max(if(o.concept_id = 166638,case o.value_coded when 703 then "Positive" when 664 then "Negative" when 163611 then "Invalid" else "" end, null)) as test_result,
		max(if(o.concept_id = 1272,case o.value_coded when 159494 then "Referred to clinician" when 165093 then "Preventive counseling" else "" end, null)) as action_taken
	from obs o
		inner join person p on p.person_id=o.person_id and p.voided=0
		inner join encounter e on e.encounter_id = o.encounter_id and e.voided=0
		inner join form f on f.form_id=e.form_id and f.uuid in ('820cbf10-54cd-11ec-bf63-0242ac130002')
	where o.voided=0 and o.concept_id in(162078,162084,164963,1271,165398,166455,162502,166638,1272)  and e.voided=0 and o.obs_group_id is not null
	group by o.obs_group_id, o.encounter_id
	) ag_rdt on ag_rdt.encounter_id = e.encounter_id
where e.voided=0
group by e.patient_id, e.encounter_id;


SELECT "Completed processing CCA covid ag-rdt test data ", CONCAT("Time: ", NOW());
END$$

--Populate etl covid clinical review: CCA
DROP PROCEDURE IF EXISTS sp_populate_etl_cca_covid_clinical_review$$
CREATE PROCEDURE sp_populate_etl_cca_covid_clinical_review()
BEGIN
SELECT "Processing CCA covid clinical data", CONCAT("Time: ", NOW());
insert into kenyaemr_etl.etl_cca_covid_clinical_review(
      uuid,
      encounter_id,
      visit_id,
      patient_id ,
      location_id,
      visit_date,
      encounter_provider,
      date_created,
      ag_rdt_test_result,
      case_classification,
      action_taken,
      hospital_referred_to,
      case_id,
      email,
      case_type,
      pcr_sample_collection_date,
      pcr_result_date,
      pcr_result,
      case_classification_after_positive_pcr,
      action_taken_after_pcr_result,
      notes,
      voided
)
select
	e.uuid,
	e.encounter_id as encounter_id,
	e.visit_id as visit_id,
	e.patient_id,
	e.location_id,
	date(e.encounter_datetime) as visit_date,
	e.creator as encounter_provider,
	e.date_created as date_created,
	max(if(o.concept_id=165852,case o.value_coded when 703 then "Positive" when 664 then "Negative" else "" end,null)) as ag_rdt_test_result,
	max(if(o.concept_id=159640 and o.obs_group_id is null,case o.value_coded when 5006 then "Asymptomatic" when 1498 then "Mild" when 1499 then "Moderate" when 1500 then "Severe" when 164830 then "Critical" else "" end,null)) as case_classification,
	max(if(o.concept_id=1272,case o.value_coded when 165901 then "Referred to home based treatment/isolation" when 1654 then "Hospital admission" when 164165 then "Referred to other hospital for treatment" when 165611 then "Referred for PCR" else "" end,null)) as action_taken,
	max(if(o.concept_id=162724,o.value_text,null)) as hospital_referred_to,
	pcr_test.case_id,
	pcr_test.email,
	pcr_test.case_type,
	pcr_test.pcr_sample_collection_date,
	pcr_test.pcr_result_date,
	pcr_test.pcr_result,
	pcr_test.case_classification_after_positive_pcr,
	pcr_test.action_taken_after_pcr_result,
	max(if(o.concept_id=161011,o.value_text,null)) as notes,
  e.voided
from encounter e
	inner join person p on p.person_id=e.patient_id and p.voided=0
	inner join form f on f.form_id=e.form_id and f.uuid = '8fb6dabd-9c14-4d17-baac-97afaf3d203d'
	left outer join obs o on o.encounter_id=e.encounter_id and o.voided=0 and o.concept_id in (165852,159640,1272,162724,161011)
	left join (
		select
		o.obs_group_id obs_group_id,
		o.encounter_id,
		max(if(o.concept_id = 162576,o.value_text, null)) as case_id,
		max(if(o.concept_id = 162725,o.value_text, null)) as email,
		max(if(o.concept_id = 162084, case o.value_coded when 162080 then "Initial" when 162081 then "Repeat" else "" end, null)) as case_type,
		max(if(o.concept_id = 162078,date(o.value_datetime), null)) as pcr_sample_collection_date,
		max(if(o.concept_id = 162079,date(o.value_datetime), null)) as pcr_result_date,
		max(if(o.concept_id = 166638,case o.value_coded when 703 then "Positive" when 664 then "Negative" when 163611 then "Invalid" when 1118 then "Not done" else "" end, null)) as pcr_result,
		max(if(o.concept_id = 159640,case o.value_coded when 5006 then "Asymptomatic" when 1498 then "Mild" when 1499 then "Moderate" when 1500 then "Severe" when 164830 then "Critical" else "" end, null)) as case_classification_after_positive_pcr,
		max(if(o.concept_id = 160721,case o.value_coded when 165901 then "Referred to home based treatment/isolation" when 1654 then "Hospital admission" when 164165 then "Referred to other hospital for treatment" when 165611 then "Referred for PCR" when 165093 then "Preventive counseling" else "" end, null)) as action_taken_after_pcr_result
	from obs o
		inner join person p on p.person_id=o.person_id and p.voided=0
		inner join encounter e on e.encounter_id = o.encounter_id and e.voided=0
		inner join form f on f.form_id=e.form_id and f.uuid = '8fb6dabd-9c14-4d17-baac-97afaf3d203d'
	where o.voided=0 and o.concept_id in(162576,162725,162078,162084,162079,159640,166638,160721)  and e.voided=0 and o.obs_group_id is not null
	group by o.obs_group_id, o.encounter_id
	) pcr_test on pcr_test.encounter_id = e.encounter_id
where e.voided=0
group by e.patient_id, e.encounter_id;


SELECT "Completed processing CCA covid clinical review data ", CONCAT("Time: ", NOW());
END$$

--Populate etl covid treatment enrollment: CCA
DROP PROCEDURE IF EXISTS sp_populate_etl_cca_covid_treatment_enrollment$$
CREATE PROCEDURE sp_populate_etl_cca_covid_treatment_enrollment()
BEGIN
SELECT "Processing CCA covid enrollment data", CONCAT("Time: ", NOW());
insert into kenyaemr_etl.etl_cca_covid_treatment_enrollment(
      uuid,
      encounter_id,
      visit_id,
      patient_id ,
      location_id,
      visit_date,
      encounter_provider,
      date_created,
      passport_id_number,
      case_classification,
      patient_type,
      hospital_referred_from,
      date_tested_covid_positive,
      action_taken,
      admission_date,
      admission_unit,
      voided
)
select
	e.uuid,
	e.encounter_id as encounter_id,
	e.visit_id as visit_id,
	e.patient_id,
	e.location_id,
	date(e.encounter_datetime) as visit_date,
	e.creator as encounter_provider,
	e.date_created as date_created,
	max(if(o.concept_id=163084,o.value_text,null)) as passport_id_number,
	max(if(o.concept_id=159640 and o.obs_group_id is null,case o.value_coded when 5006 then "Asymptomatic" when 1498 then "Mild" when 1499 then "Moderate" when 1500 then "Severe" when 164830 then "Critical" else "" end,null)) as case_classification,
	max(if(o.concept_id=161641,case o.value_coded when 164144 then "New" when 160563 then "Transfer in" else "" end,null)) as patient_type,
	max(if(o.concept_id=161550,o.value_text,null)) as hospital_referred_from,
	max(if(o.concept_id=159948,date(o.value_datetime),null)) as date_tested_covid_positive,
	max(if(o.concept_id = 1272,case o.value_coded when 165901 then "Referred to home based treatment/isolation" when 1654 then "Hospital admission" else "" end, null)) as action_taken,
	max(if(o.concept_id=1640,date(o.value_datetime),null)) as admission_date,
	max(if(o.concept_id = 161010,case o.value_coded when 165994 then "Isolation" when 165995 then "HDU" when 161936 then "ICU" else "" end, null)) as admission_unit,
  	e.voided
from encounter e
	inner join person p on p.person_id=e.patient_id and p.voided=0
	inner join form f on f.form_id=e.form_id and f.uuid = '9a5d57b6-739a-11ea-bc55-0242ac130003'
	left outer join obs o on o.encounter_id=e.encounter_id and o.voided=0 and o.concept_id in (163084,159640,161641,161550,159948,1272,1640,161010)
where e.voided=0
group by e.patient_id, e.encounter_id;


SELECT "Completed processing CCA covid treatment enrollment data ", CONCAT("Time: ", NOW());
END$$


--Populate etl covid treatment enrollment outcome: CCA
DROP PROCEDURE IF EXISTS sp_populate_etl_cca_covid_treatment_enrollment_outcome$$
CREATE PROCEDURE sp_populate_etl_cca_covid_treatment_enrollment_outcome()
BEGIN
SELECT "Processing CCA covid enrollment outcome data", CONCAT("Time: ", NOW());
insert into kenyaemr_etl.etl_cca_covid_treatment_enrollment_outcome(
      uuid,
      encounter_id,
      visit_id,
      patient_id ,
      location_id,
      visit_date,
      encounter_provider,
      date_created,
      outcome,
      facility_transferred,
      facility_referred,
      comment,
      voided
)
select
	e.uuid,
	e.encounter_id as encounter_id,
	e.visit_id as visit_id,
	e.patient_id,
	e.location_id,
	date(e.encounter_datetime) as visit_date,
	e.creator as encounter_provider,
	e.date_created as date_created,
	max(if(o.concept_id=161555,case o.value_coded when 1663 then "Completed isolation" when 164165 then "Referral" when 160034 then "Died" when 160018 then "Never reached" when 159492 then "Transferred to another hospital" else "" end,null)) as outcome,
	max(if(o.concept_id=159495,o.value_text,null)) as facility_transferred,
	max(if(o.concept_id=161562,o.value_text,null)) as facility_referred,
	max(if(o.concept_id = 160632,o.value_text,null)) as comment,
	e.voided
from encounter e
	inner join person p on p.person_id=e.patient_id and p.voided=0
	inner join form f on f.form_id=e.form_id and f.uuid = '9a5d58c4-739a-11ea-bc55-0242ac130003'
	left outer join obs o on o.encounter_id=e.encounter_id and o.voided=0 and o.concept_id in (161555,159495,161562,160632)
where e.voided=0
group by e.patient_id, e.encounter_id;


SELECT "Completed processing CCA covid treatment enrollment outcome data ", CONCAT("Time: ", NOW());
END$$

		-- end of dml procedures

		SET sql_mode=@OLD_SQL_MODE $$

-- ------------------------------------------- running all procedures -----------------------------

DROP PROCEDURE IF EXISTS sp_build_cca_covid_tables $$
CREATE PROCEDURE sp_build_cca_covid_tables()
BEGIN
DECLARE populate_script_id INT(11);
SELECT "Beginning first time setup", CONCAT("Time: ", NOW());
INSERT INTO kenyaemr_etl.etl_script_status(script_name, start_time) VALUES('initial_population_of_covid_cca_tables', NOW());
SET populate_script_id = LAST_INSERT_ID();

CALL sp_populate_etl_cca_covid_screening();
CALL sp_populate_etl_cca_covid_treatment_followup();
CALL sp_populate_etl_cca_covid_rdt_test();
CALL sp_populate_etl_cca_covid_treatment_enrollment();
CALL sp_populate_etl_cca_covid_clinical_review();
CALL sp_populate_etl_cca_covid_treatment_enrollment_outcome();

UPDATE kenyaemr_etl.etl_script_status SET stop_time=NOW() where id= populate_script_id;

SELECT "Completed first time setup", CONCAT("Time: ", NOW());
END $$



