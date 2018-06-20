package com.dmi.dao.model;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;


/**
 * The persistent class for the sms database table.
 * 
 */
@Entity
@Table(name="sms")
@NamedQuery(name="Sms.findAll", query="SELECT s FROM Sms s")
public class Sms implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	@Column(name="COUNTRY_CODE")
	private String countryCode;

	@Column(name="MOBILE_NUMBER")
	private String mobileNumber;

	//bi-directional many-to-one association to NotificationContact
	@OneToMany(mappedBy="sms")
	@JsonIgnore
	private List<NotificationContact> notificationContacts;

	public Sms() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCountryCode() {
		return this.countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getMobileNumber() {
		return this.mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public List<NotificationContact> getNotificationContacts() {
		return this.notificationContacts;
	}

	public void setNotificationContacts(List<NotificationContact> notificationContacts) {
		this.notificationContacts = notificationContacts;
	}

	public NotificationContact addNotificationContact(NotificationContact notificationContact) {
		getNotificationContacts().add(notificationContact);
		notificationContact.setSms(this);

		return notificationContact;
	}

	public NotificationContact removeNotificationContact(NotificationContact notificationContact) {
		getNotificationContacts().remove(notificationContact);
		notificationContact.setSms(null);

		return notificationContact;
	}

}