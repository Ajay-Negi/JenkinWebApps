package com.dmi.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.dmi.dao.IMLModelsTypesDAO;
import com.dmi.dao.model.MLModelsTypes;

/**
 * @author MBansal
 *
 */
@Repository
public class MLModelsTypesDAO implements IMLModelsTypesDAO
{
	@PersistenceContext
	private EntityManager entMgr;

	@Override
	@SuppressWarnings("unchecked")
	public Long getModelTypeIdFromName(String modelTypeName)
	{
		String queryStr = "SELECT m FROM MLModelsTypes m WHERE LOWER(m.modelType)=:modelTypeName";
		Query query = entMgr.createQuery(queryStr);
		query.setParameter("modelTypeName", modelTypeName.toLowerCase());
		List<MLModelsTypes> mlModelTypesList = (List<MLModelsTypes>) query
				.getResultList();
		return mlModelTypesList.get(0).getModelTypeId();
	}

	@Override
	@SuppressWarnings("unchecked")
	public String getModelTypeNameFromTypeId(Long modelTypeId)
	{
		String queryStr = "SELECT m FROM MLModelsTypes m WHERE m.modelTypeId=:modelTypeId";
		Query query = entMgr.createQuery(queryStr);
		query.setParameter("modelTypeId", modelTypeId);
		List<MLModelsTypes> mlModelTypesList = (List<MLModelsTypes>) query
				.getResultList();
		return mlModelTypesList.get(0).getModelType();
	}
}
