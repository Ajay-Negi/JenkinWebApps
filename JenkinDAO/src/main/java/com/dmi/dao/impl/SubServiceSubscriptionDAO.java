package com.dmi.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.dmi.dao.ISubServiceSubscriptionDAO;
import com.dmi.dao.model.ServiceSubscription;
import com.dmi.dao.model.SubService;
import com.dmi.dao.model.SubServiceSubscription;
/**
 * 
 * @author ANegi
 *
 */
@Repository
public class SubServiceSubscriptionDAO implements ISubServiceSubscriptionDAO
{
	private static final Logger LOG = Logger.getLogger(SubServiceSubscriptionDAO.class);

	@PersistenceContext
	EntityManager entityManager;

	@SuppressWarnings("unchecked")
	@Override
	public List<SubServiceSubscription>
			getSubscribedSubServices(ServiceSubscription serviceSubscriptionBean)
	{
		List<SubServiceSubscription> subServicesList = null;
		try
		{
			String queryStr = "SELECT ss FROM SubServiceSubscription ss where ss.serviceSubscription=:serviceSubscriptionBean";
			Query query = entityManager.createQuery(queryStr);
			query.setParameter("serviceSubscriptionBean", serviceSubscriptionBean);
			subServicesList = query.getResultList();
		}
		catch (Exception ex)
		{
			LOG.info(ex.getMessage());
			subServicesList = new ArrayList<>();
		}
		return subServicesList;
	}

	@Override
	public void save(SubServiceSubscription subServiceSubscription)
	{
		entityManager.persist(subServiceSubscription);
		entityManager.flush();
		entityManager.clear();
	}

	@Override
	public void remove(ServiceSubscription serviceSubscriptionBean, SubService subServiceBean)
	{
		try
		{
			String queryStr = "DELETE FROM SubServiceSubscription ss where ss.serviceSubscription=:serviceSubscriptionBean AND ss.subServiceBean=:subServiceBean";
			Query query = entityManager.createQuery(queryStr);
			query.setParameter("serviceSubscriptionBean", serviceSubscriptionBean);
			query.setParameter("subServiceBean", subServiceBean);
			query.executeUpdate();
		}
		catch (Exception ex)
		{
			LOG.info(ex.getMessage());
		}
	}

	@Override
	public SubServiceSubscription get(ServiceSubscription serviceSubscriptionBean,
			SubService subServiceBean)
	{
		SubServiceSubscription subServicesSubscription = null;
		try
		{
			String queryStr = "SELECT ss FROM SubServiceSubscription ss where ss.serviceSubscription=:serviceSubscriptionBean AND ss.subServiceBean=:subServiceBean";
			Query query = entityManager.createQuery(queryStr);
			query.setParameter("serviceSubscriptionBean", serviceSubscriptionBean);
			query.setParameter("subServiceBean", subServiceBean);
			subServicesSubscription = (SubServiceSubscription) query.getSingleResult();
		}
		catch (Exception ex)
		{
			LOG.info(ex.getMessage());
		}
		return subServicesSubscription;
	}

}
