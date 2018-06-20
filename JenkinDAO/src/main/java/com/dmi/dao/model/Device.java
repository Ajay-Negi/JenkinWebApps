package com.dmi.dao.model;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;


/**
 * The persistent class for the device database table.
 * 
 */
@Entity
@NamedQuery(name="Device.findAll", query="SELECT d FROM Device d")
public class Device implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	private String alias;
	
	@Lob
	@Column(name="LOGO_URL")
	private byte[] logoUrl;

	//bi-directional many-to-one association to Alert
	@OneToMany(mappedBy="deviceBean")
	@JsonIgnore
	private List<Alert> alerts;

	//bi-directional many-to-one association to DeviceType
	@ManyToOne
	@JoinColumn(name="DEVICE_TYPE")
	private DeviceType deviceTypeBean;

	//bi-directional many-to-one association to Oem
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name="OEM")
	private Oem oemBean;

	//bi-directional many-to-one association to Vin
	@OneToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name="VIN")
	private Vin vinBean;

	//bi-directional many-to-one association to DeviceData
	@OneToMany(mappedBy="device")
	@JsonIgnore
	private List<DeviceData> deviceData;

	//bi-directional many-to-one association to DeviceRegistration
	@OneToMany(mappedBy="deviceBean")
	@JsonIgnore
	private List<DeviceRegistration> deviceRegistrations;

	//bi-directional many-to-one association to Geolocation
	@OneToMany(mappedBy="deviceBean")
	@JsonIgnore
	private List<Geolocation> geolocations;

	//bi-directional many-to-one association to Speed
	@OneToMany(mappedBy="deviceBean")
	@JsonIgnore
	private List<Speed> speeds;

	public Device() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAlias() {
		return this.alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public List<Alert> getAlerts() {
		return this.alerts;
	}

	public void setAlerts(List<Alert> alerts) {
		this.alerts = alerts;
	}

	public Alert addAlert(Alert alert) {
		getAlerts().add(alert);
		alert.setDeviceBean(this);

		return alert;
	}

	public Alert removeAlert(Alert alert) {
		getAlerts().remove(alert);
		alert.setDeviceBean(null);

		return alert;
	}

	public DeviceType getDeviceTypeBean() {
		return this.deviceTypeBean;
	}

	public void setDeviceTypeBean(DeviceType deviceTypeBean) {
		this.deviceTypeBean = deviceTypeBean;
	}

	public String getLogoUrl() {
		return new String(this.logoUrl);
	}

	public void setLogoUrl(byte[] logoUrl) {
		this.logoUrl = logoUrl;
	}
	
	public Oem getOemBean() {
		return this.oemBean;
	}

	public void setOemBean(Oem oemBean) {
		this.oemBean = oemBean;
	}

	public Vin getVinBean() {
		return this.vinBean;
	}

	public void setVinBean(Vin vinBean) {
		this.vinBean = vinBean;
	}

	public List<DeviceData> getDeviceData() {
		return this.deviceData;
	}

	public void setDeviceData(List<DeviceData> deviceData) {
		this.deviceData = deviceData;
	}

	public DeviceData addDeviceData(DeviceData deviceData) {
		getDeviceData().add(deviceData);
		deviceData.setDevice(this);

		return deviceData;
	}

	public DeviceData removeDeviceData(DeviceData deviceData) {
		getDeviceData().remove(deviceData);
		deviceData.setDevice(null);

		return deviceData;
	}

	public List<DeviceRegistration> getDeviceRegistrations() {
		return this.deviceRegistrations;
	}

	public void setDeviceRegistrations(List<DeviceRegistration> deviceRegistrations) {
		this.deviceRegistrations = deviceRegistrations;
	}

	public DeviceRegistration addDeviceRegistration(DeviceRegistration deviceRegistration) {
		getDeviceRegistrations().add(deviceRegistration);
		deviceRegistration.setDeviceBean(this);

		return deviceRegistration;
	}

	public DeviceRegistration removeDeviceRegistration(DeviceRegistration deviceRegistration) {
		getDeviceRegistrations().remove(deviceRegistration);
		deviceRegistration.setDeviceBean(null);

		return deviceRegistration;
	}

	public List<Geolocation> getGeolocations() {
		return this.geolocations;
	}

	public void setGeolocations(List<Geolocation> geolocations) {
		this.geolocations = geolocations;
	}

	public Geolocation addGeolocation(Geolocation geolocation) {
		getGeolocations().add(geolocation);
		geolocation.setDeviceBean(this);

		return geolocation;
	}

	public Geolocation removeGeolocation(Geolocation geolocation) {
		getGeolocations().remove(geolocation);
		geolocation.setDeviceBean(null);

		return geolocation;
	}

	public List<Speed> getSpeeds() {
		return this.speeds;
	}

	public void setSpeeds(List<Speed> speeds) {
		this.speeds = speeds;
	}

	public Speed addSpeed(Speed speed) {
		getSpeeds().add(speed);
		speed.setDeviceBean(this);

		return speed;
	}

	public Speed removeSpeed(Speed speed) {
		getSpeeds().remove(speed);
		speed.setDeviceBean(null);

		return speed;
	}

}