package com.dmi.dao.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the notification_contact database table.
 * 
 */
@Entity
@Table(name="notification_contact")
@NamedQuery(name="NotificationContact.findAll", query="SELECT n FROM NotificationContact n")
public class NotificationContact implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	//bi-directional many-to-one association to Email
	@ManyToOne
	@JoinColumn(name="EMAIL")
	private Email emailBean;

	//bi-directional many-to-one association to Sm
	@ManyToOne
	@JoinColumn(name="SMS")
	private Sms sms;

	//bi-directional many-to-one association to User
	@OneToOne
	@JoinColumn(name="USER")
	private User userBean;

	public NotificationContact() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Email getEmailBean() {
		return this.emailBean;
	}

	public void setEmailBean(Email emailBean) {
		this.emailBean = emailBean;
	}

	public Sms getSms() {
		return this.sms;
	}

	public void setSms(Sms sms) {
		this.sms = sms;
	}

	public User getUserBean() {
		return this.userBean;
	}

	public void setUserBean(User userBean) {
		this.userBean = userBean;
	}

}