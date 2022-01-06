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
import org.openmrs.module.covid19.reporting.cohort.definition.CovidRDTTestsCohortDefinition;
import org.openmrs.module.covid19.reporting.data.converter.CovidRDTActionTakenDataConverter;
import org.openmrs.module.covid19.reporting.data.converter.CovidRDTResultDataConverter;
import org.openmrs.module.covid19.reporting.data.converter.CovidRDTTestReasonsDataConverter;
import org.openmrs.module.covid19.reporting.data.converter.CovidSymptomsDataConverter;
import org.openmrs.module.covid19.reporting.data.definition.CovidRDTAssayKitNameDataDefinition;
import org.openmrs.module.covid19.reporting.data.definition.CovidRDTCaseTypeDataDefinition;
import org.openmrs.module.covid19.reporting.data.definition.CovidRDTCovidSymptomsDataDefinition;
import org.openmrs.module.covid19.reporting.data.definition.CovidRDTDateDataDefinition;
import org.openmrs.module.covid19.reporting.data.definition.CovidRDTKitLotAndExpiryDataDefinition;
import org.openmrs.module.covid19.reporting.data.definition.CovidRDTNationalityDataDefinition;
import org.openmrs.module.covid19.reporting.data.definition.CovidRDTOnsetOfSymptomsDateDataDefinition;
import org.openmrs.module.covid19.reporting.data.definition.CovidRDTPatientIdentifierDataDefinition;
import org.openmrs.module.covid19.reporting.data.definition.CovidRDTPostResultActionTakenDataDefinition;
import org.openmrs.module.covid19.reporting.data.definition.CovidRDTResultDataDefinition;
import org.openmrs.module.covid19.reporting.data.definition.CovidRDTSampleTypeDataDefinition;
import org.openmrs.module.covid19.reporting.data.definition.CovidRDTTestReasonDataDefinition;
import org.openmrs.module.covid19.reporting.data.definition.CovidRDTVaccinationDosageDataDefinition;
import org.openmrs.module.covid19.reporting.data.definition.CovidRDTVaccinationStatusDataDefinition;
import org.openmrs.module.covid19.reporting.data.definition.KenyaEMROccupationDataDefinition;
import org.openmrs.module.kenyacore.report.ReportDescriptor;
import org.openmrs.module.kenyacore.report.ReportUtils;
import org.openmrs.module.kenyacore.report.builder.AbstractReportBuilder;
import org.openmrs.module.kenyacore.report.builder.Builds;
import org.openmrs.module.kenyacore.report.data.patient.definition.CalculationDataDefinition;
import org.openmrs.module.kenyaemr.calculation.library.mchcs.PersonAddressCalculation;
import org.openmrs.module.kenyaemr.metadata.CommonMetadata;
import org.openmrs.module.kenyaemr.reporting.data.converter.definition.HTSProviderDataDefinition;
import org.openmrs.module.metadatadeploy.MetadataUtils;
import org.openmrs.module.reporting.common.SortCriteria;
import org.openmrs.module.reporting.data.DataDefinition;
import org.openmrs.module.reporting.data.converter.BirthdateConverter;
import org.openmrs.module.reporting.data.converter.DataConverter;
import org.openmrs.module.reporting.data.converter.ObjectFormatter;
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
@Builds({ "kenyaemr.cca.rdt.tests.report" })
public class CovidRDTTestResultsReportBuilder extends AbstractReportBuilder {
	
	public static final String DATE_FORMAT = "dd/MM/yyyy";
	
	String paramMapping = "startDate=${startDate},endDate=${endDate}";
	
	@Override
	protected List<Parameter> getParameters(ReportDescriptor reportDescriptor) {
		return Arrays.asList(new Parameter("startDate", "Start Date", Date.class), new Parameter("endDate", "End Date",
		        Date.class), new Parameter("dateBasedReporting", "", String.class));
	}
	
	@Override
	protected List<Mapped<DataSetDefinition>> buildDataSets(ReportDescriptor reportDescriptor,
	        ReportDefinition reportDefinition) {
		return Arrays.asList(ReportUtils.map(reportColumns(), "startDate=${startDate},endDate=${endDate}"));
	}
	
	/**
	 * Columns for the report
	 * 
	 * @return
	 */
	protected DataSetDefinition reportColumns() {
		EncounterDataSetDefinition dsd = new EncounterDataSetDefinition();
		dsd.setName("rdtRegister");
		dsd.setDescription("Visit information");
		dsd.addSortCriteria("Visit Date", SortCriteria.SortDirection.ASC);
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
		dsd.addColumn("ID/Passport number", new CovidRDTPatientIdentifierDataDefinition(), "");
		dsd.addColumn("Nationality", new CovidRDTNationalityDataDefinition(), "");
		dsd.addColumn("County", new CalculationDataDefinition("County", new RegistrationCountyAddressCalculation()), "",
		    new GeneralCalculationResultConverter());
		dsd.addColumn("Sub-County", new CalculationDataDefinition("Sub-County",
		        new RegistrationSubCountyAddressCalculation()), "", new GeneralCalculationResultConverter());
		dsd.addColumn("Village/Estate of residence", new CalculationDataDefinition("Village/Estate of residence",
		        new PersonAddressCalculation()), "", new GeneralCalculationResultConverter());
		dsd.addColumn("Occupation", new KenyaEMROccupationDataDefinition(), "");
		
		dsd.addColumn("Type of case", new CovidRDTCaseTypeDataDefinition(), null);
		dsd.addColumn("Sample type", new CovidRDTSampleTypeDataDefinition(), null);
		dsd.addColumn("Reason for testing", new CovidRDTTestReasonDataDefinition(), "",
		    new CovidRDTTestReasonsDataConverter());
		dsd.addColumn("Date of sample collection", new CovidRDTDateDataDefinition(), null);
		dsd.addColumn("Assay kit name", new CovidRDTAssayKitNameDataDefinition(), null);
		
		dsd.addColumn("Lot No/expiry", new CovidRDTKitLotAndExpiryDataDefinition(), null);
		dsd.addColumn("Antigen result", new CovidRDTResultDataDefinition(), "", new CovidRDTResultDataConverter());
		dsd.addColumn("Date of onset of symptoms", new CovidRDTOnsetOfSymptomsDateDataDefinition(), null);
		dsd.addColumn("Have symptoms", new CovidRDTCovidSymptomsDataDefinition(), "", new CovidSymptomsDataConverter(
		        CovidSymptomsDataConverter.SYMPTOMS_PRESENCE));
		dsd.addColumn("Symptoms shown", new CovidRDTCovidSymptomsDataDefinition(), "", new CovidSymptomsDataConverter(
		        CovidSymptomsDataConverter.CODED_SYMPTOMS));
		
		dsd.addColumn("Vaccination status", new CovidRDTVaccinationStatusDataDefinition(), null);
		dsd.addColumn("Dosage", new CovidRDTVaccinationDosageDataDefinition(), null);
		dsd.addColumn("Action taken", new CovidRDTPostResultActionTakenDataDefinition(), "",
		    new CovidRDTActionTakenDataConverter());
		
		dsd.addColumn("Tester initials", new HTSProviderDataDefinition(), null);
		
		// new columns
		
		CovidRDTTestsCohortDefinition cd = new CovidRDTTestsCohortDefinition();
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		
		dsd.addRowFilter(cd, paramMapping);
		return dsd;
		
	}
}
