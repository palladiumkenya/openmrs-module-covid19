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
		Concept VaccinationStatusQuestion = Context.getConceptService().getConcept(164134);
		Concept PartialVaccinationStatus = Context.getConceptService().getConcept(166192);
		Concept FullVaccinationStatus = Context.getConceptService().getConcept(5585);
		//Covid tests
		Concept CovidTestQuestion = Context.getConceptService().getConcept(166638);
		Concept CovidTestPositive = Context.getConceptService().getConcept(703);
		Concept CovidTestNegative = Context.getConceptService().getConcept(664);
		
		CalculationResultMap lastVaccinationStatusObs = Calculations.lastObs(VaccinationStatusQuestion, alive, context);
		CalculationResultMap lastCovidTestStatusObs = Calculations.lastObs(CovidTestQuestion, alive, context);
		
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
			
			//vaccination status
			Concept vaccinationStatus = EmrCalculationUtils.codedObsResultForPatient(lastVaccinationStatusObs, ptId);
			if (vaccinationStatus != null) {
				Obs vaccinationObs = EmrCalculationUtils.obsResultForPatient(lastVaccinationStatusObs, ptId);
				if (vaccinationObs != null && vaccinationObs.getConcept() == VaccinationStatusQuestion) {
					vaccinated = true;
				}
				if (vaccinationObs != null && vaccinationObs.getConcept() == VaccinationStatusQuestion
				        && vaccinationObs.getValueCoded() == PartialVaccinationStatus) {
					partiallyVaccinated = true;
				}
				if (vaccinationObs != null && vaccinationObs.getConcept() == VaccinationStatusQuestion
				        && vaccinationObs.getValueCoded() == FullVaccinationStatus) {
					fullyVaccinated = true;
				}
			}
			//Covid Test status
			Concept covidTestStatus = EmrCalculationUtils.codedObsResultForPatient(lastCovidTestStatusObs, ptId);
			if (covidTestStatus != null) {
				Obs covidTestObs = EmrCalculationUtils.obsResultForPatient(lastCovidTestStatusObs, ptId);
				if (covidTestObs != null && covidTestObs.getConcept() == CovidTestQuestion) {
					covidTested = true;
				}
				if (covidTestObs != null && covidTestObs.getConcept() == CovidTestQuestion
				        && covidTestObs.getValueCoded() == CovidTestPositive) {
					covidTestedPositive = true;
				}
				if (covidTestObs != null && covidTestObs.getConcept() == CovidTestQuestion
				        && covidTestObs.getValueCoded() == CovidTestNegative) {
					covidTestedNegative = true;
				}
			}
			
			Encounter lastCovidAssessmentEncounter = EmrUtils.lastEncounter(Context.getPatientService().getPatient(ptId),
			    Context.getEncounterService().getEncounterTypeByUuid("86709f36-1490-11ec-82a8-0242ac130003")); //last covid 19 assessment form
			if (lastCovidAssessmentEncounter != null) {
				// Has previous covid encounters
				covidAssessed = true;
			}
			
			sb.append("covidAssessed:").append(covidAssessed).append(",");
			sb.append("vaccinated:").append(vaccinated).append(",");
			sb.append("fullyVaccinated:").append(fullyVaccinated).append(",");
			sb.append("partiallyVaccinated:").append(partiallyVaccinated).append(",");
			sb.append("covidTestedPositive:").append(covidTestedPositive).append(",");
			sb.append("covidTestedNegative:").append(covidTestedNegative).append(",");
			sb.append("covidTested:").append(covidTested).append(",");
			
			ret.put(ptId, new SimpleResult(sb.toString(), this, context));
		}
		
		log.info("Covid assessmet String ==>" + sb);
		return ret;
		
	}
	
}
