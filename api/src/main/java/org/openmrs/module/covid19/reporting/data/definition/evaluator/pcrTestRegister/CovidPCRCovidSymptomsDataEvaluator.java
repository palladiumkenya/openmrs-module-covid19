/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.covid19.reporting.data.definition.evaluator.pcrTestRegister;

import java.util.Map;

import org.openmrs.annotation.Handler;
import org.openmrs.module.covid19.reporting.data.definition.pcrTestRegister.CovidPCRCovidSymptomsDataDefinition;
import org.openmrs.module.reporting.data.encounter.EvaluatedEncounterData;
import org.openmrs.module.reporting.data.encounter.definition.EncounterDataDefinition;
import org.openmrs.module.reporting.data.encounter.evaluator.EncounterDataEvaluator;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.EvaluationException;
import org.openmrs.module.reporting.evaluation.querybuilder.SqlQueryBuilder;
import org.openmrs.module.reporting.evaluation.service.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Evaluator for covid symptoms
 */
@Handler(supports = CovidPCRCovidSymptomsDataDefinition.class, order = 50)
public class CovidPCRCovidSymptomsDataEvaluator implements EncounterDataEvaluator {
	
	@Autowired
	private EvaluationService evaluationService;
	
	public EvaluatedEncounterData evaluate(EncounterDataDefinition definition, EvaluationContext context)
	        throws EvaluationException {
		EvaluatedEncounterData c = new EvaluatedEncounterData(definition, context);
		
		String qry = "select t.encounter_id, concat_ws(',', "
		        + " if(s.fever = 'Yes','Fever',''), "
		        + " if(s.cough = 'Yes','Cough',''), "
		        + " if(s.runny_nose = 'Yes','Runny nose',''), "
		        + " if(s.diarrhoea = 'Yes','Diarrhoea',''), "
		        + " if(s.headache = 'Yes','Headache',''), "
		        + " if(s.muscular_pain = 'Yes','Muscular pain',''), "
		        + " if(s.abdominal_pain = 'Yes','Abdominal pain',''), "
		        + " if(s.general_weakness = 'Yes','General weakness',''), "
		        + " if(s.sore_throat = 'Yes','Sore-throat',''), "
		        + " if(s.breathing_difficulty = 'Yes','Breathing difficulty',''), "
		        + " if(s.nausea_vomiting = 'Yes','Nausea',''), "
		        + " if(s.altered_mental_status = 'Yes','Altered mental status',''), "
		        + " if(s.chest_pain = 'Yes','Chest pain',''), "
		        + " if(s.joint_pain = 'Yes','Joint pain',''), "
		        + " if(s.loss_of_taste_smell = 'Yes','Loss of taste or smell',''), "
		        + " if(s.other_symptom = 'Yes','Other','') "
		        + " ) symptoms from kenyaemr_etl.etl_cca_covid_clinical_review t inner join kenyaemr_etl.etl_cca_covid_screening s on s.patient_id = t.patient_id and date(s.visit_date) = date(t.visit_date)";
		
		SqlQueryBuilder queryBuilder = new SqlQueryBuilder();
		queryBuilder.append(qry);
		Map<Integer, Object> data = evaluationService.evaluateToMap(queryBuilder, Integer.class, Object.class, context);
		c.setData(data);
		return c;
	}
	
}
