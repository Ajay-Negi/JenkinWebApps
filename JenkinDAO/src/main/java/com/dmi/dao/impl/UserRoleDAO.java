package com.dmi.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import com.dmi.dao.IUserRoleDAO;
import com.dmi.dao.model.Role;

public class UserRoleDAO implements IUserRoleDAO {
	
	private static final Logger LOG = Logger.getLogger(UomDAO.class);
	
	@PersistenceContext
	EntityManager entityManager;
	
	@Override
	public Role get(String roleName)
	{
		Role role = null;
		try
		{
			String queryStr = "SELECT role FROM Role role where LOWER(role.alias) = : roleName";
			TypedQuery<Role> query = entityManager.createQuery(queryStr, Role.class);
			query.setParameter("roleName", roleName.toLowerCase());
			role = query.getSingleResult();
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
			return role;
		}
		return role;
	}


}
