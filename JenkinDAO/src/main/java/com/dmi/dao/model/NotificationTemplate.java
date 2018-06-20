package com.dmi.dao.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * The persistent class for the notification_template database table.
 * 
 */
@Entity
@Table(name="notification_template")
@NamedQuery(name="NotificationTemplate.findAll", query="SELECT n FROM NotificationTemplate n")
public class NotificationTemplate implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	@Lob
	private byte[] content;

	private String name;

	//bi-directional many-to-one association to MessageRule
	@OneToMany(mappedBy="notificationEmailTemplate")
	@JsonIgnore
	private List<MessageRule> emailMessageRules;

	//bi-directional many-to-one association to MessageRule
	@OneToMany(mappedBy="notificationSmsTemplate")
	@JsonIgnore
	private List<MessageRule> smsMessageRules;

	//bi-directional many-to-one association to NotificationChannel
	@ManyToOne
	@JoinColumn(name="NOTIFICATION_CHANNEL")
	private NotificationChannel notificationChannelBean;

	//bi-directional many-to-one association to NotificationType
	@ManyToOne
	@JoinColumn(name="NOTIFICATION_TYPE")
	private NotificationType notificationTypeBean;

	//bi-directional many-to-one association to Oem
	@ManyToOne
	@JoinColumn(name="OEM")
	private Oem oemBean;

	//bi-directional many-to-one association to SubService
	@ManyToOne
	@JoinColumn(name="SUB_SERVICE")
	private SubService subServiceBean;

	public NotificationTemplate() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContent() {
		return new String(this.content);
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<MessageRule> getEmailMessageRules() {
		return this.emailMessageRules;
	}

	public void setEmailMessageRules(List<MessageRule> emailMessageRules) {
		this.emailMessageRules = emailMessageRules;
	}

	public MessageRule addEmailMessageRules(MessageRule messageRule) {
		getEmailMessageRules().add(messageRule);
		messageRule.setNotificationEmailTemplate(this);

		return messageRule;
	}

	public MessageRule removeEmailMessageRule(MessageRule messageRule) {
		getEmailMessageRules().remove(messageRule);
		messageRule.setNotificationEmailTemplate(null);

		return messageRule;
	}

	public List<MessageRule> getSmsMessageRules() {
		return this.smsMessageRules;
	}

	public void setSmsMessageRules(List<MessageRule> smsMessageRules) {
		this.smsMessageRules = emailMessageRules;
	}

	public MessageRule addSmsMessageRule(MessageRule messageRule) {
		getSmsMessageRules().add(messageRule);
		messageRule.setNotificationSmsTemplate(this);

		return messageRule;
	}

	public MessageRule removeSmsMessageRule(MessageRule messageRule) {
		getEmailMessageRules().remove(messageRule);
		messageRule.setNotificationSmsTemplate(null);

		return messageRule;
	}

	public NotificationChannel getNotificationChannelBean() {
		return this.notificationChannelBean;
	}

	public void setNotificationChannelBean(NotificationChannel notificationChannelBean) {
		this.notificationChannelBean = notificationChannelBean;
	}

	public NotificationType getNotificationTypeBean() {
		return this.notificationTypeBean;
	}

	public void setNotificationTypeBean(NotificationType notificationTypeBean) {
		this.notificationTypeBean = notificationTypeBean;
	}

	public Oem getOemBean() {
		return this.oemBean;
	}

	public void setOemBean(Oem oemBean) {
		this.oemBean = oemBean;
	}

	public SubService getSubServiceBean() {
		return this.subServiceBean;
	}

	public void setSubServiceBean(SubService subServiceBean) {
		this.subServiceBean = subServiceBean;
	}

}