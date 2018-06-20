package com.dmi.security;

import com.dmi.exception.ValidationException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;

/**
 * @author Ajay Negi
 */
public class JWTHelper
{

	public static String parseJWTClaim(String jwtToken, String claim)
			throws ValidationException
	{

		String token = jwtToken.substring("Bearer".length()).trim();
		String result = null;

		try
		{
			// Validate the token
			result = validateToken(token, claim);
		}
		catch (Exception e)
		{
			throw new ValidationException("Illegal operation attempted!");
		}
		return result;

	}

	private static String validateToken(String token, String claim)
			throws Exception
	{
		// Check if it was issued by the server and if it's not expired
		// Throw an Exception if the token is invalid

		String result = null;

		try
		{
			Jws<?> jws = (Jws<?>) Jwts.parser()
					.setSigningKey(JWTTokenService.getKey()).parse(token);
			Claims claims = (Claims) jws.getBody();
			result = String.valueOf(claims.get(claim));
		}
		catch (SignatureException e)
		{
			throw new Exception("Token mismatch!");
		}
		catch (ExpiredJwtException e)
		{
			throw new Exception("Token expired!");
		}
		catch (Exception e)
		{
			throw new Exception("Token mismatch!");
		}

		return result;

	}

}
