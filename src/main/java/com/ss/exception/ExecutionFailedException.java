package com.ss.exception;

public class ExecutionFailedException extends RuntimeException{

	private static final long serialVersionUID = 1L;


	public ExecutionFailedException(String message) {
		super(message);
	}
}
