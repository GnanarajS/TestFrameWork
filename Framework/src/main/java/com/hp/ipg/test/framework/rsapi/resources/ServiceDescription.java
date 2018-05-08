package com.hp.ipg.test.framework.rsapi.resources;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.hp.ipg.test.framework.rsapi.config.ApiVersion;
import com.hp.ipg.test.framework.rsapi.config.ResourceName;
import com.hp.ipg.test.framework.rsapi.resources.base.HateoasResource;
import com.hp.ipg.test.framework.rsapi.resources.base.ResourceBase;

public class ServiceDescription extends HateoasResource {

	private static final String LINK_TYPE = "application/xml";
	private static final String REL_LINK = "https://company.printanywhere.com/v1/servicedescription";
	private static final String DIR_SEARCH_DESCRIPTION = "This is the PrinterOn Directory Search API";
	private static final String DOC_API_DESCRIPTION = "This is the PrinterOn DOC API";

	public enum Property{
		online_status,
		capabilities,
		links
	}

	public enum LinksProperty{
		rel,
		type,
		href,
		description
	}

	public enum CapabilitiesProperties{
		deployment_mode,
		preview,
		sra,
		max_file_size,
		dirsearch,
		docapi
	}

	public static ServiceDescription getInstance() throws IOException {
		ServiceDescription serviceDescription = (ServiceDescription) ResourceBase.loadTemplate(ResourceName.SERVICE_DESCRIPTION, ApiVersion.DEFAULT, ServiceDescription.class);
		List<Links> allLinks = new ArrayList<Links>();
		Links dirSearchLinks = new Links();
		dirSearchLinks.setType(LINK_TYPE);
		dirSearchLinks.setRel(REL_LINK);
		dirSearchLinks.setDescription(DIR_SEARCH_DESCRIPTION);
		Links docAPILinks = new Links();
		docAPILinks.setType(LINK_TYPE);
		docAPILinks.setRel(REL_LINK);
		docAPILinks.setDescription(DOC_API_DESCRIPTION);
		allLinks.add(dirSearchLinks);
		allLinks.add(docAPILinks);
		serviceDescription.setLinks(allLinks);
		return serviceDescription;
	}

	public void setOnlineStatus(String status) {
		this.put(Property.online_status.toString(), status);
	}

	public String getOnlineStatus() {
		return (String) this.get(Property.online_status.toString());
	}

	public void setCapabilities(Capabilities capabilities) {
		this.put(Property.capabilities.toString(), capabilities);
	}

	public Capabilities getCapabilities() {
		return (Capabilities) this.get(Property.capabilities.toString());
	}

	public void setLinks(List<Links> links) {
		this.put(Property.links.toString(), links);
	}

	public Links[] getLinks() {
		ArrayList<Links> links = (ArrayList<Links>) this.get(Property.links.toString());
		return (Links[]) links.toArray(new Links[links.size()]);
	}

	public static class Capabilities extends HateoasResource {

		public void setDeploymentMode(String mode) {
			this.put(CapabilitiesProperties.deployment_mode.toString(), mode);
		}

		public void setPreview(String preview) {
			this.put(CapabilitiesProperties.preview.toString(), preview);
		}

		public void setSRA(String sra) {
			this.put(CapabilitiesProperties.sra.toString(), sra);
		}

		public void setMaxFileSize(String maxFileSize) {
			this.put(CapabilitiesProperties.max_file_size.toString(), maxFileSize);
		}

		public void setDirSearch(Boolean dirSearch) {
			this.put(CapabilitiesProperties.dirsearch.toString(), dirSearch);
		}

		public void setDocAPI(Boolean docAPI) {
			this.put(CapabilitiesProperties.docapi.toString(), docAPI);
		}

		public String getDeploymentMode() {
			return (String) this.get(CapabilitiesProperties.deployment_mode.toString());
		}

		public String getPreview() {
			return (String) this.get(CapabilitiesProperties.preview.toString());
		}

		public String getSRA() {
			return (String) this.get(CapabilitiesProperties.sra.toString());
		}

		public String getMaxFileSize() {
			return (String) this.get(CapabilitiesProperties.max_file_size.toString());
		}

		public Boolean getDirSearch() {
			return (Boolean) this.get(CapabilitiesProperties.dirsearch.toString());
		}

		public Boolean getDocAPI() {
			return (Boolean) this.get(CapabilitiesProperties.docapi.toString());
		}
	}

	public static class Links extends HateoasResource {

		public void setRel(String rel) {
			this.put(LinksProperty.rel.toString(), rel);
		}

		public void setType(String type) {
			this.put(LinksProperty.type.toString(), type);
		}

		public void setHref(String href) {
			this.put(LinksProperty.href.toString(), href);
		}

		public void setDescription(String description) {
			this.put(LinksProperty.description.toString(), description);
		}

		public String getRel() {
			return (String) this.get(LinksProperty.rel.toString());
		}

		public String getType() {
			return (String) this.get(LinksProperty.type.toString());
		}

		public String getHref() {
			return (String) this.get(LinksProperty.href.toString());
		}

		public String getDescription() {
			return (String) this.get(LinksProperty.description.toString());
		}
	}
}
