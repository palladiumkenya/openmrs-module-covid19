/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */

package org.openmrs.module.covid19.metadata;

import org.openmrs.module.metadatadeploy.bundle.AbstractMetadataBundle;
import org.springframework.stereotype.Component;

import static org.openmrs.module.metadatadeploy.bundle.CoreConstructors.encounterType;
import static org.openmrs.module.metadatadeploy.bundle.CoreConstructors.form;
import static org.openmrs.module.metadatadeploy.bundle.CoreConstructors.program;

/**
 * Metadata constants
 */
@Component
public class CovidMetadata extends AbstractMetadataBundle {
	
	public static final class _Program {
		
		public static final String COVID_TREATMENT = "117093ea-5355-11ec-bf63-0242ac130002";
	}
	
	public static final class _Concept {
		
		public static final String COVID_TREATMENT = "126311AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	}
	
	public static final class _EncounterType {
		
		public static final String COVID_19_ASSESSMENT = "86709cfc-1490-11ec-82a8-0242ac130003";
		
		// added for egpaf
		public static final String COVID_SCREENING = "11708f6c-5355-11ec-bf63-0242ac130002";
		
		public static final String COVID_TREATMENT_ENROLLMENT = "33a3a55c-73ae-11ea-bc55-0242ac130003";
		
		public static final String COVID_TREATMENT_OUTCOME = "33a3a7be-73ae-11ea-bc55-0242ac130003";
		
		public static final String COVID_TREATMENT_FOLLOWUP = "33a3a8e0-73ae-11ea-bc55-0242ac130003";
		
		public static final String COVID_TESTING = "820cbccc-54cd-11ec-bf63-0242ac130002";
		
	}
	
	public static final class _Form {
		
		public static final String COVID_19_ASSESSMENT_FORM = "86709f36-1490-11ec-82a8-0242ac130003";
		
		// added for egpaf
		public static final String COVID_SCREENING = "117092aa-5355-11ec-bf63-0242ac130002";
		
		public static final String COVID_TREATMENT_ENROLLMENT = "9a5d57b6-739a-11ea-bc55-0242ac130003";
		
		public static final String COVID_TESTING = "820cbf10-54cd-11ec-bf63-0242ac130002";
		
		public static final String COVID_TREATMENT_FOLLOWUP = "33a3aab6-73ae-11ea-bc55-0242ac130003";
		
		public static final String COVID_TREATMENT_OUTCOME = "9a5d58c4-739a-11ea-bc55-0242ac130003";
		
	}
	
	@Override
	public void install() throws Exception {
		
		install(encounterType("COVID-19 Assessment Encounter",
		    "A visit encounter that provides assessment questions for covid-10 vaccination and exposure",
		    _EncounterType.COVID_19_ASSESSMENT));
		
		// added for egpaf
		install(encounterType("COVID-19 screening Encounter",
		    "A visit encounter that assesses the various COVID-19 symptoms in a patient", _EncounterType.COVID_SCREENING));
		
		install(encounterType("COVID-19 Treatment Enrollment Encounter", "A COVID-19 treatment enrollment encounter",
		    _EncounterType.COVID_TREATMENT_ENROLLMENT));
		
		install(encounterType("COVID-19 Treatment followup Encounter",
		    "A visit encounter for recording a patient's progress during COVID-19 treatment period",
		    _EncounterType.COVID_TREATMENT_FOLLOWUP));
		
		install(encounterType("COVID-19 Outcome Encounter", "An encounter for recording COVID-19 treatment outcome",
		    _EncounterType.COVID_TREATMENT_OUTCOME));
		
		install(encounterType("COVID-19 Testing", "An encounter for recording COVID-19 test information",
		    _EncounterType.COVID_TESTING));
		
		install(form("COVID-19 Assessment form", null, _EncounterType.COVID_19_ASSESSMENT, "1",
		    _Form.COVID_19_ASSESSMENT_FORM));
		
		// added for egpaf
		install(form("COVID-19 Screening Form", "COVID-19 Screening form ", _EncounterType.COVID_SCREENING, "1",
		    _Form.COVID_SCREENING));
		install(form("COVID-19 Treatment Enrollment", "COVID-19 Treatment Enrollment form ",
		    _EncounterType.COVID_TREATMENT_ENROLLMENT, "1", _Form.COVID_TREATMENT_ENROLLMENT));
		install(form("COVID-19 Treatment Followup", "COVID-19 Treatment Followup form ",
		    _EncounterType.COVID_TREATMENT_FOLLOWUP, "1", _Form.COVID_TREATMENT_FOLLOWUP));
		install(form("COVID-19 Treatment Outcome", "COVID-19 Treatment outcome form ",
		    _EncounterType.COVID_TREATMENT_OUTCOME, "1", _Form.COVID_TREATMENT_OUTCOME));
		
		install(form("COVID-19 Testing", "COVID-19 Testing ", _EncounterType.COVID_TESTING, "1", _Form.COVID_TESTING));
		
		install(program("COVID-19 Treatment", "COVID-19 Treatment Program", _Concept.COVID_TREATMENT,
		    _Program.COVID_TREATMENT));
		
	}
	
}
