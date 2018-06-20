package com.dmi.dao.model;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;


/**
 * The persistent class for the uom database table.
 * 
 */
@Entity
@NamedQuery(name="Uom.findAll", query="SELECT u FROM Uom u")
public class Uom implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	private String alias;

	//bi-directional many-to-one association to Geolocation
	@OneToMany(mappedBy="uom")
	@JsonIgnore
	private List<Geolocation> geolocations;

	//bi-directional many-to-one association to User
	@OneToMany(mappedBy="uom")
	@JsonIgnore
	private List<User> users;

	public Uom() {
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

	public List<Geolocation> getGeolocations() {
		return this.geolocations;
	}

	public void setGeolocations(List<Geolocation> geolocations) {
		this.geolocations = geolocations;
	}

	public Geolocation addGeolocation(Geolocation geolocation) {
		getGeolocations().add(geolocation);
		geolocation.setUom(this);

		return geolocation;
	}

	public Geolocation removeGeolocation(Geolocation geolocation) {
		getGeolocations().remove(geolocation);
		geolocation.setUom(null);

		return geolocation;
	}

	public List<User> getUsers() {
		return this.users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public User addUser(User user) {
		getUsers().add(user);
		user.setUom(this);

		return user;
	}

	public User removeUser(User user) {
		getUsers().remove(user);
		user.setUom(null);

		return user;
	}

}