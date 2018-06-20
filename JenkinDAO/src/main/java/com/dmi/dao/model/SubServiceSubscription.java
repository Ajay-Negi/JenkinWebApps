package com.dmi.dao.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * The persistent class for the sub_service_subscription database table.
 * 
 */
@Entity
@Table(name="sub_service_subscription")
@NamedQuery(name="SubServiceSubscription.findAll", query="SELECT s FROM SubServiceSubscription s")
public class SubServiceSubscription implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	@ManyToOne
	@JoinColumn(name="SERVICE_SUBSCRIPTION")
	private ServiceSubscription serviceSubscription;

	//bi-directional many-to-one association to NotificationSubcription
	@OneToMany(mappedBy="subServiceSubscriptionBean", cascade=CascadeType.ALL)
	@JsonIgnore
	private List<NotificationSubscription> notificationSubcriptions;

	//bi-directional many-to-one association to SubService
	@ManyToOne
	@JoinColumn(name="SUB_SERVICE")
	private SubService subServiceBean;

	public SubServiceSubscription() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ServiceSubscription getServiceSubscription() {
		return this.serviceSubscription;
	}

	public void setServiceSubscription(ServiceSubscription serviceSubscription) {
		this.serviceSubscription = serviceSubscription;
	}

	public List<NotificationSubscription> getNotificationSubcriptions() {
		return this.notificationSubcriptions;
	}

	public void setNotificationSubcriptions(List<NotificationSubscription> notificationSubcriptions) {
		this.notificationSubcriptions = notificationSubcriptions;
	}

	public NotificationSubscription addNotificationSubcription(NotificationSubscription notificationSubcription) {
		getNotificationSubcriptions().add(notificationSubcription);
		notificationSubcription.setSubServiceSubscriptionBean(this);

		return notificationSubcription;
	}

	public NotificationSubscription removeNotificationSubcription(NotificationSubscription notificationSubcription) {
		getNotificationSubcriptions().remove(notificationSubcription);
		notificationSubcription.setSubServiceSubscriptionBean(null);

		return notificationSubcription;
	}

	public SubService getSubServiceBean() {
		return this.subServiceBean;
	}

	public void setSubServiceBean(SubService subServiceBean) {
		this.subServiceBean = subServiceBean;
	}

}