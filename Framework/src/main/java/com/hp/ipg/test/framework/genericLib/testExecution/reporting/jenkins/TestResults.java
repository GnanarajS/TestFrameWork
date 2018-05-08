package com.hp.ipg.test.framework.genericLib.testExecution.reporting.jenkins;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TestResults {

    @JsonProperty
    private int failCount;
    @JsonProperty
    private int skipCount;
    @JsonProperty
    private int totalCount;
    private int passCount;

    public TestResults() {
    }

    public TestResults(int fails, int passes, int skips)
    {
        this.totalCount = fails + passes + skips;
        this.failCount = fails;
        this.passCount = passes;
        this.skipCount = skips;
    }

    public int getFailCount() {
        return failCount;
    }

    public int getSkipCount() {
        return skipCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public int getPassCount() {
        return totalCount - failCount - skipCount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("failCount", failCount)
                .append("skipCount", skipCount)
                .append("passCount", getPassCount())
                .append("totalCount", totalCount)
                .toString();
    }
}
