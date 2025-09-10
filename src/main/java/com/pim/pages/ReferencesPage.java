package com.pim.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.pim.driver.DriverManager;
import com.pim.enums.WaitLogic;
import com.pim.utils.ApplicationUtils;
import com.pim.utils.DateandTimeUtils;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReferencesPage extends BasePage {

	public final By referenceType = By
			.xpath("(//td[@class='v-table-cell-content v-table-cell-content-ReferenceType'])[1]//div");
	public final By referenceTypeAsPA = By.xpath("//td[@class='v-table-cell-content v-table-cell-content-ReferenceType']/descendant::div[text()='PA']");
	public final By referenceTypeAsSP = By.xpath("//td[@class='v-table-cell-content v-table-cell-content-ReferenceType']/descendant::div[text()='SP']");
	public final By referenceTypeAsSA = By.xpath("//td[@class='v-table-cell-content v-table-cell-content-ReferenceType']/descendant::div[text()='SA']");
	public final By referenceTypeListxpath = By
			.xpath("(//td[@class='v-table-cell-content v-table-cell-content-ReferenceType'])//div");
	private String referenceObjectNumberByEndDate = "//*[contains(text(),'${variable}')]//ancestor::tr//td[@class='v-table-cell-content v-table-cell-content-ReferencedSupplierAid']//div";
	private By referenceObjectNumber = By
			.xpath("//td[@class='v-table-cell-content v-table-cell-content-ReferencedSupplierAid']");
	private By SecondreferenceObjectNumber = By
			.xpath("(//td[@class='v-table-cell-content v-table-cell-content-ReferencedSupplierAid'])[2]");
	private By ThirdreferenceObjectNumber = By
			.xpath("(//td[@class='v-table-cell-content v-table-cell-content-ReferencedSupplierAid'])[3]");
	private By EndDatereference = By
			.xpath("(//td[@class='v-table-cell-content v-table-cell-content-Res_DateTime_02'])//div");
	private final By openExpandButtonXpath = By.cssSelector(".v-button-hpmw-expand-all-button>span");
	private String ReferenceItemNumber = "//td//div[contains(text(),'${variable}')]";

	private final By addReference = By.xpath("//span[@class='v-menubar-menuitem v-menubar-menuitem-hpmw-add']");
	private final By referenceTypeTextFeild = By
			.xpath("//div[@class='v-filterselect v-widget v-filterselect-required v-required v-has-width']//input");
	private final By clickHereButton = By
			.xpath("//div[@class='v-label v-widget hpmw-pickwindow-link v-label-hpmw-pickwindow-link v-has-width']//a");
	private final By okAndCloseWindowButton = By.xpath("//span[contains(text(),'OK and close browser window')]");
	private final By referenceObject = By.xpath(
			"//input[@class='v-textfield v-widget hpmw-referenceTextField v-textfield-hpmw-referenceTextField v-has-width']");
	private final By referenceOKButton = By.xpath("//span[contains(text(),'OK')]");
	private final By editReference = By.xpath("//span[@class='v-menubar-menuitem v-menubar-menuitem-hpmw-edit']");
	private final By referenceTypeDropdown = By.xpath(
			"//div[@class='v-filterselect v-widget v-filterselect-required v-required v-has-width']//div[@class='v-filterselect-button']");
	private String referenceTypeOptions = "//span[contains(text(),'${variable}')]";
	private final By referenceBeginDateField = By.xpath(
			"(//div[@class='v-customcomponent v-widget hpmw-datePicker v-customcomponent-hpmw-datePicker v-has-width']//input[@class='v-textfield v-widget v-has-width'])[1]");
	private final By referenceFirstRow = By.xpath("(//table[@class='v-table-table']//tr)[1]");
	private final By referenceBeginDate = By
			.xpath("//td[@class='v-table-cell-content v-table-cell-content-Res_DateTime_01']//div");
	private final By referenceTypeHeader = By.xpath("//div[contains(text(),'Reference type')]");
	private final By deleteButton = By.xpath("//span[@class='v-menubar-menuitem v-menubar-menuitem-hpmw-remove']");
	private final By yesButton = By.xpath("(//div[@role='button' and @class='v-button v-widget'])[1]");
	private final By referenceDropDown = By.xpath("//td[@class='gwt-MenuItem gwt-MenuItem-selected']//span");

	// getting reference type
	public String getReferenceType() {
		String text = getStringValues(referenceType, WaitLogic.VISIBLE, "get reference type");
		return text;
	}

	public List<String> getReferenceTypeInList() {
		List<WebElement> element = DriverManager.getDriver().findElements(referenceTypeListxpath);
		List<String> actualAttributeArray = new ArrayList<>();
		for (WebElement webElement : element) {
			actualAttributeArray.add(webElement.getText());
		}
		return actualAttributeArray;
	}

	// getting reference object number
	public String getReferenceObjectNumber() {
		String text = getStringValues(referenceObjectNumber, WaitLogic.VISIBLE, "get reference object number");
		return text;
	}

	
	/**
	 * Name of the method:getReferenceObjectNumberByEndDate 
	 * Description: getting endDate reference object number.
	 * Author:Darshan  
	 * To do: NA 
	 */
	public String getReferenceObjectNumberByEndDate() {

		String currentDate = DateandTimeUtils.getTodaysDateForQualityStatus();

		System.out.println(currentDate);
		List<WebElement> listOfEnddateReferenceID = DriverManager.getDriver().findElements(EndDatereference);
		String greaterDate = "";
		for (WebElement webElement : listOfEnddateReferenceID) {

			if (DateandTimeUtils.greaterDateComparision(currentDate, webElement.getText())) {
				greaterDate = webElement.getText();
				break;
			}

		}

		String text = getTextValue(getElementByReplaceText(referenceObjectNumberByEndDate, greaterDate),
				WaitLogic.VISIBLE, "get reference object number");
		System.out.println(text);
		return text;

	}

	// To check visibility of ReferenceType
	public boolean isReferenceTypeVisible(String fieldName) {
		By by = null;
		try {
			by = (By) ReferencesPage.class.getField(fieldName).get(ReferencesPage.this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isVisible(by, fieldName);
	}

	// To select added reference item from the list
	public String clickOnReferenceItem(String Item) {
		referenceObjectNumber = getElementByReplaceText(ReferenceItemNumber, Item);
		String text = getStringValues(referenceObjectNumber, WaitLogic.VISIBLE, "get reference object number");
		click(referenceObjectNumber, WaitLogic.CLICKABLE, "click on reference object number");
		WaitForMiliSec(5000);
		return text;
	}

	// To Click on add reference
	public ReferencesPage AddReference(String refTypeValue) {

		click(addReference, WaitLogic.CLICKABLE, "click on add reference");
		WaitForMiliSec(5000);
		sendKeys(referenceTypeTextFeild, refTypeValue, WaitLogic.VISIBLE, "referenceTypeValue");
		WaitForMiliSec(5000);
		click(referenceDropDown, WaitLogic.CLICKABLE, "click on reference dropdown");

		WaitForMiliSec(2000);

		return this;
	}

	// To click on click here button
	public void clickOnClickHereButton() {
		click(clickHereButton, WaitLogic.CLICKABLE, "clickHere Button");
		WaitForMiliSec(2000);
		// switch to new window
		switchTab(1);
		WaitForMiliSec(5000);
	}

	// To click on okAndCloseWindowButton
	public void clickOnokAndCloseWindowButton() {
		click(clickHereButton, WaitLogic.CLICKABLE, "OK and Close Browser Window");
		WaitForMiliSec(2000);
	}

	// To switch to parent window and click on OK button
	public void clickOnok() {
		click(clickHereButton, WaitLogic.CLICKABLE, "OK and Close Browser Window");
		WaitForMiliSec(2000);
	}

	public ReferencesPage enterReferenceObjectNumber(String number) {
		sendKeys(referenceObject, number, WaitLogic.VISIBLE, "referenceTypeValue");
		return this;
	}

	public ReferencesPage clickReferenceOKButton() {
		WaitForMiliSec(2000);
		click(referenceOKButton, WaitLogic.CLICKABLE, "OK and Close Browser Window");
		return this;
	}

	public ReferencesPage clickeditReference() {
		WaitForMiliSec(2000);
		click(editReference, WaitLogic.CLICKABLE, "click on edit icon");
		return this;
	}

	public ReferencesPage editReferenceBeginDate(String date) {
		sendKeys(referenceBeginDateField, date, WaitLogic.VISIBLE, "Begin Date");
		return this;
	}

	public ReferencesPage clickReferenceFirstRow() {
		click(referenceFirstRow, WaitLogic.CLICKABLE, "click on first row");
		return this;
	}

	// getting reference begin date
	public String getReferenceBeginDate() {
		String text = getStringValues(referenceBeginDate, WaitLogic.VISIBLE, "get reference object number");
		return text;
	}

	public ReferencesPage clickReferenceTypeHeader() {
		WaitForMiliSec(2000);
		click(referenceTypeHeader, WaitLogic.CLICKABLE, "click on reference type header");
		return this;
	}

	public ReferencesPage deleteReferenceItem(String ReferenceObjectNumber) {
		try {
			referenceObjectNumber = getElementByReplaceText(ReferenceItemNumber, ReferenceObjectNumber);
			DriverManager.getDriver().findElement(referenceObjectNumber).isDisplayed();
			clickOnReferenceItem(ReferenceObjectNumber);
			click(deleteButton, WaitLogic.CLICKABLE, "removed button ");
			WaitForMiliSec(3000);
			click(yesButton, WaitLogic.CLICKABLE, "Yes Button");
			WaitForMiliSec(2000);
		} catch (Exception e) {
			System.out.println("reference item are not present");
		}

		return this;
	}

	public ReferencesPage clickokAndCloseWindowButton() {
		WaitForMiliSec(3000);
		click(okAndCloseWindowButton, WaitLogic.CLICKABLE, "click on reference type header");
		return this;

	}
	
	// getting reference type As PA
	public String getReferenceTypeAsPA() {
		String text = getStringValues(referenceTypeAsPA, WaitLogic.VISIBLE, "get reference type As PA");
		return text;
	}
	
	// getting reference type As SP
	public String getReferenceTypeAsSP() {
		String text = getStringValues(referenceTypeAsSP, WaitLogic.VISIBLE, "get reference type As SP");
		return text;
	}
	
	// getting reference type As SA
	public String getReferenceTypeAsSA() {
		String text = getStringValues(referenceTypeAsSA, WaitLogic.VISIBLE, "get reference type As SA");
		return text;
	}
}
