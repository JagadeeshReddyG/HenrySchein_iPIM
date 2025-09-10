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
import com.pim.utils.Javautils;

public class MedicalCatalogTest extends BaseTest {
	private MedicalCatalogTest() {
	}

	CatalogTypePage catalogTypePage = new CatalogTypePage();
	AllCatalogsPage allCatalogsPage = new AllCatalogsPage();
	QualityStatusPage qualitystatus = new QualityStatusPage();
	ProductDetailSearchPage productdetailspage = new ProductDetailSearchPage();
	QueriesSubMenu queriesSubMenu = new QueriesSubMenu();
	PimHomepage pimHomepage = new PimHomepage();
	LoginPage loginPage = new LoginPage();
	LoginPage loginpage = new LoginPage();
	Javautils javautils = new Javautils();
	//List<String> rule_driven;
	List<String> user_driven;
	List<String> all_medical_catalog;
	List<String> user_and_rule_driven = new ArrayList<>();

	// CATR_082,CATR_083,CATR_084,CATR_085,CATR_086,CATR_087
	@PimFrameworkAnnotation(module = Modules.MEDICAL_CATALOG, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Catalog)
	@Test(description = "CATR_082,CATR_083,CATR_084,CATR_085,CATR_086,CATR_087 | Verifying Rule Driven catalog for Medical Division in US, CA and Master Catalog", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			"US", "pim","MEDICAL_CATALOG" })
	public void verify_rule_driven_for_medical_division(Map<String, String> map)
			throws InterruptedException, IOException {

		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
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
		BasePage.WaitForMiliSec(5000);
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
		BasePage.WaitForMiliSec(5000);
		List<String> masterRuledriven = catalogTypePage.getRuleDrivenCatalogs();
		Assertions.assertThat(rule_driven).containsAll(masterRuledriven);
		pimHomepage.clickLogoutButton();
	}

	// CATR_083
	@PimFrameworkAnnotation(module = Modules.MEDICAL_CATALOG, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Catalog)
	@Test(description = "CATR_083 | Verifying Rule Driven catalog for Medical Division in US, CA and Master Catalog", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			"CA", "pim","MEDICAL_CATALOG" })
	public void verify_rule_driven_for_medical_division_CA(Map<String, String> map)
			throws InterruptedException, IOException {

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
	@Test(description = "CATR_181 and CATR_182, CATR_133 | Verify All catalog tabs and exception lists for Medical Catalog list populates the catalogs with User, Rule Driven and excluding the Catalogs selected as Exception for US", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			"US", "pim","MEDICAL_CATALOG" })
	public void verify_all_catalog_tabs_and_exception_list_for_medical_division(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));

		user_driven = catalogTypePage.getUserDrivenCatalogs();
		List<String> rule_driven = catalogTypePage.getRuleDrivenCatalogs();

		user_and_rule_driven.addAll(rule_driven);
		user_and_rule_driven.addAll(user_driven); // took user and rule driven
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("AllCatalogtab")).clickRefreshIcon();
		all_medical_catalog = allCatalogsPage.getAllMedicalCatalogList();
		Assertions.assertThat(user_and_rule_driven).containsAll(all_medical_catalog);// verified all catalog without
																						// exception

		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("AllCatalogtab")).clickRefreshIcon();
		BasePage.WaitForMiliSec(5000);
		List<String> master_all_medical_catalog = allCatalogsPage.getAllMedicalCatalogList();
		// verified all catalog tab in master
		Assertions.assertThat(user_and_rule_driven).containsAll(master_all_medical_catalog);

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
				log(LogType.INFO, "Deleting the exception list");
			}
		}
		BasePage.WaitForMiliSec(3000);
		catalogTypePage.clickExceptionListField().selectExceptionCatalogsFromList(map.get("userDriven"));
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
	@Test(description = "CATR_181 and CATR_182 | Verify All catalog tabs and exception lists for Medical Catalog list populates the catalogs with User, Rule Driven and excluding the Catalogs selected as Exception for CA", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			"CA", "pim","MEDICAL_CATALOG" })
	public void verify_all_catalog_tabs_and_exception_list_for_medical_division_CA(Map<String, String> map) {
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
	@Test(description = "CATR_018,CATR_019,CATR_020,CATR_021,CATR_022,CATR_023,CATR_024,CATR_025,CATR_026,CATR_027, CATR_030,CATR_034 | Medical Catalog Verifying rule driven, userdriven, exception list, publish date, publish flag are visible and editable In US", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
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
	@Test(description = " CATR_018 - CATR_027, CATR_030,CATR_034 | Verifying Medical rule driven, userdriven, exception list, publish date, publish flag are visible and editable in CA", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
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
	@Test(description = "WRFL_011 and - 012 | Verify Set of Catalog Entity Rules triggered if we update the Publish date and Flag in Catalog Assignment for US", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
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

	// Verify Set of Catalog Entity Rules triggered if we update the Publish date
	// and Flag in Catalog Assignment TCID - WRFL_011 and - 012
	@PimFrameworkAnnotation(module = Modules.MEDICAL_CATALOG, category = CategoryType.SMOKE)
	@TestDataSheet(sheetname = TestCaseSheet.Catalog)
	@Test(description = "WRFL_011 and - 012 | Verify Set of Catalog Entity Rules triggered if we update the Publish date and Flag in Catalog Assignment for CA", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
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

	// we need recently created item and make change on ItemPublish sheet ITP_030
	@PimFrameworkAnnotation(module = Modules.MEDICAL_CATALOG, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemPublish)
	@Test(description = "ITP_030 | Verify whether the Publish date changed by the Medical division owner gets updated for other divisions when the other divisions have not modified the Original Publish Date for US.", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
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

		String publishflag = catalogTypePage.getPublishFlag();
		if (!publishflag.equals("Yes")) {
			catalogTypePage.selectPublishFlag(map.get("PublishFlag"));
			log(LogType.INFO, "assigning Yes in publish flag");
		}
		catalogTypePage.selectPublishDate(DateandTimeUtils.getTodaysDate());
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
		// Assertions.assertThat(masterpublishflag).isEqualTo(map.get("PublishFlag"));
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

		productdetailspage.selectTabfromDropdown(map.get("SpecialMarketsDivision")).clickRefreshIcon();
		String masterspecialmarketspublishdate = catalogTypePage.getPublishDate();
		Assertions.assertThat(masterspecialmarketspublishdate).isEqualTo(medicalpublishdate);
		log(LogType.INFO, "verified publish date in Special Markets division");
		pimHomepage.clickLogoutButton();
	}

	// we need recently created item and make change on ItemPublish sheet
	@PimFrameworkAnnotation(module = Modules.MEDICAL_CATALOG, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemPublish)
	@Test(description = "ITP_030 | Verify whether the Publish date changed by the Medical division owner gets updated for other divisions when the other divisions have not modified the Original Publish Date. for CA", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"CA", "pim","MEDICAL_CATALOG" })
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
		String publishflag = catalogTypePage.getPublishFlag();
		if (!publishflag.equals("Yes")) {
			catalogTypePage.selectPublishFlag(map.get("PublishFlag"));
			log(LogType.INFO, "assigning Yes in publish flag");
		}
		// qualitystatus.minimizeQualityStatusTab();
		catalogTypePage.verifySelectPublishDateFieldDropdown();
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
		// Assertions.assertThat(masterpublishflag).isEqualTo(map.get("PublishFlag"));
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
	@Test(description = "ITP_035 | Verify whether the Publish date changed by the Medical Owner division should not gets updated for other divisions when the other divisions have modified the Original Publish Date for US.", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
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

		String publishflag = catalogTypePage.getPublishFlag();
		if (!publishflag.equals("Yes")) {
			catalogTypePage.selectPublishFlag(map.get("PublishFlag"));
			log(LogType.INFO, "assigning Yes in publish flag");
		}
		catalogTypePage.selectPublishDate(DateandTimeUtils.getTodaysDate());
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

		String dentalpublishflag = catalogTypePage.getPublishFlag();
		// Assertions.assertThat(dentalpublishflag).isEqualTo(map.get("PublishFlag"));
		catalogTypePage.selectPublishDate(DateandTimeUtils.getFutureDate());
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
	@Test(description = "ITP_035 | Verify whether the Publish date changed by the Medical Owner division should not gets updated for other divisions when the other divisions have modified the Original Publish Date for CA.", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
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
		String publishflag = catalogTypePage.getPublishFlag();
		if (!publishflag.equals("Yes")) {
			catalogTypePage.selectPublishFlag(map.get("PublishFlag"));
			log(LogType.INFO, "assigning Yes in publish flag");
		}

		//change publish date to today's in medical
		catalogTypePage.selectPublishDate(DateandTimeUtils.getTodaysDate());
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

		String dentalpublishflag = catalogTypePage.getPublishFlag();
		Assertions.assertThat(dentalpublishflag).isEqualTo(map.get("PublishFlag"));
		catalogTypePage.selectPublishDate(DateandTimeUtils.getFutureDate());
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
	@Test(description = "CATR_220 | Verify if AEM_FSC gets removed from user driven list if Medical is chosen in user driven catalog list of dental division", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			"CA", "pim","MEDICAL_CATALOG" })
	public void Verify_if_AEM_FSC_gets_removed_from_user_driven_list_if_Medical_is_chosen_in_user_driven_catalog_list_of_dental_division_CATR_220(
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
		System.out.println(user_driven);
		Assertions.assertThat(user_driven).containsAnyOf(map.get("userDriven1"));
	}

	// ITP_018
	// Verify Item with Publish Flag = 'Y" and Publish Date as Future Date - All
	// Divisions and check in Middleware
	@PimFrameworkAnnotation(module = Modules.MEDICAL_CATALOG, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemPublish)
	@Test(description = "ITP_018  | Verify whether the Publish Flag for a Medical Item get updated as 'Y' and publish date as future in the Middleware Export File, when the Items loaded from PIX to PIM with Publish Flag as \"Y\" for US", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
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
	@Test(description = "ITP_018 | Verify whether the Publish Flag for a Medical Item get updated as 'Y' and publish date as future in the Middleware Export File, when the Items loaded from PIX to PIM with Publish Flag as 'Y' for CA", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
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
	@Test(description = "ITP_026 | Verify whether the Publish Flag for a Medical Item get updated as \"N\" in the Middleware Export File, when the Items loaded from PIX to PIM with Publish Flag as \"N\" for US", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			"US", "pim","MEDICAL_CATALOG" })
	public void verify_publish_flag_should_updated_as_NO_in_Medical_division_for_US(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("MedicalDivision"));
		log(LogType.INFO, "Navigate to Medical tab from Queries Menu for US Catalog");

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
				.clickOnFirstResult().selectTabfromDropdown(map.get("MedicalDivision")).clickRefreshIcon();
		log(LogType.INFO, "Navigate to Medical division in Master Catalog");

		String masterpublishflag = catalogTypePage.getPublishFlag();
		Assertions.assertThat(masterpublishflag).isEqualTo(map.get("PublishFlag"));
		pimHomepage.clickLogoutButton();
	}

	// ITP_026
	@PimFrameworkAnnotation(module = Modules.MEDICAL_CATALOG, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemPublish)
	@Test(description = "ITP_026 | Verify whether the Publish Flag for a Medical Item get updated as \"N\" in the Middleware Export File, when the Items loaded from PIX to PIM with Publish Flag as \"N\" for CA", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			"CA", "pim","MEDICAL_CATALOG" })
	public void verify_publish_flag_should_updated_as_NO_in_Medical_division_for_CA(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("MedicalDivision"));
		log(LogType.INFO, "Navigate to Medical tab from Queries Menu for CA Catalog");

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
				.clickOnSecondResult().selectTabfromDropdown(map.get("MedicalDivision")).clickRefreshIcon();
		log(LogType.INFO, "Navigate to Medical division in Master Catalog");

		String masterpublishflag = catalogTypePage.getPublishFlag();
		Assertions.assertThat(masterpublishflag).isEqualTo(map.get("PublishFlag"));
		pimHomepage.clickLogoutButton();
	}

	// ITP_022
	@PimFrameworkAnnotation(module = Modules.MEDICAL_CATALOG, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemPublish)
	@Test(description = "ITP_022 | Verify whether the Publish Flag for a Medical Item get updated as \"Y\" and publish date as Currentin the Middleware Export File, when the Items loaded from PIX to PIM with Publish Flag as \"Y\" for US", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			"US", "pim","MEDICAL_CATALOG" })
	public void verify_publish_flag_should_updated_as_Yes_in_Medical_division_for_US(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("MedicalDivision"));
		log(LogType.INFO, "Navigate to Medical tab from Queries Menu for US Catalog");

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
	@Test(description = "ITP_022 | Verify whether the Publish Flag for a Medical Item get updated as \"Y\" and publish date as Current in the Middleware Export File, when the Items loaded from PIX to PIM with Publish Flag as \"Y\" for CA", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			"CA", "pim","MEDICAL_CATALOG" })
	public void verify_publish_flag_should_updated_as_Yes_in_Medical_division_for_CA(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("MedicalDivision"));
		log(LogType.INFO, "Navigate to Medical tab from Queries Menu for US Catalog");

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
				.clickOnSecondResult().selectTabfromDropdown(map.get("MedicalDivision")).clickRefreshIcon();
		log(LogType.INFO, "Navigate to Medical division in Master Catalog");

		String masterpublishdate = catalogTypePage.getPublishDate();
		String masterpublishFlag = catalogTypePage.getPublishFlag();

		Assertions.assertThat(masterpublishdate).isEqualTo(publishdate);
		Assertions.assertThat(masterpublishFlag).isEqualTo(publishFlag);
		pimHomepage.clickLogoutButton();
	}

}
