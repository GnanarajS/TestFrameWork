package com.hp.ipg.test.framework.mobileApp.testExecution.config;

public enum Device {

    iPhone6("iPhone 6"),
    iPhone8("iPhone_8_real_us"),
    AndroidEmulator("Android Emulator"),
    SamsungGalaxyS7("Samsung_Galaxy_S7_real"),
    SamsungGalaxyS6("Samsung_Galaxy_S6_real"),
    MotorolaMotoX("Motorola_Moto_X_real"),
    AndroidGoogleAPIEmulator("Android GoogleAPI Emulator");

    private String name;

    Device(String device) {
        this.name = device;
    }

    public String getName() {
        return name;
    }

    public static Device fromString(String deviceString) {
        for (Device device : Device.values()) {
            if (device.getName().equalsIgnoreCase(deviceString)) {
                return device;
            }
        }
        throw new IllegalArgumentException(deviceString + " is not a valid name for a Device.");
    }

    @Override
    public String toString() {
        return name;
    }
}
