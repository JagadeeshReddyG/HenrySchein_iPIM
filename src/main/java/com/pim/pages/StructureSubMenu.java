package com.pim.pages;

import static com.pim.reports.FrameworkLogger.log;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import com.pim.driver.DriverManager;
import com.pim.enums.LogType;
import com.pim.enums.WaitLogic;

public class StructureSubMenu extends BasePage {

	private final By catalogDropDown = By.xpath("//div[@id='catalog_select_menu']//div[@role='button']");
	private final By taxnomyDropDown = By.xpath("//div[@id='system_select_menu']//div[@role='button']");
	private final By itemTypeDropDown = By.xpath("//div[@id='entiy_mode_select_menu']//div[@role='button']");
	private final String catalogTypeXpath = "//span[normalize-space()='${variable}']";
	private final String classificationtypeXpath = "//span[normalize-space()='${variable}']";
	private final String itemtypeXpath = "//span[normalize-space()='${variable}']";
	private final String selectedTaxnomyDropdownXpath = "//*[contains(text(),'${variable}')]";
	private final String errorOrWarningXpath = "//div[contains(text(),'${variable}')]/span[contains(@class,'v-treetable-treespacer')]";
	private final String dentalDropDownXpath = "//div[contains(text(),'${variable}')]/..";
	private final By firstItemCode = By
			.xpath("(//td[@class='v-table-cell-content v-table-cell-content-rowheader']/..)[1]");
	private final By secondItemCode = By
			.xpath("(//td[@class='v-table-cell-content v-table-cell-content-rowheader']/..)[2]");
	private final By thirdItemCode = By
			.xpath("(//td[@class='v-table-cell-content v-table-cell-content-rowheader']/..)[3]");
	private final By taxnomyDropDownOfClassificationPopUpXpath = By.xpath(
			"//div[text()='Please select a destination structure group.']/../..//div[@id='system_select_menu']/input/following-sibling::div");
	private final By taxnomyClassificationNextPage = By.xpath("//div[@class='v-filterselect-nextpage']");
	private final String selecttaxnomyOfClassificationPopUpXpath = "//td[contains(@class,'gwt-MenuItem')]/span[text()='${variable}']";
	private final By filterTextfieldOfClassificationPopUpCSS = By.cssSelector(".popupContent .v-textfield-prompt");
	private final String selectDentalErrorMessageXpath = "//span[text()='${variable}']";
	private final By showAllXpath = By.xpath("//div[text()='show all']");
	private final String SubSetDropDownXpath = ("//td[contains(.,'${variable}')]");

	// locators to select main and sub category for different categories in the
	// structures group

	private final String SepcificItemCodeXpath = "//td[@class='v-table-cell-content']//div[contains(text(),'${variable}')]";

	private final String mainCategoryDropdown = "//td[contains(.,'${variable}')]//div//span[1]";
	private final String subCategoryDropdown = "(//div[contains(text(),'${variable}')])[1]";

	// Locators for Filter by Search group name
	private final By filterBySerachGroupName = By
			.xpath("//input[@class='v-textfield v-widget v-has-width v-textfield-prompt']");
	private final By filterBySerachGroupNameInputValue = By
			.xpath("//input[@class='v-textfield v-widget v-has-width v-textfield-focus']");
	private final By filterBySerachGroupNamevaluePresent = By
			.xpath("//input[@class='v-textfield v-widget v-has-width']");
	private final String errorMsg = "//div[text()='${variable}']";


	public StructureSubMenu selectCatalogType(String CatalogType) {
		click(catalogDropDown, WaitLogic.CLICKABLE, "catalogDropDown");
		click(getElementByReplaceText(catalogTypeXpath, CatalogType), WaitLogic.CLICKABLE, "itemCatalogType");
		WaitForMiliSec(5000);
		return this;

	}

//	public StructureSubMenu selectClassificationType(String classificationtype) {
//			click(taxnomyDropDown, WaitLogic.CLICKABLE, "taxnomyDropDown");
//			click(getElementByReplaceText(classificationtypeXpath, classificationtype), WaitLogic.CLICKABLE,
//					"classificationtype");
//			WaitForMiliSec(000);
//		return this;
//	}
	
	public StructureSubMenu selectClassificationType(String classificationtype) {
		try{
			click(taxnomyDropDown, WaitLogic.CLICKABLE, "taxnomyDropDown");
			WaitForMiliSec(3000);
			click(getElementByReplaceText(classificationtypeXpath, classificationtype), WaitLogic.NONE, "classificationtype");
			WaitForMiliSec(2000);
		}
		catch(Exception e) {
			clickOnSelectedTaxnomyTypeForzNewGlobalMessage(classificationtype);
		}
		return this;
	}
	
	public StructureSubMenu clickOnSelectedTaxnomyTypeForzNewGlobalMessage(String classificationtype) {
		WaitForMiliSec(3000);
		click(taxnomyClassificationNextPage, WaitLogic.CLICKABLE, "taxnomyClassificationTextField");
		click(getElementByReplaceText(selectedTaxnomyDropdownXpath, classificationtype), WaitLogic.CLICKABLE,
				"SelectedTaxnomyType");
		WaitForMiliSec(2000);
		return this;
	}

	public StructureSubMenu selectItemType(String itemtype) {
		log(LogType.EXTENTANDCONSOLE, itemtype + " is to be selected");
		click(itemTypeDropDown, WaitLogic.CLICKABLE, "itemTypeDropDown");
		click(getElementByReplaceText(itemtypeXpath, itemtype), WaitLogic.CLICKABLE, "itemtype");
		WaitForMiliSec(5000);
		return this;
	}

	public StructureSubMenu clickOnSelectedTaxnomyType(String selectedTaxnomyType) {
			click(getElementByReplaceText(selectedTaxnomyDropdownXpath, selectedTaxnomyType), WaitLogic.CLICKABLE,
					"SelectedTaxnomyType");
			WaitForMiliSec(2000);
		return this;
	}
	
	public StructureSubMenu selectErrorOrWarning(String selectErrorOrWarning) {
		click(getElementByReplaceText(errorOrWarningXpath, selectErrorOrWarning), WaitLogic.CLICKABLE,
				"selectErrorOrWarning");
		return this;
	}

	public StructureSubMenu clickOnfirstSubsetDropdown(String SubsetDropDown) {
		click(getElementByReplaceText(SubSetDropDownXpath, SubsetDropDown), WaitLogic.CLICKABLE, "SubsetDropDown");
		return this;
	}

	public StructureSubMenu clickOnFirstItemCode() {
		WaitForMiliSec(5000);
		moveToElementAndClick(firstItemCode, "firstItemCode");
//		click(firstItemCode, WaitLogic.CLICKABLE, "firstItemCode");
		WaitForMiliSec(2000);
		return this;
	}

//	public StructureSubMenu clickOnTaxnomyDropDownOfClassificationPopUp(String taxnomyDropDownOfClassification) {
//		click(taxnomyDropDownOfClassificationPopUpXpath, WaitLogic.CLICKABLE, "taxnomyDropDownOfClassificationPopUp");
//		click(getElementByReplaceText(selecttaxnomyOfClassificationPopUpXpath, taxnomyDropDownOfClassification),
//				WaitLogic.CLICKABLE, "selecttaxnomyOfClassificationPopUp");
//		WaitForMiliSec(10000);
//		return this;
//	}

	public StructureSubMenu clickOnTaxnomyDropDownOfClassificationPopUp(String taxnomyDropDownOfClassification) {
		try{
			click(taxnomyDropDownOfClassificationPopUpXpath, WaitLogic.CLICKABLE, "taxnomyDropDownOfClassificationPopUp");
			WaitForMiliSec(3000);
			click(getElementByReplaceText(selecttaxnomyOfClassificationPopUpXpath, taxnomyDropDownOfClassification),
					WaitLogic.NONE, "selecttaxnomyOfClassificationPopUp");
			WaitForMiliSec(2000);
		}
		catch(Exception e) {
			clickOnTaxnomyDropDownForzNewGlobalMessage(taxnomyDropDownOfClassification);
		}
		return this;
	}
	
	public StructureSubMenu clickOnTaxnomyDropDownForzNewGlobalMessage(String taxnomyDropDownOfClassification) {
		WaitForMiliSec(2000);
		click(taxnomyClassificationNextPage, WaitLogic.CLICKABLE, "taxnomyClassificationTextField");
		click(getElementByReplaceText(selecttaxnomyOfClassificationPopUpXpath, taxnomyDropDownOfClassification), WaitLogic.CLICKABLE, "selecttaxnomyOfClassificationPopUp");
		WaitForMiliSec(5000);
		return this;
	}

	public StructureSubMenu sendKeysToFilterTextFieldOfClassificationPopup(String filterTextField) {
		sendKeys(filterTextfieldOfClassificationPopUpCSS, filterTextField, WaitLogic.CLICKABLE,
				"Division Type Text Feild ");
		return this;
	}

	public StructureSubMenu selectDivisionErrorOrWarningMessage(String selectDivisionErrorOrWarningMessage) {
		click(getElementByReplaceText(selectDentalErrorMessageXpath, selectDivisionErrorOrWarningMessage),
				WaitLogic.CLICKABLE, "selectDivisionErrorOrWarningMessage");
		WaitForMiliSec(5000);
		return this;
	}

	public StructureSubMenu clickOnShowAll() {
		click(showAllXpath, WaitLogic.CLICKABLE, "showAll");
		return this;
	}

	/*
	 * public StructureSubMenu selectSpecificItem(String specificItemCode) {
	 * 
	 * WaitForMiliSec(30000);
	 * scrollToElement(getElementByReplaceText(SepcificItemCodeXpath,
	 * specificItemCode),"Sepcific Element clicked");
	 * click(getElementByReplaceText(SepcificItemCodeXpath, specificItemCode),
	 * WaitLogic.CLICKABLE,"specificItemCode"); WaitForMiliSec(1000); return this;
	 * 
	 * }
	 */
	
	
	public StructureSubMenu selectSpecificItem(String specificItemCode) {
		for (int i = 0; i < 10; i++) {
			if (isVisible(getElementByReplaceText(SepcificItemCodeXpath, specificItemCode), "Specific item code")) {
				WaitForMiliSec(3000);
				click(getElementByReplaceText(SepcificItemCodeXpath, specificItemCode), WaitLogic.CLICKABLE,"specificItemCode");
				
				break;
			}
			else {
				System.out.println("Scroll Start");
				WebElement element = DriverManager.getDriver().findElement(By.xpath("//table[@class='v-table-row']//tr[last()]//td[3]/div"));
				((JavascriptExecutor) DriverManager.getDriver()).executeScript("arguments[0].scrollIntoView(true);",
						element);
				WaitForMiliSec(5000);
				System.out.println("Scroll Done");
			}
				
			}
		return this;
			
		}
		


	// To select MainCategory of selected category in the structures group
	public StructureSubMenu selectMainCategory(String mainCategory) {
		WaitForMiliSec(2000);
		click(getElementByReplaceText(mainCategoryDropdown, mainCategory), WaitLogic.CLICKABLE, "mainCategory");
		return this;
	}

	public StructureSubMenu selectMainCategoryForScroll(String mainCategory) {

		try {
			WaitForMiliSec(3000);
			EventFiringWebDriver eventFirinfWebDriver = new EventFiringWebDriver(DriverManager.getDriver());
			eventFirinfWebDriver.executeScript(
					"document.querySelector(\"div#structure_tree>div[class='v-scrollable v-table-body-wrapper v-table-body']\").scrollTop=1000");

			click(getElementByReplaceText(mainCategoryDropdown, mainCategory), WaitLogic.CLICKABLE, "mainCategory");

		} catch (Exception e) {
			WaitForMiliSec(3000);
			EventFiringWebDriver eventFirinfWebDriver = new EventFiringWebDriver(DriverManager.getDriver());
			eventFirinfWebDriver.executeScript(
					"document.querySelector(\"div#structure_tree>div[class='v-scrollable v-table-body-wrapper v-table-body']\").scrollTop=500");

			click(getElementByReplaceText(mainCategoryDropdown, mainCategory), WaitLogic.CLICKABLE, "mainCategory");
		}

		return this;
	}

	// To select SubCategory of selected category in the structures group
	public StructureSubMenu selectSubCategory(String subCategory) {
		WaitForMiliSec(2000);
		click(getElementByReplaceText(subCategoryDropdown, subCategory), WaitLogic.CLICKABLE, "subCategory");
		return this;
	}

	public StructureSubMenu enterFilterBySerachGroupName(String filterTextField) {
		WaitForMiliSec(2000);
		try {
			click(filterBySerachGroupName, WaitLogic.CLICKABLE, "subCategory");
			sendKeys(filterBySerachGroupNameInputValue, filterTextField, WaitLogic.CLICKABLE,
					"Division Type Text Feild ");
		} catch (Exception e) {
			click(filterBySerachGroupNamevaluePresent, WaitLogic.CLICKABLE, "subCategory");
			sendKeys(filterBySerachGroupNameInputValue, filterTextField, WaitLogic.CLICKABLE,
					"Division Type Text Feild ");
		}
		WaitForMiliSec(2000);
		return this;
	}
	
	
	public StructureSubMenu searchAndSelectErrorMsg(String errorMessageText) {
		enterFilterBySerachGroupName(errorMessageText);
		WaitForMiliSec(5000);
		click(getElementByReplaceText(errorMsg, errorMessageText), WaitLogic.VISIBLE, errorMessageText);
		WaitForMiliSec(8000);
		return this;
	}
}