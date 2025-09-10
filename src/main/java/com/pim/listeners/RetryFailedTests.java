package com.pim.listeners;

import com.pim.enums.ConfigProperties;
import com.pim.utils.PropertyFileRead;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryFailedTests implements IRetryAnalyzer {

    private int count=0;
    private int retries = 1;

    @Override
    public boolean retry(ITestResult result) {
        boolean value =false;
        if(PropertyFileRead.getPropValue(ConfigProperties.RETRYFAILEDTESTS).equalsIgnoreCase("yes")) {
            value = count<retries ;
            count++;
        }
        return value;
    }


}
