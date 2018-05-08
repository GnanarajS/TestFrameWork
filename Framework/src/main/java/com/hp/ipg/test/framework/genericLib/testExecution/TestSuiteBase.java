package com.hp.ipg.test.framework.genericLib.testExecution;

import com.hp.ipg.test.framework.genericLib.testExecution.reporting.LogSplitter;
import com.hp.ipg.test.framework.genericLib.testExecution.utils.BuildUtils;
import org.apache.logging.log4j.ThreadContext;
import org.slf4j.Logger;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestNGMethod;

import java.io.File;
import java.io.IOException;

public abstract class TestSuiteBase implements ISuiteListener {
    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(TestSuiteBase.class);

    private static final TestProfile DEFAULT_PROFILE = TestProfile.QA;

    private static int totalTestCount;
    private static long suiteStartTimeInMilliseconds;

    public static synchronized int getTotalTestCount() {
        return totalTestCount;
    }

    public static long getElapsedMilliseconds() {
        return System.currentTimeMillis() - suiteStartTimeInMilliseconds;
    }

    public static String restAssuredLogFile = "restassuredlog.txt";

    @Override
    public void onStart(ISuite suite) {
        ThreadContext.push("TestSuiteInit");
        suiteStartTimeInMilliseconds = System.currentTimeMillis();
        setupProfile();
        totalTestCount = getTotalTestInvocationCount(suite);
        ThreadContext.pop();
    }

    @Override
    public void onFinish(ISuite suite) {
        File testOutputDir = BuildUtils.getTestOutputDirectory();
        File testLog = new File(testOutputDir, "test.log");
        File logsDir = new File(testOutputDir, "logs");
        File restAssuredLog = new File(testOutputDir,restAssuredLogFile);

        try {
            LogSplitter.splitLog(testLog, logsDir);
        } catch (IOException e) {
            LOGGER.error("Error encountered when splitting test log.", e);
        } finally {
            try {
                bundleSuiteTriageFiles(suite);
            } catch (IOException e) {
                LOGGER.error("Error encountered while bundling suite snapshots.", e);
            }
        }
    }

    public abstract void bundleSuiteTriageFiles(ISuite suite) throws IOException;

    private int getTotalTestInvocationCount(ISuite suite) {
        int total = 0;

        for (ITestNGMethod method : suite.getAllMethods()) {
            total += method.getInvocationCount();
        }

        return total;
    }

    private void setupProfile() {
        String profile = System.getProperty("spring.profiles.active");
        if (profile == null || profile.isEmpty()) {
            System.setProperty("spring.profiles.active", DEFAULT_PROFILE.getValue());
        }
    }

}
