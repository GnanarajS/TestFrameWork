package com.hp.ipg.rsapi.test.base.dataModels;

import com.hp.ipg.test.framework.rsapi.config.ApiVersion;
import com.hp.ipg.test.framework.rsapi.config.ResourceName;
import com.hp.ipg.test.framework.rsapi.resources.base.ResourceBase;

import org.testng.Assert;

import com.hp.ipg.rsapi.test.base.dataProviders.BaseDataProvider;

import javax.annotation.PostConstruct;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public abstract class ResourceTestSet {
	public final static String PREFIX_WILDCARD = "*";
	public final static String INCREMENT_WILDCARD = "+";

	enum Property {
		Count
	}

	protected Object syncRoot = new Object();
	protected HashMap<String, ResourceBase> resourceSet = null;

	//method handled by derived class to create test resources
	abstract protected HashMap<String, ResourceBase> LoadTestSet();

	protected ResourceBase[] loadResourceSetTemplate(String resourceSetTemplate, ResourceName resource, ApiVersion version) {
		return (ResourceBase[]) BaseDataProvider.loadDataFromJson(resourceSetTemplate, resource, version, ResourceBase[].class);
	}

	/**
	 * This method will override attributes in base template with the template coming from Test Data Set
	 * 
	 * @param resource
	 *            : resource from base template
	 * @param template
	 *            : template with new values
	 * @param index
	 *            : counter to add as suffix to properties
	 * @return
	 */
	protected ResourceBase applyTemplate(ResourceBase resource, HashMap<String, Object> template, int index) {
		for (Map.Entry<String, Object> entry : template.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();

			if (value.getClass() == String.class &&
					(value.toString().endsWith(PREFIX_WILDCARD) || value.toString().endsWith(INCREMENT_WILDCARD))) {
				String newVal = value.toString();
				//remove wildcards
				newVal = newVal.substring(0, newVal.length() - 1) + String.valueOf(index);
				if (resource.containsKey(key) && value.toString().endsWith(PREFIX_WILDCARD))
					newVal += resource.get(key).toString();

				resource.put(key, newVal);
			} else {
				resource.put(key, value);
			}
		}

		return resource;
	}

	/**
	 * Removing count property which is coming through Test Data set
	 * 
	 * @param resource
	 * @return
	 */
	protected int processResourceCount(ResourceBase resource) {
		int count = 1;
		if (resource.containsKey(Property.Count.toString())) {
			count = (int) resource.get(Property.Count.toString());
			resource.remove(Property.Count.toString());
		}

		return count;
	}

	@PostConstruct
	public void Initalize() {
		if (resourceSet == null) {
			synchronized (syncRoot) {
				if (resourceSet == null) {
					resourceSet = LoadTestSet();
					Assert.assertNotNull(resourceSet, "Test Set Load Is Null!");
				}
			}
		}
	}

	public int size() {
		return resourceSet.size();
	}

	public boolean containsKey(String key) {
		return resourceSet.containsKey(key);
	}

	public ResourceBase get(String key) {
		return resourceSet.get(key);
	}

	public Set<String> keySet() {
		return resourceSet.keySet();
	}

	/**
	 * Get the random resource from the resource set
	 * 
	 */
	public ResourceBase getRandomTestResource() {
		return (ResourceBase) (resourceSet.values().toArray()[(new Random()).nextInt(resourceSet.size())]);
	}
}
