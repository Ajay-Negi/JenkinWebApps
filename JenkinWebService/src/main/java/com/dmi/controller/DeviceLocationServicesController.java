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

import com.dmi.constant.Constants;
import com.dmi.dto.location.DeviceLocationDTO;
import com.dmi.processor.DeviceLocationServicesProcessor;
import com.dmi.security.JWTHelper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 
 * @author ANegi
 *
 */
@Api(tags = "Device Geolocation Services")
@RestController
@RequestMapping(value = "/geofence")
public class DeviceLocationServicesController
{
	private static final Logger LOG = Logger.getLogger(DeviceLocationServicesController.class);
	
	@Autowired
	DeviceLocationServicesProcessor deviceLocationServicesProcessor;

	@ApiOperation(value = "Check if user is eligible for Geofencing SubService.", notes = "API to check eligibility for geofence subservice")
	@RequestMapping(value = "/eligibility", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> checkEligibility(@ApiParam(value = "JWT authorization token", required = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
			@ApiParam(value = "Device Id for which eligibility needs to be checked", required = true) @RequestParam(required = true, value = "deviceId") String deviceId)
	{
		try
		{
			String userName = JWTHelper.parseJWTClaim(jwtToken, "userName");

			JSONObject response = deviceLocationServicesProcessor.checkEligibility(userName, deviceId,
					Constants.GEOFENCE_SUBSERVICE_NAME);
			JSONObject result = new JSONObject().put("success", true)
					.put("msg", "Device's Geofence Eligibility Retreived.").put("response", response);

			return ResponseEntity.ok().body(result.toString());
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(),e);
			JSONObject result = new JSONObject().put("success", false).put("msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
		
	}

	@ApiOperation(value = "Returns location coordinates of users' devices.", notes = "API to fetch current locations of all registered devices for user, "
			+ "Username will be fetched from the JWT authorization token")
	@RequestMapping(value = "/locateAll", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<String> locateAllDevices(@ApiParam(value = "JWT authorization token", required = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken)
	{
		try
		{
			String username = JWTHelper.parseJWTClaim(jwtToken, "userName");
			DeviceLocationDTO deviceLocationDTO = deviceLocationServicesProcessor.locateAllDevices(username);
			JSONObject result = new JSONObject().put("success", true).put("msg", "Device Locations Retrieved.")
					.put("response", new JSONObject(deviceLocationDTO));
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(),e);
			JSONObject result = new JSONObject().put("success", false).put("msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}

	}

	@ApiOperation(value = "Get Device Geofence.", notes = "API to fetch saved geofence if created for the device")
	@RequestMapping(value = "/get", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> getGeofence(@ApiParam(value = "Device Id for which geofence needs to be fetched", required = true) 
	@RequestParam(required = true, value = "deviceId") String deviceId)
	{
		try
		{
			JSONObject response = deviceLocationServicesProcessor.getGeofence(deviceId);
			JSONObject result = new JSONObject().put("success", true)
					.put("msg", "Device's Geofence Retreived Successfully.").put("response", response);

			return ResponseEntity.ok().body(result.toString());
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(),e);
			JSONObject result = new JSONObject().put("success", false).put("msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
	}

	@ApiOperation(value = "Save/Update Device Geofence." , notes = "API to save geofence or update if geofence already saved for a device")
	@RequestMapping(value = "/save", method = RequestMethod.POST, produces = "application/json", consumes = "application/x-www-form-urlencoded")
	public ResponseEntity<String> saveGeofence(@ApiParam(value = "JWT authorization token", required = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
			@ApiParam(value = "Geofence details in json string format", required = true) @RequestParam(required = true, value = "geoDTO") String geoDTO,
			@ApiParam(value = "Device id for which geofence is saved", required = true) @RequestParam(required = true, value = "deviceId") String deviceId,
			@ApiParam(value = "Geofence action Id eg. On Entry or On Exit", required = true) @RequestParam(required = true, value = "geoFenceAction") Long geoFenceAction)
	{
		try
		{
			String username = JWTHelper.parseJWTClaim(jwtToken, "userName");

			deviceLocationServicesProcessor.saveUpdateGeofence(geoDTO, username, deviceId, geoFenceAction);
			JSONObject result = new JSONObject().put("success", true).put("msg",
					"Device's Geofence Saved Successfully.");

			return ResponseEntity.ok().body(result.toString());
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(),e);
			JSONObject result = new JSONObject().put("success", false).put("msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
		
	}

	@ApiOperation(value = "Delete Device Geofence.", notes = "API for deleting saved geofence for a device")
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<String> deleteGeofence(@ApiParam(value = "JWT authorization token", required = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
			@ApiParam(value = "Device id for which saved geofence needs to be deleted", required = true) @RequestParam(required = true, value = "deviceId") String deviceId)
	{
		try
		{
			String username = JWTHelper.parseJWTClaim(jwtToken, "username");

			deviceLocationServicesProcessor.deleteGeofence(username, deviceId);
			JSONObject result = new JSONObject().put("success", true).put("msg",
					"Device's Geofence Deleted Successfully.");

			return ResponseEntity.ok().body(result.toString());
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(),e);
			JSONObject result = new JSONObject().put("success", false).put("msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/*@ApiOperation(value = "Check if user is eligible for Speed Limit SubService.")
	@RequestMapping(value = "/device/speedLimit/eligibility", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> checkSpeedLimitEligibility(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
			@RequestParam(required = true, value = "deviceId") String deviceId)
	{
		try
		{
			String username = JWTHelper.parseJWTClaim(jwtToken, "username");

			JSONObject response = deviceLocationServicesProcessor.checkEligibility(username, deviceId,
					Constants.SPEEDLIMIT_SUBSERVICE_NAME);
			JSONObject result = new JSONObject().put("success", true)
					.put("msg", "Device's Speed Limit Eligibility Retreived.").put("response", response);

			return ResponseEntity.ok().body(result.toString());
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(),e);
			JSONObject result = new JSONObject().put("success", false).put("msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
		
	}

	@ApiOperation(value = "Get Device SpeedLimit details.")
	@RequestMapping(value = "/device/speedLimit", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> getSpeedLimitDetails(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
			@RequestParam(required = true, value = "deviceId") String deviceId)
	{
		try
		{
			String username = JWTHelper.parseJWTClaim(jwtToken, "username");

			JSONObject response = deviceLocationServicesProcessor.getSpeedLimitDetails(username, deviceId);
			JSONObject result = new JSONObject().put("success", true)
					.put("msg", "Device's SpeedLimit details Retreived Successfully.").put("response", response);

			return ResponseEntity.ok().body(result.toString());
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(),e);
			JSONObject result = new JSONObject().put("success", false).put("msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
	}

	@ApiOperation(value = "Save/Update Device SpeedLimit details.")
	@RequestMapping(value = "/device/speedLimit", method = RequestMethod.POST, produces = "application/json", consumes = "application/x-www-form-urlencoded")
	public ResponseEntity<String> saveSpeedLimitFence(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
			@RequestParam(required = true, value = "deviceId") String deviceId,
			@RequestParam(required = true, value = "speedLimit") int speedLimit)
	{
		try
		{
			String username = JWTHelper.parseJWTClaim(jwtToken, "username");

			deviceLocationServicesProcessor.saveUpdateSpeedLimitDetails(username, deviceId, speedLimit);
			JSONObject result = new JSONObject().put("success", true).put("msg",
					"Device's SpeedLimit details Saved Successfully.");

			return ResponseEntity.ok().body(result.toString());
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(),e);
			JSONObject result = new JSONObject().put("success", false).put("msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
		
	}

	@ApiOperation(value = "Delete Device SpeedLimit details.")
	@RequestMapping(value = "/device/speedLimit", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<String> deleteSpeedLimitDetails(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
			@RequestParam(required = true, value = "deviceId") String deviceId)
	{
		try
		{
			String username = JWTHelper.parseJWTClaim(jwtToken, "username");

			deviceLocationServicesProcessor.deleteSpeedLimitDetails(username, deviceId);
			JSONObject result = new JSONObject().put("success", true).put("msg",
					"Device's SpeedLimit details Deleted Successfully.");

			return ResponseEntity.ok().body(result.toString());
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(),e);
			JSONObject result = new JSONObject().put("success", false).put("msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}

	} */
}
