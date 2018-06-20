/* package com.dmi.controller;


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

import com.dmi.processor.SimulatorConfigProcessor;
import com.dmi.security.JWTHelper;

import io.swagger.annotations.Api;

@Api(tags = "Device Simulator")
@RestController
@RequestMapping(value = "/simulatorConfig")
public class SimulatorConfigController
{

	private static final Logger LOG = Logger.getLogger(SimulatorConfigController.class);
	
	@Autowired
	SimulatorConfigProcessor simulatorConfigProcessor;

	
	@RequestMapping(value = "/startSimulator", method = RequestMethod.GET)
	public ResponseEntity<String> startSimulator(
			@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
			@RequestParam(value = "subDomainId") String subDomainId)
	{
		try
		{
			String domainId = JWTHelper.parseJWTClaim(jwtToken, "domainId");
			String response = "Process ID - "
					+ simulatorConfigProcessor.startSimulator();
			JSONObject result = new JSONObject().put("success", true)
					.put("msg", "Simulator Started.").put("response", response);
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(), e);
			JSONObject result = new JSONObject().put("success", false).put(
					"msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
	}

	@RequestMapping(value = "/stopSimulator", method = RequestMethod.GET)
	public ResponseEntity<String> stopSimulator()
	{
		try
		{

			simulatorConfigProcessor.stopSimulator();
			JSONObject result = new JSONObject().put("success", true).put(
					"msg", "Simulator Stopped.");
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			JSONObject result = new JSONObject().put("success", false).put(
					"msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
	}

	
}
*/