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

import com.dmi.processor.VhrProcessor;
import com.dmi.security.JWTHelper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(tags = "VHR Controller")
@RestController
@RequestMapping(value = "/vhr")
public class VhrController {

	
	private static final Logger LOG = Logger.getLogger(VhrController.class);

	@Autowired
	VhrProcessor vhrProcessor;

	
	@ApiOperation(value = "Get on demand VHR for a device", notes="API to provide device Id to get the last month's Vehicle Health Report ")
	@RequestMapping(value = "/onDemandReport", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> getVHROnDemand(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
			@ApiParam(value = "VIN of the device") @RequestParam("vin") String vin,
			@ApiParam(value = "UserId of the user") @RequestParam("userId") Long userId,
			@ApiParam(value = "Month of the year for which VHR report is required eg. December") @RequestParam("month") String month,
			@ApiParam(value = "Year for which VHR is required eg. 2017") @RequestParam("year") String year)
	{
		try
		{
			Long oemId = Long.parseLong(JWTHelper.parseJWTClaim(jwtToken, "oemId"));
			
			vhrProcessor.getOnDemandVhr(vin, userId, month, year);
			JSONObject result = new JSONObject().put("success", true).put("msg", "Device VHR report sent");

			return ResponseEntity.ok().body(result.toString());
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(),e);
			JSONObject result = new JSONObject().put("success", false).put("msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
		
		
	}
	
	@ApiOperation(value = "Get all VHR templates for an OEM", notes="API to get all VHR templates for a OEM ")
	@RequestMapping(value = "/getAll", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> getAllVHRTemplate(
			@ApiParam(value = "JWT authorization token") @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken)
	{
		try
		{
			
			Long oemId = Long.parseLong(JWTHelper.parseJWTClaim(jwtToken, "oemId"));
			
			JSONArray response = vhrProcessor.getAllVhrTemplates(oemId);
			JSONObject result = new JSONObject().put("success", true).put("msg", "VHR templates retrieved.")
					.put("response", response);

			return ResponseEntity.ok().body(result.toString());
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(),e);
			JSONObject result = new JSONObject().put("success", false).put("msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
		
		
	}
	
	// This operation is used from Notification Template API save method
	/*@ApiOperation(value = "Save VHR template for device type", notes="Provide devicetype and template data to save ")
	@RequestMapping(value = "/saveTemplate", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> saveTemplate(@RequestParam("deviceTypeId") Long deviceTypeId,
			@RequestParam("templateName") String templateName,
			@RequestParam("content") String content)
	{
		try
		{
			JSONObject response = deviceActivationProcessor.checkStatus(oemId, deviceId);
			JSONObject result = new JSONObject().put("success", true).put("msg", "Device Status Retreived.")
					.put("response", response);

			return ResponseEntity.ok().body(result.toString());
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(),e);
			JSONObject result = new JSONObject().put("success", false).put("msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
		
		JSONObject result = new JSONObject().put("success", true);
		return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		
	}*/
}
