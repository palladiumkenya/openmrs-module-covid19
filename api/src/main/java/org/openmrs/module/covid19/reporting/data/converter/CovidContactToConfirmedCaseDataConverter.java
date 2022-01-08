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
 * Converter for contacts to confirmed case
 */
public class CovidContactToConfirmedCaseDataConverter implements DataConverter {
	
	@Override
	public Object convert(Object obj) {
		
		if (obj == null) {
			return "";
		}
		String value = (String) obj;
		String convertedString = "";
		
		if (value.equals("Contact with confirmed case")) {
			convertedString = "Y";
		} else {
			convertedString = "N";
		}
		
		return convertedString;
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
