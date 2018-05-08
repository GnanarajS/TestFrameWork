package com.hp.ipg.test.framework.genericLib.testExecution.reporting.jenkins;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LastBuiltRevision {

    @JsonProperty
    private String SHA1;
    @JsonProperty
    private List<Branch> branch = new ArrayList<Branch>();

    public String getSHA1() {
        return SHA1;
    }

    public List<Branch> getBranch() {
        return branch;
    }
}
