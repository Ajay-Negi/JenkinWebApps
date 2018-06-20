package com.dmi.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.dmi.dao.ISubServiceDAO;
import com.dmi.dao.model.Service;
import com.dmi.dao.model.SubService;
/**
 * 
 * @author ANegi
 *
 */
@Repository
public class SubServiceDAO implements ISubServiceDAO
{
	private static final Logger LOG = Logger.getLogger(SubServiceDAO.class);

	@PersistenceContext
	EntityManager entityManager;
	
	@Override
	public List<SubService> get(Service service)
	{
		List<SubService> subServicesList = null;
		try
		{
			String queryStr = "SELECT ss FROM SubService ss where ss.serviceBean=:service";
			TypedQuery<SubService> query = entityManager.createQuery(queryStr, SubService.class);
			query.setParameter("service", service);
			subServicesList = query.getResultList();
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
			subServicesList = new ArrayList<>();
		}
		return subServicesList;
	}

	@Override
	public SubService get(String subServiceName)
	{
		SubService subService = null;
		try
		{
			String queryStr = "SELECT ss FROM SubService ss where LOWER(ss.name)=:subServiceName";
			TypedQuery<SubService> query = entityManager.createQuery(queryStr, SubService.class);
			query.setParameter("subServiceName", subServiceName.toLowerCase());
			subService = (SubService) query.getSingleResult();
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
		}
		return subService;
	}

	@Override
	public SubService get(Long serviceId, String subServiceName) 
	{
		SubService subService = null;
		try
		{
			String queryStr = "SELECT ss FROM SubService ss where ss.name=:name and ss.serviceBean.id=:id";
			TypedQuery<SubService> query = entityManager.createQuery(queryStr, SubService.class);
			query.setParameter("name", subServiceName);
			query.setParameter("id", serviceId);
			
			subService = query.getSingleResult();
		}
		catch (Exception ex)
		{
			System.out.println(ex.getMessage());
			LOG.error(ex.getMessage(), ex);
		}
		return subService;
	}
	
	@Override
	public SubService get(Long subServiceId)
	{
		SubService subService = null;
		try
		{
			String queryStr = "SELECT ss FROM SubService ss where ss.id=:id";
			Query query = entityManager.createQuery(queryStr);
			query.setParameter("id", subServiceId);
			subService = (SubService) query.getSingleResult();
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
		}
		return subService;
	}

	@Override
	public void save(SubService subService)
	{
		entityManager.persist(subService);
		entityManager.flush();
		entityManager.clear();
		
	}

	@Override
	public void delete(Long subServiceId)
	{
		String queryStr = "DELETE from SubService ss where ss.id=:subServiceId";
		Query query = entityManager.createQuery(queryStr);
		query.setParameter("subServiceId", subServiceId);
		query.executeUpdate();
		
	}


}
