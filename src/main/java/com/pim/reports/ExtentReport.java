package com.pim.reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.pim.constants.Constants;
import com.pim.enums.CategoryType;
import com.pim.enums.Modules;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class ExtentReport {

    private ExtentReport() {
    }

    private static ExtentReports extent;

    public static void initReports() {
        if (Objects.isNull(extent)) {
            extent = new ExtentReports();
            ExtentSparkReporter spark = new ExtentSparkReporter(Constants.getExtentReportFilePath());
            extent.attachReporter(spark);
            spark.config().setTheme(Theme.STANDARD);
            spark.config().setDocumentTitle("PIM Automation Report");
            spark.config().setReportName("PIM Automation");

        }
    }

    public static void flushReports () {
        if (Objects.nonNull(extent)) {
            extent.flush();
        }
        ExtentManager.unload();
        try {
            Desktop.getDesktop().browse(new File(Constants.getExtentReportFolderPath()).toURI());
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }

    public static void createTest(String testcasename) {
        ExtentManager.setExtentTest(extent.createTest(testcasename));
    }

    public static void addAuthors(Modules module) {
            ExtentManager.getExtentTest().assignAuthor(module.name());
    }

    public static void addCategories(CategoryType[] categories) {
        for(CategoryType temp:categories) {
            ExtentManager.getExtentTest().assignCategory(temp.toString());
        }
    }

        }



