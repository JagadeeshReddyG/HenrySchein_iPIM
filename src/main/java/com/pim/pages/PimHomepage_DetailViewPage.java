package com.pim.pages;

import com.pim.driver.DriverManager;
import com.pim.enums.WaitLogic;

import java.awt.AWTException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;


public class PimHomepage_DetailViewPage extends BasePage {

	private final By priceDisplayFlagField = By.xpath("//div[@id='detail_value_930f51474c6eefd0ca9986db95540ea1']");
	//	private final By priceDisplayFlagField = By.xpath("//table//div[@class='v-filterselect-button']");
	private final By priceDisplayFieldDropdown = By.xpath("(//div[@class='v-filterselect-button'])[1]");
	//    private final By priceDisplayFlagDropDownValues = By.xpath("(//*[@role=\\\"listitem\\\"])[2]");
	private final String priceDisplayFlagDropDownValues = "//table//span[text()='${variable}']";
	//	private final By displayPriceInMaster = By.xpath("//div[@id='detail_value_930f51474c6eefd0ca9986db95540ea1']");
	private final By exceptionListArea = By.xpath("//span[contains(text(),'Exception List')]/../../..//div[@class='v-csslayout v-layout v-widget hpmw-inlineCcontainer v-csslayout-hpmw-inlineCcontainer v-has-width']");
	private By listofExceptionListSelectedcatalogs = By.xpath(
			"(//span[text()='Exception List:']//following::td[@class='v-formlayout-contentcell'])[1]//div[@class='v-slot']/div/div/div/div[2]/div");
	private By clearExceptionListSelectedCatalogs = By.xpath(
			"(//span[text()='Exception List:']//following::td[@class='v-formlayout-contentcell'])[1]//div[@class='v-slot']/div/div[1]/div/div[2]/div");
	private final By ExceptionListDropdown = By.xpath("//div[@class='v-filterselect-button']");
	private final String userAndExceptionListForAllCatalog = "//td//span[text()='${variable}']";
	//	private final By SaveButton = By.xpath("//span[contains(text(),'SAVE')]");

	
	private final By MaximizeButton = By.xpath(
			"//div[@class='v-button v-widget hpmw-detail-view-focus-btn v-button-hpmw-detail-view-focus-btn hpmw-expand-all-button v-button-hpmw-expand-all-button']//span[@class='v-button-wrap']");
	private final By MinimizeButton = By.xpath("//div//span[@class='v-button-wrap']");
	public By loadingIcon = By.xpath("//div[@class='v-loading-indicator second v-loading-indicator-delay']");

	public String editDisplayPriceTxtfld() {
		maximizeDetailViewTab();
		WaitForMiliSec(2000);
		String displayValue = getTextValue(priceDisplayFlagField, WaitLogic.CLICKABLE, "priceDisplayFlagField");
		String displayValueAfterCondition = "";
		if (displayValue.equals("No content")||displayValue.equals("No")) {
			displayValueAfterCondition = "Yes";
			click(priceDisplayFlagField, WaitLogic.CLICKABLE, "priceDisplayFlagField");
			WaitForMiliSec(1000);
			click(priceDisplayFieldDropdown, WaitLogic.CLICKABLE, "priceDisplayFieldDropdown");
			WaitForMiliSec(3000);
			click(getElementByReplaceText(priceDisplayFlagDropDownValues, displayValueAfterCondition), WaitLogic.CLICKABLE, "exceptionListRuleType");
			WaitForMiliSec(5000);
		}
		else {
			displayValueAfterCondition = "No";
			click(priceDisplayFlagField, WaitLogic.CLICKABLE, "priceDisplayFlagField");
			WaitForMiliSec(1000);
			click(priceDisplayFieldDropdown, WaitLogic.CLICKABLE, "priceDisplayFieldDropdown");
			WaitForMiliSec(3000);
			click(getElementByReplaceText(priceDisplayFlagDropDownValues, displayValueAfterCondition), WaitLogic.CLICKABLE, "exceptionListRuleType");
			WaitForMiliSec(5000);
		}
		if (isVisible(loadingIcon, "Loading Element", 5)) {
			waitUtilLoadingIconDisappear(loadingIcon, WaitLogic.INVISIBLE, "Product Load page");
		}
		minimizeDetailViewTab();
		WaitForMiliSec(2000);
		return displayValueAfterCondition;
	}

	public PimHomepage_DetailViewPage editDentalExceptionList(String ExceptionList) {
		clickOnExceptionListArea();
		clearSelectedFields(listofExceptionListSelectedcatalogs, clearExceptionListSelectedCatalogs);
		WaitForMiliSec(3000);
		click(ExceptionListDropdown, WaitLogic.CLICKABLE, "DentalExceptionListDropdown");
		WaitForMiliSec(2000);
		click(getElementByReplaceText(userAndExceptionListForAllCatalog, ExceptionList), WaitLogic.CLICKABLE, "exceptionListRuleType");
		WaitForMiliSec(2000);
		//		click(SaveButton, WaitLogic.CLICKABLE, "saveButton");
		//		WaitForMiliSec(2000);
		return this;    	
	}

	public void clearSelectedFields(By listOfSelectedCatalogs, By clearSelectedCatalogs) {
		WaitForMiliSec(5000);
		List<WebElement> webElements = DriverManager.getDriver().findElements(listOfSelectedCatalogs);
		if (webElements != null) {

			for (int i = 1; i <= webElements.size(); i++) {
				WaitForMiliSec(5000);
				DriverManager.getDriver().findElement(clearSelectedCatalogs).click();
			}
		}
	}

	public PimHomepage_DetailViewPage clickOnExceptionListArea() {
		WaitForMiliSec(1000);
		click(exceptionListArea, WaitLogic.CLICKABLE, "exceptionListArea is clicked");
		WaitForMiliSec(1000);
		return this;
	}

	public String getDisplayPriceTxtfldValue() {
		String value = getStringValues(priceDisplayFlagField, WaitLogic.VISIBLE, null);
		return value;
	}


	//	public PimHomepage_DetailViewPage switchingQueryCatalog() {
	//		WaitForMiliSec(2000);
	//		click(MaximizeButton, WaitLogic.CLICKABLE, "Maximize button clicked");
	//		WaitForMiliSec(2000);
	//		return this;
	//	}

	public PimHomepage_DetailViewPage maximizeDetailViewTab() {
		WaitForMiliSec(2000);
		click(MaximizeButton, WaitLogic.CLICKABLE, "Maximize button clicked");
		WaitForMiliSec(2000);
		return this;
	}

	public PimHomepage_DetailViewPage minimizeDetailViewTab() {
		WaitForMiliSec(2000);
		click(MinimizeButton, WaitLogic.CLICKABLE, "Minimize button clicked");
		WaitForMiliSec(2000);
		return this;
	}
}
