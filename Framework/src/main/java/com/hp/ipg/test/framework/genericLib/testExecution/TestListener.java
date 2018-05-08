package com.hp.ipg.test.framework.genericLib.testExecution;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.slf4j.Logger;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

public class TestListener extends TestListenerAdapter {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(TestListener.class);

    private int numTestsFinished = 0;
    private int failures;
    private int successes;
    private int skipped;

    @Override
    public void onTestFailure(ITestResult tr) {

        if (tr.getStatus() == ITestResult.FAILURE) {
            // Test was not successful.
            Throwable cause = tr.getThrowable();
            if (cause != null) {
                LOGGER.error("Test failed with:", cause);
            }
        }

        failures++;
        numTestsFinished++;
        logOutcome(tr);
    }

    @Override
    public void onTestSkipped(ITestResult tr) {
        skipped++;
        numTestsFinished++;
        logOutcome(tr);
    }

    @Override
    public void onTestSuccess(ITestResult tr) {
        successes++;
        numTestsFinished++;
        logOutcome(tr);
    }

    private synchronized void logOutcome(ITestResult result) {
        String status;
        switch (result.getStatus()) {
            case ITestResult.FAILURE:
                status = "Failed";
                break;
            case ITestResult.SUCCESS:
                status = "Passed";
                break;
            case ITestResult.SKIP:
                status = "Skipped";
                break;
            default:
                status = "Inconclusive";
                break;
        }

        double percentComplete = (double) numTestsFinished / (double) TestSuiteBase.getTotalTestCount() * 100.0;

        String progressString = percentComplete < 101.0 ?
                String.format("Total progress %d%% (%d/%d) : %d Success / %d Failure / %d Skipped",
                        (int) percentComplete,
                        numTestsFinished,
                        TestSuiteBase.getTotalTestCount(),
                        successes,
                        failures,
                        skipped) :
                String.format("Progress: %d Success / %d Failure / %d Skipped",
                        successes,
                        failures,
                        skipped);

        LOGGER.info(String.format("\n%s\n%s [%s]\nTest duration :%s\nTotal duration:%s\n%s\n%s",
                "+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++",
                result.getTestClass().getName() + "." + result.getMethod().getMethodName(),
                status,
                DurationFormatUtils.formatDuration(result.getEndMillis() - result.getStartMillis(), "HH:mm:ss"),
                DurationFormatUtils.formatDuration(TestSuiteBase.getElapsedMilliseconds(), "HH:mm:ss"),
                progressString,
                "+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++"));
    }

}
