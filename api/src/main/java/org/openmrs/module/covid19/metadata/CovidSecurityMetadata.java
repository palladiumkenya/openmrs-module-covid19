package org.openmrs.module.covid19.metadata;

import org.openmrs.module.metadatadeploy.bundle.AbstractMetadataBundle;
import org.openmrs.module.metadatadeploy.bundle.Requires;
import org.springframework.stereotype.Component;

import static org.openmrs.module.metadatadeploy.bundle.CoreConstructors.*;

/**
 * Implementation of access control to the app.
 */
@Component
@Requires(org.openmrs.module.kenyaemr.metadata.SecurityMetadata.class)
public class CovidSecurityMetadata extends AbstractMetadataBundle {
	
	public static class _Privilege {
		
		public static final String APP_COVID_ADMIN = "App: covid.app.home";
	}
	
	public static final class _Role {
		
		public static final String APPLICATION_COVID_ADMIN = "covid-19 app administration";
	}
	
	/**
	 * @see AbstractMetadataBundle#install()
	 */
	@Override
	public void install() {
		
		install(privilege(_Privilege.APP_COVID_ADMIN, "Able to assess covid-19 for a patient"));
		install(role(_Role.APPLICATION_COVID_ADMIN, "Can access covid-19 app",
		    idSet(org.openmrs.module.kenyaemr.metadata.SecurityMetadata._Role.API_PRIVILEGES_VIEW_AND_EDIT),
		    idSet(_Privilege.APP_COVID_ADMIN)));
	}
}
