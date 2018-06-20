package com.dmi.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.dmi.dao.IVhrHistoryDAO;
import com.dmi.dao.model.VhrHistory;

@Repository
public class VhrHistoryDAO implements IVhrHistoryDAO {
	
	@PersistenceContext
	EntityManager entityManager;
	
	private static final Logger LOG = Logger.getLogger(VhrHistoryDAO.class);
	
	@Override
	public VhrHistory getByVinMonthYear(String vinId, String month, String year) {
		
		VhrHistory vhrHistoryBean = null;
		
		try{
			
			String queryStr = "SELECT vh FROM VhrHistory vh where LOWER(vh.vhrHistoryPK.vinId) = :vinId AND LOWER(vh.vhrHistoryPK.notificationMonth) = :month "
					+ "AND LOWER(vh.vhrHistoryPK.notificationYear) = :year";
			TypedQuery<VhrHistory> query = entityManager.createQuery(queryStr, VhrHistory.class);
			query.setParameter("vinId", vinId);
			query.setParameter("month", month);
			query.setParameter("year", year);
			
			vhrHistoryBean = query.getSingleResult();
			
		}catch(Exception ex){
			
			LOG.error(ex.getMessage(), ex);
		}
		return vhrHistoryBean;
	}

}
