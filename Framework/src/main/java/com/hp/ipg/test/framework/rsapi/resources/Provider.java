package com.hp.ipg.test.framework.rsapi.resources;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.RandomStringUtils;

import com.hp.ipg.test.framework.rsapi.config.ApiVersion;
import com.hp.ipg.test.framework.rsapi.config.ResourceName;
import com.hp.ipg.test.framework.rsapi.resources.base.HateoasResource;
import com.hp.ipg.test.framework.rsapi.resources.base.ResourceBase;

public class Provider extends HateoasResource{
	
	private static final String LINK_TYPE = "application/json";
	private static final String REL_LINK ="https://company.printanywhere.com/v1/servicedescription";
	
	private enum Property {
		providerUUID,
		providerName,
		providerType,
		links
	}
	
	private enum LinksProperty{
		rel,
		type,
		href
	}
	
	public static Provider getInstance() throws IOException{
		Provider provider = (Provider) ResourceBase.loadTemplate(ResourceName.PROVIDER, ApiVersion.DEFAULT, Provider.class);
		provider.setProviderUUID(String.valueOf(UUID.randomUUID()));
		List<Links> allLinks = new ArrayList<Links>();
		Links link = new Links();
		link.setType(LINK_TYPE);
		link.setRel(REL_LINK);
		allLinks.add(link);
		provider.setLinks(allLinks);
		return provider;
	}
	
	public void setProviderUUID(String UUID) {
		this.put(Property.providerUUID.toString(), UUID);
	}
	
	public void setProviderName(String providerName) {
		this.put(Property.providerName.toString(), providerName);
	}
	
	public void setProviderType(String providerType) {
		this.put(Property.providerType.toString(), providerType);
	}
	
	public String getProviderUUID() {
		return (String) this.get(Property.providerUUID.toString());
	}
	
	public String getProviderName() {
		return (String) this.get(Property.providerName.toString());
	}
	
	public String getProviderType() {
		return (String) this.get(Property.providerType.toString());
	}
	
	public Links[] getLinks() {
		ArrayList<Links> links = (ArrayList<Links>)this.get(Property.links.toString());
		return (Links[]) links.toArray(new Links[links.size()]);
	}
	
	public void setLinks(List<Links> links) {
		put(Property.links.toString(), links);
	}
	
	public static class Links extends HateoasResource{
		
		public void setRel(String rel) {
			this.put(LinksProperty.rel.toString(), rel);
		}
		
		public void setType(String type) {
			this.put(LinksProperty.type.toString(), type);
		}
		
		public void setHref(String href) {
			this.put(LinksProperty.href.toString(), href);
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
		
	}	
}
