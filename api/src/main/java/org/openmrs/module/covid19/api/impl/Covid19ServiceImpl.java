/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.covid19.api.impl;

import org.openmrs.api.UserService;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.covid19.api.Covid19Service;
import org.openmrs.module.covid19.api.dao.Covid19Dao;

public class Covid19ServiceImpl extends BaseOpenmrsService implements Covid19Service {
	
	Covid19Dao dao;
	
	UserService userService;
	
	/**
	 * Injected in moduleApplicationContext.xml
	 */
	public void setDao(Covid19Dao dao) {
		this.dao = dao;
	}
	
	/**
	 * Injected in moduleApplicationContext.xml
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
}
