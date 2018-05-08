package com.hp.ipg.test.framework.rsapi.resources;

import com.hp.ipg.test.framework.rsapi.config.ApiVersion;
import com.hp.ipg.test.framework.rsapi.config.ResourceName;
import com.hp.ipg.test.framework.rsapi.resources.base.HateoasResource;
import com.hp.ipg.test.framework.rsapi.resources.base.ResourceBase;

import java.io.IOException;

public class PDHDeleteJob extends HateoasResource {

	public enum Property {
		FcsAPIfunc,
		JobID,
		UserName,
		Password,
		PtID
	}

	public static PDHDeleteJob getInstance() throws IOException {
		PDHDeleteJob pdhDeleteJob = (PDHDeleteJob) ResourceBase.loadTemplate(ResourceName.PDH_DELETE_JOB, ApiVersion.DEFAULT, PDHDeleteJob.class);
		return pdhDeleteJob;
	}

	public String getFcsAPIfunc() {
		return (String) this.get(Property.FcsAPIfunc.toString());
	}

	public String getUserName() {
		return (String) this.get(Property.UserName.toString());
	}

	public String getPassword() {
		return (String) this.get(Property.Password.toString());
	}

	public int getReturnCode(String result) {
		String splitResult[] = result.split("=");
		String returnCode = splitResult[splitResult.length - 1].replaceAll("[^A-Za-z0-9]", "");
		return Integer.parseInt(returnCode);
	}

}

