/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.covid19.calculation.library.covid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.calculation.result.SimpleResult;
import org.openmrs.module.kenyacore.calculation.Calculations;
import org.openmrs.module.kenyacore.calculation.Filters;
import org.openmrs.module.kenyaemr.calculation.BaseEmrCalculation;
import org.openmrs.module.kenyaemr.calculation.EmrCalculationUtils;
import org.openmrs.module.kenyaemr.util.EmrUtils;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Calculates a consolidation of covid asssessment validations such as : Have done a previous covid
 * 19 Assessment assessment form Vaccination status || partially or fully Ever tested positive for
 * Covid 19
 */
public class CovidVelocityCalculation extends BaseEmrCalculation {
	
	protected static final Log log = LogFactory.getLog(CovidVelocityCalculation.class);
	
	static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MMM-yyyy");
	
	@Override
	public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> parameterValues,
	        PatientCalculationContext context) {
		
		Set<Integer> alive = Filters.alive(cohort, context);
		//Vaccination
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
		
		CalculationResultMap ret = new CalculationResultMap();
		StringBuilder sb = new StringBuilder();
		for (Integer ptId : cohort) {
			boolean vaccinated = false;
			boolean fullyVaccinated = false;
			boolean partiallyVaccinated = false;
			boolean covidAssessed = false;
			boolean covidTested = false;
			boolean covidTestedPositive = false;
			boolean covidTestedNegative = false;
			
			// Check clients with covid assessment encounter
			Encounter lastCovidAssessmentEncounter = EmrUtils.lastEncounter(Context.getPatientService().getPatient(ptId),
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
			
			ret.put(ptId, new SimpleResult(sb.toString(), this, context));
		}
		return ret;
	}
}
