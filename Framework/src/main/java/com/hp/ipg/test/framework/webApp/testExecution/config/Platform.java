package com.hp.ipg.test.framework.webApp.testExecution.config;

public enum Platform {
    MAC_OSX10_9("OS X 10.9"),
    WINDOWS("windows");

    private String platformName;

    Platform(String platform) {
        this.platformName = platform;
    }

    public String getName() {
        return platformName;
    }

    public static Platform fromString(String platformString) {
        for (Platform platform : Platform.values()) {
            if (platform.getName().equalsIgnoreCase(platformString)) {
                return platform;
            }
        }
        throw new IllegalArgumentException(platformString + " is not a valid name for a Platform.");
    }

    @Override
    public String toString() {
        return platformName;
    }
}
