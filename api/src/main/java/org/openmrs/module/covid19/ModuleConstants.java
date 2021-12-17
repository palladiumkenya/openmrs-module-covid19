package org.openmrs.module.covid19;

import org.openmrs.EncounterType;
import org.openmrs.Form;
import org.openmrs.module.covid19.metadata.CovidMetadata;
import org.openmrs.module.metadatadeploy.MetadataUtils;

public class ModuleConstants {
	
	public static final String MODEL_ATTR_CURRENT_PATIENT = "currentPatient";
	
	public static final String MODULE_ID = "covid19";
	
	public static final String COVID_19_APP_ID = "covid.app.home";
	
	public static final Form covidEnrollmentForm = MetadataUtils.existing(Form.class,
	    CovidMetadata._Form.COVID_TREATMENT_ENROLLMENT);
	
	public static final EncounterType covidEnrollmentEncType = MetadataUtils.existing(EncounterType.class,
	    CovidMetadata._EncounterType.COVID_TREATMENT_ENROLLMENT);
	
	public static final EncounterType covidScreeningEncType = MetadataUtils.existing(EncounterType.class,
	    CovidMetadata._EncounterType.COVID_SCREENING);
	
	public static final EncounterType covidTestingEncType = MetadataUtils.existing(EncounterType.class,
	    CovidMetadata._EncounterType.COVID_TESTING);
	
	public static final EncounterType covidClinicalReviewEncType = MetadataUtils.existing(EncounterType.class,
	    CovidMetadata._EncounterType.COVID_CLINICAL_REVIEW);
	
}
