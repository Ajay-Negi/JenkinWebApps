package com.dmi.controller;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dmi.processor.OemClassificationProcessor;

/**
 * 
 * @author Ajay Negi
 */
//@Api(tags = "Oem Classification")
//@RestController
public class OemClassificationController
{
	private static final Logger LOG = Logger.getLogger(OemClassificationController.class);
	
	@Autowired
	public OemClassificationProcessor oemClassificationProcessor;

	/*@ApiOperation(value = "Get all Oem Classificaiton types." , notes = "This API provides all OEM classification types", response = OemClassification.class,
			responseContainer = "List")*/
	@RequestMapping(value = "/oem/classification", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String>
			getOemClassificationTypes()
	{
		try
		{
			JSONArray response = oemClassificationProcessor.getAll();
			JSONObject result = new JSONObject().put("success", true).put("response", response)
					.put("msg", "Oem Classificaiton Types Retrieved.");
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
