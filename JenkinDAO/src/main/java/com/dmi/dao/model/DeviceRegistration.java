package com.dmi.dao.model;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;


/**
 * The persistent class for the device_registration database table.
 * 
 */
@Entity
@Table(name="device_registration")
@NamedQuery(name="DeviceRegistration.findAll", query="SELECT d FROM DeviceRegistration d")
public class DeviceRegistration implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	//bi-directional many-to-one association to Device
	@ManyToOne
	@JoinColumn(name="DEVICE")
	private Device deviceBean;

	//bi-directional many-to-one association to DeviceStatus
	@ManyToOne
	@JoinColumn(name="DEVICE_STATUS")
	private DeviceStatus deviceStatusBean;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="USER")
	private User userBean;

	//bi-directional many-to-one association to NotificationRecord
	@OneToMany(mappedBy="deviceRegistrationBean")
	@JsonIgnore
	private List<NotificationRecord> notificationRecords;

	//bi-directional many-to-one association to ServiceSubscription
	@OneToMany(mappedBy="deviceRegistrationBean")
	@JsonIgnore
	private List<ServiceSubscription> serviceSubscriptions;

	public DeviceRegistration() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Device getDeviceBean() {
		return this.deviceBean;
	}

	public void setDeviceBean(Device deviceBean) {
		this.deviceBean = deviceBean;
	}

	public DeviceStatus getDeviceStatusBean() {
		return this.deviceStatusBean;
	}

	public void setDeviceStatusBean(DeviceStatus deviceStatusBean) {
		this.deviceStatusBean = deviceStatusBean;
	}

	public User getUserBean() {
		return this.userBean;
	}

	public void setUserBean(User userBean) {
		this.userBean = userBean;
	}

	public List<NotificationRecord> getNotificationRecords() {
		return this.notificationRecords;
	}

	public void setNotificationRecords(List<NotificationRecord> notificationRecords) {
		this.notificationRecords = notificationRecords;
	}

	public NotificationRecord addNotificationRecord(NotificationRecord notificationRecord) {
		getNotificationRecords().add(notificationRecord);
		notificationRecord.setDeviceRegistrationBean(this);

		return notificationRecord;
	}

	public NotificationRecord removeNotificationRecord(NotificationRecord notificationRecord) {
		getNotificationRecords().remove(notificationRecord);
		notificationRecord.setDeviceRegistrationBean(null);

		return notificationRecord;
	}

	public List<ServiceSubscription> getServiceSubscriptions() {
		return this.serviceSubscriptions;
	}

	public void setServiceSubscriptions(List<ServiceSubscription> serviceSubscriptions) {
		this.serviceSubscriptions = serviceSubscriptions;
	}

	public ServiceSubscription addServiceSubscription(ServiceSubscription serviceSubscription) {
		getServiceSubscriptions().add(serviceSubscription);
		serviceSubscription.setDeviceRegistrationBean(this);

		return serviceSubscription;
	}

	public ServiceSubscription removeServiceSubscription(ServiceSubscription serviceSubscription) {
		getServiceSubscriptions().remove(serviceSubscription);
		serviceSubscription.setDeviceRegistrationBean(null);

		return serviceSubscription;
	}

}