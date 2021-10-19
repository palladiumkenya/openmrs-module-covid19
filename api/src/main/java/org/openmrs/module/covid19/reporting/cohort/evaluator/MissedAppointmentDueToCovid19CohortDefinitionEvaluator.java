/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.covid19.reporting.cohort.evaluator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Cohort;
import org.openmrs.annotation.Handler;
import org.openmrs.module.covid19.reporting.cohort.definition.MissedAppointmentDueToCovid19CohortDefinition;
import org.openmrs.module.reporting.cohort.EvaluatedCohort;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.evaluator.CohortDefinitionEvaluator;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.EvaluationException;
import org.openmrs.module.reporting.evaluation.querybuilder.SqlQueryBuilder;
import org.openmrs.module.reporting.evaluation.service.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;

/**
 * Evaluator for patients who have missed their appointments due to Covid-19 infection
 */
@Handler(supports = { MissedAppointmentDueToCovid19CohortDefinition.class })
public class MissedAppointmentDueToCovid19CohortDefinitionEvaluator implements CohortDefinitionEvaluator {
	
	private final Log log = LogFactory.getLog(this.getClass());
	
	@Autowired
	EvaluationService evaluationService;
	
	@Override
	public EvaluatedCohort evaluate(CohortDefinition cohortDefinition, EvaluationContext context) throws EvaluationException {
		MissedAppointmentDueToCovid19CohortDefinition definition = (MissedAppointmentDueToCovid19CohortDefinition) cohortDefinition;
		
		if (definition == null)
			return null;
		
		Cohort newCohort = new Cohort();
		String qry = "select t.patient_id\n"
		        + "from (select fup.visit_date,\n"
		        + "             fup.patient_id,\n"
		        + "             max(e.visit_date)                                                                as enroll_date,\n"
		        + "             greatest(max(e.visit_date), ifnull(max(date(e.transfer_in_date)), '0000-00-00')) as latest_enrolment_date,\n"
		        + "             greatest(max(fup.visit_date), ifnull(max(d.visit_date), '0000-00-00'))           as latest_vis_date,\n"
		        + "             greatest(mid(max(concat(fup.visit_date, fup.next_appointment_date)), 11),\n"
		        + "                      ifnull(max(d.visit_date), '0000-00-00'))                                as latest_tca,\n"
		        + "             d.patient_id                                                                     as disc_patient,\n"
		        + "             d.effective_disc_date                                                            as effective_disc_date,\n"
		        + "             max(d.visit_date)                                                                as date_discontinued,\n"
		        + "             d.discontinuation_reason,\n"
		        + "             de.patient_id                                                                    as started_on_drugs,\n"
		        + "     t.latest_tracing_date                                                            as latest_trace_date,\n"
		        + "   t.tracing_outcome                                                                as tracing_outcome,\n"
		        + "    t.reason_for_missed_appointment                                                                    as true_stats\n"
		        + "      from kenyaemr_etl.etl_patient_hiv_followup fup\n"
		        + "             join kenyaemr_etl.etl_patient_demographics p on p.patient_id = fup.patient_id\n"
		        + "             join kenyaemr_etl.etl_hiv_enrollment e on fup.patient_id = e.patient_id\n"
		        + "             join (select t.patient_id,\n"
		        + "                          max(t.visit_date)                                     as latest_tracing_date,\n"
		        + "                          mid(max(concat(t.visit_date, t.tracing_outcome)), 11) as tracing_outcome,\n"
		        + "                          mid(max(concat(t.visit_date, t.reason_for_missed_appointment)), 11)     as reason_for_missed_appointment\n"
		        + "                   from kenyaemr_etl.etl_ccc_defaulter_tracing t\n"
		        + "                   group by t.patient_id)t on e.patient_id = t.patient_id\n"
		        + "             left outer join kenyaemr_etl.etl_drug_event de\n"
		        + "               on e.patient_id = de.patient_id and de.program = 'HIV' and date(date_started) <= date(curdate())\n"
		        + "             left outer join kenyaemr_etl.etl_covid19_assessment ca on e.patient_id = ca.patient_id\n"
		        + "             left outer JOIN (select patient_id,\n"
		        + "                                     coalesce(date(effective_discontinuation_date), visit_date) visit_date,\n"
		        + "                                     max(date(effective_discontinuation_date)) as               effective_disc_date,\n"
		        + "                                     discontinuation_reason\n"
		        + "                              from kenyaemr_etl.etl_patient_program_discontinuation\n"
		        + "                              where date(visit_date) <= date(curdate())\n"
		        + "                                and program_name = 'HIV'\n"
		        + "                              group by patient_id) d on d.patient_id = fup.patient_id\n"
		        + "      where fup.visit_date <= date(curdate())\n"
		        + "      group by patient_id\n"
		        + "      having (\n"
		        + "                 (timestampdiff(DAY, date(latest_tca), date(curdate())) between 1 and 30) and\n"
		        + "                 ((date(d.effective_disc_date) > date(curdate()) or date(enroll_date) > date(d.effective_disc_date)) or\n"
		        + "                  d.effective_disc_date is null)\n"
		        + "                   and\n"
		        + "                 (date(latest_vis_date) > date(date_discontinued) and date(latest_tca) > date(date_discontinued) or\n"
		        + "                  disc_patient is null) and (reason_for_missed_appointment in (165609,165610) and latest_tracing_date > latest_tca)\n"
		        + "                 )) t;";
		
		SqlQueryBuilder builder = new SqlQueryBuilder();
		builder.append(qry);
		List<Integer> ptIds = evaluationService.evaluateToList(builder, Integer.class, context);
		newCohort.setMemberIds(new HashSet<Integer>(ptIds));
		return new EvaluatedCohort(newCohort, definition, context);
	}
	
}
