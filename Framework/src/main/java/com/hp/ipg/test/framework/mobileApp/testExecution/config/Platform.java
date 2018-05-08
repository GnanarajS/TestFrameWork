package com.hp.ipg.test.framework.mobileApp.testExecution.config;

public enum Platform {
    IOS("iOS"),
    ANDROID("android");

    private String name;

    Platform(String platform) {
        this.name = platform;
    }

    public String getName() {
        return name;
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
        return name;
    }
}
