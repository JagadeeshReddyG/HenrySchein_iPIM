package com.pim.pages;

import org.openqa.selenium.By;

import com.pim.enums.WaitLogic;

public class CatalogSubMenu extends BasePage {
	private final String catalogTypeXpath = "//div[contains(text(),'${variable}')]";
	private final String masterCatalogXpath = "//span[contains(text(),'${variable}')]";
	private final String catalogTitleXpath = "//li[@class='active hmpw-breadcrumb-item'][@title='${variable}')]";

	public ProductDetailSearchPage selectCatalogType(String catalogType) {

		click(getElementByReplaceText(catalogTypeXpath, catalogType), WaitLogic.CLICKABLE, "catalogType");
		return new ProductDetailSearchPage();
	}
	
	public ProductDetailSearchPage selectMasterCatalog(String masterCatalog) {
		click(getElementByReplaceText(masterCatalogXpath, masterCatalog), WaitLogic.CLICKABLE, "catalogType");
		return new ProductDetailSearchPage();
	}

	
	public ProductDetailSearchPage ClickTitle(String title) {

		click(getElementByReplaceText(catalogTitleXpath, title), WaitLogic.CLICKABLE, "catalogType");
		return new ProductDetailSearchPage();
	}
}