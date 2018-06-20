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

import com.dmi.processor.DeviceTypeProcessor;
import com.dmi.security.JWTHelper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 
 * @author Ajay Negi
 */
@Api(tags = "Device Type")
@RestController
@RequestMapping(value = "/deviceTypes")
public class DeviceTypeController
{
	private static final Logger LOG = Logger.getLogger(DeviceTypeController.class);
	
	@Autowired
	DeviceTypeProcessor deviceTypeProcessor;

	@ApiOperation(value = "Get all available device types", notes = "API to fetch all device types in OEM")
	@RequestMapping(value = "/getAll", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> getServices(
			@ApiParam(value = "JWT authorization token", required = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken)
	{
		try
		{		
			Long oemId = Long.parseLong(JWTHelper.parseJWTClaim(jwtToken, "oemId"));
			JSONArray deviceTypesList = deviceTypeProcessor.getDeviceTypes(oemId);
			
			JSONObject response = new JSONObject().put("deviceTypesList", deviceTypesList);
			JSONObject result = new JSONObject().put("success", true).put("response", response)
					.put("msg", "Device Types list Retrieved.");
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(),e);
			JSONObject result = new JSONObject().put("success", false).put("msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
	
	}
	
	@ApiOperation(value = "Save a device Type", notes = "API to save a new device type for an OEM")
	@RequestMapping(value = "/save", method = RequestMethod.POST, produces = "application/json", consumes = "application/x-www-form-urlencoded")
	public ResponseEntity<String> saveService(
		@ApiParam(value = "JWT authorization token", required = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
		@ApiParam(value = "Name for new device type", required = true) @RequestParam(required = true, value = "deviceTypeName") String deviceTypeName,
		@ApiParam(value = "Metadata in json string format for new device type", required = true) @RequestParam(required = true, value = "metadata") String metadata,
		@ApiParam(value = "URL for new device type logo", required = true) @RequestParam(required = true, value = "logoUrl") String logoUrl)
	{
		try
		{
			Long oemId = Long.parseLong(JWTHelper.parseJWTClaim(jwtToken, "oemId"));
			String roleCode = JWTHelper.parseJWTClaim(jwtToken, "roleCode");
			
			deviceTypeProcessor.saveDeviceType(oemId,roleCode,deviceTypeName, metadata, logoUrl);
			
			JSONObject result = new JSONObject().put("success", true).put("msg", "New device type saved.");
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(),e);
			JSONObject result = new JSONObject().put("success", false).put("msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
		
	}
	
	@ApiOperation(value = "Delete a device type", notes = "API to delete device type for provided deviceType Id")
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<String> deleteService(
			@ApiParam(value = "JWT authorization token", required = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
			@ApiParam(value = "Id of device type which needs to be deleted", required = true) @RequestParam(required = true, value = "deviceTypeId") Long deviceTypeId)
	{
		try
		{
			String roleCode = JWTHelper.parseJWTClaim(jwtToken, "roleCode");
			
			deviceTypeProcessor.deleteDeviceType(roleCode,deviceTypeId);
			
			JSONObject result = new JSONObject().put("success", true).put("msg", "Device Type deleted.");
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
