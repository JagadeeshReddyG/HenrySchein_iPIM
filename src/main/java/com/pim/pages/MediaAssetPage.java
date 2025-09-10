package com.pim.pages;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.pim.driver.DriverManager;
import com.pim.enums.WaitLogic;
import com.pim.utils.Javautils;

public class MediaAssetPage extends BasePage{
	ProductDetailSearchPage pdp = new ProductDetailSearchPage();
	
	private final By firstAsset = By.xpath("(//div[@class='v-splitpanel-first-container v-splitpanel-first-container-rounded v-scrollable']//img)[1]");
	private final By technicalInformation = By.xpath("//div[contains(text(),'Technical information')]");
	private final By embeddedmetadata = By.xpath("//div[contains(text(),'Embe')]");
	private final By productImagesCount = By.xpath("//span[contains(text(),'Images')]");
	private final By filterIcon = By.xpath("//span[@class='v-menubar-menuitem v-menubar-menuitem-hpmw-search v-menubar-menuitem-unchecked']");
	private final By filterTextField = By.xpath("//input[@class='v-textfield v-widget v-has-width']");
	private final By filterButton = By.xpath("//span[contains(text(),'Filter')]");
	private final By usageListItemNumber = By.xpath("//td[@class='v-table-cell-content v-table-cell-content-ArticleType_SupplierAltAid']//div");
	
	public MediaAssetPage clickFirstAsset() {
		pdp.waitUntilBufferingIconDisappear();
		click(firstAsset, WaitLogic.CLICKABLE, "first Asset");
		return this;
	}
	
	public boolean isTechnicalInformationVisible() {
		return isVisible(technicalInformation,"Technical Information");
	}
	
	public boolean productImagesCount() {
		return isVisible(productImagesCount,"product asset count");
	}
	
	public boolean isEmbeddedMetaDataVisible() {
		return isVisible(embeddedmetadata,"Embedded Meta Data");
	}
	
	public MediaAssetPage clickFilterIcon() {
		click(filterIcon, WaitLogic.CLICKABLE, "filter icon");
		return this;
	}
	
	public MediaAssetPage enterValueInFilerTextField(String itemnumber) {
		pdp.waitUntilBufferingIconDisappear();
		sendKeys(filterTextField, itemnumber, WaitLogic.VISIBLE, "filter textbox");
		return this;
	}
	
	public MediaAssetPage clickFilterButton() {
		click(filterButton, WaitLogic.CLICKABLE, "filter button");
		return this;
	}
	
	public List<String> getHSIItemNumber(){
		List<WebElement> listSize = DriverManager.getDriver().findElements(usageListItemNumber);
		List<String> lisHSIItem = new ArrayList<String>();
		for (WebElement itemnumber : listSize) {
			String getitemnumber = itemnumber.getText();
			lisHSIItem.add(getitemnumber);
		}
		return lisHSIItem;
	}

}
