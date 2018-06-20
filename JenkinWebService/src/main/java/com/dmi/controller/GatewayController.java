package com.dmi.controller;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dmi.dto.gateway.DeviceRequestDTO;
import com.dmi.processor.GatewayProcessor;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(tags="Gateway")
@RestController
@RequestMapping(value="/connectedVehicle")
public class GatewayController {

private static final Logger LOG = Logger.getLogger(GatewayController.class);
	
	@Autowired
	GatewayProcessor gatewayProcessor;

	@ApiOperation(value = "Get the configuration for a vehicle.", notes = "API to get DataCollectionInterval, DataUploadInterval for TelemetryData and VehicleHealthData")
	@RequestMapping(value = "/getConfigData", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<String> getConfiguration(
			@ApiParam(value = "JSON string which contain device's VIN ") @RequestBody DeviceRequestDTO deviceRequestDTO)
	{
		try
		{
			
			JSONObject response = gatewayProcessor.getDeviceConfigModelByVinId(deviceRequestDTO.getVinId());
			
			JSONObject result = new JSONObject().put("success", true).put("response", response)
					.put("msg", "Device ConfigModel Retrieved.");
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(),e);
			JSONObject result = new JSONObject().put("success", true).put("msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
		
	}
	
	@ApiOperation(value = "Get key data for a vehicle.", notes = "API to get data for a vehicle eg. key, deviceID, MQTT topicURL, configDetailURL")
	@RequestMapping(value = "/getKeyData", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<String> getServices(
			@ApiParam(value = "JSON string which contain device's VIN ") @RequestBody DeviceRequestDTO deviceRequestDTO)
	{
		try
		{
			JSONObject response = gatewayProcessor.getKeyDataByDevice(deviceRequestDTO.getVinId());
			
			JSONObject result = new JSONObject().put("success", true).put("response", response)
					.put("msg", "DeviceKeyData Retrieved.");
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(),e);
			JSONObject result = new JSONObject().put("success", true).put("msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
		
	}
}
