package com.dmi.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import com.dmi.constant.Constants;
import com.dmi.dto.UserDTO;

/**
 * 
 * @author Mukul Bansal
 */
public class JWTTokenService
{
	public static String issueToken(UserDTO user)
	{

		String token = "";
		Key key = getKey();

		try
		{
			token = Jwts
					.builder()
					.setSubject(user.getUserId().toString())
					.claim("userName", user.getUserName())
					.claim("oemId", user.getOemId().toString())
					.claim("locationServices", user.getLocationServicesStatus())
					.claim("oemClassification", user.getOemClassificationAlias())
					.claim("oemName",user.getOemName())
					.claim("roleCode", user.getRoleCd())
					.setIssuedAt(new Date())
					.setExpiration(
							new Date(System.currentTimeMillis()
									+ (Constants.JWT_Timeout)))
					.signWith(SignatureAlgorithm.HS512, key).compact();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return token;
	}
	
	public static String issueEmailConfirmationToken(String jwtSubject,String username, String email)
	{

		String token = "";
		Key key = getKey();

		try
		{
			token = Jwts
					.builder()
					.setSubject(jwtSubject)
					.claim("username", username)
					.claim("email", email)
					.setIssuedAt(new Date())
					.setExpiration(
							new Date(System.currentTimeMillis()
									+ (Constants.EMAIL_JWT_TIMEOUT)))
					.signWith(SignatureAlgorithm.HS512, key).compact();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return token;
	}

	public static Key getKey()
	{

		PBEKeySpec keySpec = new PBEKeySpec(Constants.JWT_KEY.toCharArray());
		SecretKeyFactory kf = null;

		try
		{
			kf = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
			return kf.generateSecret(keySpec);
		}
		catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
		catch (InvalidKeySpecException e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
