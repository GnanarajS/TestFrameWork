package com.hp.ipg.test.framework.genericLib.testExecution.reporting.jenkins;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JenkinsArtifact {

    @JsonProperty
    private String displayPath;
    @JsonProperty
    private String filename;
    @JsonProperty
    private String relativePath;

    public String getDisplayPath() {
        return displayPath;
    }

    public String getFilename() {
        return filename;
    }

    public String getRelativePath() {
        return relativePath;
    }
}
