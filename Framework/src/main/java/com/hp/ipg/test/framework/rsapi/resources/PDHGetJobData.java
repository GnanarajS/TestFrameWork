package com.hp.ipg.test.framework.rsapi.resources;

import java.io.IOException;

import com.hp.ipg.test.framework.rsapi.config.ApiVersion;
import com.hp.ipg.test.framework.rsapi.config.ResourceName;
import com.hp.ipg.test.framework.rsapi.resources.base.HateoasResource;
import com.hp.ipg.test.framework.rsapi.resources.base.ResourceBase;

public class PDHGetJobData extends HateoasResource {

	public enum Property {
		FcsAPIfunc,
		DataFormat,
		JobID,
		UserName,
		Password,
		PtID
	}

	public static PDHGetJobData getInstance() throws IOException {
		PDHGetJobData pdhJobsData = (PDHGetJobData) ResourceBase.loadTemplate(ResourceName.PDH_GET_JOB_DATA, ApiVersion.DEFAULT, PDHGetJobData.class);
		return pdhJobsData;
	}

	public String getFcsAPIfunc() {
		return (String) this.get(Property.FcsAPIfunc.toString());
	}

	public String getDataFormat() {
		return (String) this.get(Property.DataFormat.toString());
	}

	public String getUserName() {
		return (String) this.get(Property.UserName.toString());
	}

	public String setPassword(String password) {
		return (String) this.put(Property.Password.toString(), password);
	}

	public String getPassword() {
		return (String) this.get(Property.Password.toString());
	}

	public String getJobId() {
		return (String) this.get(Property.JobID.toString());
	}

	public String setJobId(String jobId) {
		return (String) this.put(Property.JobID.toString(), jobId);
	}

	public String getPtId() {
		return (String) this.get(Property.PtID.toString());
	}

	public String setPtId(String ptId) {
		return (String) this.put(Property.PtID.toString(), ptId);
	}
}

