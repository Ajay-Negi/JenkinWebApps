package com.dmi.dao.model;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;


/**
 * The persistent class for the user_status database table.
 * 
 */
@Entity
@Table(name="user_status")
@NamedQuery(name="UserStatus.findAll", query="SELECT u FROM UserStatus u")
public class UserStatus implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	private String alias;

	//bi-directional many-to-one association to User
	@OneToMany(mappedBy="userStatusBean")
	@JsonIgnore
	private List<User> users;

	public UserStatus() {
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

	public List<User> getUsers() {
		return this.users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public User addUser(User user) {
		getUsers().add(user);
		user.setUserStatusBean(this);

		return user;
	}

	public User removeUser(User user) {
		getUsers().remove(user);
		user.setUserStatusBean(null);

		return user;
	}

}