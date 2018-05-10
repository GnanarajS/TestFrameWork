package com.hp.ipg.test.framework.rsapi.resources;

import java.io.IOException;

import com.hp.ipg.test.framework.rsapi.config.ApiVersion;
import com.hp.ipg.test.framework.rsapi.config.ResourceName;
import com.hp.ipg.test.framework.rsapi.resources.base.HateoasResource;
import com.hp.ipg.test.framework.rsapi.resources.base.ResourceBase;

public class PasswordChange extends HateoasResource {

	public enum Property {
		verificationHash,
		password,
		locale
	}

	public static PasswordChange getInstance() throws IOException {
		return (PasswordChange) ResourceBase.loadTemplate(ResourceName.PASSWORD_CHANGE, ApiVersion.DEFAULT, PasswordChange.class);
	}

	public void setVerificationHash(String verificationHash) {
		this.put(Property.verificationHash.toString(), verificationHash);
	}

	public String getVerificationHash() {
		return (String) this.get(Property.verificationHash.toString());
	}

	public void setPassword(String password) {
		this.put(Property.password.toString(), password);
	}

	public String getPassword() {
		return (String) this.get(Property.password.toString());
	}

	public void setLocale(String locale) {
		this.put(Property.locale.toString(), locale);
	}

	public String getLocale() {
		return (String) this.get(Property.locale.toString());
	}
}
