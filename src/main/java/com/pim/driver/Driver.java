package com.pim.driver;


import com.pim.enums.ConfigProperties;
import com.pim.exceptions.BrowserInvocationFailedException;
import com.pim.factories.DriverFactory;
import com.pim.utils.PropertyFileRead;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.net.MalformedURLException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public final class Driver {
    private Driver(){

    }
    public static WebDriver driver;


    public static void initDriver(String browser, String envUrl){
        String runmode = PropertyFileRead.getPropValue(ConfigProperties.RUNMODE);
        if(Objects.isNull(DriverManager.getDriver())) {
            try {
                DriverManager.setDriver(DriverFactory.getDriver(browser));
                DriverManager.getDriver().manage().timeouts().pageLoadTimeout(120, TimeUnit.SECONDS);
                DriverManager.getDriver().get(envUrl);
                
                DriverManager.getDriver().manage().window().maximize();
            }
            catch (MalformedURLException e) {
                throw new BrowserInvocationFailedException("Please check the capabilities of browser");
            }catch (WebDriverException we){
            	
                System.out.println("Application is down.");
            }
        }
    }

    public static void quitDriver(){
       if(Objects.nonNull(DriverManager.getDriver())){
            DriverManager.getDriver().quit();
            DriverManager.unload();
       }

    }

}