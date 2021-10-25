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
import org.openmrs.module.reporting.indicator.CohortIndicator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.openmrs.module.kenyacore.report.ReportUtils.map;
import static org.openmrs.module.kenyaemr.reporting.EmrReportingUtils.cohortIndicator;

/**
 * Library of Covid-19 vaccinations
 */
@Component
public class Covid19VaccinationIndicatorLibrary {
	
	@Autowired
	private Covid19VaccinationCohortLibrary cohortLibrary;
	
	/**
	 * Number of patients fully vaccinated
	 * 
	 * @return the indicator
	 */
	public CohortIndicator fullyVaccinated() {
		return cohortIndicator("Fully vaccinated",
		    ReportUtils.map(cohortLibrary.onArtFullyVaccinated(), "startDate=${startDate},endDate=${endDate}"));
	}
	
	/**
	 * Number of patients partially vaccinated
	 * 
	 * @return
	 */
	public CohortIndicator partiallyVaccinated() {
		return cohortIndicator("Partially vaccinated",
		    ReportUtils.map(cohortLibrary.onArtPartiallyVaccinated(), "startDate=${startDate},endDate=${endDate}"));
	}
	
	/**
	 * Number of patients not vaccinated for Covid-19
	 * 
	 * @return
	 */
	public CohortIndicator notVaccinated() {
		return cohortIndicator("Not vaccinated",
		    ReportUtils.map(cohortLibrary.onArtNotVaccinatedCovid19(), "startDate=${startDate},endDate=${endDate}"));
	}
	
	/**
	 * Number of patients on ART and 18+ years old
	 * 
	 * @return the indicator
	 */
	public CohortIndicator onArt18AndAbove() {
		return cohortIndicator("On ART and 18+ years",
		    ReportUtils.map(cohortLibrary.onArtAged18AndAbove(), "startDate=${startDate},endDate=${endDate}"));
	}
	
	/**
	 * Number of patients with verified 1st dose
	 * 
	 * @return the indicator
	 */
	public CohortIndicator doseOneVerified() {
		return cohortIndicator("Verified 1st Dose",
		    ReportUtils.map(cohortLibrary.onArtFirstDoseVerified(), "startDate=${startDate},endDate=${endDate}"));
	}
	
	/**
	 * Number of patients with verified 2nd dose
	 * 
	 * @return the indicator
	 */
	public CohortIndicator doseTwoVerified() {
		return cohortIndicator("Verified 2nd Dose",
		    ReportUtils.map(cohortLibrary.onArtSecondDoseVerified(), "startDate=${startDate},endDate=${endDate}"));
	}
	
	/**
	 * Number of patients with verified booster dose
	 * 
	 * @return the indicator
	 */
	public CohortIndicator boosterDoseVerified() {
		return cohortIndicator("Verified booster Dose",
		    ReportUtils.map(cohortLibrary.onArtBoosterDoseVerified(), "startDate=${startDate},endDate=${endDate}"));
	}
	
	/**
	 * Number of patients Ever infected with Covid-19
	 * 
	 * @return the indicator
	 */
	public CohortIndicator everInfectedWithCovid19() {
		return cohortIndicator("Ever infected with Covid-19",
		    ReportUtils.map(cohortLibrary.onArtEverInfected(), "startDate=${startDate},endDate=${endDate}"));
	}
	
	/**
	 * Number of patients Admitted to hospital due to Covid-19 complications
	 * 
	 * @return the indicator
	 */
	public CohortIndicator hospitalAdmission() {
		return cohortIndicator("Admitted to hospital due to Covid-19",
		    ReportUtils.map(cohortLibrary.onArtEverHospitalised(), "startDate=${startDate},endDate=${endDate}"));
	}
	
	/**
	 * Number of patients who died of Covid-19
	 * 
	 * @return the indicator
	 */
	public CohortIndicator diedOfCovid19() {
		return cohortIndicator("Died of Covid-19",
		    ReportUtils.map(cohortLibrary.diedDueToCovid(), "startDate=${startDate},endDate=${endDate}"));
	}
	
}
