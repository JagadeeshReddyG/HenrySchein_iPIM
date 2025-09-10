package com.pim.pages;

import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import com.pim.driver.DriverManager;
import com.pim.enums.WaitLogic;

public class WebPages extends BasePage {

	private final By eshinePageSearchValueXpth = By
			.xpath("//input[@id='ctl00_ucHeader_ucSearchBarHeaderbar_txtSearch']");
	private final By eshinePageSearchButtonXpth = By
			.xpath("//a[@class='hs-btn secondary-btn rounded proxy-search-btn']");

	// Product Search Xpath in the Page
	private final String productSearchXpath = "//strong[normalize-space()='${variable}']/ancestor::h2/a";

	// Eshine Product Page validation Xpath

	//private final String MetaData_MetaKeywordValueXpath = "//meta[@name='keywords' and @content='${variable}']";
	private final By MetaData_MetaKeywordValueXpath = By.xpath("//meta[@name='keywords']");
	//private final String MetaData_MetaDescriptionValueXpath = "//meta[@name='description' and @content='${variable}']";
	private final By MetaData_MetaDescriptionValueXpath = By.xpath("//meta[@name='description']");
	//private final String DescriptionValueXpath = "//li[@class='customer-notes']//div[@class='field']/..//div[contains(text(),'${variable}')]";
	private final By DescriptionValueXpath = By.xpath("//li[@class='customer-notes']//div[@class='value']");
	//private final String H1tagXpath = "//h1[@class='title main-page-title']";
	private final By H1tagXpath = By.xpath("//h1[@class='title main-page-title']");
	

	public WebPages clickSearchBarInEshinePage(String SerachValue) {
		WaitForMiliSec(2000);
		sendKeys(eshinePageSearchValueXpth, SerachValue, WaitLogic.CLICKABLE, SerachValue);
		WaitForMiliSec(2000);
		click(eshinePageSearchButtonXpth, WaitLogic.CLICKABLE, "Click on search Button");
		WaitForMiliSec(10000);
		return this;
	}

	public WebPages navigateToProductLinkPage(String ValueSerach) {
		WaitForMiliSec(5000);
		WebElement ProductLinkXpth = DriverManager.getDriver()
				.findElement(getElementByReplaceText(productSearchXpath, ValueSerach));
		String ProductLink = ProductLinkXpth.getAttribute("href");

		WaitForMiliSec(3000);
		/*
		 * WebDriver newTab
		 * =DriverManager.getDriver().switchTo().newWindow(WindowType.TAB);
		 * WaitForMiliSec(8000); newTab.get(ProductLink);
		 */
		DriverManager.getDriver().findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL + "t");
		WaitForMiliSec(3000);
		DriverManager.getDriver().get(ProductLink);
		Set<String> windowHandles = DriverManager.getDriver().getWindowHandles();

		System.out.println(windowHandles.size());

		for (String winHandle : DriverManager.getDriver().getWindowHandles()) {

			DriverManager.getDriver().switchTo().window(winHandle);

		}

		DriverManager.getDriver().navigate().to(ProductLink);
		WaitForMiliSec(10000);
		return this;
	}

	public String getPageTitleForproduct() {
		return getPageTitle();
	}
	public String getMetaDataDescriptionForproduct() {
		String MetaData_MetaDescription = DriverManager.getDriver().findElement(MetaData_MetaDescriptionValueXpath).getAttribute("content");
		return MetaData_MetaDescription;
	}
	public String getMetaKeywordForproduct() {
		String MetaData_MetaKeyword = DriverManager.getDriver().findElement(MetaData_MetaKeywordValueXpath).getAttribute("content");
		return MetaData_MetaKeyword;
	}
	public String getDescriptionForproduct() {
		String Description = DriverManager.getDriver().findElement(DescriptionValueXpath).getText();
		return Description;
	}
	public String getH1TagForproduct() {
		String H1tag = DriverManager.getDriver().findElement(H1tagXpath).getText();
		return H1tag;
	}
	

	/*
	 * public boolean verifyExpectedDataIsAvailableForMetaKeyword(String
	 * MetaKeyword) {
	 * 
	 * boolean elementAvailability = true; try {
	 * DriverManager.getDriver().findElement(getElementByReplaceText(
	 * MetaData_MetaKeywordValueXpath, MetaKeyword)); elementAvailability = true;
	 * 
	 * } catch (Exception e) { elementAvailability = false; } return
	 * elementAvailability;
	 * 
	 * }
	 * 
	 * 
	 * public boolean verifyExpectedDataIsAvailableForMetaDescription(String
	 * MetaDescription) {
	 * 
	 * boolean elementAvailability = true; try {
	 * DriverManager.getDriver().findElement(getElementByReplaceText(
	 * MetaData_MetaDescriptionValueXpath, MetaDescription)); elementAvailability =
	 * true;
	 * 
	 * } catch (Exception e) { elementAvailability = false; } return
	 * elementAvailability;
	 * 
	 * } public boolean verifyExpectedDataIsAvailableForDescription(String
	 * Description) {
	 * 
	 * boolean elementAvailability = true; try {
	 * DriverManager.getDriver().findElement(getElementByReplaceText(
	 * DescriptionValueXpath, Description)); elementAvailability = true;
	 * 
	 * } catch (Exception e) { elementAvailability = false; } return
	 * elementAvailability;
	 * 
	 * } public boolean verifyExpectedDataIsAvailableForH1tag(String H1Tag) {
	 * 
	 * boolean elementAvailability = true; try {
	 * DriverManager.getDriver().findElement(getElementByReplaceText(H1tagXpath,
	 * H1Tag)); elementAvailability = true;
	 * 
	 * } catch (Exception e) { elementAvailability = false; } return
	 * elementAvailability;
	 * 
	 * }
	 */
}
