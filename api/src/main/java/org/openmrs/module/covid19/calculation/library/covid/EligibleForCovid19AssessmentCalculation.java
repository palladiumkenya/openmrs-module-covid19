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
import org.openmrs.api.PersonService;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.calculation.result.SimpleResult;
import org.openmrs.module.kenyacore.calculation.AbstractPatientCalculation;
import org.openmrs.module.kenyacore.calculation.BooleanResult;
import org.openmrs.module.kenyacore.calculation.Filters;
import org.openmrs.module.kenyacore.calculation.PatientFlagCalculation;
import org.openmrs.module.kenyaemr.calculation.BaseEmrCalculation;
import org.openmrs.module.kenyaemr.calculation.library.hiv.art.CurrentArtRegimenCalculation;
import org.openmrs.module.kenyaemr.calculation.library.hiv.art.OnArtCalculation;
import org.openmrs.module.kenyaemr.metadata.HivMetadata;
import org.openmrs.module.kenyaemr.util.EmrUtils;
import org.openmrs.module.metadatadeploy.MetadataUtils;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Calculates eligibility for covid asssessment Criteria Alive In HIV Above 18 years
 */
public class EligibleForCovid19AssessmentCalculation extends AbstractPatientCalculation implements PatientFlagCalculation {
	
	/**
	 * @see org.openmrs.module.kenyacore.calculation.PatientFlagCalculation#getFlagMessage()
	 */
	@Override
	public String getFlagMessage() {
		return "Eligible for Covid Assessment";
	}
	
	/**
	 * @see org.openmrs.calculation.patient.PatientCalculation#evaluate(java.util.Collection,
	 *      java.util.Map, org.openmrs.calculation.patient.PatientCalculationContext)
	 * @should calculate eligibility
	 */
	protected static final Log log = LogFactory.getLog(EligibleForCovid19AssessmentCalculation.class);
	
	@Override
	public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> parameterValues,
	        PatientCalculationContext context) {
		
		Program hivProgram = MetadataUtils.existing(Program.class, HivMetadata._Program.HIV);
		
		Set<Integer> alive = Filters.alive(cohort, context);
		Set<Integer> inHivProgram = Filters.inProgram(hivProgram, alive, context);
		PersonService service = Context.getPersonService();
		CalculationResultMap ret = new CalculationResultMap();
		
		for (int ptId : cohort) {
			boolean eligible = false;
			boolean covidAssessed = false;
			
			// need to exclude those already assessed for Covid 19
			// Check clients with covid assessment encounter
			Encounter lastCovidAssessmentEncounter = EmrUtils.lastEncounter(Context.getPatientService().getPatient(ptId),
			    Context.getEncounterService().getEncounterTypeByUuid("86709cfc-1490-11ec-82a8-0242ac130003")); //last covid 19 assessment encounter
			
			if (inHivProgram.contains(ptId) && lastCovidAssessmentEncounter == null
			        && service.getPerson(ptId).getAge() >= 18) {
				eligible = true;
			}
			ret.put(ptId, new BooleanResult(eligible, this));
		}
		return ret;
	}
}
