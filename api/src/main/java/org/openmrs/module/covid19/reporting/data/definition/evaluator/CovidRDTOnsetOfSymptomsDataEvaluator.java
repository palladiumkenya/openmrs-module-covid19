/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.covid19.reporting.data.definition.evaluator;

import org.openmrs.annotation.Handler;
import org.openmrs.module.covid19.reporting.data.definition.CovidRDTOnsetOfSymptomsDateDataDefinition;
import org.openmrs.module.covid19.reporting.data.definition.CovidRDTSampleTypeDataDefinition;
import org.openmrs.module.reporting.data.encounter.EvaluatedEncounterData;
import org.openmrs.module.reporting.data.encounter.definition.EncounterDataDefinition;
import org.openmrs.module.reporting.data.encounter.evaluator.EncounterDataEvaluator;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.EvaluationException;
import org.openmrs.module.reporting.evaluation.querybuilder.SqlQueryBuilder;
import org.openmrs.module.reporting.evaluation.service.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Evaluator for RDT date of onset of symptoms
 */
@Handler(supports = CovidRDTOnsetOfSymptomsDateDataDefinition.class, order = 50)
public class CovidRDTOnsetOfSymptomsDataEvaluator implements EncounterDataEvaluator {
	
	@Autowired
	private EvaluationService evaluationService;
	
	public EvaluatedEncounterData evaluate(EncounterDataDefinition definition, EvaluationContext context)
	        throws EvaluationException {
		EvaluatedEncounterData c = new EvaluatedEncounterData(definition, context);
		
		String qry = "select t.encounter_id, date(s.onset_symptoms_date) from kenyaemr_etl.etl_cca_covid_rdt_test t inner join kenyaemr_etl.etl_cca_covid_screening s on s.patient_id = t.patient_id and date(s.visit_date) = date(t.visit_date)";
		
		SqlQueryBuilder queryBuilder = new SqlQueryBuilder();
		queryBuilder.append(qry);
		Map<Integer, Object> data = evaluationService.evaluateToMap(queryBuilder, Integer.class, Object.class, context);
		c.setData(data);
		return c;
	}
}
