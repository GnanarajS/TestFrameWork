package com.hp.ipg.test.framework.genericLib.testExecution.reporting.jenkins;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class View {

    @JsonProperty
    private String name;
    @JsonProperty
    private URL url;
    @JsonProperty
    private List<JobHeader> jobs;

    public String getName() {
        return name;
    }

    public URL getUrl() {
        return url;
    }

    public List<JobHeader> getJobs() throws IOException {
        if (jobs == null) {
            jobs = new ArrayList<JobHeader>();
        }
        return jobs;
    }
}

