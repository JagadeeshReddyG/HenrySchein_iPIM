package com.pim.pages;

import org.openqa.selenium.By;

public class GeneralInformationTab extends BasePage{
	ProductDetailSearchPage pdp = new ProductDetailSearchPage();
	
	private final By DQStatus = By.xpath("(//span[contains(text(),'DQ Status:')]/ancestor::td/following-sibling::td)[2]//pre");
	
	public boolean isDQStatusVisible() {
		pdp.waitUntilBufferingIconDisappear();
		return isVisible(DQStatus,"DQ Status");
	}

}
