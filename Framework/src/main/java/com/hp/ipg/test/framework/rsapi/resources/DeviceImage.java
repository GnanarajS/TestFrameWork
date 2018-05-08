package com.hp.ipg.test.framework.rsapi.resources;

import java.io.IOException;

import com.hp.ipg.test.framework.rsapi.config.ApiVersion;
import com.hp.ipg.test.framework.rsapi.config.ResourceName;
import com.hp.ipg.test.framework.rsapi.resources.DocProcess.Property;
import com.hp.ipg.test.framework.rsapi.resources.base.HateoasResource;
import com.hp.ipg.test.framework.rsapi.resources.base.ResourceBase;

public class DeviceImage extends HateoasResource {

	public enum Property {
		Make,
		Model,
		Content
	}

	public static DeviceImage getInstance() throws IOException {
		DeviceImage deviceImage = (DeviceImage) ResourceBase.loadTemplate(ResourceName.DEVICE_IMAGE, ApiVersion.DEFAULT, DeviceImage.class);
		return deviceImage;
	}

	// Setter Methods
	public void setDeviceModel(String model) {
		this.put(Property.Model.toString(), model);
	}

	public void setDeviceManufacturer(String make) {
		this.put(Property.Make.toString(), make);
	}

	public void setDeviceContent(String content) {
		this.put(Property.Content.toString(), content);
	}

	//Getter method
	public String getDeviceModel() {
		return (String) this.get(Property.Model.toString());
	}

	public String getDeviceManufacturer() {
		return (String) this.get(Property.Make.toString());
	}

	public String getDeviceContent() {
		return (String) this.get(Property.Content.toString());
	}
}
