package com.dmi.dao.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * The persistent class for the user database table.
 * 
 */
@Entity
@NamedQuery(name="User.findAll", query="SELECT u FROM User u")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	private String address;

	@Column(name="FIRST_NAME")
	private String firstName;

	@Column(name="LAST_NAME")
	private String lastName;

	private String password;

	private String username;

	//bi-directional many-to-one association to DeviceRegistration
	@OneToMany(mappedBy="userBean")
	@JsonIgnore
	private List<DeviceRegistration> deviceRegistrations;

	//bi-directional many-to-one association to NotificationContact
	@OneToOne(mappedBy="userBean")
	@JsonIgnore
	private NotificationContact notificationContact;

	//bi-directional many-to-one association to Oem
	@ManyToOne
	@JoinColumn(name="OEM")
	private Oem oemBean;

	//bi-directional many-to-one association to Role
	@ManyToOne
	@JoinColumn(name="ROLE")
	private Role roleBean;

	//bi-directional many-to-one association to Uom
	@ManyToOne
	@JoinColumn(name="MEASUREMENT_UNIT")
	private Uom uom;

	//bi-directional many-to-one association to UserStatus
	@ManyToOne
	@JoinColumn(name="USER_STATUS")
	private UserStatus userStatusBean;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TNC_ACCEPTANCE_DT")
	private Date tncAcceptanceDt;
	
	// bi-directional many-to-one association to Tnc
	@ManyToOne
	@JoinColumn(name = "TNC")
	private Tnc tncBean;

	public User() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
	public Date getTncAcceptanceDt() {
		return this.tncAcceptanceDt;
	}

	public void setTncAcceptanceDt(Date tncAcceptanceDt) {
		this.tncAcceptanceDt = tncAcceptanceDt;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<DeviceRegistration> getDeviceRegistrations() {
		return this.deviceRegistrations;
	}

	public void setDeviceRegistrations(List<DeviceRegistration> deviceRegistrations) {
		this.deviceRegistrations = deviceRegistrations;
	}

	public DeviceRegistration addDeviceRegistration(DeviceRegistration deviceRegistration) {
		getDeviceRegistrations().add(deviceRegistration);
		deviceRegistration.setUserBean(this);

		return deviceRegistration;
	}

	public DeviceRegistration removeDeviceRegistration(DeviceRegistration deviceRegistration) {
		getDeviceRegistrations().remove(deviceRegistration);
		deviceRegistration.setUserBean(null);

		return deviceRegistration;
	}

	public NotificationContact getNotificationContact() {
		return this.notificationContact;
	}

	public void setNotificationContacts(NotificationContact notificationContact) {
		this.notificationContact = notificationContact;
	}

	public Oem getOemBean() {
		return this.oemBean;
	}

	public void setOemBean(Oem oemBean) {
		this.oemBean = oemBean;
	}

	public Role getRoleBean() {
		return this.roleBean;
	}

	public void setRoleBean(Role roleBean) {
		this.roleBean = roleBean;
	}

	public Uom getUom() {
		return this.uom;
	}

	public void setUom(Uom uom) {
		this.uom = uom;
	}

	public UserStatus getUserStatusBean() {
		return this.userStatusBean;
	}

	public void setUserStatusBean(UserStatus userStatusBean) {
		this.userStatusBean = userStatusBean;
	}

	public Tnc getTncBean()
	{
		return tncBean;
	}

	public void setTncBean(Tnc tncBean)
	{
		this.tncBean = tncBean;
	}
}