package com.dmi.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.dmi.dao.IVinDAO;
import com.dmi.dao.model.Device;
import com.dmi.dao.model.Vin;

@Repository
public class VinDAO implements IVinDAO {
	
	@PersistenceContext
	EntityManager entityManager;
	
	private static final Logger LOG = Logger.getLogger(VinDAO.class);
	
	@Override
	public void save(Vin vinBean) {
		
		entityManager.persist(vinBean);
		entityManager.flush();
		entityManager.clear();
	}

	@Override
	public Vin getByVinId(String vinId) {
		
		Vin vinBean = null;
		
		try{
			
			String queryStr = "SELECT v FROM Vin v where LOWER(v.id) = :vinId";
			TypedQuery<Vin> query = entityManager.createQuery(queryStr, Vin.class);
			query.setParameter("vinId", vinId.toLowerCase());
			vinBean = query.getSingleResult();
			
		}catch(Exception ex){
			
			LOG.error(ex.getMessage(), ex);
		}
		return vinBean;
		
	}

	@Override
	public void delete(String vinId) {
		
		String queryStr = "DELETE from Vin v where LOWER(v.id) = :vinId";
		Query query = entityManager.createQuery(queryStr);
		query.setParameter("vinId", vinId.toLowerCase());
		query.executeUpdate();
		
	}
	
	@Override
	public boolean checkVinAvailability(String vinId)
	{
		String queryStr = "SELECT v FROM Vin v where v.id= :vinId";
		TypedQuery<Device> query = entityManager.createQuery(queryStr, Device.class);
		query.setParameter("vinId", vinId);
		try
		{
			query.getSingleResult();
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
			return true;
		}
		return false;
	}

}
