
package com.dmi.dto.oemmodel.metainfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@JsonInclude(Include.NON_EMPTY)
public class PIIDataSet implements Serializable{

	private static final long serialVersionUID = 1L;
	@SerializedName("locationList")
    @Expose
    private List<LocationList> locationList = new ArrayList<>();

    /**
     * 
     * @return
     *     The locationList
     */
    public List<LocationList> getLocationList() {
        return locationList;
    }

    /**
     * 
     * @param locationList
     *     The locationList
     */
    public void setLocationList(List<LocationList> locationList) {
        this.locationList = locationList;
    }


}
