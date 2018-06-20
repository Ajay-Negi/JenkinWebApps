package com.dmi.dao;

import java.util.List;

import com.dmi.dao.model.ConnectorEndpoint;


public interface IConnectorEndpointDAO {

	public void save(ConnectorEndpoint connectorEndpoint);
	
	public List<ConnectorEndpoint> list();
	
	public List<ConnectorEndpoint> getConnectorListByUserId(long userId);

	public void deleteConnector(ConnectorEndpoint entity); 

}
