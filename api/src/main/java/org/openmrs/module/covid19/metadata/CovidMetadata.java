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

/**
 * Metadata constants
 */
//@Component
public class CovidMetadata extends AbstractMetadataBundle {
	
	public static final class _EncounterType {
		
		public static final String COVID_19_ASSESSMENT = "86709cfc-1490-11ec-82a8-0242ac130003";
	}
	
	public static final class _Form {
		
		public static final String COVID_19_ASSESSMENT_FORM = "86709f36-1490-11ec-82a8-0242ac130003";
	}
	
	@Override
	public void install() throws Exception {
		
		install(encounterType("COVID-19 Assessment Encounter",
		    "A visit encounter that provides assessment questions for covid-10 vaccination and exposure",
		    _EncounterType.COVID_19_ASSESSMENT));
		
		install(form("COVID-19 Assessment form", null, _EncounterType.COVID_19_ASSESSMENT, "1",
		    _Form.COVID_19_ASSESSMENT_FORM));
	}
	
}
