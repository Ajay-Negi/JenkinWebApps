package com.dmi.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.dmi.dao.IConnectorEndpointDAO;
import com.dmi.dao.model.ConnectorEndpoint;



@Repository
public class ConnectorEndpointDAO implements IConnectorEndpointDAO {

	@PersistenceContext
	private EntityManager entMgr;

	@Override
	public void save(ConnectorEndpoint notificationEndpoint) {
		entMgr.persist(notificationEndpoint);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ConnectorEndpoint> list() {
		Query query = entMgr.createNamedQuery("ConnectorEndpoint.findAll");
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ConnectorEndpoint> getConnectorListByUserId(long userId) {
		String queryStr = "SELECT d FROM ConnectorEndpoint d where d.userId=:userId";
		Query query = entMgr.createQuery(queryStr);
		query.setParameter("userId", userId);
		return query.getResultList();
	}

	@Override
	public void deleteConnector(ConnectorEndpoint entity) {
		String queryStr = "DELETE from ConnectorEndpoint ce where ce.id=:endpointId AND ce.userId =:userId";
		Query query = entMgr.createQuery(queryStr);

		query.setParameter("endpointId", entity.getId());
		query.setParameter("userId", entity.getUserId());
		query.executeUpdate();
		entMgr.flush();
	}

}
