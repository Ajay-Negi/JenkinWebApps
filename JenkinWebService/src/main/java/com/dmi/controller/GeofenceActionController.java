package com.dmi.controller;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dmi.processor.GeofenceActionProcessor;
import com.google.gson.JsonArray;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 
 * @author Mukul Bansal
 */
@Api(tags = "Geofence Action Type")
@RestController
@RequestMapping(value = "/geofenceActionTypes")
public class GeofenceActionController
{
	
	private static final Logger LOG = Logger.getLogger(GeofenceActionController.class);
	
	@Autowired
	GeofenceActionProcessor geofenceActionProcessor;

	@ApiOperation(value = "Get geofence action types", notes = "API to get action types for a geofence, eg. Entry or Exit action", response= JsonArray.class)
	@RequestMapping(value = "/getAll", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> getFenceAreaOptions()
	{
		try
		{
			JSONArray response = geofenceActionProcessor.getGeofenceActions();
			JSONObject result = new JSONObject().put("success", true)
					.put("msg", "Fence Area options Retreived Successfully.")
					.put("response", new JSONObject().put("fence", response));

			return ResponseEntity.ok().body(result.toString());
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(),e);
			JSONObject result = new JSONObject().put("success", false).put("msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
		
	}
}
