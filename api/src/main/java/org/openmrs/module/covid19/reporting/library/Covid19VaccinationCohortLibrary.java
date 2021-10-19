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

import org.openmrs.module.kenyacore.report.ReportUtils;
import org.openmrs.module.kenyaemr.reporting.library.ETLReports.RevisedDatim.DatimCohortLibrary;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CompositionCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.SqlCohortDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Library of cohort definitions for Covid-19 vaccinations
 */
@Component
public class Covid19VaccinationCohortLibrary {
	
	@Autowired
	private DatimCohortLibrary datimCohortLibrary;
	
	public CohortDefinition fullyVaccinated() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select patient_id from kenyaemr_etl.etl_covid19_assessment\n"
		        + "group by patient_id\n"
		        + "having mid(max(concat(visit_date,final_vaccination_status)),11) = 5585 and mid(max(concat(visit_date,ever_vaccinated)),11) is not null\n"
		        + "and mid(max(concat(visit_date,date(second_dose_date))),11) between date(:startDate) and :endDate \n"
		        + "or (mid(max(concat(visit_date,first_vaccine_type)),11)= 166355 and mid(max(concat(visit_date,date(first_dose_date))),11) between date(:startDate) and :endDate);\n";
		cd.setName("fullyVaccinated");
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setQuery(sqlQuery);
		cd.setDescription("fullyVaccinated");
		
		return cd;
	}
	
	public CohortDefinition partiallyVaccinated() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select patient_id from kenyaemr_etl.etl_covid19_assessment\n"
		        + "group by patient_id\n"
		        + "having mid(max(concat(visit_date,final_vaccination_status)),11) = 166192 and mid(max(concat(visit_date,ever_vaccinated)),11) is not null\n"
		        + "and mid(max(concat(visit_date,date(first_dose_date))),11) between date(:startDate) and :endDate and mid(max(concat(visit_date,first_vaccine_type)),11) <> 166355;\n";
		cd.setName("partiallyVaccinated;");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setDescription("partiallyVaccinated");
		
		return cd;
	}
	
	public CohortDefinition everInfected() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select patient_id from kenyaemr_etl.etl_covid19_assessment where ever_tested_covid_19_positive = 703 and ever_vaccinated is not null\n"
		        + "group by patient_id;";
		cd.setName("everInfected;");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setDescription("everInfected");
		
		return cd;
	}
	
	public CohortDefinition everHospitalised() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select patient_id from kenyaemr_etl.etl_covid19_assessment where hospital_admission = 1065 and ever_vaccinated is not null;\n";
		cd.setName("everHospitalised");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setDescription("everHospitalised");
		
		return cd;
	}
	
	public CohortDefinition diedDueToCovid() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select patient_id from kenyaemr_etl.etl_patient_program_discontinuation where discontinuation_reason =160034 and specific_death_cause=165609\n"
		        + "and visit_date between date(:startDate) and :endDate;";
		cd.setName("diedDueToCovid");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setDescription("diedDueToCovid");
		
		return cd;
	}
	
	public CohortDefinition aged18AndAbove() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select patient_id from kenyaemr_etl.etl_patient_demographics where timestampdiff(YEAR ,dob,date(:endDate))>= 18;\n";
		cd.setName("aged18andAbove");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setDescription("aged18andAbove");
		
		return cd;
	}
	
	public CohortDefinition firstDoseVerifiedSQl() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = " select patient_id from kenyaemr_etl.etl_covid19_assessment where first_vaccination_verified = 164134 and ever_vaccinated is not null and\n"
		        + "        first_dose_date between date(:startDate) and :endDate;";
		cd.setName("firstDose");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setDescription("firstDose");
		
		return cd;
	}
	
	public CohortDefinition secondDoseVerifiedSQL() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "select patient_id from kenyaemr_etl.etl_covid19_assessment where second_vaccination_verified = 164134 and ever_vaccinated is not null and\n"
		        + "        second_dose_date between date(:startDate) and :endDate;";
		cd.setName("secondDose");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setDescription("secondDose");
		
		return cd;
	}
	
	public CohortDefinition boosterDoseVerifiedSQL() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = " select patient_id from kenyaemr_etl.etl_covid19_assessment where booster_dose_verified = 164134 and ever_vaccinated is not null and\n"
		        + "        date_taken_booster_vaccine between date(:startDate) and :endDate;";
		cd.setName("boosterDose");
		cd.setQuery(sqlQuery);
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.setDescription("boosterDose");
		
		return cd;
	}
	
	/**
	 * Patients OnArt and partially vaccinated
	 * 
	 * @return the cohort definition
	 */
	public CohortDefinition onArtPartiallyVaccinated() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addSearch("txcurr",
		    ReportUtils.map(datimCohortLibrary.currentlyOnArt(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("partiallyVaccinated",
		    ReportUtils.map(partiallyVaccinated(), "startDate=${startDate},endDate=${endDate}"));
		cd.setCompositionString("txcurr AND partiallyVaccinated");
		return cd;
	}
	
	/**
	 * Patients OnArt and fully vaccinated
	 * 
	 * @return the cohort definition
	 */
	public CohortDefinition onArtFullyVaccinated() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addSearch("txcurr",
		    ReportUtils.map(datimCohortLibrary.currentlyOnArt(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("fullyVaccinated", ReportUtils.map(fullyVaccinated(), "startDate=${startDate},endDate=${endDate}"));
		cd.setCompositionString("txcurr AND fullyVaccinated");
		return cd;
	}
	
	/**
	 * Patients OnArt and ever infected
	 * 
	 * @return the cohort definition
	 */
	public CohortDefinition onArtEverInfected() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addSearch("txcurr",
		    ReportUtils.map(datimCohortLibrary.currentlyOnArt(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("everInfected", ReportUtils.map(everInfected(), "startDate=${startDate},endDate=${endDate}"));
		cd.setCompositionString("txcurr AND everInfected");
		return cd;
	}
	
	/**
	 * Patients OnArt and ever admitted to hospital due to covid
	 * 
	 * @return the cohort definition
	 */
	public CohortDefinition onArtEverHospitalised() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addSearch("txcurr",
		    ReportUtils.map(datimCohortLibrary.currentlyOnArt(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("everHospitalised", ReportUtils.map(everHospitalised(), "startDate=${startDate},endDate=${endDate}"));
		cd.setCompositionString("txcurr AND everHospitalised");
		return cd;
	}
	
	/**
	 * Patients OnArt and 18 years and above
	 * 
	 * @return the cohort definition
	 */
	public CohortDefinition onArtAged18AndAbove() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addSearch("txcurr",
		    ReportUtils.map(datimCohortLibrary.currentlyOnArt(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("aged18andAbove", ReportUtils.map(aged18AndAbove(), "startDate=${startDate},endDate=${endDate}"));
		cd.setCompositionString("txcurr AND aged18andAbove");
		return cd;
	}
	
	/**
	 * Patients with first dose verified
	 * 
	 * @return the cohort definition
	 */
	public CohortDefinition onArtFirstDoseVerified() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addSearch("txcurr",
		    ReportUtils.map(datimCohortLibrary.currentlyOnArt(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("firstDoseVerified",
		    ReportUtils.map(firstDoseVerifiedSQl(), "startDate=${startDate},endDate=${endDate}"));
		cd.setCompositionString("txcurr AND firstDoseVerified");
		return cd;
	}
	
	/**
	 * Patients with second dose verified
	 * 
	 * @return the cohort definition
	 */
	public CohortDefinition onArtSecondDoseVerified() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addSearch("txcurr",
		    ReportUtils.map(datimCohortLibrary.currentlyOnArt(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("secondDoseVerified",
		    ReportUtils.map(secondDoseVerifiedSQL(), "startDate=${startDate},endDate=${endDate}"));
		cd.setCompositionString("txcurr AND secondDoseVerified");
		return cd;
	}
	
	/**
	 * Patients with booster dose verified
	 * 
	 * @return the cohort definition
	 */
	public CohortDefinition onArtBoosterDoseVerified() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addSearch("txcurr",
		    ReportUtils.map(datimCohortLibrary.currentlyOnArt(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("boosterDoseVerified",
		    ReportUtils.map(boosterDoseVerifiedSQL(), "startDate=${startDate},endDate=${endDate}"));
		cd.setCompositionString("txcurr AND boosterDoseVerified");
		return cd;
	}
}
