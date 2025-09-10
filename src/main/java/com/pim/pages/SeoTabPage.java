package com.pim.pages;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import com.pim.driver.Driver;
import com.pim.driver.DriverManager;
import com.pim.enums.WaitLogic;
import com.pim.utils.DateandTimeUtils;
import com.pim.utils.ExcelUtils;
import com.pim.utils.Javautils;

public class SeoTabPage extends BasePage {

	// Seo Tab Page Header
	private By RetriveRules = By.xpath("//button[@class='v-nativebutton v-widget']");
	private By GenerateDescription = By.xpath("//*[contains(text(),'Generate Descriptions')]");
	private By SaveRule = By.xpath("//*[contains(text(),'Save Rules')]");

	// 1.Page Title (Browser Title)--Xpath

	private By pageTitleBrowserTitle = By.xpath("//font[contains(text(),'Page Title (Browser Title)')]");
	private By selectRuleParameterdropdownpageTitleBrowserTitle = By
			.xpath("//font[contains(text(),'Page Title (Browser Title)')]/ancestor::tbody/tr[2]//div[@role='button']");
	private By dropdowFieldValuenPageTitleBrowserTitle = By
			.xpath("//font[contains(text(),'Page Title (Browser Title)')]/ancestor::tbody/tr[2]//input");
	private By dropdowFieldButtonPageTitleBrowserTitle = By.xpath(
			"//font[contains(text(),'Page Title (Browser Title)')]/ancestor::tbody/tr[2]//div[@class=\"v-filterselect-button\"]");
	private By descriptionFieldpageTitleBrowserTitle = By.xpath(
			"//font[contains(text(),'Page Title (Browser Title)')]/ancestor::tbody/tr[4]//div[@class='v-label v-widget custom-label1 v-label-custom-label1 v-label-undef-w']");
	private By dropHereToDeletepageTitleBrowserTitle = By
			.xpath("//table[2]/tbody/tr[5]//div[contains(text(),'Drop Here to Delete')]");
	private By AddingTextinFreeTextAttributeForPageTitleBrowserTitle = By.xpath(
			"//font[contains(text(),'Page Title (Browser Title)')]/ancestor::tbody//tr[3]//td[1]//div//input[contains(@class, 'v-textfield v-widget custom-label3 v-textfield-custom-label3')]");
	private By AttributeForPagetitle = By
			.xpath("//font[contains(text(),'Page Title (Browser Title)')]/ancestor::tbody//tr[3]//td[1]//div//input");

	private By AttributeForsecondElement = By.xpath(
			"//font[contains(text(),'Page Title (Browser Title)')]/ancestor::tbody//tr[3]//td[1]/div/div/div[2]/div/input");
	private By AttributeForFirstElement = By.xpath(
			"//font[contains(text(),'Page Title (Browser Title)')]/ancestor::tbody//tr[3]//td[1]/div/div/div[1]/div/input");

	// 2.Meta Data (Meta Description)--Xpath
	private By metaDataMetaDescription = By.xpath("//font[contains(text(),'Meta Data (Meta Description)')]");
	private By selectRuleParameterdropdownmetaDataMetaDescription = By.xpath(
			"//font[contains(text(),'Meta Data (Meta Description)')]/ancestor::tbody/tr[2]//div[@role='button']");
	private By descriptionmetaDataMetaDescription = By.xpath(
			"//font[contains(text(),'Meta Data (Meta Description)')]/ancestor::tbody/tr[4]//div[@class='v-label v-widget custom-label1 v-label-custom-label1 v-label-undef-w']");
	private By dropHereToDeletemetaDataMetaDescription = By
			.xpath("//table[3]/tbody/tr[5]//div[contains(text(),'Drop Here to Delete')]");
	private By dropdowFieldValueMetaData = By
			.xpath("//font[contains(text(),'Meta Data (Meta Description)')]/ancestor::tbody/tr[2]//input");
	private By dropdowFieldButtonMetaData = By.xpath(
			"//font[contains(text(),'Meta Data (Meta Description)')]/ancestor::tbody/tr[2]//div[@class=\"v-filterselect-button\"]");
	private By AddingTextinFreeTextAttributeForMetaData = By.xpath(
			"//font[contains(text(),'Meta Data (Meta Description)')]/ancestor::tbody//tr[3]//td[1]//div//input[contains(@class, 'v-textfield v-widget custom-label3 v-textfield-custom-label3')]");
	private By AttributeForMetaData = By
			.xpath("//font[contains(text(),'Meta Data (Meta Description)')]/ancestor::tbody//tr[3]//td[1]//div//input");

	// 3.Keywords (Meta Keywords)--Xpath
	private By keywordsMetaKeywords = By.xpath("//font[contains(text(),'Keywords (Meta Keywords)')]");
	private By selectRuleParameterdropdownkeywordsMetaKeywords = By
			.xpath("//font[contains(text(),'Keywords (Meta Keywords)')]/ancestor::tbody/tr[2]//div[@role='button']");
	private By descriptionkeywordsMetaKeywords = By.xpath(
			"//font[contains(text(),'Keywords (Meta Keywords)')]/ancestor::tbody/tr[4]//div[@class='v-label v-widget custom-label1 v-label-custom-label1 v-label-undef-w']");
	private By dropHereToDeletekeywordsMetaKeywords = By
			.xpath("//table[4]/tbody/tr[5]//div[contains(text(),'Drop Here to Delete')]");
	private By dropdowFieldValueKeywords = By
			.xpath("//font[contains(text(),'Keywords (Meta Keywords)')]/ancestor::tbody/tr[2]//input");
	private By dropdowFieldButtonKeyWords = By.xpath(
			"//font[contains(text(),'Keywords (Meta Keywords)')]/ancestor::tbody/tr[2]//div[@class=\"v-filterselect-button\"]");
	private By AddingTextinFreeTextAttributeForKeywords = By.xpath(
			"//font[contains(text(),'Keywords (Meta Keywords)')]/ancestor::tbody//tr[3]//td[1]//div//input[contains(@class, 'v-textfield v-widget custom-label3 v-textfield-custom-label3')]");
	private By AttributeForKeywords = By
			.xpath("//font[contains(text(),'Keywords (Meta Keywords)')]/ancestor::tbody//tr[3]//td[1]//div//input");

	// 4.Description (Product or Category description)--Xpath
	private By descriptionProductOrCategoryDescription = By
			.xpath("//font[contains(text(),'Description (Product or Category description)')]");
	private By selectRuleParameterdropdown4 = By.xpath(
			"//font[contains(text(),'Description (Product or Category description)')]/ancestor::tbody/tr[2]//div[@role='button']");
	private By descriptiondescriptionProductOrCategoryDescription = By.xpath(
			"//font[contains(text(),'Description (Product or Category description)')]/ancestor::tbody/tr[4]//div[@class='v-label v-widget custom-label1 v-label-custom-label1 v-label-undef-w']");
	private By dropHereToDeletedescriptionProductOrCategoryDescription = By
			.xpath("//table[5]/tbody/tr[5]//div[contains(text(),'Drop Here to Delete')]");
	private By dropdowFieldValueDescription = By.xpath(
			"//font[contains(text(),'Description (Product or Category description)')]/ancestor::tbody/tr[2]//input");
	private By dropdowFieldButtonDescription = By.xpath(
			"//font[contains(text(),'Description (Product or Category description)')]/ancestor::tbody/tr[2]//div[@class=\"v-filterselect-button\"]");
	private By AddingTextinFreeTextAttributeForDescription = By.xpath(
			"//font[contains(text(),'Description (Product or Category description)')]/ancestor::tbody//tr[3]//td[1]//div//input[contains(@class, 'v-textfield v-widget custom-label3 v-textfield-custom-label3')]");
	private By AttributeForDescription = By.xpath(
			"//font[contains(text(),'Description (Product or Category description)')]/ancestor::tbody//tr[3]//td[1]//div//input");

	// 5.H1 Tag --Xpath
	private By H1Tag = By.xpath("//font[contains(text(),'H1 Tag')]");
	private By selectRuleParameterdropdownH1Tag = By
			.xpath("//font[contains(text(),'H1 Tag')]/ancestor::tbody/tr[2]//div[@role=\"button\"]");
	private By descriptionH1Tag = By.xpath(
			"//font[contains(text(),'H1 Tag')]/ancestor::tbody/tr[4]//div[@class='v-label v-widget custom-label1 v-label-custom-label1 v-label-undef-w']");
	private By dropHereToDeleteH1Tag = By.xpath("//table[6]/tbody/tr[5]//div[contains(text(),'Drop Here to Delete')]");
	private By dropdowFieldValueH1Tag = By.xpath("//font[contains(text(),'H1 Tag')]/ancestor::tbody/tr[2]//input");
	private By dropdowFieldButtonH1Tag = By
			.xpath("//font[contains(text(),'H1 Tag')]/ancestor::tbody/tr[2]//div[@class=\"v-filterselect-button\"]");
	private By AddingTextinFreeTextAttributeForH1Tag = By.xpath(
			"//font[contains(text(),'H1 Tag')]/ancestor::tbody//tr[3]//td[1]//div//input[contains(@class, 'v-textfield v-widget custom-label3 v-textfield-custom-label3')]");
	private By AttributeForH1Tag = By
			.xpath("//font[contains(text(),'H1 Tag')]/ancestor::tbody//tr[3]//td[1]//div//input");

	// 5.Owner of Category (Dental, Medical, Zahn)--Xpath
	private By OwnerOfCatagory = By.xpath("//font[contains(text(),'Owner of Category (Dental, Medical, Zahn)')]");
	private By selectRuleParameterdropdownOwnerOfCatagory = By.xpath(
			"//font[contains(text(),'Owner of Category (Dental, Medical, Zahn)')]/ancestor::tbody/tr[2]//div[@role=\"button\"]");
	private By descriptionOwnerOfCatagory = By.xpath(
			"//font[contains(text(),'Owner of Category (Dental, Medical, Zahn)')]/ancestor::tbody/tr[4]//div[@class='v-label v-widget custom-label1 v-label-custom-label1 v-label-undef-w']");
	private By dropHereToDeleteOwnerOfCatagory = By
			.xpath("//table[7]/tbody/tr[5]//div[contains(text(),'Drop Here to Delete')]");
	private By dropdowFieldValueOwnerofCategory = By
			.xpath("//font[contains(text(),'Owner of Category (Dental, Medical, Zahn)')]/ancestor::tbody/tr[2]//input");
	private By AttributeForOwnerOfCategory = By.xpath(
			"//font[contains(text(),'Owner of Category (Dental, Medical, Zahn)')]/ancestor::tbody//tr[3]//td[1]//div//input");
	private By AttributeForOwnerOfCategoryForTaxnomy = By.xpath(
			"//font[contains(text(),'Owner of Category (Dental, Medical, Zahn, Special Markets)')]/ancestor::tbody//tr[3]//td[1]//div//input");
	private By descriptionOwnerOfCatagoryForTaxnomy = By.xpath(
			"//font[contains(text(),'Owner of Category (Dental, Medical, Zahn, Special Markets)')]/ancestor::tbody/tr[4]//div[@class='v-label v-widget custom-label1 v-label-custom-label1 v-label-undef-w']");

	private By dropdowFieldButtonOwnerOfCategoryForTaxnomy = By.xpath(
			"//font[contains(text(),'Owner of Category (Dental, Medical, Zahn, Special Markets)')]/ancestor::tbody/tr[2]//div[@class=\"v-filterselect-button\"]");

	// General Xpath
	private final String dropdownvaluexpath = "//span[normalize-space()='${variable}']";

	private By DropdownValuesFromdropDownPage = By.xpath("//td[@role='listitem']//span");
	public By DropdownButtonFromdropDownPage = By.xpath("//div[@class='v-filterselect-nextpage']//span");

	public final String dropdownvaluexpathForQuantityAttribute = "//span[normalize-space()='$Quantity']";
	public final String freeTextField = "//span[normalize-space()='Free Text']";

	private By dropDownSelectingBlankValueXpath = By
			.xpath("//body[1]/div[2]/div[2]/div[1]/div[2]/table[1]/tbody[1]/tr[1]/td[1]");

	static String webdescritionvalue = "";

	private By SEOText = By.xpath("//div[contains(text(),'SEO')]");

	public SeoTabPage clickRetrieveRules() {
		click(RetriveRules, WaitLogic.CLICKABLE, "Retrive Rules");
		WaitForMiliSec(10000);
		return this;
	}

	public SeoTabPage clickGenerateDescription() {
		click(GenerateDescription, WaitLogic.CLICKABLE, "GenerateDescription");
		WaitForMiliSec(5000);
		return this;
	}

	public SeoTabPage clickSaveRules() {
		click(SaveRule, WaitLogic.CLICKABLE, "Save Rules");
		WaitForMiliSec(10000);
		return this;
	}

	
	// Adding Values from PageTitle
	public void addingAttributeFromPageTitleFieldAttribute(String pageTitleExpectedAttributes) {
		WaitForMiliSec(5000);

		List<WebElement> element = DriverManager.getDriver().findElements(AttributeForPagetitle);
		boolean attributeVisibity = false;
		List<String> actualAttributeArray = new ArrayList<>();
		for (WebElement webElement : element) {
			actualAttributeArray.add(webElement.getAttribute("value"));
		}
		Javautils util = new Javautils();
		List<String> expectedAttributesList = util.readMultipleValuesFromExcel(pageTitleExpectedAttributes);

		for (String string : expectedAttributesList) {
			if (!actualAttributeArray.contains(string)) {
				click(dropdowFieldValuenPageTitleBrowserTitle, WaitLogic.CLICKABLE, "click on dropdownfiled");
				sendKeys(dropdowFieldValuenPageTitleBrowserTitle, string, WaitLogic.PRESENCE, "expectedAttributes");
				WaitForMiliSec(1000);
				click(getElementByReplaceText(dropdownvaluexpath, string), WaitLogic.CLICKABLE,
						pageTitleExpectedAttributes);
				WaitForMiliSec(2000);
				click(GenerateDescription, WaitLogic.CLICKABLE, "generate Discription");
				WaitForMiliSec(5000);
				click(SaveRule, WaitLogic.CLICKABLE, "SaveRule");
				WaitForMiliSec(5000);
			}
		}
	}

	//Adding Attribute from Meta Data
	public void AddingAttributeFromMetaData_MetaDescription(String metaDataExpectedAttributes) {

		List<WebElement> element = DriverManager.getDriver().findElements(AttributeForMetaData);
		boolean attributeVisibity = false;
		List<String> actualAttributeArray = new ArrayList<>();
		for (WebElement webElement : element) {
			actualAttributeArray.add(webElement.getAttribute("value"));
		}
		Javautils util = new Javautils();
		List<String> expectedAttributesList = util.readMultipleValuesFromExcel(metaDataExpectedAttributes);

		for (String string : expectedAttributesList) {
			if (!actualAttributeArray.contains(string)) {
				click(dropdowFieldValueMetaData, WaitLogic.CLICKABLE, "click on dropdownfiled");
				sendKeys(dropdowFieldValueMetaData, string, WaitLogic.PRESENCE, "expectedAttributes");
				WaitForMiliSec(1000);
				click(getElementByReplaceText(dropdownvaluexpath, string), WaitLogic.CLICKABLE,
						metaDataExpectedAttributes);
				WaitForMiliSec(2000);
				click(GenerateDescription, WaitLogic.CLICKABLE, "generate Discription");
				WaitForMiliSec(2000);
				click(SaveRule, WaitLogic.CLICKABLE, "SaveRule");
				WaitForMiliSec(5000);
			}
		}
	}

	//Adding the attribute from Keywords
	public void AddingAttributeFromKeywords_MetaKeywords(String KeywordsExpectedAttributes) {

		List<WebElement> element = DriverManager.getDriver().findElements(AttributeForKeywords);
		boolean attributeVisibity = false;
		List<String> actualAttributeArray = new ArrayList<>();
		for (WebElement webElement : element) {
			actualAttributeArray.add(webElement.getAttribute("value"));
		}
		Javautils util = new Javautils();
		List<String> expectedAttributesList = util.readMultipleValuesFromExcel(KeywordsExpectedAttributes);

		for (String string : expectedAttributesList) {
			if (!actualAttributeArray.contains(string)) {
				click(dropdowFieldValueKeywords, WaitLogic.CLICKABLE, "click on dropdownfiled");
				sendKeys(dropdowFieldValueKeywords, string, WaitLogic.PRESENCE, "expectedAttributes");
				WaitForMiliSec(1000);
				click(getElementByReplaceText(dropdownvaluexpath, string), WaitLogic.CLICKABLE,
						KeywordsExpectedAttributes);
				WaitForMiliSec(2000);
				click(GenerateDescription, WaitLogic.CLICKABLE, "generate Discription");
				WaitForMiliSec(2000);
				click(SaveRule, WaitLogic.CLICKABLE, "SaveRule");
				WaitForMiliSec(5000);
			}
		}
	}

	//Adding the Attribute from Description
	public void addingAttributeFromDescription_ProductorCategorydescription(String DescriptionExpectedAttributes) {

		List<WebElement> element = DriverManager.getDriver().findElements(AttributeForDescription);
		boolean attributeVisibity = false;
		List<String> actualAttributeArray = new ArrayList<>();
		for (WebElement webElement : element) {
			actualAttributeArray.add(webElement.getAttribute("value"));
		}
		Javautils util = new Javautils();
		List<String> expectedAttributesList = util.readMultipleValuesFromExcel(DescriptionExpectedAttributes);

		for (String string : expectedAttributesList) {
			if (!actualAttributeArray.contains(string)) {
				click(dropdowFieldValueDescription, WaitLogic.CLICKABLE, "click on dropdownfiled");
				sendKeys(dropdowFieldValueDescription, string, WaitLogic.VISIBLE, "expectedAttributes");
				WaitForMiliSec(1000);
				click(getElementByReplaceText(dropdownvaluexpath, string), WaitLogic.CLICKABLE,
						DescriptionExpectedAttributes);
				WaitForMiliSec(2000);
				click(GenerateDescription, WaitLogic.CLICKABLE, "generate Discription");
				WaitForMiliSec(2000);
				click(SaveRule, WaitLogic.CLICKABLE, "SaveRule");
				WaitForMiliSec(5000);
			}
		}
	}

	//Adding Attribute from H1 Tag
	public void AddingAttributeFromH1_Tag(String h1TagExpectedAttributes) {

		List<WebElement> element = DriverManager.getDriver().findElements(AttributeForH1Tag);
		boolean attributeVisibity = false;
		List<String> actualAttributeArray = new ArrayList<>();
		for (WebElement webElement : element) {
			actualAttributeArray.add(webElement.getAttribute("value"));
		}
		Javautils util = new Javautils();
		List<String> expectedAttributesList = util.readMultipleValuesFromExcel(h1TagExpectedAttributes);

		for (String string : expectedAttributesList) {
			if (!actualAttributeArray.contains(string)) {
				click(dropdowFieldValueH1Tag, WaitLogic.CLICKABLE, "click on dropdownfiled");
				sendKeys(dropdowFieldValueH1Tag, string, WaitLogic.PRESENCE, "expectedAttributes");
				WaitForMiliSec(1000);
				click(getElementByReplaceText(dropdownvaluexpath, string), WaitLogic.CLICKABLE,
						h1TagExpectedAttributes);
				WaitForMiliSec(2000);
				click(GenerateDescription, WaitLogic.CLICKABLE, "generate Discription");
				WaitForMiliSec(2000);
				click(SaveRule, WaitLogic.CLICKABLE, "SaveRule");
				WaitForMiliSec(5000);
			}
		}
	}

	//Adding Attribute from Owner of category
	public void addingAttributeFromOwnerofCategory(String OwnerofCategoryExpectedAttributes) {

		List<WebElement> element = DriverManager.getDriver().findElements(AttributeForOwnerOfCategory);
		boolean attributeVisibity = false;
		List<String> actualAttributeArray = new ArrayList<>();
		for (WebElement webElement : element) {
			actualAttributeArray.add(webElement.getAttribute("value"));
		}
		Javautils util = new Javautils();
		List<String> expectedAttributesList = util.readMultipleValuesFromExcel(OwnerofCategoryExpectedAttributes);

		for (String string : expectedAttributesList) {
			if (!actualAttributeArray.contains(string)) {
				click(dropdowFieldValueOwnerofCategory, WaitLogic.CLICKABLE, "click on dropdownfiled");
				sendKeys(dropdowFieldValueOwnerofCategory, string, WaitLogic.PRESENCE, "expectedAttributes");
				WaitForMiliSec(1000);
				click(getElementByReplaceText(dropdownvaluexpath, string), WaitLogic.CLICKABLE,
						OwnerofCategoryExpectedAttributes);
				WaitForMiliSec(2000);
				click(GenerateDescription, WaitLogic.CLICKABLE, "generate Discription");
				WaitForMiliSec(2000);
				click(SaveRule, WaitLogic.CLICKABLE, "SaveRule");
				WaitForMiliSec(5000);
			}
		}
	}

	// Getting Attribute Values from PageTitle
	public String getAttributesFromPageTitle() {
		WaitForMiliSec(3000);
		List<WebElement> element = DriverManager.getDriver().findElements(AttributeForPagetitle);
		String attributesFromPageTitle = "";
		for (WebElement webElement : element) {
			attributesFromPageTitle = attributesFromPageTitle + " " + webElement.getAttribute("value");
		}
		return attributesFromPageTitle.trim();

	}

	public int getFreeTextAttributeCountFromPageTitle() {
		WaitForMiliSec(3000);
		List<WebElement> element = DriverManager.getDriver().findElements(AttributeForPagetitle);
		String attributesFromPageTitle = "";
		int count = 0;
		for (WebElement webElement : element) {
			if (webElement.getAttribute("value").equals("Free Text")) {
				count++;
			}
		}
		return count;

	}

	public int getFreeTextAttributeCountFromMetaData() {
		WaitForMiliSec(3000);
		List<WebElement> element = DriverManager.getDriver().findElements(AttributeForMetaData);
		String attributesFromMetaData = "";
		int count = 0;
		for (WebElement webElement : element) {
			if (webElement.getAttribute("value").equals("Free Text")) {
				count++;
			}
		}
		return count;

	}

	public int getFreeTextAttributeCountFromKeywords() {
		WaitForMiliSec(3000);
		List<WebElement> element = DriverManager.getDriver().findElements(AttributeForKeywords);
		String attributesFromMetaData = "";
		int count = 0;
		for (WebElement webElement : element) {
			if (webElement.getAttribute("value").equals("Free Text")) {
				count++;
			}
		}
		return count;
	}

	public int getFreeTextAttributeCountFromDescription() {
		WaitForMiliSec(3000);
		List<WebElement> element = DriverManager.getDriver().findElements(AttributeForDescription);
		String attributesFromMataData = "";
		int count = 0;
		for (WebElement webElement : element) {
			if (webElement.getAttribute("value").equals("Free Text")) {
				count++;
			}
		}
		return count;
	}

	public int getFreeTextAttributeCountFromH1Tag() {
		WaitForMiliSec(3000);
		List<WebElement> element = DriverManager.getDriver().findElements(AttributeForH1Tag);
		String attributesFromMetaData = "";
		int count = 0;
		for (WebElement webElement : element) {
			if (webElement.getAttribute("value").equals("Free Text")) {
				count++;
			}
		}
		return count;
	}

	// Getting Description value from Page title
	public String getDescriptionValueFromPageTitle() {
		WaitForMiliSec(3000);
		String descriptionValueFromPageTitle = getStringValues(descriptionFieldpageTitleBrowserTitle, WaitLogic.VISIBLE,
				"get DescriptionValue from Page title");
		return descriptionValueFromPageTitle;
	}

	// Getting Attribute Values from MetaData
	public String getAttributesFromMetaData() {
		List<WebElement> element = DriverManager.getDriver().findElements(AttributeForMetaData);
		String attributesFromMetaData = "";
		for (WebElement webElement : element) {
			attributesFromMetaData = attributesFromMetaData + " " + webElement.getAttribute("value");
		}
		return attributesFromMetaData.trim();

	}

	// Getting Description value from MetaData
	public String getDescriptionValueFromMetaData() {
		String descriptionValueFromMetaData = getStringValues(descriptionmetaDataMetaDescription, WaitLogic.VISIBLE,
				"get DescriptionValue from Meta Data");
		return descriptionValueFromMetaData;
	}

	// Getting Attribute Values from Keyword
	public String getAttributesFromKeywords() {
		List<WebElement> element = DriverManager.getDriver().findElements(AttributeForKeywords);
		String attributesFromKeyword = "";
		for (WebElement webElement : element) {
			attributesFromKeyword = attributesFromKeyword + " " + webElement.getAttribute("value");
		}
		return attributesFromKeyword.trim();

	}

	// Getting Description value from Keyword
	public String getDescriptionValueFromKeywords() {
		String descriptionValueFromKeyword = getStringValues(descriptionkeywordsMetaKeywords, WaitLogic.VISIBLE,
				"get DescriptionValue from Keyword");
		return descriptionValueFromKeyword;
	}

	// Getting Attribute Values from Description
	public String getAttributesFromDescription() {
		List<WebElement> element = DriverManager.getDriver().findElements(AttributeForDescription);
		String attributesFromDescription = "";
		for (WebElement webElement : element) {
			attributesFromDescription = attributesFromDescription + " " + webElement.getAttribute("value");
		}
		return attributesFromDescription.trim();

	}

	// Getting Description value from Description
	public String getDescriptionValueDescription() {
		String descriptionValueFromDescription = getStringValues(descriptiondescriptionProductOrCategoryDescription,
				WaitLogic.VISIBLE, "get DescriptionValue from Description");
		return descriptionValueFromDescription;
	}

	// Getting Attribute Values from H1Tag
	public String getAttributesFromH1Tag() {
		List<WebElement> element = DriverManager.getDriver().findElements(AttributeForH1Tag);
		String attributesFromH1Tag = "";
		for (WebElement webElement : element) {
			attributesFromH1Tag = attributesFromH1Tag + " " + webElement.getAttribute("value");
		}
		return attributesFromH1Tag.trim();

	}

	// Getting Description value from H1Tag
	public String getDescriptionValueH1Tag() {
		String descriptionValueFromH1Tag = getStringValues(descriptionH1Tag, WaitLogic.VISIBLE,
				"get DescriptionValue from H1Tag");
		return descriptionValueFromH1Tag;
	}

	// Getting Attribute Values from OwnerOfCatogory
	public String getAttributesFromOwnerOfCatogory() {
		List<WebElement> element = DriverManager.getDriver().findElements(By.xpath(
				"//font[contains(text(),'Owner of Category (Dental, Medical, Zahn)')]/ancestor::tbody//tr[3]//td[1]//div//input"));
		String attributesFromOwnerOfCatogory = "";
		for (WebElement webElement : element) {
			attributesFromOwnerOfCatogory = attributesFromOwnerOfCatogory + " " + webElement.getAttribute("value");
		}
		return attributesFromOwnerOfCatogory.trim();
	}

	// Getting Attribute Values from OwnerOfCatogory From Structures
	public String getAttributesFromOwnerOfCatogoryForTaxnomy() {
		List<WebElement> element = DriverManager.getDriver().findElements(AttributeForOwnerOfCategoryForTaxnomy);
		String attributesFromOwnerOfCatogory = "";
		for (WebElement webElement : element) {
			attributesFromOwnerOfCatogory = attributesFromOwnerOfCatogory + " " + webElement.getAttribute("value");
		}
		return attributesFromOwnerOfCatogory.trim();
	}

	// Getting Description value from OwnerOfCatogory
	public String getDescriptionValueOwnerOfCatogory() {
		String descriptionValueFromOwnerOfCataogory = getStringValues(descriptionOwnerOfCatagory, WaitLogic.VISIBLE,
				"get DescriptionValue from OwnerOfCataogory");
		return descriptionValueFromOwnerOfCataogory;
	}

	public String getDescriptionValueOwnerOfCatogoryForTaxnomy() {
		String descriptionValueFromOwnerOfCataogory = getStringValues(descriptionOwnerOfCatagoryForTaxnomy,
				WaitLogic.VISIBLE, "get DescriptionValue from OwnerOfCataogory");
		return descriptionValueFromOwnerOfCataogory;
	}
	
	// Deleting the Values From Pagetitle
	public void deletingValueFromPageTitle(String OverridePageTitleExpectedAttributes) {

		boolean attributeVisibity = false;
		Javautils util = new Javautils();
		List<String> expectedAttributesList = util.readMultipleValuesFromExcel(OverridePageTitleExpectedAttributes);
		WaitForMiliSec(5000);
		List<WebElement> element = DriverManager.getDriver().findElements(AttributeForPagetitle);
		for (String expected : expectedAttributesList) {
			for (WebElement webelement : element) {
				String actual = webelement.getAttribute("value");
				if (expected.contains(actual)) {
					dragAndDrop(webelement,
							DriverManager.getDriver().findElement(dropHereToDeletepageTitleBrowserTitle));
					WaitForMiliSec(5000);

				}

			}

		}
		WaitForMiliSec(2000);
		click(GenerateDescription, WaitLogic.CLICKABLE, "generate Discription");
		WaitForMiliSec(2000);
		click(SaveRule, WaitLogic.CLICKABLE, "SaveRule");
		WaitForMiliSec(5000);
	}

	// Deleting the Values For MetaData
	public void deletingValueFromMetaData(String OverrideMetaDataExpectedAttributes) {
		WaitForMiliSec(3000);

		boolean attributeVisibity = false;
		Javautils util = new Javautils();
		List<String> expectedAttributesList = util.readMultipleValuesFromExcel(OverrideMetaDataExpectedAttributes);
		WaitForMiliSec(5000);
		List<WebElement> element = DriverManager.getDriver().findElements(AttributeForMetaData);

		for (String expected : expectedAttributesList) {
			for (WebElement webelement : element) {
				String actual = webelement.getAttribute("value");
				if (expected.contains(actual)) {
					dragAndDrop(webelement,
							DriverManager.getDriver().findElement(dropHereToDeletemetaDataMetaDescription));
					WaitForMiliSec(5000);

				}

			}

		}
		WaitForMiliSec(2000);
		click(GenerateDescription, WaitLogic.CLICKABLE, "generate Discription");
		WaitForMiliSec(2000);
		click(SaveRule, WaitLogic.CLICKABLE, "SaveRule");
		WaitForMiliSec(5000);

	}

	// Deleting the Values For Keywords
	public void deletingValueFromKeywords(String OverrideKeywordExpectedAttributes) {

		boolean attributeVisibity = false;
		Javautils util = new Javautils();
		List<String> expectedAttributesList = util.readMultipleValuesFromExcel(OverrideKeywordExpectedAttributes);
		WaitForMiliSec(5000);
		List<WebElement> element = DriverManager.getDriver().findElements(AttributeForKeywords);

		for (String expected : expectedAttributesList) {
			for (WebElement webelement : element) {
				String actual = webelement.getAttribute("value");
				if (expected.contains(actual)) {
					dragAndDrop(webelement,
							DriverManager.getDriver().findElement(dropHereToDeletekeywordsMetaKeywords));
					WaitForMiliSec(5000);

				}

			}

		}
		WaitForMiliSec(2000);
		click(GenerateDescription, WaitLogic.CLICKABLE, "generate Discription");
		WaitForMiliSec(2000);
		click(SaveRule, WaitLogic.CLICKABLE, "SaveRule");
		WaitForMiliSec(5000);

	}

	// Overiding the Values For Description
	public void deletingValueFromDescription(String OverrideDescriptionExpectedAttributes) {

		boolean attributeVisibity = false;
		Javautils util = new Javautils();
		List<String> expectedAttributesList = util.readMultipleValuesFromExcel(OverrideDescriptionExpectedAttributes);
		WaitForMiliSec(5000);
		List<WebElement> element = DriverManager.getDriver().findElements(AttributeForDescription);

		for (String expected : expectedAttributesList) {
			for (WebElement webelement : element) {
				String actual = webelement.getAttribute("value");
				if (expected.contains(actual)) {
					dragAndDrop(webelement, DriverManager.getDriver()
							.findElement(dropHereToDeletedescriptionProductOrCategoryDescription));
					WaitForMiliSec(5000);

				}

			}

		}
		WaitForMiliSec(2000);
		click(GenerateDescription, WaitLogic.CLICKABLE, "generate Discription");
		WaitForMiliSec(2000);
		click(SaveRule, WaitLogic.CLICKABLE, "SaveRule");
		WaitForMiliSec(5000);

	}

	// Overiding the Values For H1Tag
	public void deletingValueFromH1Tag(String OverrideH1TagExpectedAttributes) {

		boolean attributeVisibity = false;
		Javautils util = new Javautils();
		List<String> expectedAttributesList = util.readMultipleValuesFromExcel(OverrideH1TagExpectedAttributes);
		WaitForMiliSec(5000);
		List<WebElement> element = DriverManager.getDriver().findElements(AttributeForH1Tag);

		for (String expected : expectedAttributesList) {
			for (WebElement webelement : element) {
				String actual = webelement.getAttribute("value");
				if (expected.contains(actual)) {
					dragAndDrop(webelement, DriverManager.getDriver().findElement(dropHereToDeleteH1Tag));
					WaitForMiliSec(5000);
				}

			}

		}
		WaitForMiliSec(2000);
		click(GenerateDescription, WaitLogic.CLICKABLE, "generate Discription");
		WaitForMiliSec(2000);
		click(SaveRule, WaitLogic.CLICKABLE, "SaveRule");
		WaitForMiliSec(5000);

	}

	// Overiding the Values For OwnerOfCatogory
	public void deletingValueFromOwnerOfCategory(String OverrideOwnerOfCategoryExpectedAttributes) {

		boolean attributeVisibity = false;
		Javautils util = new Javautils();
		List<String> expectedAttributesList = util
				.readMultipleValuesFromExcel(OverrideOwnerOfCategoryExpectedAttributes);
		WaitForMiliSec(5000);
		List<WebElement> element = DriverManager.getDriver().findElements(AttributeForOwnerOfCategory);
		//equalsIgnoreCase

		for (String expected : expectedAttributesList) {
			for (WebElement webelement : element) {
				String actual = webelement.getAttribute("value");
				if (expected.contains(actual)) {
					dragAndDrop(webelement, DriverManager.getDriver().findElement(dropHereToDeleteOwnerOfCatagory));
					WaitForMiliSec(5000);
					break;
				}

			}

		}
		WaitForMiliSec(2000);
		click(GenerateDescription, WaitLogic.CLICKABLE, "generate Discription");
		WaitForMiliSec(2000);
		click(SaveRule, WaitLogic.CLICKABLE, "SaveRule");
		WaitForMiliSec(5000);

	}

	// Overiding the Values For OwnerOfCatogory for Taxnomy
	public void overRidingValueFromOwnerOfCategoryForTaxanomy(String OverrideOwnerOfCategoryExpectedAttributes) {

		boolean attributeVisibity = false;
		Javautils util = new Javautils();
		List<String> expectedAttributesList = util
				.readMultipleValuesFromExcel(OverrideOwnerOfCategoryExpectedAttributes);
		WaitForMiliSec(5000);
		List<WebElement> element = DriverManager.getDriver().findElements(AttributeForOwnerOfCategoryForTaxnomy);

		for (String expected : expectedAttributesList) {
			for (WebElement webelement : element) {
				String actual = webelement.getAttribute("value");
				if (expected.equalsIgnoreCase(actual)) {
					dragAndDrop(webelement, DriverManager.getDriver().findElement(dropHereToDeleteOwnerOfCatagory));
					WaitForMiliSec(5000);
					break;
				}

			}

		}
		WaitForMiliSec(2000);
		click(GenerateDescription, WaitLogic.CLICKABLE, "generate Discription");
		WaitForMiliSec(2000);
		click(SaveRule, WaitLogic.CLICKABLE, "SaveRule");
		WaitForMiliSec(5000);

	}

	public void dragAndDropAttributesFromPageTitle() {

		WaitForMiliSec(5000);
		List<WebElement> element = DriverManager.getDriver().findElements(AttributeForPagetitle);
		String attributesFromPageTitle = "";
		for (WebElement webElement : element) {
			WaitForMiliSec(3000);

			dragAndDrop(webElement, DriverManager.getDriver().findElement(dropHereToDeletepageTitleBrowserTitle));

		}

		WaitForMiliSec(2000);
		click(GenerateDescription, WaitLogic.CLICKABLE, "generate Discription");
		WaitForMiliSec(2000);
		click(SaveRule, WaitLogic.CLICKABLE, "SaveRule");
		WaitForMiliSec(5000);

	}

	public void dragAndDropAttributesFromMetaData() {
		WaitForMiliSec(5000);
		List<WebElement> element = DriverManager.getDriver().findElements(AttributeForMetaData);
		for (WebElement webElement : element) {
			WaitForMiliSec(3000);

			dragAndDrop(webElement, DriverManager.getDriver().findElement(dropHereToDeletemetaDataMetaDescription));

		}
		WaitForMiliSec(2000);
		click(GenerateDescription, WaitLogic.CLICKABLE, "generate Discription");
		WaitForMiliSec(2000);
		click(SaveRule, WaitLogic.CLICKABLE, "SaveRule");
		WaitForMiliSec(5000);

	}

	public void dragAndDropAttributesFromKeywords() {
		WaitForMiliSec(5000);
		List<WebElement> element = DriverManager.getDriver().findElements(AttributeForKeywords);
		for (WebElement webElement : element) {
			WaitForMiliSec(3000);

			dragAndDrop(webElement, DriverManager.getDriver().findElement(dropHereToDeletekeywordsMetaKeywords));

		}
		WaitForMiliSec(2000);
		click(GenerateDescription, WaitLogic.CLICKABLE, "generate Discription");
		WaitForMiliSec(2000);
		click(SaveRule, WaitLogic.CLICKABLE, "SaveRule");
		WaitForMiliSec(5000);

	}

	public void dragAndDropAttributesFromDescription() {
		WaitForMiliSec(5000);
		List<WebElement> element = DriverManager.getDriver().findElements(AttributeForDescription);
		for (WebElement webElement : element) {
			WaitForMiliSec(3000);
			dragAndDrop(webElement,
					DriverManager.getDriver().findElement(dropHereToDeletedescriptionProductOrCategoryDescription));

		}
		WaitForMiliSec(2000);
		click(GenerateDescription, WaitLogic.CLICKABLE, "generate Discription");
		WaitForMiliSec(2000);
		click(SaveRule, WaitLogic.CLICKABLE, "SaveRule");
		WaitForMiliSec(5000);

	}

	public void dragAndDropAttributesFromH1Tag() {
		WaitForMiliSec(5000);
		List<WebElement> element = DriverManager.getDriver().findElements(AttributeForH1Tag);
		for (WebElement webElement : element) {
			WaitForMiliSec(3000);

			dragAndDrop(webElement, DriverManager.getDriver().findElement(dropHereToDeleteH1Tag));

		}
		WaitForMiliSec(2000);
		click(GenerateDescription, WaitLogic.CLICKABLE, "generate Discription");
		WaitForMiliSec(2000);
		click(SaveRule, WaitLogic.CLICKABLE, "SaveRule");
		WaitForMiliSec(5000);

	}

	public void dragAndDropAttributesFromOwnerOfCategory() {
		WaitForMiliSec(5000);
		List<WebElement> element = DriverManager.getDriver().findElements(AttributeForOwnerOfCategory);
		for (WebElement webElement : element) {
			WaitForMiliSec(3000);
			dragAndDrop(webElement, DriverManager.getDriver().findElement(dropHereToDeleteOwnerOfCatagory));

		}
		WaitForMiliSec(2000);
		click(GenerateDescription, WaitLogic.CLICKABLE, "generate Discription");
		WaitForMiliSec(2000);
		click(SaveRule, WaitLogic.CLICKABLE, "SaveRule");
		WaitForMiliSec(5000);
	}

	public void dropDownLovFromPageTitle(String QuantityField) {
		WaitForMiliSec(2000);
		click(dropdowFieldValuenPageTitleBrowserTitle, WaitLogic.CLICKABLE, "click on dropdownfiled");
		sendKeys(dropdowFieldValuenPageTitleBrowserTitle, QuantityField, WaitLogic.PRESENCE, "expectedAttributes");
		WaitForMiliSec(1000);

	}

	public void dropDownLovFromMetaData(String QuantityField) {
		WaitForMiliSec(4000);
		JSClick(dropdowFieldValueMetaData, WaitLogic.CLICKABLE, "click on dropdownfiled");
		WaitForMiliSec(3000);
		sendKeys(dropdowFieldValueMetaData, QuantityField, WaitLogic.PRESENCE, "expectedAttributes");
		WaitForMiliSec(1000);
	}

	public void dropDownLovFromKeywords(String QuantityField) {
		WaitForMiliSec(2000);
		click(dropdowFieldValueKeywords, WaitLogic.CLICKABLE, "click on dropdownfiled");
		sendKeys(dropdowFieldValueKeywords, QuantityField, WaitLogic.PRESENCE, "expectedAttributes");
		WaitForMiliSec(1000);
	}

	public void dropDownLovFromDescription(String QuantityField) {
		WaitForMiliSec(2000);
		click(dropdowFieldValueDescription, WaitLogic.CLICKABLE, "click on dropdownfiled");
		sendKeys(dropdowFieldValueDescription, QuantityField, WaitLogic.PRESENCE, "expectedAttributes");
		WaitForMiliSec(1000);
	}

	public void dropDownLovFromH1Tag(String QuantityField) {
		WaitForMiliSec(2000);
		click(dropdowFieldValueH1Tag, WaitLogic.CLICKABLE, "click on dropdownfiled");
		sendKeys(dropdowFieldValueH1Tag, QuantityField, WaitLogic.PRESENCE, "expectedAttributes");
		WaitForMiliSec(1000);
	}

	public void dropDownLovFromOwnerOfCategory(String QuantityField) {
		WaitForMiliSec(2000);
		click(dropdowFieldValueOwnerofCategory, WaitLogic.CLICKABLE, "click on dropdownfiled");
		sendKeys(dropdowFieldValueOwnerofCategory, QuantityField, WaitLogic.PRESENCE, "expectedAttributes");
		WaitForMiliSec(1000);
	}

	public boolean isDropDownValueVisible(String fieldname) {
		By by = null;
		try {
			by = (By) SeoTabPage.class.getField(fieldname).get(SeoTabPage.this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isVisible(by, fieldname, 10);
	}

	public void addingAndEditingAttributeFromPageTitleFieldAttribute(String pageTitleExpectedAttributes,
			String EditedFreeText) {
		WaitForMiliSec(5000);

		List<WebElement> element = DriverManager.getDriver().findElements(AttributeForPagetitle);
		boolean attributeVisibity = false;
		List<String> actualAttributeArray = new ArrayList<>();
		for (WebElement webElement : element) {
			actualAttributeArray.add(webElement.getAttribute("value"));
		}
		Javautils util = new Javautils();
		List<String> expectedAttributesList = util.readMultipleValuesFromExcel(pageTitleExpectedAttributes);

		for (String string : expectedAttributesList) {
			if (!actualAttributeArray.contains(string)) {
				click(dropdowFieldValuenPageTitleBrowserTitle, WaitLogic.CLICKABLE, "click on dropdownfiled");
				sendKeys(dropdowFieldValuenPageTitleBrowserTitle, string, WaitLogic.PRESENCE, "expectedAttributes");
				WaitForMiliSec(1000);
				click(getElementByReplaceText(dropdownvaluexpath, string), WaitLogic.CLICKABLE,
						pageTitleExpectedAttributes);
				WaitForMiliSec(2000);
				click(AddingTextinFreeTextAttributeForPageTitleBrowserTitle, WaitLogic.CLICKABLE, "click on FreeText");
				WaitForMiliSec(3000);
				Javautils.selectAllAndDeletingThroughKeboard();
				WaitForMiliSec(3000);
				sendKeys(AddingTextinFreeTextAttributeForPageTitleBrowserTitle, EditedFreeText, WaitLogic.PRESENCE,
						"AddingText to the freeTextfield");
				WaitForMiliSec(2000);
				click(GenerateDescription, WaitLogic.CLICKABLE, "generate Discription");
				WaitForMiliSec(5000);
				click(SaveRule, WaitLogic.CLICKABLE, "SaveRule");
				WaitForMiliSec(5000);
			}
		}
	}
	

	public void addingAndEditingAttributeFromMetaDataFieldAttribute(String MetaDataExpectedAttributes,
			String EditedFreeText) {
		WaitForMiliSec(5000);

		List<WebElement> element = DriverManager.getDriver().findElements(AttributeForMetaData);
		boolean attributeVisibity = false;
		List<String> actualAttributeArray = new ArrayList<>();
		for (WebElement webElement : element) {
			actualAttributeArray.add(webElement.getAttribute("value"));
		}
		Javautils util = new Javautils();
		List<String> expectedAttributesList = util.readMultipleValuesFromExcel(MetaDataExpectedAttributes);

		for (String string : expectedAttributesList) {
			if (!actualAttributeArray.contains(string)) {
				click(dropdowFieldValueMetaData, WaitLogic.CLICKABLE, "click on dropdownfiled");
				sendKeys(dropdowFieldValueMetaData, string, WaitLogic.PRESENCE, "expectedAttributes");
				WaitForMiliSec(3000);
				click(getElementByReplaceText(dropdownvaluexpath, string), WaitLogic.CLICKABLE,
						MetaDataExpectedAttributes);
				WaitForMiliSec(2000);
				click(AddingTextinFreeTextAttributeForMetaData, WaitLogic.CLICKABLE, "click on FreeText");
				WaitForMiliSec(3000);
				selectTextViaKeyboard();
				deleteSelected();
				DriverManager.getDriver().switchTo().activeElement().click();
				WaitForMiliSec(3000);
				DriverManager.getDriver().switchTo().activeElement().sendKeys(EditedFreeText);
				WaitForMiliSec(2000);
				click(GenerateDescription, WaitLogic.CLICKABLE, "generate Discription");
				WaitForMiliSec(5000);
				click(SaveRule, WaitLogic.CLICKABLE, "SaveRule");
				WaitForMiliSec(5000);
			}
		}
	}

	public void addingAndEditingAttributeFromKeywordsFieldAttribute(String KeywordsExpectedAttributes,
			String EditedFreeText) {
		WaitForMiliSec(5000);

		List<WebElement> element = DriverManager.getDriver().findElements(AttributeForKeywords);
		boolean attributeVisibity = false;
		List<String> actualAttributeArray = new ArrayList<>();
		for (WebElement webElement : element) {
			actualAttributeArray.add(webElement.getAttribute("value"));
		}
		Javautils util = new Javautils();
		List<String> expectedAttributesList = util.readMultipleValuesFromExcel(KeywordsExpectedAttributes);

		for (String string : expectedAttributesList) {
			if (!actualAttributeArray.contains(string)) {
				click(dropdowFieldValueKeywords, WaitLogic.CLICKABLE, "click on dropdownfiled");
				sendKeys(dropdowFieldValueKeywords, string, WaitLogic.PRESENCE, "expectedAttributes");
				WaitForMiliSec(1000);
				click(getElementByReplaceText(dropdownvaluexpath, string), WaitLogic.CLICKABLE,
						KeywordsExpectedAttributes);
				WaitForMiliSec(2000);
				click(AddingTextinFreeTextAttributeForKeywords, WaitLogic.CLICKABLE, "click on FreeText");
				WaitForMiliSec(3000);
				selectTextViaKeyboard();
				deleteSelected();
				DriverManager.getDriver().switchTo().activeElement().click();
				WaitForMiliSec(3000);
				DriverManager.getDriver().switchTo().activeElement().sendKeys(EditedFreeText);
				WaitForMiliSec(2000);
				click(GenerateDescription, WaitLogic.CLICKABLE, "generate Discription");
				WaitForMiliSec(5000);
				click(SaveRule, WaitLogic.CLICKABLE, "SaveRule");
				WaitForMiliSec(5000);
			}
		}
	}

	public void addingAndEditingAttributeFromDescriptionFieldAttribute(String DescriptionExpectedAttributes,
			String EditedFreeText) {
		WaitForMiliSec(5000);

		List<WebElement> element = DriverManager.getDriver().findElements(AttributeForDescription);
		boolean attributeVisibity = false;
		List<String> actualAttributeArray = new ArrayList<>();
		for (WebElement webElement : element) {
			actualAttributeArray.add(webElement.getAttribute("value"));
		}
		Javautils util = new Javautils();
		List<String> expectedAttributesList = util.readMultipleValuesFromExcel(DescriptionExpectedAttributes);

		for (String string : expectedAttributesList) {
			if (!actualAttributeArray.contains(string)) {
				click(dropdowFieldValueDescription, WaitLogic.CLICKABLE, "click on dropdownfiled");
				sendKeys(dropdowFieldValueDescription, string, WaitLogic.PRESENCE, "expectedAttributes");
				WaitForMiliSec(3000);
				click(getElementByReplaceText(dropdownvaluexpath, string), WaitLogic.CLICKABLE,
						DescriptionExpectedAttributes);
				WaitForMiliSec(3000);
				click(AddingTextinFreeTextAttributeForDescription, WaitLogic.CLICKABLE, "click on FreeText");
				WaitForMiliSec(3000);
				selectTextViaKeyboard();
				deleteSelected();
				DriverManager.getDriver().switchTo().activeElement().click();
				WaitForMiliSec(3000);
				DriverManager.getDriver().switchTo().activeElement().sendKeys(EditedFreeText);
				WaitForMiliSec(2000);
				click(GenerateDescription, WaitLogic.CLICKABLE, "generate Discription");
				WaitForMiliSec(5000);
				click(SaveRule, WaitLogic.CLICKABLE, "SaveRule");
				WaitForMiliSec(5000);
			}
		}
	}

	public void addingAndEditingAttributeFromH1TagFieldAttribute(String H1tagExpectedAttributes,
			String EditedFreeText) {
			WaitForMiliSec(5000);

			List<WebElement> element = DriverManager.getDriver().findElements(AttributeForH1Tag);
			boolean attributeVisibity = false;
			List<String> actualAttributeArray = new ArrayList<>();
			for (WebElement webElement : element) {
				actualAttributeArray.add(webElement.getAttribute("value"));
			}
			Javautils util = new Javautils();
			List<String> expectedAttributesList = util.readMultipleValuesFromExcel(H1tagExpectedAttributes);

			for (String string : expectedAttributesList) {
				if (!actualAttributeArray.contains(string)) {
					click(dropdowFieldValueH1Tag, WaitLogic.CLICKABLE, "click on dropdownfiled");
					sendKeys(dropdowFieldValueH1Tag, string, WaitLogic.PRESENCE, "expectedAttributes");
					WaitForMiliSec(3000);
					click(getElementByReplaceText(dropdownvaluexpath, string), WaitLogic.CLICKABLE,
							H1tagExpectedAttributes);
					WaitForMiliSec(3000);
					scrollToElement(AddingTextinFreeTextAttributeForH1Tag,  "AddingTextinFreeTextAttributeForH1Tag");
					click(AddingTextinFreeTextAttributeForH1Tag, WaitLogic.CLICKABLE, "click on FreeText");
					WaitForMiliSec(3000);
					selectTextViaKeyboard();
					deleteSelected();
					DriverManager.getDriver().switchTo().activeElement().click();
					WaitForMiliSec(3000);
					DriverManager.getDriver().switchTo().activeElement().sendKeys(EditedFreeText);
					WaitForMiliSec(2000);
					click(GenerateDescription, WaitLogic.CLICKABLE, "generate Discription");
					WaitForMiliSec(5000);
					click(SaveRule, WaitLogic.CLICKABLE, "SaveRule");
					WaitForMiliSec(5000);
				}
			}
		}


	public List<String> dropDownLovValuesFromPagetitle() {
		WaitForMiliSec(2000);
		click(dropdowFieldButtonPageTitleBrowserTitle, WaitLogic.CLICKABLE, "click on dropdownfiled");
		WaitForMiliSec(3000);
		List<WebElement> DropdownListOfValuesFromPageTitleWebElements = DriverManager.getDriver()
				.findElements(DropdownValuesFromdropDownPage);
		List<String> DropdownListOfValuesFromPageTitle = new ArrayList<>();

		for (WebElement webElement : DropdownListOfValuesFromPageTitleWebElements) {
			DropdownListOfValuesFromPageTitle.add(webElement.getText());
		}
		WaitForMiliSec(3000);
		while (isVisible(DropdownButtonFromdropDownPage, "", 5)) {
			DriverManager.getDriver().findElement(DropdownButtonFromdropDownPage).click();
			WaitForMiliSec(3000);
			DropdownListOfValuesFromPageTitleWebElements = DriverManager.getDriver()
					.findElements(DropdownValuesFromdropDownPage);
			for (WebElement webElement : DropdownListOfValuesFromPageTitleWebElements) {
				DropdownListOfValuesFromPageTitle.add(webElement.getText());
			}
		}

		return DropdownListOfValuesFromPageTitle;
	}

	public List<String> dropDownLovValuesFromMetaData() {
		WaitForMiliSec(2000);
		click(dropdowFieldButtonMetaData, WaitLogic.CLICKABLE, "click on dropdownfiled");
		WaitForMiliSec(3000);
		List<WebElement> DropdownListOfValuesFromMetaDataWebElements = DriverManager.getDriver()
				.findElements(DropdownValuesFromdropDownPage);
		List<String> DropdownListOfValuesFromMetaData = new ArrayList<>();

		for (WebElement webElement : DropdownListOfValuesFromMetaDataWebElements) {
			DropdownListOfValuesFromMetaData.add(webElement.getText());
		}
		WaitForMiliSec(3000);
		while (isVisible(DropdownButtonFromdropDownPage, "", 5)) {
			DriverManager.getDriver().findElement(DropdownButtonFromdropDownPage).click();
			WaitForMiliSec(3000);
			DropdownListOfValuesFromMetaDataWebElements = DriverManager.getDriver()
					.findElements(DropdownValuesFromdropDownPage);
			for (WebElement webElement : DropdownListOfValuesFromMetaDataWebElements) {
				DropdownListOfValuesFromMetaData.add(webElement.getText());
			}
		}

		return DropdownListOfValuesFromMetaData;
	}

	public List<String> dropDownLovValuesFromKeywords() {
		WaitForMiliSec(2000);
		click(dropdowFieldButtonKeyWords, WaitLogic.CLICKABLE, "click on dropdownfiled");
		WaitForMiliSec(3000);
		List<WebElement> DropdownListOfValuesFromKeywordsWebElements = DriverManager.getDriver()
				.findElements(DropdownValuesFromdropDownPage);
		List<String> DropdownListOfValuesFromKeywords = new ArrayList<>();

		for (WebElement webElement : DropdownListOfValuesFromKeywordsWebElements) {
			DropdownListOfValuesFromKeywords.add(webElement.getText());
		}
		WaitForMiliSec(3000);
		while (isVisible(DropdownButtonFromdropDownPage, "", 5)) {
			DriverManager.getDriver().findElement(DropdownButtonFromdropDownPage).click();
			WaitForMiliSec(3000);
			DropdownListOfValuesFromKeywordsWebElements = DriverManager.getDriver()
					.findElements(DropdownValuesFromdropDownPage);
			for (WebElement webElement : DropdownListOfValuesFromKeywordsWebElements) {
				DropdownListOfValuesFromKeywords.add(webElement.getText());
			}
		}

		return DropdownListOfValuesFromKeywords;
	}

	public List<String> dropDownLovValuesFromDescription() {
		WaitForMiliSec(2000);
		click(dropdowFieldButtonDescription, WaitLogic.CLICKABLE, "click on dropdownfiled");
		WaitForMiliSec(3000);
		List<WebElement> DropdownListOfValuesFromDescriptionWebElements = DriverManager.getDriver()
				.findElements(DropdownValuesFromdropDownPage);
		List<String> DropdownListOfValuesFromDescription = new ArrayList<>();

		for (WebElement webElement : DropdownListOfValuesFromDescriptionWebElements) {
			DropdownListOfValuesFromDescription.add(webElement.getText());
		}
		WaitForMiliSec(3000);
		while (isVisible(DropdownButtonFromdropDownPage, "", 5)) {
			DriverManager.getDriver().findElement(DropdownButtonFromdropDownPage).click();
			WaitForMiliSec(3000);
			DropdownListOfValuesFromDescriptionWebElements = DriverManager.getDriver()
					.findElements(DropdownValuesFromdropDownPage);
			for (WebElement webElement : DropdownListOfValuesFromDescriptionWebElements) {
				DropdownListOfValuesFromDescription.add(webElement.getText());
			}
		}

		return DropdownListOfValuesFromDescription;
	}

	public List<String> dropDownLovValuesFromH1Tag() {
		WaitForMiliSec(2000);
		click(dropdowFieldButtonH1Tag, WaitLogic.CLICKABLE, "click on dropdownfiled");
		WaitForMiliSec(3000);
		List<WebElement> DropdownListOfValuesFromH1TagWebElements = DriverManager.getDriver()
				.findElements(DropdownValuesFromdropDownPage);
		List<String> DropdownListOfValuesFromH1Tag = new ArrayList<>();

		for (WebElement webElement : DropdownListOfValuesFromH1TagWebElements) {
			DropdownListOfValuesFromH1Tag.add(webElement.getText());
		}
		WaitForMiliSec(3000);
		while (isVisible(DropdownButtonFromdropDownPage, "", 5)) {
			DriverManager.getDriver().findElement(DropdownButtonFromdropDownPage).click();
			WaitForMiliSec(3000);
			DropdownListOfValuesFromH1TagWebElements = DriverManager.getDriver()
					.findElements(DropdownValuesFromdropDownPage);
			for (WebElement webElement : DropdownListOfValuesFromH1TagWebElements) {
				DropdownListOfValuesFromH1Tag.add(webElement.getText());
			}
		}

		return DropdownListOfValuesFromH1Tag;
	}

	public String getSEOName() {
		WaitForMiliSec(2000);
		String SEOName = getStringValues(SEOText, WaitLogic.VISIBLE, "get SEO Name");
		return SEOName.trim();
	}

	public String getAddedFreeTextSpace(String descriptionValueBeforeAddingFreeText,
			String descriptionValueAfterAddingFreeText) {
		WaitForMiliSec(3000);
		String[] a = descriptionValueAfterAddingFreeText.split(descriptionValueBeforeAddingFreeText + " ");
		return a[1];
	}

	public void addingTwiseFreeTextinPageTitle(String AddingFreeText) {
		click(dropdowFieldValuenPageTitleBrowserTitle, WaitLogic.CLICKABLE, "click on dropdownfiled");
		sendKeys(dropdowFieldValuenPageTitleBrowserTitle, AddingFreeText, WaitLogic.PRESENCE, "expectedAttributes");
		click(getElementByReplaceText(dropdownvaluexpath, AddingFreeText), WaitLogic.CLICKABLE, AddingFreeText);
		WaitForMiliSec(2000);
		click(dropdowFieldValuenPageTitleBrowserTitle, WaitLogic.CLICKABLE, "click on dropdownfiled");
		sendKeys(dropdowFieldValuenPageTitleBrowserTitle, AddingFreeText, WaitLogic.PRESENCE, "expectedAttributes");
		click(getElementByReplaceText(dropdownvaluexpath, AddingFreeText), WaitLogic.CLICKABLE, AddingFreeText);
		WaitForMiliSec(2000);
		click(GenerateDescription, WaitLogic.CLICKABLE, "generate Discription");
		WaitForMiliSec(2000);
		click(SaveRule, WaitLogic.CLICKABLE, "SaveRule");
		WaitForMiliSec(2000);
		return;
	}

	public void addingTwiseFreeTextinMetaData(String AddingFreeText) {
		click(dropdowFieldValueMetaData, WaitLogic.CLICKABLE, "click on dropdownfiled");
		sendKeys(dropdowFieldValueMetaData, AddingFreeText, WaitLogic.PRESENCE, "expectedAttributes");
		click(getElementByReplaceText(dropdownvaluexpath, AddingFreeText), WaitLogic.CLICKABLE, AddingFreeText);
		WaitForMiliSec(2000);
		click(dropdowFieldValueMetaData, WaitLogic.CLICKABLE, "click on dropdownfiled");
		sendKeys(dropdowFieldValueMetaData, AddingFreeText, WaitLogic.PRESENCE, "expectedAttributes");
		click(getElementByReplaceText(dropdownvaluexpath, AddingFreeText), WaitLogic.CLICKABLE, AddingFreeText);
		WaitForMiliSec(2000);
		click(GenerateDescription, WaitLogic.CLICKABLE, "generate Discription");
		WaitForMiliSec(2000);
		click(SaveRule, WaitLogic.CLICKABLE, "SaveRule");
		WaitForMiliSec(2000);
		return;
	}

	public void addingTwiseFreeTextinKeyWords(String AddingFreeText) {
		click(dropdowFieldValueKeywords, WaitLogic.CLICKABLE, "click on dropdownfiled");
		sendKeys(dropdowFieldValueKeywords, AddingFreeText, WaitLogic.PRESENCE, "expectedAttributes");
		click(getElementByReplaceText(dropdownvaluexpath, AddingFreeText), WaitLogic.CLICKABLE, AddingFreeText);
		WaitForMiliSec(2000);
		click(dropdowFieldValueKeywords, WaitLogic.CLICKABLE, "click on dropdownfiled");
		sendKeys(dropdowFieldValueKeywords, AddingFreeText, WaitLogic.PRESENCE, "expectedAttributes");
		click(getElementByReplaceText(dropdownvaluexpath, AddingFreeText), WaitLogic.CLICKABLE, AddingFreeText);
		WaitForMiliSec(2000);
		click(GenerateDescription, WaitLogic.CLICKABLE, "generate Discription");
		WaitForMiliSec(2000);
		click(SaveRule, WaitLogic.CLICKABLE, "SaveRule");
		WaitForMiliSec(2000);
		return;
	}

	public void addingTwiseFreeTextinDescription(String AddingFreeText) {
		click(dropdowFieldValueDescription, WaitLogic.CLICKABLE, "click on dropdownfiled");
		sendKeys(dropdowFieldValueDescription, AddingFreeText, WaitLogic.PRESENCE, "expectedAttributes");
		click(getElementByReplaceText(dropdownvaluexpath, AddingFreeText), WaitLogic.CLICKABLE, AddingFreeText);
		WaitForMiliSec(2000);
		click(dropdowFieldValueDescription, WaitLogic.CLICKABLE, "click on dropdownfiled");
		sendKeys(dropdowFieldValueDescription, AddingFreeText, WaitLogic.PRESENCE, "expectedAttributes");
		click(getElementByReplaceText(dropdownvaluexpath, AddingFreeText), WaitLogic.CLICKABLE, AddingFreeText);
		WaitForMiliSec(2000);
		click(GenerateDescription, WaitLogic.CLICKABLE, "generate Discription");
		WaitForMiliSec(2000);
		click(SaveRule, WaitLogic.CLICKABLE, "SaveRule");
		WaitForMiliSec(2000);
		return;
	}

	public void addingTwiseFreeTextinH1Tag(String AddingFreeText) {
		click(dropdowFieldValueH1Tag, WaitLogic.CLICKABLE, "click on dropdownfiled");
		sendKeys(dropdowFieldValueH1Tag, AddingFreeText, WaitLogic.PRESENCE, "expectedAttributes");
		click(getElementByReplaceText(dropdownvaluexpath, AddingFreeText), WaitLogic.CLICKABLE, AddingFreeText);
		WaitForMiliSec(2000);
		click(dropdowFieldValueH1Tag, WaitLogic.CLICKABLE, "click on dropdownfiled");
		sendKeys(dropdowFieldValueH1Tag, AddingFreeText, WaitLogic.PRESENCE, "expectedAttributes");
		click(getElementByReplaceText(dropdownvaluexpath, AddingFreeText), WaitLogic.CLICKABLE, AddingFreeText);
		WaitForMiliSec(2000);
		click(GenerateDescription, WaitLogic.CLICKABLE, "generate Discription");
		WaitForMiliSec(2000);
		click(SaveRule, WaitLogic.CLICKABLE, "SaveRule");
		WaitForMiliSec(2000);
		return;
	}

	public boolean dragAndMoveAttributeFunctionalityPageTitle() {
		WaitForMiliSec(2000);
		boolean dragAndDropWorking = false;
		String beforeSwapFirstElementValue = getAttributeValues(AttributeForFirstElement, WaitLogic.VISIBLE);
		System.out.println(beforeSwapFirstElementValue);
		String beforeSwapSecondElementValue = getAttributeValues(AttributeForsecondElement, WaitLogic.VISIBLE);
		System.out.println(beforeSwapSecondElementValue);
		dragAndDrop(DriverManager.getDriver().findElement(AttributeForsecondElement),
				DriverManager.getDriver().findElement(AttributeForFirstElement));
		WaitForMiliSec(2000);
		String afterSwapFirstElementValue = getAttributeValues(AttributeForFirstElement, WaitLogic.VISIBLE);

		String afterSwapSecondElementValue = getAttributeValues(AttributeForsecondElement, WaitLogic.VISIBLE);

		if (beforeSwapFirstElementValue.equals(afterSwapSecondElementValue)
				& beforeSwapSecondElementValue.equals(afterSwapFirstElementValue)) {
			dragAndDropWorking = true;
		}
		return dragAndDropWorking;
	}

	public void addingBlankValueFromPageTitle() {
		click(dropdowFieldButtonPageTitleBrowserTitle, WaitLogic.CLICKABLE, "click on dropdownfiled");
		WaitForMiliSec(2000);
		click(dropDownSelectingBlankValueXpath, WaitLogic.CLICKABLE, "Selecting Blank dropdownfiledValue");
		WaitForMiliSec(2000);
		click(GenerateDescription, WaitLogic.CLICKABLE, "generate Discription");
		WaitForMiliSec(2000);
		click(SaveRule, WaitLogic.CLICKABLE, "SaveRule");
		WaitForMiliSec(2000);
		return;
	}

	public void addingBlankValueFromMetaData() {
		click(dropdowFieldButtonMetaData, WaitLogic.CLICKABLE, "click on dropdownfiled");
		WaitForMiliSec(2000);
		click(dropDownSelectingBlankValueXpath, WaitLogic.CLICKABLE, "Selecting Blank dropdownfiledValue");
		WaitForMiliSec(2000);
		click(GenerateDescription, WaitLogic.CLICKABLE, "generate Discription");
		WaitForMiliSec(2000);
		click(SaveRule, WaitLogic.CLICKABLE, "SaveRule");
		WaitForMiliSec(2000);
		return;
	}

	public void addingBlankValueFromKeyword() {
		click(dropdowFieldButtonKeyWords, WaitLogic.CLICKABLE, "click on dropdownfiled");
		WaitForMiliSec(2000);
		click(dropDownSelectingBlankValueXpath, WaitLogic.CLICKABLE, "Selecting Blank dropdownfiledValue");
		WaitForMiliSec(2000);
		click(GenerateDescription, WaitLogic.CLICKABLE, "generate Discription");
		WaitForMiliSec(2000);
		click(SaveRule, WaitLogic.CLICKABLE, "SaveRule");
		WaitForMiliSec(2000);
		return;
	}

	public void addingBlankValueFromDescription() {
		click(dropdowFieldButtonDescription, WaitLogic.CLICKABLE, "click on dropdownfiled");
		WaitForMiliSec(2000);
		click(dropDownSelectingBlankValueXpath, WaitLogic.CLICKABLE, "Selecting Blank dropdownfiledValue");
		WaitForMiliSec(2000);
		click(GenerateDescription, WaitLogic.CLICKABLE, "generate Discription");
		WaitForMiliSec(2000);
		click(SaveRule, WaitLogic.CLICKABLE, "SaveRule");
		WaitForMiliSec(2000);
		return;
	}

	public void addingBlankValueFromH1Tag() {
		click(dropdowFieldButtonH1Tag, WaitLogic.CLICKABLE, "click on dropdownfiled");
		WaitForMiliSec(2000);
		click(dropDownSelectingBlankValueXpath, WaitLogic.CLICKABLE, "Selecting Blank dropdownfiledValue");
		WaitForMiliSec(2000);
		click(GenerateDescription, WaitLogic.CLICKABLE, "generate Discription");
		WaitForMiliSec(2000);
		click(SaveRule, WaitLogic.CLICKABLE, "SaveRule");
		WaitForMiliSec(2000);
		return;
	}

	public void addingBlankValueFromOwnerOfCategoryForTaxnomy() {
		click(dropdowFieldButtonOwnerOfCategoryForTaxnomy, WaitLogic.CLICKABLE, "click on dropdownfiled");
		WaitForMiliSec(2000);
		click(dropDownSelectingBlankValueXpath, WaitLogic.CLICKABLE, "Selecting Blank dropdownfiledValue");
		WaitForMiliSec(2000);
		click(GenerateDescription, WaitLogic.CLICKABLE, "generate Discription");
		WaitForMiliSec(2000);
		click(SaveRule, WaitLogic.CLICKABLE, "SaveRule");
		WaitForMiliSec(2000);
		return;
	}

	public void updateDataInExcel(String DescriptionValueFromPageTitle, String DescriptionValueFromMetaData,
			String DescriptionValueFromKeyWords, String DescriptionValueFromDescription,
			String DescriptionValueFromH1Tag) {

		ExcelUtils excel = new ExcelUtils();
		String path = System.getProperty("user.dir")
				+ "\\src\\test\\resources\\excel\\PimUpdatedDataForEshineValidation.xlsx";
		excel.excelDataSheetToWrite(path, "Sheet1");
		int lastRowCount = excel.getSheet(path, "Sheet1").getLastRowCount();
		excel.getCellValue(lastRowCount, 0);
		String TodayDate = DateandTimeUtils.getTodaysDate();
		if (excel.getCellValue(lastRowCount, 0).equals(TodayDate)) {
			excel.setRow(lastRowCount);
		} else {
			excel.createRow(lastRowCount + 1);
		}
		excel.setData(0, TodayDate).setData(1, DescriptionValueFromPageTitle).setData(2, DescriptionValueFromMetaData)
				.setData(3, DescriptionValueFromKeyWords).setData(4, DescriptionValueFromDescription)
				.setData(5, DescriptionValueFromH1Tag).closeWriting();

	}

	public Map<String, String> getUpdatedPimFromExcel() {

		ExcelUtils excel = new ExcelUtils();
		String path = System.getProperty("user.dir")
				+ "/src/test/resources/excel/PimUpdatedDataForEshineValidation.xlsx";
		excel.excelDataSheetToWrite(path, "Sheet1");

		List<String> dateList = excel.getColumnValues(0);
		Collections.sort(dateList, Collections.reverseOrder());

		String dateValueForComparision = "";
		try {

			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			String dateStr1 = DateandTimeUtils.getSpecifiedDayFromCurrentDay(-4);

			Date date1 = formatter.parse(dateStr1);

			for (String dayFromList : dateList) {

				Date date2 = formatter.parse(dayFromList);
				if (date1.after(date2) | date1.equals(date2)) {
					dateValueForComparision = dayFromList;
					break;
				}

			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		return excel.getSpecifiedRowDataRowHeader(dateValueForComparision);

	}

}