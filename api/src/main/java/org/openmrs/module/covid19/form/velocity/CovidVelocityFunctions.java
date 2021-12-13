/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.covid19.form.velocity;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.calculation.result.CalculationResult;
import org.openmrs.module.covid19.ModuleConstants;
import org.openmrs.module.covid19.calculation.library.covid.CovidVelocityCalculation;
import org.openmrs.module.htmlformentry.FormEntrySession;
import org.openmrs.module.kenyaemr.calculation.EmrCalculationUtils;
import org.openmrs.module.kenyaemr.util.EmrUtils;

/**
 * Velocity functions for adding logic to HTML forms
 */
public class CovidVelocityFunctions {
	
	private FormEntrySession session;
	
	protected static final Log log = LogFactory.getLog(CovidVelocityFunctions.class);
	
	/**
	 * Constructs a new functions provider
	 * 
	 * @param session the form entry session
	 */
	public CovidVelocityFunctions(FormEntrySession session) {
		this.session = session;
	}
	
	/**
	 * Calculates a consolidation of covid asssessment validations such as : Have done a previous
	 * covid 19 Assessment assessment form Vaccination status || partially or fully Ever tested
	 * positive for Covid 19
	 */
	
	public String CovidVelocityCalculation() {
		
		CalculationResult covid19Velocity = EmrCalculationUtils.evaluateForPatient(CovidVelocityCalculation.class, null,
		    session.getPatient());
		return (String) covid19Velocity.getValue();
		
	}
	
	public String lastDocumentedIsolationLocation() {
		
		int isolationQuestionConceptId = 1272;
		int homebaseIsolationConceptId = 165901;
		int hospitalIsolationConceptId = 164165;
		if (session.getPatient() == null) {
			return "";
		} else {
			Encounter lastEnrollmentEnc = EmrUtils.lastEncounter(session.getPatient(),
			    ModuleConstants.covidEnrollmentEncType, ModuleConstants.covidEnrollmentForm);
			if (lastEnrollmentEnc != null) {
				for (Obs o : lastEnrollmentEnc.getObs()) {
					if (o.getConcept().getConceptId().equals(isolationQuestionConceptId)) {
						if (o.getValueCoded().getConceptId().equals(homebaseIsolationConceptId)) {
							return "home_isolation";
						} else if (o.getValueCoded().getConceptId().equals(hospitalIsolationConceptId)) {
							return "hospital_isolation";
						}
					}
				}
			}
		}
		return "";
	}
	
}
