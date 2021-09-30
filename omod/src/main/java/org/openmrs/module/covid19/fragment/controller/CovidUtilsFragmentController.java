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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.Encounter;
import org.openmrs.Form;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.api.ConceptService;
import org.openmrs.api.ObsService;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.api.context.ContextAuthenticationException;
import org.openmrs.module.covid19.metadata.CovidMetadata;
import org.openmrs.module.kenyaemr.util.EmrUtils;
import org.openmrs.module.kenyaui.annotation.PublicAction;
import org.openmrs.module.metadatadeploy.MetadataUtils;
import org.openmrs.ui.framework.SimpleObject;
import org.openmrs.ui.framework.annotation.FragmentParam;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Fragment actions generally useful for Covid-19
 */
public class CovidUtilsFragmentController {
	
	protected static final Log log = LogFactory.getLog(CovidUtilsFragmentController.class);
	
	public static final String VACCINATION_HISTORY_GROUPING_CONCEPT = "1421AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	
	public static final String VACCINATION_BOOSTER_DOSE_HISTORY_GROUPING_CONCEPT = "1184AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	
	ConceptService conceptService = Context.getConceptService();
	
	ObsService obsService = Context.getObsService();
	
	PatientService patientService = Context.getPatientService();
	
	SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
	
	/**
	 * Checks if current user session is authenticated
	 * 
	 * @return simple object {authenticated: true/false}
	 */
	@PublicAction
	public SimpleObject isAuthenticated() {
		return SimpleObject.create("authenticated", Context.isAuthenticated());
	}
	
	/**
	 * Attempt to authenticate current user session with the given credentials
	 * 
	 * @param username the username
	 * @param password the password
	 * @return simple object {authenticated: true/false}
	 */
	@PublicAction
	public SimpleObject authenticate(@RequestParam(value = "username", required = false) String username,
	        @RequestParam(value = "password", required = false) String password) {
		try {
			Context.authenticate(username, password);
		}
		catch (ContextAuthenticationException ex) {
			// do nothing
		}
		return isAuthenticated();
	}
	
	/**
	 * Composes covid asssessment outcomes such as : Have done a previous covid 19 Assessment
	 * assessment form Vaccination status || partially or fully Ever tested positive for Covid 19
	 */
	public SimpleObject recordedCovidAssessmentIndicators(
	        @RequestParam(value = "patientId", required = false) Integer patientId) {
		
		Integer VaccinationStatusQuestion = 163100;
		Integer CompletionStatusQuestion = 164134;
		Integer PartialVaccinationStatus = 166192;
		Integer FullVaccinationStatus = 5585;
		//Covid tests
		Integer CovidTestQuestion = 165852;
		Integer CovidResultQuestion = 166638;
		Integer CovidTestPositive = 703;
		Integer CovidTestNegative = 664;
		Integer YesConcept = 1065;
		
		boolean vaccinated = false;
		boolean fullyVaccinated = false;
		boolean partiallyVaccinated = false;
		boolean covidAssessed = false;
		boolean covidTested = false;
		boolean covidTestedPositive = false;
		boolean covidTestedNegative = false;
		PatientService patientService = Context.getPatientService();
		
		// Check clients with covid assessment encounter
		Encounter lastCovidAssessmentEncounter = EmrUtils.lastEncounter(Context.getPatientService().getPatient(patientId),
		    Context.getEncounterService().getEncounterTypeByUuid("86709cfc-1490-11ec-82a8-0242ac130003")); //last covid 19 assessment encounter
		if (lastCovidAssessmentEncounter != null) {
			covidAssessed = true;
			for (Obs obs : lastCovidAssessmentEncounter.getObs()) {
				if (obs.getConcept().getConceptId().equals(VaccinationStatusQuestion)
				        && (obs.getValueCoded().getConceptId().equals(YesConcept))) {
					vaccinated = true;
				}
				if (obs.getConcept().getConceptId().equals(CompletionStatusQuestion)
				        && (obs.getValueCoded().getConceptId().equals(PartialVaccinationStatus))) {
					partiallyVaccinated = true;
				}
				if (obs.getConcept().getConceptId().equals(CompletionStatusQuestion)
				        && (obs.getValueCoded().getConceptId().equals(FullVaccinationStatus))) {
					fullyVaccinated = true;
				}
				if (obs.getConcept().getConceptId().equals(CovidTestQuestion)
				        && (obs.getValueCoded().getConceptId().equals(YesConcept))) {
					covidTested = true;
				}
				if (obs.getConcept().getConceptId().equals(CovidResultQuestion)
				        && (obs.getValueCoded().getConceptId().equals(CovidTestPositive))) {
					covidTestedPositive = true;
				}
				if (obs.getConcept().getConceptId().equals(CovidResultQuestion)
				        && (obs.getValueCoded().getConceptId().equals(CovidTestNegative))) {
					covidTestedNegative = true;
				}
			}
		}
		
		return SimpleObject.create("covidAssessed", covidAssessed, "vaccinated", vaccinated, "fullyVaccinated",
		    fullyVaccinated, "partiallyVaccinated", partiallyVaccinated, "covidTestedPositive", covidTestedPositive,
		    "covidTestedNegative", covidTestedNegative, "covidTested", covidTested
		
		);
	}
	
	public SimpleObject recordedCovidAssessmentHistory(@RequestParam(value = "patientId", required = false) Integer patientId) {
		
		Form covidAssessmentForm = MetadataUtils.existing(Form.class, CovidMetadata._Form.COVID_19_ASSESSMENT_FORM);
		
		List<Encounter> encounters = Context.getEncounterService().getEncounters(patientService.getPatient(patientId), null,
		    null, null, Arrays.asList(covidAssessmentForm), null, null, null, null, false);
		
		//Collections.reverse(encounters);
		List<SimpleObject> vaccinationFirstAndSecondDoseList = new ArrayList<SimpleObject>();
		List<SimpleObject> vaccinationBoosterDoseList = new ArrayList<SimpleObject>();
		String firstDoseVaccineName = "";
		int firstDose = 1;
		String firstDoseDate = "";
		String secondDoseVaccineName = "";
		int secondDose = 2;
		String secondDoseDate = "";
		String boosterDoseVaccineName = "";
		int boosterVaccineDose = 1;
		String boosterDoseDate = "";
		for (Encounter e : encounters) {
			SimpleObject so = extractEncounterData(e);
			if (so != null) {
				List<SimpleObject> firstAndSecondDose = (List<SimpleObject>) so.get("firstAndSecondDoseData");
				List<SimpleObject> boosterDose = (List<SimpleObject>) so.get("boosterDoseData");
				if (firstAndSecondDose.size() > 0) {
					vaccinationFirstAndSecondDoseList.addAll(firstAndSecondDose);
					firstDoseVaccineName = vaccinationFirstAndSecondDoseList.get(0).get("vaccineType").toString() != null ? vaccinationFirstAndSecondDoseList
					        .get(0).get("vaccineType").toString()
					        : "";
					firstDoseDate = vaccinationFirstAndSecondDoseList.get(0).get("vaccinationDate").toString() != null ? vaccinationFirstAndSecondDoseList
					        .get(0).get("vaccinationDate").toString()
					        : "";
				}
				if (firstAndSecondDose.size() > 1) {
					secondDoseVaccineName = vaccinationFirstAndSecondDoseList.get(firstAndSecondDose.size() - 1)
					        .get("vaccineType").toString() != null ? vaccinationFirstAndSecondDoseList
					        .get(firstAndSecondDose.size() - 1).get("vaccineType").toString() : "";
					secondDoseDate = vaccinationFirstAndSecondDoseList.get(firstAndSecondDose.size() - 1)
					        .get("vaccinationDate").toString() != null ? vaccinationFirstAndSecondDoseList
					        .get(firstAndSecondDose.size() - 1).get("vaccinationDate").toString() : "";
					
				}
				
				if (boosterDose.size() > 0) {
					vaccinationBoosterDoseList.addAll(boosterDose);
					boosterDoseVaccineName = vaccinationBoosterDoseList.get(0).get("vaccineType").toString() != null ? vaccinationBoosterDoseList
					        .get(0).get("vaccineType").toString()
					        : "";
					boosterDoseDate = vaccinationBoosterDoseList.get(0).get("vaccinationDate").toString() != null ? vaccinationBoosterDoseList
					        .get(0).get("vaccinationDate").toString()
					        : "";
					
				}
			}
		}
		return SimpleObject.create("firstDoseVaccineName", firstDoseVaccineName, "firstDose", firstDose, "firstDoseDate",
		    firstDoseDate, "secondDoseVaccineName", secondDoseVaccineName, "secondDose", secondDose, "secondDoseDate",
		    secondDoseDate, "boosterDoseVaccineName", boosterDoseVaccineName, "boosterVaccineDose", boosterVaccineDose,
		    "boosterDoseDate", boosterDoseDate
		
		);

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
			System.out.println("Vaccination obs id: " + o.getObsId());
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
		
		return SimpleObject.create("firstAndSecondDoseData", firstAndSecondDoseVaccineData, "boosterDoseData",
		    boosterDoseVaccineData);
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
		String vaccinationVerified = null;
		
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
