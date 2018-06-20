package com.dmi.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.dmi.dao.IUserDAO;
import com.dmi.dao.model.Oem;
import com.dmi.dao.model.User;


@Repository
public class UserDAO implements IUserDAO
{

	private static final Logger LOG = Logger.getLogger(UserDAO.class);

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public User getByCredentials(String username, String password)
	{
		User user = null;
		try
		{
			String queryStr = "SELECT user FROM User user where user.username=:username AND user.password=:password ";
			TypedQuery<User> query = entityManager.createQuery(queryStr,User.class);
			query.setParameter("username", username);
			query.setParameter("password", password);
			user = query.getSingleResult();
		}
		catch (Exception ex)
		{
			LOG.info(ex.getMessage());
			user = null;
		}
		return user;
	}

	@Override
	public boolean checkUsernameAvailability(String usernm)
	{
		try
		{
			String queryStr = "SELECT user FROM User user where user.username=:username";
			TypedQuery<User> query = entityManager.createQuery(queryStr,User.class);
			query.setParameter("username", usernm);
			query.getSingleResult();
		}
		catch (Exception ex)
		{
			LOG.info(ex.getMessage());
			return false;
		}
		return true;
	}

	@Override
	public void registerUser(User user)
	{
		entityManager.persist(user);
		entityManager.flush();
		entityManager.clear();
	}

	@Override
	public Long getUserId(String username)
	{
		String queryStr = "select user FROM User user where user.username=:username";
		TypedQuery<User> query = entityManager.createQuery(queryStr,User.class);
		query.setParameter("username", username);
		User user;
		try
		{
			user = query.getSingleResult();
		}
		catch (Exception ex)
		{
			LOG.info(ex.getMessage());
			return null;
		}
		return user.getId();
	}

	@Override
	public User getUser(String userName)
	{
		String queryStr = "select user FROM User user where LOWER(user.username) = :username";
		TypedQuery<User> query = entityManager.createQuery(queryStr,User.class);
		query.setParameter("username", userName.toLowerCase());
		User user;
		try
		{
			user = query.getSingleResult();
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
			return null;
		}
		return user;
	}

	@Override
	public void updateUser(User user)
	{
		entityManager.merge(user);
		entityManager.flush();
		entityManager.clear();
	}

	@Override
	public void updatePassword(Long userId, String password)
	{
		User user = entityManager.find(User.class, userId);
		user.setPassword(password);
		entityManager.merge(user);
		entityManager.flush();
		entityManager.clear();
	}

	@Override
	public User getUserById(Long id)
	{
		String queryStr = "select user FROM User user where user.id=:id";
		TypedQuery<User> query = entityManager.createQuery(queryStr,User.class);
		query.setParameter("id", id);
		User user = null;
		try
		{
			user = query.getSingleResult();
		}
		catch (Exception ex)
		{
			LOG.info(ex.getMessage());
		}
		return user;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getAll(Oem oem)
	{
		List<User> users = null;

		String queryStr = "select user FROM User user where user.oemBean=:oem";
		Query query = entityManager.createQuery(queryStr);
		query.setParameter("oem", oem);
		try
		{
			users = query.getResultList();
		}
		catch (Exception ex)
		{
			LOG.info(ex.getMessage());
		}
		return users;
	}

}
