
package com.dmi.dto.oemmodel.metainfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@JsonInclude(Include.NON_EMPTY)
public class ConnectivityUnit implements Serializable{
	
	


	private static final long serialVersionUID = 1L;
	@SerializedName("attributeList")
    @Expose
    private List<AttributeList> attributeList = new ArrayList<AttributeList>();
    @SerializedName("capabilityList")
    @Expose
    private List<CapabilityList> capabilityList = new ArrayList<CapabilityList>();
    @SerializedName("unitId")
    @Expose
    private String unitId;
    
    public ConnectivityUnit()
	{
		
	}
	
	public ConnectivityUnit(final List<AttributeList> attributeList,final List<CapabilityList> capabilityList,final String unitId)
	{
		this.attributeList = attributeList;
		this.capabilityList = capabilityList;
		this.unitId = unitId;
	}

    /**
     * 
     * @return
     *     The attributeList
     */
    public List<AttributeList> getAttributeList() {
        return attributeList;
    }

    /**
     * 
     * @param attributeList
     *     The attributeList
     */
    public void setAttributeList(List<AttributeList> attributeList) {
        this.attributeList = attributeList;
    }

    /**
     * 
     * @return
     *     The capabilityList
     */
    public List<CapabilityList> getCapabilityList() {
        return capabilityList;
    }

    /**
     * 
     * @param capabilityList
     *     The capabilityList
     */
    public void setCapabilityList(List<CapabilityList> capabilityList) {
        this.capabilityList = capabilityList;
    }

    /**
     * 
     * @return
     *     The unitId
     */
    public String getUnitId() {
        return unitId;
    }

    /**
     * 
     * @param unitId
     *     The unitId
     */
    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public static class ConnectivityUnitBuilder
	 {

        private List<AttributeList> attributeList = new ArrayList<AttributeList>();
        private List<CapabilityList> capabilityList = new ArrayList<CapabilityList>();
        private String unitId;

		 public ConnectivityUnitBuilder()
		 {

		 }

		 public ConnectivityUnitBuilder withAttributeList(List<AttributeList> attributeList)
		 {
			 this.attributeList = attributeList;
			 return this;
		 }

		 public ConnectivityUnitBuilder withCapabilityList(List<CapabilityList> capabilityList)
		 {
			 this.capabilityList = capabilityList;
			 return this;
		 }
		 
		 public ConnectivityUnitBuilder withUnitId(String unitId)
		 {
			 this.unitId = unitId;
			 return this;
		 }


		 public ConnectivityUnit buildConnectivityUnit()
		 {
			 return new ConnectivityUnit(attributeList,capabilityList,unitId);
		 }

	 }
}
