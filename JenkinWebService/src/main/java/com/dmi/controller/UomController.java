package com.dmi.controller;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 
 * @author Ajay Negi
 */
//@Api(tags = "Unit Of Measurement")
@RestController
public class UomController
{
	
	/*private static final Logger LOG = Logger.getLogger(UomController.class);
	
	@Autowired
	public UomProcessor uomProcessor;

	@ApiOperation(value = "Return all suppported Unit of Measurements.", response = Uom.class)
	@RequestMapping(value = "/uom", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> getAllUom()
	{
		try
		{
			JSONArray allUom = uomProcessor.getAllUom();
			
			// Return the token on the response
			JSONObject result = new JSONObject().put("success", true)
					.put("msg", "All Supported UOM retreived.").put("response", allUom);

			return ResponseEntity.ok().body(result.toString());

		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(),e);
			JSONObject result = new JSONObject().put("success", false).put(
					"msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(
					result.toString());
		}
		
		JSONObject result = new JSONObject().put("success", true);
		return ResponseEntity.ok().body(result.toString());

	}
	
	@ApiOperation(value = "Save a Unit of Measurement.", notes = "Provide Unit of Measurement to save")
	@RequestMapping(value = "/uom", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<String> getAllUom(@RequestParam(required = true, value = "name") String name)
	{
		try
		{
			JSONArray allUom = uomProcessor.getAllUom();
			
			// Return the token on the response
			JSONObject result = new JSONObject().put("success", true)
					.put("msg", "All Supported UOM retreived.").put("response", allUom);

			return ResponseEntity.ok().body(result.toString());

		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(),e);
			JSONObject result = new JSONObject().put("success", false).put(
					"msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(
					result.toString());
		}
		
		JSONObject result = new JSONObject().put("success", true);
		return ResponseEntity.ok().body(result.toString());

	}*/
}
