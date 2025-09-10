package com.pim.exceptions;

import static com.pim.reports.FrameworkLogger.log;

import com.pim.enums.LogType;

@SuppressWarnings("serial")
public class NullPointerForFileDownloadException extends RuntimeException{
	
	public NullPointerForFileDownloadException(String message) {
        super(message);
        log(LogType.INFO,"Downloaded file, we trying to read in that given path is empty");
    }

    public NullPointerForFileDownloadException(String message,Throwable cause) {
        super(message,cause);
    }

}
