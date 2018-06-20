package com.dmi.dao.model;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;


/**
 * The persistent class for the notification_type database table.
 * 
 */
@Entity
@Table(name="notification_type")
@NamedQuery(name="NotificationType.findAll", query="SELECT n FROM NotificationType n")
public class NotificationType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	private String alias;

	//bi-directional many-to-one association to NotificationTemplate
	@OneToMany(mappedBy="notificationTypeBean")
	@JsonIgnore
	private List<NotificationTemplate> notificationTemplates;

	public NotificationType() {
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

	public List<NotificationTemplate> getNotificationTemplates() {
		return this.notificationTemplates;
	}

	public void setNotificationTemplates(List<NotificationTemplate> notificationTemplates) {
		this.notificationTemplates = notificationTemplates;
	}

	public NotificationTemplate addNotificationTemplate(NotificationTemplate notificationTemplate) {
		getNotificationTemplates().add(notificationTemplate);
		notificationTemplate.setNotificationTypeBean(this);

		return notificationTemplate;
	}

	public NotificationTemplate removeNotificationTemplate(NotificationTemplate notificationTemplate) {
		getNotificationTemplates().remove(notificationTemplate);
		notificationTemplate.setNotificationTypeBean(null);

		return notificationTemplate;
	}

}