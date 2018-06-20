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

import com.dmi.dao.model.SubService;
import com.dmi.processor.SubServiceProcessor;
import com.dmi.security.JWTHelper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 
 * @author ANegi
 *
 */
@Api(tags = "Sub Services")
@RestController
@RequestMapping(value = "/subServices")
public class SubServiceController
{
	
	private static final Logger LOG = Logger.getLogger(SubServiceController.class);
	
	@Autowired
	SubServiceProcessor subServiceProcessor;

	@ApiOperation(value = "Get all available sub-services for a Service.", notes="APi to fetch all name and id of all Subservices under a Service",
			response = SubService.class, responseContainer = "List")
	@RequestMapping(value = "/getAll", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> getServices(
			@ApiParam(value = "JWT authorization token") @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
			@ApiParam(value = "Service id for which needs to fetch subservices") @RequestParam(required = true, value = "serviceId") Long serviceId)
	{
		try
		{			
			JSONArray servicesList = subServiceProcessor.getSubServices(serviceId);
			
			JSONObject response = new JSONObject().put("subServicesList", servicesList);
			JSONObject result = new JSONObject().put("success", true).put("response", response)
					.put("msg", "Sub-Services Retrieved.");
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(),e);
			JSONObject result = new JSONObject().put("success", false).put("msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
	}
	
	@ApiOperation(value = "Save a sub-service for a Service.", notes = "API to save new Subservice under a existing Service")
	@RequestMapping(value = "/save", method = RequestMethod.POST, produces = "application/json", consumes = "application/x-www-form-urlencoded")
	public ResponseEntity<String> saveService(
			@ApiParam(value = "JWT authorization token") @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
			@ApiParam(value = "Service Id under which new Subservice is created") @RequestParam(required = true, value = "serviceId") Long serviceId,
			@ApiParam(value = "Name of new SubService") @RequestParam(required = true, value = "subServiceName") String subServiceName)
	{
		try
		{
			String roleCode = JWTHelper.parseJWTClaim(jwtToken, "roleCode");
			
			subServiceProcessor.saveSubService(serviceId,roleCode,subServiceName);
			
			JSONObject result = new JSONObject().put("success", true).put("msg", "New sub-service saved.");
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(),e);
			JSONObject result = new JSONObject().put("success", false).put("msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
		
	}
	
	@ApiOperation(value = "Delete a sub-service for a Service.", notes="API to delete Subservice for provided Subservice id")
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<String> deleteService(
			@ApiParam(value = "JWT authorization token") @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
			@ApiParam(value = "Subservice Id which needs to be deleted") @RequestParam(required = true, value = "subServiceId") Long subServiceId)
	{
		try
		{
			String roleCode = JWTHelper.parseJWTClaim(jwtToken, "roleCode");
			
			subServiceProcessor.deleteSubService(roleCode,subServiceId);
			
			JSONObject result = new JSONObject().put("success", true).put("msg", "Sub-service deleted.");
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
