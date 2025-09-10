package com.pim.tests.JsonVerificationPreRequsite;

import com.pim.annotations.PimFrameworkAnnotation;
import com.pim.annotations.TestDataSheet;
import com.pim.enums.CategoryType;
import com.pim.enums.Modules;
import com.pim.enums.TestCaseSheet;
import com.pim.pages.*;
import com.pim.tests.BaseTest;
import com.pim.utils.DataProviderUtils;
import com.pim.utils.ExcelUtils;
import com.pim.utils.FileUtils;
import com.pim.utils.Javautils;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;
import org.testng.asserts.Assertion;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class JsonVerificationPreRequsite extends BaseTest {
    Assertion a = new Assertion();
    GEP_WebDescriptionPage gepWebDescPage = new GEP_WebDescriptionPage();
    LocalAttributePage localAttPage = new LocalAttributePage();
    BasePage basePage = new BasePage();
    ProductDetailSearchPage productDetailSearchPage = new ProductDetailSearchPage();
    QueriesSubMenu queriesSubMenu = new QueriesSubMenu();
    QualityStatusPage qualityStatusPage = new QualityStatusPage();



    //JSON Verification Product Notes (US) Pre Requisite TC
    @PimFrameworkAnnotation(module = Modules.GEP_WEB_DESCRIPTION, category = CategoryType.REGRESSION)
    @TestDataSheet(sheetname = TestCaseSheet.Json_Verification_Prerequsite)
    @Test(priority = 0,description = "WRFL_110 US | Verify if below GEP Web Description fields are editable, Full Display Description, Local Attribute Update for PRODUCT NOTES US ",
            dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"US","WEB_DESCRIPTION"})
    public void verify_Product_Notes_JSON_Verification_US(Map<String, String> map) throws InterruptedException, IOException {

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


    //JSON Verification Product Notes (CA) Pre Requisite TC
    @PimFrameworkAnnotation(module = Modules.GEP_WEB_DESCRIPTION, category = CategoryType.REGRESSION)
    @TestDataSheet(sheetname = TestCaseSheet.Json_Verification_Prerequsite)
    @Test(priority = 0,description = "WRFL_110 CA | Verify if below GEP Web Description fields are editable, Full Display Description, Local Attribute Update for PRODUCT NOTES CA ",
            dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"CA","WEB_DESCRIPTION"})
    public void verify_Product_Notes_JSON_Verification_CA(Map<String, String> map) throws InterruptedException, IOException {
        FileUtils.storeCurrentTimeInJsonTimesPropertiesFile(map.get("TestCaseName")+map.get("itemNumber"));

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



}
