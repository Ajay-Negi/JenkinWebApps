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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * The persistent class for the TNC database table.
 * 
 */
@Entity
@NamedQuery(name = "Tnc.findAll", query = "SELECT t FROM Tnc t")
public class Tnc implements Serializable
{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	@Lob
	private byte[] agreement;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EFFECTUATION_DATE")
	private Date effectuationDate;

	private Float version;

	// bi-directional many-to-one association to DeviceType
	@ManyToOne
	@JoinColumn(name = "oem")
	private Oem oemBean;

	// bi-directional many-to-one association to User
	@OneToMany(mappedBy = "tncBean")
	@JsonIgnore
	private List<User> users;

	public Tnc()
	{
	}

	public long getId()
	{
		return this.id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public byte[] getAgreement()
	{
		return this.agreement;
	}

	public void setAgreement(byte[] agreement)
	{
		this.agreement = agreement;
	}

	public Date getEffectuationDate()
	{
		return effectuationDate;
	}

	public void setEffectuationDate(Date effectuationDate)
	{
		this.effectuationDate = effectuationDate;
	}

	public List<User> getUsers()
	{
		return this.users;
	}

	public void setUsers(List<User> users)
	{
		this.users = users;
	}

	public User addUser(User user)
	{
		getUsers().add(user);
		user.setTncBean(this);

		return user;
	}

	public User removeUser(User user)
	{
		getUsers().remove(user);
		user.setTncBean(null);

		return user;
	}

	public Float getVersion()
	{
		return version;
	}

	public void setVersion(Float version)
	{
		this.version = version;
	}

	public Oem getOemBean()
	{
		return oemBean;
	}

	public void setOemBean(Oem oemBean)
	{
		this.oemBean = oemBean;
	}
	
	

}