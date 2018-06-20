package com.dmi.exception;

import org.springframework.stereotype.Component;

/**
 * 
 * @author SAgarwal
 *
 */

@Component
public class HttpValidationException extends RuntimeException
{

	private static final long serialVersionUID = 1L;

	public HttpValidationException()
	{
		super();
	}

	public HttpValidationException(String message)
	{
		super(message);
	}

	public HttpValidationException(Throwable cause)
	{
		super(cause);
	}

	public HttpValidationException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public HttpValidationException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}
}