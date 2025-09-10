package com.pim.tests.SEO;

import com.pim.annotations.PimFrameworkAnnotation;
import com.pim.annotations.TestDataSheet;
import com.pim.driver.DriverManager;
import com.pim.enums.CategoryType;
import com.pim.enums.LogType;
import com.pim.enums.Modules;
import com.pim.enums.TestCaseSheet;
import com.pim.pages.FieldSelectionPage;
import com.pim.pages.GlobalAttributePage;
import com.pim.pages.LocalAttributePage;
import com.pim.pages.LoginPage;
import com.pim.pages.MainMenu;
import com.pim.pages.PimHomepage;
import com.pim.pages.ProductDetailSearchPage;
import com.pim.pages.QualityStatusPage;
import com.pim.pages.QueriesSubMenu;
import com.pim.pages.SeoTabPage;
import com.pim.pages.WebDescription;
import com.pim.reports.ExtentReport;
import com.pim.tests.BaseTest;
import com.pim.utils.DataProviderUtils;
import com.pim.utils.DateandTimeUtils;
import com.pim.utils.ExcelUtils;
import com.pim.utils.Javautils;

import static com.pim.reports.FrameworkLogger.log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

public final class SeoTests extends BaseTest {
	private SeoTests() {

	}

	LocalAttributePage localAttributePage = new LocalAttributePage();
	ProductDetailSearchPage productDetailSearchPage = new ProductDetailSearchPage();
	QualityStatusPage qualityStatusPage = new QualityStatusPage();
	SeoTabPage seoTabPage = new SeoTabPage();
	GlobalAttributePage globalAttributePage = new GlobalAttributePage();
	WebDescription webDescription = new WebDescription();
	QueriesSubMenu queriesSubMenu = new QueriesSubMenu();
	FieldSelectionPage fieldselectionpage = new FieldSelectionPage();
	MainMenu mainMenu = new MainMenu();

	// SEO_PL_001
	@PimFrameworkAnnotation(module = Modules.SEO, category = CategoryType.SANITY)
	@TestDataSheet(sheetname = TestCaseSheet.SEO_Tab)
	@Test(description = "SEO_PL_001 | Verifying the default rule in SEO Page for PageTitle", groups = {"US","pim","SANITY","SEO"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void Verify_default_Rules_in_SEO_populated_when_nochange_made_to_item_Rule_Page_Title(
			Map<String, String> map) {

		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("itemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("SEOTAB"));

		seoTabPage.clickRetrieveRules();
		String actualAttributesFromPageTitle = seoTabPage.getAttributesFromPageTitle();
		String ExpectedAttributesFromPageTitle = map.get("AttributeforPageTitile");
		Assertions.assertThat(actualAttributesFromPageTitle).isEqualTo(ExpectedAttributesFromPageTitle);

		String actualDescriptionValueFromPageTitle = seoTabPage.getDescriptionValueFromPageTitle();

		// Getting expected value from different Tab

		String expectedOwnerDivision = seoTabPage.getDescriptionValueOwnerOfCatogory();

		// Navigating to Local Attribute Tab
		productDetailSearchPage.clickRefreshIcon().selectTabfromDropdown(map.get("TabName"));
		String expectedBrandValue = localAttributePage.getBrandValue();
		String expectedItemValue = localAttributePage.getItemValue();
		String expectedItemtypeValue = localAttributePage.getItemTypeValue();

		// Navigating to Global Attribute Tab
		productDetailSearchPage.clickRefreshIcon().selectTabfromDropdown(map.get("ContinueTabName"));
		String expectedManufacturePartNumber = globalAttributePage.getManufacturerPartNumber();

		// Appending All Expected Value
		String expectedDescriptionValueFromPageTitle = expectedBrandValue + expectedManufacturePartNumber
				+ expectedItemtypeValue + expectedItemValue + "- Henry Schein " + expectedOwnerDivision;

		Assertions.assertThat(actualDescriptionValueFromPageTitle).isEqualTo(expectedDescriptionValueFromPageTitle);

		log(LogType.INFO, "Validating default rule in Page title");

	}

	// SEO_PL_002
	@PimFrameworkAnnotation(module = Modules.SEO, category = CategoryType.SANITY)
	@TestDataSheet(sheetname = TestCaseSheet.SEO_Tab)
	@Test(description = "SEO_PL_002 | Verifying the default rule in SEO Page MetaData", groups = {
			"US","pim","SANITY","SEO"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void Verify_default_Rules_in_SEO_populated_when_nochange_made_to_item_Rule_Meta_Data(
			Map<String, String> map) {

		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("itemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("SEOTAB"));

		seoTabPage.clickRetrieveRules();
		String actualAttributesFromMetaData = seoTabPage.getAttributesFromMetaData();

		String ExpectedAttributesFromMetaData = map.get("AttributeforMetaData");
		Assertions.assertThat(actualAttributesFromMetaData).isEqualTo(ExpectedAttributesFromMetaData);
		String actualDescriptionValueFromMetaData = seoTabPage.getDescriptionValueFromMetaData();

		// Getting expected value from different Tab
		String expectedOwnerDivision = seoTabPage.getDescriptionValueOwnerOfCatogory();
		// Navigating to Local Attribute Tab
		productDetailSearchPage.clickRefreshIcon().selectTabfromDropdown(map.get("TabName"));
		String expectedBrandValue = localAttributePage.getBrandValue();
		String expectedItemValue = localAttributePage.getItemValue();
		String expectedItemtypeValue = localAttributePage.getItemTypeValue();

		// Navigating to Gloabal attribute Tab
		productDetailSearchPage.clickRefreshIcon().selectTabfromDropdown(map.get("ContinueTabName"));

		String expectedManufacturePartNumber = globalAttributePage.getManufacturerPartNumber();

		// Appeding the values from Diifernt Tabs
		String expectedDescriptionValueFromMetaData = "Shop Henry Schein " + expectedOwnerDivision + " for "
				+ expectedBrandValue + expectedManufacturePartNumber + expectedItemtypeValue + expectedItemValue
				+ ". Browse our full selection of products and order online.";
		Assertions.assertThat(actualDescriptionValueFromMetaData).isEqualTo(expectedDescriptionValueFromMetaData);

		log(LogType.INFO, "Validating default rule in MetaData");

	}

	// SEO_PL_003
	@PimFrameworkAnnotation(module = Modules.SEO, category = CategoryType.SANITY)
	@TestDataSheet(sheetname = TestCaseSheet.SEO_Tab)
	@Test(description = "SEO_PL_003 | Validating default rule in Keywords", groups = {
			"US","pim","SANITY","SEO"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void Verify_default_Rules_in_SEO_populated_when_nochange_made_to_item_Rule_Keyword(Map<String, String> map) {

		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("itemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("SEOTAB"));

		seoTabPage.clickRetrieveRules();
		seoTabPage.AddingAttributeFromKeywords_MetaKeywords(map.get("AddingAttributeForKeywords"));
		String actualAttributesFromKeywords = seoTabPage.getAttributesFromKeywords();
		String ExpectedAttributesFromKeywords = map.get("AttributeforKeyword");
		Assertions.assertThat(actualAttributesFromKeywords).isEqualTo(ExpectedAttributesFromKeywords);
		String actualDescriptionValueFromKeywords = seoTabPage.getDescriptionValueFromKeywords();

		// Getting expected value from different Tab

		// Navigating to local Attribute Tab
		productDetailSearchPage.clickRefreshIcon().selectTabfromDropdown(map.get("TabName"));
		String expectedAllItemAttribute = localAttributePage.getAllitemAttributeExceptQuantity();

		// Navigating to Gloabal Attribute Tab
		productDetailSearchPage.clickRefreshIcon().selectTabfromDropdown(map.get("ContinueTabName"));

		String expectedManufacturePartNumber = globalAttributePage.getManufacturerPartNumber().trim();

		// Appending Values from differnt tabs
		String expectedDescriptionValueFromKeyword = expectedAllItemAttribute + " " + expectedManufacturePartNumber;

		Assertions.assertThat(actualDescriptionValueFromKeywords).isEqualTo(expectedDescriptionValueFromKeyword);

		log(LogType.INFO, "Validating default rule in Keywords");

	}

	// SEO_PL_004
	@PimFrameworkAnnotation(module = Modules.SEO, category = CategoryType.SANITY)
	@TestDataSheet(sheetname = TestCaseSheet.SEO_Tab)
	@Test(description = "SEO_PL_004 | Validating default rule in Description", groups = {
			"US","pim","SANITY","SEO"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void Verify_default_Rules_in_SEO_populated_when_nochange_made_to_item_Rule_Description(
			Map<String, String> map) {

		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("itemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("SEOTAB"));

		seoTabPage.clickRetrieveRules();
		String actualAttributesFromDescription = seoTabPage.getAttributesFromDescription();
		String ExpectedAttributesFromDescription = map.get("AttributeforDescription");
		Assertions.assertThat(actualAttributesFromDescription).isEqualTo(ExpectedAttributesFromDescription);
		String actualDescriptionValueFromDescription = seoTabPage.getDescriptionValueDescription();

		// Getting expected value from different Tab

		// Navigating to webDescription Tab
		productDetailSearchPage.clickRefreshIcon().selectTabfromDropdown(map.get("ContinueTabName"));

		String expectedFullDisplaydescription = webDescription.getFullDisplayDescription();
		String expectedCatalogDescription = webDescription.getStiboDescription();

		// Appended Values
		String expectedDescriptionValueFromDescription = expectedFullDisplaydescription + "- "
				+ expectedCatalogDescription;

		Assertions.assertThat(actualDescriptionValueFromDescription).isEqualTo(expectedDescriptionValueFromDescription);

		log(LogType.INFO, "Validating default rule in Desciption");

	}

	// SEO_PL_005
	@PimFrameworkAnnotation(module = Modules.SEO, category = CategoryType.SANITY)
	@TestDataSheet(sheetname = TestCaseSheet.SEO_Tab)
	@Test(description = "SEO_PL_005 | Validating default rule in H1Tag", groups = {
			"US","pim","SANITY","SEO"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void Verify_default_Rules_in_SEO_populated_when_nochange_made_to_item_Rule_H1Tag(Map<String, String> map) {

		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("itemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("SEOTAB"));

		seoTabPage.clickRetrieveRules();
		String actualAttributesFromH1Tag = seoTabPage.getAttributesFromH1Tag();
		String ExpectedAttributesFromH1Tag = map.get("AttributeforH1Tag");
		Assertions.assertThat(actualAttributesFromH1Tag).isEqualTo(ExpectedAttributesFromH1Tag);
		String actualDescriptionValueFromH1Tag = seoTabPage.getDescriptionValueH1Tag();

		// Getting expected value from different Tab

		// Navigating to local Attribute Tab
		productDetailSearchPage.clickRefreshIcon().selectTabfromDropdown(map.get("TabName"));
		String expectedBrandValue = localAttributePage.getBrandValue();
		String expectedItemValue = localAttributePage.getItemValue();
		String expectedItemtypeValue = localAttributePage.getItemTypeValue();

		// Navigating to Gloabal Attribute Tab
		productDetailSearchPage.clickRefreshIcon().selectTabfromDropdown(map.get("ContinueTabName"));

		String expectedManufacturePartNumber = globalAttributePage.getManufacturerPartNumber();

		String expectedDescriptionValueFromH1Tag = expectedBrandValue + expectedManufacturePartNumber
				+ expectedItemtypeValue + expectedItemValue;
		Assertions.assertThat(actualDescriptionValueFromH1Tag).isEqualTo(expectedDescriptionValueFromH1Tag);

		log(LogType.INFO, "Validating default rule in H1Tag");

	}

	// SEO_PL_006
	@PimFrameworkAnnotation(module = Modules.SEO, category = CategoryType.SANITY)
	@TestDataSheet(sheetname = TestCaseSheet.SEO_Tab)
	@Test(description = "SEO_PL_006 | Verifying the Dental Product, Owner Divison", groups = {
			"US","pim","SANITY","SEO"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void Verify_if_Owner_division_defaults_to_Dental_on_choosing_Dental_Major_Product(Map<String, String> map) {

		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("itemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("SEOTAB"));

		seoTabPage.clickRetrieveRules();
		String actualDescriptionValueOwnerOfCatogory = seoTabPage.getDescriptionValueOwnerOfCatogory();
		String ExpectedDescriptionValueOwnerOfCatogory = map.get("ExpectedAttributeValueforOwnerofCategory");
		Assertions.assertThat(actualDescriptionValueOwnerOfCatogory).isEqualTo(ExpectedDescriptionValueOwnerOfCatogory);

		log(LogType.INFO, "Validating Owner Division for DentalProduct");

	}

	// SEO_PL_007
	@PimFrameworkAnnotation(module = Modules.SEO, category = CategoryType.SANITY)
	@TestDataSheet(sheetname = TestCaseSheet.SEO_Tab)
	@Test(description = "SEO_PL_007 | Verifying the Medical Product, Owner Divison", groups = {
			"US","pim","SANITY","SEO"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void Verify_if_Owner_division_defaults_to_Medical_on_choosing_Medical_Major_Product(
			Map<String, String> map) {

		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("itemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("SEOTAB"));

		seoTabPage.clickRetrieveRules();
		String actualDescriptionValueOwnerOfCatogory = seoTabPage.getDescriptionValueOwnerOfCatogory();
		String ExpectedDescriptionValueOwnerOfCatogory = map.get("ExpectedAttributeValueforOwnerofCategory");
		Assertions.assertThat(actualDescriptionValueOwnerOfCatogory).isEqualTo(ExpectedDescriptionValueOwnerOfCatogory);

		log(LogType.INFO, "Validating Owner Division for MedicalProduct");

	}

	// SEO_PL_008
	@PimFrameworkAnnotation(module = Modules.SEO, category = CategoryType.SANITY)
	@TestDataSheet(sheetname = TestCaseSheet.SEO_Tab)
	@Test(description = "SEO_PL_008 | Verifying the Zahn Product, Owner Divison ", groups = {
			"US","pim","SANITY","SEO"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void Verify_if_Owner_division_defaults_to_Dental_on_choosing_Zahn_Major_Product(Map<String, String> map) {

		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("itemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("SEOTAB"));

		seoTabPage.clickRetrieveRules();
		String actualDescriptionValueOwnerOfCatogory = seoTabPage.getDescriptionValueOwnerOfCatogory();
		String ExpectedDescriptionValueOwnerOfCatogory = map.get("ExpectedAttributeValueforOwnerofCategory");
		Assertions.assertThat(actualDescriptionValueOwnerOfCatogory).isEqualTo(ExpectedDescriptionValueOwnerOfCatogory);

		log(LogType.INFO, "Validating Owner Division for ZahnProduct");

	}

	// SEO_PL_009
	@PimFrameworkAnnotation(module = Modules.SEO, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.SEO_Tab)
	@Test(description = "SEO_PL_009 | Verifying The Default Rule overriden and FreeText for PageTitle ", groups = {
			"US","pim","SANITY","SEO"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void Verify_if_User_can_Override_the_default_rule_for_Page_Title_Attribute(Map<String, String> map) {

		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("itemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("SEOTAB"));

		seoTabPage.clickRetrieveRules();
		// Overiding the default Rule for Pagetitle
		seoTabPage.deletingValueFromPageTitle(map.get("DeletingAttributeForPageTitle"));
		String DefaultRuleFromPageTitle = seoTabPage.getAttributesFromPageTitle();
		seoTabPage.addingAttributeFromPageTitleFieldAttribute(map.get("AddingAttributeForPageTitle"));
		String OverideDefaultRuleFromPageTitle = seoTabPage.getAttributesFromPageTitle();
		Assertions.assertThat(DefaultRuleFromPageTitle).isNotEqualTo(OverideDefaultRuleFromPageTitle);
		Assertions.assertThat(OverideDefaultRuleFromPageTitle).isEqualTo(map.get("AttributeforPageTitile"));
		String DescriptionValueFromPageTitle = seoTabPage.getDescriptionValueFromPageTitle();
		Assertions.assertThat(DescriptionValueFromPageTitle).contains(map.get("AddingAttributeForPageTitle"));

		log(LogType.INFO, "Validating Free Text Value is getting added to Description field in Page Title");

	}

	// SEO_PL_010
	@PimFrameworkAnnotation(module = Modules.SEO, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.SEO_Tab)
	@Test(description = "SEO_PL_010 | Verifying The Default Rule overriden and FreeText For Meta Data ", groups = {
			"US","pim","SANITY","SEO"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void Verify_if_User_can_Override_the_default_rule_for_Meta_Data(Map<String, String> map) {

		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("itemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("SEOTAB"));
		seoTabPage.clickRetrieveRules();
		// Overiding the default Rule for MetaData
		seoTabPage.deletingValueFromMetaData(map.get("DeletingAttributeFromMetaData"));
		String DefaultRuleFromMetaData = seoTabPage.getAttributesFromMetaData();
		seoTabPage.AddingAttributeFromMetaData_MetaDescription(map.get("AddingAttributeForMetaData"));
		String OverideDefaultRuleFromMetaData = seoTabPage.getAttributesFromMetaData();
		Assertions.assertThat(DefaultRuleFromMetaData).isNotEqualTo(OverideDefaultRuleFromMetaData);
		Assertions.assertThat(OverideDefaultRuleFromMetaData).isEqualTo(map.get("AttributeforMetaData"));
		String DescriptionValueFromMetaData = seoTabPage.getDescriptionValueFromMetaData();
		Assertions.assertThat(DescriptionValueFromMetaData).contains(map.get("AddingAttributeForMetaData"));
		log(LogType.INFO, "Validating Free Text Value is getting added to Description field in Meta Data");

	}

	// SEO_PL_011
	@PimFrameworkAnnotation(module = Modules.SEO, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.SEO_Tab)
	@Test(description = "SEO_PL_011 | Verifying The Default Rule overriden and FreeText for Keyword ", groups = {
			"US","pim","SANITY","SEO"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void Verify_if_User_can_Override_the_default_rule_for_Keywords(Map<String, String> map) {

		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("itemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("SEOTAB"));

		seoTabPage.clickRetrieveRules();

		// Overiding the default Rule for KeyWords
		seoTabPage.deletingValueFromKeywords(map.get("DeletingAttributeForKeywords"));
		String DefaultRuleFromKeywords = seoTabPage.getAttributesFromKeywords();
		seoTabPage.AddingAttributeFromKeywords_MetaKeywords(map.get("AddingAttributeForKeywords"));
		String OverideDefaultRuleFromKeywords = seoTabPage.getAttributesFromKeywords();
		Assertions.assertThat(DefaultRuleFromKeywords).isNotEqualTo(OverideDefaultRuleFromKeywords);
		Assertions.assertThat(OverideDefaultRuleFromKeywords).isEqualTo(map.get("AttributeforKeyword"));
		String DescriptionValueFromKeywords = seoTabPage.getDescriptionValueFromKeywords();
		Assertions.assertThat(DescriptionValueFromKeywords).contains(map.get("AddingAttributeForKeywords"));

		log(LogType.INFO, "Validating Free Text Value is getting added to Description field in Keywords");

	}

	// SEO_PL_012
	@PimFrameworkAnnotation(module = Modules.SEO, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.SEO_Tab)
	@Test(description = "SEO_PL_012 | Verifying The Default Rule overriden and FreeText for Description ", groups = {
			"US","pim","SANITY" ,"SEO"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void Verify_if_User_can_Override_the_default_rule_for_Description(Map<String, String> map) {

		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("itemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("SEOTAB"));

		seoTabPage.clickRetrieveRules();
		// Overiding the default Rule for Description
		seoTabPage.deletingValueFromDescription(map.get("DeletingAttributeForDescription"));
		String DefaultRuleFromDescription = seoTabPage.getAttributesFromDescription();
		seoTabPage
				.addingAttributeFromDescription_ProductorCategorydescription(map.get("AddingAttributeForDescription"));
		String OverideDefaultRuleFromDescription = seoTabPage.getAttributesFromDescription();
		Assertions.assertThat(DefaultRuleFromDescription).isNotEqualTo(OverideDefaultRuleFromDescription);
		Assertions.assertThat(OverideDefaultRuleFromDescription).isEqualTo(map.get("AttributeforDescription"));
		String DescriptionValueFromDescription = seoTabPage.getDescriptionValueDescription();
		Assertions.assertThat(DescriptionValueFromDescription).contains(map.get("AddingAttributeForDescription"));

		log(LogType.INFO, "Validating Free Text Value is getting added to Description field in Description");

	}

	// SEO_PL_013
	@PimFrameworkAnnotation(module = Modules.SEO, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.SEO_Tab)
	@Test(description = "SEO_PL_013 | Verifying The Default Rule overriden and FreeText for H1Tag ", groups = {
			"US","pim","SANITY","SEO"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void Verify_if_User_can_Override_the_default_rule_for_H1Tag(Map<String, String> map) {

		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("itemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("SEOTAB"));

		seoTabPage.clickRetrieveRules();

		// Overiding the default Rule for H1Tag
		seoTabPage.deletingValueFromH1Tag(map.get("DeletingAttributeforH1Tag"));
		String DefaultRuleFromH1Tag = seoTabPage.getAttributesFromH1Tag();
		seoTabPage.AddingAttributeFromH1_Tag(map.get("AddingAttributeforH1Tag"));
		String OverideDefaultRuleFromH1Tag = seoTabPage.getAttributesFromH1Tag();
		Assertions.assertThat(DefaultRuleFromH1Tag).isNotEqualTo(OverideDefaultRuleFromH1Tag);
		Assertions.assertThat(OverideDefaultRuleFromH1Tag).isEqualTo(map.get("AttributeforH1Tag"));
		String DescriptionValueFromH1Tag = seoTabPage.getDescriptionValueH1Tag();
		Assertions.assertThat(DescriptionValueFromH1Tag).contains(map.get("AddingAttributeforH1Tag"));

		log(LogType.INFO, "Validating Free Text Value is getting added to Description field in H1Tag");

	}

	// SEO_PL_014 --failing becuse freeText is not availble
	@PimFrameworkAnnotation(module = Modules.SEO, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.SEO_Tab)
	@Test(description = "SEO_PL_014 | Verifying The Default Rule not overriden of FreeText for OwnerOfCatagory SEO_PL_014 ", groups = {
			"US","pim","SANITY","SEO" }, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void Verify_if_User_can_not_Override_the_default_rule_for_OwnerOfCategory(Map<String, String> map) {

		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("itemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("SEOTAB"));
		seoTabPage.clickRetrieveRules();
		// checking in OwnerOfCategory
		seoTabPage.dropDownLovFromOwnerOfCategory(map.get("AttributeforOwnerofCategory"));
		Assertions.assertThat(seoTabPage.isDropDownValueVisible("freeTextField")).isFalse();

		String ActualDescriptionValueFromOwnerOfCatagory = seoTabPage.getDescriptionValueOwnerOfCatogory();

		String ExpectedDescriptionValueFromOwnerOfCatogey = map.get("ExpectedAttributeValueforOwnerofCategory");

		Assertions.assertThat(ActualDescriptionValueFromOwnerOfCatagory).contains(ExpectedDescriptionValueFromOwnerOfCatogey);

		log(LogType.INFO, "Validating Free Text Value is getting added to Description field in OwnerOfCatogory");

	}

	// SEO_PL_015, SEO_PL_039
	@PimFrameworkAnnotation(module = Modules.SEO, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.SEO_Tab)
	@Test(description = "SEO_PL_015, SEO_PL_039 | Verify SEO Tabs after chaanging the values in local Attribute Tab and some of the values are null means no content", groups = {
			"US","pim","SEO"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void Verify_Local_Attribute_Fields_Are_getting_Populated_SEO_item_and_ItemType(Map<String, String> map) {

		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("itemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));

		localAttributePage.brandFieldAttribute(map.get("LocalAttributeBrandFieldValue"))
				.colorFieldAttribute(map.get("LocalAttributeColourFieldValue"))
				.addingMandatoryQuantityFieldAttribute(map.get("LocalAttributeQuantityFieldValue"))
				.itemTypeFieldAttribute(map.get("LocalAttributeitemTypeFieldValue"))
				.MandatoryItemFieldAttribute(map.get("LocalAttributeItemFieldValue"));

		String expectedBrandValue = localAttributePage.getBrandValue().trim();
		String expectedItemValue = localAttributePage.getItemValue().trim();
		String expectedItemtypeValue = localAttributePage.getItemTypeValue().trim();
		String expectedColorValue = localAttributePage.getColorValue().trim();

		// Navigating to Global Attribute Tab
		productDetailSearchPage.clickRefreshIcon().selectTabfromDropdown(map.get("ContinueTabName"));
		String ExpectedManufacturePartNumber = globalAttributePage.getManufacturerPartNumberWithoutSpace();
		// Navigating to SEO Tab
		productDetailSearchPage.clickRefreshIcon().selectTabfromDropdown(map.get("SEOTAB"));

		// Validating in PageTitle
		seoTabPage.clickRetrieveRules()
				.addingAttributeFromPageTitleFieldAttribute(map.get("AddingAttributeForPageTitle"));
		String actualValueForPageTitleDescription = seoTabPage.getDescriptionValueFromPageTitle();
		// containsAnyOf(ExpectedManufacturePartNumber);
		Assertions.assertThat(actualValueForPageTitleDescription).contains(ExpectedManufacturePartNumber);
		Assertions.assertThat(actualValueForPageTitleDescription).contains(expectedBrandValue);
		Assertions.assertThat(actualValueForPageTitleDescription).contains(expectedColorValue);
		Assertions.assertThat(actualValueForPageTitleDescription).contains(expectedItemValue);
		Assertions.assertThat(actualValueForPageTitleDescription).contains(expectedItemtypeValue);

		log(LogType.INFO,
				"Changed the values in local attribute and navigete to SEO Tab validating the values are appended in PageTitleDescription");

		// Validating in Meta Data
		seoTabPage.AddingAttributeFromMetaData_MetaDescription(map.get("AddingAttributeForMetaData"));

		String actualValueForMetaDataDescription = seoTabPage.getDescriptionValueFromMetaData();
		Assertions.assertThat(actualValueForMetaDataDescription).contains(ExpectedManufacturePartNumber);
		Assertions.assertThat(actualValueForMetaDataDescription).contains(expectedBrandValue);
		Assertions.assertThat(actualValueForMetaDataDescription).contains(expectedColorValue);
		Assertions.assertThat(actualValueForMetaDataDescription).contains(expectedItemValue);
		Assertions.assertThat(actualValueForMetaDataDescription).contains(expectedItemtypeValue);

		log(LogType.INFO,
				"Changed the values in local attribute and navigete to SEO Tab validating the values are appended in MetaDataDescription");

		// Validating in Keywords
		seoTabPage.AddingAttributeFromKeywords_MetaKeywords(map.get("AddingAttributeForKeywords"));

		String actualValueForKeywordsDescription = seoTabPage.getDescriptionValueFromKeywords();

		Assertions.assertThat(actualValueForKeywordsDescription).contains(ExpectedManufacturePartNumber);
		Assertions.assertThat(actualValueForKeywordsDescription).contains(expectedColorValue);

		log(LogType.INFO,
				"Changed the values in local attribute and navigete to SEO Tab validating the values are appended in KeywordDescription");

		// Validating in Description
		seoTabPage
				.addingAttributeFromDescription_ProductorCategorydescription(map.get("AddingAttributeForDescription"));

		String actualValueForDescriptionDescription = seoTabPage.getDescriptionValueDescription();

		Assertions.assertThat(actualValueForDescriptionDescription).contains(expectedBrandValue);
		Assertions.assertThat(actualValueForDescriptionDescription).contains(expectedColorValue);
		Assertions.assertThat(actualValueForDescriptionDescription).contains(expectedItemtypeValue);

		log(LogType.INFO,
				"Changed the values in local attribute and navigete to SEO Tab validating the values are appended in DescriptionDescription");

		// Validating in H1 Tag
		seoTabPage.AddingAttributeFromH1_Tag(map.get("AddingAttributeforH1Tag"));

		String actualValueForH1TagDescription = seoTabPage.getDescriptionValueH1Tag();
		Assertions.assertThat(actualValueForH1TagDescription).contains(expectedBrandValue);
		Assertions.assertThat(actualValueForH1TagDescription).contains(expectedItemtypeValue);
		Assertions.assertThat(actualValueForH1TagDescription).contains(ExpectedManufacturePartNumber);
		log(LogType.INFO,
				"Changed the values in local attribute and navigete to SEO Tab validating the values are appended in H1TagDescription");

		// Validating in owner Of Category
		seoTabPage.addingAttributeFromOwnerofCategory(map.get("AttributeforOwnerofCategory"));
		String actualValueForOwnerOfCatagoryDescription = seoTabPage.getDescriptionValueOwnerOfCatogory();
		Assertions.assertThat(actualValueForOwnerOfCatagoryDescription)
				.contains(map.get("ExpectedAttributeValueforOwnerofCategory"));

		log(LogType.INFO,
				"Changed the values in local attribute and navigete to SEO Tab validating the values are appended in OwnerofCategoryDescription");

	}

	// SEO_PL_016
	@PimFrameworkAnnotation(module = Modules.SEO, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.SEO_Tab)
	@Test(description = "SEO_PL_016 | Verify other products that doesn�t have Overridden Rules_but similar product has overridden rules ", groups = {
			"US","pim","SEO"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void Verify_other_Products_that_doesnt_have_Overridden_Rules_but_similar_product_has_overridden_rules(
			Map<String, String> map) {

		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("itemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("SEOTAB"));

		seoTabPage.clickRetrieveRules().deletingValueFromPageTitle(map.get("DeletingAttributeForPageTitle"));
		seoTabPage.addingAttributeFromPageTitleFieldAttribute(map.get("AddingAttributeForPageTitle"));
		String overRidingTheDefualtRuleFromOneProduct = seoTabPage.getAttributesFromPageTitle();
		String DescriptionValueFromOneProduct = seoTabPage.getDescriptionValueFromPageTitle();
		Assertions.assertThat(DescriptionValueFromOneProduct).contains(map.get("AddingAttributeForPageTitle"));

		// Navigating the Item in US Catalog for different product
		log(LogType.INFO, "Navigating to SEO Tab for another Product");
		pimHomepage.mainMenu().clickRefreshMenuIcon();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("Different_item_Number"))
				.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("ContinueTabName"))
				.selectTabfromDropdown(map.get("SEOTAB"));

		// after navigating the item getting Attribute from SEO Tab
		seoTabPage.clickRetrieveRules();
		String expectedAttributeFromPageTitleFromDifferentProduct = seoTabPage.getAttributesFromPageTitle();
		String ExpectedDescriptionFromPageTitleFromDifferentProduct = seoTabPage.getDescriptionValueFromPageTitle();

		Assertions.assertThat(expectedAttributeFromPageTitleFromDifferentProduct)
				.isNotEqualTo(overRidingTheDefualtRuleFromOneProduct);
		Assertions.assertThat(expectedAttributeFromPageTitleFromDifferentProduct)
				.isEqualTo(map.get("AttributeforPageTitile"));
		Assertions.assertThat(ExpectedDescriptionFromPageTitleFromDifferentProduct)
				.doesNotContain(map.get("AddingAttributeForPageTitle"));

		log(LogType.INFO,
				"Verify other products that doesn�t have Overridden Rules_but similar product has overridden rules");

	}

	// SEO_PL_017
	@PimFrameworkAnnotation(module = Modules.SEO, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.SEO_Tab)
	@Test(description = "SEO_PL_017 | Verify SEO file for local attribute Left blank", groups = {
			"US","pim","SEO"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void Verify_Local_Attribute_Fields_Are_getting_Populated_SEO_item_and_ItemType_Blank_Value_In_local_Attribute(
			Map<String, String> map) {

		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("itemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));

		localAttributePage.brandFieldAttribute(map.get("LocalAttributeBrandFieldValue"))
				.colorFieldAttribute(map.get("LocalAttributeColourFieldValue"))
				.addingMandatoryQuantityFieldAttribute(map.get("LocalAttributeQuantityFieldValue"))
				.itemTypeFieldAttribute(map.get("LocalAttributeitemTypeFieldValue"))
				.MandatoryItemFieldAttribute(map.get("LocalAttributeItemFieldValue"))
				.primaryLwhFieldAttribute(map.get("LocalAttributeLWHFieldValue"));

		String expectedBrandValue = localAttributePage.getBrandValue();
		String expectedItemValue = localAttributePage.getItemValue();
		String expectedItemtypeValue = localAttributePage.getItemTypeValue();
		String expectedColorValue = localAttributePage.getColorValue();

		productDetailSearchPage.clickRefreshIcon().selectTabfromDropdown(map.get("ContinueTabName"));

		String ExpectedManufacturePartNumber = globalAttributePage.getManufacturerPartNumber().trim();

		productDetailSearchPage.clickRefreshIcon().selectTabfromDropdown(map.get("SEOTAB"));

		seoTabPage.clickRetrieveRules()
				.addingAttributeFromPageTitleFieldAttribute(map.get("AddingAttributeForPageTitle"));

		String actualValueForPageTitleDescription = seoTabPage.getDescriptionValueFromPageTitle();

		Assertions.assertThat(actualValueForPageTitleDescription).contains(ExpectedManufacturePartNumber);
		Assertions.assertThat(actualValueForPageTitleDescription).contains(expectedBrandValue);
		Assertions.assertThat(actualValueForPageTitleDescription).contains(expectedColorValue);
		Assertions.assertThat(actualValueForPageTitleDescription).contains(expectedItemValue);
		Assertions.assertThat(actualValueForPageTitleDescription).contains(expectedItemtypeValue);

		log(LogType.INFO,
				"Changed the values in local attribute and navigete to SEO Tab validating the values are appended in PageTitleDescription");

		seoTabPage.addingAttributeFromPageTitleFieldAttribute(map.get("AddingAttributeForMetaData"));
		String actualValueForMetaDataDescription = seoTabPage.getDescriptionValueFromMetaData();

		Assertions.assertThat(actualValueForMetaDataDescription).contains(ExpectedManufacturePartNumber);
		Assertions.assertThat(actualValueForMetaDataDescription).contains(expectedBrandValue);
		Assertions.assertThat(actualValueForMetaDataDescription).contains(expectedColorValue);
		Assertions.assertThat(actualValueForMetaDataDescription).contains(expectedItemValue);
		Assertions.assertThat(actualValueForMetaDataDescription).contains(expectedItemtypeValue);

		log(LogType.INFO,
				"Changed the values in local attribute and navigete to SEO Tab validating the values are appended in MetaDataDescription");

		seoTabPage.AddingAttributeFromKeywords_MetaKeywords(map.get("AddingAttributeForKeywords"));
		String actualValueForKeywordsDescription = seoTabPage.getDescriptionValueFromKeywords();

		Assertions.assertThat(actualValueForKeywordsDescription).contains(ExpectedManufacturePartNumber);
		Assertions.assertThat(actualValueForKeywordsDescription).contains(expectedColorValue);

		log(LogType.INFO,
				"Changed the values in local attribute and navigete to SEO Tab validating the values are appended in KeywordDescription");

		seoTabPage
				.addingAttributeFromDescription_ProductorCategorydescription(map.get("AddingAttributeForDescription"));
		String actualValueForDescriptionDescription = seoTabPage.getDescriptionValueDescription();

		Assertions.assertThat(actualValueForDescriptionDescription).contains(expectedBrandValue);
		Assertions.assertThat(actualValueForDescriptionDescription).contains(expectedColorValue);
		Assertions.assertThat(actualValueForDescriptionDescription).contains(expectedItemtypeValue);

		log(LogType.INFO,
				"Changed the values in local attribute and navigete to SEO Tab validating the values are appended in DescriptionDescription");

		seoTabPage.AddingAttributeFromH1_Tag(map.get("AddingAttributeforH1Tag"));
		String actualValueForH1TagDescription = seoTabPage.getDescriptionValueH1Tag();
		Assertions.assertThat(actualValueForH1TagDescription).contains(expectedBrandValue);
		Assertions.assertThat(actualValueForH1TagDescription).contains(expectedItemtypeValue);
		Assertions.assertThat(actualValueForH1TagDescription).contains(ExpectedManufacturePartNumber);
		log(LogType.INFO,
				"Changed the values in local attribute and navigete to SEO Tab validating the values are appended in H1TagDescription");

		seoTabPage.addingAttributeFromOwnerofCategory(map.get("AttributeforOwnerofCategory"));
		String actualValueForOwnerOfCatagoryDescription = seoTabPage.getDescriptionValueOwnerOfCatogory();
		Assertions.assertThat(actualValueForOwnerOfCatagoryDescription)
				.contains(map.get("ExpectedAttributeValueforOwnerofCategory"));

		log(LogType.INFO,
				"Changed the values in local attribute and navigete to SEO Tab validating the values are appended in OwnerofCategoryDescription");

	}

	// SEO_PL_018
	@PimFrameworkAnnotation(module = Modules.SEO, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.SEO_Tab)
	@Test(description = "SEO_PL_018 | Verify whether existing rule gets retrieved on saving  Page Title field with blank rule ", groups = {
			"US","pim","SEO"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void Verify_whether_existing_rule_gets_retrieved_on_saving_Page_Title_field_with_blank_rule(
			Map<String, String> map) {

		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("itemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("SEOTAB"));
		seoTabPage.clickRetrieveRules();
		String actualAttributeFromPageTitle = seoTabPage.getAttributesFromPageTitle();
		seoTabPage.dragAndDropAttributesFromPageTitle();

		// Navigating the Item in US Catalog
		pimHomepage.mainMenu().clickRefreshMenuIcon();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("itemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));

		productDetailSearchPage.selectTabfromDropdown(map.get("SEOTAB"));

		// after navigating the item getting Attribute from SEO Tab
		seoTabPage.clickRetrieveRules();
		String expectedAttributeFromPageTitle = seoTabPage.getAttributesFromPageTitle();

		Assertions.assertThat(actualAttributeFromPageTitle).isEqualTo(expectedAttributeFromPageTitle);

		log(LogType.INFO, "Validating existing rule gets retrieved on saving  Page Title field with blank rule");

	}

	// SEO_PL_019
	@PimFrameworkAnnotation(module = Modules.SEO, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.SEO_Tab)
	@Test(description = "SEO_PL_019 | Verify whether existing rule gets retrieved on saving  MetaData field with blank rule ", groups = {
			"US","pim","SEO"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void Verify_whether_existing_rule_gets_retrieved_on_saving_MetaData_field_with_blank_rule(
			Map<String, String> map) {

		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("itemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("SEOTAB"));
		seoTabPage.clickRetrieveRules();
		String actualAttributeFromMetaData = seoTabPage.getAttributesFromMetaData();
		seoTabPage.dragAndDropAttributesFromMetaData();

		// Navigating the Item in US Catalog
		pimHomepage.mainMenu().clickRefreshMenuIcon();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("itemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));

		productDetailSearchPage.selectTabfromDropdown(map.get("SEOTAB"));

		// after navigating the item getting Attribute from SEO Tab
		seoTabPage.clickRetrieveRules();
		String expectedAttributeFromMetaData = seoTabPage.getAttributesFromMetaData();

		Assertions.assertThat(actualAttributeFromMetaData).isEqualTo(expectedAttributeFromMetaData);

		log(LogType.INFO, "Validating existing rule gets retrieved on saving  Meta Data field with blank rule");

	}

	// SEO_PL_020
	@PimFrameworkAnnotation(module = Modules.SEO, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.SEO_Tab)
	@Test(description = "SEO_PL_020 | Verify whether existing rule gets retrieved on saving  Keywords field with blank rule ", groups = {
			"US","pim","SEO"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void Verify_whether_existing_rule_gets_retrieved_on_saving_Keywords_field_with_blank_rule(
			Map<String, String> map) {

		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("itemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("SEOTAB"));
		seoTabPage.clickRetrieveRules();
		String actualAttributeFromKeywords = seoTabPage.getAttributesFromKeywords();
		seoTabPage.dragAndDropAttributesFromKeywords();

		// Navigating the Item in US Catalog
		pimHomepage.mainMenu().clickRefreshMenuIcon();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("itemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));

		productDetailSearchPage.selectTabfromDropdown(map.get("SEOTAB"));

		// after navigating the item getting Attribute from SEO Tab
		seoTabPage.clickRetrieveRules();
		String expectedAttributeFromKeywords = seoTabPage.getAttributesFromKeywords();

		Assertions.assertThat(actualAttributeFromKeywords).isEqualTo(expectedAttributeFromKeywords);

		log(LogType.INFO, "Validating existing rule gets retrieved on saving  Keywords field with blank rule");

	}

	// SEO_PL_021
	@PimFrameworkAnnotation(module = Modules.SEO, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.SEO_Tab)
	@Test(description = "SEO_PL_021 | Verify whether existing rule gets retrieved on saving  Description field with blank rule ", groups = {
			"US","pim","SEO"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void Verify_whether_existing_rule_gets_retrieved_on_saving_Description_field_with_blank_rule(
			Map<String, String> map) {

		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("itemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("SEOTAB"));
		seoTabPage.clickRetrieveRules();
		String actualAttributeFromDescription = seoTabPage.getAttributesFromDescription();
		seoTabPage.dragAndDropAttributesFromDescription();

		// Navigating the Item in US Catalog
		pimHomepage.mainMenu().clickRefreshMenuIcon();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("itemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));

		productDetailSearchPage.selectTabfromDropdown(map.get("SEOTAB"));

		// after navigating the item getting Attribute from SEO Tab
		seoTabPage.clickRetrieveRules();
		String expectedAttributeFromDescription = seoTabPage.getAttributesFromDescription();

		Assertions.assertThat(actualAttributeFromDescription).isEqualTo(expectedAttributeFromDescription);

		log(LogType.INFO, "Validating existing rule gets retrieved on saving  Description field with blank rule");

	}

	// SEO_PL_022
	@PimFrameworkAnnotation(module = Modules.SEO, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.SEO_Tab)
	@Test(description = "SEO_PL_022 | Verify whether existing rule gets retrieved on saving  H1Tag field with blank rule ", groups = {
			"US","pim","SEO"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void Verify_whether_existing_rule_gets_retrieved_on_saving_H1Tag_field_with_blank_rule(
			Map<String, String> map) {

		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("itemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("SEOTAB"));
		seoTabPage.clickRetrieveRules();
		String actualAttributeFromH1Tag = seoTabPage.getAttributesFromH1Tag();
		seoTabPage.dragAndDropAttributesFromH1Tag();

		// Navigating the Item in US Catalog
		pimHomepage.mainMenu().clickRefreshMenuIcon();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("itemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));

		productDetailSearchPage.selectTabfromDropdown(map.get("SEOTAB"));

		// after navigating the item getting Attribute from SEO Tab
		seoTabPage.clickRetrieveRules();
		String expectedAttributeFromH1Tag = seoTabPage.getAttributesFromH1Tag();

		Assertions.assertThat(actualAttributeFromH1Tag).isEqualTo(expectedAttributeFromH1Tag);

		log(LogType.INFO, "Validating existing rule gets retrieved on saving  H1Tag field with blank rule");

	}

	// SEO_PL_023
	@PimFrameworkAnnotation(module = Modules.SEO, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.SEO_Tab)
	@Test(description = "SEO_PL_023 | Verify other products that doesnt have Overridden Rules_but similar product has overridden rules ", groups = {
			"US","pim","SEO"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void Verify_whether_existing_rule_gets_retrieved_on_saving_OwnerOfCategory_field_with_blank_rule(
			Map<String, String> map) {

		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("itemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("SEOTAB"));
		seoTabPage.clickRetrieveRules();
		String actualAttributeFromOwnerOfCategory = seoTabPage.getAttributesFromOwnerOfCatogory();
		seoTabPage.dragAndDropAttributesFromOwnerOfCategory();

		// Navigating the Item in US Catalog
		pimHomepage.mainMenu().clickRefreshMenuIcon();
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("itemNumber"))
				.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));

		productDetailSearchPage.selectTabfromDropdown(map.get("SEOTAB"));

		// after navigating the item getting Attribute from SEO Tab
		seoTabPage.clickRetrieveRules();
		String expectedAttributeFromOwnerOfCategory = seoTabPage.getAttributesFromOwnerOfCatogory();

		Assertions.assertThat(actualAttributeFromOwnerOfCategory).isEqualTo(expectedAttributeFromOwnerOfCategory);

		log(LogType.INFO, "Validating existing rule gets retrieved on saving  OwnerOfCategory field with blank rule");

	}

	// SEO_PL_024 ---Failed
	@PimFrameworkAnnotation(module = Modules.SEO, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.SEO_Tab)
	@Test(description = "SEO_PL_024 | Description formed out of invalid LOV should not merge to master catalog rules ", groups = {
			"US","pim","SEO"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void Verify_whether_mandatory_local_attribute_present_as_part_of_SEO_export_field_rule_with_invalid_value_does_not_get_merged_to_master_catalog_when_DQ_rule_fails(
			Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("itemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));

		qualityStatusPage.maximizeQualityStatusTab();
		String ItemTypeValue = localAttributePage.getItemTypeValue();
		
		localAttributePage.itemFieldInavlidValue(map.get("LocalAttributeBrandOrItemFieldValue")).ItemTypeAttribute(map.get("LocalAttributeitemTypeFieldValue"), ItemTypeValue)
				.selectValuefromSaveDropdown(map.get("saveButton"));

		String ActualValueFromitem = localAttributePage.getItemValue().trim();
		qualityStatusPage.minimizeQualityStatusTab();
		// Navigation to Quality Status Tab

		productDetailSearchPage.clickRefreshIcon();
		productDetailSearchPage.selectTabfromDropdown(map.get("ContinueTabName"));
		productDetailSearchPage.clickRefreshIcon();
		List<String> localmandatoryrule = qualityStatusPage.maximizeQualityStatusTab().sortRulestByLastExecution()
				.getRuleNamesByRuleStatus(map.get("QualityTab_StatusColoum"));
		qualityStatusPage.minimizeQualityStatusTab();

		List<String> myList = new Javautils().readMultipleValuesFromExcel(map.get("LocalAttributeRule"));
		Assertions.assertThat(Javautils.compareList(myList, localmandatoryrule)).isEqualTo(true);

		// Navigating SEO Tab

		productDetailSearchPage.selectTabfromDropdown(map.get("SEOTAB"));
		seoTabPage.clickRetrieveRules();

		seoTabPage.addingAttributeFromPageTitleFieldAttribute(map.get("AddingAttributeForPageTitle"));
		String ExpectedDescriptionValueFromPagetitle = seoTabPage.getDescriptionValueFromPageTitle();

		Assertions.assertThat(ExpectedDescriptionValueFromPagetitle).contains(ActualValueFromitem);

		String ExpectedAttributeFromPagetitle = seoTabPage.getAttributesFromPageTitle();

		seoTabPage.AddingAttributeFromMetaData_MetaDescription(map.get("AddingAttributeForMetaData"));
		String ExpectedDescriptionValueFromMetaData = seoTabPage.getDescriptionValueFromMetaData();

		Assertions.assertThat(ExpectedDescriptionValueFromMetaData).contains(ActualValueFromitem);

		String ExpectedAttributeFromMetaData = seoTabPage.getAttributesFromMetaData();

		seoTabPage.AddingAttributeFromKeywords_MetaKeywords(map.get("AddingAttributeForKeywords"));

		String ExpectedDescriptionValueFromKeywords = seoTabPage.getDescriptionValueFromKeywords();

		Assertions.assertThat(ExpectedDescriptionValueFromKeywords).contains(ActualValueFromitem);

		String ExpectedAttributeFromKeyword = seoTabPage.getAttributesFromKeywords();

		seoTabPage
				.addingAttributeFromDescription_ProductorCategorydescription(map.get("AddingAttributeForDescription"));
		String ExpectedDescriptionValueFromDescription = seoTabPage.getDescriptionValueDescription();

		Assertions.assertThat(ExpectedDescriptionValueFromDescription).contains(ActualValueFromitem);

		String ExpectedAttributeFromDescription = seoTabPage.getAttributesFromDescription();

		seoTabPage.AddingAttributeFromH1_Tag(map.get("AddingAttributeforH1Tag"));

		String ExpectedDescriptionValueFromH1Tag = seoTabPage.getDescriptionValueH1Tag();

		Assertions.assertThat(ExpectedDescriptionValueFromH1Tag).contains(ActualValueFromitem);

		String ExpectedAttributeFromH1Tag = seoTabPage.getAttributesFromH1Tag();

		String ExpectedAttributeFromOwnerOfCategory = seoTabPage.getAttributesFromOwnerOfCatogory();

		String ExpecteddescriptionValueFromOwnerOfCategory = seoTabPage.getDescriptionValueOwnerOfCatogory();

		Assertions.assertThat(ExpecteddescriptionValueFromOwnerOfCategory)
				.contains(map.get("ExpectedAttributeValueforOwnerofCategory"));

		// Navigating The same item Number in Master and validating SEO Tab
		pimHomepage.mainMenu().clickRefreshMenuIcon();
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogTypeMaster"))
		.enterHsiItemNumber(map.get("itemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName"))
				.selectTabfromDropdown(map.get("SEOTAB"));
        seoTabPage.clickRetrieveRules();

		// SEO PageTitle in master
		String ActualAttributeFromPageTitle = seoTabPage.getAttributesFromPageTitle();

		//Assertions.assertThat(ActualAttributeFromPageTitle).isEqualTo(ExpectedAttributeFromPagetitle);

		String MasterDesciptionValuefromPagetitle = seoTabPage.getDescriptionValueFromPageTitle();

		Assertions.assertThat(MasterDesciptionValuefromPagetitle).doesNotContain(ActualValueFromitem);

		// Seo MetaData in master

		String ActualAttributeFromMetaData = seoTabPage.getAttributesFromMetaData();

		//Assertions.assertThat(ActualAttributeFromMetaData).isEqualTo(ExpectedAttributeFromMetaData);

		String MasterDesciptionValuefromMetaData = seoTabPage.getDescriptionValueFromMetaData();
		Assertions.assertThat(MasterDesciptionValuefromMetaData).doesNotContain(ActualValueFromitem);

		// SEO Keywords in master

		String ActualAttributeFromKeywords = seoTabPage.getAttributesFromKeywords();

		//Assertions.assertThat(ActualAttributeFromKeywords).isEqualTo(ExpectedAttributeFromKeyword);

		String MasterDesciptionValuefromKeyword = seoTabPage.getDescriptionValueFromKeywords();
		Assertions.assertThat(MasterDesciptionValuefromKeyword).doesNotContain(ActualValueFromitem);

		// SEO Description in Master
		String ActualAttributeFromDescription = seoTabPage.getAttributesFromDescription();

		String MasterDesciptionValuefromDescription = seoTabPage.getDescriptionValueDescription();
		Assertions.assertThat(MasterDesciptionValuefromDescription).doesNotContain(ActualValueFromitem);

		// SEO H1Tag in Master
		String ActualAttributeFromH1Tag = seoTabPage.getAttributesFromH1Tag();


		String MasterDesciptionValuefromH1Tag = seoTabPage.getDescriptionValueH1Tag();
		Assertions.assertThat(MasterDesciptionValuefromH1Tag).doesNotContain(ActualValueFromitem);

		// SEO OwnerOfCategory in Master
		String ActualAttributeFromOwnerOfCategory = seoTabPage.getAttributesFromOwnerOfCatogory();

		Assertions.assertThat(ActualAttributeFromOwnerOfCategory).isEqualTo(ExpectedAttributeFromOwnerOfCategory);

		String MasterDesciptionValuefromOwnerOfCategory = seoTabPage.getDescriptionValueOwnerOfCatogory();
		Assertions.assertThat(MasterDesciptionValuefromOwnerOfCategory).doesNotContain(ActualValueFromitem);

		log(LogType.INFO,
				"Verify whether changed the invalid value in local attribute any attribute then quality status rule get failed and in SEO Tab, the invalid value gets add, in master the invalid value its not get add ");

	}

	// SEO_PL_025

	@PimFrameworkAnnotation(module = Modules.SEO, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.SEO_Tab)
	@Test(description = "SEO_PL_025 | Verify whether rules get retrieved as saved  ", groups = {
			"US","pim","SEO"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void Verify_whether_rules_get_retrieved_as_saved(Map<String, String> map) {

		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("itemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("SEOTAB"));

		// Adding FreeText in PageTitle Attribute
		seoTabPage.clickRetrieveRules().deletingValueFromPageTitle(map.get("DeletingAttributeForPageTitle"));
		seoTabPage.addingAttributeFromPageTitleFieldAttribute(map.get("AddingAttributeForPageTitle"));

		String ExpectedAttributeFromPageTitle = seoTabPage.getAttributesFromPageTitle();

		// Adding FreeText in MetaData Attribute
		seoTabPage.deletingValueFromMetaData(map.get("DeletingAttributeFromMetaData"));
		seoTabPage.AddingAttributeFromMetaData_MetaDescription(map.get("AddingAttributeForMetaData"));

		String ExpectedAttributeFromMetaData = seoTabPage.getAttributesFromMetaData();

		// Adding FreeText in Keyword

		seoTabPage.deletingValueFromKeywords(map.get("DeletingAttributeForKeywords"));
		seoTabPage.AddingAttributeFromMetaData_MetaDescription(map.get("AddingAttributeForKeywords"));

		String ExpectedAttributeFromKeywords = seoTabPage.getAttributesFromKeywords();

		// Adding FreeText in Description field

		seoTabPage.deletingValueFromDescription(map.get("DeletingAttributeForDescription"));
		seoTabPage
				.addingAttributeFromDescription_ProductorCategorydescription(map.get("AddingAttributeForDescription"));

		String ExpectedAttributeFromDescription = seoTabPage.getAttributesFromDescription();

		// Adding FreeText in H1Tag Field

		seoTabPage.deletingValueFromH1Tag(map.get("DeletingAttributeforH1Tag"));
		seoTabPage.AddingAttributeFromH1_Tag(map.get("AddingAttributeforH1Tag"));

		String ExpeectedAttributeFromH1Tag = seoTabPage.getAttributesFromH1Tag();

		// Adding FreeText in OwnerOf category

		/*
		 * seoTabPage.addingAttributeFromOwnerofCategory(map.get(
		 * "AttributeforOwnerofCategory"));
		 * 
		 * String expectedAttributeFromOwnerOfcategory =
		 * seoTabPage.getAttributesFromOwnerOfCatogory();
		 */

		// Navigating the Item in US Catalog for Same Item number
		pimHomepage.mainMenu().clickRefreshMenuIcon();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("itemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));

		productDetailSearchPage.selectTabfromDropdown(map.get("SEOTAB"));

		// after navigating the item from SEO Tab
		seoTabPage.clickRetrieveRules();
		String ActualAttributeFromPageTitle = seoTabPage.getAttributesFromPageTitle();
		String ActualAttributeFromMetaData = seoTabPage.getAttributesFromMetaData();
		String ActualAttributeFromKeyword = seoTabPage.getAttributesFromKeywords();
		String ActualAttributeFromDescription = seoTabPage.getAttributesFromDescription();
		String ActaulAttributeFromH1Tag = seoTabPage.getAttributesFromH1Tag();
		String ActualAttributeFromOwnerOfCatogory = seoTabPage.getAttributesFromOwnerOfCatogory();

		// Assertion Both are same

		Assertions.assertThat(ActualAttributeFromPageTitle).isEqualTo(ExpectedAttributeFromPageTitle);
		Assertions.assertThat(ActualAttributeFromMetaData).isEqualTo(ExpectedAttributeFromMetaData);
		Assertions.assertThat(ActualAttributeFromKeyword).isEqualTo(ExpectedAttributeFromKeywords);
		Assertions.assertThat(ActualAttributeFromDescription).isEqualTo(ExpectedAttributeFromDescription);
		Assertions.assertThat(ActaulAttributeFromH1Tag).isEqualTo(ExpeectedAttributeFromH1Tag);
		/*
		 * Assertions.assertThat(ActualAttributeFromOwnerOfCatogory).isEqualTo(
		 * expectedAttributeFromOwnerOfcategory);
		 */

		log(LogType.INFO, "Verify whether rules get retrieved as saved ");

	}

	// SEO_PL_026
	@PimFrameworkAnnotation(module = Modules.SEO, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.SEO_Tab)
	@Test(description = "SEO_PL_026 | Verify whether local attribute quantity is excluded in rule parameter list for SEO export fields ", groups = {
			"US","pim","SANITY","SEO"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void Verify_whether_local_attribute_quantity_is_excluded_in_rule_parameter_list_for_SEO_export_fields(
			Map<String, String> map) {

		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("itemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("SEOTAB"));
		qualityStatusPage.maximizeQualityStatusTab();
		seoTabPage.clickRetrieveRules();

		// Checking in PageTitle
		seoTabPage.dropDownLovFromPageTitle(map.get("LocalAttributeQuantityFieldValue"));

		Assertions.assertThat(seoTabPage.isDropDownValueVisible("dropdownvaluexpathForQuantityAttribute")).isFalse();
		// Checking in MetaData
		seoTabPage.dropDownLovFromMetaData(map.get("LocalAttributeQuantityFieldValue"));

		Assertions.assertThat(seoTabPage.isDropDownValueVisible("dropdownvaluexpathForQuantityAttribute")).isFalse();
		// Checking in Keyword
		seoTabPage.dropDownLovFromKeywords(map.get("LocalAttributeQuantityFieldValue"));

		Assertions.assertThat(seoTabPage.isDropDownValueVisible("dropdownvaluexpathForQuantityAttribute")).isFalse();
		// Checking in Description
		seoTabPage.dropDownLovFromDescription(map.get("LocalAttributeQuantityFieldValue"));

		Assertions.assertThat(seoTabPage.isDropDownValueVisible("dropdownvaluexpathForQuantityAttribute")).isFalse();

		// Checking in H1Tag
		seoTabPage.dropDownLovFromH1Tag(map.get("LocalAttributeQuantityFieldValue"));
		Assertions.assertThat(seoTabPage.isDropDownValueVisible("dropdownvaluexpathForQuantityAttribute")).isFalse();

		// checking in OwnerOfCategory
		seoTabPage.dropDownLovFromOwnerOfCategory(map.get("LocalAttributeQuantityFieldValue"));

		Assertions.assertThat(seoTabPage.isDropDownValueVisible("dropdownvaluexpathForQuantityAttribute")).isFalse();

		qualityStatusPage.minimizeQualityStatusTab();
		log(LogType.INFO, "Verify whether $Quantity field is not availble in 6 SEO Field ");

	}

	// SEO_PL_027
	@PimFrameworkAnnotation(module = Modules.SEO, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.SEO_Tab)
	@Test(description = "SEO_PL_027 | Verify whether_All item attributes_concatenates all the local attribute value fields Except Quantity field  ", groups = {
			"US","pim","SEO"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void Verify_whether_All_item_attributes_Keywords_Meta_Keywords_field(Map<String, String> map) {

		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("itemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));

		String ExpectedAllValuesFromLocalAttribute = localAttributePage.getAllitemAttributeExceptQuantity();

		productDetailSearchPage.selectTabfromDropdown(map.get("SEOTAB"));

		seoTabPage.clickRetrieveRules().deletingValueFromKeywords(map.get("DeletingAttributeForKeywords"));

		String ActualValueFromKeyords = seoTabPage.getDescriptionValueFromKeywords();

		Assertions.assertThat(ExpectedAllValuesFromLocalAttribute).isEqualTo(ActualValueFromKeyords);
	}

	// SEO_PL_028
	@PimFrameworkAnnotation(module = Modules.SEO, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.SEO_Tab)
	@Test(description = "SEO_PL_028 | Verify whether all the local attribute fields gets listed as an option in rule parameter dropdown for all the 6 SEO export fields ", groups = {
			"US","pim","SEO"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void Verify_whether_all_the_local_attribute_fields_gets_listed_as_an_option_in_rule_parameter_dropdown_for_all_the_6_SEO_export_fields(
			Map<String, String> map) {

		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("itemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
		List<String> ActualAttributeValuesFromLocalAttribute = localAttributePage
				.ListOfLocalAttributesWithExceptionfield(map.get("LocalAttributeExceptionField"));
		productDetailSearchPage.selectTabfromDropdown(map.get("SEOTAB"));
		seoTabPage.clickRetrieveRules();
		// From Page title
		List<String> ExpectedAttributeLOVfromPageTitle = seoTabPage.dropDownLovValuesFromPagetitle();
		Assertions.assertThat(ActualAttributeValuesFromLocalAttribute)
				.allMatch(e -> ExpectedAttributeLOVfromPageTitle.contains(e));
		// From Meta Data
		List<String> ExpectedAttributeLOVfromMetaData = seoTabPage.dropDownLovValuesFromMetaData();
		Assertions.assertThat(ActualAttributeValuesFromLocalAttribute)
				.allMatch(e -> ExpectedAttributeLOVfromMetaData.contains(e));
		// From Keywords
		List<String> ExpectedAttributeLOVfromKeyWords = seoTabPage.dropDownLovValuesFromKeywords();
		Assertions.assertThat(ActualAttributeValuesFromLocalAttribute)
				.allMatch(e -> ExpectedAttributeLOVfromKeyWords.contains(e));
		// From Description
		List<String> ExpectedAttributeLOVfromDescription = seoTabPage.dropDownLovValuesFromDescription();
		Assertions.assertThat(ActualAttributeValuesFromLocalAttribute)
				.allMatch(e -> ExpectedAttributeLOVfromDescription.contains(e));
		// From H1Tag
		List<String> ExpectedAttributeLOVfromH1Tag = seoTabPage.dropDownLovValuesFromH1Tag();
		Assertions.assertThat(ActualAttributeValuesFromLocalAttribute)
				.allMatch(e -> ExpectedAttributeLOVfromH1Tag.contains(e));
	}

	// SEO_PL_029
	@PimFrameworkAnnotation(module = Modules.SEO, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.SEO_Tab)
	@Test(description = "SEO_PL_029 | Verify whether *space gets trimmed off in free text* rule parameter when overridden as part of 6 SEO export fields ", groups = {
			"US","pim","SEO"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void Verify_whether_space_gets_trimmed_off_in_free_text_rule_parameter_when_overridden_as_part_of_6_SEO_export_fields(
			Map<String, String> map) {

		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("itemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("SEOTAB"));

		// FreeText with *Space* Added into Page Title
		qualityStatusPage.maximizeQualityStatusTab();
		seoTabPage.clickRetrieveRules().deletingValueFromPageTitle(map.get("DeletingAttributeForPageTitle"));
		String descriptionValueBeforeAddingFreeTextFromPageTitle = seoTabPage.getDescriptionValueFromPageTitle();
		seoTabPage.addingAndEditingAttributeFromPageTitleFieldAttribute(map.get("AddingAttributeForPageTitle"),
				map.get("AddingValuesInFreeText"));
		String descriptionValueAfterAddingFreeTextwithSpaceFromPageTitle = seoTabPage
				.getDescriptionValueFromPageTitle();
		String ActualFreeTextFromPageTitle = seoTabPage.getAddedFreeTextSpace(
				descriptionValueBeforeAddingFreeTextFromPageTitle,
				descriptionValueAfterAddingFreeTextwithSpaceFromPageTitle);
		Assertions.assertThat(map.get("AddingValuesInFreeText").trim()).isEqualTo(ActualFreeTextFromPageTitle);

		// FreeText with *Space* Added into MetaData
		seoTabPage.clickRetrieveRules().deletingValueFromMetaData(map.get("DeletingAttributeFromMetaData"));
		String descriptionValueBeforeAddingFreeTextFromMetaData = seoTabPage.getDescriptionValueFromMetaData();
		seoTabPage.addingAndEditingAttributeFromMetaDataFieldAttribute(map.get("AddingAttributeForMetaData"),
				map.get("AddingValuesInFreeText"));
		String descriptionValueAfterAddingFreeTextwithSpaceFromMetadata = seoTabPage.getDescriptionValueFromMetaData();
		String ActualFreeTextFromMetaData = seoTabPage.getAddedFreeTextSpace(
				descriptionValueBeforeAddingFreeTextFromMetaData,
				descriptionValueAfterAddingFreeTextwithSpaceFromMetadata);
		Assertions.assertThat(map.get("AddingValuesInFreeText").trim()).isEqualTo(ActualFreeTextFromMetaData);

		// FreeText with *Space* Added into Keywords
		seoTabPage.clickRetrieveRules().deletingValueFromKeywords(map.get("DeletingAttributeForKeywords"));
		String descriptionValueBeforeAddingFreeTextFromKeywords = seoTabPage.getDescriptionValueFromKeywords();
		seoTabPage.addingAndEditingAttributeFromKeywordsFieldAttribute(map.get("AddingAttributeForKeywords"),
				map.get("AddingValuesInFreeText"));
		String descriptionValueAfterAddingFreeTextwithSpaceFromKeywords = seoTabPage.getDescriptionValueFromKeywords();
		String ActualFreeTextFromKeyWords = seoTabPage.getAddedFreeTextSpace(
				descriptionValueBeforeAddingFreeTextFromKeywords,
				descriptionValueAfterAddingFreeTextwithSpaceFromKeywords);
		Assertions.assertThat(map.get("AddingValuesInFreeText").trim()).isEqualTo(ActualFreeTextFromKeyWords);

		// FreeText with *Space* Added into Description
		seoTabPage.clickRetrieveRules().deletingValueFromDescription(map.get("DeletingAttributeForDescription"));
		String descriptionValueBeforeAddingFreeTextFromDescription = seoTabPage.getDescriptionValueDescription();
		seoTabPage.addingAndEditingAttributeFromDescriptionFieldAttribute(map.get("AddingAttributeForDescription"),
				map.get("AddingValuesInFreeText"));
		String descriptionValueAfterAddingFreeTextwithSpaceFromdescription = seoTabPage
				.getDescriptionValueDescription();
		String ActualFreeTextFromDescription = seoTabPage.getAddedFreeTextSpace(
				descriptionValueBeforeAddingFreeTextFromDescription,
				descriptionValueAfterAddingFreeTextwithSpaceFromdescription);
		Assertions.assertThat(map.get("AddingValuesInFreeText").trim()).isEqualTo(ActualFreeTextFromDescription);

		// FreeText with *Space* Added into H1Tag
		seoTabPage.clickRetrieveRules().deletingValueFromH1Tag(map.get("DeletingAttributeforH1Tag"));
		String descriptionValueBeforeAddingFreeTextFromH1Tag = seoTabPage.getDescriptionValueH1Tag();
		seoTabPage.addingAndEditingAttributeFromH1TagFieldAttribute(map.get("AddingAttributeforH1Tag"),
				map.get("AddingValuesInFreeText"));
		String descriptionValueAfterAddingFreeTextwithSpaceFromH1Tag = seoTabPage.getDescriptionValueH1Tag();
		String ActualFreeTextFromH1Tag = seoTabPage.getAddedFreeTextSpace(descriptionValueBeforeAddingFreeTextFromH1Tag,
				descriptionValueAfterAddingFreeTextwithSpaceFromH1Tag);
		Assertions.assertThat(map.get("AddingValuesInFreeText").trim()).isEqualTo(ActualFreeTextFromH1Tag);
		qualityStatusPage.minimizeQualityStatusTab();

	}

	// SEO_PL_030
	@PimFrameworkAnnotation(module = Modules.SEO, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.SEO_Tab)
	@Test(description = "SEO_PL_030 | Verify when we do changes in rule, click on generate description and save the rule, generating current date in SEO Rule update ", groups = {
			"US","pim","SANITY","SEO"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void Verify_if_changes_made_in_default_rule_then_currentDate_is_displayed_in_SEORuleUpdateDate(
			Map<String, String> map) {

		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("itemNumber")).clickSeachButton()
				.clickSettingIcon().clickFieldSelectionOption();
		log(LogType.INFO, "Navigate to Field Selection Page from Setting Icon for US Catalog");

		fieldselectionpage.enterFieldName(map.get("Header")).clickHeaderText(map.get("HeaderText")).clickAddButton()
				.clickOkButton();

		productDetailSearchPage.clickOnFirstResult().selectTabfromDropdown(map.get("SEOTAB"));
		seoTabPage.clickRetrieveRules().deletingValueFromPageTitle(map.get("DeletingAttributeForPageTitle"));
		seoTabPage.addingAttributeFromPageTitleFieldAttribute(map.get("AddingAttributeForPageTitle"));
		String GetSEORuleUpdatedDate = productDetailSearchPage.getItemField(map.get("fieldIndex"));
		String todaysDate = DateandTimeUtils.getTodaysDateForQualityStatus();
		Assertions.assertThat(GetSEORuleUpdatedDate).contains(todaysDate);
	}

	// SEO_PL_031
	@PimFrameworkAnnotation(module = Modules.SEO, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.SEO_Tab)
	@Test(description = "SEO_PL_031 | Verify when we click on generate description and save the rule, generating current date in SEO Rule update ", groups = {
			"US","pim","SANITY","SEO"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void Verify_if_NOchanges_made_in_default_rule_then_currentDate_is_Should_not_displayed_in_SEORuleUpdateDate(
			Map<String, String> map) {

		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("itemNumber")).clickSeachButton()
				.clickSettingIcon().clickFieldSelectionOption();
		log(LogType.INFO, "Navigate to Field Selection Page from Setting Icon for US Catalog");

		fieldselectionpage.enterFieldName(map.get("Header")).clickHeaderText(map.get("HeaderText")).clickAddButton()
				.clickOkButton();

		productDetailSearchPage.clickOnFirstResult().selectTabfromDropdown(map.get("SEOTAB"));
		seoTabPage.clickRetrieveRules().clickGenerateDescription().clickSaveRules();
		String GetSEORuleUpdatedDate = productDetailSearchPage.getItemField(map.get("fieldIndex"));
		String todaysDate = DateandTimeUtils.getTodaysDate();
		Assertions.assertThat(GetSEORuleUpdatedDate).doesNotContain(todaysDate);

	}

	// SEO_PL_032
	@PimFrameworkAnnotation(module = Modules.SEO, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.SEO_Tab)
	@Test(description = "SEO_PL_032 |Verify when we click on generate description and save the rule, generating current date in SEO Rule update ", groups = {
			"US","pim","SANITY","SEO"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void Verify_if_NOchanges_made_in_default_rule_which_doesnt_has_SEO_rule_ran_priory_then_currentDate_is_Should_not_displayed_in_SEORuleUpdateDate(
			Map<String, String> map) {

		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("itemNumber")).clickSeachButton()
				.clickSettingIcon().clickFieldSelectionOption();
		log(LogType.INFO, "Navigate to Field Selection Page from Setting Icon for US Catalog");

		fieldselectionpage.enterFieldName(map.get("Header")).clickHeaderText(map.get("HeaderText")).clickAddButton()
				.clickOkButton();

		productDetailSearchPage.clickOnFirstResult().selectTabfromDropdown(map.get("SEOTAB"));
		seoTabPage.clickRetrieveRules();
		String GetSEORuleUpdatedDate = productDetailSearchPage.getItemField(map.get("fieldIndex"));
		String todaysDate = DateandTimeUtils.getTodaysDate();
		Assertions.assertThat(GetSEORuleUpdatedDate).doesNotContain(todaysDate);
		Assertions.assertThat(GetSEORuleUpdatedDate).isEqualTo("");

	}

	// SEO_PL_036
	@PimFrameworkAnnotation(module = Modules.SEO, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.SEO_Tab)
	@Test(description = "SEO_PL_036 | Verify_adding_freeText_twise_for_All_the_6SEO_export_fields ", groups = {
			"US","pim","SEO"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void Verify_adding_freeText_twise_for_All_the_6SEO_export_fields(Map<String, String> map) {

		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("itemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("SEOTAB"));
		// Adding FreeText twise into PageTitle
		seoTabPage.clickRetrieveRules().deletingValueFromPageTitle(map.get("OverRiddenDragAndDropPageTitleValues"));
		seoTabPage.addingTwiseFreeTextinPageTitle(map.get("AttributeforPageTitile"));
		Assertions.assertThat(seoTabPage.getFreeTextAttributeCountFromPageTitle()).isEqualTo(2);
		// Adding FreeText twise into MetaData
		seoTabPage.clickRetrieveRules().deletingValueFromMetaData(map.get("OveriddenAttributeValueforMetaData"));
		seoTabPage.addingTwiseFreeTextinMetaData(map.get("AttributeforMetaData"));
		Assertions.assertThat(seoTabPage.getFreeTextAttributeCountFromMetaData()).isEqualTo(2);
		// Adding FreeText twise into Keywords
		seoTabPage.clickRetrieveRules().deletingValueFromKeywords(map.get("OverriddenAttributeValueforKeyword"));
		seoTabPage.addingTwiseFreeTextinKeyWords(map.get("AttributeforKeyword"));
		Assertions.assertThat(seoTabPage.getFreeTextAttributeCountFromKeywords()).isEqualTo(2);
		// Adding FreeText twise into Description
		seoTabPage.clickRetrieveRules()
				.deletingValueFromDescription(map.get("OveriddenAttributeValueforDescription"));
		seoTabPage.addingTwiseFreeTextinDescription(map.get("AttributeforDescription"));
		Assertions.assertThat(seoTabPage.getFreeTextAttributeCountFromDescription()).isEqualTo(2);
		// Adding FreeText twise into Description
		seoTabPage.clickRetrieveRules().deletingValueFromH1Tag(map.get("OveriddenAttributeValueforH1Tag"));
		seoTabPage.addingTwiseFreeTextinH1Tag(map.get("AttributeforH1Tag"));
		Assertions.assertThat(seoTabPage.getFreeTextAttributeCountFromH1Tag()).isEqualTo(2);

	}

	// SEO_PL_037
	@PimFrameworkAnnotation(module = Modules.SEO, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.SEO_Tab)
	@Test(description = "SEO_PL_037 | Selected rule parameter should get dropped from Meta description field ", groups = {
			"US","pim","SANITY","SEO"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void Verify_if_rule_parameters_are_getting_dropped_for_SEO_fields(Map<String, String> map) {

		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("itemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("SEOTAB"));

		seoTabPage.clickRetrieveRules()
				.AddingAttributeFromMetaData_MetaDescription(map.get("AddingAttributeForMetaData"));

		seoTabPage.deletingValueFromMetaData(map.get("DeletingAttributeFromMetaData"));

		String ExpectedAttributeFromMetaData = seoTabPage.getAttributesFromMetaData();

		String ActualValueFromMetaData = map.get("DeletingAttributeFromMetaData");

		Assertions.assertThat(ActualValueFromMetaData).doesNotContain(ExpectedAttributeFromMetaData);

	}

	// SEO_PL_038
	@PimFrameworkAnnotation(module = Modules.SEO, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.SEO_Tab)
	@Test(description = "SEO_PL_038 | Verify if tab containing SEO fields has been named as SEO ", groups = {
			"US","pim","SANITY","SEO"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void Verify_if_tab_containing_SEO_fields_has_been_named_as_SEO(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("itemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("SEOTAB"));
		String ActualSEOName = seoTabPage.getSEOName();
		String ExpectedSEOName = "SEO";
		Assertions.assertThat(ActualSEOName).isEqualTo(ExpectedSEOName);
	}

}