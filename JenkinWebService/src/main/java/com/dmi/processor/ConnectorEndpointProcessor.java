package com.dmi.processor;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dmi.dao.IConnectorEndpointDAO;
import com.dmi.dao.IUserDAO;
import com.dmi.dao.model.ConnectorEndpoint;
import com.dmi.dao.model.User;
import com.dmi.dto.ConnectorEndpointDTO;
import com.dmi.exception.ProcessorException;
import com.dmi.utils.DateTimeUtils;


@Service
public class ConnectorEndpointProcessor
{
	
	private static final Logger LOG = Logger.getLogger(ConnectorEndpointProcessor.class);

	@Autowired
	IConnectorEndpointDAO notificationEndpointDAO;

	@Autowired
	IUserDAO userDAO;

	@Transactional
	public boolean saveEndpoint(ConnectorEndpointDTO connectorEndpointVO,
			String userName) throws ProcessorException
	{

		ConnectorEndpoint connectorEntity = new ConnectorEndpoint();

		if (connectorEndpointVO.getJmsqname() != null
				&& connectorEndpointVO.getJmsurl() != null)
		{
			connectorEntity.setJmsUrl(connectorEndpointVO.getJmsurl());
			connectorEntity.setJmsQName(connectorEndpointVO.getJmsqname());
			connectorEntity.setNotificationType("JMS");

		}
		else if (connectorEndpointVO.getRestServiceUrl() != null
				&& connectorEndpointVO.getRestServiceName() != null)
		{
			connectorEntity.setRestServiceName(connectorEndpointVO.getRestServiceName());
			connectorEntity.setRestServiceUrl(connectorEndpointVO.getRestServiceUrl());
			connectorEntity.setNotificationType("REST");

		}

		try
		{
			User portalUserObj = userDAO.getUser(userName);

			if (portalUserObj != null)
			{
				connectorEntity.setUserId(portalUserObj.getId());
				connectorEntity.setCreatedById(portalUserObj.getUsername());
				connectorEntity.setCreatedTimeStamp(new Date());
				connectorEntity.setUpdatedTimeStamp(new Date());
				notificationEndpointDAO.save(connectorEntity);
			}
			return true;
		}
		catch (DataAccessException ex)
		{
			LOG.error(ex.getMessage(), ex);
			return false;
		}
	}

	public JSONArray getConnectorListByUserId(Long userId)
	
	{

		List<ConnectorEndpoint> connectorEntityList = null;

		try
		{
			connectorEntityList = notificationEndpointDAO
					.getConnectorListByUserId(userId);

			JSONArray jArray = new JSONArray();
			if (connectorEntityList != null)
			{
				for (ConnectorEndpoint connectorObj : connectorEntityList)
				{
					JSONObject jObj = new JSONObject();

					if (connectorObj.getJmsQName() != null
							&& connectorObj.getJmsUrl() != null)
					{
						jObj.put("name", connectorObj.getJmsQName());
						jObj.put("url", connectorObj.getJmsUrl());
					}
					if (connectorObj.getRestServiceUrl() != null)
					{
						jObj.put("name", connectorObj.getRestServiceName());
						jObj.put("url", connectorObj.getRestServiceUrl());
					}

					jObj.put("type", connectorObj.getNotificationType());
					jObj.put("id", connectorObj.getId());
					jObj.put("updatedTimeStamp", DateTimeUtils.convertToCommonTimeFormat(connectorObj.getUpdatedTimeStamp()));

					jArray.put(jObj);
				}
			}
			return jArray;
		}
		catch (DataAccessException ex)
		{
			LOG.error(ex.getMessage(), ex);
			return null;
		}

	}

	@Transactional
	public void deleteEndpoint(Long userId, int endpointId)
			throws ProcessorException
	{
		try
		{

			ConnectorEndpoint entity = new ConnectorEndpoint();
			entity.setUserId(userId);
			entity.setId(endpointId);

			notificationEndpointDAO.deleteConnector(entity);

		}
		catch (DataAccessException ex)
		{
			LOG.error(ex.getMessage(), ex);
		}
	}

}
