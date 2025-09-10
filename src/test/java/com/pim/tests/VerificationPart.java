package com.pim.tests;

import static com.pim.reports.FrameworkLogger.log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.assertj.core.api.Assertions;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.pim.annotations.PimFrameworkAnnotation;
import com.pim.annotations.TestDataSheet;
import com.pim.enums.CategoryType;
import com.pim.enums.LogType;
import com.pim.enums.Modules;
import com.pim.enums.TestCaseSheet;
import com.pim.listeners.RetryFailedTests;
import com.pim.pages.AllCatalogsPage;
import com.pim.pages.BasePage;
import com.pim.pages.CatalogTypePage;
import com.pim.pages.CatalogAssignmentPage;
import com.pim.pages.ClassificationsPage;
import com.pim.pages.GlobalAttributePage;
import com.pim.pages.LocalAttributePage;
import com.pim.pages.LoginPage;
import com.pim.pages.MainMenu;
import com.pim.pages.PimHomepage;
import com.pim.pages.ProductDetailSearchPage;
import com.pim.pages.QualityStatusPage;
import com.pim.pages.QueriesSubMenu;
import com.pim.pages.ReferencesPage;
import com.pim.pages.StructureSubMenu;
import com.pim.tests.BaseTest;
import com.pim.utils.DataProviderUtils;
import com.pim.utils.DateandTimeUtils;
import com.pim.utils.ExcelUtils;
import com.pim.utils.Javautils;

import com.pim.annotations.PimFrameworkAnnotation;
import com.pim.annotations.TestDataSheet;
import com.pim.driver.DriverManager;
import com.pim.enums.CategoryType;
import com.pim.enums.LogType;
import com.pim.enums.Modules;
import com.pim.enums.TestCaseSheet;
import com.pim.pages.ActionsPage;
import com.pim.pages.AllCatalogsPage;
import com.pim.pages.BasePage;
import com.pim.pages.CatalogTypePage;
import com.pim.pages.ClassificationsPage;
import com.pim.pages.FieldSelectionPage;
import com.pim.pages.GEP_WebDescriptionPage;
import com.pim.pages.GlobalAttributePage;
import com.pim.pages.LocalAttributePage;
import com.pim.pages.LoginPage;
import com.pim.pages.MainMenu;
import com.pim.pages.PimHomepage;
import com.pim.pages.ProductDetailSearchPage;
import com.pim.pages.QualityStatusPage;
import com.pim.pages.QueriesSubMenu;
import com.pim.pages.ReferencesPage;
import com.pim.pages.SeoTabPage;
import com.pim.pages.StructureSubMenu;
import com.pim.pages.TasksSubMenu;
import com.pim.pages.TextPage;
import com.pim.pages.WebDescription;
import com.pim.pages.us.CreateItemValidationPage;
import com.pim.pages.us.ItemPublishDateAndFlagPage;
import com.pim.tests.BaseTest;
import com.pim.utils.ApplicationUtils;
import com.pim.utils.DataProviderUtils;
import com.pim.utils.DateandTimeUtils;
import com.pim.utils.ExcelUtils;
import com.pim.utils.Javautils;
import org.assertj.core.api.Assertions;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.Assertion;
import org.testng.asserts.SoftAssert;

import java.awt.AWTException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import static com.pim.reports.FrameworkLogger.log;



public class VerificationPart extends BaseTest {
	private VerificationPart() {

	}
	
//	PART_1: DENTAL CATALOG TESTS
//	PART_3: SPECIAL MARKET TESTS
//	PART_4: CatalogAssignmentBasedOnDivisionalItemRuleTest

	

	CatalogTypePage catalogTypePage = new CatalogTypePage();
	AllCatalogsPage allCatalogsPage = new AllCatalogsPage();
	QualityStatusPage qualitystatus = new QualityStatusPage();
	ProductDetailSearchPage productdetailspage = new ProductDetailSearchPage();
	QueriesSubMenu queriesSubMenu = new QueriesSubMenu();
	PimHomepage pimHomepage = new PimHomepage();
	LoginPage loginPage = new LoginPage();
	//List<String> rule_driven;
	List<String> user_driven;
	List<String> all_dental_catalog;
	List<String> user_and_rule_driven = new ArrayList<>();
	Javautils javautils = new Javautils();

	// CATR_077 to 081 and 013
	@PimFrameworkAnnotation(module = Modules.DENTAL_CATALOG, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Catalog)
	@Test(priority = 1, description = "CATR_077, Verification Part | CATR_077, CATR_078, CATR_079, CATR_080, CATR_081, CATR_013 | Verifying Rule Driven catalog for Dental Division in US Master Catalog", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			"US", "pim","DENTAL_CATALOG" })
	public void verify_rule_driven_for_dental_division(Map<String, String> map) {

		// CatalogTypePage catalogTypePage = new CatalogTypePage();
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Dental User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Dental User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));

//		catalogTypePage.clearUserDriven(map.get("userDriven"));
//
//		BasePage.WaitForMiliSec(3000);
//		catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
//		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		List<String> rule_driven = catalogTypePage.getRuleDrivenCatalogs();
		Assertions.assertThat(rule_driven).containsAnyOf(map.get("RuleDriven"));

		// verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus"))
				.maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
		for (String Rules : javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule"))) {
			Assertions.assertThat(catalogentityrule).contains(Rules);
		}
		qualitystatus.minimizeQualityStatusTab();
		log(LogType.INFO, "Verifying catalog entity rule");

		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
		List<String> masterRuledriven = catalogTypePage.getRuleDrivenCatalogs();
		Assertions.assertThat(rule_driven).containsAll(masterRuledriven);
		pimHomepage.clickLogoutButton();
	}

	// CATR_077 to 081
	@PimFrameworkAnnotation(module = Modules.DENTAL_CATALOG, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Catalog)
	@Test(priority = 1, description = "CATR_077, Verification Part | CATR_077, CATR_078, CATR_079, CATR_080, CATR_081 | Verifying Rule Driven catalog for Dental Division in CA and Master Catalog", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			"CA", "pim","DENTAL_CATALOG" })

	public void verify_rule_driven_for_dental_division_CA(Map<String, String> map) {

		// CatalogTypePage catalogTypePage = new CatalogTypePage();
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));

//		List<String> userdrivencatalogs = catalogTypePage.getUserDrivenCatalogs();
//		List<String> multiplevaluesfromexcel = javautils.readMultipleValuesFromExcel(map.get("userDriven"));
//		for (String userdriven : multiplevaluesfromexcel) {
//			if (userdrivencatalogs.contains(userdriven)) {
//				// delete userdriven
//				catalogTypePage.clearSelectedFields(userdriven);
//				productdetailspage.clickRefreshIcon();
//				log(LogType.INFO, "Deleting the Userdriven");
//			}
//		}
//		BasePage.WaitForMiliSec(3000);
//
//		catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
//		pimHomepage.productDetailSearchPage().clickRefreshIcon();
//		pimHomepage.productDetailSearchPage().clickRefreshIcon();
//		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		List<String> rule_driven = catalogTypePage.getRuleDrivenCatalogs();
		Assertions.assertThat(rule_driven).containsAnyOf(map.get("RuleDriven"));

		// verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus"))
				.maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
		for (String Rules : javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule"))) {
			Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);
		}
		qualitystatus.minimizeQualityStatusTab();
		log(LogType.INFO, "Verifying catalog entity rule");

		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
				.clickOnSecondResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
		List<String> masterRuledriven = catalogTypePage.getRuleDrivenCatalogs();
		Assertions.assertThat(rule_driven).containsAll(masterRuledriven);
		pimHomepage.clickLogoutButton();
	}

	// CATR_179 and CATR_180, CATR_132
	@PimFrameworkAnnotation(module = Modules.DENTAL_CATALOG, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Catalog)
	@Test(priority = 1, description = "CATR_179 Verification Part | CATR_179 and CATR_180, CATR_132 | Verify All catalog tabs and exception lists for Dental Catalog list populates the catalogs with User, Rule Driven and excluding the Catalogs selected as Exception  For US", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			"US", "pim","DENTAL_CATALOG" })
	public void verify_all_catalog_tabs_and_exception_list_for_dental_division(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Dental User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Dental User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));

		user_driven = catalogTypePage.getUserDrivenCatalogs();
		List<String> rule_driven = catalogTypePage.getRuleDrivenCatalogs();

		user_and_rule_driven.addAll(rule_driven);
		user_and_rule_driven.addAll(user_driven); // took user and rule driven
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("AllCatalogtab")).clickRefreshIcon();
		all_dental_catalog = allCatalogsPage.getAllDentalCatalogList();
		Assertions.assertThat(user_and_rule_driven).containsAll(all_dental_catalog);// verified all catalog without
																					// exception

		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("AllCatalogtab")).clickRefreshIcon();
		List<String> master_all_dental_catalog = allCatalogsPage.getAllDentalCatalogList();
		// verified all catalog tab in master
		Assertions.assertThat(user_and_rule_driven).containsAll(master_all_dental_catalog);

		// verifying with exception list
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
		List<String> userdrivencatalogs = catalogTypePage.getExceptionListCatalogs();
		List<String> multiplevaluesfromexcel = javautils.readMultipleValuesFromExcel(map.get("userDriven"));
//		for (String userdriven : multiplevaluesfromexcel) {
//			if (userdrivencatalogs.contains(userdriven)) {
//				// delete userdriven
//				catalogTypePage.clearExceptionListField(userdriven);
//				productdetailspage.clickRefreshIcon();
//				log(LogType.INFO, "Deleting the Userdriven");
//			}
//		}
//		BasePage.WaitForMiliSec(3000);
//		catalogTypePage.clickExceptionListField().selectExceptionCatalogsFromList(map.get("userDriven"));
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("AllCatalogtab")).clickRefreshIcon();

		List<String> all_catalog_except_exceptionallist = allCatalogsPage.getAllDentalCatalogList();
		Assertions.assertThat(all_catalog_except_exceptionallist).isNotEqualTo(user_and_rule_driven);

		// verifying with exception list in master catalog
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon()
				.selectTabfromDropdown(map.get("AllCatalogtab"));
		List<String> master_exception_alldental__catalog = allCatalogsPage.getAllDentalCatalogList();

		Assertions.assertThat(user_and_rule_driven).isNotEqualTo(master_exception_alldental__catalog);
		Assertions.assertThat(all_catalog_except_exceptionallist).containsAll(master_exception_alldental__catalog);
		pimHomepage.clickLogoutButton();
	}

	// CATR_179 and CATR_180, CATR_132
	@PimFrameworkAnnotation(module = Modules.DENTAL_CATALOG, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Catalog)
	@Test(priority = 1, description = "CATR_179 Verification Part | CATR_179 and CATR_180, CATR_132 | Verify All catalog tabs and exception lists for Dental Catalog list populates the catalogs with User, Rule Driven and excluding the Catalogs selected as Exception For CA", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			"CA", "pim","DENTAL_CATALOG" })
	public void verify_all_catalog_tabs_and_exception_list_for_dental_division_CA(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));

		user_driven = catalogTypePage.getUserDrivenCatalogs();
		List<String> rule_driven = catalogTypePage.getRuleDrivenCatalogs();

		user_and_rule_driven.addAll(rule_driven);
		user_and_rule_driven.addAll(user_driven); // took user and rule driven
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("AllCatalogtab")).clickRefreshIcon();
		all_dental_catalog = allCatalogsPage.getAllDentalCatalogList();
		Assertions.assertThat(user_and_rule_driven).containsAll(all_dental_catalog);// verified all catalog without
																					// exception

		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("AllCatalogtab")).clickRefreshIcon();
		List<String> master_all_dental_catalog = allCatalogsPage.getAllDentalCatalogList();
		// verified all catalog tab in master
		Assertions.assertThat(user_and_rule_driven).containsAll(master_all_dental_catalog);

		// verifying with exception list
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
		List<String> userdrivencatalogs = catalogTypePage.getExceptionListCatalogs();
		List<String> multiplevaluesfromexcel = javautils.readMultipleValuesFromExcel(map.get("userDriven"));
//		for (String userdriven : multiplevaluesfromexcel) {
//			if (userdrivencatalogs.contains(userdriven)) {
//				// delete userdriven
//				catalogTypePage.clearExceptionListField(userdriven);
//				productdetailspage.clickRefreshIcon();
//				log(LogType.INFO, "Deleting the Userdriven");
//			}
//		}
//		BasePage.WaitForMiliSec(3000);
//		catalogTypePage.clickExceptionListField().selectExceptionCatalogsFromList(map.get("userDriven"));
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("AllCatalogtab")).clickRefreshIcon();

		List<String> all_catalog_except_exceptionallist = allCatalogsPage.getAllDentalCatalogList();
		Assertions.assertThat(all_catalog_except_exceptionallist).isNotEqualTo(user_and_rule_driven);

		// verifying with exception list in master catalog
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon()
				.selectTabfromDropdown(map.get("AllCatalogtab"));
		List<String> master_exception_alldental__catalog = allCatalogsPage.getAllDentalCatalogList();

		Assertions.assertThat(user_and_rule_driven).isNotEqualTo(master_exception_alldental__catalog);
		Assertions.assertThat(all_catalog_except_exceptionallist).containsAll(master_exception_alldental__catalog);
		pimHomepage.clickLogoutButton();
	}

	// CATR_001,CATR_002,CATR_003,CATR_004,CATR_005,CATR_006,CATR_007,CATR_008,CATR_009,CATR_010,CATR_013,CATR_017
	@PimFrameworkAnnotation(module = Modules.DENTAL_CATALOG, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.UI_Validation_Catalogs_Field)
	@Test(priority = 1, description = "CATR_001,Verification Part | CATR_001,CATR_002,CATR_003,CATR_004,CATR_005,CATR_006,CATR_007,CATR_008,CATR_009,CATR_010,CATR_013,CATR_017 |Dental Catalog Verifying rule driven, userdriven, exception list, publish date, publish flag are visible and editable In US", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			"US", "pim", "SANITY","DENTAL_CATALOG" })
	public void UI_validation_Dental_catalog_In_US(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("Dental User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Dental User").get("Password")).clickLoginButton();
				pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));

		// Validating on "Dental catalog"
		Assertions.assertThat(catalogTypePage.isCatalogFieldVisible("tabDentalCatalog")).isTrue();

		Assertions.assertThat(catalogTypePage.isCatalogFieldVisible("RuleDrivenField")).isTrue();
		Assertions.assertThat(catalogTypePage.isCatalogFieldVisible("userDrivenField")).isTrue();
		Assertions.assertThat(catalogTypePage.isCatalogFieldVisible("exceptionListField")).isTrue();
		Assertions.assertThat(catalogTypePage.isCatalogFieldVisible("publishFlagField")).isTrue();
		Assertions.assertThat(catalogTypePage.isCatalogFieldVisible("publishDateField")).isTrue();

		Assertions.assertThat(catalogTypePage.isCatalogFieldEditable("RuleDrivenField", "FieldDropDown")).isFalse();
		Assertions.assertThat(catalogTypePage.isCatalogFieldEditable("userDrivenField", "FieldDropDown")).isTrue();
		Assertions.assertThat(catalogTypePage.isCatalogFieldEditable("exceptionListField", "FieldDropDown")).isTrue();
		Assertions.assertThat(catalogTypePage.isCatalogFieldEditable("publishFlagField", "FieldDropDown")).isTrue();
		Assertions.assertThat(catalogTypePage.isCatalogFieldEditable("publishDateField", "PublishDateFieldDropDown"))
				.isTrue();
		catalogTypePage.verifySelectUserdrivenDropdown().verifySelectExceptionListDropdown()
				.verifySelectPublishFlagFieldDropdown().verifySelectPublishDateFieldDropdown();

		// Validating on "All catalog Tab" for dental user ProductDetailSearchPage

		productdetailspage.selectTabfromDropdown(map.get("ContinueTabName"));

		// AllCatalogsPage allCatalogsPage = new AllCatalogsPage();

		Assertions.assertThat(allCatalogsPage.isCatalogFieldVisible("tabAllCatalogs")).isTrue();

		Assertions.assertThat(allCatalogsPage.isCatalogFieldVisible("fieldDentalCatalogListUI")).isTrue();
		Assertions.assertThat(allCatalogsPage.isCatalogFieldVisible("fieldMedicalCatalogListUI")).isTrue();
		Assertions.assertThat(allCatalogsPage.isCatalogFieldVisible("fieldZahnCatalogListUI")).isTrue();
		Assertions.assertThat(allCatalogsPage.isCatalogFieldVisible("fieldSpecialMarketsCatalogListUI")).isTrue();

		Assertions.assertThat(allCatalogsPage.isCatalogFieldEditable("fieldDentalCatalogListUI", "FieldDropDown"))
				.isFalse();
		Assertions.assertThat(allCatalogsPage.isCatalogFieldEditable("fieldMedicalCatalogListUI", "FieldDropDown"))
				.isFalse();
		Assertions.assertThat(allCatalogsPage.isCatalogFieldEditable("fieldZahnCatalogListUI", "FieldDropDown"))
				.isFalse();
		Assertions
				.assertThat(allCatalogsPage.isCatalogFieldEditable("fieldSpecialMarketsCatalogListUI", "FieldDropDown"))
				.isFalse();

		// Validating in master catalog for dental user for "dental catalog tab"

		queriesSubMenu.selectCatalogType(map.get("ContinueCatalogType")).clickSeachButton().clickOnFirstResult()
				.selectTabfromDropdown(map.get("TabName"));

		Assertions.assertThat(catalogTypePage.isCatalogFieldEditable("RuleDrivenField", "FieldDropDown")).isFalse();
		Assertions.assertThat(catalogTypePage.isCatalogFieldEditable("userDrivenField", "FieldDropDown")).isFalse();
		Assertions.assertThat(catalogTypePage.isCatalogFieldEditable("exceptionListField", "FieldDropDown")).isFalse();
		Assertions.assertThat(catalogTypePage.isCatalogFieldEditable("publishFlagField", "FieldDropDown")).isFalse();
		Assertions.assertThat(catalogTypePage.isCatalogFieldEditable("publishDateField", "PublishDateFieldDropDown"))
				.isFalse();

		// Validating in master catalog for dental user for "All catalog tab"
		productdetailspage.selectTabfromDropdown(map.get("ContinueTabName"));

		Assertions.assertThat(allCatalogsPage.isCatalogFieldEditable("fieldDentalCatalogListUI", "FieldDropDown"))
				.isFalse();
		Assertions.assertThat(allCatalogsPage.isCatalogFieldEditable("fieldMedicalCatalogListUI", "FieldDropDown"))
				.isFalse();
		Assertions.assertThat(allCatalogsPage.isCatalogFieldEditable("fieldZahnCatalogListUI", "FieldDropDown"))
				.isFalse();
		Assertions
				.assertThat(allCatalogsPage.isCatalogFieldEditable("fieldSpecialMarketsCatalogListUI", "FieldDropDown"))
				.isFalse();
		pimHomepage.clickLogoutButton();
	}

	// CATR_001,CATR_002,CATR_003,CATR_004,CATR_005,CATR_006,CATR_007,CATR_008,CATR_009,CATR_010,CATR_013,CATR_017 for CA
	@PimFrameworkAnnotation(module = Modules.DENTAL_CATALOG, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.UI_Validation_Catalogs_Field)
	@Test(priority = 1, description = "CATR_001,Verification Part | CATR_001,CATR_002,CATR_003,CATR_004,CATR_005,CATR_006,CATR_007,CATR_008,CATR_009,CATR_010,CATR_013,CATR_017 | Dental Catalog Verifying rule driven, userdriven, exception list, publish date, publish flag are visible and editable in CA", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			"CA", "pim", "SANITY","DENTAL_CATALOG" })
	public void UI_validation_Dental_catalog_In_CA(Map<String, String> map) {
		loginPage.enterUserName(map.get("Username")).enterPassword(map.get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));

		// Validating on "Dental catalog"
		Assertions.assertThat(catalogTypePage.isCatalogFieldVisible("tabCanadianDentalCatalogs")).isTrue();

		Assertions.assertThat(catalogTypePage.isCatalogFieldVisible("RuleDrivenField")).isTrue();
		Assertions.assertThat(catalogTypePage.isCatalogFieldVisible("userDrivenField")).isTrue();
		Assertions.assertThat(catalogTypePage.isCatalogFieldVisible("exceptionListField")).isTrue();
		Assertions.assertThat(catalogTypePage.isCatalogFieldVisible("publishFlagField")).isTrue();
		Assertions.assertThat(catalogTypePage.isCatalogFieldVisible("publishDateField")).isTrue();

		Assertions.assertThat(catalogTypePage.isCatalogFieldEditable("RuleDrivenField", "FieldDropDown")).isFalse();
		Assertions.assertThat(catalogTypePage.isCatalogFieldEditable("userDrivenField", "FieldDropDown")).isTrue();
		Assertions.assertThat(catalogTypePage.isCatalogFieldEditable("exceptionListField", "FieldDropDown")).isTrue();
		Assertions.assertThat(catalogTypePage.isCatalogFieldEditable("publishFlagField", "FieldDropDown")).isTrue();
		Assertions.assertThat(catalogTypePage.isCatalogFieldEditable("publishDateField", "PublishDateFieldDropDown"))
				.isTrue();
		catalogTypePage.verifySelectUserdrivenDropdown().verifySelectExceptionListDropdown()
				.verifySelectPublishFlagFieldDropdown().verifySelectPublishDateFieldDropdown()
				.removingTheExceptionField();
		productdetailspage.clickRefreshIcon();

		// Validating on "All catalog Tab" for dental user ProductDetailSearchPage

		productdetailspage.selectTabfromDropdown(map.get("ContinueTabNameForCA"));

		// AllCatalogsPage allCatalogsPage = new AllCatalogsPage();

		Assertions.assertThat(allCatalogsPage.isCatalogFieldVisible("fieldDentalCatalogListUI")).isTrue();
		Assertions.assertThat(allCatalogsPage.isCatalogFieldVisible("fieldMedicalCatalogListUI")).isTrue();
		Assertions.assertThat(allCatalogsPage.isCatalogFieldVisible("fieldZahnCatalogListUI")).isTrue();

		//Assertions.assertThat(allCatalogsPage.isCatalogFieldVisible("fieldSpecialMarketsCatalogListUI")).isTrue();

		Assertions.assertThat(allCatalogsPage.isCatalogFieldEditable("fieldDentalCatalogListUI", "FieldDropDown"))
				.isFalse();
		Assertions.assertThat(allCatalogsPage.isCatalogFieldEditable("fieldMedicalCatalogListUI", "FieldDropDown"))
				.isFalse();
		Assertions.assertThat(allCatalogsPage.isCatalogFieldEditable("fieldZahnCatalogListUI", "FieldDropDown"))
				.isFalse();

		//Assertions.assertThat(allCatalogsPage.isCatalogFieldEditable("fieldSpecialMarketsCatalogListUI", "FieldDropDown")).isFalse();

		// Validating in master catalog for dental user for "dental catalog tab"

		queriesSubMenu.selectCatalogType(map.get("ContinueCatalogType")).clickSeachButton().clickOnSecondResult()
				.selectTabfromDropdown(map.get("TabName"));

		Assertions.assertThat(catalogTypePage.isCatalogFieldEditable("RuleDrivenField", "FieldDropDown")).isFalse();
		Assertions.assertThat(catalogTypePage.isCatalogFieldEditable("userDrivenField", "FieldDropDown")).isFalse();
		Assertions.assertThat(catalogTypePage.isCatalogFieldEditable("exceptionListField", "FieldDropDown")).isFalse();
		Assertions.assertThat(catalogTypePage.isCatalogFieldEditable("publishFlagField", "FieldDropDown")).isFalse();
		Assertions.assertThat(catalogTypePage.isCatalogFieldEditable("publishDateField", "PublishDateFieldDropDown"))
				.isFalse();

		// Validating in master catalog for dental user for "All catalog tab"
		productdetailspage.selectTabfromDropdown(map.get("ContinueTabNameForCA"));

		Assertions.assertThat(allCatalogsPage.isCatalogFieldEditable("fieldDentalCatalogListUI", "FieldDropDown"))
				.isFalse();
		Assertions.assertThat(allCatalogsPage.isCatalogFieldEditable("fieldMedicalCatalogListUI", "FieldDropDown"))
				.isFalse();
		Assertions.assertThat(allCatalogsPage.isCatalogFieldEditable("fieldZahnCatalogListUI", "FieldDropDown"))
				.isFalse();
		//Assertions.assertThat(allCatalogsPage.isCatalogFieldEditable("fieldSpecialMarketsCatalogListUI", "FieldDropDown")).isFalse();
		pimHomepage.clickLogoutButton();
	}

	// WRFL_005 and WRFL_006
	@PimFrameworkAnnotation(module = Modules.DENTAL_CATALOG, category = CategoryType.SMOKE)
	@TestDataSheet(sheetname = TestCaseSheet.Catalog)
	@Test(priority = 1, description = "WRFL_005 Verification Part | WRFL_005 and WRFL_006 | Verify Set of Catalog Entity Rules triggered if we update the Publish date and Flag in Catalog Assignment for US", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			"SMOKE","US", "pim","DENTAL_CATALOG" })
	public void verify_quality_status_when_change_publish_date_and_flag_for_US_dental_division(
			Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Dental User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Dental User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
		log(LogType.INFO, "Navigate to Dental Catalog tab from Queries Menu for US Catalog");
		BasePage.WaitForMiliSec(3000);
//		catalogTypePage.selectPublishDate(DateandTimeUtils.getTodaysDate()).selectPublishFlag();
//		log(LogType.INFO, "Change publish date to current date and publish flag");
		String publishdate = catalogTypePage.getPublishDate();
		String publishflag = catalogTypePage.getPublishFlag();

		// verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus"))
				.maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
		for (String Rules : javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule"))) {
			Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);
		}
		qualitystatus.minimizeQualityStatusTab();
		log(LogType.INFO, "Verifying catalog entity rule");

		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon().expandTab();
		log(LogType.INFO, "Navigate to Master Catalog");
		Assertions.assertThat(publishdate).isEqualTo(catalogTypePage.getPublishDate());
		Assertions.assertThat(publishflag).isEqualTo(catalogTypePage.getPublishFlag());

		pimHomepage.clickLogoutButton();
	}

	// WRFL_005 and WRFL_006
	@PimFrameworkAnnotation(module = Modules.DENTAL_CATALOG, category = CategoryType.SMOKE)
	@TestDataSheet(sheetname = TestCaseSheet.Catalog)
	@Test(priority = 1, description = "WRFL_005 Verification Part | WRFL_005 and WRFL_006 | Verify Set of Catalog Entity Rules triggered if we update the Publish date and Flag in Catalog Assignment for CA - WRFL_005 and WRFL_006", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			"SMOKE","CA", "pim","DENTAL_CATALOG" })
	public void verify_quality_status_when_change_publish_date_and_flag_for_CA_dental_division(
			Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
		log(LogType.INFO, "Navigate to Dental Catalog tab from Queries Menu for CA Catalog");
		BasePage.WaitForMiliSec(3000);
//		catalogTypePage.selectPublishDate(DateandTimeUtils.getTodaysDate()).selectPublishFlag();
//		log(LogType.INFO, "Change publish date to current date and publish flag");
		String publishdate = catalogTypePage.getPublishDate();
		String publishflag = catalogTypePage.getPublishFlag();

		// verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus"))
				.maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
		for (String Rules : javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule"))) {
			Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);
		}
		qualitystatus.minimizeQualityStatusTab();
		log(LogType.INFO, "Verifying catalog entity rule");

		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
				.clickOnSecondResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon().expandTab();
		log(LogType.INFO, "Navigate to Master Catalog");
		Assertions.assertThat(publishdate).isEqualTo(catalogTypePage.getPublishDate());
		Assertions.assertThat(publishflag).isEqualTo(catalogTypePage.getPublishFlag());

		pimHomepage.clickLogoutButton();
	}

	// ITP_029
	// we need recently created item from pix and make change on ItemPublish sheet
	@PimFrameworkAnnotation(module = Modules.DENTAL_CATALOG, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemPublish)
	@Test(priority = 1, description = "ITP_029 Verification Part |  ITP_029 | Verify whether the Publish date changed by the Dental division owner gets updated for other divisions when the other divisions have not modified the Original Publish Date for US.", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			"US", "pim","DENTAL_CATALOG" })
	public void verify_publish_date_changed_by_dental_division_for_US(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("DentalDivision"));
		log(LogType.INFO, "Navigate to Dental Catalog tab from Queries Menu for US Catalog");
		BasePage.WaitForMiliSec(3000);

		String dentalpublishflag = catalogTypePage.getPublishFlag();
//		if (!dentalpublishflag.equals("Yes")) {
//			catalogTypePage.selectPublishFlag(map.get("PublishFlag"));
//			log(LogType.INFO, "assigning yes in publish flag");
//		}
//
//		catalogTypePage.selectPublishDate(DateandTimeUtils.getTodaysDate());
		productdetailspage.clickRefreshIcon();
		String dentalpublishdate = catalogTypePage.getPublishDate();
		log(LogType.INFO, "Change publish date to current date");

		// verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus"))
				.maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
		for (String Rules : javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule"))) {
			Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);
		}
		qualitystatus.minimizeQualityStatusTab();
		log(LogType.INFO, "Verifying catalog entity rule");

		productdetailspage.selectTabfromDropdown(map.get("MedicalDivision")).clickRefreshIcon();
		String medicalpublishdate = catalogTypePage.getPublishDate();
		Assertions.assertThat(medicalpublishdate).isEqualTo(dentalpublishdate);
		log(LogType.INFO, "verified publish date in Medical division");

		productdetailspage.selectTabfromDropdown(map.get("ZahnDivision")).clickRefreshIcon();
		String zahnpublishdate = catalogTypePage.getPublishDate();
		Assertions.assertThat(zahnpublishdate).isEqualTo(dentalpublishdate);
		log(LogType.INFO, "verified publish date in Zahn division");

		productdetailspage.selectTabfromDropdown(map.get("SpecialMarketsDivision")).clickRefreshIcon();
		String specialmarketspublishdate = catalogTypePage.getPublishDate();
		Assertions.assertThat(specialmarketspublishdate).isEqualTo(dentalpublishdate);
		log(LogType.INFO, "verified publish date in Special Markets division");

		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("DentalDivision")).clickRefreshIcon();
		log(LogType.INFO, "Navigate to Master Catalog");
		BasePage.WaitForMiliSec(3000);
		String masterpublishflag = catalogTypePage.getPublishFlag();
		// Assertions.assertThat(masterpublishflag).isEqualTo(map.get("PublishFlag"));
		log(LogType.INFO, "Verifying publis flag");

		productdetailspage.selectTabfromDropdown(map.get("MedicalDivision")).clickRefreshIcon();
		String mastermedicalpublishdate = catalogTypePage.getPublishDate();
		Assertions.assertThat(mastermedicalpublishdate).isEqualTo(dentalpublishdate);
		log(LogType.INFO, "verified publish date in Medical division");

		productdetailspage.selectTabfromDropdown(map.get("ZahnDivision")).clickRefreshIcon();
		String masterzahnpublishdate = catalogTypePage.getPublishDate();
		Assertions.assertThat(masterzahnpublishdate).isEqualTo(dentalpublishdate);
		log(LogType.INFO, "verified publish date in Zahn division");

		productdetailspage.selectTabfromDropdown(map.get("SpecialMarketsDivision")).clickRefreshIcon();
		String masterspecialmarketspublishdate = catalogTypePage.getPublishDate();
		Assertions.assertThat(masterspecialmarketspublishdate).isEqualTo(dentalpublishdate);
		log(LogType.INFO, "verified publish date in Special Markets division");
		pimHomepage.clickLogoutButton();
	}

	// ITP_029
	// we need recently created item and make change on ItemPublish sheet
	@PimFrameworkAnnotation(module = Modules.DENTAL_CATALOG, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemPublish)
	@Test(priority = 1, description = "ITP_029 Verification Part | ITP_029 | Verify whether the Publish date changed by the Dental division owner gets updated for other divisions when the other divisions have not modified the Original Publish Date for CA.", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			"CA", "pim","DENTAL_CATALOG" })
	public void verify_publish_date_changed_by_dental_division_for_CA(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("DentalDivision"));
		log(LogType.INFO, "Navigate to Dental Catalog tab from Queries Menu for CA Catalog");
		BasePage.WaitForMiliSec(3000);

		String publishflag = catalogTypePage.getPublishFlag();
//		if (!publishflag.equals("Yes")) {
//			catalogTypePage.selectPublishFlag(map.get("PublishFlag"));
//			log(LogType.INFO, "assigning Yes in publish flag");
//		}
//		catalogTypePage.verifySelectPublishDateFieldDropdown();
//		productdetailspage.clickRefreshIcon();
		String dentalpublishdate = catalogTypePage.getPublishDate();
		log(LogType.INFO, "Change publish date to current date");

		// verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus"))
				.maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
		for (String Rules : javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule"))) {
			Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);
		}
		qualitystatus.minimizeQualityStatusTab();
		log(LogType.INFO, "Verifying catalog entity rule");

		productdetailspage.selectTabfromDropdown(map.get("MedicalDivision")).clickRefreshIcon();
		String medicalpublishdate = catalogTypePage.getPublishDate();
		Assertions.assertThat(medicalpublishdate).isEqualTo(dentalpublishdate);
		log(LogType.INFO, "verified publish date in Medical division");

		productdetailspage.selectTabfromDropdown(map.get("ZahnDivision")).clickRefreshIcon();
		String zahnpublishdate = catalogTypePage.getPublishDate();
		Assertions.assertThat(zahnpublishdate).isEqualTo(dentalpublishdate);
		log(LogType.INFO, "verified publish date in Zahn division");

		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
				.clickOnSecondResult().selectTabfromDropdown(map.get("DentalDivision")).clickRefreshIcon();
		log(LogType.INFO, "Navigate to Master Catalog");
		BasePage.WaitForMiliSec(3000);
		String masterpublishflag = catalogTypePage.getPublishFlag();
		// Assertions.assertThat(masterpublishflag).isEqualTo(map.get("PublishFlag"));
		log(LogType.INFO, "Verifying publish flag");

		productdetailspage.selectTabfromDropdown(map.get("MedicalDivision")).clickRefreshIcon();
		String mastermedicalpublishdate = catalogTypePage.getPublishDate();
		Assertions.assertThat(mastermedicalpublishdate).isEqualTo(dentalpublishdate);
		log(LogType.INFO, "verified publish date in Medical division");

		productdetailspage.selectTabfromDropdown(map.get("ZahnDivision")).clickRefreshIcon();
		String masterzahnpublishdate = catalogTypePage.getPublishDate();
		Assertions.assertThat(masterzahnpublishdate).isEqualTo(dentalpublishdate);
		log(LogType.INFO, "verified publish date in Zahn division");
		pimHomepage.clickLogoutButton();
	}

	// ITP_034
	@PimFrameworkAnnotation(module = Modules.DENTAL_CATALOG, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemPublish)
	@Test(priority = 1, description = "ITP_034 Verification Part | ITP_034 | Verify whether the Publish date changed by the Dental Owner division should not gets updated for other divisions when the other divisions have modified the Original Publish Date for US.", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			"US", "pim","DENTAL_CATALOG" })
	public void verify_publish_date_not_update_for_medical_when_changed_in_dental_for_US(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("MedicalDivision"));
		log(LogType.INFO, "Navigate to Medical Catalog tab from Queries Menu for US Catalog");
		BasePage.WaitForMiliSec(3000);

		String publishflag = catalogTypePage.getPublishFlag();
//		if (!publishflag.equals("Yes")) {
//			catalogTypePage.selectPublishFlag(map.get("PublishFlag"));
//			log(LogType.INFO, "assigning Yes in publish flag");
//		}
//		catalogTypePage.selectPublishDate(DateandTimeUtils.getTodaysDate());
//		productdetailspage.clickRefreshIcon();
		String medicalpublishdate = catalogTypePage.getPublishDate();
		log(LogType.INFO, "Change publish date to current date for Medical division");

		productdetailspage.selectTabfromDropdown(map.get("DentalDivision"));
		String dentalpublishflag = catalogTypePage.getPublishFlag();
		// Assertions.assertThat(dentalpublishflag).isEqualTo(map.get("PublishFlag"));
		catalogTypePage.selectPublishDate(DateandTimeUtils.getFutureDate());
		productdetailspage.clickRefreshIcon();
		String dentalpublishdate = catalogTypePage.getPublishDate();
		log(LogType.INFO, "Change publish date to current date for Dental division");

		// verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus"))
				.maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
		for (String Rules : javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule"))) {
			Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);
		}
		qualitystatus.minimizeQualityStatusTab();
		log(LogType.INFO, "Verifying catalog entity rule");

		productdetailspage.selectTabfromDropdown(map.get("ZahnDivision")).clickRefreshIcon();
		String zahnpublishdate = catalogTypePage.getPublishDate();
		Assertions.assertThat(zahnpublishdate).isEqualTo(dentalpublishdate);
		log(LogType.INFO, "verified publish date in Zahn division");

		productdetailspage.selectTabfromDropdown(map.get("SpecialMarketsDivision")).clickRefreshIcon();
		String specialmarketspublishdate = catalogTypePage.getPublishDate();
		Assertions.assertThat(specialmarketspublishdate).isEqualTo(dentalpublishdate);
		log(LogType.INFO, "verified publish date in Special Markets division");

		productdetailspage.selectTabfromDropdown(map.get("MedicalDivision")).clickRefreshIcon();
		Assertions.assertThat(medicalpublishdate).isNotEqualTo(dentalpublishdate);

		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("DentalDivision")).clickRefreshIcon();
		log(LogType.INFO, "Navigate to Master Catalog");
		BasePage.WaitForMiliSec(3000);
		String masterpublishflag = catalogTypePage.getPublishFlag();
		// Assertions.assertThat(masterpublishflag).isEqualTo(map.get("PublishFlag"));
		log(LogType.INFO, "Verifying publish flag");

		productdetailspage.selectTabfromDropdown(map.get("MedicalDivision")).clickRefreshIcon();
		String mastermedicalpublishdate = catalogTypePage.getPublishDate();
		Assertions.assertThat(mastermedicalpublishdate).isNotEqualTo(dentalpublishdate);
		log(LogType.INFO, "verified publish date in Medical division");

		productdetailspage.selectTabfromDropdown(map.get("ZahnDivision")).clickRefreshIcon();
		String masterzahnpublishdate = catalogTypePage.getPublishDate();
		Assertions.assertThat(masterzahnpublishdate).isEqualTo(dentalpublishdate);
		log(LogType.INFO, "verified publish date in Zahn division");

		productdetailspage.selectTabfromDropdown(map.get("SpecialMarketsDivision")).clickRefreshIcon();
		String masterspecialmarketspublishdate = catalogTypePage.getPublishDate();
		Assertions.assertThat(masterspecialmarketspublishdate).isEqualTo(dentalpublishdate);
		log(LogType.INFO, "verified publish date in Special Markets division");
		pimHomepage.clickLogoutButton();
	}

	// ITP_034
	@PimFrameworkAnnotation(module = Modules.DENTAL_CATALOG, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemPublish)
	@Test(priority = 1, description = "ITP_034 Verification Part | ITP_034 | Verify whether the Publish date changed by the Dental Owner division should not gets updated for other divisions when the other divisions have modified the Original Publish Date for CA.", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			"CA", "pim","DENTAL_CATALOG" })
	public void verify_publish_date_not_update_for_medical_when_changed_in_dental_for_CA(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("MedicalDivision"));
		log(LogType.INFO, "Navigate to Medical Catalog tab from Queries Menu for US Catalog");
		BasePage.WaitForMiliSec(3000);

		String publishflag = catalogTypePage.getPublishFlag();
//		if (!publishflag.equals("Yes")) {
//			catalogTypePage.selectPublishFlag(map.get("PublishFlag"));
//			log(LogType.INFO, "assigning Yes in publish flag");
//		}
//		catalogTypePage.selectPublishDate(DateandTimeUtils.getTodaysDate());
//		productdetailspage.clickRefreshIcon();
		String medicalpublishdate = catalogTypePage.getPublishDate();
		log(LogType.INFO, "Change publish date to current date for Medical division");

		productdetailspage.selectTabfromDropdown(map.get("DentalDivision"));
		String dentalpublishflag = catalogTypePage.getPublishFlag();
		// Assertions.assertThat(dentalpublishflag).isEqualTo(map.get("PublishFlag"));
		catalogTypePage.selectPublishDate(DateandTimeUtils.getFutureDate());
		productdetailspage.clickRefreshIcon();
		String dentalpublishdate = catalogTypePage.getPublishDate();
		log(LogType.INFO, "Change publish date to current date for Dental division");

		// verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus"))
				.maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
		for (String Rules : javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule"))) {
			Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);
		}
		qualitystatus.minimizeQualityStatusTab();
		log(LogType.INFO, "Verifying catalog entity rule");

		productdetailspage.selectTabfromDropdown(map.get("ZahnDivision")).clickRefreshIcon();
		String zahnpublishdate = catalogTypePage.getPublishDate();
		Assertions.assertThat(zahnpublishdate).isEqualTo(dentalpublishdate);
		log(LogType.INFO, "verified publish date in Zahn division");

		productdetailspage.selectTabfromDropdown(map.get("MedicalDivision")).clickRefreshIcon();
		Assertions.assertThat(medicalpublishdate).isNotEqualTo(dentalpublishdate);

		// master
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
				.clickOnSecondResult().selectTabfromDropdown(map.get("DentalDivision")).clickRefreshIcon();
		log(LogType.INFO, "Navigate to Master Catalog");
		BasePage.WaitForMiliSec(3000);
		String masterpublishflag = catalogTypePage.getPublishFlag();
		Assertions.assertThat(masterpublishflag).isEqualTo(map.get("PublishFlag"));
		log(LogType.INFO, "Verifying publish flag");

		productdetailspage.selectTabfromDropdown(map.get("MedicalDivision")).clickRefreshIcon();
		String mastermedicalpublishdate = catalogTypePage.getPublishDate();
		Assertions.assertThat(mastermedicalpublishdate).isNotEqualTo(dentalpublishdate);
		log(LogType.INFO, "verified publish date in Medical division");

		productdetailspage.selectTabfromDropdown(map.get("ZahnDivision")).clickRefreshIcon();
		String masterzahnpublishdate = catalogTypePage.getPublishDate();
		Assertions.assertThat(masterzahnpublishdate).isEqualTo(dentalpublishdate);
		log(LogType.INFO, "verified publish date in Zahn division");
		pimHomepage.clickLogoutButton();
	}

	@PimFrameworkAnnotation(module = Modules.DENTAL_CATALOG, category = CategoryType.SMOKE)
	@TestDataSheet(sheetname = TestCaseSheet.Catalog)
	@Test(priority = 1, description = "MDLW_015 Verification Part | MDLW_015 | Verify the Publish flag and publish date  was updated in  respective table from the outbound file", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			"SMOKE","US", "pim","DENTAL_CATALOG" })
	public void verify_publish_flag_and_date_update_in_respective_column(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Dental User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Dental User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
		log(LogType.INFO, "Navigate to Dental tab from Queries Menu for US Catalog");

//		catalogTypePage.clearUserDriven(map.get("userDriven"));
//		BasePage.WaitForMiliSec(3000);
//		catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
//		productdetailspage.clickRefreshIcon();
//
//		catalogTypePage.selectPublishFlag().selectPublishDate(DateandTimeUtils.getTodaysDate());
//		productdetailspage.clickRefreshIcon();

		// verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus"))
				.maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
		for (String Rules : javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule"))) {
			Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);
		}
		qualitystatus.minimizeQualityStatusTab();
		log(LogType.INFO, "Verifying catalog entity rule");

		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType("Master catalog").clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
		BasePage.WaitForMiliSec(3000);

		pimHomepage.clickLogoutButton();
	}


	// CATR_219-Verify if AED_FSC gets removed from user driven list if Dental is
	// chosen in user driven catalog list of dental division
	@PimFrameworkAnnotation(module = Modules.DENTAL_CATALOG, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Catalog_CA)
	@Test(priority = 1, description = "CATR_219 Verification Part | CATR_219 | Verify if AED_FSC gets removed from user driven list if Dental is chosen in user driven catalog list of dental division", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			"CA", "pim","DENTAL_CATALOG" })
	public void Verify_if_AED_FSC_gets_removed_from_user_driven_list_if_Dental_is_chosen_in_user_driven_catalog_list_of_dental_division_CATR_219(
			Map<String, String> map) {

		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
//		catalogTypePage.clearUserDriven(map.get("userDriven1"));
//		catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
//		pimHomepage.productDetailSearchPage().clickRefreshIcon();
//		catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven1"));
//		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		user_driven = catalogTypePage.getUserDrivenCatalogs();
		Assertions.assertThat(user_driven).containsAnyOf(map.get("userDriven1"));
	}

	// ITP_017 and Middleware TC_ID is MDLW_017
	// Verify Item with Publish Flag = 'Y" and Publish Date as Future Date - All
	// Divisions and check in Middleware
	@PimFrameworkAnnotation(module = Modules.DENTAL_CATALOG, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemPublish)
	@Test(priority = 1, description = "ITP_017, Verification Part | ITP_017,MDLW_017  | Verify whether the Publish Flag for a Dental Item get updated as 'Y' and publish date be future in the Middleware Export File, when the Items loaded from PIX to PIM with Publish Flag as \"Y\" for US", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			"US", "pim","DENTAL_CATALOG" })
	public void verify_publish_flag_should_yes_and_publish_date_should_be_future_US_for_Dental_division(
			Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Dental User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Dental User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("DentalDivision"));
		log(LogType.INFO, "Navigate to Dental tab from Queries Menu for US Catalog");

		String dentalpublishflag = catalogTypePage.getPublishFlag();
		String Y = "Yes";
//		if (!dentalpublishflag.equals(Y)) {
//			catalogTypePage.selectPublishFlag(map.get("PublishFlag"));
//			log(LogType.INFO, "assigning yes in publish flag");
//		}
//		catalogTypePage.selectPublishDate(DateandTimeUtils.getFutureDate());
//		log(LogType.INFO, "assigning future date");
		productdetailspage.clickRefreshIcon();
		String date = catalogTypePage.getPublishDate();

		// verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus"))
				.maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
		for (String Rules : javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule"))) {
			Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);
		}
		qualitystatus.minimizeQualityStatusTab();
		log(LogType.INFO, "Verifying catalog entity rule");

		// master
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("DentalDivision")).clickRefreshIcon();
		log(LogType.INFO, "Navigate to Master Catalog");

		String masterdate = catalogTypePage.getPublishDate();
		Assertions.assertThat(date).isEqualTo(masterdate);
		log(LogType.INFO, "verifying dates should get update in master");
		pimHomepage.clickLogoutButton();
	}

	// ITP_017
	@PimFrameworkAnnotation(module = Modules.DENTAL_CATALOG, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemPublish)
	@Test(priority = 1, description = "ITP_017 Verification Part | ITP_017 | Verify whether the Publish Flag for a Dental Item get updated as 'Y' and publish date be future in the Middleware Export File, when the Items loaded from PIX to PIM with Publish Flag as \"Y\" for CA", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			"CA", "pim","DENTAL_CATALOG" })
	public void verify_publish_flag_should_yes_and_publish_date_should_be_future_CA_for_Dental_division(
			Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("DentalDivision"));
		log(LogType.INFO, "Navigate to Dental tab from Queries Menu for CA Catalog");

		String dentalpublishflag = catalogTypePage.getPublishFlag();
		String Y = "Yes";
//		if (!dentalpublishflag.equals(Y)) {
//			catalogTypePage.selectPublishFlag(map.get("PublishFlag"));
//			log(LogType.INFO, "assigning yes in publish flag");
//		}
//		catalogTypePage.selectPublishDate(DateandTimeUtils.getFutureDate());
//		log(LogType.INFO, "assigning future date");
		productdetailspage.clickRefreshIcon();
		String date = catalogTypePage.getPublishDate();

		// verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus"))
				.maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
		for (String Rules : javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule"))) {
			Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);
		}
		qualitystatus.minimizeQualityStatusTab();
		log(LogType.INFO, "Verifying catalog entity rule");

		// master
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
				.clickOnSecondResult().selectTabfromDropdown(map.get("DentalDivision")).clickRefreshIcon();
		log(LogType.INFO, "Navigate to Master Catalog");

		String masterdate = catalogTypePage.getPublishDate();
		Assertions.assertThat(date).isEqualTo(masterdate);
		log(LogType.INFO, "verifying dates should get update in master");
		pimHomepage.clickLogoutButton();
	}

	// ITP_025
	@PimFrameworkAnnotation(module = Modules.DENTAL_CATALOG, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemPublish)
	@Test(priority = 1, description = "ITP_025 Verification Part | ITP_025 | Verify whether the Publish Flag for a Dental Item get updated as \"N\" in the Middleware Export File, when the Items loaded from PIX to PIM with Publish Flag as \"N\" for US", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			"US", "pim","DENTAL_CATALOG" })
	public void verify_publish_flag_should_updated_as_NO_in_Dental_division_for_US(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Dental User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Dental User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("DentalDivision"));
		log(LogType.INFO, "Navigate to Dental tab from Queries Menu for US Catalog");

//		String dentalpublishflag = catalogTypePage.getPublishFlag();
//		if (!dentalpublishflag.equals("No")) {
//			catalogTypePage.selectPublishFlag(map.get("PublishFlag"));
//			log(LogType.INFO, "assigning No in publish flag");
//		}
//		List<String> userdrivencatalogs = catalogTypePage.getUserDrivenCatalogs();
//		if (!userdrivencatalogs.contains(map.get("userDriven"))) {
//			catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
//		}

		// verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus"))
				.maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
		for (String Rules : javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule"))) {
			// Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);
		}
		qualitystatus.minimizeQualityStatusTab();
		log(LogType.INFO, "Verifying catalog entity rule");

		// master
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("DentalDivision")).clickRefreshIcon();
		log(LogType.INFO, "Navigate to Dental division in Master Catalog");

		String masterpublishflag = catalogTypePage.getPublishFlag();
		Assertions.assertThat(masterpublishflag).isEqualTo(map.get("PublishFlag"));
		pimHomepage.clickLogoutButton();
	}

	// ITP_025
	@PimFrameworkAnnotation(module = Modules.DENTAL_CATALOG, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemPublish)
	@Test(priority = 1, description = "ITP_025 Verification Part | ITP_025 | Verify whether the Publish Flag for a Dental Item get updated as \"N\" in the Middleware Export File, when the Items loaded from PIX to PIM with Publish Flag as \"N\" for CA", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			"CA", "pim","DENTAL_CATALOG" })
	public void verify_publish_flag_should_updated_as_NO_in_Dental_division_for_CA(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("DentalDivision"));
		log(LogType.INFO, "Navigate to Dental tab from Queries Menu for CA Catalog");

//		String dentalpublishflag = catalogTypePage.getPublishFlag();
//		if (!dentalpublishflag.equals("No")) {
//			catalogTypePage.selectPublishFlag(map.get("PublishFlag"));
//			log(LogType.INFO, "assigning No in publish flag");
//		}
//		List<String> userdrivencatalogs = catalogTypePage.getUserDrivenCatalogs();
//		if (!userdrivencatalogs.contains(map.get("userDriven"))) {
//			catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
//		}

		// verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus"))
				.maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
		for (String Rules : javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule"))) {
			// Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);
		}
		qualitystatus.minimizeQualityStatusTab();
		log(LogType.INFO, "Verifying catalog entity rule");

		// master
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
				.clickOnSecondResult().selectTabfromDropdown(map.get("DentalDivision")).clickRefreshIcon();
		log(LogType.INFO, "Navigate to Dental division in Master Catalog");

		String masterpublishflag = catalogTypePage.getPublishFlag();
		Assertions.assertThat(masterpublishflag).isEqualTo(map.get("PublishFlag"));
		pimHomepage.clickLogoutButton();
	}

	// ITP_021
	@PimFrameworkAnnotation(module = Modules.DENTAL_CATALOG, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemPublish)
	@Test(priority = 1, description = "ITP_021 Verification Part | ITP_021 | Verify whether the Publish Flag for a Dental Item get updated as \"Y\" and publish date as Current in the Middleware Export File, when the Items loaded from PIX to PIM with Publish Flag as \"Y\" for US", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			"US", "pim","DENTAL_CATALOG" })
	public void verify_publish_flag_should_updated_as_Yes_in_Dental_division_for_US(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Dental User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Dental User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("DentalDivision"));
		log(LogType.INFO, "Navigate to Dental tab from Queries Menu for US Catalog");

//		catalogTypePage.selectPublishFlag(map.get("PublishFlag"));
//		log(LogType.INFO, "assigning Yes in publish flag");
//
//		catalogTypePage.selectPublishDate(DateandTimeUtils.getTodaysDate());
//		productdetailspage.clickRefreshIcon();
//		log(LogType.INFO, "assigning current date");

		String publishdate = catalogTypePage.getPublishDate();
		String publishFlag = catalogTypePage.getPublishFlag();

		List<String> userdrivencatalogs = catalogTypePage.getUserDrivenCatalogs();
//		if (!userdrivencatalogs.contains(map.get("userDriven"))) {
//			catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
//		}

		// verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus"))
				.maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
		for (String Rules : javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule"))) {
			Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);
		}
		qualitystatus.minimizeQualityStatusTab();
		log(LogType.INFO, "Verifying catalog entity rule");

		// master
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("DentalDivision")).clickRefreshIcon();
		log(LogType.INFO, "Navigate to Dental division in Master Catalog");

		String masterpublishdate = catalogTypePage.getPublishDate();
		String masterpublishFlag = catalogTypePage.getPublishFlag();

		Assertions.assertThat(masterpublishdate).isEqualTo(publishdate);
		Assertions.assertThat(masterpublishFlag).isEqualTo(publishFlag);
		pimHomepage.clickLogoutButton();
	}

	// ITP_021
	@PimFrameworkAnnotation(module = Modules.DENTAL_CATALOG, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemPublish)
	@Test(priority = 1, description = "ITP_021 Verification Part | ITP_021 | Verify whether the Publish Flag for a Dental Item get updated as \"Y\" and publish date as Current in the Middleware Export File, when the Items loaded from PIX to PIM with Publish Flag as \"Y\" for CA", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			"CA", "pim","DENTAL_CATALOG" })
	public void verify_publish_flag_should_updated_as_Yes_in_Dental_division_for_CA(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("DentalDivision"));
		log(LogType.INFO, "Navigate to Dental tab from Queries Menu for US Catalog");

//		catalogTypePage.selectPublishFlag(map.get("PublishFlag"));
//		log(LogType.INFO, "assigning Yes in publish flag");
//
//		catalogTypePage.selectPublishDate(DateandTimeUtils.getTodaysDate());
//		productdetailspage.clickRefreshIcon();
//		log(LogType.INFO, "assigning current date");

		String publishdate = catalogTypePage.getPublishDate();
		String publishFlag = catalogTypePage.getPublishFlag();

		List<String> userdrivencatalogs = catalogTypePage.getUserDrivenCatalogs();
//		if (!userdrivencatalogs.contains(map.get("userDriven"))) {
//			catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
//		}

		// verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus"))
				.maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
		for (String Rules : javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule"))) {
			Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);
		}
		qualitystatus.minimizeQualityStatusTab();
		log(LogType.INFO, "Verifying catalog entity rule");

		// master
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
				.clickOnSecondResult().selectTabfromDropdown(map.get("DentalDivision")).clickRefreshIcon();
		log(LogType.INFO, "Navigate to Dental division in Master Catalog");

		String masterpublishdate = catalogTypePage.getPublishDate();
		String masterpublishFlag = catalogTypePage.getPublishFlag();

		Assertions.assertThat(masterpublishdate).isEqualTo(publishdate);
		Assertions.assertThat(masterpublishFlag).isEqualTo(publishFlag);
		pimHomepage.clickLogoutButton();
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
//	PART_3: SpecialMarketCatalogTest (SPECIAL MARKET TESTS)

//	CatalogTypePage catalogTypePage = new CatalogTypePage();
//	AllCatalogsPage allCatalogsPage = new AllCatalogsPage();
//	QualityStatusPage qualitystatus = new QualityStatusPage();
//	ProductDetailSearchPage productdetailspage = new ProductDetailSearchPage();
	LoginPage loginpage = new LoginPage();
//	QueriesSubMenu queriesSubMenu = new QueriesSubMenu();
//	PimHomepage pimHomepage = new PimHomepage();
//	Javautils javautils = new Javautils();
	//List<String> rule_driven;
	//List<String> user_driven;
	//List<String> all_special_markets_catalog;
	//List<String> user_and_rule_driven = new ArrayList<>();

	// WRFL_016
	@PimFrameworkAnnotation(module = Modules.SPECIAL_MARKET_CATALOG, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Catalog)
	@Test(priority = 1, description = "WRFL_016 Verification Part | WRFL_016 | Verifying Rule Driven catalog for Special Market Division in US, CA and Master Catalog", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			"US", "pim","SPECIAL_MARKET_CATALOG" })
	public void verify_rule_driven_for_special_markets_division(Map<String, String> map)
			throws InterruptedException, IOException {

		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Special Markets User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Special Markets User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));

		List<String> userdrivencatalogs = catalogTypePage.getUserDrivenCatalogs();
		List<String> multiplevaluesfromexcel = javautils.readMultipleValuesFromExcel(map.get("userDriven"));
//		for (String userdriven : multiplevaluesfromexcel) {
//			if (userdrivencatalogs.contains(userdriven)) {
//				// delete userdriven
//				catalogTypePage.clearSelectedFields(userdriven);
//				productdetailspage.clickRefreshIcon();
//				log(LogType.INFO, "Deleting the Userdriven");
//			}
//		}
//		BasePage.WaitForMiliSec(3000);
//		catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
//		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		List<String> ruledriven = catalogTypePage.getRuleDrivenCatalogs();

		// verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus"))
				.maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
		for (String Rules : javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule"))) {
			Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);
		}
		qualitystatus.minimizeQualityStatusTab();
		log(LogType.INFO, "Verifying catalog entity rule");

		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
		List<String> masterruledriven = catalogTypePage.getRuleDrivenCatalogs();
		Assertions.assertThat(masterruledriven).containsAll(ruledriven);
		pimHomepage.clickLogoutButton();
	}

	// CATR_185, CATR_186 and CATR_135
	@PimFrameworkAnnotation(module = Modules.SPECIAL_MARKET_CATALOG, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Catalog)
	@Test(priority = 1, description = "CATR_185, Verification Part | CATR_185, CATR_186 and CATR_135 | Verify All catalog tabs and exception lists for Special markets Catalog list populates the catalogs with User, Rule Driven and excluding the Catalogs selected as Exception ", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			"US", "pim","SPECIAL_MARKET_CATALOG" })
	public void verify_all_catalog_tabs_and_exception_list_for_special_markets_division(Map<String, String> map)
			throws Throwable {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Special Markets User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Special Markets User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));

		List<String> user_driven = catalogTypePage.getUserDrivenCatalogs();
		List<String> rule_driven = catalogTypePage.getRuleDrivenCatalogs();
		List<String> user_and_rule_driven = new ArrayList<>();
		user_and_rule_driven.addAll(rule_driven);
		user_and_rule_driven.addAll(user_driven); // took user and rule driven
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("AllCatalogtab")).clickRefreshIcon();
		List<String> all_special_markets_catalog = allCatalogsPage.getAllSpecialMarketsCatalogList();
		Assertions.assertThat(user_and_rule_driven).containsAll(all_special_markets_catalog);// verified all catalog
																								// without exception

		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon()
				.selectTabfromDropdown(map.get("AllCatalogtab")).clickRefreshIcon();
		List<String> master_all_special_markets_catalog = allCatalogsPage.getAllSpecialMarketsCatalogList();
		// verified all catalog tab in master
		Assertions.assertThat(user_and_rule_driven).containsAll(master_all_special_markets_catalog);

		// verifying with exception list
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
		List<String> userdrivencatalogs = catalogTypePage.getExceptionListCatalogs();
		List<String> multiplevaluesfromexcel = javautils.readMultipleValuesFromExcel(map.get("userDriven"));
//		for (String userdriven : multiplevaluesfromexcel) {
//			if (userdrivencatalogs.contains(userdriven)) {
//				// delete userdriven
//				catalogTypePage.clearExceptionListField(userdriven);
//				productdetailspage.clickRefreshIcon();
//				log(LogType.INFO, "Deleting the Userdriven");
//			}
//		}
//		BasePage.WaitForMiliSec(3000);
//		catalogTypePage.clickExceptionListField().selectExceptionCatalogsFromList(map.get("userDriven"));
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("AllCatalogtab")).clickRefreshIcon();

		List<String> all_catalog_except_exceptionallist = allCatalogsPage.getAllSpecialMarketsCatalogList();
		Assertions.assertThat(all_catalog_except_exceptionallist).isNotEqualTo(user_and_rule_driven);
		Assert.assertNotEquals(all_catalog_except_exceptionallist, user_and_rule_driven);

		// verifying with exception list in master catalog
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon()
				.selectTabfromDropdown(map.get("AllCatalogtab"));
		List<String> master_exception_alldental__catalog = allCatalogsPage.getAllSpecialMarketsCatalogList();
		// Assert.assertNotEquals(master_exception_all_catalog, user_and_rule_driven);
		// Assert.assertEquals(all_catalog_except_exceptionallist,
		// master_exception_all_catalog);
		Assertions.assertThat(user_and_rule_driven).isNotEqualTo(master_exception_alldental__catalog);
		Assertions.assertThat(all_catalog_except_exceptionallist).containsAll(master_exception_alldental__catalog);
		pimHomepage.clickLogoutButton();
	}

	// CATR_035,CATR_036,CATR_037,CATR_038,CATR_039,CATR_040,CATR_041,CATR_042,CATR_043,CATR_044,CATR_047,CATR_050

	@PimFrameworkAnnotation(module = Modules.SPECIAL_MARKET_CATALOG, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.UI_Validation_Catalogs_Field)
	@Test(priority = 1, description = "CATR_035,Verification Part | CATR_035,CATR_036,CATR_037,CATR_038,CATR_039,CATR_040,CATR_041,CATR_042,CATR_043,CATR_044,CATR_047,CATR_050 | Special market Verifying rule driven, userdriven, exception list, publish date, publish flag are visible and editable In US", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			"US", "pim", "SANITY","SPECIAL_MARKET_CATALOG" })
	public void UI_validation_Special_Markets_Catalog_In_US(Map<String, String> map) {

		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Special Markets User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Special Markets User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));

		// Validating on "Special Markets catalog"
		Assertions.assertThat(catalogTypePage.isCatalogFieldVisible("tabSpecialMarketsCatalog")).isTrue();
		Assertions.assertThat(catalogTypePage.isCatalogFieldVisible("RuleDrivenField")).isTrue();
		Assertions.assertThat(catalogTypePage.isCatalogFieldVisible("userDrivenField")).isTrue();
		Assertions.assertThat(catalogTypePage.isCatalogFieldVisible("exceptionListField")).isTrue();
		Assertions.assertThat(catalogTypePage.isCatalogFieldVisible("publishFlagField")).isTrue();
		Assertions.assertThat(catalogTypePage.isCatalogFieldVisible("publishDateField")).isTrue();

		Assertions.assertThat(catalogTypePage.isCatalogFieldEditable("RuleDrivenField", "FieldDropDown")).isFalse();
		Assertions.assertThat(catalogTypePage.isCatalogFieldEditable("userDrivenField", "FieldDropDown")).isTrue();
		Assertions.assertThat(catalogTypePage.isCatalogFieldEditable("exceptionListField", "FieldDropDown")).isTrue();
		Assertions.assertThat(catalogTypePage.isCatalogFieldEditable("publishFlagField", "FieldDropDown")).isTrue();
		Assertions.assertThat(catalogTypePage.isCatalogFieldEditable("publishDateField", "PublishDateFieldDropDown"))
				.isTrue();
		catalogTypePage.verifySelectUserdrivenDropdown().verifySelectExceptionListDropdown()
				.verifySelectPublishFlagFieldDropdown().verifySelectPublishDateFieldDropdown();

		// Validating on "All catalog Tab" for dental user ProductDetailSearchPage

		productdetailspage.selectTabfromDropdown(map.get("ContinueTabName"));

		// AllCatalogsPage allCatalogsPage = new AllCatalogsPage();

		Assertions.assertThat(allCatalogsPage.isCatalogFieldVisible("tabAllCatalogs")).isTrue();

		Assertions.assertThat(allCatalogsPage.isCatalogFieldVisible("fieldDentalCatalogListUI")).isTrue();
		Assertions.assertThat(allCatalogsPage.isCatalogFieldVisible("fieldMedicalCatalogListUI")).isTrue();
		Assertions.assertThat(allCatalogsPage.isCatalogFieldVisible("fieldZahnCatalogListUI")).isTrue();
		Assertions.assertThat(allCatalogsPage.isCatalogFieldVisible("fieldSpecialMarketsCatalogListUI")).isTrue();

		Assertions.assertThat(allCatalogsPage.isCatalogFieldEditable("fieldDentalCatalogListUI", "FieldDropDown"))
				.isFalse();
		Assertions.assertThat(allCatalogsPage.isCatalogFieldEditable("fieldMedicalCatalogListUI", "FieldDropDown"))
				.isFalse();
		Assertions.assertThat(allCatalogsPage.isCatalogFieldEditable("fieldZahnCatalogListUI", "FieldDropDown"))
				.isFalse();
		Assertions
				.assertThat(allCatalogsPage.isCatalogFieldEditable("fieldSpecialMarketsCatalogListUI", "FieldDropDown"))
				.isFalse();

		// Validating in master catalog for dental user for "dental catalog tab"

		queriesSubMenu.selectCatalogType(map.get("ContinueCatalogType")).clickSeachButton().clickOnFirstResult()
				.selectTabfromDropdown(map.get("TabName"));

		Assertions.assertThat(catalogTypePage.isCatalogFieldEditable("RuleDrivenField", "FieldDropDown")).isFalse();
		Assertions.assertThat(catalogTypePage.isCatalogFieldEditable("userDrivenField", "FieldDropDown")).isFalse();
		Assertions.assertThat(catalogTypePage.isCatalogFieldEditable("exceptionListField", "FieldDropDown")).isFalse();
		Assertions.assertThat(catalogTypePage.isCatalogFieldEditable("publishFlagField", "FieldDropDown")).isFalse();
		Assertions.assertThat(catalogTypePage.isCatalogFieldEditable("publishDateField", "PublishDateFieldDropDown"))
				.isFalse();

		// Validating in master catalog for dental user for "All catalog tab"
		productdetailspage.selectTabfromDropdown(map.get("ContinueTabName"));

		Assertions.assertThat(allCatalogsPage.isCatalogFieldEditable("fieldDentalCatalogListUI", "FieldDropDown"))
				.isFalse();
		Assertions.assertThat(allCatalogsPage.isCatalogFieldEditable("fieldMedicalCatalogListUI", "FieldDropDown"))
				.isFalse();
		Assertions.assertThat(allCatalogsPage.isCatalogFieldEditable("fieldZahnCatalogListUI", "FieldDropDown"))
				.isFalse();
		Assertions
				.assertThat(allCatalogsPage.isCatalogFieldEditable("fieldSpecialMarketsCatalogListUI", "FieldDropDown"))
				.isFalse();
		pimHomepage.clickLogoutButton();
	}

	// WRFL_017 and WRFL_018
	@PimFrameworkAnnotation(module = Modules.SPECIAL_MARKET_CATALOG, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Catalog)
	@Test(priority = 1, description = "WRFL_017 Verification Part | WRFL_017 and WRFL_018 | Verify Set of Catalog Entity Rules triggered if we update the Publish date and Flag in Catalog Assignment for US", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			"US", "pim","SPECIAL_MARKET_CATALOG" })
	public void verify_quality_status_when_change_publish_date_and_flag_for_US_special_market_division(
			Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Special Markets User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Special Markets User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
//		log(LogType.INFO, "Navigate to Dental Catalog tab from Queries Menu for US Catalog");
//		BasePage.WaitForMiliSec(3000);
//		catalogTypePage.selectPublishDate(DateandTimeUtils.getTodaysDate()).selectPublishFlag();
//		log(LogType.INFO, "Change publish date to current date and publish flag");
		String publishdate = catalogTypePage.getPublishDate();
		String publishflag = catalogTypePage.getPublishFlag();

		// verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus"))
				.maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
		for (String Rules : javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule"))) {
			Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);
		}
		qualitystatus.minimizeQualityStatusTab();
		log(LogType.INFO, "Verifying catalog entity rule");

		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon().expandTab();
		log(LogType.INFO, "Navigate to Master Catalog");
		Assertions.assertThat(publishdate).isEqualTo(catalogTypePage.getPublishDate());
		Assertions.assertThat(publishflag).isEqualTo(catalogTypePage.getPublishFlag());

		pimHomepage.clickLogoutButton();
	}

	// ITP_032
	// we need recently created item and make change on ItemPublish sheet
	@PimFrameworkAnnotation(module = Modules.SPECIAL_MARKET_CATALOG, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemPublish)
	@Test(priority = 1, description = "ITP_032 Verification Part | ITP_032 | Verify whether the Publish date changed by the Special Markets division owner gets updated for other divisions when the other divisions have not modified the Original Publish Date.", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			"US", "pim","SPECIAL_MARKET_CATALOG" })
	public void verify_publish_date_changed_by_special_markets_division_for_US(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("SpecialMarketsDivision"));
		log(LogType.INFO, "Navigate to Special Markets Catalog tab from Queries Menu for US Catalog");
		BasePage.WaitForMiliSec(3000);

		String publishflag = catalogTypePage.getPublishFlag();
		Assertions.assertThat(publishflag).isEqualTo(map.get("PublishFlag"));
//		catalogTypePage.selectPublishDate(DateandTimeUtils.getTodaysDate());
//		productdetailspage.clickRefreshIcon();
		String specialmarketspublishdate = catalogTypePage.getPublishDate();
		log(LogType.INFO, "Change publish date to current date");

		// verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus"))
				.maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
		for (String Rules : javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule"))) {
			Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);
		}
		qualitystatus.minimizeQualityStatusTab();
		log(LogType.INFO, "Verifying catalog entity rule");

		productdetailspage.selectTabfromDropdown(map.get("DentalDivision")).clickRefreshIcon();
		String dentalpublishdate = catalogTypePage.getPublishDate();
		Assertions.assertThat(dentalpublishdate).isEqualTo(specialmarketspublishdate);
		log(LogType.INFO, "verified publish date in Dental division");

		productdetailspage.selectTabfromDropdown(map.get("MedicalDivision")).clickRefreshIcon();
		String medicalpublishdate = catalogTypePage.getPublishDate();
		Assertions.assertThat(medicalpublishdate).isEqualTo(specialmarketspublishdate);
		log(LogType.INFO, "verified publish date in Medical division");

		productdetailspage.selectTabfromDropdown(map.get("ZahnDivision")).clickRefreshIcon();
		String zahnpublishdate = catalogTypePage.getPublishDate();
		Assertions.assertThat(zahnpublishdate).isEqualTo(specialmarketspublishdate);
		log(LogType.INFO, "verified publish date in Zahn division");

		// master
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("SpecialMarketsDivision")).clickRefreshIcon();
		log(LogType.INFO, "Navigate to Master Catalog");
		BasePage.WaitForMiliSec(3000);

		String masterpublishflag = catalogTypePage.getPublishFlag();
		Assertions.assertThat(masterpublishflag).isEqualTo(map.get("PublishFlag"));
		log(LogType.INFO, "getting publishdate");

		productdetailspage.selectTabfromDropdown(map.get("DentalDivision")).clickRefreshIcon();
		String masterdentalpublishdate = catalogTypePage.getPublishDate();
		Assertions.assertThat(masterdentalpublishdate).isEqualTo(specialmarketspublishdate);
		log(LogType.INFO, "verified publish date in Dental division");

		productdetailspage.selectTabfromDropdown(map.get("MedicalDivision")).clickRefreshIcon();
		String mastermedicalpublishdate = catalogTypePage.getPublishDate();
		Assertions.assertThat(mastermedicalpublishdate).isEqualTo(specialmarketspublishdate);
		log(LogType.INFO, "verified publish date in Medical division");

		productdetailspage.selectTabfromDropdown(map.get("ZahnDivision")).clickRefreshIcon();
		String masterzahnpublishdate = catalogTypePage.getPublishDate();
		Assertions.assertThat(masterzahnpublishdate).isEqualTo(specialmarketspublishdate);
		log(LogType.INFO, "verified publish date in Zahn division");
		pimHomepage.clickLogoutButton();
	}

	// ITP_037
	@PimFrameworkAnnotation(module = Modules.SPECIAL_MARKET_CATALOG, category = CategoryType.Data_Issue)
	@TestDataSheet(sheetname = TestCaseSheet.ItemPublish)
	@Test(priority = 1, description = "ITP_037 Verification Part | ITP_037 | Verify whether the Publish date changed by the Special Markets Owner division should not gets updated for other divisions when the other divisions have modified the Original Publish Date.", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			"US", "pim","SPECIAL_MARKET_CATALOG" })
	public void verify_publish_date_not_update_for_zahn_when_changed_in_special_markets_for_US(
			Map<String, String> map) {

		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Special Markets User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Special Markets User").get("Password"))
				.clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("SpecialMarketsDivision"));
		log(LogType.INFO, "Navigate to Special Markets Catalog tab from Queries Menu for US Catalog");
		BasePage.WaitForMiliSec(3000);

		//Updates special market publish date to current date
		String publishflag = catalogTypePage.getPublishFlag();
		Assertions.assertThat(publishflag).isEqualTo(map.get("PublishFlag"));
//		catalogTypePage.selectPublishDate(DateandTimeUtils.getTodaysDate());
//		productdetailspage.clickRefreshIcon();
		String specialmarketspublishdate = catalogTypePage.getPublishDate();
		log(LogType.INFO, "Change publish date to current date");
		pimHomepage.clickLogoutButton();
		log(LogType.INFO, "Logout from Special markets user");

		loginpage.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("ZahnDivision"));
		log(LogType.INFO, "Navigate to Zahn Catalog tab from Queries Menu for US Catalog");
		BasePage.WaitForMiliSec(3000);

		//updates zahn division publish date to future date
		String specialpublishflag = catalogTypePage.getPublishFlag();
		Assertions.assertThat(specialpublishflag).isEqualTo(map.get("PublishFlag"));
		catalogTypePage.selectPublishDate(DateandTimeUtils.getFutureDate());
		productdetailspage.clickRefreshIcon();
		String zahnpublishdate = catalogTypePage.getPublishDate();
		productdetailspage.clickRefreshIcon();

		// verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus"))
				.maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
		for (String Rules : javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule"))) {
			Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);
		}
		qualitystatus.minimizeQualityStatusTab();
		log(LogType.INFO, "Verifying catalog entity rule");


		//zahn division gets updated for dental
		productdetailspage.selectTabfromDropdown(map.get("DentalDivision")).clickRefreshIcon();
		String dentalpublishdate = catalogTypePage.getPublishDate();
		Assertions.assertThat(dentalpublishdate).isEqualTo(zahnpublishdate);
		log(LogType.INFO, "verified publish date in Dental division");

		//zahn division gets updated for medical
		productdetailspage.selectTabfromDropdown(map.get("MedicalDivision")).clickRefreshIcon();
		String medicalpublishdate = catalogTypePage.getPublishDate();
		Assertions.assertThat(medicalpublishdate).isEqualTo(zahnpublishdate);
		log(LogType.INFO, "verified publish date in Medical division");

		//zahn division does not get updated for special market
		productdetailspage.selectTabfromDropdown(map.get("ZahnDivision")).clickRefreshIcon();
		Assertions.assertThat(zahnpublishdate).isNotEqualTo(specialmarketspublishdate);
		log(LogType.INFO, "verified publish date in Zahn division");

		// master
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("SpecialMarketsDivision")).clickRefreshIcon();
		log(LogType.INFO, "Navigate to Master Catalog");
		BasePage.WaitForMiliSec(3000);
		String masterpublishflag = catalogTypePage.getPublishFlag();
		Assertions.assertThat(masterpublishflag).isEqualTo(map.get("PublishFlag"));
		log(LogType.INFO, "Change publish date to current date");

		//zahn division gets updated for dental
		productdetailspage.selectTabfromDropdown(map.get("DentalDivision")).clickRefreshIcon();
		String masterdentalpublishdate = catalogTypePage.getPublishDate();
		Assertions.assertThat(masterdentalpublishdate).isEqualTo(zahnpublishdate);
		log(LogType.INFO, "verified publish date in Dental division");

		//zahn division gets updated for medical
		productdetailspage.selectTabfromDropdown(map.get("MedicalDivision")).clickRefreshIcon();
		String mastermedicalpublishdate = catalogTypePage.getPublishDate();
		Assertions.assertThat(mastermedicalpublishdate).isEqualTo(zahnpublishdate);
		log(LogType.INFO, "verified publish date in Medical division");

		//zahn division does not get updated for special market
		productdetailspage.selectTabfromDropdown(map.get("ZahnDivision")).clickRefreshIcon();
		String masterspecialmarketspublishdate = catalogTypePage.getPublishDate();
		Assertions.assertThat(masterspecialmarketspublishdate).isNotEqualTo(specialmarketspublishdate);
		log(LogType.INFO, "verified publish date in Zahn division");
		pimHomepage.clickLogoutButton();

	}

	// ITP_020
	// Verify Item with Publish Flag = 'Y" and Publish Date as Future Date - All
	// Divisions and check in Middleware
	@PimFrameworkAnnotation(module = Modules.SPECIAL_MARKET_CATALOG, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemPublish)
	@Test(priority = 1, description = "ITP_020 Verification Part | ITP_020 | Verify whether the Publish Flag for a Special Markets Item get updated as 'Y' and publish date as Future in the Middleware Export File, when the Items loaded from PIX to PIM with Publish Flag as \"Y\" ", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			"US", "pim","SPECIAL_MARKET_CATALOG" })
	public void verify_publish_flag_should_yes_and_publish_date_should_be_future_US_for_Special_Markets_division(
			Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Special Markets User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Special Markets User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("SpecialMarketsDivision"));
		log(LogType.INFO, "Navigate to Dental tab from Queries Menu for US Catalog");

		String dentalpublishflag = catalogTypePage.getPublishFlag();
		String Y = "Yes";
//		if (!dentalpublishflag.equals(Y)) {
//			catalogTypePage.selectPublishFlag(map.get("PublishFlag"));
//			log(LogType.INFO, "assigning yes in publish flag");
//		}
//		catalogTypePage.selectPublishDate(DateandTimeUtils.getFutureDate());
//		log(LogType.INFO, "assigning future date");
//		productdetailspage.clickRefreshIcon();
		String date = catalogTypePage.getPublishDate();

		// verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus"))
				.maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
		for (String Rules : javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule"))) {
			Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);
		}
		qualitystatus.minimizeQualityStatusTab();
		log(LogType.INFO, "Verifying catalog entity rule");

		// master
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("SpecialMarketsDivision")).clickRefreshIcon();
		log(LogType.INFO, "Navigate to Master Catalog");

		String masterdate = catalogTypePage.getPublishDate();
		Assertions.assertThat(date).isEqualTo(masterdate);
		log(LogType.INFO, "verifying dates should get update in master");
		pimHomepage.clickLogoutButton();
	}

	// ITP_028
	@PimFrameworkAnnotation(module = Modules.SPECIAL_MARKET_CATALOG, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemPublish)
	@Test(priority = 1, description = "ITP_028 Verification Part | ITP_028 | Verify whether the Publish Flag for a Special Markets Item get updated as \"N\" in the Middleware Export File, when the Items loaded from PIX to PIM with Publish Flag as \"N\" ", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			"US", "pim","SPECIAL_MARKET_CATALOG" })
	public void verify_publish_flag_should_updated_as_NO_in_Special_Markets_division_for_US(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Special Markets User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Special Markets User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("SpecialMarketsDivision"));
		log(LogType.INFO, "Navigate to Zahn tab from Queries Menu for US Catalog");

		String dentalpublishflag = catalogTypePage.getPublishFlag();
//		if (!dentalpublishflag.equals("No")) {
//			catalogTypePage.selectPublishFlag(map.get("PublishFlag"));
//			log(LogType.INFO, "assigning No in publish flag");
//		}
//		List<String> userdrivencatalogs = catalogTypePage.getUserDrivenCatalogs();
//		if (!userdrivencatalogs.contains(map.get("userDriven"))) {
//			catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
//		}

		// verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus"))
				.maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
		for (String Rules : javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule"))) {
			// Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);
		}
		qualitystatus.minimizeQualityStatusTab();
		log(LogType.INFO, "Verifying catalog entity rule");

		// master
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("SpecialMarketsDivision")).clickRefreshIcon();
		log(LogType.INFO, "Navigate to Zahn division in Master Catalog");

		String masterpublishflag = catalogTypePage.getPublishFlag();
		Assertions.assertThat(masterpublishflag).isEqualTo(map.get("PublishFlag"));
		pimHomepage.clickLogoutButton();
	}

	// ITP_024
	@PimFrameworkAnnotation(module = Modules.SPECIAL_MARKET_CATALOG, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemPublish)
	@Test(priority = 1, description = "ITP_024 Verification Part | ITP_024 | Verify whether the Publish Flag for a Special Markets Item get updated as \"Y\" and publish date as Current in the Middleware Export File, when the Items loaded from PIX to PIM with Publish Flag as \"Y\"", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			"US", "pim","SPECIAL_MARKET_CATALOG" })
	public void verify_publish_flag_should_updated_as_Yes_in_Special_Markets_division_for_US(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Special Markets User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Special Markets User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("SpecialMarketsDivision"));
		log(LogType.INFO, "Navigate to Special Markets tab from Queries Menu for US Catalog");

//		catalogTypePage.selectPublishFlag(map.get("PublishFlag"));
//		log(LogType.INFO, "assigning Yes in publish flag");
//
//		catalogTypePage.selectPublishDate(DateandTimeUtils.getTodaysDate());
//		productdetailspage.clickRefreshIcon();
//		log(LogType.INFO, "assigning current date");

		String publishdate = catalogTypePage.getPublishDate();
		String publishFlag = catalogTypePage.getPublishFlag();

		List<String> userdrivencatalogs = catalogTypePage.getUserDrivenCatalogs();
		if (!userdrivencatalogs.contains(map.get("userDriven"))) {
			catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
		}

		// verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus"))
				.maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
		for (String Rules : javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule"))) {
			Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);
		}
		qualitystatus.minimizeQualityStatusTab();
		log(LogType.INFO, "Verifying catalog entity rule");

		// master
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("SpecialMarketsDivision")).clickRefreshIcon();
		log(LogType.INFO, "Navigate to Special Markets division in Master Catalog");

		String masterpublishdate = catalogTypePage.getPublishDate();
		String masterpublishFlag = catalogTypePage.getPublishFlag();

		Assertions.assertThat(masterpublishdate).isEqualTo(publishdate);
		Assertions.assertThat(masterpublishFlag).isEqualTo(publishFlag);
		pimHomepage.clickLogoutButton();
	}

	

	
	
	
	
	
	

	

	
	
	

//		CatalogTypePage catalogTypePage = new CatalogTypePage();
		ProductDetailSearchPage productdetails = new ProductDetailSearchPage();
		AllCatalogsPage allcatalog = new AllCatalogsPage();
//		QualityStatusPage qualitystatus = new QualityStatusPage();
//		Javautils javautils = new Javautils();
//		GlobalAttributePage globalattribute = new GlobalAttributePage();

		//List<String> rule_driven;
		//List<String> multiple_rule_driven;
		//List<String> all_catalog_special_markets;
		//List<String> all_catalog_master_special_markets;
//		String manufacturecode;

		//Verification of Copy Rules Functionality in Special Markets Division when assign DENTAL in dental division TCID - CATR_136 to CATR_139
		@PimFrameworkAnnotation(module = Modules.SPECIAL_MARKET_CATALOG, category = CategoryType.REGRESSION)
		@TestDataSheet(sheetname = TestCaseSheet.CopyRuleCatalog)
		@Test(priority = 1, description = "CATR_136, Verification Part | CATR_136, CATR_137, CATR_138, CATR_139 | Verify whether the catalog COMFORTDTL gets appended to the target division based on the Special Markets Copy Rule for CAGA", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"US","pim","SPECIAL_MARKET_CATALOG"})
		public void verify_catalog_get_appended_to_the_target_division_based_on_special_market_copy_rule_when_assign_DENTAL(Map<String, String> map){
			PimHomepage pimHomepage = new LoginPage()
					.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
					.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
			pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
			.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("DentalTab"));
			log(LogType.INFO,"Navigate to Dental tab from Queries Menu for US Catalog");

//			catalogTypePage.clickUserDriven().clearUserDriven(map.get("userDriven")).clickUserDriven().selectUserDriven(map.get("userDriven"));
//			pimHomepage.productDetailSearchPage().clickRefreshIcon();
//			log(LogType.INFO,"Assigning userdriven in Dental Division");

			productdetails.selectTabfromDropdown(map.get("SpecialMarketTab")).clickRefreshIcon();
			log(LogType.INFO,"Navigate to Special markets tab from Queries Menu for US Catalog");
			List<String> rule_driven = catalogTypePage.getRuleDrivenCatalogs();

			List<String> multiple_rule_driven = javautils.readMultipleValuesFromExcel(map.get("RuleDriven"));
			for(String ruledriven : multiple_rule_driven) 
				Assertions.assertThat(rule_driven).containsAnyOf(ruledriven);

			productdetails.selectTabfromDropdown(map.get("AllCatalogtab")).clickRefreshIcon();
			log(LogType.INFO,"Navigate to All Catalog tab from Queries Menu for US Catalog");
			List<String> all_catalog_special_markets = allcatalog.getAllSpecialMarketsCatalogList();

			for(String allcatalog : multiple_rule_driven)
				Assertions.assertThat(all_catalog_special_markets).containsAnyOf(allcatalog);

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
			pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog"))
			.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("AllCatalogtab")).clickRefreshIcon();

			List<String> all_catalog_master_special_markets = allcatalog.getAllSpecialMarketsCatalogList();

			for(String masterallcatalog : multiple_rule_driven)
				Assertions.assertThat(all_catalog_master_special_markets).containsAnyOf(masterallcatalog);

			//delete assigned userdriven
			pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType"))
			.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("DentalTab"));
			catalogTypePage.clearSelectedFields(map.get("userDriven"));
			log(LogType.INFO,"Deleting the Userdriven");
			pimHomepage.clickLogoutButton();
		}

		//Verification of Copy Rules Functionality in Special Markets Division when assign MEDICAL in medical division TCID - CATR_140
		@PimFrameworkAnnotation(module = Modules.SPECIAL_MARKET_CATALOG, category = CategoryType.REGRESSION)
		@TestDataSheet(sheetname = TestCaseSheet.CopyRuleCatalog)
		@Test(priority = 1, description = "CATR_140 Verification Part | CATR_140 | Verify whether the catalog PRIME gets appended to the target division based on the Special Markets Copy Rule for MEDICAL", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"US","pim","SPECIAL_MARKET_CATALOG"})
		public void verify_catalog_get_appended_to_the_target_division_based_on_special_market_copy_rule_when_assign_MEDICAL(Map<String, String> map){
			PimHomepage pimHomepage = new LoginPage()
					.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
					.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
			pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
			.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("MedicalTab"));
			log(LogType.INFO,"Navigate to Medical Division from Queries Menu for US Catalog");

//			catalogTypePage.clickUserDriven().clearUserDriven(map.get("userDriven")).clickUserDriven().selectUserDriven(map.get("userDriven"));
//			pimHomepage.productDetailSearchPage().clickRefreshIcon();
//			log(LogType.INFO,"Assigning userdriven in Medical Division");

			productdetails.selectTabfromDropdown(map.get("SpecialMarketTab")).clickRefreshIcon();
			log(LogType.INFO,"Navigate to Special markets tab from Queries Menu for US Catalog");
			List<String> rule_driven = catalogTypePage.getRuleDrivenCatalogs();

			List<String> multiple_rule_driven = javautils.readMultipleValuesFromExcel(map.get("RuleDriven"));
			for(String ruledriven : multiple_rule_driven) 
				Assertions.assertThat(rule_driven).containsAnyOf(ruledriven);

			productdetails.selectTabfromDropdown(map.get("AllCatalogtab")).clickRefreshIcon();
			log(LogType.INFO,"Navigate to All Catalog tab from Queries Menu for US Catalog");
			List<String> all_catalog_special_markets = allcatalog.getAllSpecialMarketsCatalogList();

			for(String allcatalog : multiple_rule_driven)
				Assertions.assertThat(all_catalog_special_markets).containsAnyOf(allcatalog);

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
			pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog"))
			.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("AllCatalogtab")).clickRefreshIcon();

			List<String> all_catalog_master_special_markets = allcatalog.getAllSpecialMarketsCatalogList();

			for(String masterallcatalog : multiple_rule_driven)
				Assertions.assertThat(all_catalog_master_special_markets).containsAnyOf(masterallcatalog);

			//delete assigned userdriven
			pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType"))
			.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("MedicalTab"));
			catalogTypePage.clearSelectedFields(map.get("userDriven"));
			log(LogType.INFO,"Deleting the Userdriven");
			pimHomepage.clickLogoutButton();
		}
		
		
		
		CatalogTypePage catalogtype = new CatalogTypePage();
		ClassificationsPage classificationpage = new ClassificationsPage();
//		QualityStatusPage qualitystatus = new QualityStatusPage();
		GlobalAttributePage globalattribute = new GlobalAttributePage();
		Javautils javautil = new Javautils();
		List<String> rule_driven;
		String primarytaxonomy;
		String ecommercetaxonomy;
		String manufacturecode;
		String suppliercode;
		
		
		
		

		//Verification of Copy Rules Functionality in Special Markets Division when assign DENTAL in dental division for ~ex manufacturer code TCID - CATR_141 and CATR_142
		@PimFrameworkAnnotation(module = Modules.SPECIAL_MARKET_CATALOG, category = CategoryType.REGRESSION)
		@TestDataSheet(sheetname = TestCaseSheet.CopyRuleCatalog)
		@Test(priority = 1, description = "CATR_141 Verification Part | CATR_141 and CATR_142 | Verify whether the catalog FEDERAL gets appended to the target division based on the Special Markets Copy Rule for PRIME", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"US","pim","SPECIAL_MARKET_CATALOG"})
		public void verify_catalog_get_appended_to_the_target_division_based_on_special_market_copy_rule_when_assign_DENTAL_for_exclusive_manufacturer_code(Map<String, String> map){
			PimHomepage pimHomepage = new LoginPage()
					.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
					.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
			pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
			.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
			log(LogType.INFO,"Navigate to Global Attribute tab from Queries Menu for US Catalog");
			
			manufacturecode = globalattribute.getManufacturercode();
			for(String code: javautils.readMultipleValuesFromExcel(map.get("ManufactureCode"))) {
				Assertions.assertThat(manufacturecode).isNotEqualTo(code);
			}
			log(LogType.INFO,"Verifying manufacturer code");

			productdetails.selectTabfromDropdown(map.get("DentalTab"));
//			catalogTypePage.clickUserDriven().clearUserDriven(map.get("userDriven")).clickUserDriven().selectUserDriven(map.get("userDriven"));
//			pimHomepage.productDetailSearchPage().clickRefreshIcon();
//			log(LogType.INFO,"Assigning userdriven in Dental Division");

			productdetails.selectTabfromDropdown(map.get("SpecialMarketTab")).clickRefreshIcon();
			log(LogType.INFO,"Navigate to Special markets tab from Queries Menu for US Catalog");
			List<String> rule_driven = catalogTypePage.getRuleDrivenCatalogs();

			List<String> multiple_rule_driven = javautils.readMultipleValuesFromExcel(map.get("RuleDriven"));
			for(String ruledriven : multiple_rule_driven) 
				Assertions.assertThat(rule_driven).containsAnyOf(ruledriven);

			productdetails.selectTabfromDropdown(map.get("AllCatalogtab")).clickRefreshIcon();
			log(LogType.INFO,"Navigate to All Catalog tab from Queries Menu for US Catalog");
			List<String> all_catalog_special_markets = allcatalog.getAllSpecialMarketsCatalogList();

			for(String allcatalog : multiple_rule_driven)
				Assertions.assertThat(all_catalog_special_markets).containsAnyOf(allcatalog);

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
			pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog"))
			.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("AllCatalogtab")).clickRefreshIcon();

			List<String> all_catalog_master_special_markets = allcatalog.getAllSpecialMarketsCatalogList();

			for(String masterallcatalog : multiple_rule_driven)
				Assertions.assertThat(all_catalog_master_special_markets).containsAnyOf(masterallcatalog);

			//delete assigned userdriven
			pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType"))
			.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("DentalTab"));
			catalogTypePage.clearSelectedFields(map.get("userDriven"));
			log(LogType.INFO,"Deleting the Userdriven");
			pimHomepage.clickLogoutButton();

		}


		
		
		
		
		
		
		
		
		
		


			LocalAttributePage localAttributePage = new LocalAttributePage();
			StructureSubMenu structuresubmenu=new StructureSubMenu();
			ProductDetailSearchPage productDetailSearchPage = new ProductDetailSearchPage();                     
			MainMenu mainmenu=new MainMenu();
//			QueriesSubMenu queriesSubMenu=new QueriesSubMenu();
			BasePage basePage = new BasePage();;
			ReferencesPage referencesPage=new  ReferencesPage();
//			CatalogTypePage catalogTypePage = new CatalogTypePage();
//			AllCatalogsPage allCatalogsPage=new AllCatalogsPage();
//			QualityStatusPage qualitystatus = new QualityStatusPage();
//			Javautils javautils = new Javautils();
			//List<String> user_driven;
			//List<String> all_catalog_Dental;
			//List<String> all_catalog_Medical;
			//List<String> all_catalog_SM;
			//List<String> all_catalog_Zahn;



			//CATR_146--Done
			@PimFrameworkAnnotation(module = Modules.SubstituteCatalogRule, category = CategoryType.REGRESSION)
			@TestDataSheet(sheetname = TestCaseSheet.Catalog)
			@Test(priority = 1, description = "CATR_146 Verification Part | CATR_146 | Verification Of Substitution Rules In Dental Divisions With Sub Type As S/SA/GE", groups = {"US","pim","DentalSubstituteCatalogRule"} ,dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)

			public void VerificationOfSubstitutionRulesInDentalCatalogWithSubTypeAsSA(Map<String, String> map)   
			{

				PimHomepage pimHomepage = new LoginPage()
						.enterUserName(ExcelUtils.getLoginData().get("Dental User").get("UserName"))
						.enterPassword(ExcelUtils.getLoginData().get("Dental User").get("Password")).clickLoginButton();

				pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
				.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("ReferenceTab"));
				String referenceType = referencesPage.getReferenceType();
				Assertions.assertThat(referenceType).isEqualTo(map.get("ReferenceType"));
				String referenceObjectNumber = referencesPage.getReferenceObjectNumber();
				productDetailSearchPage.selectTabfromDropdown(map.get("TabName"));

//				catalogTypePage.clearUserDriven(map.get("userDriven"));
//				BasePage.WaitForMiliSec(3000);
//				String userDrivernCatalog =map.get("userDriven");
//				catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
//				pimHomepage.productDetailSearchPage().clickRefreshIcon();
//				List<String> user_driven = catalogTypePage.getUserDrivenCatalogs();
//				Assertions.assertThat(user_driven).containsAnyOf(userDrivernCatalog);
				log(LogType.INFO, "Navigate to Dental catalog Tab to assign dental in userdriven in Parent");

				//verify quality status 
				pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
				List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus"))
						.maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
				for (String Rules:javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule")))
				{
					Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);	
				}
				qualitystatus.minimizeQualityStatusTab();
				log(LogType.INFO,"Verifying catalog entity rule");

				//Verify in master catalog
				pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog"))
				.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
				List<String> masterUserdriven = catalogTypePage.getUserDrivenCatalogs();
				Assertions.assertThat(user_driven).containsAll(masterUserdriven);
				log(LogType.INFO, "Verifying Dental Catalog in Master");

				// Verifying in child item
				pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType"))
				.removeAndEnterHsiItemNumber(referenceObjectNumber).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("AllCatalogtab"));
				List<String> all_catalog_Dental =allCatalogsPage.getAllDentalCatalogList();
				Assertions.assertThat(all_catalog_Dental).contains(map.get("userDriven"));	
				log(LogType.INFO, "Verifying Catalogs in Child Item");
			}

			//CATR_150--Not Working
			//Verify Substitute Of Type SA For Any Division Item Does Not Populates With The Same Catalog List Of Parent Item
			@PimFrameworkAnnotation(module = Modules.SubstituteCatalogRule, category = CategoryType.Valid_Failure)
			@TestDataSheet(sheetname = TestCaseSheet.Catalog)
			@Test(priority = 1, description = "CATR_150 Verification Part | CATR_150 | Verify Substitute Of Type SA For Any Division Item Does Not Populates With The Same Catalog List Of Parent Item", groups = {"US","pim","DentalSubstituteCatalogRule"} ,dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
			public void VerifySubstituteOfTypeSAForAnyDivisionItemDoesNotPopulatesWithTheSameCatalogListOfParentItem(Map<String, String> map)   
			{
				PimHomepage pimHomepage = new LoginPage()
						.enterUserName(ExcelUtils.getLoginData().get("Dental User").get("UserName"))
						.enterPassword(ExcelUtils.getLoginData().get("Dental User").get("Password")).clickLoginButton();

				pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
				.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("ReferenceTab"));
				String referenceType = referencesPage.getReferenceType();
				Assertions.assertThat(referenceType).isEqualTo(map.get("ReferenceType"));
				String referenceObjectNumber = referencesPage.getReferenceObjectNumber();
				productDetailSearchPage.selectTabfromDropdown(map.get("TabName"));

//				catalogTypePage.clearUserDriven(map.get("userDriven"));
//				BasePage.WaitForMiliSec(3000);
				String userDrivernCatalog =map.get("userDriven");
//				catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
//				pimHomepage.productDetailSearchPage().clickRefreshIcon();
				List<String> user_driven = catalogTypePage.getUserDrivenCatalogs();
				Assertions.assertThat(user_driven).containsAnyOf(userDrivernCatalog);
				log(LogType.INFO, "Navigate to Dental Catalog Tab to assign Dental catalog in Parent");
				//Verify Quality Status rule
				pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
				List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus"))
						.maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
				for (String Rules:javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule")))
				{
					Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);	
				}
				qualitystatus.minimizeQualityStatusTab();
				log(LogType.INFO,"Verifying catalog entity rule");

				//Verify in master catalog
				pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog"))
				.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
				List<String> masterUserdriven = catalogTypePage.getUserDrivenCatalogs();
				Assertions.assertThat(user_driven).containsAll(masterUserdriven);
				log(LogType.INFO, "Verifying Dental Catalog in Master");

				// Verifying in child item
				pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType"))
				.removeAndEnterHsiItemNumber(referenceObjectNumber).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("AllCatalogtab"));
				List<String> all_catalog_Dental =allCatalogsPage.getAllDentalCatalogList();
				Assertions.assertThat(all_catalog_Dental).doesNotContainSequence(userDrivernCatalog);
				log(LogType.INFO, "Verifying Catalog in Child Item should not get assign");
			}
			//CATR_153--Done
			@PimFrameworkAnnotation(module = Modules.SubstituteCatalogRule, category = CategoryType.REGRESSION)
			@TestDataSheet(sheetname = TestCaseSheet.Catalog)
			@Test(priority = 1, description = "CATR_153 Verification Part | CATR_153 | Verification Of Substitution Rules In Dental Divisions With Sub Type As SP CATR_153", groups = {"US","pim","DentalSubstituteCatalogRule"} , dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
			public void VerificationOfSubstitutionRulesForDentalDivisionWithSubTypeAsSP(Map<String, String> map) {

				PimHomepage pimHomepage = new LoginPage()
						.enterUserName(ExcelUtils.getLoginData().get("Dental User").get("UserName"))
						.enterPassword(ExcelUtils.getLoginData().get("Dental User").get("Password")).clickLoginButton();

				pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
				.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("ReferenceTab"));
				List<String> referenceType = referencesPage.getReferenceTypeInList();
				Assertions.assertThat(referenceType).containsAnyOf((map.get("ReferenceType")));
				//Assertions.assertThat(referenceType).isEqualTo(map.get("ReferenceType"));
				String referenceObjectNumber = referencesPage.getReferenceObjectNumber();
				productDetailSearchPage.selectTabfromDropdown(map.get("TabName"));

//				catalogTypePage.clearUserDriven(map.get("userDriven"));
//				BasePage.WaitForMiliSec(3000);
//				String userDrivernCatalog =map.get("userDriven");
//				catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
//				pimHomepage.productDetailSearchPage().clickRefreshIcon();
//				List<String> user_driven = catalogTypePage.getUserDrivenCatalogs();
//				Assertions.assertThat(user_driven).containsAnyOf(userDrivernCatalog);

				// verify quality status
				pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
				List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
				for (String Rules:javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule")))
				{
					Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);	
				}
				qualitystatus.minimizeQualityStatusTab();
				log(LogType.INFO,"Verifying catalog entity rule");

				// Verify in master catalog
				pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
				List<String> masterUserdriven = catalogTypePage.getUserDrivenCatalogs();
				Assertions.assertThat(user_driven).containsAll(masterUserdriven);
				log(LogType.INFO, "Verifying Dental Catalog in Master");

				// Verifying Catalogs in Child item
				pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType"))
				.removeAndEnterHsiItemNumber(referenceObjectNumber).clickSeachButton().clickOnFirstResult()
				.selectTabfromDropdown(map.get("AllCatalogtab"));
				qualitystatus.maximizeQualityStatusTab();
				List<String> all_catalog_Dental =allCatalogsPage.getAllDentalCatalogList();
				Assertions.assertThat(all_catalog_Dental).contains(map.get("userDriven"));
				qualitystatus.minimizeQualityStatusTab();
				log(LogType.INFO, "Verifying Catalogs in Child Item");

			}

			//CATR_156--Done
			@PimFrameworkAnnotation(module = Modules.SubstituteCatalogRule, category = CategoryType.REGRESSION)
			@TestDataSheet(sheetname = TestCaseSheet.Catalog)
			@Test(priority = 1, description = "CATR_156 Verification Part | CATR_156 | Verification Of Substitution Rules In Dental Divisions With Sub Type As PA", groups = {"US","pim","DentalSubstituteCatalogRule"} , dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
			public void VerificationOfSubstitutionRulesInDentalDivisionsWithSubTypeAsPA(Map<String, String> map) {

				PimHomepage pimHomepage = new LoginPage()
						.enterUserName(ExcelUtils.getLoginData().get("Dental User").get("UserName"))
						.enterPassword(ExcelUtils.getLoginData().get("Dental User").get("Password")).clickLoginButton();

				pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("ReferenceTab"));
				String referenceType = referencesPage.getReferenceType();
				Assertions.assertThat(referenceType).isEqualTo(map.get("ReferenceType"));
				String referenceObjectNumber = referencesPage.getReferenceObjectNumber();
				productDetailSearchPage.selectTabfromDropdown(map.get("TabName"));

//				catalogTypePage.clearUserDriven(map.get("userDriven"));
//				BasePage.WaitForMiliSec(3000);
				String userDrivernCatalog = map.get("userDriven");
//				catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
//				pimHomepage.productDetailSearchPage().clickRefreshIcon();
//				List<String> user_driven = catalogTypePage.getUserDrivenCatalogs();
				Assertions.assertThat(user_driven).containsAnyOf(userDrivernCatalog);
				log(LogType.INFO, "Navigate to Dental Catalog Tab to assign Dental catalog in Parent");

				// verify quality status
				pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
				List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
				for (String Rules:javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule")))
				{
					Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);	
				}
				qualitystatus.minimizeQualityStatusTab();
				log(LogType.INFO,"Verifying catalog entity rule");

				// Verify in master catalog
				pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
				List<String> masterUserdriven = catalogTypePage.getUserDrivenCatalogs();
				Assertions.assertThat(user_driven).containsAll(masterUserdriven);
				log(LogType.INFO, "Verifying Dental Catalog in Master");

				// Verifying in Child item
				pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType"))
				.removeAndEnterHsiItemNumber(referenceObjectNumber).clickSeachButton().clickOnFirstResult()
				.selectTabfromDropdown(map.get("AllCatalogtab"));
				qualitystatus.maximizeQualityStatusTab();
				List<String> all_catalog_Dental = allCatalogsPage.getAllDentalCatalogList();
				Assertions.assertThat(all_catalog_Dental).contains(map.get("userDriven"));
				qualitystatus.minimizeQualityStatusTab();
				log(LogType.INFO, "Verifying Catalogs in Child Item");

				//Verify In Medical Catalog should not be populated for child item
				List<String> all_catalog_Medical =allCatalogsPage.getAllMedicalCatalogList();
				Assertions.assertThat(all_catalog_Medical).doesNotContainSequence(userDrivernCatalog);
				log(LogType.INFO, "Verifying Catalogs which are not populated in Child Item");
			}

			//CATR_162-Done
			@PimFrameworkAnnotation(module = Modules.SubstituteCatalogRule, category = CategoryType.REGRESSION)
			@TestDataSheet(sheetname = TestCaseSheet.Catalog)
			@Test(priority = 1, description = "CATR_162 Verification Part | CATR_162 | Verification Of Substitution Rules In Dental Divisions With Sub Type As A3", groups = {"US","pim","DentalSubstituteCatalogRule"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
			public void VerificationOfSubstitutionRulesInDentalDivisionsWithSubTypeAsA3(Map<String, String> map) {

				PimHomepage pimHomepage = new LoginPage()
						.enterUserName(ExcelUtils.getLoginData().get("Dental User").get("UserName"))
						.enterPassword(ExcelUtils.getLoginData().get("Dental User").get("Password")).clickLoginButton();

				pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("ReferenceTab"));
				String referenceType = referencesPage.getReferenceType();
				Assertions.assertThat(referenceType).isEqualTo(map.get("ReferenceType"));
				String referenceObjectNumber = referencesPage.getReferenceObjectNumber();
				productDetailSearchPage.selectTabfromDropdown(map.get("TabName"));

//				catalogTypePage.clearUserDriven(map.get("userDriven"));
//				BasePage.WaitForMiliSec(3000);
				String userDrivernCatalog = map.get("userDriven");
//				catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
//				pimHomepage.productDetailSearchPage().clickRefreshIcon();
//				List<String> user_driven = catalogTypePage.getUserDrivenCatalogs();
				Assertions.assertThat(user_driven).containsAnyOf(userDrivernCatalog);
				log(LogType.INFO, "Navigate to Dental Catalog Tab to assign Dental catalog in Parent");

				// verify quality status
				pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
				List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
				for (String Rules:javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule")))
				{
					Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);	
				}
				qualitystatus.minimizeQualityStatusTab();
				log(LogType.INFO,"Verifying catalog entity rule");


				// Verify in master catalog
				pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
				List<String> masterUserdriven = catalogTypePage.getUserDrivenCatalogs();
				Assertions.assertThat(user_driven).containsAll(masterUserdriven);
				log(LogType.INFO, "Verifying Dental Catalog in Master");

				// Verifying in Child item
				pimHomepage.mainMenu().clickRefreshMenuIcon().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
				.enterHsiItemNumber(referenceObjectNumber).clickSeachButton().clickOnFirstResult()
				.selectTabfromDropdown(map.get("AllCatalogtab"));
				qualitystatus.maximizeQualityStatusTab();
				List<String> all_catalog_Dental = allCatalogsPage.getAllDentalCatalogList();
				Assertions.assertThat(all_catalog_Dental).doesNotContainSequence(userDrivernCatalog);
				qualitystatus.minimizeQualityStatusTab();
				log(LogType.INFO, "Verifying Catalog in Child Item should not assign");
			}
			//CATR_166--Data Issue
			@PimFrameworkAnnotation(module = Modules.SubstituteCatalogRule, category = CategoryType.REGRESSION)
			@TestDataSheet(sheetname = TestCaseSheet.Catalog)
			@Test(priority = 1, description = "CATR_166 Verification Part | CATR_166 | Verification Of Substitution Rules In Dental Divisions With Sub Type As A6", groups = {"US","pim","DentalSubstituteCatalogRule"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
			public void VerificationOfSubstitutionRulesForDentalDivisionWithSubTypeAsA6(Map<String, String> map) {

				PimHomepage pimHomepage = new LoginPage()
						.enterUserName(ExcelUtils.getLoginData().get("Dental User").get("UserName"))
						.enterPassword(ExcelUtils.getLoginData().get("Dental User").get("Password")).clickLoginButton();

				pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("ReferenceTab"));
				String referenceType = referencesPage.getReferenceType();
				Assertions.assertThat(referenceType).isEqualTo(map.get("ReferenceType"));
				String referenceObjectNumber = referencesPage.getReferenceObjectNumber();
				productDetailSearchPage.selectTabfromDropdown(map.get("TabName"));

//				catalogTypePage.clearUserDriven(map.get("userDriven"));
//				BasePage.WaitForMiliSec(3000);
//				log(LogType.INFO, "Navigate to Dental Catalog Tab to assign Dental catalog in Parent");
//
				String userDrivernCatalog = map.get("userDriven");
//				catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
//				pimHomepage.productDetailSearchPage().clickRefreshIcon();
				List<String> user_driven = catalogTypePage.getUserDrivenCatalogs();
				Assertions.assertThat(user_driven).containsAnyOf(userDrivernCatalog);

				//verify quality status 
				pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
				List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus"))
						.maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
				for (String Rules:javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule")))
				{
					Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);	
				}
				qualitystatus.minimizeQualityStatusTab();
				log(LogType.INFO,"Verifying catalog entity rule");

				// Verify in master catalog
				pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
				List<String> masterUserdriven = catalogTypePage.getUserDrivenCatalogs();
				Assertions.assertThat(user_driven).containsAll(masterUserdriven);
				log(LogType.INFO, "Verifying Dental Catalog in Master");

				// Verifying in Child item
				pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType"))
				.removeAndEnterHsiItemNumber(referenceObjectNumber).clickSeachButton().clickOnFirstResult()
				.selectTabfromDropdown(map.get("AllCatalogtab"));
				List<String> all_catalog_Dental = allCatalogsPage.getAllDentalCatalogList();
				Assert.assertNotEquals(all_catalog_Dental, userDrivernCatalog);
				log(LogType.INFO, "Verifying Catalog in Child Item should assign");
			}

			
			
			
			
			
			
			
			
			
			
//				CatalogTypePage catalogTypePage = new CatalogTypePage();
			    ProductDetailSearchPage productdetail = new ProductDetailSearchPage();
			    QueriesSubMenu queriessubmenu = new QueriesSubMenu();
//			    QualityStatusPage qualitystatus = new QualityStatusPage();
//			    GlobalAttributePage globalattribute = new GlobalAttributePage();
//			    Javautils javautil = new Javautils();
			    List<String> medical_user_driven;

			    //CATR_127
			    @PimFrameworkAnnotation(module = Modules.MEDICAL_CATALOG, category = CategoryType.REGRESSION)
			    @TestDataSheet(sheetname = TestCaseSheet.DivisionalExceptionRule)
			    @Test(priority = 1, description = "CATR_127 Verification Part | CATR_127 | Verify whether the rule driven catalogs were removed for MPC 143 ClassItems based on the Divsional Exceptional Rule.", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"US","pim","MEDICAL_CATALOG"})
			    public void verify_rule_driven_catalog_for_medical_division_not_showing_up_for_MPC_143(Map<String, String> map) {
			        PimHomepage pimHomepage = new LoginPage()
			                .enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
			                .enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();

			        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
			                .enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
			        log(LogType.INFO, "Navigate to medical catalog tab to verify rule driven");

			        //verifying MPC code
			        String mpccode = productdetail.getItemField(map.get("fieldIndex"));
			        Assertions.assertThat(mpccode).isEqualTo(map.get("MPC"));
			        log(LogType.INFO, "Verifying MPC code");

			        //assigning medical catalog
//			        catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
//			        pimHomepage.productDetailSearchPage().clickRefreshIcon();
//			        medical_user_driven = catalogTypePage.getUserDrivenCatalogs();
//			        Assertions.assertThat(medical_user_driven).containsAnyOf(map.get("userDriven"));

			        //verify rule driven
			        String empty_rule_driven = catalogTypePage.getEmptyRuleDriven();
			        Assertions.assertThat(empty_rule_driven).isEqualTo(map.get("RuleDriven"));

			        //verify quality status
			        pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
			        qualitystatus.sortRulestByLastExecution();
			        List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("Status"));
			        Assertions.assertThat(catalogentityrule).containsAnyOf(map.get("CatalogEntityRule"));
			        log(LogType.INFO, "Verifying catalog entity rule");

			        //verify in master
			        productdetail.clickMenuRefreshIcon();
			        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
			        log(LogType.INFO, "Navigate to medical catalog tab to verify the userdriven and rule driven in Master Catalog");

			        //verifying MPC code
			        mpccode = productdetail.getItemField(map.get("fieldIndex"));
			        Assertions.assertThat(mpccode).isEqualTo(map.get("MPC"));
			        log(LogType.INFO, "Verifying MPC code");

			        List<String> masterUserdriven = catalogTypePage.getUserDrivenCatalogs();
			        String masterRuledriven = catalogTypePage.getEmptyRuleDriven();
			        Assertions.assertThat(masterUserdriven).containsAnyOf(map.get("userDriven"));
			        Assertions.assertThat(masterRuledriven).isEqualTo(map.get("RuleDriven"));

			        //Deleting userdriven
			        productdetail.clickMenuRefreshIcon();
			        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
			                .enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
			        catalogTypePage.clearSelectedFields(map.get("userDriven"));
			        log(LogType.INFO, "Deleting the Userdriven");
			        pimHomepage.clickLogoutButton();
			    }

			    //CATR_128
			    @PimFrameworkAnnotation(module = Modules.MEDICAL_CATALOG, category = CategoryType.REGRESSION)
			    @TestDataSheet(sheetname = TestCaseSheet.DivisionalExceptionRule)
			    @Test(priority = 1, description = "CATR_128 Verification Part |  CATR_128 | Verify whether the user driven catalogs showing up INSOURCE & C340B catalog for M61/G61 ClassItems based on the Divsional Exceptional Rule.", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"US","pim","MEDICAL_CATALOG"})
			    public void verify_user_driven_catalog_for_medical_division_showing_up_only_for_INSOURCE_C340B_for_M61_G61_Class(Map<String, String> map) {
			        PimHomepage pimHomepage = new LoginPage()
			                .enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
			                .enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();

			        List<String> itemcodelist = javautil.readMultipleValuesFromExcel(map.get("ItemNumber"));
			        List<String> classificationcodelist = javautil.readMultipleValuesFromExcel(map.get("Classification"));
			        List<String> alluserdrivenlist = javautil.readMultipleValuesFromExcel(map.get("allUserDriven"));
			        List<String> userdrivenlist = javautil.readMultipleValuesFromExcel(map.get("userDriven"));
			        for (int i = 0; i <= itemcodelist.size() - 1; i++) {
			        	productdetail.clickMenuRefreshIcon();
			            pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"));
			            queriessubmenu.enterHsiItemNumber(itemcodelist.get(i)).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
			            log(LogType.INFO, "Navigate to global attribute tab to verify classification code");

			            //verifying classification code
			            String classificationcode = globalattribute.getClassificationCode();
			            Assertions.assertThat(classificationcode).isEqualTo(classificationcodelist.get(i));
			            log(LogType.INFO, "Verifying classification code");

			            productdetail.selectTabfromDropdown(map.get("TabName"));
			            log(LogType.INFO, "Navigate to Medical catalog to assign catalogs in userdriven");

			            //assigning catalogs
//			            for(int j = 0; j <= alluserdrivenlist.size() - 1; j++) {
//			            	catalogTypePage.clearUserDriven(alluserdrivenlist.get(j));
//			        		BasePage.WaitForMiliSec(3000);
//			                catalogTypePage.clickUserDriven().selectUserDriven(alluserdrivenlist.get(j));
//			                pimHomepage.productDetailSearchPage().clickRefreshIcon();
//			            }
			            medical_user_driven = catalogTypePage.getUserDrivenCatalogs();
			            Assertions.assertThat(medical_user_driven).containsAnyElementsOf(userdrivenlist);

			          //verify quality status
						pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
						List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
						for (String Rules:javautil.readMultipleValuesFromExcel(map.get("CatalogEntityRule")))
						{
							Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);	
						}
						qualitystatus.minimizeQualityStatusTab();
						log(LogType.INFO,"Verifying catalog entity rule");

			            //verify in master
			            productdetail.clickMenuRefreshIcon();
			            queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog")).enterHsiItemNumber(itemcodelist.get(i)).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
			            log(LogType.INFO, "Navigate to global attribute tab to verify classification code in Master Catalog");

			            //verifying classification code
			            classificationcode = globalattribute.getClassificationCode();
			            Assertions.assertThat(classificationcode).isEqualTo(classificationcodelist.get(i));
			            log(LogType.INFO, "Verifying classification code");

			            productdetail.selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
			            log(LogType.INFO, "Navigate to Medical catalog to verify catalogs in userdriven for Master Catalog");

			            List<String> masterUserdriven = catalogTypePage.getUserDrivenCatalogs();
			            Assertions.assertThat(masterUserdriven).containsAll(userdrivenlist);
						
			        }
			        pimHomepage.clickLogoutButton();
			    }

			        //CATR_129,CATR_130
			        @PimFrameworkAnnotation(module = Modules.MEDICAL_CATALOG, category = CategoryType.REGRESSION)
			        @TestDataSheet(sheetname = TestCaseSheet.DivisionalExceptionRule)
			        @Test(priority = 1, description = "CATR_129, Verification Part | CATR_129,CATR_130 | Verify whether the rule driven catalogs were removed for S10 and S30 ClassItems based on the Divsional Exceptional Rule.", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"US","pim","MEDICAL_CATALOG"})
			        public void verify_rule_driven_catalog_for_medical_division_not_showing_up_for_S10_S30_Class(Map < String, String > map){
			            PimHomepage pimHomepage = new LoginPage()
			                    .enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
			                    .enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();

			            pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
			                    .enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
			            log(LogType.INFO, "Navigate to global attribute tab to verify classification code");

			            //verifying classification code
			            String classificationcode = globalattribute.getClassificationCode();
			            Assertions.assertThat(classificationcode).isEqualTo(map.get("Classification"));
			            log(LogType.INFO, "Verifying classification code");

			            productdetail.selectTabfromDropdown(map.get("TabName"));
			            log(LogType.INFO, "Navigate to Medical catalog to assign Medical in userdriven");

			            //assigning medical catalog
//			            catalogTypePage.clearUserDriven(map.get("userDriven"));
//			    		BasePage.WaitForMiliSec(3000);
//			            catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
//			            pimHomepage.productDetailSearchPage().clickRefreshIcon();
			            medical_user_driven = catalogTypePage.getUserDrivenCatalogs();
			            Assertions.assertThat(medical_user_driven).containsAnyOf(map.get("userDriven"));

			            //verify rule driven
			            String empty_rule_driven = catalogTypePage.getEmptyRuleDriven();
			            Assertions.assertThat(empty_rule_driven).isEqualTo(map.get("RuleDriven"));

			          //verify quality status
			    		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
			    		List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
			    		for (String Rules:javautil.readMultipleValuesFromExcel(map.get("CatalogEntityRule")))
			    		{
			    			Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);	
			    		}
			    		qualitystatus.minimizeQualityStatusTab();

			            //verify in master
			            productdetail.clickMenuRefreshIcon();
			            queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
			            log(LogType.INFO, "Navigate to global attribute tab to verify classification code in Master Catalog");

			            //verifying classification code
			            classificationcode = globalattribute.getClassificationCode();
			            Assertions.assertThat(classificationcode).isEqualTo(map.get("Classification"));
			            log(LogType.INFO, "Verifying classification code");

			            productdetail.selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
			            log(LogType.INFO, "Navigate to Medical catalog to verify Medical in userdriven and empty ruledriven");

			            List<String> masterUserdriven = catalogTypePage.getUserDrivenCatalogs();
			            String masterRuledriven = catalogTypePage.getEmptyRuleDriven();
			            Assertions.assertThat(masterUserdriven).containsAnyOf(map.get("userDriven"));
			            Assertions.assertThat(masterRuledriven).isEqualTo(map.get("RuleDriven"));

			            pimHomepage.clickLogoutButton();
			        }



			        
			        
			        
			        
			        
			        

//			    	LocalAttributePage localAttributePage = new LocalAttributePage();
//			    	StructureSubMenu structuresubmenu = new StructureSubMenu();
//			    	ProductDetailSearchPage productDetailSearchPage = new ProductDetailSearchPage();
//			    	MainMenu mainmenu = new MainMenu();
//			    	QueriesSubMenu queriesSubMenu = new QueriesSubMenu();
//			    	BasePage basePage = new BasePage();
//			    	ReferencesPage referencesPage = new ReferencesPage();
//			    	CatalogTypePage catalogTypePage = new CatalogTypePage();
//			    	AllCatalogsPage allCatalogsPage = new AllCatalogsPage();
//			    	QualityStatusPage qualitystatus = new QualityStatusPage();
			    	//List<String> user_driven;
			    	//List<String> all_catalog_Dental;
			    	//List<String> all_catalog_Medical;
			    	//List<String> all_catalog_SM;
			    	//List<String> all_catalog_Zahn;
//			    	Javautils javautils = new Javautils();

			    	// CATR_147-Done

			    	@PimFrameworkAnnotation(module = Modules.SubstituteCatalogRule, category = CategoryType.REGRESSION)
			    	@TestDataSheet(sheetname = TestCaseSheet.Catalog)
			    	@Test(priority = 1, description = "CATR_147 Verification Part | CATR_147 | Verification Of Substitution Rules In Medical Divisions With Sub Type As S/SA/GE CATR_147", groups = {"US","pim","MedicalSubstituteCatalogRule"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)

			    	public void VerificationOfSubstitutionRulesInMedicalCatalogWithSubTypeAsSA(Map<String, String> map) {

			    		PimHomepage pimHomepage = new LoginPage()
			    				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
			    				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();

			    		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
			    		.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
			    		.clickOnFirstResult().selectTabfromDropdown(map.get("ReferenceTab"));
			    		String referenceType = referencesPage.getReferenceType();
			    		Assertions.assertThat(referenceType).isEqualTo(map.get("ReferenceType"));
			    		String referenceObjectNumber = referencesPage.getReferenceObjectNumber();
			    		productDetailSearchPage.selectTabfromDropdown(map.get("TabName"));	

			    		catalogTypePage.clearUserDriven(map.get("userDriven"));
			    		BasePage.WaitForMiliSec(3000);

			    		String userDrivernCatalog = map.get("userDriven");
//			    		catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
//			    		pimHomepage.productDetailSearchPage().clickRefreshIcon();
			    		List<String> user_driven = catalogTypePage.getUserDrivenCatalogs();
			    		Assertions.assertThat(user_driven).containsAnyOf(userDrivernCatalog);
			    		log(LogType.INFO, "Navigate to Medical Catalog Tab to assign Medical catalog in Parent");

			    		//verify quality status 
			    		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
			    		List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
			    		for (String Rules:javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule")))
			    		{
			    			Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);	
			    		}
			    		qualitystatus.minimizeQualityStatusTab();
			    		log(LogType.INFO,"Verifying catalog entity rule");

			    		// Verify in master catalog
			    		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
			    		.clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
			    		List<String> masterUserdriven = catalogTypePage.getUserDrivenCatalogs();
			    		Assertions.assertThat(user_driven).containsAll(masterUserdriven);
			    		log(LogType.INFO, "Verifying Catalogs in Master");

			    		// Verifying catalog in child item
			    		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType"))
			    		.removeAndEnterHsiItemNumber(referenceObjectNumber).clickSeachButton().clickOnFirstResult()
			    		.selectTabfromDropdown(map.get("AllCatalogtab"));
			    		List<String> all_catalog_Medical = allCatalogsPage.getAllMedicalCatalogList();
			    		Assertions.assertThat(all_catalog_Medical).contains(map.get("userDriven"));
			    		log(LogType.INFO, "Verifying Catalogs in Child Item");
			    	}
			    	//CATR_151--Done
			    	@PimFrameworkAnnotation(module = Modules.SubstituteCatalogRule, category = CategoryType.REGRESSION)
			    	@TestDataSheet(sheetname = TestCaseSheet.Catalog)
			    	@Test(priority = 1, description = "CATR_151 Verification Part | CATR_151 | Verification Of Substitution Rules In Medical Divisions With Sub Type As SP CATR_151",groups = {"US","pim","MedicalSubstituteCatalogRule"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
			    	public void VerificationOfSubstitutionRulesForMedicalDivisionWithSubTypeAsSP(Map<String, String> map) {

			    		PimHomepage pimHomepage = new LoginPage()
			    				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
			    				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();

			    		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
			    		.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
			    		.clickOnFirstResult().selectTabfromDropdown(map.get("ReferenceTab"));
			    		List<String> referenceType = referencesPage.getReferenceTypeInList();
			    		//Assertions.assertThat(referenceType).isEqualTo(map.get("ReferenceType"));
			    		Assertions.assertThat(referenceType).containsAnyOf((map.get("ReferenceType")));
			    		String referenceObjectNumber = referencesPage.getReferenceObjectNumber();
			    		productDetailSearchPage.selectTabfromDropdown(map.get("TabName"));
			    		catalogTypePage.clearUserDriven(map.get("userDriven"));
			    		BasePage.WaitForMiliSec(3000);

			    		String userDrivernCatalog = map.get("userDriven");
//			    		catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
//			    		pimHomepage.productDetailSearchPage().clickRefreshIcon();
			    		List<String> user_driven = catalogTypePage.getUserDrivenCatalogs();
			    		Assertions.assertThat(user_driven).containsAnyOf(userDrivernCatalog);
			    		log(LogType.INFO, "Navigate to Medical Catalog Tab to assign Medical catalog in Parent");

			    		//verify quality status 
			    		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
			    		List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
			    		for (String Rules:javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule")))
			    		{
			    			Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);	
			    		}
			    		qualitystatus.minimizeQualityStatusTab();
			    		log(LogType.INFO,"Verifying catalog entity rule");


			    		// Verify in master catalog
			    		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
			    		.clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
			    		List<String> masterUserdriven = catalogTypePage.getUserDrivenCatalogs();
			    		Assertions.assertThat(user_driven).containsAll(masterUserdriven);
			    		log(LogType.INFO, "Verifying Catalogs in Master");

			    		// Verifying in child item
			    		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType"))
			    		.removeAndEnterHsiItemNumber(referenceObjectNumber).clickSeachButton().clickOnFirstResult()
			    		.selectTabfromDropdown(map.get("AllCatalogtab"));
			    		//maxi
			    		qualitystatus.maximizeQualityStatusTab();
			    		List<String> all_catalog_Medical = allCatalogsPage.getAllMedicalCatalogList();
			    		Assertions.assertThat(all_catalog_Medical).contains(map.get("userDriven"));
			    		//mini
			    		qualitystatus.minimizeQualityStatusTab();
			    		log(LogType.INFO, "Verifying Catalogs in Child Item");


			    		//Verify Dental and Zahn Catalog should not be populated
			    		List<String> all_catalog_Dental =allCatalogsPage.getAllDentalCatalogList();
			    		Assertions.assertThat(all_catalog_Dental).doesNotContainSequence(userDrivernCatalog);
			    		List<String> all_catalog_Zahn =allCatalogsPage.getAllZahnCatalogList();
			    		Assertions.assertThat(all_catalog_Zahn).doesNotContainSequence(userDrivernCatalog);
			    		log(LogType.INFO, "Verifying Catalogs which are not populated in Child Item");
			    	}
			    	//CATR_159--Done
			    	@PimFrameworkAnnotation(module = Modules.SubstituteCatalogRule, category = CategoryType.REGRESSION)
			    	@TestDataSheet(sheetname = TestCaseSheet.Catalog)
			    	@Test(priority = 1, description = "CATR_159 Verification Part | CATR_159 | Verification Of Substitution Rules In Medical Divisions With Sub Type As PA", groups = {"US","pim","MedicalSubstituteCatalogRule"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
			    	public void VerificationOfSubstitutionRulesInMedicalDivisionsWithSubTypeAsPA(Map<String, String> map) {

			    		PimHomepage pimHomepage = new LoginPage()
			    				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
			    				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();

			    		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
			    		.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
			    		.clickOnFirstResult().selectTabfromDropdown(map.get("ReferenceTab"));
			    		String referenceType = referencesPage.getReferenceType();
			    		Assertions.assertThat(referenceType).isEqualTo(map.get("ReferenceType"));
			    		String referenceObjectNumber = referencesPage.getReferenceObjectNumber();
			    		productDetailSearchPage.selectTabfromDropdown(map.get("TabName"));

			    		catalogTypePage.clearUserDriven(map.get("userDriven"));
			    		BasePage.WaitForMiliSec(3000);


			    		String userDrivernCatalog = map.get("userDriven");
//			    		catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
//			    		pimHomepage.productDetailSearchPage().clickRefreshIcon();
			    		List<String> user_driven = catalogTypePage.getUserDrivenCatalogs();
			    		Assertions.assertThat(user_driven).containsAnyOf(userDrivernCatalog);
			    		log(LogType.INFO, "Navigate to Medical Catalog Tab to assign Medical catalog in Parent");

			    		//verify quality status 
			    		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
			    		List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
			    		for (String Rules:javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule")))
			    		{
			    			Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);	
			    		}
			    		qualitystatus.minimizeQualityStatusTab();
			    		log(LogType.INFO,"Verifying catalog entity rule");

			    		// Verify in master catalog
			    		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
			    		.clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
			    		List<String> masterUserdriven = catalogTypePage.getUserDrivenCatalogs();
			    		Assertions.assertThat(user_driven).containsAll(masterUserdriven);
			    		log(LogType.INFO, "Verifying Catalogs in Master");

			    		// Verifying Catalogs in Child item
			    		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType"))
			    		.removeAndEnterHsiItemNumber(referenceObjectNumber).clickSeachButton().clickOnFirstResult()
			    		.selectTabfromDropdown(map.get("AllCatalogtab"));
			    		List<String> all_catalog_Medical =allCatalogsPage.getAllMedicalCatalogList();
			    		Assertions.assertThat(all_catalog_Medical).doesNotContain(map.get("userDriven"));
			    	}

			    	//CATR_160--Not Working
			    	//Verify whether the Catalog list of the parent item does not gets assigned to the child Items in the respective tabs (Dental/Zahn/Special Market Web Catalog Assignment),based on the Auto Sub Flag value is "N" in the Catalog_Master dictionary
			    	@PimFrameworkAnnotation(module = Modules.SubstituteCatalogRule, category = CategoryType.REGRESSION)
			    	@TestDataSheet(sheetname = TestCaseSheet.Catalog)
			    	@Test(priority = 1, description = "CATR_160 Verification Part | CATR_160 | Verification Substitute Of Type PA For Any Division Item Does Not Populates With The Same CatalogList Of Parent Item for Medical catalog rule", groups = {"US","pim","MedicalSubstituteCatalogRule"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
			    	public void VerifySubstituteOfTypePAForAnyDivisionItemDoesNotPopulatesWithTheSameCatalogListOfParentItem(Map<String, String> map) {

			    		PimHomepage pimHomepage = new LoginPage()
			    				.enterUserName(ExcelUtils.getLoginData().get("Dental User").get("UserName"))
			    				.enterPassword(ExcelUtils.getLoginData().get("Dental User").get("Password")).clickLoginButton();

			    		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
			    		.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
			    		.clickOnFirstResult().selectTabfromDropdown(map.get("ReferenceTab"));
			    		String referenceType = referencesPage.getReferenceType();
			    		Assertions.assertThat(referenceType).isEqualTo(map.get("ReferenceType"));
			    		String referenceObjectNumber = referencesPage.getReferenceObjectNumber();
			    		productDetailSearchPage.selectTabfromDropdown(map.get("TabName"));

			    		catalogTypePage.clearUserDriven(map.get("userDriven"));
			    		BasePage.WaitForMiliSec(3000);
			    		log(LogType.INFO, "Navigate to Dental Catalog Tab to assign Dental catalog in Parent");

//			    		catalogTypePage.clearUserDriven(map.get("userDriven"));
//			    		BasePage.WaitForMiliSec(3000);
			    		String userDrivernCatalog = map.get("userDriven");
//			    		catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
//			    		pimHomepage.productDetailSearchPage().clickRefreshIcon();
			    		List<String> user_driven = catalogTypePage.getUserDrivenCatalogs();
			    		Assertions.assertThat(user_driven).containsAnyOf(userDrivernCatalog);

			    		//verify quality status 
			    		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
			    		List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
			    		for (String Rules:javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule")))
			    		{
			    			Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);	
			    		}
			    		
			    		qualitystatus.minimizeQualityStatusTab();
			    		log(LogType.INFO,"Verifying catalog entity rule");
			    		// Verify in master catalog
			    		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
			    		.clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
			    		List<String> masterUserdriven = catalogTypePage.getUserDrivenCatalogs();
			    		Assertions.assertThat(user_driven).containsAll(masterUserdriven);
			    		log(LogType.INFO, "Verifying Catalogs in Master");

			    		//Verify Dental/Zahn/Special Market Catalog should not be populated for child item
			    		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType"))
			    		.removeAndEnterHsiItemNumber(referenceObjectNumber).clickSeachButton().clickOnFirstResult()
			    		.selectTabfromDropdown(map.get("AllCatalogtab"));
			    		List<String> all_catalog_Dental = allCatalogsPage.getAllDentalCatalogList();
			    		Assert.assertNotEquals(all_catalog_Dental, userDrivernCatalog);
			    		List<String> all_catalog_Zahn = allCatalogsPage.getAllZahnCatalogList();
			    		Assert.assertNotEquals(all_catalog_Zahn, userDrivernCatalog);
			    		List<String> all_catalog_SM =allCatalogsPage.getAllSpecialMarketsCatalogList(); 
			    		Assert.assertNotEquals(all_catalog_SM, userDrivernCatalog);
			    		log(LogType.INFO, "Verifying Catalogs which are not populated in Child Item");
			    	}

			    	//CATR_161 --Done
			    	@PimFrameworkAnnotation(module = Modules.SubstituteCatalogRule, category = CategoryType.REGRESSION)
			    	@TestDataSheet(sheetname = TestCaseSheet.Catalog)
			    	@Test(priority = 1, description = "CATR_161 Verification Part | CATR_161 | Verification Of Substitution Rules In Medical Divisions With Sub Type As A3", groups = {"US","pim","MedicalSubstituteCatalogRule"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
			    	public void VerificationOfSubstitutionRulesInMedicalDivisionsWithSubTypeAsA3(Map<String, String> map) {

			    		PimHomepage pimHomepage = new LoginPage()
			    				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
			    				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();

			    		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
			    		.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
			    		.clickOnFirstResult().selectTabfromDropdown(map.get("ReferenceTab"));
			    		String referenceType = referencesPage.getReferenceType();
			    		Assertions.assertThat(referenceType).isEqualTo(map.get("ReferenceType"));
			    		String referenceObjectNumber = referencesPage.getReferenceObjectNumber();
			    		productDetailSearchPage.selectTabfromDropdown(map.get("TabName"));

			    		catalogTypePage.clearUserDriven(map.get("userDriven"));
			    		BasePage.WaitForMiliSec(3000);
			    		String userDrivernCatalog = map.get("userDriven");
//			    		catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
//			    		pimHomepage.productDetailSearchPage().clickRefreshIcon();
			    		List<String> user_driven = catalogTypePage.getUserDrivenCatalogs();
			    		Assertions.assertThat(user_driven).containsAnyOf(userDrivernCatalog);
			    		log(LogType.INFO, "Navigate to Medical Catalog Tab to assign Medical catalog in Parent");

			    		//verify quality status 
			    		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
			    		List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
			    		for (String Rules:javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule")))
			    		{
			    			Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);	
			    		}
			    		qualitystatus.minimizeQualityStatusTab();
			    		log(LogType.INFO,"Verifying catalog entity rule");

			    		// Verify in master catalog
			    		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
			    		.clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
			    		List<String> masterUserdriven = catalogTypePage.getUserDrivenCatalogs();
			    		Assertions.assertThat(user_driven).containsAll(masterUserdriven);
			    		log(LogType.INFO, "Verifying Catalogs in Master");

			    		// Verifying Catalogs in Child item
			    		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType"))
			    		.removeAndEnterHsiItemNumber(referenceObjectNumber).clickSeachButton().clickOnFirstResult()
			    		.selectTabfromDropdown(map.get("AllCatalogtab"));
			    		List<String> all_catalog_Medical =allCatalogsPage.getAllMedicalCatalogList();
			    		Assert.assertNotEquals(all_catalog_Medical, userDrivernCatalog);
			    		log(LogType.INFO, "Verifying Catalog in Child Item should assign");
			    	}


			    	
			    	
			    	
			    	
			    	
			    	
	

			    	
			    	
			    	
			    	
			    	
			    	
			    	
			    	
			    	
//			    	 ReferencesPage referencesPage = new ReferencesPage();
//			    	    ProductDetailSearchPage productdetail = new ProductDetailSearchPage();
//			    	    CatalogTypePage catalogTypePage = new CatalogTypePage();
//			    	    QualityStatusPage qualitystatus = new QualityStatusPage();
//			    	    AllCatalogsPage allCatalogsPage=new AllCatalogsPage();
//			    	    QueriesSubMenu queriessubmenu = new QueriesSubMenu();
			    	    String referenceObjectNumber;
			    	    //List<String> medical_user_driven;
			    	    //List<String> dental_user_driven;
			    	    //List<String> zahn_user_driven;
			    	    //List<String> special_market_user_driven;
			    	    //List<String> special_market_catalogs;
//			    	    Javautils javautils = new Javautils();

			    	    //CATR_195
			    	    @PimFrameworkAnnotation(module = Modules.DENTAL_CATALOG, category = CategoryType.REGRESSION)
			    	    @TestDataSheet(sheetname = TestCaseSheet.RecursiveSubstituteRule)
			    	    @Test(priority = 1, description = "CATR_195 Verification Part | CATR_195 | Verification Of Recursive Substitution Rule In Dental Divisions CATR_195", groups = {"US", "CA","pim","DENTAL_CATALOG"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
			    	    public void verificationOfRecursiveSubstitutionRuleInDentalCatalog(Map<String, String> map) { PimHomepage pimHomepage = new LoginPage()
			    	            .enterUserName(ExcelUtils.getLoginData().get("Dental User").get("UserName"))
			    	            .enterPassword(ExcelUtils.getLoginData().get("Dental User").get("Password")).clickLoginButton();

			    	        //verifying in parent item
			    	        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
			    	                .enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("ReferenceTab"));
			    	        log(LogType.INFO, "Navigate to Reference Tab to fetch child item 1");
			    	        referenceObjectNumber = referencesPage.getReferenceObjectNumberByEndDate();
			    	        log(LogType.INFO, "Navigate to Reference Tab to fetch child item 1" +" " +referenceObjectNumber);

			    	        //assigning dental catalog in parent
			    	        productdetail.selectTabfromDropdown(map.get("TabName"));
			    	        log(LogType.INFO, "Navigate to Dental Catalog Tab to assign Dental catalog in Parent");
//			    	        catalogTypePage.clearUserDriven(map.get("userDriven"));
//			    	        BasePage.WaitForMiliSec(3000);
//			    	        catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
//			    	        pimHomepage.productDetailSearchPage().clickRefreshIcon();
			    	        List<String> dental_user_driven = catalogTypePage.getUserDrivenCatalogs();
			    	        Assertions.assertThat(dental_user_driven).containsAnyOf(map.get("userDriven"));

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
			    	        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog")).enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
			    	        log(LogType.INFO, "Navigate to Dental Catalog tab to verify the Dental userDriven field in Master Catalog");
			    	        List<String> masterUserdriven = catalogTypePage.getUserDrivenCatalogs();
			    	        Assertions.assertThat(masterUserdriven).containsAnyOf(map.get("userDriven"));

			    	        //verifying in child item 1
			    	        productdetail.clickMenuRefreshIcon();
			    	        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
			    	                .enterHsiItemNumber(referenceObjectNumber).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("ReferenceTab"));
			    	        log(LogType.INFO, "Navigate to Reference Tab to fetch child item 2");
			    	        referenceObjectNumber = referencesPage.getReferenceObjectNumberByEndDate();
			    	        log(LogType.INFO, "Navigate to Reference Tab to fetch child item 2" +" " +referenceObjectNumber);

			    	        log(LogType.INFO, "Navigate to Dental catalog Tab to verify assigned user driven for parent is inherited to rules in child");
			    	        productdetail.selectTabfromDropdown(map.get("TabName"));
			    	        List<String> RuleDrivenForChild1 = catalogTypePage.getRuleDrivenCatalogs();
			    	        Assertions.assertThat(RuleDrivenForChild1).contains(map.get("userDrivenAssert"));

			    	        log(LogType.INFO, "Navigate to All Catalogs Tab to verify Dental userDriven field");
			    	        productdetail.selectTabfromDropdown(map.get("AllCatalogsTab"));
			    	        dental_user_driven = allCatalogsPage.getAllDentalCatalogList();
			    	        Assertions.assertThat(dental_user_driven).contains(map.get("userDrivenAssert"));

			    	        //verifying in child item 2
			    	        productdetail.clickMenuRefreshIcon();
			    	        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
			    	                .enterHsiItemNumber(referenceObjectNumber).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
			    	        log(LogType.INFO, "Navigate to Dental catalog Tab to verify assigned user driven for parent is inherited to rules in child");
			    	        List<String> RuleDrivenForChild2 = catalogTypePage.getRuleDrivenCatalogs();
			    	        Assertions.assertThat(RuleDrivenForChild2).contains(map.get("userDrivenAssert"));

			    	        productdetail.selectTabfromDropdown(map.get("AllCatalogsTab"));
			    	        dental_user_driven = allCatalogsPage.getAllDentalCatalogList();
			    	        Assertions.assertThat(dental_user_driven).containsAnyOf(map.get("userDrivenAssert"));

			    	        //Deleting userDriven in parent
			    	        productdetail.clickMenuRefreshIcon();
			    	        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
			    	                .enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
			    	        catalogTypePage.clearSelectedFields(map.get("userDriven"));
			    	        log(LogType.INFO,"Deleting the UserDriven");
			    	        pimHomepage.clickLogoutButton();

			    	    }

			    	    //CATR_196
			    	    @PimFrameworkAnnotation(module = Modules.MEDICAL_CATALOG, category = CategoryType.REGRESSION)
			    	    @TestDataSheet(sheetname = TestCaseSheet.RecursiveSubstituteRule)
			    	    @Test(priority = 1, description = "CATR_196 Verification Part | CATR_196 | Verification Of Recursive Substitution Rule In Medical Divisions CATR_196", groups = {"US", "CA","pim","MEDICAL_CATALOG"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
			    	    public void verificationOfRecursiveSubstitutionRuleInMedicalCatalog(Map<String, String> map) {
			    	        PimHomepage pimHomepage = new LoginPage()
			    	                .enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
			    	                .enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();

			    	        //verifying in parent item
			    	        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
			    	                .enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("ReferenceTab"));
			    	        log(LogType.INFO, "Navigate to Reference Tab to fetch child item 1");
			    	        referenceObjectNumber = referencesPage.getReferenceObjectNumberByEndDate();
			    	        log(LogType.INFO, "Navigate to Reference Tab to fetch child item 1" +" " +referenceObjectNumber);

			    	        //assigning medical catalog in parent
			    	        productdetail.selectTabfromDropdown(map.get("TabName"));
			    	        log(LogType.INFO, "Navigate to Medical Catalog Tab to assign Medical catalog in Parent");
//			    	        catalogTypePage.clearUserDriven(map.get("userDriven"));
//			    	        BasePage.WaitForMiliSec(3000);
//			    	        catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
//			    	        pimHomepage.productDetailSearchPage().clickRefreshIcon();
			    	        List<String>  MedicalUserDriven = catalogTypePage.getUserDrivenCatalogs();
			    	        Assertions.assertThat(MedicalUserDriven).containsAnyOf(map.get("userDriven"));

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
			    	        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog")).enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
			    	        log(LogType.INFO, "Navigate to Medical Catalog tab to verify the medical user driven field in Master Catalog");
			    	        List<String> MasterUserDriven = catalogTypePage.getUserDrivenCatalogs();
			    	        Assertions.assertThat(MasterUserDriven).containsAnyOf(map.get("userDriven"));

			    	        //verifying in child item 1
			    	        productdetail.clickMenuRefreshIcon();
			    	        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
			    	                .enterHsiItemNumber(referenceObjectNumber).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("ReferenceTab"));
			    	        log(LogType.INFO, "Navigate to Reference Tab to fetch child item 2");
			    	        referenceObjectNumber = referencesPage.getReferenceObjectNumberByEndDate();
			    	        log(LogType.INFO, "Navigate to Reference Tab to fetch child item 2" +" " +referenceObjectNumber);

			    	        log(LogType.INFO, "Navigate to Medical catalog Tab to verify assigned user driven for parent is inherited to rules in child");
			    	        productdetail.selectTabfromDropdown(map.get("TabName"));
			    	        List<String> RuleDrivenForChild1 = catalogTypePage.getRuleDrivenCatalogs();
			    	        Assertions.assertThat(RuleDrivenForChild1).contains(map.get("userDrivenAssert"));

			    	        log(LogType.INFO, "Navigate to All Catalogs Tab to verify Medical userDriven field");
			    	        productdetail.selectTabfromDropdown(map.get("AllCatalogsTab"));
			    	        List<String> medical_user_driven = allCatalogsPage.getAllMedicalCatalogList();
			    	        Assertions.assertThat(medical_user_driven).contains(map.get("userDrivenAssert"));

			    	        //verifying in child item 2
			    	        productdetail.clickMenuRefreshIcon();
			    	        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
			    	                .enterHsiItemNumber(referenceObjectNumber).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
			    	        log(LogType.INFO, "Navigate to Medical catalog Tab to verify assigned user driven for parent is inherited to rules in child");
			    	        List<String> RuleDrivenForChild2 = catalogTypePage.getRuleDrivenCatalogs();
			    	        Assertions.assertThat(RuleDrivenForChild2).contains(map.get("userDrivenAssert"));

			    	        log(LogType.INFO, "Navigate to All Catalogs Tab to verify Medical userDriven field");
			    	        productdetail.selectTabfromDropdown(map.get("AllCatalogsTab"));
			    	        medical_user_driven = allCatalogsPage.getAllMedicalCatalogList();
			    	        Assertions.assertThat(medical_user_driven).containsAnyOf(map.get("userDrivenAssert"));

			    	        //Deleting userDriven in parent
			    	        productdetail.clickMenuRefreshIcon();
			    	        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
			    	                .enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
			    	        catalogTypePage.clearSelectedFields(map.get("userDriven"));
			    	        log(LogType.INFO,"Deleting the UserDriven");
			    	        pimHomepage.clickLogoutButton();
			    	    }

			    	    //CATR_197 - Child items will not inherit user driven catalogs for when verifying recursive substitution rule in zahn division
			    	    @PimFrameworkAnnotation(module = Modules.ZAHN_CATALOG, category = CategoryType.Valid_Failure)
			    	    @TestDataSheet(sheetname = TestCaseSheet.RecursiveSubstituteRule)
			    	    @Test(priority = 1, description = "CATR_197 Verification Part | CATR_197 | Verification Of Recursive Substitution Rule In Zahn Division", groups = {"US", "CA","pim","ZAHN_CATALOG"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
			    	    public void verificationOfRecursiveSubstitutionRuleInZahnCatalog(Map<String, String> map) {
			    	        PimHomepage pimHomepage = new LoginPage()
			    	                .enterUserName(ExcelUtils.getLoginData().get("Zahn User").get("UserName"))
			    	                .enterPassword(ExcelUtils.getLoginData().get("Zahn User").get("Password")).clickLoginButton();

			    	        //verifying in parent item
			    	        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
			    	                .enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("ReferenceTab"));
			    	        log(LogType.INFO, "Navigate to Reference Tab to fetch child item 1");
			    	        referenceObjectNumber = referencesPage.getReferenceObjectNumberByEndDate();
			    	        log(LogType.INFO, "Navigate to Reference Tab to fetch child item 1" +" " +referenceObjectNumber);

			    	        //assigning Zahn catalog in parent
			    	        productdetail.selectTabfromDropdown(map.get("TabName"));
//			    	        catalogTypePage.clearUserDriven(map.get("userDriven"));
//			    	        BasePage.WaitForMiliSec(3000);
//			    	        log(LogType.INFO, "Navigate to Zahn Catalog Tab to assign Zahn catalog in Parent");
//			    	        catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
//			    	        pimHomepage.productDetailSearchPage().clickRefreshIcon();
			    	        List<String> zahn_user_driven = catalogTypePage.getUserDrivenCatalogs();
			    	        Assertions.assertThat(zahn_user_driven).containsAnyOf(map.get("userDriven"));

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
			    	        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog")).enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
			    	        log(LogType.INFO, "Navigate to Zahn Catalog tab to verify the zahn user driven field in Master Catalog");
			    	        List<String> masterUserdriven = catalogTypePage.getUserDrivenCatalogs();
			    	        Assertions.assertThat(masterUserdriven).containsAnyOf(map.get("userDriven"));

			    	        //verifying in child item 1
			    	        productdetail.clickMenuRefreshIcon();
			    	        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
			    	                .enterHsiItemNumber(referenceObjectNumber).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("ReferenceTab"));
			    	        log(LogType.INFO, "Navigate to Reference Tab to fetch child item 2");
			    	        referenceObjectNumber = referencesPage.getReferenceObjectNumberByEndDate();
			    	        log(LogType.INFO, "Navigate to Reference Tab to fetch child item 2" +" " +referenceObjectNumber);

			    	        log(LogType.INFO, "Navigate to Zahn catalog Tab to verify assigned user driven for parent is inherited to rules in child");
			    	        productdetail.selectTabfromDropdown(map.get("TabName"));
			    	        List<String> RuleDrivenForChild1 = catalogTypePage.getRuleDrivenCatalogs();
			    	        Assertions.assertThat(RuleDrivenForChild1).doesNotContain(map.get("userDrivenAssert"));

			    	        log(LogType.INFO, "Navigate to All Catalogs Tab to verify Zahn userDriven field");
			    	        productdetail.selectTabfromDropdown(map.get("AllCatalogsTab"));
			    	        zahn_user_driven = allCatalogsPage.getAllZahnCatalogList();
			    	        Assertions.assertThat(zahn_user_driven).doesNotContain(map.get("userDriven"));

			    	        //verifying in child item 2
			    	        productdetail.clickMenuRefreshIcon();
			    	        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
			    	                .enterHsiItemNumber(referenceObjectNumber).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
			    	        log(LogType.INFO, "Navigate to Zahn catalog Tab to verify assigned user driven for parent is inherited to rules in child");
			    	        List<String> RuleDrivenForChild2 = catalogTypePage.getRuleDrivenCatalogs();
			    	        Assertions.assertThat(RuleDrivenForChild2).doesNotContain(map.get("userDrivenAssert"));

			    	        log(LogType.INFO, "Navigate to All Catalogs Tab to verify Zahn userDriven field");
			    	        productdetail.selectTabfromDropdown(map.get("AllCatalogsTab"));
			    	        zahn_user_driven = allCatalogsPage.getAllZahnCatalogList();
			    	        Assertions.assertThat(zahn_user_driven).doesNotContain(map.get("userDriven"));

			    	        //Deleting userDriven in parent
			    	        productdetail.clickMenuRefreshIcon();
			    	        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
			    	                .enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
			    	        catalogTypePage.clearSelectedFields(map.get("userDriven"));
			    	        log(LogType.INFO,"Deleting the UserDriven");
			    	        pimHomepage.clickLogoutButton();
			    	    }

			    	    //CATR_198
			    	    @PimFrameworkAnnotation(module = Modules.SPECIAL_MARKET_CATALOG, category = CategoryType.REGRESSION)
			    	    @TestDataSheet(sheetname = TestCaseSheet.RecursiveSubstituteRule)
			    	    @Test(priority = 1, description = "CATR_198 Verification Part | CATR_198 | Verification Of Recursive Substitution Rule In SM Divisions", groups = {"US", "CA","pim","SPECIAL_MARKET_CATALOG"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
			    	    public void verificationOfRecursiveSubstitutionRuleInSMCatalog(Map<String, String> map) {
			    	        PimHomepage pimHomepage = new LoginPage()
			    	                .enterUserName(ExcelUtils.getLoginData().get("Special Markets User").get("UserName"))
			    	                .enterPassword(ExcelUtils.getLoginData().get("Special Markets User").get("Password")).clickLoginButton();

			    	        //verifying in parent item
			    	        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
			    	                .enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("ReferenceTab"));
			    	        log(LogType.INFO, "Navigate to Reference Tab to fetch child item 1");
			    	        referenceObjectNumber = referencesPage.getReferenceObjectNumberByEndDate();
			    	        log(LogType.INFO, "Navigate to Reference Tab to fetch child item 1" +" " +referenceObjectNumber);

			    	        //assigning special market catalog in parent
			    	        productdetail.selectTabfromDropdown(map.get("TabName"));
			    	        log(LogType.INFO, "Navigate to SM Catalog Tab to assign SM catalog in Parent");
//			    	        catalogTypePage.clearUserDriven(map.get("userDriven"));
//			    	        BasePage.WaitForMiliSec(3000);
//			    	        catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
//			    	        pimHomepage.productDetailSearchPage().clickRefreshIcon();
			    	        List<String> special_market_user_driven = catalogTypePage.getUserDrivenCatalogs();
			    	        Assertions.assertThat(special_market_user_driven).containsAnyOf(map.get("userDriven"));

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
			    	        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog")).enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
			    	        log(LogType.INFO, "Navigate to sm catalog tab to verify the sm user driven field in Master Catalog");
			    	        List<String> masterUserdriven = catalogTypePage.getUserDrivenCatalogs();
			    	        Assertions.assertThat(masterUserdriven).containsAnyOf(map.get("userDriven"));

			    	        //verifying in child item 1
			    	        productdetail.clickMenuRefreshIcon();
			    	        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
			    	                .enterHsiItemNumber(referenceObjectNumber).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("ReferenceTab"));
			    	        log(LogType.INFO, "Navigate to Reference Tab to fetch child item 2");
			    	        referenceObjectNumber = referencesPage.getReferenceObjectNumberByEndDate();
			    	        log(LogType.INFO, "Navigate to Reference Tab to fetch child item 2" +" " +referenceObjectNumber);

			    	        log(LogType.INFO, "Navigate to special market catalog Tab to verify assigned user driven for parent is inherited to rules in child");
			    	        productdetail.selectTabfromDropdown(map.get("TabName"));
			    	        List<String> RuleDrivenForChild1 = catalogTypePage.getRuleDrivenCatalogs();
			    	        Assertions.assertThat(RuleDrivenForChild1).contains(map.get("userDrivenAssert"));

			    	        log(LogType.INFO, "Navigate to All Catalogs Tab to verify Special market userDriven field");
			    	        productdetail.selectTabfromDropdown(map.get("AllCatalogsTab"));
			    	        special_market_user_driven = allCatalogsPage.getAllSpecialMarketsCatalogList();
			    	        Assertions.assertThat(special_market_user_driven).contains(map.get("userDrivenAssert"));

			    	        //verifying in child item 2
			    	        productdetail.clickMenuRefreshIcon();
			    	        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
			    	                .enterHsiItemNumber(referenceObjectNumber).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
			    	        log(LogType.INFO, "Navigate to special market catalog Tab to verify assigned user driven for parent is inherited to rules in child");
			    	        List<String> RuleDrivenForChild2 = catalogTypePage.getRuleDrivenCatalogs();
			    	        Assertions.assertThat(RuleDrivenForChild2).contains(map.get("userDrivenAssert"));

			    	        log(LogType.INFO, "Navigate to All Catalogs Tab to verify Special market userDriven field");
			    	        productdetail.selectTabfromDropdown(map.get("AllCatalogsTab"));
			    	        special_market_user_driven = allCatalogsPage.getAllSpecialMarketsCatalogList();
			    	        Assertions.assertThat(special_market_user_driven).contains(map.get("userDrivenAssert"));

			    	        //Deleting userDriven in parent
			    	        productdetail.clickMenuRefreshIcon();
			    	        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
			    	                .enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
			    	        catalogTypePage.clearSelectedFields(map.get("userDriven"));
			    	        log(LogType.INFO,"Deleting the UserDriven");
			    	        pimHomepage.clickLogoutButton();
			    	    }

			    	    //CATR_204
			    	    @PimFrameworkAnnotation(module = Modules.MEDICAL_CATALOG, category = CategoryType.REGRESSION)
			    	    @TestDataSheet(sheetname = TestCaseSheet.RecursiveSubstituteRule)
			    	    @Test(priority = 1, description = "CATR_204 Verification Part | CATR_204 | Verification Of Recursive Substitution Rule In Medical Divisions when child catalog exists ehrn parent catalog delete", groups = {"US", "CA","pim","MEDICAL_CATALOG"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
			    	    public void verificationOfRecursiveSubstitutionRuleWhetherChildCatalogExistAfterParentCatalogDeletion(Map<String, String> map) {
			    	        PimHomepage pimHomepage = new LoginPage()
			    	                .enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
			    	                .enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();

			    	        //verifying in parent item
			    	        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
			    	                .enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("ReferenceTab"));
			    	        log(LogType.INFO, "Navigate to Reference Tab to fetch child item");
			    	        referenceObjectNumber = referencesPage.getReferenceObjectNumberByEndDate();

			    	        //assigning medical catalog in parent
			    	        productdetail.selectTabfromDropdown(map.get("TabName"));
			    	        log(LogType.INFO, "Navigate to Medical Catalog Tab to assign medical catalog in Parent");
//			    	        catalogTypePage.clearUserDriven(map.get("userDriven"));
//			    	        BasePage.WaitForMiliSec(3000);
//			    	        catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
//			    	        pimHomepage.productDetailSearchPage().clickRefreshIcon();
			    	        List<String> medical_user_driven = catalogTypePage.getUserDrivenCatalogs();
			    	        Assertions.assertThat(medical_user_driven).containsAnyOf(map.get("userDriven"));

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
			    	        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog")).enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
			    	        log(LogType.INFO, "Navigate to medical catalog tab to verify the medical userdriven field in Master Catalog");
			    	        List<String> masterUserdriven = catalogTypePage.getUserDrivenCatalogs();
			    	        Assertions.assertThat(masterUserdriven).containsAnyOf(map.get("userDriven"));

			    	        //verifying in child item
			    	        productdetail.clickMenuRefreshIcon();
			    	        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
			    	                .enterHsiItemNumber(referenceObjectNumber).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("AllCatalogsTab"));
			    	        log(LogType.INFO, "Navigate to All catalogs tab");

			    	        medical_user_driven = allCatalogsPage.getAllMedicalCatalogList();
			    	        Assertions.assertThat(medical_user_driven).containsAnyOf(map.get("userDriven"));

			    	        //Deletion of user driven in parent
			    	        productdetail.clickMenuRefreshIcon();
			    	        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
			    	                .enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
			    	        catalogTypePage.clearSelectedFields(map.get("userDriven"));
			    	        pimHomepage.productDetailSearchPage().clickRefreshIcon();
			    	        medical_user_driven = catalogTypePage.getUserDrivenCatalogs();
			    	        Assertions.assertThat(medical_user_driven).doesNotContain(map.get("userDriven"));
			    	        log(LogType.INFO,"Deleting the Userdriven");

			    	        //verifying in child item whether it still exists
			    	        productdetail.clickMenuRefreshIcon();
			    	        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
			    	                .enterHsiItemNumber(referenceObjectNumber).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("AllCatalogsTab"));
			    	        log(LogType.INFO, "Navigate to All catalogs tab to see whether deleted catalog still exist after deletion in parent.");

			    	        medical_user_driven = allCatalogsPage.getAllMedicalCatalogList();
			    	        Assertions.assertThat(medical_user_driven).containsAnyOf(map.get("userDriven"));
			    	        pimHomepage.clickLogoutButton();
			    	    }
			    	    
			    	    
			    	    
			    	    
			    	    
			    	    
			    	    
			    	    
			    	    
			    	    
			    	    
			    	  
			    	    
			    	    //By Shubham
			    	    
			    	    
			    	    
			    	    
			    	    
			    	    

//			    	    	CatalogTypePage catalogTypePage = new CatalogTypePage();
//			    	    	AllCatalogsPage allCatalogsPage = new AllCatalogsPage();
//			    	    	QualityStatusPage qualitystatus = new QualityStatusPage();
//			    	    	ProductDetailSearchPage productdetailspage = new ProductDetailSearchPage();
//			    	    	QueriesSubMenu queriesSubMenu = new QueriesSubMenu();
//			    	    	PimHomepage pimHomepage = new PimHomepage();
//			    	    	LoginPage loginPage = new LoginPage();
//			    	    	LoginPage loginpage = new LoginPage();
//			    	    	Javautils javautils = new Javautils();
//			    	    	//List<String> rule_driven;
//			    	    	List<String> user_driven;
			    	    	List<String> all_medical_catalog;
//			    	    	List<String> user_and_rule_driven = new ArrayList<>();
//			    	    	
//			    	    	LocalAttributePage localAttributePage = new LocalAttributePage();
//			    	    	StructureSubMenu structuresubmenu=new StructureSubMenu();
//			    	    	ProductDetailSearchPage productDetailSearchPage = new ProductDetailSearchPage();                     
//			    	    	MainMenu mainmenu=new MainMenu();
//			    	    	BasePage basePage = new BasePage();;
//			    	    	ReferencesPage referencesPage=new  ReferencesPage();
//			    	    	
//			    	    	ProductDetailSearchPage productdetail = new ProductDetailSearchPage();
//			    	        QueriesSubMenu queriessubmenu = new QueriesSubMenu();
//			    	        GlobalAttributePage globalattribute = new GlobalAttributePage();
			    	        List<String> special_market_user_driven;
			    	        List<String> special_market_rule_driven;
			    	        List<String> special_market_catalog_list;
			    	        List<String> dental_user_driven;
			    	        List<String> dental_rule_driven;
			    	        
//			    	        List<String> medical_user_driven;
			    	        List<String> special_market_catalogs;
			    	        
			    	        ClassificationsPage classificationPage = new ClassificationsPage();
			    	        CatalogAssignmentPage CatalogAssignmentPage = new CatalogAssignmentPage();
			    	        
			    	    	List<String> all_catalog_Dental;
			    	    	List<String> all_catalog_Medical;
			    	    	List<String> all_catalog_SM;
			    	    	List<String> all_catalog_Zahn;
			    	    	

			    	    	//MedicalCatalogTest--------------------------------------------------------
			    	    	
			    	    	// CATR_082,CATR_083,CATR_084,CATR_085,CATR_086,CATR_087
			    	    	@PimFrameworkAnnotation(module = Modules.MEDICAL_CATALOG, category = CategoryType.REGRESSION)
			    	    	@TestDataSheet(sheetname = TestCaseSheet.Catalog)
			    	    	@Test(priority = 1, description = "CATR_082, Verification Part | CATR_082,CATR_083,CATR_084,CATR_085,CATR_086,CATR_087 | Verifying Rule Driven catalog for Medical Division in US, CA and Master Catalog", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			    	    			"US", "pim","MEDICAL_CATALOG" })
			    	    	public void verify_rule_driven_for_medical_division(Map<String, String> map)
			    	    			throws InterruptedException, IOException {

			    	    		PimHomepage pimHomepage = new LoginPage()
			    	    				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
			    	    				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
			    	    		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
			    	    				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
			    	    				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));

//			    	    		List<String> userdrivencatalogs = catalogTypePage.getUserDrivenCatalogs();
//			    	    		List<String> multiplevaluesfromexcel = javautils.readMultipleValuesFromExcel(map.get("userDriven"));
//			    	    		for (String userdriven : multiplevaluesfromexcel) {
//			    	    			if (userdrivencatalogs.contains(userdriven)) {
//			    	    				// delete userdriven
//			    	    				catalogTypePage.clearSelectedFields(userdriven);
//			    	    				productdetailspage.clickRefreshIcon();
//			    	    				log(LogType.INFO, "Deleting the Userdriven");
//			    	    			}
//			    	    		}
//			    	    		BasePage.WaitForMiliSec(3000);
//			    	    		catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
			    	    		pimHomepage.productDetailSearchPage().clickRefreshIcon();
			    	    		List<String> rule_driven = catalogTypePage.getRuleDrivenCatalogs();
			    	    		Assertions.assertThat(rule_driven).containsAnyOf(map.get("RuleDriven"));

			    	    		// verify quality status
			    	    		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
			    	    		List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus"))
			    	    				.maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
			    	    		for (String Rules : javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule"))) {
			    	    			Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);
			    	    		}
			    	    		qualitystatus.minimizeQualityStatusTab();
			    	    		log(LogType.INFO, "Verifying catalog entity rule");

			    	    		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
			    	    				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
			    	    		List<String> masterRuledriven = catalogTypePage.getRuleDrivenCatalogs();
			    	    		Assertions.assertThat(rule_driven).containsAll(masterRuledriven);
			    	    		pimHomepage.clickLogoutButton();
			    	    	}

			    	    	// CATR_083
			    	    	@PimFrameworkAnnotation(module = Modules.MEDICAL_CATALOG, category = CategoryType.REGRESSION)
			    	    	@TestDataSheet(sheetname = TestCaseSheet.Catalog)
			    	    	@Test(priority = 1, description = "CATR_083 Verification Part | CATR_083 | Verifying Rule Driven catalog for Medical Division in US, CA and Master Catalog", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			    	    			"CA", "pim","MEDICAL_CATALOG" })
			    	    	public void verify_rule_driven_for_medical_division_CA(Map<String, String> map)
			    	    			throws InterruptedException, IOException {

			    	    		PimHomepage pimHomepage = new LoginPage()
			    	    				.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
			    	    				.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
			    	    		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
			    	    				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
			    	    				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));

//			    	    		List<String> userdrivencatalogs = catalogTypePage.getUserDrivenCatalogs();
//			    	    		List<String> multiplevaluesfromexcel = javautils.readMultipleValuesFromExcel(map.get("userDriven"));
//			    	    		for (String userdriven : multiplevaluesfromexcel) {
//			    	    			if (userdrivencatalogs.contains(userdriven)) {
//			    	    				// delete userdriven
//			    	    				catalogTypePage.clearSelectedFields(userdriven);
//			    	    				productdetailspage.clickRefreshIcon();
//			    	    				log(LogType.INFO, "Deleting the Userdriven");
//			    	    			}
//			    	    		}
//			    	    		BasePage.WaitForMiliSec(3000);
//			    	    		catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
			    	    		pimHomepage.productDetailSearchPage().clickRefreshIcon();
			    	    		BasePage.WaitForMiliSec(60000);
			    	    		pimHomepage.productDetailSearchPage().clickRefreshIcon();
			    	    		BasePage.WaitForMiliSec(30000);
			    	    		pimHomepage.productDetailSearchPage().clickRefreshIcon();
			    	    		BasePage.WaitForMiliSec(300000);
			    	    		pimHomepage.productDetailSearchPage().clickRefreshIcon();
			    	    		List<String> rule_driven = catalogTypePage.getRuleDrivenCatalogs();
			    	    		Assertions.assertThat(rule_driven).containsAnyOf(map.get("RuleDriven"));

			    	    		// verify quality status
			    	    		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
			    	    		List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus"))
			    	    				.maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
			    	    		for (String Rules : javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule"))) {
			    	    			Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);
			    	    		}
			    	    		qualitystatus.minimizeQualityStatusTab();
			    	    		log(LogType.INFO, "Verifying catalog entity rule");

			    	    		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
			    	    				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
			    	    		List<String> masterRuledriven = catalogTypePage.getRuleDrivenCatalogs();
			    	    		Assertions.assertThat(rule_driven).containsAll(masterRuledriven);
			    	    		pimHomepage.clickLogoutButton();
			    	    	}

			    	    	// CATR_181 and CATR_182, CATR_133
			    	    	@PimFrameworkAnnotation(module = Modules.MEDICAL_CATALOG, category = CategoryType.REGRESSION)
			    	    	@TestDataSheet(sheetname = TestCaseSheet.Catalog)
			    	    	@Test(priority = 1, description = "CATR_181 Verification Part | CATR_181 and CATR_182, CATR_133 | Verify All catalog tabs and exception lists for Medical Catalog list populates the catalogs with User, Rule Driven and excluding the Catalogs selected as Exception for US", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			    	    			"US", "pim","MEDICAL_CATALOG" })
			    	    	public void verify_all_catalog_tabs_and_exception_list_for_medical_division(Map<String, String> map) {
			    	    		PimHomepage pimHomepage = new LoginPage()
			    	    				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
			    	    				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
			    	    		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
			    	    				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
			    	    				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));

			    	    		user_driven = catalogTypePage.getUserDrivenCatalogs();
//			    	    		List<String> rule_driven = catalogTypePage.getRuleDrivenCatalogs();

//			    	    		user_and_rule_driven.addAll(rule_driven);
//			    	    		user_and_rule_driven.addAll(user_driven); // took user and rule driven
			    	    		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("AllCatalogtab")).clickRefreshIcon();
			    	    		all_medical_catalog = allCatalogsPage.getAllMedicalCatalogList();
			    	    		Assertions.assertThat(user_and_rule_driven).containsAll(all_medical_catalog);// verified all catalog without
			    	    																						// exception

			    	    		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
			    	    				.clickOnFirstResult().selectTabfromDropdown(map.get("AllCatalogtab")).clickRefreshIcon();
			    	    		List<String> master_all_medical_catalog = allCatalogsPage.getAllMedicalCatalogList();
			    	    		// verified all catalog tab in master
			    	    		Assertions.assertThat(user_and_rule_driven).containsAll(master_all_medical_catalog);

			    	    		// verifying with exception list
			    	    		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType")).clickSeachButton()
			    	    				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
//			    	    		List<String> userdrivencatalogs = catalogTypePage.getExceptionListCatalogs();
//			    	    		List<String> multiplevaluesfromexcel = javautils.readMultipleValuesFromExcel(map.get("userDriven"));
//			    	    		for (String userdriven : multiplevaluesfromexcel) {
//			    	    			if (userdrivencatalogs.contains(userdriven)) {
//			    	    				// delete userdriven
//			    	    				catalogTypePage.clearExceptionListField(userdriven);
//			    	    				productdetailspage.clickRefreshIcon();
//			    	    				log(LogType.INFO, "Deleting the exception list");
//			    	    			}
//			    	    		}
//			    	    		BasePage.WaitForMiliSec(3000);
//			    	    		catalogTypePage.clickExceptionListField().selectExceptionCatalogsFromList(map.get("userDriven"));
			    	    		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("AllCatalogtab")).clickRefreshIcon();

			    	    		List<String> all_catalog_except_exceptionallist = allCatalogsPage.getAllMedicalCatalogList();
			    	    		Assertions.assertThat(all_catalog_except_exceptionallist).isNotEqualTo(user_and_rule_driven);

			    	    		// verifying with exception list in master catalog
			    	    		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
			    	    				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon()
			    	    				.selectTabfromDropdown(map.get("AllCatalogtab"));
			    	    		List<String> master_exception_allmedical_catalog = allCatalogsPage.getAllMedicalCatalogList();

			    	    		Assertions.assertThat(user_and_rule_driven).isNotEqualTo(master_exception_allmedical_catalog);
			    	    		Assertions.assertThat(all_catalog_except_exceptionallist).containsAll(master_exception_allmedical_catalog);
			    	    		pimHomepage.clickLogoutButton();
			    	    	}

			    	    	// CATR_181 and CATR_182
			    	    	@PimFrameworkAnnotation(module = Modules.MEDICAL_CATALOG, category = CategoryType.REGRESSION)
			    	    	@TestDataSheet(sheetname = TestCaseSheet.Catalog)
			    	    	@Test(priority = 1, description = "CATR_181 Verification Part | CATR_181 and CATR_182 | Verify All catalog tabs and exception lists for Medical Catalog list populates the catalogs with User, Rule Driven and excluding the Catalogs selected as Exception for CA", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			    	    			"CA", "pim","MEDICAL_CATALOG" })
			    	    	public void verify_all_catalog_tabs_and_exception_list_for_medical_division_CA(Map<String, String> map) {
			    	    		PimHomepage pimHomepage = new LoginPage()
			    	    				.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
			    	    				.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
			    	    		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
			    	    				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
			    	    				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));

			    	    		user_driven = catalogTypePage.getUserDrivenCatalogs();
//			    	    		List<String> rule_driven = catalogTypePage.getRuleDrivenCatalogs();

//			    	    		user_and_rule_driven.addAll(rule_driven);
//			    	    		user_and_rule_driven.addAll(user_driven); // took user and rule driven
			    	    		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("AllCatalogtab")).clickRefreshIcon();
			    	    		all_medical_catalog = allCatalogsPage.getAllMedicalCatalogListForCA();
			    	    		Assertions.assertThat(user_and_rule_driven).containsAll(all_medical_catalog);// verified all catalog without
			    	    																						// exception

			    	    		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
			    	    				.clickOnFirstResult().selectTabfromDropdown(map.get("AllCatalogtab")).clickRefreshIcon();
			    	    		List<String> master_all_medical_catalog = allCatalogsPage.getAllMedicalCatalogList();
			    	    		// verified all catalog tab in master
			    	    		Assertions.assertThat(user_and_rule_driven).containsAll(master_all_medical_catalog);

			    	    		// verifying with exception list
			    	    		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType")).clickSeachButton()
			    	    				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();

			    	    		List<String> userdrivencatalogs = catalogTypePage.getExceptionListCatalogs();
//			    	    		List<String> multiplevaluesfromexcel = javautils.readMultipleValuesFromExcel(map.get("userDriven"));
//			    	    		for (String userdriven : multiplevaluesfromexcel) {
//			    	    			if (userdrivencatalogs.contains(userdriven)) {
//			    	    				// delete userdriven
//			    	    				catalogTypePage.clearExceptionListField(userdriven);
//			    	    				productdetailspage.clickRefreshIcon();
//			    	    				log(LogType.INFO, "Deleting the Userdriven");
//			    	    			}
//			    	    		}
//			    	    		BasePage.WaitForMiliSec(3000);
//			    	    		catalogTypePage.clickExceptionListField().selectExceptionCatalogsFromList(map.get("userDriven"));
			    	    		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("AllCatalogtab")).clickRefreshIcon();

			    	    		List<String> all_catalog_except_exceptionallist = allCatalogsPage.getAllMedicalCatalogList();
			    	    		Assertions.assertThat(all_catalog_except_exceptionallist).isNotEqualTo(user_and_rule_driven);

			    	    		// verifying with exception list in master catalog
			    	    		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
			    	    				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon()
			    	    				.selectTabfromDropdown(map.get("AllCatalogtab"));
			    	    		List<String> master_exception_alldental__catalog = allCatalogsPage.getAllMedicalCatalogList();

			    	    		Assertions.assertThat(user_and_rule_driven).isNotEqualTo(master_exception_alldental__catalog);
			    	    		Assertions.assertThat(all_catalog_except_exceptionallist).containsAll(master_exception_alldental__catalog);
			    	    		pimHomepage.clickLogoutButton();
			    	    	}

			    	    	// CATR_018,CATR_019,CATR_020,CATR_021,CATR_022,CATR_023,CATR_024,CATR_025,CATR_026,CATR_027, CATR_030,CATR_034

			    	    	@PimFrameworkAnnotation(module = Modules.MEDICAL_CATALOG, category = CategoryType.REGRESSION)
			    	    	@TestDataSheet(sheetname = TestCaseSheet.UI_Validation_Catalogs_Field)
			    	    	@Test(priority = 1, description = "CATR_018, Verification Part | CATR_018,CATR_019,CATR_020,CATR_021,CATR_022,CATR_023,CATR_024,CATR_025,CATR_026,CATR_027, CATR_030,CATR_034 | Medical Catalog Verifying rule driven, userdriven, exception list, publish date, publish flag are visible and editable In US", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			    	    			"US", "pim", "SANITY","MEDICAL_CATALOG" })
			    	    	public void UI_validation_Medical_Catalog_In_US(Map<String, String> map) {
			    	    		PimHomepage pimHomepage = new LoginPage()
			    	    				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
			    	    				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
			    	    		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
			    	    				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
			    	    				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));

			    	    		// Validating on "Medical catalog"
			    	    		Assertions.assertThat(catalogTypePage.isCatalogFieldVisible("tabMedicalCatalog")).isTrue();
			    	    		Assertions.assertThat(catalogTypePage.isCatalogFieldVisible("RuleDrivenField")).isTrue();
			    	    		Assertions.assertThat(catalogTypePage.isCatalogFieldVisible("userDrivenField")).isTrue();
			    	    		Assertions.assertThat(catalogTypePage.isCatalogFieldVisible("exceptionListField")).isTrue();
			    	    		Assertions.assertThat(catalogTypePage.isCatalogFieldVisible("publishFlagField")).isTrue();
			    	    		Assertions.assertThat(catalogTypePage.isCatalogFieldVisible("publishDateField")).isTrue();

			    	    		Assertions.assertThat(catalogTypePage.isCatalogFieldEditable("RuleDrivenField", "FieldDropDown")).isFalse();
			    	    		Assertions.assertThat(catalogTypePage.isCatalogFieldEditable("userDrivenField", "FieldDropDown")).isTrue();
			    	    		Assertions.assertThat(catalogTypePage.isCatalogFieldEditable("exceptionListField", "FieldDropDown")).isTrue();
			    	    		Assertions.assertThat(catalogTypePage.isCatalogFieldEditable("publishFlagField", "FieldDropDown")).isTrue();
			    	    		Assertions.assertThat(catalogTypePage.isCatalogFieldEditable("publishDateField", "PublishDateFieldDropDown"))
			    	    				.isTrue();
			    	    		catalogTypePage.verifySelectUserdrivenDropdown().verifySelectExceptionListDropdown()
			    	    				.verifySelectPublishFlagFieldDropdown().verifySelectPublishDateFieldDropdown();

			    	    		// Validating on "All catalog Tab" for dental user ProductDetailSearchPage

			    	    		productdetailspage.selectTabfromDropdown(map.get("ContinueTabName"));

			    	    		// AllCatalogsPage allCatalogsPage = new AllCatalogsPage();

			    	    		Assertions.assertThat(allCatalogsPage.isCatalogFieldVisible("tabAllCatalogs")).isTrue();

			    	    		Assertions.assertThat(allCatalogsPage.isCatalogFieldVisible("fieldDentalCatalogListUI")).isTrue();
			    	    		Assertions.assertThat(allCatalogsPage.isCatalogFieldVisible("fieldMedicalCatalogListUI")).isTrue();
			    	    		Assertions.assertThat(allCatalogsPage.isCatalogFieldVisible("fieldZahnCatalogListUI")).isTrue();
			    	    		Assertions.assertThat(allCatalogsPage.isCatalogFieldVisible("fieldSpecialMarketsCatalogListUI")).isTrue();

			    	    		Assertions.assertThat(allCatalogsPage.isCatalogFieldEditable("fieldDentalCatalogListUI", "FieldDropDown"))
			    	    				.isFalse();
			    	    		Assertions.assertThat(allCatalogsPage.isCatalogFieldEditable("fieldMedicalCatalogListUI", "FieldDropDown"))
			    	    				.isFalse();
			    	    		Assertions.assertThat(allCatalogsPage.isCatalogFieldEditable("fieldZahnCatalogListUI", "FieldDropDown"))
			    	    				.isFalse();
			    	    		Assertions
			    	    				.assertThat(allCatalogsPage.isCatalogFieldEditable("fieldSpecialMarketsCatalogListUI", "FieldDropDown"))
			    	    				.isFalse();

			    	    		// Validating in master catalog for dental user for "dental catalog tab"

			    	    		queriesSubMenu.selectCatalogType(map.get("ContinueCatalogType")).clickSeachButton().clickOnFirstResult()
			    	    				.selectTabfromDropdown(map.get("TabName"));

			    	    		Assertions.assertThat(catalogTypePage.isCatalogFieldEditable("RuleDrivenField", "FieldDropDown")).isFalse();
			    	    		Assertions.assertThat(catalogTypePage.isCatalogFieldEditable("userDrivenField", "FieldDropDown")).isFalse();
			    	    		Assertions.assertThat(catalogTypePage.isCatalogFieldEditable("exceptionListField", "FieldDropDown")).isFalse();
			    	    		Assertions.assertThat(catalogTypePage.isCatalogFieldEditable("publishFlagField", "FieldDropDown")).isFalse();
			    	    		Assertions.assertThat(catalogTypePage.isCatalogFieldEditable("publishDateField", "PublishDateFieldDropDown"))
			    	    				.isFalse();

			    	    		// Validating in master catalog for dental user for "All catalog tab"
			    	    		productdetailspage.selectTabfromDropdown(map.get("ContinueTabName"));

			    	    		Assertions.assertThat(allCatalogsPage.isCatalogFieldEditable("fieldDentalCatalogListUI", "FieldDropDown"))
			    	    				.isFalse();
			    	    		Assertions.assertThat(allCatalogsPage.isCatalogFieldEditable("fieldMedicalCatalogListUI", "FieldDropDown"))
			    	    				.isFalse();
			    	    		Assertions.assertThat(allCatalogsPage.isCatalogFieldEditable("fieldZahnCatalogListUI", "FieldDropDown"))
			    	    				.isFalse();
			    	    		Assertions
			    	    				.assertThat(allCatalogsPage.isCatalogFieldEditable("fieldSpecialMarketsCatalogListUI", "FieldDropDown"))
			    	    				.isFalse();
			    	    		pimHomepage.clickLogoutButton();
			    	    	}

			    	    	// CATR_018 - CATR_027, CATR_030,CATR_034

			    	    	@PimFrameworkAnnotation(module = Modules.MEDICAL_CATALOG, category = CategoryType.REGRESSION)
			    	    	@TestDataSheet(sheetname = TestCaseSheet.UI_Validation_Catalogs_Field)
			    	    	@Test(priority = 1, description = "CATR_018 Verification Part | CATR_018 - CATR_027, CATR_030,CATR_034 | Verifying Medical rule driven, userdriven, exception list, publish date, publish flag are visible and editable in CA", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			    	    			"CA", "pim", "SANITY","MEDICAL_CATALOG" })
			    	    	public void UI_validation_Medical_Catalog_In_CA(Map<String, String> map) {
			    	    		loginPage.enterUserName(map.get("Username")).enterPassword(map.get("Password")).clickLoginButton();
			    	    		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
			    	    				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
			    	    				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));

			    	    		// Validating on "Dental catalog"
			    	    		Assertions.assertThat(catalogTypePage.isCatalogFieldVisible("tabCanadianDentalCatalogs")).isTrue();

			    	    		Assertions.assertThat(catalogTypePage.isCatalogFieldVisible("RuleDrivenField")).isTrue();
			    	    		Assertions.assertThat(catalogTypePage.isCatalogFieldVisible("userDrivenField")).isTrue();
			    	    		Assertions.assertThat(catalogTypePage.isCatalogFieldVisible("exceptionListField")).isTrue();
			    	    		Assertions.assertThat(catalogTypePage.isCatalogFieldVisible("publishFlagField")).isTrue();
			    	    		Assertions.assertThat(catalogTypePage.isCatalogFieldVisible("publishDateField")).isTrue();

			    	    		Assertions.assertThat(catalogTypePage.isCatalogFieldEditable("RuleDrivenField", "FieldDropDown")).isFalse();
			    	    		Assertions.assertThat(catalogTypePage.isCatalogFieldEditable("userDrivenField", "FieldDropDown")).isTrue();
			    	    		Assertions.assertThat(catalogTypePage.isCatalogFieldEditable("exceptionListField", "FieldDropDown")).isTrue();
			    	    		Assertions.assertThat(catalogTypePage.isCatalogFieldEditable("publishFlagField", "FieldDropDown")).isTrue();
			    	    		Assertions.assertThat(catalogTypePage.isCatalogFieldEditable("publishDateField", "PublishDateFieldDropDown"))
			    	    				.isTrue();
			    	    		catalogTypePage.verifySelectUserdrivenDropdown().verifySelectExceptionListDropdown()
			    	    				.verifySelectPublishFlagFieldDropdown().verifySelectPublishDateFieldDropdown()
			    	    				.removingTheExceptionField();
			    	    		productdetailspage.clickRefreshIcon();

			    	    		// Validating on "All catalog Tab" for dental user ProductDetailSearchPage

			    	    		productdetailspage.selectTabfromDropdown(map.get("ContinueTabNameForCA"));

			    	    		// AllCatalogsPage allCatalogsPage = new AllCatalogsPage();

			    	    		Assertions.assertThat(allCatalogsPage.isCatalogFieldVisible("fieldDentalCatalogListUI")).isTrue();
			    	    		Assertions.assertThat(allCatalogsPage.isCatalogFieldVisible("fieldMedicalCatalogListUI")).isTrue();
			    	    		Assertions.assertThat(allCatalogsPage.isCatalogFieldVisible("fieldZahnCatalogListUI")).isTrue();

			    	    		//Assertions.assertThat(allCatalogsPage.isCatalogFieldVisible("fieldSpecialMarketsCatalogListUI")).isTrue();

			    	    		Assertions.assertThat(allCatalogsPage.isCatalogFieldEditable("fieldDentalCatalogListUI", "FieldDropDown"))
			    	    				.isFalse();
			    	    		Assertions.assertThat(allCatalogsPage.isCatalogFieldEditable("fieldMedicalCatalogListUI", "FieldDropDown"))
			    	    				.isFalse();
			    	    		Assertions.assertThat(allCatalogsPage.isCatalogFieldEditable("fieldZahnCatalogListUI", "FieldDropDown"))
			    	    				.isFalse();
			    	    		//Assertions.assertThat(allCatalogsPage.isCatalogFieldEditable("fieldSpecialMarketsCatalogListUI", "FieldDropDown")).isFalse();

			    	    		// Validating in master catalog for dental user for "dental catalog tab"

			    	    		queriesSubMenu.selectCatalogType(map.get("ContinueCatalogType")).clickSeachButton().clickOnSecondResult()
			    	    				.selectTabfromDropdown(map.get("TabName"));

			    	    		Assertions.assertThat(catalogTypePage.isCatalogFieldEditable("RuleDrivenField", "FieldDropDown")).isFalse();
			    	    		Assertions.assertThat(catalogTypePage.isCatalogFieldEditable("userDrivenField", "FieldDropDown")).isFalse();
			    	    		Assertions.assertThat(catalogTypePage.isCatalogFieldEditable("exceptionListField", "FieldDropDown")).isFalse();
			    	    		Assertions.assertThat(catalogTypePage.isCatalogFieldEditable("publishFlagField", "FieldDropDown")).isFalse();
			    	    		Assertions.assertThat(catalogTypePage.isCatalogFieldEditable("publishDateField", "PublishDateFieldDropDown"))
			    	    				.isFalse();

			    	    		// Validating in master catalog for dental user for "All catalog tab"
			    	    		productdetailspage.selectTabfromDropdown(map.get("ContinueTabNameForCA"));

			    	    		Assertions.assertThat(allCatalogsPage.isCatalogFieldEditable("fieldDentalCatalogListUI", "FieldDropDown"))
			    	    				.isFalse();
			    	    		Assertions.assertThat(allCatalogsPage.isCatalogFieldEditable("fieldMedicalCatalogListUI", "FieldDropDown"))
			    	    				.isFalse();
			    	    		Assertions.assertThat(allCatalogsPage.isCatalogFieldEditable("fieldZahnCatalogListUI", "FieldDropDown"))
			    	    				.isFalse();

			    	    		//Assertions.assertThat(allCatalogsPage.isCatalogFieldEditable("fieldSpecialMarketsCatalogListUI", "FieldDropDown")).isFalse();
			    	    		pimHomepage.clickLogoutButton();
			    	    	}

			    	    	// Verify Set of Catalog Entity Rules triggered if we update the Publish date
			    	    	// and Flag in Catalog Assignment TCID - WRFL_011 and - 012
			    	    	@PimFrameworkAnnotation(module = Modules.MEDICAL_CATALOG, category = CategoryType.SMOKE)
			    	    	@TestDataSheet(sheetname = TestCaseSheet.Catalog)
			    	    	@Test(priority = 1, description = "WRFL_011 Verification Part | WRFL_011 and - 012 | Verify Set of Catalog Entity Rules triggered if we update the Publish date and Flag in Catalog Assignment for US", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			    	    			"SMOKE","US", "pim","MEDICAL_CATALOG" })
			    	    	public void verify_quality_status_when_change_publish_date_and_flag_for_US_medical_division(
			    	    			Map<String, String> map) {
			    	    		PimHomepage pimHomepage = new LoginPage()
			    	    				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
			    	    				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
			    	    		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
			    	    				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
			    	    				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
			    	    		log(LogType.INFO, "Navigate to Dental Catalog tab from Queries Menu for US Catalog");
			    	    		BasePage.WaitForMiliSec(3000);
//			    	    		catalogTypePage.selectPublishDate(DateandTimeUtils.getTodaysDate()).selectPublishFlag();
			    	    		log(LogType.INFO, "Change publish date to current date and publish flag");
			    	    		String publishdate = catalogTypePage.getPublishDate();
			    	    		String publishflag = catalogTypePage.getPublishFlag();

			    	    		// verify quality status
			    	    		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
			    	    		List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus"))
			    	    				.maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
			    	    		for (String Rules : javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule"))) {
			    	    			Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);
			    	    		}
			    	    		qualitystatus.minimizeQualityStatusTab();
			    	    		log(LogType.INFO, "Verifying catalog entity rule");

			    	    		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
			    	    				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon().expandTab();
			    	    		log(LogType.INFO, "Navigate to Master Catalog");
			    	    		Assertions.assertThat(publishdate).isEqualTo(catalogTypePage.getPublishDate());
			    	    		Assertions.assertThat(publishflag).isEqualTo(catalogTypePage.getPublishFlag());

			    	    		pimHomepage.clickLogoutButton();
			    	    	}

			    	    	// Verify Set of Catalog Entity Rules triggered if we update the Publish date
			    	    	// and Flag in Catalog Assignment TCID - WRFL_011 and - 012
			    	    	@PimFrameworkAnnotation(module = Modules.MEDICAL_CATALOG, category = CategoryType.SMOKE)
			    	    	@TestDataSheet(sheetname = TestCaseSheet.Catalog)
			    	    	@Test(priority = 1, description = "WRFL_011 Verification Part | WRFL_011 and 012 | Verify Set of Catalog Entity Rules triggered if we update the Publish date and Flag in Catalog Assignment for CA", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			    	    			"SMOKE","CA", "pim","MEDICAL_CATALOG" })
			    	    	public void verify_quality_status_when_change_publish_date_and_flag_for_CA_medical_division(
			    	    			Map<String, String> map) {
			    	    		PimHomepage pimHomepage = new LoginPage()
			    	    				.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
			    	    				.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
			    	    		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
			    	    				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
			    	    				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
			    	    		log(LogType.INFO, "Navigate to Medical Catalog tab from Queries Menu for CA Catalog");
			    	    		BasePage.WaitForMiliSec(3000);
//			    	    		catalogTypePage.selectPublishDate(DateandTimeUtils.getTodaysDate()).selectPublishFlag();
			    	    		log(LogType.INFO, "Change publish date to current date and publish flag");
			    	    		String publishdate = catalogTypePage.getPublishDate();
			    	    		String publishflag = catalogTypePage.getPublishFlag();

			    	    		// verify quality status
			    	    		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
			    	    		List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus"))
			    	    				.maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
			    	    		for (String Rules : javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule"))) {
			    	    			Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);
			    	    		}
			    	    		qualitystatus.minimizeQualityStatusTab();
			    	    		log(LogType.INFO, "Verifying catalog entity rule");

			    	    		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
			    	    				.clickOnSecondResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon().expandTab();
			    	    		log(LogType.INFO, "Navigate to Master Catalog");
			    	    		Assertions.assertThat(publishdate).isEqualTo(catalogTypePage.getPublishDate());
			    	    		Assertions.assertThat(publishflag).isEqualTo(catalogTypePage.getPublishFlag());
			    	    		pimHomepage.clickLogoutButton();
			    	    	}

			    	    	// we need recently created item and make change on ItemPublish sheet ITP_030
			    	    	@PimFrameworkAnnotation(module = Modules.MEDICAL_CATALOG, category = CategoryType.REGRESSION)
			    	    	@TestDataSheet(sheetname = TestCaseSheet.ItemPublish)
			    	    	@Test(priority = 1, description = "ITP_030 Verification Part | ITP_030 | Verify whether the Publish date changed by the Medical division owner gets updated for other divisions when the other divisions have not modified the Original Publish Date for US.", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			    	    			"US", "pim","MEDICAL_CATALOG" })
			    	    	public void verify_publish_date_changed_by_medical_division_for_US(Map<String, String> map) {
			    	    		PimHomepage pimHomepage = new LoginPage()
			    	    				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
			    	    				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
			    	    		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
			    	    				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
			    	    				.clickOnFirstResult().selectTabfromDropdown(map.get("MedicalDivision"));
			    	    		log(LogType.INFO, "Navigate to Medical Catalog tab from Queries Menu for US Catalog");
			    	    		BasePage.WaitForMiliSec(3000);

//			    	    		String publishflag = catalogTypePage.getPublishFlag();
//			    	    		if (!publishflag.equals("Yes")) {
//			    	    			catalogTypePage.selectPublishFlag(map.get("PublishFlag"));
//			    	    			log(LogType.INFO, "assigning Yes in publish flag");
//			    	    		}
//			    	    		catalogTypePage.selectPublishDate(DateandTimeUtils.getTodaysDate());
			    	    		productdetailspage.clickRefreshIcon();
			    	    		String medicalpublishdate = catalogTypePage.getPublishDate();
			    	    		log(LogType.INFO, "Change publish date to current date");

			    	    		// verify quality status
			    	    		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
			    	    		List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus"))
			    	    				.maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
			    	    		for (String Rules : javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule"))) {
			    	    			Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);
			    	    		}
			    	    		qualitystatus.minimizeQualityStatusTab();
			    	    		log(LogType.INFO, "Verifying catalog entity rule");

			    	    		productdetailspage.selectTabfromDropdown(map.get("DentalDivision")).clickRefreshIcon();
			    	    		String dentalpublishdate = catalogTypePage.getPublishDate();
			    	    		Assertions.assertThat(dentalpublishdate).isEqualTo(medicalpublishdate);
			    	    		log(LogType.INFO, "verified publish date in Dental division");

			    	    		productdetailspage.selectTabfromDropdown(map.get("ZahnDivision")).clickRefreshIcon();
			    	    		String zahnpublishdate = catalogTypePage.getPublishDate();
			    	    		Assertions.assertThat(zahnpublishdate).isEqualTo(medicalpublishdate);
			    	    		log(LogType.INFO, "verified publish date in Zahn division");

			    	    		productdetailspage.selectTabfromDropdown(map.get("SpecialMarketsDivision")).clickRefreshIcon();
			    	    		String specialmarketspublishdate = catalogTypePage.getPublishDate();
			    	    		Assertions.assertThat(specialmarketspublishdate).isEqualTo(medicalpublishdate);
			    	    		log(LogType.INFO, "verified publish date in Special Markets division");

			    	    		// master
			    	    		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
			    	    				.clickOnFirstResult().selectTabfromDropdown(map.get("MedicalDivision")).clickRefreshIcon();
			    	    		log(LogType.INFO, "Navigate to Master Catalog");
			    	    		BasePage.WaitForMiliSec(3000);
			    	    		String masterpublishflag = catalogTypePage.getPublishFlag();
			    	    		//Assertions.assertThat(masterpublishflag).isEqualTo(map.get("PublishFlag"));
			    	    		//String mastermedicalpublishdate = catalogTypePage.getPublishDate();
			    	    		log(LogType.INFO, "Change publish date to current date");

			    	    		productdetailspage.selectTabfromDropdown(map.get("DentalDivision")).clickRefreshIcon();
			    	    		String masterdentalpublishdate = catalogTypePage.getPublishDate();
			    	    		Assertions.assertThat(masterdentalpublishdate).isEqualTo(medicalpublishdate);
			    	    		log(LogType.INFO, "verified publish date in Dental division");

			    	    		productdetailspage.selectTabfromDropdown(map.get("ZahnDivision")).clickRefreshIcon();
			    	    		String masterzahnpublishdate = catalogTypePage.getPublishDate();
			    	    		Assertions.assertThat(masterzahnpublishdate).isEqualTo(medicalpublishdate);
			    	    		log(LogType.INFO, "verified publish date in Zahn division");

			    	    		productdetailspage.selectTabfromDropdown(map.get("SpecialMarketsDivision")).clickRefreshIcon();
			    	    		String masterspecialmarketspublishdate = catalogTypePage.getPublishDate();
			    	    		Assertions.assertThat(masterspecialmarketspublishdate).isEqualTo(medicalpublishdate);
			    	    		log(LogType.INFO, "verified publish date in Special Markets division");
			    	    		pimHomepage.clickLogoutButton();
			    	    	}

			    	    	// we need recently created item and make change on ItemPublish sheet
			    	    	@PimFrameworkAnnotation(module = Modules.MEDICAL_CATALOG, category = CategoryType.REGRESSION)
			    	    	@TestDataSheet(sheetname = TestCaseSheet.ItemPublish)
			    	    	@Test(priority = 1, description = "ITP_030 Verification Part | ITP_030 | Verify whether the Publish date changed by the Medical division owner gets updated for other divisions when the other divisions have not modified the Original Publish Date. for CA", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"CA", "pim","MEDICAL_CATALOG" })
			    	    	public void verify_publish_date_changed_by_medical_division_for_CA(Map<String, String> map) {
			    	    		PimHomepage pimHomepage = new LoginPage()
			    	    				.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
			    	    				.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
			    	    		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
			    	    				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
			    	    				.clickOnFirstResult().selectTabfromDropdown(map.get("MedicalDivision"));
			    	    		log(LogType.INFO, "Navigate to Medical Catalog tab from Queries Menu for US Catalog");
			    	    		BasePage.WaitForMiliSec(3000);

			    	    		// qualitystatus.maximizeQualityStatusTab();
//			    	    		String publishflag = catalogTypePage.getPublishFlag();
//			    	    		if (!publishflag.equals("Yes")) {
//			    	    			catalogTypePage.selectPublishFlag(map.get("PublishFlag"));
//			    	    			log(LogType.INFO, "assigning Yes in publish flag");
//			    	    		}
//			    	    		// qualitystatus.minimizeQualityStatusTab();
//			    	    		catalogTypePage.verifySelectPublishDateFieldDropdown();
			    	    		productdetailspage.clickRefreshIcon();
			    	    		String medicalpublishdate = catalogTypePage.getPublishDate();
			    	    		log(LogType.INFO, "Change publish date to current date");

			    	    		// verify quality status
			    	    		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
			    	    		List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus"))
			    	    				.maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
			    	    		for (String Rules : javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule"))) {
			    	    			Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);
			    	    		}
			    	    		qualitystatus.minimizeQualityStatusTab();
			    	    		log(LogType.INFO, "Verifying catalog entity rule");

			    	    		productdetailspage.selectTabfromDropdown(map.get("DentalDivision")).clickRefreshIcon();
			    	    		String dentalpublishdate = catalogTypePage.getPublishDate();
			    	    		Assertions.assertThat(dentalpublishdate).isEqualTo(medicalpublishdate);
			    	    		log(LogType.INFO, "verified publish date in Dental division");

			    	    		productdetailspage.selectTabfromDropdown(map.get("ZahnDivision")).clickRefreshIcon();
			    	    		String zahnpublishdate = catalogTypePage.getPublishDate();
			    	    		Assertions.assertThat(zahnpublishdate).isEqualTo(medicalpublishdate);
			    	    		log(LogType.INFO, "verified publish date in Zahn division");

			    	    		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
			    	    				.clickOnSecondResult().selectTabfromDropdown(map.get("MedicalDivision")).clickRefreshIcon();
			    	    		log(LogType.INFO, "Navigate to Master Catalog");
			    	    		BasePage.WaitForMiliSec(3000);
			    	    		String masterpublishflag = catalogTypePage.getPublishFlag();
			    	    		//Assertions.assertThat(masterpublishflag).isEqualTo(map.get("PublishFlag"));
			    	    		// String mastermedicalpublishdate = catalogTypePage.getPublishDate();
			    	    		log(LogType.INFO, "Change publish date to current date");

			    	    		productdetailspage.selectTabfromDropdown(map.get("DentalDivision")).clickRefreshIcon();
			    	    		String masterdentalpublishdate = catalogTypePage.getPublishDate();
			    	    		Assertions.assertThat(masterdentalpublishdate).isEqualTo(medicalpublishdate);
			    	    		log(LogType.INFO, "verified publish date in Dental division");

			    	    		productdetailspage.selectTabfromDropdown(map.get("ZahnDivision")).clickRefreshIcon();
			    	    		String masterzahnpublishdate = catalogTypePage.getPublishDate();
			    	    		Assertions.assertThat(masterzahnpublishdate).isEqualTo(medicalpublishdate);
			    	    		log(LogType.INFO, "verified publish date in Zahn division");
			    	    		pimHomepage.clickLogoutButton();
			    	    	}

			    	    	// ITP_035
			    	    	@PimFrameworkAnnotation(module = Modules.MEDICAL_CATALOG, category = CategoryType.REGRESSION)
			    	    	@TestDataSheet(sheetname = TestCaseSheet.ItemPublish)
			    	    	@Test(priority = 1, description = "ITP_035 Verification Part | ITP_035 | Verify whether the Publish date changed by the Medical Owner division should not gets updated for other divisions when the other divisions have modified the Original Publish Date for US.", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			    	    			"US", "pim","MEDICAL_CATALOG" })
			    	    	public void verify_publish_date_not_update_for_dental_when_changed_in_medical_for_US(Map<String, String> map) {
			    	    		PimHomepage pimHomepage = new LoginPage()
			    	    				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
			    	    				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
			    	    		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
			    	    				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
			    	    				.clickOnFirstResult().selectTabfromDropdown(map.get("MedicalDivision"));
			    	    		log(LogType.INFO, "Navigate to Medical Catalog tab from Queries Menu for US Catalog");
			    	    		BasePage.WaitForMiliSec(3000);

//			    	    		String publishflag = catalogTypePage.getPublishFlag();
//			    	    		if (!publishflag.equals("Yes")) {
//			    	    			catalogTypePage.selectPublishFlag(map.get("PublishFlag"));
//			    	    			log(LogType.INFO, "assigning Yes in publish flag");
//			    	    		}
//			    	    		catalogTypePage.selectPublishDate(DateandTimeUtils.getTodaysDate());
			    	    		productdetailspage.clickRefreshIcon();
			    	    		String medicalpublishdate = catalogTypePage.getPublishDate();
			    	    		log(LogType.INFO, "Change publish date to current date");
			    	    		pimHomepage.clickLogoutButton();
			    	    		log(LogType.INFO, "Logout from Medical user");

			    	    		loginpage.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
			    	    				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
			    	    		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
			    	    				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
			    	    				.clickOnFirstResult().selectTabfromDropdown(map.get("DentalDivision"));
			    	    		log(LogType.INFO, "Navigate to Dental Catalog tab from Queries Menu for US Catalog");
			    	    		BasePage.WaitForMiliSec(3000);

//			    	    		String dentalpublishflag = catalogTypePage.getPublishFlag();
//			    	    		// Assertions.assertThat(dentalpublishflag).isEqualTo(map.get("PublishFlag"));
//			    	    		catalogTypePage.selectPublishDate(DateandTimeUtils.getFutureDate());
			    	    		productdetailspage.clickRefreshIcon();

			    	    		// verify quality status
			    	    		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
			    	    		List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus"))
			    	    				.maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
			    	    		for (String Rules : javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule"))) {
			    	    			Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);
			    	    		}
			    	    		qualitystatus.minimizeQualityStatusTab();
			    	    		log(LogType.INFO, "Verifying catalog entity rule");

			    	    		productdetailspage.selectTabfromDropdown(map.get("DentalDivision")).clickRefreshIcon();
			    	    		String dentalpublishdate = catalogTypePage.getPublishDate();
			    	    		Assertions.assertThat(dentalpublishdate).isNotEqualTo(medicalpublishdate);
			    	    		log(LogType.INFO, "verified publish date in Dental division");

			    	    		productdetailspage.selectTabfromDropdown(map.get("ZahnDivision")).clickRefreshIcon();
			    	    		String zahnpublishdate = catalogTypePage.getPublishDate();
			    	    		Assertions.assertThat(zahnpublishdate).isEqualTo(medicalpublishdate);
			    	    		log(LogType.INFO, "verified publish date in Zahn division");

			    	    		productdetailspage.selectTabfromDropdown(map.get("SpecialMarketsDivision")).clickRefreshIcon();
			    	    		String specialmarketspublishdate = catalogTypePage.getPublishDate();
			    	    		Assertions.assertThat(specialmarketspublishdate).isEqualTo(medicalpublishdate);
			    	    		log(LogType.INFO, "verified publish date in Special Markets division");

			    	    		// master
			    	    		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
			    	    				.clickOnFirstResult().selectTabfromDropdown(map.get("MedicalDivision")).clickRefreshIcon();
			    	    		log(LogType.INFO, "Navigate to Master Catalog");
			    	    		BasePage.WaitForMiliSec(3000);
			    	    		String masterpublishflag = catalogTypePage.getPublishFlag();
			    	    		Assertions.assertThat(masterpublishflag).isEqualTo(map.get("PublishFlag"));
			    	    		log(LogType.INFO, "Change publish date to current date");

			    	    		productdetailspage.selectTabfromDropdown(map.get("DentalDivision")).clickRefreshIcon();
			    	    		String masterdentalpublishdate = catalogTypePage.getPublishDate();
			    	    		Assertions.assertThat(masterdentalpublishdate).isNotEqualTo(medicalpublishdate);
			    	    		log(LogType.INFO, "verified publish date in Dental division");

			    	    		productdetailspage.selectTabfromDropdown(map.get("ZahnDivision")).clickRefreshIcon();
			    	    		String masterzahnpublishdate = catalogTypePage.getPublishDate();
			    	    		Assertions.assertThat(masterzahnpublishdate).isEqualTo(medicalpublishdate);
			    	    		log(LogType.INFO, "verified publish date in Zahn division");

			    	    		productdetailspage.selectTabfromDropdown(map.get("SpecialMarketsDivision")).clickRefreshIcon();
			    	    		String masterspecialmarketspublishdate = catalogTypePage.getPublishDate();
			    	    		Assertions.assertThat(masterspecialmarketspublishdate).isEqualTo(medicalpublishdate);
			    	    		log(LogType.INFO, "verified publish date in Special Markets division");
			    	    		pimHomepage.clickLogoutButton();
			    	    	}

			    	    	// ITP_035
			    	    	@PimFrameworkAnnotation(module = Modules.MEDICAL_CATALOG, category = CategoryType.Valid_Failure)
			    	    	@TestDataSheet(sheetname = TestCaseSheet.ItemPublish)
			    	    	@Test(priority = 1, description = "ITP_035 Verification Part | ITP_035 | Verify whether the Publish date changed by the Medical Owner division should not gets updated for other divisions when the other divisions have modified the Original Publish Date for CA.", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			    	    			"CA", "pim","MEDICAL_CATALOG" })
			    	    	public void verify_publish_date_not_update_for_dental_when_changed_in_medical_for_CA(Map<String, String> map) {

			    	    		PimHomepage pimHomepage = new LoginPage()
			    	    				.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
			    	    				.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
			    	    		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
			    	    				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
			    	    				.clickOnFirstResult().selectTabfromDropdown(map.get("MedicalDivision"));
			    	    		log(LogType.INFO, "Navigate to Medical Catalog tab from Queries Menu for US Catalog");
			    	    		BasePage.WaitForMiliSec(3000);

			    	    		//selects publish flag to yes in medical tab
//			    	    		String publishflag = catalogTypePage.getPublishFlag();
//			    	    		if (!publishflag.equals("Yes")) {
//			    	    			catalogTypePage.selectPublishFlag(map.get("PublishFlag"));
//			    	    			log(LogType.INFO, "assigning Yes in publish flag");
//			    	    		}

			    	    		//change publish date to today's in medical
//			    	    		catalogTypePage.selectPublishDate(DateandTimeUtils.getTodaysDate());
			    	    		productdetailspage.clickRefreshIcon();
			    	    		String medicalpublishdate = catalogTypePage.getPublishDate();
			    	    		log(LogType.INFO, "Change publish date to current date");
			    	    		pimHomepage.clickLogoutButton();
			    	    		log(LogType.INFO, "Logout from Medical user");

			    	    		//Verify publish flag in dental division as yes and set publish date with future date
			    	    		loginpage.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
			    	    				.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
			    	    		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
			    	    				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
			    	    				.clickOnFirstResult().selectTabfromDropdown(map.get("DentalDivision"));
			    	    		log(LogType.INFO, "Navigate to Dental Catalog tab from Queries Menu for US Catalog");
			    	    		BasePage.WaitForMiliSec(3000);

//			    	    		String dentalpublishflag = catalogTypePage.getPublishFlag();
//			    	    		Assertions.assertThat(dentalpublishflag).isEqualTo(map.get("PublishFlag"));
//			    	    		catalogTypePage.selectPublishDate(DateandTimeUtils.getFutureDate());
			    	    		productdetailspage.clickRefreshIcon();

			    	    		// verify quality status, catalog entity rule must get run
			    	    		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
			    	    		List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus"))
			    	    				.maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
			    	    		for (String Rules : javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule"))) {
			    	    			Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);
			    	    		}
			    	    		qualitystatus.minimizeQualityStatusTab();
			    	    		log(LogType.INFO, "Verifying catalog entity rule");

			    	    		//dental and medical publish date should not be equal because medical is got updated
			    	    		productdetailspage.selectTabfromDropdown(map.get("DentalDivision")).clickRefreshIcon();
			    	    		String dentalpublishdate = catalogTypePage.getPublishDate();
			    	    		Assertions.assertThat(dentalpublishdate).isNotEqualTo(medicalpublishdate);
			    	    		log(LogType.INFO, "verified publish date in Dental division");

			    	    		//Zahn and dental publish date should be equal because the zahn never got updated
			    	    		productdetailspage.selectTabfromDropdown(map.get("ZahnDivision")).clickRefreshIcon();
			    	    		String zahnpublishdate = catalogTypePage.getPublishDate();
			    	    		Assertions.assertThat(zahnpublishdate).isEqualTo(dentalpublishdate);
			    	    		log(LogType.INFO, "verified publish date in Zahn division");

			    	    		// master
			    	    		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
			    	    				.clickOnSecondResult().selectTabfromDropdown(map.get("MedicalDivision")).clickRefreshIcon();
			    	    		log(LogType.INFO, "Navigate to Master Catalog");
			    	    		BasePage.WaitForMiliSec(3000);

			    	    		//verifies Flag should be updated to yes
			    	    		String masterpublishflag = catalogTypePage.getPublishFlag();
			    	    		Assertions.assertThat(masterpublishflag).isEqualTo(map.get("PublishFlag"));
			    	    		log(LogType.INFO, "Change publish date to current date");

			    	    		//Master dental and medical publish date should not be equal because medical is got updated
			    	    		productdetailspage.selectTabfromDropdown(map.get("DentalDivision")).clickRefreshIcon();
			    	    		String masterdentalpublishdate = catalogTypePage.getPublishDate();
			    	    		Assertions.assertThat(masterdentalpublishdate).isNotEqualTo(medicalpublishdate);
			    	    		log(LogType.INFO, "verified publish date in Dental division");

			    	    		//Master zahn publish date should be equal to medical publish date
			    	    		productdetailspage.selectTabfromDropdown(map.get("ZahnDivision")).clickRefreshIcon();
			    	    		String masterzahnpublishdate = catalogTypePage.getPublishDate();
			    	    		Assertions.assertThat(masterzahnpublishdate).isEqualTo(dentalpublishdate);
			    	    		log(LogType.INFO, "verified publish date in Zahn division");

			    	    		pimHomepage.clickLogoutButton();	
			    	    	}

			    	    	// CATR_220-Verify if AEM_FSC gets removed from user driven list if Medical is
			    	    	// chosen in user driven catalog list of dental division

			    	    	@PimFrameworkAnnotation(module = Modules.MEDICAL_CATALOG, category = CategoryType.REGRESSION)
			    	    	@TestDataSheet(sheetname = TestCaseSheet.Catalog_CA)
			    	    	@Test(priority = 1, description = "CATR_220 Verification Part | CATR_220 | Verify if AEM_FSC gets removed from user driven list if Medical is chosen in user driven catalog list of dental division", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			    	    			"CA", "pim","MEDICAL_CATALOG" })
			    	    	public void Verify_if_AEM_FSC_gets_removed_from_user_driven_list_if_Medical_is_chosen_in_user_driven_catalog_list_of_dental_division_CATR_220(
			    	    			Map<String, String> map) {

			    	    		PimHomepage pimHomepage = new LoginPage()
			    	    				.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
			    	    				.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
			    	    		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
			    	    				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
			    	    				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
//			    	    		catalogTypePage.clearUserDriven(map.get("userDriven1"));
//			    	    		catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
			    	    		pimHomepage.productDetailSearchPage().clickRefreshIcon();
//			    	    		catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven1"));
			    	    		pimHomepage.productDetailSearchPage().clickRefreshIcon();
			    	    		user_driven = catalogTypePage.getUserDrivenCatalogs();
			    	    		Assertions.assertThat(user_driven).containsAnyOf(map.get("userDriven1"));
			    	    	}

			    	    	// ITP_018
			    	    	// Verify Item with Publish Flag = 'Y" and Publish Date as Future Date - All
			    	    	// Divisions and check in Middleware
			    	    	@PimFrameworkAnnotation(module = Modules.MEDICAL_CATALOG, category = CategoryType.REGRESSION)
			    	    	@TestDataSheet(sheetname = TestCaseSheet.ItemPublish)
			    	    	@Test(priority = 1, description = "ITP_018 Verification Part | ITP_018  | Verify whether the Publish Flag for a Medical Item get updated as 'Y' and publish date as future in the Middleware Export File, when the Items loaded from PIX to PIM with Publish Flag as \"Y\" for US", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			    	    			"US", "pim","MEDICAL_CATALOG" })
			    	    	public void verify_publish_flag_should_yes_and_publish_date_should_be_future_US_for_Medical_division(
			    	    			Map<String, String> map) {
			    	    		PimHomepage pimHomepage = new LoginPage()
			    	    				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
			    	    				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
			    	    		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
			    	    				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
			    	    				.clickOnFirstResult().selectTabfromDropdown(map.get("MedicalDivision"));
			    	    		log(LogType.INFO, "Navigate to Dental tab from Queries Menu for US Catalog");

//			    	    		String dentalpublishflag = catalogTypePage.getPublishFlag();
//			    	    		String Y = "Yes";
//			    	    		if (!dentalpublishflag.equals(Y)) {
//			    	    			catalogTypePage.selectPublishFlag(map.get("PublishFlag"));
//			    	    			log(LogType.INFO, "assigning yes in publish flag");
//			    	    		}
//			    	    		catalogTypePage.selectPublishDate(DateandTimeUtils.getFutureDate());
			    	    		log(LogType.INFO, "assigning future date");
			    	    		productdetailspage.clickRefreshIcon();
			    	    		String date = catalogTypePage.getPublishDate();

			    	    		// verify quality status
			    	    		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
			    	    		List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus"))
			    	    				.maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
			    	    		for (String Rules : javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule"))) {
			    	    			Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);
			    	    		}
			    	    		qualitystatus.minimizeQualityStatusTab();
			    	    		log(LogType.INFO, "Verifying catalog entity rule");

			    	    		// master
			    	    		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
			    	    				.clickOnFirstResult().selectTabfromDropdown(map.get("MedicalDivision")).clickRefreshIcon();
			    	    		log(LogType.INFO, "Navigate to Master Catalog");

			    	    		String masterdate = catalogTypePage.getPublishDate();
			    	    		Assertions.assertThat(date).isEqualTo(masterdate);
			    	    		log(LogType.INFO, "verifying dates should get update in master");
			    	    		pimHomepage.clickLogoutButton();
			    	    	}

			    	    	// ITP_018
			    	    	// Verify Item with Publish Flag = 'Y" and Publish Date as Future Date - All
			    	    	// Divisions and check in Middleware
			    	    	@PimFrameworkAnnotation(module = Modules.MEDICAL_CATALOG, category = CategoryType.REGRESSION)
			    	    	@TestDataSheet(sheetname = TestCaseSheet.ItemPublish)
			    	    	@Test(priority = 1, description = "ITP_018 Verification Part | ITP_018 | Verify whether the Publish Flag for a Medical Item get updated as 'Y' and publish date as future in the Middleware Export File, when the Items loaded from PIX to PIM with Publish Flag as 'Y' for CA", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			    	    			"CA", "pim","MEDICAL_CATALOG" })
			    	    	public void verify_publish_flag_should_yes_and_publish_date_should_be_future_CA_for_Medical_division(
			    	    			Map<String, String> map) {
			    	    		PimHomepage pimHomepage = new LoginPage()
			    	    				.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
			    	    				.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
			    	    		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
			    	    				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
			    	    				.clickOnFirstResult().selectTabfromDropdown(map.get("MedicalDivision"));
			    	    		log(LogType.INFO, "Navigate to Dental tab from Queries Menu for US Catalog");

//			    	    		String dentalpublishflag = catalogTypePage.getPublishFlag();
//			    	    		String Y = "Yes";
//			    	    		if (!dentalpublishflag.equals(Y)) {
//			    	    			catalogTypePage.selectPublishFlag(map.get("PublishFlag"));
//			    	    			log(LogType.INFO, "assigning yes in publish flag");
//			    	    		}
//			    	    		catalogTypePage.selectPublishDate(DateandTimeUtils.getFutureDate());
//			    	    		log(LogType.INFO, "assigning future date");
			    	    		productdetailspage.clickRefreshIcon();
			    	    		String date = catalogTypePage.getPublishDate();

			    	    		// verify quality status
			    	    		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
			    	    		List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus"))
			    	    				.maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
			    	    		for (String Rules : javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule"))) {
			    	    			Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);
			    	    		}
			    	    		qualitystatus.minimizeQualityStatusTab();
			    	    		log(LogType.INFO, "Verifying catalog entity rule");

			    	    		// master
			    	    		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
			    	    				.clickOnSecondResult().selectTabfromDropdown(map.get("MedicalDivision")).clickRefreshIcon();
			    	    		log(LogType.INFO, "Navigate to Master Catalog");

			    	    		String masterdate = catalogTypePage.getPublishDate();
			    	    		Assertions.assertThat(date).isEqualTo(masterdate);
			    	    		log(LogType.INFO, "verifying dates should get update in master");
			    	    		pimHomepage.clickLogoutButton();
			    	    	}

			    	    	// ITP_026
			    	    	@PimFrameworkAnnotation(module = Modules.MEDICAL_CATALOG, category = CategoryType.REGRESSION)
			    	    	@TestDataSheet(sheetname = TestCaseSheet.ItemPublish)
			    	    	@Test(priority = 1, description = "ITP_026 Verification Part | ITP_026 | Verify whether the Publish Flag for a Medical Item get updated as \"N\" in the Middleware Export File, when the Items loaded from PIX to PIM with Publish Flag as \"N\" for US", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			    	    			"US", "pim","MEDICAL_CATALOG" })
			    	    	public void verify_publish_flag_should_updated_as_NO_in_Medical_division_for_US(Map<String, String> map) {
			    	    		PimHomepage pimHomepage = new LoginPage()
			    	    				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
			    	    				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
			    	    		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
			    	    				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
			    	    				.clickOnFirstResult().selectTabfromDropdown(map.get("MedicalDivision"));
			    	    		log(LogType.INFO, "Navigate to Medical tab from Queries Menu for US Catalog");

//			    	    		String dentalpublishflag = catalogTypePage.getPublishFlag();
//			    	    		if (!dentalpublishflag.equals("No")) {
//			    	    			catalogTypePage.selectPublishFlag(map.get("PublishFlag"));
//			    	    			log(LogType.INFO, "assigning No in publish flag");
//			    	    		}
//			    	    		List<String> userdrivencatalogs = catalogTypePage.getUserDrivenCatalogs();
//			    	    		if (!userdrivencatalogs.contains(map.get("userDriven"))) {
//			    	    			catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
//			    	    		}

			    	    		// verify quality status
			    	    		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
			    	    		List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus"))
			    	    				.maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
			    	    		for (String Rules : javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule"))) {
			    	    			// Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);
			    	    		}
			    	    		qualitystatus.minimizeQualityStatusTab();
			    	    		log(LogType.INFO, "Verifying catalog entity rule");

			    	    		// master
			    	    		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
			    	    				.clickOnFirstResult().selectTabfromDropdown(map.get("MedicalDivision")).clickRefreshIcon();
			    	    		log(LogType.INFO, "Navigate to Medical division in Master Catalog");

			    	    		String masterpublishflag = catalogTypePage.getPublishFlag();
			    	    		Assertions.assertThat(masterpublishflag).isEqualTo(map.get("PublishFlag"));
			    	    		pimHomepage.clickLogoutButton();
			    	    	}

			    	    	// ITP_026
			    	    	@PimFrameworkAnnotation(module = Modules.MEDICAL_CATALOG, category = CategoryType.REGRESSION)
			    	    	@TestDataSheet(sheetname = TestCaseSheet.ItemPublish)
			    	    	@Test(priority = 1, description = "ITP_026 Verification Part | ITP_026_CA | Verify whether the Publish Flag for a Medical Item get updated as \"N\" in the Middleware Export File, when the Items loaded from PIX to PIM with Publish Flag as \"N\" for CA", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			    	    			"CA", "pim","MEDICAL_CATALOG" })
			    	    	public void verify_publish_flag_should_updated_as_NO_in_Medical_division_for_CA(Map<String, String> map) {
			    	    		PimHomepage pimHomepage = new LoginPage()
			    	    				.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
			    	    				.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
			    	    		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
			    	    				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
			    	    				.clickOnFirstResult().selectTabfromDropdown(map.get("MedicalDivision"));
			    	    		log(LogType.INFO, "Navigate to Medical tab from Queries Menu for CA Catalog");

//			    	    		String dentalpublishflag = catalogTypePage.getPublishFlag();
//			    	    		if (!dentalpublishflag.equals("No")) {
//			    	    			catalogTypePage.selectPublishFlag(map.get("PublishFlag"));
//			    	    			log(LogType.INFO, "assigning No in publish flag");
//			    	    		}
//			    	    		List<String> userdrivencatalogs = catalogTypePage.getUserDrivenCatalogs();
//			    	    		if (!userdrivencatalogs.contains(map.get("userDriven"))) {
//			    	    			catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
//			    	    		}

			    	    		// verify quality status
			    	    		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
			    	    		List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus"))
			    	    				.maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
			    	    		for (String Rules : javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule"))) {
			    	    			// Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);
			    	    		}
			    	    		qualitystatus.minimizeQualityStatusTab();
			    	    		log(LogType.INFO, "Verifying catalog entity rule");

			    	    		// master
			    	    		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
			    	    				.clickOnSecondResult().selectTabfromDropdown(map.get("MedicalDivision")).clickRefreshIcon();
			    	    		log(LogType.INFO, "Navigate to Medical division in Master Catalog");

			    	    		String masterpublishflag = catalogTypePage.getPublishFlag();
			    	    		Assertions.assertThat(masterpublishflag).isEqualTo(map.get("PublishFlag"));
			    	    		pimHomepage.clickLogoutButton();
			    	    	}

			    	    	// ITP_022
			    	    	@PimFrameworkAnnotation(module = Modules.MEDICAL_CATALOG, category = CategoryType.REGRESSION)
			    	    	@TestDataSheet(sheetname = TestCaseSheet.ItemPublish)
			    	    	@Test(priority = 1, description = "ITP_022 Verification Part | ITP_022 | Verify whether the Publish Flag for a Medical Item get updated as \"Y\" and publish date as Currentin the Middleware Export File, when the Items loaded from PIX to PIM with Publish Flag as \"Y\" for US", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			    	    			"US", "pim","MEDICAL_CATALOG" })
			    	    	public void verify_publish_flag_should_updated_as_Yes_in_Medical_division_for_US(Map<String, String> map) {
			    	    		PimHomepage pimHomepage = new LoginPage()
			    	    				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
			    	    				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
			    	    		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
			    	    				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
			    	    				.clickOnFirstResult().selectTabfromDropdown(map.get("MedicalDivision"));
			    	    		log(LogType.INFO, "Navigate to Medical tab from Queries Menu for US Catalog");

//			    	    		catalogTypePage.selectPublishFlag(map.get("PublishFlag"));
//			    	    		log(LogType.INFO, "assigning Yes in publish flag");
			    	    //
//			    	    		catalogTypePage.selectPublishDate(DateandTimeUtils.getTodaysDate());
//			    	    		productdetailspage.clickRefreshIcon();
//			    	    		log(LogType.INFO, "assigning current date");

			    	    		String publishdate = catalogTypePage.getPublishDate();
			    	    		String publishFlag = catalogTypePage.getPublishFlag();

//			    	    		List<String> userdrivencatalogs = catalogTypePage.getUserDrivenCatalogs();
//			    	    		if (!userdrivencatalogs.contains(map.get("userDriven"))) {
//			    	    			catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
//			    	    		}

			    	    		// verify quality status
			    	    		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
			    	    		List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus"))
			    	    				.maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
			    	    		for (String Rules : javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule"))) {
			    	    			Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);
			    	    		}
			    	    		qualitystatus.minimizeQualityStatusTab();
			    	    		log(LogType.INFO, "Verifying catalog entity rule");

			    	    		// master
			    	    		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
			    	    				.clickOnFirstResult().selectTabfromDropdown(map.get("MedicalDivision")).clickRefreshIcon();
			    	    		log(LogType.INFO, "Navigate to Medical division in Master Catalog");

			    	    		String masterpublishdate = catalogTypePage.getPublishDate();
			    	    		String masterpublishFlag = catalogTypePage.getPublishFlag();

			    	    		Assertions.assertThat(masterpublishdate).isEqualTo(publishdate);
			    	    		Assertions.assertThat(masterpublishFlag).isEqualTo(publishFlag);
			    	    		pimHomepage.clickLogoutButton();
			    	    	}

			    	    	// ITP_022
			    	    	@PimFrameworkAnnotation(module = Modules.MEDICAL_CATALOG, category = CategoryType.REGRESSION)
			    	    	@TestDataSheet(sheetname = TestCaseSheet.ItemPublish)
			    	    	@Test(priority = 1, description = "ITP_022 Verification Part | ITP_022_CA | Verify whether the Publish Flag for a Medical Item get updated as \"Y\" and publish date as Current in the Middleware Export File, when the Items loaded from PIX to PIM with Publish Flag as \"Y\" for CA", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			    	    			"CA", "pim","MEDICAL_CATALOG" })
			    	    	public void verify_publish_flag_should_updated_as_Yes_in_Medical_division_for_CA(Map<String, String> map) {
			    	    		PimHomepage pimHomepage = new LoginPage()
			    	    				.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
			    	    				.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
			    	    		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
			    	    				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
			    	    				.clickOnFirstResult().selectTabfromDropdown(map.get("MedicalDivision"));
			    	    		log(LogType.INFO, "Navigate to Medical tab from Queries Menu for US Catalog");

//			    	    		catalogTypePage.selectPublishFlag(map.get("PublishFlag"));
//			    	    		log(LogType.INFO, "assigning Yes in publish flag");
			    	    //
//			    	    		catalogTypePage.selectPublishDate(DateandTimeUtils.getTodaysDate());
//			    	    		productdetailspage.clickRefreshIcon();
//			    	    		log(LogType.INFO, "assigning current date");

			    	    		String publishdate = catalogTypePage.getPublishDate();
			    	    		String publishFlag = catalogTypePage.getPublishFlag();

			    	    		List<String> userdrivencatalogs = catalogTypePage.getUserDrivenCatalogs();
//			    	    		if (!userdrivencatalogs.contains(map.get("userDriven"))) {
//			    	    			catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
//			    	    		}

			    	    		// verify quality status
			    	    		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
			    	    		List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus"))
			    	    				.maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
			    	    		for (String Rules : javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule"))) {
			    	    			Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);
			    	    		}
			    	    		qualitystatus.minimizeQualityStatusTab();
			    	    		log(LogType.INFO, "Verifying catalog entity rule");

			    	    		// master
			    	    		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
			    	    				.clickOnSecondResult().selectTabfromDropdown(map.get("MedicalDivision")).clickRefreshIcon();
			    	    		log(LogType.INFO, "Navigate to Medical division in Master Catalog");

			    	    		String masterpublishdate = catalogTypePage.getPublishDate();
			    	    		String masterpublishFlag = catalogTypePage.getPublishFlag();

			    	    		Assertions.assertThat(masterpublishdate).isEqualTo(publishdate);
			    	    		Assertions.assertThat(masterpublishFlag).isEqualTo(publishFlag);
			    	    		pimHomepage.clickLogoutButton();
			    	    	}

			    	    	
			    	    	//SpecialMarketsCatalogSubstitutionRulesTest----------------------------------------
			    	    	
			    	    	//CATR_149--Done
			    	    		@PimFrameworkAnnotation(module = Modules.SubstituteCatalogRule, category = CategoryType.REGRESSION)
			    	    		@TestDataSheet(sheetname = TestCaseSheet.Catalog)
			    	    		@Test(priority = 1, description = "CATR_149 Verification Part | CATR_149 | Verification Of Substitution Rules In SpecialMarkets Divisions With Sub Type As S/SA/GE",groups = {"US","pim","SMSubstituteCatalogRule"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)

			    	    		public void VerificationOfSubstitutionRulesInSpecialMarketsCatalogWithSubTypeAsSA(Map<String, String> map)      

			    	    		{

			    	    			PimHomepage pimHomepage = new LoginPage()
			    	    					.enterUserName(ExcelUtils.getLoginData().get("Special Markets User").get("UserName"))
			    	    					.enterPassword(ExcelUtils.getLoginData().get("Special Markets User").get("Password")).clickLoginButton();

			    	    			pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
			    	    			.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("ReferenceTab"));
			    	    			String referenceType = referencesPage.getReferenceType();
			    	    			Assertions.assertThat(referenceType).isEqualTo(map.get("ReferenceType"));
			    	    			String referenceObjectNumber = referencesPage.getReferenceObjectNumber();
			    	    			productDetailSearchPage.selectTabfromDropdown(map.get("TabName"));

//			    	    			catalogTypePage.clearUserDriven(map.get("userDriven"));
			    	    			BasePage.WaitForMiliSec(3000);

			    	    			String userDrivernCatalog =map.get("userDriven");
//			    	    			catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
			    	    			pimHomepage.productDetailSearchPage().clickRefreshIcon();
			    	    			List<String> user_driven = catalogTypePage.getUserDrivenCatalogs();
			    	    			Assertions.assertThat(user_driven).containsAnyOf(userDrivernCatalog);
			    	    			log(LogType.INFO, "Navigate to Dental Catalog Tab to assign SpecialMarket catalog in Parent");

			    	    			//verify quality status
			    	    			pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
			    	    			List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
			    	    			for (String Rules:javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule")))
			    	    			{
			    	    				Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);	
			    	    			}
			    	    			qualitystatus.minimizeQualityStatusTab();
			    	    			log(LogType.INFO,"Verifying catalog entity rule");

			    	    			//Verify in master catalog
			    	    			pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog"))
			    	    			.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
			    	    			List<String> masterUserdriven = catalogTypePage.getUserDrivenCatalogs();
			    	    			Assertions.assertThat(user_driven).containsAll(masterUserdriven);
			    	    			log(LogType.INFO, "Verifying Catalogs in Master");

			    	    			//Verify Catalog in Child item
			    	    			pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType"))
			    	    			.removeAndEnterHsiItemNumber(referenceObjectNumber).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("AllCatalogtab"));
			    	    			List<String> all_catalog_SM =allCatalogsPage.getAllSpecialMarketsCatalogList();
			    	    			Assertions.assertThat(all_catalog_SM).contains(map.get("userDriven"));	
			    	    			log(LogType.INFO, "Verifying Catalogs in Child Item");
			    	    		}

			    	    		//CATR_152--Done
			    	    		@PimFrameworkAnnotation(module = Modules.SubstituteCatalogRule, category = CategoryType.REGRESSION)
			    	    		@TestDataSheet(sheetname = TestCaseSheet.Catalog)
			    	    		@Test(priority = 1, description = "CATR_152 Verification Part | CATR_152 | Verification Of Substitution Rules In SpecialMarkets Divisions With Sub Type As SP CATR_152",groups = {"US","pim","SMSubstituteCatalogRule"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
			    	    		public void VerificationOfSubstitutionRulesForSpecialMarketDivisionWithSubTypeAsSP(Map<String, String> map) {

			    	    			PimHomepage pimHomepage = new LoginPage()
			    	    					.enterUserName(ExcelUtils.getLoginData().get("Special Markets User").get("UserName"))
			    	    					.enterPassword(ExcelUtils.getLoginData().get("Special Markets User").get("Password")).clickLoginButton();

			    	    			pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
			    	    			.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
			    	    			.clickOnFirstResult().selectTabfromDropdown(map.get("ReferenceTab"));
			    	    			List<String> referenceType = referencesPage.getReferenceTypeInList();
			    	    			//Assertions.assertThat(referenceType).isEqualTo(map.get("ReferenceType"));
			    	    			Assertions.assertThat(referenceType).containsAnyOf((map.get("ReferenceType")));
			    	    			String referenceObjectNumber = referencesPage.getReferenceObjectNumber();
			    	    			productDetailSearchPage.selectTabfromDropdown(map.get("TabName"));

			    	    			catalogTypePage.clearUserDriven(map.get("userDriven"));
			    	    			BasePage.WaitForMiliSec(3000);
			    	    			String userDrivernCatalog = map.get("userDriven");
//			    	    			catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
//			    	    			pimHomepage.productDetailSearchPage().clickRefreshIcon();
//			    	    			List<String> user_driven = catalogTypePage.getUserDrivenCatalogs();
//			    	    			Assertions.assertThat(user_driven).containsAnyOf(userDrivernCatalog);
//			    	    			log(LogType.INFO, "Navigate to SpecialMarkets Catalog Tab to assign SpecialMarkets catalog in Parent");

			    	    			// verify quality status
			    	    			pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
			    	    			List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
			    	    			for (String Rules:javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule")))
			    	    			{
			    	    				Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);	
			    	    			}
			    	    			qualitystatus.minimizeQualityStatusTab();
			    	    			log(LogType.INFO,"Verifying catalog entity rule");
			    	    			// Verify in master catalog
			    	    			pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
			    	    			.clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
			    	    			List<String> masterUserdriven = catalogTypePage.getUserDrivenCatalogs();
			    	    			Assertions.assertThat(user_driven).containsAll(masterUserdriven);
			    	    			log(LogType.INFO, "Verifying Catalogs in Master");

			    	    			// Verifying in Child item
			    	    			pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType"))
			    	    			.removeAndEnterHsiItemNumber(referenceObjectNumber).clickSeachButton().clickOnFirstResult()
			    	    			.selectTabfromDropdown(map.get("AllCatalogtab"));
			    	    			List<String> all_catalog_SM =allCatalogsPage.getAllSpecialMarketsCatalogList(); 
			    	    			Assertions.assertThat(all_catalog_SM).contains(map.get("userDriven"));
			    	    			log(LogType.INFO, "Verifying Catalogs in Child Item");

			    	    			//Verify Dental and Zahn Catalog should not be populated
			    	    			List<String> all_catalog_Dental =allCatalogsPage.getAllDentalCatalogList();
			    	    			Assertions.assertThat(all_catalog_Dental).doesNotContainSequence(userDrivernCatalog);
			    	    			List<String> all_catalog_Zahn =allCatalogsPage.getAllZahnCatalogList();
			    	    			Assertions.assertThat(all_catalog_Zahn).doesNotContainSequence(userDrivernCatalog);
			    	    			log(LogType.INFO, "Verifying Catalogs which are not populated in Child Item");
			    	    		}

			    	    		//CATR_158--Done
			    	    		@PimFrameworkAnnotation(module = Modules.SubstituteCatalogRule, category = CategoryType.REGRESSION)
			    	    		@TestDataSheet(sheetname = TestCaseSheet.Catalog)
			    	    		@Test(priority = 1, description = "CATR_158 Verification Part | CATR_158 | Verification Of Substitution Rules In SpecialMarkets Divisions With Sub Type As PA", groups = {"US","pim","SMSubstituteCatalogRule"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
			    	    		public void VerificationOfSubstitutionRulesForSpecialMarketsDivisionWithSubTypeAsPA(Map<String, String> map) {

			    	    			PimHomepage pimHomepage = new LoginPage()
			    	    					.enterUserName(ExcelUtils.getLoginData().get("Special Markets User").get("UserName"))
			    	    					.enterPassword(ExcelUtils.getLoginData().get("Special Markets User").get("Password")).clickLoginButton();

			    	    			pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
			    	    			.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
			    	    			.clickOnFirstResult().selectTabfromDropdown(map.get("ReferenceTab"));
			    	    			String referenceType = referencesPage.getReferenceType();
			    	    			Assertions.assertThat(referenceType).isEqualTo(map.get("ReferenceType"));
			    	    			String referenceObjectNumber = referencesPage.getReferenceObjectNumber();
			    	    			productDetailSearchPage.selectTabfromDropdown(map.get("TabName"));

//			    	    			catalogTypePage.clearUserDriven(map.get("userDriven"));
			    	    			BasePage.WaitForMiliSec(3000);
			    	    			String userDrivernCatalog = map.get("userDriven");
//			    	    			catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
			    	    			pimHomepage.productDetailSearchPage().clickRefreshIcon();
			    	    			List<String> user_driven = catalogTypePage.getUserDrivenCatalogs();
			    	    			Assertions.assertThat(user_driven).containsAnyOf(userDrivernCatalog);
			    	    			log(LogType.INFO, "Navigate to SpecialMarkets Catalog Tab to assign SpecialMarkets catalog in Parent");

			    	    			// verify quality status
			    	    			pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
			    	    			List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
			    	    			for (String Rules:javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule")))
			    	    			{
			    	    				Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);	
			    	    			}
			    	    			qualitystatus.minimizeQualityStatusTab();
			    	    			log(LogType.INFO,"Verifying catalog entity rule");

			    	    			// Verify in master catalog
			    	    			pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
			    	    			.clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
			    	    			List<String> masterUserdriven = catalogTypePage.getUserDrivenCatalogs();
			    	    			Assertions.assertThat(user_driven).containsAll(masterUserdriven);
			    	    			log(LogType.INFO, "Verifying Catalogs in Master");

			    	    			// Verifying in Child item
			    	    			pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType"))
			    	    			.removeAndEnterHsiItemNumber(referenceObjectNumber).clickSeachButton().clickOnFirstResult()
			    	    			.selectTabfromDropdown(map.get("AllCatalogtab"));
			    	    			List<String> all_catalog_SM =allCatalogsPage.getAllSpecialMarketsCatalogList(); 
			    	    			Assertions.assertThat(all_catalog_SM).contains(map.get("userDriven"));
			    	    			log(LogType.INFO, "Verifying Catalogs Child Item");

			    	    			//Verify In Medical Catalog should not be populated for child item
			    	    			List<String> all_catalog_Medical =allCatalogsPage.getAllMedicalCatalogList();
			    	    			Assertions.assertThat(all_catalog_Medical).doesNotContainSequence(userDrivernCatalog);
			    	    			log(LogType.INFO, "Verifying Catalogs which are not populated in Child Item");
			    	    		}
			    	    		//CATR_164--Done
			    	    		@PimFrameworkAnnotation(module = Modules.SubstituteCatalogRule, category = CategoryType.REGRESSION)
			    	    		@TestDataSheet(sheetname = TestCaseSheet.Catalog)
			    	    		@Test(priority = 1, description = "CATR_164 Verification Part | CATR_164 | Verification Of Substitution Rules In Special Market Divisions With Sub Type As A3",groups = {"US","pim","SMSubstituteCatalogRule"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
			    	    		public void VerificationOfSubstitutionRulesForSpecialMarketDivisionWithSubTypeAsA3(Map<String, String> map) {

			    	    			PimHomepage pimHomepage = new LoginPage()
			    	    					.enterUserName(ExcelUtils.getLoginData().get("Special Markets User").get("UserName"))
			    	    					.enterPassword(ExcelUtils.getLoginData().get("Special Markets User").get("Password")).clickLoginButton();

			    	    			pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
			    	    			.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
			    	    			.clickOnFirstResult().selectTabfromDropdown(map.get("ReferenceTab"));
			    	    			String referenceType = referencesPage.getReferenceType();
			    	    			Assertions.assertThat(referenceType).isEqualTo(map.get("ReferenceType"));
			    	    			String referenceObjectNumber = referencesPage.getReferenceObjectNumber();
			    	    			productDetailSearchPage.selectTabfromDropdown(map.get("TabName"));

//			    	    			catalogTypePage.clearUserDriven(map.get("userDriven"));
			    	    			BasePage.WaitForMiliSec(3000);
			    	    			String userDrivernCatalog = map.get("userDriven");
//			    	    			catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
			    	    			pimHomepage.productDetailSearchPage().clickRefreshIcon();
			    	    			List<String> user_driven = catalogTypePage.getUserDrivenCatalogs();
			    	    			Assertions.assertThat(user_driven).containsAnyOf(userDrivernCatalog);
			    	    			log(LogType.INFO, "Navigate to Special Market Catalog Tab to assign Special Market catalog in Parent");

			    	    			//verify quality status
			    	    			pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
			    	    			List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
			    	    			for (String Rules:javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule")))
			    	    			{
			    	    				Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);	
			    	    			}
			    	    			qualitystatus.minimizeQualityStatusTab();
			    	    			log(LogType.INFO,"Verifying catalog entity rule");


			    	    			// Verify in master catalog
			    	    			pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
			    	    			.clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
			    	    			List<String> masterUserdriven = catalogTypePage.getUserDrivenCatalogs();
			    	    			Assertions.assertThat(user_driven).containsAll(masterUserdriven);
			    	    			log(LogType.INFO, "Verifying SM Catalog in Master");

			    	    			// Verifying in Child item
			    	    			pimHomepage.mainMenu().clickRefreshMenuIcon().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
			    	    			.enterHsiItemNumber(referenceObjectNumber).clickSeachButton().clickOnFirstResult()
			    	    			.selectTabfromDropdown(map.get("AllCatalogtab"));
			    	    			qualitystatus.maximizeQualityStatusTab();
			    	    			List<String> all_catalog_SM = allCatalogsPage.getAllSpecialMarketsCatalogList();
			    	    			Assertions.assertThat(all_catalog_SM).doesNotContainSequence(userDrivernCatalog);
			    	    			qualitystatus.minimizeQualityStatusTab();
			    	    			log(LogType.INFO, "Verifying Catalog in Child Item should not get assign");
			    	    		}
			    	    		//CATR_169--Data Issue
			    	    		@PimFrameworkAnnotation(module = Modules.SubstituteCatalogRule, category = CategoryType.REGRESSION)
			    	    		@TestDataSheet(sheetname = TestCaseSheet.Catalog)
			    	    		@Test(priority = 1, description = "CATR_169 Verification Part | CATR_169 | Verification Of Substitution Rules In Special Market Divisions With Sub Type As A6",groups = {"US","pim","SMSubstituteCatalogRule"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
			    	    		public void VerificationOfSubstitutionRulesForSpecialMarketDivisionWithSubTypeAsA6(Map<String, String> map) {

			    	    			PimHomepage pimHomepage = new LoginPage()
			    	    					.enterUserName(ExcelUtils.getLoginData().get("Special Markets User").get("UserName"))
			    	    					.enterPassword(ExcelUtils.getLoginData().get("Special Markets User").get("Password")).clickLoginButton();

			    	    			pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
			    	    			.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
			    	    			.clickOnFirstResult().selectTabfromDropdown(map.get("ReferenceTab"));
			    	    			String referenceType = referencesPage.getReferenceType();
			    	    			Assertions.assertThat(referenceType).isEqualTo(map.get("ReferenceType"));
			    	    			String referenceObjectNumber = referencesPage.getReferenceObjectNumber();
			    	    			productDetailSearchPage.selectTabfromDropdown(map.get("TabName"));

//			    	    			catalogTypePage.clearUserDriven(map.get("userDriven"));
			    	    			BasePage.WaitForMiliSec(3000);
			    	    			log(LogType.INFO, "Navigate to Special Market Catalog Tab to assign Special Market catalog in Parent");

			    	    			String userDrivernCatalog = map.get("userDriven");
//			    	    			catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
			    	    			pimHomepage.productDetailSearchPage().clickRefreshIcon();
			    	    			List<String> user_driven = catalogTypePage.getUserDrivenCatalogs();
			    	    			Assertions.assertThat(user_driven).containsAnyOf(userDrivernCatalog);

			    	    			//verify quality status 
			    	    			pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
			    	    			List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus"))
			    	    					.maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
			    	    			for (String Rules:javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule")))
			    	    			{
			    	    				Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);	
			    	    			}
			    	    			qualitystatus.minimizeQualityStatusTab();
			    	    			log(LogType.INFO,"Verifying catalog entity rule");

			    	    			// Verify in master catalog
			    	    			pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
			    	    			.clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
			    	    			List<String> masterUserdriven = catalogTypePage.getUserDrivenCatalogs();
			    	    			Assertions.assertThat(user_driven).containsAll(masterUserdriven);
			    	    			log(LogType.INFO, "Verifying SM Catalog in Master");

			    	    			// Verifying in Child item
			    	    			pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType"))
			    	    			.removeAndEnterHsiItemNumber(referenceObjectNumber).clickSeachButton().clickOnFirstResult()
			    	    			.selectTabfromDropdown(map.get("AllCatalogtab"));
			    	    			List<String> all_catalog_SM = allCatalogsPage.getAllSpecialMarketsCatalogList();
			    	    			Assert.assertNotEquals(all_catalog_SM, userDrivernCatalog);
			    	    			log(LogType.INFO, "Verifying Catalog in Child Item should not get assign");
			    	    		}

			    	    		//StandardExceptionRuleTest-----------------------------------------------------
			    	    		
			    	    		//CATR_205
			    	    	    @PimFrameworkAnnotation(module = Modules.SPECIAL_MARKET_CATALOG, category = CategoryType.REGRESSION)
			    	    	    @TestDataSheet(sheetname = TestCaseSheet.CopyRule)
			    	    	    @Test(priority = 1, description = "CATR_205 Verification Part | CATR_205 | Verification Of Standard Exception Rule In Special Markets Divisions", groups = {"US","pim","SPECIAL_MARKET_CATALOG"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
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

			    	    	        List<String> sm_userdriven_list = javautils.readMultipleValuesFromExcel(map.get("userDriven"));
			    	    	        productdetail.selectTabfromDropdown(map.get("TabName"));
			    	    	        log(LogType.INFO, "Navigate to Special Markets Catalog Tab to assign FEDDEALER and FEDERAL catalog in Parent");

			    	    	        //assigning feeddealer in special markets catalog
//			    	    	        catalogTypePage.clearUserDriven(map.get("userDriven"));
//			    	    			BasePage.WaitForMiliSec(3000);
//			    	    			System.out.println(sm_userdriven_list.get(0));
//			    	    	        catalogTypePage.clickUserDriven().selectUserDriven(sm_userdriven_list.get(0));
//			    	    	        pimHomepage.productDetailSearchPage().clickRefreshIcon();
//			    	    			BasePage.WaitForMiliSec(60000);
//			    	    	        pimHomepage.productDetailSearchPage().clickRefreshIcon();
//			    	    			BasePage.WaitForMiliSec(60000);
//			    	    	        pimHomepage.productDetailSearchPage().clickRefreshIcon();
//			    	    			BasePage.WaitForMiliSec(60000);
//			    	    	        pimHomepage.productDetailSearchPage().clickRefreshIcon();
//			    	    			BasePage.WaitForMiliSec(60000);
//			    	    	        pimHomepage.productDetailSearchPage().clickRefreshIcon();
			    	    	        special_market_user_driven = catalogTypePage.getUserDrivenCatalogs();
			    	    	        pimHomepage.productDetailSearchPage().clickRefreshIcon().clickRefreshIcon();
			    	    	        Assertions.assertThat(special_market_user_driven).doesNotContain(sm_userdriven_list.get(0));
			    	    	        log(LogType.INFO, "Verifying FEDDEALER is not added");

			    	    	        //assigning federal in special markets catalog
//			    	    	        catalogTypePage.clearUserDriven(map.get("userDriven"));
//			    	    			BasePage.WaitForMiliSec(3000);
//			    	    	        catalogTypePage.clickUserDriven().selectUserDriven(sm_userdriven_list.get(1));
//			    	    	        pimHomepage.productDetailSearchPage().clickRefreshIcon();
//			    	    			BasePage.WaitForMiliSec(60000);
//			    	    	        pimHomepage.productDetailSearchPage().clickRefreshIcon();
//			    	    			BasePage.WaitForMiliSec(60000);
//			    	    	        pimHomepage.productDetailSearchPage().clickRefreshIcon();
//			    	    			BasePage.WaitForMiliSec(60000);
//			    	    	        pimHomepage.productDetailSearchPage().clickRefreshIcon();
//			    	    			BasePage.WaitForMiliSec(60000);
//			    	    	        pimHomepage.productDetailSearchPage().clickRefreshIcon();
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
			    	    	    @Test(priority = 1, description = "CATR_206 Verification Part | CATR_206 | Verification Of copy rule In Special Markets Divisions", groups = {"US","pim","SPECIAL_MARKET_CATALOG"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
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
//			    	    	        catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
			    	    	        pimHomepage.productDetailSearchPage().clickRefreshIcon().clickRefreshIcon();
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
			    	    	    @Test(priority = 1, description = "CATR_207 Verification Part | CATR_207 | Verification Of copy rule In Dental Divisions", groups = {"US","pim","SPECIAL_MARKET_CATALOG"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
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
			    	    	    
			    	    	    //SubstituteExceptionRuleTest-------------------------------------------------------
			    	    	    
			    	    	    //CATR_187
			    	    	    @PimFrameworkAnnotation(module = Modules.MEDICAL_CATALOG, category = CategoryType.REGRESSION)
			    	    	    @TestDataSheet(sheetname = TestCaseSheet.SubstituteExceptionRule)
			    	    	    @Test(priority = 1, description = "CATR_187 Verification Part | CATR_187 | Verification Of Substitution Exception Rule In Medical Divisions where parent and child manu code is not same", groups = {"US","pim","MEDICAL_CATALOG"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
			    	    	    public void verificationOfSubstitutionExceptionRuleInMedicalCatalogWithChildManuCodeIsNotSameAsParent(Map<String, String> map) {
			    	    	        PimHomepage pimHomepage = new LoginPage()
			    	    	                .enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
			    	    	                .enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();

			    	    	        //verifying in parent item in US catalog
			    	    	        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
			    	    	                .enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("ReferenceTab"));
			    	    	        String referenceType = referencesPage.getReferenceType();
			    	    	        Assertions.assertThat(referenceType).isEqualTo(map.get("ReferenceType"));
			    	    	        String referenceObjectNumber = referencesPage.getReferenceObjectNumber();

			    	    	        productdetail.selectTabfromDropdown(map.get("GlobalAttributeTab"));
			    	    	        log(LogType.INFO, "Navigate to Global Attribute Tab to verify Manufacture Code in Parent");

			    	    	        //verifying manufacture code
			    	    	        String manufacturecode = globalattribute.getManufacturercode();
			    	    	        Assertions.assertThat(manufacturecode).isEqualTo(map.get("ManufactureCode"));

			    	    	        //assigning medical catalog
			    	    	        productdetail.selectTabfromDropdown(map.get("MedicalTab"));
			    	    	        log(LogType.INFO, "Navigate to Medical Catalog Tab to assign Medical catalog in Parent");
//			    	    	        catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
			    	    	        pimHomepage.productDetailSearchPage().clickRefreshIcon();
			    	    	        medical_user_driven = catalogTypePage.getUserDrivenCatalogs();
			    	    	        Assertions.assertThat(medical_user_driven).containsAnyOf(map.get("userDrivenAssert"));

			    	    	        //assigning feeddealer in special markets catalog
			    	    	        productdetail.selectTabfromDropdown("Special Markets Catalog");
			    	    	        log(LogType.INFO, "Navigate to Special Markets Catalog Tab to assign FEDDEALER catalog in Parent");
//			    	    	        catalogTypePage.clickUserDriven().selectUserDriven(map.get("SMUserDriven"));
			    	    	        pimHomepage.productDetailSearchPage().clickRefreshIcon();
			    	    	        special_market_user_driven = catalogTypePage.getUserDrivenCatalogs();
			    	    	        Assertions.assertThat(special_market_user_driven).containsAnyOf(map.get("SMUserDriven"));

			    	    	        //verify quality status
			    	    	        productdetail.selectTabfromDropdown(map.get("Qualitystatustab"));
			    	    	        List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).getRuleNamesByRuleStatus(map.get("Status"));
			    	    	        List<String> myList = new ArrayList<String>(Arrays.asList(map.get("CatalogEntityRule").split(",")));
			    	    	        Assertions.assertThat(catalogentityrule).isEqualTo(myList);
			    	    	        log(LogType.INFO,"Verifying catalog entity rule");

			    	    	        //verify in master
			    	    	        productdetail.clickMenuRefreshIcon();
			    	    	        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog")).enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
			    	    	        log(LogType.INFO, "Navigate to Global attribute tab to verify the Manufacture code in Master Catalog");
			    	    	        productdetail.selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
			    	    	        List<String> masterUserdriven = catalogTypePage.getUserDrivenCatalogs();
			    	    	        Assertions.assertThat(masterUserdriven).containsAnyOf(map.get("userDriven"));

			    	    	        //verifying in child item
			    	    	        productdetail.clickMenuRefreshIcon();
			    	    	        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
			    	    	                .enterHsiItemNumber(referenceObjectNumber).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
			    	    	        log(LogType.INFO, "Navigate to Global Attribute Tab to verify Manufacture Code in Child");

			    	    	        //verifying manufacture code
			    	    	        manufacturecode = globalattribute.getManufacturercode();
			    	    	        Assertions.assertThat(manufacturecode).isNotEqualTo(map.get("ManufactureCode"));

			    	    	        productdetail.selectTabfromDropdown("AllCatalogsTab");
			    	    	        special_market_catalogs = allCatalogsPage.getAllSpecialMarketsCatalogList();
			    	    	        Assertions.assertThat(special_market_catalogs).doesNotContain(map.get("SMUserDriven"));

			    	    	        //Deleting userdriven in parent
			    	    	        productdetail.clickMenuRefreshIcon();
			    	    	        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
			    	    	                .enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("MedicalTab"));
			    	    	        catalogTypePage.clearSelectedFields(map.get("userDriven"));
			    	    	        log(LogType.INFO,"Deleting the Userdriven");
			    	    	        pimHomepage.clickLogoutButton();
			    	    	    }

			    	    	    //CATR_188
			    	    	    @PimFrameworkAnnotation(module = Modules.DENTAL_CATALOG, category = CategoryType.REGRESSION)
			    	    	    @TestDataSheet(sheetname = TestCaseSheet.SubstituteExceptionRule)
			    	    	    @Test(priority = 1, description = "CATR_188 Verification Part | CATR_188 | Verification Of Substitution Exception Rule In Dental Divisions when parent item Manu code is same as child", groups = {"US","pim","DENTAL_CATALOG"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
			    	    	    public void verificationOfSubstitutionExceptionRuleInDentalCatalogWithParentManuCodeIsSameAsChild(Map<String, String> map) {
			    	    	        PimHomepage pimHomepage = new LoginPage()
			    	    	                .enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
			    	    	                .enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();

			    	    	        //verifying in parent item
			    	    	        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
			    	    	                .enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("ReferenceTab"));
			    	    	        String referenceType = referencesPage.clickReferenceTypeHeader().getReferenceType();
			    	    	        Assertions.assertThat(referenceType).isEqualTo(map.get("ReferenceType"));
			    	    	        String referenceObjectNumber = referencesPage.getReferenceObjectNumber();

			    	    	        productdetail.selectTabfromDropdown(map.get("GlobalAttributeTab"));
			    	    	        log(LogType.INFO, "Navigate to Global Attribute Tab to verify Manufacture Code in Parent");

			    	    	        //verifying manufacture code
			    	    	        String manufacturecode = globalattribute.getManufacturercode();
			    	    	        Assertions.assertThat(manufacturecode).isEqualTo(map.get("ManufactureCode"));

			    	    	        //assigning dental catalog
			    	    	        productdetail.selectTabfromDropdown(map.get("DentalTab"));
			    	    	        log(LogType.INFO, "Navigate to Dental Catalog Tab to assign Dental catalog in Parent");
//			    	    	        catalogTypePage.clearUserDriven(map.get("userDriven"));
//			    	    	        catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
			    	    	        pimHomepage.productDetailSearchPage().clickRefreshIcon();
			    	    	        dental_user_driven = catalogTypePage.getUserDrivenCatalogs();
			    	    	        Assertions.assertThat(dental_user_driven).containsAnyOf(map.get("userDriven"));

			    	    	        //assigning feeddealer in special markets catalog
			    	    	        productdetail.selectTabfromDropdown("Special Markets Catalog");
			    	    	        log(LogType.INFO, "Navigate to Special Markets Catalog Tab to assign FEDDEALER catalog in Parent");
//			    	    	        catalogTypePage.clearUserDriven(map.get("SMUserDriven"));
//			    	    	        catalogTypePage.clickUserDriven().selectUserDriven(map.get("SMUserDriven"));
			    	    	        pimHomepage.productDetailSearchPage().clickRefreshIcon();
			    	    	        special_market_user_driven = catalogTypePage.getUserDrivenCatalogs();
			    	    	        Assertions.assertThat(special_market_user_driven).doesNotContain(map.get("SMUserDriven"));

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
			    	    	        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog")).enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
			    	    	        log(LogType.INFO, "Navigate to Global attribute tab to verify the Manufacture code in Master Catalog");
			    	    	        productdetail.selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
			    	    	        List<String> masterUserdriven = catalogTypePage.getUserDrivenCatalogs();
			    	    	        Assertions.assertThat(masterUserdriven).containsAnyOf(map.get("userDriven"));

			    	    	        //verifying in child item
			    	    	        productdetail.clickMenuRefreshIcon();
			    	    	        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
			    	    	                .enterHsiItemNumber(referenceObjectNumber).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
			    	    	        log(LogType.INFO, "Navigate to Global Attribute Tab to verify Manufacture Code in Child");

			    	    	        //verifying manufacture code
			    	    	        manufacturecode = globalattribute.getManufacturercode();
			    	    	        Assertions.assertThat(manufacturecode).isEqualTo(map.get("ManufactureCode"));

			    	    	        productdetail.selectTabfromDropdown(map.get("AllCatalogtab"));
			    	    	        special_market_catalogs = allCatalogsPage.getAllSpecialMarketsCatalogList();
			    	    	        Assertions.assertThat(special_market_catalogs).doesNotContain(map.get("SMUserDriven"));

			    	    	        pimHomepage.clickLogoutButton();
			    	    	    }

			    	    	    //CATR_189
			    	    	    @PimFrameworkAnnotation(module = Modules.DENTAL_CATALOG, category = CategoryType.REGRESSION)
			    	    	    @TestDataSheet(sheetname = TestCaseSheet.SubstituteExceptionRule)
			    	    	    @Test(priority = 1, description = "CATR_189 Verification Part | CATR_189 | Verification Of Substitution Exception Rule In Dental Divisions when child item Manu code is not same as Parent", groups = {"US","pim","DENTAL_CATALOG"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
			    	    	    public void verificationOfSubstitutionExceptionRuleInDentalCatalogWithChildManuCodeIsNotSameAsParent(Map<String, String> map) {
			    	    	        PimHomepage pimHomepage = new LoginPage()
			    	    	                .enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
			    	    	                .enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();

			    	    	        //verifying in parent item
			    	    	        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
			    	    	                .enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("ReferenceTab"));
			    	    	        String referenceType = referencesPage.getReferenceType();
			    	    	        Assertions.assertThat(referenceType).isEqualTo(map.get("ReferenceType"));
			    	    	        String referenceObjectNumber = referencesPage.getReferenceObjectNumber();

			    	    	        productdetail.selectTabfromDropdown(map.get("GlobalAttributeTab"));
			    	    	        log(LogType.INFO, "Navigate to Global Attribute Tab to verify Manufacture Code in Parent");

			    	    	        //verifying manufacture code
			    	    	        String manufacturecode = globalattribute.getManufacturercode();
			    	    	        Assertions.assertThat(manufacturecode).isEqualTo(map.get("ManufactureCode"));

			    	    	        //assigning dental catalog
			    	    	        productdetail.selectTabfromDropdown(map.get("DentalTab"));
			    	    	        log(LogType.INFO, "Navigate to Dental Catalog Tab to assign Dental catalog in Parent");
//			    	    	        catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
//			    	    	        pimHomepage.productDetailSearchPage().clickRefreshIcon();
//			    	    	        dental_user_driven = catalogTypePage.getUserDrivenCatalogs();
//			    	    	        Assertions.assertThat(dental_user_driven).containsAnyOf(map.get("userDrivenAssert"));

			    	    	        //assigning feeddealer in special markets catalog
			    	    	        productdetail.selectTabfromDropdown("Special Markets Catalog");
			    	    	        log(LogType.INFO, "Navigate to Special Markets Catalog Tab to assign FEDDEALER catalog in Parent");
//			    	    	        catalogTypePage.clickUserDriven().selectUserDriven(map.get("SMUserDriven"));
			    	    	        pimHomepage.productDetailSearchPage().clickRefreshIcon();
			    	    	        special_market_user_driven = catalogTypePage.getUserDrivenCatalogs();
			    	    	        Assertions.assertThat(special_market_user_driven).containsAnyOf(map.get("SMUserDriven"));

			    	    	        //verify quality status
			    	    	        productdetail.selectTabfromDropdown(map.get("Qualitystatustab"));
			    	    	        List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).getRuleNamesByRuleStatus(map.get("Status"));
			    	    	        List<String> myList = new ArrayList<String>(Arrays.asList(map.get("CatalogEntityRule").split(",")));
			    	    	        Assertions.assertThat(catalogentityrule).isEqualTo(myList);
			    	    	        log(LogType.INFO,"Verifying catalog entity rule");

			    	    	        //verify in master
			    	    	        productdetail.clickMenuRefreshIcon();
			    	    	        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog")).enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
			    	    	        log(LogType.INFO, "Navigate to Global attribute tab to verify the Manufacture code in Master Catalog");
			    	    	        productdetail.selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
			    	    	        List<String> masterUserdriven = catalogTypePage.getUserDrivenCatalogs();
			    	    	        Assertions.assertThat(masterUserdriven).containsAnyOf(map.get("userDriven"));

			    	    	        //verifying in child item
			    	    	        productdetail.clickMenuRefreshIcon();
			    	    	        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
			    	    	                .enterHsiItemNumber(referenceObjectNumber).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
			    	    	        log(LogType.INFO, "Navigate to Global Attribute Tab to verify Manufacture Code in Child");

			    	    	        //verifying manufacture code
			    	    	        manufacturecode = globalattribute.getManufacturercode();
			    	    	        Assertions.assertThat(manufacturecode).isNotEqualTo(map.get("ManufactureCode"));

			    	    	        productdetail.selectTabfromDropdown("AllCatalogsTab");
			    	    	        special_market_catalogs = allCatalogsPage.getAllSpecialMarketsCatalogList();
			    	    	        Assertions.assertThat(special_market_catalogs).doesNotContain(map.get("SMUserDriven"));

			    	    	        //Deleting userdriven in parent
			    	    	        productdetail.clickMenuRefreshIcon();
			    	    	        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
			    	    	                .enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("DentalTab"));
//			    	    	       catalogTypePage.clearSelectedFields(map.get("userDriven"));
			    	    	        log(LogType.INFO,"Deleting the Userdriven");
			    	    	        pimHomepage.clickLogoutButton();
			    	    	    }

			    	    	    //CATR_190
			    	    	    @PimFrameworkAnnotation(module = Modules.MEDICAL_CATALOG, category = CategoryType.REGRESSION)
			    	    	    @TestDataSheet(sheetname = TestCaseSheet.SubstituteExceptionRule)
			    	    	    @Test(priority = 1, description = "CATR_190 Verification Part | CATR_190 | Verification Of Substitution Exception Rule In Medical Divisions where parent and child manu code is not same", groups = {"US","pim","MEDICAL_CATALOG"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
			    	    	    public void verificationOfSubstitutionExceptionRuleInMedicalCatalogWithBothParentChildManuCodeIsNotSame(Map<String, String> map) {
			    	    	        PimHomepage pimHomepage = new LoginPage()
			    	    	                .enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
			    	    	                .enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();

			    	    	        //verifying in parent item
			    	    	        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
			    	    	                .enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("ReferenceTab"));
			    	    	        String referenceType = referencesPage.getReferenceType();
			    	    	        Assertions.assertThat(referenceType).isEqualTo(map.get("ReferenceType"));
			    	    	        String referenceObjectNumber = referencesPage.getReferenceObjectNumber();

			    	    	        productdetail.selectTabfromDropdown(map.get("GlobalAttributeTab"));
			    	    	        log(LogType.INFO, "Navigate to Global Attribute Tab to verify Manufacture Code in Parent");

			    	    	        //verifying manufacture code
			    	    	        String manufacturecode = globalattribute.getManufacturercode();
			    	    	        Assertions.assertThat(manufacturecode).isNotEqualTo(map.get("ManufactureCode"));

			    	    	        //assigning medical catalog
			    	    	        productdetail.selectTabfromDropdown(map.get("MedicalTab"));
			    	    	        log(LogType.INFO, "Navigate to Medical Catalog Tab to assign Medical catalog in Parent");
//			    	    	        catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
			    	    	        pimHomepage.productDetailSearchPage().clickRefreshIcon();
			    	    	        medical_user_driven = catalogTypePage.getUserDrivenCatalogs();
			    	    	        Assertions.assertThat(dental_user_driven).containsAnyOf(map.get("userDrivenAssert"));

			    	    	        //assigning feeddealer in special markets catalog
			    	    	        productdetail.selectTabfromDropdown("Special Markets Catalog");
			    	    	        log(LogType.INFO, "Navigate to Special Markets Catalog Tab to assign FEDDEALER catalog in Parent");
//			    	    	        catalogTypePage.clickUserDriven().selectUserDriven(map.get("SMUserDriven"));
			    	    	        pimHomepage.productDetailSearchPage().clickRefreshIcon();
			    	    	        special_market_user_driven = catalogTypePage.getUserDrivenCatalogs();
			    	    	        Assertions.assertThat(special_market_user_driven).containsAnyOf(map.get("SMUserDriven"));

			    	    	        //verify quality status
			    	    	        productdetail.selectTabfromDropdown(map.get("Qualitystatustab"));
			    	    	        List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).getRuleNamesByRuleStatus(map.get("Status"));
			    	    	        List<String> myList = new ArrayList<String>(Arrays.asList(map.get("CatalogEntityRule").split(",")));
			    	    	        Assertions.assertThat(catalogentityrule).isEqualTo(myList);
			    	    	        log(LogType.INFO,"Verifying catalog entity rule");

			    	    	        //verify in master
			    	    	        productdetail.clickMenuRefreshIcon();
			    	    	        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog")).enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
			    	    	        log(LogType.INFO, "Navigate to Global attribute tab to verify the Manufacture code in Master Catalog");
			    	    	        productdetail.selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
			    	    	        List<String> masterUserdriven = catalogTypePage.getUserDrivenCatalogs();
			    	    	        Assertions.assertThat(masterUserdriven).containsAnyOf(map.get("userDriven"));

			    	    	        //verifying in child item
			    	    	        productdetail.clickMenuRefreshIcon();
			    	    	        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
			    	    	                .enterHsiItemNumber(referenceObjectNumber).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
			    	    	        log(LogType.INFO, "Navigate to Global Attribute Tab to verify Manufacture Code in Child");

			    	    	        //verifying manufacture code
			    	    	        manufacturecode = globalattribute.getManufacturercode();
			    	    	        Assertions.assertThat(manufacturecode).isNotEqualTo(map.get("ManufactureCode"));

			    	    	        productdetail.selectTabfromDropdown("AllCatalogsTab");
			    	    	        special_market_catalogs = allCatalogsPage.getAllSpecialMarketsCatalogList();
			    	    	        Assertions.assertThat(special_market_catalogs).containsAnyOf(map.get("SMUserDriven"));

			    	    	        //Deleting userdriven in parent
			    	    	        productdetail.clickMenuRefreshIcon();
			    	    	        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
			    	    	                .enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("MedicalTab"));
//			    	    	        catalogTypePage.clearSelectedFields(map.get("userDriven"));
			    	    	        log(LogType.INFO,"Deleting the Userdriven");
			    	    	        pimHomepage.clickLogoutButton();
			    	    	    }

			    	    	    //CATR_191
			    	    	    @PimFrameworkAnnotation(module = Modules.DENTAL_CATALOG, category = CategoryType.REGRESSION)
			    	    	    @TestDataSheet(sheetname = TestCaseSheet.SubstituteExceptionRule)
			    	    	    @Test(priority = 1, description = "CATR_191 Verification Part | CATR_191 | Verification Of Substitution Exception Rule In Dental Divisions where parent and child manu code is not ORAPHA", groups = {"US","pim","DENTAL_CATALOG"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
			    	    	    public void verificationOfSubstitutionExceptionRuleInDentalCatalogWithParentAndChildManuCodeIsNotORAPHA(Map<String, String> map) {
			    	    	        PimHomepage pimHomepage = new LoginPage()
			    	    	                .enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
			    	    	                .enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();

			    	    	        //verifying in parent item
			    	    	        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
			    	    	                .enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("ReferenceTab"));
			    	    	        String referenceType = referencesPage.getReferenceType();
			    	    	        Assertions.assertThat(referenceType).isEqualTo(map.get("ReferenceType"));
			    	    	        String referenceObjectNumber = referencesPage.getReferenceObjectNumber();

			    	    	        productdetail.selectTabfromDropdown(map.get("GlobalAttributeTab"));
			    	    	        log(LogType.INFO, "Navigate to Global Attribute Tab to verify Manufacture Code in Parent");

			    	    	        //verifying manufacture code
			    	    	        String manufacturecode = globalattribute.getManufacturercode();
			    	    	        Assertions.assertThat(manufacturecode).isNotEqualTo(map.get("ManufactureCode"));

			    	    	        //assigning dental catalog
			    	    	        productdetail.selectTabfromDropdown(map.get("DentalTab"));
			    	    	        log(LogType.INFO, "Navigate to Dental Catalog Tab to assign Dental catalog in Parent");
//			    	    	        catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
			    	    	        pimHomepage.productDetailSearchPage().clickRefreshIcon();
			    	    	        dental_user_driven = catalogTypePage.getUserDrivenCatalogs();
			    	    	        Assertions.assertThat(dental_user_driven).containsAnyOf(map.get("userDrivenAssert"));

			    	    	        //assigning feeddealer in special markets catalog
			    	    	        productdetail.selectTabfromDropdown("Special Markets Catalog");
			    	    	        log(LogType.INFO, "Navigate to Special Markets Catalog Tab to assign FEDDEALER catalog in Parent");
//			    	    	        catalogTypePage.clickUserDriven().selectUserDriven(map.get("SMUserDriven"));
			    	    	        pimHomepage.productDetailSearchPage().clickRefreshIcon();
			    	    	        special_market_user_driven = catalogTypePage.getUserDrivenCatalogs();
			    	    	        Assertions.assertThat(special_market_user_driven).doesNotContain(map.get("SMUserDriven"));

			    	    	        //verify quality status
			    	    	        productdetail.selectTabfromDropdown(map.get("Qualitystatustab"));
			    	    	        List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).getRuleNamesByRuleStatus(map.get("Status"));
			    	    	        List<String> myList = new ArrayList<String>(Arrays.asList(map.get("CatalogEntityRule").split(",")));
			    	    	        Assertions.assertThat(catalogentityrule).isEqualTo(myList);
			    	    	        log(LogType.INFO,"Verifying catalog entity rule");

			    	    	        //verify in master
			    	    	        productdetail.clickMenuRefreshIcon();
			    	    	        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog")).enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
			    	    	        log(LogType.INFO, "Navigate to Global attribute tab to verify the Manufacture code in Master Catalog");
			    	    	        productdetail.selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
			    	    	        List<String> masterUserdriven = catalogTypePage.getUserDrivenCatalogs();
			    	    	        Assertions.assertThat(masterUserdriven).containsAnyOf(map.get("userDriven"));

			    	    	        //verifying in child item
			    	    	        productdetail.clickMenuRefreshIcon();
			    	    	        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
			    	    	                .enterHsiItemNumber(referenceObjectNumber).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
			    	    	        log(LogType.INFO, "Navigate to Global Attribute Tab to verify Manufacture Code in Child");

			    	    	        //verifying manufacture code
			    	    	        manufacturecode = globalattribute.getManufacturercode();
			    	    	        Assertions.assertThat(manufacturecode).isNotEqualTo(map.get("ManufactureCode"));

			    	    	        productdetail.selectTabfromDropdown("AllCatalogsTab");
			    	    	        special_market_catalogs = allCatalogsPage.getAllSpecialMarketsCatalogList();
			    	    	        Assertions.assertThat(special_market_catalogs).doesNotContain(map.get("SMUserDriven"));

			    	    	        //Deleting userdriven in parent
			    	    	        productdetail.clickMenuRefreshIcon();
			    	    	        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
			    	    	                .enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("DentalTab"));
//			    	    	        catalogTypePage.clearSelectedFields(map.get("userDriven"));
			    	    	        log(LogType.INFO,"Deleting the Userdriven");
			    	    	        pimHomepage.clickLogoutButton();
			    	    	    }

			    	    	    //CATR_192
			    	    	    @PimFrameworkAnnotation(module = Modules.MEDICAL_CATALOG, category = CategoryType.REGRESSION)
			    	    	    @TestDataSheet(sheetname = TestCaseSheet.SubstituteExceptionRule)
			    	    	    @Test(priority = 1, description = "CATR_192 Verification Part | CATR_192 | Verification Of Substitution Exception Rule In Medical Divisions where parent manu code is not same as child", groups = {"US","pim","MEDICAL_CATALOG"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
			    	    	    public void verificationOfSubstitutionExceptionRuleInMedicalCatalogWithParentManuCodeIsNotSameAsChild(Map<String, String> map) {
			    	    	        PimHomepage pimHomepage = new LoginPage()
			    	    	                .enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
			    	    	                .enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();

			    	    	        //verifying in parent item
			    	    	        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
			    	    	                .enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("ReferenceTab"));
			    	    	        String referenceType = referencesPage.getReferenceType();
			    	    	        Assertions.assertThat(referenceType).isEqualTo(map.get("ReferenceType"));
			    	    	        String referenceObjectNumber = referencesPage.getReferenceObjectNumber();

			    	    	        productdetail.selectTabfromDropdown(map.get("GlobalAttributeTab"));
			    	    	        log(LogType.INFO, "Navigate to Global Attribute Tab to verify Manufacture Code in Parent");

			    	    	        //verifying manufacture code
			    	    	        String manufacturecode = globalattribute.getManufacturercode();
			    	    	        Assertions.assertThat(manufacturecode).isNotEqualTo(map.get("ManufactureCode"));

			    	    	        //assigning medical catalog
			    	    	        productdetail.selectTabfromDropdown(map.get("MedicalTab"));
			    	    	        log(LogType.INFO, "Navigate to Medical Catalog Tab to assign Medical catalog in Parent");
//			    	    	        catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
			    	    	        pimHomepage.productDetailSearchPage().clickRefreshIcon();
			    	    	        medical_user_driven = catalogTypePage.getUserDrivenCatalogs();
			    	    	        Assertions.assertThat(medical_user_driven).containsAnyOf(map.get("userDrivenAssert"));

			    	    	        //assigning feeddealer in special markets catalog
			    	    	        productdetail.selectTabfromDropdown("Special Markets Catalog");
			    	    	        log(LogType.INFO, "Navigate to Special Markets Catalog Tab to assign FEDDEALER catalog in Parent");
//			    	    	        catalogTypePage.clickUserDriven().selectUserDriven(map.get("SMUserDriven"));
			    	    	        pimHomepage.productDetailSearchPage().clickRefreshIcon();
			    	    	        special_market_user_driven = catalogTypePage.getUserDrivenCatalogs();
			    	    	        Assertions.assertThat(special_market_user_driven).containsAnyOf(map.get("SMUserDriven"));

			    	    	        //verify quality status
			    	    	        productdetail.selectTabfromDropdown(map.get("Qualitystatustab"));
			    	    	        List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).getRuleNamesByRuleStatus(map.get("Status"));
			    	    	        List<String> myList = new ArrayList<String>(Arrays.asList(map.get("CatalogEntityRule").split(",")));
			    	    	        Assertions.assertThat(catalogentityrule).isEqualTo(myList);
			    	    	        log(LogType.INFO,"Verifying catalog entity rule");

			    	    	        //verify in master
			    	    	        productdetail.clickMenuRefreshIcon();
			    	    	        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog")).enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
			    	    	        log(LogType.INFO, "Navigate to Global attribute tab to verify the Manufacture code in Master Catalog");
			    	    	        productdetail.selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
			    	    	        List<String> masterUserdriven = catalogTypePage.getUserDrivenCatalogs();
			    	    	        Assertions.assertThat(masterUserdriven).containsAnyOf(map.get("userDriven"));

			    	    	        //verifying in child item
			    	    	        productdetail.clickMenuRefreshIcon();
			    	    	        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
			    	    	                .enterHsiItemNumber(referenceObjectNumber).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
			    	    	        log(LogType.INFO, "Navigate to Global Attribute Tab to verify Manufacture Code in Child");

			    	    	        //verifying manufacture code
			    	    	        manufacturecode = globalattribute.getManufacturercode();
			    	    	        Assertions.assertThat(manufacturecode).isNotEqualTo(map.get("ManufactureCode"));

			    	    	        productdetail.selectTabfromDropdown("AllCatalogsTab");
			    	    	        special_market_catalogs = allCatalogsPage.getAllSpecialMarketsCatalogList();
			    	    	        Assertions.assertThat(special_market_catalogs).doesNotContain(map.get("SMUserDriven"));

			    	    	        //Deleting userdriven in parent
			    	    	        productdetail.clickMenuRefreshIcon();
			    	    	        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
			    	    	                .enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("MedicalTab"));
//			    	    	        catalogTypePage.clearSelectedFields(map.get("userDriven"));
			    	    	        log(LogType.INFO,"Deleting the Userdriven");
			    	    	        pimHomepage.clickLogoutButton();
			    	    	    }

			    	    	    //CATR_193
			    	    	    @PimFrameworkAnnotation(module = Modules.DENTAL_CATALOG, category = CategoryType.REGRESSION)
			    	    	    @TestDataSheet(sheetname = TestCaseSheet.SubstituteExceptionRule)
			    	    	    @Test(priority = 1, description = "CATR_193 Verification Part | CATR_193 | Verification Of Substitution Exception Rule In Dental Divisions where parent and child manu code is ORAPHA", groups = {"US","pim","DENTAL_CATALOG"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
			    	    	    public void verificationOfSubstitutionExceptionRuleInDentalCatalogWithParentAndChildManuCodeIsORAPHA(Map<String, String> map) {
			    	    	        PimHomepage pimHomepage = new LoginPage()
			    	    	                .enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
			    	    	                .enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();

			    	    	        //verifying in parent item
			    	    	        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
			    	    	                .enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("ReferenceTab"));
			    	    	        String referenceType = referencesPage.getReferenceType();
			    	    	        Assertions.assertThat(referenceType).isEqualTo(map.get("ReferenceType"));
			    	    	        String referenceObjectNumber = referencesPage.getReferenceObjectNumber();

			    	    	        productdetail.selectTabfromDropdown(map.get("GlobalAttributeTab"));
			    	    	        log(LogType.INFO, "Navigate to Global Attribute Tab to verify Manufacture Code in Parent");

			    	    	        //verifying manufacture code
			    	    	        String manufacturecode = globalattribute.getManufacturercode();
			    	    	        Assertions.assertThat(manufacturecode).isEqualTo(map.get("ManufactureCode"));

			    	    	        //assigning dental catalog
			    	    	        productdetail.selectTabfromDropdown(map.get("DentalTab"));
			    	    	        log(LogType.INFO, "Navigate to Dental Catalog Tab to assign Dental catalog in Parent");
//			    	    	        catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
			    	    	        pimHomepage.productDetailSearchPage().clickRefreshIcon();
			    	    	        dental_user_driven = catalogTypePage.getUserDrivenCatalogs();
			    	    	        Assertions.assertThat(dental_user_driven).containsAnyOf(map.get("userDrivenAssert"));

			    	    	        //assigning feeddealer in special markets catalog
			    	    	        productdetail.selectTabfromDropdown("Special Markets Catalog");
			    	    	        log(LogType.INFO, "Navigate to Special Markets Catalog Tab to assign FEDDEALER catalog in Parent");
//			    	    	        catalogTypePage.clickUserDriven().selectUserDriven(map.get("SMUserDriven"));
			    	    	        pimHomepage.productDetailSearchPage().clickRefreshIcon();
			    	    	        special_market_user_driven = catalogTypePage.getUserDrivenCatalogs();
			    	    	        Assertions.assertThat(special_market_user_driven).doesNotContain(map.get("SMUserDriven"));

			    	    	        //verify quality status
			    	    	        productdetail.selectTabfromDropdown(map.get("Qualitystatustab"));
			    	    	        List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).getRuleNamesByRuleStatus(map.get("Status"));
			    	    	        List<String> myList = new ArrayList<String>(Arrays.asList(map.get("CatalogEntityRule").split(",")));
			    	    	        Assertions.assertThat(catalogentityrule).isEqualTo(myList);
			    	    	        log(LogType.INFO,"Verifying catalog entity rule");

			    	    	        //verify in master
			    	    	        productdetail.clickMenuRefreshIcon();
			    	    	        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog")).enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
			    	    	        log(LogType.INFO, "Navigate to Global attribute tab to verify the Manufacture code in Master Catalog");
			    	    	        productdetail.selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
			    	    	        List<String> masterUserdriven = catalogTypePage.getUserDrivenCatalogs();
			    	    	        Assertions.assertThat(masterUserdriven).containsAnyOf(map.get("userDriven"));

			    	    	        //verifying in child item
			    	    	        productdetail.clickMenuRefreshIcon();
			    	    	        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
			    	    	                .enterHsiItemNumber(referenceObjectNumber).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
			    	    	        log(LogType.INFO, "Navigate to Global Attribute Tab to verify Manufacture Code in Child");

			    	    	        //verifying manufacture code
			    	    	        manufacturecode = globalattribute.getManufacturercode();
			    	    	        Assertions.assertThat(manufacturecode).isEqualTo(map.get("ManufactureCode"));

			    	    	        productdetail.selectTabfromDropdown("AllCatalogsTab");
			    	    	        special_market_catalogs = allCatalogsPage.getAllSpecialMarketsCatalogList();
			    	    	        Assertions.assertThat(special_market_catalogs).doesNotContain(map.get("SMUserDriven"));

			    	    	        //Deleting userdriven in parent
			    	    	        productdetail.clickMenuRefreshIcon();
			    	    	        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
			    	    	                .enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("DentalTab"));
//			    	    	        catalogTypePage.clearSelectedFields(map.get("userDriven"));
			    	    	        log(LogType.INFO,"Deleting the Userdriven");
			    	    	        pimHomepage.clickLogoutButton();
			    	    	    }

			    	    	    //CATR_194
			    	    	    @PimFrameworkAnnotation(module = Modules.MEDICAL_CATALOG, category = CategoryType.REGRESSION)
			    	    	    @TestDataSheet(sheetname = TestCaseSheet.SubstituteExceptionRule)
			    	    	    @Test(priority = 1, description = "CATR_194 Verification Part | CATR_194 | Verification Of Substitution Exception Rule In Medical Divisions", groups = {"US","pim","MEDICAL_CATALOG"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
			    	    	    public void verificationOfSubstitutionExceptionRuleInMedicalCatalogWithParentManuCodeIsNotORAPHAAndChildIsORAPHA(Map<String, String> map) {
			    	    	        PimHomepage pimHomepage = new LoginPage()
			    	    	                .enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
			    	    	                .enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();

			    	    	        //verifying in parent item
			    	    	        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
			    	    	                .enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("ReferenceTab"));
			    	    	        String referenceType = referencesPage.getReferenceType();
			    	    	        Assertions.assertThat(referenceType).isEqualTo(map.get("ReferenceType"));
			    	    	        String referenceObjectNumber = referencesPage.getReferenceObjectNumber();

			    	    	        productdetail.selectTabfromDropdown(map.get("GlobalAttributeTab"));
			    	    	        log(LogType.INFO, "Navigate to Global Attribute Tab to verify Manufacture Code in Parent");

			    	    	        //verifying manufacture code
			    	    	        String manufacturecode = globalattribute.getManufacturercode();
			    	    	        Assertions.assertThat(manufacturecode).isNotEqualTo(map.get("ManufactureCode"));

			    	    	        //assigning medical catalog
			    	    	        productdetail.selectTabfromDropdown(map.get("MedicalTab"));
			    	    	        log(LogType.INFO, "Navigate to Medical Catalog Tab to assign Medical catalog in Parent");
//			    	    	        catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
			    	    	        pimHomepage.productDetailSearchPage().clickRefreshIcon();
			    	    	        medical_user_driven = catalogTypePage.getUserDrivenCatalogs();
			    	    	        Assertions.assertThat(medical_user_driven).containsAnyOf(map.get("userDrivenAssert"));

			    	    	        //assigning feeddealer in special markets catalog
			    	    	        productdetail.selectTabfromDropdown("Special Markets Catalog");
			    	    	        log(LogType.INFO, "Navigate to Special Markets Catalog Tab to assign FEDDEALER catalog in Parent");
//			    	    	        catalogTypePage.clickUserDriven().selectUserDriven(map.get("SMUserDriven"));
			    	    	        pimHomepage.productDetailSearchPage().clickRefreshIcon();
			    	    	        special_market_user_driven = catalogTypePage.getUserDrivenCatalogs();
			    	    	        Assertions.assertThat(special_market_user_driven).containsAnyOf(map.get("SMUserDriven"));

			    	    	        //verify quality status
			    	    	        productdetail.selectTabfromDropdown(map.get("Qualitystatustab"));
			    	    	        List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).getRuleNamesByRuleStatus(map.get("Status"));
			    	    	        List<String> myList = new ArrayList<String>(Arrays.asList(map.get("CatalogEntityRule").split(",")));
			    	    	        Assertions.assertThat(catalogentityrule).isEqualTo(myList);
			    	    	        log(LogType.INFO,"Verifying catalog entity rule");

			    	    	        //verify in master
			    	    	        productdetail.clickMenuRefreshIcon();
			    	    	        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog")).enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
			    	    	        log(LogType.INFO, "Navigate to Global attribute tab to verify the Manufacture code in Master Catalog");
			    	    	        productdetail.selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
			    	    	        List<String> masterUserdriven = catalogTypePage.getUserDrivenCatalogs();
			    	    	        Assertions.assertThat(masterUserdriven).containsAnyOf(map.get("userDriven"));

			    	    	        //verifying in child item
			    	    	        productdetail.clickMenuRefreshIcon();
			    	    	        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
			    	    	                .enterHsiItemNumber(referenceObjectNumber).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
			    	    	        log(LogType.INFO, "Navigate to Global Attribute Tab to verify Manufacture Code in Child");

			    	    	        //verifying manufacture code
			    	    	        manufacturecode = globalattribute.getManufacturercode();
			    	    	        Assertions.assertThat(manufacturecode).isEqualTo(map.get("ManufactureCode"));

			    	    	        productdetail.selectTabfromDropdown("AllCatalogsTab");
			    	    	        special_market_catalogs = allCatalogsPage.getAllSpecialMarketsCatalogList();
			    	    	        Assertions.assertThat(special_market_catalogs).doesNotContain(map.get("SMUserDriven"));

			    	    	        //Deleting userdriven in parent
			    	    	        productdetail.clickMenuRefreshIcon();
			    	    	        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
			    	    	                .enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("MedicalTab"));
//			    	    	        catalogTypePage.clearSelectedFields(map.get("userDriven"));
			    	    	        log(LogType.INFO,"Deleting the Userdriven");
			    	    	        pimHomepage.clickLogoutButton();
			    	    	    }

			    	    	    //CATR_209
			    	    	    @PimFrameworkAnnotation(module = Modules.MEDICAL_CATALOG, category = CategoryType.REGRESSION)
			    	    	    @TestDataSheet(sheetname = TestCaseSheet.SubstituteExceptionRule)
			    	    	    @Test(priority = 1, description = "CATR_209 Verification Part | CATR_209 | Verification Of Substitution Exception Rule In Medical Divisions for MPC143", groups = {"US","pim","MEDICAL_CATALOG"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
			    	    	    public void verificationOfSubstitutionExceptionRuleInMedicalCatalogWithChildManuAsMPC143(Map<String, String> map) {
			    	    	        PimHomepage pimHomepage = new LoginPage()
			    	    	                .enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
			    	    	                .enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();

			    	    	        //verifying in parent item in US catalog
			    	    	        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
			    	    	                .enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("ReferenceTab"));
			    	    	        String referenceType = referencesPage.getReferenceType();
			    	    	        Assertions.assertThat(referenceType).isEqualTo(map.get("ReferenceType"));
			    	    	        String referenceObjectNumber = referencesPage.getReferenceObjectNumber();

			    	    	        //assigning medical catalog
			    	    	        productdetail.selectTabfromDropdown(map.get("MedicalTab"));
			    	    	        log(LogType.INFO, "Navigate to Medical Catalog Tab to assign Medical catalog in Parent");
//			    	    	        catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
			    	    	        pimHomepage.productDetailSearchPage().clickRefreshIcon();
			    	    	        medical_user_driven = catalogTypePage.getUserDrivenCatalogs();
			    	    	        Assertions.assertThat(medical_user_driven).containsAnyOf(map.get("userDrivenAssert"));

			    	    	        //verify quality status
			    	    	        productdetail.selectTabfromDropdown(map.get("Qualitystatustab"));
			    	    	        List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).getRuleNamesByRuleStatus(map.get("Status"));
			    	    	        List<String> myList = new ArrayList<String>(Arrays.asList(map.get("CatalogEntityRule").split(",")));
			    	    	        Assertions.assertThat(catalogentityrule).isEqualTo(myList);
			    	    	        log(LogType.INFO,"Verifying catalog entity rule");

			    	    	        //verify in master
			    	    	        productdetail.clickMenuRefreshIcon();
			    	    	        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog")).enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("MedicalTab")).clickRefreshIcon();
			    	    	        log(LogType.INFO, "Navigate to Medical catalog of Master Catalog");
			    	    	        List<String> masterUserdriven = catalogTypePage.getUserDrivenCatalogs();
			    	    	        Assertions.assertThat(masterUserdriven).containsAnyOf(map.get("userDriven"));

			    	    	        //verifying in child item
			    	    	        productdetail.clickMenuRefreshIcon();
			    	    	        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
			    	    	                .enterHsiItemNumber(referenceObjectNumber).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
			    	    	        log(LogType.INFO, "Navigate to Global Attribute Tab to verify Manufacture Code in Child");

			    	    	        //verifying manufacture code
			    	    	        String manufacturecode = globalattribute.getManufacturercode();
			    	    	        Assertions.assertThat(manufacturecode).isEqualTo(map.get("ManufactureCode"));

			    	    	        productdetail.selectTabfromDropdown("TabName");
			    	    	        medical_user_driven = catalogTypePage.getUserDrivenCatalogs();
			    	    	        Assertions.assertThat(medical_user_driven).isEqualTo(map.get("userDrivenAssert"));

			    	    	        //Deleting userdriven in parent
			    	    	        productdetail.clickMenuRefreshIcon();
			    	    	        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
			    	    	                .enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("MedicalTab"));
//			    	    	        catalogTypePage.clearSelectedFields(map.get("userDriven"));
			    	    	        log(LogType.INFO,"Deleting the Userdriven");
			    	    	        pimHomepage.clickLogoutButton();

			    	    	    }
			    	    	    
			    	    	    
			    	    	    //VerifyCatalogRulesTest-----------------------------------------------------------------------------
			    	    	    
			    	    	  //CATR_222, CATR_223, CATR_224
			    	    	    @PimFrameworkAnnotation(module = Modules.AddCatalogs, category = CategoryType.REGRESSION)
			    	    	    @TestDataSheet(sheetname = TestCaseSheet.AddCatalogs)
			    	    	    @Test(priority = 1, description = "CATR_222 Verification Part | CATR_222, CATR_223, CATR_224 | To add and verify dental user driven catalog rules for US", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"US","pim","AddCatalogs"})
			    	    	    public void selectDentalUserDrivenCatalogAndExceptionListForUS(Map<String, String> map) {
			    	    	        PimHomepage pimHomepage = new LoginPage()
			    	    	                .enterUserName(ExcelUtils.getLoginData().get("Dental User").get("UserName"))
			    	    	                .enterPassword(ExcelUtils.getLoginData().get("Dental User").get("Password")).clickLoginButton();

			    	    	        //Search for Multiple Items from Queries
			    	    	        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemTypeInQueriesMenu")).selectCatalogType(map.get("CatalogType"));
			    	    	        List<String> itemCodeList = javautils.readMultipleValuesFromExcel(map.get("ItemNumber"));
			    	    	        for (int i = 0; i <= itemCodeList.size() - 1; i++) {
			    	    	            queriesSubMenu.enterHsiItemNumber(itemCodeList.get(i)).clickSeachButton();
			    	    	        }
			    	    	        basePage.WaitForMiliSec(3000);
			    	    	        basePage.clickAndHoldCtrlKey();
			    	    	        CatalogAssignmentPage.selectMultipleItemCodes();
			    	    	        basePage.releaseCtrlKey();
			    	    	        classificationPage.clickOnExpandButton();
//			    	    	      CatalogAssignmentPage.SelectDentalUserDrivenCatalog(map.get("UserDriven"));
//			    	    	      CatalogAssignmentPage.SelectDentalExceptionList(map.get("ExceptionList"));
			    	    	        productDetailSearchPage.clickMenuRefreshIcon();
			    	    	        classificationPage.clickOnCloseExpandButton();

			    	    	        //Verify catalog user driven and exception list assignment in master
			    	    	        mainmenu.clickRefreshMenuIcon();
			    	    	        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemTypeInQueriesMenu")).selectCatalogType(map.get("CatalogType"))
			    	    	                .enterHsiItemNumber(map.get("ItemNumber1")).clickSeachButton().clickOnFirstResult();
//			    	    	      pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Dental Catalog"));
			    	    	        List<String> userDrivenForDental = CatalogAssignmentPage.getUserDrivenDentalCatalogs();
			    	    	            Assertions.assertThat(userDrivenForDental).containsAnyOf(map.get("UserDriven"));
			    	    	            List<String> ExceptionListForDental = CatalogAssignmentPage.getExceptionListForDentalCatalog();
			    	    	            Assertions.assertThat(ExceptionListForDental).containsAnyOf(map.get("ExceptionList"));
			    	    	            productDetailSearchPage.selectTabfromDropdown(map.get("All Catalogs"));
			    	    	            productDetailSearchPage.clickMenuRefreshIcon();
			    	    	            List<String> allCatalog = CatalogAssignmentPage.getConsolidatedDentalCatalogList();
			    	    	            List<String> myList = new ArrayList<String>(Arrays.asList(map.get("ConsolidatedCatalogs").split(",")));
			    	    	            //Assertions.assertThat(allCatalog).isEqualTo(myList);
			    	    	            Assertions.assertThat(myList).allMatch(e -> allCatalog.contains(e));

			    	    	        pimHomepage.clickLogoutButton();
			    	    	    }

			    	    	    //CATR_222, CATR_223, CATR_224 For CA
			    	    	    @PimFrameworkAnnotation(module = Modules.AddCatalogs, category = CategoryType.REGRESSION)
			    	    	    @TestDataSheet(sheetname = TestCaseSheet.AddCatalogs)
			    	    	    @Test(priority = 1, description = "CATR_222 Verification Part | CATR_222_CA, CATR_223, CATR_224 For CA| To add and verify dental user driven catalog rules for CA", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"CA","pim","AddCatalogs"})
			    	    	    public void selectDentalUserDrivenCatalogAndExceptionListForCA(Map<String, String> map) {
			    	    	        PimHomepage pimHomepage = new LoginPage()
			    	    	                .enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
			    	    	                .enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();

			    	    	        //Search for Multiple Items from Queries
			    	    	        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemTypeInQueriesMenu")).selectCatalogType(map.get("CatalogType"));
			    	    	        List<String> itemCodeList = javautils.readMultipleValuesFromExcel(map.get("ItemNumber"));
			    	    	        for (int i = 0; i <= itemCodeList.size() - 1; i++) {
			    	    	            queriesSubMenu.enterHsiItemNumber(itemCodeList.get(i)).clickSeachButton();
			    	    	        }
			    	    	        basePage.WaitForMiliSec(3000);
			    	    	        basePage.clickAndHoldCtrlKey();
			    	    	        CatalogAssignmentPage.selectMultipleItemCodes();
			    	    	        basePage.releaseCtrlKey();
			    	    	        classificationPage.clickOnExpandButton();
//			    	    	      CatalogAssignmentPage.SelectDentalUserDrivenCatalog(map.get("UserDriven"));
//			    	    	      CatalogAssignmentPage.SelectDentalExceptionList(map.get("ExceptionList"));
			    	    	        productDetailSearchPage.clickMenuRefreshIcon();
			    	    	        classificationPage.clickOnCloseExpandButton();

			    	    	        //Verify catalog user driven and exception list assignment in master
			    	    	        productDetailSearchPage.clickMenuRefreshIcon();
			    	    	        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemTypeInQueriesMenu")).selectCatalogType(map.get("CatalogType"))
			    	    	                .enterHsiItemNumber(map.get("ItemNumber1")).clickSeachButton().clickOnFirstResult();
			    	    	        pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Dental Catalog"));
			    	    	        List<String> userDrivenForDental = CatalogAssignmentPage.getUserDrivenDentalCatalogs();
			    	    	        Assertions.assertThat(userDrivenForDental).containsAnyOf(map.get("UserDriven"));
			    	    	        List<String> ExceptionListForDental = CatalogAssignmentPage.getExceptionListForDentalCatalog();
			    	    	        Assertions.assertThat(ExceptionListForDental).containsAnyOf(map.get("ExceptionList"));
			    	    	        productDetailSearchPage.selectTabfromDropdown(map.get("All Catalogs"));
			    	    	        productDetailSearchPage.clickMenuRefreshIcon();
			    	    	        List<String> allCatalog = CatalogAssignmentPage.getConsolidatedDentalCatalogList();
			    	    	        List<String> myList = new ArrayList<String>(Arrays.asList(map.get("ConsolidatedCatalogs").split(",")));
			    	    	        Assertions.assertThat(allCatalog).isEqualTo(myList);

			    	    	        pimHomepage.clickLogoutButton();
			    	    	    }


			    	    	    //CATR_226
			    	    	    @PimFrameworkAnnotation(module = Modules.AddCatalogs, category = CategoryType.REGRESSION)
			    	    	    @TestDataSheet(sheetname = TestCaseSheet.AddCatalogs)
			    	    	    @Test(priority = 1, description = "CATR_226 Verification Part | CATR_226_CA | verify tabs namely CanadianZahnCatalogs,CanadianDentalCatalogs and CanadianMedicalCatalogTab for CA english user", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"CA","pim","AddCatalogs"})
			    	    	    public void verifyAllDivisionTabsForCA_EnglishUser(Map<String, String> map) {
			    	    	        PimHomepage pimHomepage = new LoginPage()
			    	    	                .enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
			    	    	                .enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
			    	    	        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemTypeInQueriesMenu")).selectCatalogType(map.get("CatalogType"))
			    	    	                .enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult();
			    	    	        pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Canadian Zahn Catalogs"));
			    	    	        verifyAllDivisionsTabForCA_EnglishUser();
			    	    	        pimHomepage.clickLogoutButton();
			    	    	    }
			    	    	    public void verifyAllDivisionsTabForCA_EnglishUser() {
			    	    	        Assertions.assertThat(CatalogAssignmentPage.isAllAttributesVisible("ZahnCatalogCanadianTab")).isTrue();
			    	    	        Assertions.assertThat(CatalogAssignmentPage.isAllAttributesVisible("MedicalCatalogCanadianTab")).isTrue();
			    	    	        Assertions.assertThat(CatalogAssignmentPage.isAllAttributesVisible("DentalCatalogCanadianTab")).isTrue();
			    	    	    }

			    	    	    //CATR_225, CATR_227, CATR_228, CATR_229
			    	    	    @PimFrameworkAnnotation(module = Modules.AddCatalogs, category = CategoryType.REGRESSION)
			    	    	    @TestDataSheet(sheetname = TestCaseSheet.AddCatalogs)
			    	    	    @Test(priority = 1, description = "CATR_225 Verification Part | CATR_225_CA, CATR_227, CATR_228, CATR_229 | Verify user driven and exception catalog list for Dental, Medical and Zahn canadian catalogs", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"CA","pim","AddCatalogs"})
			    	    	    public void VerifyUserDrivenCatalogAndExceptionalCatalogList(Map<String, String> map) {
			    	    	        PimHomepage pimHomepage = new LoginPage()
			    	    	                .enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
			    	    	                .enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
			    	    	        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemTypeInQueriesMenu")).selectCatalogType(map.get("CatalogType"))
			    	    	                .enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult();

			    	    	        //Verify User Driven and Exception List for Canadian Dental Catalog
			    	    	        productDetailSearchPage.selectTabfromDropdown(map.get("Canadian Dental Catalogs"));
			    	    	        List<String> allCatalogForUserDrivenDental =  CatalogAssignmentPage.clearAndVerifyUserDrivenList();
			    	    	        List<String> myListUDDental = new ArrayList<String>(Arrays.asList(map.get("UserDrivenDental").split(",")));
			    	    	        Assertions.assertThat(allCatalogForUserDrivenDental).isEqualTo(myListUDDental);
			    	    	        classificationPage.clickOnExpandButton();
			    	    	        List<String> allCatalogForExceptionListDental =CatalogAssignmentPage.clearAndVerifyExceptionList();
			    	    	        List<String> myListELDental = new ArrayList<String>(Arrays.asList(map.get("ExceptionListDental").split(",")));
			    	    	        Assertions.assertThat(allCatalogForExceptionListDental).isEqualTo(myListELDental);
			    	    	        classificationPage.clickOnCloseExpandButton();

			    	    	        //Verify User Driven and Exception List for Canadian Medical Catalog
			    	    	        productDetailSearchPage.selectTabfromDropdown(map.get("Canadian Medical Catalogs"));
			    	    	        List<String> allCatalogForUserDrivenMedical =  CatalogAssignmentPage.clearAndVerifyUserDrivenList();
			    	    	        List<String> myListUDMedical = new ArrayList<String>(Arrays.asList(map.get("UserDrivenMedical").split(",")));
			    	    	        Assertions.assertThat(allCatalogForUserDrivenMedical).isEqualTo(myListUDMedical);
			    	    	        classificationPage.clickOnExpandButton();
			    	    	        List<String> allCatalogForExceptionListMedical =CatalogAssignmentPage.clearAndVerifyExceptionList();
			    	    	        List<String> myListELMedical = new ArrayList<String>(Arrays.asList(map.get("ExceptionListMedical").split(",")));
			    	    	        Assertions.assertThat(allCatalogForExceptionListMedical).isEqualTo(myListELMedical);
			    	    	        classificationPage.clickOnCloseExpandButton();

			    	    	        //Verify User Driven and Exception List for Canadian Zahn Catalog
			    	    	        productDetailSearchPage.selectTabfromDropdown(map.get("Canadian Zahn Catalogs"));
			    	    	        List<String> allCatalogForUserDrivenZahn =  CatalogAssignmentPage.clearAndVerifyUserDrivenList();
			    	    	        List<String> myListUDZahn = new ArrayList<String>(Arrays.asList(map.get("UserDrivenZahn").split(",")));
			    	    	        Assertions.assertThat(allCatalogForUserDrivenZahn).isEqualTo(myListUDZahn);
			    	    	        classificationPage.clickOnExpandButton();
			    	    	        List<String> allCatalogForExceptionListZahn =CatalogAssignmentPage.clearAndVerifyExceptionList();
			    	    	        List<String> myListELZahn = new ArrayList<String>(Arrays.asList(map.get("ExceptionListZahn").split(",")));
			    	    	        Assertions.assertThat(allCatalogForExceptionListZahn).isEqualTo(myListELZahn);
			    	    	        classificationPage.clickOnCloseExpandButton();
			    	    	        pimHomepage.clickLogoutButton();
			    	    	    }

			    	    	    //CATR_230
			    	    	    @PimFrameworkAnnotation(module = Modules.AddCatalogs, category = CategoryType.REGRESSION)
			    	    	    @TestDataSheet(sheetname = TestCaseSheet.AddCatalogs)
			    	    	    @Test(priority = 1, description = "CATR_230 Verification Part | CATR_230_CA | Verify Catalog not part of medical division should not be present as part of user driven/exceptional catalog list", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"CA","pim","AddCatalogs"})
			    	    	    public void VerifyCatalogNotPartOfMedicalShouldNotBePresent(Map<String, String> map) {
			    	    	        PimHomepage pimHomepage = new LoginPage()
			    	    	                .enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
			    	    	                .enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
			    	    	        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemTypeInQueriesMenu")).selectCatalogType(map.get("CatalogType"))
			    	    	                .enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult();
//			    	    	      productDetailSearchPage.selectTabfromDropdown(map.get("Canadian Medical Catalogs"));
			    	    	        List<String> userDrivenForDental = CatalogAssignmentPage.getUserDrivenDentalCatalogs();
			    	    	        Assertions.assertThat(userDrivenForDental).doesNotContain(map.get("UserDriven"));
			    	    	        log(LogType.INFO,"Medical User Driven values does not contain Dental Values");
			    	    	        List<String> ExceptionListForDental = CatalogAssignmentPage.getExceptionListForDentalCatalog();
			    	    	        Assertions.assertThat(ExceptionListForDental).doesNotContain(map.get("ExceptionList"));
			    	    	        log(LogType.INFO,"Medical Exception List values does not contain Dental Values");
			    	    	        pimHomepage.clickLogoutButton();
			    	    	    }
			    	    	    
			    	    	    //ZahnCatalogSubstitutionRulesTest----------------------------------------------------------------------
			    	    	    
			    	    	  //CATR_148
			    	    		@PimFrameworkAnnotation(module = Modules.SubstituteCatalogRule, category = CategoryType.Data_Issue)
			    	    		@TestDataSheet(sheetname = TestCaseSheet.Catalog)
			    	    		@Test(priority = 1, description = "CATR_148 Verification Part | CATR_148 | Verification Of Substitution Rules In Zahn Divisions With Sub Type As S/SA/GE",groups = {"US","pim","ZAHNSubstituteCatalogRule"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)

			    	    		public void VerificationOfSubstitutionRulesInZahnCatalogWithSubTypeAsSA(Map<String, String> map)   

			    	    		{

			    	    			PimHomepage pimHomepage = new LoginPage()
			    	    					.enterUserName(ExcelUtils.getLoginData().get("Zahn User").get("UserName"))
			    	    					.enterPassword(ExcelUtils.getLoginData().get("Zahn User").get("Password")).clickLoginButton();

			    	    			pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
			    	    			.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("ReferenceTab"));
			    	    			String referenceType = referencesPage.getReferenceTypeAsSA();
			    	    			Assertions.assertThat(referenceType).isEqualTo(map.get("ReferenceType"));
			    	    			String referenceObjectNumber = referencesPage.getReferenceObjectNumber();
			    	    			productDetailSearchPage.selectTabfromDropdown(map.get("TabName"));

//			    	    			catalogTypePage.clearUserDriven(map.get("userDriven"));
			    	    			BasePage.WaitForMiliSec(3000);
			    	    			log(LogType.INFO, "Navigate to Zahn Catalog Tab to assign Zahn catalog in Parent");

			    	    			String userDrivernCatalog =map.get("userDriven");
//			    	    			catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
//			    	    			pimHomepage.productDetailSearchPage().clickRefreshIcon();
//			    	    			user_driven = catalogTypePage.getUserDrivenCatalogs();
//			    	    			Assertions.assertThat(user_driven).containsAnyOf(userDrivernCatalog);
			    	    			//verify quality status 
			    	    			pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
			    	    			List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus"))
			    	    					.maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
			    	    			for (String Rules:javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule")))
			    	    			{
			    	    				Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);	
			    	    			}
			    	    			qualitystatus.minimizeQualityStatusTab();
			    	    			log(LogType.INFO,"Verifying catalog entity rule");
			    	    			//Verify in master catalog
			    	    			pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog"))
			    	    			.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
			    	    			List<String> masterUserdriven = catalogTypePage.getUserDrivenCatalogs();
			    	    			Assertions.assertThat(user_driven).containsAll(masterUserdriven);
			    	    			log(LogType.INFO, "Verifying Catalog in Master");

			    	    			// Verifying Catalogs in Child item
			    	    			pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType"))
			    	    			.removeAndEnterHsiItemNumber(referenceObjectNumber).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("AllCatalogtab"));
			    	    			all_catalog_Zahn =allCatalogsPage.getAllZahnCatalogList();
			    	    			Assertions.assertThat(all_catalog_Zahn).doesNotContain(map.get("userDriven"));
			    	    			log(LogType.INFO, "Verifying Catalogs in Child Item");
			    	    		}
			    	    		//CATR_154
			    	    		@PimFrameworkAnnotation(module = Modules.SubstituteCatalogRule, category = CategoryType.Data_Issue)
			    	    		@TestDataSheet(sheetname = TestCaseSheet.Catalog)
			    	    		@Test(priority = 1, description = "CATR_154 Verification Part | CATR_154 | Verification Of Substitution Rules In Zahn Divisions With Sub Type As SP",groups = {"US","pim","ZAHNSubstituteCatalogRule"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
			    	    		public void VerificationOfSubstitutionRulesForZahnDivisionWithSubTypeAsSP(Map<String, String> map)   
			    	    		{

			    	    			PimHomepage pimHomepage = new LoginPage()
			    	    					.enterUserName(ExcelUtils.getLoginData().get("Zahn User").get("UserName"))
			    	    					.enterPassword(ExcelUtils.getLoginData().get("Zahn User").get("Password")).clickLoginButton();

			    	    			pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
			    	    			.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("ReferenceTab"));
			    	    			String referenceType = referencesPage.getReferenceTypeAsSP();
			    	    			Assertions.assertThat(referenceType).isEqualTo(map.get("ReferenceType"));
			    	    			String referenceObjectNumber = referencesPage.getReferenceObjectNumber();
			    	    			productDetailSearchPage.selectTabfromDropdown(map.get("TabName"));

//			    	    			catalogTypePage.clearUserDriven(map.get("userDriven"));
			    	    			BasePage.WaitForMiliSec(3000);
			    	    			log(LogType.INFO, "Navigate to Zahn Catalog Tab to assign Zahn catalog in Parent");

			    	    			String userDrivernCatalog =map.get("userDriven");
//			    	    			catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
			    	    			pimHomepage.productDetailSearchPage().clickRefreshIcon();
			    	    			user_driven = catalogTypePage.getUserDrivenCatalogs();
			    	    			Assertions.assertThat(user_driven).containsAnyOf(userDrivernCatalog);

			    	    			//verify quality status 
			    	    			pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
			    	    			List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus"))
			    	    					.maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
			    	    			for (String Rules:javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule")))
			    	    			{
			    	    				Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);	
			    	    			}
			    	    			qualitystatus.minimizeQualityStatusTab();
			    	    			log(LogType.INFO,"Verifying catalog entity rule");

			    	    			//Verify in master catalog
			    	    			pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog"))
			    	    			.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
			    	    			List<String> masterUserdriven = catalogTypePage.getUserDrivenCatalogs();
			    	    			Assertions.assertThat(user_driven).containsAll(masterUserdriven);
			    	    			log(LogType.INFO, "Verifying Catalog in Master");

			    	    			// Verifying Catalogs in Child item
			    	    			pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType"))
			    	    			.removeAndEnterHsiItemNumber(referenceObjectNumber).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("AllCatalogtab"));
			    	    			all_catalog_Medical = allCatalogsPage.getAllMedicalCatalogList();
			    	    			Assertions.assertThat(all_catalog_Medical).contains(map.get("medicalCatalog"));
			    	    			all_catalog_SM =allCatalogsPage.getAllSpecialMarketsCatalogList(); 
			    	    			Assertions.assertThat(all_catalog_SM).contains(map.get("specialMarketCatalog"));
			    	    			log(LogType.INFO, "Verifying Catalogs in Child Item");

			    	    			//Verify Dental and Zahn Catalog should not be populated
			    	    			all_catalog_Dental =allCatalogsPage.getAllDentalCatalogList();
			    	    			Assert.assertNotEquals(all_catalog_Dental, userDrivernCatalog);
			    	    			log(LogType.INFO, "Verifying Catalogs which are not populated in Child Item");
			    	    		}

			    	    		//CATR_155
			    	    		//Verify the Catalog list of the parent item does not gets assigned to the child Items in the respective tabs (Medical/Special Market Web Catalog Assignment), based on the Auto Sub Flag value is "N" in the Catalog_Master dictionary
			    	    		@PimFrameworkAnnotation(module = Modules.SubstituteCatalogRule, category = CategoryType.Data_Issue)
			    	    		@TestDataSheet(sheetname = TestCaseSheet.Catalog)
			    	    		@Test(priority = 1, description = "CATR_155 Verification Part | CATR_155 | Verify Substitute Of Type SP For Dental Division Item Does Not Populates With The Same Catalog List Of Parent Item", groups = {"US","pim","ZAHNSubstituteCatalogRule"} ,dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
			    	    		public void VerifySubstituteOfTypeSPForAnyDivisionItemDoesNotPopulatesWithTheSameCatalogListOfParentItem(Map<String, String> map)   
			    	    		{
			    	    			PimHomepage pimHomepage = new LoginPage()
			    	    					.enterUserName(ExcelUtils.getLoginData().get("Dental User").get("UserName"))
			    	    					.enterPassword(ExcelUtils.getLoginData().get("Dental User").get("Password")).clickLoginButton();

			    	    			pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
			    	    			.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("ReferenceTab"));
			    	    			List<String> referenceType = referencesPage.getReferenceTypeInList();
			    	    			Assertions.assertThat(referenceType).contains(map.get("ReferenceType"));
			    	    			String referenceObjectNumber = referencesPage.getReferenceObjectNumber();
			    	    			productDetailSearchPage.selectTabfromDropdown(map.get("TabName"));

//			    	    			catalogTypePage.clearUserDriven(map.get("userDriven"));
			    	    			BasePage.WaitForMiliSec(3000);
			    	    			log(LogType.INFO, "Navigate to Dental Catalog Tab to assign Dental catalog in Parent");

			    	    			String userDrivernCatalog =map.get("userDriven");
//			    	    			catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
			    	    			pimHomepage.productDetailSearchPage().clickRefreshIcon();
			    	    			user_driven = catalogTypePage.getUserDrivenCatalogs();
			    	    			Assertions.assertThat(user_driven).containsAnyOf(userDrivernCatalog);

			    	    			//verify quality status 
			    	    			pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
			    	    			List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus"))
			    	    					.maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
			    	    			for (String Rules:javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule")))
			    	    			{
			    	    				Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);	
			    	    			}
			    	    			qualitystatus.minimizeQualityStatusTab();
			    	    			log(LogType.INFO,"Verifying catalog entity rule");
			    	    			//Verify in master catalog
			    	    			pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog"))
			    	    			.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
			    	    			List<String> masterUserdriven = catalogTypePage.getUserDrivenCatalogs();
			    	    			Assertions.assertThat(user_driven).containsAll(masterUserdriven);
			    	    			log(LogType.INFO, "Verifying Dental Catalog in Master");

			    	    			// Verifying in child item
			    	    			pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType"))
			    	    			.removeAndEnterHsiItemNumber(referenceObjectNumber).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("AllCatalogtab"));
			    	    			all_catalog_Dental =allCatalogsPage.getAllDentalCatalogList();
			    	    			Assertions.assertThat(all_catalog_Dental).contains(map.get("userDriven"));	
			    	    			log(LogType.INFO, "Verifying Catalogs in Child Item should not get assign");
			    	    		}

			    	    		//CATR_158
			    	    		@PimFrameworkAnnotation(module = Modules.SubstituteCatalogRule, category = CategoryType.REGRESSION)
			    	    		@TestDataSheet(sheetname = TestCaseSheet.Catalog)
			    	    		@Test(priority = 1, description = "CATR_158 Verification Part | CATR_158 | Verification Of Substitution Rules In Zahn Divisions With Sub Type As PA",groups = {"US","pim","ZAHNSubstituteCatalogRule"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
			    	    		public void VerificationOfSubstitutionRulesForZahnDivisionWithSubTypeAsPA(Map<String, String> map) {

			    	    			PimHomepage pimHomepage = new LoginPage()
			    	    					.enterUserName(ExcelUtils.getLoginData().get("Zahn User").get("UserName"))
			    	    					.enterPassword(ExcelUtils.getLoginData().get("Zahn User").get("Password")).clickLoginButton();

			    	    			pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
			    	    			.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
			    	    			.clickOnFirstResult().selectTabfromDropdown(map.get("ReferenceTab"));
			    	    			String referenceType = referencesPage.getReferenceType();
			    	    			Assertions.assertThat(referenceType).isEqualTo(map.get("ReferenceType"));
			    	    			String referenceObjectNumber = referencesPage.getReferenceObjectNumber();
			    	    			productDetailSearchPage.selectTabfromDropdown(map.get("TabName"));

//			    	    			catalogTypePage.clearUserDriven(map.get("userDriven"));
			    	    			BasePage.WaitForMiliSec(3000);
			    	    			log(LogType.INFO, "Navigate to Zahn Catalog Tab to assign Zahn catalog in Parent");

			    	    			String userDrivernCatalog = map.get("userDriven");
//			    	    			catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
			    	    			pimHomepage.productDetailSearchPage().clickRefreshIcon();
			    	    			user_driven = catalogTypePage.getUserDrivenCatalogs();
			    	    			Assertions.assertThat(user_driven).containsAnyOf(userDrivernCatalog);

			    	    			//verify quality status 
			    	    			pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
			    	    			List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus"))
			    	    					.maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
			    	    			for (String Rules:javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule")))
			    	    			{
			    	    				Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);	
			    	    			}
			    	    			qualitystatus.minimizeQualityStatusTab();
			    	    			log(LogType.INFO,"Verifying catalog entity rule");

			    	    			// Verify in master catalog
			    	    			pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
			    	    			.clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
			    	    			List<String> masterUserdriven = catalogTypePage.getUserDrivenCatalogs();
			    	    			Assertions.assertThat(user_driven).containsAll(masterUserdriven);
			    	    			log(LogType.INFO, "Verifying Catalog in master");

			    	    			// Verifying in Child item
			    	    			pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType"))
			    	    			.removeAndEnterHsiItemNumber(referenceObjectNumber).clickSeachButton().clickOnFirstResult()
			    	    			.selectTabfromDropdown(map.get("AllCatalogtab"));
			    	    			all_catalog_Dental = allCatalogsPage.getAllDentalCatalogList();
			    	    			Assertions.assertThat(all_catalog_Dental).contains(map.get("userDriven"));
			    	    			all_catalog_Zahn = allCatalogsPage.getAllZahnCatalogList();
			    	    			Assertions.assertThat(all_catalog_Zahn).contains(map.get("userDriven"));
			    	    			all_catalog_SM =allCatalogsPage.getAllSpecialMarketsCatalogList(); 
			    	    			Assertions.assertThat(all_catalog_SM).contains(map.get("userDriven"));
			    	    			log(LogType.INFO, "Verifying Catalogs in Child Item");

			    	    			//Verify In Medical Catalog should not be populated for child item
			    	    			all_catalog_Medical =allCatalogsPage.getAllMedicalCatalogList();
			    	    			Assert.assertNotEquals(all_catalog_Medical, userDrivernCatalog);
			    	    			log(LogType.INFO, "Verifying Catalogs which are not populated in Child Item");
			    	    		}
			    	    		//CATR_163
			    	    		@PimFrameworkAnnotation(module = Modules.SubstituteCatalogRule, category = CategoryType.Data_Issue)
			    	    		@TestDataSheet(sheetname = TestCaseSheet.Catalog)
			    	    		@Test(priority = 1, description = "CATR_163 Verification Part | CATR_163 | Verification Of Substitution Rules In Zahn Divisions With Sub Type As A3",groups = {"US","pim","ZAHNSubstituteCatalogRule"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)

			    	    		public void VerificationOfSubstitutionRulesInZahnDivisionsWithSubTypeAsA3(Map<String, String> map) {

			    	    			PimHomepage pimHomepage = new LoginPage()
			    	    					.enterUserName(ExcelUtils.getLoginData().get("Zahn User").get("UserName"))
			    	    					.enterPassword(ExcelUtils.getLoginData().get("Zahn User").get("Password")).clickLoginButton();

			    	    			pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
			    	    			.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
			    	    			.clickOnFirstResult().selectTabfromDropdown(map.get("ReferenceTab"));
			    	    			String referenceType = referencesPage.getReferenceType();
			    	    			Assertions.assertThat(referenceType).isEqualTo(map.get("ReferenceType"));
			    	    			String referenceObjectNumber = referencesPage.getReferenceObjectNumber();
			    	    			productDetailSearchPage.selectTabfromDropdown(map.get("TabName"));

//			    	    			catalogTypePage.clearUserDriven(map.get("userDriven"));
			    	    			BasePage.WaitForMiliSec(3000);
			    	    			log(LogType.INFO, "Navigate to Zahn Catalog Tab to assign Zahn catalog in Parent");

			    	    			String userDrivernCatalog = map.get("userDriven");
//			    	    			catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
			    	    			pimHomepage.productDetailSearchPage().clickRefreshIcon();
			    	    			user_driven = catalogTypePage.getUserDrivenCatalogs();
			    	    			Assertions.assertThat(user_driven).containsAnyOf(userDrivernCatalog);

			    	    			//verify quality status 
			    	    			pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
			    	    			List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus"))
			    	    					.maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
			    	    			for (String Rules:javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule")))
			    	    			{
			    	    				Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);	
			    	    			}
			    	    			qualitystatus.minimizeQualityStatusTab();
			    	    			log(LogType.INFO,"Verifying catalog entity rule");
			    	    			// Verify in master catalog
			    	    			pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
			    	    			.clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
			    	    			List<String> masterUserdriven = catalogTypePage.getUserDrivenCatalogs();
			    	    			Assertions.assertThat(user_driven).containsAll(masterUserdriven);
			    	    			log(LogType.INFO, "Verifying Zahn Catalog in Master");

			    	    			// Verifying in Child item
			    	    			pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType"))
			    	    			.removeAndEnterHsiItemNumber(referenceObjectNumber).clickSeachButton().clickOnFirstResult()
			    	    			.selectTabfromDropdown(map.get("AllCatalogtab"));
			    	    			all_catalog_Zahn = allCatalogsPage.getAllZahnCatalogList();
			    	    			Assert.assertNotEquals(all_catalog_Zahn, userDrivernCatalog);
			    	    			log(LogType.INFO, "Verifying Catalog in Child Item should not get assign");
			    	    		}

			    	    		
			    	    		//ZahnCatalogTest----------------------------------------------------------------------	  
			    	    		
			    	    		// CATR_088
			    	    		@PimFrameworkAnnotation(module = Modules.ZAHN_CATALOG, category = CategoryType.REGRESSION)
			    	    		@TestDataSheet(sheetname = TestCaseSheet.Catalog)
			    	    		@Test(priority = 1, description = "CATR_088 Verification Part | CATR_088 | Verifying Rule Driven catalog for Zahn Division in US, CA and Master Catalog", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			    	    				"US", "pim","ZAHN_CATALOG" })
			    	    		public void verify_rule_driven_for_zahn_division(Map<String, String> map) throws InterruptedException, IOException {

			    	    			PimHomepage pimHomepage = new LoginPage()
			    	    					.enterUserName(ExcelUtils.getLoginData().get("Zahn User").get("UserName"))
			    	    					.enterPassword(ExcelUtils.getLoginData().get("Zahn User").get("Password")).clickLoginButton();
			    	    			pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
			    	    					.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
			    	    					.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));

			    	    			catalogTypePage.clearUserDriven(map.get("userDriven"));
			    	    			BasePage.WaitForMiliSec(3000);
//			    	    			catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
			    	    			pimHomepage.productDetailSearchPage().clickRefreshIcon();
			    	    			List<String> rule_driven = catalogTypePage.getRuleDrivenCatalogs();
			    	    			Assertions.assertThat(rule_driven).containsAnyOf(map.get("RuleDriven"));

			    	    			// verify quality status
			    	    			pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
			    	    			List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus"))
			    	    					.maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
			    	    			for (String Rules : javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule"))) {
			    	    				Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);
			    	    			}
			    	    			qualitystatus.minimizeQualityStatusTab();
			    	    			log(LogType.INFO, "Verifying catalog entity rule");

			    	    			pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
			    	    					.clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
			    	    			List<String> masterRuledriven = catalogTypePage.getRuleDrivenCatalogs();
			    	    			Assertions.assertThat(rule_driven).containsAll(masterRuledriven);
			    	    			pimHomepage.clickLogoutButton();
			    	    		}

			    	    		// CATR_089
			    	    		@PimFrameworkAnnotation(module = Modules.ZAHN_CATALOG, category = CategoryType.REGRESSION)
			    	    		@TestDataSheet(sheetname = TestCaseSheet.Catalog)
			    	    		@Test(priority = 1, description = "CATR_089 Verification Part | CATR_089_CA | Verifying Rule Driven catalog for Zahn Division in US, CA and Master Catalog", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			    	    				"CA", "pim","ZAHN_CATALOG" })
			    	    		public void verify_rule_driven_for_zahn_division_CA(Map<String, String> map)
			    	    				throws InterruptedException, IOException {

			    	    			PimHomepage pimHomepage = new LoginPage()
			    	    					.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
			    	    					.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
			    	    			pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
			    	    					.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
			    	    					.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));

			    	    			List<String> userdrivencatalogs = catalogTypePage.getUserDrivenCatalogs();
//			    	    			List<String> multiplevaluesfromexcel = javautils.readMultipleValuesFromExcel(map.get("userDriven"));
//			    	    			for (String userdriven : multiplevaluesfromexcel) {
//			    	    				if (userdrivencatalogs.contains(userdriven)) {
//			    	    					// delete userdriven
//			    	    					catalogTypePage.clearSelectedFields(userdriven);
//			    	    					productdetailspage.clickRefreshIcon();
//			    	    					log(LogType.INFO, "Deleting the Userdriven");
//			    	    				}
//			    	    			}
			    	    			BasePage.WaitForMiliSec(3000);
//			    	    			catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
			    	    			pimHomepage.productDetailSearchPage().clickRefreshIcon();
			    	    			List<String> rule_driven = catalogTypePage.getRuleDrivenCatalogs();
			    	    			Assertions.assertThat(rule_driven).containsAnyOf(map.get("RuleDriven"));

			    	    			// verify quality status
			    	    			pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
			    	    			List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus"))
			    	    					.maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
			    	    			for (String Rules : javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule"))) {
			    	    				Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);
			    	    			}
			    	    			qualitystatus.minimizeQualityStatusTab();
			    	    			log(LogType.INFO, "Verifying catalog entity rule");

			    	    			pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
			    	    					.clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
			    	    			List<String> masterRuledriven = catalogTypePage.getRuleDrivenCatalogs();
			    	    			Assertions.assertThat(rule_driven).containsAll(masterRuledriven);
			    	    			pimHomepage.clickLogoutButton();
			    	    		}

			    	    		// CATR_183, CATR_184
			    	    		@PimFrameworkAnnotation(module = Modules.ZAHN_CATALOG, category = CategoryType.REGRESSION)
			    	    		@TestDataSheet(sheetname = TestCaseSheet.Catalog)
			    	    		@Test(priority = 1, description = "CATR_183, Verification Part | CATR_183, CATR_184 For US | Verify All catalog tabs and exception lists for ZAHN Catalog list populates the catalogs with User, Rule Driven and excluding the Catalogs selected as Exception for US", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			    	    				"US", "pim","ZAHN_CATALOG" })
			    	    		public void verify_all_catalog_tabs_and_exception_list_for_zahn_division(Map<String, String> map) throws Throwable {
			    	    			PimHomepage pimHomepage = new LoginPage()
			    	    					.enterUserName(ExcelUtils.getLoginData().get("Zahn User").get("UserName"))
			    	    					.enterPassword(ExcelUtils.getLoginData().get("Zahn User").get("Password")).clickLoginButton();
			    	    			pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
			    	    					.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
			    	    					.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));

			    	    			List<String> user_driven = catalogTypePage.getUserDrivenCatalogs();
			    	    			List<String> rule_driven = catalogTypePage.getRuleDrivenCatalogs();
			    	    			List<String> user_and_rule_driven = new ArrayList<>();
//			    	    			user_and_rule_driven.addAll(rule_driven);
//			    	    			user_and_rule_driven.addAll(user_driven); // took user and rule driven
			    	    			pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("AllCatalogtab")).clickRefreshIcon();
			    	    			List<String> all_zahn_catalog = allCatalogsPage.getAllZahnCatalogList();
			    	    			Assertions.assertThat(user_and_rule_driven).containsAll(all_zahn_catalog);// verified all catalog without
			    	    																						// exception

			    	    			pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
			    	    					.clickOnFirstResult().selectTabfromDropdown(map.get("AllCatalogtab")).clickRefreshIcon();
			    	    			List<String> master_all_zahn_catalog = allCatalogsPage.getAllZahnCatalogList();
			    	    			// verified all catalog tab in master
			    	    			Assertions.assertThat(user_and_rule_driven).containsAll(master_all_zahn_catalog);

			    	    			// verifying with exception list
			    	    			pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType")).clickSeachButton()
			    	    					.clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();

			    	    			List<String> userdrivencatalogs = catalogTypePage.getExceptionListCatalogs();
//			    	    			List<String> multiplevaluesfromexcel = javautils.readMultipleValuesFromExcel(map.get("userDriven"));
//			    	    			for (String userdriven : multiplevaluesfromexcel) {
//			    	    				if (userdrivencatalogs.contains(userdriven)) {
//			    	    					// delete userdriven
//			    	    					catalogTypePage.clearExceptionListField(userdriven);
//			    	    					productdetailspage.clickRefreshIcon();
//			    	    					log(LogType.INFO, "Deleting the Userdriven");
//			    	    				}
//			    	    			}
			    	    			BasePage.WaitForMiliSec(3000);
			    	    			catalogTypePage.clickExceptionListField().selectExceptionCatalogsFromList(map.get("userDriven"));
			    	    			pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("AllCatalogtab")).clickRefreshIcon();

			    	    			List<String> all_catalog_except_exceptionallist = allCatalogsPage.getAllZahnCatalogList();
			    	    			Assertions.assertThat(all_catalog_except_exceptionallist).isNotEqualTo(user_and_rule_driven);
			    	    			// Assert.assertNotEquals(all_catalog_except_exceptionallist,
			    	    			// user_and_rule_driven);

			    	    			// verifying with exception list in master catalog
			    	    			pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
			    	    					.clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon()
			    	    					.selectTabfromDropdown(map.get("AllCatalogtab"));
			    	    			List<String> master_exception_alldental__catalog = allCatalogsPage.getAllZahnCatalogList();
			    	    			// Assert.assertNotEquals(master_exception_all_catalog, user_and_rule_driven);
			    	    			// Assert.assertEquals(all_catalog_except_exceptionallist,
			    	    			// master_exception_all_catalog);
			    	    			Assertions.assertThat(user_and_rule_driven).isNotEqualTo(master_exception_alldental__catalog);
			    	    			Assertions.assertThat(all_catalog_except_exceptionallist).containsAll(master_exception_alldental__catalog);
			    	    			pimHomepage.clickLogoutButton();
			    	    		}

			    	    		// CATR_183, 184 for CA
			    	    		@PimFrameworkAnnotation(module = Modules.ZAHN_CATALOG, category = CategoryType.REGRESSION)
			    	    		@TestDataSheet(sheetname = TestCaseSheet.Catalog)
			    	    		@Test(priority = 1, description = "CATR_183 Verification Part | CATR_183_CA, CATR_183 For CA | Verify All catalog tabs and exception lists for ZAHN Catalog list populates the catalogs with User, Rule Driven and excluding the Catalogs selected as Exception for CA", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			    	    				"CA", "pim","ZAHN_CATALOG" })
			    	    		public void verify_all_catalog_tabs_and_exception_list_for_zahn_division_CA(Map<String, String> map) {
			    	    			PimHomepage pimHomepage = new LoginPage()
			    	    					.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
			    	    					.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
			    	    			pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
			    	    					.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
			    	    					.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));

			    	    			List<String> user_driven = catalogTypePage.getUserDrivenCatalogs();
			    	    			List<String> rule_driven = catalogTypePage.getRuleDrivenCatalogs();
			    	    			List<String> user_and_rule_driven = new ArrayList<>();
//			    	    			user_and_rule_driven.addAll(rule_driven);
//			    	    			user_and_rule_driven.addAll(user_driven); // took user and rule driven
			    	    			pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("AllCatalogtab")).clickRefreshIcon();
			    	    			List<String> all_zahn_catalog = allCatalogsPage.getAllZahnCatalogList();
			    	    			Assertions.assertThat(user_and_rule_driven).containsAll(all_zahn_catalog);// verified all catalog without
			    	    																						// exception

			    	    			pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
			    	    					.clickOnFirstResult().selectTabfromDropdown(map.get("AllCatalogtab")).clickRefreshIcon();
			    	    			List<String> master_all_zahn_catalog = allCatalogsPage.getAllZahnCatalogList();
			    	    			// verified all catalog tab in master
			    	    			Assertions.assertThat(user_and_rule_driven).containsAll(master_all_zahn_catalog);

			    	    			// verifying with exception list
			    	    			pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType")).clickSeachButton()
			    	    					.clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
			    	    			List<String> userdrivencatalogs = catalogTypePage.getExceptionListCatalogs();
//			    	    			List<String> multiplevaluesfromexcel = javautils.readMultipleValuesFromExcel(map.get("userDriven"));
//			    	    			for (String userdriven : multiplevaluesfromexcel) {
//			    	    				if (userdrivencatalogs.contains(userdriven)) {
//			    	    					// delete userdriven
//			    	    					catalogTypePage.clearExceptionListField(userdriven);
//			    	    					productdetailspage.clickRefreshIcon();
//			    	    					log(LogType.INFO, "Deleting the Userdriven");
//			    	    				}
//			    	    			}
			    	    			BasePage.WaitForMiliSec(3000);
			    	    			catalogTypePage.clickExceptionListField().selectExceptionCatalogsFromList(map.get("userDriven"));
			    	    			pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("AllCatalogtab")).clickRefreshIcon();

			    	    			List<String> all_catalog_except_exceptionallist = allCatalogsPage.getAllZahnCatalogList();
			    	    			Assertions.assertThat(all_catalog_except_exceptionallist).isNotEqualTo(user_and_rule_driven);

			    	    			// verifying with exception list in master catalog
			    	    			pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
			    	    					.clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon()
			    	    					.selectTabfromDropdown(map.get("AllCatalogtab"));
			    	    			List<String> master_exception_alldental__catalog = allCatalogsPage.getAllZahnCatalogList();
			    	    			Assertions.assertThat(user_and_rule_driven).isNotEqualTo(master_exception_alldental__catalog);
			    	    			Assertions.assertThat(all_catalog_except_exceptionallist).containsAll(master_exception_alldental__catalog);
			    	    			pimHomepage.clickLogoutButton();
			    	    		}

			    	    		// For US - WRFL_011, WRFL_012
			    	    		@PimFrameworkAnnotation(module = Modules.ZAHN_CATALOG, category = CategoryType.SMOKE)
			    	    		@TestDataSheet(sheetname = TestCaseSheet.Catalog)
			    	    		@Test(priority = 1, description = "For US - WRFL_011 Verification Part | For US - WRFL_011, WRFL_012 | Verify Set of Catalog Entity Rules triggered if we update the Publish date and Flag in Catalog Assignment for US for Zahn Catalog", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			    	    				"SMOKE","US", "pim","ZAHN_CATALOG" })
			    	    		public void verify_quality_status_when_change_publish_date_and_flag_for_US_zahn_division(Map<String, String> map) {
			    	    			PimHomepage pimHomepage = new LoginPage()
			    	    					.enterUserName(ExcelUtils.getLoginData().get("Zahn User").get("UserName"))
			    	    					.enterPassword(ExcelUtils.getLoginData().get("Zahn User").get("Password")).clickLoginButton();
			    	    			pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
			    	    					.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
			    	    					.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
			    	    			log(LogType.INFO, "Navigate to Dental Catalog tab from Queries Menu for US Catalog");
			    	    			BasePage.WaitForMiliSec(3000);
//			    	    			catalogTypePage.selectPublishDate(DateandTimeUtils.getTodaysDate()).selectPublishFlag();
			    	    			log(LogType.INFO, "Change publish date to current date and publish flag");
			    	    			String publishdate = catalogTypePage.getPublishDate();
			    	    			String publishflag = catalogTypePage.getPublishFlag();

			    	    			// verify quality status
			    	    			pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
			    	    			List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus"))
			    	    					.maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
			    	    			for (String Rules : javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule"))) {
			    	    				Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);
			    	    			}
			    	    			qualitystatus.minimizeQualityStatusTab();
			    	    			log(LogType.INFO, "Verifying catalog entity rule");

			    	    			pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
			    	    					.clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon().expandTab();
			    	    			log(LogType.INFO, "Navigate to Master Catalog");
			    	    			Assertions.assertThat(publishdate).isEqualTo(catalogTypePage.getPublishDate());
			    	    			Assertions.assertThat(publishflag).isEqualTo(catalogTypePage.getPublishFlag());

			    	    			pimHomepage.clickLogoutButton();
			    	    		}

			    	    		// For CA - WRFL_011 WRFL_012
			    	    		@PimFrameworkAnnotation(module = Modules.ZAHN_CATALOG, category = CategoryType.SMOKE)
			    	    		@TestDataSheet(sheetname = TestCaseSheet.Catalog)
			    	    		@Test(priority = 1, description = " For CA - WRFL_011 Verification Part | For CA - WRFL_011 WRFL_012 | Verify Set of Catalog Entity Rules triggered if we update the Publish date and Flag in Catalog Assignment for CA", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			    	    				"SMOKE","CA", "pim","ZAHN_CATALOG" })
			    	    		public void verify_quality_status_when_change_publish_date_and_flag_for_CA_zahn_division(Map<String, String> map) {
			    	    			PimHomepage pimHomepage = new LoginPage()
			    	    					.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
			    	    					.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
			    	    			pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
			    	    					.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
			    	    					.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
			    	    			log(LogType.INFO, "Navigate to Dental Catalog tab from Queries Menu for CA Catalog");
			    	    			BasePage.WaitForMiliSec(3000);
//			    	    			catalogTypePage.selectPublishDate(DateandTimeUtils.getTodaysDate()).selectPublishFlag();
			    	    			log(LogType.INFO, "Change publish date to current date and publish flag");
			    	    			String publishdate = catalogTypePage.getPublishDate();
			    	    			String publishflag = catalogTypePage.getPublishFlag();

			    	    			// verify quality status
			    	    			pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
			    	    			List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus"))
			    	    					.maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
			    	    			for (String Rules : javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule"))) {
			    	    				Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);
			    	    			}
			    	    			qualitystatus.minimizeQualityStatusTab();
			    	    			log(LogType.INFO, "Verifying catalog entity rule");

			    	    			pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
			    	    					.clickOnSecondResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon().expandTab();
			    	    			log(LogType.INFO, "Navigate to Master Catalog");
			    	    			Assertions.assertThat(publishdate).isEqualTo(catalogTypePage.getPublishDate());
			    	    			Assertions.assertThat(publishflag).isEqualTo(catalogTypePage.getPublishFlag());

			    	    			pimHomepage.clickLogoutButton();
			    	    		}

			    	    		// we need recently created item and make change on ItemPublish sheet
			    	    		@PimFrameworkAnnotation(module = Modules.ZAHN_CATALOG, category = CategoryType.REGRESSION)
			    	    		@TestDataSheet(sheetname = TestCaseSheet.ItemPublish)
			    	    		@Test(priority = 1, description = "For US - ITP_031 Verification Part | For US - ITP_031 | Verify whether the Publish date changed by the Zahn division owner gets updated for other divisions when the other divisions have not modified the Original Publish Date for US.", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			    	    				"US", "pim","ZAHN_CATALOG" })
			    	    		public void verify_publish_date_changed_by_zahn_division_for_US(Map<String, String> map) {
			    	    			PimHomepage pimHomepage = new LoginPage()
			    	    					.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
			    	    					.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
			    	    			pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
			    	    					.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
			    	    					.clickOnFirstResult().selectTabfromDropdown(map.get("ZahnDivision"));
			    	    			log(LogType.INFO, "Navigate to Zahn Catalog tab from Queries Menu for US Catalog");
			    	    			BasePage.WaitForMiliSec(3000);

			    	    			String publishflag = catalogTypePage.getPublishFlag();
			    	    			// Assertions.assertThat(publishflag).isEqualTo(map.get("PublishFlag"));
//			    	    			catalogTypePage.selectPublishDate(DateandTimeUtils.getTodaysDate());
			    	    			productdetailspage.clickRefreshIcon();
			    	    			String zahnpublishdate = catalogTypePage.getPublishDate();
			    	    			log(LogType.INFO, "Change publish date to current date");

			    	    			// verify quality status
			    	    			pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
			    	    			List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus"))
			    	    					.maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
			    	    			for (String Rules : javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule"))) {
			    	    				Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);
			    	    			}
			    	    			qualitystatus.minimizeQualityStatusTab();
			    	    			log(LogType.INFO, "Verifying catalog entity rule");

			    	    			productdetailspage.selectTabfromDropdown(map.get("DentalDivision")).clickRefreshIcon();
			    	    			String dentalpublishdate = catalogTypePage.getPublishDate();
			    	    			Assertions.assertThat(dentalpublishdate).isEqualTo(zahnpublishdate);
			    	    			log(LogType.INFO, "verified publish date in Dental division");

			    	    			productdetailspage.selectTabfromDropdown(map.get("MedicalDivision")).clickRefreshIcon();
			    	    			String medicalpublishdate = catalogTypePage.getPublishDate();
			    	    			Assertions.assertThat(medicalpublishdate).isEqualTo(zahnpublishdate);
			    	    			log(LogType.INFO, "verified publish date in Medical division");

			    	    			productdetailspage.selectTabfromDropdown(map.get("SpecialMarketsDivision")).clickRefreshIcon();
			    	    			String specialmarketspublishdate = catalogTypePage.getPublishDate();
			    	    			Assertions.assertThat(specialmarketspublishdate).isEqualTo(zahnpublishdate);
			    	    			log(LogType.INFO, "verified publish date in Special Markets division");

			    	    			// master
			    	    			pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
			    	    					.clickOnFirstResult().selectTabfromDropdown(map.get("ZahnDivision")).clickRefreshIcon();
			    	    			log(LogType.INFO, "Navigate to Master Catalog");
			    	    			BasePage.WaitForMiliSec(3000);
			    	    			String masterpublishflag = catalogTypePage.getPublishFlag();
			    	    			// Assertions.assertThat(masterpublishflag).isEqualTo(map.get("PublishFlag"));
			    	    			// String masterzahnpublishdate = catalogTypePage.getPublishDate();
			    	    			log(LogType.INFO, "getting publishdate");

			    	    			productdetailspage.selectTabfromDropdown(map.get("DentalDivision")).clickRefreshIcon();
			    	    			String masterdentalpublishdate = catalogTypePage.getPublishDate();
			    	    			Assertions.assertThat(masterdentalpublishdate).isEqualTo(zahnpublishdate);
			    	    			log(LogType.INFO, "verified publish date in Dental division");

			    	    			productdetailspage.selectTabfromDropdown(map.get("MedicalDivision")).clickRefreshIcon();
			    	    			String mastermedicalpublishdate = catalogTypePage.getPublishDate();
			    	    			Assertions.assertThat(mastermedicalpublishdate).isEqualTo(zahnpublishdate);
			    	    			log(LogType.INFO, "verified publish date in Medical division");

			    	    			productdetailspage.selectTabfromDropdown(map.get("SpecialMarketsDivision")).clickRefreshIcon();
			    	    			String masterspecialmarketspublishdate = catalogTypePage.getPublishDate();
			    	    			Assertions.assertThat(masterspecialmarketspublishdate).isEqualTo(zahnpublishdate);
			    	    			log(LogType.INFO, "verified publish date in Special Markets division");
			    	    			pimHomepage.clickLogoutButton();
			    	    		}

			    	    		// we need recently created item and make change on ItemPublish sheet
			    	    		@PimFrameworkAnnotation(module = Modules.ZAHN_CATALOG, category = CategoryType.REGRESSION)
			    	    		@TestDataSheet(sheetname = TestCaseSheet.ItemPublish)
			    	    		@Test(priority = 1, description = "For CA - ITP_031 Verification Part | For CA - ITP_031 | Verify whether the Publish date changed by the Zahn division owner gets updated for other divisions when the other divisions have not modified the Original Publish Date for CA.", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			    	    				"CA", "pim","ZAHN_CATALOG" })
			    	    		public void verify_publish_date_changed_by_zahn_division_for_CA(Map<String, String> map) {
			    	    			PimHomepage pimHomepage = new LoginPage()
			    	    					.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
			    	    					.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
			    	    			pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
			    	    					.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
			    	    					.clickOnFirstResult().selectTabfromDropdown(map.get("ZahnDivision"));
			    	    			log(LogType.INFO, "Navigate to Zahn Catalog tab from Queries Menu for US Catalog");
			    	    			BasePage.WaitForMiliSec(3000);

			    	    			String publishflag = catalogTypePage.getPublishFlag();
			    	    			// Assertions.assertThat(publishflag).isEqualTo(map.get("PublishFlag"));
//			    	    			catalogTypePage.selectPublishDate(DateandTimeUtils.getTodaysDate());
			    	    			productdetailspage.clickRefreshIcon();
			    	    			String zahnpublishdate = catalogTypePage.getPublishDate();
			    	    			log(LogType.INFO, "Change publish date to current date");

			    	    			// verify quality status
			    	    			pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
			    	    			List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus"))
			    	    					.maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
			    	    			for (String Rules : javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule"))) {
			    	    				Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);
			    	    			}
			    	    			qualitystatus.minimizeQualityStatusTab();
			    	    			log(LogType.INFO, "Verifying catalog entity rule");

			    	    			productdetailspage.selectTabfromDropdown(map.get("DentalDivision")).clickRefreshIcon();
			    	    			String dentalpublishdate = catalogTypePage.getPublishDate();
			    	    			Assertions.assertThat(dentalpublishdate).isEqualTo(zahnpublishdate);
			    	    			log(LogType.INFO, "verified publish date in Dental division");

			    	    			productdetailspage.selectTabfromDropdown(map.get("MedicalDivision")).clickRefreshIcon();
			    	    			String medicalpublishdate = catalogTypePage.getPublishDate();
			    	    			Assertions.assertThat(medicalpublishdate).isEqualTo(zahnpublishdate);
			    	    			log(LogType.INFO, "verified publish date in Medical division");

			    	    			// master
			    	    			pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
			    	    					.clickOnSecondResult().selectTabfromDropdown(map.get("ZahnDivision")).clickRefreshIcon();
			    	    			log(LogType.INFO, "Navigate to Master Catalog");
			    	    			BasePage.WaitForMiliSec(3000);
			    	    			String masterpublishflag = catalogTypePage.getPublishFlag();
			    	    			// Assertions.assertThat(masterpublishflag).isEqualTo(map.get("PublishFlag"));
			    	    			log(LogType.INFO, "getting publishdate");

			    	    			productdetailspage.selectTabfromDropdown(map.get("DentalDivision")).clickRefreshIcon();
			    	    			String masterdentalpublishdate = catalogTypePage.getPublishDate();
			    	    			Assertions.assertThat(masterdentalpublishdate).isEqualTo(zahnpublishdate);
			    	    			log(LogType.INFO, "verified publish date in Dental division");

			    	    			productdetailspage.selectTabfromDropdown(map.get("MedicalDivision")).clickRefreshIcon();
			    	    			String mastermedicalpublishdate = catalogTypePage.getPublishDate();
			    	    			Assertions.assertThat(mastermedicalpublishdate).isEqualTo(zahnpublishdate);
			    	    			log(LogType.INFO, "verified publish date in Medical division");

			    	    			pimHomepage.clickLogoutButton();
			    	    		}

			    	    		// For US - ITP_031
			    	    		@PimFrameworkAnnotation(module = Modules.ZAHN_CATALOG, category = CategoryType.Data_Issue)
			    	    		@TestDataSheet(sheetname = TestCaseSheet.ItemPublish)
			    	    		@Test(priority = 1, description = "For US - ITP_031 Verification Part | For US - ITP_031 | Verify whether the Publish date changed by the ZAHN Owner division should not gets updated for other divisions when the other divisions have modified the Original Publish Date for US.", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			    	    				"US", "pim","ZAHN_CATALOG" })
			    	    		public void verify_publish_date_not_update_for_special_markets_when_changed_in_zahn_for_US(
			    	    				Map<String, String> map) {
			    	    			PimHomepage pimHomepage = new LoginPage()
			    	    					.enterUserName(ExcelUtils.getLoginData().get("Zahn User").get("UserName"))
			    	    					.enterPassword(ExcelUtils.getLoginData().get("Zahn User").get("Password")).clickLoginButton();
			    	    			pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
			    	    					.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
			    	    					.clickOnFirstResult().selectTabfromDropdown(map.get("ZahnDivision"));
			    	    			log(LogType.INFO, "Navigate to Zahn Catalog tab from Queries Menu for US Catalog");
			    	    			BasePage.WaitForMiliSec(3000);

			    	    			String publishflag = catalogTypePage.getPublishFlag();
			    	    			Assertions.assertThat(publishflag).isEqualTo(map.get("PublishFlag"));
//			    	    			catalogTypePage.selectPublishDate(DateandTimeUtils.getTodaysDate());
			    	    			productdetailspage.clickRefreshIcon();
			    	    			String zahnpublishdate = catalogTypePage.getPublishDate();
			    	    			log(LogType.INFO, "Change publish date to current date");
			    	    			pimHomepage.clickLogoutButton();
			    	    			log(LogType.INFO, "Logout from Zahn user");

			    	    			loginpage.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
			    	    					.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
			    	    			pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
			    	    					.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
			    	    					.clickOnFirstResult().selectTabfromDropdown(map.get("SpecialMarketsDivision"));
			    	    			log(LogType.INFO, "Navigate to Special Markets Catalog tab from Queries Menu for US Catalog");
			    	    			BasePage.WaitForMiliSec(3000);

			    	    			String specialpublishflag = catalogTypePage.getPublishFlag();
			    	    			Assertions.assertThat(specialpublishflag).isEqualTo(map.get("PublishFlag"));
//			    	    			catalogTypePage.selectPublishDate(DateandTimeUtils.getFutureDate());
//			    	    			productdetailspage.clickRefreshIcon();

			    	    			// verify quality status
			    	    			pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
			    	    			List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus"))
			    	    					.maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
			    	    			for (String Rules : javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule"))) {
			    	    				Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);
			    	    			}
			    	    			qualitystatus.minimizeQualityStatusTab();
			    	    			log(LogType.INFO, "Verifying catalog entity rule");

			    	    			productdetailspage.selectTabfromDropdown(map.get("DentalDivision")).clickRefreshIcon();
			    	    			String dentalpublishdate = catalogTypePage.getPublishDate();
			    	    			Assertions.assertThat(dentalpublishdate).isEqualTo(zahnpublishdate);
			    	    			log(LogType.INFO, "verified publish date in Dental division");

			    	    			productdetailspage.selectTabfromDropdown(map.get("MedicalDivision")).clickRefreshIcon();
			    	    			String medicalpublishdate = catalogTypePage.getPublishDate();
			    	    			Assertions.assertThat(medicalpublishdate).isEqualTo(zahnpublishdate);
			    	    			log(LogType.INFO, "verified publish date in Medical division");

			    	    			productdetailspage.selectTabfromDropdown(map.get("SpecialMarketsDivision")).clickRefreshIcon();
			    	    			String specialmarketspublishdate = catalogTypePage.getPublishDate();
			    	    			Assertions.assertThat(specialmarketspublishdate).isNotEqualTo(zahnpublishdate);
			    	    			log(LogType.INFO, "verified publish date in Special Markets division");

			    	    			// master
			    	    			pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
			    	    					.clickOnFirstResult().selectTabfromDropdown(map.get("ZahnDivision")).clickRefreshIcon();
			    	    			log(LogType.INFO, "Navigate to Master Catalog");
			    	    			BasePage.WaitForMiliSec(3000);
			    	    			String masterpublishflag = catalogTypePage.getPublishFlag();
			    	    			Assertions.assertThat(masterpublishflag).isEqualTo(map.get("PublishFlag"));
			    	    			log(LogType.INFO, "Change publish date to current date");

			    	    			productdetailspage.selectTabfromDropdown(map.get("DentalDivision")).clickRefreshIcon();
			    	    			String masterdentalpublishdate = catalogTypePage.getPublishDate();
			    	    			Assertions.assertThat(masterdentalpublishdate).isEqualTo(zahnpublishdate);
			    	    			log(LogType.INFO, "verified publish date in Dental division");

			    	    			productdetailspage.selectTabfromDropdown(map.get("MedicalDivision")).clickRefreshIcon();
			    	    			String mastermedicalpublishdate = catalogTypePage.getPublishDate();
			    	    			Assertions.assertThat(mastermedicalpublishdate).isEqualTo(zahnpublishdate);
			    	    			log(LogType.INFO, "verified publish date in Medical division");

			    	    			productdetailspage.selectTabfromDropdown(map.get("SpecialMarketsDivision")).clickRefreshIcon();
			    	    			String masterspecialmarketspublishdate = catalogTypePage.getPublishDate();
			    	    			Assertions.assertThat(masterspecialmarketspublishdate).isNotEqualTo(zahnpublishdate);
			    	    			log(LogType.INFO, "verified publish date in Special Markets division");
			    	    			pimHomepage.clickLogoutButton();
			    	    		}

			    	    		// For CA - ITP_031
			    	    		@PimFrameworkAnnotation(module = Modules.ZAHN_CATALOG, category = CategoryType.REGRESSION)
			    	    		@TestDataSheet(sheetname = TestCaseSheet.ItemPublish)
			    	    		@Test(priority = 1, description = "For CA - ITP_031 Verification Part | For CA - ITP_031 | Verify whether the Publish date changed by the ZAHN Owner division should not gets updated for other divisions when the other divisions have modified the Original Publish Date for CA.", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			    	    				"CA", "pim","ZAHN_CATALOG" })
			    	    		public void verify_publish_date_not_update_for_special_markets_when_changed_in_zahn_for_CA(
			    	    				Map<String, String> map) {
			    	    			PimHomepage pimHomepage = new LoginPage()
			    	    					.enterUserName(ExcelUtils.getLoginData().get("Zahn User").get("UserName"))
			    	    					.enterPassword(ExcelUtils.getLoginData().get("Zahn User").get("Password")).clickLoginButton();
			    	    			pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
			    	    					.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
			    	    					.clickOnFirstResult().selectTabfromDropdown(map.get("ZahnDivision"));
			    	    			log(LogType.INFO, "Navigate to Zahn Catalog tab from Queries Menu for US Catalog");
			    	    			BasePage.WaitForMiliSec(3000);

			    	    			String publishflag = catalogTypePage.getPublishFlag();
			    	    			Assertions.assertThat(publishflag).isEqualTo(map.get("PublishFlag"));
//			    	    			catalogTypePage.selectPublishDate(DateandTimeUtils.getTodaysDate());
			    	    			productdetailspage.clickRefreshIcon();
			    	    			String zahnpublishdate = catalogTypePage.getPublishDate();
			    	    			log(LogType.INFO, "Change publish date to current date");
			    	    			pimHomepage.clickLogoutButton();
			    	    			log(LogType.INFO, "Logout from Zahn user");

			    	    			loginpage.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
			    	    					.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
			    	    			pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
			    	    					.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
			    	    					.clickOnFirstResult().selectTabfromDropdown(map.get("SpecialMarketsDivision"));
			    	    			log(LogType.INFO, "Navigate to Special Markets Catalog tab from Queries Menu for US Catalog");
			    	    			BasePage.WaitForMiliSec(3000);

			    	    			String specialpublishflag = catalogTypePage.getPublishFlag();
			    	    			Assertions.assertThat(specialpublishflag).isEqualTo(map.get("PublishFlag"));
//			    	    			catalogTypePage.selectPublishDate(DateandTimeUtils.getFutureDate());
//			    	    			productdetailspage.clickRefreshIcon();

			    	    			// verify quality status
			    	    			pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
			    	    			List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus"))
			    	    					.maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
			    	    			for (String Rules : javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule"))) {
			    	    				Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);
			    	    			}
			    	    			qualitystatus.minimizeQualityStatusTab();
			    	    			log(LogType.INFO, "Verifying catalog entity rule");

			    	    			productdetailspage.selectTabfromDropdown(map.get("DentalDivision")).clickRefreshIcon();
			    	    			String dentalpublishdate = catalogTypePage.getPublishDate();
			    	    			Assertions.assertThat(dentalpublishdate).isEqualTo(zahnpublishdate);
			    	    			log(LogType.INFO, "verified publish date in Dental division");

			    	    			productdetailspage.selectTabfromDropdown(map.get("MedicalDivision")).clickRefreshIcon();
			    	    			String medicalpublishdate = catalogTypePage.getPublishDate();
			    	    			Assertions.assertThat(medicalpublishdate).isEqualTo(zahnpublishdate);
			    	    			log(LogType.INFO, "verified publish date in Medical division");

			    	    			// master
			    	    			pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
			    	    					.clickOnFirstResult().selectTabfromDropdown(map.get("ZahnDivision")).clickRefreshIcon();
			    	    			log(LogType.INFO, "Navigate to Master Catalog");
			    	    			BasePage.WaitForMiliSec(3000);
			    	    			String masterpublishflag = catalogTypePage.getPublishFlag();
			    	    			Assertions.assertThat(masterpublishflag).isEqualTo(map.get("PublishFlag"));
			    	    			log(LogType.INFO, "Change publish date to current date");

			    	    			productdetailspage.selectTabfromDropdown(map.get("DentalDivision")).clickRefreshIcon();
			    	    			String masterdentalpublishdate = catalogTypePage.getPublishDate();
			    	    			Assertions.assertThat(masterdentalpublishdate).isEqualTo(zahnpublishdate);
			    	    			log(LogType.INFO, "verified publish date in Dental division");

			    	    			productdetailspage.selectTabfromDropdown(map.get("MedicalDivision")).clickRefreshIcon();
			    	    			String mastermedicalpublishdate = catalogTypePage.getPublishDate();
			    	    			Assertions.assertThat(mastermedicalpublishdate).isEqualTo(zahnpublishdate);
			    	    			log(LogType.INFO, "verified publish date in Medical division");

			    	    			pimHomepage.clickLogoutButton();
			    	    		}

			    	    		// CATR_221-Verify if ZAHN_FSC gets removed from user driven list if Zahn is
			    	    		// chosen in user driven catalog list of dental division
			    	    		@PimFrameworkAnnotation(module = Modules.ZAHN_CATALOG, category = CategoryType.REGRESSION)
			    	    		@TestDataSheet(sheetname = TestCaseSheet.Catalog_CA)
			    	    		@Test(priority = 1, description = "CATR_221 Verification Part | CATR_221 | Verify if ZAHN_FSC gets removed from user driven list if ZAHN is chosen in user driven catalog list of dental division", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			    	    				"CA", "pim","ZAHN_CATALOG" })
			    	    		public void Verify_if_ZAHN_FSC_gets_removed_from_user_driven_list_if_ZAHN_is_chosen_in_user_driven_catalog_list_of_dental_division_CATR_221(
			    	    				Map<String, String> map) {
			    	    			PimHomepage pimHomepage = new LoginPage()
			    	    					.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
			    	    					.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
			    	    			pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
			    	    					.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
			    	    					.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
			    	    			// catalogTypePage.clearSelectedFields(map.get("userDriven1"));
//			    	    			catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
			    	    			pimHomepage.productDetailSearchPage().clickRefreshIcon();
//			    	    			catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven1"));
//			    	    			pimHomepage.productDetailSearchPage().clickRefreshIcon().clickRefreshIcon();
			    	    			List<String> user_driven = catalogTypePage.getUserDrivenCatalogs();
			    	    			Assertions.assertThat(user_driven).doesNotContain(map.get("userDriven"));
			    	    		}

			    	    		// For US - ITP_019
			    	    		// Verify Item with Publish Flag = 'Y" and Publish Date as Future Date - All
			    	    		// Divisions and check in Middleware
			    	    		@PimFrameworkAnnotation(module = Modules.ZAHN_CATALOG, category = CategoryType.REGRESSION)
			    	    		@TestDataSheet(sheetname = TestCaseSheet.ItemPublish)
			    	    		@Test(priority = 1, description = "For US - ITP_019 Verification Part | For US - ITP_019 | Verify whether the Publish Flag for a Zahn Item get updated as 'Y' and publish date as Future in the Middleware Export File, when the Items loaded from PIX to PIM with Publish Flag as \"Y\" for US", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			    	    				"US", "pim","ZAHN_CATALOG" })
			    	    		public void verify_publish_flag_should_yes_and_publish_date_should_be_future_US_for_Zahn_division(
			    	    				Map<String, String> map) {
			    	    			PimHomepage pimHomepage = new LoginPage()
			    	    					.enterUserName(ExcelUtils.getLoginData().get("Zahn User").get("UserName"))
			    	    					.enterPassword(ExcelUtils.getLoginData().get("Zahn User").get("Password")).clickLoginButton();
			    	    			pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
			    	    					.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
			    	    					.clickOnFirstResult().selectTabfromDropdown(map.get("ZahnDivision"));
			    	    			log(LogType.INFO, "Navigate to Dental tab from Queries Menu for US Catalog");

//			    	    			String dentalpublishflag = catalogTypePage.getPublishFlag();
//			    	    			String Y = "Yes";
//			    	    			if (!dentalpublishflag.equals(Y)) {
//			    	    				catalogTypePage.selectPublishFlag(map.get("PublishFlag"));
//			    	    				log(LogType.INFO, "assigning yes in publish flag");
//			    	    			}
//			    	    			catalogTypePage.selectPublishDate(DateandTimeUtils.getFutureDate());
			    	    			log(LogType.INFO, "assigning future date");
			    	    			productdetailspage.clickRefreshIcon();
			    	    			String date = catalogTypePage.getPublishDate();

			    	    			// verify quality status
			    	    			pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
			    	    			List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus"))
			    	    					.maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
			    	    			for (String Rules : javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule"))) {
			    	    				Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);
			    	    			}
			    	    			qualitystatus.minimizeQualityStatusTab();
			    	    			log(LogType.INFO, "Verifying catalog entity rule");

			    	    			// master
			    	    			pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
			    	    					.clickOnFirstResult().selectTabfromDropdown(map.get("ZahnDivision")).clickRefreshIcon();
			    	    			log(LogType.INFO, "Navigate to Master Catalog");

			    	    			String masterdate = catalogTypePage.getPublishDate();
			    	    			Assertions.assertThat(date).isEqualTo(masterdate);
			    	    			log(LogType.INFO, "verifying dates should get update in master");
			    	    			pimHomepage.clickLogoutButton();
			    	    		}

			    	    		// For CA - ITP_019
			    	    		// Verify Item with Publish Flag = 'Y" and Publish Date as Future Date - All
			    	    		// Divisions and check in Middleware
			    	    		@PimFrameworkAnnotation(module = Modules.ZAHN_CATALOG, category = CategoryType.REGRESSION)
			    	    		@TestDataSheet(sheetname = TestCaseSheet.ItemPublish)
			    	    		@Test(priority = 1, description = "For CA - ITP_019 Verification Part | For CA - ITP_019 | Verify whether the Publish Flag for a Zahn Item get updated as 'Y' and Publish date as Future in the Middleware Export File, when the Items loaded from PIX to PIM with Publish Flag as \"Y\" for CA", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			    	    				"CA", "pim","ZAHN_CATALOG" })
			    	    		public void verify_publish_flag_should_yes_and_publish_date_should_be_future_CA_for_Zahn_division(
			    	    				Map<String, String> map) {
			    	    			PimHomepage pimHomepage = new LoginPage()
			    	    					.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
			    	    					.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
			    	    			pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
			    	    					.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
			    	    					.clickOnFirstResult().selectTabfromDropdown(map.get("ZahnDivision"));
			    	    			log(LogType.INFO, "Navigate to Dental tab from Queries Menu for US Catalog");

//			    	    			String dentalpublishflag = catalogTypePage.getPublishFlag();
//			    	    			String Y = "Yes";
//			    	    			if (!dentalpublishflag.equals(Y)) {
//			    	    				catalogTypePage.selectPublishFlag(map.get("PublishFlag"));
//			    	    				log(LogType.INFO, "assigning yes in publish flag");
//			    	    			}
//			    	    			catalogTypePage.selectPublishDate(DateandTimeUtils.getFutureDate());
			    	    			log(LogType.INFO, "assigning future date");
			    	    			productdetailspage.clickRefreshIcon();
			    	    			String date = catalogTypePage.getPublishDate();

			    	    			// verify quality status
			    	    			pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
			    	    			List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus"))
			    	    					.maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
			    	    			for (String Rules : javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule"))) {
			    	    				Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);
			    	    			}
			    	    			qualitystatus.minimizeQualityStatusTab();
			    	    			log(LogType.INFO, "Verifying catalog entity rule");

			    	    			// master
			    	    			pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
			    	    					.clickOnSecondResult().selectTabfromDropdown(map.get("ZahnDivision")).clickRefreshIcon();
			    	    			log(LogType.INFO, "Navigate to Master Catalog");

			    	    			String masterdate = catalogTypePage.getPublishDate();
			    	    			Assertions.assertThat(date).isEqualTo(masterdate);
			    	    			log(LogType.INFO, "verifying dates should get update in master");
			    	    			pimHomepage.clickLogoutButton();
			    	    		}

			    	    		// For US - ITP_027
			    	    		@PimFrameworkAnnotation(module = Modules.ZAHN_CATALOG, category = CategoryType.REGRESSION)
			    	    		@TestDataSheet(sheetname = TestCaseSheet.ItemPublish)
			    	    		@Test(priority = 1, description = "For US - ITP_027 Verification Part | For US - ITP_027 | Verify whether the Publish Flag for a Zahn Item get updated as \"N\" in the Middleware Export File, when the Items loaded from PIX to PIM with Publish Flag as \"N\" for US", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			    	    				"US", "pim","ZAHN_CATALOG" })
			    	    		public void verify_publish_flag_should_updated_as_NO_in_Zahn_division_for_US(Map<String, String> map) {
			    	    			PimHomepage pimHomepage = new LoginPage()
			    	    					.enterUserName(ExcelUtils.getLoginData().get("Zahn User").get("UserName"))
			    	    					.enterPassword(ExcelUtils.getLoginData().get("Zahn User").get("Password")).clickLoginButton();
			    	    			pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
			    	    					.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
			    	    					.clickOnFirstResult().selectTabfromDropdown(map.get("ZahnDivision"));
			    	    			log(LogType.INFO, "Navigate to Zahn tab from Queries Menu for US Catalog");

//			    	    			String dentalpublishflag = catalogTypePage.getPublishFlag();
//			    	    			if (!dentalpublishflag.equals("No")) {
//			    	    				catalogTypePage.selectPublishFlag(map.get("PublishFlag"));
//			    	    				log(LogType.INFO, "assigning No in publish flag");
//			    	    			}
//			    	    			List<String> userdrivencatalogs = catalogTypePage.getUserDrivenCatalogs();
//			    	    			if (!userdrivencatalogs.contains(map.get("userDriven"))) {
//			    	    				catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
//			    	    			}

			    	    			// verify quality status
			    	    			pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
			    	    			List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus"))
			    	    					.maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
			    	    			for (String Rules : javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule"))) {
			    	    				// Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);
			    	    			}
			    	    			qualitystatus.minimizeQualityStatusTab();
			    	    			log(LogType.INFO, "Verifying catalog entity rule");

			    	    			// master
			    	    			pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
			    	    					.clickOnFirstResult().selectTabfromDropdown(map.get("ZahnDivision")).clickRefreshIcon();
			    	    			log(LogType.INFO, "Navigate to Zahn division in Master Catalog");

			    	    			String masterpublishflag = catalogTypePage.getPublishFlag();
			    	    			Assertions.assertThat(masterpublishflag).isEqualTo(map.get("PublishFlag"));
			    	    			pimHomepage.clickLogoutButton();
			    	    		}

			    	    		// For CA - ITP_027
			    	    		@PimFrameworkAnnotation(module = Modules.ZAHN_CATALOG, category = CategoryType.REGRESSION)
			    	    		@TestDataSheet(sheetname = TestCaseSheet.ItemPublish)
			    	    		@Test(priority = 1, description = "For CA - ITP_027 Verification Part | For CA - ITP_027 | Verify whether the Publish Flag for a Zahn Item get updated as \"N\" in the Middleware Export File, when the Items loaded from PIX to PIM with Publish Flag as \"N\" for CA", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			    	    				"CA", "pim","ZAHN_CATALOG" })
			    	    		public void verify_publish_flag_should_updated_as_NO_in_Zahn_division_for_CA(Map<String, String> map) {
			    	    			PimHomepage pimHomepage = new LoginPage()
			    	    					.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
			    	    					.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
			    	    			pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
			    	    					.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
			    	    					.clickOnFirstResult().selectTabfromDropdown(map.get("ZahnDivision"));
			    	    			log(LogType.INFO, "Navigate to Zahn tab from Queries Menu for CA Catalog");

//			    	    			String dentalpublishflag = catalogTypePage.getPublishFlag();
//			    	    			if (!dentalpublishflag.equals("No")) {
//			    	    				catalogTypePage.selectPublishFlag(map.get("PublishFlag"));
//			    	    				log(LogType.INFO, "assigning No in publish flag");
//			    	    			}
//			    	    			List<String> userdrivencatalogs = catalogTypePage.getUserDrivenCatalogs();
//			    	    			if (!userdrivencatalogs.contains(map.get("userDriven"))) {
//			    	    				catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
//			    	    			}

			    	    			// verify quality status
			    	    			pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
			    	    			List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus"))
			    	    					.maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
			    	    			for (String Rules : javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule"))) {
			    	    				// Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);
			    	    			}
			    	    			qualitystatus.minimizeQualityStatusTab();
			    	    			log(LogType.INFO, "Verifying catalog entity rule");

			    	    			// master
			    	    			pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
			    	    					.clickOnSecondResult().selectTabfromDropdown(map.get("ZahnDivision")).clickRefreshIcon();
			    	    			log(LogType.INFO, "Navigate to Zahn division in Master Catalog");

			    	    			String masterpublishflag = catalogTypePage.getPublishFlag();
			    	    			Assertions.assertThat(masterpublishflag).isEqualTo(map.get("PublishFlag"));
			    	    			pimHomepage.clickLogoutButton();
			    	    		}

			    	    		// For US - ITP_023
			    	    		@PimFrameworkAnnotation(module = Modules.ZAHN_CATALOG, category = CategoryType.REGRESSION)
			    	    		@TestDataSheet(sheetname = TestCaseSheet.ItemPublish)
			    	    		@Test(priority = 1, description = "For US - ITP_023 Verification Part | For US - ITP_023 | Verify whether the Publish Flag for a Zahn Item get updated as \"Y\" and Publish date as Current in the Middleware Export File, when the Items loaded from PIX to PIM with Publish Flag as \"Y\" for US", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			    	    				"US", "pim","ZAHN_CATALOG" })
			    	    		public void verify_publish_flag_should_updated_as_Yes_in_Zahn_division_for_US(Map<String, String> map) {
			    	    			PimHomepage pimHomepage = new LoginPage()
			    	    					.enterUserName(ExcelUtils.getLoginData().get("Zahn User").get("UserName"))
			    	    					.enterPassword(ExcelUtils.getLoginData().get("Zahn User").get("Password")).clickLoginButton();
			    	    			pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
			    	    					.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
			    	    					.clickOnFirstResult().selectTabfromDropdown(map.get("ZahnDivision"));
			    	    			log(LogType.INFO, "Navigate to Zahn tab from Queries Menu for US Catalog");

//			    	    			catalogTypePage.selectPublishFlag(map.get("PublishFlag"));
//			    	    			log(LogType.INFO, "assigning Yes in publish flag");
			    	    	//
//			    	    			catalogTypePage.selectPublishDate(DateandTimeUtils.getTodaysDate());
//			    	    			productdetailspage.clickRefreshIcon();
//			    	    			log(LogType.INFO, "assigning current date");

			    	    			String publishdate = catalogTypePage.getPublishDate();
			    	    			String publishFlag = catalogTypePage.getPublishFlag();

//			    	    			List<String> userdrivencatalogs = catalogTypePage.getUserDrivenCatalogs();
//			    	    			if (!userdrivencatalogs.contains(map.get("userDriven"))) {
//			    	    				catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
//			    	    			}

			    	    			// verify quality status
			    	    			pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
			    	    			List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus"))
			    	    					.maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
			    	    			for (String Rules : javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule"))) {
			    	    				Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);
			    	    			}
			    	    			qualitystatus.minimizeQualityStatusTab();
			    	    			log(LogType.INFO, "Verifying catalog entity rule");

			    	    			// master
			    	    			pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
			    	    					.clickOnFirstResult().selectTabfromDropdown(map.get("ZahnDivision")).clickRefreshIcon();
			    	    			log(LogType.INFO, "Navigate to Zahn division in Master Catalog");

			    	    			String masterpublishdate = catalogTypePage.getPublishDate();
			    	    			String masterpublishFlag = catalogTypePage.getPublishFlag();

			    	    			Assertions.assertThat(masterpublishdate).isEqualTo(publishdate);
			    	    			Assertions.assertThat(masterpublishFlag).isEqualTo(publishFlag);
			    	    			pimHomepage.clickLogoutButton();
			    	    		}

			    	    		// For CA - ITP_023
			    	    		@PimFrameworkAnnotation(module = Modules.ZAHN_CATALOG, category = CategoryType.REGRESSION)
			    	    		@TestDataSheet(sheetname = TestCaseSheet.ItemPublish)
			    	    		@Test(priority = 1, description = "For CA - ITP_023 Verification Part | For CA - ITP_023 | Verify whether the Publish Flag for a Zahn Item get updated as \"Y\" and publish date as Current in the Middleware Export File, when the Items loaded from PIX to PIM with Publish Flag as \"Y\" for CA", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			    	    				"CA", "pim","ZAHN_CATALOG" })
			    	    		public void verify_publish_flag_should_updated_as_Yes_in_Zahn_division_for_CA(Map<String, String> map) {
			    	    			PimHomepage pimHomepage = new LoginPage()
			    	    					.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
			    	    					.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
			    	    			pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
			    	    					.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
			    	    					.clickOnFirstResult().selectTabfromDropdown(map.get("ZahnDivision"));
			    	    			log(LogType.INFO, "Navigate to Zahn tab from Queries Menu for US Catalog");

//			    	    			catalogTypePage.selectPublishFlag(map.get("PublishFlag"));
//			    	    			log(LogType.INFO, "assigning Yes in publish flag");
			    	    	//
//			    	    			catalogTypePage.selectPublishDate(DateandTimeUtils.getTodaysDate());
//			    	    			productdetailspage.clickRefreshIcon();
//			    	    			log(LogType.INFO, "assigning current date");

			    	    			String publishdate = catalogTypePage.getPublishDate();
			    	    			String publishFlag = catalogTypePage.getPublishFlag();

//			    	    			List<String> userdrivencatalogs = catalogTypePage.getUserDrivenCatalogs();
//			    	    			if (!userdrivencatalogs.contains(map.get("userDriven"))) {
//			    	    				catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
//			    	    			}

			    	    			// verify quality status
			    	    			pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
			    	    			List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus"))
			    	    					.maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
			    	    			for (String Rules : javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule"))) {
			    	    				Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);
			    	    			}
			    	    			qualitystatus.minimizeQualityStatusTab();
			    	    			log(LogType.INFO, "Verifying catalog entity rule");

			    	    			// master
			    	    			pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
			    	    					.clickOnSecondResult().selectTabfromDropdown(map.get("ZahnDivision")).clickRefreshIcon();
			    	    			log(LogType.INFO, "Navigate to Zahn division in Master Catalog");

			    	    			String masterpublishdate = catalogTypePage.getPublishDate();
			    	    			String masterpublishFlag = catalogTypePage.getPublishFlag();

			    	    			Assertions.assertThat(masterpublishdate).isEqualTo(publishdate);
			    	    			Assertions.assertThat(masterpublishFlag).isEqualTo(publishFlag);
			    	    			pimHomepage.clickLogoutButton();
			    	    		}
			    	    	    

			    	    		
			    	    		
			    	    		
			    	    		
			    	    		
			    	    		
			    	    		
			    	    		
			    	    		
			    	    		
			    	    		
			    	    		
			    	    		
			    	    		
			    	    		
			    	    		
			    	    		
			    	    		
			    	    		
			    	    		
			    	    		
			    	    		
			    	    		
		//Item Message and other modules	    	    		
			    	    		
			    	    		
			    	    		
			    	    		
			    	    		
			    	    		
			    	    		
			    	    		
			    	    		
			    	    



	ActionsPage action = new ActionsPage();
//	StructureSubMenu structuresubmenu = new StructureSubMenu();

//    ProductDetailSearchPage productDetailSearchPage = new ProductDetailSearchPage();
//    ClassificationsPage classificationpage = new ClassificationsPage();
//	QualityStatusPage qualitystatus = new QualityStatusPage();
//	ProductDetailSearchPage productdetail = new ProductDetailSearchPage();
//	GlobalAttributePage globalattribute = new GlobalAttributePage();
//	QueriesSubMenu queriessubmenu = new QueriesSubMenu();
	FieldSelectionPage fieldselectionpage = new FieldSelectionPage();
//	Javautils javautil = new Javautils();
	String znewglobalerrormessage;
	String zcanadaglobalerrormessage;
	String hazardouscode;
	String classificationcode;
//	String manufacturecode;
	String jdedescription;
	String linetypecode;
	String fdac;
	String supplierCode;
	
//	PART_1: DENTAL CATALOG TESTS

	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemMessage)
	@Test(priority = 1, description = "ITM_044 Verification Part | ITM_044, ITM_046, ITM_106, ITM_107, ITM_126, ITM_127 | Verifying Item Message based on Availability Code", groups = { "US",
			"pim","ITEM_MESSAGE" }, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verifyItemMessageforAvailabilityCode(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();

		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult();
		// verifying availability code
		String availabilitycode = productdetail.getItemField(map.get("fieldIndex"));
		Assertions.assertThat(availabilitycode).isEqualTo(map.get("AvailabilityCode"));
		log(LogType.INFO, "Verifying availability code");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO, "Navigate to Classification tab after verifying the availability code in US Catalog");

		classificationpage.clickOnExpandButton();
//		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
//				map.get("structurePath"));
//		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
//		classificationpage.clickStructureGroup();
//		classificationpage.removeButton();
//		classificationpage.clickYes();
		// verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		qualitystatus.sortRulestByLastExecution();
		List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("ruleStatus"));
		Assertions.assertThat(catalogentityrule).containsAnyOf(map.get("CatalogEntityRule"));
		log(LogType.INFO, "Verifying catalog entity rule");
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickOnCloseExpandButton();
		// verify in master
		productdetail.clickMenuRefreshIcon();
		queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog"))
				.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult();
		// verifying availability code
		availabilitycode = productdetail.getItemField(map.get("fieldIndex"));
		Assertions.assertThat(availabilitycode).isEqualTo(map.get("AvailabilityCode"));
		log(LogType.INFO, "Verifying availability code");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO, "Navigate to Classification tab after verifying the availability code in Master Catalog");
		classificationpage.clickOnExpandButton();
		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickOnCloseExpandButton();
		pimHomepage.clickLogoutButton();
	}

	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemMessage)
	@Test(priority = 1, description = "ITM_118 Verification Part | ITM_118 | Verifying Item Message based on Availability Code for Special Marketse", groups = { "US",
	"pim","ITEM_MESSAGE" }, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)

	public void verifyItemMessageforAvailabilityCode_SpecialMarkets(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();

		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult();
		// verifying availability code
		String availabilitycode = productdetail.getItemField(map.get("fieldIndex"));
		Assertions.assertThat(availabilitycode).isEqualTo(map.get("AvailabilityCode"));
		log(LogType.INFO, "Verifying availability code");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO, "Navigate to Classification tab after verifying the availability code in US Catalog");

		classificationpage.clickOnExpandButton();
//		znewglobalerrormessage = classificationpage.getStructureGroupHavingBlankSpace(map.get("structureSystem"),
//				map.get("structurePath"));
//		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
//		classificationpage.clickStructureGroup();
//		classificationpage.removeButton();
//		classificationpage.clickYes();
		// verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		qualitystatus.sortRulestByLastExecution();
		List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("ruleStatus"));
		Assertions.assertThat(catalogentityrule).containsAnyOf(map.get("CatalogEntityRule"));
		log(LogType.INFO, "Verifying catalog entity rule");
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		znewglobalerrormessage = classificationpage.getStructureGroupHavingBlankSpace(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickOnCloseExpandButton();
		// verify in master
		productdetail.clickMenuRefreshIcon();
		queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog"))
				.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult();
		// verifying availability code
		availabilitycode = productdetail.getItemField(map.get("fieldIndex"));
		Assertions.assertThat(availabilitycode).isEqualTo(map.get("AvailabilityCode"));
		log(LogType.INFO, "Verifying availability code");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO, "Navigate to Classification tab after verifying the availability code in Master Catalog");
		classificationpage.clickOnExpandButton();
		znewglobalerrormessage = classificationpage.getStructureGroupHavingBlankSpace(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickOnCloseExpandButton();
		pimHomepage.clickLogoutButton();
	}

	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemMessage)
	@Test(priority = 1, description = "ITM_053 Verification Part | ITM_053, ITM_060, ITM_061, ITM_062, ITM_067, ITM_072, ITM_073, ITM_074, ITM_076, ITM_078, ITM_088, ITM_091 | Verifying Item Message based on Hazardous Code", groups = { "US",
			"pim","ITEM_MESSAGE" }, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verifyItemMessageforHazardousCode(Map<String, String> map) throws Throwable {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
		log(LogType.INFO, "Navigate to Global attribute tab to verify the hazardous code in US Catalog");

		// verifying hazardous code
		hazardouscode = globalattribute.getHazardousClassCode();
		Assertions.assertThat(hazardouscode).isEqualTo(map.get("HazardousCode"));

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO, "Navigate to Classification tab to verify the structure group");

		classificationpage.clickOnExpandButton();
//		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
//				map.get("structurePath"));
//		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
//		classificationpage.clickStructureGroup();
//		classificationpage.removeButton();
//		classificationpage.clickYes();
		// verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		qualitystatus.sortRulestByLastExecution();
		List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("ruleStatus"));
		Assertions.assertThat(catalogentityrule).containsAnyOf(map.get("CatalogEntityRule"));
		log(LogType.INFO, "Verifying catalog entity rule");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		log(LogType.INFO, "Navigate to Classification tab to verify the structure group");
		classificationpage.clickOnCloseExpandButton();

		// verify in master
		productdetail.clickMenuRefreshIcon();
		queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog"))
				.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult()
				.selectTabfromDropdown(map.get("GlobalAttributeTab"));
		log(LogType.INFO, "Navigate to Global attribute tab to verify the hazardous code in Master Catalog");

		// verifying hazardous code
		hazardouscode = globalattribute.getHazardousClassCode();
		Assertions.assertThat(hazardouscode).isEqualTo(map.get("HazardousCode"));
		log(LogType.INFO, "Verifying hazardous code");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO, "Navigate to Classification tab to verify the structure group in Master Catalog");
		classificationpage.clickOnExpandButton();
		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickOnCloseExpandButton();
		pimHomepage.clickLogoutButton();
	}

	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemMessage)
	@Test(priority = 1, description = "ITM_119 Verification Part | ITM_119 | Verifying Item Message based on Hazardous Code with Special Markets", groups = { "US",
			"pim","ITEM_MESSAGE" }, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verifyItemMessageforHazardousCode_SpecialMarkets(Map<String, String> map) throws Throwable {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
		log(LogType.INFO, "Navigate to Global attribute tab to verify the hazardous code in US Catalog");

		// verifying hazardous code
		hazardouscode = globalattribute.getHazardousClassCode();
		Assertions.assertThat(hazardouscode).isEqualTo(map.get("HazardousCode"));

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO, "Navigate to Classification tab to verify the structure group");

		classificationpage.clickOnExpandButton();
//		znewglobalerrormessage = classificationpage.getStructureGroupHavingBlankSpace(map.get("structureSystem"),
//				map.get("structurePath"));
//		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
//		classificationpage.clickStructureGroup();
//		classificationpage.removeButton();
		classificationpage.clickYes();
		// verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		qualitystatus.sortRulestByLastExecution();
		List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("ruleStatus"));
		Assertions.assertThat(catalogentityrule).containsAnyOf(map.get("CatalogEntityRule"));
		log(LogType.INFO, "Verifying catalog entity rule");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		znewglobalerrormessage = classificationpage.getStructureGroupHavingBlankSpace(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		log(LogType.INFO, "Navigate to Classification tab to verify the structure group");
		classificationpage.clickOnCloseExpandButton();

		// verify in master
		productdetail.clickMenuRefreshIcon();
		queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog"))
				.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult()
				.selectTabfromDropdown(map.get("GlobalAttributeTab"));
		log(LogType.INFO, "Navigate to Global attribute tab to verify the hazardous code in Master Catalog");

		// verifying hazardous code
		hazardouscode = globalattribute.getHazardousClassCode();
		Assertions.assertThat(hazardouscode).isEqualTo(map.get("HazardousCode"));
		log(LogType.INFO, "Verifying hazardous code");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO, "Navigate to Classification tab to verify the structure group in Master Catalog");
		classificationpage.clickOnExpandButton();
		znewglobalerrormessage = classificationpage.getStructureGroupHavingBlankSpace(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickOnCloseExpandButton();
		pimHomepage.clickLogoutButton();
	}


	// ITM_104
	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.Valid_Failure)
	@TestDataSheet(sheetname = TestCaseSheet.ItemMessage)
	@Test(priority = 1, description = "ITM_104 Verification Part | ITM_104 | Verifying Item Message based on Classification Code", groups = { "US",
			"pim","ITEM_MESSAGE" }, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verifyItemMessageforClassificationCode(Map<String, String> map) throws Throwable {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
		// verifying classification code
		classificationcode = globalattribute.getClassificationCode();
		Assertions.assertThat(classificationcode).isEqualTo(map.get("ClassificationCode"));
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		classificationpage.clickOnExpandButton();

		// As per Manual Testing Team Medical_85 item messge we have to add and in DQ rule Medical_85 is not present

//		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
//				map.get("structurePath"));
//		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
//		classificationpage.clickStructureGroup();
//		classificationpage.removeButton();
//		classificationpage.clickYes();
		// verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		qualitystatus.sortRulestByLastExecution();
		List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("ruleStatus"));
		Assertions.assertThat(catalogentityrule).containsAnyOf(map.get("CatalogEntityRule"));
		log(LogType.INFO, "Verifying catalog entity rule");
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickOnCloseExpandButton();
		// verify in master
		productdetail.clickMenuRefreshIcon();
		queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog"))
				.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult()
				.selectTabfromDropdown(map.get("GlobalAttributeTab"));
		// verifying Classification code
		classificationcode = globalattribute.getClassificationCode();
		Assertions.assertThat(classificationcode).isEqualTo(map.get("ClassificationCode"));
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		classificationpage.clickOnExpandButton();
		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickOnCloseExpandButton();
		pimHomepage.clickLogoutButton();

	}

	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemMessage)
	@Test(priority = 1, description = "ITM_075 Verification Part | ITM_075, ITM_109, ITM_112 | Verifying Item Message based on Manufacture Code", groups = { "US",
			"pim","ITEM_MESSAGE" }, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verifyItemMessageforManufactureCode(Map<String, String> map) throws Throwable {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
		// verifying manufacture code
		manufacturecode = globalattribute.getManufacturercode();
		Assertions.assertThat(manufacturecode).isEqualTo(map.get("ManufactureCode"));
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		classificationpage.clickOnExpandButton();
//		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
//				map.get("structurePath"));
//		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
//		classificationpage.clickStructureGroup();
//		classificationpage.removeButton();
//		classificationpage.clickYes();
		// verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		qualitystatus.sortRulestByLastExecution();
		List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("ruleStatus"));
		Assertions.assertThat(catalogentityrule).containsAnyOf(map.get("CatalogEntityRule"));
		log(LogType.INFO, "Verifying catalog entity rule");
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickOnCloseExpandButton();
		// verify in master
		productdetail.clickMenuRefreshIcon();
		queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog"))
				.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult()
				.selectTabfromDropdown(map.get("GlobalAttributeTab"));
		// verifying hazardous code
		manufacturecode = globalattribute.getManufacturercode();
		Assertions.assertThat(manufacturecode).isEqualTo(map.get("ManufactureCode"));
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		classificationpage.clickOnExpandButton();
		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickOnCloseExpandButton();
		pimHomepage.clickLogoutButton();
	}

	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemMessage)
	@Test(priority = 1, description = "ITM_071 Verification Part | ITM_071 | Verifying Item Message based on Manufacture Code with JDE description", groups = { "US",
			"pim","ITEM_MESSAGE" }, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verifyItemMessageforManufactureCodeAndJdeDescription(Map<String, String> map) throws Throwable {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
		// verifying manufacture code
		manufacturecode = globalattribute.getManufacturercode();
		Assertions.assertThat(manufacturecode).isEqualTo(map.get("ManufactureCode"));
		// verifying jde description
		jdedescription = globalattribute.getJdeDescription();
		Assertions.assertThat(jdedescription).contains(map.get("JDE"));
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		classificationpage.clickOnExpandButton();
//		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
//				map.get("structurePath"));
//		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
//		classificationpage.clickStructureGroup();
//		classificationpage.removeButton();
//		classificationpage.clickYes();
		// verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		qualitystatus.sortRulestByLastExecution();
		List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("ruleStatus"));
		Assertions.assertThat(catalogentityrule).containsAnyOf(map.get("CatalogEntityRule"));
		log(LogType.INFO, "Verifying catalog entity rule");
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickOnCloseExpandButton();
		// verify in master
		productdetail.clickMenuRefreshIcon();
		queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog"))
				.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult()
				.selectTabfromDropdown(map.get("GlobalAttributeTab"));
		// verifying manufacture code
		manufacturecode = globalattribute.getManufacturercode();
		Assertions.assertThat(manufacturecode).isEqualTo(map.get("ManufactureCode"));
		// verifying jde description
		jdedescription = globalattribute.getJdeDescription();
		Assertions.assertThat(jdedescription).contains(map.get("JDE"));
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		classificationpage.clickOnExpandButton();
		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickOnCloseExpandButton();
		pimHomepage.clickLogoutButton();
	}

	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemMessage)
	@Test(priority = 1, description = "ITM_058 Verification Part | ITM_058 | Verifying Item Message based on ClassificationCode as inc. & ManufactureCode as exc.", groups = {
			"US", "pim","ITEM_MESSAGE" }, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verifyItemMessageforClassificationCodeIncAndManufactureCodeExc(Map<String, String> map)
			throws Throwable {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
		// verifying classification code
		classificationcode = globalattribute.getClassificationCode();
		Assertions.assertThat(classificationcode).isEqualTo(map.get("ClassificationCode"));
		// verifying manufacture code
		manufacturecode = globalattribute.getManufacturercode();
		Assertions.assertThat(manufacturecode).isNotEqualTo(map.get("ManufactureCode"));
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		classificationpage.clickOnExpandButton();
//		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
//				map.get("structurePath"));
//		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
//		classificationpage.clickStructureGroup();
//		classificationpage.removeButton();
//		classificationpage.clickYes();
		// verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		qualitystatus.sortRulestByLastExecution();
		List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("ruleStatus"));
		Assertions.assertThat(catalogentityrule).containsAnyOf(map.get("CatalogEntityRule"));
		log(LogType.INFO, "Verifying catalog entity rule");
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickOnCloseExpandButton();
		// verify in master
		productdetail.clickMenuRefreshIcon();
		queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog"))
				.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult()
				.selectTabfromDropdown(map.get("GlobalAttributeTab"));
		// verifying classification code
		classificationcode = globalattribute.getClassificationCode();
		Assertions.assertThat(classificationcode).isEqualTo(map.get("ClassificationCode"));
		// verifying manufacture code
		manufacturecode = globalattribute.getManufacturercode();
		Assertions.assertThat(manufacturecode).isNotEqualTo(map.get("ManufactureCode"));
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		classificationpage.clickOnExpandButton();
		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickOnCloseExpandButton();
		pimHomepage.clickLogoutButton();
	}

	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemMessage)
	@Test(priority = 1, description = "ITM_050 Verification Part | ITM_050, ITM_055 | Verifying Item Message based on two or more ItemCode", groups = { "US",
			"pim","ITEM_MESSAGE" }, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verifyItemMessageforItemCode(Map<String, String> map) throws Throwable {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		List<String> itemcodelist = javautil.readMultipleValuesFromExcel(map.get("ItemNumber"));
		for (int i = 0; i <= itemcodelist.size() - 1; i++) {
			pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
					.selectCatalogType(map.get("CatalogType"));
			queriessubmenu.enterHsiItemNumber(itemcodelist.get(i)).clickSeachButton().clickOnFirstResult()
					.selectTabfromDropdown(map.get("Classificationtab"));
			classificationpage.clickOnExpandButton();
//			znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
//					map.get("structurePath"));
//			Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
//			classificationpage.clickStructureGroup();
//			classificationpage.removeButton();
//			classificationpage.clickYes();
			// verify quality status
			pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
			qualitystatus.sortRulestByLastExecution();
			List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("ruleStatus"));
			Assertions.assertThat(catalogentityrule).containsAnyOf(map.get("CatalogEntityRule"));
			log(LogType.INFO, "Verifying catalog entity rule");
			pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
			pimHomepage.productDetailSearchPage().clickRefreshIcon();
			znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
					map.get("structurePath"));
			Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
			classificationpage.clickOnCloseExpandButton();
			productdetail.clickMenuRefreshIcon();
		}
		// verify in master
		for (int j = 0; j <= itemcodelist.size() - 1; j++) {
			queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog"));
			queriessubmenu.enterHsiItemNumber(itemcodelist.get(j)).clickSeachButton().clickOnFirstResult()
					.selectTabfromDropdown(map.get("Classificationtab"));
			classificationpage.clickOnExpandButton();
			znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
					map.get("structurePath"));
			Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
			classificationpage.clickOnCloseExpandButton();
			productdetail.clickMenuRefreshIcon();
		}
		pimHomepage.clickLogoutButton();
	}

	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemMessage)
	@Test(priority = 1, description = "ITM_054 Verification Part | ITM_054 | Verifying Item Message based on Item Code with Brand Code", groups = { "US",
			"pim","ITEM_MESSAGE" }, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verifyItemMessageforItemCodeWithBrandCode(Map<String, String> map) throws Throwable {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO, "Navigate to Classification tab from Queries Menu for US Catalog");

		classificationpage.clickOnExpandButton();
//		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
//				map.get("structurePath"));
//		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
//		classificationpage.clickStructureGroup();
//		classificationpage.removeButton();
//		classificationpage.clickYes();
		// verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		qualitystatus.sortRulestByLastExecution();
		List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("ruleStatus"));
		Assertions.assertThat(catalogentityrule).containsAnyOf(map.get("CatalogEntityRule"));
		log(LogType.INFO, "Verifying catalog entity rule");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		log(LogType.INFO, "Verifying item message code.");
		classificationpage.clickOnCloseExpandButton();

		// verify in master
		productdetail.clickMenuRefreshIcon();
		queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog"))
				.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult()
				.selectTabfromDropdown(map.get("Classificationtab"));
		classificationpage.clickOnExpandButton();
		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickOnCloseExpandButton();
		pimHomepage.clickLogoutButton();
	}

	// ITM_130,ITM_131
	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.Valid_Failure)
	@TestDataSheet(sheetname = TestCaseSheet.ItemMessage)
	@Test(priority = 1, description = "ITM_130 Verification Part | ITM_130,ITM_131 | Verify the item message for ItemCode in CA account", groups = { "CA",
			"pim","ITEM_MESSAGE" }, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verifyItemMessageforItemCodeForCaUser(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO, "Navigate to Classification tab from Queries Menu for US Catalog");

		classificationpage.clickOnExpandButton();
//		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
//				map.get("structurePath"));
//		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
//		classificationpage.clickStructureGroup();
//		classificationpage.removeButton();
//		classificationpage.clickYes();
		// verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		qualitystatus.sortRulestByLastExecution();
		List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("ruleStatus"));
		Assertions.assertThat(catalogentityrule).containsAnyOf(map.get("CatalogEntityRule"));
		log(LogType.INFO, "Verifying catalog entity rule");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		log(LogType.INFO, "Verifying item message code.");
		classificationpage.clickOnCloseExpandButton();

		// verify in master
		productdetail.clickMenuRefreshIcon();
		queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog"))
				.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult()
				.selectTabfromDropdown(map.get("Classificationtab"));
		classificationpage.clickOnExpandButton();
		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickOnCloseExpandButton();
		pimHomepage.clickLogoutButton();

	}

	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.Valid_Failure)
	@TestDataSheet(sheetname = TestCaseSheet.ItemMessage)
	@Test(priority = 1, description = "ITM_152 Verification Part | ITM_152 | Verify the item message for LineTypeCode in CA account", groups = { "CA",
			"pim","ITEM_MESSAGE" }, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verifyItemMessageforLineTypeCodeForCaUser(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
		log(LogType.INFO, "Navigate to Global Attribute tab from Queries Menu for US Catalog");

		// verifying linetype code
		linetypecode = globalattribute.getLineTypeCode();
		Assertions.assertThat(linetypecode).isEqualTo(map.get("LineTypeCode"));
		log(LogType.INFO, "Verifying line type code");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		classificationpage.clickOnExpandButton();
//		zcanadaglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
//				map.get("structurePath"));
//		Assertions.assertThat(zcanadaglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
//		classificationpage.clickStructureGroup();
//		classificationpage.removeButton();
//		classificationpage.clickYes();

		// verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		qualitystatus.sortRulestByLastExecution();
		List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("ruleStatus"));
		Assertions.assertThat(catalogentityrule).containsAnyOf(map.get("CatalogEntityRule"));
		log(LogType.INFO, "Verifying catalog entity rule");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		zcanadaglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(zcanadaglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		log(LogType.INFO, "Verifying item message code.");
		classificationpage.clickOnCloseExpandButton();

		// verify in master
		productdetail.clickMenuRefreshIcon();
		queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog"))
				.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult()
				.selectTabfromDropdown(map.get("GlobalAttributeTab"));
		log(LogType.INFO, "Navigate to Global Attribute tab from Queries Menu for Master Catalog");

		// verifying linetype code in master
		linetypecode = globalattribute.getLineTypeCode();
		Assertions.assertThat(linetypecode).isEqualTo(map.get("LineTypeCode"));
		log(LogType.INFO, "Verifying line type code in master");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		classificationpage.clickOnExpandButton();
		zcanadaglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(zcanadaglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickOnCloseExpandButton();
		pimHomepage.clickLogoutButton();

	}

	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemMessage)
	@Test(priority = 1, description = "ITM_084 Verification Part | ITM_084, ITM_085 | Verifying Item Message based on Print Message", groups = { "US",
			"pim","ITEM_MESSAGE" }, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verifyItemMessageforPrintMessage(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickSettingIcon().clickFieldSelectionOption();
		log(LogType.INFO, "Navigate to Field Selection Page from Setting Icon for US Catalog");

		fieldselectionpage.enterFieldName(map.get("Header")).clickHeaderText(map.get("HeaderText")).clickAddButton()
				.clickOkButton();
		log(LogType.INFO, "Print Message header is added in header");

		productdetail.clickOnFirstResult();
		// verify print message
		String printMessage = productdetail.getItemField(map.get("fieldIndex"));
		Assertions.assertThat(printMessage).isEqualTo(map.get("PrintMessage"));
		log(LogType.INFO, "Verifying Print Message");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		classificationpage.clickOnExpandButton();
//		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
//				map.get("structurePath"));
//		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
//		classificationpage.clickStructureGroup();
//		classificationpage.removeButton();
//		classificationpage.clickYes();

		// verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		qualitystatus.sortRulestByLastExecution();
		List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("ruleStatus"));
		Assertions.assertThat(catalogentityrule).containsAnyOf(map.get("CatalogEntityRule"));
		log(LogType.INFO, "Verifying catalog entity rule");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		log(LogType.INFO, "Verifying item message code.");
		classificationpage.clickOnCloseExpandButton();

		// verify in master
		productdetail.clickMenuRefreshIcon();
		queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog"))
				.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult();

		// verify print message
		printMessage = productdetail.getItemField(map.get("fieldIndex"));
		Assertions.assertThat(printMessage).isEqualTo(map.get("PrintMessage"));

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		classificationpage.clickOnExpandButton();
		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickOnCloseExpandButton();
		pimHomepage.clickLogoutButton();
	}

	// ITM_150
	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.Valid_Failure)
	@TestDataSheet(sheetname = TestCaseSheet.ItemMessage)
	@Test(priority = 1, description = "ITM_150 Verification Part | ITM_150 | Verify the item message for MPC in CA account", groups = { "CA",
			"pim","ITEM_MESSAGE" }, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verifyItemMessageforMPCForCaUser(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult();

		// verifying major product class code
		String majorproductclasscode = productdetail.getItemField(map.get("fieldIndex"));
		Assertions.assertThat(majorproductclasscode).isEqualTo(map.get("MPC"));
		log(LogType.INFO, "Verifying Major Product Class code");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO, "Navigate to Classification tab after verifying the MPC in US Catalog");

		classificationpage.clickOnExpandButton();
//		zcanadaglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
//				map.get("structurePath"));
//		Assertions.assertThat(zcanadaglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
//		classificationpage.clickStructureGroup();
//		classificationpage.removeButton();
//		classificationpage.clickYes();

		// verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		qualitystatus.sortRulestByLastExecution();
		List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("ruleStatus"));
		Assertions.assertThat(catalogentityrule).containsAnyOf(map.get("CatalogEntityRule"));
		log(LogType.INFO, "Verifying catalog entity rule");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		zcanadaglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(zcanadaglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		log(LogType.INFO, "Verifying item message code.");
		classificationpage.clickOnCloseExpandButton();

		// verify in master
		productdetail.clickMenuRefreshIcon();
		queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog"))
				.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult();
		// verifying major product class code
		majorproductclasscode = productdetail.getItemField(map.get("fieldIndex"));
		Assertions.assertThat(majorproductclasscode).isEqualTo(map.get("MPC"));
		log(LogType.INFO, "Verifying Major Product Class code in Master catalog");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO, "Navigate to Classification tab after verifying the MPC in Master Catalog");

		classificationpage.clickOnExpandButton();
		zcanadaglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(zcanadaglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickOnCloseExpandButton();
		pimHomepage.clickLogoutButton();
	}

	// ITM_132
	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemMessage)
	@Test(priority = 1, description = "ITM_132 Verification Part | ITM_132 | Verify the item message for Availability code in CA account", groups = { "CA",
			"pim","ITEM_MESSAGE" }, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verifyItemMessageforAvailabilityCodeForCaUser(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult();

		// verifying availability code
		String availabilitycode = productdetail.getItemField(map.get("fieldIndex"));
		Assertions.assertThat(availabilitycode).isEqualTo(map.get("AvailabilityCode"));
		log(LogType.INFO, "Verifying Availability code");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO, "Navigate to Classification tab after verifying the availability code in CA Catalog");

		classificationpage.clickOnExpandButton();
//		zcanadaglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
//				map.get("structurePath"));
//		Assertions.assertThat(zcanadaglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
//		classificationpage.clickStructureGroup();
//		classificationpage.removeButton();
//		classificationpage.clickYes();

		// verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		qualitystatus.sortRulestByLastExecution();
		List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("ruleStatus"));
		Assertions.assertThat(catalogentityrule).containsAnyOf(map.get("CatalogEntityRule"));
		log(LogType.INFO, "Verifying catalog entity rule");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		zcanadaglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(zcanadaglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		log(LogType.INFO, "Verifying item message code.");
		classificationpage.clickOnCloseExpandButton();

		// verify in master
		productdetail.clickMenuRefreshIcon();
		queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog"))
				.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult();
		// verifying availability code
		availabilitycode = productdetail.getItemField(map.get("fieldIndex"));
		Assertions.assertThat(availabilitycode).isEqualTo(map.get("AvailabilityCode"));
		log(LogType.INFO, "Verifying availability code in Master catalog");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO, "Navigate to Classification tab after verifying the availability code in Master Catalog");

		classificationpage.clickOnExpandButton();
		zcanadaglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(zcanadaglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickOnCloseExpandButton();
		pimHomepage.clickLogoutButton();
	}

	// ITM_144
	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemMessage)
	@Test(priority = 1, description = "ITM_144 Verification Part | ITM_144 | Verify the item message for MPC inc & Sub-MPC exc in CA account", groups = { "CA",
			"pim","ITEM_MESSAGE" }, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verifyItemMessageforMPCincSubMPCexcForCaUser(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult();

		// verifying MPC code
		String mpccode = productdetail.getItemField(map.get("fieldIndex"));
		Assertions.assertThat(mpccode).isEqualTo(map.get("MPC"));
		log(LogType.INFO, "Verifying MPC code");
		// verifying Sub-MPC code
		String submpccode = productdetail.getItemField(map.get("fieldIndex1"));
		Assertions.assertThat(submpccode).isNotEqualTo(map.get("SubMPC"));
		log(LogType.INFO, "Verifying Sub-MPC code");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO,
				"Navigate to Classification tab after verifying the MPC code inc. & Sub-MPC exc.  in CA Catalog");

		classificationpage.clickOnExpandButton();
//		zcanadaglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
//				map.get("structurePath"));
//		Assertions.assertThat(zcanadaglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
//		classificationpage.clickStructureGroup();
//		classificationpage.removeButton();
//		classificationpage.clickYes();

		// verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		qualitystatus.sortRulestByLastExecution();
		List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("ruleStatus"));
		Assertions.assertThat(catalogentityrule).containsAnyOf(map.get("CatalogEntityRule"));
		log(LogType.INFO, "Verifying catalog entity rule");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		zcanadaglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(zcanadaglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		log(LogType.INFO, "Verifying item message code.");
		classificationpage.clickOnCloseExpandButton();

		// verify in master
		productdetail.clickMenuRefreshIcon();
		queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog"))
				.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult();
		// verifying MPC code
		mpccode = productdetail.getItemField(map.get("fieldIndex"));
		Assertions.assertThat(mpccode).isEqualTo(map.get("MPC"));
		log(LogType.INFO, "Verifying MPC code");
		// verifying Sub-MPC code
		submpccode = productdetail.getItemField(map.get("fieldIndex1"));
		Assertions.assertThat(submpccode).isNotEqualTo(map.get("SubMPC"));
		log(LogType.INFO, "Verifying Sub-MPC code");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO,
				"Navigate to Classification tab after verifying the MPC code inc. & Sub-MPC exc. in Master Catalog");

		classificationpage.clickOnExpandButton();
		zcanadaglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(zcanadaglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickOnCloseExpandButton();
		pimHomepage.clickLogoutButton();
	}

	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemMessage)
	@Test(priority = 1, description = "ITM_083 Verification Part | ITM_083 | Verifying Item Message based on Line type code and print message inclusive", groups = { "US",
			"pim","ITEM_MESSAGE" }, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verifyItemMessageforLineTypeIncPrintMessageInc(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickCatalogsMenu();
		productdetail.clickSettingIcon().clickFieldSelectionOption();
		log(LogType.INFO, "Navigate to Field Selection Page from Setting Icon for US Catalog");

		fieldselectionpage.enterFieldName(map.get("Header")).clickHeaderText(map.get("HeaderText")).clickAddButton()
				.clickOkButton();
		log(LogType.INFO, "Print Message header is added in header");
		BasePage.WaitForMiliSec(10000);
		productdetail.clickRefreshIcon();

		List<String> itemcodelist = javautil.readMultipleValuesFromExcel(map.get("ItemNumber"));
		List<String> printmessagelist = javautil.readMultipleValuesFromExcel(map.get("PrintMessage"));
		for (int i = 0; i <= itemcodelist.size() - 1; i++) {
			pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
					.selectCatalogType(map.get("CatalogType"));
			queriessubmenu.enterHsiItemNumber(itemcodelist.get(i)).clickSeachButton().clickOnFirstResult();

			// verify print message
			String printMessage = productdetail.getItemField(map.get("fieldIndex"));
			Assertions.assertThat(printMessage).isEqualTo(printmessagelist.get(i));
			log(LogType.INFO, "Verifying Print Message");
			// verifying linetype code
			productdetail.selectTabfromDropdown(map.get("GlobalAttributeTab"));
			linetypecode = globalattribute.getLineTypeCode();
			Assertions.assertThat(linetypecode).isEqualTo(map.get("LineTypeCode"));
			log(LogType.INFO, "Verifying line type code");

			pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
			classificationpage.clickOnExpandButton();
//			znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
//					map.get("structurePath"));
//			Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
//			classificationpage.clickStructureGroup();
//			classificationpage.removeButton();
//			classificationpage.clickYes();
			// verify quality status
			pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
			qualitystatus.sortRulestByLastExecution();
			List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("ruleStatus"));
			Assertions.assertThat(catalogentityrule).containsAnyOf(map.get("CatalogEntityRule"));
			log(LogType.INFO, "Verifying catalog entity rule");
			pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
			pimHomepage.productDetailSearchPage().clickRefreshIcon();
			znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
					map.get("structurePath"));
			Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
			classificationpage.clickOnCloseExpandButton();
			productdetail.clickMenuRefreshIcon();
		}
		// verify in master
		for (int j = 0; j <= itemcodelist.size() - 1; j++) {
			queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog"));
			queriessubmenu.enterHsiItemNumber(itemcodelist.get(j)).clickSeachButton().clickOnFirstResult();

			// verify print message
			String printMessage = productdetail.getItemField(map.get("fieldIndex"));
			Assertions.assertThat(printMessage).isEqualTo(printmessagelist.get(j));
			log(LogType.INFO, "Verifying Print Message");
			// verifying linetype code
			productdetail.selectTabfromDropdown(map.get("GlobalAttributeTab"));
			linetypecode = globalattribute.getLineTypeCode();
			Assertions.assertThat(linetypecode).isEqualTo(map.get("LineTypeCode"));
			log(LogType.INFO, "Verifying line type code");

			pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
			classificationpage.clickOnExpandButton();
			znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
					map.get("structurePath"));
			Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
			classificationpage.clickOnCloseExpandButton();
			productdetail.clickMenuRefreshIcon();
		}
		pimHomepage.clickLogoutButton();
	}

	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemMessage)
	@Test(priority = 1, description = "ITM_082 Verification Part | ITM_082 | Verifying Item Message based on Location Type", groups = { "US",
			"pim","ITEM_MESSAGE" }, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verifyItemMessageforLocationType(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult();

		// verifying location type
		String locationtype = productdetail.getItemField(map.get("fieldIndex"));
		Assertions.assertThat(locationtype).isEqualTo(map.get("LocationType"));
		log(LogType.INFO, "Verifying Location type");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO, "Navigate to Classification tab after verifying the location type in US Catalog");

		classificationpage.clickOnExpandButton();
//		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
//				map.get("structurePath"));
//		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
//		classificationpage.clickStructureGroup();
//		classificationpage.removeButton();
//		classificationpage.clickYes();

		// verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		qualitystatus.sortRulestByLastExecution();
		List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("ruleStatus"));
		Assertions.assertThat(catalogentityrule).containsAnyOf(map.get("CatalogEntityRule"));
		log(LogType.INFO, "Verifying catalog entity rule");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		zcanadaglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(zcanadaglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		log(LogType.INFO, "Verifying item message code.");
		classificationpage.clickOnCloseExpandButton();

		// verify in master
		productdetail.clickMenuRefreshIcon();
		queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog"))
				.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult();
		// verifying location type
		locationtype = productdetail.getItemField(map.get("fieldIndex"));
		Assertions.assertThat(locationtype).isEqualTo(map.get("LocationType"));
		log(LogType.INFO, "Verifying location type in Master catalog");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO, "Navigate to Classification tab after verifying the location type in Master Catalog");

		classificationpage.clickOnExpandButton();
		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickOnCloseExpandButton();
		pimHomepage.clickLogoutButton();
	}

	// ITM_135,ITM_140,ITM_142
	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemMessage)
	@Test(priority = 1, description = "ITM_135, Verification Part | ITM_135,ITM_140,ITM_142 | Verify the item message for Availability Code & Cat code inc in CA account", groups = {
			"CA", "pim","ITEM_MESSAGE" }, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verifyItemMessageforAvailabilityCodeIncCatcodeIncCaUser(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickSettingIcon().clickFieldSelectionOption();
		log(LogType.INFO, "Navigate to Field Selection Page from Setting Icon for CA Catalog");

		fieldselectionpage.enterFieldName(map.get("Header")).clickHeaderText(map.get("HeaderText")).clickAddButton()
				.clickOkButton();
		log(LogType.INFO, "Cat code is added in header");

		productdetail.clickOnFirstResult();

		// verify availability code
		String availabilitycode = productdetail.getItemField(map.get("fieldIndex"));
		Assertions.assertThat(availabilitycode).isEqualTo(map.get("AvailabilityCode"));
		log(LogType.INFO, "Verifying Availability Code");
		// verify cat code
		String catcode = productdetail.getItemField(map.get("fieldIndex1"));
		Assertions.assertThat(catcode).isEqualTo(map.get("CatCode"));
		log(LogType.INFO, "Verifying Cat Code");
		
		
		  pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get(
		  "Classificationtab")); log(LogType.INFO,
		  "Navigate to Classification tab after verifying the availability code inc. & cat code incc. in CA Catalog"
		  );
		  
		  classificationpage.clickOnExpandButton(); 
//		  zcanadaglobalerrormessage =
//		  classificationpage.getStructureGroup(map.get("structureSystem"),
//		  map.get("structurePath"));
//		  Assertions.assertThat(zcanadaglobalerrormessage).isEqualTo(map.get(
//		  "ItemMessageCode")); classificationpage.clickStructureGroup();
//		  classificationpage.removeButton(); BasePage.WaitForMiliSec(5000);
//		  classificationpage.clickYes();
		 

		
		  // verify quality status
		  pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get(
		  "Qualitystatustab")); qualitystatus.sortRulestByLastExecution(); List<String>
		  catalogentityrule =
		  qualitystatus.getRuleNamesByRuleStatus(map.get("ruleStatus"));
		  Assertions.assertThat(catalogentityrule).containsAnyOf(map.get(
		  "CatalogEntityRule")); log(LogType.INFO, "Verifying catalog entity rule");
		  
		  pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get(
		  "Classificationtab"));
		  pimHomepage.productDetailSearchPage().clickRefreshIcon();
		  zcanadaglobalerrormessage =
		  classificationpage.getStructureGroup(map.get("structureSystem"),
		  map.get("structurePath"));
		  Assertions.assertThat(zcanadaglobalerrormessage).isEqualTo(map.get(
		  "ItemMessageCode")); log(LogType.INFO, "Verifying item message code.");
		  classificationpage.clickOnCloseExpandButton();
		 

		// verify in master
		productdetail.clickMenuRefreshIcon();
		queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog"))
				.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton();
		
        productDetailSearchPage.applyFilterInSearchPageByAnyValue(map.get("HeaderText"), map.get("CatCode")).clickOnFirstResult();

		
		// verify availability code
		availabilitycode = productdetail.getItemField(map.get("fieldIndex"));
		Assertions.assertThat(availabilitycode).isEqualTo(map.get("AvailabilityCode"));
		log(LogType.INFO, "Verifying Availability Code");
		// verify cat code
		catcode = productdetail.getItemField(map.get("fieldIndex1"));
		Assertions.assertThat(catcode).isEqualTo(map.get("CatCode"));
		log(LogType.INFO, "Verifying Cat Code");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO,
				"Navigate to Classification tab after verifying the MPC code inc. & Sub-MPC exc. in Master Catalog");

		classificationpage.clickOnExpandButton();
		zcanadaglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(zcanadaglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickOnCloseExpandButton();
		pimHomepage.clickLogoutButton();
	}

	// ITM_141
	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemMessage)
	@Test(priority = 1, description = "ITM_141 Verification Part | ITM_141 | Verify the item message for Availability Code & Cat code inc in CA account", groups = {
			"CA", "pim","ITEM_MESSAGE" }, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verifyItemMessageforAvailabilityCodeIncCatcodeIncCaUserForD57(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickSettingIcon().clickFieldSelectionOption();
		log(LogType.INFO, "Navigate to Field Selection Page from Setting Icon for CA Catalog");

		fieldselectionpage.enterFieldName(map.get("Header")).clickHeaderText(map.get("HeaderText")).clickAddButton()
				.clickOkButton();
		log(LogType.INFO, "Cat code is added in header");

		productdetail.clickOnFirstResult();

		// verify availability code
		String availabilitycode = productdetail.getItemField(map.get("fieldIndex"));
		Assertions.assertThat(availabilitycode).isEqualTo(map.get("AvailabilityCode"));
		log(LogType.INFO, "Verifying Availability Code");
		// verify cat code
		String catcode = productdetail.getItemField(map.get("fieldIndex1"));
		Assertions.assertThat(catcode).isEqualTo(map.get("CatCode"));
		log(LogType.INFO, "Verifying Cat Code");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO,
				"Navigate to Classification tab after verifying the availability code inc. & cat code incc. in CA Catalog");

		classificationpage.clickOnExpandButton();
//		zcanadaglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
//				map.get("structurePath"));
//		Assertions.assertThat(zcanadaglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
//		classificationpage.clickStructureGroup();
//		classificationpage.removeButton();
//		classificationpage.clickYes();

		// verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		qualitystatus.sortRulestByLastExecution();
		List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("ruleStatus"));
		Assertions.assertThat(catalogentityrule).containsAnyOf(map.get("CatalogEntityRule"));
		log(LogType.INFO, "Verifying catalog entity rule");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		zcanadaglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(zcanadaglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		log(LogType.INFO, "Verifying item message code.");
		classificationpage.clickOnCloseExpandButton();

		// verify in master
		productdetail.clickMenuRefreshIcon();
		queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog"))
				.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnSecondResult();
		// verify availability code
		availabilitycode = productdetail.getItemField(map.get("fieldIndex"));
		Assertions.assertThat(availabilitycode).isEqualTo(map.get("AvailabilityCode"));
		log(LogType.INFO, "Verifying Availability Code");
		// verify cat code
		catcode = productdetail.getItemField(map.get("fieldIndex1"));
		Assertions.assertThat(catcode).isEqualTo(map.get("CatCode"));
		log(LogType.INFO, "Verifying Cat Code");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO,
				"Navigate to Classification tab after verifying the MPC code inc. & Sub-MPC exc. in Master Catalog");

		classificationpage.clickOnExpandButton();
		zcanadaglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(zcanadaglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickOnCloseExpandButton();
		pimHomepage.clickLogoutButton();
	}

	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemMessage)
	@Test(priority = 1, description = "ITM_052 Verification Part | ITM_052, ITM_059, ITM_069 | Verifying Item Message based on Pricing Group", groups = { "US",
			"pim","ITEM_MESSAGE" }, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verifyItemMessageForPricingGroup(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickSettingIcon().clickFieldSelectionOption();
		log(LogType.INFO, "Navigate to Field Selection Page from Setting Icon for US Catalog");

		fieldselectionpage.enterFieldName(map.get("Header")).clickHeaderText(map.get("HeaderText")).clickAddButton()
				.clickOkButton();
		log(LogType.INFO, "Print Message header is added in header");

		productdetail.clickOnFirstResult();
		// verify pricing group
		String pricingGroup = productdetail.getItemField(map.get("fieldIndex"));
		Assertions.assertThat(pricingGroup).isEqualTo(map.get("PricingGroup"));
		log(LogType.INFO, "Verifying Pricing Group.");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		classificationpage.clickOnExpandButton();
//		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
//				map.get("structurePath"));
//		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
//		classificationpage.clickStructureGroup();
//		classificationpage.removeButton();
//		classificationpage.clickYes();

		// verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		qualitystatus.sortRulestByLastExecution();
		List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("ruleStatus"));
		Assertions.assertThat(catalogentityrule).containsAnyOf(map.get("CatalogEntityRule"));
		log(LogType.INFO, "Verifying catalog entity rule");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		log(LogType.INFO, "Verifying item message code.");
		classificationpage.clickOnCloseExpandButton();

		// verify in master
		productdetail.clickMenuRefreshIcon();
		queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog"))
				.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult();

		// verify pricing group
		pricingGroup = productdetail.getItemField(map.get("fieldIndex"));
		Assertions.assertThat(pricingGroup).isEqualTo(map.get("PricingGroup"));

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		classificationpage.clickOnExpandButton();
		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickOnCloseExpandButton();
		pimHomepage.clickLogoutButton();
	}

	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemMessage)
	@Test(priority = 1, description = "ITM_089 Verification Part | ITM_089 | Verifying Item Message based on Federal Drug Acct Code", groups = { "US",
			"pim","ITEM_MESSAGE" }, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verifyItemMessageforFDAC(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));

		// verifying FDAC
		fdac = globalattribute.getFdac();
		Assertions.assertThat(fdac).isEqualTo(map.get("FDAC"));
		log(LogType.INFO, "Verifying Federal Drug Acct Code");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO, "Navigate to Classification tab after verifying the fdac in US Catalog");

		classificationpage.clickOnExpandButton();
//		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
//				map.get("structurePath"));
//		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
//		classificationpage.clickStructureGroup();
//		classificationpage.removeButton();
//		classificationpage.clickYes();

		// verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		qualitystatus.sortRulestByLastExecution();
		List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("ruleStatus"));
		Assertions.assertThat(catalogentityrule).containsAnyOf(map.get("CatalogEntityRule"));
		log(LogType.INFO, "Verifying catalog entity rule");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		zcanadaglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(zcanadaglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		log(LogType.INFO, "Verifying item message code.");
		classificationpage.clickOnCloseExpandButton();

		// verify in master
		productdetail.clickMenuRefreshIcon();
		queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog"))
				.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult()
				.selectTabfromDropdown(map.get("GlobalAttributeTab"));
		// verifying FDAC
		fdac = globalattribute.getFdac();
		Assertions.assertThat(fdac).isEqualTo(map.get("FDAC"));
		log(LogType.INFO, "Verifying Federal Drug Acct Code in master catalog");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO, "Navigate to Classification tab after verifying the fdac in Master Catalog");

		classificationpage.clickOnExpandButton();
		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickOnCloseExpandButton();
		pimHomepage.clickLogoutButton();
	}

	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemMessage)
	@Test(priority = 1, description = "ITM_101 Verification Part | ITM_101 | Verify the item message for MPC in US account", groups = { "US",
			"pim","ITEM_MESSAGE" }, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verifyItemMessageforMPC(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult();

		// verifying major product class code
		String majorproductclasscode = productdetail.getItemField(map.get("fieldIndex"));
		Assertions.assertThat(majorproductclasscode).isEqualTo(map.get("MPC"));
		log(LogType.INFO, "Verifying Major Product Class code");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO, "Navigate to Classification tab after verifying the MPC in US Catalog");

		classificationpage.clickOnExpandButton();
//		zcanadaglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
//				map.get("structurePath"));
//		Assertions.assertThat(zcanadaglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
//		classificationpage.clickStructureGroup();
//		classificationpage.removeButton();
//		classificationpage.clickYes();

		// verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		qualitystatus.sortRulestByLastExecution();
		List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("ruleStatus"));
		Assertions.assertThat(catalogentityrule).containsAnyOf(map.get("CatalogEntityRule"));
		log(LogType.INFO, "Verifying catalog entity rule");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		zcanadaglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(zcanadaglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		log(LogType.INFO, "Verifying item message code.");
		classificationpage.clickOnCloseExpandButton();

		// verify in master
		productdetail.clickMenuRefreshIcon();
		queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog"))
				.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult();
		// verifying major product class code
		majorproductclasscode = productdetail.getItemField(map.get("fieldIndex"));
		Assertions.assertThat(majorproductclasscode).isEqualTo(map.get("MPC"));
		log(LogType.INFO, "Verifying Major Product Class code in Master catalog");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO, "Navigate to Classification tab after verifying the MPC in Master Catalog");

		classificationpage.clickOnExpandButton();
		zcanadaglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(zcanadaglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickOnCloseExpandButton();
		pimHomepage.clickLogoutButton();
	}

	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemMessage)
	@Test(priority = 1, description = "ITM_096 Verification Part | ITM_096 | Verify the item message for Availability Code & Cat code inc in US account", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class,groups = { "US",
			"pim","ITEM_MESSAGE" })
	public void verifyItemMessageforAvailabilityCodeIncCatcodeInc(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickSettingIcon().clickFieldSelectionOption();
		log(LogType.INFO, "Navigate to Field Selection Page from Setting Icon for US Catalog");

		fieldselectionpage.enterFieldName(map.get("Header")).clickHeaderText(map.get("HeaderText")).clickAddButton()
				.clickOkButton();
		log(LogType.INFO, "Cat code is added in header");

		productdetail.clickOnFirstResult();

		// verify availability code
		String availabilitycode = productdetail.getItemField(map.get("fieldIndex"));
		Assertions.assertThat(availabilitycode).isEqualTo(map.get("AvailabilityCode"));
		log(LogType.INFO, "Verifying Availability Code");
		// verify cat code
		String catcode = productdetail.getItemField(map.get("fieldIndex1"));
		Assertions.assertThat(catcode).isEqualTo(map.get("CatCode"));
		log(LogType.INFO, "Verifying Cat Code");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO,
				"Navigate to Classification tab after verifying the availability code inc. & cat code inc. in US Catalog");

		classificationpage.clickOnExpandButton();
//		zcanadaglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
//				map.get("structurePath"));
//		Assertions.assertThat(zcanadaglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
//		classificationpage.clickStructureGroup();
//		classificationpage.removeButton();
//		classificationpage.clickYes();

		// verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		qualitystatus.sortRulestByLastExecution();
		List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("ruleStatus"));
		Assertions.assertThat(catalogentityrule).containsAnyOf(map.get("CatalogEntityRule"));
		log(LogType.INFO, "Verifying catalog entity rule");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		zcanadaglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(zcanadaglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		log(LogType.INFO, "Verifying item message code.");
		classificationpage.clickOnCloseExpandButton();

		// verify in master
		productdetail.clickMenuRefreshIcon();
		queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog"))
				.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult();
		// verify availability code
		availabilitycode = productdetail.getItemField(map.get("fieldIndex"));
		Assertions.assertThat(availabilitycode).isEqualTo(map.get("AvailabilityCode"));
		log(LogType.INFO, "Verifying Availability Code");
		// verify cat code
		catcode = productdetail.getItemField(map.get("fieldIndex1"));
		Assertions.assertThat(catcode).isEqualTo(map.get("CatCode"));
		log(LogType.INFO, "Verifying Cat Code");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO,
				"Navigate to Classification tab after verifying the MPC code inc. & Sub-MPC exc. in Master Catalog");

		classificationpage.clickOnExpandButton();
		zcanadaglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(zcanadaglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickOnCloseExpandButton();
		pimHomepage.clickLogoutButton();
	}

	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemMessage)
	@Test(priority = 1, description = "ITM_108 Verification Part | ITM_108 | Verifying Item Message based on multiple supplier code", groups = { "US",
			"pim","ITEM_MESSAGE" }, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verifyItemMessageforMultipleSupplierCode(Map<String, String> map) throws Throwable {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		List<String> itemcodelist = javautil.readMultipleValuesFromExcel(map.get("ItemNumber"));
		List<String> suppliercodelist = javautil.readMultipleValuesFromExcel(map.get("SupplierCode"));
		for (int i = 0; i <= itemcodelist.size() - 1; i++) {
			pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
					.selectCatalogType(map.get("CatalogType"));
			queriessubmenu.enterHsiItemNumber(itemcodelist.get(i)).clickSeachButton().clickOnFirstResult()
					.selectTabfromDropdown(map.get("GlobalAttributeTab"));
			log(LogType.INFO, "Navigate to Global attribute tab to verify supplier code in US Catalog");

			// verify supplier code
			supplierCode = globalattribute.getSuppliercode();
			Assertions.assertThat(supplierCode).isEqualTo(suppliercodelist.get(i));
			productdetail.selectTabfromDropdown(map.get("Classificationtab"));
			log(LogType.INFO, "Navigate to Classification tab after verifying the supplier code in US Catalog");

			classificationpage.clickOnExpandButton();
//			classificationpage.sortStructureGroup();
//			znewglobalerrormessage = classificationpage.getStructureGroupHavingBlankSpace(map.get("structureSystem"),
//					map.get("structurePath"));
//			Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
//			classificationpage.clickStructureGroup();
//			classificationpage.removeButton();
//			classificationpage.clickYes();

			// verify quality status
			pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
			qualitystatus.sortRulestByLastExecution();
			List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("ruleStatus"));
			Assertions.assertThat(catalogentityrule).containsAnyOf(map.get("CatalogEntityRule"));
			log(LogType.INFO, "Verifying catalog entity rule");

			pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
			pimHomepage.productDetailSearchPage().clickRefreshIcon();
			classificationpage.sortStructureGroup();
			znewglobalerrormessage = classificationpage.getStructureGroupHavingBlankSpace(map.get("structureSystem"),
					map.get("structurePath"));
			Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
			classificationpage.clickOnCloseExpandButton();
			productdetail.clickMenuRefreshIcon();
		}

		// verify in master
		for (int j = 0; j <= itemcodelist.size() - 1; j++) {
			queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog"));
			queriessubmenu.enterHsiItemNumber(itemcodelist.get(j)).clickSeachButton().clickOnFirstResult()
					.selectTabfromDropdown(map.get("GlobalAttributeTab"));

			// verify supplier code
			supplierCode = globalattribute.getSuppliercode();
			Assertions.assertThat(supplierCode).isEqualTo(suppliercodelist.get(j));
			productdetail.selectTabfromDropdown(map.get("Classificationtab"));
			log(LogType.INFO, "Navigate to Classification tab after verifying the supplier code in Master Catalog");

			classificationpage.clickOnExpandButton();
			classificationpage.sortStructureGroup();
			znewglobalerrormessage = classificationpage.getStructureGroupHavingBlankSpace(map.get("structureSystem"),
					map.get("structurePath"));
			Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
			classificationpage.clickOnCloseExpandButton();
			productdetail.clickMenuRefreshIcon();
		}
		pimHomepage.clickLogoutButton();
	}

	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemMessage)
	@Test(priority = 1, description = "ITM_110 Verification Part | ITM_110 | Verifying Item Message based on multiple fdac", groups = { "US",
			"pim","ITEM_MESSAGE" }, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verifyItemMessageforMultipleFdac(Map<String, String> map) throws Throwable {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		List<String> itemcodelist = javautil.readMultipleValuesFromExcel(map.get("ItemNumber"));
		List<String> fdaclist = javautil.readMultipleValuesFromExcel(map.get("FDAC"));
		for (int i = 0; i <= itemcodelist.size() - 1; i++) {
			pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
					.selectCatalogType(map.get("CatalogType"));
			queriessubmenu.enterHsiItemNumber(itemcodelist.get(i)).clickSeachButton().clickOnFirstResult()
					.selectTabfromDropdown(map.get("GlobalAttributeTab"));
			log(LogType.INFO, "Navigate to Global attribute tab to verify fdac in US Catalog");

			// verify fdac
			fdac = globalattribute.getFdac();
			Assertions.assertThat(fdac).isEqualTo(fdaclist.get(i));
			productdetail.selectTabfromDropdown(map.get("Classificationtab"));
			log(LogType.INFO, "Navigate to Classification tab after verifying the fdac in US Catalog");

			classificationpage.clickOnExpandButton();
//			classificationpage.sortStructureGroup();
//			znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
//					map.get("structurePath"));
//			Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
//			classificationpage.clickStructureGroup();
//			classificationpage.removeButton();
//			classificationpage.clickYes();

			// verify quality status
			pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
			qualitystatus.sortRulestByLastExecution();
			List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("ruleStatus"));
			Assertions.assertThat(catalogentityrule).containsAnyOf(map.get("CatalogEntityRule"));
			log(LogType.INFO, "Verifying catalog entity rule");

			pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
			pimHomepage.productDetailSearchPage().clickRefreshIcon();
			classificationpage.sortStructureGroup();
			znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
					map.get("structurePath"));
			Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
			classificationpage.clickOnCloseExpandButton();
			productdetail.clickMenuRefreshIcon();
		}

		// verify in master
		for (int j = 0; j <= itemcodelist.size() - 1; j++) {
			queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog"));
			queriessubmenu.enterHsiItemNumber(itemcodelist.get(j)).clickSeachButton().clickOnFirstResult()
					.selectTabfromDropdown(map.get("GlobalAttributeTab"));

			// verify fdac
			fdac = globalattribute.getFdac();
			Assertions.assertThat(fdac).isEqualTo(fdaclist.get(j));
			productdetail.selectTabfromDropdown(map.get("Classificationtab"));
			log(LogType.INFO, "Navigate to Classification tab after verifying the fdac in Master Catalog");

			classificationpage.clickOnExpandButton();
			classificationpage.sortStructureGroup();
			znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
					map.get("structurePath"));
			Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
			classificationpage.clickOnCloseExpandButton();
			productdetail.clickMenuRefreshIcon();
		}
		pimHomepage.clickLogoutButton();
	}

	// ITM_087
	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemMessage)
	@Test(priority = 10, description = "ITM_087 Verification Part | ITM_087 | Verifying Item Message based on two fdac", groups = { "US",
			"pim","ITEM_MESSAGE" }, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verifyItemMessageforTwoFdac(Map<String, String> map) throws Throwable {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		List<String> itemcodelist = javautil.readMultipleValuesFromExcel(map.get("ItemNumber"));
		List<String> fdaclist = javautil.readMultipleValuesFromExcel(map.get("FDAC"));
		for (int i = 0; i <= itemcodelist.size() - 1; i++) {
			pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
					.selectCatalogType(map.get("CatalogType"));
			queriessubmenu.enterHsiItemNumber(itemcodelist.get(i)).clickSeachButton().clickOnFirstResult()
					.selectTabfromDropdown(map.get("GlobalAttributeTab"));
			log(LogType.INFO, "Navigate to Global attribute tab to verify fdac in US Catalog");

			// verify fdac
			fdac = globalattribute.getFdac();
			Assertions.assertThat(fdac).isEqualTo(fdaclist.get(i));
			productdetail.selectTabfromDropdown(map.get("Classificationtab"));
			log(LogType.INFO, "Navigate to Classification tab after verifying the fdac in US Catalog");

			classificationpage.clickOnExpandButton();
//			classificationpage.sortStructureGroup();
//			znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
//					map.get("structurePath"));
//			Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
//			classificationpage.clickStructureGroup();
//			classificationpage.removeButton();
//			classificationpage.clickYes();

			// verify quality status
			pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
			qualitystatus.sortRulestByLastExecution();
			List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("ruleStatus"));
			Assertions.assertThat(catalogentityrule).containsAnyOf(map.get("CatalogEntityRule"));
			log(LogType.INFO, "Verifying catalog entity rule");

			pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
			pimHomepage.productDetailSearchPage().clickRefreshIcon();
			classificationpage.sortStructureGroup();
			znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
					map.get("structurePath"));
			Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
			classificationpage.clickOnCloseExpandButton();
			productdetail.clickMenuRefreshIcon();
		}

		// verify in master
		for (int j = 0; j <= itemcodelist.size() - 1; j++) {
			queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog"));
			queriessubmenu.enterHsiItemNumber(itemcodelist.get(j)).clickSeachButton().clickOnFirstResult()
					.selectTabfromDropdown(map.get("GlobalAttributeTab"));

			// verify fdac
			fdac = globalattribute.getFdac();
			Assertions.assertThat(fdac).isEqualTo(fdaclist.get(j));
			productdetail.selectTabfromDropdown(map.get("Classificationtab"));
			log(LogType.INFO, "Navigate to Classification tab after verifying the fdac in Master Catalog");

			classificationpage.clickOnExpandButton();
			classificationpage.sortStructureGroup();
			znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
					map.get("structurePath"));
			Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
			classificationpage.clickOnCloseExpandButton();
			productdetail.clickMenuRefreshIcon();
		}
		pimHomepage.clickLogoutButton();
	}

	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemMessage)
	@Test(priority = 1, description = "ITM_117 Verification Part | ITM_117 | Verifying Item Message based on Availability code as inc. & SupplierCode as exc.", groups = {
			"US", "pim" ,"ITEM_MESSAGE"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verifyItemMessageforAvailabilityCodeIncAndSupplierCodeExc(Map<String, String> map) throws Throwable {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult();

		// verifying availability code
		String availabilitycode = productdetail.getItemField(map.get("fieldIndex"));
		Assertions.assertThat(availabilitycode).isEqualTo(map.get("AvailabilityCode"));
		log(LogType.INFO, "Verifying availability code");

		productdetail.selectTabfromDropdown(map.get("GlobalAttributeTab"));
		log(LogType.INFO, "Navigate to global attribute tab to verify supplier code");

		// verifying supplier code
		String suppliercode = globalattribute.getSuppliercode();
		Assertions.assertThat(suppliercode).isNotEqualTo(map.get("SupplierCode"));

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO, "Navigate to Classification tab after verifying the supplier code in US Catalog");

		classificationpage.clickOnExpandButton();
//		znewglobalerrormessage = classificationpage.getStructureGroupHavingBlankSpace(map.get("structureSystem"),
//				map.get("structurePath"));
//		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
//		classificationpage.clickStructureGroup();
//		classificationpage.removeButton();
//		classificationpage.clickYes();

		// verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		qualitystatus.sortRulestByLastExecution();
		List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("ruleStatus"));
		Assertions.assertThat(catalogentityrule).containsAnyOf(map.get("CatalogEntityRule"));
		log(LogType.INFO, "Verifying catalog entity rule");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		znewglobalerrormessage = classificationpage.getStructureGroupHavingBlankSpace(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickOnCloseExpandButton();

		// verify in master
		productdetail.clickMenuRefreshIcon();
		queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog"))
				.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult();

		// verifying availability code in master
		availabilitycode = productdetail.getItemField(map.get("fieldIndex"));
		Assertions.assertThat(availabilitycode).isEqualTo(map.get("AvailabilityCode"));
		log(LogType.INFO, "Verifying availability code in master");

		productdetail.selectTabfromDropdown(map.get("GlobalAttributeTab"));
		log(LogType.INFO, "Navigate to global attribute tab to verify supplier code in master");

		// verifying supplier code in master
		suppliercode = globalattribute.getSuppliercode();
		Assertions.assertThat(suppliercode).isNotEqualTo(map.get("SupplierCode"));

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO, "Navigate to Classification tab after verifying the supplier code in Master Catalog");

		classificationpage.clickOnExpandButton();
		znewglobalerrormessage = classificationpage.getStructureGroupHavingBlankSpace(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickOnCloseExpandButton();
		pimHomepage.clickLogoutButton();
	}

	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemMessage)
	@Test(priority = 1, description = "ITM_105 Verification Part | ITM_105 | Verifying Item Message based on multiple classification code", groups = { "US",
			"pim" ,"ITEM_MESSAGE"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verifyItemMessageforMultipleClassificationCode(Map<String, String> map) throws Throwable {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		List<String> itemcodelist = javautil.readMultipleValuesFromExcel(map.get("ItemNumber"));
		List<String> classificationcodelist = javautil.readMultipleValuesFromExcel(map.get("ClassificationCode"));

		for (int i = 0; i <= itemcodelist.size() - 1; i++) {
			pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
					.selectCatalogType(map.get("CatalogType"));
			queriessubmenu.enterHsiItemNumber(itemcodelist.get(i)).clickSeachButton().clickOnFirstResult()
					.selectTabfromDropdown(map.get("GlobalAttributeTab"));
			log(LogType.INFO, "Navigate to Global attribute tab to verify classification code in US Catalog");

			// verify classification code
			classificationcode = globalattribute.getClassificationCode();
			Assertions.assertThat(classificationcode).isEqualTo(classificationcodelist.get(i));
			productdetail.selectTabfromDropdown(map.get("Classificationtab"));
			log(LogType.INFO, "Navigate to Classification tab after verifying the classification code in US Catalog");

			classificationpage.clickOnExpandButton();
//			classificationpage.sortStructureGroup();
//			znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
//					map.get("structurePath"));
//			Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
//			classificationpage.clickStructureGroup();
//			classificationpage.removeButton();
//			classificationpage.clickYes();

			// verify quality status
			pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
			qualitystatus.sortRulestByLastExecution();
			List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("ruleStatus"));
			Assertions.assertThat(catalogentityrule).containsAnyOf(map.get("CatalogEntityRule"));
			log(LogType.INFO, "Verifying catalog entity rule");

			pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
			pimHomepage.productDetailSearchPage().clickRefreshIcon();
			classificationpage.sortStructureGroup();
			znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
					map.get("structurePath"));
			Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
			classificationpage.clickOnCloseExpandButton();
			productdetail.clickMenuRefreshIcon();
		}

		// verify in master
		for (int j = 0; j <= itemcodelist.size() - 1; j++) {
			queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog"));
			queriessubmenu.enterHsiItemNumber(itemcodelist.get(j)).clickSeachButton().clickOnFirstResult()
					.selectTabfromDropdown(map.get("GlobalAttributeTab"));

			// verify classification code in master
			classificationcode = globalattribute.getClassificationCode();
			Assertions.assertThat(classificationcode).isEqualTo(classificationcodelist.get(j));
			productdetail.selectTabfromDropdown(map.get("Classificationtab"));
			log(LogType.INFO,
					"Navigate to Classification tab after verifying the classification code in Master Catalog");

			classificationpage.clickOnExpandButton();
			classificationpage.sortStructureGroup();
			znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
					map.get("structurePath"));
			Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
			classificationpage.clickOnCloseExpandButton();
			productdetail.clickMenuRefreshIcon();
		}
		pimHomepage.clickLogoutButton();
	}

	// ITM_120
	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.Data_Issue)
	@TestDataSheet(sheetname = TestCaseSheet.ItemMessage)
	@Test(priority = 10, description = "ITM_120 Verification Part | ITM_120 | Verifying Item Message based on MPC code inc. & Manufacture code inc.", groups = {
			"US", "pim","ITEM_MESSAGE" }, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)

	public void verifyItemMessageforMPCCodeIncAndManufactureCodeInc(Map<String, String> map) throws Throwable {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult();

		// verifying mpc code
		String mpccode = productdetail.getItemField(map.get("fieldIndex"));
		Assertions.assertThat(mpccode).isEqualTo(map.get("MPC"));
		log(LogType.INFO, "Verifying mpc code");

		productdetail.selectTabfromDropdown(map.get("GlobalAttributeTab"));
		log(LogType.INFO, "Navigate to global attribute tab to verify manufacture code");

		// verifying manufacture code
		manufacturecode = globalattribute.getManufacturercode();
		Assertions.assertThat(manufacturecode).isEqualTo(map.get("ManufactureCode"));

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO, "Navigate to Classification tab after verifying the manufacture code in US Catalog");

		classificationpage.clickOnExpandButton();
//		znewglobalerrormessage = classificationpage.getStructureGroupHavingBlankSpace(map.get("structureSystem"),
//				map.get("structurePath"));
//		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
//		classificationpage.clickStructureGroup();
//		classificationpage.removeButton();
//		classificationpage.clickYes();

		// verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		qualitystatus.sortRulestByLastExecution();
		List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("ruleStatus"));
		Assertions.assertThat(catalogentityrule).containsAnyOf(map.get("CatalogEntityRule"));
		log(LogType.INFO, "Verifying catalog entity rule");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		znewglobalerrormessage = classificationpage.getStructureGroupHavingBlankSpace(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickOnCloseExpandButton();

		// verify in master
		productdetail.clickMenuRefreshIcon();
		queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog"))
				.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult();

		// verifying mpc code in master
		mpccode = productdetail.getItemField(map.get("fieldIndex"));
		Assertions.assertThat(mpccode).isEqualTo(map.get("MPC"));
		log(LogType.INFO, "Verifying mpc code in master");

		productdetail.selectTabfromDropdown(map.get("GlobalAttributeTab"));
		log(LogType.INFO, "Navigate to global attribute tab to verify manufacture code in master");

		// verifying manufacture code in master
		manufacturecode = globalattribute.getManufacturercode();
		Assertions.assertThat(manufacturecode).isEqualTo(map.get("ManufactureCode"));

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO, "Navigate to Classification tab after verifying the manufacture code in Master Catalog");

		classificationpage.clickOnExpandButton();
		znewglobalerrormessage = classificationpage.getStructureGroupHavingBlankSpace(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickOnCloseExpandButton();
		pimHomepage.clickLogoutButton();
	}

	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemMessage)
	@Test(priority = 1, description = "ITM_090 Verification Part | ITM_090 | Verifying Item Message based on mpc code inc. and multiple sub-mpc code inc.", groups = { "US",
			"pim","ITEM_MESSAGE" }, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verifyItemMessageforMpcCodeIncAndMultipleSubMpcCodeInc(Map<String, String> map) throws Throwable {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		List<String> itemcodelist = javautil.readMultipleValuesFromExcel(map.get("ItemNumber"));
		List<String> submpccodelist = javautil.readMultipleValuesFromExcel(map.get("SubMPC"));

		for (int i = 0; i <= itemcodelist.size() - 1; i++) {
			pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
					.selectCatalogType(map.get("CatalogType"));
			queriessubmenu.enterHsiItemNumber(itemcodelist.get(i)).clickSeachButton().clickOnFirstResult();

			// verifying mpc code
			String mpccode = productdetail.getItemField(map.get("fieldIndex"));
			Assertions.assertThat(mpccode).isEqualTo(map.get("MPC"));
			log(LogType.INFO, "Verifying mpc code");

			// verifying submpc code

			String submpccode = productdetail.getItemField(map.get("fieldIndex1"));
			Assertions.assertThat(submpccode).isEqualTo(submpccodelist.get(i));
			log(LogType.INFO, "Verifying sub-mpc code");

			pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
			log(LogType.INFO, "Navigate to Classification tab after verifying the mpc and sub-mpc code in US Catalog");

			classificationpage.clickOnExpandButton();
//			classificationpage.sortStructureGroup();
//			znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
//					map.get("structurePath"));
//			Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
//			classificationpage.clickStructureGroup();
//			classificationpage.removeButton();
//			classificationpage.clickYes();

			// verify quality status
			pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
			qualitystatus.sortRulestByLastExecution();
			List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("ruleStatus"));
			Assertions.assertThat(catalogentityrule).containsAnyOf(map.get("CatalogEntityRule"));
			log(LogType.INFO, "Verifying catalog entity rule");

			pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
			pimHomepage.productDetailSearchPage().clickRefreshIcon();
			classificationpage.sortStructureGroup();
			znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
					map.get("structurePath"));
			Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
			classificationpage.clickOnCloseExpandButton();
			productdetail.clickMenuRefreshIcon();
		}

		// verify in master
		for (int j = 0; j <= itemcodelist.size() - 1; j++) {
			queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog"));
			queriessubmenu.enterHsiItemNumber(itemcodelist.get(j)).clickSeachButton().clickOnFirstResult();

			// verifying mpc code in master
			String mpccode = productdetail.getItemField(map.get("fieldIndex"));
			Assertions.assertThat(mpccode).isEqualTo(map.get("MPC"));
			log(LogType.INFO, "Verifying mpc code");

			// verifying submpc code in master
			String submpccode = productdetail.getItemField(map.get("fieldIndex1"));
			Assertions.assertThat(submpccode).isEqualTo(submpccodelist.get(j));
			log(LogType.INFO, "Verifying sub-mpc code");

			pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
			log(LogType.INFO, "Navigate to Classification tab after verifying the mpc and sub-mpc code in US Catalog");

			classificationpage.clickOnExpandButton();
			classificationpage.sortStructureGroup();
			znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
					map.get("structurePath"));
			Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
			classificationpage.clickOnCloseExpandButton();
			productdetail.clickMenuRefreshIcon();
		}
		pimHomepage.clickLogoutButton();
	}

	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemMessage)
	@Test(priority = 1, description = "ITM_063 Verification Part | ITM_063 | Verifying Item Message based on multiple hazardous class code", groups = { "US",
			"pim","ITEM_MESSAGE" }, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verifyItemMessageforMultipleHazardousCode(Map<String, String> map) throws Throwable {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		List<String> itemcodelist = javautil.readMultipleValuesFromExcel(map.get("ItemNumber"));
		List<String> hazardouscodelist = javautil.readMultipleValuesFromExcel(map.get("HazardousCode"));
		for (int i = 0; i <= itemcodelist.size() - 1; i++) {
			pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
					.selectCatalogType(map.get("CatalogType"));
			queriessubmenu.enterHsiItemNumber(itemcodelist.get(i)).clickSeachButton().clickOnFirstResult()
					.selectTabfromDropdown(map.get("GlobalAttributeTab"));
			log(LogType.INFO, "Navigate to Global attribute tab to verify supplier code in US Catalog");

			// verify hazardous code
			hazardouscode = globalattribute.getHazardousClassCode();
			Assertions.assertThat(hazardouscode).isEqualTo(hazardouscodelist.get(i));
			productdetail.selectTabfromDropdown(map.get("Classificationtab"));
			log(LogType.INFO, "Navigate to Classification tab after verifying the hazardous code in US Catalog");

			classificationpage.clickOnExpandButton();
//			classificationpage.sortStructureGroup();
//			znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
//					map.get("structurePath"));
//			Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
//			classificationpage.clickStructureGroup();
//			classificationpage.removeButton();
//			classificationpage.clickYes();

			// verify quality status
			pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
			qualitystatus.sortRulestByLastExecution();
			List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("ruleStatus"));
			Assertions.assertThat(catalogentityrule).containsAnyOf(map.get("CatalogEntityRule"));
			log(LogType.INFO, "Verifying catalog entity rule");

			pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
			pimHomepage.productDetailSearchPage().clickRefreshIcon();
			classificationpage.sortStructureGroup();
			znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
					map.get("structurePath"));
			Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
			classificationpage.clickOnCloseExpandButton();
			productdetail.clickMenuRefreshIcon();
		}

		// verify in master
		for (int j = 0; j <= itemcodelist.size() - 1; j++) {
			queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog"));
			queriessubmenu.enterHsiItemNumber(itemcodelist.get(j)).clickSeachButton().clickOnFirstResult()
					.selectTabfromDropdown(map.get("GlobalAttributeTab"));

			// verify hazardous code
			hazardouscode = globalattribute.getHazardousClassCode();
			Assertions.assertThat(hazardouscode).isEqualTo(hazardouscodelist.get(j));
			productdetail.selectTabfromDropdown(map.get("Classificationtab"));
			log(LogType.INFO, "Navigate to Classification tab after verifying the hazardous code in Master Catalog");

			classificationpage.clickOnExpandButton();
			classificationpage.sortStructureGroup();
			znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
					map.get("structurePath"));
			Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
			classificationpage.clickOnCloseExpandButton();
			productdetail.clickMenuRefreshIcon();
		}
		pimHomepage.clickLogoutButton();
	}

	// ITM_047
	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemMessage)
	@Test(priority = 1, description = "ITM_047 Verification Part | ITM_047 | Verifying Item Message based on Availability code as inc. & Manufacturer Code as exc.", groups = {
			"US", "pim","ITEM_MESSAGE" }, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verifyItemMessageforAvailabilityCodeIncAndManufacturerCodeExc(Map<String, String> map)
			throws Throwable {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult();

		// verifying availability code
		String availabilitycode = productdetail.getItemField(map.get("fieldIndex"));
		Assertions.assertThat(availabilitycode).isEqualTo(map.get("AvailabilityCode"));
		log(LogType.INFO, "Verifying availability code");

		productdetail.selectTabfromDropdown(map.get("GlobalAttributeTab"));
		log(LogType.INFO, "Navigate to global attribute tab to verify manufacturer code");

		// verifying manufacture code
		String manufacturercode = globalattribute.getManufacturercode();
		Assertions.assertThat(manufacturercode).isNotEqualTo(map.get("ManufactureCode"));

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO, "Navigate to Classification tab after verifying the manufacturer code in US Catalog");

		classificationpage.clickOnExpandButton();
//		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
//				map.get("structurePath"));
//		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
//		classificationpage.clickStructureGroup();
//		classificationpage.removeButton();
//		classificationpage.clickYes();

		// verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		qualitystatus.sortRulestByLastExecution();
		List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("ruleStatus"));
		Assertions.assertThat(catalogentityrule).containsAnyOf(map.get("CatalogEntityRule"));
		log(LogType.INFO, "Verifying catalog entity rule");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickOnCloseExpandButton();

		// verify in master
		productdetail.clickMenuRefreshIcon();
		queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog"))
				.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult();

		// verifying availability code in master
		availabilitycode = productdetail.getItemField(map.get("fieldIndex"));
		Assertions.assertThat(availabilitycode).isEqualTo(map.get("AvailabilityCode"));
		log(LogType.INFO, "Verifying availability code in master");

		productdetail.selectTabfromDropdown(map.get("GlobalAttributeTab"));
		log(LogType.INFO, "Navigate to global attribute tab to verify manufacturer code in master");

		// verifying manufacturer code in master
		manufacturercode = globalattribute.getManufacturercode();
		Assertions.assertThat(manufacturercode).isNotEqualTo(map.get("ManufactureCode"));

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO, "Navigate to Classification tab after verifying the manufacturer code in Master Catalog");

		classificationpage.clickOnExpandButton();
		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickOnCloseExpandButton();
		pimHomepage.clickLogoutButton();
	}

	// ITM_051
	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemMessage)
	@Test(priority = 1, description = "ITM_051 Verification Part | ITM_051 | Verifying Item Message based on ItemCode which do not have Hazardous code as OA", groups = {
			"US", "pim","ITEM_MESSAGE" }, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verifyItemMessageforItemCodeHavingHazardousCodeIsNotOA(Map<String, String> map) throws Throwable {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult();

		productdetail.selectTabfromDropdown(map.get("GlobalAttributeTab"));
		log(LogType.INFO, "Navigate to global attribute tab to verify hazardous code");

		// verifying hazardous code
		String hazardouscode = globalattribute.getHazardousClassCode();
		Assertions.assertThat(hazardouscode).isNotEqualTo(map.get("HazardousCode"));

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO, "Navigate to Classification tab after verifying the hazardous code in US Catalog");

		classificationpage.clickOnExpandButton();
//		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
//				map.get("structurePath"));
//		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
//		classificationpage.clickStructureGroup();
//		classificationpage.removeButton();
//		classificationpage.clickYes();

		// verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		qualitystatus.sortRulestByLastExecution();
		List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("ruleStatus"));
		Assertions.assertThat(catalogentityrule).containsAnyOf(map.get("CatalogEntityRule"));
		log(LogType.INFO, "Verifying catalog entity rule");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickOnCloseExpandButton();

		// verify in master
		productdetail.clickMenuRefreshIcon();
		queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog"))
				.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult();

		productdetail.selectTabfromDropdown(map.get("GlobalAttributeTab"));
		log(LogType.INFO, "Navigate to global attribute tab to verify hazardous code in master");

		// verifying hazardous code in master
		hazardouscode = globalattribute.getHazardousClassCode();
		Assertions.assertThat(hazardouscode).isNotEqualTo(map.get("HazardousCode"));

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO, "Navigate to Classification tab after verifying the hazardous code in Master Catalog");

		classificationpage.clickOnExpandButton();
		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickOnCloseExpandButton();
		pimHomepage.clickLogoutButton();
	}

	// ITM_079
	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemMessage)
	@Test(priority = 1, description = "ITM_079 Verification Part | ITM_079 | Verifying Item Message based on mpc code inc. and sub-mpc code inc.", groups = { "US",
			"pim","ITEM_MESSAGE" }, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verifyItemMessageforMpcCodeIncSubMpcCodeInc(Map<String, String> map) throws Throwable {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();

		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType"));
		queriessubmenu.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult();

		// verifying mpc code
		String mpccode = productdetail.getItemField(map.get("fieldIndex"));
		Assertions.assertThat(mpccode).isEqualTo(map.get("MPC"));
		log(LogType.INFO, "Verifying mpc code");

		// verifying submpc code
		String submpccode = productdetail.getItemField(map.get("fieldIndex1"));
		Assertions.assertThat(submpccode).isEqualTo(map.get("SubMPC"));
		log(LogType.INFO, "Verifying sub-mpc code");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO, "Navigate to Classification tab after verifying the mpc and sub-mpc code in US Catalog");

		classificationpage.clickOnExpandButton();
//		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
//				map.get("structurePath"));
//		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
//		classificationpage.clickStructureGroup();
//		classificationpage.removeButton();
//		classificationpage.clickYes();

		// verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		qualitystatus.sortRulestByLastExecution();
		List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("ruleStatus"));
		Assertions.assertThat(catalogentityrule).containsAnyOf(map.get("CatalogEntityRule"));
		log(LogType.INFO, "Verifying catalog entity rule");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickOnCloseExpandButton();

		// verify in master
		productdetail.clickMenuRefreshIcon();
		queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog"))
				.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult();

		// verifying mpc code in master
		mpccode = productdetail.getItemField(map.get("fieldIndex"));
		Assertions.assertThat(mpccode).isEqualTo(map.get("MPC"));
		log(LogType.INFO, "Verifying mpc code");

		// verifying submpc code in master
		submpccode = productdetail.getItemField(map.get("fieldIndex1"));
		Assertions.assertThat(submpccode).isEqualTo(map.get("SubMPC"));
		log(LogType.INFO, "Verifying sub-mpc code");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO, "Navigate to Classification tab after verifying the mpc and sub-mpc code in Master Catalog");

		classificationpage.clickOnExpandButton();
		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickOnCloseExpandButton();
		pimHomepage.clickLogoutButton();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
//	ActionsPage action=new ActionsPage();
//	StructureSubMenu structuresubmenu=new StructureSubMenu();
//	ProductDetailSearchPage productDetailSearchPage = new ProductDetailSearchPage();                     
//	MainMenu mainmenu=new MainMenu();
//	ClassificationsPage classificationpage=new ClassificationsPage();
	QualityStatusPage qualitystatuspage=new QualityStatusPage();
//	QualityStatusPage qualitystatus=new  QualityStatusPage();
//	QueriesSubMenu queriesSubMenu=new QueriesSubMenu();
//	BasePage basePage = new BasePage();
//	Javautils javautil = new Javautils();
	String errorAndWarningmessage;
	List<String> item_code;
	ApplicationUtils appUtils = new ApplicationUtils();
	

	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGES, category = CategoryType.SMOKE)
	@TestDataSheet(sheetname = TestCaseSheet.ItemErrorMessageTestData)
	@Test(priority = 1, description = "ITM_018 Verification Part | ITM_018 | Verifying Error Global Message For US, and Master Catalog", 
		groups = {"SMOKE","US","pim","ITEM_MESSAGES"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verifyErrorGlobalMessage(Map<String, String> map)   
	{
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();

		List<String> messages = javautil.readMultipleValuesFromExcel(map.get("ErrorMessage"));
		List<String> structurePaths = javautil.readMultipleValuesFromExcel(map.get("ClassificationItem"));
		SoftAssert softAssert = new SoftAssert();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemTypeInQueriesMenu"))
		.selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("ItemNumber"));
		queriesSubMenu.clickSeachButton();

		structuresubmenu.clickOnFirstItemCode();

		productDetailSearchPage.selectTabfromDropdown(map.get("ClassificationTab"));
		classificationpage.clickOnExpandButton();
		
		int index = 0;
		boolean assigedflag = false;
		for(int i=0;i<messages.size();i++){
			if(classificationpage.isMessageAdded(map.get("TaxnomyType"),structurePaths.get(i))){
//				classificationpage.deleteMessageIfExisit(map.get("TaxnomyType"),structurePaths.get(i));
				appUtils.updateMessageInCSV(map.get("DivisionType"),"D",map.get("ErrorAndWarningType"),messages.get(i));
				log(LogType.INFO, messages.get(i) + " is deleted");
				continue;
			}else if(!assigedflag){
				action.clickOnActionsDropdown();
				action.clickOnClassifyItem();
				structuresubmenu.clickOnTaxnomyDropDownOfClassificationPopUp(map.get("TaxnomyDropDownOfClassificationPopUp"));
				structuresubmenu.sendKeysToFilterTextFieldOfClassificationPopup(map.get("DivisionTypeOfClassificationpPop")).
						selectDivisionErrorOrWarningMessage(messages.get(i));
				action.clickOnRadioCopyButton();
				action.clickOnOkButton().clickOnOkButton1();
				productDetailSearchPage.selectTabfromDropdown(map.get("ClassificationTab"));
				softAssert.assertTrue(classificationpage.verifyItemErrorMessageforAllDivisions(map.get("TaxnomyDropDownOfClassificationPopUp"), map.get("ErrorAndWarningType"), map.get("DivisionTypeOfClassificationpPop").toUpperCase(), messages.get(i).toUpperCase()));
				index = i;
				assigedflag = true;
				appUtils.updateMessageInCSV(map.get("DivisionType"),"U",map.get("ErrorAndWarningType"),messages.get(i));
				log(LogType.INFO, messages.get(i) + " is updated");
			}
		}

		//verify quality status log(LogType.INFO,"Verifying catalog entity rule");
		  pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get(
		  "Qualitystatustab")); List<String> catalogentityrule =
		  qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus"))
		  .sortRulestByLastExecution().
		  getRuleNamesByRuleStatus(map.get("Status")); for (String
		  Rules:javautil.readMultipleValuesFromExcel(map.get("CatalogEntityRule"))) {
		  Assertions.assertThat(catalogentityrule).containsAnyOf(Rules); }
	      classificationpage.clickOnCloseExpandButton();

		  
			/*
			 * //Verify whether the user is able to view the newly added Item message in PIM
			 * Web UI
			 * pimHomepage.mainMenu().clickStructuresMenu().selectCatalogType(map.get(
			 * "CatalogType"))
			 * .selectClassificationType(map.get("CategoryType")).selectItemType(map.get(
			 * "ItemType"));
			 * structuresubmenu.clickOnSelectedTaxnomyType(map.get("TaxnomyType"));
			 * structuresubmenu.selectErrorOrWarning(map.get("ErrorAndWarningType"));
			 * classificationpage.selectDivisionAndErrorMsg(map.get("DivisionType"),messages
			 * .get(index)); item_code = classificationpage.getHSIItemCodeList();
			 * Assertions.assertThat(item_code).contains(map.get("ItemNumber"));
			 */

		//Verify Error msg in Master catalog
		pimHomepage.mainMenu().clickQueriesMenu();
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog"));
		queriesSubMenu.clickSeachButton();

		structuresubmenu.clickOnFirstItemCode();

		productDetailSearchPage.selectTabfromDropdown(map.get("ClassificationTab"));
		softAssert.assertTrue(classificationpage.verifyItemErrorMessageforAllDivisions(map.get("TaxnomyDropDownOfClassificationPopUp"),map.get("ErrorAndWarningType"),map.get("DivisionTypeOfClassificationpPop").toUpperCase(),messages.get(index).toUpperCase()));

		pimHomepage.clickLogoutButton();

	}
	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGES, category = CategoryType.SMOKE)
	@TestDataSheet(sheetname = TestCaseSheet.ItemErrorMessageTestData)
	@Test(priority = 1, description = "ITM_019 Verification Part | ITM_019 | Verifying Warning Global Message For US, and Master Catalog",
		groups = {"SMOKE","US","pim","ITEM_MESSAGES"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verifyWarningGlobalMessage(Map<String, String> map) throws IOException, InterruptedException, AWTException  
	{
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		List<String> messages = javautil.readMultipleValuesFromExcel(map.get("ErrorMessage"));
		List<String> structurePaths = javautil.readMultipleValuesFromExcel(map.get("ClassificationItem"));
		SoftAssert softAssert = new SoftAssert();
		
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemTypeInQueriesMenu"))
		.selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("ItemNumber"));
		queriesSubMenu.clickSeachButton();
		structuresubmenu.clickOnFirstItemCode();
		productDetailSearchPage.selectTabfromDropdown(map.get("ClassificationTab"));
		classificationpage.clickOnExpandButton();


		int index = 0;
		boolean assigedflag = false;
		for(int i=0;i<messages.size();i++){
			if(classificationpage.isMessageAdded(map.get("TaxnomyType"),structurePaths.get(i))){
//				classificationpage.deleteMessageIfExisit(map.get("TaxnomyType"),structurePaths.get(i));
				appUtils.updateMessageInCSV(map.get("DivisionType"),"D",map.get("ErrorAndWarningType"),messages.get(i));
				log(LogType.INFO, messages.get(i) + " is deleted");
				continue;
			}else if(!assigedflag){
				action.clickOnActionsDropdown();
				action.clickOnClassifyItem();
				structuresubmenu.clickOnTaxnomyDropDownOfClassificationPopUp(map.get("TaxnomyDropDownOfClassificationPopUp"));
				structuresubmenu.sendKeysToFilterTextFieldOfClassificationPopup(map.get("DivisionTypeOfClassificationpPop")).
						selectDivisionErrorOrWarningMessage(messages.get(i));
				action.clickOnRadioCopyButton();
				action.clickOnOkButton();
				productDetailSearchPage.selectTabfromDropdown(map.get("ClassificationTab"));
				softAssert.assertTrue(classificationpage.verifyItemErrorMessageforAllDivisions(map.get("TaxnomyDropDownOfClassificationPopUp"), map.get("ErrorAndWarningType"), map.get("DivisionTypeOfClassificationpPop").toUpperCase(), messages.get(i).toUpperCase()));
				index = i;
				assigedflag = true;
				appUtils.updateMessageInCSV(map.get("DivisionType"),"U",map.get("ErrorAndWarningType"),messages.get(i));
				log(LogType.INFO, messages.get(i) + " is updated");
			}
		}

		//verify quality status 
				log(LogType.INFO,"Verifying catalog entity rule");
				pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
				List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus"))
					.sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
				for (String Rules:javautil.readMultipleValuesFromExcel(map.get("CatalogEntityRule")))
				{
					Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);	
				}
			
				log(LogType.INFO,"Verified catalog entity rule");
				classificationpage.clickOnCloseExpandButton();

				/*
				 * //Verify whether the user is able to view the newly added Item message in PIM
				 * Web UI
				 * pimHomepage.mainMenu().clickStructuresMenu().selectCatalogType(map.get(
				 * "CatalogType"))
				 * .selectClassificationType(map.get("CategoryType")).selectItemType(map.get(
				 * "ItemType"));
				 * structuresubmenu.clickOnSelectedTaxnomyType(map.get("TaxnomyType"));
				 * structuresubmenu.selectErrorOrWarning(map.get("ErrorAndWarningType"));
				 * classificationpage.selectDivisionAndErrorMsg(map.get("DivisionType"),messages
				 * .get(index)); item_code = classificationpage.getHSIItemCodeList();
				 * Assertions.assertThat(item_code).contains(map.get("ItemNumber"));
				 */

		//Verify Warning msg in Master catalog
		pimHomepage.mainMenu().clickQueriesMenu();
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog"));
		queriesSubMenu.clickSeachButton();
		structuresubmenu.clickOnFirstItemCode();
		productDetailSearchPage.selectTabfromDropdown(map.get("ClassificationTab"));
		softAssert.assertTrue(classificationpage.verifyItemErrorMessageforAllDivisions(map.get("TaxnomyDropDownOfClassificationPopUp"),map.get("ErrorAndWarningType"),map.get("DivisionTypeOfClassificationpPop").toUpperCase(),messages.get(index).toUpperCase()));
		pimHomepage.clickLogoutButton();

	}

	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGES, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemErrorMessageTestData)
	@Test(priority = 1, description = "Verifying Add Verification Part | Verifying Add Global Error Message for multiple item code in US, and Master Catalog",groups = {"US","pim","ITEM_MESSAGES"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verifyAddErrorGlobalMessageForMultipleItemCode(Map<String, String> map) throws IOException, InterruptedException, AWTException  
	{
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();

		//Verify Delete Error message
		List<String> itemCodeList = javautil.readMultipleValuesFromExcel(map.get("ItemNumber"));
		pimHomepage.mainMenu().clickQueriesMenu();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemTypeInQueriesMenu")).selectCatalogType(map.get("CatalogType"));

		// search all itemcodes on left panel
		for (int i = 0; i <=itemCodeList.size()-1; i++)
		{
			queriesSubMenu.enterHsiItemNumber(itemCodeList.get(i)).clickSeachButton();
		}
		BasePage.WaitForMiliSec(5000);
		basePage.clickAndHoldCtrlKey();
		classificationpage.selectMultipleuitemcodeList(); 
		basePage.releaseCtrlKey();
		productDetailSearchPage.selectTabfromDropdown(map.get("ClassificationTab"));
//		classificationpage.clickOnExpandButton();
//		classificationpage.deleteMessageIfExisit(map.get("TaxnomyType"),map.get("ClassificationItem"));
//		classificationpage.clickOnCloseExpandButton();


		//Verify Assigned Error msg in US catalog
		/*
		 * pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get(
		 * "CatalogType")); queriesSubMenu.clickSeachButton();
		 * basePage.clickAndHoldCtrlKey();
		 * classificationpage.selectMultipleuitemcodeList(); basePage.releaseCtrlKey();
		 */
		action.clickOnActionsDropdown();
		action.clickOnClassifyItem();
		structuresubmenu.clickOnTaxnomyDropDownOfClassificationPopUp(map.get(
				"TaxnomyDropDownOfClassificationPopUp"));
		structuresubmenu.sendKeysToFilterTextFieldOfClassificationPopup(map.get(
				"DivisionTypeOfClassificationpPop")).
		selectDivisionErrorOrWarningMessage(map.get("ErrorMessage"));
		action.clickOnRadioCopyButton(); action.clickOnOkButton();
		productDetailSearchPage.selectTabfromDropdown(map.get("ClassificationTab"));
		SoftAssert softAssert = new SoftAssert();
		softAssert.assertTrue(classificationpage.
				verifyItemErrorMessageforAllDivisions(map.get(
						"TaxnomyDropDownOfClassificationPopUp"),map.get("ErrorAndWarningType"),map.
						get("DivisionTypeOfClassificationpPop").toUpperCase(),map.get("ErrorMessage")
						.toUpperCase()));

		//Verify whether the user is able to view the newly added Item message in PIM
		//Web UI
		pimHomepage.mainMenu().clickStructuresMenu().selectCatalogType(map.get(
				"CatalogType"))
		.selectClassificationType(map.get("CategoryType")).selectItemType(map.get(
				"ItemType"));
		structuresubmenu.clickOnSelectedTaxnomyType(map.get("TaxnomyType"));
		structuresubmenu.selectErrorOrWarning(map.get("ErrorAndWarningType"));
		classificationpage.selectDivisionAndErrorMsg(map.get("DivisionType"),map.get(
				"ErrorMessage"));
		item_code = classificationpage.getHSIItemCodeList();
		System.out.println(map.get("ItemNumber"));
		// search all item codes in the filtered results
		for (int i = 0; i <= item_code.size()-1; i++)
		{
			Assertions.assertThat(item_code).contains(item_code.get(i));
		}

		//Verify Error msg in Master catalog
		log(LogType.INFO, "Verify Error Messages in Master for Multiple item code");
		BasePage.WaitForMiliSec(5000);
		pimHomepage.mainMenu().clickQueriesMenu();
		pimHomepage.mainMenu().clickRefreshMenuIcon();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemTypeInQueriesMenu"));
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get(
				"MasterCatalog"));
		for (int i = 0; i <itemCodeList.size(); i++)
		{
			queriesSubMenu.enterHsiItemNumber(itemCodeList.get(i)).clickSeachButton();
		}
		BasePage.WaitForMiliSec(5000);
		productDetailSearchPage.clickAndHoldCtrlKey();
		classificationpage.selectMultipleuitemcodeList();
		productDetailSearchPage.releaseCtrlKey();
		productDetailSearchPage.selectTabfromDropdown(map.get("ClassificationTab"));
		softAssert.assertTrue(classificationpage.
				verifyItemErrorMessageforAllDivisions(map.get(
						"TaxnomyDropDownOfClassificationPopUp"),map.get("ErrorAndWarningType"),map.
						get("DivisionTypeOfClassificationpPop").toUpperCase(),map.get("ErrorMessage")
						.toUpperCase())); pimHomepage.clickLogoutButton();

	}


	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
//    ProductDetailSearchPage productDetailSearchPage = new ProductDetailSearchPage();
//    ClassificationsPage classificationPage = new ClassificationsPage();
//    MainMenu mainmenu = new MainMenu();
    TextPage textPage = new TextPage();
    StructureSubMenu structureSubMenu = new StructureSubMenu();


    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
//    StructureSubMenu structuresubmenu=new StructureSubMenu();
//    ProductDetailSearchPage productDetailSearchPage = new ProductDetailSearchPage();
//    QueriesSubMenu queriesSubMenu=new QueriesSubMenu();
//    ClassificationsPage classificationPage=new ClassificationsPage();
//	ActionsPage action = new ActionsPage();



    @PimFrameworkAnnotation(module = Modules.ITEM_MESSAGES, category = CategoryType.REGRESSION)
    @TestDataSheet(sheetname = TestCaseSheet.ItemErrorMessageTestData)
    @Test(priority = 1, description = "ITM_154_US Verification Part | ITM_154_US | Verify deletion of Primary Taxonomy assigned message for an item in classifications tab for US",groups = {"US","pim","ITEM_MESSAGES"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
    public void verifyPrimaryTaxonomyMessageDeletionInUS(Map<String, String> map)
    {
        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();

        //Verify deletion of primary taxonomy item message in US Catalog
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemTypeInQueriesMenu"))
                .selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(map.get("ItemNumber"));
        queriesSubMenu.clickSeachButton();
        structuresubmenu.clickOnFirstItemCode();
        productDetailSearchPage.selectTabfromDropdown(map.get("ClassificationTab"));
        classificationPage.clickOnExpandButton();
        String actualPrimaryTaxonomyMessageInUS = classificationPage.clickClassificationItem(map.get("TaxnomyType"),map.get("ClassificationItem"));
//        classificationPage.removeButton();
//        classificationPage.clickYes();
        Assertions.assertThat(classificationPage.isPermissionErrorMessageVisible(map.get("permissionErrorMessage"))).isTrue();
		log(LogType.EXTENTANDCONSOLE, map.get("permissionErrorMessage") + " error message appears");
        classificationPage.closePermissionRequiredErrorMessage();
        verifyPrimaryTaxonomyIsVisibleForUS();
        classificationPage.clickOnCloseExpandButton();

        //Verify deletion of primary taxonomy item message in MASTER Catalog
        pimHomepage.mainMenu().clickRefreshMenuIcon();
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemTypeInQueriesMenu"))
                .selectCatalogType(map.get("MasterCatalog")).enterHsiItemNumber(map.get("ItemNumber"));
        queriesSubMenu.clickSeachButton();
        structuresubmenu.clickOnFirstItemCode();
        productDetailSearchPage.selectTabfromDropdown(map.get("ClassificationTab"));
        classificationPage.clickOnExpandButton();
        classificationPage.clickClassificationItem(map.get("TaxnomyType"),map.get("ClassificationItem"));
        verifyPrimaryTaxonomyIsVisibleForUS();
        classificationPage.clickOnCloseExpandButton();
        pimHomepage.clickLogoutButton();
    }
    public void verifyPrimaryTaxonomyIsVisibleForUS() {
        Assertions.assertThat(classificationPage.isAllAttributesVisible("PrimaryTaxonomy")).isTrue();
    }

        @PimFrameworkAnnotation(module = Modules.ITEM_MESSAGES, category = CategoryType.REGRESSION)
    @TestDataSheet(sheetname = TestCaseSheet.ItemErrorMessageTestData)
    @Test(priority = 1, description = "ITM_154_CA Verification Part | ITM_154_CA | Verify deletion of Primary Taxonomy assigned message for an item in classifications tab for CA",groups = {"CA","pim","ITEM_MESSAGES"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
    public void verifyPrimaryTaxonomyMessageDeletionInCA(Map<String, String> map)
    {
        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();

        //Verify deletion of primary taxonomy item message in CA Catalog
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemTypeInQueriesMenu"))
                .selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(map.get("ItemNumber"));
        queriesSubMenu.clickSeachButton();
        structuresubmenu.clickOnFirstItemCode();
        productDetailSearchPage.selectTabfromDropdown(map.get("ClassificationTab"));
        classificationPage.clickOnExpandButton();
        classificationPage.clickClassificationItem(map.get("TaxnomyType"),map.get("ClassificationItem"));
//        classificationPage.removeButton();
//        classificationPage.clickYes();
//        Assertions.assertThat(classificationPage.isPermissionErrorMessageVisible(map.get("permissionErrorMessage"))).isTrue();
		log(LogType.EXTENTANDCONSOLE, map.get("permissionErrorMessage") + " error message appears");
        classificationPage.closePermissionRequiredErrorMessage();
        verifyPrimaryTaxonomyIsVisibleForCA();
        classificationPage.clickOnCloseExpandButton();

        //Verify deletion of primary taxonomy item message in MASTER Catalog
        pimHomepage.mainMenu().clickRefreshMenuIcon();
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemTypeInQueriesMenu"))
                .selectCatalogType(map.get("MasterCatalog")).enterHsiItemNumber(map.get("ItemNumber"));
        queriesSubMenu.clickSeachButton();
        structuresubmenu.clickOnFirstItemCode();
        productDetailSearchPage.selectTabfromDropdown(map.get("ClassificationTab"));
        classificationPage.clickOnExpandButton();
        classificationPage.clickClassificationItem(map.get("TaxnomyType"),map.get("ClassificationItem"));
        verifyPrimaryTaxonomyIsVisibleForCA();
        classificationPage.clickOnCloseExpandButton();
        pimHomepage.clickLogoutButton();
    }
    public void verifyPrimaryTaxonomyIsVisibleForCA() {
        Assertions.assertThat(classificationPage.isAllAttributesVisible("PrimaryTaxonomy")).isTrue();
    }


    @PimFrameworkAnnotation(module = Modules.ITEM_MESSAGES, category = CategoryType.REGRESSION)
    @TestDataSheet(sheetname = TestCaseSheet.ItemErrorMessageTestData)
    @Test(priority = 1, description = "ITM_155_US Verification Part | ITM_155_US | Verify deletion of ecommerce Taxonomy assigned message for an item in classifications tab for US",groups = {"US","pim","ITEM_MESSAGES"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
    public void verifyEcommerceTaxonomyMessageDeletionInUS(Map<String, String> map)
    {
        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();

        //Verify deletion of Ecommerce taxonomy item message in US Catalog
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemTypeInQueriesMenu"))
                .selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(map.get("ItemNumber"));
        queriesSubMenu.clickSeachButton();
        structuresubmenu.clickOnFirstItemCode();
        productDetailSearchPage.selectTabfromDropdown(map.get("ClassificationTab"));
        classificationPage.clickOnExpandButton();
        classificationPage.clickClassificationItem(map.get("TaxnomyType"),map.get("ClassificationItem"));
//        classificationPage.removeButton();
//        classificationPage.clickYes();
        Assertions.assertThat(classificationPage.isPermissionErrorMessageVisible(map.get("permissionErrorMessage"))).isTrue();
		log(LogType.EXTENTANDCONSOLE, map.get("permissionErrorMessage") + " error message appears");
        classificationPage.closePermissionRequiredErrorMessage();
        verifyEcommerceTaxonomyIsVisibleForUS();
        classificationPage.clickOnCloseExpandButton();

        //Verify deletion of Ecommerce taxonomy item message in MASTER Catalog
        pimHomepage.mainMenu().clickRefreshMenuIcon();
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemTypeInQueriesMenu"))
                .selectCatalogType(map.get("MasterCatalog")).enterHsiItemNumber(map.get("ItemNumber"));
        queriesSubMenu.clickSeachButton();
        structuresubmenu.clickOnFirstItemCode();
        productDetailSearchPage.selectTabfromDropdown(map.get("ClassificationTab"));
        classificationPage.clickOnExpandButton();
        classificationPage.clickClassificationItem(map.get("TaxnomyType"),map.get("ClassificationItem"));
        verifyEcommerceTaxonomyIsVisibleForUS();
        classificationPage.clickOnCloseExpandButton();
    }
    public void verifyEcommerceTaxonomyIsVisibleForUS() {
        Assertions.assertThat(classificationPage.isAllAttributesVisible("EcommerceTaxonomy")).isTrue();
    }

    @PimFrameworkAnnotation(module = Modules.ITEM_MESSAGES, category = CategoryType.REGRESSION)
    @TestDataSheet(sheetname = TestCaseSheet.ItemErrorMessageTestData)
    @Test(priority = 1, description = "ITM_155_CA Verification Part | ITM_155_CA | Verify deletion of ecommerce Taxonomy assigned message for an item in classifications tab for CA", groups = {"US","pim","ITEM_MESSAGES"},dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
    public void verifyEcommerceTaxonomyMessageDeletionInCA(Map<String, String> map)
    {
        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();

        //Verify deletion of Ecommerce taxonomy item message in CA Catalog
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemTypeInQueriesMenu"))
                .selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(map.get("ItemNumber"));
        queriesSubMenu.clickSeachButton();
        structuresubmenu.clickOnFirstItemCode();
        productDetailSearchPage.selectTabfromDropdown(map.get("ClassificationTab"));
        classificationPage.clickOnExpandButton();
        classificationPage.clickClassificationItem(map.get("TaxnomyType"),map.get("ClassificationItem"));
//        classificationPage.removeButton();
//        classificationPage.clickYes();
        Assertions.assertThat(classificationPage.isPermissionErrorMessageVisible(map.get("permissionErrorMessage"))).isTrue();
		log(LogType.EXTENTANDCONSOLE, map.get("permissionErrorMessage") + " error message appears");
        classificationPage.closePermissionRequiredErrorMessage();
        verifyEcommerceTaxonomyIsVisibleForCA();
        classificationPage.clickOnCloseExpandButton();

        //Verify deletion of Ecommerce taxonomy item message in MASTER Catalog
        pimHomepage.mainMenu().clickRefreshMenuIcon();
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemTypeInQueriesMenu"))
                .selectCatalogType(map.get("MasterCatalog")).enterHsiItemNumber(map.get("ItemNumber"));
        queriesSubMenu.clickSeachButton();
        structuresubmenu.clickOnFirstItemCode();
        productDetailSearchPage.selectTabfromDropdown(map.get("ClassificationTab"));
        classificationPage.clickOnExpandButton();
        classificationPage.clickClassificationItem(map.get("TaxnomyType"),map.get("ClassificationItem"));
        verifyEcommerceTaxonomyIsVisibleForCA();
        classificationPage.clickOnCloseExpandButton();
        pimHomepage.clickLogoutButton();
    }
    public void verifyEcommerceTaxonomyIsVisibleForCA() {
        Assertions.assertThat(classificationPage.isAllAttributesVisible("EcommerceTaxonomy")).isTrue();
    }
    
    

    
    
    
    
    
    
    
    
    
    
    
    
    
//    CatalogTypePage catalogTypePage = new CatalogTypePage();
//    QualityStatusPage qualitystatus = new QualityStatusPage();
    ItemPublishDateAndFlagPage itemPublish = new ItemPublishDateAndFlagPage();
    CreateItemValidationPage itemValidationPage = new CreateItemValidationPage();
//    LoginPage loginPage = new LoginPage();
//    Javautils javautils=new Javautils();
//    ProductDetailSearchPage productDetailSearchPage = new ProductDetailSearchPage();
//    StructureSubMenu structuresubmenu=new StructureSubMenu();
//    PimHomepage pimHomepage = new PimHomepage();

    @PimFrameworkAnnotation(module = Modules.DENTAL_CATALOG, category = CategoryType.REGRESSION)
    @TestDataSheet(sheetname = TestCaseSheet.ItemPublishTestData)
    @Test(priority = 1, description = "ITP_040 Verification Part | ITP_040 | Verify publish flag defaults to Y and publish date as current date if catalog unassigned in PIX on item creation for Dental Division",groups = {"US","pim","DENTAL_CATALOG"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
    public void verify_PublishFlag_Defaults_PublishDate_If_Catalog_Unassigned_loadedFromPixTests_In_Dental_Catalog(Map<String, String> map) {

        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("Dental User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("Dental User").get("Password")).clickLoginButton();
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
        log(LogType.INFO,"Navigate to Dental Catalog tab from Queries Menu for US Catalog");
        BasePage.WaitForMiliSec(3000);
//        catalogTypePage.selectPublishDate(DateandTimeUtils.getTodaysDate()).selectPublishFlag();
//        log(LogType.INFO,"Change publish date to current date and publish flag");
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
    @Test(priority = 1, description = "ITP_041 Verification Part | ITP_041 | Verify publish flag defaults to Y and publish date as current date if catalog unassigned in PIX on item creation for Medical Division",groups = {"US","pim","MEDICAL_CATALOG"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
    public void verify_PublishFlag_Defaults_PublishDate_If_Catalog_Unassigned_loadedFromPixTests_In_Medical_Catalog(Map<String, String> map) {

        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
        log(LogType.INFO,"Navigate to Dental Catalog tab from Queries Menu for US Catalog");
        BasePage.WaitForMiliSec(3000);
//        catalogTypePage.selectPublishDate(DateandTimeUtils.getTodaysDate()).selectPublishFlag();
//        log(LogType.INFO,"Change publish date to current date and publish flag");
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
    @Test(priority = 1, description = "ITP_042 Verification Part | ITP_042 | Verify publish flag defaults to Y and publish date as current date if catalog unassigned in PIX on item creation for Zahn Division", groups = {"US","ZAHN_CATALOG","pim"},dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
    public void verify_PublishFlag_Defaults_PublishDate_If_Catalog_Unassigned_loadedFromPixTests_In_Zahn_Catalog(Map<String, String> map) {

        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("Zahn User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("Zahn User").get("Password")).clickLoginButton();
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
        log(LogType.INFO,"Navigate to Dental Catalog tab from Queries Menu for US Catalog");
        BasePage.WaitForMiliSec(3000);
//        catalogTypePage.selectPublishDate(DateandTimeUtils.getTodaysDate()).selectPublishFlag();
//        log(LogType.INFO,"Change publish date to current date and publish flag");
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
    @Test(priority = 1, description = "ITP_043 Verification Part | ITP_043 | Verify publish flag defaults to Y and publish date as current date if catalog unassigned in PIX on item creation for SpecialMarkets Division",groups = {"US","pim","SPECIAL_MARKET_CATALOG"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
    public void verify_PublishFlag_Defaults_PublishDate_If_Catalog_Unassigned_loadedFromPixTests_In_SpecialMarkets_Catalog(Map<String, String> map) {

        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("Special Markets User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("Special Markets User").get("Password")).clickLoginButton();
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
        log(LogType.INFO,"Navigate to Dental Catalog tab from Queries Menu for US Catalog");
        BasePage.WaitForMiliSec(3000);
//        catalogTypePage.selectPublishDate(DateandTimeUtils.getTodaysDate()).selectPublishFlag();
//        log(LogType.INFO,"Change publish date to current date and publish flag");
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
    @Test(priority = 1, description = "ITP_044 Verification Part | ITP_044 | Verify publish flag defaults to Y and publish date as current date ,if catalogs which are not rule driven is added with publish date not mapped in PIX for CA Dental Division",groups = {"CA","pim","DENTAL_CATALOG"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
    public void verify_PublishFlag_Defaults_PublishDate_CurrentDate_If_Catalog_Not_Added_RuleDriven_For_CA_Dental_Division(Map<String, String> map) {
        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
        log(LogType.INFO,"Navigate to Dental Catalog tab from Queries Menu for CA Catalog");
        BasePage.WaitForMiliSec(3000);
        DriverManager.getDriver().findElement(ItemPublishDateAndFlagPage.userDrivenfield).isDisplayed();
     

//        catalogTypePage.selectPublishDate(DateandTimeUtils.getTodaysDate()).selectPublishFlag();
//        log(LogType.INFO,"Change publish date to current date and publish flag");
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
    @Test(priority = 1, description = "ITP_045 Verification Part | ITP_045 | Verify publish flag defaults to Y and publish date as current date ,if catalogs which are not rule driven is added with publish date not mapped in PIX for CA Medical Division", groups = {"CA","pim","MEDICAL_CATALOG"},dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
    public void verify_PublishFlag_Defaults_PublishDate_CurrentDate_If_Catalog_Not_Added_RuleDriven_For_CA_Medical_Division(Map<String, String> map) {
        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
        log(LogType.INFO,"Navigate to Dental Catalog tab from Queries Menu for CA Catalog");
        BasePage.WaitForMiliSec(3000);
        DriverManager.getDriver().findElement(ItemPublishDateAndFlagPage.userDrivenfield).isDisplayed();
        

//        catalogTypePage.selectPublishDate(DateandTimeUtils.getTodaysDate()).selectPublishFlag();
//        log(LogType.INFO,"Change publish date to current date and publish flag");
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
    @Test(priority = 1, description = "ITP_046 Verification Part | ITP_046 | Verify publish flag defaults to Y and publish date as current date ,if catalogs which are not rule driven is added with publish date not mapped in PIX for CA Zahn Division", groups = {"CA","pim","ZAHN_CATALOG"},dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
    public void verify_PublishFlag_Defaults_PublishDate_CurrentDate_If_Catalog_Not_Added_RuleDriven_For_CA_Zahn_Division(Map<String, String> map) {
        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
        log(LogType.INFO,"Navigate to Dental Catalog tab from Queries Menu for CA Catalog");
        BasePage.WaitForMiliSec(3000);
        DriverManager.getDriver().findElement(ItemPublishDateAndFlagPage.userDrivenfield).isDisplayed();
       

//        catalogTypePage.selectPublishDate(DateandTimeUtils.getTodaysDate()).selectPublishFlag();
//        log(LogType.INFO,"Change publish date to current date and publish flag");
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
    

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
//	LocalAttributePage localAttributePage = new LocalAttributePage();
//	ProductDetailSearchPage productDetailSearchPage = new ProductDetailSearchPage();
	QualityStatusPage qualityStatusPage = new QualityStatusPage();
	SeoTabPage seoTabPage = new SeoTabPage();
	GlobalAttributePage globalAttributePage = new GlobalAttributePage();
	WebDescription webDescription = new WebDescription();
//	QueriesSubMenu queriesSubMenu = new QueriesSubMenu();
//	PimHomepage pimHomepage = new PimHomepage();
//	LoginPage loginPage = new LoginPage();
	MainMenu mainMenu = new MainMenu();

	// WRFL_028 For US
	@PimFrameworkAnnotation(module = Modules.LOCAL_ATTRIBUTE, category = CategoryType.SMOKE)
	@TestDataSheet(sheetname = TestCaseSheet.Local_Attribute)
	@Test(priority = 1, description = "For US - WRFL_028 Verification Part | For US - WRFL_028 | Verify the Entity Rules and Mandatory Field exectued after a Local Attribute change Rules are failedFor US", dataProvider = "getCatalogData", groups = {
			"SMOKE","US","LOCAL_ATTRIBUTE" }, dataProviderClass = DataProviderUtils.class)
	public void Verify_Removing_Mandatory_Field_Local_Attribute_Failed_For_US(Map<String, String> map) {
		

		loginPage.enterUserName(map.get("Username")).enterPassword(map.get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("itemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));

		// Removing Value
//		localAttributePage.brandFieldAttribute(map.get("LocalAttributeBrandFieldValueWithNoContent"))
//				.MandatoryItemFieldAttribute(map.get("LocalAttributeItemFieldValueWithNoContent"))
//				.itemTypeFieldAttribute(map.get("LocalAttributeitemTypeFieldValueWithNoContent"))
//				.clearingMandatoryQuantityFieldAttribute().selectValuefromSaveDropdown(map.get("saveButton"));

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
		@Test(priority = 1, description = "For CA - WRFL_028 Verification Part | For CA - WRFL_028 | Verify the Entity Rules and Mandatory Field exectued after a Local Attribute change Rules are failed For CA", dataProvider = "getCatalogData", groups = {
				 "SMOKE","CA","LOCAL_ATTRIBUTE" }, dataProviderClass = DataProviderUtils.class)
		public void Verify_Removing_Mandatory_Field_Local_Attribute_Failed_For_CA(Map<String, String> map) {
			
			loginPage.enterUserName(map.get("Username")).enterPassword(map.get("Password")).clickLoginButton();
			pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
					.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("itemNumber")).clickSeachButton()
					.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));

			// Removing Value
//			localAttributePage.brandFieldAttribute(map.get("LocalAttributeBrandFieldValueWithNoContent"))
//					.MandatoryItemFieldAttribute(map.get("LocalAttributeItemFieldValueWithNoContent"))
//					.itemTypeFieldAttribute(map.get("LocalAttributeitemTypeFieldValueWithNoContent"))
//					.clearingMandatoryQuantityFieldAttribute().selectValuefromSaveDropdown(map.get("saveButton"));

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
	@Test(priority = 1, description = "For US - WRFL_029, Verification Part | For US - WRFL_029,WRFL_071 | Verify the Mandatory field exectued after a Local Attribute change Rules are passed for US", dataProvider = "getCatalogData", groups = {
			"US" ,"LOCAL_ATTRIBUTE"}, dataProviderClass = DataProviderUtils.class)
	public void Verify_Adding_Mandatory_Field_Local_Attribute_Passed_For_US(Map<String, String> map) {
		
		loginPage.enterUserName(map.get("Username")).enterPassword(map.get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("itemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
		// Adding Value
//		localAttributePage.brandFieldAttribute(map.get("LocalAttributeBrandFieldValue"))
//				.MandatoryItemFieldAttribute(map.get("LocalAttributeItemFieldValue"))
//				.itemTypeFieldAttribute(map.get("LocalAttributeitemTypeFieldValue"))
//				.addingMandatoryQuantityFieldAttribute(map.get("LocalAttributeQuantityFieldValue"))
//				.selectValuefromSaveDropdown(map.get("saveButton"));

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
	@Test(priority = 1, description = "For CA - WRFL_029, Verification Part | For CA - WRFL_029,WRFL_071 | Verify the Mandatory field exectued after a Local Attribute change Rules are passed for CA", dataProvider = "getCatalogData", groups = {
			 "CA","LOCAL_ATTRIBUTE" }, dataProviderClass = DataProviderUtils.class)
	public void Verify_Adding_Mandatory_Field_Local_Attribute_Passed_For_CA(Map<String, String> map) {
		
		loginPage.enterUserName(map.get("Username")).enterPassword(map.get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("itemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
		// Adding Value
//		localAttributePage.brandFieldAttribute(map.get("LocalAttributeBrandFieldValue"))
//				.MandatoryItemFieldAttribute(map.get("LocalAttributeItemFieldValue"))
//				.itemTypeFieldAttribute(map.get("LocalAttributeitemTypeFieldValue"))
//				.addingMandatoryQuantityFieldAttribute(map.get("LocalAttributeQuantityFieldValue"))
//				.selectValuefromSaveDropdown(map.get("saveButton"));

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
	@Test(priority = 1, description = "FDM_19 Verification Part | FDM_19,FDM_20,FDM_21 | Verify the Entity Rules exectued after a Local Attribute change", groups = {
			"CA","LOCAL_ATTRIBUTE" }, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void Verify_UI_Validation_For_Quantity_Field_in_Local_Attribute(Map<String, String> map) {
		
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("itemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
//		localAttributePage.brandFieldAttribute(map.get("LocalAttributeBrandFieldValue"));

		Assertions.assertThat(localAttributePage.isLocalAttributeFieldVisible("quantityInputField")).isTrue();
//		localAttributePage.selectLanguageFieldDropdown(map.get("LocalAttributeLaunguageField")).selectValuefromSaveDropdown(map.get("saveButton"));

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

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	Assertion a = new Assertion();
	GEP_WebDescriptionPage gepWebDescPage = new GEP_WebDescriptionPage();
	LocalAttributePage localAttPage = new LocalAttributePage();
//	BasePage basePage = new BasePage();
//	ProductDetailSearchPage productDetailSearchPage = new ProductDetailSearchPage();
//	QueriesSubMenu queriesSubMenu = new QueriesSubMenu();
//	QualityStatusPage qualityStatusPage = new QualityStatusPage();
//	GlobalAttributePage globalAttributePage=new GlobalAttributePage();
	
	
	//MDM-758-006 (US) WRFL_110
	@PimFrameworkAnnotation(module = Modules.GEP_WEB_DESCRIPTION, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.GEPWebDescription)
	@Test(priority = 1, description = "WRFL_110 US Verification Part | WRFL_110 US | Verify if below GEP Web Description fields are editable, GEP Full Display Description, GEP Abbreviated Display Description, GEP Look Ahead Search Description, GEP Detailed or Extended Description", 
	dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"US","WEB_DESCRIPTION"})
	public void updationOfGEPWebDescriptionAfterUpdationOfLocalAttributeParameterForUS(Map<String, String> map) throws InterruptedException, IOException {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Dental User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Dental User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("itemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabNameLA"));
		BasePage.WaitForMiliSec(2000);
//		localAttPage.itemFieldAttributeChange(map.get("Item1")).selectValuefromSaveDropdown(map.get("Save"));
//		productDetailSearchPage.clickRefreshIcon();	
//		//updating one more time with other utem
//		localAttPage.itemFieldAttributeChange(map.get("Item2")).selectValuefromSaveDropdown(map.get("Save"));
//		productDetailSearchPage.clickRefreshIcon();
		
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
	@Test(priority = 1, description = "WRFL_110 CA Verification Part | WRFL_110 CA | Verify if below GEP Web Description fields are editable, GEP Full Display Description, GEP Abbreviated Display Description, GEP Look Ahead Search Description, GEP Detailed or Extended Description", 
	dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"CA","WEB_DESCRIPTION"})
	public void updationOfGEPWebDescriptionAfterUpdationOfLocalAttributeParameterForCA(Map<String, String> map) throws InterruptedException, IOException {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("itemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabNameLA"));
		BasePage.WaitForMiliSec(2000);
//		localAttPage.itemFieldAttributeChange(map.get("Item1")).selectValuefromSaveDropdown(map.get("Save"));
//		productDetailSearchPage.clickRefreshIcon();	
//		//updating one more time with other utem
//		localAttPage.itemFieldAttributeChange(map.get("Item2")).selectValuefromSaveDropdown(map.get("Save"));
//		productDetailSearchPage.clickRefreshIcon();
		
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
	@Test(priority = 1, description = "WRFL_112 US Verification Part | WRFL_112 US | Verify if existing 'GEP Web Description' is getting updated upon updating value of one of the Local Attribute parameter for an item having Mandatory Local Attribute Failure", 
	dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"US","WEB_DESCRIPTION"})
	public void chekingUpdationInLocalAttributeDoesNotUpdateGEPWDInCaseOfMandatoryLocalAttributeFailureForUS(Map<String, String> map) throws InterruptedException, IOException {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Dental User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Dental User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("itemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabNameLA"));
		BasePage.WaitForMiliSec(2000);
//		localAttPage.itemFieldAttributeChange(map.get("Item1")).selectValuefromSaveDropdown(map.get("Save"));
//		productDetailSearchPage.clickRefreshIcon();	
//		//updating one more time with other item.
//		localAttPage.itemFieldAttributeChange(map.get("Item2")).selectValuefromSaveDropdown(map.get("Save"));
//		productDetailSearchPage.clickRefreshIcon();
		
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
	@Test(priority = 1, description = "WRFL_112 CA Verification Part | WRFL_112 CA | Verify if existing 'GEP Web Description' is getting updated upon updating value of one of the Local Attribute parameter for an item having Mandatory Local Attribute Failure", 
	dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"CA","WEB_DESCRIPTION"})
	public void chekingUpdationInLocalAttributeDoesNotUpdateGEPWDInCaseOfMandatoryLocalAttributeFailureForCA(Map<String, String> map) throws InterruptedException, IOException {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("itemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabNameLA"));
		BasePage.WaitForMiliSec(2000);
//		localAttPage.itemFieldAttributeChange(map.get("Item1")).selectValuefromSaveDropdown(map.get("Save"));
//		productDetailSearchPage.clickRefreshIcon();	
//		//updating one more time with other item.
//		localAttPage.itemFieldAttributeChange(map.get("Item2")).selectValuefromSaveDropdown(map.get("Save"));
//		productDetailSearchPage.clickRefreshIcon();
		
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//By Shubham
	
	
	
	
	
	
	
	
	
//	 ProductDetailSearchPage productDetailSearchPage = new ProductDetailSearchPage();
//	    QualityStatusPage qualityStatus = new QualityStatusPage();
//	    LocalAttributePage localAttributePage = new LocalAttributePage();
	    ActionsPage actionsPage = new ActionsPage();
	    TasksSubMenu tasksSubMenu = new TasksSubMenu();
	    
//	    ReferencesPage referencesPage = new ReferencesPage();
//		ProductDetailSearchPage productdetail = new ProductDetailSearchPage();
//		CatalogTypePage catalogTypePage = new CatalogTypePage();
//		QueriesSubMenu queriessubmenu = new QueriesSubMenu();
		QualityStatusPage qualityStatus = new QualityStatusPage();
//		List dental_user_driven;
//		List medical_user_driven;
		List zahn_user_driven;
//		Javautils javautils = new Javautils();
//		ProductDetailSearchPage productdetailspage = new ProductDetailSearchPage();


	    //VerifyFailedWorkFlowRuleTest
	    
	    @PimFrameworkAnnotation(module = Modules.WorkFlow, category = CategoryType.Valid_Failure)
	    @TestDataSheet(sheetname = TestCaseSheet.WorkFlow_TestData)
	    @Test(priority = 1, description = "Verify failed Verification Part | Verify failed workflow rules is generated for empty mandatory local attribute feilds", groups = {"US","pim","WorkFlow"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	    public void verifyFailedWorkFlowRuleIsGeneratedForEmptyMandatoryLocalAttributes(Map<String, String> map) {
	        PimHomepage pimHomepage = new LoginPage()
	                .enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
	                .enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
	        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
	                .selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("itemNumber")).clickSeachButton()
	                .clickOnFirstResult();

	        pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("LocalAttribute"));

	        qualityStatus.maximizeQualityStatusTab();

//	        //Update Local Attributes to No Content
//	        pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("LocalAttribute"));
//	        localAttributePage.MandatoryItemFieldAttribute(map.get("ItemValueEnter2")).selectValuefromSaveDropdown(map.get("saveButton"));
//	        productDetailSearchPage.clickRefreshIcon();
//	        productDetailSearchPage.clickRefreshIcon();
//	        localAttributePage.MandatoryItemFieldAttribute(map.get("ItemValueEnter"))
//	                .selectValuefromSaveDropdown(map.get("saveButton"));
//	        productDetailSearchPage.clickRefreshIcon();
//	        productDetailSearchPage.clickRefreshIcon();


	        //Verify Failed WorkFlow Rule in Quality Status Tab
	        productDetailSearchPage.selectTabfromDropdown(map.get("QualityStatusTab"));
	        productDetailSearchPage.clickRefreshIcon();
	        productDetailSearchPage.clickRefreshIcon();
	        List<String> LocalMandatoryRule2 = qualityStatus.sortRulestByStatus()
					.getRuleNamesByRuleStatus(map.get("Status"));
	        Assertions.assertThat(LocalMandatoryRule2).containsAnyOf(map.get("FailedWorkFlowRule"));
	        qualityStatus.minimizeQualityStatusTab();

	        //Trigger WorkFlowBusinessRule
	        actionsPage.clickOnActionsDropdown();
	        actionsPage.SelectBusinessRule(map.get("BusinessRunRules"));

	        //Verify Task is Created to Update Missing Mandatory Local Attributes
	        pimHomepage.clickLogoutButton();
	        PimHomepage pimHomepage2 = new LoginPage()
	                .enterUserName(ExcelUtils.getLoginData().get("Dental User").get("UserName"))
	                .enterPassword(ExcelUtils.getLoginData().get("Dental User").get("Password")).clickLoginButton();
	        pimHomepage.mainMenu().clickTasksMenu().enterSearchTextToFilter(map.get("CanadaTranslator")).clickFilterButton().SelectSpecificUserGroup(map.get("CanadaTranslator"));
	        productDetailSearchPage.applyFilterInSearchPageByAnyValue(map.get("TypeName"), map.get("ItemNumber")).clickOnFirstResult();
	        Assertions.assertThat(tasksSubMenu.isTaskPresent("TasksList")).isTrue();
	        String ActualTaskName = tasksSubMenu.getTaskName();
	        Assertions.assertThat(ActualTaskName).isEqualTo("["+map.get("ItemNumber")+"]"+" "+map.get("ExpectedTaskName"));
	        pimHomepage.clickLogoutButton();
	    }
	    
	    
	    //CatalogEntityWorkFlowTest
	    
	    //WRFL_009_US
	  	@PimFrameworkAnnotation(module = Modules.DENTAL_CATALOG, category = CategoryType.REGRESSION)
	  	@TestDataSheet(sheetname = TestCaseSheet.CatalogEntityRule)
	  	@Test(priority = 1, description = "WRFL_009_US Verification Part | WRFL_009_US | Verify substitution exception rule for child get triggered if we update the dental catalog in parent for US", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"US","pim","DENTAL_CATALOG"})
	  	public void verify_quality_status_when_dental_catalog_is_updated_in_parent_US_dental_division(Map<String, String> map) {
	  		PimHomepage pimHomepage = new LoginPage()
	  				.enterUserName(ExcelUtils.getLoginData().get("Dental User").get("UserName"))
	  				.enterPassword(ExcelUtils.getLoginData().get("Dental User").get("Password")).clickLoginButton();

	  		//verifying in parent item in US catalog
	  		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
	  		.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("ReferenceTab"));
	  		qualitystatus.maximizeQualityStatusTab();
	  		String referenceObjectNumber = referencesPage.getReferenceObjectNumber();
	  		qualitystatus.minimizeQualityStatusTab();

	  		productdetail.selectTabfromDropdown(map.get("TabName"));
	  		log(LogType.INFO, "Navigate to Dental catalog Tab to assign dental in userdriven in Parent");

//	  		catalogTypePage.clearUserDriven(map.get("userDriven"));
//	  		BasePage.WaitForMiliSec(3000);
	//
//	  		//assigning dental catalog
//	  		catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
	  		pimHomepage.productDetailSearchPage().clickRefreshIcon();
	  		dental_user_driven = catalogTypePage.getUserDrivenCatalogs();
	  		Assertions.assertThat(dental_user_driven).containsAnyOf(map.get("userDriven"));

	  		//verify in master
	  		productdetail.clickMenuRefreshIcon();
	  		queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
	  		log(LogType.INFO, "Navigate to Dental catalog tab to verify the userdriven in Master Catalog");
	  		List<String> masterUserdriven = catalogTypePage.getUserDrivenCatalogs();
	  		Assertions.assertThat(masterUserdriven).containsAnyOf(map.get("userDriven"));

	  		//verifying in child item
	  		productdetail.clickMenuRefreshIcon();
	  		queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
	  		.enterHsiItemNumber(referenceObjectNumber).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("Qualitystatustab"));
	  		log(LogType.INFO, "Navigate to Quality Status Tab to verify catalog entity rule in Child");

	  		qualitystatus.maximizeQualityStatusTab().sortRulestByLastExecution();
	  		List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("Status"));
	  		List<String> myList = new ArrayList<String>(Arrays.asList(map.get("CatalogEntityRule").split(",")));
	  		Assertions.assertThat(catalogentityrule).containsAll(myList);
	  		qualitystatus.minimizeQualityStatusTab();
	  		log(LogType.INFO, "Verifying catalog entity rule");
	  		pimHomepage.clickLogoutButton();
	  	}

	  	//WRFL_009_CA
	  	@PimFrameworkAnnotation(module = Modules.DENTAL_CATALOG, category = CategoryType.REGRESSION)
	  	@TestDataSheet(sheetname = TestCaseSheet.CatalogEntityRule)
	  	@Test(priority = 1, description = "WRFL_009_CA Verification Part | WRFL_009_CA | Verify substitution exception rule for child get triggered if we update the dental catalog in parent for CA", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"CA","pim","DENTAL_CATALOG"})
	  	public void verify_quality_status_when_dental_catalog_is_updated_in_parent_CA_dental_division(Map<String, String> map) {
	  		PimHomepage pimHomepage = new LoginPage()
	  				.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
	  				.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();

	  		//verifying in parent item in CA catalog
	  		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
	  		.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("ReferenceTab"));
	  		qualitystatus.maximizeQualityStatusTab();
	  		String referenceObjectNumber = referencesPage.getReferenceObjectNumber();
	  		qualitystatus.minimizeQualityStatusTab();

	  		productdetail.selectTabfromDropdown(map.get("TabName"));
	  		log(LogType.INFO, "Navigate to Canadian Dental catalog Tab to assign dental in userdriven in Parent");

//	  		catalogTypePage.clearUserDriven(map.get("userDriven"));
//	  		BasePage.WaitForMiliSec(3000);
	//
//	  		//assigning dental catalog
//	  		catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
	  		pimHomepage.productDetailSearchPage().clickRefreshIcon();
	  		dental_user_driven = catalogTypePage.getUserDrivenCatalogs();
	  		Assertions.assertThat(dental_user_driven).containsAnyOf(map.get("userDriven"));

	  		//verifying in child item
	  		productdetail.clickMenuRefreshIcon();
	  		queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
	  		.enterHsiItemNumber(referenceObjectNumber).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("Qualitystatustab"));
	  		log(LogType.INFO, "Navigate to Quality Status Tab to verify catalog entity rule in Child");

	  		qualitystatus.maximizeQualityStatusTab().sortRulestByLastExecution();
	  		List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("Status"));
	  		List<String> myList = new ArrayList<String>(Arrays.asList(map.get("CatalogEntityRule").split(",")));
	  		Assertions.assertThat(catalogentityrule).containsAll(myList);
	  		qualitystatus.minimizeQualityStatusTab();
	  		log(LogType.INFO, "Verifying catalog entity rule");
	  		pimHomepage.clickLogoutButton();
	  	}

	  	//WRFL_015_US
	  	@PimFrameworkAnnotation(module = Modules.MEDICAL_CATALOG, category = CategoryType.REGRESSION)
	  	@TestDataSheet(sheetname = TestCaseSheet.CatalogEntityRule)
	  	@Test(priority = 1, description = "WRFL_015_US Verification Part | WRFL_015_US | Verify substitution exception rule for child get triggered if we update the medical catalog in parent for US", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"US","pim","MEDICAL_CATALOG"})
	  	public void verify_quality_status_when_medical_catalog_is_updated_in_parent_US_medical_division(Map<String, String> map) {
	  		PimHomepage pimHomepage = new LoginPage()
	  				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
	  				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();

	  		//verifying in parent item in US catalog
	  		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
	  		.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("ReferenceTab"));
	  		qualitystatus.maximizeQualityStatusTab();
	  		String referenceObjectNumber = referencesPage.getReferenceObjectNumber();
	  		qualitystatus.minimizeQualityStatusTab();

	  		productdetail.selectTabfromDropdown(map.get("TabName"));
	  		log(LogType.INFO, "Navigate to Medical catalog Tab to assign medical in userdriven in Parent");

//	  		catalogTypePage.clearUserDriven(map.get("userDriven"));
//	  		BasePage.WaitForMiliSec(3000);
	//
//	  		//assigning medical catalog
//	  		catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
	  		pimHomepage.productDetailSearchPage().clickRefreshIcon();
	  		medical_user_driven = catalogTypePage.getUserDrivenCatalogs();
	  		Assertions.assertThat(medical_user_driven).containsAnyOf(map.get("userDriven"));

	  		//verify in master
	  		productdetail.clickMenuRefreshIcon();
	  		queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
	  		log(LogType.INFO, "Navigate to Medical catalog tab to verify the userdriven in Master Catalog");
	  		List<String> masterUserdriven = catalogTypePage.getUserDrivenCatalogs();
	  		Assertions.assertThat(masterUserdriven).containsAnyOf(map.get("userDriven"));

	  		//verifying in child item
	  		productdetail.clickMenuRefreshIcon();
	  		queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
	  		.enterHsiItemNumber(referenceObjectNumber).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("Qualitystatustab"));
	  		log(LogType.INFO, "Navigate to Quality Status Tab to verify catalog entity rule in Child");

	  		qualitystatus.maximizeQualityStatusTab().sortRulestByLastExecution();
	  		List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("Status"));
	  		List<String> myList = new ArrayList<String>(Arrays.asList(map.get("CatalogEntityRule").split(",")));
	  		Assertions.assertThat(catalogentityrule).containsAll(myList);
	  		qualitystatus.minimizeQualityStatusTab();
	  		log(LogType.INFO, "Verifying catalog entity rule");
	  		pimHomepage.clickLogoutButton();
	  	}

	  	//WRFL_015_CA
	  	@PimFrameworkAnnotation(module = Modules.MEDICAL_CATALOG, category = CategoryType.REGRESSION)
	  	@TestDataSheet(sheetname = TestCaseSheet.CatalogEntityRule)
	  	@Test(priority = 1, description = "WRFL_015_CA Verification Part | WRFL_015_CA | Verify substitution exception rule for child get triggered if we update the medical catalog in parent for CA", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"CA","pim","MEDICAL_CATALOG"})
	  	public void verify_quality_status_when_medical_catalog_is_updated_in_parent_CA_medical_division(Map<String, String> map) {
	  		PimHomepage pimHomepage = new LoginPage()
	  				.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
	  				.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();

	  		//verifying in parent item in CA catalog
	  		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
	  		.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("ReferenceTab"));
	  		qualitystatus.maximizeQualityStatusTab();
	  		String referenceObjectNumber = referencesPage.getReferenceObjectNumber();
	  		qualitystatus.minimizeQualityStatusTab();

	  		productdetail.selectTabfromDropdown(map.get("TabName"));
	  		log(LogType.INFO, "Navigate to Canadian Medical catalog Tab to assign medical in userdriven in Parent");

//	  		catalogTypePage.clearUserDriven(map.get("userDriven"));
//	  		BasePage.WaitForMiliSec(3000);
	//
//	  		//assigning medical catalog
//	  		catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
	  		pimHomepage.productDetailSearchPage().clickRefreshIcon();
	  		medical_user_driven = catalogTypePage.getUserDrivenCatalogs();
	  		Assertions.assertThat(medical_user_driven).containsAnyOf(map.get("userDriven"));

	  		//verifying in child item
	  		productdetail.clickMenuRefreshIcon();
	  		queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
	  		.enterHsiItemNumber(referenceObjectNumber).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("Qualitystatustab"));
	  		log(LogType.INFO, "Navigate to Quality Status Tab to verify catalog entity rule in Child");

	  		qualitystatus.maximizeQualityStatusTab().sortRulestByLastExecution();
	  		List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("Status"));
	  		List<String> myList = new ArrayList<String>(Arrays.asList(map.get("CatalogEntityRule").split(",")));
	  		Assertions.assertThat(catalogentityrule).containsAll(myList);
	  		qualitystatus.minimizeQualityStatusTab();
	  		log(LogType.INFO, "Verifying catalog entity rule");
	  		pimHomepage.clickLogoutButton();
	  	}

	  	//WRFL_027_US
	  	@PimFrameworkAnnotation(module = Modules.ZAHN_CATALOG, category = CategoryType.REGRESSION)
	  	@TestDataSheet(sheetname = TestCaseSheet.CatalogEntityRule)
	  	@Test(priority = 1, description = "WRFL_027_US Verification Part | WRFL_027_US | Verify substitution exception rule for child get triggered if we update the zahn catalog in parent for US", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"US","pim","ZAHN_CATALOG"})
	  	public void verify_quality_status_when_zahn_catalog_is_updated_in_parent_US_zahn_division(Map<String, String> map) {
	  		PimHomepage pimHomepage = new LoginPage()
	  				.enterUserName(ExcelUtils.getLoginData().get("Zahn User").get("UserName"))
	  				.enterPassword(ExcelUtils.getLoginData().get("Zahn User").get("Password")).clickLoginButton();

	  		//verifying in parent item in US catalog
	  		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
	  		.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("ReferenceTab"));
	  		qualitystatus.maximizeQualityStatusTab();
	  		String referenceObjectNumber = referencesPage.getReferenceObjectNumber();
	  		qualitystatus.minimizeQualityStatusTab();

	  		productdetail.selectTabfromDropdown(map.get("TabName"));
	  		log(LogType.INFO, "Navigate to zahn catalog Tab to assign caga in userdriven in Parent");

//	  		catalogTypePage.clearUserDriven(map.get("userDriven"));
//	  		BasePage.WaitForMiliSec(3000);
	//
//	  		//assigning zahn catalog
//	  		catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
	  		pimHomepage.productDetailSearchPage().clickRefreshIcon();
	  		zahn_user_driven = catalogTypePage.getUserDrivenCatalogs();
	  		Assertions.assertThat(zahn_user_driven).containsAnyOf(map.get("userDriven"));

	  		//verify in master
	  		productdetail.clickMenuRefreshIcon();
	  		queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
	  		log(LogType.INFO, "Navigate to zahn catalog tab to verify the userdriven in Master Catalog");
	  		List<String> masterUserdriven = catalogTypePage.getUserDrivenCatalogs();
	  		Assertions.assertThat(masterUserdriven).containsAnyOf(map.get("userDriven"));

	  		//verifying in child item
	  		productdetail.clickMenuRefreshIcon();
	  		queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
	  		.enterHsiItemNumber(referenceObjectNumber).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("Qualitystatustab"));
	  		log(LogType.INFO, "Navigate to Quality Status Tab to verify catalog entity rule in Child");

	  		qualitystatus.maximizeQualityStatusTab().sortRulestByLastExecution();
	  		List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("Status"));
	  		List<String> myList = new ArrayList<String>(Arrays.asList(map.get("CatalogEntityRule").split(",")));
	  		Assertions.assertThat(catalogentityrule).containsAll(myList);
	  		qualitystatus.minimizeQualityStatusTab();
	  		log(LogType.INFO, "Verifying catalog entity rule");
	  		pimHomepage.clickLogoutButton();
	  	}

	  	//WRFL_027_CA
	  	@PimFrameworkAnnotation(module = Modules.ZAHN_CATALOG, category = CategoryType.REGRESSION)
	  	@TestDataSheet(sheetname = TestCaseSheet.CatalogEntityRule)
	  	@Test(priority = 1, description = "WRFL_027_CA Verification Part | WRFL_027_CA | Verify substitution exception rule for child get triggered if we update the zahn catalog in parent for CA", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"CA","pim","ZAHN_CATALOG"})
	  	public void verify_quality_status_when_zahn_catalog_is_updated_in_parent_CA_zahn_division(Map<String, String> map) {
	  		PimHomepage pimHomepage = new LoginPage()
	  				.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
	  				.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();

	  		//verifying in parent item in CA catalog
	  		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
	  		.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("ReferenceTab"));
	  		qualitystatus.maximizeQualityStatusTab();
	  		String referenceObjectNumber = referencesPage.getReferenceObjectNumber();
	  		qualitystatus.minimizeQualityStatusTab();

	  		productdetail.selectTabfromDropdown(map.get("TabName"));
	  		log(LogType.INFO, "Navigate to Canadian zahn catalog Tab to assign caga in userdriven in Parent");

//	  		catalogTypePage.clearUserDriven(map.get("userDriven"));
//	  		BasePage.WaitForMiliSec(3000);
//
//	  		//assigning zahn catalog
//	  		catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
//	  		pimHomepage.productDetailSearchPage().clickRefreshIcon();
	  		zahn_user_driven = catalogTypePage.getUserDrivenCatalogs();

	  		//verifying in child item
	  		productdetail.clickMenuRefreshIcon();
	  		queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
	  		.enterHsiItemNumber(referenceObjectNumber).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("Qualitystatustab"));
	  		log(LogType.INFO, "Navigate to Quality Status Tab to verify catalog entity rule in Child");

	  		qualitystatus.maximizeQualityStatusTab().sortRulestByLastExecution();
	  		List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("Status"));
	  		List<String> myList = new ArrayList<String>(Arrays.asList(map.get("CatalogEntityRule").split(",")));
	  		Assertions.assertThat(catalogentityrule).containsAll(myList);
	  		qualitystatus.minimizeQualityStatusTab();
	  		log(LogType.INFO, "Verifying catalog entity rule");
	  		pimHomepage.clickLogoutButton();
	  	}
	  	
  	
	  	
	

}

