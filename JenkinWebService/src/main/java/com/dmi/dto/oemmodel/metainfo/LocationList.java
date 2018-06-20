
package com.dmi.dto.oemmodel.metainfo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@JsonInclude(Include.NON_EMPTY)
public class LocationList implements Serializable{

	private static final long serialVersionUID = 1L;
	@SerializedName("altitude")
    @Expose
    private String altitude;
    @SerializedName("direction")
    @Expose
    private String direction;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("speed")
    @Expose
    private String speed;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;
    
    public LocationList()
	{

	}

	public LocationList(final String altitude,final String direction,final String latitude,final String longitude,final String speed,final String timestamp)
	{
		this.altitude = altitude;
		this.direction = direction;
		this.latitude = latitude;
		this.longitude = longitude;
		this.speed = speed;
		this.timestamp = timestamp;
	}

    /**
     * 
     * @return
     *     The altitude
     */
    public String getAltitude() {
        return altitude;
    }

    /**
     * 
     * @param altitude
     *     The altitude
     */
    public void setAltitude(String altitude) {
        this.altitude = altitude;
    }

    /**
     * 
     * @return
     *     The direction
     */
    public String getDirection() {
        return direction;
    }

    /**
     * 
     * @param direction
     *     The direction
     */
    public void setDirection(String direction) {
        this.direction = direction;
    }

    /**
     * 
     * @return
     *     The latitude
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * 
     * @param latitude
     *     The latitude
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    /**
     * 
     * @return
     *     The longitude
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     * 
     * @param longitude
     *     The longitude
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    /**
     * 
     * @return
     *     The speed
     */
    public String getSpeed() {
        return speed;
    }

    /**
     * 
     * @param speed
     *     The speed
     */
    public void setSpeed(String speed) {
        this.speed = speed;
    }

    /**
     * 
     * @return
     *     The timestamp
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * 
     * @param timestamp
     *     The timestamp
     */
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    /*@Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
*/
    public static class LocationListBuilder
    {

        private String altitude;
        private String direction;
        private String latitude;
        private String longitude;
        private String speed;
        private String timestamp;
        
        public LocationListBuilder()
        {
        	
        }
        
        public LocationListBuilder withAltitude(String altitude)
        {
        this.altitude = altitude;
        return this;
        }
        
        public LocationListBuilder withDirection(String direction)
        {
        this.direction = direction;
        return this;
        }
        
        public LocationListBuilder withLatitude(String latitude)
        {
        this.latitude = latitude;
        return this;
        }
        
        public LocationListBuilder withLongitude(String longitude)
        {
        this.longitude = longitude;
        return this;
        }
        
        public LocationListBuilder withSpeed(String speed)
        {
        this.speed = speed;
        return this;
        }
        
        
        public LocationListBuilder withTimestamp(String timestamp)
        {
        this.timestamp = timestamp;
        return this;
        }
        
        
        public LocationList buildLocationList()
        {
           return new LocationList(altitude,direction,latitude,longitude,speed,timestamp);
        }
    	
    }
}
