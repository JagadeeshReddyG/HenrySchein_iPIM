package com.pim.pages;

import com.pim.enums.WaitLogic;

import org.openqa.selenium.By;

import java.lang.reflect.Method;

public class PimHomepage extends BasePage {

    private final By logoutButton = By.xpath("//div[contains(@class,'logout-button')]/button");

    public LoginPage clickLogoutButton(){
        click(logoutButton, WaitLogic.CLICKABLE, "logout Button");
        return new LoginPage();
    }
	public MainMenu mainMenu() {
		return new MainMenu();
}
	
	public ProductDetailSearchPage productDetailSearchPage(){
        return new ProductDetailSearchPage();
    }
}
