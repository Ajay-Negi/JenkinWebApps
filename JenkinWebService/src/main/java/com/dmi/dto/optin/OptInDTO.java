
package com.dmi.dto.optin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "servicesList",
    "deviceTypeId",
    "oemId",
    "userId",
    "deviceId"
})
public class OptInDTO {

    @JsonProperty("servicesList")
    private List<Services> servicesList = null;
    @JsonProperty("deviceTypeId")
    private Long deviceTypeId;
    @JsonProperty("oemId")
    private Long oemId;
    @JsonProperty("userId")
    private Long userId;
    @JsonProperty("deviceId")
    private String deviceId;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("servicesList")
    public List<Services> getServicesList() {
        return servicesList;
    }

    @JsonProperty("servicesList")
    public void setServicesList(List<Services> servicesList) {
        this.servicesList = servicesList;
    }

    @JsonProperty("deviceTypeId")
    public Long getDeviceTypeId() {
        return deviceTypeId;
    }

    @JsonProperty("deviceTypeId")
    public void setDeviceTypeId(Long deviceTypeId) {
        this.deviceTypeId = deviceTypeId;
    }

    @JsonProperty("oemId")
    public Long getOemId() {
        return oemId;
    }

    @JsonProperty("oemId")
    public void setOemId(Long oemId) {
        this.oemId = oemId;
    }

    @JsonProperty("userId")
    public Long getUserId() {
        return userId;
    }

    @JsonProperty("userId")
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @JsonProperty("deviceId")
    public String getDeviceId() {
        return deviceId;
    }

    @JsonProperty("deviceId")
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
