package com.dmi.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.dmi.dao.IVhrTemplateDAO;
import com.dmi.dao.model.VhrTemplate;

@Repository
public class VhrTemplateDAO implements IVhrTemplateDAO {


	private static final Logger LOG = Logger.getLogger(VhrTemplateDAO.class);

	@PersistenceContext
	EntityManager entityManager;
	
	@Override
	public VhrTemplate getByDeviceTypeId(Long deviceTypeId) {
		
		VhrTemplate vhrTemplate;
		String queryStr = "SELECT vhr FROM VhrTemplate vhr where vhr.deviceTypeBean.id=:deviceTypeId";
		TypedQuery<VhrTemplate> query = entityManager.createQuery(queryStr, VhrTemplate.class);
		query.setParameter("deviceTypeId", deviceTypeId);
		try
		{
			vhrTemplate = query.getSingleResult();
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
			vhrTemplate = null;
		}
		return vhrTemplate;
	}
	
	@Override
	public VhrTemplate getById(Long id) {
		
		VhrTemplate vhrTemplate;
		String queryStr = "SELECT vhr FROM VhrTemplate vhr where vhr.id=:id";
		TypedQuery<VhrTemplate> query = entityManager.createQuery(queryStr, VhrTemplate.class);
		query.setParameter("id", id);
		try
		{
			vhrTemplate = query.getSingleResult();
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
			vhrTemplate = null;
		}
		return vhrTemplate;
	}

	@Override
	public void delete(Long vhrTemplateId) {
		
		String queryStr = "DELETE from VhrTemplate vhr where vhr.id=:vhrTemplateId";
		Query query = entityManager.createQuery(queryStr);
		query.setParameter("vhrTemplateId", vhrTemplateId);
		query.executeUpdate();
	}

	@Override
	public void save(VhrTemplate vhrTemplate) {

		entityManager.persist(vhrTemplate);
		entityManager.flush();
		entityManager.clear();
	}

	@Override
	public void update(VhrTemplate vhrTemplate) {

		entityManager.merge(vhrTemplate);
		entityManager.flush();
		entityManager.clear();
	}

	@Override
	public List<VhrTemplate> getAllByOemId(Long oemId) {
		
		List<VhrTemplate> vhrTemplateList = new ArrayList<>();
		String queryStr = "SELECT vhr FROM VhrTemplate vhr where vhr.deviceTypeBean.oemBean.id =:oemId";
		TypedQuery<VhrTemplate> query = entityManager.createQuery(queryStr, VhrTemplate.class);
		query.setParameter("oemId", oemId);
		try
		{
			vhrTemplateList = query.getResultList();
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
			vhrTemplateList = null;
		}
		return vhrTemplateList;
	}
	
}
