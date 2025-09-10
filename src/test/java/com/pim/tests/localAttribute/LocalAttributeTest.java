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
import com.pim.utils.ExcelUtils;
import com.pim.utils.FileUtils;
import com.pim.utils.Javautils;

public class LocalAttributeTest extends BaseTest {
	private LocalAttributeTest() {

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

	// WRFL_028 For US
	@PimFrameworkAnnotation(module = Modules.LOCAL_ATTRIBUTE, category = CategoryType.SMOKE)
	@TestDataSheet(sheetname = TestCaseSheet.Local_Attribute)
	@Test(description = "For US - WRFL_028 | Verify the Entity Rules and Mandatory Field exectued after a Local Attribute change Rules are failedFor US", dataProvider = "getCatalogData", groups = {
			"SMOKE","US","LOCAL_ATTRIBUTE" }, dataProviderClass = DataProviderUtils.class)
	public void Verify_Removing_Mandatory_Field_Local_Attribute_Failed_For_US(Map<String, String> map) {
		

		loginPage.enterUserName(map.get("Username")).enterPassword(map.get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("itemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));

		// Removing Value
		localAttributePage.brandFieldAttribute(map.get("LocalAttributeBrandFieldValueWithNoContent"))
				.MandatoryItemFieldAttribute(map.get("LocalAttributeItemFieldValueWithNoContent"))
				.itemTypeFieldAttribute(map.get("LocalAttributeitemTypeFieldValueWithNoContent"))
				.clearingMandatoryQuantityFieldAttribute().selectValuefromSaveDropdown(map.get("saveButton"));

		productDetailSearchPage.clickRefreshIcon();
		String GetBrandValueForNoContent = localAttributePage.getBrandValue();
		String GetItemValueForNoContent = localAttributePage.getItemValue();
		String GetItemTypeValueForNoContent = localAttributePage.getItemTypeValue();
		
		productDetailSearchPage.selectTabfromDropdown(map.get("ContinueTabName"));
		productDetailSearchPage.clickRefreshIcon();

		List<String> localmandatoryrule = qualityStatusPage.maximizeQualityStatusTab().sortRulestByLastExecution()
				.getRuleNamesByRuleStatus(map.get("QualityTab_StatusColoum"));
		qualityStatusPage.minimizeQualityStatusTab();

		List<String> myList = new Javautils().readMultipleValuesFromExcel(map.get("LocalAttributeRule"));

		Assertions.assertThat(Javautils.compareList(myList, localmandatoryrule)).isEqualTo(true);

		// Navigating to Master	
		pimHomepage.mainMenu().clickRefreshMenuIcon();
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("ContinueCatalogType"))
		.enterHsiItemNumber(map.get("itemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
		
		productDetailSearchPage.clickRefreshIcon();

		productDetailSearchPage.clickRefreshIcon();
		// Validating Item Number should not present in master With new change
		
		String MasterBrandValue = localAttributePage.getBrandValue();
		String MasterItemValue = localAttributePage.getItemValue();
		String MasterItemTypeValue = localAttributePage.getItemTypeValue();
		
		Assertions.assertThat(MasterBrandValue).isNotEqualTo(GetBrandValueForNoContent);
		Assertions.assertThat(MasterItemValue).isNotEqualTo(GetItemValueForNoContent);
		Assertions.assertThat(MasterItemTypeValue).isNotEqualTo(GetItemTypeValueForNoContent);

	}
	
	// WRFL_028 For CA
		@PimFrameworkAnnotation(module = Modules.LOCAL_ATTRIBUTE, category = CategoryType.SMOKE)
		@TestDataSheet(sheetname = TestCaseSheet.Local_Attribute)
		@Test(description = "For CA - WRFL_028 | Verify the Entity Rules and Mandatory Field exectued after a Local Attribute change Rules are failed For CA", dataProvider = "getCatalogData", groups = {
				 "SMOKE","CA","LOCAL_ATTRIBUTE" }, dataProviderClass = DataProviderUtils.class)
		public void Verify_Removing_Mandatory_Field_Local_Attribute_Failed_For_CA(Map<String, String> map) {
			
			loginPage.enterUserName(map.get("Username")).enterPassword(map.get("Password")).clickLoginButton();
			pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
					.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("itemNumber")).clickSeachButton()
					.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));

			// Removing Value
			localAttributePage.brandFieldAttribute(map.get("LocalAttributeBrandFieldValueWithNoContent"))
					.MandatoryItemFieldAttribute(map.get("LocalAttributeItemFieldValueWithNoContent"))
					.itemTypeFieldAttribute(map.get("LocalAttributeitemTypeFieldValueWithNoContent"))
					.clearingMandatoryQuantityFieldAttribute().selectValuefromSaveDropdown(map.get("saveButton"));

			productDetailSearchPage.clickRefreshIcon();
			
			String GetBrandValueForNoContent = localAttributePage.getBrandValue();
			String GetItemValueForNoContent = localAttributePage.getItemValue();
			String GetItemTypeValueForNoContent = localAttributePage.getItemTypeValue();
			
			productDetailSearchPage.selectTabfromDropdown(map.get("ContinueTabName"));
			productDetailSearchPage.clickRefreshIcon();

			List<String> localmandatoryrule = qualityStatusPage.maximizeQualityStatusTab().sortRulestByLastExecution()
					.getRuleNamesByRuleStatus(map.get("QualityTab_StatusColoum"));
			qualityStatusPage.minimizeQualityStatusTab();

			List<String> myList = new Javautils().readMultipleValuesFromExcel(map.get("LocalAttributeRule"));

			Assertions.assertThat(Javautils.compareList(myList, localmandatoryrule)).isEqualTo(true);

			// Navigating to Master
	        pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("ContinueCatalogType"))
			.enterHsiItemNumber(map.get("itemNumber")).clickSeachButton().clickOnSecondResult().selectTabfromDropdown(map.get("TabName"));
//	        mainMenu.clickRefreshMenuIcon();
//	        queriesSubMenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("ContinueCatalogType"))
//			.enterHsiItemNumber(map.get("itemNumber")).clickSeachButton().clickOnSecondResult().selectTabfromDropdown(map.get("TabName"));
			
			productDetailSearchPage.clickRefreshIcon();

			// Validating Item Number should not present in master With new change
			
			String MasterBrandValue = localAttributePage.getBrandValue();
			String MasterItemValue = localAttributePage.getItemValue();
			String MasterItemTypeValue = localAttributePage.getItemTypeValue();
			
			//Assertions.assertThat(MasterBrandValue).isNotEqualTo(GetBrandValueForNoContent);
			Assertions.assertThat(MasterItemValue).isNotEqualTo(GetItemValueForNoContent);
			//Assertions.assertThat(MasterItemTypeValue).isNotEqualTo(GetItemTypeValueForNoContent);

		}

	// WRFL_029,WRFL_071 for US
	@PimFrameworkAnnotation(module = Modules.LOCAL_ATTRIBUTE, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Local_Attribute)
	@Test(description = "For US - WRFL_029,WRFL_071 | Verify the Mandatory field exectued after a Local Attribute change Rules are passed for US", dataProvider = "getCatalogData", groups = {
			"US" ,"LOCAL_ATTRIBUTE"}, dataProviderClass = DataProviderUtils.class)
	public void Verify_Adding_Mandatory_Field_Local_Attribute_Passed_For_US(Map<String, String> map) {
		
		loginPage.enterUserName(map.get("Username")).enterPassword(map.get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("itemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
		// Adding Value
		localAttributePage.brandFieldAttribute(map.get("LocalAttributeBrandFieldValue"))
				.MandatoryItemFieldAttribute(map.get("LocalAttributeItemFieldValue"))
				.itemTypeFieldAttribute(map.get("LocalAttributeitemTypeFieldValue"))
				.addingMandatoryQuantityFieldAttribute(map.get("LocalAttributeQuantityFieldValue"))
				.selectValuefromSaveDropdown(map.get("saveButton"));

		productDetailSearchPage.clickRefreshIcon();
		productDetailSearchPage.selectTabfromDropdown(map.get("ContinueTabName"));
		productDetailSearchPage.clickRefreshIcon();

		List<String> localmandatoryrule = qualityStatusPage.maximizeQualityStatusTab().sortRulestByLastExecution()
				.getRuleNamesByRuleStatus(map.get("QualityTab_StatusColoum"));
		qualityStatusPage.minimizeQualityStatusTab();

		System.out.println(localmandatoryrule);
		
		List<String> myList = new Javautils().readMultipleValuesFromExcel(map.get("LocalAttributeRulePass"));
		Assertions.assertThat(Javautils.compareList(myList, localmandatoryrule)).isEqualTo(true);

		// Navigating to Master
		queriesSubMenu.selectCatalogType(map.get("ContinueCatalogType"))
				.removeAndEnterHsiItemNumber(map.get("itemNumber")).clickSeachButton().clickOnFirstResult()
				.selectTabfromDropdown(map.get("TabName"));
		
		productDetailSearchPage.clickRefreshIcon();
		productDetailSearchPage.clickRefreshIcon();

		// Validating Item Number should present in master With new change
		
		String MasterBrandValue = localAttributePage.getBrandValue().trim();
		String MasterItemValue = localAttributePage.getItemValue().trim();
		String MasterItemTypeValue = localAttributePage.getItemTypeValue().trim();
		
		Assertions.assertThat(MasterBrandValue).isEqualTo(map.get("LocalAttributeBrandFieldValue"));
		Assertions.assertThat(MasterItemValue).isEqualTo(map.get("LocalAttributeItemFieldValue"));
		Assertions.assertThat(MasterItemTypeValue).isEqualTo(map.get("LocalAttributeitemTypeFieldValue"));

	}
	
	// WRFL_029,WRFL_071 for CA
	@PimFrameworkAnnotation(module = Modules.LOCAL_ATTRIBUTE, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Local_Attribute)
	@Test(description = "For CA - WRFL_029,WRFL_071 | Verify the Mandatory field exectued after a Local Attribute change Rules are passed for CA", dataProvider = "getCatalogData", groups = {
			 "CA","LOCAL_ATTRIBUTE" }, dataProviderClass = DataProviderUtils.class)
	public void Verify_Adding_Mandatory_Field_Local_Attribute_Passed_For_CA(Map<String, String> map) {
		
		loginPage.enterUserName(map.get("Username")).enterPassword(map.get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("itemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
		// Adding Value
		localAttributePage.brandFieldAttribute(map.get("LocalAttributeBrandFieldValue"))
				.MandatoryItemFieldAttribute(map.get("LocalAttributeItemFieldValue"))
				.itemTypeFieldAttribute(map.get("LocalAttributeitemTypeFieldValue"))
				.addingMandatoryQuantityFieldAttribute(map.get("LocalAttributeQuantityFieldValue"))
				.selectValuefromSaveDropdown(map.get("saveButton"));

		productDetailSearchPage.clickRefreshIcon();
		productDetailSearchPage.selectTabfromDropdown(map.get("ContinueTabName"));
		productDetailSearchPage.clickRefreshIcon();

		List<String> localmandatoryrule = qualityStatusPage.maximizeQualityStatusTab().sortRulestByLastExecution()
				.getRuleNamesByRuleStatus(map.get("QualityTab_StatusColoum"));
		qualityStatusPage.minimizeQualityStatusTab();

		List<String> myList = new Javautils().readMultipleValuesFromExcel(map.get("LocalAttributeRulePass"));
		Assertions.assertThat(Javautils.compareList(myList, localmandatoryrule)).isEqualTo(true);

		// Navigating to Master
		queriesSubMenu.selectCatalogType(map.get("ContinueCatalogType"))
				.removeAndEnterHsiItemNumber(map.get("itemNumber")).clickSeachButton().clickOnSecondResult()
				.selectTabfromDropdown(map.get("TabName"));
		
		productDetailSearchPage.clickRefreshIcon();
		
		productDetailSearchPage.clickRefreshIcon();

		// Validating Item Number should present in master With new change
		
		String MasterBrandValue = localAttributePage.getBrandValue().trim();
		String MasterItemValue = localAttributePage.getItemValue().trim();
		String MasterItemTypeValue = localAttributePage.getItemTypeValue().trim();
		
		Assertions.assertThat(MasterBrandValue).isEqualTo(map.get("LocalAttributeBrandFieldValue"));
		Assertions.assertThat(MasterItemValue).isEqualTo(map.get("LocalAttributeItemFieldValue"));
		Assertions.assertThat(MasterItemTypeValue).isEqualTo(map.get("LocalAttributeitemTypeFieldValue"));

	}

	// FDM_19,FDM_20,FDM_21
	@PimFrameworkAnnotation(module = Modules.LOCAL_ATTRIBUTE, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Local_Attribute)
	@Test(description = "FDM_19,FDM_20,FDM_21 | Verify the Entity Rules exectued after a Local Attribute change", groups = {
			"CA","LOCAL_ATTRIBUTE" }, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void Verify_UI_Validation_For_Quantity_Field_in_Local_Attribute(Map<String, String> map) {
		
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("itemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
		localAttributePage.brandFieldAttribute(map.get("LocalAttributeBrandFieldValue"));

		Assertions.assertThat(localAttributePage.isLocalAttributeFieldVisible("quantityInputField")).isTrue();
		localAttributePage.selectLanguageFieldDropdown(map.get("LocalAttributeLaunguageField")).selectValuefromSaveDropdown(map.get("saveButton"));

		Assertions.assertThat(localAttributePage.isLocalAttributeFieldEditable("QuantiteFieldFrench")).isTrue();

		localAttributePage.addingMandatoryQuantityFieldAttributeForFrench(map.get("LocalAttributeQuantityFieldValue"))
				.selectValuefromSaveDropdown(map.get("saveButton"));
		productDetailSearchPage.clickRefreshIcon();
		productDetailSearchPage.selectTabfromDropdown(map.get("ContinueTabName"));
		productDetailSearchPage.clickRefreshIcon();
		List<String> dqRuleForQuanityField = qualityStatusPage.maximizeQualityStatusTab().sortRulestByLastExecution()
				.getRuleNamesByRuleStatus(map.get("QualityTab_StatusColoum"));
		qualityStatusPage.minimizeQualityStatusTab();

		List<String> myList = new Javautils().readMultipleValuesFromExcel(map.get("LocalAttributeRulePass"));

		Assertions.assertThat(Javautils.compareList(myList, dqRuleForQuanityField)).isEqualTo(true);

	}

	// INT_010 For US
	@PimFrameworkAnnotation(module = Modules.LOCAL_ATTRIBUTE, category = CategoryType.SMOKE)
	@TestDataSheet(sheetname = TestCaseSheet.Local_Attribute)
	@Test(description = "For US - INT_010 | Verify the Removing And Adding Mandatory Field values In Local Attribute for US", dataProvider = "getCatalogData", groups = {
			"SMOKE","US","LOCAL_ATTRIBUTE" }, dataProviderClass = DataProviderUtils.class)
	public void Verify_Removing_Mandatory_Field_And_Adding_Mandatory_Field_In_Local_Attribute_For_US(Map<String, String> map) {
		
		loginPage.enterUserName(map.get("Username")).enterPassword(map.get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("itemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
		// Removing Value
		qualityStatusPage.maximizeQualityStatusTab();
		localAttributePage.brandFieldAttribute(map.get("LocalAttributeBrandFieldValueWithNoContent"))
				.MandatoryItemFieldAttribute(map.get("LocalAttributeItemFieldValueWithNoContent"))
				.itemTypeFieldAttribute(map.get("LocalAttributeitemTypeFieldValueWithNoContent"))
				.clearingMandatoryQuantityFieldAttribute().selectValuefromSaveDropdown(map.get("saveButton"));
		qualityStatusPage.minimizeQualityStatusTab();

		productDetailSearchPage.clickRefreshIcon();
		
		String GetBrandValueNoContent = localAttributePage.getBrandValue().trim();
		String GetItemValueNoContent = localAttributePage.getItemValue().trim();
		String getItemTypeValueNoContent = localAttributePage.getItemTypeValue().trim();
		
		productDetailSearchPage.selectTabfromDropdown(map.get("ContinueTabName"));
		productDetailSearchPage.clickRefreshIcon();
		// Checkig in Quality Status tab Rules get Failed
		List<String> localmandatoryrule = qualityStatusPage.maximizeQualityStatusTab().sortRulestByLastExecution()
				.getRuleNamesByRuleStatus(map.get("QualityTab_StatusColoum"));
		qualityStatusPage.minimizeQualityStatusTab();

		List<String> myList = new Javautils().readMultipleValuesFromExcel(map.get("LocalAttributeRule"));
		Assertions.assertThat(Javautils.compareList(myList, localmandatoryrule)).isEqualTo(true);

		// Navigating to Master
		pimHomepage.mainMenu().clickRefreshMenuIcon();
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("ContinueCatalogType"))
		.enterHsiItemNumber(map.get("itemNumber"));
		queriesSubMenu.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
		
		productDetailSearchPage.clickRefreshIcon();

		// Validating Item Number should not present in master With new change
		
		String MasterBrandValueForNoContent = localAttributePage.getBrandValue().trim();
		String MasterItemValueForNoContent = localAttributePage.getItemValue().trim();
		String MasterItemTypeValueForNoContent = localAttributePage.getItemTypeValue().trim();
		
		Assertions.assertThat(MasterBrandValueForNoContent).isNotEqualTo(GetBrandValueNoContent);
		Assertions.assertThat(MasterItemValueForNoContent).isNotEqualTo(GetItemValueNoContent);
		Assertions.assertThat(MasterItemTypeValueForNoContent).isNotEqualTo(getItemTypeValueNoContent);
		

		// Adding The valid Value in Local Attribute Filed
		pimHomepage.mainMenu().clickRefreshMenuIcon();
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("itemNumber"));
		queriesSubMenu.clickSeachButton();
		productDetailSearchPage.clickOnFirstResult().selectTabfromDropdown(map.get("ContinueTabName")).selectTabfromDropdown(map.get("TabName"));
		qualityStatusPage.maximizeQualityStatusTab();
		localAttributePage.brandFieldAttribute(map.get("LocalAttributeBrandFieldValue"))
				.MandatoryItemFieldAttribute(map.get("LocalAttributeItemFieldValue"))
				.itemTypeFieldAttribute(map.get("LocalAttributeitemTypeFieldValue"))
				.addingMandatoryQuantityFieldAttribute(map.get("LocalAttributeQuantityFieldValue"))
				.selectValuefromSaveDropdown(map.get("saveButton"));
		qualityStatusPage.minimizeQualityStatusTab();

		productDetailSearchPage.clickRefreshIcon();
		String GetBrandValue = localAttributePage.getBrandValue().trim();
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
		queriesSubMenu.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
		
		productDetailSearchPage.clickRefreshIcon();

		// Validating Item Number should  present in master With new change
		
		String MasterBrandValue = localAttributePage.getBrandValue().trim();
		String MasterItemValue = localAttributePage.getItemValue().trim();
		String MasterItemTypeValue = localAttributePage.getItemTypeValue().trim();
		
		Assertions.assertThat(MasterBrandValue).isEqualTo(GetBrandValue);
		Assertions.assertThat(MasterItemValue).isEqualTo(GetItemValue);
		Assertions.assertThat(MasterItemTypeValue).isEqualTo(getItemTypeValue);
	
	}
	
	// INT_010 For CA
	@PimFrameworkAnnotation(module = Modules.LOCAL_ATTRIBUTE, category = CategoryType.SMOKE)
	@TestDataSheet(sheetname = TestCaseSheet.Local_Attribute)
	@Test(description = "For CA - INT_010 | Verify the Removing And Adding Mandatory Field values In Local Attribute for CA", dataProvider = "getCatalogData", groups = {
			"SMOKE","CA","LOCAL_ATTRIBUTE" }, dataProviderClass = DataProviderUtils.class)
	public void Verify_Removing_Mandatory_Field_And_Adding_Mandatory_Field_In_Local_Attribute_For_CA(Map<String, String> map) {
		
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

	// pre-requisite MDLW_023
	@PimFrameworkAnnotation(module = Modules.LOCAL_ATTRIBUTE, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Local_Attribute)
	@Test(description = "MDLW_023 | Verify local attribute changes for a Dental item updated in respective table when the items local attribute are modified in PIM --Pre-Requisite ", dataProvider = "getCatalogData",
	groups = {"US","pim","LOCAL_ATTRIBUTE"}, dataProviderClass = DataProviderUtils.class)
	public void verify_localAttribute_changes_For_DentalItem_updated_In_RespectiveTable(
			Map<String, String> map) {
		
		FileUtils.storeCurrentTimeInJsonTimesPropertiesFile(map.get("TestCaseName")+map.get("itemNumber"));

		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Dental User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Dental User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("itemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
		
        productDetailSearchPage.maximizeProductDetailTab();

		localAttributePage.brandFieldAttribute(map.get("LocalAttributeBrandFieldValueWithNoContent"))
		.MandatoryItemFieldAttribute(map.get("LocalAttributeItemFieldValueWithNoContent"))
		.itemTypeFieldAttribute(map.get("LocalAttributeitemTypeFieldValueWithNoContent"))
		.selectValuefromSaveDropdown(map.get("saveButton"));
		productDetailSearchPage.clickRefreshIcon();
		
        BasePage.WaitForMiliSec(5000);
		// Adding The valid Value in Local Attribute Filed
		localAttributePage.brandFieldAttribute(map.get("LocalAttributeBrandFieldValue"))
		.MandatoryItemFieldAttribute(map.get("LocalAttributeItemFieldValue"))
		.itemTypeFieldAttribute(map.get("LocalAttributeitemTypeFieldValue"))
		.selectValuefromSaveDropdown(map.get("saveButton"));
		productDetailSearchPage.clickRefreshIcon();
		
		String itemValue = localAttributePage.getItemValue().trim();
		Assertions.assertThat(itemValue).isEqualTo(map.get("LocalAttributeItemFieldValue"));
		String itemTypeValue = localAttributePage.getItemTypeValue().trim();
		Assertions.assertThat(itemTypeValue).isEqualTo(map.get("LocalAttributeitemTypeFieldValue"));
		String brandValue = localAttributePage.getBrandValue().trim();
		Assertions.assertThat(brandValue).isEqualTo(map.get("LocalAttributeBrandFieldValue"));
		

	}

	// pre-requisite MDLW_024
	@PimFrameworkAnnotation(module = Modules.LOCAL_ATTRIBUTE, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Local_Attribute)
	@Test(description = "MDLW_024 | Verify local attribute changes for a Medical item updated in respective table when the items local attribute are modified in PIM pre-requisite",groups = {"US","pim","LOCAL_ATTRIBUTE"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verify_localAttribute_changes_For_Medical_Item_updated_In_RespectiveTable(
			Map<String, String> map) {
		
		FileUtils.storeCurrentTimeInJsonTimesPropertiesFile(map.get("TestCaseName")+map.get("itemNumber"));

		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("itemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
        productDetailSearchPage.maximizeProductDetailTab();

		// Adding The valid Value in Local Attribute Filed
		localAttributePage.brandFieldAttribute(map.get("LocalAttributeBrandFieldValueWithNoContent"))
		.MandatoryItemFieldAttribute(map.get("LocalAttributeItemFieldValueWithNoContent"))
		.itemTypeFieldAttribute(map.get("LocalAttributeitemTypeFieldValueWithNoContent"))
		.selectValuefromSaveDropdown(map.get("saveButton"));
		productDetailSearchPage.clickRefreshIcon();
		
		BasePage.WaitForMiliSec(5000);
		productDetailSearchPage.clickRefreshIcon();
		BasePage.WaitForMiliSec(2000);
		// Adding The valid Value in Local Attribute Filed
		localAttributePage.brandFieldAttribute(map.get("LocalAttributeBrandFieldValue"))
		.MandatoryItemFieldAttribute(map.get("LocalAttributeItemFieldValue"))
		.itemTypeFieldAttribute(map.get("LocalAttributeitemTypeFieldValue"))
		.selectValuefromSaveDropdown(map.get("saveButton"));
		productDetailSearchPage.clickRefreshIcon();
		
		String itemValue = localAttributePage.getItemValue().trim();
		Assertions.assertThat(itemValue).isEqualTo(map.get("LocalAttributeItemFieldValue"));
		String itemTypeValue = localAttributePage.getItemTypeValue().trim();
		Assertions.assertThat(itemTypeValue).isEqualTo(map.get("LocalAttributeitemTypeFieldValue"));
		String brandValue = localAttributePage.getBrandValue().trim();
		Assertions.assertThat(brandValue).isEqualTo(map.get("LocalAttributeBrandFieldValue"));
	}

	// pre-requisite MDLW_025
	@PimFrameworkAnnotation(module = Modules.LOCAL_ATTRIBUTE, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Local_Attribute)
	@Test(description = "MDLW_025 | Verify local attribute changes for a SpecialMarket item updated in respective table when the items local attribute are modified in PIM  pre-requisite", groups = {"US","pim","LOCAL_ATTRIBUTE"},dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verify_localAttribute_changes_For_SpecialMarketItem_updated_In_RespectiveTable_Item(
			Map<String, String> map) {

		FileUtils.storeCurrentTimeInJsonTimesPropertiesFile(map.get("TestCaseName")+map.get("itemNumber"));

		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Special Markets User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Special Markets User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("itemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
        productDetailSearchPage.maximizeProductDetailTab();

		// Adding The valid Value in Local Attribute Filed
		localAttributePage.brandFieldAttribute(map.get("LocalAttributeBrandFieldValueWithNoContent"))
		.MandatoryItemFieldAttribute(map.get("LocalAttributeItemFieldValueWithNoContent"))
		.itemTypeFieldAttribute(map.get("LocalAttributeitemTypeFieldValueWithNoContent"))
		.selectValuefromSaveDropdown(map.get("saveButton"));
		productDetailSearchPage.clickRefreshIcon();
		
        BasePage.WaitForMiliSec(5000);
		// Adding The valid Value in Local Attribute Filed
		localAttributePage.brandFieldAttribute(map.get("LocalAttributeBrandFieldValue"))
		.MandatoryItemFieldAttribute(map.get("LocalAttributeItemFieldValue"))
		.itemTypeFieldAttribute(map.get("LocalAttributeitemTypeFieldValue"))
		.selectValuefromSaveDropdown(map.get("saveButton"));
		productDetailSearchPage.clickRefreshIcon();
		
		String itemValue = localAttributePage.getItemValue().trim();
		Assertions.assertThat(itemValue).isEqualTo(map.get("LocalAttributeItemFieldValue"));
		String itemTypeValue = localAttributePage.getItemTypeValue().trim();
		Assertions.assertThat(itemTypeValue).isEqualTo(map.get("LocalAttributeitemTypeFieldValue"));
		String brandValue = localAttributePage.getBrandValue().trim();
		Assertions.assertThat(brandValue).isEqualTo(map.get("LocalAttributeBrandFieldValue"));

	}

	// pre-requisite MDLW_026
	@PimFrameworkAnnotation(module = Modules.LOCAL_ATTRIBUTE, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Local_Attribute)
	@Test(description = "MDLW_026 | Verify local attribute changes for a Zahn item updated in respective table For Item when the items local attribute are modified in PIM pre-requisite", groups = {"US","pim","LOCAL_ATTRIBUTE"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verify_localAttribute_changes_For_ZahnItem_updated_In_RespectiveTable(
			Map<String, String> map) {

		FileUtils.storeCurrentTimeInJsonTimesPropertiesFile(map.get("TestCaseName")+map.get("itemNumber"));

		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Zahn User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Zahn User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("itemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));

        productDetailSearchPage.maximizeProductDetailTab();

		// Adding The valid Value in Local Attribute Filed
		localAttributePage.brandFieldAttribute(map.get("LocalAttributeBrandFieldValueWithNoContent"))
		.MandatoryItemFieldAttribute(map.get("LocalAttributeItemFieldValueWithNoContent"))
		.itemTypeFieldAttribute(map.get("LocalAttributeitemTypeFieldValueWithNoContent"))
		.selectValuefromSaveDropdown(map.get("saveButton"));
		productDetailSearchPage.clickRefreshIcon();
		
        BasePage.WaitForMiliSec(5000);
		// Adding The valid Value in Local Attribute Filed
		localAttributePage.brandFieldAttribute(map.get("LocalAttributeBrandFieldValue"))
		.MandatoryItemFieldAttribute(map.get("LocalAttributeItemFieldValue"))
		.itemTypeFieldAttribute(map.get("LocalAttributeitemTypeFieldValue"))
		.selectValuefromSaveDropdown(map.get("saveButton"));
		productDetailSearchPage.clickRefreshIcon();
		
		String itemValue = localAttributePage.getItemValue().trim();
		Assertions.assertThat(itemValue).isEqualTo(map.get("LocalAttributeItemFieldValue"));
		String itemTypeValue = localAttributePage.getItemTypeValue().trim();
		Assertions.assertThat(itemTypeValue).isEqualTo(map.get("LocalAttributeitemTypeFieldValue"));
		String brandValue = localAttributePage.getBrandValue().trim();
		Assertions.assertThat(brandValue).isEqualTo(map.get("LocalAttributeBrandFieldValue"));

	}

	// pre-requisite INT_012
	@PimFrameworkAnnotation(module = Modules.LOCAL_ATTRIBUTE, category = CategoryType.SMOKE)
	@TestDataSheet(sheetname = TestCaseSheet.Local_Attribute)
	@Test(description = "INT_012 | Verify local attribute changes and Medical userdriven changes reflecting into DB pre-requisite",
		groups = {"SMOKE","US","pim","LOCAL_ATTRIBUTE"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verify_the_ItemChanges_In_PIM_reflected_In_Middleware_DB_Test(Map<String, String> map) {
		
		CatalogTypePage catalogTypePage = new CatalogTypePage();
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("itemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));

		// Adding The LOV in Local Attribute Tab Field
		localAttributePage.brandFieldAttribute(map.get("LocalAttributeBrandFieldValue"))
		.MandatoryItemFieldAttribute(map.get("LocalAttributeItemFieldValue"))
		.itemTypeFieldAttribute(map.get("LocalAttributeitemTypeFieldValue"))
		.selectValuefromSaveDropdown(map.get("saveButton"));

		productDetailSearchPage.clickRefreshIcon();
		// selecting user driven field
		productDetailSearchPage.clickRefreshIcon().selectTabfromDropdown(map.get("ContinueTabName"));
		catalogTypePage.clearUserDriven(map.get("UserDriven"));
		BasePage.WaitForMiliSec(3000);
		catalogTypePage.clickUserDriven().selectUserDriven(map.get("UserDriven"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();

	}

}