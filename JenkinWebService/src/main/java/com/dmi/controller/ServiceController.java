package com.dmi.controller;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dmi.dao.model.Service;
import com.dmi.exception.ProcessorException;
import com.dmi.processor.ServiceProcessor;
import com.dmi.security.JWTHelper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 
 * @author ANegi
 *
 */
@Api(tags = "Services")
@RestController
@RequestMapping(value = "/services")
public class ServiceController
{
	
	private static final Logger LOG = Logger.getLogger(ServiceController.class);
	
	@Autowired
	ServiceProcessor serviceProcessor;

	@ApiOperation(value = "Get all available services for a Device Type.", notes="API to fetch services available for provided device type",
			response = Service.class, responseContainer = "List")
	@RequestMapping(value = "/getAll", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> getServices(
			@ApiParam(value = "JWT authorization token") @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
			@ApiParam(value = "Device type id for fetching services") @RequestParam(required = true, value = "deviceTypeId") Long deviceTypeId)
	{
		try
		{			
			Long oemId = Long.parseLong(JWTHelper.parseJWTClaim(jwtToken, "oemId"));
			
			JSONArray servicesList = serviceProcessor.getServices(deviceTypeId, oemId);
			
			JSONObject response = new JSONObject().put("servicesList", servicesList);
			JSONObject result = new JSONObject().put("success", true).put("response", response)
					.put("msg", "Services Retrieved.");
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
		catch (ProcessorException e)
		{
			LOG.error(e.getMessage(), e);
			JSONObject result = new JSONObject().put("success", false).put("msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(),ex);
			JSONObject result = new JSONObject().put("success", false).put("msg", ex.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
	
	}
	
	@ApiOperation(value = "Get all available services for a Device Type except Location based.", 
			notes="API to fetch all services except Location Based Services available for device type",
			response = Service.class, responseContainer = "List")
	@RequestMapping(value = "/getAllExceptGeolocation", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> getServicesExceptGeolocation(
			@ApiParam(value = "JWT authorization token") @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
			@ApiParam(value = "Device type Id") @RequestParam(required = true, value = "deviceTypeId") Long deviceTypeId)
	{
		try
		{			
			Long oemId = Long.parseLong(JWTHelper.parseJWTClaim(jwtToken, "oemId"));
			
			JSONArray servicesList = serviceProcessor.getServicesExceptGeolocation(deviceTypeId, oemId);
			
			JSONObject response = new JSONObject().put("servicesList", servicesList);
			JSONObject result = new JSONObject().put("success", true).put("response", response)
					.put("msg", "Services Retrieved.");
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
		catch (ProcessorException e)
		{
			LOG.error(e.getMessage(), e);
			JSONObject result = new JSONObject().put("success", false).put("msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(),ex);
			JSONObject result = new JSONObject().put("success", false).put("msg", ex.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
	
	}
	
	@ApiOperation(value = "Save a service for a Device Type.", notes = "API to save a new service for a device type")
	@RequestMapping(value = "/save", method = RequestMethod.POST, produces = "application/json", consumes = "application/x-www-form-urlencoded")
	public ResponseEntity<String> saveService(
			@ApiParam(value = "JWT authorization token") @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
			@ApiParam(value = "Device type id") @RequestParam(required = true, value = "deviceTypeId") Long deviceTypeId,
			@ApiParam(value = "New Service Name") @RequestParam(required = true, value = "serviceName") String serviceName)
	{
		try
		{
			String roleCode = JWTHelper.parseJWTClaim(jwtToken, "roleCode");
			Long oemId = Long.parseLong(JWTHelper.parseJWTClaim(jwtToken, "oemId"));
			
			serviceProcessor.saveService(deviceTypeId,roleCode,serviceName, oemId);
			
			JSONObject result = new JSONObject().put("success", true).put("msg", "New service saved.");
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
		catch (ProcessorException ex)
		{
			LOG.error(ex.getMessage(), ex);
			JSONObject result = new JSONObject().put("success", false).put("msg", ex.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(), e);
			JSONObject result = new JSONObject().put("success", false).put("msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
		
		
	}
	
	@ApiOperation(value = "Delete a service for serviceId.", notes = "API to delete service for provided service id")
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<String>
			deleteService(
					@ApiParam(value = "JWT authorization token") @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
					@ApiParam(value = "Service Id which needs to be deleted") @RequestParam(required = true, value = "serviceId") Long serviceId)
	{
		try
		{
			String roleCode = JWTHelper.parseJWTClaim(jwtToken, "roleCode");
			
			serviceProcessor.deleteService(roleCode,serviceId);
			
			JSONObject result = new JSONObject().put("success", true).put("msg", "Service deleted.");
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
		catch (ProcessorException ex)
		{
			LOG.error(ex.getMessage(), ex);
			JSONObject result = new JSONObject().put("success", false).put("msg", ex.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(),e);
			JSONObject result = new JSONObject().put("success", false).put("msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
	}
}
