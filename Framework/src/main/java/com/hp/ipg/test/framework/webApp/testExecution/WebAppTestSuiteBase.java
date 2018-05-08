package com.hp.ipg.test.framework.webApp.testExecution;

import com.hp.ipg.test.framework.genericLib.testExecution.TestSuiteBase;
import com.hp.ipg.test.framework.genericLib.testExecution.utils.BuildUtils;
import org.codehaus.plexus.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ISuite;

import java.io.File;
import java.io.IOException;

public class WebAppTestSuiteBase  extends TestSuiteBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebAppTestSuiteBase.class);
    public void bundleSuiteTriageFiles(ISuite suite) throws IOException {
        String browserSnapshotsName = "browserSnapshots";
        String desktopSnapshotsName = "desktopSnapshots";
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

        if (!destDir.getAbsolutePath().equals(suiteOutput.getAbsolutePath())) {
            File snapshotDir = new File(suiteOutput, browserSnapshotsName);
            if (snapshotDir.exists()) {
                FileUtils.copyDirectory(snapshotDir, new File(destDir, browserSnapshotsName));
                FileUtils.deleteDirectory(snapshotDir);
            }
            File desktopSnapshotsDir = new File(suiteOutput, desktopSnapshotsName);
            if (desktopSnapshotsDir.exists()) {
                FileUtils.copyDirectory(desktopSnapshotsDir, new File(destDir, desktopSnapshotsName));
                FileUtils.deleteDirectory(desktopSnapshotsDir);
            }
            File browserLogsDir = new File(suiteOutput, browserLogsName);
            if (browserLogsDir.exists()) {
                FileUtils.copyDirectory(browserLogsDir, new File(destDir, browserLogsName));
                FileUtils.deleteDirectory(browserLogsDir);
            }
        }
    }
}
