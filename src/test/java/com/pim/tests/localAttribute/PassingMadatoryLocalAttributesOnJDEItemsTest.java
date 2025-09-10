package com.pim.tests.localAttribute;

import static com.pim.reports.FrameworkLogger.log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import com.pim.pages.BasePage;
import com.pim.pages.CatalogTypePage;
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

import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import com.pim.annotations.PimFrameworkAnnotation;
import com.pim.annotations.TestDataSheet;
import com.pim.enums.CategoryType;
import com.pim.enums.LogType;
import com.pim.enums.Modules;
import com.pim.enums.TestCaseSheet;
import com.pim.tests.BaseTest;
import com.pim.utils.DataProviderUtils;
import com.pim.utils.DateandTimeUtils;
import com.pim.utils.ExcelUtils;
import com.pim.utils.Javautils;

public class PassingMadatoryLocalAttributesOnJDEItemsTest extends BaseTest {
	private PassingMadatoryLocalAttributesOnJDEItemsTest() {

	}

	LocalAttributePage localAttributePage = new LocalAttributePage();
	ProductDetailSearchPage productDetailSearchPage = new ProductDetailSearchPage();
	QualityStatusPage qualityStatusPage = new QualityStatusPage();
	SeoTabPage seoTabPage = new SeoTabPage();
	GlobalAttributePage globalAttributePage = new GlobalAttributePage();
	WebDescription webDescription = new WebDescription();
	QueriesSubMenu queriesSubMenu = new QueriesSubMenu();
	PimHomepage pimHomepage = new PimHomepage();
	LoginPage loginPage = new LoginPage();
	MainMenu mainMenu = new MainMenu();


	// INT_010 For US
	@PimFrameworkAnnotation(module = Modules.LOCAL_ATTRIBUTE, category = CategoryType.SMOKE)
	@TestDataSheet(sheetname = TestCaseSheet.Local_Attribute)
	@Test(description = "For US | Adding Mandatory Field values In Local Attribute", dataProvider = "getCatalogData", groups = {
			"US","LOCAL_ATTRIBUTE" }, dataProviderClass = DataProviderUtils.class)
	public void Adding_Mandatory_Field_In_Local_Attribute_For_US1(Map<String, String> map) {

		loginPage.enterUserName(map.get("Username")).enterPassword(map.get("Password")).clickLoginButton();
		
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType("Business").selectItemCreatedBetweenTwoDates().selectCatalogType(map.get("CatalogType"))
        .assignItemCreationDateGreater(DateandTimeUtils.getSpecifiedDayFromCurrentDay(-1)).assignItemCreationDateLess(DateandTimeUtils.getNextDayDate()).clickSeachButton().clickOnSearchResult(map.get("productName"));
        List<String> itemNumbersList = queriesSubMenu.getItemNumberAsPerGivenJDEDescription(map.get("productName"));
        for (String itemNumber : itemNumbersList) {
			pimHomepage.mainMenu().clickRefreshMenuIcon();
	        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
			.enterHsiItemNumber(itemNumber);
			queriesSubMenu.clickSeachButton();
			productDetailSearchPage.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
			qualityStatusPage.maximizeQualityStatusTab();

			// Adding The valid Value in Local Attribute Filed
			BasePage.WaitForMiliSec(2000);
			localAttributePage.editCompositionAttribute(map.get("Composition"));
			BasePage.WaitForMiliSec(1000);
			localAttributePage.itemFieldAttributeChange(map.get("Item"));
			BasePage.WaitForMiliSec(1000);
			localAttributePage.itemTypeFieldAttribute(map.get("Item Type"));

			localAttributePage.selectValuefromSaveDropdown(map.get("saveButton"));

			qualityStatusPage.minimizeQualityStatusTab();
        }
        
	}
	
	// INT_010 For CA
	@PimFrameworkAnnotation(module = Modules.LOCAL_ATTRIBUTE, category = CategoryType.SMOKE)
	@TestDataSheet(sheetname = TestCaseSheet.Local_Attribute)
	@Test(description = "For CA - INT_010 | Verify the Removing And Adding Mandatory Field values In Local Attribute for CA", dataProvider = "getCatalogData", groups = {
			"SMOKE","CA","LOCAL_ATTRIBUTE" }, dataProviderClass = DataProviderUtils.class)
	public void Verify_Removing_Mandatory_Field_And_Adding_Mandatory_Field_In_Local_Attribute_For_CA1(Map<String, String> map) {
		
		loginPage.enterUserName(map.get("Username")).enterPassword(map.get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("itemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
		// Removing Value
//		localAttributePage.brandFieldAttribute(map.get("LocalAttributeBrandFieldValueWithNoContent"));
//		localAttributePage.brandFieldAttribute(map.get("LocalAttributeitemTypeFieldValueWithNoContent"));
		localAttributePage.MandatoryItemFieldAttribute(map.get("LocalAttributeItemFieldValueWithNoContent"))
				.clearingMandatoryQuantityFieldAttribute().selectValuefromSaveDropdown(map.get("saveButton"));

		productDetailSearchPage.clickRefreshIcon();
		
//		String GetBrandValueNoContent = localAttributePage.getBrandValue().trim();
		String GetItemValueNoContent = localAttributePage.getItemValue().trim();
//		String getItemTypeValueNoContent = localAttributePage.getItemTypeValue().trim();
		
		productDetailSearchPage.selectTabfromDropdown(map.get("ContinueTabName"));
		productDetailSearchPage.clickRefreshIcon();
		// Checking in Quality Status tab Rules get Failed
		List<String> localmandatoryrule = qualityStatusPage.maximizeQualityStatusTab().sortRulestByLastExecution()
				.getRuleNamesByRuleStatus(map.get("QualityTab_StatusColoum"));
		qualityStatusPage.minimizeQualityStatusTab();

		List<String> myList = new Javautils().readMultipleValuesFromExcel(map.get("LocalAttributeRule"));
		Assertions.assertThat(Javautils.compareList(myList, localmandatoryrule)).isEqualTo(true);

		// Navigating to Master
		pimHomepage.mainMenu().clickRefreshMenuIcon();
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("ContinueCatalogType"))
		.enterHsiItemNumber(map.get("itemNumber"));
		queriesSubMenu.clickSeachButton().clickOnSecondResult().selectTabfromDropdown(map.get("TabName"));
		
		productDetailSearchPage.clickRefreshIcon();

		// Validating Item Number should not present in master With new change
		
//		String MasterBrandValueForNoContent = localAttributePage.getBrandValue().trim();
		String MasterItemValueForNoContent = localAttributePage.getItemValue().trim();
//		String MasterItemTypeValueForNoContent = localAttributePage.getItemTypeValue().trim();
		
//		Assertions.assertThat(MasterBrandValueForNoContent).isNotEqualTo(GetBrandValueNoContent);
		Assertions.assertThat(MasterItemValueForNoContent).isNotEqualTo(GetItemValueNoContent);
//		Assertions.assertThat(MasterItemTypeValueForNoContent).isNotEqualTo(getItemTypeValueNoContent);
		

		// Adding The valid Value in Local Attribute Filed
		pimHomepage.mainMenu().clickRefreshMenuIcon();
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("itemNumber"));
		queriesSubMenu.clickSeachButton();
		productDetailSearchPage.clickOnFirstResult().selectTabfromDropdown(map.get("ContinueTabName")).selectTabfromDropdown(map.get("TabName"));
		localAttributePage.brandFieldAttribute(map.get("LocalAttributeBrandFieldValue"))
				.MandatoryItemFieldAttribute(map.get("LocalAttributeItemFieldValue"))
				.itemTypeFieldAttribute(map.get("LocalAttributeitemTypeFieldValue"))
				.addingMandatoryQuantityFieldAttribute(map.get("LocalAttributeQuantityFieldValue"))
				.selectValuefromSaveDropdown(map.get("saveButton"));

		productDetailSearchPage.clickRefreshIcon();
//		String GetBrandValue = localAttributePage.getBrandValue().trim();
		String GetItemValue = localAttributePage.getItemValue().trim();
		String getItemTypeValue = localAttributePage.getItemTypeValue().trim();
		productDetailSearchPage.selectTabfromDropdown(map.get("ContinueTabName"));
		productDetailSearchPage.clickRefreshIcon();
		// Checkig in Quality Status tab Rules get Failed

		List<String> localmandatoryrulePass = qualityStatusPage.maximizeQualityStatusTab().sortRulestByLastExecution()
				.getRuleNamesByRuleStatus(map.get("QualityTab_StatusColoumPass"));
		qualityStatusPage.minimizeQualityStatusTab();

		List<String> myListPassed = new Javautils().readMultipleValuesFromExcel(map.get("LocalAttributeRulePass"));
		Assertions.assertThat(Javautils.compareList(myListPassed, localmandatoryrulePass)).isEqualTo(true);

		// Navigating to Master
		pimHomepage.mainMenu().clickRefreshMenuIcon();
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("ContinueCatalogType"))
		.enterHsiItemNumber(map.get("itemNumber"));
		queriesSubMenu.clickSeachButton().clickOnSecondResult().selectTabfromDropdown(map.get("TabName"));
		
		productDetailSearchPage.clickRefreshIcon();

		// Validating Item Number should  present in master With new change
		
		String MasterBrandValue = localAttributePage.getBrandValue().trim();
		String MasterItemValue = localAttributePage.getItemValue().trim();
		String MasterItemTypeValue = localAttributePage.getItemTypeValue().trim();
		
//		Assertions.assertThat(MasterBrandValue).isEqualTo(GetBrandValue);
		Assertions.assertThat(MasterItemValue).isEqualTo(GetItemValue);
		Assertions.assertThat(MasterItemTypeValue).isEqualTo(getItemTypeValue);
	
	}


}