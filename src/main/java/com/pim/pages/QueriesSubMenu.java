package com.pim.pages;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import com.pim.driver.DriverManager;
import com.pim.enums.WaitLogic;
import com.pim.utils.DateandTimeUtils;

public class QueriesSubMenu extends BasePage {

	private final By itemDropdown = By.xpath("//*[@id='search-select-menu-%entity.Article.name']");
	private final By catalogItemDropdown = By.xpath("(//div[contains(@class,'v-filterselect v-widget')]//div[@role='button'])[1]");
	private final By itemSearchNumber = By
			.xpath("//div[@class='v-filterselect v-widget v-filterselect-prompt']//input[@type='text']");
	private final By itemNumberTextLabel = By.xpath("//div/input[@class='v-filterselect-input']");
	private final By searchButton = By
			.xpath("//*[@class='v-button v-widget v-has-width']//span[@class='v-button-wrap']");
	private final By searchItemDropDown = By.xpath("(//td[@class=\"gwt-MenuItem\"])[1]");

	// private final By removingNumber = By.xpath("//span[contains(text(),'
	// �')]");
	private final By removingNumber = By.xpath("//span[contains(text(),' ×')]");

	private final String itemTypeXpath = "//span[contains(text(),'${variable}')]";
	private final String itemCatalogTypeXpath = "//span[normalize-space()='${variable}']";
	private final By hsiitemnumbertext = By.xpath("//span[contains(text(),'HSI Item')]");
	private final By itemCreateBetTwoDates = By.xpath("(//span[contains(text(),'Items Created Between Two Dates')])[1]");
	private final By itemCreationDateGreater = By.xpath("(//span[contains(text(),'Item Creation Date')]/ancestor::div[@class=\"v-caption v-caption-tokenfield\"]/following-sibling::div//span[@class='v-button-wrap']//span)[1]");
    private final By itemCreationDateGreaterTextField = By.xpath("(//span[contains(text(),'Item Creation Date')]/ancestor::div[@class='v-caption v-caption-tokenfield']/following-sibling::div//input)[1]");
	private final By itemCreationDateLess = By.xpath("(//span[contains(text(),'Item Creation Date')]/ancestor::div[@class=\"v-caption v-caption-tokenfield\"]/following-sibling::div//span[@class='v-button-wrap']//span)[2]");
    private final By itemCreationDateLessTextField = By.xpath("(//span[contains(text(),'Item Creation Date')]/ancestor::div[@class='v-caption v-caption-tokenfield']/following-sibling::div//input)[2]");
	private final By click = By.xpath("(//input[@class='v-filterselect-input'])[1]");
	private final String itemNumberAsPerJDEDesc = ("//div[contains(text(), '${variable}')]/../../td[2]/div");
	
	public QueriesSubMenu selectItemType(String itemType) {
		WaitForMiliSec(3000);
		click(itemDropdown, WaitLogic.CLICKABLE, "itemDropdown");
		WaitForMiliSec(3000);
		click(getElementByReplaceText(itemTypeXpath, itemType), WaitLogic.CLICKABLE, "itemType");
		return this;
	}

	public QueriesSubMenu selectCatalogType(String CatalogType) {
		WaitForMiliSec(1000);
		click(catalogItemDropdown, WaitLogic.CLICKABLE, "catalogItemDropdown");
		WaitForMiliSec(1000);
		click(getElementByReplaceText(itemCatalogTypeXpath, CatalogType), WaitLogic.CLICKABLE, "itemCatalogType");
		return this;
	}

	public QueriesSubMenu removeAndEnterHsiItemNumber(String itemnuber) {
		try {
			WaitForMiliSec(3000);
			DriverManager.getDriver().findElement(removingNumber).click();
			WaitForMiliSec(1000);
			DriverManager.getDriver().findElement(itemSearchNumber).sendKeys(itemnuber, Keys.ENTER);
			WaitForMiliSec(3000);
			try {
				WaitForMiliSec(3000);
				DriverManager.getDriver().findElement(searchItemDropDown).click();
				WaitForMiliSec(3000);
			} catch (Exception e) {
				WaitForMiliSec(3000);
			}
		} catch (Exception e) {
			WaitForMiliSec(3000);
			sendKeys(itemSearchNumber, itemnuber, WaitLogic.CLICKABLE, "itemSearchNumber");
			WaitForMiliSec(3000);
		}
		return new QueriesSubMenu();
	}

	public QueriesSubMenu enterHsiItemNumber(String itemnumber) {
		click(itemNumberTextLabel, WaitLogic.VISIBLE, "HSI Item Number");
		sendKeys(itemSearchNumber, itemnumber, WaitLogic.PRESENCE, "itemSearchNumber");
		WaitForMiliSec(3000);
		return new QueriesSubMenu();

	}

	public ProductDetailSearchPage clickSeachButton() {
		click(click, WaitLogic.VISIBLE, "Enter HSI number Field Label ");
		BasePage.WaitForMiliSec(5000);
		click(searchButton, WaitLogic.CLICKABLE, "searchButton");
		return new ProductDetailSearchPage();
	}

	public void clickHsiItemText() {
		click(hsiitemnumbertext, WaitLogic.CLICKABLE, "hsi item text");
	}
	
	public QueriesSubMenu selectItemCreatedBetweenTwoDates() {
		click(itemCreateBetTwoDates, WaitLogic.VISIBLE, "item created between 2 dates");
		return this;
	}
	
	public QueriesSubMenu assignItemCreationDateGreater(String date) {
//		click(itemCreationDateGreater, WaitLogic.CLICKABLE, "previously assigned date");
		WaitForMiliSec(3000);
		sendKeys(itemCreationDateGreaterTextField, date + " 12:00 AM", WaitLogic.PRESENCE, "item creation date greater");
		DriverManager.getDriver().findElement(itemCreationDateGreaterTextField).sendKeys(Keys.ENTER);
		return this;
	}
	
	public QueriesSubMenu assignItemCreationDateLess(String date) {
//		click(itemCreationDateLess, WaitLogic.CLICKABLE, "previously assigned date");
		WaitForMiliSec(3000);
		sendKeys(itemCreationDateLessTextField, date + " 12:00 AM", WaitLogic.PRESENCE, "item creation date less");
		DriverManager.getDriver().findElement(itemCreationDateGreaterTextField).sendKeys(Keys.ENTER);
		return this;
	}
	
	public List<String> getItemNumberAsPerGivenJDEDescription(String givenJDEDescription) {
		PimHomepage pimHomepage = new PimHomepage();
		List<String> al_ActualRuleNames = new ArrayList<>();
		int counter = 0;
		do {
			WaitForMiliSec(3000);
//			pimHomepage.productDetailSearchPage().clickRefreshIcon();
			try {
				al_ActualRuleNames.addAll(getItemNumberAsPerJDEDescription(givenJDEDescription));
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			counter++;
		} while (counter <= 3 &&  al_ActualRuleNames.isEmpty());
		return al_ActualRuleNames;
	}
	
	
	private Set<String> getItemNumberAsPerJDEDescription(String givenJDEDescription) {

		WaitForMiliSec(5000);
		System.out.println(getElementByReplaceText(itemNumberAsPerJDEDesc, givenJDEDescription));
		WaitForMiliSec(5000);
		Set<String> al_ActualRuleNames = new HashSet<>();
		for (int i = 0; i < 3; i++) {
			List<WebElement> listOfRulesWebElements = DriverManager.getDriver()
					.findElements(getElementByReplaceText(itemNumberAsPerJDEDesc, givenJDEDescription));

			for (int j = 0; j < listOfRulesWebElements.size(); j++) {
				String ruleName = listOfRulesWebElements.get(j).getText();
				al_ActualRuleNames.add(ruleName);

				if (j == listOfRulesWebElements.size() - 1) {
					System.out.println("Scroll Start");
					WebElement element = listOfRulesWebElements.get(j);
					((JavascriptExecutor) DriverManager.getDriver()).executeScript("arguments[0].scrollIntoView(true);",
							element);
					WaitForMiliSec(5000);
					System.out.println("Scroll Done");
				}
			}

		}
		System.out.println(al_ActualRuleNames.size());
		System.out.println(al_ActualRuleNames);
		 return al_ActualRuleNames;

	}
}
