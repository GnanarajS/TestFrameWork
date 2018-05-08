package com.hp.ipg.test.framework.genericLib.testExecution.utils;

import java.net.MalformedURLException;
import java.net.URL;

public final class JenkinsUtils {

    private static final String BUILD_URL = "BUILD_URL";
    private static final String BUILD_NUMBER = "BUILD_NUMBER";
    private static final String JOB_NAME = "JOB_NAME";
    private static final String JOB_URL = "JOB_URL";

    private JenkinsUtils() {
    }

    /*
     * Obtains the jenkins job url of the currently executing job (build number included).
     * @returns valid URL if applicable or null if not running in Jenkins
     */
    public static URL getJenkinsBuildUrl() throws MalformedURLException {
        URL url = null;
        String buildURL = System.getenv(BUILD_URL);

        if (buildURL != null) {
            url = new URL(buildURL);
        }
        return url;
    }

    /*
     * Obtains the build number of the currently executing Jenkins build
     * @returns valid build number (as String) or null if not applicable.
     */
    public static String getJenkinsBuildNumber() {
        return System.getenv(BUILD_NUMBER);
    }

    /*
     * Obtains the current jenkins job base url without the build number included.
     * @returns valid URL if applicable or null if not running in Jenkins
     */
    public static URL getJenkinsJobUrl() throws MalformedURLException {
        URL url = null;
        String buildURL = System.getenv(JOB_URL);

        if (buildURL != null) {
            url = new URL(buildURL);
        }
        return url;
    }

    /*
     * Obtains the currently executing Jenkins job name.
     */
    public static String getJenkinsJobName() {
        return System.getenv(JOB_NAME);
    }

    public static boolean isRunningOnJenkins() {
        return System.getenv(JOB_NAME) != null;
    }
}
