package com.pim.pages;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import com.pim.driver.DriverManager;
import com.pim.enums.WaitLogic;
import com.pim.utils.Javautils;

public class CatalogTypePage extends BasePage {
	Javautils javautil = new Javautils();
	ProductDetailSearchPage productdetailspage = new ProductDetailSearchPage();

	// Catalog Tabs Locators
	public By tabDentalCatalog = By.xpath("//div[text()='Dental Catalog']");
	public By tabCanadianDentalCatalogs = By.xpath("//div[text()='Canadian Dental Catalogs']");
	public By tabMedicalCatalog = By.xpath("//div[text()='Medical Catalog']");
	public By tabCanadianMedicalCatalogs = By.xpath("//div[text()='Canadian Medical Catalogs']");
	public By tabZahnCatalog = By.xpath("//div[text()='Zahn Catalog']");
	public By tabCanadianZahnCatalogs = By.xpath("//div[text()='Canadian Zahn Catalogs']");
	public By tabSpecialMarketsCatalog = By.xpath("//div[text()='Special Markets Catalog']");

	// Catalog Attributes Locators for directly sending the input in to fields
	public By userDrivenField = By
			.xpath("(//span[text()='User Driven:']//following::td[@class='v-formlayout-contentcell'])[1]//div[2]");
	public By exceptionListField = By
			.xpath("((//span[text()='User Driven:']//following::td[@class='v-formlayout-contentcell'])[2]//div)[2]");
	public By publishFlagField = By.xpath(
			"(//span[contains(text(),'Publish Flag')]/ancestor::td/following-sibling::td)[2]//div[@class='v-label v-widget v-has-width']");
	public By publishDateField = By
			.xpath("(//span[text()='Publish Date:']//following::td)[2]//div[contains(@class,'v-label')]");
	public By userExceptionTextField = By.xpath("(//input[@class='v-filterselect-input'])[3]");
	public By userExceptionValue = By.xpath("//td[@class='gwt-MenuItem gwt-MenuItem-selected']");
	public By ruleDrivenFieldValue = By.xpath(
			"(//div[contains(@class, 'v-caption v-caption-hpmw-detailFormLabel v-caption-hasdescription')]/ancestor::td/following-sibling::td)[2]//div[@class='v-label v-widget hpmw-valueLabel v-label-hpmw-valueLabel v-label-undef-w']");

	public By userDrivenvalue = By.xpath(
			"(//span[contains(text(),'User Driven')]/ancestor::td/following-sibling::td)[2]//div[@class='v-label v-widget hpmw-valueLabel v-label-hpmw-valueLabel v-label-undef-w']");
	public By userDrivenDropdownValue = By.xpath("//div[@class='v-filterselect-suggestmenu']//tr//span");

	public By publishDateTextField = By.xpath("//input[@class='v-textfield v-widget v-textfield-focus']");

	// User driven field dropdown and values
	public By userDrivenDropdown = By.xpath("(//input[@class='v-filterselect-input'])[2]");
	public By userDrivenValue = By.xpath("//td[@class='gwt-MenuItem gwt-MenuItem-selected']");

	// Rule driven field dropdown and values
	public By RuleDrivenField = By
			.xpath("(//span[text()='Rule Driven:']//following::td[@class='v-formlayout-contentcell'])[1]");
	// catalog field values dropdown
	public By FieldDropDown = By.xpath("//table//div[@class='v-filterselect-button']");

	// catalog field values dropdown for publishFlag
	private By publishFlagDropDownValues = By.xpath("(//*[@role=\"listitem\"])[2]");

	// catalog field values dropdown for PublishDate
	public By PublishDateFieldDropDown = By
			.xpath("//*[text()='Publish Date:']/ancestor::tr//span[@class=\"v-button-wrap\"]");
	private By PublishDateTodayValue = By.xpath("//*[contains(text(),'Today')]");
	private By PublishDateApplyValue = By.xpath("//*[contains(text(),'Apply')]");

	private By fieldDropDownValues = By.xpath("//*[@role=\"listitem\"][1]");

	// Clearing Field Values
	private By clearUserDrivenSelectedCatalogs = By.xpath(
			"(//span[text()='User Driven:']//following::td[@class='v-formlayout-contentcell'])[1]//div[@class='v-slot']/div/div[1]/div/div[2]/div");
	private By listOfUserDrivenSelectedCatalogs = By.xpath(
			"(//span[text()='User Driven:']//following::td[@class='v-formlayout-contentcell'])[1]//div[@class='v-slot']/div/div/div/div[2]/div");
	private By clearExceptionListSelectedCatalogs = By.xpath(
			"(//span[text()='Exception List:']//following::td[@class='v-formlayout-contentcell'])[1]//div[@class='v-slot']/div/div[1]/div/div[2]/div");
	private By listofExceptionListSelectedcatalogs = By.xpath(
			"(//span[text()='Exception List:']//following::td[@class='v-formlayout-contentcell'])[1]//div[@class='v-slot']/div/div/div/div[2]/div");
	private String crossIconForUserDriven = "(//span[contains(text(),'User Driven:')]/ancestor::td/following-sibling::td)[2]//div[contains(text(),'${variable}')]/parent::div/following-sibling::div//div";
	private String crossIconForExceptionList = "(//span[contains(text(),'Exception List:')]/ancestor::td/following-sibling::td)[2]//div[contains(text(),'${variable}')]/parent::div/following-sibling::div//div";

	private By BypublishFlagDropdown = By.xpath("(//div[@class='v-filterselect-button'])[4]");
	private By publishNo = By.xpath("//span[contains(text(),'No')]");
	private By publishYes = By.xpath("//span[contains(text(),'Yes')]");
	private String publishflag = "//span[contains(text(),'${variable}')]";
	private By ruleDrivenEmptyField = By.xpath(
			"(//td[@class='v-formlayout-contentcell']//div[@class='v-panel-content v-panel-content-light v-scrollable'])[1]//div[text()='No content']");
	private By exceptionListValues = By.xpath(
			"(//span[contains(text(),'Exception List')]/ancestor::td/following-sibling::td)[2]//div[@class='v-label v-widget hpmw-valueLabel v-label-hpmw-valueLabel v-label-undef-w']");

	public CatalogTypePage clickUserDriven() {
		WaitForMiliSec(8000);
		click(userDrivenField, WaitLogic.CLICKABLE, "click UserDriven");
		return this;
	}

	public CatalogTypePage selectUserDriven(String userdriven) {
		WaitForMiliSec(3000);
		for (String usercatalog : javautil.readMultipleValuesFromExcel(userdriven)) {
			WaitForMiliSec(5000);
			sendKeys(userExceptionTextField, usercatalog, WaitLogic.VISIBLE, "UserDriven");
			WaitForMiliSec(2000);
			click(userExceptionValue, WaitLogic.CLICKABLE, "Select UserDriven");
			WaitForMiliSec(7000);
		}
		return this;
	}

	public CatalogTypePage clickExceptionListField() {
		click(exceptionListField, WaitLogic.CLICKABLE, "click UserDriven");
		return this;
	}

	public CatalogTypePage selectPublishFlag(String flag) {
		click(publishFlagField, WaitLogic.CLICKABLE, "click publish flag");
		WaitForMiliSec(3000);
		click(BypublishFlagDropdown, WaitLogic.CLICKABLE, "click publish flag");
		click(getElementByReplaceText(publishflag, flag), WaitLogic.CLICKABLE, "click publish flag");
		return this;
	}

	public CatalogTypePage selectPublishDate(String date) {
		click(publishDateField, WaitLogic.CLICKABLE, "click PublishDate field");
		sendKeys(publishDateTextField, date, WaitLogic.VISIBLE, "PublishDate");
		return this;
	}

	public List<String> getRuleDrivenCatalogs() {
		WaitForMiliSec(5000);
		List<WebElement> listSize = DriverManager.getDriver().findElements(ruleDrivenFieldValue);
		List<String> listRuleDriven = new ArrayList<String>();
		for (WebElement catalogRules : listSize) {
			String getruledriven = catalogRules.getText().trim();
			listRuleDriven.add(getruledriven);
		}
		return listRuleDriven;
	}
	

	public CatalogTypePage verifySelectUserdrivenDropdown() {
		click(userDrivenField, WaitLogic.CLICKABLE, "UserDrivenField");
		WaitForMiliSec(5000);
		clearSelectedFields(listOfUserDrivenSelectedCatalogs, clearUserDrivenSelectedCatalogs);
		WaitForMiliSec(5000);
		click(FieldDropDown, WaitLogic.CLICKABLE, "UserDrivenFieldDropDown");
		WaitForMiliSec(5000);
		click(fieldDropDownValues, WaitLogic.CLICKABLE, "UserDrivenFieldDropDownValues_1");
		WaitForMiliSec(5000);
		click(FieldDropDown, WaitLogic.CLICKABLE, "UserDrivenFieldDropDown");
		WaitForMiliSec(5000);
		click(fieldDropDownValues, WaitLogic.CLICKABLE, "UserDrivenFieldDropDownValues_2");
		WaitForMiliSec(5000);
		return this;
	}

	public CatalogTypePage verifySelectExceptionListDropdown() {
		click(exceptionListField, WaitLogic.CLICKABLE, "ExceptionListField");
		WaitForMiliSec(5000);
		clearSelectedFields(listofExceptionListSelectedcatalogs, clearExceptionListSelectedCatalogs);
		WaitForMiliSec(5000);
		click(FieldDropDown, WaitLogic.CLICKABLE, "ExceptionListFieldDropDown");
		WaitForMiliSec(5000);
		click(fieldDropDownValues, WaitLogic.CLICKABLE, "ExceptionListFieldDropDownValues_1");
		WaitForMiliSec(5000);
		click(FieldDropDown, WaitLogic.CLICKABLE, "ExceptionListFieldDropDown");
		WaitForMiliSec(5000);
		click(fieldDropDownValues, WaitLogic.CLICKABLE, "ExceptionListFieldDropDownValues_2");
		WaitForMiliSec(5000);
		return this;
	}

	public CatalogTypePage removingTheExceptionField() {
		WaitForMiliSec(5000);
		click(exceptionListField, WaitLogic.CLICKABLE, "ExceptionListField");
		WaitForMiliSec(5000);
		clearSelectedFields(listofExceptionListSelectedcatalogs, clearExceptionListSelectedCatalogs);
		WaitForMiliSec(5000);
		return this;
	}

	public CatalogTypePage verifySelectPublishFlagFieldDropdown() {
		click(publishFlagField, WaitLogic.CLICKABLE, "PublishFlagField");
		WaitForMiliSec(5000);
		click(FieldDropDown, WaitLogic.CLICKABLE, "PublishFlagFieldDropDown");
		WaitForMiliSec(5000);
		click(publishFlagDropDownValues, WaitLogic.CLICKABLE, "PublishFlagFieldDropDownValues");
		return this;
	}

	public CatalogTypePage verifySelectPublishDateFieldDropdown() {
		click(publishDateField, WaitLogic.CLICKABLE, "PublishDateField");
		WaitForMiliSec(5000);
		click(PublishDateFieldDropDown, WaitLogic.CLICKABLE, "PublishDateFieldDropDown");
		WaitForMiliSec(5000);
		click(PublishDateTodayValue, WaitLogic.CLICKABLE, "PublishDateTodayValue");
		WaitForMiliSec(5000);
		click(PublishDateApplyValue, WaitLogic.CLICKABLE, "PublishDateTodayValue");
		WaitForMiliSec(5000);
		return this;
	}

	// For Catalog field "Field visible"

	public boolean isCatalogFieldVisible(String fieldname) {
		By by = null;
		try {
			by = (By) CatalogTypePage.class.getField(fieldname).get(CatalogTypePage.this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isVisible(by, fieldname);
	}

	// For Catalog field "Field Editable"
	public boolean isCatalogFieldEditable(String fieldname, String visibilityElement) {

		By by = null;

		try {
			by = (By) CatalogTypePage.class.getField(fieldname).get(CatalogTypePage.this);
			click(by, WaitLogic.CLICKABLE, fieldname);
			by = (By) CatalogTypePage.class.getField(visibilityElement).get(CatalogTypePage.this);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return (isVisible(by, fieldname + visibilityElement, 5));
	}

	public void clearSelectedFields(By listOfSelectedCatalogs, By clearSelectedCatalogs) {

		WaitForMiliSec(5000);
		List<WebElement> webElements = DriverManager.getDriver().findElements(listOfSelectedCatalogs);
		System.out.println("SizeOfSelectedElement " + webElements.size());
		if (webElements != null) {

			for (int i = 1; i <= webElements.size(); i++) {
				WaitForMiliSec(5000);
				DriverManager.getDriver().findElement(clearSelectedCatalogs).click();
			}
		}
	}

	public CatalogTypePage selectExceptionCatalogsFromList(String exceptionList) {
		sendKeys(userExceptionTextField, exceptionList, WaitLogic.PRESENCE, "Exception List");
		click(userExceptionValue, WaitLogic.CLICKABLE, "Select Exception List");
		return this;
	}

	public List<String> getUserDrivenCatalogs() {
		WaitForMiliSec(5000);
		List<WebElement> listSize = DriverManager.getDriver().findElements(userDrivenvalue);
		List<String> listUserDriven = new ArrayList<String>();
		for (WebElement catalogRules : listSize) {
			String getuserdriven = catalogRules.getText().trim();
			listUserDriven.add(getuserdriven);
		}
		WaitForMiliSec(3000);
		return listUserDriven;

	}

	public List<String> getExceptionListCatalogs() {
		List<WebElement> listSize = DriverManager.getDriver().findElements(exceptionListValues);
		List<String> listUserDriven = new ArrayList<String>();
		for (WebElement catalogRules : listSize) {
			String getuserdriven = catalogRules.getText().trim();
			listUserDriven.add(getuserdriven);
		}
		return listUserDriven;

	}

	public void clearSelectedFields(String userdriven) {
		WaitForMiliSec(3000);
		click(userDrivenField, WaitLogic.CLICKABLE, "click UserDriven");
		click(getElementByReplaceText(crossIconForUserDriven, userdriven), WaitLogic.CLICKABLE, "Delete userdriven");
	}

	public CatalogTypePage clearUserDriven(String userdriven) {
		List<String> userdrivencatalogs = getUserDrivenCatalogs();
		List<String> multiplevaluesfromexcel = javautil.readMultipleValuesFromExcel(userdriven);
		for (String catalog : multiplevaluesfromexcel) {
			if (userdrivencatalogs.contains(catalog)) {
				WaitForMiliSec(3000);
				click(userDrivenField, WaitLogic.CLICKABLE, "click UserDriven");
				click(getElementByReplaceText(crossIconForUserDriven, catalog), WaitLogic.CLICKABLE,
						"Delete userdriven");
				productdetailspage.clickRefreshIcon();
			}
		}
		return this;
	}

	public void clearExceptionListField(String userdriven) {
		click(exceptionListField, WaitLogic.CLICKABLE, "click UserDriven");
		click(getElementByReplaceText(crossIconForExceptionList, userdriven), WaitLogic.CLICKABLE,
				"Delete exceptionlist");
	}

	public CatalogTypePage selectPublishFlag() {
		WaitForMiliSec(3000);
		String pflag = getStringValues(publishFlagField, WaitLogic.VISIBLE, "Publish flag");
		System.out.println(pflag);
		if (pflag.contains("Yes")) {
			System.out.println("Yes");
			click(publishFlagField, WaitLogic.CLICKABLE, "click publish flag");
			WaitForMiliSec(3000);
			click(BypublishFlagDropdown, WaitLogic.CLICKABLE, "click publish flag");
			click(publishNo, WaitLogic.CLICKABLE, "click publish No flag");
		} else {
			System.out.println("No");
			click(publishFlagField, WaitLogic.CLICKABLE, "click publish flag");
			WaitForMiliSec(3000);
			click(BypublishFlagDropdown, WaitLogic.CLICKABLE, "click publish flag");
			click(publishYes, WaitLogic.CLICKABLE, "click publish Yes flag");
		}
		return this;
	}

	public String getPublishDate() {
		String pdate = getStringValues(publishDateField, WaitLogic.VISIBLE, "Publish Date");
		return pdate;
	}

	public String getPublishFlag() {
		String pflag = getStringValues(publishFlagField, WaitLogic.VISIBLE, "Publish flag");
		return pflag;
	}

	public List<String> getUserDrivenCatalogsDropdownValues() {
		WaitForMiliSec(5000);
		click(FieldDropDown, WaitLogic.CLICKABLE, "field dropdown");
		WaitForMiliSec(4000);
		List<WebElement> listSize = DriverManager.getDriver().findElements(userDrivenDropdownValue);
		List<String> listUserDriven = new ArrayList<String>();
		for (WebElement catalogRules : listSize) {
			String getuserdriven = catalogRules.getText().trim();
			listUserDriven.add(getuserdriven);
		}
		return listUserDriven;
	}

	public String getEmptyRuleDriven() {
		String empty_rule_driven = getStringValues(ruleDrivenEmptyField, WaitLogic.VISIBLE, "Rule driven");
		return empty_rule_driven;
	}

}