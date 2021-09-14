/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.covid19.page.controller;

import org.openmrs.Patient;
import org.openmrs.module.covid19.ModuleConstants;
import org.openmrs.module.kenyaui.annotation.AppPage;
import org.openmrs.ui.framework.page.PageModel;

/**
 * View patient page for covid app
 */
@AppPage(ModuleConstants.COVID_19_APP_ID)
public class CovidViewPatientPageController {
	
	public void controller(PageModel model) {
		Patient patient = (Patient) model.getAttribute(ModuleConstants.MODEL_ATTR_CURRENT_PATIENT);
		/*Form htsInitialForm = MetadataUtils.existing(Form.class, CommonMetadata._Form.HTS_INITIAL_TEST);
		Form htsRetestForm = MetadataUtils.existing(Form.class, CommonMetadata._Form.HTS_CONFIRMATORY_TEST);
		Form htsLinkageForm = MetadataUtils.existing(Form.class, CommonMetadata._Form.HTS_LINKAGE);
		*/
		//List<Encounter> encounters = Context.getEncounterService().getEncounters(patient, null, null, null, Arrays.asList(htsInitialForm, htsRetestForm, htsLinkageForm), null, null, null, null, false);
		
		model.put("hasEncounters", true);
	}
}
