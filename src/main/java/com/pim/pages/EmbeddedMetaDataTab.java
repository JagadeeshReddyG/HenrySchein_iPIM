package com.pim.pages;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.pim.driver.DriverManager;

public class EmbeddedMetaDataTab extends BasePage{
	
	private final By metaDatas = By.xpath("//tbody//tr[@class='v-formlayout-row']");
	
	
	public int getMetaData() {
		List<WebElement> metadatas = DriverManager.getDriver().findElements(metaDatas);
		int count = 0;
		for (WebElement metadata : metadatas) {
			if(metadata.isDisplayed()) {
				count++;
			}
		}
		return count;
	}

}
