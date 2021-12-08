/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.covid19.fragment.controller.program.covid;

import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.PatientProgram;
import org.openmrs.api.EncounterService;
import org.openmrs.api.context.Context;
import org.openmrs.module.kenyaemr.Dictionary;
import org.openmrs.module.kenyaemr.wrapper.EncounterWrapper;
import org.openmrs.ui.framework.annotation.FragmentParam;
import org.openmrs.ui.framework.fragment.FragmentModel;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Patient program enrollment fragment
 */
public class CovidInitiationSummaryFragmentController {
	
	String PATIENT_TYPE_CONCEPT = "161641AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	
	String ACTION_TAKEN_CONCEPT = "1272AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	
	String DATE_DETECTED_CONCEPT = "159948AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	
	String STABILITY_CONCEPT = "159640AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	
	String REFERRING_FACILITY_CONCEPT = "161550AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	
	String ADMISSION_DATE_CONCEPT = "1640AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	
	EncounterService encounterService = Context.getEncounterService();
	
	public String controller(@FragmentParam("patientProgram") PatientProgram patientProgram,
	        @FragmentParam(value = "encounter", required = false) Encounter encounter,
	        @FragmentParam("showClinicalData") boolean showClinicalData, FragmentModel model) {
		
		Map<String, Object> dataPoints = new LinkedHashMap<String, Object>();
		dataPoints.put("Date case reported", patientProgram.getDateEnrolled());
		
		if (encounter != null) {
			EncounterWrapper wrapper = new EncounterWrapper(encounter);
			
			Obs patientType = wrapper.firstObs(Dictionary.getConcept(PATIENT_TYPE_CONCEPT));
			Obs actionTaken = wrapper.firstObs(Dictionary.getConcept(ACTION_TAKEN_CONCEPT));
			Obs dateDetected = wrapper.firstObs(Dictionary.getConcept(DATE_DETECTED_CONCEPT));
			Obs patientState = wrapper.firstObs(Dictionary.getConcept(STABILITY_CONCEPT));
			Obs referringFacility = wrapper.firstObs(Dictionary.getConcept(REFERRING_FACILITY_CONCEPT));
			Obs admissionDate = wrapper.firstObs(Dictionary.getConcept(ADMISSION_DATE_CONCEPT));
			
			if (patientType != null) {
				dataPoints.put("Patient type", patientType.getValueCoded());
			}
			
			if (actionTaken != null) {
				if (actionTaken.getValueCoded().getConceptId().equals(165901)) {
					dataPoints.put("Action taken", "Referred to home based treatment/isolation");
				} else {
					dataPoints.put("Action taken", "Referred to hospital for treatment");
				}
			}
			
			if (dateDetected != null) {
				dataPoints.put("Date tested +ve", dateDetected.getValueDatetime());
			}
			
			if (patientState != null) {
				dataPoints.put("Status at enrollment", patientState.getValueCoded());
			}
			
			if (referringFacility != null) {
				dataPoints.put("Facility transferred from", referringFacility.getValueText());
			}
			
			if (admissionDate != null) {
				dataPoints.put("Date of admission", admissionDate.getValueDatetime());
			}
			
		}
		model.put("dataPoints", dataPoints);
		return "view/dataPoints";
	}
}
