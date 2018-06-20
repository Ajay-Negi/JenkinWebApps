package com.dmi.dao.model;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;


/**
 * The persistent class for the sub_service database table.
 * 
 */
@Entity
@Table(name="sub_service")
@NamedQuery(name="SubService.findAll", query="SELECT s FROM SubService s")
public class SubService implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	private String name;

	//bi-directional many-to-one association to MessageRule
	@OneToMany(mappedBy="subService")
	@JsonIgnore
	private List<MessageRule> messageRules;

	//bi-directional many-to-one association to NotificationRecord
	@OneToMany(mappedBy="subServiceBean")
	@JsonIgnore
	private List<NotificationRecord> notificationRecords;

	//bi-directional many-to-one association to NotificationTemplate
	@OneToMany(mappedBy="subServiceBean")
	@JsonIgnore
	private List<NotificationTemplate> notificationTemplates;

	//bi-directional many-to-one association to Service
	@ManyToOne
	@JoinColumn(name="SERVICE")
	private Service serviceBean;

	//bi-directional many-to-one association to SubServiceSubscription
	@OneToMany(mappedBy="subServiceBean")
	@JsonIgnore
	private List<SubServiceSubscription> subServiceSubscriptions;

	public SubService() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<MessageRule> getMessageRules() {
		return this.messageRules;
	}

	public void setMessageRules(List<MessageRule> messageRules) {
		this.messageRules = messageRules;
	}

	public MessageRule addMessageRule(MessageRule messageRule) {
		getMessageRules().add(messageRule);
		messageRule.setSubService(this);

		return messageRule;
	}

	public MessageRule removeMessageRule(MessageRule messageRule) {
		getMessageRules().remove(messageRule);
		messageRule.setSubService(null);

		return messageRule;
	}

	public List<NotificationRecord> getNotificationRecords() {
		return this.notificationRecords;
	}

	public void setNotificationRecords(List<NotificationRecord> notificationRecords) {
		this.notificationRecords = notificationRecords;
	}

	public NotificationRecord addNotificationRecord(NotificationRecord notificationRecord) {
		getNotificationRecords().add(notificationRecord);
		notificationRecord.setSubServiceBean(this);

		return notificationRecord;
	}

	public NotificationRecord removeNotificationRecord(NotificationRecord notificationRecord) {
		getNotificationRecords().remove(notificationRecord);
		notificationRecord.setSubServiceBean(null);

		return notificationRecord;
	}

	public List<NotificationTemplate> getNotificationTemplates() {
		return this.notificationTemplates;
	}

	public void setNotificationTemplates(List<NotificationTemplate> notificationTemplates) {
		this.notificationTemplates = notificationTemplates;
	}

	public NotificationTemplate addNotificationTemplate(NotificationTemplate notificationTemplate) {
		getNotificationTemplates().add(notificationTemplate);
		notificationTemplate.setSubServiceBean(this);

		return notificationTemplate;
	}

	public NotificationTemplate removeNotificationTemplate(NotificationTemplate notificationTemplate) {
		getNotificationTemplates().remove(notificationTemplate);
		notificationTemplate.setSubServiceBean(null);

		return notificationTemplate;
	}

	public Service getServiceBean() {
		return this.serviceBean;
	}

	public void setServiceBean(Service serviceBean) {
		this.serviceBean = serviceBean;
	}

	public List<SubServiceSubscription> getSubServiceSubscriptions() {
		return this.subServiceSubscriptions;
	}

	public void setSubServiceSubscriptions(List<SubServiceSubscription> subServiceSubscriptions) {
		this.subServiceSubscriptions = subServiceSubscriptions;
	}

	public SubServiceSubscription addSubServiceSubscription(SubServiceSubscription subServiceSubscription) {
		getSubServiceSubscriptions().add(subServiceSubscription);
		subServiceSubscription.setSubServiceBean(this);

		return subServiceSubscription;
	}

	public SubServiceSubscription removeSubServiceSubscription(SubServiceSubscription subServiceSubscription) {
		getSubServiceSubscriptions().remove(subServiceSubscription);
		subServiceSubscription.setSubServiceBean(null);

		return subServiceSubscription;
	}

}