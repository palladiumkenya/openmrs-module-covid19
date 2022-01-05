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

import org.openmrs.module.reporting.data.converter.DataConverter;

/**
 * Converter for RDT test reasons
 */
public class CovidRDTTestReasonsDataConverter implements DataConverter {
	
	@Override
	public Object convert(Object obj) {
		
		if (obj == null) {
			return "";
		}
		String value = (String) obj;
		
		if (value.equals("Symptomatic")) {
			return 1;
		} else if (value.equals("Contact with confirmed case")) {
			return 2;
		} else if (value.equals("Health care worker")) {
			return 3;
		} else if (value.equals("Outbreak investigation")) {
			return 4;
		} else if (value.equals("Prison remand")) {
			return 5;
		} else if (value.equals("High risk client in health facility")) {
			return 6;
		}
		
		return "";
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
