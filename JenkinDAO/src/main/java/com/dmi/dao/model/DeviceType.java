package com.dmi.dao.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * The persistent class for the device_type database table.
 * 
 */
@Entity
@Table(name="device_type")
@NamedQuery(name="DeviceType.findAll", query="SELECT d FROM DeviceType d")
public class DeviceType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	private String alias;

	@Lob
	private byte[] metadata;
	
	@Lob
	@Column(name="LOGO_URL")
	private byte[] logoUrl;

	//bi-directional many-to-one association to Device
	@OneToMany(mappedBy="deviceTypeBean")
	@JsonIgnore
	private List<Device> devices;

	//bi-directional many-to-one association to Oem
	@ManyToOne
	@JoinColumn(name="OEM")
	@JsonIgnore
	private Oem oemBean;

	//bi-directional many-to-one association to MessageModel
	@OneToMany(mappedBy="deviceTypeBean")
	@JsonIgnore
	private List<MessageModel> messageModels;

	//bi-directional many-to-one association to Service
	@OneToMany(mappedBy="deviceTypeBean")
	@JsonIgnore
	private List<Service> services;

	//bi-directional many-to-one association to VhrTemplate
	@OneToMany(mappedBy="deviceTypeBean")
	@JsonIgnore
	private List<VhrTemplate> vhrTemplates;
	
	// bi-directional many-to-one association to MLModelsAndExp
	@OneToMany(mappedBy = "deviceType")
	@JsonIgnore
	private List<MLModelsAndExp> mlModelsAndExp;
	
	//bi-directional many-to-one association to CustomRuleAsset
	@OneToMany(mappedBy="deviceTypeBean")
	@JsonIgnore
	private List<CustomRuleAsset> customRuleAssets;

	public DeviceType() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAlias() {
		return this.alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getMetadata() {
		return new String(this.metadata);
	}

	public void setMetadata(byte[] metadata) {
		this.metadata = metadata;
	}

	public byte[] getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(byte[] logoUrl) {
		this.logoUrl = logoUrl;
	}

	public List<Device> getDevices() {
		return this.devices;
	}

	public void setDevices(List<Device> devices) {
		this.devices = devices;
	}

	public Device addDevice(Device device) {
		getDevices().add(device);
		device.setDeviceTypeBean(this);

		return device;
	}

	public Device removeDevice(Device device) {
		getDevices().remove(device);
		device.setDeviceTypeBean(null);

		return device;
	}

	public Oem getOemBean() {
		return this.oemBean;
	}

	public void setOemBean(Oem oemBean) {
		this.oemBean = oemBean;
	}

	public List<MessageModel> getMessageModels() {
		return this.messageModels;
	}

	public void setMessageModels(List<MessageModel> messageModels) {
		this.messageModels = messageModels;
	}

	public MessageModel addMessageModel(MessageModel messageModel) {
		getMessageModels().add(messageModel);
		messageModel.setDeviceTypeBean(this);

		return messageModel;
	}

	public MessageModel removeMessageModel(MessageModel messageModel) {
		getMessageModels().remove(messageModel);
		messageModel.setDeviceTypeBean(null);

		return messageModel;
	}
	
	public List<MLModelsAndExp> getMlModelsAndExp() {
		return mlModelsAndExp;
	}

	public void setMlModelsAndExp(List<MLModelsAndExp> mlModelsAndExp) {
		this.mlModelsAndExp = mlModelsAndExp;
	}

	public MLModelsAndExp addMLModelsAndExp(MLModelsAndExp mlModelsAndExp) {
		getMlModelsAndExp().add(mlModelsAndExp);
		mlModelsAndExp.setDeviceType(this);

		return mlModelsAndExp;
	}

	public MLModelsAndExp removeMLModelsAndExp(MLModelsAndExp mlModelsAndExp) {
		getMlModelsAndExp().remove(mlModelsAndExp);
		mlModelsAndExp.setDeviceType(null);

		return mlModelsAndExp;
	}

	public List<Service> getServices() {
		return this.services;
	}

	public void setServices(List<Service> services) {
		this.services = services;
	}

	public Service addService(Service service) {
		getServices().add(service);
		service.setDeviceTypeBean(this);

		return service;
	}

	public Service removeService(Service service) {
		getServices().remove(service);
		service.setDeviceTypeBean(null);

		return service;
	}

	public List<VhrTemplate> getVhrTemplates() {
		return this.vhrTemplates;
	}

	public void setVhrTemplates(List<VhrTemplate> vhrTemplates) {
		this.vhrTemplates = vhrTemplates;
	}

	public VhrTemplate addVhrTemplate(VhrTemplate vhrTemplate) {
		getVhrTemplates().add(vhrTemplate);
		vhrTemplate.setDeviceTypeBean(this);

		return vhrTemplate;
	}

	public VhrTemplate removeVhrTemplate(VhrTemplate vhrTemplate) {
		getVhrTemplates().remove(vhrTemplate);
		vhrTemplate.setDeviceTypeBean(null);

		return vhrTemplate;
	}
	
	public List<CustomRuleAsset> getCustomRuleAssets() {
		return this.customRuleAssets;
	}

	public void setCustomRuleAssets(List<CustomRuleAsset> customRuleAssets) {
		this.customRuleAssets = customRuleAssets;
	}

	public CustomRuleAsset addCustomRuleAsset(CustomRuleAsset customRuleAsset) {
		getCustomRuleAssets().add(customRuleAsset);
		customRuleAsset.setDeviceTypeBean(this);

		return customRuleAsset;
	}

	public CustomRuleAsset removeCustomRuleAsset(CustomRuleAsset customRuleAsset) {
		getCustomRuleAssets().remove(customRuleAsset);
		customRuleAsset.setDeviceTypeBean(null);

		return customRuleAsset;
	}

}