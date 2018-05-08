package com.hp.ipg.test.framework.genericLib.testExecution.utils;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.slf4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class BuildUtils {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(BuildUtils.class);

    private BuildUtils() {
    }

    /*
     * Retrieves the fully qualified path to the repo where the current test instance is executing.
     */
    public static Path getRepoPath() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);
        CommandLine commandline = CommandLine.parse("git rev-parse --show-toplevel");

        DefaultExecutor exec = new DefaultExecutor();
        exec.setStreamHandler(streamHandler);
        exec.execute(commandline);

        return new File(outputStream.toString().trim()).toPath();
    }

    /*
     * Retrieves the current build tag from git.
     */
    public static String getBuildTag() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);
        CommandLine commandline = CommandLine.parse("git describe --tags --always");

        DefaultExecutor exec = new DefaultExecutor();
        exec.setStreamHandler(streamHandler);
        exec.execute(commandline);

        return outputStream.toString().trim();
    }

    /*
    * Retrieves the current build tag from a remote git repo.
    */
    public static int getBuildTagFromRepo(String repo, String filter) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);
        CommandLine commandline = CommandLine.parse("git ls-remote --tags " + repo + " " + filter);

        DefaultExecutor exec = new DefaultExecutor();
        exec.setStreamHandler(streamHandler);
        exec.execute(commandline);

        String output = outputStream.toString().trim().replace("refs/tags/", "");
        List<Integer> labels = new ArrayList<>();
        for (String outputLine : output.split("\n")) {
            String[] tagSegments = outputLine.split("\t");
            labels.add(Integer.parseInt(tagSegments[tagSegments.length - 1].replace("SUCCESS-", "")));
        }
        Collections.sort(labels);
        return labels.get(labels.size() - 1);
    }

    /*
     * Returns property if !null, then environment variable if !null, otherwise
     * will return defaultValue.
     */
    public static String getPropertyOrEnvVar(String key, String defaultValue) {
        String response = System.getProperty(key);
        if (response == null) {
            response = System.getenv(key);
        }
        if (response == null) {
            response = defaultValue;
        }
        return response;
    }

    public static File getTestOutputDirectory() {
        Path curPath = Paths.get("");
        File curDir = new File(curPath.toAbsolutePath().toString());

        for (File child : curDir.listFiles()) {
            if (("MobileApp".equals(child.getName())) || ("ApiTests".equals(child.getName()))) {
                curDir = child;
                break;
            }
        }

        // up one parent
        curDir = curDir.getParentFile();

        curDir = new File(curDir, "test-output");
        if (!curDir.exists()) {
            curDir.mkdir();
        }

        return curDir;
    }
}

