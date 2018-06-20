package com.dmi.dao;

import java.util.List;

import com.dmi.dao.model.Oem;
import com.dmi.dao.model.User;

public interface IUserDAO
{
	User getByCredentials(String usernm, String password);

	boolean checkUsernameAvailability(String usernm);

	void registerUser(User user);

	Long getUserId(String username);

	User getUser(String username);

	User getUserById(Long id);

	void updateUser(User user);

	void updatePassword(Long id, String password);

	List<User> getAll(Oem oem);
}
