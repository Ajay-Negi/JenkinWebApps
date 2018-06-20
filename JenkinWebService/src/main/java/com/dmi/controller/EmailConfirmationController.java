package com.dmi.controller;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dmi.processor.EmailConfirmationProcessor;
import com.dmi.utils.ValidationHelper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 
 * @author Mukul Bansal
 *
 */
@Api(tags = "Email Verification")
@RestController
@RequestMapping(value = "/email")
public class EmailConfirmationController
{
	private static final Logger LOG = Logger.getLogger(EmailConfirmationController.class);
	
	@Autowired
	EmailConfirmationProcessor emailConfirmationProcessor;
	
	@ApiOperation(value = "Send verification E-Mail to confirm the email ID of a user.", notes = "API to send email verification link for a new registered user")
	@RequestMapping(value = "/sendVerificationEmail", method = RequestMethod.POST, produces = "application/json", consumes = "application/x-www-form-urlencoded")
	public ResponseEntity<String> sendVerificationMail(
			@ApiParam(value = "Username for the new user", required = true) @RequestParam(required = true, value = "username") String username,
			@ApiParam(value = "Registered email of the user", required = true) @RequestParam(required = true, value = "email") String email)
	{
		try
		{
			ValidationHelper.validate(username, ValidationHelper.NAME);
			ValidationHelper.validate(email, ValidationHelper.EMAIL);
			
			emailConfirmationProcessor.sendVerificationEmail(username, email);
			JSONObject result = new JSONObject().put("success", true).put(
					"msg", "Verification Email Sent.");
			return ResponseEntity.status(HttpStatus.OK).body(
					result.toString());
		}
		catch(Exception e)
		{
			LOG.error(e.getMessage(),e);
	
			JSONObject result = new JSONObject().put("success", false).put(
					"msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(
					result.toString());
		}
	}
	
	@ApiOperation(value = "Confirm Email Id of a user", notes = "API which is called when email confirmation link is clicked by the user.")
	@RequestMapping(value = "/confirmEmail", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> confirmEmail(@ApiParam(value = "JWT token String", required = true) @RequestParam("jwt") String jwt)
	{
		try
		{
			emailConfirmationProcessor.confirmEmail(jwt);
			JSONObject result = new JSONObject().put("success", true).put("msg", "Email Confirmed. Login to continue.");
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
			
		}
		catch(Exception e)
		{
			LOG.error(e.getMessage(),e);
			JSONObject result = new JSONObject().put("success", false).put("msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
	}

}
