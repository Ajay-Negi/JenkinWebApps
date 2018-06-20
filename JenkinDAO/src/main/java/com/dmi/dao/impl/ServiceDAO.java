package com.dmi.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.dmi.dao.IServiceDAO;
import com.dmi.dao.model.DeviceType;
import com.dmi.dao.model.Service;

/**
 * 
 * @author ANegi
 *
 */
@Repository
public class ServiceDAO implements IServiceDAO
{
	private static final Logger LOG = Logger.getLogger(ServiceDAO.class);

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public List<Service> get(DeviceType deviceType, Long oemId)
	{
		List<Service> servicesList = null;
		try
		{
			String queryStr = "SELECT service FROM Service service where service.deviceTypeBean=:deviceType AND service.deviceTypeBean.oemBean.id=:oemId";
			TypedQuery<Service> query = entityManager.createQuery(queryStr, Service.class);
			query.setParameter("deviceType", deviceType);
			query.setParameter("oemId", oemId);
			servicesList = query.getResultList();
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
			servicesList = new ArrayList<>();
		}
		return servicesList;
	}

	@Override
	public Service get(String serviceName)
	{
		Service service = null;
		try
		{
			String queryStr = "SELECT service FROM Service service where LOWER(service.name)= :serviceName";
			TypedQuery<Service> query = entityManager.createQuery(queryStr, Service.class);
			query.setParameter("serviceName", serviceName.toLowerCase());
			service = (Service) query.getSingleResult();
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
		}

		return service;
	}
	
	@Override
	public Service getByServiceNameAndDeviceType(String serviceName, DeviceType deviceType, Long oemId)
	{
		Service service = null;
		try
		{
			String queryStr = "SELECT service FROM Service service where LOWER(service.name)= :serviceName "
					+ "AND service.deviceTypeBean=:deviceType AND service.deviceTypeBean.oemBean.id=:oemId";
			TypedQuery<Service> query = entityManager.createQuery(queryStr, Service.class);
			query.setParameter("serviceName", serviceName.toLowerCase());
			query.setParameter("deviceType", deviceType);
			query.setParameter("oemId", oemId);
			service = (Service) query.getSingleResult();
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
		}

		return service;
	}

	@Override
	public void save(Service service)
	{
		entityManager.persist(service);
		entityManager.flush();
		entityManager.clear();
	}

	@Override
	public void delete(Long serviceId)
	{
		String queryStr = "DELETE from Service service where service.id=:serviceId";
		Query query = entityManager.createQuery(queryStr);
		query.setParameter("serviceId", serviceId);
		query.executeUpdate();
	}

	@Override
	public Service get(Long serviceId)
	{
		Service service = null;
		try
		{
			String queryStr = "SELECT service FROM Service service where service.id=:serviceId";
			TypedQuery<Service> query = entityManager.createQuery(queryStr, Service.class);
			query.setParameter("serviceId", serviceId);
			service = query.getSingleResult();
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
		}

		return service;
	}

}
