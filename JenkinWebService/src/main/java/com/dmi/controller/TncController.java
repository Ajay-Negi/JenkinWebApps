package com.dmi.controller;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dmi.dto.TncDTO;
import com.dmi.exception.ProcessorException;
import com.dmi.processor.TncProcessor;
import com.dmi.security.JWTHelper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 
 * @author Mukul Bansal
 */

@Api(tags = "Terms And Conditions")
@RestController
public class TncController
{
	
	private static final Logger LOG = Logger.getLogger(TncController.class);
	
	@Autowired
	public TncProcessor tncProcessor;

	@ApiOperation(value = "Get latest Terms and Conditions Agreement.")
	@RequestMapping(value = "/tnc/getLatest", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> getLatestTnc(@RequestParam("oemId") Long oemId)
	{
		try
		{
			JSONObject tnc = tncProcessor.getLatestTnc(oemId);
			JSONObject result = new JSONObject().put("success", true)
					.put("msg", "Latest Terms And Conditions Retrieved.").put("response", tnc);
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(),e);
			JSONObject result = new JSONObject().put("success", false).put("msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
	}

	@ApiOperation(value = "Save a Terms and Conditions Agreement for an OEM.")
	@RequestMapping(value = "/tnc/save", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<String> save(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
			@RequestBody TncDTO tncDTO)
	{
		try
		{
			String username = JWTHelper.parseJWTClaim(jwtToken, "userName");
			Long oemId = Long.parseLong(JWTHelper.parseJWTClaim(jwtToken, "oemId"));
			tncProcessor.save(username, oemId, tncDTO.getVersion(), tncDTO.getAgreement());
			JSONObject result = new JSONObject().put("success", true).put("msg",
					"Terms And Conditions Saved.");
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
		catch (ProcessorException e)
		{
			LOG.error(e.getMessage(),e);
			JSONObject result = new JSONObject().put("success", false).put("msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(),e);
			JSONObject result = new JSONObject().put("success", false).put("msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
	}

	@ApiOperation(value = "Get all Terms and Conditions Agreement for an OEM.")
	@RequestMapping(value = "/tnc/getAll", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> save(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken)
	{
		try
		{
			Long oemId = Long.parseLong(JWTHelper.parseJWTClaim(jwtToken, "oemId"));
			JSONArray allTncs = tncProcessor.get(oemId);
			JSONObject result = new JSONObject().put("success", true).put("response", allTncs)
					.put("msg", "All Terms And Conditions Retrieved.");
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(),e);
			JSONObject result = new JSONObject().put("success", false).put("msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
	}

	@ApiOperation(value = "Update a Terms and Conditions Agreement for an OEM.")
	@RequestMapping(value = "/tnc/update", method = RequestMethod.PUT)
	public ResponseEntity<String> updateTnc(
			@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken, @RequestBody TncDTO tncDTO)
	{
		try
		{
			String username = JWTHelper.parseJWTClaim(jwtToken, "userName");
			Long oemId = Long.parseLong(JWTHelper.parseJWTClaim(jwtToken, "oemId"));
			tncProcessor.update(username, oemId, tncDTO);
			JSONObject result = new JSONObject().put("success", true).put("msg",
					"Terms and Conditions Updated successfully.");
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(),e);
			JSONObject result = new JSONObject().put("success", false).put("msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
	}

	@ApiOperation(value = "Delete a Terms and Conditions Agreement.")
	@RequestMapping(value = "/tnc/delete", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<String> deleteTnc(
			@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
			@RequestParam(required = true, value = "tncId") long tncId)
	{
		JSONObject result = new JSONObject();
		try
		{
			String username = JWTHelper.parseJWTClaim(jwtToken, "userName");

			tncProcessor.delete(username, tncId);
			result.put("success", true).put("msg", "Terms And Conditions Deleted Successfully.");
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(),ex);
			result.put("success", false).put("msg", ex.getMessage());
		}
		return ResponseEntity.ok().body(result.toString());
	}

}