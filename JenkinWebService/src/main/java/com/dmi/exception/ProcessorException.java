package com.dmi.exception;

public class ProcessorException extends Exception
{

	/**
	 * @author Mukul Bansal
	 */
	private static final long serialVersionUID = 1L;

	public ProcessorException()
	{
		super();
	}

	public ProcessorException(String msg)
	{
		super(msg);
	}

	public ProcessorException(String msg, Exception ex)
	{
		super(msg, ex);
	}

	public ProcessorException(Exception ex)
	{
		super(ex);
	}

}
