/**
 * 
 */
package com.dmi.exception;

/**
 * @author Mukul Bansal
 *
 */
public class ValidationException extends Exception
{

	private static final long serialVersionUID = 1L;

	public ValidationException()
	{
		super();
	}

	public ValidationException(String msg)
	{
		super(msg);
	}

	public ValidationException(Exception ex)
	{
		super(ex);
	}

	public ValidationException(String msg, Exception ex)
	{
		super(msg, ex);
	}

}
