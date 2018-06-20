package com.dmi.dao.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the notification_subcription database table.
 * 
 */
@Entity
@Table(name="notification_subscription")
@NamedQuery(name="NotificationSubscription.findAll", query="SELECT n FROM NotificationSubscription n")
public class NotificationSubscription implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	//bi-directional many-to-one association to NotificationChannel
	@ManyToOne
	@JoinColumn(name="NOTIFICATION_CHANNEL")
	private NotificationChannel notificationChannelBean;

	//bi-directional many-to-one association to SubServiceSubscription
	@ManyToOne
	@JoinColumn(name="SUB_SERVICE_SUBSCRIPTION")
	private SubServiceSubscription subServiceSubscriptionBean;

	public NotificationSubscription() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public NotificationChannel getNotificationChannelBean() {
		return this.notificationChannelBean;
	}

	public void setNotificationChannelBean(NotificationChannel notificationChannelBean) {
		this.notificationChannelBean = notificationChannelBean;
	}

	public SubServiceSubscription getSubServiceSubscriptionBean() {
		return this.subServiceSubscriptionBean;
	}

	public void setSubServiceSubscriptionBean(SubServiceSubscription subServiceSubscriptionBean) {
		this.subServiceSubscriptionBean = subServiceSubscriptionBean;
	}

}