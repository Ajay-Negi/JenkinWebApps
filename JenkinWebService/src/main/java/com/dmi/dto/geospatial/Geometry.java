
package com.dmi.dto.geospatial;

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
    "radius",
    "coordinates"
})
public class Geometry {

    @JsonProperty("radius")
    private Float radius;
    @JsonProperty("coordinates")
    private List<Coordinate> coordinates = null;

    @JsonProperty("radius")
    public Float getRadius() {
        return radius;
    }

    @JsonProperty("radius")
    public void setRadius(Float radius) {
        this.radius = radius;
    }

    @JsonProperty("coordinates")
    public List<Coordinate> getCoordinates() {
        return coordinates;
    }

    @JsonProperty("coordinates")
    public void setCoordinates(List<Coordinate> coordinates) {
        this.coordinates = coordinates;
    }

}
