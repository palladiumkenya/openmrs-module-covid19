package org.openmrs.module.covid19.page.controller;

import org.openmrs.Patient;
import org.openmrs.module.covid19.ModuleConstants;
import org.openmrs.module.kenyaui.annotation.AppPage;
import org.openmrs.ui.framework.SimpleObject;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.page.PageModel;

/**
 * Homepage for the covid provider app
 */
@AppPage(ModuleConstants.COVID_19_APP_ID)
public class CovidHomePageController {
	
	public String controller(UiUtils ui, PageModel model) {
		Patient patient = (Patient) model.getAttribute(ModuleConstants.MODEL_ATTR_CURRENT_PATIENT);
		
		if (patient != null) {
			return "redirect:"
			        + ui.pageLink(ModuleConstants.MODULE_ID, "covidViewPatient",
			            SimpleObject.create("patientId", patient.getId()));
		} else {
			return null;
		}
	}
}
