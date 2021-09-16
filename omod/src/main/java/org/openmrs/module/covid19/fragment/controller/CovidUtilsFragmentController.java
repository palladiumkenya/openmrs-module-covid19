/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.covid19.fragment.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.api.context.ContextAuthenticationException;
import org.openmrs.module.kenyaemr.util.EmrUtils;
import org.openmrs.module.kenyaui.annotation.PublicAction;
import org.openmrs.ui.framework.SimpleObject;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Fragment actions generally useful for Covid-19
 */
public class CovidUtilsFragmentController {
	
	protected static final Log log = LogFactory.getLog(CovidUtilsFragmentController.class);
	
	/**
	 * Checks if current user session is authenticated
	 * 
	 * @return simple object {authenticated: true/false}
	 */
	@PublicAction
	public SimpleObject isAuthenticated() {
		return SimpleObject.create("authenticated", Context.isAuthenticated());
	}
	
	/**
	 * Attempt to authenticate current user session with the given credentials
	 * 
	 * @param username the username
	 * @param password the password
	 * @return simple object {authenticated: true/false}
	 */
	@PublicAction
	public SimpleObject authenticate(@RequestParam(value = "username", required = false) String username,
	        @RequestParam(value = "password", required = false) String password) {
		try {
			Context.authenticate(username, password);
		}
		catch (ContextAuthenticationException ex) {
			// do nothing
		}
		return isAuthenticated();
	}
	
	/**
	 * COmposes covid asssessment outcomes such as : Have done a previous covid 19 Assessment
	 * assessment form Vaccination status || partially or fully Ever tested positive for Covid 19
	 */
	public SimpleObject recordedCovidAssessmentIndicators(
	        @RequestParam(value = "patientId", required = false) Integer patientId) {
		
		Integer VaccinationStatusQuestion = 163100;
		Integer CompletionStatusQuestion = 164134;
		Integer PartialVaccinationStatus = 166192;
		Integer FullVaccinationStatus = 5585;
		//Covid tests
		Integer CovidTestQuestion = 165852;
		Integer CovidResultQuestion = 166638;
		Integer CovidTestPositive = 703;
		Integer CovidTestNegative = 664;
		Integer YesConcept = 1065;
		
		boolean vaccinated = false;
		boolean fullyVaccinated = false;
		boolean partiallyVaccinated = false;
		boolean covidAssessed = false;
		boolean covidTested = false;
		boolean covidTestedPositive = false;
		boolean covidTestedNegative = false;
		PatientService patientService = Context.getPatientService();
		
		// Check clients with covid assessment encounter
		Encounter lastCovidAssessmentEncounter = EmrUtils.lastEncounter(Context.getPatientService().getPatient(patientId),
		    Context.getEncounterService().getEncounterTypeByUuid("86709cfc-1490-11ec-82a8-0242ac130003")); //last covid 19 assessment encounter
		if (lastCovidAssessmentEncounter != null) {
			covidAssessed = true;
			for (Obs obs : lastCovidAssessmentEncounter.getObs()) {
				if (obs.getConcept().getConceptId().equals(VaccinationStatusQuestion)
				        && (obs.getValueCoded().getConceptId().equals(YesConcept))) {
					vaccinated = true;
				}
				if (obs.getConcept().getConceptId().equals(CompletionStatusQuestion)
				        && (obs.getValueCoded().getConceptId().equals(PartialVaccinationStatus))) {
					partiallyVaccinated = true;
				}
				if (obs.getConcept().getConceptId().equals(CompletionStatusQuestion)
				        && (obs.getValueCoded().getConceptId().equals(FullVaccinationStatus))) {
					fullyVaccinated = true;
				}
				if (obs.getConcept().getConceptId().equals(CovidTestQuestion)
				        && (obs.getValueCoded().getConceptId().equals(YesConcept))) {
					covidTested = true;
				}
				if (obs.getConcept().getConceptId().equals(CovidResultQuestion)
				        && (obs.getValueCoded().getConceptId().equals(CovidTestPositive))) {
					covidTestedPositive = true;
				}
				if (obs.getConcept().getConceptId().equals(CovidResultQuestion)
				        && (obs.getValueCoded().getConceptId().equals(CovidTestNegative))) {
					covidTestedNegative = true;
				}
			}
		}
		
		return SimpleObject.create("covidAssessed", covidAssessed, "vaccinated", vaccinated, "fullyVaccinated",
		    fullyVaccinated, "partiallyVaccinated", partiallyVaccinated, "covidTestedPositive", covidTestedPositive,
		    "covidTestedNegative", covidTestedNegative, "covidTested", covidTested
		
		);
	}
}
