package com.pim.listeners;
import com.pim.annotations.PimFrameworkAnnotation;
import com.pim.constants.Constants;
import com.pim.enums.LogType;
import com.pim.reports.ExtentReport;

import static com.pim.reports.FrameworkLogger.log;
import org.testng.*;

import java.util.Arrays;

public class ListenerClass implements ITestListener, ISuiteListener {

    @Override
    public void onStart(ISuite suite) {
            ExtentReport.initReports();
    }

    @Override
    public void onFinish(ISuite suite) {
            ExtentReport.flushReports();
    }

    @Override
    public void onTestStart(ITestResult result) {
    	String description = result.getMethod().getDescription();
        if(Constants.LOCALE != null) {
        	description = Constants.LOCALE + " | " + result.getMethod().getDescription();
        }
        ExtentReport.createTest(description);
       ExtentReport.addAuthors(result.getMethod().getConstructorOrMethod().getMethod().getAnnotation(PimFrameworkAnnotation.class)
                .module());
       ExtentReport.addCategories(result.getMethod().getConstructorOrMethod().getMethod().getAnnotation(PimFrameworkAnnotation.class)
                .category());

    }

    @Override
    public void onTestSuccess(ITestResult result) {
        log(LogType.PASS,result.getMethod().getMethodName() +" is passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        log(LogType.FAIL,result.getMethod().getMethodName() +" is failed");
        log(LogType.FAIL,result.getThrowable().toString());
        log(LogType.FAIL, Arrays.toString(result.getThrowable().getStackTrace()));
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        log(LogType.SKIP,result.getMethod().getMethodName()+" is skipped");
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        /*
         * For now, we are not using this.
         */
    }

    @Override
    public void onStart(ITestContext context) {
        /*
         * We are having just one test in our suite.
         */
    }

    @Override
    public void onFinish(ITestContext context) {
        /*
         * We are having just one test in our suite.
         */

    }



}