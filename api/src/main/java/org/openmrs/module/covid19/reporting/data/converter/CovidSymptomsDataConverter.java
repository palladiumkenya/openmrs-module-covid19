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

import java.util.Arrays;
import org.apache.commons.lang3.StringUtils;
import org.openmrs.module.reporting.data.converter.DataConverter;

/**
 * Created by codehub on 18/06/15.
 */
public class CovidSymptomsDataConverter implements DataConverter {
	
	public static String SYMPTOMS_PRESENCE = "symptomsPresence";
	
	public static String CODED_SYMPTOMS = "codedSymptoms";
	
	private String whatToFormat; // can be 'symptomsPresence' or 'codedSymptoms'
	
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
		System.out.println("Symptoms string::::" + value);
		
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
			 * runny nose 10.Nausea or vomiting 11.Other" if(s.fever = 'Yes','Fever',''), if(s.cough
			 * = 'Yes','Cough',''), if(s.runny_nose = 'Yes','Runny nose',''), if(s.diarrhoea =
			 * 'Yes','Diarrhoea',''), if(s.headache = 'Yes','Headache',''), if(s.muscular_pain =
			 * 'Yes','Muscular pain',''), if(s.abdominal_pain = 'Yes','Abdominal pain',''),
			 * if(s.general_weakness = 'Yes','General weakness',''), if(s.sore_throat =
			 * 'Yes','Sore-throat',''), if(s.breathing_difficulty = 'Yes','Breathing
			 * difficulty',''), if(s.nausea_vomiting = 'Yes','Nausea',''),
			 * if(s.altered_mental_status = 'Yes','Altered mental status',''), if(s.chest_pain =
			 * 'Yes','Chest pain',''), if(s.joint_pain = 'Yes','Joint pain',''),
			 * if(s.loss_of_taste_smell = 'Yes','Loss of taste or smell',''), if(s.other_symptom =
			 * 'Yes','Other','')
			 */
			int[] symptomCodes = new int[symptoms.length];
			for (int i = 0; i < symptoms.length; i++) {
				if (StringUtils.isNotEmpty(symptoms[i]) && symptoms[i].equals("Fever")) {
					symptomCodes[i] = 1;
				} else if (StringUtils.isNotEmpty(symptoms[i]) && symptoms[i].equals("Cough")) {
					symptomCodes[i] = 2;
				} else if (StringUtils.isNotEmpty(symptoms[i]) && symptoms[i].equals("Breathing difficulty")) {
					symptomCodes[i] = 3;
				} else if (StringUtils.isNotEmpty(symptoms[i]) && symptoms[i].equals("General weakness")) {
					symptomCodes[i] = 4;
				} else if (StringUtils.isNotEmpty(symptoms[i])
				        && (symptoms[i].equals("Chest pain") || symptoms[i].equals("Joint pain")
				                || symptoms[i].equals("Abdominal pain") || symptoms[i].equals("Muscular pain"))) {
					symptomCodes[i] = 5;
				} else if (StringUtils.isNotEmpty(symptoms[i]) && symptoms[i].equals("Headache")) {
					symptomCodes[i] = 6;
				} else if (StringUtils.isNotEmpty(symptoms[i]) && symptoms[i].equals("Loss of taste or smell")) {
					symptomCodes[i] = 7;
				} else if (StringUtils.isNotEmpty(symptoms[i]) && symptoms[i].equals("Sore-throat")) {
					symptomCodes[i] = 8;
				} else if (StringUtils.isNotEmpty(symptoms[i]) && symptoms[i].equals("Runny nose")) {
					symptomCodes[i] = 9;
				} else if (StringUtils.isNotEmpty(symptoms[i]) && symptoms[i].equals("Nausea")) {
					symptomCodes[i] = 10;
				} else {
					symptomCodes[i] = 11;
				}
			}
			Arrays.sort(symptomCodes);
			String joinedSymptoms = StringUtils.join(symptomCodes, "/");
			System.out.println("Symptoms::::" + joinedSymptoms);
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
