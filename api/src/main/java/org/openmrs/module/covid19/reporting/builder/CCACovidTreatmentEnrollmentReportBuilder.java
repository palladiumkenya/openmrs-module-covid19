/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.covid19.reporting.builder;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.openmrs.PersonAttributeType;
import org.openmrs.module.covid19.reporting.calculation.RegistrationCountyAddressCalculation;
import org.openmrs.module.covid19.reporting.calculation.RegistrationSubCountyAddressCalculation;
import org.openmrs.module.covid19.reporting.calculation.converter.GeneralCalculationResultConverter;
import org.openmrs.module.covid19.reporting.cohort.definition.PatientsEnrolledInCCACovidTreatmentCohortDefinition;
import org.openmrs.module.covid19.reporting.data.definition.KenyaEMROccupationDataDefinition;
import org.openmrs.module.covid19.reporting.data.definition.covidTreatmentEnrollmentRegister.ActionTakenDataDefinition;
import org.openmrs.module.covid19.reporting.data.definition.covidTreatmentEnrollmentRegister.AdmissionUnitDataDefinition;
import org.openmrs.module.covid19.reporting.data.definition.covidTreatmentEnrollmentRegister.CovidCaseClassificationDataDefinition;
import org.openmrs.module.covid19.reporting.data.definition.covidTreatmentEnrollmentRegister.CovidPatientTypeDataDefinition;
import org.openmrs.module.covid19.reporting.data.definition.covidTreatmentEnrollmentRegister.FacilityReferredFromDataDefinition;
import org.openmrs.module.kenyacore.report.ReportDescriptor;
import org.openmrs.module.kenyacore.report.ReportUtils;
import org.openmrs.module.kenyacore.report.builder.AbstractReportBuilder;
import org.openmrs.module.kenyacore.report.builder.Builds;
import org.openmrs.module.kenyacore.report.data.patient.definition.CalculationDataDefinition;
import org.openmrs.module.kenyaemr.calculation.library.mchcs.PersonAddressCalculation;
import org.openmrs.module.kenyaemr.metadata.CommonMetadata;
import org.openmrs.module.metadatadeploy.MetadataUtils;
import org.openmrs.module.reporting.common.SortCriteria;
import org.openmrs.module.reporting.data.DataDefinition;
import org.openmrs.module.reporting.data.converter.BirthdateConverter;
import org.openmrs.module.reporting.data.converter.DataConverter;
import org.openmrs.module.reporting.data.converter.DateConverter;
import org.openmrs.module.reporting.data.converter.ObjectFormatter;
import org.openmrs.module.reporting.data.encounter.definition.EncounterDatetimeDataDefinition;
import org.openmrs.module.reporting.data.patient.definition.PatientIdDataDefinition;
import org.openmrs.module.reporting.data.person.definition.AgeDataDefinition;
import org.openmrs.module.reporting.data.person.definition.BirthdateDataDefinition;
import org.openmrs.module.reporting.data.person.definition.ConvertedPersonDataDefinition;
import org.openmrs.module.reporting.data.person.definition.GenderDataDefinition;
import org.openmrs.module.reporting.data.person.definition.PersonAttributeDataDefinition;
import org.openmrs.module.reporting.data.person.definition.PreferredNameDataDefinition;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.EncounterDataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.springframework.stereotype.Component;

@Component
@Builds({ "kenyaemr.cca.covid.enrollment.report" })
public class CCACovidTreatmentEnrollmentReportBuilder extends AbstractReportBuilder {
	
	public static final String DATE_FORMAT = "dd/MM/yyyy";
	
	@Override
	protected List<Mapped<DataSetDefinition>> buildDataSets(ReportDescriptor descriptor, ReportDefinition report) {
		
		return Arrays.asList(ReportUtils.map(reportColumns(), "startDate=${startDate},endDate=${endDate}"));
	}
	
	@Override
	protected List<Parameter> getParameters(ReportDescriptor reportDescriptor) {
		return Arrays.asList(new Parameter("startDate", "Start Date", Date.class), new Parameter("endDate", "End Date",
		        Date.class), new Parameter("dateBasedReporting", "", String.class));
	}
	
	/**
	 * Columns for the report
	 * 
	 * @return
	 */
	protected DataSetDefinition reportColumns() {
		EncounterDataSetDefinition dsd = new EncounterDataSetDefinition();
		dsd.setName("covidEnrollmentRegister");
		dsd.setDescription("Covid treatment enrollment");
		dsd.addSortCriteria("Enrollment Date", SortCriteria.SortDirection.ASC);
		dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
		
		String paramMapping = "startDate=${startDate},endDate=${endDate}";
		
		DataConverter nameFormatter = new ObjectFormatter("{familyName}, {givenName} {middleName}");
		DataDefinition nameDef = new ConvertedPersonDataDefinition("name", new PreferredNameDataDefinition(), nameFormatter);
		PersonAttributeType phoneNumber = MetadataUtils.existing(PersonAttributeType.class,
		    CommonMetadata._PersonAttributeType.TELEPHONE_CONTACT);
		
		dsd.addColumn("Name", nameDef, "");
		dsd.addColumn("id", new PatientIdDataDefinition(), "");
		dsd.addColumn("Date of Birth", new BirthdateDataDefinition(), "", new BirthdateConverter(DATE_FORMAT));
		dsd.addColumn("Age", new AgeDataDefinition(), "");
		dsd.addColumn("Sex", new GenderDataDefinition(), "");
		dsd.addColumn("Telephone No", new PersonAttributeDataDefinition(phoneNumber), "");
		dsd.addColumn("County", new CalculationDataDefinition("County", new RegistrationCountyAddressCalculation()), "",
		    new GeneralCalculationResultConverter());
		dsd.addColumn("Sub-County", new CalculationDataDefinition("Sub-County",
		        new RegistrationSubCountyAddressCalculation()), "", new GeneralCalculationResultConverter());
		
		dsd.addColumn("Village/Estate of residence", new CalculationDataDefinition("Village/Estate of residence",
		        new PersonAddressCalculation()), "", new GeneralCalculationResultConverter());
		dsd.addColumn("Occupation", new KenyaEMROccupationDataDefinition(), "");
		dsd.addColumn("Enrollment Date", new EncounterDatetimeDataDefinition(), "", new DateConverter(DATE_FORMAT));
		dsd.addColumn("Case classificaton", new CovidCaseClassificationDataDefinition(), "");
		dsd.addColumn("Patient type", new CovidPatientTypeDataDefinition(), "");
		dsd.addColumn("Hospital referred from", new FacilityReferredFromDataDefinition(), "");
		dsd.addColumn("Action taken", new ActionTakenDataDefinition(), "");
		
		dsd.addColumn("Admission unit", new AdmissionUnitDataDefinition(), "");
		
		// new columns
		
		PatientsEnrolledInCCACovidTreatmentCohortDefinition cd = new PatientsEnrolledInCCACovidTreatmentCohortDefinition();
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		
		dsd.addRowFilter(cd, paramMapping);
		return dsd;
		
	}
	
}
