package com.pim.utils;

public class DynamicLocatorUtils {

    private DynamicLocatorUtils() {}

    public static String getLocator(String locator, String value) {
        return String.format(locator, value);//a[text()='%s']
    }

    public static String getLocator(String locator, String value1, String value2) {
        return String.format(locator, value1,value2);
    }
}
