
package com.dmi.dto.location;

import java.util.List;

import com.dmi.dto.geospatial.Coordinate;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "type",
    "coordinates"
})
public class Geometry {

    @JsonProperty("type")
    private String type;
    @JsonProperty("coordinates")
    private List<Coordinate> coordinates = null;
    @JsonProperty("reference")
    private List<Float> reference = null;
    @JsonProperty("radius")
    private Float radius;
    @JsonProperty("fence")
    private Fence fence;
    
    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("coordinates")
    public List<Coordinate> getCoordinates() {
        return coordinates;
    }

    @JsonProperty("coordinates")
    public void setCoordinates(List<Coordinate> coordinates) {
        this.coordinates = coordinates;
    }

	public List<Float> getReference()
	{
		return reference;
	}

	public void setReference(List<Float> reference)
	{
		this.reference = reference;
	}

	public Float getRadius()
	{
		return radius;
	}

	public void setRadius(Float radius)
	{
		this.radius = radius;
	}

	public Fence getFence()
	{
		return fence;
	}

	public void setFence(Fence fence)
	{
		this.fence = fence;
	}

}
