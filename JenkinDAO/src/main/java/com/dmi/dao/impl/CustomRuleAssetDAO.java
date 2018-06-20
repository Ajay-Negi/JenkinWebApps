package com.dmi.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.dmi.dao.ICustomRuleAssetDAO;
import com.dmi.dao.model.CustomRuleAsset;

@Repository
public class CustomRuleAssetDAO implements ICustomRuleAssetDAO{

	private static final Logger LOG = Logger.getLogger(CustomRuleAssetDAO.class);
	
	@PersistenceContext
	EntityManager entityManager;
	
	@Override
	public void save(CustomRuleAsset cra) {
		
		try{
			entityManager.persist(cra);
		}
		catch(DataAccessException ex){
			
			LOG.error(ex);
			
		}
	}
	
	@Override
	public CustomRuleAsset getByDeviceTypeId(long id) {

		CustomRuleAsset cra = null;
		String queryString = "select  cra FROM CustomRuleAsset cra WHERE cra.deviceTypeBean.id = :deviceTypeId";

		TypedQuery<CustomRuleAsset> query = entityManager.createQuery(queryString, CustomRuleAsset.class);
		query.setParameter("deviceTypeId", id);

		try {
			cra = query.getSingleResult();
		} catch (Exception ex) {
			LOG.error(ex);
		}

		return cra;
	}

	@Override
	public CustomRuleAsset getCRAByDeviceTypeAndModelName(long deviceTypeId, String customMessageModelName) {
		
		CustomRuleAsset cra = null;
		String queryString = "select  cra FROM CustomRuleAsset cra WHERE cra.deviceTypeBean.id = :deviceTypeId AND cra.messageModelName=:name";

		TypedQuery<CustomRuleAsset> query = entityManager.createQuery(queryString, CustomRuleAsset.class);
		query.setParameter("deviceTypeId", deviceTypeId);
		query.setParameter("name", customMessageModelName);

		try {
			cra = query.getSingleResult();
		} catch (Exception ex) {
			LOG.error(ex);
		}

		return cra;
	}

	@Override
	public List<CustomRuleAsset> getCRAByDeviceTypeId(long deviceTypeId) {
		
		List<CustomRuleAsset> craList = null;
		String queryString = "select  cra FROM CustomRuleAsset cra WHERE cra.deviceTypeBean.id = :deviceTypeId";

		TypedQuery<CustomRuleAsset> query = entityManager.createQuery(queryString, CustomRuleAsset.class);
		query.setParameter("deviceTypeId", deviceTypeId);

		try {
			craList = query.getResultList();
		} catch (Exception ex) {
			LOG.error(ex);
		}

		return craList;
	}
	
	@Override
	public CustomRuleAsset getCRAByModelName(String customMessageModelName) {
		
		CustomRuleAsset cra = null;
		String queryString = "select  cra FROM CustomRuleAsset cra WHERE cra.messageModelName=:name";

		TypedQuery<CustomRuleAsset> query = entityManager.createQuery(queryString, CustomRuleAsset.class);
		query.setParameter("name", customMessageModelName);

		try {
			cra = query.getSingleResult();
		} catch (Exception ex) {
			LOG.error(ex);
		}

		return cra;
	}

}
