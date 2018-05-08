package com.hp.ipg.test.framework.genericLib.testExecution.reporting.jenkins;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Job {

    @JsonProperty
    private String name;
    @JsonProperty
    private URL url;
    @JsonProperty
    private String color;
    @JsonProperty
    private List<JobHeader> activeConfigurations;
    @JsonProperty
    private List<BuildHeader> builds;
    @JsonProperty
    private String description;
    @JsonProperty
    private String displayName;
    @JsonProperty
    private BuildHeader lastCompletedBuild;
    @JsonProperty
    private List<JobHeader> downstreamProjects;
    @JsonProperty
    private List<JobHeader> upstreamProjects;
    @JsonProperty
    private int nextBuildNumber;
    @JsonProperty
    private boolean inQueue;
    @JsonProperty
    private QueueItem queueItem;

    public String getName() {
        return name;
    }

    public URL getUrl() {
        return url;
    }

    public String getColor() {
        return color;
    }

    public String getDescription() {
        return description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isMultiConfigurationJob() {
        return activeConfigurations != null;
    }

    public List<JobHeader> getConfigurations() throws IOException {
        if (activeConfigurations == null) {
            activeConfigurations = new ArrayList<JobHeader>();
        }
        return activeConfigurations;
    }

    public List<JobHeader> getDownstreamProjects() {
        if (downstreamProjects == null) {
            downstreamProjects = new ArrayList<JobHeader>();
        }
        return downstreamProjects;
    }

    public List<JobHeader> getUpstreamProjects() {
        if (upstreamProjects == null) {
            upstreamProjects = new ArrayList<JobHeader>();
        }
        return upstreamProjects;
    }

    public int getNextBuildNumber() {
        return nextBuildNumber;
    }

    public boolean isInQueue() {
        return inQueue;
    }

    public QueueItem getQueueItem() {
        return queueItem;
    }

    public List<BuildHeader> getBuilds() throws IOException {
        if (builds == null) {
            builds = new ArrayList<BuildHeader>();
        }
        return builds;
    }

    public String getJobNameFromUrl() {
        String jobName = "unknown";
        String[] path = url.getPath().split("/");
        if (path.length >= 3 && path[1].equals("job")) {
            jobName = path[2];
        }
        return jobName;
    }

    public String getViewNameFromUrl() {
        String viewName = "unknown";
        String[] path = url.getPath().split("/");
        int viewIndex = 0;
        for (String segment : path) {
            if (segment.equals("view")) {
                viewIndex++;
            }
        }
        if (viewIndex > 0) {
            viewName = path[viewIndex + 1];
        }
        return viewName;
    }

    public BuildHeader getLastCompletedBuild() throws IOException {
        return lastCompletedBuild;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("name", name)
                .append("url", url)
                .append("color", color)
                .toString();
    }
}

