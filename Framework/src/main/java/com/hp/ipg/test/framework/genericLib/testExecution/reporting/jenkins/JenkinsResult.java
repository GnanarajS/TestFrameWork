package com.hp.ipg.test.framework.genericLib.testExecution.reporting.jenkins;

import java.net.URL;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JenkinsResult {

    @JsonProperty
    private String name;
    @JsonProperty
    private String className;
    @JsonProperty
    private String description;
    @JsonProperty
    private String exception;
    @JsonProperty
    private String status;

    private URL resultUrl;

    public String getName() {
        return name;
    }

    public String getClassName() {
        return className;
    }

    public String getDescription() {
        return description;
    }

    public String getException() {
        return exception;
    }

    public Result getResult() {
        return Result.fromString(status);
    }

    void setUrl(URL url) {
        resultUrl = url;
    }

    public URL getUrl() {
        return resultUrl;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("name", name)
                .append("className", className)
                .append("description", description)
                .append("status", status).toString();
    }

    public enum Result {
        PASS,
        FAIL,
        SKIP,
        UNKNOWN;

        public static Result fromString(String stateString) {
            for (Result result : Result.values()) {
                if (result.toString().equalsIgnoreCase(stateString)) {
                    return result;
                }
            }

            return Result.UNKNOWN;
        }
    }
}
