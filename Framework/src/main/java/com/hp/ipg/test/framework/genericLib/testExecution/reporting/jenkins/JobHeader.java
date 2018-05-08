package com.hp.ipg.test.framework.genericLib.testExecution.reporting.jenkins;

import java.net.URL;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JobHeader {

    @JsonProperty
    private String name;
    @JsonProperty
    private URL url;
    @JsonProperty
    private String color;

    public String getName() {
        return name;
    }

    public URL getUrl() {
        return url;
    }

    public String getColor() {
        return color;
    }
}
