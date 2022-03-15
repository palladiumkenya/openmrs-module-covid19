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

import static org.openmrs.module.kenyaemr.reporting.EmrReportingUtils.cohortIndicator;

import org.openmrs.module.kenyacore.report.ReportUtils;
import org.openmrs.module.reporting.evaluation.parameter.Parameterizable;
import org.openmrs.module.reporting.indicator.CohortIndicator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Indicator library for CCA cohorts
 */
@Component
public class CCACovid19TreatmentIndicatorLibrary {
	
	@Autowired
	private CCACovid19TreatmentCohortLibrary cohortLibrary;
	
	/**
	 * Number of patients fully vaccinated
	 * 
	 * @return the indicator
	 */
	public CohortIndicator allVisits() {
		return cohortIndicator("All visits", ReportUtils.map(cohortLibrary.allVisits(), "startDate=${startDate}"));
	}
	
	/**
	 * Number of patients who were screened for covid symptoms
	 * 
	 * @return
	 */
	public CohortIndicator screenedForCovidSymptoms() {
		return cohortIndicator("Screened for covid symptoms",
		    ReportUtils.map(cohortLibrary.screenedForCovidSymptoms(), "startDate=${startDate}"));
	}
	
	/**
	 * Number who visited but wasn't screened for covid symptoms
	 * 
	 * @return
	 */
	public CohortIndicator notScreenedForCovidSymptoms() {
		return cohortIndicator("Not screened for covid symptoms",
		    ReportUtils.map(cohortLibrary.patientsNotScreenedForCovidSymptoms(), "startDate=${startDate}"));
	}
	
	/**
	 * Clients who were screened and were eligible for covid testing
	 * 
	 * @return
	 */
	public CohortIndicator eligibleForCovidTest() {
		return cohortIndicator("eligible for covid testing",
		    ReportUtils.map(cohortLibrary.eligibleForCovidTest(), "startDate=${startDate}"));
	}
	
	/**
	 * Clients who were tested for took ag rdt test
	 * 
	 * @return
	 */
	public CohortIndicator testedUsingAgRDT() {
		return cohortIndicator("had Ag RDT test",
		    ReportUtils.map(cohortLibrary.testedUsingAgRDT(), "startDate=${startDate}"));
	}
	
	/**
	 * Clients who turned positive for ag rdt
	 * 
	 * @return
	 */
	public CohortIndicator agRDTPositive() {
		return cohortIndicator("positive Ag RDT test",
		    ReportUtils.map(cohortLibrary.agRDTPositive(), "startDate=${startDate}"));
	}
	
	/**
	 * Clients who had pcr tests
	 * 
	 * @return
	 */
	public CohortIndicator pcrCovidTest() {
		return cohortIndicator("pcr test", ReportUtils.map(cohortLibrary.pcrCovidTest(), "startDate=${startDate}"));
	}
	
	/**
	 * Clients who had positive pcr results
	 * 
	 * @return
	 */
	public CohortIndicator positivePcrCovidTest() {
		return cohortIndicator("pcr test", ReportUtils.map(cohortLibrary.positivePcrCovidTest(), "startDate=${startDate}"));
	}
	
	/**
	 * Clients who were enrolled/admitted in hospital for covid treatment
	 * 
	 * @return
	 */
	public CohortIndicator hospitalTreatment() {
		return cohortIndicator("hospital covid treatment",
		    ReportUtils.map(cohortLibrary.hospitalTreatment(), "startDate=${startDate}"));
	}
	
	/**
	 * Clients who were enrolled for home based care
	 * 
	 * @return
	 */
	public CohortIndicator homeIsolation() {
		return cohortIndicator("home covid treatment isolation",
		    ReportUtils.map(cohortLibrary.homeIsolation(), "startDate=${startDate}"));
	}
	
	/**
	 * Clients who are referred for covid treatment from other facilities
	 * 
	 * @return
	 */
	public CohortIndicator referralsFromOtherFacilities() {
		return cohortIndicator("referrals from other facilities",
		    ReportUtils.map(cohortLibrary.referralsFromOtherFacilities(), "startDate=${startDate}"));
	}
	
	/**
	 * Clients who are referred for covid treatment to other facilities
	 * 
	 * @return
	 */
	public CohortIndicator referralsToOtherFacilities() {
		return cohortIndicator("referrals to other facilities",
		    ReportUtils.map(cohortLibrary.referralsToOtherFacilities(), "startDate=${startDate}"));
	}
	
	/**
	 * Clients who were screened, eligible, and consented for covid testing
	 * 
	 * @return
	 */
	public CohortIndicator consentedForCovidTest() {
		return cohortIndicator("consented for covid testing",
		    ReportUtils.map(cohortLibrary.consentedForCovidTest(), "startDate=${startDate}"));
	}
	
	/**
	 * Clients who were screened, eligible, but declined covid testing
	 * 
	 * @return
	 */
	public CohortIndicator declinedCovidTest() {
		return cohortIndicator("declined covid testing",
		    ReportUtils.map(cohortLibrary.declinedCovidTest(), "startDate=${startDate}"));
	}
}
