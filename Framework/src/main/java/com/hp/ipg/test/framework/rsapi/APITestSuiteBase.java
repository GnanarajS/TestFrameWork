package com.hp.ipg.test.framework.rsapi;

import com.hp.ipg.test.framework.genericLib.testExecution.TestSuiteBase;
import com.hp.ipg.test.framework.genericLib.testExecution.utils.BuildUtils;
import org.codehaus.plexus.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ISuite;

import java.io.File;
import java.io.IOException;

public class APITestSuiteBase extends TestSuiteBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(APITestSuiteBase.class);

    public void bundleSuiteTriageFiles(ISuite suite) throws IOException {
        String browserLogsName = "browserLogs";
        File destDir = new File(BuildUtils.getTestOutputDirectory(), suite.getName());
        File suiteOutput = new File(suite.getOutputDirectory());

        LOGGER.info("Moving triage files from [{}] into [{}]...",
                suiteOutput.getAbsolutePath(),
                destDir.getAbsolutePath());

        if (!suiteOutput.exists()) {
            LOGGER.warn("Could not locate any triage files in \"" + suiteOutput.getAbsolutePath() + "\".");
            return;
        }

            File browserLogsDir = new File(suiteOutput, browserLogsName);
            if (browserLogsDir.exists()) {
                FileUtils.copyDirectory(browserLogsDir, new File(destDir, browserLogsName));
                FileUtils.deleteDirectory(browserLogsDir);
            }
    }
}
