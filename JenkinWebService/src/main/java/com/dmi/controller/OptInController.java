package com.dmi.controller;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dmi.dto.optin.OptInDTO;
import com.dmi.processor.OptInProcessor;
import com.dmi.security.JWTHelper;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 
 * @author Ajay Negi
 *
 */
@Api(tags = "Service and Sub-Service Preferences")
@RestController
@RequestMapping(value = "/preferences")
public class OptInController
{
	
	private static final Logger LOG = Logger.getLogger(OptInController.class);
	
	@Autowired
	public OptInProcessor optInProcessor;

	@ApiOperation(value = "Get a list of all subscribed services and sub-services for a particular Device.", 
			notes = "API to fetch offered services for a device.Provide device id for getting subscribed services and subservices as json array", response = OptInDTO.class)
	@RequestMapping(value = "/subscribed/{deviceId}", method = RequestMethod.GET)
	public ResponseEntity<String> getCustomer(@ApiParam(value = "JWT authorization token") @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
			@ApiParam(value = "Device id for which offered services and subservices needs to be fetched") @PathVariable(value = "deviceId") String deviceId)
	{
		try
		{
			Long oemId = Long.parseLong(JWTHelper.parseJWTClaim(jwtToken, "oemId"));
			
			OptInDTO optInDTO = optInProcessor.getOfferings(deviceId, oemId);
			ObjectMapper mapper = new ObjectMapper();
			String jsonOptInDTO = mapper.writeValueAsString(optInDTO);

			JSONObject response = new JSONObject().put("success", true).put("msg", "Offerings Retreived.")
					.put("response", new JSONObject(jsonOptInDTO));
			return ResponseEntity.ok().body(response.toString());
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(),e);
			JSONObject result = new JSONObject().put("success", false).put("msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}

	}

	@ApiOperation(value = "Save service and sub-service preferences for a particular device.", 
			notes = "API to save services and subservices subscription. Provide details as per OptinDTO in json format to save the service preferences")
	@RequestMapping(value = "/save", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<String> saveServicePrefereneces(
			@ApiParam(value = "JWT authorization token") @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
			@ApiParam(value = "JSON string to providee service and subservice details") @RequestBody OptInDTO optInDTO)
	{
		try
		{
			String userName = JWTHelper.parseJWTClaim(jwtToken, "userName");
			Long oemId = Long.parseLong(JWTHelper.parseJWTClaim(jwtToken, "oemId"));

			optInProcessor.saveServicePreferences(userName, oemId, optInDTO);

			JSONObject response = new JSONObject().put("success", true).put("msg", "Service Preferences Updated.");
			return ResponseEntity.ok().body(response.toString());
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(),e);
			JSONObject result = new JSONObject().put("success", false).put("msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
	}
}
