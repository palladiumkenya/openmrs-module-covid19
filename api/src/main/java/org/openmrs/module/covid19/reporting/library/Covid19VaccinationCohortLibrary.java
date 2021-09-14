/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.covid19.reporting.library;

import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.SqlCohortDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Library of cohort definitions for Covid-19 vaccinations
 */
@Component
public class Covid19VaccinationCohortLibrary {
    public CohortDefinition fullyVaccinated(){
        SqlCohortDefinition cd = new SqlCohortDefinition();
        String sqlQuery = "";
        cd.setName("fullyVaccinated");
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.setQuery(sqlQuery);
        cd.setDescription("fullyVaccinated");

        return cd;
    }

    public  CohortDefinition partiallyVaccinated() {
        SqlCohortDefinition cd = new SqlCohortDefinition();
        String sqlQuery="" ;
        cd.setName("partiallyVaccinated");
        cd.setQuery(sqlQuery);
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.setDescription("partiallyVaccinated");

        return cd;
    }

    public  CohortDefinition notVaccinated() {
        String sqlQuery="";
        SqlCohortDefinition cd = new SqlCohortDefinition();
        cd.setName("notVaccinated");
        cd.setQuery(sqlQuery);
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.setDescription("notVaccinated");
        return cd;
    }

}
