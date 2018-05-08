package com.hp.ipg.test.framework.rsapi.resources;

import com.hp.ipg.test.framework.rsapi.config.ApiVersion;
import com.hp.ipg.test.framework.rsapi.config.ResourceName;
import com.hp.ipg.test.framework.rsapi.resources.base.HateoasResource;
import com.hp.ipg.test.framework.rsapi.resources.base.ResourceBase;

import java.io.IOException;

public class PDHGetJobs extends HateoasResource {

	public enum Property {
		FcsAPIfunc,
		UserName,
		Password,
		PrinterURI,
		WhichJobs,
	}

	public static PDHGetJobs getInstance() throws IOException {
		PDHGetJobs pdhGetJobs = (PDHGetJobs) ResourceBase.loadTemplate(ResourceName.PDH_GET_JOBS, ApiVersion.DEFAULT, PDHGetJobs.class);
		return pdhGetJobs;
	}

	public String getFcsAPIfunc() {
		return (String) this.get(Property.FcsAPIfunc.toString());
	}

	public String getWhichJobs() {
		return (String) this.get(Property.WhichJobs.toString());
	}

}
