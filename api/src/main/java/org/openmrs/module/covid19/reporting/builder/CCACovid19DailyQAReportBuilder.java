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

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.openmrs.module.covid19.reporting.library.CCACovid19TreatmentIndicatorLibrary;
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

/**
 * Report builder for the daily quality indicators
 */
@Component
@Builds({ "kenyaemr.cca.daily.qa.report" })
public class CCACovid19DailyQAReportBuilder extends AbstractReportBuilder {
	
	@Autowired
	private CCACovid19TreatmentIndicatorLibrary covid19VaccinationIndicatorLibrary;
	
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	
	@Override
	protected List<Parameter> getParameters(ReportDescriptor reportDescriptor) {
		return Arrays.asList(new Parameter("startDate", "Start Date", Date.class));
	}
	
	@Override
	protected List<Mapped<DataSetDefinition>> buildDataSets(ReportDescriptor reportDescriptor,
	        ReportDefinition reportDefinition) {
		return Arrays.asList(ReportUtils.map(covid19QualityIndicatorsDataSet(), "startDate=${startDate}"));
	}
	
	protected DataSetDefinition covid19QualityIndicatorsDataSet() {
		CohortIndicatorDataSetDefinition cohortDsd = new CohortIndicatorDataSetDefinition();
		cohortDsd.setName("covid19CCATreatment");
		cohortDsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		String indParams = "startDate=${startDate}";
		cohortDsd.setDescription("Covid-19 CCA treatment report");
		
		/*
		cohortDsd.addColumn("Clients who came and were not screened for Covid", "",
		    ReportUtils.map(covid19VaccinationIndicatorLibrary.cic18AndAbove(), indParams), "");
		cohortDsd.addColumn("Clients who were eligible for Covid testing but were not tested", "",
		    ReportUtils.map(covid19VaccinationIndicatorLibrary.partiallyVaccinated(), indParams), "");
		cohortDsd.addColumn("Clients who were positive for Covid and were not referred for treatment", "",
		    ReportUtils.map(covid19VaccinationIndicatorLibrary.fullyVaccinated(), indParams), "");
		
		cohortDsd.addColumn("Clients on Covid treatment for at least 14 days and no outcome indicated", "",
		    ReportUtils.map(covid19VaccinationIndicatorLibrary.notVaccinated(), indParams), "");
		*/
		cohortDsd
		        .addColumn("All visits", "", ReportUtils.map(covid19VaccinationIndicatorLibrary.allVisits(), indParams), "");
		
		cohortDsd.addColumn("Screened for Covid symptoms", "",
		    ReportUtils.map(covid19VaccinationIndicatorLibrary.screenedForCovidSymptoms(), indParams), "");
		
		cohortDsd.addColumn("Not screened for covid symptoms", "",
		    ReportUtils.map(covid19VaccinationIndicatorLibrary.notScreenedForCovidSymptoms(), indParams), "");
		
		cohortDsd.addColumn("Clients eligible for Covid testing", "",
		    ReportUtils.map(covid19VaccinationIndicatorLibrary.eligibleForCovidTest(), indParams), "");
		
		cohortDsd.addColumn("Clients consented for Covid testing", "",
		    ReportUtils.map(covid19VaccinationIndicatorLibrary.consentedForCovidTest(), indParams), "");
		
		cohortDsd.addColumn("Clients who declined Covid testing", "",
		    ReportUtils.map(covid19VaccinationIndicatorLibrary.declinedCovidTest(), indParams), "");
		
		cohortDsd.addColumn("Clients tested for COVID using Ag RDT", "",
		    ReportUtils.map(covid19VaccinationIndicatorLibrary.testedUsingAgRDT(), indParams), "");
		
		cohortDsd.addColumn("Clients who tested positive using Ag RDT", "",
		    ReportUtils.map(covid19VaccinationIndicatorLibrary.agRDTPositive(), indParams), "");
		
		cohortDsd.addColumn("Clients tested for COVID using PCR", "",
		    ReportUtils.map(covid19VaccinationIndicatorLibrary.pcrCovidTest(), indParams), "");
		
		cohortDsd.addColumn("Clients enrolled into hospital", "",
		    ReportUtils.map(covid19VaccinationIndicatorLibrary.hospitalTreatment(), indParams), "");
		
		cohortDsd.addColumn("Clients enrolled to home-based care", "",
		    ReportUtils.map(covid19VaccinationIndicatorLibrary.homeIsolation(), indParams), "");
		
		cohortDsd.addColumn("Clients referred to other facilities", "",
		    ReportUtils.map(covid19VaccinationIndicatorLibrary.referralsToOtherFacilities(), indParams), "");
		
		cohortDsd.addColumn("Clients transfer in from another facility", "",
		    ReportUtils.map(covid19VaccinationIndicatorLibrary.referralsFromOtherFacilities(), indParams), "");
		
		/**
		 * Clients who had a visits (Workload) · Clients who were screened · Clients eligible for
		 * Covid testing · Clients testing for Covid using ag RDT · Clients who were tested Positive
		 * using ag. RDT · Clients testing for Covid using a PCR · Clients who were tested Positive
		 * using PCR · Clients Positive for Covid (PCR/RDT) · Clients enrolled into Hospital ·
		 * Clients enrolled to Home based Care · Clients referred to other facility · Clients
		 * transfer in from another facility · Clients with a covid treatment Outcome completed (by
		 * type of Outcome)
		 */
		return cohortDsd;
		
	}
}
