package com.hp.ipg.test.framework.genericLib.testExecution.reporting;

import org.slf4j.Logger;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

public final class LogSplitter {
    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(LogSplitter.class);
    private static final String NDC_REGEX_PORTION = "\\[-*\\d+:\\w+\\]";
    private static final String LOG_ENTRY_REGEX = "(INFO|WARN|DEBUG|ERROR).*\\[-*\\d+:\\w+\\].*";

    /**
     * Private to prevent class instantiation
     */
    private LogSplitter() {

    }

    /**
     * Separates the main log file into individual log files named after the failing tests.
     *
     * @param logFile
     *            The file containing all the test logging (i.e. test.log)
     * @param destinationDirectory
     *            The directory to put all the individual test logs in
     */
    public static final void splitLog(File logFile, File destinationDirectory) throws IOException {
        if (!logFile.exists()) {
            throw new FileNotFoundException("The logFile \"" + logFile.getAbsolutePath() + "\" does not exist.");
        }

        //Create the output directory once.
        if (!destinationDirectory.exists()) {
            destinationDirectory.mkdir();
        }

        LOGGER.info("Splitting test log \"{}\" and saving logs to \"{}\"...",
                logFile.getAbsolutePath(),
                destinationDirectory.getAbsolutePath());

        long startLogSplitTime = System.currentTimeMillis();
        Map<String, TestEntry> entries = parseTestEntries(logFile);

        Iterator<Entry<String, TestEntry>> it = entries.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, TestEntry> kvp = it.next();
            if (!kvp.getValue().getTestPassed()) {
                kvp.getValue().saveLog(destinationDirectory);
            }
        }

        LOGGER.info("Log splitting took " + (System.currentTimeMillis() - startLogSplitTime) + " ms");
    }

    private static Map<String, TestEntry> parseTestEntries(File logFile) throws NumberFormatException, IOException {
        HashMap<String, TestEntry> entries = new HashMap<String, TestEntry>();

        BufferedReader bufferedReader = new BufferedReader(new FileReader(logFile));
        String line = null;
        String curThreadId = null;

        while ((line = bufferedReader.readLine()) != null) {
            String[] splitLineOnSpaces = line.split(" ");
            if (line.matches(LOG_ENTRY_REGEX)) {
                curThreadId = splitLineOnSpaces[2].split(":")[0].replace("[", "");
            } else if (line.startsWith("Starting")) {
                String fullName = splitLineOnSpaces[1];
                String testName = fullName.substring(fullName.lastIndexOf('.') + 1);
                TestEntry entry = new TestEntry(testName, fullName, curThreadId);
                entries.put(curThreadId, entry);
            } else if (line.endsWith("[Failed]")) {
                if (splitLineOnSpaces.length == 2) {
                    entries.get(curThreadId).setTestPassed(false);
                }
            }

            if (entries.containsKey(curThreadId)) {
                entries.get(curThreadId).addLogEntry(line.replaceFirst(NDC_REGEX_PORTION, ""));
            }
        }

        bufferedReader.close();
        return entries;
    }

    /**
     * Represents a single test's log entries
     */
    static class TestEntry {
        private String testMethodName;
        private String fullTestName;
        private String threadId;
        private boolean testPassed;
        private Collection<String> logEntries;

        TestEntry(String testMethodName, String fullTestName, String threadId) {
            this.testMethodName = testMethodName;
            this.fullTestName = fullTestName;
            this.threadId = threadId;
            testPassed = true;
            logEntries = new ArrayList<String>();
        }

        public String getTestMethodName() {
            return testMethodName;
        }

        public String getFullTestName() {
            return fullTestName;
        }

        public String getThreadId() {
            return threadId;
        }

        public boolean getTestPassed() {
            return testPassed;
        }

        public void setTestPassed(boolean testPassed) {
            this.testPassed = testPassed;
        }

        public void addLogEntry(String logEntry) {
            logEntries.add(logEntry);
        }

        public void saveLog(File outputDirectory) throws IOException {

            File logFile = new File(outputDirectory, this.fullTestName + "_" + threadId + ".log");
            BufferedWriter writer = new BufferedWriter(new FileWriter(logFile));
            for (String line : logEntries) {
                writer.write(line + "\r\n");
            }
            writer.close();
        }
    }
}
