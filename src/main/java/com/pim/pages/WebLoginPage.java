package com.pim.pages;

import org.openqa.selenium.By;

import com.pim.driver.DriverManager;
import com.pim.enums.ConfigProperties;
import com.pim.enums.WaitLogic;
import com.pim.utils.PropertyFileRead;

public class WebLoginPage extends BasePage {
	    //private final By eshinePageLoginButton = By.xpath("//a[@class='hs-btn primary-btn x-small rounded medium-above tab-like']");
	    private final By escheinPageLoginButton = By.xpath("//div[@class='hs-login']");
	    private final By escheintextboxUsername = By.xpath("//*[@type=\"email\"]");
	    private final By escheintextboxPassword = By.xpath("//*[@type=\"password\"]");
	    private final By escheinLoginButton = By.xpath("//div[@class='hs-form-row']//*[@type='button']");
	  //a[@class="hs-btn xx-small rounded medium-below tab-like"]
	    
	    //Navigating  Login 
	    public WebLoginPage navigatingToEscheinWeb(){
			WaitForMiliSec(5000);
			DriverManager.getDriver().get(PropertyFileRead.getPropValue(ConfigProperties.escheinurl));
			WaitForMiliSec(10000);
	        return this;
	    }
	    
	    //Eschein Login 
	    public WebLoginPage clickLoginButtonEscheinPage(){
	    	try {
		        moveToElementAndClick(escheinPageLoginButton, "login Button");
			} catch (Exception e) {
		        moveToElementAndClick(escheinPageLoginButton, "login Button");
			}
	        moveToElementAndClick(escheinPageLoginButton, "login Button");

	    	WaitForMiliSec(2000);
	        return this;
	    
	}
	    public WebLoginPage enterUserNameEschein(String username){
	        sendKeys(escheintextboxUsername,username, WaitLogic.CLICKABLE, "username");
	        return this;
	    }

	    public WebLoginPage enterPasswordEschein(String password){
	        sendKeys(escheintextboxPassword,password, WaitLogic.PRESENCE, "password");
	        return this;
	    }
	    public WebLoginPage clickLoginButton(){
	        click(escheinLoginButton, WaitLogic.CLICKABLE, "login Button");
	        return this;      
	}
	
	   
	    
	}

