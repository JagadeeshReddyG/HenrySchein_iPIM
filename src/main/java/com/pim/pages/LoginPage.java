package com.pim.pages;

import com.pim.enums.WaitLogic;

import org.openqa.selenium.By;

public class LoginPage extends BasePage {

    private final By textboxUsername = By.id("userName");
    private final By textboxPassword = By.id("password");
    private final By buttonLogin = By.id("login-button");
    private final By eshineLoginButton = By.xpath("//a[@class='hs-btn primary-btn x-small rounded medium-above tab-like']");
    private final By eshinetextboxUsername = By.xpath("//input[@id='ctl00_ucHeader_ucSessionBar_ucLogin_txtLogonName']");
    private final By eshinetextboxPassword = By.xpath("//input[@id='ctl00_ucHeader_ucSessionBar_ucLogin_txtPassword']");

    
    public LoginPage enterUserName(String username){
        sendKeys(textboxUsername,username, WaitLogic.CLICKABLE, "username");
        return this;
    }

    public LoginPage enterPassword(String password){
        WaitForMiliSec(1000);
        sendKeys(textboxPassword,password, WaitLogic.PRESENCE);
        return this;
    }

    public PimHomepage clickLoginButton(){
        click(buttonLogin, WaitLogic.CLICKABLE, "login Button");
        return new PimHomepage();
    }
    public String getTitle(){
        return getPageTitle();
    }
    

    
}
