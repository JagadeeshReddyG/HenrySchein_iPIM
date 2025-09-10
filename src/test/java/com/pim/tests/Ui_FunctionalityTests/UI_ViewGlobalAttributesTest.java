package com.pim.tests.Ui_FunctionalityTests;
import com.pim.annotations.PimFrameworkAnnotation;
import com.pim.annotations.TestDataSheet;
import com.pim.enums.CategoryType;
import com.pim.enums.LogType;
import com.pim.enums.Modules;
import com.pim.enums.TestCaseSheet;
import com.pim.pages.BasePage;
import com.pim.pages.GlobalAttributePage;
import com.pim.pages.LoginPage;
import com.pim.pages.MainMenu;
import com.pim.pages.PimHomepage;
import com.pim.pages.ProductDetailSearchPage;
import com.pim.pages.StructureSubMenu;
import com.pim.tests.BaseTest;
import com.pim.utils.DataProviderUtils;
import com.pim.utils.ExcelUtils;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;
import static com.pim.reports.FrameworkLogger.log;

import java.util.Map;

public class UI_ViewGlobalAttributesTest extends BaseTest {
    ProductDetailSearchPage productDetailSearchPage = new ProductDetailSearchPage();
    GlobalAttributePage globalAttributePage =new GlobalAttributePage();

    private UI_ViewGlobalAttributesTest(){
    }

    //UI_021
    @PimFrameworkAnnotation(module = Modules.Entities_UI_Func, category = CategoryType.REGRESSION)
    @TestDataSheet(sheetname = TestCaseSheet.Entities_UI_Func_TestData)
    @Test(description ="UI_021 | verify using Dental steward user all global attributes of product are viewable", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
    public void verifyGlobalAttributesForDentalStewardUser(Map<String, String> map) {
    	
        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("Dental User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("Dental User").get("Password")).clickLoginButton();
        pimHomepage.mainMenu().clickStructuresMenu().selectCatalogType(map.get("CatalogType"))
                .selectClassificationType(map.get("CategoryType")).selectItemType(map.get("ItemType"))
                .clickOnSelectedTaxnomyType(map.get("CategoryType")).clickOnfirstSubsetDropdown(map.get("SubsetDropDown"))
                .clickOnShowAll().clickOnFirstItemCode();
        productDetailSearchPage.selectTabfromDropdown(map.get("GlobalAttributeTab"));
        verifyAllGlobalAttributesForDentalStewardUser();
        pimHomepage.clickLogoutButton();
    }
    public void verifyAllGlobalAttributesForDentalStewardUser() {
        Assertions.assertThat(globalAttributePage.isAllAttributesVisible("HSI_Item_Number")).isTrue();
        Assertions.assertThat(globalAttributePage.isAllAttributesVisible("JDE_Description")).isTrue();
        Assertions.assertThat(globalAttributePage.isAllAttributesVisible("JDE_Strength")).isTrue();
        Assertions.assertThat(globalAttributePage.isAllAttributesVisible("WCS_Description")).isTrue();
        Assertions.assertThat(globalAttributePage.isAllAttributesVisible("WCS_Strength")).isTrue();
        Assertions.assertThat(globalAttributePage.isAllAttributesVisible("JDE_Size")).isTrue();
        Assertions.assertThat(globalAttributePage.isAllAttributesVisible("Component_UOM")).isTrue();
        Assertions.assertThat(globalAttributePage.isAllAttributesVisible("Shipping_UOM")).isTrue();
        Assertions.assertThat(globalAttributePage.isAllAttributesVisible("JDE_French_Description")).isTrue();
        Assertions.assertThat(globalAttributePage.isAllAttributesVisible("UOM_Product")).isTrue();
        Assertions.assertThat(globalAttributePage.isAllAttributesVisible("FDA_Listed_Kit_Flag")).isTrue();
    }

    //UI_022
    @PimFrameworkAnnotation(module = Modules.Entities_UI_Func, category = CategoryType.REGRESSION)
    @TestDataSheet(sheetname = TestCaseSheet.Entities_UI_Func_TestData)
    @Test(description ="UI_022 | verify using Medical steward user all global attributes of product are viewable", groups = { "US",
			"pim","Entities_UI_Func" },dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
    public void verifyGlobalAttributesForMedicalStewardUser(Map<String, String> map)
    {
    	
        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
        pimHomepage.mainMenu().clickStructuresMenu().selectCatalogType(map.get("CatalogType"))
                .selectClassificationType(map.get("CategoryType")).selectItemType(map.get("ItemType"))
                .clickOnSelectedTaxnomyType(map.get("CategoryType")).clickOnfirstSubsetDropdown(map.get("SubsetDropDown"))
                .clickOnShowAll().clickOnFirstItemCode();
        productDetailSearchPage.selectTabfromDropdown(map.get("GlobalAttributeTab"));
        verifyAllGlobalAttributesForMedicalStewardUser();
        pimHomepage.clickLogoutButton();
    }
    public void verifyAllGlobalAttributesForMedicalStewardUser() {
        Assertions.assertThat(globalAttributePage.isAllAttributesVisible("HSI_Item_Number")).isTrue();
        Assertions.assertThat(globalAttributePage.isAllAttributesVisible("JDE_Description")).isTrue();
        Assertions.assertThat(globalAttributePage.isAllAttributesVisible("JDE_Strength")).isTrue();
        Assertions.assertThat(globalAttributePage.isAllAttributesVisible("WCS_Description")).isTrue();
        Assertions.assertThat(globalAttributePage.isAllAttributesVisible("WCS_Strength")).isTrue();
        Assertions.assertThat(globalAttributePage.isAllAttributesVisible("JDE_Size")).isTrue();
        Assertions.assertThat(globalAttributePage.isAllAttributesVisible("Component_UOM")).isTrue();
        Assertions.assertThat(globalAttributePage.isAllAttributesVisible("Shipping_UOM")).isTrue();
        Assertions.assertThat(globalAttributePage.isAllAttributesVisible("JDE_French_Description")).isTrue();
        Assertions.assertThat(globalAttributePage.isAllAttributesVisible("UOM_Product")).isTrue();
        Assertions.assertThat(globalAttributePage.isAllAttributesVisible("FDA_Listed_Kit_Flag")).isTrue();
    }

    //UI_023
    @PimFrameworkAnnotation(module = Modules.Entities_UI_Func, category = CategoryType.REGRESSION)
    @TestDataSheet(sheetname = TestCaseSheet.Entities_UI_Func_TestData)
    @Test(description ="UI_023 | verify using Zahn steward user all global attributes of product are viewable", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class,groups = { "US",
			"pim","Entities_UI_Func" })
    public void verifyGlobalAttributesForZahnStewardUser(Map<String, String> map)
    {
    	
        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("Zahn User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("Zahn User").get("Password")).clickLoginButton();
        pimHomepage.mainMenu().clickStructuresMenu().selectCatalogType(map.get("CatalogType"))
                .selectClassificationType(map.get("CategoryType")).selectItemType(map.get("ItemType"))
                .clickOnSelectedTaxnomyType(map.get("CategoryType")).clickOnfirstSubsetDropdown(map.get("SubsetDropDown"))
                .clickOnShowAll().clickOnFirstItemCode();
        productDetailSearchPage.selectTabfromDropdown(map.get("GlobalAttributeTab"));
        verifyAllGlobalAttributesForZahnStewardUser();
        pimHomepage.clickLogoutButton();
    }
    public void verifyAllGlobalAttributesForZahnStewardUser() {
        Assertions.assertThat(globalAttributePage.isAllAttributesVisible("HSI_Item_Number")).isTrue();
        Assertions.assertThat(globalAttributePage.isAllAttributesVisible("JDE_Description")).isTrue();
        Assertions.assertThat(globalAttributePage.isAllAttributesVisible("JDE_Strength")).isTrue();
        Assertions.assertThat(globalAttributePage.isAllAttributesVisible("WCS_Description")).isTrue();
        Assertions.assertThat(globalAttributePage.isAllAttributesVisible("WCS_Strength")).isTrue();
        Assertions.assertThat(globalAttributePage.isAllAttributesVisible("JDE_Size")).isTrue();
        Assertions.assertThat(globalAttributePage.isAllAttributesVisible("Component_UOM")).isTrue();
        Assertions.assertThat(globalAttributePage.isAllAttributesVisible("Shipping_UOM")).isTrue();
        Assertions.assertThat(globalAttributePage.isAllAttributesVisible("JDE_French_Description")).isTrue();
        Assertions.assertThat(globalAttributePage.isAllAttributesVisible("UOM_Product")).isTrue();
        Assertions.assertThat(globalAttributePage.isAllAttributesVisible("FDA_Listed_Kit_Flag")).isTrue();
    }

    //UI_017
    @PimFrameworkAnnotation(module = Modules.Entities_UI_Func, category = CategoryType.REGRESSION)
    @TestDataSheet(sheetname = TestCaseSheet.Entities_UI_Func_TestData)
    @Test(description ="UI_017 | verify using Admin Dental User all global attributes of product are viewable",groups = {"CA","pim","Entities_UI_Func"},dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
    public void verifyGlobalAttributesForAdminDentalUser(Map<String, String> map)
    {
    	
        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
        pimHomepage.mainMenu().clickStructuresMenu().selectCatalogType(map.get("CatalogType"))
                .selectClassificationType(map.get("CategoryType")).selectItemType(map.get("ItemType"))
                .clickOnSelectedTaxnomyType(map.get("CategoryType")).clickOnfirstSubsetDropdown(map.get("SubsetDropDown"))
                .clickOnShowAll().clickOnFirstItemCode();
        productDetailSearchPage.selectTabfromDropdown(map.get("GlobalAttributeTab"));
        verifyAllGlobalAttributesForAdminDentalUser();
        pimHomepage.clickLogoutButton();
    }
    public void verifyAllGlobalAttributesForAdminDentalUser() {
        Assertions.assertThat(globalAttributePage.isAllAttributesVisible("HSI_Item_Number")).isTrue();
        Assertions.assertThat(globalAttributePage.isAllAttributesVisible("JDE_Description")).isTrue();
        Assertions.assertThat(globalAttributePage.isAllAttributesVisible("JDE_Strength")).isTrue();
        Assertions.assertThat(globalAttributePage.isAllAttributesVisible("WCS_Description")).isTrue();
        Assertions.assertThat(globalAttributePage.isAllAttributesVisible("WCS_Strength")).isTrue();
        Assertions.assertThat(globalAttributePage.isAllAttributesVisible("JDE_Size")).isTrue();
        Assertions.assertThat(globalAttributePage.isAllAttributesVisible("Component_UOM")).isTrue();
        Assertions.assertThat(globalAttributePage.isAllAttributesVisible("Shipping_UOM")).isTrue();
        Assertions.assertThat(globalAttributePage.isAllAttributesVisible("JDE_French_Description")).isTrue();
        Assertions.assertThat(globalAttributePage.isAllAttributesVisible("UOM_Product")).isTrue();
        Assertions.assertThat(globalAttributePage.isAllAttributesVisible("FDA_Listed_Kit_Flag")).isTrue();
    }

    //UI_018
    @PimFrameworkAnnotation(module = Modules.Entities_UI_Func, category = CategoryType.REGRESSION)
    @TestDataSheet(sheetname = TestCaseSheet.Entities_UI_Func_TestData)
    @Test(description ="UI_018 | verify using Admin Medical User all global attributes of product are viewable",groups = {"CA","pim","Entities_UI_Func"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
    public void verifyGlobalAttributesForAdminMedicalUser(Map<String, String> map)
    {
    	
        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
        pimHomepage.mainMenu().clickStructuresMenu().selectCatalogType(map.get("CatalogType"))
                .selectClassificationType(map.get("CategoryType")).selectItemType(map.get("ItemType"))
                .clickOnSelectedTaxnomyType(map.get("CategoryType")).clickOnfirstSubsetDropdown(map.get("SubsetDropDown"))
                .clickOnShowAll().clickOnFirstItemCode();
        productDetailSearchPage.selectTabfromDropdown(map.get("GlobalAttributeTab"));
        verifyAllGlobalAttributesForAdminMedicalUser();
        pimHomepage.clickLogoutButton();
    }
    public void verifyAllGlobalAttributesForAdminMedicalUser() {
        Assertions.assertThat(globalAttributePage.isAllAttributesVisible("HSI_Item_Number")).isTrue();
        Assertions.assertThat(globalAttributePage.isAllAttributesVisible("JDE_Description")).isTrue();
        Assertions.assertThat(globalAttributePage.isAllAttributesVisible("JDE_Strength")).isTrue();
        Assertions.assertThat(globalAttributePage.isAllAttributesVisible("WCS_Description")).isTrue();
        Assertions.assertThat(globalAttributePage.isAllAttributesVisible("WCS_Strength")).isTrue();
        Assertions.assertThat(globalAttributePage.isAllAttributesVisible("JDE_Size")).isTrue();
        Assertions.assertThat(globalAttributePage.isAllAttributesVisible("Component_UOM")).isTrue();
        Assertions.assertThat(globalAttributePage.isAllAttributesVisible("Shipping_UOM")).isTrue();
        Assertions.assertThat(globalAttributePage.isAllAttributesVisible("JDE_French_Description")).isTrue();
        Assertions.assertThat(globalAttributePage.isAllAttributesVisible("UOM_Product")).isTrue();
        Assertions.assertThat(globalAttributePage.isAllAttributesVisible("FDA_Listed_Kit_Flag")).isTrue();
    }

    //UI_019
    @PimFrameworkAnnotation(module = Modules.Entities_UI_Func, category = CategoryType.REGRESSION)
    @TestDataSheet(sheetname = TestCaseSheet.Entities_UI_Func_TestData)
    @Test(description ="UI_019 | verify using Admin Zahn User all global attributes of product are viewable",groups = {"CA","pim","Entities_UI_Func"},dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
    public void verifyGlobalAttributesForAdminZahnUser(Map<String, String> map) {
    	
        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
        pimHomepage.mainMenu().clickStructuresMenu().selectCatalogType(map.get("CatalogType"))
                .selectClassificationType(map.get("CategoryType")).selectItemType(map.get("ItemType"))
                .clickOnSelectedTaxnomyType(map.get("CategoryType")).clickOnfirstSubsetDropdown(map.get("SubsetDropDown"))
                .clickOnShowAll().clickOnFirstItemCode();
        productDetailSearchPage.selectTabfromDropdown(map.get("GlobalAttributeTab"));
        verifyAllGlobalAttributesForAdminZahnUser();
        pimHomepage.clickLogoutButton();
    }
    public void verifyAllGlobalAttributesForAdminZahnUser() {
        Assertions.assertThat(globalAttributePage.isAllAttributesVisible("HSI_Item_Number")).isTrue();
        Assertions.assertThat(globalAttributePage.isAllAttributesVisible("JDE_Description")).isTrue();
        Assertions.assertThat(globalAttributePage.isAllAttributesVisible("JDE_Strength")).isTrue();
        Assertions.assertThat(globalAttributePage.isAllAttributesVisible("WCS_Description")).isTrue();
        Assertions.assertThat(globalAttributePage.isAllAttributesVisible("WCS_Strength")).isTrue();
        Assertions.assertThat(globalAttributePage.isAllAttributesVisible("JDE_Size")).isTrue();
        Assertions.assertThat(globalAttributePage.isAllAttributesVisible("Component_UOM")).isTrue();
        Assertions.assertThat(globalAttributePage.isAllAttributesVisible("Shipping_UOM")).isTrue();
        Assertions.assertThat(globalAttributePage.isAllAttributesVisible("JDE_French_Description")).isTrue();
        Assertions.assertThat(globalAttributePage.isAllAttributesVisible("UOM_Product")).isTrue();
        Assertions.assertThat(globalAttributePage.isAllAttributesVisible("FDA_Listed_Kit_Flag")).isTrue();
    }


}
