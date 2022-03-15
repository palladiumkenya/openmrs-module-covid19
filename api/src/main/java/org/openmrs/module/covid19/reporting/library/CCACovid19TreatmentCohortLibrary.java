/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.covid19.reporting.library;

import java.util.Date;

import org.openmrs.module.kenyacore.report.ReportUtils;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CompositionCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.SqlCohortDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.evaluation.parameter.Parameterizable;
import org.springframework.stereotype.Component;

/**
 * Library of cohort definitions for Covid-19 CCA covid treatment
 */
@Component
public class CCACovid19TreatmentCohortLibrary {
	
	/**
	 * All visits on a day
	 * 
	 * @return
	 */
	public CohortDefinition allVisits() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select patient_id from visit where voided = 0 and date(date_started) = date(:startDate) \n";
		
		cd.setName("allVisits");
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.setQuery(sqlQuery);
		cd.setDescription("All visits");
		
		return cd;
	}
	
	public CohortDefinition screenedForCovidSymptoms() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select patient_id from kenyaemr_etl.etl_cca_covid_screening where voided = 0 and date(visit_date) = date(:startDate) \n";
		
		cd.setName("screenedForCovidSymptoms;");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.setDescription("screenedForCovidSymptoms");
		
		return cd;
	}
	
	public CohortDefinition eligibleForCovidTest() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select patient_id from kenyaemr_etl.etl_cca_covid_screening where voided = 0 and date(visit_date) = date(:startDate) and eligible_for_covid_test = 'Yes';";
		cd.setName("eligibleForRDT;");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.setDescription("eligibleForRDT");
		
		return cd;
	}
	
	public CohortDefinition testedUsingAgRDT() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select patient_id from kenyaemr_etl.etl_cca_covid_rdt_test where voided = 0 and date(visit_date) = date(:startDate) and test_result is not null ";
		cd.setName("rdtTest");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.setDescription("rdtTest");
		
		return cd;
	}
	
	public CohortDefinition agRDTPositive() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select patient_id from kenyaemr_etl.etl_cca_covid_rdt_test where voided = 0 and date(visit_date) = date(:startDate) and test_result = 'Positive' ";
		cd.setName("positiveRDT");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.setDescription("positiveRDT");
		
		return cd;
	}
	
	public CohortDefinition pcrCovidTest() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select patient_id from kenyaemr_etl.etl_cca_covid_clinical_review where voided = 0 and date(visit_date) = date(:startDate) and pcr_sample_collection_date is not null ";
		cd.setName("pcrCovidTest");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.setDescription("pcrCovidTest");
		
		return cd;
	}
	
	public CohortDefinition positivePcrCovidTest() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select patient_id from kenyaemr_etl.etl_cca_covid_clinical_review where voided = 0 and date(visit_date) = date(:startDate) and pcr_result = 'Positive' ";
		cd.setName("positivePcrCovidTest");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.setDescription("positivePcrCovidTest");
		
		return cd;
	}
	
	public CohortDefinition hospitalTreatment() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select patient_id from kenyaemr_etl.etl_cca_covid_treatment_enrollment where voided = 0 and date(visit_date) = date(:startDate) and action_taken = 'Hospital admission';\n";
		cd.setName("hospitalTreatment");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.setDescription("hospitalTreatment");
		
		return cd;
	}
	
	public CohortDefinition homeIsolation() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select patient_id from kenyaemr_etl.etl_cca_covid_treatment_enrollment where voided = 0 and date(visit_date) = date(:startDate) and action_taken = 'Referred to home based treatment/isolation';\n";
		cd.setName("homeIsolation");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.setDescription("homeIsolation");
		
		return cd;
	}
	
	public CohortDefinition referralsFromOtherFacilities() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select patient_id from kenyaemr_etl.etl_cca_covid_treatment_enrollment where voided = 0 and date(visit_date) = date(:startDate) and hospital_referred_from is not null;\n";
		
		cd.setName("referralsFromOtherFacilities");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.setDescription("referralsFromOtherFacilities");
		
		return cd;
	}
	
	public CohortDefinition referralsToOtherFacilities() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select patient_id from kenyaemr_etl.etl_cca_covid_clinical_review where voided = 0 and date(visit_date) = date(:startDate) and action_taken = 'Referred to other hospital for treatment' ";
		cd.setName("referralsToOtherFacilities");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.setDescription("referralsToOtherFacilities");
		
		return cd;
	}
	
	/**
	 * Patients who visited but were not screened for covid
	 * 
	 * @return the cohort definition
	 */
	public CohortDefinition patientsNotScreenedForCovidSymptoms() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addSearch("visitedToday", ReportUtils.map(allVisits(), "startDate=${startDate}"));
		cd.addSearch("screendForCovid", ReportUtils.map(screenedForCovidSymptoms(), "startDate=${startDate}"));
		cd.setCompositionString("visitedToday AND NOT screendForCovid");
		return cd;
	}
	
	/**
	 * Consented for covid test
	 * 
	 * @return
	 */
	public CohortDefinition consentedForCovidTest() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select patient_id from kenyaemr_etl.etl_cca_covid_screening where voided = 0 and date(visit_date) = date(:startDate) and consented_for_covid_test = 'Yes';";
		cd.setName("consentedForCovidTest;");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.setDescription("consentedForCovidTest");
		
		return cd;
	}
	
	/**
	 * Declined covid test
	 * 
	 * @return
	 */
	public CohortDefinition declinedCovidTest() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select patient_id from kenyaemr_etl.etl_cca_covid_screening where voided = 0 and date(visit_date) = date(:startDate) and consented_for_covid_test = 'No';";
		cd.setName("declinedCovidTest;");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.setDescription("declinedCovidTest");
		
		return cd;
	}
}
