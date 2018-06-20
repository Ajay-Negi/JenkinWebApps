package com.dmi.dao.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the notification_record database table.
 * 
 */
@Entity
@Table(name="notification_record")
@NamedQuery(name="NotificationRecord.findAll", query="SELECT n FROM NotificationRecord n")
public class NotificationRecord implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	@Lob
	private byte[] content;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="NOTIFICATION_TIMESTAMP")
	private Date notificationTimestamp;

	//bi-directional many-to-one association to DeviceRegistration
	@ManyToOne
	@JoinColumn(name="DEVICE_REGISTRATION")
	private DeviceRegistration deviceRegistrationBean;

	//bi-directional many-to-one association to NotificationChannel
	@ManyToOne
	@JoinColumn(name="NOTIFICATION_CHANNEL")
	private NotificationChannel notificationChannelBean;

	//bi-directional many-to-one association to SubService
	@ManyToOne
	@JoinColumn(name="SUB_SERVICE")
	private SubService subServiceBean;

	public NotificationRecord() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public byte[] getContent() {
		return this.content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public Date getNotificationTimestamp() {
		return this.notificationTimestamp;
	}

	public void setNotificationTimestamp(Date notificationTimestamp) {
		this.notificationTimestamp = notificationTimestamp;
	}

	public DeviceRegistration getDeviceRegistrationBean() {
		return this.deviceRegistrationBean;
	}

	public void setDeviceRegistrationBean(DeviceRegistration deviceRegistrationBean) {
		this.deviceRegistrationBean = deviceRegistrationBean;
	}

	public NotificationChannel getNotificationChannelBean() {
		return this.notificationChannelBean;
	}

	public void setNotificationChannelBean(NotificationChannel notificationChannelBean) {
		this.notificationChannelBean = notificationChannelBean;
	}

	public SubService getSubServiceBean() {
		return this.subServiceBean;
	}

	public void setSubServiceBean(SubService subServiceBean) {
		this.subServiceBean = subServiceBean;
	}

}