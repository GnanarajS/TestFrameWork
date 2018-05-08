package com.hp.ipg.test.framework.rsapi.resources;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.UUID;

import com.hp.ipg.test.framework.rsapi.config.ApiVersion;
import com.hp.ipg.test.framework.rsapi.config.ResourceName;
import com.hp.ipg.test.framework.rsapi.resources.base.HateoasResource;
import com.hp.ipg.test.framework.rsapi.resources.base.ResourceBase;

public class PONSite extends HateoasResource{

	private enum Property {
		siteUUID,
		userUUID,
		status,
		site_class,
		config,
		license,
		contact,
		address,
		identity

	}

	private enum ConfigProperty{
		defaultLang,
		printerPrefix,
		guest
	}

	private enum GuestProperty{
		enabled,
		behariour,
		appendPrefix,
		prefix,
		defaultID
	}

	private enum LicenseProperty{
		expirationDate,
		externalRefID,
		userCreateReqConfirm,
		licensedPrinters,
		provisionedPrinter,
		licensedUsers,
		provisionedUsers,
		licenseType,
		licenseQualifier,
		licenseTermType
	}

	private enum ContactProperty{
		firstName,
		lastName,
		emailAddress
	}

	private enum AddressProperty{
		city,
		country,
		addressLine1,
		addressLine2,
		postalZip,
		state
	}

	private enum IdentityProperty{
		orgName,
		orgId,
		siteAuth,
		serviceName,
		serviceURL,
		siteUUID,
		type,
		siteUID
	}

	public static PONSite getInstance(User user) throws IOException{
		PONSite ponSite = (PONSite) ResourceBase.loadTemplate(ResourceName.PON_SITE, ApiVersion.DEFAULT, PONSite.class);
		String siteUUID = String.valueOf(UUID.randomUUID());
		ponSite.setSiteUUID(siteUUID);
		ponSite.setUserUUID(user.getUserId().toString());
		((LinkedHashMap<String, String>) ponSite.get("contact")).put("emailAddress", user.getUserNameFromResponse());
		((LinkedHashMap<String, String>) ponSite.get("identity")).put("siteUUID", siteUUID);
		((LinkedHashMap<String, String>) ponSite.get("identity")).put("siteUID", UUID.randomUUID().toString());
		((LinkedHashMap<String, String>) ponSite.get("identity")).put("orgId", UUID.randomUUID().toString());
		return ponSite;
	}

	// Setter Methods
	public void setSiteUUID(String siteUUID) {
		this.put(Property.siteUUID.toString(), siteUUID);
	}

	public void setUserUUID(String userUUID) {
		this.put(Property.userUUID.toString(), userUUID);
	}

	public void setStatus(String status) {
		this.put(Property.status.toString(), status);
	}

	public void setSiteClass(String siteClass) {
		this.put(Property.site_class.toString(), siteClass);
	}

	public void setConfig(SiteConfig config) {
		this.put(Property.config.toString(), config);
	}

	public void setLicense(SiteLicense license) {
		this.put(Property.license.toString(), license);
	}

	public void setContact(SiteContact contact) {
		this.put(Property.contact.toString(), contact);
	}

	public void setAddress(SiteAddress address) {
		this.put(Property.address.toString(), address);
	}

	public void setIdentity(SiteIdentity identity) {
		this.put(Property.identity.toString(), identity);
	}

	// Getter Methods
	public String getSiteUUID() {
		return (String) this.get(Property.siteUUID.toString());
	}

	public String getUserUUID() {
		return (String) this.get(Property.userUUID.toString());
	}

	public String getStatus() {
		return (String) this.get(Property.status.toString());
	}

	public SiteConfig getConfig() {
		return (SiteConfig) this.get(Property.config.toString());
	}

	public SiteLicense getLicense() {
		return (SiteLicense) this.get(Property.license.toString());
	}

	public SiteAddress getAddress() {
		return (SiteAddress) this.get(Property.address.toString());
	}

	public SiteContact getContact() {
		return (SiteContact) this.get(Property.contact.toString());
	}

	public SiteIdentity getIdentity() {
		return (SiteIdentity) this.get(Property.identity.toString());
	}

	public static class SiteConfig extends HateoasResource{

		// Setter Methods
		public void setDefaultLang(String defaultLang) {
			this.put(ConfigProperty.defaultLang.toString(), defaultLang);
		}

		public void setPrinterPrefix(String printerPrefix) {
			this.put(ConfigProperty.printerPrefix.toString(), printerPrefix);
		}

		public void setGuest(Guest guest) {
			this.put(ConfigProperty.guest.toString(), guest);
		}

		// Getter Methods
		public String getDefaultLang() {
			return (String) this.get(ConfigProperty.defaultLang.toString());
		}

		public String getPrinterPrefix() {
			return (String) this.get(ConfigProperty.printerPrefix.toString());
		}

		public Guest getGuest() {
			return (Guest) this.get(ConfigProperty.guest.toString());
		}
	}

	public static class SiteLicense extends HateoasResource{

		// Setter Methods
		public void setExpirationDate(String expirationDate) {
			this.put(LicenseProperty.expirationDate.toString(), expirationDate);
		}

		public void setExternalRefID(String externalRefID) {
			this.put(LicenseProperty.externalRefID.toString(), externalRefID);
		}

		public void setUserCreateReqConfirm(String userCreateReqConfirm) {
			this.put(LicenseProperty.userCreateReqConfirm.toString(), userCreateReqConfirm);
		}

		public void setLicensedPrinters(String licensedPrinters) {
			this.put(LicenseProperty.licensedPrinters.toString(), licensedPrinters);
		}

		public void setProvisionedPrinter(String provisionedPrinter) {
			this.put(LicenseProperty.provisionedPrinter.toString(), provisionedPrinter);
		}

		public void setLicensedUsers(String licensedUsers) {
			this.put(LicenseProperty.licensedUsers.toString(), licensedUsers);
		}

		public void setProvisionedUsers(String provisionedUsers) {
			this.put(LicenseProperty.provisionedUsers.toString(), provisionedUsers);
		}

		public void setLicenseType(String licenseType) {
			this.put(LicenseProperty.licenseType.toString(), licenseType);
		}

		public void setLicenseQualifier(String licenseQualifier) {
			this.put(LicenseProperty.licenseQualifier.toString(), licenseQualifier);
		}

		public void setLicenseTermType(String licenseTermType) {
			this.put(LicenseProperty.licenseTermType.toString(), licenseTermType);
		}

		// Getter Methods
		public String getExpirationDate() {
			return (String) this.get(LicenseProperty.expirationDate.toString());
		}

		public String getExternalRefID() {
			return (String) this.get(LicenseProperty.externalRefID.toString());
		}

		public String getUserCreateReqConfirm() {
			return (String) this.get(LicenseProperty.userCreateReqConfirm.toString());
		}

		public String getLicensedPrinters() {
			return (String) this.get(LicenseProperty.licensedPrinters.toString());
		}

		public String getProvisionedPrinter() {
			return (String) this.get(LicenseProperty.provisionedPrinter.toString());
		}

		public String getLicensedUsers() {
			return (String) this.get(LicenseProperty.licensedUsers.toString());
		}

		public String getProvisionedUsers() {
			return (String) this.get(LicenseProperty.provisionedUsers.toString());
		}

		public String getLicenseType() {
			return (String) this.get(LicenseProperty.licenseType.toString());
		}

		public String getLicenseQualifier() {
			return (String) this.get(LicenseProperty.licenseQualifier.toString());
		}

		public String getLicenseTermType() {
			return (String) this.get(LicenseProperty.licenseTermType.toString());
		}
	}

	public static class SiteAddress extends HateoasResource{

		// Setter Methods
		public void setCity(String city) {
			this.put(AddressProperty.city.toString(), city);
		}

		public void setCountry(String country) {
			this.put(AddressProperty.country.toString(), country);
		}

		public void setAddressLine1(String addressLine1) {
			this.put(AddressProperty.addressLine1.toString(), addressLine1);
		}

		public void setPostalZip(String postalZip) {
			this.put(AddressProperty.postalZip.toString(), postalZip);
		}

		public void setAddressLine2(String addressLine2) {
			this.put(AddressProperty.addressLine2.toString(), addressLine2);
		}

		public void setState(String state) {
			this.put(AddressProperty.state.toString(), state);
		}

	}

	public static class SiteContact extends HateoasResource{

		// Setter Methods
		public void setFirstName(String firstName) {
			this.put(ContactProperty.firstName.toString(), firstName);
		}

		public void setLastName(String lastName) {
			this.put(ContactProperty.lastName.toString(), lastName);
		}

		public void setEmailAddress(String emailAddress) {
			this.put(ContactProperty.emailAddress.toString(), emailAddress);
		}
		
		//Getter methods
		public String getFirstName() {
			return (String) this.get(ContactProperty.firstName.toString());
		}
		
		public String getLasttName() {
			return (String) this.get(ContactProperty.lastName.toString());
		}
		
		public String getEmailAddress() {
			return (String) this.get(ContactProperty.emailAddress.toString());
		}
	}

	public static class Guest extends HateoasResource{

		// Setter Methods
		public void setEnabled(String enabled) {
			this.put(GuestProperty.enabled.toString(), enabled);
		}

		public void setBehariour(String behariour) {
			this.put(GuestProperty.behariour.toString(), behariour);
		}

		public void setAppendPrefix(String appendPrefix) {
			this.put(GuestProperty.appendPrefix.toString(), appendPrefix);
		}

		public void setPrefix(String prefix) {
			this.put(GuestProperty.prefix.toString(), prefix);
		}

		public void setDefaultID(String defaultID) {
			this.put(GuestProperty.defaultID.toString(), defaultID);
		}
	}

	public static class SiteIdentity extends HateoasResource{

		// Setter Methods
		public void setSiteUUID(String siteUUID) {
			this.put(IdentityProperty.siteUUID.toString(), siteUUID);
		}

		public void setSiteUID(String siteUID) {
			this.put(IdentityProperty.siteUID.toString(), siteUID);
		}

		public void setSiteAuth(String siteAuth) {
			this.put(IdentityProperty.siteAuth.toString(), siteAuth);
		}

		public void setOrdId(String orgId) {
			this.put(IdentityProperty.orgId.toString(), orgId);
		}

		public void setOrgName(String orgName) {
			this.put(IdentityProperty.orgName.toString(), orgName);
		}

		public void setServiceName(String serviceName) {
			this.put(IdentityProperty.serviceName.toString(), serviceName);
		}

		public void setServiceURL(String serviceURL) {
			this.put(IdentityProperty.serviceURL.toString(), serviceURL);
		}

		public void setType(String type) {
			this.put(IdentityProperty.type.toString(), type);
		}

		// Getter Methods
		public String getOrgId() {
			return (String) this.get(IdentityProperty.orgId.toString());
		}
	}
}

