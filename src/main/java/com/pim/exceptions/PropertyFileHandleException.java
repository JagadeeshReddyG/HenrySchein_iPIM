package com.pim.exceptions;

@SuppressWarnings("serial")
public class PropertyFileHandleException extends FrameworkException{
    /**
     * Pass the message that needs to be appended to the stacktrace
     * @param message Details about the exception or custom message
     */
    public PropertyFileHandleException(String message) {
        super(message);
    }

    /**
     *
     * @param message Details abou t the exception or custom message
     * @param cause Pass the enriched stacktrace or customised stacktrace
     */
    public PropertyFileHandleException(String message, Throwable cause) {
        super(message,cause);
    }

}
