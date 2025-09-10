package com.pim.pages;

import org.openqa.selenium.By;

import com.pim.enums.WaitLogic;

public class SearchSubMenu extends BasePage {

	private final String itemTypeXpath = "//span[normalize-space()='${variable}']";
	private final By searchItemDropdown = By.xpath("//div[@class='v-filterselect-button']");
	private final By search = By.xpath("//input[@id='search_input_textfield']");
	private final By searchButton1 = By.xpath("//*[@id=\"fulltextsearch_button\"]");

	public SearchSubMenu selectCodeType(String codeType) {
		WaitForMiliSec(5000);
		click(searchItemDropdown, WaitLogic.CLICKABLE, "searchItemDropdown");
		click(getElementByReplaceText(itemTypeXpath, codeType), WaitLogic.CLICKABLE, "itemType");
		WaitForMiliSec(1000);
		return this;
	}

	public SearchSubMenu enterSearchText(String searchText) {

		click(search, WaitLogic.PRESENCE, "search");
		sendKeys(search, searchText, WaitLogic.PRESENCE, "searchText");
		WaitForMiliSec(3000);
		return this;
	}

	public ProductDetailSearchPage clickSearchButton() {
		click(searchButton1, WaitLogic.PRESENCE, "searchButton1");
		WaitForMiliSec(3000);
		return new ProductDetailSearchPage();
	}

}
