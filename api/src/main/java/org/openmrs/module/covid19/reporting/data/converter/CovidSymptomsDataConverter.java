/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.covid19.reporting.data.converter;

import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.module.reporting.data.converter.DataConverter;

/**
 * Created by codehub on 18/06/15.
 */
public class CovidSymptomsDataConverter implements DataConverter {
	
	public static String SYMPTOMS_PRESENCE = "symptomsPresence";
	
	public static String CODED_SYMPTOMS = "codedSymptoms";
	
	private String whatToFormat;
	
	public CovidSymptomsDataConverter(String whatToFormat) {
		this.whatToFormat = whatToFormat;
	}
	
	@Override
	public Object convert(Object obj) {
		
		if (obj == null) {
			return "";
		}
		String value = (String) obj;
		String[] symptoms = value.split(",");
		
		if (whatToFormat != null && whatToFormat.equals(SYMPTOMS_PRESENCE)) {
			// split the string and check for non-null
			for (int i = 0; i < symptoms.length; i++) {
				if (StringUtils.isNotEmpty(symptoms[i])) {
					return "Y";
				}
			}
			return "N";
			
		} else if (whatToFormat != null && whatToFormat.equals(CODED_SYMPTOMS)) {
			/**
			 * indicate the symptoms shown, list them separated by a semi colon (;) eg 1; 5; 1.
			 * Fever 2. Cough 3.Shortness of breath or difficulty breathing 4. Fatigue 5.Muscle or
			 * body aches 6. Headache 7. New loss of taste or smell 8.Sore throat 9.Congestion or
			 * runny nose 10.Nausea or vomiting 11.Other" Possible answers from data definition
			 * 'Fever','Cough','Runny nose','Diarrhoea','Headache','Muscular pain','Abdominal
			 * pain','General weakness','Sore-throat', 'Breathing difficulty','Nausea','Altered
			 * mental status','Chest pain','Joint pain','Loss of taste or smell','Other'
			 */
			Set<Integer> symptomCodes = new TreeSet<Integer>();
			for (int i = 0; i < symptoms.length - 1; i++) {
				if (StringUtils.isNotEmpty(symptoms[i]) && symptoms[i].equals("Fever")) {
					symptomCodes.add(1);
				} else if (StringUtils.isNotEmpty(symptoms[i]) && symptoms[i].equals("Cough")) {
					symptomCodes.add(2);
				} else if (StringUtils.isNotEmpty(symptoms[i]) && symptoms[i].equals("Breathing difficulty")) {
					symptomCodes.add(3);
				} else if (StringUtils.isNotEmpty(symptoms[i]) && symptoms[i].equals("General weakness")) {
					symptomCodes.add(4);
				} else if (StringUtils.isNotEmpty(symptoms[i])
				        && (symptoms[i].equals("Chest pain") || symptoms[i].equals("Joint pain")
				                || symptoms[i].equals("Abdominal pain") || symptoms[i].equals("Muscular pain"))) {
					symptomCodes.add(5);
				} else if (StringUtils.isNotEmpty(symptoms[i]) && symptoms[i].equals("Headache")) {
					symptomCodes.add(6);
				} else if (StringUtils.isNotEmpty(symptoms[i]) && symptoms[i].equals("Loss of taste or smell")) {
					symptomCodes.add(7);
				} else if (StringUtils.isNotEmpty(symptoms[i]) && symptoms[i].equals("Sore-throat")) {
					symptomCodes.add(8);
				} else if (StringUtils.isNotEmpty(symptoms[i]) && symptoms[i].equals("Runny nose")) {
					symptomCodes.add(9);
				} else if (StringUtils.isNotEmpty(symptoms[i]) && symptoms[i].equals("Nausea")) {
					symptomCodes.add(10);
					
				} else if (StringUtils.isNotEmpty(symptoms[i])) {
					symptomCodes.add(11);
				}
				
			}
			String joinedSymptoms = StringUtils.join(symptomCodes, "/");
			return joinedSymptoms;
			
		}
		
		return "";
	}
	
	public String getWhatToFormat() {
		return whatToFormat;
	}
	
	public void setWhatToFormat(String whatToFormat) {
		this.whatToFormat = whatToFormat;
	}
	
	@Override
	public Class<?> getInputDataType() {
		return String.class;
	}
	
	@Override
	public Class<?> getDataType() {
		return String.class;
	}
}
