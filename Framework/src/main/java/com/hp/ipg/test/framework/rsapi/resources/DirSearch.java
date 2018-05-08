package com.hp.ipg.test.framework.rsapi.resources;

import java.io.IOException;

import com.hp.ipg.test.framework.rsapi.config.ApiVersion;
import com.hp.ipg.test.framework.rsapi.config.ResourceName;
import com.hp.ipg.test.framework.rsapi.resources.base.HateoasResource;
import com.hp.ipg.test.framework.rsapi.resources.base.ResourceBase;

public class DirSearch extends HateoasResource {

	private enum Property {
		ponAPIfunc,
		clientSWKey,
		clientSWName,
		clientSWVer,
		showChildren,
		reportDetail,
		geoLocation,
		geoProximity
	}

	public static DirSearch getInstance() throws IOException {
		DirSearch dirSearch = (DirSearch) ResourceBase.loadTemplate(ResourceName.DIR_SEARCH, ApiVersion.DEFAULT, DirSearch.class);
		return dirSearch;
	}

	public String getPonAPIfunc() {
		return (String) this.get(Property.ponAPIfunc.toString());
	}

	public String getClientSWKey() {
		return (String) this.get(Property.clientSWKey.toString());
	}

	public String getClientSWName() {
		return (String) this.get(Property.clientSWName.toString());
	}

	public String getClientSWVer() {
		return (String) this.get(Property.clientSWVer.toString());
	}

	public String getShowChildren() {
		return (String) this.get(Property.showChildren.toString());
	}

	public String getReportDetail() {
		return (String) this.get(Property.reportDetail.toString());
	}

	public String getGeoLocation() {
		return (String) this.get(Property.geoLocation.toString());
	}

	public String getGeoProximity() {
		return (String) this.get(Property.geoProximity.toString());
	}
}
