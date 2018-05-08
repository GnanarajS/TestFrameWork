package com.hp.ipg.test.framework.genericLib.testExecution.reporting.jenkins;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Build {

    @JsonProperty
    private int number;
    @JsonProperty
    private URL url;
    @JsonProperty
    private long duration;
    @JsonProperty
    private List<TestResults> testResults;
    @JsonProperty
    private Date timestamp;
    @JsonProperty
    private String description;
    @JsonProperty
    private String result;
    @JsonProperty
    private Boolean building;
    @JsonProperty
    private List<Action> actions;
    @JsonProperty
    private List<JenkinsArtifact> artifacts;

    public int getNumber() {
        return number;
    }

    public String getResult() {
        return result;
    }

    public Boolean isBuilding() {
        return building;
    }

    public URL getUrl() {
        return url;
    }

    public long getDuration() {
        return duration;
    }

    public Date getDate() {
        return timestamp;
    }

    public long getDurationMilliseconds() {
        return duration;
    }

    public String getDescription() {
        return description;
    }

    public List<Action> getActions() {
        return actions;
    }

    public List<JenkinsArtifact> getArtifacts() {
        return artifacts;
    }

    public boolean hasTestResults() throws IOException {
        if (artifacts != null) {
            for (JenkinsArtifact artifact : artifacts) {
                if (artifact.getRelativePath().contains("test-output/triageReport")) {
                    return true;
                }
            }
        }
        return false;
    }

    public TestResults getResults() {
        TestResults retVal = null;

        if (testResults != null) {
            for (TestResults result : testResults) {
                if (result.getTotalCount() > 0) {
                    retVal = result;
                    break;
                }
            }
        }
        return retVal;
    }

    public List<URL> getTriageReportUrls() throws MalformedURLException {
        List<URL> triageReportUrls = new ArrayList<URL>();
        if (artifacts != null) {
            for (JenkinsArtifact artifact : artifacts) {
                if (artifact.getRelativePath().contains("test-output/triageReport")) {
                    triageReportUrls.add(new URL(url + "artifact/" + artifact.getRelativePath()));
                }
            }
        }
        return triageReportUrls;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("number", number)
                .append("url", url)
                .append("duration", duration)
                .append("results", getResults())
                .toString();
    }
}
