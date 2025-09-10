package com.pim.tests.ItemPublishTests;

import com.pim.annotations.PimFrameworkAnnotation;
import com.pim.annotations.TestDataSheet;
import com.pim.driver.DriverManager;
import com.pim.enums.*;
import com.pim.pages.BasePage;
import com.pim.pages.CatalogTypePage;
import com.pim.pages.LoginPage;
import com.pim.pages.PimHomepage;
import com.pim.pages.ProductDetailSearchPage;
import com.pim.pages.QualityStatusPage;
import com.pim.pages.QueriesSubMenu;
import com.pim.pages.StructureSubMenu;
import com.pim.pages.us.CreateItemValidationPage;
import com.pim.pages.us.ItemPublishDateAndFlagPage;
import com.pim.tests.BaseTest;
import com.pim.utils.ApplicationUtils;
import com.pim.utils.DataProviderUtils;
import com.pim.utils.DateandTimeUtils;
import com.pim.utils.ExcelUtils;
import com.pim.utils.Javautils;

import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import java.awt.AWTException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.pim.reports.FrameworkLogger.log;

public class ItemPublishFlagAndDateTests extends BaseTest {

    CatalogTypePage catalogTypePage = new CatalogTypePage();
    QualityStatusPage qualitystatus = new QualityStatusPage();
    ItemPublishDateAndFlagPage itemPublish = new ItemPublishDateAndFlagPage();
    CreateItemValidationPage itemValidationPage = new CreateItemValidationPage();
    LoginPage loginPage = new LoginPage();
    Javautils javautils=new Javautils();
    ProductDetailSearchPage productDetailSearchPage = new ProductDetailSearchPage();
    StructureSubMenu structuresubmenu=new StructureSubMenu();
    PimHomepage pimHomepage = new PimHomepage();
    

    @PimFrameworkAnnotation(module = Modules.DENTAL_CATALOG, category = CategoryType.REGRESSION)
    @TestDataSheet(sheetname = TestCaseSheet.ItemPublishTestData)
    @Test(description = "ITP_001 | validate Item loaded from PIX For Dental Division", dataProvider = "getCatalogData",
    	groups = {"SMOKE_PIX","US","pim","DENTAL_CATALOG"}, dataProviderClass = DataProviderUtils.class)
    public void verifyItemLoadedFromPixTestsForDentalDivision(Map<String, String> map) {
        loginPage.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"));
        loginPage.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password"));
        loginPage.clickLoginButton();
        pimHomepage.mainMenu().clickCatalogsMenu().selectCatalogType(map.get("CatalogType")).clickOnFirstResult().applyDateFilterInSearchPageByAnyValue(map.get("ItemCreationDate"),map.get("Action"));
		structuresubmenu.selectSpecificItem(map.get("specificItemCode"));
        BasePage.WaitForMiliSec(30000);
        productDetailSearchPage.selectTabfromDropdown(map.get("TabName"));      
        BasePage.WaitForMiliSec(10000);
        String userDrivenSpecified = itemPublish.captureUserDrivenValue();
        Assertions.assertThat(userDrivenSpecified).isEqualTo(map.get("userDriven"));
        String publishFlagSpecified = itemPublish.capturePublishFlagValue();
        Assertions.assertThat(publishFlagSpecified).isEqualTo(map.get("publishFlag"));
        String publishdate= itemPublish.capturePublishDateValue();
        Assertions.assertThat(publishdate).isEqualTo(itemPublish.capturePublishDateValue());
        productDetailSearchPage.selectTabfromDropdown(map.get("SecondTab"));
        BasePage.WaitForMiliSec(3000);
        Assertions.assertThat(publishFlagSpecified).isEqualTo(map.get("publishFlag"));
        Assertions.assertThat(publishdate).isEqualTo(itemPublish.capturePublishDateValue());
        productDetailSearchPage.selectTabfromDropdown(map.get("ThirdTab"));
        BasePage.WaitForMiliSec(3000);
        Assertions.assertThat(publishFlagSpecified).isEqualTo(map.get("publishFlag"));
        Assertions.assertThat(publishdate).isEqualTo(itemPublish.capturePublishDateValue());
        productDetailSearchPage.selectTabfromDropdown(map.get("FourthTab"));
        BasePage.WaitForMiliSec(3000);
        Assertions.assertThat(publishFlagSpecified).isEqualTo(map.get("publishFlag"));
        Assertions.assertThat(publishdate).isEqualTo(itemPublish.capturePublishDateValue());


    }

    @PimFrameworkAnnotation(module = Modules.MEDICAL_CATALOG, category = CategoryType.REGRESSION)
    @TestDataSheet(sheetname = TestCaseSheet.ItemPublishTestData)
    @Test(description = "ITP_002 | validate Item loaded from PIX For Medical Division", dataProvider = "getCatalogData", 
    	groups = {"SMOKE_PIX","US","pim","MEDICAL_CATALOG"},dataProviderClass = DataProviderUtils.class)
    public void verifyItemLoadedFromPixTestsForMedicalDivision(Map<String, String> map) {
        loginPage.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"));
        loginPage.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password"));
        loginPage.clickLoginButton();
        pimHomepage.mainMenu().clickCatalogsMenu().selectCatalogType(map.get("CatalogType")).clickOnFirstResult().applyDateFilterInSearchPageByAnyValue(map.get("ItemCreationDate"),map.get("Action"));
		structuresubmenu.selectSpecificItem(map.get("specificItemCode"));
        BasePage.WaitForMiliSec(30000);
        productDetailSearchPage.selectTabfromDropdown(map.get("TabName"));  
        BasePage.WaitForMiliSec(10000);
        String userDrivenSpecified = itemPublish.captureUserDrivenValue();
        Assertions.assertThat(userDrivenSpecified).isEqualTo(map.get("userDriven"));
        String publishFlagSpecified = itemPublish.capturePublishFlagValue();
        Assertions.assertThat(publishFlagSpecified).isEqualTo(map.get("publishFlag"));
        String publishdate= itemPublish.capturePublishDateValue();
        Assertions.assertThat(publishdate).isEqualTo(itemPublish.capturePublishDateValue());
        productDetailSearchPage.selectTabfromDropdown(map.get("SecondTab"));
        BasePage.WaitForMiliSec(3000);
        Assertions.assertThat(publishFlagSpecified).isEqualTo(map.get("publishFlag"));
        Assertions.assertThat(publishdate).isEqualTo(itemPublish.capturePublishDateValue());
        productDetailSearchPage.selectTabfromDropdown(map.get("ThirdTab"));
        BasePage.WaitForMiliSec(3000);
        Assertions.assertThat(publishFlagSpecified).isEqualTo(map.get("publishFlag"));
        Assertions.assertThat(publishdate).isEqualTo(itemPublish.capturePublishDateValue());
        productDetailSearchPage.selectTabfromDropdown(map.get("FourthTab"));
        BasePage.WaitForMiliSec(3000);
        Assertions.assertThat(publishFlagSpecified).isEqualTo(map.get("publishFlag"));
        Assertions.assertThat(publishdate).isEqualTo(itemPublish.capturePublishDateValue());
    }

    @PimFrameworkAnnotation(module = Modules.ZAHN_CATALOG, category = CategoryType.Exceptionally_Passed)
    @TestDataSheet(sheetname = TestCaseSheet.ItemPublishTestData)
    @Test(description = "ITP_003 | validate Item loaded from PIX For Zahn Division", dataProvider = "getCatalogData", groups = {"US","pim","ZAHN_CATALOG"},dataProviderClass = DataProviderUtils.class)
    public void verifyItemLoadedFromPixTestsForZahnDivision(Map<String, String> map) {
        loginPage.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"));
        loginPage.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password"));
        loginPage.clickLoginButton();
        pimHomepage.mainMenu().clickCatalogsMenu().selectCatalogType(map.get("CatalogType")).clickOnFirstResult().applyDateFilterInSearchPageByAnyValue(map.get("ItemCreationDate"),map.get("Action"));
		structuresubmenu.selectSpecificItem(map.get("specificItemCode"));
        BasePage.WaitForMiliSec(30000);
        productDetailSearchPage.selectTabfromDropdown(map.get("TabName")); 
        BasePage.WaitForMiliSec(10000);
        String userDrivenSpecified = itemPublish.captureUserDrivenValue();
        Assertions.assertThat(userDrivenSpecified).isEqualTo(map.get("userDriven"));
        String publishFlagSpecified = itemPublish.capturePublishFlagValue();
        Assertions.assertThat(publishFlagSpecified).isEqualTo(map.get("publishFlag"));
        String publishdate= itemPublish.capturePublishDateValue();
        Assertions.assertThat(publishdate).isEqualTo(itemPublish.capturePublishDateValue());
        productDetailSearchPage.selectTabfromDropdown(map.get("SecondTab"));
        BasePage.WaitForMiliSec(3000);
        Assertions.assertThat(publishFlagSpecified).isEqualTo(map.get("publishFlag"));
        Assertions.assertThat(publishdate).isEqualTo(itemPublish.capturePublishDateValue());
        productDetailSearchPage.selectTabfromDropdown(map.get("ThirdTab"));
        BasePage.WaitForMiliSec(3000);
        Assertions.assertThat(publishFlagSpecified).isEqualTo(map.get("publishFlag"));
        Assertions.assertThat(publishdate).isEqualTo(itemPublish.capturePublishDateValue());
        productDetailSearchPage.selectTabfromDropdown(map.get("FourthTab"));
        BasePage.WaitForMiliSec(3000);
        Assertions.assertThat(publishFlagSpecified).isEqualTo(map.get("publishFlag"));
        Assertions.assertThat(publishdate).isEqualTo(itemPublish.capturePublishDateValue());
    }

    @PimFrameworkAnnotation(module = Modules.SPECIAL_MARKET_CATALOG, category = CategoryType.Valid_Failure)
    @TestDataSheet(sheetname = TestCaseSheet.ItemPublishTestData)
    @Test(description = "ITP_004 | validate Item loaded from PIX For SpecialMarkets Division", dataProvider = "getCatalogData",groups = {"US","pim","SPECIAL_MARKET_CATALOG"}, dataProviderClass = DataProviderUtils.class)
    public void verifyItemLoadedFromPixTestsForSpecialMarketsDivision(Map<String, String> map) {
        loginPage.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"));
        loginPage.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password"));
        loginPage.clickLoginButton();
        pimHomepage.mainMenu().clickCatalogsMenu().selectCatalogType(map.get("CatalogType")).clickOnFirstResult().applyDateFilterInSearchPageByAnyValue(map.get("ItemCreationDate"),map.get("Action"));
		structuresubmenu.selectSpecificItem(map.get("specificItemCode"));
        BasePage.WaitForMiliSec(30000);
        productDetailSearchPage.selectTabfromDropdown(map.get("TabName")); 
        BasePage.WaitForMiliSec(10000);
        String userDrivenSpecified = itemPublish.captureUserDrivenValue();
        Assertions.assertThat(userDrivenSpecified).isEqualTo(map.get("userDriven"));
        String publishFlagSpecified = itemPublish.capturePublishFlagValue();
        Assertions.assertThat(publishFlagSpecified).isEqualTo(map.get("publishFlag"));
        String publishdate= itemPublish.capturePublishDateValue();
        Assertions.assertThat(publishdate).isEqualTo(itemPublish.capturePublishDateValue());
        productDetailSearchPage.selectTabfromDropdown(map.get("SecondTab"));
        BasePage.WaitForMiliSec(3000);
        Assertions.assertThat(publishFlagSpecified).isEqualTo(map.get("publishFlag"));
        Assertions.assertThat(publishdate).isEqualTo(itemPublish.capturePublishDateValue());
        productDetailSearchPage.selectTabfromDropdown(map.get("ThirdTab"));
        BasePage.WaitForMiliSec(3000);
        Assertions.assertThat(publishFlagSpecified).isEqualTo(map.get("publishFlag"));
        Assertions.assertThat(publishdate).isEqualTo(itemPublish.capturePublishDateValue());
        productDetailSearchPage.selectTabfromDropdown(map.get("FourthTab"));
        BasePage.WaitForMiliSec(3000);
        Assertions.assertThat(publishFlagSpecified).isEqualTo(map.get("publishFlag"));
        Assertions.assertThat(publishdate).isEqualTo(itemPublish.capturePublishDateValue());


    }
    

    @PimFrameworkAnnotation(module = Modules.DENTAL_CATALOG, category = CategoryType.REGRESSION)
    @TestDataSheet(sheetname = TestCaseSheet.ItemPublishTestData)
    @Test(description = "ITP_040 | Verify publish flag defaults to Y and publish date as current date if catalog unassigned in PIX on item creation for Dental Division",groups = {"US","pim","DENTAL_CATALOG"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
    public void verify_PublishFlag_Defaults_PublishDate_If_Catalog_Unassigned_loadedFromPixTests_In_Dental_Catalog(Map<String, String> map) {

        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("Dental User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("Dental User").get("Password")).clickLoginButton();
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
        log(LogType.INFO,"Navigate to Dental Catalog tab from Queries Menu for US Catalog");
        BasePage.WaitForMiliSec(3000);
        catalogTypePage.selectPublishDate(DateandTimeUtils.getTodaysDate()).selectPublishFlag();
        log(LogType.INFO,"Change publish date to current date and publish flag");
        String publishdate = catalogTypePage.getPublishDate();
        String publishflag = catalogTypePage.getPublishFlag();

        //verify quality status
        pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
        
		qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).maximizeQualityStatusTab();
		List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("Status"));
		for (String Rules:javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule")))
		{
			Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);	
		}
		qualitystatus.minimizeQualityStatusTab();
		log(LogType.INFO,"Verifying workflow rule");
      
            
        pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog"))
                .clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon().expandTab();
        log(LogType.INFO,"Navigate to Master Catalog");
        Assertions.assertThat(publishdate).isEqualTo(catalogTypePage.getPublishDate());
        Assertions.assertThat(publishflag).isEqualTo(catalogTypePage.getPublishFlag());

        pimHomepage.clickLogoutButton();

    }

    @PimFrameworkAnnotation(module = Modules.MEDICAL_CATALOG, category = CategoryType.REGRESSION)
    @TestDataSheet(sheetname = TestCaseSheet.ItemPublishTestData)
    @Test(description = "ITP_041 | Verify publish flag defaults to Y and publish date as current date if catalog unassigned in PIX on item creation for Medical Division",groups = {"US","pim","MEDICAL_CATALOG"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
    public void verify_PublishFlag_Defaults_PublishDate_If_Catalog_Unassigned_loadedFromPixTests_In_Medical_Catalog(Map<String, String> map) {

        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
        log(LogType.INFO,"Navigate to Dental Catalog tab from Queries Menu for US Catalog");
        BasePage.WaitForMiliSec(3000);
        catalogTypePage.selectPublishDate(DateandTimeUtils.getTodaysDate()).selectPublishFlag();
        log(LogType.INFO,"Change publish date to current date and publish flag");
        String publishdate = catalogTypePage.getPublishDate();
        String publishflag = catalogTypePage.getPublishFlag();

        //verify quality status
        pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
        qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).maximizeQualityStatusTab();
		List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("Status"));
		for (String Rules:javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule")))
		{
			Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);	
		}
		qualitystatus.minimizeQualityStatusTab();
		log(LogType.INFO,"Verifying workflow rule");

        pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog"))
                .clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon().expandTab();
        log(LogType.INFO,"Navigate to Master Catalog");
        Assertions.assertThat(publishdate).isEqualTo(catalogTypePage.getPublishDate());
        Assertions.assertThat(publishflag).isEqualTo(catalogTypePage.getPublishFlag());

        pimHomepage.clickLogoutButton();

    }

    @PimFrameworkAnnotation(module = Modules.ZAHN_CATALOG, category = CategoryType.REGRESSION)
    @TestDataSheet(sheetname = TestCaseSheet.ItemPublishTestData)
    @Test(description = "ITP_042 | Verify publish flag defaults to Y and publish date as current date if catalog unassigned in PIX on item creation for Zahn Division", groups = {"US","ZAHN_CATALOG","pim"},dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
    public void verify_PublishFlag_Defaults_PublishDate_If_Catalog_Unassigned_loadedFromPixTests_In_Zahn_Catalog(Map<String, String> map) {

        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("Zahn User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("Zahn User").get("Password")).clickLoginButton();
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
        log(LogType.INFO,"Navigate to Dental Catalog tab from Queries Menu for US Catalog");
        BasePage.WaitForMiliSec(3000);
        catalogTypePage.selectPublishDate(DateandTimeUtils.getTodaysDate()).selectPublishFlag();
        log(LogType.INFO,"Change publish date to current date and publish flag");
        String publishdate = catalogTypePage.getPublishDate();
        String publishflag = catalogTypePage.getPublishFlag();

        //verify quality status
        pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
        qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).maximizeQualityStatusTab();
		List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("Status"));
		for (String Rules:javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule")))
		{
			Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);	
		}
		qualitystatus.minimizeQualityStatusTab();
		log(LogType.INFO,"Verifying workflow rule");

        pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog"))
                .clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon().expandTab();
        log(LogType.INFO,"Navigate to Master Catalog");
        Assertions.assertThat(publishdate).isEqualTo(catalogTypePage.getPublishDate());
        Assertions.assertThat(publishflag).isEqualTo(catalogTypePage.getPublishFlag());

        pimHomepage.clickLogoutButton();

    }

    @PimFrameworkAnnotation(module = Modules.SPECIAL_MARKET_CATALOG, category = CategoryType.REGRESSION)
    @TestDataSheet(sheetname = TestCaseSheet.ItemPublishTestData)
    @Test(description = "ITP_043 | Verify publish flag defaults to Y and publish date as current date if catalog unassigned in PIX on item creation for SpecialMarkets Division",groups = {"US","pim","SPECIAL_MARKET_CATALOG"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
    public void verify_PublishFlag_Defaults_PublishDate_If_Catalog_Unassigned_loadedFromPixTests_In_SpecialMarkets_Catalog(Map<String, String> map) {

        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("Special Markets User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("Special Markets User").get("Password")).clickLoginButton();
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
        log(LogType.INFO,"Navigate to Dental Catalog tab from Queries Menu for US Catalog");
        BasePage.WaitForMiliSec(3000);
        catalogTypePage.selectPublishDate(DateandTimeUtils.getTodaysDate()).selectPublishFlag();
        log(LogType.INFO,"Change publish date to current date and publish flag");
        String publishdate = catalogTypePage.getPublishDate();
        String publishflag = catalogTypePage.getPublishFlag();

        //verify quality status
        pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
        qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).maximizeQualityStatusTab();
		List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("Status"));
		for (String Rules:javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule")))
		{
			Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);	
		}
		qualitystatus.minimizeQualityStatusTab();
		log(LogType.INFO,"Verifying workflow rule");

        pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog"))
                .clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon().expandTab();
        log(LogType.INFO,"Navigate to Master Catalog");
        Assertions.assertThat(publishdate).isEqualTo(catalogTypePage.getPublishDate());
        Assertions.assertThat(publishflag).isEqualTo(catalogTypePage.getPublishFlag());

        pimHomepage.clickLogoutButton();

    }
    @PimFrameworkAnnotation(module = Modules.DENTAL_CATALOG, category = CategoryType.REGRESSION)
    @TestDataSheet(sheetname = TestCaseSheet.ItemPublishTestData)
    @Test(description = "ITP_044 | Verify publish flag defaults to Y and publish date as current date ,if catalogs which are not rule driven is added with publish date not mapped in PIX for CA Dental Division",groups = {"CA","pim","DENTAL_CATALOG"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
    public void verify_PublishFlag_Defaults_PublishDate_CurrentDate_If_Catalog_Not_Added_RuleDriven_For_CA_Dental_Division(Map<String, String> map) {
        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
        log(LogType.INFO,"Navigate to Dental Catalog tab from Queries Menu for CA Catalog");
        BasePage.WaitForMiliSec(3000);
        DriverManager.getDriver().findElement(ItemPublishDateAndFlagPage.userDrivenfield).isDisplayed();
     

        catalogTypePage.selectPublishDate(DateandTimeUtils.getTodaysDate()).selectPublishFlag();
        log(LogType.INFO,"Change publish date to current date and publish flag");
        String publishdate = catalogTypePage.getPublishDate();
        String publishflag = catalogTypePage.getPublishFlag();

      //verify quality status
        pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
        qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).maximizeQualityStatusTab();
		List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("Status"));
		for (String Rules:javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule")))
		{
			Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);	
		}
		qualitystatus.minimizeQualityStatusTab();
		log(LogType.INFO,"Verifying workflow rule");

        pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog"))
                .clickSeachButton().clickOnSecondResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon().expandTab();
        log(LogType.INFO,"Navigate to Master Catalog");
        Assertions.assertThat(publishdate).isEqualTo(catalogTypePage.getPublishDate());
        Assertions.assertThat(publishflag).isEqualTo(catalogTypePage.getPublishFlag());

        pimHomepage.clickLogoutButton();

    }

    @PimFrameworkAnnotation(module = Modules.MEDICAL_CATALOG, category = CategoryType.REGRESSION)
    @TestDataSheet(sheetname = TestCaseSheet.ItemPublishTestData)
    @Test(description = "ITP_045 | Verify publish flag defaults to Y and publish date as current date ,if catalogs which are not rule driven is added with publish date not mapped in PIX for CA Medical Division", groups = {"CA","pim","MEDICAL_CATALOG"},dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
    public void verify_PublishFlag_Defaults_PublishDate_CurrentDate_If_Catalog_Not_Added_RuleDriven_For_CA_Medical_Division(Map<String, String> map) {
        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
        log(LogType.INFO,"Navigate to Dental Catalog tab from Queries Menu for CA Catalog");
        BasePage.WaitForMiliSec(3000);
        DriverManager.getDriver().findElement(ItemPublishDateAndFlagPage.userDrivenfield).isDisplayed();
        

        catalogTypePage.selectPublishDate(DateandTimeUtils.getTodaysDate()).selectPublishFlag();
        log(LogType.INFO,"Change publish date to current date and publish flag");
        String publishdate = catalogTypePage.getPublishDate();
        String publishflag = catalogTypePage.getPublishFlag();

        //verify quality status
        pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
        qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).maximizeQualityStatusTab();
		List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("Status"));
		for (String Rules:javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule")))
		{
			Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);	
		}
		qualitystatus.minimizeQualityStatusTab();
		log(LogType.INFO,"Verifying workflow rule");

        pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog"))
                .clickSeachButton().clickOnSecondResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon().expandTab();
        log(LogType.INFO,"Navigate to Master Catalog");
        Assertions.assertThat(publishdate).isEqualTo(catalogTypePage.getPublishDate());
        Assertions.assertThat(publishflag).isEqualTo(catalogTypePage.getPublishFlag());

        pimHomepage.clickLogoutButton();

    }


    @PimFrameworkAnnotation(module = Modules.ZAHN_CATALOG, category = CategoryType.REGRESSION)
    @TestDataSheet(sheetname = TestCaseSheet.ItemPublishTestData)
    @Test(description = "ITP_046 | Verify publish flag defaults to Y and publish date as current date ,if catalogs which are not rule driven is added with publish date not mapped in PIX for CA Zahn Division", groups = {"CA","pim","ZAHN_CATALOG"},dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
    public void verify_PublishFlag_Defaults_PublishDate_CurrentDate_If_Catalog_Not_Added_RuleDriven_For_CA_Zahn_Division(Map<String, String> map) {
        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
        log(LogType.INFO,"Navigate to Dental Catalog tab from Queries Menu for CA Catalog");
        BasePage.WaitForMiliSec(3000);
        DriverManager.getDriver().findElement(ItemPublishDateAndFlagPage.userDrivenfield).isDisplayed();
       

        catalogTypePage.selectPublishDate(DateandTimeUtils.getTodaysDate()).selectPublishFlag();
        log(LogType.INFO,"Change publish date to current date and publish flag");
        String publishdate = catalogTypePage.getPublishDate();
        String publishflag = catalogTypePage.getPublishFlag();

        //verify quality status
        pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
        qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).maximizeQualityStatusTab();
		List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("Status"));
		for (String Rules:javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule")))
		{
			Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);	
		}
		qualitystatus.minimizeQualityStatusTab();
		log(LogType.INFO,"Verifying workflow rule");
		

        pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog"))
                .clickSeachButton().clickOnSecondResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon().expandTab();
        log(LogType.INFO,"Navigate to Master Catalog");
        Assertions.assertThat(publishdate).isEqualTo(catalogTypePage.getPublishDate());
        Assertions.assertThat(publishflag).isEqualTo(catalogTypePage.getPublishFlag());

        pimHomepage.clickLogoutButton();

    }
    
    // verifying created item in pim for US user
    @PimFrameworkAnnotation(module = Modules.ITEM_CREATION, category = CategoryType.SMOKE)
    @TestDataSheet(sheetname = TestCaseSheet.TestDataPix)
    @Test(description = "For US - Creating item in pix and validating it on PIM and Midleware ", dataProvider = "getCatalogData", 
    dataProviderClass = DataProviderUtils.class, groups = {"Integration-Smoke"}, priority = 2)
    public void verifycreateditemreflectinPIMAndMiddleware(Map<String, String> map) {
        loginPage.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"));
        loginPage.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password"));
        loginPage.clickLoginButton();
        pimHomepage.mainMenu().clickCatalogsMenu().selectCatalogType(map.get("CatalogType")).clickOnFirstResult().applyJDEFilter(map.get("ItemCreationDate"),ApplicationUtils.getJDEDescription(map.get("productName"))).clickOnFirstResult();
		//structuresubmenu.selectSpecificItem(map.get("JDEdescription"));
        BasePage.WaitForMiliSec(3000);
        productDetailSearchPage.selectTabfromDropdown(map.get("TabName"));      
        BasePage.WaitForMiliSec(1000);
        List<String> userdrivencatalogs = catalogTypePage.getUserDrivenCatalogs();
        Assertions.assertThat(userdrivencatalogs).containsAnyOf(map.get("catalogType"));
        
        String publishflag = catalogTypePage.getPublishFlag();
        Assertions.assertThat(publishflag).isEqualTo("Yes");
        
        String publishdate = catalogTypePage.getPublishDate();
        Assertions.assertThat(publishdate).isEqualTo(DateandTimeUtils.getTodaysDateForQualityStatus());
        log(LogType.INFO,"pass");
        
        pimHomepage.mainMenu().clickCatalogsMenu().selectMasterCatalog(map.get("MasterCatalog")).clickFilterButton().clickOnFirstResult();
        BasePage.WaitForMiliSec(3000);
        productDetailSearchPage.selectTabfromDropdown(map.get("TabName"));      
        BasePage.WaitForMiliSec(1000);
        List<String> masteruserdrivencatalogs = catalogTypePage.getUserDrivenCatalogs();
        Assertions.assertThat(masteruserdrivencatalogs).containsAnyOf(map.get("catalogType"));
        log(LogType.INFO,"Publish Flag is Yes");
        
        String masterpublishflag = catalogTypePage.getPublishFlag();
        Assertions.assertThat(masterpublishflag).isEqualTo("Yes");
        log(LogType.INFO,"Publish Flag is Yes");
        
        String masterpublishdate = catalogTypePage.getPublishDate();
        Assertions.assertThat(masterpublishdate).isEqualTo(DateandTimeUtils.getTodaysDateForQualityStatus());
        log(LogType.INFO,"Today's is present");
  
    }
    
    // verifying created item in pim for CA user
    @PimFrameworkAnnotation(module = Modules.ITEM_CREATION, category = CategoryType.SMOKE)
	@TestDataSheet(sheetname = TestCaseSheet.Canada_Catalog_TestData_PIX)
	@Test(description = "For CA - Creating item in pix and validating it on PIM and Midleware ", dataProvider = "getCatalogData", 
	dataProviderClass = DataProviderUtils.class, groups = {"Integration-Smoke"}, priority = 2)
	public void verifying_created_ca_item_in_PIM_and_Middleware(Map<String, String> map) throws InterruptedException, AWTException, IOException {
       loginPage.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"));
        loginPage.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password"));
        loginPage.clickLoginButton();
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType("Business").selectItemCreatedBetweenTwoDates().selectCatalogType(map.get("CatalogType"))
        .assignItemCreationDateGreater(DateandTimeUtils.getSpecifiedDayFromCurrentDay(-1)).assignItemCreationDateLess(DateandTimeUtils.getNextDayDate()).clickSeachButton().clickOnSearchResult(map.get("productName") + " " + DateandTimeUtils.getSpecifiedDayFromCurrentDay(-1));
       // pimHomepage.mainMenu().clickCatalogsMenu().selectCatalogType(map.get("CatalogType")).clickOnFirstResult().applyJDEFilter(map.get("ItemCreationDate"),"Item1 3/10/2022"/*ApplicationUtils.getJDEDescription(map.get("productName"))*/).clickOnFirstResult();
//		structuresubmenu.selectSpecificItem(map.get("JDEdescription"+" "+DateandTimeUtils.getSpecifiedDayFromCurrentDay(-1)));
        BasePage.WaitForMiliSec(3000);
        productDetailSearchPage.selectTabfromDropdown(map.get("TabName"));      
        BasePage.WaitForMiliSec(1000);
        
        String publishflag = catalogTypePage.getPublishFlag();
        Assertions.assertThat(publishflag).isEqualTo("Yes");
        
        String publishdate = catalogTypePage.getPublishDate();
        Assertions.assertThat(publishdate).isEqualTo(DateandTimeUtils.getSpecifiedDayFromCurrentDay(-1));
        log(LogType.INFO,"pass");
        QueriesSubMenu queriesSubMenu = new QueriesSubMenu();
        queriesSubMenu.selectCatalogType(map.get("MasterCatalog")).clickSeachButton().clickOnSearchResult(map.get("productName") + " " + DateandTimeUtils.getSpecifiedDayFromCurrentDay(-1));
        //pimHomepage.mainMenu().clickCatalogsMenu().selectMasterCatalog(map.get("MasterCatalog")).clickFilterButton().clickOnFirstResult();
        BasePage.WaitForMiliSec(3000);
//        productDetailSearchPage.selectTabfromDropdown(map.get("TabName"));      
        BasePage.WaitForMiliSec(1000);
        
        String masterpublishflag = catalogTypePage.getPublishFlag();
        Assertions.assertThat(masterpublishflag).isEqualTo("Yes");
        log(LogType.INFO,"Publish Flag is Yes");
        
        String masterpublishdate = catalogTypePage.getPublishDate();
        Assertions.assertThat(masterpublishdate).isEqualTo(DateandTimeUtils.getSpecifiedDayFromCurrentDay(-1));
        log(LogType.INFO,"Today's is present");
  
    }

}
