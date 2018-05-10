package com.hp.ipg.test.framework.rsapi.config;

/**
 * ResourceName enumeration for all resource objects in api
 */
public enum ResourceName {

	HP_ROAM("HpRoam"),
	JOBS("Jobs"),
	SERVICE_DESCRIPTION("ServiceDescription"),
	USERS("Users"),
	USER("User"),
	DOC_PROCESS("DocProcess"),
	DIR_SEARCH("DirSearch"),
	RELEASE_JOB("ReleaseJob"),
	DOC_STATUS("DocStatus"),
	DOC_API("DocApi"),
	REGISTER_CLIENT("RegisterClient"),
	CHECK_JOBS("CheckJobs"),
	PDH("Pdh"),
	PDH_GET_JOB_DATA("PDHGetJobData"),
	PDH_DELETE_JOB("PDHDeleteJob"),
	PDH_GET_JOB("PDHGetJob"),
	PDH_GET_JOBS("PDHGetJobs"),
	SITE_USER("SiteUser"),
	SITE("Site"),
	PROVIDER("Provider"),
	PROVIDERS("Providers"),
	SERVICE_DESCRIPTION_BY_UUID("ServiceDescriptionByUUID"),
	LINK_USER_PROVIDER("LinkUserProvider"),
	SITE_CONFIGURATION("SiteConfiguration"),
	PON_SITE("PONSite"),
	SITES("Sites"),
	SITE_ADDRESS("SiteAddress"),
	SITE_IDENTITY("SiteIdentity"),
	SITE_LICENSE("SiteLicense"),
	SITE_CONTACT("SiteContact"),
	AUTHENTICATION("Authentication"),
	AUTHENTICATE_JWT("AuthenticateJWT"),
	JWT_TOKEN("JWTToken"),
	DEVICE_IMAGE("DeviceImage"),
	DEVICE_IMAGE_MANUFACTURER("DeviceImageManufacturer"),
	DEVICE_IMAGE_MODEL("DeviceImageModel"),
	EMPTY_DEVICE_IMAGE_MODEL("EmptyDeviceImageModel");


	private String name;

	ResourceName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}

	public static ResourceName fromString(String resourceString) {
		for (ResourceName rn : ResourceName.values()) {
			if (resourceString.equalsIgnoreCase(rn.toString())) {
				return rn;
			}
		}
		throw new IllegalArgumentException(resourceString + " is not a valid value for ResourceName.");
	}
}
