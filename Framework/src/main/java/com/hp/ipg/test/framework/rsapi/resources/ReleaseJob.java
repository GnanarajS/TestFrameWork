package com.hp.ipg.test.framework.rsapi.resources;

import com.hp.ipg.test.framework.rsapi.config.ApiVersion;
import com.hp.ipg.test.framework.rsapi.config.ResourceName;
import com.hp.ipg.test.framework.rsapi.resources.base.HateoasResource;
import com.hp.ipg.test.framework.rsapi.resources.base.ResourceBase;

import java.io.IOException;

public class ReleaseJob extends HateoasResource {

    public enum Property {
        operation,
        jobReferenceID,
        outputDestinationID,
        releaseCode
    }

    public static ReleaseJob getInstance() throws IOException {
        ReleaseJob jobRelease = (ReleaseJob) ResourceBase.loadTemplate(ResourceName.RELEASE_JOB, ApiVersion.DEFAULT, ReleaseJob.class);
        return jobRelease;
    }

    public String setOperation(String operation) {
        return (String) this.put(Property.operation.toString(), operation);
    }

    public String setJobReferenceID(String jobReferenceID) {
        return (String) this.put(Property.jobReferenceID.toString(), jobReferenceID);
    }

    public String setOutputDestinationID(String outputDestinationID) {
        return (String) this.put(Property.outputDestinationID.toString(), outputDestinationID);
    }

    public String setReleaseCode(String releaseCode) {
        return (String) this.put(Property.releaseCode.toString(), releaseCode);
    }

    public String getOperation() {
        return (String) this.get(Property.operation.toString());
    }

    public String getJobReferenceID() {
        return (String) this.get(Property.jobReferenceID.toString());
    }

    public String getOutputDestinationID() {
        return (String) this.get(Property.outputDestinationID.toString());
    }

    public String getReleaseCode() {
        return (String) this.get(Property.releaseCode.toString());
    }
}