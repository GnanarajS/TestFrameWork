package com.hp.ipg.rsapi.test.base.dataProviders;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.ArrayList;
import com.fasterxml.jackson.core.JsonParser;
import com.hp.ipg.rsapi.test.base.config.RsApiBaseTestConfiguration;
import com.hp.ipg.rsapi.test.base.dataModels.DataProviderBaseModel;
import org.slf4j.Logger;
import org.testng.Assert;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hp.ipg.test.framework.rsapi.config.ApiVersion;
import com.hp.ipg.test.framework.rsapi.config.ResourceName;

/**
 * This class provides methods to load the json file to Java object.
 */
public class BaseDataProvider {

	public final static String VERSION = "default";
	public final static Integer TEST_JSON_DATA_BASEINDEX = 0;
	public final static String JSON_EXT = ".json";
	public final static String TXT_EXT = ".txt";
	public final static String TEST_SOURCE_DIRECTORY = "testSource";
	protected static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(BaseDataProvider.class);

	/**
	 * This method loads the JSON file and convert it into Java object
	 * 
	 * @param fileToLoad
	 *            : Name of JSON file to load
	 * @param resource
	 *            : resource endpoint to test(Foldername)
	 * @param version
	 *            : api version to test(Foldername)
	 * @param deserilizedClass
	 *            : class for dataProvider Model to deserialize test data
	 * @return : 2D array of objects, 1st index with total number of dataset and 2nd index with number of
	 *         parameters required by test method
	 */
	public static Object loadDataFromJson(String fileToLoad, ResourceName resource, ApiVersion version, Class deserilizedClass) {
		String pathToVersionFile = getFilePath(version, resource, fileToLoad);
		Assert.assertTrue(pathToVersionFile != null, "JSON data provider file " + fileToLoad + " not found for version " + version + "for resource" + resource);

		Object result;
		try {
			result = BaseDataProvider.convertJsonToObject(pathToVersionFile, deserilizedClass);
		} catch (IOException e) {
			throw new RuntimeException("Data Load Failed!!", e);
		}

		//Check to see if this is a Data Provider deserialization to filter disabled tests
		if (!result.getClass().isArray() || !result.getClass().getComponentType().isArray()) {
			return result;
		}

		//check and see if test set is specified in skip config
		ArrayList<String> skipConfig;
		String testSetName = version + RsApiBaseTestConfiguration.TEST_PATH_SEPARATOR + fileToLoad;
		if (RsApiBaseTestConfiguration.SKIP_TEST_CONFIG.containsKey(testSetName)) {
			skipConfig = RsApiBaseTestConfiguration.SKIP_TEST_CONFIG.get(testSetName);
		} else {
			skipConfig = new ArrayList<>();
		}

		//check to see if full test set should be skipped
		if (skipConfig.contains(RsApiBaseTestConfiguration.TEST_ANY)) {
			LOGGER.warn("Skipping Tests for: " + pathToVersionFile);
			return (Object[][]) Array.newInstance(result.getClass().getComponentType(), 0);
		}

		//determine if type derives from base data provider model class
		Class arrayType = result.getClass().getComponentType().getComponentType();
		while (arrayType != Object.class) {
			if (arrayType == DataProviderBaseModel.class) {
				ArrayList<Object[]> enabledList = new ArrayList<>();
				DataProviderBaseModel[][] dpbmList = (DataProviderBaseModel[][]) result;
				int testIndex = 1;

				//track enabled/skipped tests
				for (DataProviderBaseModel[] dpbm : dpbmList) {
					String id = dpbm[0].getId();
					if (id == null)
						id = String.valueOf(testIndex);

					if (dpbm[0].getEnabled() && !skipConfig.contains(id)) {
						enabledList.add(dpbm);
					} else {
						ObjectMapper mapper = new ObjectMapper();
						try {
							LOGGER.warn("Skipping Disabled Test: " + pathToVersionFile + "\r\n" + mapper.writeValueAsString(dpbm[0]));
						} catch (JsonProcessingException e) {
							LOGGER.warn("Skipping Disabled Test: " + id);
						}
					}

					testIndex++;
				}

				//create new updated array to return
				Object[][] dpArray = (Object[][]) Array.newInstance(result.getClass().getComponentType(), enabledList.size());
				for (int i = 0; i < enabledList.size(); i++)
					dpArray[i] = enabledList.get(i);

				return dpArray;
			}

			arrayType = arrayType.getSuperclass();
		}

		return null;
	}

	/**
	 * This method checks if versioned file is available or not,
	 * If not, file is loaded from the base directory.
	 * 
	 * @param version
	 *            : api version to test(Foldername)
	 * @param resource
	 *            : resource endpoint to test(Foldername)
	 * @param fileToLoad
	 *            : Name of JSON file to load
	 * @return absolute path of the json file.
	 */
	public static String getFilePath(ApiVersion version, ResourceName resource, String fileToLoad) {
		File filePath = getAbsolutePath(version, resource, fileToLoad);
		if (filePath == null) {
			filePath = getAbsolutePath(ApiVersion.DEFAULT, resource, fileToLoad);
		}
		return filePath.getPath();
	}

	/**
	 * This method converts JSON data on the path to 2D Object array
	 * This method should only be used by data providers which have data in required format.
	 * 
	 * @param path
	 *            : absolute path to JSON file
	 * @return : 2D Object array
	 * @throws IOException
	 */
	private static Object convertJsonToObject(String path, Class deserializedClass) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
		return mapper.readValue(new File(path), deserializedClass);
	}

	/**
	 * This method provides the file path to the test resource folder
	 * 
	 * @param version
	 *            : api version to test(Foldername)
	 * @param resource
	 *            : resource endpoint to test(Foldername)
	 * @param file
	 *            : Name of JSON file to load
	 * @return
	 */
	private static File getAbsolutePath(ApiVersion version, ResourceName resource, String file) {
		String resourceFile = file + JSON_EXT;
		String resourceName = resource.toString();
		File filePath = null;
		//ensure directory case i.e. first char is lowercase
		if (Character.isUpperCase(resourceName.charAt(0)))
			resourceName = Character.toLowerCase(resourceName.charAt(0)) + resourceName.substring(1);
		try {
			filePath =  new File(BaseDataProvider.class.getClassLoader().getResource(Paths.get(TEST_SOURCE_DIRECTORY, version.toString(), resourceName, resourceFile).toString()).toURI());
		} catch (URISyntaxException e) {
			LOGGER.info("Error in forming the file path. " + e.getMessage());
		}
		return filePath;
	}
}
