/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.covid19.reporting.builder;

import org.openmrs.module.covid19.reporting.library.Covid19VaccinationIndicatorLibrary;
import org.openmrs.module.kenyacore.report.ReportDescriptor;
import org.openmrs.module.kenyacore.report.ReportUtils;
import org.openmrs.module.kenyacore.report.builder.AbstractReportBuilder;
import org.openmrs.module.kenyacore.report.builder.Builds;
import org.openmrs.module.reporting.dataset.definition.CohortIndicatorDataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Report builder for Covid-19 vaccinations report
 */
@Component
@Builds({ "kenyaemr.hiv.report.covid19vaccination" })
public class Covid19VaccinationReportBuilder extends AbstractReportBuilder {
	
	@Autowired
	private Covid19VaccinationIndicatorLibrary covid19VaccinationIndicatorLibrary;
	
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	
	@Override
	protected List<Parameter> getParameters(ReportDescriptor reportDescriptor) {
		return Arrays.asList(new Parameter("startDate", "Start Date", Date.class), new Parameter("endDate", "End Date",
		        Date.class), new Parameter("dateBasedReporting", "", String.class));
	}
	
	@Override
	protected List<Mapped<DataSetDefinition>> buildDataSets(ReportDescriptor reportDescriptor,
	        ReportDefinition reportDefinition) {
		return Arrays.asList(ReportUtils.map(covid19Vaccination(), "startDate=${startDate},endDate=${endDate}"));
	}
	
	protected DataSetDefinition covid19Vaccination() {
		CohortIndicatorDataSetDefinition cohortDsd = new CohortIndicatorDataSetDefinition();
		cohortDsd.setName("covid19Vaccination");
		cohortDsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cohortDsd.addParameter(new Parameter("endDate", "End Date", Date.class));
		String indParams = "startDate=${startDate},endDate=${endDate}";
		cohortDsd.setDescription("Covid-19 vaccination report");
		
		cohortDsd.addColumn("On ART 18+ years", "",
		    ReportUtils.map(covid19VaccinationIndicatorLibrary.onArt18AndAbove(), indParams), "");
		cohortDsd.addColumn("Partially vaccinated", "",
		    ReportUtils.map(covid19VaccinationIndicatorLibrary.partiallyVaccinated(), indParams), "");
		cohortDsd.addColumn("Fully vaccinated", "",
		    ReportUtils.map(covid19VaccinationIndicatorLibrary.fullyVaccinated(), indParams), "");
		
		cohortDsd.addColumn("Not vaccinated", "",
		    ReportUtils.map(covid19VaccinationIndicatorLibrary.notVaccinated(), indParams), "");
		
		cohortDsd.addColumn("Unknown vaccination status", "",
		    ReportUtils.map(covid19VaccinationIndicatorLibrary.unknownVaccinationStatus(), indParams), "");
		
		cohortDsd.addColumn("Verified 1st Dose", "",
		    ReportUtils.map(covid19VaccinationIndicatorLibrary.doseOneVerified(), indParams), "");
		
		cohortDsd.addColumn("Verified 2nd Dose", "",
		    ReportUtils.map(covid19VaccinationIndicatorLibrary.doseTwoVerified(), indParams), "");
		
		cohortDsd.addColumn("Verified booster dose", "",
		    ReportUtils.map(covid19VaccinationIndicatorLibrary.boosterDoseVerified(), indParams), "");
		
		cohortDsd.addColumn("Ever infected with Covid-19", "",
		    ReportUtils.map(covid19VaccinationIndicatorLibrary.everInfectedWithCovid19(), indParams), "");
		
		cohortDsd.addColumn("Admitted to hospital due to Covid-19", "",
		    ReportUtils.map(covid19VaccinationIndicatorLibrary.hospitalAdmission(), indParams), "");
		
		cohortDsd.addColumn("Died of Covid-19", "",
		    ReportUtils.map(covid19VaccinationIndicatorLibrary.diedOfCovid19(), indParams), "");
		
		return cohortDsd;
		
	}
}
