package com.pim.tests.dataEntitiesAndAttributes;

import com.pim.annotations.PimFrameworkAnnotation;
import com.pim.annotations.TestDataSheet;
import com.pim.enums.CategoryType;
import com.pim.enums.Modules;
import com.pim.enums.TestCaseSheet;
import com.pim.pages.BasePage;
import com.pim.pages.LocalAttributePage;
import com.pim.pages.LoginPage;
import com.pim.pages.PimHomepage;
import com.pim.pages.ProductDetailSearchPage;
import com.pim.pages.StructureSubMenu;
import com.pim.tests.BaseTest;
import com.pim.utils.DataProviderUtils;
import com.pim.utils.ExcelUtils;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import java.util.Map;

public class DataAttributes_Facets_For_All_DivisionTest extends BaseTest {

    StructureSubMenu structureSubMenu = new StructureSubMenu();
    ProductDetailSearchPage productDetailSearchPage = new ProductDetailSearchPage();
    LocalAttributePage localAttributePage =new LocalAttributePage();

    //DataAttributes_009
    @PimFrameworkAnnotation(module = Modules.DataEntities_Attributes, category = CategoryType.REGRESSION)
    @TestDataSheet(sheetname = TestCaseSheet.DataAttributeFacetsTest)
    @Test(description = "DataAttributes_009 | verify that system is able to identify certain local attributes as facets for the web in Dental division DataAttributes_009",
             dataProvider = "getCatalogData",groups = {"US","pim","DataEntities_Attributes"}, dataProviderClass = DataProviderUtils.class)
    public void verify_that_System_Is_Able_to_identify_LocalAttributes_as_Facets_for_the_Web_In_Dental_Division(
            Map<String, String> map) {

        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
        pimHomepage.mainMenu().clickStructuresMenu().selectCatalogType(map.get("CatalogType"))
                .selectClassificationType(map.get("CategoryType")).selectItemType(map.get("ItemType"));
        structureSubMenu.clickOnSelectedTaxnomyType(map.get("CategoryType"))
                .selectMainCategory(map.get("SubSetOfTaxanomy")).selectMainCategory(map.get("SubSetOfLevel1")).selectMainCategory(map.get("SubSetOfLevel2"))
                .selectSubCategory(map.get("SubSetOfLevel3"));
        productDetailSearchPage.applyFilterInSearchPage(map.get("itemNumber")).clickOnFirstResult()
		.selectTabfromDropdown(map.get("TabName"));
        Assertions.assertThat(localAttributePage.isAllAttributesVisible("brand")).isTrue();
        Assertions.assertThat(localAttributePage.isAllAttributesVisible("color")).isTrue();
        Assertions.assertThat(localAttributePage.isAllAttributesVisible("composition")).isTrue();
        Assertions.assertThat(localAttributePage.isAllAttributesVisible("disposable")).isTrue();
        Assertions.assertThat(localAttributePage.isAllAttributesVisible("item")).isTrue();
        Assertions.assertThat(localAttributePage.isAllAttributesVisible("packaging")).isTrue();
        Assertions.assertThat(localAttributePage.isAllAttributesVisible("sterileNonSterile")).isTrue();
        Assertions.assertThat(localAttributePage.isAllAttributesVisible("itemType")).isTrue();
        Assertions.assertThat(localAttributePage.isAllAttributesVisible("lwh")).isTrue();
        Assertions.assertThat(localAttributePage.isAllAttributesVisible("quantityInputField")).isTrue();
        Assertions.assertThat(localAttributePage.isAllAttributesVisible("sizeText")).isTrue();
        BasePage.WaitForMiliSec(1000);
        pimHomepage.clickLogoutButton();


    }

    //DataAttributes_010
    @PimFrameworkAnnotation(module = Modules.DataEntities_Attributes, category = CategoryType.REGRESSION)
    @TestDataSheet(sheetname = TestCaseSheet.DataAttributeFacetsTest)
    @Test(description = "DataAttributes_010 | verify that system is able to identify certain local attributes as facets for the web in Medical division DataAttributes_010",
            dataProvider = "getCatalogData",groups = {"US","pim","DataEntities_Attributes"}, dataProviderClass = DataProviderUtils.class)
    public void verify_that_System_Is_Able_to_identify_LocalAttributes_as_Facets_for_the_Web_In_Medical_Division(
            Map<String, String> map) {

        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
        pimHomepage.mainMenu().clickStructuresMenu().selectCatalogType(map.get("CatalogType"))
                .selectClassificationType(map.get("CategoryType")).selectItemType(map.get("ItemType"));
        structureSubMenu.clickOnSelectedTaxnomyType(map.get("CategoryType"))
                .selectMainCategory(map.get("SubSetOfTaxanomy")).selectMainCategory(map.get("SubSetOfLevel1"))
                .selectSubCategory(map.get("SubSetOfLevel3"));
        productDetailSearchPage.applyFilterInSearchPage(map.get("itemNumber")).clickOnFirstResult()
		.selectTabfromDropdown(map.get("TabName"));
        Assertions.assertThat(localAttributePage.isAllAttributesVisible("brand")).isTrue();
        Assertions.assertThat(localAttributePage.isAllAttributesVisible("color")).isTrue();
        Assertions.assertThat(localAttributePage.isAllAttributesVisible("composition")).isTrue();
        Assertions.assertThat(localAttributePage.isAllAttributesVisible("disposable")).isTrue();
        Assertions.assertThat(localAttributePage.isAllAttributesVisible("item")).isTrue();
        Assertions.assertThat(localAttributePage.isAllAttributesVisible("packaging")).isTrue();
        Assertions.assertThat(localAttributePage.isAllAttributesVisible("sterileNonSterile")).isTrue();
        Assertions.assertThat(localAttributePage.isAllAttributesVisible("itemType")).isTrue();
        Assertions.assertThat(localAttributePage.isAllAttributesVisible("lwh")).isTrue();
        Assertions.assertThat(localAttributePage.isAllAttributesVisible("quantityInputField")).isTrue();
        Assertions.assertThat(localAttributePage.isAllAttributesVisible("sizeText")).isTrue();
        BasePage.WaitForMiliSec(1000);
        pimHomepage.clickLogoutButton();


    }

    //DataAttributes_011
    @PimFrameworkAnnotation(module = Modules.DataEntities_Attributes, category = CategoryType.REGRESSION)
    @TestDataSheet(sheetname = TestCaseSheet.DataAttributeFacetsTest)
    @Test(description = "DataAttributes_011 | verify that system is able to identify certain local attributes as facets for the web in Zahn division DataAttributes_011",
            dataProvider = "getCatalogData",groups = {"US","pim","DataEntities_Attributes"}, dataProviderClass = DataProviderUtils.class)
    public void verify_that_System_Is_Able_to_identify_LocalAttributes_as_Facets_for_the_Web_In_Zahn_Division(
            Map<String, String> map) {

        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
        pimHomepage.mainMenu().clickStructuresMenu().selectCatalogType(map.get("CatalogType"))
                .selectClassificationType(map.get("CategoryType")).selectItemType(map.get("ItemType"));
        structureSubMenu.clickOnSelectedTaxnomyType(map.get("CategoryType"))
                .selectMainCategory(map.get("SubSetOfTaxanomy")).selectMainCategory(map.get("SubSetOfLevel1")).selectMainCategory(map.get("SubSetOfLevel2"))
                .selectSubCategory(map.get("SubSetOfLevel3"));
        productDetailSearchPage.applyFilterInSearchPage(map.get("itemNumber")).clickOnFirstResult()
		.selectTabfromDropdown(map.get("TabName"));
        Assertions.assertThat(localAttributePage.isAllAttributesVisible("brand")).isTrue();
        Assertions.assertThat(localAttributePage.isAllAttributesVisible("color")).isTrue();
        Assertions.assertThat(localAttributePage.isAllAttributesVisible("composition")).isTrue();
        Assertions.assertThat(localAttributePage.isAllAttributesVisible("disposable")).isTrue();
        Assertions.assertThat(localAttributePage.isAllAttributesVisible("item")).isTrue();
        Assertions.assertThat(localAttributePage.isAllAttributesVisible("packaging")).isTrue();
        Assertions.assertThat(localAttributePage.isAllAttributesVisible("sterileNonSterile")).isTrue();
        Assertions.assertThat(localAttributePage.isAllAttributesVisible("itemType")).isTrue();
        Assertions.assertThat(localAttributePage.isAllAttributesVisible("lwh")).isTrue();
        Assertions.assertThat(localAttributePage.isAllAttributesVisible("quantityInputField")).isTrue();
        Assertions.assertThat(localAttributePage.isAllAttributesVisible("sizeText")).isTrue();
        BasePage.WaitForMiliSec(1000);
        pimHomepage.clickLogoutButton();
    }

    //DataAttributes_012
    @PimFrameworkAnnotation(module = Modules.DataEntities_Attributes, category = CategoryType.Valid_Failure)
    @TestDataSheet(sheetname = TestCaseSheet.DataAttributeFacetsTest)
    @Test(description = "DataAttributes_012 | verify that system is able to identify certain local attributes as facets for the web in Special Market division DataAttributes_012",
            dataProvider = "getCatalogData",groups = {"US","pim","DataEntities_Attributes"}, dataProviderClass = DataProviderUtils.class)
    public void verify_that_System_Is_Able_to_identify_LocalAttributes_as_Facets_for_the_Web_In_SpecialMarket_Division(
            Map<String, String> map) {

        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
        pimHomepage.mainMenu().clickStructuresMenu().selectCatalogType(map.get("CatalogType"))
                .selectClassificationType(map.get("CategoryType")).selectItemType(map.get("ItemType"));
        structureSubMenu.clickOnSelectedTaxnomyType(map.get("CategoryType"))
                .selectMainCategory(map.get("SubSetOfTaxanomy")).selectMainCategory(map.get("SubSetOfLevel1")).selectMainCategory(map.get("SubSetOfLevel2"))
                .selectSubCategory(map.get("SubSetOfLevel3"));
        productDetailSearchPage.applyFilterInSearchPage(map.get("itemNumber")).clickOnFirstResult()
		.selectTabfromDropdown(map.get("TabName"));
        Assertions.assertThat(localAttributePage.isAllAttributesVisible("brand")).isTrue();
        Assertions.assertThat(localAttributePage.isAllAttributesVisible("color")).isTrue();
        Assertions.assertThat(localAttributePage.isAllAttributesVisible("composition")).isTrue();
        Assertions.assertThat(localAttributePage.isAllAttributesVisible("disposable")).isTrue();
        Assertions.assertThat(localAttributePage.isAllAttributesVisible("item")).isTrue();
        Assertions.assertThat(localAttributePage.isAllAttributesVisible("packaging")).isTrue();
        Assertions.assertThat(localAttributePage.isAllAttributesVisible("sterileNonSterile")).isTrue();
        Assertions.assertThat(localAttributePage.isAllAttributesVisible("itemType")).isTrue();
        Assertions.assertThat(localAttributePage.isAllAttributesVisible("lwh")).isTrue();
        Assertions.assertThat(localAttributePage.isAllAttributesVisible("quantityInputField")).isTrue();
        Assertions.assertThat(localAttributePage.isAllAttributesVisible("sizeText")).isTrue();
        BasePage.WaitForMiliSec(1000);
        pimHomepage.clickLogoutButton();



    }

}