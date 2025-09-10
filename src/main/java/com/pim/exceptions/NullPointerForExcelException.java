package com.pim.exceptions;

import static com.pim.reports.FrameworkLogger.log;

import com.pim.enums.LogType;

@SuppressWarnings("serial")
public class NullPointerForExcelException extends RuntimeException{
	
	public NullPointerForExcelException(String message) {
        super(message);
        log(LogType.INFO,"Excel cell you trying to read is empty");
    }

    public NullPointerForExcelException(String message,Throwable cause) {
        super(message,cause);
    }

}
