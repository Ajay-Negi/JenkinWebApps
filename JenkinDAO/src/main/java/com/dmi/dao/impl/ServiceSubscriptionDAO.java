package com.dmi.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.dmi.dao.IServiceSubscriptionDAO;
import com.dmi.dao.model.DeviceRegistration;
import com.dmi.dao.model.Service;
import com.dmi.dao.model.ServiceSubscription;

/**
 * 
 * @author Ajay Negi
 */

@Repository
public class ServiceSubscriptionDAO implements IServiceSubscriptionDAO
{

	private static final Logger LOG = Logger.getLogger(ServiceSubscriptionDAO.class);

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public List<ServiceSubscription> getSubscribedServices(DeviceRegistration deviceRegistration)
	{
		List<ServiceSubscription> subscribedServicesList = null;
		try
		{
			String queryStr = "SELECT ss FROM ServiceSubscription ss where ss.deviceRegistrationBean = :deviceRegistration";
			TypedQuery<ServiceSubscription> query = entityManager.createQuery(queryStr, ServiceSubscription.class);
			query.setParameter("deviceRegistration", deviceRegistration);
			subscribedServicesList = query.getResultList();
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
			subscribedServicesList = new ArrayList<>();
			
		}
		return subscribedServicesList;

	}

	@Override
	public void save(ServiceSubscription serviceSubscription)
	{
		entityManager.persist(serviceSubscription);
		entityManager.flush();
		entityManager.clear();
	}

	@Override
	public ServiceSubscription get(DeviceRegistration deviceRegistration, Service service)
	{
		ServiceSubscription serviceSubscription = null;
		try
		{
			String queryStr = "SELECT ss FROM ServiceSubscription ss where ss.deviceRegistrationBean.id = :deviceRegistrationId AND ss.serviceBean.id = :serviceId";
			TypedQuery<ServiceSubscription> query = entityManager.createQuery(queryStr, ServiceSubscription.class);
			query.setParameter("deviceRegistrationId", deviceRegistration.getId());
			query.setParameter("serviceId", service.getId());
			serviceSubscription = query.getSingleResult();
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
			serviceSubscription=new ServiceSubscription();
		}
		return serviceSubscription;
	}

	@Override
	public void remove(DeviceRegistration deviceRegistration, Service service)
	{
		try
		{
			String queryStr = "DELETE from ServiceSubscription ss where ss.deviceRegistrationBean =:deviceRegistration AND ss.serviceBean=:service";
			TypedQuery<ServiceSubscription> query = entityManager.createQuery(queryStr, ServiceSubscription.class);
			query.setParameter("deviceRegistration", deviceRegistration);
			query.setParameter("service", service);
			
			query.executeUpdate();
			entityManager.flush();
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
			
		}	

	}
	
	@Override
	public void remove(DeviceRegistration deviceRegistration)
	{
		try
		{
			String queryStr = "DELETE from ServiceSubscription ss where ss.deviceRegistrationBean = :deviceRegistration";
			TypedQuery<ServiceSubscription> query = entityManager.createQuery(queryStr, ServiceSubscription.class);
			query.setParameter("deviceRegistration", deviceRegistration);
			
			query.executeUpdate();
			entityManager.flush();
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
			
		}

	}

}
