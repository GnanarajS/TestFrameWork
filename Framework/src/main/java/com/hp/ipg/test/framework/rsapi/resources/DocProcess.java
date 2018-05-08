package com.hp.ipg.test.framework.rsapi.resources;

import java.io.IOException;

import com.hp.ipg.test.framework.rsapi.config.ApiVersion;
import com.hp.ipg.test.framework.rsapi.config.ResourceName;
import com.hp.ipg.test.framework.rsapi.resources.base.HateoasResource;
import com.hp.ipg.test.framework.rsapi.resources.base.ResourceBase;

public class DocProcess extends HateoasResource {

	public enum Property {
		docAPIfunc,
		clientSWProtocol,
		clientSWKey,
		clientSWName,
		clientSWVer,
		userLang,
		jobDestination,
		jobReferenceID,
		releaseCode,
		documentURI,
		poColor
	}

	public static DocProcess getInstance() throws IOException {
		DocProcess docProcess = (DocProcess) ResourceBase.loadTemplate(ResourceName.DOC_PROCESS, ApiVersion.DEFAULT, DocProcess.class);
		return docProcess;
	}

	public String setDocApiFunction(String docApi) {
		return (String) this.put(Property.docAPIfunc.toString(), docApi);
	}

	public String setClientSWProtocol(String clientSWProtocol) {
		return (String) this.put(Property.clientSWProtocol.toString(), clientSWProtocol);
	}

	public String setClientSWKey(String clientSWKey) {
		return (String) this.put(Property.clientSWKey.toString(), clientSWKey);
	}

	public String setClientSWName(String clientSWName) {
		return (String) this.put(Property.clientSWName.toString(), clientSWName);
	}

	public String setClientSWVer(String clientSWVer) {
		return (String) this.put(Property.clientSWVer.toString(), clientSWVer);
	}

	public String setUserLang(String userLang) {
		return (String) this.put(Property.userLang.toString(), userLang);
	}

	public String setJobDestination(String jobDestination) {
		return (String) this.put(Property.jobDestination.toString(), jobDestination);
	}

	public String getDocApiFunction() {
		return (String) this.get(Property.docAPIfunc.toString());
	}

	public String getClientSWProtocol() {
		return (String) this.get(Property.clientSWProtocol.toString());
	}

	public String getCientSWKey() {
		return (String) this.get(Property.clientSWKey.toString());
	}

	public String getClientSWName() {
		return (String) this.get(Property.clientSWName.toString());
	}

	public String getClientSWVer() {
		return (String) this.get(Property.clientSWVer.toString());
	}

	public String getJobReferenceID() {
		return (String) this.get(Property.jobReferenceID.toString());
	}

	public String getReleaseCode() {
		return (String) this.get(Property.releaseCode.toString());
	}

	public String setJobReferenceID(String refID) {
		return (String) this.put(Property.jobReferenceID.toString(), refID);
	}

	public String setReleaseCode(String releaseCode) {
		return (String) this.put(Property.releaseCode.toString(), releaseCode);
	}

	public String getJobDestination() {
		return (String) this.get(Property.jobDestination.toString());
	}

	public String getUserLang() {
		return (String) this.get(Property.userLang.toString());
	}

	public String getDocumentURI() {
		return (String) this.get(Property.documentURI.toString());
	}

	public String getPoColor() {
		return (String) this.get(Property.poColor.toString());
	}

}