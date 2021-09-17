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
		    ReportUtils.map(cohortLibrary.fullyVaccinated(), "startDate=${startDate},endDate=${endDate}"));
	}
	
	/**
	 * Number of patients partially vaccinated
	 * 
	 * @return
	 */
	public CohortIndicator partiallyVaccinated() {
		return cohortIndicator("Partially vaccinated",
		    ReportUtils.map(cohortLibrary.partiallyVaccinated(), "startDate=${startDate},endDate=${endDate}"));
	}
	
	/**
	 * Number of patients not vaccinated
	 * 
	 * @return the indicator
	 */
	public CohortIndicator notVaccinated() {
		return cohortIndicator("Not vaccinated",
		    ReportUtils.map(cohortLibrary.notVaccinated(), "startDate=${startDate},endDate=${endDate}"));
	}
	
}
