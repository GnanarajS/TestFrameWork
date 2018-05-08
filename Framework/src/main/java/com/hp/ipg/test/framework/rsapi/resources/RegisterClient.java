package com.hp.ipg.test.framework.rsapi.resources;

import java.io.IOException;

import com.hp.ipg.test.framework.rsapi.config.ApiVersion;
import com.hp.ipg.test.framework.rsapi.config.ResourceName;
import com.hp.ipg.test.framework.rsapi.resources.base.HateoasResource;
import com.hp.ipg.test.framework.rsapi.resources.base.ResourceBase;

public class RegisterClient extends HateoasResource {

	public enum Property {
		fcsAPIfunc,
		username,
		timeout,
		printerURI
	}

	public static RegisterClient getInstance() throws IOException {
		RegisterClient registerClient = (RegisterClient) ResourceBase.loadTemplate(ResourceName.REGISTER_CLIENT, ApiVersion.DEFAULT, RegisterClient.class);
		return registerClient;
	}

	public String getFcsApiFunc() {
		return (String) this.get(Property.fcsAPIfunc.toString());
	}

	public String getUsername() {
		return (String) this.get(Property.username.toString());
	}

	public String getTimeout() {
		return (String) this.get(Property.timeout.toString());
	}

	public String getPrinterURI() {
		return (String) this.get(Property.printerURI.toString());
	}
}
