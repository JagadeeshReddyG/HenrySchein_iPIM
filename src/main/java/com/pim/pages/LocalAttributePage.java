package com.pim.pages;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import com.mysql.cj.jdbc.Driver;
import com.pim.driver.DriverManager;
import com.pim.enums.WaitLogic;
import com.pim.utils.Javautils;

public class LocalAttributePage extends BasePage {

	// languageField xpath
	private final By languageFieldDropdown = By.xpath(
			"//span[contains(text(),'Language')]/ancestor::tr//td[@class=\"v-formlayout-contentcell\"]//div[@class=\"v-filterselect-button\"]");
	private final By languageInputField = By
			.xpath("//span[contains(text(),'Language')]/ancestor::tr//td[@class=\"v-formlayout-contentcell\"]//input");
	// languageField xpath for English
	private final By languageDropdownValueEnglish = By
			.xpath("(//div[@class=\"v-filterselect-suggestmenu\"]/../..//tr)[3]");
	private final By absorbency = By.xpath("//span[text()='Absorbency:']");
	private final By absorbencyInputField = By
			.xpath("//span[text()='Absorbency:']/../../../td[3]//div[@class='v-label v-widget v-has-width']");
	private final By additionalAttribute = By.xpath("//span[text()='Additional Attributes:']");
	private final By additionalAttributeInputField = By
			.xpath("//span[text()='Additional Attributes:']/../../../td[3]//div[contains(@class,'v-panel ')]");
	private final By antimicrobial = By.xpath("//span[text()='Antimicrobial:']");
	private final By antimicrobialInputField = By
			.xpath("//span[text()='Antimicrobial:']/../../../td[3]//div[contains(@class,'v-panel ')]");
	private final By arms = By.xpath("//span[text()='Arms:']");
	private final By armsInputField = By
			.xpath("//span[text()='Arms:']/../../../td[3]//div[contains(@class,'v-panel ')]");
	public final By brand = By.xpath("//span[text()='Brand:']");
	private final By coated = By.xpath("//span[text()='Coated:']");
	private final By coatedInputField = By
			.xpath("//span[text()='Coated:']/../../../td[3]//div[contains(@class,'v-panel ')]");
	public final By color = By.xpath("//span[text()='Color:']");
	private final By colorInputField = By
			.xpath("//span[text()='Color:']/../../../td[3]//div[contains(@class,'v-panel ')]");
	public final By composition = By.xpath("//span[contains(text(),'Composition')]");
	private final By compositionInputField = By
			.xpath("//span[contains(text(),'Composition')]/../../../td[3]//div[contains(@class,'v-panel ')]");
	private final By construction = By.xpath("//span[text()='Construction:']");
	private final By constructionInputField = By
			.xpath("//span[text()='Construction:']/../../../td[3]//div[contains(@class,'v-panel ')]");
	public final By disposable = By.xpath("//span[contains(text(),'Disposable')]");
	private final By disposableInputField = By
			.xpath("//span[contains(text(),'Disposable')]/../../../td[3]//div[contains(@class,'v-panel ')]");
	private final By ergonomicLocation = By.xpath("//span[contains(text(),'Ergonomic')]");
	private final By ergonomicLocationInputField = By
			.xpath("//span[contains(text(),'Ergonomic')]/../../../td[3]//div[contains(@class,'v-panel ')]");
	public final By item = By.xpath("//span[text()='Item:']");

	public final By itemType = By.xpath("//span[text()='Item Type:']");
	public final By lwh = By.xpath("//span[text()='LWH:']");
	public final By fragrance = By.xpath("//span[text()='Fragrance:']");
	public final By manufacturer = By.xpath("//span[text()='Manufacturer:']");
	public final By compositionIngredients = By.xpath("//span[text()='Composition/Ingredients:']");
	public final By sizeNumericText = By.xpath("//span[text()='Size (Numeric & Text):']");
	public final By sizeText = By.xpath("//span[text()='Size (Text):']");

	private final By latex = By.xpath("//span[contains(text(),'Latex')]");
	private final By latexInputField = By
			.xpath("//span[contains(text(),'Latex')]/../../../td[3]//div[contains(@class,'v-panel ')]");
	private final By needleCode = By.xpath("//span[contains(text(),'Needle Code')]");
	private final By needleCodeInputField = By
			.xpath("//span[contains(text(),'Needle Code')]/../../../td[3]//div[contains(@class,'v-panel ')]");
	public final By packaging = By.xpath("//span[contains(text(),'Packaging')]");
	private final By packagingInputField = By
			.xpath("//span[contains(text(),'Packaging')]/../../../td[3]//div[contains(@class,'v-panel ')]");
	private final By quantity = By.xpath("//span[contains(text(),'Quantity')]");

	public final By sterileNonSterile = By.xpath("//span[contains(text(),'Sterile')]");
	private final By sterileInputField = By
			.xpath("//span[contains(text(),'Sterile')]/../../../td[3]//div[contains(@class,'v-panel ')]");
	private final By sutureLength = By.xpath("//span[contains(text(),'Suture Length')]");
	private final By sutureLengthInputField = By
			.xpath("//span[contains(text(),'Suture Length')]/../../../td[3]//div[contains(@class,'v-panel ')]");
	private final By sutureSize = By.xpath("//span[contains(text(),'Suture Size')]");
	private final By sutureSizeInputField = By
			.xpath("//span[contains(text(),'Suture Size')]/../../../td[3]//div[contains(@class,'v-panel ')]");

	// languageField xpath for French For quantity field
	public final By QuantiteFieldFrench = By
			.xpath("//span[contains(text(),'Quantit')]/../../../td[3]//div[contains(@class,'v-panel ')]");
	public final By QuantityInputFieldValueForFrench = By.xpath(
			"//span[contains(text(),'Quantit')]/../../../td[3]//div[contains(@class,'v-panel ')]//input[contains(@id, 'detail_input')]");

	// languageField xpath for English For quantity field
	public final By quantityInputField = By
			.xpath("//span[contains(text(),'Quantity')]/../../../td[3]//div[contains(@class,'v-panel ')]");
	public final By quantityInputFieldValue = By.xpath(
			"//span[contains(text(),'Quantity')]/../../../td[3]//div[contains(@class,'v-panel ')]//input[contains(@id, 'detail_input')]");

	// this xpath will applicable for all fields, we have to send field name
	public final String localAttributeLableField = "//span[normalize-space()='${variable}:']/ancestor::tr/td[3]//div[contains(@id,\"detail_value\")]";

	// Selecting the Launguge Values Xpath
	private final String selectingValuesFromLaungugeField = "//td[normalize-space()='${variable}']";

	// Item field Related Xpath For Launguge "English"
	public By itemField = By.xpath("//span[text()='Item:']/../../../td[3]//div[contains(@class,'v-panel ')]");
	private final By itemFieldValue = By
			.xpath("//span[normalize-space()='Item:']/ancestor::tr/td[3]//input[@class='v-filterselect-input']");
	private final By itemFieldValueDropdown = By
			.xpath("//span[normalize-space()='Item:']/ancestor::tr/td[3]//div[@class='v-filterselect-button']");

	// Item field Related Xpath For Launguge "English"
	public By itemTypeField = By.xpath("//span[text()='Item Type:']/../../../td[3]//div[contains(@class,'v-panel ')]");
	private final By itemTypeFieldValue = By
			.xpath("//span[normalize-space()='Item Type:']/ancestor::tr/td[3]//input[@class='v-filterselect-input']");
	private final By itemTypeFieldValueDropdown = By
			.xpath("//span[normalize-space()='Item Type:']/ancestor::tr/td[3]//div[@class='v-filterselect-button']");

	// LWH field Related Xpath For Launguge "English"
	private final By PrimaryLWHField = By
			.xpath("//span[text()='Primary LWH:']/../../../td[3]//div[contains(@class,'v-panel ')]");
	private final By PrimaryLWHFieldValueDropdown = By
			.xpath("//span[normalize-space()='Primary LWH:']/ancestor::tr/td[3]//div[@class='v-filterselect-button']");

	// languageField xpath for English For Brand field
	public final By brandField = By.xpath("//span[text()='Brand:']/../../../td[3]//div[contains(@class,'v-panel ')]");
//	public final By brandFieldValue = By
//			.xpath("//span[normalize-space()='Brand:']/ancestor::tr/td[3]//input[@class='v-filterselect-input']");
	public final By brandFieldValue = By
			.xpath("//span[normalize-space()='Brand:']/ancestor::tr/td[3]//div[@class='v-label v-widget v-has-width']");
	
	private final By brandValueDropdown = By
			.xpath("//span[normalize-space()='Brand:']/ancestor::tr/td[3]//div[@class='v-filterselect-button']");

	public By allLocalAttributesKeys = By.xpath("//div[contains(@class,'v-caption v-caption-hpmw-detailFormLabel v-caption')]/span");
	public By allLocalAttributesValues = By.xpath("//div[contains(@class,'v-caption v-caption-hpmw-detailFormLabel v-caption')]/../..//div[contains(@class,'v-label v-widget')]");
	public String LocalAttributesValuesAsPerGivenName = "//span[text()='${variable}:']/../../..//td[@class='v-formlayout-contentcell']//div[contains(text(),' ')]";

	// languageField xpath for English For BrandOrItem field

	public final By BrandOrItem = By
			.xpath("//span[text()='Brand/Item:']/../../../td[3]//div[contains(@class,'v-panel')]");
	public final By BrandOrItemValue = By
			.xpath("//span[normalize-space()='Brand/Item:']/ancestor::tr/td[3]//input[@class='v-filterselect-input']");
	private final By BrandOrItemDropdown = By
			.xpath("//span[normalize-space()='Brand/Item:']/ancestor::tr/td[3]//div[@class='v-filterselect-button']");

	// languageField xpath for English For Composition/Ingredients field

	public final By CompositionOrIngredients = By
			.xpath("//span[text()='Composition/Ingredients:']/../../../td[3]//div[contains(@class,'v-panel')]");
	public final By CompositionOrIngredientsValue = By.xpath(
			"//span[normalize-space()='Composition/Ingredients:']/ancestor::tr/td[3]//input[@class='v-filterselect-input']");
	private final By CompositionOrIngredientsDropdown = By.xpath(
			"//span[normalize-space()='Composition/Ingredients:']/ancestor::tr/td[3]//div[@class='v-filterselect-button']");

	// languageField xpath for English For item field
	public final By itemField2 = By.xpath("//span[text()='Item:']/../../../td[3]//div[contains(@class,'v-panel ')]");
	public final By itemFieldValue2 = By
			.xpath("//span[normalize-space()='Item:']/ancestor::tr/td[3]//input[@class='v-filterselect-input']");
	private final By itemValueDropdown2 = By
			.xpath("//span[normalize-space()='Item:']/ancestor::tr/td[3]//div[@class='v-filterselect-button']");
	
	// languageField xpath for English For color field
	public final By colorField = By.xpath("//span[text()='Color:']/../../../td[3]//div[contains(@class,'v-panel ')]");
	public final By colorFieldValue = By
			.xpath("//span[normalize-space()='Color:']/ancestor::tr/td[3]//input[@class='v-filterselect-input']");
	private final By colorValueDropdown = By
			.xpath("//span[normalize-space()='Color:']/ancestor::tr/td[3]//div[@class='v-filterselect-button']");

	// private final String valueSearchxpathfromDropdown
	public String valueSearchxpathfromDropdown = "//span[contains(text(),'${variable}')]";

	// dropdown Field for Local attribute field
	public By FieldDropDown = By.xpath("//table//div[@class='v-filterselect-button']");

	// Local attribute list xpath
	private final By localAttributesList = By.xpath("//tr[@class='v-formlayout-row']");

	private final By selectSaveToSubmitField = By.xpath(
			"//span[contains(text(),'Select Save to Submit Changes')]/ancestor:: tr//td[@class='v-formlayout-contentcell']//div[2]");

	// LastSubmittedOn xpath
	private final By lastSubmittedOnTxtfld = By.xpath("//div[@id='detail_value_951bdae294311d80d1a9a7b9a58f5f22']");
	private final By calenderButton = By.xpath("//div[@class='v-button v-widget hpmw-datepicker-button v-button-hpmw-datepicker-button']");
	private final By todayInCalenderButton = By.xpath("//span[@class='v-button-caption' and contains(text(),'Today')]");
	private final By applyInCalenderButton = By.xpath("//span[@class='v-button-caption' and contains(text(),'Apply')]");
	
	private final By InputSaveFieldDropDown = By.xpath(
			"//span[contains(text(),'Select Save to Submit Changes')]/ancestor:: tr//td[@class='v-formlayout-contentcell']//div[@class='v-filterselect-button']");
	private final String tabSearchxpath = "//span[normalize-space()='${variable}']";

	// local Attribute "Shank Type" xpath for Dental user
	public By shankType = By
			.xpath("//span[contains(text(),'Shank Type')]/../../../td//div[contains(@class,'v-panel ')]");

	// local Attribute "Capsule Size" xpath for Medical user
	public By capsuleSize = By
			.xpath("//span[contains(text(),'Capsule Size')]/../../../td//div[contains(@class,'v-panel ')]");

	// local Attribute "Set Type" xpath for Medical user
	public By setType = By
			.xpath("//span[contains(text(),'Set Type')]/../../../td[3]//div[contains(@class,'v-panel ')]");

	// local Attribute "Spill" xpath for Medical user
	public By spill = By
			.xpath("(//span[contains(text(),'Spill')]/../../../td[3]//div[contains(@class,'v-panel ')])[1]");
	// Local attribute list xpath
	private final By localAttributeFields = By
			.xpath("//div[@class=\"v-expand\"]/div[1]//tbody[1]//tr[contains(@class, 'v-formlayout')]/td[1]//span");

	public final By quantityFieldValue = By.xpath("//span[normalize-space()='Quantity:']/ancestor::tr/td[3]//div[@class='v-label v-widget v-has-width']");
	public final By itemFieldValueNew = By.xpath("//span[normalize-space()='Item:']/ancestor::tr/td[3]//div[@class='v-label v-widget v-has-width']");
	public final By itemTypeFieldValueNew = By.xpath("//span[normalize-space()='Item Type:']/ancestor::tr/td[3]//div[@class='v-label v-widget v-has-width']");
	public final By packagingTypeFieldValue = By.xpath("//span[normalize-space()='Packaging Type:']/ancestor::tr/td[3]//div[@class='v-label v-widget v-has-width']");





	public boolean isLocalAttributeFieldVisible(String fieldname) {
		By by = null;
		try {
			by = (By) LocalAttributePage.class.getField(fieldname).get(LocalAttributePage.this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isVisible(by, fieldname);
	}

	public boolean isLocalAttributeFieldEditable(String fieldname) {
		By by = null;
		try {

			by = (By) LocalAttributePage.class.getField(fieldname).get(LocalAttributePage.this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isClickable(by, fieldname);
	}

	public LocalAttributePage selectLanguageFieldDropdown(String Launguage) {
		WaitForMiliSec(3000);
		click(languageFieldDropdown, WaitLogic.CLICKABLE, "Clicked on LaunguageFieldDropdown");
		WaitForMiliSec(1000);
		click(getElementByReplaceText(selectingValuesFromLaungugeField, Launguage), WaitLogic.CLICKABLE, Launguage);
		WaitForMiliSec(5000);
		return this;

	}

	public LocalAttributePage MandatoryItemFieldAttribute(String InputFieldValue) {
		WaitForMiliSec(2000);
		click(itemField, WaitLogic.CLICKABLE, "itemInputField");
		WaitForMiliSec(1000);
		click(itemFieldValueDropdown, WaitLogic.CLICKABLE, "Clicked on DropDown");
		WaitForMiliSec(2000);
		click(getElementByReplaceText(valueSearchxpathfromDropdown, InputFieldValue), WaitLogic.CLICKABLE,
				InputFieldValue);
		return this;

	}

	public LocalAttributePage clearingMandatoryQuantityFieldAttribute() {
		click(quantityInputField, WaitLogic.CLICKABLE, "itemInputField");
		WaitForMiliSec(2000);
		clearField(quantityInputFieldValue, WaitLogic.CLICKABLE, "quantityInputFieldValue");
		WaitForMiliSec(1000);
		return this;

	}

	public LocalAttributePage addingMandatoryQuantityFieldAttribute(String QuantityFieldValue) {
		click(quantityInputField, WaitLogic.CLICKABLE, "QuantityInputField");
		Javautils.selectAllAndDeletingThroughKeboard();
		WaitForMiliSec(3000);
		clearField(quantityInputFieldValue, WaitLogic.CLICKABLE, "quantityInputFieldValue");
		WaitForMiliSec(3000);
		click(quantityInputField, WaitLogic.CLICKABLE, "QuantityInputField");
		DriverManager.getDriver().switchTo().activeElement().click();
		WaitForMiliSec(3000);
		DriverManager.getDriver().switchTo().activeElement().sendKeys(QuantityFieldValue);
		WaitForMiliSec(2000);
		return this;

	}

	public LocalAttributePage itemTypeFieldAttribute(String itemTypeFieldValue) {
		WaitForMiliSec(1000);
		click(itemTypeField, WaitLogic.CLICKABLE, "itemTypeField");
		WaitForMiliSec(1000);
		click(itemTypeFieldValueDropdown, WaitLogic.CLICKABLE, "Clicked on DropDown");
		WaitForMiliSec(1000);
		click(getElementByReplaceText(valueSearchxpathfromDropdown, itemTypeFieldValue), WaitLogic.CLICKABLE,
				itemTypeFieldValue);
		WaitForMiliSec(1000);
		return this;

	}

	public LocalAttributePage primaryLwhFieldAttribute(String LWHFieldValue) {
		WaitForMiliSec(1000);
		click(PrimaryLWHField, WaitLogic.CLICKABLE, "itemTypeField");
		WaitForMiliSec(1000);
		click(PrimaryLWHFieldValueDropdown, WaitLogic.CLICKABLE, "Clicked on DropDown");
		WaitForMiliSec(1000);
		click(getElementByReplaceText(valueSearchxpathfromDropdown, LWHFieldValue), WaitLogic.CLICKABLE, LWHFieldValue);
		WaitForMiliSec(1000);
		return this;

	}

	public LocalAttributePage brandFieldAttribute(String BrandFieldValue) {

		try {
			WaitForMiliSec(1000);
			click(brandField, WaitLogic.CLICKABLE, "brandField");
			WaitForMiliSec(1000);
			click(brandValueDropdown, WaitLogic.CLICKABLE, "Clicked on DropDown");
			WaitForMiliSec(1000);
			click(getElementByReplaceText(valueSearchxpathfromDropdown, BrandFieldValue), WaitLogic.CLICKABLE,
					BrandFieldValue);
			WaitForMiliSec(1000);

		} catch (Exception e) {
			WaitForMiliSec(1000);
			click(brandField, WaitLogic.CLICKABLE, "brandField");
			WaitForMiliSec(1000);
			Javautils.selectAllAndDeletingThroughKeboard();
			WaitForMiliSec(1000);
			sendKeys(brandFieldValue, BrandFieldValue, WaitLogic.VISIBLE, BrandFieldValue);
			WaitForMiliSec(1000);
		}
		return this;

	}

	public LocalAttributePage brandFieldAttribute(String BrandFieldValue, String GetTextValue) {
		Javautils util = new Javautils();
		List<String> expectedBrandList = util.readMultipleValuesFromExcel(BrandFieldValue);
		for (String expectedBrandValue : expectedBrandList) {
			if ((!GetTextValue.contains(expectedBrandValue)) || (GetTextValue.equalsIgnoreCase(""))) {
				try {
					WaitForMiliSec(3000);
					click(brandField, WaitLogic.CLICKABLE, "brandField");
					WaitForMiliSec(3000);
					click(brandValueDropdown, WaitLogic.CLICKABLE, "Clicked on DropDown");
					WaitForMiliSec(3000);
					click(getElementByReplaceText(valueSearchxpathfromDropdown, expectedBrandValue),
							WaitLogic.CLICKABLE, BrandFieldValue);
					WaitForMiliSec(3000);

				} catch (Exception e) {
					WaitForMiliSec(3000);
					click(brandField, WaitLogic.CLICKABLE, "itemTypeField");
					WaitForMiliSec(3000);
					Javautils.selectAllAndDeletingThroughKeboard();
					WaitForMiliSec(3000);
					sendKeys(brandFieldValue, expectedBrandValue, WaitLogic.VISIBLE, BrandFieldValue);
					WaitForMiliSec(3000);
				}

			}
		}
		return this;
	}
	
	
	
	public LocalAttributePage ItemTypeAttribute(String ItemTypeFieldValue, String GetTextValue) {
		Javautils util = new Javautils();
		List<String> expectedList = util.readMultipleValuesFromExcel(ItemTypeFieldValue);
		for (String expectedValue : expectedList) {
			if ((!GetTextValue.contains(expectedValue)) || (GetTextValue.equalsIgnoreCase(""))) {
				try {
					WaitForMiliSec(3000);
					click(itemTypeField, WaitLogic.CLICKABLE, "ItemType");
					WaitForMiliSec(3000);
					click(itemTypeFieldValueDropdown, WaitLogic.CLICKABLE, "Clicked on DropDown");
					WaitForMiliSec(3000);
					click(getElementByReplaceText(valueSearchxpathfromDropdown, expectedValue),
							WaitLogic.CLICKABLE, ItemTypeFieldValue);
					WaitForMiliSec(3000);

				} catch (Exception e) {
					WaitForMiliSec(3000);
					click(itemTypeField, WaitLogic.CLICKABLE, "itemTypeField");
					WaitForMiliSec(3000);
					Javautils.selectAllAndDeletingThroughKeboard();
					WaitForMiliSec(3000);
					sendKeys(itemTypeField, expectedValue, WaitLogic.VISIBLE, ItemTypeFieldValue);
					WaitForMiliSec(3000);
				}

			}
		}
		return this;
	}

	public LocalAttributePage itemFieldInavlidValue(String ItemField) {
		WaitForMiliSec(3000);
		scrollToElement(itemField, "Scroll to Itemfield");
		WaitForMiliSec(1000);
		click(itemField, WaitLogic.CLICKABLE, "itemfield");
		WaitForMiliSec(1000);
		selectTextViaKeyboard();
		deleteSelected();
		WaitForMiliSec(1000);
		DriverManager.getDriver().switchTo().activeElement().clear();
		DriverManager.getDriver().switchTo().activeElement().click();
		WaitForMiliSec(3000);
		DriverManager.getDriver().switchTo().activeElement().sendKeys(ItemField + " " + Javautils.randomNumber());
		WaitForMiliSec(3000);
		// sendKeys(itemFieldValue, ItemField, WaitLogic.VISIBLE, ItemField);
		WaitForMiliSec(3000);
		return this;

	}
	
	public LocalAttributePage itemTypeFieldInavlidValue(String ItemTypeField) {
		WaitForMiliSec(3000);
		scrollToElement(itemTypeField, "Scroll to Itemfield");
		WaitForMiliSec(1000);
		click(itemTypeField, WaitLogic.CLICKABLE, "itemfield");
		WaitForMiliSec(1000);
		selectTextViaKeyboard();
		deleteSelected();
		WaitForMiliSec(1000);
		DriverManager.getDriver().switchTo().activeElement().clear();
		DriverManager.getDriver().switchTo().activeElement().click();
		WaitForMiliSec(3000);
		DriverManager.getDriver().switchTo().activeElement().sendKeys(ItemTypeField);
		WaitForMiliSec(3000);
		// sendKeys(itemFieldValue, ItemField, WaitLogic.VISIBLE, ItemField);
		WaitForMiliSec(3000);
		return this;

	}



	public LocalAttributePage colorFieldAttribute(String colorFieldValue) {
		WaitForMiliSec(2000);
		click(colorField, WaitLogic.CLICKABLE, "itemTypeField");
		WaitForMiliSec(1000);
		click(colorValueDropdown, WaitLogic.CLICKABLE, "Clicked on DropDown");
		WaitForMiliSec(2000);
		click(getElementByReplaceText(valueSearchxpathfromDropdown, colorFieldValue), WaitLogic.CLICKABLE,
				colorFieldValue);
		WaitForMiliSec(3000);
		return this;
	}
	
	public LocalAttributePage itemFieldAttributeChange(String itemFieldValue) {
		WaitForMiliSec(2000);
		click(itemField2, WaitLogic.CLICKABLE, "itemTypeField");
		WaitForMiliSec(1000);
		click(itemValueDropdown2, WaitLogic.CLICKABLE, "Clicked on DropDown");
		WaitForMiliSec(2000);
		click(getElementByReplaceText(valueSearchxpathfromDropdown, itemFieldValue), WaitLogic.CLICKABLE,
				itemFieldValue);
		WaitForMiliSec(3000);
		return this;
	}

	public boolean isLocalAttributeFieldEditable(String fieldname, String visibilityElement) {

		By by = null;

		try {
			by = (By) LocalAttributePage.class.getField(fieldname).get(LocalAttributePage.this);
			click(by, WaitLogic.CLICKABLE, fieldname);
			by = (By) LocalAttributePage.class.getField(visibilityElement).get(LocalAttributePage.this);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return (isClickable(by, fieldname + visibilityElement, 5));
	}

	public String getBrandValue() {
		WaitForMiliSec(10000);
		String BrandValue = getStringValues(brandField, WaitLogic.VISIBLE, "get Brand Value");
		if (BrandValue.equals("No content")) {
			return BrandValue.replace(BrandValue, "");
		}
		return BrandValue.trim() + " ";
	}

	public String getItemValue() {
		String ItemValue = getStringValues(itemField, WaitLogic.VISIBLE, "get Item Value");
		if (ItemValue.equals("No content")) {
			return ItemValue.replace(ItemValue, "");

		}

		return ItemValue.trim();
	}

	public String getItemTypeValue() {
		WaitForMiliSec(2000);
		String ItemTypeValue = getStringValues(itemTypeField, WaitLogic.VISIBLE, "get ItemType Value");
		if (ItemTypeValue.equals("No content")) {

			return ItemTypeValue.replace(ItemTypeValue, "");
		}
		return ItemTypeValue.trim() + " ";
	}

	public String getItemTypeValueWithOutSpace() {
		WaitForMiliSec(2000);
		String ItemTypeValue = getStringValues(itemTypeField, WaitLogic.VISIBLE, "get ItemType Value");
		return ItemTypeValue;
	}

	public String getColorValue() {
		String ColorValue = getStringValues(colorField, WaitLogic.VISIBLE, "get ItemType Value");
		if (ColorValue.equals("No content")) {
			return ColorValue.replace(ColorValue, "");
		}
		return ColorValue.trim() + " ";
	}

	public String getBranOrItemValue() {
		String BrandOrItemValue = getStringValues(BrandOrItem, WaitLogic.VISIBLE, "get BrandOrItem Value");
		if (BrandOrItemValue.equals("No content")) {
			return BrandOrItemValue.replace(BrandOrItemValue, "");
		}
		return BrandOrItemValue.trim() + " ";
	}

	public String getCompositionOrIngredientsValue() {
		String CompositionOrIngredientsValue = getStringValues(CompositionOrIngredients, WaitLogic.VISIBLE,
				"get CompositionOrIngredientsValue Value");
		if (BrandOrItemValue.equals("No content")) {
			return CompositionOrIngredientsValue.replace(CompositionOrIngredientsValue, "");
		}
		return CompositionOrIngredientsValue.trim() + " ";
	}

	public String getAllitemAttribute() {
		WaitForMiliSec(20000);
		List<WebElement> allAttributeField = DriverManager.getDriver().findElements(By.xpath(
				"(//table[@class='v-formlayout-margin-right v-formlayout-margin-bottom v-formlayout-spacing'])[1]//td[@class='v-formlayout-contentcell']//div//div//div//div//div"));

		String expectedString = "";

		for (int i = 0; i < allAttributeField.size(); i++) {

			if (!allAttributeField.get(i).getText().contains("No content")) {

				if (!(expectedString.equals("")) & (i != allAttributeField.size() - 1)) {
					expectedString = expectedString + ", " + allAttributeField.get(i).getText().trim();
				} else {
					expectedString = expectedString + allAttributeField.get(i).getText().trim();
				}

			}

		}

		return expectedString;
	}
	
	public String getAllitemAttributeExceptQuantity() {
		WaitForMiliSec(20000);
		List<WebElement> allAttributeField = DriverManager.getDriver().findElements(By.xpath(
				"(//table[@class='v-formlayout-margin-right v-formlayout-margin-bottom v-formlayout-spacing'])[1]//td[@class='v-formlayout-contentcell']//div//div//div//div//div"));

		String expectedString = "";

		for (int i = 0; i < allAttributeField.size(); i++) {

			String attributeText = allAttributeField.get(i).getText().trim();

			// Check if the field name is exactly "Quantity" and skip if it is
			if (!attributeText.contains("No content") && !attributeText.equalsIgnoreCase("Each")) {
				System.out.println("attributeText :" + attributeText);
				if (!expectedString.equals("")) {
					expectedString = expectedString + ", " + attributeText;
				} else {
					expectedString = expectedString + attributeText;
				}

			}

		}

		return expectedString;
	}

	public String getQuantityValue() {
		String QuantityValue = getStringValues(quantityInputField, WaitLogic.VISIBLE, "get Item Value");
		return QuantityValue.trim();
	}

	public String getQuantityValuewithoutTrimforFrench() {
		WaitForMiliSec(2000);
		String QuantityValue = getStringValues(QuantiteFieldFrench, WaitLogic.VISIBLE, "get Item Value");
		return QuantityValue;
	}

	public boolean isAllAttributesVisible(String fieldName) {
		By by = null;
		try {
			by = (By) LocalAttributePage.class.getField(fieldName).get(LocalAttributePage.this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isVisible(by, fieldName);
	}

	// To get list of Local Attributes for selected products from the list of items
	public List<String> ListOfLocalAttributesWithExceptionfield(String ExceptedlocalAttributes) {
		WaitForMiliSec(3000);

		Javautils util = new Javautils();
		List<String> ExceptedAttributesList = util.readMultipleValuesFromExcel(ExceptedlocalAttributes);

		List<WebElement> localattributeWebelements = DriverManager.getDriver().findElements(localAttributeFields);
		List<String> listOfLocalAttributesToCompare = new ArrayList<String>();
		for (WebElement localAttributeWebElement : localattributeWebelements) {
			String localAttributeValue = localAttributeWebElement.getText().replace(":", "");
			if (!ExceptedAttributesList.contains(localAttributeValue)) {
				listOfLocalAttributesToCompare.add("$" + localAttributeValue);
			}

		}
		WaitForMiliSec(3000);
		return listOfLocalAttributesToCompare;
	}

	public LocalAttributePage selectValuefromSaveDropdown(String tabname) {
		WaitForMiliSec(3000);
		click(selectSaveToSubmitField, WaitLogic.CLICKABLE, "Save Field");
		WaitForMiliSec(3000);
		//sendKeys(InputSaveField, tabname, WaitLogic.VISIBLE, "tabnameSearchTextbox");
		click(InputSaveFieldDropDown, WaitLogic.CLICKABLE, "Save Field Button Dropdown");
		WaitForMiliSec(3000);
		click(getElementByReplaceText(tabSearchxpath, tabname), WaitLogic.CLICKABLE, "tabname");
		WaitForMiliSec(10000);
		return this;
	}

	public LocalAttributePage editLastSubmittedOnToToday() {
		WaitForMiliSec(2000);
		click(lastSubmittedOnTxtfld, WaitLogic.CLICKABLE, "LastSubmittedOn Textfield");
		click(calenderButton, WaitLogic.CLICKABLE, "calenderButton");
		WaitForMiliSec(2000);
		click(todayInCalenderButton, WaitLogic.CLICKABLE, "todayInCalenderButton");
		WaitForMiliSec(1000);
		click(applyInCalenderButton, WaitLogic.CLICKABLE, "applyInCalenderButton");
		WaitForMiliSec(10000);
		return this;
	}
	
	public LocalAttributePage clickRefreshButton() {
		WaitForMiliSec(2000);
		click(lastSubmittedOnTxtfld, WaitLogic.CLICKABLE, "LastSubmittedOn Textfield");
		click(calenderButton, WaitLogic.CLICKABLE, "calenderButton");
		WaitForMiliSec(2000);
		click(todayInCalenderButton, WaitLogic.CLICKABLE, "todayInCalenderButton");
		WaitForMiliSec(1000);
		click(applyInCalenderButton, WaitLogic.CLICKABLE, "applyInCalenderButton");
		WaitForMiliSec(10000);
		return this;
	}

	public LocalAttributePage editItemTypeFieldAttribute(String tabname) {
		WaitForMiliSec(3000);
		click(itemTypeField, WaitLogic.CLICKABLE, "Save Field");
		sendKeys(itemTypeFieldValue, tabname, WaitLogic.VISIBLE, "tabnameSearchTextbox");
		WaitForMiliSec(3000);
		click(getElementByReplaceText(tabSearchxpath, tabname), WaitLogic.CLICKABLE, "tabname");
		WaitForMiliSec(10000);

		return this;
	}

	// For French
	public LocalAttributePage addingMandatoryQuantityFieldAttributeForFrench(String QuantityFieldValue) {
		WaitForMiliSec(3000);
		click(QuantiteFieldFrench, WaitLogic.CLICKABLE, "QuantityInputField");
		Javautils.selectAllAndDeletingThroughKeboard();
		WaitForMiliSec(3000);
		clearField(QuantityInputFieldValueForFrench, WaitLogic.CLICKABLE, "quantityInputFieldValue");
		WaitForMiliSec(3000);
		click(QuantiteFieldFrench, WaitLogic.CLICKABLE, "QuantityInputField");
		DriverManager.getDriver().switchTo().activeElement().click();
		WaitForMiliSec(3000);
		DriverManager.getDriver().switchTo().activeElement().sendKeys(QuantityFieldValue);
		WaitForMiliSec(2000);
		return this;

	}

	public LocalAttributePage editCompositionAttribute(String tabname) {
		WaitForMiliSec(2000);
		click(CompositionOrIngredients, WaitLogic.CLICKABLE, "CompositionOrIngredients");
		WaitForMiliSec(1000);
		click(CompositionOrIngredientsDropdown, WaitLogic.CLICKABLE, "CompositionOrIngredientsDropdown");
		WaitForMiliSec(2000);
		click(getElementByReplaceText(valueSearchxpathfromDropdown, tabname), WaitLogic.CLICKABLE,
				tabname);
		WaitForMiliSec(3000);
		return this;
	}	


	public ArrayList<String> getKeyForJSONFromLocalAttributeTab() {
		ArrayList<String> keyList = new ArrayList<String>();
		List<WebElement> allLocalAttributesKeysXpath = DriverManager.getDriver().findElements(allLocalAttributesKeys);
		for (int i = 0; i < allLocalAttributesKeysXpath.size(); i++) {
			String LA_Keys = allLocalAttributesKeysXpath.get(i).getText().trim();
			String LA_KeysWithoutColon = LA_Keys.substring(0, LA_Keys.length() - 1);
			
			if (LA_KeysWithoutColon.equals("Select Save to Submit Changes")) {
				LA_KeysWithoutColon.replace(LA_KeysWithoutColon, "");
			}
			else if (LA_KeysWithoutColon.equals("Last Submitted on")) {
				LA_KeysWithoutColon.replace(LA_KeysWithoutColon, "");
			}
			else {
				keyList.add(LA_KeysWithoutColon);
			}
		}
		return keyList;
	}
	
	/**
     * Removes the element at the given index from the ArrayList.
     * @param list The original ArrayList of strings.
     * @param indexToRemove The index of the element to be removed.
     * @return A new ArrayList without the specified element.
     */
    public ArrayList<String> removeElementByIndex(ArrayList<String> list, int indexToRemove) {
        if (list == null || indexToRemove < 0 || indexToRemove >= list.size()) {
            throw new IllegalArgumentException("Invalid index or null list");
        }

        ArrayList<String> result = new ArrayList<>(list);
        result.remove(indexToRemove);
        return result;
    }
    
    
    /**
     * Removes the first occurrence of the specified value from the ArrayList.
     * @param list The original ArrayList of strings.
     * @param valueToRemove The string value to be removed.
     * @return A new ArrayList without the specified value.
     */
    public ArrayList<String> removeElementByValue(ArrayList<String> list, String valueToRemove) {
        if (list == null || valueToRemove == null) {
            throw new IllegalArgumentException("List or value cannot be null");
        }

        ArrayList<String> result = new ArrayList<>(list);
        result.remove(valueToRemove); // removes the first matching value
        return result;
    }
    
    public Map<String, String> normalizeKeys(Map<String, String> original) {
        return original.entrySet().stream()
                .collect(Collectors.toMap(
                        e -> e.getKey().toLowerCase(),
                        Map.Entry::getValue
                ));
    }

    public List<String> normalizeList(List<String> originalList) {
        return originalList.stream()
                           .map(String::toLowerCase)
                           .collect(Collectors.toList());
    }
    
    
    public  List<String> createNormalizedList(String input) {
        List<String> list = new ArrayList<>();
        if (input != null) {
            list.add(normalizeString(input));
        }
        return list;
    }

    public String normalizeString(String input) {
        return input == null ? null : input.trim().toLowerCase();
    }
    
    
    
	public Map<String, String> getValuesOfGivenLocalAttributesName(ArrayList<String> localAttributesName) {
		Map<String, String> map_LA = new HashMap<String, String>();
		for (int i = 0; i < localAttributesName.size(); i++) {
			String LA_Keys = localAttributesName.get(i);
			
			String LA_Values = (getTextValue(getElementByReplaceText(LocalAttributesValuesAsPerGivenName, LA_Keys),WaitLogic.VISIBLE, "Captured Values")).trim();
			if (LA_Values.equals("No content")) {
				LA_Values = "";
			}
			map_LA.put(LA_Keys, LA_Values);
		}

		return map_LA;
	}


//	public List<String> getAllDimensionsNames() {
//		List<String> dimensionsNames = new ArrayList<>();
//
//		dimensionsNames.add(getTextValue(brand, WaitLogic.PRESENCE, "Brand").replace(":","").trim());
//		dimensionsNames.add(getTextValue(quantity, WaitLogic.PRESENCE, "Quantity").replace(":","").trim());
//		dimensionsNames.add(getTextValue(item, WaitLogic.PRESENCE, "Item").replace(":","").trim());
//		dimensionsNames.add(getTextValue(itemType, WaitLogic.PRESENCE, "Item Type").replace(":","").trim());
//		dimensionsNames.add(getTextValue(packaging, WaitLogic.PRESENCE, "Packaging Type").replace(":","").trim());
//
//		return dimensionsNames;
//	}

	public List<String> getAllDimensionsNames() {
		List<String> dimensionsNames = new ArrayList<>();

		addIfPresent(dimensionsNames, brand, brandFieldValue, "Brand");
		addIfPresent(dimensionsNames, quantity, quantityFieldValue, "Quantity");
		addIfPresent(dimensionsNames, item, itemFieldValueNew, "Item");
		addIfPresent(dimensionsNames, itemType, itemTypeFieldValueNew, "Item Type");
		addIfPresent(dimensionsNames, packaging, packagingTypeFieldValue, "Packaging Type");

		return dimensionsNames;
	}

	private void addIfPresent(List<String> list, By nameLocator, By valueLocator, String label) {
		try {
			String name = getTextValue(nameLocator, WaitLogic.PRESENCE, label);
			String value = getTextValue(valueLocator, WaitLogic.PRESENCE, label + " Value");

			if (name != null && !name.trim().isEmpty() &&
					value != null && !value.trim().isEmpty()) {
				list.add(name.replace(":", "").trim());
			}
		} catch (Exception e) {
			// skip if element not present
		}
	}

	public List<String> getAllDimensionsValues() {
		List<String> dimensionsValues = new ArrayList<>();

		addValueIfPresent(dimensionsValues, brandFieldValue, "Brand Value");
		addValueIfPresent(dimensionsValues, quantityFieldValue, "Quantity Value");
		addValueIfPresent(dimensionsValues, itemFieldValueNew, "Item Value");
		addValueIfPresent(dimensionsValues, itemTypeFieldValueNew, "Item Type Value");
		addValueIfPresent(dimensionsValues, packagingTypeFieldValue, "Packaging Type Value");

		return dimensionsValues;
	}

	private void addValueIfPresent(List<String> list, By valueLocator, String label) {
		try {
			String value = getTextValue(valueLocator, WaitLogic.PRESENCE, label);

			if (value != null && !value.trim().isEmpty()) {
				list.add(value.replace(":", "").trim());
			}
		} catch (Exception e) {
			// skip if element not present
		}
	}


//	public List<String> getAllDimensionsValues() {
//		List<String> dimensionsValues = new ArrayList<>();
//
//		dimensionsValues.add(getTextValue(brandFieldValue, WaitLogic.PRESENCE, "Brand Value").replace(":","").trim());
//		dimensionsValues.add(getTextValue(quantityFieldValue, WaitLogic.PRESENCE, "Quantity Value").replace(":","").trim());
//		dimensionsValues.add(getTextValue(itemFieldValueNew, WaitLogic.PRESENCE, "Item Value").replace(":","").trim());
//		dimensionsValues.add(getTextValue(itemTypeFieldValueNew, WaitLogic.PRESENCE, "Item Type Value").replace(":","").trim());
//		dimensionsValues.add(getTextValue(packagingTypeFieldValue, WaitLogic.PRESENCE, "Packaging Type Value"));
//
//		return dimensionsValues;
//	}

//	public String getAllDimensions() {
//		String brand = getTextValue(brand,WaitLogic.PRESENCE, "Brand");
//		return brand;
//	}

}