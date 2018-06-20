package com.dmi.dao.model;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;


/**
 * The persistent class for the email database table.
 * 
 */
@Entity
@NamedQuery(name="Email.findAll", query="SELECT e FROM Email e")
public class Email implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	@Column(name="ALTERNATE_EMAIL")
	private String alternateEmail;

	@Column(name="PRIMARY_EMAIL")
	private String primaryEmail;

	//bi-directional many-to-one association to NotificationContact
	@OneToMany(mappedBy="emailBean")
	@JsonIgnore
	private List<NotificationContact> notificationContacts;

	public Email() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAlternateEmail() {
		return this.alternateEmail;
	}

	public void setAlternateEmail(String alternateEmail) {
		this.alternateEmail = alternateEmail;
	}

	public String getPrimaryEmail() {
		return this.primaryEmail;
	}

	public void setPrimaryEmail(String primaryEmail) {
		this.primaryEmail = primaryEmail;
	}

	public List<NotificationContact> getNotificationContacts() {
		return this.notificationContacts;
	}

	public void setNotificationContacts(List<NotificationContact> notificationContacts) {
		this.notificationContacts = notificationContacts;
	}

	public NotificationContact addNotificationContact(NotificationContact notificationContact) {
		getNotificationContacts().add(notificationContact);
		notificationContact.setEmailBean(this);

		return notificationContact;
	}

	public NotificationContact removeNotificationContact(NotificationContact notificationContact) {
		getNotificationContacts().remove(notificationContact);
		notificationContact.setEmailBean(null);

		return notificationContact;
	}

}