
package com.dmi.dto.location;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "deviceId",
    "deviceTypeId",
    "deviceAlias",
    "place",
    "time"
})
public class Properties {

    @JsonProperty("deviceId")
    private String deviceId;
    @JsonProperty("deviceTypeId")
    private Long deviceTypeId;
    @JsonProperty("deviceAlias")
    private String deviceAlias;
    @JsonProperty("place")
    private String place;
    @JsonProperty("time")
    private Long time;

    @JsonProperty("deviceId")
    public String getDeviceId() {
        return deviceId;
    }

    @JsonProperty("deviceId")
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @JsonProperty("deviceTypeId")
    public Long getDeviceTypeId() {
        return deviceTypeId;
    }

    @JsonProperty("deviceTypeId")
    public void setDeviceTypeId(Long deviceTypeId) {
        this.deviceTypeId = deviceTypeId;
    }

    @JsonProperty("deviceAlias")
    public String getDeviceAlias() {
        return deviceAlias;
    }

    @JsonProperty("deviceAlias")
    public void setDeviceAlias(String deviceAlias) {
        this.deviceAlias = deviceAlias;
    }

    @JsonProperty("place")
    public String getPlace() {
        return place;
    }

    @JsonProperty("place")
    public void setPlace(String place) {
        this.place = place;
    }

    @JsonProperty("time")
    public Long getTime() {
        return time;
    }

    @JsonProperty("time")
    public void setTime(Long time) {
        this.time = time;
    }

}
