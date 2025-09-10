package com.pim.driver;


import org.openqa.selenium.WebDriver;

import java.util.Objects;


public final class DriverManager {
    private DriverManager(){

    }

    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public static WebDriver getDriver(){
        return driver.get();
    }


    static void setDriver(WebDriver wd){
        if(Objects.nonNull(wd)){
            driver.set(wd);
        }
    }

    static void unload(){
        driver.remove();
    }


}
