package com.hp.ipg.test.framework.rsapi.config;

/**
 * Version enumeration for versions in system
 */
public enum ApiVersion {
	DEFAULT("default");

	private String version;

	ApiVersion(String version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return this.version;
	}

	public static ApiVersion fromString(String version) {
		for (ApiVersion av : ApiVersion.values()) {
			if (version.equalsIgnoreCase(av.toString())) {
				return av;
			}
		}
		throw new IllegalArgumentException(version + " is not a valid value for ApiVersion.");
	}
}
