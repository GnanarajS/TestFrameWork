package com.hp.ipg.test.framework.rsapi.resources;

import com.hp.ipg.test.framework.rsapi.config.ApiVersion;
import com.hp.ipg.test.framework.rsapi.config.ResourceName;
import com.hp.ipg.test.framework.rsapi.resources.base.HateoasResource;
import com.hp.ipg.test.framework.rsapi.resources.base.ResourceBase;
import java.io.IOException;

public class DocStatus extends HateoasResource {

    public enum Property {
        docAPIfunc,
        jobReferenceID,
        jobDestination,
        clientSWVer,
        clientSWProtocol,
        clientSWName,
        clientSWKey
    }

    public static DocStatus getInstance() throws IOException {
        return (DocStatus) ResourceBase.loadTemplate(ResourceName.DOC_STATUS, ApiVersion.DEFAULT, DocStatus.class);
    }

    public void setDocAPIfunc(String docAPIfunc) {
        this.put(Property.docAPIfunc.toString(), docAPIfunc);
    }

    public String getDocAPIfunc() {
        return (String) this.get(Property.docAPIfunc.toString());
    }

    public void setJobReferenceId(String jobReferenceId) {
        this.put(Property.jobReferenceID.toString(), jobReferenceId);
    }

    public String getJobReferenceId() {
        return (String) this.get(Property.jobReferenceID.toString());
    }

    public void setJobDestination(String jobDestination) {
        this.put(Property.jobDestination.toString(), jobDestination);
    }

    public String getJobDestination() {
        return (String) this.get(Property.jobDestination.toString());
    }

    public void setClientSWVer(String clientSWVer) {
        this.put(Property.clientSWVer.toString(), clientSWVer);
    }

    public String getClientSWVer() {
        return (String) this.get(Property.clientSWVer.toString());
    }

    public void setClientSWName(String clientSWName) {
        this.put(Property.clientSWName.toString(), clientSWName);
    }

    public String getClientSWName() {
        return (String) this.get(Property.clientSWName.toString());
    }

    public void setClientSWKey(String clientSWKey) {
        this.put(Property.clientSWKey.toString(), clientSWKey);
    }

    public String getClientSWKey() {
        return (String) this.get(Property.clientSWKey.toString());
    }

    public void setClientSWProtocol(String clientSWKey) {
        this.put(Property.clientSWProtocol.toString(), clientSWKey);
    }

    public String getClientSWProtocol() {
        return (String) this.get(Property.clientSWProtocol.toString());
    }
}