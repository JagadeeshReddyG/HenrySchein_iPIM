package com.pim.exceptions;

@SuppressWarnings("serial")
public class UnableToParseStringForSpecifiedFormatException extends InvalidPathForFilesException{

    public UnableToParseStringForSpecifiedFormatException(String message) {
        super(message);
    }

    public UnableToParseStringForSpecifiedFormatException(String message,Throwable cause) {
        super(message,cause);
    }

}
