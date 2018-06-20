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

import com.fasterxml.jackson.annotation.JsonIgnore;



/**
 * The persistent class for the oem database table.
 * 
 */
@Entity
@NamedQuery(name="Oem.findAll", query="SELECT o FROM Oem o")
public class Oem implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	@Column(name="LOCATION_SERVICES_STATUS")
	private String locationServicesStatus;

	@Lob
	@Column(name="LOGO_URL")
	private byte[] logoUrl;
	
	@Lob
	@Column(name="BACKGROUND_IMAGE_URL")
	private byte[] backGroundImageUrl;

	private String name;

	//bi-directional many-to-one association to AnalyticsConfig
	@OneToMany(mappedBy="oemBean")
	private List<AnalyticsConfig> analyticsConfigs;

	//bi-directional many-to-one association to Device
	@OneToMany(mappedBy="oemBean")
	@JsonIgnore
	private List<Device> devices;

	//bi-directional many-to-one association to DeviceType
	@OneToMany(mappedBy="oemBean")
	@JsonIgnore
	private List<DeviceType> deviceTypes;

	//bi-directional many-to-one association to NotificationTemplate
	@OneToMany(mappedBy="oemBean")
	@JsonIgnore
	private List<NotificationTemplate> notificationTemplates;

	//bi-directional many-to-one association to OemClassification
	@ManyToOne
	@JoinColumn(name="OEM_CLASSIFICATION")
	private OemClassification oemClassificationBean;

	//bi-directional many-to-one association to User
	@OneToMany(mappedBy="oemBean")
	@JsonIgnore
	private List<User> users;
	
	// bi-directional many-to-one association to MLModelsAndExp
	@OneToMany(mappedBy = "oem")
	@JsonIgnore
	private List<MLModelsAndExp> mlModelsAndExp;
	
	// bi-directional many-to-one association to Device
	@OneToMany(mappedBy = "oemBean")
	@JsonIgnore
	private List<Tnc> tncs;

	public Oem() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLocationServicesStatus() {
		return this.locationServicesStatus;
	}

	public void setLocationServicesStatus(String locationServicesStatus) {
		this.locationServicesStatus = locationServicesStatus;
	}

	public String getLogoUrl() {
		return new String(this.logoUrl);
	}

	public void setLogoUrl(byte[] logoUrl) {
		this.logoUrl = logoUrl;
	}

	public String getBackGroundImageUrl() {
		return new String(this.backGroundImageUrl);
	}

	public void setBackGroundImageUrl(byte[] backGroundImageUrl) {
		this.backGroundImageUrl = backGroundImageUrl;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<AnalyticsConfig> getAnalyticsConfigs() {
		return this.analyticsConfigs;
	}

	public void setAnalyticsConfigs(List<AnalyticsConfig> analyticsConfigs) {
		this.analyticsConfigs = analyticsConfigs;
	}

	public AnalyticsConfig addAnalyticsConfig(AnalyticsConfig analyticsConfig) {
		getAnalyticsConfigs().add(analyticsConfig);
		analyticsConfig.setOemBean(this);

		return analyticsConfig;
	}

	public AnalyticsConfig removeAnalyticsConfig(AnalyticsConfig analyticsConfig) {
		getAnalyticsConfigs().remove(analyticsConfig);
		analyticsConfig.setOemBean(null);

		return analyticsConfig;
	}

	public List<Device> getDevices() {
		return this.devices;
	}

	public void setDevices(List<Device> devices) {
		this.devices = devices;
	}

	public Device addDevice(Device device) {
		getDevices().add(device);
		device.setOemBean(this);

		return device;
	}

	public Device removeDevice(Device device) {
		getDevices().remove(device);
		device.setOemBean(null);

		return device;
	}

	public List<DeviceType> getDeviceTypes() {
		return this.deviceTypes;
	}

	public void setDeviceTypes(List<DeviceType> deviceTypes) {
		this.deviceTypes = deviceTypes;
	}

	public DeviceType addDeviceType(DeviceType deviceType) {
		getDeviceTypes().add(deviceType);
		deviceType.setOemBean(this);

		return deviceType;
	}

	public DeviceType removeDeviceType(DeviceType deviceType) {
		getDeviceTypes().remove(deviceType);
		deviceType.setOemBean(null);

		return deviceType;
	}

	public List<NotificationTemplate> getNotificationTemplates() {
		return this.notificationTemplates;
	}

	public void setNotificationTemplates(List<NotificationTemplate> notificationTemplates) {
		this.notificationTemplates = notificationTemplates;
	}

	public NotificationTemplate addNotificationTemplate(NotificationTemplate notificationTemplate) {
		getNotificationTemplates().add(notificationTemplate);
		notificationTemplate.setOemBean(this);

		return notificationTemplate;
	}

	public NotificationTemplate removeNotificationTemplate(NotificationTemplate notificationTemplate) {
		getNotificationTemplates().remove(notificationTemplate);
		notificationTemplate.setOemBean(null);

		return notificationTemplate;
	}

	public OemClassification getOemClassificationBean() {
		return this.oemClassificationBean;
	}

	public void setOemClassificationBean(OemClassification oemClassificationBean) {
		this.oemClassificationBean = oemClassificationBean;
	}

	public List<User> getUsers() {
		return this.users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public User addUser(User user) {
		getUsers().add(user);
		user.setOemBean(this);

		return user;
	}

	public User removeUser(User user) {
		getUsers().remove(user);
		user.setOemBean(null);

		return user;
	}
	
	public List<MLModelsAndExp> getMlModelsAndExp()
	{
		return mlModelsAndExp;
	}

	public void setMlModelsAndExp(List<MLModelsAndExp> mlModelsAndExp)
	{
		this.mlModelsAndExp = mlModelsAndExp;
	}

	public MLModelsAndExp addMLModelsAndExp(MLModelsAndExp mlModelsAndExp)
	{
		getMlModelsAndExp().add(mlModelsAndExp);
		mlModelsAndExp.setOem(this);

		return mlModelsAndExp;
	}

	public MLModelsAndExp removeMLModelsAndExp(MLModelsAndExp mlModelsAndExp)
	{
		getMlModelsAndExp().remove(mlModelsAndExp);
		mlModelsAndExp.setOem(null);

		return mlModelsAndExp;
	}
	
	public List<Tnc> getTncs() {
		return this.tncs;
	}

	public void setTncs(List<Tnc> tncs) {
		this.tncs = tncs;
	}

	public Tnc addTnc(Tnc tnc) {
		getTncs().add(tnc);
		tnc.setOemBean(this);

		return tnc;
	}

	public Tnc removeTnc(Tnc tnc) {
		getTncs().remove(tnc);
		tnc.setOemBean(null);

		return tnc;
	}
}