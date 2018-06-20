package com.dmi.dao.model;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;


/**
 * The persistent class for the notification_channel database table.
 * 
 */
@Entity
@Table(name="notification_channel")
@NamedQuery(name="NotificationChannel.findAll", query="SELECT n FROM NotificationChannel n")
public class NotificationChannel implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	private String alias;

	//bi-directional many-to-one association to NotificationRecord
	@OneToMany(mappedBy="notificationChannelBean")
	@JsonIgnore
	private List<NotificationRecord> notificationRecords;

	//bi-directional many-to-one association to NotificationSubcription
	@OneToMany(mappedBy="notificationChannelBean")
	@JsonIgnore
	private List<NotificationSubscription> notificationSubcriptions;

	//bi-directional many-to-one association to NotificationTemplate
	@OneToMany(mappedBy="notificationChannelBean")
	@JsonIgnore
	private List<NotificationTemplate> notificationTemplates;

	public NotificationChannel() {
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

	public List<NotificationRecord> getNotificationRecords() {
		return this.notificationRecords;
	}

	public void setNotificationRecords(List<NotificationRecord> notificationRecords) {
		this.notificationRecords = notificationRecords;
	}

	public NotificationRecord addNotificationRecord(NotificationRecord notificationRecord) {
		getNotificationRecords().add(notificationRecord);
		notificationRecord.setNotificationChannelBean(this);

		return notificationRecord;
	}

	public NotificationRecord removeNotificationRecord(NotificationRecord notificationRecord) {
		getNotificationRecords().remove(notificationRecord);
		notificationRecord.setNotificationChannelBean(null);

		return notificationRecord;
	}

	public List<NotificationSubscription> getNotificationSubcriptions() {
		return this.notificationSubcriptions;
	}

	public void setNotificationSubcriptions(List<NotificationSubscription> notificationSubcriptions) {
		this.notificationSubcriptions = notificationSubcriptions;
	}

	public NotificationSubscription addNotificationSubcription(NotificationSubscription notificationSubcription) {
		getNotificationSubcriptions().add(notificationSubcription);
		notificationSubcription.setNotificationChannelBean(this);

		return notificationSubcription;
	}

	public NotificationSubscription removeNotificationSubcription(NotificationSubscription notificationSubcription) {
		getNotificationSubcriptions().remove(notificationSubcription);
		notificationSubcription.setNotificationChannelBean(null);

		return notificationSubcription;
	}

	public List<NotificationTemplate> getNotificationTemplates() {
		return this.notificationTemplates;
	}

	public void setNotificationTemplates(List<NotificationTemplate> notificationTemplates) {
		this.notificationTemplates = notificationTemplates;
	}

	public NotificationTemplate addNotificationTemplate(NotificationTemplate notificationTemplate) {
		getNotificationTemplates().add(notificationTemplate);
		notificationTemplate.setNotificationChannelBean(this);

		return notificationTemplate;
	}

	public NotificationTemplate removeNotificationTemplate(NotificationTemplate notificationTemplate) {
		getNotificationTemplates().remove(notificationTemplate);
		notificationTemplate.setNotificationChannelBean(null);

		return notificationTemplate;
	}

}