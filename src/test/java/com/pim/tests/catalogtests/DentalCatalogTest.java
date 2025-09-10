package com.pim.tests.catalogtests;

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
import com.pim.pages.LoginPage;
import com.pim.pages.PimHomepage;
import com.pim.pages.ProductDetailSearchPage;
import com.pim.pages.QualityStatusPage;
import com.pim.pages.QueriesSubMenu;
import com.pim.tests.BaseTest;
import com.pim.utils.DataProviderUtils;
import com.pim.utils.DateandTimeUtils;
import com.pim.utils.ExcelUtils;
import com.pim.utils.FileUtils;
import com.pim.utils.Javautils;

public class DentalCatalogTest extends BaseTest {
	private DentalCatalogTest() {

	}

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
	@Test(description = "CATR_077, CATR_078, CATR_079, CATR_080, CATR_081, CATR_013 | Verifying Rule Driven catalog for Dental Division in US Master Catalog", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			"US", "pim","DENTAL_CATALOG" })
	public void verify_rule_driven_for_dental_division(Map<String, String> map) {
		
		FileUtils.storeCurrentTimeInJsonTimesPropertiesFile(map.get("TestCaseName")+map.get("ItemNumber"));

		// CatalogTypePage catalogTypePage = new CatalogTypePage();
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Dental User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Dental User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));

		catalogTypePage.clearUserDriven(map.get("userDriven"));

		BasePage.WaitForMiliSec(3000);
		catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
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
		BasePage.WaitForMiliSec(3000);
		Assertions.assertThat(rule_driven).containsAll(masterRuledriven);
		pimHomepage.clickLogoutButton();
	}

	// CATR_077 to 081
	@PimFrameworkAnnotation(module = Modules.DENTAL_CATALOG, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Catalog)
	@Test(description = "CATR_077, CATR_078, CATR_079, CATR_080, CATR_081 | Verifying Rule Driven catalog for Dental Division in CA and Master Catalog", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			"CA", "pim","DENTAL_CATALOG" })

	public void verify_rule_driven_for_dental_division_CA(Map<String, String> map) {

		// CatalogTypePage catalogTypePage = new CatalogTypePage();
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));

		List<String> userdrivencatalogs = catalogTypePage.getUserDrivenCatalogs();
		List<String> multiplevaluesfromexcel = javautils.readMultipleValuesFromExcel(map.get("userDriven"));
		for (String userdriven : multiplevaluesfromexcel) {
			if (userdrivencatalogs.contains(userdriven)) {
				// delete userdriven
				catalogTypePage.clearSelectedFields(userdriven);
				productdetailspage.clickRefreshIcon();
				log(LogType.INFO, "Deleting the Userdriven");
			}
		}
		BasePage.WaitForMiliSec(3000);

		catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
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
				.clickOnSecondResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
		List<String> masterRuledriven = catalogTypePage.getRuleDrivenCatalogs();
		Assertions.assertThat(rule_driven).containsAll(masterRuledriven);
		pimHomepage.clickLogoutButton();
	}

	// CATR_179 and CATR_180, CATR_132
	@PimFrameworkAnnotation(module = Modules.DENTAL_CATALOG, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Catalog)
	@Test(description = "CATR_179 and CATR_180, CATR_132 | Verify All catalog tabs and exception lists for Dental Catalog list populates the catalogs with User, Rule Driven and excluding the Catalogs selected as Exception  For US", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			"US", "pim","DENTAL_CATALOG" })
	public void verify_all_catalog_tabs_and_exception_list_for_dental_division(Map<String, String> map) {
		
		FileUtils.storeCurrentTimeInJsonTimesPropertiesFile(map.get("TestCaseName")+map.get("ItemNumber"));
		
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
		for (String userdriven : multiplevaluesfromexcel) {
			if (userdrivencatalogs.contains(userdriven)) {
				// delete userdriven
				catalogTypePage.clearExceptionListField(userdriven);
				productdetailspage.clickRefreshIcon();
				log(LogType.INFO, "Deleting the Userdriven");
			}
		}
		BasePage.WaitForMiliSec(3000);
		catalogTypePage.clickExceptionListField().selectExceptionCatalogsFromList(map.get("userDriven"));
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
	@Test(description = "CATR_179 and CATR_180, CATR_132 | Verify All catalog tabs and exception lists for Dental Catalog list populates the catalogs with User, Rule Driven and excluding the Catalogs selected as Exception For CA", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
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
		for (String userdriven : multiplevaluesfromexcel) {
			if (userdrivencatalogs.contains(userdriven)) {
				// delete userdriven
				catalogTypePage.clearExceptionListField(userdriven);
				productdetailspage.clickRefreshIcon();
				log(LogType.INFO, "Deleting the Userdriven");
			}
		}
		BasePage.WaitForMiliSec(3000);
		catalogTypePage.clickExceptionListField().selectExceptionCatalogsFromList(map.get("userDriven"));
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
	@Test(description = "CATR_001,CATR_002,CATR_003,CATR_004,CATR_005,CATR_006,CATR_007,CATR_008,CATR_009,CATR_010,CATR_013,CATR_017 |Dental Catalog Verifying rule driven, userdriven, exception list, publish date, publish flag are visible and editable In US", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
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
	@Test(description = "CATR_001,CATR_002,CATR_003,CATR_004,CATR_005,CATR_006,CATR_007,CATR_008,CATR_009,CATR_010,CATR_013,CATR_017 | Dental Catalog Verifying rule driven, userdriven, exception list, publish date, publish flag are visible and editable in CA", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
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
	@Test(description = "WRFL_005 and WRFL_006 | Verify Set of Catalog Entity Rules triggered if we update the Publish date and Flag in Catalog Assignment for US", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
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
		catalogTypePage.selectPublishDate(DateandTimeUtils.getTodaysDate()).selectPublishFlag();
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

	// WRFL_005 and WRFL_006
	@PimFrameworkAnnotation(module = Modules.DENTAL_CATALOG, category = CategoryType.SMOKE)
	@TestDataSheet(sheetname = TestCaseSheet.Catalog)
	@Test(description = "WRFL_005 and WRFL_006 | Verify Set of Catalog Entity Rules triggered if we update the Publish date and Flag in Catalog Assignment for CA - WRFL_005 and WRFL_006", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
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
		catalogTypePage.selectPublishDate(DateandTimeUtils.getTodaysDate()).selectPublishFlag();
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

	// ITP_029
	// we need recently created item from pix and make change on ItemPublish sheet
	@PimFrameworkAnnotation(module = Modules.DENTAL_CATALOG, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemPublish)
	@Test(description = " ITP_029 | Verify whether the Publish date changed by the Dental division owner gets updated for other divisions when the other divisions have not modified the Original Publish Date for US.", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
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
		if (!dentalpublishflag.equals("Yes")) {
			catalogTypePage.selectPublishFlag(map.get("PublishFlag"));
			log(LogType.INFO, "assigning yes in publish flag");
		}

		catalogTypePage.selectPublishDate(DateandTimeUtils.getTodaysDate());
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
	@Test(description = " ITP_029 | Verify whether the Publish date changed by the Dental division owner gets updated for other divisions when the other divisions have not modified the Original Publish Date for CA.", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
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
		if (!publishflag.equals("Yes")) {
			catalogTypePage.selectPublishFlag(map.get("PublishFlag"));
			log(LogType.INFO, "assigning Yes in publish flag");
		}
		catalogTypePage.verifySelectPublishDateFieldDropdown();
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
	@Test(description = "ITP_034 | Verify whether the Publish date changed by the Dental Owner division should not gets updated for other divisions when the other divisions have modified the Original Publish Date for US.", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
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
		if (!publishflag.equals("Yes")) {
			catalogTypePage.selectPublishFlag(map.get("PublishFlag"));
			log(LogType.INFO, "assigning Yes in publish flag");
		}
		catalogTypePage.selectPublishDate(DateandTimeUtils.getTodaysDate());
		productdetailspage.clickRefreshIcon();
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
	@Test(description = "ITP_034 | Verify whether the Publish date changed by the Dental Owner division should not gets updated for other divisions when the other divisions have modified the Original Publish Date for CA.", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
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
		if (!publishflag.equals("Yes")) {
			catalogTypePage.selectPublishFlag(map.get("PublishFlag"));
			log(LogType.INFO, "assigning Yes in publish flag");
		}
		catalogTypePage.selectPublishDate(DateandTimeUtils.getTodaysDate());
		productdetailspage.clickRefreshIcon();
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

	// prerequisite for Middleware

	//MDLW_005
	@PimFrameworkAnnotation(module = Modules.DENTAL_CATALOG, category = CategoryType.SMOKE)
	@TestDataSheet(sheetname = TestCaseSheet.Catalog)
	@Test(description = "MDLW_005 | Verify whether the catalogs assigned to items are displayed in the respective columns in respective table", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			"SMOKE","US", "pim","DENTAL_CATALOG" })
	public void verify_catalog_assigned_item_displays_in_database(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Dental User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Dental User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
		log(LogType.INFO, "Navigate to Dental tab from Queries Menu for US Catalog");

		List<String> userdrivencatalogs = catalogTypePage.getUserDrivenCatalogs();
		List<String> multiplevaluesfromexcel = javautils.readMultipleValuesFromExcel(map.get("userDriven"));
		for (String userdriven : multiplevaluesfromexcel) {
			if (userdrivencatalogs.contains(userdriven)) {
				// delete userdriven
				catalogTypePage.clearSelectedFields(userdriven);
				productdetailspage.clickRefreshIcon();
				log(LogType.INFO, "Deleting the Userdriven");
			}
		}

		BasePage.WaitForMiliSec(3000);
		catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();

	}

	@PimFrameworkAnnotation(module = Modules.DENTAL_CATALOG, category = CategoryType.SMOKE)
	@TestDataSheet(sheetname = TestCaseSheet.Catalog)
	@Test(description = "MDLW_015 | Verify the Publish flag and publish date  was updated in  respective table from the outbound file", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			"SMOKE","US", "pim","DENTAL_CATALOG" })
	public void verify_publish_flag_and_date_update_in_respective_column(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Dental User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Dental User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
		log(LogType.INFO, "Navigate to Dental tab from Queries Menu for US Catalog");

		catalogTypePage.clearUserDriven(map.get("userDriven"));
		BasePage.WaitForMiliSec(3000);
		catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
		productdetailspage.clickRefreshIcon();

		catalogTypePage.selectPublishFlag().selectPublishDate(DateandTimeUtils.getTodaysDate());
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

		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType("Master catalog").clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
		BasePage.WaitForMiliSec(3000);

		pimHomepage.clickLogoutButton();
	}

	@PimFrameworkAnnotation(module = Modules.DENTAL_CATALOG, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Catalog)
	@Test(description = "MDLW_016 prerequisite | Verify for the Item default values for Publish Flag and Publish Date gets updated in respective table  when items are loaded from PIX to PIM", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			"US", "pim","DENTAL_CATALOG" })
	public void verify_publish_date_and_flag_should_updated_when_item_loaded_from_pix_to_pim(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Dental User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Dental User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
		log(LogType.INFO, "Navigate to Dental tab from Queries Menu for US Catalog");

		catalogTypePage.clearUserDriven(map.get("userDriven"));
		BasePage.WaitForMiliSec(3000);
		catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
		productdetailspage.clickRefreshIcon();

		catalogTypePage.selectPublishFlag();
		productdetailspage.clickRefreshIcon();
		pimHomepage.clickLogoutButton();
	}

	// CATR_219-Verify if AED_FSC gets removed from user driven list if Dental is
	// chosen in user driven catalog list of dental division
	@PimFrameworkAnnotation(module = Modules.DENTAL_CATALOG, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Catalog_CA)
	@Test(description = "CATR_219 | Verify if AED_FSC gets removed from user driven list if Dental is chosen in user driven catalog list of dental division", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			"CA", "pim","DENTAL_CATALOG" })
	public void Verify_if_AED_FSC_gets_removed_from_user_driven_list_if_Dental_is_chosen_in_user_driven_catalog_list_of_dental_division_CATR_219(
			Map<String, String> map) {

		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
		catalogTypePage.clearUserDriven(map.get("userDriven1"));
		catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven1"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		user_driven = catalogTypePage.getUserDrivenCatalogs();
		Assertions.assertThat(user_driven).containsAnyOf(map.get("userDriven1"));
	}

	// ITP_017 and Middleware TC_ID is MDLW_017
	// Verify Item with Publish Flag = 'Y" and Publish Date as Future Date - All
	// Divisions and check in Middleware
	@PimFrameworkAnnotation(module = Modules.DENTAL_CATALOG, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemPublish)
	@Test(description = "ITP_017,MDLW_017  | Verify whether the Publish Flag for a Dental Item get updated as 'Y' and publish date be future in the Middleware Export File, when the Items loaded from PIX to PIM with Publish Flag as \"Y\" for US", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
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
		if (!dentalpublishflag.equals(Y)) {
			catalogTypePage.selectPublishFlag(map.get("PublishFlag"));
			log(LogType.INFO, "assigning yes in publish flag");
		}
		catalogTypePage.selectPublishDate(DateandTimeUtils.getFutureDate());
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
	@Test(description = "ITP_017 | Verify whether the Publish Flag for a Dental Item get updated as 'Y' and publish date be future in the Middleware Export File, when the Items loaded from PIX to PIM with Publish Flag as \"Y\" for CA", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
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
		if (!dentalpublishflag.equals(Y)) {
			catalogTypePage.selectPublishFlag(map.get("PublishFlag"));
			log(LogType.INFO, "assigning yes in publish flag");
		}
		catalogTypePage.selectPublishDate(DateandTimeUtils.getFutureDate());
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
	@Test(description = "ITP_025 | Verify whether the Publish Flag for a Dental Item get updated as \"N\" in the Middleware Export File, when the Items loaded from PIX to PIM with Publish Flag as \"N\" for US", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			"US", "pim","DENTAL_CATALOG" })
	public void verify_publish_flag_should_updated_as_NO_in_Dental_division_for_US(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Dental User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Dental User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("DentalDivision"));
		log(LogType.INFO, "Navigate to Dental tab from Queries Menu for US Catalog");

		String dentalpublishflag = catalogTypePage.getPublishFlag();
		if (!dentalpublishflag.equals("No")) {
			catalogTypePage.selectPublishFlag(map.get("PublishFlag"));
			log(LogType.INFO, "assigning No in publish flag");
		}
		List<String> userdrivencatalogs = catalogTypePage.getUserDrivenCatalogs();
		if (!userdrivencatalogs.contains(map.get("userDriven"))) {
			catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
		}

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
	@Test(description = "ITP_025 | Verify whether the Publish Flag for a Dental Item get updated as \"N\" in the Middleware Export File, when the Items loaded from PIX to PIM with Publish Flag as \"N\" for CA", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			"CA", "pim","DENTAL_CATALOG" })
	public void verify_publish_flag_should_updated_as_NO_in_Dental_division_for_CA(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("DentalDivision"));
		log(LogType.INFO, "Navigate to Dental tab from Queries Menu for CA Catalog");

		String dentalpublishflag = catalogTypePage.getPublishFlag();
		if (!dentalpublishflag.equals("No")) {
			catalogTypePage.selectPublishFlag(map.get("PublishFlag"));
			log(LogType.INFO, "assigning No in publish flag");
		}
		List<String> userdrivencatalogs = catalogTypePage.getUserDrivenCatalogs();
		if (!userdrivencatalogs.contains(map.get("userDriven"))) {
			catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
		}

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
	@Test(description = "ITP_021 | Verify whether the Publish Flag for a Dental Item get updated as \"Y\" and publish date as Current in the Middleware Export File, when the Items loaded from PIX to PIM with Publish Flag as \"Y\" for US", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			"US", "pim","DENTAL_CATALOG" })
	public void verify_publish_flag_should_updated_as_Yes_in_Dental_division_for_US(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Dental User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Dental User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("DentalDivision"));
		log(LogType.INFO, "Navigate to Dental tab from Queries Menu for US Catalog");

		catalogTypePage.selectPublishFlag(map.get("PublishFlag"));
		log(LogType.INFO, "assigning Yes in publish flag");

		catalogTypePage.selectPublishDate(DateandTimeUtils.getTodaysDate());
		productdetailspage.clickRefreshIcon();
		log(LogType.INFO, "assigning current date");

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
	@Test(description = "ITP_021 | Verify whether the Publish Flag for a Dental Item get updated as \"Y\" and publish date as Current in the Middleware Export File, when the Items loaded from PIX to PIM with Publish Flag as \"Y\" for CA", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			"CA", "pim","DENTAL_CATALOG" })
	public void verify_publish_flag_should_updated_as_Yes_in_Dental_division_for_CA(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("DentalDivision"));
		log(LogType.INFO, "Navigate to Dental tab from Queries Menu for US Catalog");

		catalogTypePage.selectPublishFlag(map.get("PublishFlag"));
		log(LogType.INFO, "assigning Yes in publish flag");

		catalogTypePage.selectPublishDate(DateandTimeUtils.getTodaysDate());
		productdetailspage.clickRefreshIcon();
		log(LogType.INFO, "assigning current date");

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
				.clickOnSecondResult().selectTabfromDropdown(map.get("DentalDivision")).clickRefreshIcon();
		log(LogType.INFO, "Navigate to Dental division in Master Catalog");

		String masterpublishdate = catalogTypePage.getPublishDate();
		String masterpublishFlag = catalogTypePage.getPublishFlag();

		Assertions.assertThat(masterpublishdate).isEqualTo(publishdate);
		Assertions.assertThat(masterpublishFlag).isEqualTo(publishFlag);
		pimHomepage.clickLogoutButton();
	}

}