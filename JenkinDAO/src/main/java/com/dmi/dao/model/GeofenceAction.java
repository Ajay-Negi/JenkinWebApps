package com.dmi.dao.model;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;


/**
 * The persistent class for the geofence_action database table.
 * 
 */
@Entity
@Table(name="geofence_action")
@NamedQuery(name="GeofenceAction.findAll", query="SELECT g FROM GeofenceAction g")
public class GeofenceAction implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	private String alias;

	//bi-directional many-to-one association to Geolocation
	@OneToMany(mappedBy="geofenceActionBean")
	@JsonIgnore
	private List<Geolocation> geolocations;

	public GeofenceAction() {
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
		geolocation.setGeofenceActionBean(this);

		return geolocation;
	}

	public Geolocation removeGeolocation(Geolocation geolocation) {
		getGeolocations().remove(geolocation);
		geolocation.setGeofenceActionBean(null);

		return geolocation;
	}

}