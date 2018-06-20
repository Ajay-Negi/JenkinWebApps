package com.dmi.controller;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dmi.dto.ConnectorEndpointDTO;
import com.dmi.exception.ProcessorException;
import com.dmi.exception.ValidationException;
import com.dmi.processor.ConnectorEndpointProcessor;
import com.dmi.security.JWTHelper;


@RestController
public class ConnectorController
{
	
	private static final Logger LOG = Logger.getLogger(ConnectorController.class);

	@Autowired
	public ConnectorEndpointProcessor connectorEndpointProcessor;

	@RequestMapping(value = "/saveNotificationEndpoint", method =
	{ RequestMethod.PUT })
	public ResponseEntity<String> saveEndpoint(
			@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
			@RequestBody String data)
	{

		try
		{

			ConnectorEndpointDTO dto = new ConnectorEndpointDTO();
			JSONObject incomingData = new JSONObject(data);
			if (incomingData.getString("type").equalsIgnoreCase("REST"))
			{
				dto.setRestServiceName(incomingData.getString("name"));
				dto.setRestServiceUrl(incomingData.getString("url"));
			}
			else if (incomingData.getString("type").equalsIgnoreCase("JMS"))
			{
				dto.setJmsqname(incomingData.getString("name"));
				dto.setJmsurl(incomingData.getString("url"));
			}

			String userName = JWTHelper.parseJWTClaim(jwtToken, "userName");

			boolean saved = connectorEndpointProcessor.saveEndpoint(dto,userName);

			JSONObject result = new JSONObject().put("success", true).put(
					"msg", "Notification End Point Saved.");
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());

		}
		catch (ValidationException e)
		{
			LOG.error(e.getMessage(), e);
			JSONObject result = new JSONObject().put("success", false).put(
					"msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					result.toString());
		}
		catch (ProcessorException e)
		{
			LOG.error(e.getMessage(), e);
			JSONObject result = new JSONObject().put("success", false).put(
					"msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.CONFLICT).body(
					result.toString());
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(), e);
			JSONObject result = new JSONObject().put("success", false).put(
					"msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.CONFLICT).body(
					result.toString());
		}
	}

	@RequestMapping(value = "/getNotificationEndPoints", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> getNotificationEndpoints(
			@RequestHeader(HttpHeaders.AUTHORIZATION) String token)
	{
		JSONObject response = new JSONObject();

		try
		{
			JSONArray connectorList = null;
			Long userId = Long.parseLong(JWTHelper.parseJWTClaim(token, "sub"));
			try
			{
				connectorList = connectorEndpointProcessor.getConnectorListByUserId(userId);
				if (connectorList == null)
					throw new Exception("Failed to fetch connector list.");
			}
			catch (Exception e)
			{
				LOG.error(e.getMessage(), e);
			}

			response.put("success", true)
					.put("msg", "Opertaion completed successfully.")
					.put("connectorList", connectorList);
			return ResponseEntity.ok().body(response.toString());

		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
			response.put("success", false).put("msg", ex.getMessage());
			return ResponseEntity.ok().body(response.toString());
		}
	}

	@RequestMapping(value = "/deleteNotificationEndPoint", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<String> deleteNotificationEndpoints(
			@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
			@RequestParam(value = "id", required = true) int endpointId)
	{

		JSONObject response = new JSONObject();

		try
		{

			Long userId = Long.parseLong(JWTHelper.parseJWTClaim(token, "sub"));
			connectorEndpointProcessor.deleteEndpoint(userId, endpointId);

			response.put("success", true).put("msg",
					"Connector deleted successfully.");
			return ResponseEntity.ok().body(response.toString());

		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
			response.put("success", false).put("msg", ex.getMessage());
			return ResponseEntity.ok().body(response.toString());
		}
	}
}
