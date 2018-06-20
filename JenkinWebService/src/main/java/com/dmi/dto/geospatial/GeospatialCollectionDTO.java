
package com.dmi.dto.geospatial;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "type",
    "geography"
})
public class GeospatialCollectionDTO {

    @JsonProperty("type")
    private String type;
    
    @JsonProperty("geography")
    private Geography geography;

    
    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("geography")
    public Geography getGeography() {
        return geography;
    }

    @JsonProperty("geography")
    public void setGeography(Geography geography) {
        this.geography = geography;
    }

}
