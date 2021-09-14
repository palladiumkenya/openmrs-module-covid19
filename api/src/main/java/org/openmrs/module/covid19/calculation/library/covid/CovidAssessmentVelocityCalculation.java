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
import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.kenyacore.calculation.BooleanResult;
import org.openmrs.module.kenyacore.calculation.Filters;
import org.openmrs.module.kenyaemr.calculation.BaseEmrCalculation;
import org.openmrs.module.kenyaemr.util.EmrUtils;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Calculates a consolidation of covid asssessment validations such as : Have done a previous covid
 * assessment form Vaccination status || partially or fully Ever tested positive for Covid 19
 */
public class CovidAssessmentVelocityCalculation extends BaseEmrCalculation {
	
	protected static final Log log = LogFactory.getLog(CovidAssessmentVelocityCalculation.class);
	
	static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MMM-yyyy");
	
	@Override
	public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> parameterValues,
	        PatientCalculationContext context) {
		
		Set<Integer> alive = Filters.alive(cohort, context);
		
		CalculationResultMap ret = new CalculationResultMap();
		for (Integer ptId : cohort) {
			
			boolean fullyVaccinated = false;
			
			Encounter lastCovidAssessmentEncounter = EmrUtils.lastEncounter(Context.getPatientService().getPatient(ptId),
			    Context.getEncounterService().getEncounterTypeByUuid("86709f36-1490-11ec-82a8-0242ac130003")); //last covid 19 assessment form
			if (lastCovidAssessmentEncounter != null) {
				for (Obs obs : lastCovidAssessmentEncounter.getObs()) {
					if (obs.getConcept().getConceptId().equals(fullyVaccinated)
					        && (obs.getValueCoded().getConceptId().equals(5585))) {
						fullyVaccinated = true;
					}
				}
			}
			
			ret.put(ptId, new BooleanResult(fullyVaccinated, this));
		}
		return ret;
	}
	
}
