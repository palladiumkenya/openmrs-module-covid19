/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.covid19.reporting.data.definition.evaluator.covidTreatmentEnrollmentRegister;

import java.util.Map;

import org.openmrs.annotation.Handler;
import org.openmrs.module.covid19.reporting.data.definition.covidTreatmentEnrollmentRegister.TreatmentOutcomeDateDataDefinition;
import org.openmrs.module.reporting.data.encounter.EvaluatedEncounterData;
import org.openmrs.module.reporting.data.encounter.definition.EncounterDataDefinition;
import org.openmrs.module.reporting.data.encounter.evaluator.EncounterDataEvaluator;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.EvaluationException;
import org.openmrs.module.reporting.evaluation.querybuilder.SqlQueryBuilder;
import org.openmrs.module.reporting.evaluation.service.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Evaluates a VisitIdDataDefinition to produce a VisitData
 */
@Handler(supports = TreatmentOutcomeDateDataDefinition.class, order = 50)
public class TreatmentOutcomeDateDataEvaluator implements EncounterDataEvaluator {
	
	@Autowired
	private EvaluationService evaluationService;
	
	public EvaluatedEncounterData evaluate(EncounterDataDefinition definition, EvaluationContext context)
	        throws EvaluationException {
		EvaluatedEncounterData c = new EvaluatedEncounterData(definition, context);
		
		String qry = "select e.encounter_id, o.visit_date "
		        + " from kenyaemr_etl.etl_cca_covid_treatment_enrollment e "
		        + " inner join kenyaemr_etl.etl_patient_program pp on e.patient_id = pp.patient_id and date(e.visit_date) = date(pp.date_enrolled) "
		        + " inner join kenyaemr_etl.etl_cca_covid_treatment_enrollment_outcome o on o.patient_id = pp.patient_id  and date(o.visit_date) = date(pp.date_completed) ";
		
		SqlQueryBuilder queryBuilder = new SqlQueryBuilder();
		queryBuilder.append(qry);
		Map<Integer, Object> data = evaluationService.evaluateToMap(queryBuilder, Integer.class, Object.class, context);
		c.setData(data);
		return c;
	}
}
