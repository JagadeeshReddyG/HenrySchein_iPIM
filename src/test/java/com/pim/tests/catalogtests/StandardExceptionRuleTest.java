package com.pim.tests.catalogtests;

import com.pim.annotations.PimFrameworkAnnotation;
import com.pim.annotations.TestDataSheet;
import com.pim.enums.CategoryType;
import com.pim.enums.LogType;
import com.pim.enums.Modules;
import com.pim.enums.TestCaseSheet;
import com.pim.pages.AllCatalogsPage;
import com.pim.pages.BasePage;
import com.pim.pages.CatalogTypePage;
import com.pim.pages.GlobalAttributePage;
import com.pim.pages.LoginPage;
import com.pim.pages.PimHomepage;
import com.pim.pages.ProductDetailSearchPage;
import com.pim.pages.QualityStatusPage;
import com.pim.pages.QueriesSubMenu;
import com.pim.tests.BaseTest;
import com.pim.utils.DataProviderUtils;
import com.pim.utils.ExcelUtils;
import com.pim.utils.Javautils;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.pim.reports.FrameworkLogger.log;

public class StandardExceptionRuleTest extends BaseTest {

    ProductDetailSearchPage productdetail = new ProductDetailSearchPage();
    CatalogTypePage catalogTypePage = new CatalogTypePage();
    QualityStatusPage qualitystatus = new QualityStatusPage();
    AllCatalogsPage allCatalogsPage=new AllCatalogsPage();
    QueriesSubMenu queriessubmenu = new QueriesSubMenu();
    GlobalAttributePage globalattribute = new GlobalAttributePage();
    Javautils javautil = new Javautils();
    List<String> special_market_user_driven;
    List<String> special_market_rule_driven;
    List<String> special_market_catalog_list;
    List<String> dental_user_driven;
    List<String> dental_rule_driven;
    Javautils javautils = new Javautils();

    //CATR_205
    @PimFrameworkAnnotation(module = Modules.SPECIAL_MARKET_CATALOG, category = CategoryType.REGRESSION)
    @TestDataSheet(sheetname = TestCaseSheet.CopyRule)
    @Test(description = "CATR_205 | Verification Of Standard Exception Rule In Special Markets Divisions", groups = {"US","pim","SPECIAL_MARKET_CATALOG"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
    public void verificationOfStandardExceptionRuleInSMCatalog(Map<String, String> map) {
        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("Special Markets User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("Special Markets User").get("Password")).clickLoginButton();

        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
        log(LogType.INFO, "Navigate to Global Attribute Tab to verify Manufacture Code");

        //verifying manufacture code
        String manufacturecode = globalattribute.getManufacturercode();
        Assertions.assertThat(manufacturecode).isNotEqualTo(map.get("ManufactureCode"));

        List<String> sm_userdriven_list = javautil.readMultipleValuesFromExcel(map.get("userDriven"));
        productdetail.selectTabfromDropdown(map.get("TabName"));
        log(LogType.INFO, "Navigate to Special Markets Catalog Tab to assign FEDDEALER and FEDERAL catalog in Parent");

        //assigning feeddealer in special markets catalog
        catalogTypePage.clearUserDriven(map.get("userDriven"));
		BasePage.WaitForMiliSec(3000);
		System.out.println(sm_userdriven_list.get(0));
        catalogTypePage.clickUserDriven().selectUserDriven(sm_userdriven_list.get(0));
        pimHomepage.productDetailSearchPage().clickRefreshIcon();
		BasePage.WaitForMiliSec(60000);
        pimHomepage.productDetailSearchPage().clickRefreshIcon();
		BasePage.WaitForMiliSec(60000);
        pimHomepage.productDetailSearchPage().clickRefreshIcon();
		BasePage.WaitForMiliSec(60000);
        pimHomepage.productDetailSearchPage().clickRefreshIcon();
		BasePage.WaitForMiliSec(60000);
        pimHomepage.productDetailSearchPage().clickRefreshIcon();
        special_market_user_driven = catalogTypePage.getUserDrivenCatalogs();
        pimHomepage.productDetailSearchPage().clickRefreshIcon().clickRefreshIcon();
        Assertions.assertThat(special_market_user_driven).doesNotContain(sm_userdriven_list.get(0));
        log(LogType.INFO, "Verifying FEDDEALER is not added");

        //assigning federal in special markets catalog
        catalogTypePage.clearUserDriven(map.get("userDriven"));
		BasePage.WaitForMiliSec(3000);
        catalogTypePage.clickUserDriven().selectUserDriven(sm_userdriven_list.get(1));
        pimHomepage.productDetailSearchPage().clickRefreshIcon();
		BasePage.WaitForMiliSec(60000);
        pimHomepage.productDetailSearchPage().clickRefreshIcon();
		BasePage.WaitForMiliSec(60000);
        pimHomepage.productDetailSearchPage().clickRefreshIcon();
		BasePage.WaitForMiliSec(60000);
        pimHomepage.productDetailSearchPage().clickRefreshIcon();
		BasePage.WaitForMiliSec(60000);
        pimHomepage.productDetailSearchPage().clickRefreshIcon();
        special_market_user_driven = catalogTypePage.getUserDrivenCatalogs();
        pimHomepage.productDetailSearchPage().clickRefreshIcon();
        Assertions.assertThat(special_market_user_driven).doesNotContain(sm_userdriven_list.get(1));
        log(LogType.INFO, "Verifying FEDERAL is not added.");

      //verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
		for (String Rules:javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule")))
		{
			Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);	
		}
		qualitystatus.minimizeQualityStatusTab();
		log(LogType.INFO,"Verifying catalog entity rule");
		 

        //verify in master
        productdetail.clickMenuRefreshIcon();
        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
        log(LogType.INFO, "Navigate to Global attribute tab to verify the Manufacture code in Master Catalog");

        //verifying manufacture code
        manufacturecode = globalattribute.getManufacturercode();
        Assertions.assertThat(manufacturecode).isNotEqualTo(map.get("ManufactureCode"));

        productdetail.selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
        List<String> masterUserdriven = catalogTypePage.getUserDrivenCatalogs();
        Assertions.assertThat(masterUserdriven).doesNotContain(sm_userdriven_list.get(0));
        Assertions.assertThat(masterUserdriven).doesNotContain(sm_userdriven_list.get(1));
        pimHomepage.clickLogoutButton();
    }

    //CATR_206
    @PimFrameworkAnnotation(module = Modules.SPECIAL_MARKET_CATALOG, category = CategoryType.REGRESSION)
    @TestDataSheet(sheetname = TestCaseSheet.CopyRule)
    @Test(description = "CATR_206 |Verification Of copy rule In Special Markets Divisions", groups = {"US","pim","SPECIAL_MARKET_CATALOG"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
    public void verificationOfCopyRuleInSMCatalog(Map<String, String> map) {
        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("Special Markets User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("Special Markets User").get("Password")).clickLoginButton();

        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
        log(LogType.INFO, "Navigate to Global Attribute Tab to verify Manufacture Code");

        //verifying manufacture code
        String manufacturecode = globalattribute.getManufacturercode();
        Assertions.assertThat(manufacturecode).isEqualTo(map.get("ManufactureCode"));

        productdetail.selectTabfromDropdown(map.get("TabName"));
        log(LogType.INFO, "Navigate to Special Markets Catalog Tab to assign FEDERAL catalog");

        //assigning federal in special markets catalog
        catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
        pimHomepage.productDetailSearchPage().clickRefreshIcon();
        BasePage.WaitForMiliSec(15000);
        pimHomepage.productDetailSearchPage().clickRefreshIcon();
        special_market_user_driven = catalogTypePage.getUserDrivenCatalogs();
        special_market_rule_driven = catalogTypePage.getRuleDrivenCatalogs();
        Assertions.assertThat(special_market_user_driven).doesNotContain(map.get("userDriven"));
        Assertions.assertThat(special_market_rule_driven).doesNotContain(map.get("userDriven"));
        log(LogType.INFO, "Verifying FEDDEALER is not added");

        productdetail.selectTabfromDropdown(map.get("AllCatalogsTab"));
        log(LogType.INFO, "Navigate to All Catalog Tab to verify the Special Markets list");

        special_market_catalog_list = allCatalogsPage.getAllSpecialMarketsCatalogList();
        Assertions.assertThat(special_market_catalog_list).doesNotContain(map.get("userDriven"));

        //verify in master
        productdetail.clickMenuRefreshIcon();
        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
        log(LogType.INFO, "Navigate to Global attribute tab to verify the Manufacture code in Master Catalog");

        //verifying manufacture code
        manufacturecode = globalattribute.getManufacturercode();
        Assertions.assertThat(manufacturecode).isEqualTo(map.get("ManufactureCode"));

        productdetail.selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
        List<String> masterUserdriven = catalogTypePage.getUserDrivenCatalogs();
        List<String> masterRuledriven = catalogTypePage.getRuleDrivenCatalogs();
        Assertions.assertThat(masterUserdriven).doesNotContain(map.get("userDriven"));
        Assertions.assertThat(masterRuledriven).doesNotContain(map.get("userDriven"));
        pimHomepage.clickLogoutButton();
    }

    //CATR_207
    @PimFrameworkAnnotation(module = Modules.SPECIAL_MARKET_CATALOG, category = CategoryType.REGRESSION)
    @TestDataSheet(sheetname = TestCaseSheet.CopyRule)
    @Test(description = "CATR_207 | Verification Of copy rule In Dental Divisions", groups = {"US","pim","SPECIAL_MARKET_CATALOG"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
    public void verificationOfCopyRuleInDentalCatalog(Map<String, String> map) {
        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("Dental User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("Dental User").get("Password")).clickLoginButton();

        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
        log(LogType.INFO, "Navigate to Global Attribute Tab to verify Manufacture Code");

        //verifying manufacture code
        String manufacturecode = globalattribute.getManufacturercode();
        Assertions.assertThat(manufacturecode).isNotEqualTo(map.get("ManufactureCode"));

        productdetail.selectTabfromDropdown(map.get("TabName"));
        log(LogType.INFO, "Navigate to Dental Catalog Tab to assign FEDERAL/FEDDEALER catalog");

        //verifying federal/feddealer in userdriven dropdown values
        catalogTypePage.clickUserDriven();
        List<String> user_driven_dropdown_values = catalogTypePage.getUserDrivenCatalogsDropdownValues();
        List<String> myList = new ArrayList<String>(Arrays.asList(map.get("userDriven").split(",")));
        Assertions.assertThat(user_driven_dropdown_values).doesNotContainAnyElementsOf(myList);
        log(LogType.INFO, "Verifying user driven drop down values");
        pimHomepage.clickLogoutButton();
    }
}
