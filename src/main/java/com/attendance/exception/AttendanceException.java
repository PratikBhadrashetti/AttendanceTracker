package com.attendance.exception;

import java.io.Serializable;

import org.springframework.http.HttpStatus;

/**
 * @author harsha.patil
 *
 */

public class AttendanceException extends Exception implements Serializable
{

	private static final long serialVersionUID = 1L;

	private HttpStatus httpStatus;
	
	public AttendanceException(String errorMessage, HttpStatus httpStatus)
	{
		super (errorMessage);
		this.httpStatus = httpStatus;
	}

	public HttpStatus getHttpStatus() 
	{
		return httpStatus;
	}

	public void setHttpStatus(HttpStatus httpStatus)
	{
		this.httpStatus = httpStatus;
	}
}
