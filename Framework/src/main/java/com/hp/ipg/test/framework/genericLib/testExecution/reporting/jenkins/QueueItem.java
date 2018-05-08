package com.hp.ipg.test.framework.genericLib.testExecution.reporting.jenkins;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.annotation.Generated;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "blocked",
        "buildable",
        "id",
        "inQueueSince",
        "params",
        "stuck",
        "url",
        "why",
        "buildableStartMilliseconds"
})
public class QueueItem {

    @JsonProperty("blocked")
    private Boolean blocked;
    @JsonProperty("buildable")
    private Boolean buildable;
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("inQueueSince")
    private Long inQueueSince;
    @JsonProperty("params")
    private String params;
    @JsonProperty("stuck")
    private Boolean stuck;
    @JsonProperty("url")
    private String url;
    @JsonProperty("why")
    private String why;
    @JsonProperty("buildableStartMilliseconds")
    private Long buildableStartMilliseconds;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("blocked")
    public Boolean getBlocked() {
        return blocked;
    }

    @JsonProperty("buildable")
    public Boolean getBuildable() {
        return buildable;
    }

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("inQueueSince")
    public Long getInQueueSince() {
        return inQueueSince;
    }

    @JsonProperty("params")
    public String getParams() {
        return params;
    }

    @JsonProperty("stuck")
    public Boolean getStuck() {
        return stuck;
    }

    @JsonProperty("url")
    public String getUrl() {
        return url;
    }

    @JsonProperty("why")
    public String getWhy() {
        return why;
    }

    @JsonProperty("buildableStartMilliseconds")
    public Long getBuildableStartMilliseconds() {
        return buildableStartMilliseconds;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(blocked).append(buildable).append(id).append(inQueueSince).append(params).append(stuck).append(url).append(why).append(buildableStartMilliseconds).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof QueueItem) == false) {
            return false;
        }
        QueueItem rhs = ((QueueItem) other);
        return new EqualsBuilder().append(blocked, rhs.blocked).append(buildable, rhs.buildable).append(id, rhs.id).append(inQueueSince, rhs.inQueueSince).append(params, rhs.params).append(stuck, rhs.stuck).append(url, rhs.url).append(why, rhs.why).append(buildableStartMilliseconds, rhs.buildableStartMilliseconds).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
