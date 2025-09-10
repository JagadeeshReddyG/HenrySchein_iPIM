package com.pim.exceptions;

@SuppressWarnings("serial")
public class InvalidPathForImageException extends RuntimeException{
	
	public InvalidPathForImageException(String message) {
        super(message);
    }

    public InvalidPathForImageException(String message,Throwable cause) {
        super(message,cause);
    }
}
