
package com.dmi.dto.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.dmi.dto.oemmodel.metainfo.AttributeList;
import com.dmi.dto.oemmodel.metainfo.CapabilityList;
import com.dmi.dto.oemmodel.metainfo.ConnectivityUnit;
import com.dmi.dto.oemmodel.metainfo.EventSubscriptionList;
import com.dmi.dto.oemmodel.metainfo.ImageDataList;
import com.dmi.dto.oemmodel.metainfo.PIIDataSet;
import com.dmi.dto.oemmodel.rawmessage.AssetDataList;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


@JsonInclude(Include.NON_EMPTY)
public class OemModel implements Serializable {

	private static final long serialVersionUID = 1L;

	@SerializedName("messageModelVersion")
	@Expose
	private String messageModelVersion;

	@SerializedName("assetId")
	@Expose
	private String assetId;

	@SerializedName("oemId")
	@Expose
	private String oemId;

	@SerializedName("deviceTypeId")
	@Expose
	private String deviceTypeId;

	@SerializedName("assetType")
	@Expose
	private String assetType;

	@SerializedName("attributeList")
	@Expose
	private List<AttributeList> attributeList = new ArrayList<AttributeList>();

	@SerializedName("capabilityList")
	@Expose
	private List<CapabilityList> capabilityList = new ArrayList<CapabilityList>();

	@SerializedName("connectivityUnit")
	@Expose
	private ConnectivityUnit connectivityUnit;

	@SerializedName("customerId")
	@Expose
	private String customerId;

	@SerializedName("eventSubscriptionList")
	@Expose
	private List<EventSubscriptionList> eventSubscriptionList = new ArrayList<EventSubscriptionList>();

	@SerializedName("PIIDataSet")
	@Expose
	private PIIDataSet PIIDataSet;

	@SerializedName("assetDataList")
	@Expose
	private List<AssetDataList> assetDataList = new ArrayList<AssetDataList>();

	@SerializedName("imageDataList")
	@Expose
	private List<ImageDataList> imageDataList = new ArrayList<ImageDataList>();

	public OemModel() {

	}

	private OemModel(final String messageModelVersion, final String assetId, final String oemId,
			final String deviceTypeId, final String assetType, final List<AttributeList> attributeList,
			final List<CapabilityList> capabilityList, final ConnectivityUnit connectivityUnit, final String customerId,
			final List<EventSubscriptionList> eventSubscriptionList, final PIIDataSet PIIDataSet,
			final List<AssetDataList> assetDataList, final List<ImageDataList> imageDataList) {
		this.messageModelVersion = messageModelVersion;
		this.assetId = assetId;
		this.assetType = assetType;
		this.customerId = customerId;
		this.oemId = oemId;
		this.deviceTypeId = deviceTypeId;
		this.attributeList = attributeList;
		this.capabilityList = capabilityList;
		this.connectivityUnit = connectivityUnit;
		this.eventSubscriptionList = eventSubscriptionList;
		this.PIIDataSet = PIIDataSet;
		this.assetDataList = assetDataList;
		this.imageDataList = imageDataList;
	}

	

	public String getMessageModelVersion() {
		return messageModelVersion;
	}

	public void setMessageModelVersion(String messageModelVersion) {
		this.messageModelVersion = messageModelVersion;
	}

	public String getAssetId() {
		return assetId;
	}

	
	public String getOemId() {
		return oemId;
	}

	public void setOemId(String oemId) {
		this.oemId = oemId;
	}

	public String getDeviceTypeId() {
		return deviceTypeId;
	}

	public void setDeviceTypeId(String deviceTypeId) {
		this.deviceTypeId = deviceTypeId;
	}

	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}

	public String getAssetType() {
		return assetType;
	}

	public void setAssetType(String assetType) {
		this.assetType = assetType;
	}

	public List<AttributeList> getAttributeList() {
		return attributeList;
	}

	public void setAttributeList(List<AttributeList> attributeList) {
		this.attributeList = attributeList;
	}

	public List<CapabilityList> getCapabilityList() {
		return capabilityList;
	}

	public void setCapabilityList(List<CapabilityList> capabilityList) {
		this.capabilityList = capabilityList;
	}

	public ConnectivityUnit getConnectivityUnit() {
		return connectivityUnit;
	}

	public void setConnectivityUnit(ConnectivityUnit connectivityUnit) {
		this.connectivityUnit = connectivityUnit;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public List<EventSubscriptionList> getEventSubscriptionList() {
		return eventSubscriptionList;
	}

	public void setEventSubscriptionList(List<EventSubscriptionList> eventSubscriptionList) {
		this.eventSubscriptionList = eventSubscriptionList;
	}

	public PIIDataSet getPIIDataSet() {
		return PIIDataSet;
	}

	public void setPIIDataSet(PIIDataSet PIIDataSet) {
		this.PIIDataSet = PIIDataSet;
	}

	public List<AssetDataList> getAssetDataList() {
		return assetDataList;
	}

	public void setAssetDataList(List<AssetDataList> assetDataList) {
		this.assetDataList = assetDataList;
	}

	public List<ImageDataList> getImageDataList() {
		return imageDataList;
	}

	public void setImageDataList(List<ImageDataList> imageDataList) {
		this.imageDataList = imageDataList;
	}

	public static class OemModelBuilder {

		private String messageModelVersion;
		private String assetId;
		private String assetType;
		private String customerId;

		private String oemId;
		private String deviceTypeId;
		private List<AttributeList> attributeList = new ArrayList<AttributeList>();
		private List<CapabilityList> capabilityList = new ArrayList<CapabilityList>();
		private ConnectivityUnit connectivityUnit;
		private List<EventSubscriptionList> eventSubscriptionList = new ArrayList<EventSubscriptionList>();
		private PIIDataSet PIIDataSet;
		private List<AssetDataList> assetDataList = new ArrayList<AssetDataList>();
		private List<ImageDataList> imageDataList = new ArrayList<>();

		public OemModelBuilder() {

		}

		public OemModelBuilder withCustomerId(String customerId) {
			this.customerId = customerId;
			return this;
		}

		public OemModelBuilder withAssetID(String assetId) {
			this.assetId = assetId;
			return this;
		}

		public OemModelBuilder withAssetType(String assetType) {
			this.assetType = assetType;
			return this;
		}

		public OemModelBuilder withDomainId(String oemId) {
			this.oemId = oemId;
			return this;
		}

		public OemModelBuilder withSubdomainId(String deviceTypeId) {
			this.deviceTypeId = deviceTypeId;
			return this;
		}

		public OemModelBuilder withMessageModelVersion(String messageModelVersion) {
			this.messageModelVersion = messageModelVersion;
			return this;
		}

		public OemModelBuilder withAttributeList(List<AttributeList> attributeList) {
			this.attributeList = attributeList;
			return this;
		}

		public OemModelBuilder withCapabilityList(List<CapabilityList> capabilityList) {
			this.capabilityList = capabilityList;
			return this;
		}

		public OemModelBuilder withConnectivityUnit(ConnectivityUnit connectivityUnit) {
			this.connectivityUnit = connectivityUnit;
			return this;
		}

		public OemModelBuilder withEventSubscriptionList(List<EventSubscriptionList> eventSubscriptionList) {
			this.eventSubscriptionList = eventSubscriptionList;
			return this;
		}

		public OemModelBuilder withPIIDataSet(PIIDataSet PIIDataSet) {
			this.PIIDataSet = PIIDataSet;
			return this;
		}

		public OemModelBuilder withAssetDataList(List<AssetDataList> assetDataList) {
			this.assetDataList = assetDataList;
			return this;
		}

		public OemModelBuilder withImageDataList(List<ImageDataList> imageDataList) {
			this.imageDataList = imageDataList;
			return this;
		}

		public OemModel build() {
			return new OemModel(messageModelVersion, assetId, oemId, deviceTypeId, assetType, attributeList,
					capabilityList, connectivityUnit, customerId, eventSubscriptionList, PIIDataSet, assetDataList,
					imageDataList);

		}

	}

}
