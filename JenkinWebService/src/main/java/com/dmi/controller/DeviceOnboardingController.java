package com.dmi.controller;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dmi.constant.Constants;
import com.dmi.processor.DeviceOnboardingProcessor;
import com.dmi.processor.OemProcessor;
import com.dmi.security.JWTHelper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 
 * @author Ajay Negi
 *
 */
@Api(tags = "Device Onboarding")
@RestController
@RequestMapping(value = "/device")
public class DeviceOnboardingController
{
	private static final Logger LOG = Logger.getLogger(DeviceOnboardingController.class);
	
	@Autowired
	DeviceOnboardingProcessor deviceOnboardingProcessor;
	
	@Autowired
	OemProcessor oemProcessor;
	

	@ApiOperation(value = "Get device details by Device Id", notes = "API to fetch details of a registered device")
	@RequestMapping(value = "/getById/{deviceId}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> getRegisteredDevice( @ApiParam(value = "Device id of which details needs to fetched", required = true) @PathVariable(value="deviceId") String deviceId)
	{
		JSONObject response;
		try
		{
			response = deviceOnboardingProcessor.getRegisteredDeviceById(deviceId);
			JSONObject result = new JSONObject().put("success", true).put("response", response)
					.put("msg", "Device(s) details Retrieved.");
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(),e);
			JSONObject result = new JSONObject().put("success", false).put("msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
	}

	@ApiOperation(value = "Get all registered devices", 
			notes = "API to get all registered devices for a user of an oem, User name will be fetched from authorization token")
	@RequestMapping(value = "/getAll", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> getRegisteredDevices(
			@ApiParam(value = "JWT authorization token", required = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken)
	{
		JSONObject response;
		try
		{
			String userName = JWTHelper.parseJWTClaim(jwtToken, "userName");
			String roleCode = JWTHelper.parseJWTClaim(jwtToken, "roleCode");
			Long oemId = Long.parseLong(JWTHelper.parseJWTClaim(jwtToken, "oemId"));
			
			JSONArray devicesList = new JSONArray();
			
			if(roleCode.equalsIgnoreCase(Constants.USER_ROLE_CODE_ADMIN))
			{
				devicesList = deviceOnboardingProcessor.getRegisteredDevicesForOem(oemId);
				
			}
			else{
				
				devicesList = deviceOnboardingProcessor.getRegisteredDevicesForUser(userName);
			}
			
			
			response = new JSONObject().put("devicesList", devicesList);

			JSONObject result = new JSONObject().put("success", true).put("response", response)
					.put("msg", "Device(s) details Retrieved.");
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(),e);
			JSONObject result = new JSONObject().put("success", false).put("msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
	} 
	
	
	@ApiOperation(value = "Get all registered devices by device type", notes = "API to fetch all registered devices for an oem by DeviceTypeId")
	@RequestMapping(value = "/getAllByDeviceTypeId", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> getRegisteredDevices(
		@ApiParam(value = "JWT authorization token", required = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
		@ApiParam(value = "Id of device type of which type devices needs to be fetched ", required = true) @RequestParam(required = true, value = "deviceTypeId") Long deviceTypeId)
	{
		JSONObject response;
		try
		{
			Long oemId = Long.parseLong(JWTHelper.parseJWTClaim(jwtToken, "oemId"));
			
			JSONArray devicesList = new JSONArray();
			
			devicesList = deviceOnboardingProcessor.getRegisteredDevicesForOemByDeviceTypeId(oemId, deviceTypeId);
			
			response = new JSONObject().put("devicesList", devicesList);

			JSONObject result = new JSONObject().put("success", true).put("response", response)
					.put("msg", "Device(s) details Retrieved.");
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(),e);
			JSONObject result = new JSONObject().put("success", false).put("msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
	} 
	
	
	
	@ApiOperation(value = "Get all registered devices cumulative data", notes = "Get all registered devices cumulative data irrespective of user as this is for admin")
	@RequestMapping(value = "/getCumulativeDataforAllDevices", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> getCumulativeDataforAllDevices(@ApiParam(value = "JWT authorization token", required = true) 
	@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken)
	{
		JSONObject response;
		try
		{
			Long oemId = Long.parseLong(JWTHelper.parseJWTClaim(jwtToken, "oemId"));
			
			JSONArray devicesList = deviceOnboardingProcessor.getCumulativeDataForAllDevices(oemId);
			response = new JSONObject().put("devicesList", devicesList);

			JSONObject result = new JSONObject().put("success", true).put("response", response)
					.put("msg", "Device(s) cumulative data retrieved.");
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(),e);
			JSONObject result = new JSONObject().put("success", false).put("msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
	}
	
	
	@ApiOperation(value = "Get all registered devices for all users in an OEM", notes="API to get fetch mapping of all devices corresponding to all users")
	@RequestMapping(value = "/getAllUserDeviceMapping", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> getAllUserDeviceMapping(@ApiParam(value = "Oem name for which users and devices mapping is required", required = true) 
	@RequestParam(required = true, value = "oemName") String oemName)
	{
		
		
		JSONObject response;
		try
		{
			
			JSONArray deviceUserList = deviceOnboardingProcessor.getDeviceUserMapping(oemName);
			response = new JSONObject().put("devicesList", deviceUserList);

			JSONObject result = new JSONObject().put("success", true).put("response", response)
					.put("msg", "Device(s) details Retrieved.");
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(),e);
			JSONObject result = new JSONObject().put("success", false).put("msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
	}
	
	@ApiOperation(value = "Get all registered devices for a user.", notes = "API to fetch all registered devices for a user")
	@RequestMapping(value = "/getAllDevicesForUser", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> getAllDevicesForUser(@ApiParam(value = "User Id for which registered devices needs to be fetch", required = true) 
	@RequestParam(required = true, value = "userId") Long userId)
	{
		
		
		JSONObject response;
		try
		{
			
			JSONArray deviceUserList = deviceOnboardingProcessor.getAllDevicesForUser(userId);
			response = new JSONObject().put("devicesList", deviceUserList);

			JSONObject result = new JSONObject().put("success", true).put("response", response)
					.put("msg", "Device(s) details Retrieved.");
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(),e);
			JSONObject result = new JSONObject().put("success", false).put("msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
	}

	@ApiOperation(value = "Register a new device.", notes = "API to register a device for a user in an OEM")
	@RequestMapping(value = "/register", method = RequestMethod.POST, produces = "application/json", consumes = "application/x-www-form-urlencoded")
	public ResponseEntity<String> registerDevice(
			@ApiParam(value = "JWT authorization token", required = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
			@ApiParam(value = "Friendly name for the device", required = true) @RequestParam(required = true, value = "deviceAlias") String deviceAlias,
			@ApiParam(value = "Device type Id", required = true) @RequestParam(required = true, value = "deviceTypeId") Long deviceTypeId,
			@ApiParam(value = "VIN of the device", required = true) @RequestParam(required = true, value = "vin") String vin,
			@ApiParam(value = "IMEI numbner of the device", required = true) @RequestParam(required = true, value = "imei") String imei,
			@ApiParam(value = "URL of personalized logo for the device", required = false) @RequestParam(required = false, value = "deviceLogoUrl") String deviceLogoUrl)

	{
		try
		{
			String userName = JWTHelper.parseJWTClaim(jwtToken, "userName");
			Long oemId = new Long(JWTHelper.parseJWTClaim(jwtToken, "oemId"));
			deviceOnboardingProcessor.registerDevice(userName, deviceAlias,
					deviceTypeId, oemId, vin, imei, deviceLogoUrl);
			JSONObject result = new JSONObject().put("success", true).put("msg",
					"Device Registered Successfully.");
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(),e);
			JSONObject result = new JSONObject().put("success", false).put("msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
	}

	/*@ApiOperation(value = "Update an already registered device.", notes = "Provide details to update a existing device")
	@RequestMapping(value = "/update", method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<String> updateDevice(
			@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
			@RequestBody DeviceDTO deviceDTO)
	{
		try
		{
			// retrieving username just for the purpose of checking if user is
			// logged in.
			String username = JWTHelper.parseJWTClaim(jwtToken, "username");

			deviceRegistrationProcessor.updateDevice(username, deviceDTO);
			JSONObject result = new JSONObject().put("success", true).put("msg",
					"Device Updated Successfully.");
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());

		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(),e);
			JSONObject result = new JSONObject().put("success", false).put("msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
	}*/
	
	/*@ApiOperation(value = "Delete a device.")
	@RequestMapping(value = "/delete/{deviceId}", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<String> deleteDevice(@PathVariable(value = "deviceId") String deviceId)
	{
		try
		{
			deviceRegistrationProcessor.deleteDevice(deviceId);
			JSONObject result = new JSONObject().put("success", true).put("msg",
					"Device Deleted Successfully.");
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());

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
