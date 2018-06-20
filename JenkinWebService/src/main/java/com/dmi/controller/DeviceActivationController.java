package com.dmi.controller;

import org.apache.log4j.Logger;
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

import com.dmi.processor.DeviceActivationProcessor;
import com.dmi.security.JWTHelper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 
 * @author ANegi
 *
 */
@Api(tags = "Device Activation")
@RestController
public class DeviceActivationController
{

	private static final Logger LOG = Logger.getLogger(DeviceActivationController.class);
	
	@Autowired
	DeviceActivationProcessor deviceActivationProcessor;

	@ApiOperation(value = "Set status of a device to Activated.", notes="API to change status of a device to Activated ",
			response = String.class)
	@RequestMapping(value = "/device/activation", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> statusChangeToActivated(@ApiParam(value = "Device Id of device whose status needs to be changed", required = true) @RequestParam("deviceId") String deviceId, 
			@ApiParam(value = "JWT authorization token", required = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken)
	{
		try
		{
			Long oemId = new Long(JWTHelper.parseJWTClaim(jwtToken, "oemId"));
			
			JSONObject response = deviceActivationProcessor.statusChangeToActivated(oemId, deviceId);
			JSONObject result = new JSONObject().put("success", true).put("msg", "Device Status set to Activated.")
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

	
	/*@ApiOperation(value = "Check status of a device.", notes="Provide OEM Id and Device Id to get the activation status of the device")
	@RequestMapping(value = "/device/status", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> checkStatus(@RequestParam("oemId") Long oemId,
			@RequestParam("deviceId") String deviceId)
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
		
	}*/
	
/*	@ApiOperation(value = "Set device status to Registered.", notes = "Provide vin id to change its status to Registered")
	@RequestMapping(value = "/device/registration", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> statusChangeToRegistered(@RequestParam("vinId") Long vinId)
	{
		try
		{
			//JSONObject response = deviceActivationProcessor.statusChangeToRegistered(oemId, deviceId);
			JSONObject response = new JSONObject();
			JSONObject result = new JSONObject().put("success", true).put("msg", "Device status set to Registered.")
					.put("response", response);

			return ResponseEntity.ok().body(result.toString());
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(),e);
			JSONObject result = new JSONObject().put("success", false).put("msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
		
	}*/

	/*@ApiOperation(value = "Set status of a device to Registered.")
	@RequestMapping(value = "/device/status/change/registered", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> statusChangeToRegistered(@RequestParam("oemId") Long oemId,
			@RequestParam("deviceId") String deviceId)
	{
		try
		{
			JSONObject response = deviceActivationProcessor.statusChangeToRegistered(oemId, deviceId);
			JSONObject result = new JSONObject().put("success", true).put("msg", "Device status set to Registered.")
					.put("response", response);

			return ResponseEntity.ok().body(result.toString());
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(),e);
			JSONObject result = new JSONObject().put("success", false).put("msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
		
	}*/

	/*@ApiOperation(value = "Set status of a device to under-activation.")
	@RequestMapping(value = "/device/status/change/underActivation", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> statusChangeToUnderActivation(@RequestParam("oemId") Long oemId,
			@RequestParam("deviceId") String deviceId)
	{
		try
		{
			JSONObject response = deviceActivationProcessor.statusChangeToUnderActivation(oemId, deviceId);
			JSONObject result = new JSONObject().put("success", true)
					.put("msg", "Device status set to Under-Activation.").put("response", response);

			return ResponseEntity.ok().body(result.toString());
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(),e);
			JSONObject result = new JSONObject().put("success", false).put("msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
	}
*/
	
}
