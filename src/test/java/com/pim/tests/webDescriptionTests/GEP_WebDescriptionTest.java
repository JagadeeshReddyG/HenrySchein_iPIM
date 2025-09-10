package com.pim.tests.webDescriptionTests;

import com.aventstack.extentreports.model.Log;
import com.pim.annotations.PimFrameworkAnnotation;
import com.pim.annotations.TestDataSheet;
import com.pim.enums.CategoryType;
import com.pim.enums.LogType;
import com.pim.enums.Modules;
import com.pim.enums.TestCaseSheet;
import com.pim.pages.BasePage;
import com.pim.pages.GEP_WebDescriptionPage;
import com.pim.pages.GlobalAttributePage;
import com.pim.pages.LocalAttributePage;
import com.pim.pages.LoginPage;
import com.pim.pages.MainMenu;
import com.pim.pages.PimHomepage;
import com.pim.pages.ProductDetailSearchPage;
import com.pim.pages.QualityStatusPage;
import com.pim.pages.QueriesSubMenu;
import com.pim.tests.BaseTest;
import com.pim.utils.DataProviderUtils;
import com.pim.utils.ExcelUtils;
import com.pim.utils.FileUtils;
import com.pim.utils.Javautils;

import org.assertj.core.api.AbstractBooleanAssert;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;
import org.testng.asserts.Assertion;

import static com.pim.reports.FrameworkLogger.log;

import java.io.IOException;
import java.util.List;
import java.util.Map;


public class GEP_WebDescriptionTest extends BaseTest {

	Assertion a = new Assertion();
	GEP_WebDescriptionPage gepWebDescPage = new GEP_WebDescriptionPage();
	LocalAttributePage localAttPage = new LocalAttributePage();
	BasePage basePage = new BasePage();
	ProductDetailSearchPage productDetailSearchPage = new ProductDetailSearchPage();
	QueriesSubMenu queriesSubMenu = new QueriesSubMenu();
	QualityStatusPage qualityStatusPage = new QualityStatusPage();
	GlobalAttributePage globalAttributePage=new GlobalAttributePage();

	public void VerifyWebDescriptionFields() {
		Assertions.assertThat(gepWebDescPage.isWebDescriptionFieldEditable("fddTextfield")).isTrue();
		Assertions.assertThat(gepWebDescPage.isWebDescriptionFieldEditable("addTextfield")).isTrue();
		Assertions.assertThat(gepWebDescPage.isWebDescriptionFieldEditable("lsdTextField")).isTrue();
		Assertions.assertThat(gepWebDescPage.isWebDescriptionFieldEditable("sdTextField")).isTrue();
//		Assertions.assertThat(gepWebDescPage.isWebDescriptionFieldEditable("doedTextField")).isTrue();
		Assertions.assertThat(gepWebDescPage.isWebDescriptionFieldEditable("poTextField")).isTrue();
		Assertions.assertThat(gepWebDescPage.isWebDescriptionFieldEditable("ewdTextField")).isTrue();
//		Assertions.assertThat(gepWebDescPage.isWebDescriptionFieldEditable("pcdTextField")).isTrue();
	}
	//MDM-758-001 (US) WRFL_105 US
	@PimFrameworkAnnotation(module = Modules.GEP_WEB_DESCRIPTION, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.GEPWebDescription)
	@Test(priority = 0,description = "WRFL_105 US | Verify if below GEP Web Description fields are editable, GEP Full Display Description, GEP Abbreviated Display Description, GEP Look Ahead Search Description, GEP Detailed or Extended Description", 
	dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"US","WEB_DESCRIPTION"})
	public void AllFieldsCheckTestForUS(Map<String, String> map) throws InterruptedException, IOException {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("itemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabNameGEP"));
		BasePage.WaitForMiliSec(2000);
		VerifyWebDescriptionFields();
	}
	
	//MDM-758-001 (CA) WRFL_105 CA
	@PimFrameworkAnnotation(module = Modules.GEP_WEB_DESCRIPTION, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.GEPWebDescription)
	@Test(priority = 0,description = "WRFL_105 CA | Verify if below GEP Web Description fields are editable, GEP Full Display Description, GEP Abbreviated Display Description, GEP Look Ahead Search Description, GEP Detailed or Extended Description", 
	dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"CA","WEB_DESCRIPTION"})
	public void AllFieldsCheckTestForCA(Map<String, String> map) throws InterruptedException, IOException {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("itemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabNameGEP"));
		BasePage.WaitForMiliSec(2000);
		VerifyWebDescriptionFields();
	}
	
	//MDM-758-006 (US) WRFL_110
	@PimFrameworkAnnotation(module = Modules.GEP_WEB_DESCRIPTION, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.GEPWebDescription)
	@Test(priority = 0,description = "WRFL_110 US | Verify if below GEP Web Description fields are editable, GEP Full Display Description, GEP Abbreviated Display Description, GEP Look Ahead Search Description, GEP Detailed or Extended Description", 
	dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"US","WEB_DESCRIPTION"})
	public void updationOfGEPWebDescriptionAfterUpdationOfLocalAttributeParameterForUS(Map<String, String> map) throws InterruptedException, IOException {
		
		FileUtils.storeCurrentTimeInJsonTimesPropertiesFile(map.get("TestCaseName")+map.get("itemNumber"));

		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("itemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabNameLA"));
		BasePage.WaitForMiliSec(2000);
		localAttPage.itemFieldAttributeChange(map.get("Item1")).selectValuefromSaveDropdown(map.get("Save"));
		productDetailSearchPage.clickRefreshIcon();	
		//updating one more time with other utem
		localAttPage.itemFieldAttributeChange(map.get("Item2")).selectValuefromSaveDropdown(map.get("Save"));
		productDetailSearchPage.clickRefreshIcon();
		
		// Checking in Quality Status tab Rules should be OK
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType"))
		.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabNameQS"));
		List<String> localmandatoryrule = qualityStatusPage.maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
		qualityStatusPage.minimizeQualityStatusTab();

		List<String> myList = new Javautils().readMultipleValuesFromExcel(map.get("QualityStatusRule"));
		Assertions.assertThat(Javautils.compareList(myList, localmandatoryrule)).isEqualTo(true);
		
		//checking change is visible in GEP Web Description or Not
		productDetailSearchPage.clickRefreshIcon();
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType"))
		.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabNameGEP"));
		BasePage.WaitForMiliSec(2000);
		productDetailSearchPage.clickRefreshIcon();
		String fullDisplayDesc = gepWebDescPage.getFullDisplayDescription();
		Assertions.assertThat(fullDisplayDesc.contains(map.get("Item2")));		
		
		//checking in Master
		queriesSubMenu.selectCatalogType(map.get("CatalogTypeMaster"))
		.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabNameGEP"));
		BasePage.WaitForMiliSec(2000);
		productDetailSearchPage.clickRefreshIcon();
		String fullDisplayDescInPIM = gepWebDescPage.getFullDisplayDescription();
		Assertions.assertThat(fullDisplayDescInPIM.contains(map.get("Item2")));
		
		//changing Item one more time for next execution help
		queriesSubMenu.selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("itemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabNameLA"));
		BasePage.WaitForMiliSec(2000);
		localAttPage.itemFieldAttributeChange(map.get("Item3")).selectValuefromSaveDropdown(map.get("Save"));
		productDetailSearchPage.clickRefreshIcon();	
		pimHomepage.clickLogoutButton();
	}
	
	//MDM-758-006 (CA) WRFL_110
	@PimFrameworkAnnotation(module = Modules.GEP_WEB_DESCRIPTION, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.GEPWebDescription)
	@Test(priority = 0,description = "WRFL_110 CA | Verify if below GEP Web Description fields are editable, GEP Full Display Description, GEP Abbreviated Display Description, GEP Look Ahead Search Description, GEP Detailed or Extended Description", 
	dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"CA","WEB_DESCRIPTION"})
	public void updationOfGEPWebDescriptionAfterUpdationOfLocalAttributeParameterForCA(Map<String, String> map) throws InterruptedException, IOException {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("itemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabNameLA"));
		BasePage.WaitForMiliSec(2000);
		localAttPage.itemFieldAttributeChange(map.get("Item1")).selectValuefromSaveDropdown(map.get("Save"));
		productDetailSearchPage.clickRefreshIcon();	
		//updating one more time with other utem
		localAttPage.itemFieldAttributeChange(map.get("Item2")).selectValuefromSaveDropdown(map.get("Save"));
		productDetailSearchPage.clickRefreshIcon();
		
		// Checking in Quality Status tab Rules should be OK
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType"))
		.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabNameQS"));
		List<String> localmandatoryrule = qualityStatusPage.maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
		qualityStatusPage.minimizeQualityStatusTab();

		List<String> myList = new Javautils().readMultipleValuesFromExcel(map.get("QualityStatusRule"));
		Assertions.assertThat(Javautils.compareList(myList, localmandatoryrule)).isEqualTo(true);
		
		//checking change is visible in GEP Web Description or Not
		productDetailSearchPage.clickRefreshIcon();
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType"))
		.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabNameGEP"));
		BasePage.WaitForMiliSec(2000);
		productDetailSearchPage.clickRefreshIcon();
		String fullDisplayDesc = gepWebDescPage.getFullDisplayDescription();
		Assertions.assertThat(fullDisplayDesc.contains(map.get("Item2")));		
		
		//checking in Master
		queriesSubMenu.selectCatalogType(map.get("CatalogTypeMaster"))
		.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabNameGEP"));
		BasePage.WaitForMiliSec(2000);
		productDetailSearchPage.clickRefreshIcon();
		String fullDisplayDescInPIM = gepWebDescPage.getFullDisplayDescription();
		Assertions.assertThat(fullDisplayDescInPIM.contains(map.get("Item2")));
		
		//changing Item one more time for next execution help
		queriesSubMenu.selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("itemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabNameLA"));
		BasePage.WaitForMiliSec(2000);
		localAttPage.itemFieldAttributeChange(map.get("Item3")).selectValuefromSaveDropdown(map.get("Save"));
		productDetailSearchPage.clickRefreshIcon();	
		pimHomepage.clickLogoutButton();
	}

	//MDM-758-011 (US)
	@PimFrameworkAnnotation(module = Modules.GEP_WEB_DESCRIPTION, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.GEPWebDescription)
	@Test(priority = 0,description = "WRFL_112 US | Verify if existing 'GEP Web Description' is getting updated upon updating value of one of the Local Attribute parameter for an item having Mandatory Local Attribute Failure", 
	dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"US","WEB_DESCRIPTION"})
	public void chekingUpdationInLocalAttributeDoesNotUpdateGEPWDInCaseOfMandatoryLocalAttributeFailureForUS(Map<String, String> map) throws InterruptedException, IOException {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("itemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabNameLA"));
		BasePage.WaitForMiliSec(2000);
		localAttPage.itemFieldAttributeChange(map.get("Item1")).selectValuefromSaveDropdown(map.get("Save"));
		productDetailSearchPage.clickRefreshIcon();	
		//updating one more time with other item.
		localAttPage.itemFieldAttributeChange(map.get("Item2")).selectValuefromSaveDropdown(map.get("Save"));
		productDetailSearchPage.clickRefreshIcon();
		
		// Checking in Quality Status tab Rules Should be Failed
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType"))
		.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabNameQS"));
		List<String> localmandatoryrule = qualityStatusPage.maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
		qualityStatusPage.minimizeQualityStatusTab();

		List<String> myList = new Javautils().readMultipleValuesFromExcel(map.get("QualityStatusRule"));
		Assertions.assertThat(Javautils.compareList(myList, localmandatoryrule)).isEqualTo(true);
		
		//checking change is visible in GEP Web Description or Not
		BasePage.WaitForMiliSec(60000);
		productDetailSearchPage.clickRefreshIcon();
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType"))
		.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabNameGEP"));
		BasePage.WaitForMiliSec(2000);
		productDetailSearchPage.clickRefreshIcon();
		String fullDisplayDesc = gepWebDescPage.getFullDisplayDescription();
		Boolean contains = fullDisplayDesc.contains(map.get("Item2"));
		a.assertFalse(contains);
		
		//changing Item one more time for next execution help
		queriesSubMenu.selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("itemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabNameLA"));
		BasePage.WaitForMiliSec(2000);
		localAttPage.itemFieldAttributeChange(map.get("Item3")).selectValuefromSaveDropdown(map.get("Save"));
		productDetailSearchPage.clickRefreshIcon();	
		pimHomepage.clickLogoutButton();
	}
	
	//MDM-758-011 (CA)
	@PimFrameworkAnnotation(module = Modules.GEP_WEB_DESCRIPTION, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.GEPWebDescription)
	@Test(priority = 0,description = "WRFL_112 CA | Verify if existing 'GEP Web Description' is getting updated upon updating value of one of the Local Attribute parameter for an item having Mandatory Local Attribute Failure", 
	dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"CA","WEB_DESCRIPTION"})
	public void chekingUpdationInLocalAttributeDoesNotUpdateGEPWDInCaseOfMandatoryLocalAttributeFailureForCA(Map<String, String> map) throws InterruptedException, IOException {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("itemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabNameLA"));
		BasePage.WaitForMiliSec(2000);
		localAttPage.itemFieldAttributeChange(map.get("Item1")).selectValuefromSaveDropdown(map.get("Save"));
		productDetailSearchPage.clickRefreshIcon();	
		//updating one more time with other item.
		localAttPage.itemFieldAttributeChange(map.get("Item2")).selectValuefromSaveDropdown(map.get("Save"));
		productDetailSearchPage.clickRefreshIcon();
		
		// Checking in Quality Status tab Rules Should be Failed
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType"))
		.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabNameQS"));
		List<String> localmandatoryrule = qualityStatusPage.maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
		qualityStatusPage.minimizeQualityStatusTab();

		List<String> myList = new Javautils().readMultipleValuesFromExcel(map.get("QualityStatusRule"));
		Assertions.assertThat(Javautils.compareList(myList, localmandatoryrule)).isEqualTo(true);
		
		//checking change is visible in GEP Web Description or Not
		productDetailSearchPage.clickRefreshIcon();
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType"))
		.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabNameGEP"));
		BasePage.WaitForMiliSec(2000);
		productDetailSearchPage.clickRefreshIcon();
		String fullDisplayDesc = gepWebDescPage.getFullDisplayDescription();
		Boolean contains = fullDisplayDesc.contains(map.get("Item2"));
		a.assertFalse(contains);
		
		//changing Item one more time for next execution help
		queriesSubMenu.selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("itemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabNameLA"));
		BasePage.WaitForMiliSec(2000);
		localAttPage.itemFieldAttributeChange(map.get("Item3")).selectValuefromSaveDropdown(map.get("Save"));
		productDetailSearchPage.clickRefreshIcon();	
		pimHomepage.clickLogoutButton();
	}
	
	//MDM-758-002 (US) WRFL_107 CA
	@PimFrameworkAnnotation(module = Modules.GEP_WEB_DESCRIPTION, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.GEPWebDescription)
	@Test(priority = 0,description = "WRFL_107 US | Verify if 'GEP Full Display Description'  gets formed as per the 'IDQ_Desc Rules' for brand new Items belongs to specific Primary Taxonomy, under 'GEP WEB Description' Tab", 
	dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"US","WEB_DESCRIPTION"})
	public void chekingFullDisplayDescriptionFormationIsFollowingIDQDescRuleForUS(Map<String, String> map) throws InterruptedException, IOException {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("itemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabNameLA"));
		BasePage.WaitForMiliSec(2000);
		
		productDetailSearchPage.maximizeProductDetailTab();
		String LAValuesWithSpace = gepWebDescPage.getLAValuesForFullDisplayDescAndOthers(map.get("LANamesList"));
		String LAValues = gepWebDescPage.removeSpaceInFirstAndLast(LAValuesWithSpace);
		productDetailSearchPage.minimizeProductDetailTab();
		
//		productDetailSearchPage.selectTabfromDropdown(map.get("TabNameGA"));
//		String jdeSize = globalAttributePage.getJdeSize();
//		String jdeDescValue = globalAttributePage.convertEAIntoEachForJdeSize(jdeSize);
//		log(LogType.EXTENTANDCONSOLE, LAValues + jdeDescValue);
		log(LogType.EXTENTANDCONSOLE, LAValues);


		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType"))
		.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabNameGEP"));
		String fullDisplayDescription = gepWebDescPage.getFullDisplayDescription();
		log(LogType.EXTENTANDCONSOLE, fullDisplayDescription + "");
		a.assertEquals((LAValues) , (fullDisplayDescription));
		pimHomepage.clickLogoutButton();
	}
	
	//MDM-758-002 (CA)
	@PimFrameworkAnnotation(module = Modules.GEP_WEB_DESCRIPTION, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.GEPWebDescription)
	@Test(priority = 0,description = "WRFL_107 CA | Verify if 'GEP Full Display Description'  gets formed as per the 'IDQ_Desc Rules' for brand new Items belongs to specific Primary Taxonomy, under 'GEP WEB Description' Tab", 
	dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"US","WEB_DESCRIPTION"})
	public void chekingFullDisplayDescriptionFormationIsFollowingIDQDescRuleForCA(Map<String, String> map) throws InterruptedException, IOException {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("itemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabNameLA"));
		BasePage.WaitForMiliSec(2000);
		
		productDetailSearchPage.maximizeProductDetailTab();
		String LAValuesWithSpace = gepWebDescPage.getLAValuesForFullDisplayDescAndOthers(map.get("LANamesList"));
		String LAValues = gepWebDescPage.removeSpaceInFirstAndLast(LAValuesWithSpace);
		productDetailSearchPage.minimizeProductDetailTab();
		
//		productDetailSearchPage.selectTabfromDropdown(map.get("TabNameGA"));
//		String jdeSize = globalAttributePage.getJdeSize();
//		String jdeDescValue = globalAttributePage.convertEAIntoEachForJdeSize(jdeSize);
//		log(LogType.EXTENTANDCONSOLE, LAValues + jdeDescValue);
		log(LogType.EXTENTANDCONSOLE, LAValues);


		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType"))
		.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabNameGEP"));
		String fullDisplayDescription = gepWebDescPage.getFullDisplayDescription();
		log(LogType.EXTENTANDCONSOLE, fullDisplayDescription + "");
		a.assertEquals((LAValues) , (fullDisplayDescription));
		pimHomepage.clickLogoutButton();
	}

	//MDM-758-003 (US) WRFL_108  US
	@PimFrameworkAnnotation(module = Modules.GEP_WEB_DESCRIPTION, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.GEPWebDescription)
	@Test(priority = 0,description = "WRFL_108 US | Verify if 'GEP Abbreviated Display Description'  gets formed as per the 'IDQ_Desc Rules' for brand new Items belongs to specific Primary Taxonomy, under 'GEP WEB Description' Tab", 
	dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"US","WEB_DESCRIPTION"})
	public void chekingAbbreviatedDisplayDescriptionFormationIsFollowingIDQDescRuleForUS(Map<String, String> map) throws InterruptedException, IOException {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("itemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabNameLA"));
		BasePage.WaitForMiliSec(2000);
		
		productDetailSearchPage.maximizeProductDetailTab();
		String LAValuesWithSpace = gepWebDescPage.getLAValuesForFullDisplayDescAndOthers(map.get("LANamesList"));
		String LAValuesWithJar = gepWebDescPage.removeSpaceInFirstAndLast(LAValuesWithSpace);
		String LAValues = gepWebDescPage.convertJarintoJr(LAValuesWithJar);
		productDetailSearchPage.minimizeProductDetailTab();
		
//		productDetailSearchPage.selectTabfromDropdown(map.get("TabNameGA"));
//		String jdeSize = globalAttributePage.getJdeSize();
//		String jdeDescValue = globalAttributePage.convertEAIntoEachForJdeSize(jdeSize);
//		log(LogType.EXTENTANDCONSOLE, LAValues + jdeDescValue);
		log(LogType.EXTENTANDCONSOLE, LAValues);

		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType"))
		.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabNameGEP"));
		String abbreviatedDisplayDescription = gepWebDescPage.getAbbreviatedDisplayDescription();
		log(LogType.EXTENTANDCONSOLE, abbreviatedDisplayDescription + "");
		a.assertEquals((LAValues) , abbreviatedDisplayDescription);
		pimHomepage.clickLogoutButton();
	}

	
	//MDM-758-003 (CA) WRFL_108 CA
	@PimFrameworkAnnotation(module = Modules.GEP_WEB_DESCRIPTION, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.GEPWebDescription)
	@Test(priority = 0,description = "WRFL_108 CA | Verify if 'GEP Abbreviated Display Description'  gets formed as per the 'IDQ_Desc Rules' for brand new Items belongs to specific Primary Taxonomy, under 'GEP WEB Description' Tab", 
	dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"US","WEB_DESCRIPTION"})
	public void chekingAbbreviatedDisplayDescriptionFormationIsFollowingIDQDescRuleForCA(Map<String, String> map) throws InterruptedException, IOException {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("itemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabNameLA"));
		BasePage.WaitForMiliSec(2000);
		
		productDetailSearchPage.maximizeProductDetailTab();
		String LAValuesWithSpace = gepWebDescPage.getLAValuesForFullDisplayDescAndOthers(map.get("LANamesList"));
		String LAValuesWithJar = gepWebDescPage.removeSpaceInFirstAndLast(LAValuesWithSpace);
		String LAValues = gepWebDescPage.convertJarintoJr(LAValuesWithJar);

		productDetailSearchPage.minimizeProductDetailTab();
		
//		productDetailSearchPage.selectTabfromDropdown(map.get("TabNameGA"));
//		String jdeSize = globalAttributePage.getJdeSize();
//		String jdeDescValue = globalAttributePage.convertEAIntoEachForJdeSize(jdeSize);
		log(LogType.EXTENTANDCONSOLE, LAValues);

		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType"))
		.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabNameGEP"));
		String abbreviatedDisplayDescription = gepWebDescPage.getAbbreviatedDisplayDescription();
		log(LogType.EXTENTANDCONSOLE, abbreviatedDisplayDescription + "");
		a.assertEquals((LAValues) , abbreviatedDisplayDescription);
		pimHomepage.clickLogoutButton();
	}

	//MDM-758-021 (US) WRFL_109
	@PimFrameworkAnnotation(module = Modules.GEP_WEB_DESCRIPTION, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.GEPWebDescription)
	@Test(priority = 0,description = "WRFL_109 US | Verify if Look Ahead Search Description field gets formed out of concatenation between item and brand", 
	dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"US","WEB_DESCRIPTION"})
	public void chekingLookAheadSearchDescriptionFormationIsFollowingIDQDescRuleForUS(Map<String, String> map) throws InterruptedException, IOException {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("itemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabNameLA"));
		BasePage.WaitForMiliSec(2000);
		
		productDetailSearchPage.maximizeProductDetailTab();
		String LAValues = gepWebDescPage.getLAValuesForFullDisplayDescAndOthers(map.get("LANamesList"));
		productDetailSearchPage.minimizeProductDetailTab();
		log(LogType.EXTENTANDCONSOLE, LAValues);

		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType"))
		.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabNameGEP"));
		String lookAheadSearchDescription = gepWebDescPage.getLookAheadSearchDescription();
		log(LogType.EXTENTANDCONSOLE, lookAheadSearchDescription + "");
		a.assertEquals(LAValues , (lookAheadSearchDescription+" "));
		pimHomepage.clickLogoutButton();
	}
	
	//MDM-758-021 (CA)
	@PimFrameworkAnnotation(module = Modules.GEP_WEB_DESCRIPTION, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.GEPWebDescription)
	@Test(priority = 0,description = "WRFL_109 CA | Verify if Look Ahead Search Description field gets formed out of concatenation between item and brand", 
	dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"US","WEB_DESCRIPTION"})
	public void chekingLookAheadSearchDescriptionFormationIsFollowingIDQDescRuleForCA(Map<String, String> map) throws InterruptedException, IOException {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("itemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabNameLA"));
		BasePage.WaitForMiliSec(2000);
		
		productDetailSearchPage.maximizeProductDetailTab();
		String LAValues = gepWebDescPage.getLAValuesForFullDisplayDescAndOthers(map.get("LANamesList"));
		productDetailSearchPage.minimizeProductDetailTab();
		log(LogType.EXTENTANDCONSOLE, LAValues);

		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType"))
		.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabNameGEP"));
		String lookAheadSearchDescription = gepWebDescPage.getLookAheadSearchDescription();
		log(LogType.EXTENTANDCONSOLE, lookAheadSearchDescription + "");
		a.assertEquals(LAValues , (lookAheadSearchDescription+" "));
		pimHomepage.clickLogoutButton();
	}
}