package com.pim.tests.webDescriptionTests;

import com.pim.annotations.PimFrameworkAnnotation;
import com.pim.annotations.TestDataSheet;
import com.pim.driver.DriverManager;
import com.pim.enums.CategoryType;
import com.pim.enums.LogType;
import com.pim.enums.Modules;
import com.pim.enums.TestCaseSheet;
import com.pim.pages.BasePage;
import com.pim.pages.CanadaTranslationPage;
import com.pim.pages.LoginPage;
import com.pim.pages.PimHomepage;
import com.pim.pages.ProductDetailSearchPage;
import com.pim.pages.QualityStatusPage;
import com.pim.pages.QueriesSubMenu;
import com.pim.pages.StructureSubMenu;
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

import static com.pim.reports.FrameworkLogger.log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class AbbreviatedDisplayDescriptionTest extends BaseTest  {
	QualityStatusPage qualitystatus = new QualityStatusPage();
	Javautils javautils=new Javautils();
	QueriesSubMenu queriesSubMenu= new QueriesSubMenu();
	StructureSubMenu structuresubmenu=new StructureSubMenu();

	@PimFrameworkAnnotation(module = Modules.WEB_DESCRIPTION, category = CategoryType.SANITY)
	@TestDataSheet(sheetname = TestCaseSheet.DataAttributeTest)
	@Test(description = "TCID | Verify Abbreviated Display Description for Dental division to special charecter",groups = {"CA","pim","WEB_DESCRIPTION"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
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
		Javautils.validateSpecialCharacterAvailability(webDescription_dataEntitiesAndAttributesPage.captureAbbreviatedDisplayDescriptionFieldValue());
		pimHomepage.clickLogoutButton();

	}
	//WRFL_069-Verify if update performed in abbreviated description and full display description of French option under CA catalog gets merged to master catalog if item already exists in master catalog
	@PimFrameworkAnnotation(module = Modules.WEB_DESCRIPTION, category = CategoryType.Valid_Failure)
	@TestDataSheet(sheetname = TestCaseSheet.AbbreviatedAndFullDescription)
	@Test(description = "WRFL_069 | Verify if update performed in abbreviated description and full display description of French option under CA catalog gets merged to master catalog if item already exists in master catalog", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class,groups = {"CA","pim","WEB_DESCRIPTION"})
	public void VerifyIfUpdatePerformedInAbbreviatedDescriptionAndFullDisplayDescriptionOfFrenchOptionUnderCACatalogGetsMergedToMasterCatalogIfItemAlreadyExistsInMasterCatalog(Map<String, String> map){

		CanadaTranslationPage cannadaTranslationPage= new CanadaTranslationPage();
		ProductDetailSearchPage productDetailSearchPage=new ProductDetailSearchPage();

		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("CA French User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("CA French User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
		cannadaTranslationPage.EnterFullDisplayDescriptionManually(map.get("FullDisplayDescription"));
		cannadaTranslationPage.EnterAbbreviatedDisplayDescription(map.get("AbbreviatedDisplayDescription"));

		//verify quality status
		productDetailSearchPage.selectTabfromDropdown(map.get("TabNameSecond"));   
		qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).maximizeQualityStatusTab();
		List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("Status"));
		for (String Rules:javautils.readMultipleValuesFromExcel(map.get("QualityStatusRules")))
		{
			Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);	
		}
		qualitystatus.minimizeQualityStatusTab();


		queriesSubMenu.selectCatalogType(map.get("CatalogTypeMaster")).clickSeachButton();
		structuresubmenu.selectSpecificItem(map.get("specificItemCode"));
		productDetailSearchPage.selectTabfromDropdown(map.get("TabName"));
		String Full_Display_Description=cannadaTranslationPage.getFullDisplayDescription();
		Assertions.assertThat(Full_Display_Description).isEqualTo(map.get("FullDisplayDescription"));
		String Abbreviated_Display_Description = cannadaTranslationPage.getAbbreviatedDisplayDescription();
		Assertions.assertThat(Abbreviated_Display_Description).isEqualTo(map.get("AbbreviatedDisplayDescription"));

	}


}
