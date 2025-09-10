package com.pim.tests.webDescriptionTests;

import com.pim.annotations.PimFrameworkAnnotation;
import com.pim.annotations.TestDataSheet;
import com.pim.driver.DriverManager;
import com.pim.enums.CategoryType;
import com.pim.enums.Modules;
import com.pim.enums.TestCaseSheet;
import com.pim.pages.BasePage;
import com.pim.pages.LoginPage;
import com.pim.pages.PimHomepage;
import com.pim.pages.WebDescription_DataEntitiesAndAttributesPage;
import com.pim.pages.us.CreateItemValidationPage;
import com.pim.pages.us.ItemPublishDateAndFlagPage;
import com.pim.tests.BaseTest;
import com.pim.utils.DataProviderUtils;
import com.pim.utils.ExcelUtils;
import com.pim.utils.Javautils;
import org.assertj.core.api.Assertions;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.Map;



public class WebDescription_DataEntitiesAndAttributesTests extends BaseTest {

    @PimFrameworkAnnotation(module = Modules.WEB_DESCRIPTION, category = CategoryType.SANITY)
    @TestDataSheet(sheetname = TestCaseSheet.DataAttributeTest)
    @Test(description = "TCID | Verify Full Display Description for Dental division", dataProvider = "getCatalogData", 
    dataProviderClass = DataProviderUtils.class,groups = {"CA","WEB_DESCRIPTION"})
    public void validateFullDisplayDescriptionForDentalDivision(Map<String, String> map){
        WebDescription_DataEntitiesAndAttributesPage webDescription_dataEntitiesAndAttributesPage = new WebDescription_DataEntitiesAndAttributesPage();
        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("itemNumber")).clickSeachButton();
        BasePage.WaitForMiliSec(10000);
        ItemPublishDateAndFlagPage itemPublish = new ItemPublishDateAndFlagPage();
        itemPublish.clickOnJDEdescriptionContent();
        webDescription_dataEntitiesAndAttributesPage.clickOnWebDescriptionTab().clickOnDivisionSelectDropdown().clickOnDentalDivisionOption();
        BasePage.WaitForMiliSec(3000);
        Javautils.validateFirstLetterOfEveryWordIsInUppercase(webDescription_dataEntitiesAndAttributesPage.captureFullDisplayDescriptionFieldValue());
        BasePage.WaitForMiliSec(2000);
        Javautils.validateSpecialCharacterAvailability(webDescription_dataEntitiesAndAttributesPage.captureFullDisplayDescriptionFieldValue());
        BasePage.WaitForMiliSec(2000);
        Javautils.validateMaximumLengthOf512Characters(webDescription_dataEntitiesAndAttributesPage.captureFullDisplayDescriptionFieldValue());
        pimHomepage.clickLogoutButton();
    }

    @PimFrameworkAnnotation(module = Modules.WEB_DESCRIPTION, category = CategoryType.SANITY)
    @TestDataSheet(sheetname = TestCaseSheet.DataAttributeTest)
    @Test(description = "TCID | Verify Full Display Description for Dental division As Failed", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class,groups = {"CA","WEB_DESCRIPTION"})
    public void validateFullDisplayDescriptionForDentalDivisionAsFailed(Map<String, String> map){
        WebDescription_DataEntitiesAndAttributesPage webDescription_dataEntitiesAndAttributesPage = new WebDescription_DataEntitiesAndAttributesPage();
        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("itemNumber")).clickSeachButton();
        BasePage.WaitForMiliSec(10000);
        ItemPublishDateAndFlagPage itemPublish = new ItemPublishDateAndFlagPage();
        itemPublish.clickOnJDEdescriptionContent();
        webDescription_dataEntitiesAndAttributesPage.clickOnWebDescriptionTab().clickOnDivisionSelectDropdown().clickOnDentalDivisionOption();
        BasePage.WaitForMiliSec(3000);
        Assert.assertTrue(DriverManager.getDriver().findElement(WebDescription_DataEntitiesAndAttributesPage.fullDisplayDesc).isDisplayed());
        BasePage.WaitForMiliSec(2000);
        Javautils.validateSpecialCharacterNon_Availability(webDescription_dataEntitiesAndAttributesPage.captureFullDisplayDescriptionFieldValue());
        BasePage.WaitForMiliSec(2000);
        Javautils.validateLengthOfNotEqualTo512Characters(webDescription_dataEntitiesAndAttributesPage.captureFullDisplayDescriptionFieldValue());
        pimHomepage.clickLogoutButton();

    }

    @PimFrameworkAnnotation(module = Modules.WEB_DESCRIPTION, category = CategoryType.SANITY)
    @TestDataSheet(sheetname = TestCaseSheet.DataAttributeTest)
    @Test(description = "TCID | Verify Full Display Description for Medical division", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class,groups = {"CA","WEB_DESCRIPTION"})
    public void validateFullDisplayDescriptionForMedicalDivision(Map<String, String> map){
        WebDescription_DataEntitiesAndAttributesPage webDescription_dataEntitiesAndAttributesPage = new WebDescription_DataEntitiesAndAttributesPage();
        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("itemNumber")).clickSeachButton();
        BasePage.WaitForMiliSec(10000);
        ItemPublishDateAndFlagPage itemPublish = new ItemPublishDateAndFlagPage();
        itemPublish.clickOnJDEdescriptionContent();
        webDescription_dataEntitiesAndAttributesPage.clickOnWebDescriptionTab().clickOnDivisionSelectDropdown().clickOnMedicalDivisionOption();
        BasePage.WaitForMiliSec(3000);
        Javautils.validateFirstLetterOfEveryWordIsInUppercase(webDescription_dataEntitiesAndAttributesPage.captureFullDisplayDescriptionFieldValue());
        BasePage.WaitForMiliSec(2000);
        Javautils.validateSpecialCharacterAvailability(webDescription_dataEntitiesAndAttributesPage.captureFullDisplayDescriptionFieldValue());
        BasePage.WaitForMiliSec(2000);
        Javautils.validateMaximumLengthOf512Characters(webDescription_dataEntitiesAndAttributesPage.captureFullDisplayDescriptionFieldValue());
        pimHomepage.clickLogoutButton();

    }

    @PimFrameworkAnnotation(module = Modules.WEB_DESCRIPTION, category = CategoryType.SANITY)
    @TestDataSheet(sheetname = TestCaseSheet.DataAttributeTest)
    @Test(description = "TCID | Verify Full Display Description for Medical division As Failed", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class,groups = {"CA","WEB_DESCRIPTION"})
    public void validateFullDisplayDescriptionForMedicalDivisionAsFailed(Map<String, String> map){
        WebDescription_DataEntitiesAndAttributesPage webDescription_dataEntitiesAndAttributesPage = new WebDescription_DataEntitiesAndAttributesPage();
        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("itemNumber")).clickSeachButton();
        BasePage.WaitForMiliSec(10000);
        ItemPublishDateAndFlagPage itemPublish = new ItemPublishDateAndFlagPage();
        itemPublish.clickOnJDEdescriptionContent();
        webDescription_dataEntitiesAndAttributesPage.clickOnWebDescriptionTab().clickOnDivisionSelectDropdown().clickOnMedicalDivisionOption();
        BasePage.WaitForMiliSec(3000);
        Assert.assertTrue(DriverManager.getDriver().findElement(WebDescription_DataEntitiesAndAttributesPage.fullDisplayDesc).isDisplayed());
        BasePage.WaitForMiliSec(2000);
        Javautils.validateSpecialCharacterNon_Availability(webDescription_dataEntitiesAndAttributesPage.captureFullDisplayDescriptionFieldValue());
        BasePage.WaitForMiliSec(2000);
        Javautils.validateLengthOfNotEqualTo512Characters(webDescription_dataEntitiesAndAttributesPage.captureFullDisplayDescriptionFieldValue());
        pimHomepage.clickLogoutButton();

    }

    @PimFrameworkAnnotation(module = Modules.WEB_DESCRIPTION, category = CategoryType.SANITY)
    @TestDataSheet(sheetname = TestCaseSheet.DataAttributeTest)
    @Test(description = "TCID | Verify Full Display Description for Zahn division", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class,groups = {"CA","WEB_DESCRIPTION"})
    public void validateFullDisplayDescriptionForZahnDivision(Map<String, String> map){
        WebDescription_DataEntitiesAndAttributesPage webDescription_dataEntitiesAndAttributesPage = new WebDescription_DataEntitiesAndAttributesPage();
        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("itemNumber")).clickSeachButton();
        BasePage.WaitForMiliSec(10000);
        ItemPublishDateAndFlagPage itemPublish = new ItemPublishDateAndFlagPage();
        itemPublish.clickOnJDEdescriptionContent();
        webDescription_dataEntitiesAndAttributesPage.clickOnWebDescriptionTab().clickOnDivisionSelectDropdown().clickOnZahnDivisionOption();
        BasePage.WaitForMiliSec(3000);
        Javautils.validateFirstLetterOfEveryWordIsInUppercase(webDescription_dataEntitiesAndAttributesPage.captureFullDisplayDescriptionFieldValue());
        BasePage.WaitForMiliSec(2000);
        Javautils.validateSpecialCharacterAvailability(webDescription_dataEntitiesAndAttributesPage.captureFullDisplayDescriptionFieldValue());
        BasePage.WaitForMiliSec(2000);
        Javautils.validateMaximumLengthOf512Characters(webDescription_dataEntitiesAndAttributesPage.captureFullDisplayDescriptionFieldValue());
        pimHomepage.clickLogoutButton();

    }

    @PimFrameworkAnnotation(module = Modules.WEB_DESCRIPTION, category = CategoryType.SANITY)
    @TestDataSheet(sheetname = TestCaseSheet.DataAttributeTest)
    @Test(description = "TCID | Verify Full Display Description for Zahn division As Failed", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class,groups = {"CA","WEB_DESCRIPTION"})
    public void validateFullDisplayDescriptionForZahnDivisionAsFailed(Map<String, String> map){
        WebDescription_DataEntitiesAndAttributesPage webDescription_dataEntitiesAndAttributesPage = new WebDescription_DataEntitiesAndAttributesPage();
        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("itemNumber")).clickSeachButton();
        BasePage.WaitForMiliSec(10000);
        ItemPublishDateAndFlagPage itemPublish = new ItemPublishDateAndFlagPage();
        itemPublish.clickOnJDEdescriptionContent();
        webDescription_dataEntitiesAndAttributesPage.clickOnWebDescriptionTab().clickOnDivisionSelectDropdown().clickOnZahnDivisionOption();
        BasePage.WaitForMiliSec(3000);
        Assert.assertTrue(DriverManager.getDriver().findElement(WebDescription_DataEntitiesAndAttributesPage.fullDisplayDesc).isDisplayed());
        Javautils.validateSpecialCharacterNon_Availability(webDescription_dataEntitiesAndAttributesPage.captureFullDisplayDescriptionFieldValue());
        BasePage.WaitForMiliSec(2000);
        Javautils.validateLengthOfNotEqualTo512Characters(webDescription_dataEntitiesAndAttributesPage.captureFullDisplayDescriptionFieldValue());
        pimHomepage.clickLogoutButton();

    }

    @PimFrameworkAnnotation(module = Modules.WEB_DESCRIPTION, category = CategoryType.SANITY)
    @TestDataSheet(sheetname = TestCaseSheet.DataAttributeTest)
    @Test(description = "TCID | Verify Abbreviated Display Description for Dental division to check 80 charecter", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class,groups = {"CA","WEB_DESCRIPTION"})
    public void validateAbbreviatedDisplayDescriptionForDentalDivision(Map<String, String> map){
        WebDescription_DataEntitiesAndAttributesPage webDescription_dataEntitiesAndAttributesPage = new WebDescription_DataEntitiesAndAttributesPage();
        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("itemNumber")).clickSeachButton();
        BasePage.WaitForMiliSec(10000);
        ItemPublishDateAndFlagPage itemPublish = new ItemPublishDateAndFlagPage();
        itemPublish.clickOnJDEdescriptionContent();
        webDescription_dataEntitiesAndAttributesPage.clickOnWebDescriptionTab().clickOnDivisionSelectDropdown().clickOnDentalDivisionOption();
        BasePage.WaitForMiliSec(3000);
        Assert.assertTrue(DriverManager.getDriver().findElement(WebDescription_DataEntitiesAndAttributesPage.abbreviatedDisplayDesc).isDisplayed());
        Javautils.validateLengthOfNotEqualTo80Characters(webDescription_dataEntitiesAndAttributesPage.captureAbbreviatedDisplayDescriptionFieldValue());
        pimHomepage.clickLogoutButton();

    }

    @PimFrameworkAnnotation(module = Modules.WEB_DESCRIPTION, category = CategoryType.SANITY)
    @TestDataSheet(sheetname = TestCaseSheet.DataAttributeTest)
    @Test(description = "TCID | Verify Abbreviated Display Description for Medical division", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class,groups = {"CA","WEB_DESCRIPTION"})
    public void validateAbbreviatedDisplayDescriptionForMedicalDivision(Map<String, String> map){
        WebDescription_DataEntitiesAndAttributesPage webDescription_dataEntitiesAndAttributesPage = new WebDescription_DataEntitiesAndAttributesPage();
        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("itemNumber")).clickSeachButton();
        BasePage.WaitForMiliSec(10000);
        ItemPublishDateAndFlagPage itemPublish = new ItemPublishDateAndFlagPage();
        itemPublish.clickOnJDEdescriptionContent();
        webDescription_dataEntitiesAndAttributesPage.clickOnWebDescriptionTab().clickOnDivisionSelectDropdown().clickOnMedicalDivisionOption();
        BasePage.WaitForMiliSec(3000);
        Assert.assertTrue(DriverManager.getDriver().findElement(WebDescription_DataEntitiesAndAttributesPage.abbreviatedDisplayDesc).isDisplayed());
        Javautils.validateLengthOfNotEqualTo80Characters(webDescription_dataEntitiesAndAttributesPage.captureAbbreviatedDisplayDescriptionFieldValue());
        pimHomepage.clickLogoutButton();

    }

    @PimFrameworkAnnotation(module = Modules.WEB_DESCRIPTION, category = CategoryType.SANITY)
    @TestDataSheet(sheetname = TestCaseSheet.DataAttributeTest)
    @Test(description = "TCID | Verify Abbreviated Display Description for Zahn division", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class,groups = {"CA","WEB_DESCRIPTION"})
    public void validateAbbreviatedDisplayDescriptionForZahnDivision(Map<String, String> map){
        WebDescription_DataEntitiesAndAttributesPage webDescription_dataEntitiesAndAttributesPage = new WebDescription_DataEntitiesAndAttributesPage();
        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("itemNumber")).clickSeachButton();
        BasePage.WaitForMiliSec(10000);
        ItemPublishDateAndFlagPage itemPublish = new ItemPublishDateAndFlagPage();
        itemPublish.clickOnJDEdescriptionContent();
        webDescription_dataEntitiesAndAttributesPage.clickOnWebDescriptionTab().clickOnDivisionSelectDropdown().clickOnZahnDivisionOption();
        BasePage.WaitForMiliSec(3000);
        Assert.assertTrue(DriverManager.getDriver().findElement(WebDescription_DataEntitiesAndAttributesPage.abbreviatedDisplayDesc).isDisplayed());
        Javautils.validateLengthOfNotEqualTo80Characters(webDescription_dataEntitiesAndAttributesPage.captureAbbreviatedDisplayDescriptionFieldValue());
        pimHomepage.clickLogoutButton();

    }


}
