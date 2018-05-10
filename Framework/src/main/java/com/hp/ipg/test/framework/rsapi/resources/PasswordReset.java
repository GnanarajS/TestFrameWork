package com.hp.ipg.test.framework.rsapi.resources;

import java.io.IOException;

import com.hp.ipg.test.framework.rsapi.config.ApiVersion;
import com.hp.ipg.test.framework.rsapi.config.ResourceName;
import com.hp.ipg.test.framework.rsapi.resources.base.HateoasResource;
import com.hp.ipg.test.framework.rsapi.resources.base.ResourceBase;

public class PasswordReset extends HateoasResource {

	public enum Property {
		email,
		locale
	}

	public static PasswordReset getInstance() throws IOException {
		return (PasswordReset) ResourceBase.loadTemplate(ResourceName.PASSWORD_RESET, ApiVersion.DEFAULT, PasswordReset.class);
	}

	public void setEmail(String email) {
		this.put(Property.email.toString(), email);
	}

	public String getEmail() {
		return (String) this.get(Property.email.toString());
	}

	public void setLocale(String locale) {
		this.put(Property.locale.toString(), locale);
	}

	public String getLocale() {
		return (String) this.get(Property.locale.toString());
	}
}
