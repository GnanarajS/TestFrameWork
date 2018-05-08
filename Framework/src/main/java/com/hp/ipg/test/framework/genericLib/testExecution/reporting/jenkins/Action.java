package com.hp.ipg.test.framework.genericLib.testExecution.reporting.jenkins;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Action {

    @JsonProperty
    private List<Cause> causes = new ArrayList<Cause>();
    @JsonProperty
    private LastBuiltRevision lastBuiltRevision;
    @JsonProperty
    private List<String> remoteUrls = new ArrayList<String>();
    @JsonProperty
    private String scmName;

    public List<Cause> getCauses() {
        return causes;
    }

    public LastBuiltRevision getLastBuiltRevision() {
        return lastBuiltRevision;
    }

    public List<String> getRemoteUrls() {
        return remoteUrls;
    }

    public String getScmName() {
        return scmName;
    }
}