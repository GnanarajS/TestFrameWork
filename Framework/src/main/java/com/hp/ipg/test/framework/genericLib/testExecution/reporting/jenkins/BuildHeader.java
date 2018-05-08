package com.hp.ipg.test.framework.genericLib.testExecution.reporting.jenkins;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.URL;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BuildHeader {

    @JsonProperty
    private int number;
    @JsonProperty
    private URL url;

    public int getNumber() {
        return number;
    }

    public URL getUrl() {
        return url;
    }
}

