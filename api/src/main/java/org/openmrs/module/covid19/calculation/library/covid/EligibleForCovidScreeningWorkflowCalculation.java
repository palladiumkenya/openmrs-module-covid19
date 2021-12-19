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

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Program;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.covid19.metadata.CovidMetadata;
import org.openmrs.module.kenyacore.calculation.AbstractPatientCalculation;
import org.openmrs.module.kenyacore.calculation.BooleanResult;
import org.openmrs.module.kenyacore.calculation.Filters;
import org.openmrs.module.metadatadeploy.MetadataUtils;

/**
 * Calculates not vaccinated status
 */
public class EligibleForCovidScreeningWorkflowCalculation extends AbstractPatientCalculation {
	
	/**
	 * @see org.openmrs.calculation.patient.PatientCalculation#evaluate(Collection, Map,
	 *      PatientCalculationContext)
	 * @should calculate eligibility
	 */
	protected static final Log log = LogFactory.getLog(NotVaccinatedCalculation.class);
	
	@Override
	public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> parameterValues,
	        PatientCalculationContext context) {
		
		Program covidProgram = MetadataUtils.existing(Program.class, CovidMetadata._Program.COVID_TREATMENT);
		Set<Integer> alive = Filters.alive(cohort, context);
		Set<Integer> inCovidTreatmentProgram = Filters.inProgram(covidProgram, alive, context);
		
		CalculationResultMap ret = new CalculationResultMap();
		for (int ptId : cohort) {
			boolean eligible = false;
			
			if (!inCovidTreatmentProgram.contains(ptId)) {
				eligible = true;
			}
			
			ret.put(ptId, new BooleanResult(eligible, this));
		}
		return ret;
	}
	
}
