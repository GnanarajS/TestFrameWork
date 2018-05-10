package com.hp.ipg.test.framework.rsapi.resources.base;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hp.ipg.test.framework.rsapi.config.ApiVersion;
import com.hp.ipg.test.framework.rsapi.config.ResourceName;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.codec.Base64;

/**
 * Resource base
 * 
 * Base class for all resources that model json data to be serialized/deserialized
 * Also contains utility methods for access jar related resources
 * 
 */
public class ResourceBase extends HashMap<String, Object> {

	public static final String SCHEMAS_DIRECTORY = "schemas";
	public static final String TEMPLATES_DIRECTORY = "templates";
	public static final String DEFAULT_PASSWORD = "R0am@User";
	public static final String AUTHORIZATION = "Authorization";
	public static final String PASSWORD_RESET_AUTHORIZATION = "Basic am9obi5zbWl0aEBocC5jb206MTIzNDU2Nzg=";

	//TODO: Support JSON & XML
	public static String getFileExtension() {
		return ".json";
	}

	public static String getXsdFileExtension() {
		return ".xsd";
	}

	public static String getContentType() {
		return "application/json";
	}

	public static String getFormContentType() {
		return "multipart/form-data";
	}
	
	protected static boolean ResourceExists(String resourcePath) {
		URL templateUrl = ResourceBase.class.getClassLoader().getResource(resourcePath);
		return (templateUrl != null);
	}

	protected static String getResourcePath(String prefixPath, String resourceName, ApiVersion version) {
		String templatePath = null;

		if (version != null) {
			String path = Paths.get(prefixPath, version.toString(), resourceName).toString();
			if (ResourceExists(path))
				templatePath = path;
		}

		if (templatePath == null) {
			String path = Paths.get(prefixPath, ApiVersion.DEFAULT.toString(), resourceName).toString();
			if (ResourceExists(path))
				templatePath = path;
		}

		return templatePath;
	}

	public Map<String,String> splitToMap(String value) {
		value = StringUtils.substringBetween(value,"{","}");
		String[] keyValue = value.split(",");
		Map<String,String> map = new HashMap<>();

		for(String pair : keyValue) {
			String[] entry = pair.split(":");
			map.put(entry[0].trim(),entry[1].trim());
		}
		return map;
	}


	/**
	 * Method gets relative schema path for a resource first looking for version specific
	 * schema and if not found retrieving the default schema
	 * 
	 * @param resourceName
	 *            - name of resource to retreive schema for
	 * @param version
	 *            - schema version
	 * @return - returns a relative path for schema resource
	 */
	public static String getSchemaPath(ResourceName resourceName, ApiVersion version) {
		return getResourcePath(SCHEMAS_DIRECTORY, resourceName + getFileExtension(), version);
	}

	public static String getXmlSchemaPath(ResourceName resourceName, ApiVersion version) {
		return getResourcePath(SCHEMAS_DIRECTORY, resourceName + getXsdFileExtension(), version);
	}

	/**
	 * Method load a json template and deserializes it into specified class
	 * 
	 * @param resourceName
	 *            - name of resource to load template for
	 * @param version
	 *            - template version
	 * @param containerClass
	 *            - specified type to deserialize template into
	 * @return - instance of specified type
	 * @throws IOException
	 */
	public static ResourceBase loadTemplate(ResourceName resourceName, ApiVersion version, Class containerClass) throws IOException {
		InputStream templateStream = null;
		String resourceFile = resourceName + getFileExtension();

		try {
			if (version != null)
				templateStream = ResourceBase.class.getResourceAsStream("/" + Paths.get(TEMPLATES_DIRECTORY, version.toString(), resourceFile));

			if (templateStream == null)
				templateStream = ResourceBase.class.getResourceAsStream("/" + Paths.get(TEMPLATES_DIRECTORY, ApiVersion.DEFAULT.toString(), resourceFile));

			if (templateStream == null)
				throw new IOException("Invalid Resource Name: " + resourceName);

			ObjectMapper mapper = new ObjectMapper();
			return (ResourceBase) mapper.readValue(templateStream, containerClass);
		} finally {
			if (templateStream != null)
				templateStream.close();
		}
	}

	public boolean getBooleanValue(String key) {

		String value = this.get(key).toString();
		boolean booleanValue = Boolean.valueOf(value);
		return booleanValue;
	}

	public int getIntegerValue(String key) {

		String value = this.get(key).toString();
		int integerValue = Integer.valueOf(value);
		return integerValue;
	}

	public String getStringValue(String key) {

		return this.get(key).toString();
	}

	public static String getMD5Hash(String username) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		byte[] array = md.digest(username.getBytes());
		String md5Hash = new String(Hex.encodeHex(array));

		return new String(Base64.encode(md5Hash.getBytes()));
	}
}
