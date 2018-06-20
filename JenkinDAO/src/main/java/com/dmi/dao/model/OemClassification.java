package com.dmi.dao.model;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;


/**
 * The persistent class for the oem_classification database table.
 * 
 */
@Entity
@Table(name="oem_classification")
@NamedQuery(name="OemClassification.findAll", query="SELECT o FROM OemClassification o")
public class OemClassification implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	private String name;

	//bi-directional many-to-one association to Oem
	@OneToMany(mappedBy="oemClassificationBean")
	@JsonIgnore
	private List<Oem> oems;

	public OemClassification() {
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

	public List<Oem> getOems() {
		return this.oems;
	}

	public void setOems(List<Oem> oems) {
		this.oems = oems;
	}

	public Oem addOem(Oem oem) {
		getOems().add(oem);
		oem.setOemClassificationBean(this);

		return oem;
	}

	public Oem removeOem(Oem oem) {
		getOems().remove(oem);
		oem.setOemClassificationBean(null);

		return oem;
	}

}