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

import com.dmi.processor.ForgotPasswordProcessor;
import com.dmi.utils.ValidationHelper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 
 * @author Ajay Negi
 */

@Api(tags = "Forgot Password")
@RestController
public class ForgotPasswordController
{
	
	private static final Logger LOG = Logger.getLogger(ForgotPasswordController.class);
	
	@Autowired
	ForgotPasswordProcessor forgotPasswordProcessor;

	@ApiOperation(value = "Sends a password reset email to the user's registered Email ID.", notes = "API to send password reset link to the user")
	@RequestMapping(value = "/forgotPassword", method = RequestMethod.POST, produces = "application/json", consumes = "application/x-www-form-urlencoded")
	public ResponseEntity<String> forgotPassword(@ApiParam(value = "Username of the user", required = true) @RequestParam(required = true, value = "username") String username)
	{
		try
		{
			ValidationHelper.validate(username, ValidationHelper.NAME);
			forgotPasswordProcessor.sendPasswordResetEmail(username);
			JSONObject result = new JSONObject().put("success", true).put("msg",
					"Link for resetting password sent to your email address.");
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(),e);
			JSONObject result = new JSONObject().put("success", false).put("msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
	}

	@ApiOperation(value = "Reset a user's password.", notes = "API to reset password by the user")
	@RequestMapping(value = "/resetPassword", method = RequestMethod.POST, produces = "application/json", consumes = "application/x-www-form-urlencoded")
	public ResponseEntity<String> resetPassword(@RequestParam(required = true, value = "token") String jwtToken,
			@ApiParam(value = "Password string", required = true) @RequestParam(required = true, value = "password") String password,
			@ApiParam(value = "Password string to confirm correct password", required = true) @RequestParam(required = true, value = "confirmPwd") String confirmPwd)
	{
		try
		{
			forgotPasswordProcessor.resetPassword(jwtToken, password);
			JSONObject result = new JSONObject().put("success", true).put("msg",
					"Password Changed Successfully. Login with your new Password to continue.");
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(),e);
			JSONObject result = new JSONObject().put("success", false).put("msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(result.toString());
		}
		
	}
}
