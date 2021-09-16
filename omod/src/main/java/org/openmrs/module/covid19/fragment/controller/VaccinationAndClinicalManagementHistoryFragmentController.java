/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.covid19.fragment.controller;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.Concept;
import org.openmrs.Encounter;
import org.openmrs.Form;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.api.ConceptService;
import org.openmrs.api.ObsService;
import org.openmrs.api.context.Context;
import org.openmrs.module.covid19.metadata.CovidMetadata;
import org.openmrs.module.metadatadeploy.MetadataUtils;
import org.openmrs.ui.framework.SimpleObject;
import org.openmrs.ui.framework.annotation.FragmentParam;
import org.openmrs.ui.framework.fragment.FragmentModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Covid-19 vaccination and clinical management summary fragment
 */
public class VaccinationAndClinicalManagementHistoryFragmentController {
	
	public static final String VACCINATION_HISTORY_GROUPING_CONCEPT = "1421AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	
	public static final String VACCINATION_BOOSTER_DOSE_HISTORY_GROUPING_CONCEPT = "1184AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	
	int testResultConceptId = 166638;
	
	int testResultPositiveConceptId = 703;
	
	int testResultNegativeConceptId = 664;
	
	int covidPresentationConceptId = 159640;
	
	int covidPresentationSymptomaticConceptId = 1068;
	
	int covidPresentationAsymptomaticConceptId = 165912;
	
	int dateDiagnosedConceptId = 159948;
	
	int hospitalAdmissionConceptId = 162477;
	
	int admissionUnitConceptId = 161010;
	
	int admissionUnitIsolationConceptId = 165994;
	
	int admissionUnitHDUConceptId = 165995;
	
	int admissionUnitICUConceptId = 161936;
	
	int supplementalOxygenConceptId = 165864;
	
	int ventilatorConceptId = 165932;
	
	int yesConceptId = 1065;
	
	int noConceptId = 1066;
	
	ObsService obsService = Context.getObsService();
	
	ConceptService conceptService = Context.getConceptService();
	
	SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
	
	public void controller(FragmentModel model, @FragmentParam("patient") Patient patient) {
		
		Form covidAssessmentForm = MetadataUtils.existing(Form.class, CovidMetadata._Form.COVID_19_ASSESSMENT_FORM);
		
		List<Encounter> encounters = Context.getEncounterService().getEncounters(patient, null, null, null,
		    Arrays.asList(covidAssessmentForm), null, null, null, null, false);
		
		Collections.reverse(encounters);
		List<SimpleObject> vaccinationFirstAndSecondDoseList = new ArrayList<SimpleObject>();
		List<SimpleObject> vaccinationBoosterDoseList = new ArrayList<SimpleObject>();
		List<SimpleObject> diagnosisAndManagementList = new ArrayList<SimpleObject>();
		
		for (Encounter e : encounters) {
			SimpleObject so = extractEncounterData(e);
			if (so != null) {
				List<SimpleObject> firstAndSecondDose = (List<SimpleObject>) so.get("firstAndSecondDoseData");
				List<SimpleObject> boosterDose = (List<SimpleObject>) so.get("boosterDoseData");
				SimpleObject diagnosisData = (SimpleObject) so.get("diagnosisData");
				if (firstAndSecondDose.size() > 0) {
					vaccinationFirstAndSecondDoseList.addAll(firstAndSecondDose);
				}
				
				if (boosterDose.size() > 0) {
					vaccinationBoosterDoseList.addAll(boosterDose);
				}
				
				if (diagnosisData != null) {
					diagnosisAndManagementList.add(diagnosisData);
				}
			}
		}
		
		model.put("firstAndSecondDoseList", vaccinationFirstAndSecondDoseList);
		model.put("boosterDoseList", vaccinationBoosterDoseList);
		model.put("diagnosisAndManagementList", diagnosisAndManagementList);
	}
	
	/**
	 * Extracts COVID-19 vaccination/exposure data from an encounter
	 * 
	 * @param e
	 * @return
	 */
	private SimpleObject extractEncounterData(Encounter e) {
		
		List<SimpleObject> firstAndSecondDoseVaccineData = new ArrayList<SimpleObject>();
		List<SimpleObject> boosterDoseVaccineData = new ArrayList<SimpleObject>();
		
		// get observations for first and second covid vaccine doses
		List<Obs> vaccinationObs = obsService.getObservations(
		    Arrays.asList(Context.getPersonService().getPerson(e.getPatient().getPersonId())), Arrays.asList(e),
		    Arrays.asList(conceptService.getConceptByUuid(VACCINATION_HISTORY_GROUPING_CONCEPT)), null, null, null,
		    Arrays.asList("obsId"), null, null, null, null, false);
		
		for (Obs o : vaccinationObs) {
			SimpleObject data = extractVaccinationHistoryData(o.getGroupMembers());
			firstAndSecondDoseVaccineData.add(data);
		}
		
		// get observations for booster dose
		List<Obs> vaccinationBoosterObs = obsService.getObservations(
		    Arrays.asList(Context.getPersonService().getPerson(e.getPatient().getPersonId())), Arrays.asList(e),
		    Arrays.asList(conceptService.getConceptByUuid(VACCINATION_BOOSTER_DOSE_HISTORY_GROUPING_CONCEPT)), null, null,
		    null, Arrays.asList("obsId"), null, null, null, null, false);
		
		for (Obs o : vaccinationBoosterObs) {
			SimpleObject data = extractVaccinationHistoryData(o.getGroupMembers());
			boosterDoseVaccineData.add(data);
		}
		
		// get observations for covid diagnosis and management
		List<Obs> covidDiagnosisObs = obsService.getObservations(Arrays.asList(Context.getPersonService().getPerson(
		    e.getPatient().getPersonId())), Arrays.asList(e), Arrays.asList(conceptService.getConcept(testResultConceptId),
		    conceptService.getConcept(covidPresentationConceptId), conceptService.getConcept(dateDiagnosedConceptId),
		    conceptService.getConcept(hospitalAdmissionConceptId), conceptService.getConcept(admissionUnitConceptId),
		    conceptService.getConcept(supplementalOxygenConceptId), conceptService.getConcept(ventilatorConceptId)), null,
		    null, null, null, null, null, null, null, false);
		
		SimpleObject diagnosisData = extractCovidDiagnosisData(covidDiagnosisObs);
		
		return SimpleObject.create("firstAndSecondDoseData", firstAndSecondDoseVaccineData, "boosterDoseData",
		    boosterDoseVaccineData, "diagnosisData", diagnosisData);
	}
	
	/**
	 * Extract COVID-19 diagnosis encounter data
	 * 
	 * @param covidDiagnosisObs
	 * @return
	 */
	private SimpleObject extractCovidDiagnosisData(List<Obs> covidDiagnosisObs) {
		
		String covidPresentation = "";
		String dateTested = null;
		String hospitalAdmission = "";
		List<String> admissionUnitList = new ArrayList<String>();
		String supplementalOxygen = "";
		String ventilator = "";
		boolean covidPositiveClient = true;
		
		for (Obs obs : covidDiagnosisObs) {
			
			if (obs.getConcept().getConceptId().equals(testResultConceptId)) {
				if (obs.getValueCoded().getConceptId().equals(testResultNegativeConceptId)) {
					covidPositiveClient = false;
					break;
				}
			} else if (obs.getConcept().getConceptId().equals(dateDiagnosedConceptId)) {
				dateTested = DATE_FORMAT.format(obs.getValueDatetime());
			} else if (obs.getConcept().getConceptId().equals(hospitalAdmissionConceptId)) {
				if (obs.getValueCoded().getConceptId().equals(yesConceptId)) {
					hospitalAdmission = "Yes";
				} else if (obs.getValueCoded().getConceptId().equals(noConceptId)) {
					hospitalAdmission = "No";
				}
			} else if (obs.getConcept().getConceptId().equals(admissionUnitConceptId)) {
				if (obs.getValueCoded().getConceptId().equals(admissionUnitIsolationConceptId)) {
					admissionUnitList.add("Isolation");
				} else if (obs.getValueCoded().getConceptId().equals(admissionUnitHDUConceptId)) {
					admissionUnitList.add("HDU");
				} else if (obs.getValueCoded().getConceptId().equals(admissionUnitICUConceptId)) {
					admissionUnitList.add("ICU");
				}
			} else if (obs.getConcept().getConceptId().equals(supplementalOxygenConceptId)) {
				if (obs.getValueCoded().getConceptId().equals(yesConceptId)) {
					supplementalOxygen = "Yes";
				} else if (obs.getValueCoded().getConceptId().equals(noConceptId)) {
					supplementalOxygen = "No";
				}
			} else if (obs.getConcept().getConceptId().equals(ventilatorConceptId)) {
				if (obs.getValueCoded().getConceptId().equals(yesConceptId)) {
					ventilator = "Yes";
				} else if (obs.getValueCoded().getConceptId().equals(noConceptId)) {
					ventilator = "No";
				}
			} else if (obs.getConcept().getConceptId().equals(covidPresentationConceptId)) {
				if (obs.getValueCoded().getConceptId().equals(covidPresentationSymptomaticConceptId)) {
					covidPresentation = "Symptomatic";
				} else if (obs.getValueCoded().getConceptId().equals(covidPresentationAsymptomaticConceptId)) {
					covidPresentation = "Asymptomatic";
				}
			}
		}
		
		if (covidPositiveClient && StringUtils.isNotBlank(covidPresentation)) {
			return SimpleObject.create("dateTested", dateTested, "hospitalAdmission", hospitalAdmission, "admissionUnits",
			    StringUtils.join(admissionUnitList, ","), "supplementalOxygen", supplementalOxygen, "covidPresentation",
			    covidPresentation, "ventilator", ventilator);
		} else {
			return null;
		}
	}
	
	/**
	 * Extracts and organizes covid vaccine grouped observations
	 * 
	 * @param groupMembers
	 * @return
	 */
	private SimpleObject extractVaccinationHistoryData(Set<Obs> groupMembers) {
		
		int vaccineConcept = 984;
		int doseConcept = 1418;
		int dateConcept = 1410;
		int verifiedConcept = 164464;
		int vaccineVerifiedConceptAns = 164134;
		
		String vaccineType = null;
		int vaccinationDose = 0;
		String vaccinationDate = null;
		String vaccinationVerified = "No";
		
		for (Obs obs : groupMembers) {
			
			if (obs.getConcept().getConceptId().equals(doseConcept)) {
				vaccinationDose = obs.getValueNumeric().intValue();
			} else if (obs.getConcept().getConceptId().equals(dateConcept)) {
				vaccinationDate = DATE_FORMAT.format(obs.getValueDatetime());
			} else if (obs.getConcept().getConceptId().equals(vaccineConcept)) {
				vaccineType = vaccineTypeConverter(obs.getValueCoded());
			} else if (obs.getConcept().getConceptId().equals(verifiedConcept)) {
				vaccinationVerified = obs.getValueCoded().getConceptId().equals(vaccineVerifiedConceptAns) ? "Yes" : "No";
			}
		}
		
		return SimpleObject.create("vaccineType", vaccineType, "vaccinationDose", vaccinationDose, "vaccinationDate",
		    vaccinationDate, "vaccinationVerified", vaccinationVerified);
	}
	
	/**
	 * Converter for COVID-19 vaccine types
	 * 
	 * @param key
	 * @return
	 */
	String vaccineTypeConverter(Concept key) {
		Map<Concept, String> entryPointList = new HashMap<Concept, String>();
		entryPointList.put(conceptService.getConcept(166156), "Astrazeneca");
		entryPointList.put(conceptService.getConcept(166355), "Johnson and Johnson");
		entryPointList.put(conceptService.getConcept(166154), "Moderna");
		entryPointList.put(conceptService.getConcept(166155), "Pfizer");
		entryPointList.put(conceptService.getConcept(166157), "Sputnik");
		entryPointList.put(conceptService.getConcept(166379), "Sinopharm");
		entryPointList.put(conceptService.getConcept(1067), "Unknown");
		entryPointList.put(conceptService.getConcept(5622), "Other");
		return entryPointList.get(key);
	}
}
