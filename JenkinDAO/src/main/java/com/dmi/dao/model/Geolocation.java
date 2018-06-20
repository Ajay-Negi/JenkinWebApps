package com.dmi.dao.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;


/**
 * The persistent class for the geolocation database table.
 * 
 */
@Entity
@NamedQuery(name="Geolocation.findAll", query="SELECT g FROM Geolocation g")
public class Geolocation implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	@Column(name="EMAIL_TEMPLATE_ID")
	private Long emailTemplateId;

	@Lob
	private byte[] geography;

	@Column(name="SERVICE_ID")
	private Long serviceId;

	@Column(name="SMS_TEMPLATE_ID")
	private Long smsTemplateId;

	@Column(name="SUB_SERVICE_ID")
	private Long subServiceId;

	//bi-directional many-to-one association to Device
	@ManyToOne
	@JoinColumn(name="DEVICE")
	private Device deviceBean;

	//bi-directional many-to-one association to GeofenceAction
	@ManyToOne
	@JoinColumn(name="GEOFENCE_ACTION")
	private GeofenceAction geofenceActionBean;

	//bi-directional many-to-one association to Uom
	@ManyToOne
	@JoinColumn(name="MEASUREMENT_UNIT")
	private Uom uom;

	public Geolocation() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getEmailTemplateId() {
		return this.emailTemplateId;
	}

	public void setEmailTemplateId(Long emailTemplateId) {
		this.emailTemplateId = emailTemplateId;
	}

	public byte[] getGeography() {
		return this.geography;
	}

	public void setGeography(byte[] geography) {
		this.geography = geography;
	}

	public Long getServiceId() {
		return this.serviceId;
	}

	public void setServiceId(Long serviceId) {
		this.serviceId = serviceId;
	}

	public Long getSmsTemplateId() {
		return this.smsTemplateId;
	}

	public void setSmsTemplateId(Long smsTemplateId) {
		this.smsTemplateId = smsTemplateId;
	}

	public Long getSubServiceId() {
		return this.subServiceId;
	}

	public void setSubServiceId(Long subServiceId) {
		this.subServiceId = subServiceId;
	}

	public Device getDeviceBean() {
		return this.deviceBean;
	}

	public void setDeviceBean(Device deviceBean) {
		this.deviceBean = deviceBean;
	}

	public GeofenceAction getGeofenceActionBean() {
		return this.geofenceActionBean;
	}

	public void setGeofenceActionBean(GeofenceAction geofenceActionBean) {
		this.geofenceActionBean = geofenceActionBean;
	}

	public Uom getUom() {
		return this.uom;
	}

	public void setUom(Uom uom) {
		this.uom = uom;
	}

}