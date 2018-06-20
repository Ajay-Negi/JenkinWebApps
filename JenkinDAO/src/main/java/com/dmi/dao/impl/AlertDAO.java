package com.dmi.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.dmi.dao.IAlertDAO;
import com.dmi.dao.model.Alert;


/**
 * 
 * @author ANegi
 *
 */
@Repository
public class AlertDAO implements IAlertDAO {

	@PersistenceContext
	EntityManager entityManager;
	
	private static final Logger LOG = Logger.getLogger(AlertDAO.class);
	
	@Override
	public void save(Alert deviceAlertBean) {
		
		entityManager.persist(deviceAlertBean);
		entityManager.flush();
		entityManager.clear();
	}

	@Override
	public List<Alert> getByDeviceId(String deviceId) {
		
		List<Alert> deviceAlertList = null;
		
		try{
			
			String queryStr = "SELECT alert FROM Alert alert where LOWER(alert.deviceId) = :deviceId";
			TypedQuery<Alert> query = entityManager.createQuery(queryStr, Alert.class);
			query.setParameter("deviceId", deviceId.toLowerCase());
			deviceAlertList = query.getResultList();
			
		}catch(Exception ex){
			
			LOG.error(ex.getMessage(), ex);
		}
		return deviceAlertList;
		
	}

	@Override
	public void delete(String deviceId) {
		
		String queryStr = "DELETE from Alert alert where LOWER(alert.deviceId)= :id";
		Query query = entityManager.createQuery(queryStr);
		query.setParameter("id", deviceId.toLowerCase());
		query.executeUpdate();
		
	}
	
	@Override
	public long getAlertsCount(String deviceId) {
		
		long alertCount = 0;
		try{
			
			String queryStr = "SELECT COUNT(alert) FROM Alert alert where LOWER(alert.deviceBean.id) = :deviceId";
			Query query = entityManager.createQuery(queryStr);
			query.setParameter("deviceId", deviceId.toLowerCase());
			alertCount = (long) query.getSingleResult();
			
		}catch(Exception ex){
			
			LOG.error(ex.getMessage(), ex);
		}
		
		return alertCount;
		
		
	}

}
