package com.hp.ipg.test.framework.rsapi.resources;

import com.hp.ipg.test.framework.rsapi.config.ApiVersion;
import com.hp.ipg.test.framework.rsapi.config.ResourceName;
import com.hp.ipg.test.framework.rsapi.resources.base.HateoasResource;
import com.hp.ipg.test.framework.rsapi.resources.base.ResourceBase;

import java.io.IOException;

public class CheckJobs extends HateoasResource {

	public enum Property {
		fcsAPIfunc,
		printerURI;
	}

	public static CheckJobs getInstance() throws IOException {
		CheckJobs CheckJobs = (CheckJobs) ResourceBase.loadTemplate(ResourceName.CHECK_JOBS, ApiVersion.DEFAULT, CheckJobs.class);
		return CheckJobs;
	}

	public String getFcsApiFunc() {
		return (String) this.get(Property.fcsAPIfunc.toString());
	}
}

