package com.hp.ipg.test.framework.rsapi.resources;

import com.hp.ipg.test.framework.rsapi.config.ApiVersion;
import com.hp.ipg.test.framework.rsapi.config.ResourceName;
import com.hp.ipg.test.framework.rsapi.resources.base.HateoasResource;
import com.hp.ipg.test.framework.rsapi.resources.base.ResourceBase;

import java.io.IOException;

public class PDHGetJob extends HateoasResource {

	public enum Property {
		FcsAPIfunc,
		jobID
	}

	public static PDHGetJob getInstance() throws IOException {
		PDHGetJob pdhGetJob = (PDHGetJob) ResourceBase.loadTemplate(ResourceName.PDH_GET_JOB, ApiVersion.DEFAULT, PDHGetJob.class);
		return pdhGetJob;
	}

	public String getFcsAPIfunc() {
		return (String) this.get(Property.FcsAPIfunc.toString());
	}

}
