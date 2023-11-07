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
import org.openmrs.Program;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.kenyacore.calculation.AbstractPatientCalculation;
import org.openmrs.module.kenyacore.calculation.BooleanResult;
import org.openmrs.module.kenyacore.calculation.Filters;
import org.openmrs.module.kenyacore.calculation.PatientFlagCalculation;
import org.openmrs.module.kenyaemr.metadata.HivMetadata;
import org.openmrs.module.kenyaemr.util.EmrUtils;
import org.openmrs.module.metadatadeploy.MetadataUtils;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Calculates partial vaccination status
 * Retired flag
 */
public class PartiallyVaccinatedCalculation extends AbstractPatientCalculation  {
	
	/**
	 * @see PatientFlagCalculation#getFlagMessage()
	 */
//	@Override
//	public String getFlagMessage() {
//		return "Partially COVID-19 Vaccinated";
//	}
	
	/**
	 * @see org.openmrs.calculation.patient.PatientCalculation#evaluate(Collection, Map,
	 *      PatientCalculationContext)
	 * @should calculate eligibility
	 */
	protected static final Log log = LogFactory.getLog(PartiallyVaccinatedCalculation.class);
	
	@Override
	public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> parameterValues,
	        PatientCalculationContext context) {
		
		Program hivProgram = MetadataUtils.existing(Program.class, HivMetadata._Program.HIV);
		
		Integer CompletionStatusQuestion = 164134;
		Integer PartialVaccinationStatus = 166192;
		
		Set<Integer> alive = Filters.alive(cohort, context);
		Set<Integer> inHivProgram = Filters.inProgram(hivProgram, alive, context);
		CalculationResultMap ret = new CalculationResultMap();
		
		for (int ptId : cohort) {
			boolean partiallyVaccinated = false;
			boolean eligible = false;
			// Check clients with covid assessment encounter
			Encounter lastCovidAssessmentEncounter = EmrUtils.lastEncounter(Context.getPatientService().getPatient(ptId),
			    Context.getEncounterService().getEncounterTypeByUuid("86709cfc-1490-11ec-82a8-0242ac130003")); //last covid 19 assessment encounter
			if (lastCovidAssessmentEncounter != null) {
				
				for (Obs obs : lastCovidAssessmentEncounter.getObs()) {
					if (obs.getConcept().getConceptId().equals(CompletionStatusQuestion)
					        && (obs.getValueCoded().getConceptId().equals(PartialVaccinationStatus))) {
						partiallyVaccinated = true;
					}
					
					if (inHivProgram.contains(ptId) && partiallyVaccinated) {
						eligible = true;
					}
					
				}
			}
			ret.put(ptId, new BooleanResult(eligible, this));
		}
		return ret;
	}
	
}
