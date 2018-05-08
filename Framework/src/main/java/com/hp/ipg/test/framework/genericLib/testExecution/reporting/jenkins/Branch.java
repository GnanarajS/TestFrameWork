package com.hp.ipg.test.framework.genericLib.testExecution.reporting.jenkins;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Branch {

    @JsonProperty
    private String SHA1;
    @JsonProperty
    private String name;

    public String getSHA1() {
        return SHA1;
    }

    public String getName() {
        return name;
    }
}
