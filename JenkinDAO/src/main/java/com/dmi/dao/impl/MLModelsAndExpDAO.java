package com.dmi.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.dmi.dao.IMLModelsAndExpDAO;
import com.dmi.dao.model.MLModelsAndExp;


/**
 * @author MBansal
 *
 */
@Repository
public class MLModelsAndExpDAO implements IMLModelsAndExpDAO
{
	@PersistenceContext
	private EntityManager entMgr;

	@Override
	public List<MLModelsAndExp> fetchByOemAndDeviceType(long oemId,
			long deviceTypeId)
	{
		
		String queryStr= "SELECT d FROM MLModelsAndExp d WHERE d.oem.id = :oemId AND d.deviceType.id = :deviceTypeId";
		TypedQuery<MLModelsAndExp> query = entMgr.createQuery(queryStr, MLModelsAndExp.class);
		query.setParameter("deviceTypeId", deviceTypeId);
		query.setParameter("oemId", oemId);
		List<MLModelsAndExp> modelList = query.getResultList();
		
		
		if (modelList.isEmpty())
			return null;
		return modelList;
	}

	@Override
	public void saveModel(MLModelsAndExp mlModelsAndExp)
	{
		entMgr.persist(mlModelsAndExp);
	}

	@Override
	public boolean checkForExistingModelNameForDeviceType(Long oemId,
			Long deviceTypeId, String modelName)
	{
		String sqlQuery = "SELECT d FROM MLModelsAndExp d WHERE d.oem.id = :oemId AND d.deviceType.id = :deviceTypeId AND d.modelName=:modelName";
		TypedQuery<MLModelsAndExp> query = entMgr.createQuery(sqlQuery,
				MLModelsAndExp.class);
		query.setParameter("oemId", oemId);
		query.setParameter("deviceTypeId", deviceTypeId);
		query.setParameter("modelName", modelName);
		List<MLModelsAndExp> modelList = query.getResultList();
		if (modelList.isEmpty())
			return false;
		return true;
	}

	@Override
	public List<MLModelsAndExp> fetchByOem(Long oemId)
	{
		String sqlQuery = "SELECT d FROM MLModelsAndExp d WHERE d.oem.id = :oemId";
		TypedQuery<MLModelsAndExp> query = entMgr.createQuery(sqlQuery,
				MLModelsAndExp.class);
		query.setParameter("oemId", oemId);
		List<MLModelsAndExp> modelList = query.getResultList();
		if (modelList.isEmpty())
			return null;
		return modelList;
	}

	@Override
	public void deleteModelById(Long modelId)
	{
		String sqlQuery = "DELETE FROM MLModelsAndExp d WHERE d.id = :id";
		Query query = entMgr.createQuery(sqlQuery);
		query.setParameter("id", modelId);
		query.executeUpdate();
		entMgr.flush();

	}
}
