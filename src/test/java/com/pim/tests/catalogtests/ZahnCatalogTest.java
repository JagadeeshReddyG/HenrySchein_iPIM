package com.pim.tests.catalogtests;

import static com.pim.reports.FrameworkLogger.log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.assertj.core.api.Assertions;
import org.testng.Assert;
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

public class ZahnCatalogTest extends BaseTest {
	private ZahnCatalogTest() {

	}

	CatalogTypePage catalogTypePage = new CatalogTypePage();
	AllCatalogsPage allCatalogsPage = new AllCatalogsPage();
	QualityStatusPage qualitystatus = new QualityStatusPage();
	ProductDetailSearchPage productdetailspage = new ProductDetailSearchPage();
	QueriesSubMenu queriesSubMenu = new QueriesSubMenu();
	LoginPage loginpage = new LoginPage();
	PimHomepage pimHomepage = new PimHomepage();
	Javautils javautils = new Javautils();
	//List<String> rule_driven;
	//List<String> user_driven;
	//List<String> all_zahn_catalog;
	//List<String> user_and_rule_driven = new ArrayList<>();

	// CATR_088
	@PimFrameworkAnnotation(module = Modules.ZAHN_CATALOG, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Catalog)
	@Test(description = "CATR_088 | Verifying Rule Driven catalog for Zahn Division in US, CA and Master Catalog", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
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
		catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
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
	@Test(description = "CATR_089 | Verifying Rule Driven catalog for Zahn Division in US, CA and Master Catalog", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
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
	@Test(description = "CATR_183, CATR_184 For US | Verify All catalog tabs and exception lists for ZAHN Catalog list populates the catalogs with User, Rule Driven and excluding the Catalogs selected as Exception for US", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
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
		user_and_rule_driven.addAll(rule_driven);
		user_and_rule_driven.addAll(user_driven); // took user and rule driven
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
	@Test(description = "CATR_183, CATR_183 For CA | Verify All catalog tabs and exception lists for ZAHN Catalog list populates the catalogs with User, Rule Driven and excluding the Catalogs selected as Exception for CA", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
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
		user_and_rule_driven.addAll(rule_driven);
		user_and_rule_driven.addAll(user_driven); // took user and rule driven
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

	//For US - CATR_051, CATR_052, CATR_053, CATR_054, CATR_055, CATR_056, CATR_057, CATR_058, CATR_059, CATR_060 , CATR_063, CATR_067
	@PimFrameworkAnnotation(module = Modules.ZAHN_CATALOG, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.UI_Validation_Catalogs_Field)
	@Test(description = "For US - CATR_051, CATR_052, CATR_053, CATR_054, CATR_055, CATR_056, CATR_057, CATR_058, CATR_059, CATR_060 , CATR_063, CATR_067 | Zahn Catalog Verifying rule driven, userdriven, exception list, publish date, publish flag are visible and editable In US", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			"US", "pim", "SANITY","ZAHN_CATALOG" })
	public void UI_validation_Zahn_Catalog_In_US(Map<String, String> map) {

		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Zahn User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Zahn User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));


		// Validating on "Zahn catalog"
		Assertions.assertThat(catalogTypePage.isCatalogFieldVisible("tabZahnCatalog")).isTrue();

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

	//For CA - CATR_051, CATR_052, CATR_053, CATR_054, CATR_055, CATR_056, CATR_057, CATR_058, CATR_059, CATR_060 , CATR_063, CATR_067
	@PimFrameworkAnnotation(module = Modules.ZAHN_CATALOG, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.UI_Validation_Catalogs_Field)
	@Test(description = "For CA - CATR_051, CATR_052, CATR_053, CATR_054, CATR_055, CATR_056, CATR_057, CATR_058, CATR_059, CATR_060 , CATR_063, CATR_067 | Zahn Catalog Verifying rule driven, userdriven, exception list, publish date, publish flag are visible and editable In CA", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			"CA", "pim", "SANITY","ZAHN_CATALOG" })
	public void UI_validation_Zahn_Catalog_In_CA(Map<String, String> map) {
		loginpage.enterUserName(map.get("Username")).enterPassword(map.get("Password")).clickLoginButton();
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


		// Validating in master catalog for dental user for "Zahn catalog tab"

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

	// For US - WRFL_011, WRFL_012
	@PimFrameworkAnnotation(module = Modules.ZAHN_CATALOG, category = CategoryType.SMOKE)
	@TestDataSheet(sheetname = TestCaseSheet.Catalog)
	@Test(description = "For US - WRFL_011, WRFL_012 | Verify Set of Catalog Entity Rules triggered if we update the Publish date and Flag in Catalog Assignment for US for Zahn Catalog", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
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

	// For CA - WRFL_011 WRFL_012
	@PimFrameworkAnnotation(module = Modules.ZAHN_CATALOG, category = CategoryType.SMOKE)
	@TestDataSheet(sheetname = TestCaseSheet.Catalog)
	@Test(description = "For CA - WRFL_011 WRFL_012 | Verify Set of Catalog Entity Rules triggered if we update the Publish date and Flag in Catalog Assignment for CA", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
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

	// we need recently created item and make change on ItemPublish sheet
	@PimFrameworkAnnotation(module = Modules.ZAHN_CATALOG, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemPublish)
	@Test(description = "For US - ITP_031 | Verify whether the Publish date changed by the Zahn division owner gets updated for other divisions when the other divisions have not modified the Original Publish Date for US.", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
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
		catalogTypePage.selectPublishDate(DateandTimeUtils.getTodaysDate());
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
	@Test(description = "For CA - ITP_031 | Verify whether the Publish date changed by the Zahn division owner gets updated for other divisions when the other divisions have not modified the Original Publish Date for CA.", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
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
		catalogTypePage.selectPublishDate(DateandTimeUtils.getTodaysDate());
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
	@Test(description = "For US - ITP_031 | Verify whether the Publish date changed by the ZAHN Owner division should not gets updated for other divisions when the other divisions have modified the Original Publish Date for US.", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
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
		catalogTypePage.selectPublishDate(DateandTimeUtils.getTodaysDate());
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
	@Test(description = "For CA - ITP_031 | Verify whether the Publish date changed by the ZAHN Owner division should not gets updated for other divisions when the other divisions have modified the Original Publish Date for CA.", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
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
		catalogTypePage.selectPublishDate(DateandTimeUtils.getTodaysDate());
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
	@Test(description = "CATR_221 | Verify if ZAHN_FSC gets removed from user driven list if ZAHN is chosen in user driven catalog list of dental division", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
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
		catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven1"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon().clickRefreshIcon();
		List<String> user_driven = catalogTypePage.getUserDrivenCatalogs();
		Assertions.assertThat(user_driven).doesNotContain(map.get("userDriven"));
	}

	// For US - ITP_019
	// Verify Item with Publish Flag = 'Y" and Publish Date as Future Date - All
	// Divisions and check in Middleware
	@PimFrameworkAnnotation(module = Modules.ZAHN_CATALOG, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemPublish)
	@Test(description = "For US - ITP_019 | Verify whether the Publish Flag for a Zahn Item get updated as 'Y' and publish date as Future in the Middleware Export File, when the Items loaded from PIX to PIM with Publish Flag as \"Y\" for US", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
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
	@Test(description = "For CA - ITP_019 | Verify whether the Publish Flag for a Zahn Item get updated as 'Y' and Publish date as Future in the Middleware Export File, when the Items loaded from PIX to PIM with Publish Flag as \"Y\" for CA", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
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
	@Test(description = "For US - ITP_027 | Verify whether the Publish Flag for a Zahn Item get updated as \"N\" in the Middleware Export File, when the Items loaded from PIX to PIM with Publish Flag as \"N\" for US", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			"US", "pim","ZAHN_CATALOG" })
	public void verify_publish_flag_should_updated_as_NO_in_Zahn_division_for_US(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Zahn User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Zahn User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("ZahnDivision"));
		log(LogType.INFO, "Navigate to Zahn tab from Queries Menu for US Catalog");

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
				.clickOnFirstResult().selectTabfromDropdown(map.get("ZahnDivision")).clickRefreshIcon();
		log(LogType.INFO, "Navigate to Zahn division in Master Catalog");

		String masterpublishflag = catalogTypePage.getPublishFlag();
		Assertions.assertThat(masterpublishflag).isEqualTo(map.get("PublishFlag"));
		pimHomepage.clickLogoutButton();
	}

	// For CA - ITP_027
	@PimFrameworkAnnotation(module = Modules.ZAHN_CATALOG, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemPublish)
	@Test(description = "For CA - ITP_027 | Verify whether the Publish Flag for a Zahn Item get updated as \"N\" in the Middleware Export File, when the Items loaded from PIX to PIM with Publish Flag as \"N\" for CA", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			"CA", "pim","ZAHN_CATALOG" })
	public void verify_publish_flag_should_updated_as_NO_in_Zahn_division_for_CA(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("ZahnDivision"));
		log(LogType.INFO, "Navigate to Zahn tab from Queries Menu for CA Catalog");

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
				.clickOnSecondResult().selectTabfromDropdown(map.get("ZahnDivision")).clickRefreshIcon();
		log(LogType.INFO, "Navigate to Zahn division in Master Catalog");

		String masterpublishflag = catalogTypePage.getPublishFlag();
		Assertions.assertThat(masterpublishflag).isEqualTo(map.get("PublishFlag"));
		pimHomepage.clickLogoutButton();
	}

	// For US - ITP_023
	@PimFrameworkAnnotation(module = Modules.ZAHN_CATALOG, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemPublish)
	@Test(description = "For US - ITP_023 | Verify whether the Publish Flag for a Zahn Item get updated as \"Y\" and Publish date as Current in the Middleware Export File, when the Items loaded from PIX to PIM with Publish Flag as \"Y\" for US", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			"US", "pim","ZAHN_CATALOG" })
	public void verify_publish_flag_should_updated_as_Yes_in_Zahn_division_for_US(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Zahn User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Zahn User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("ZahnDivision"));
		log(LogType.INFO, "Navigate to Zahn tab from Queries Menu for US Catalog");

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
	@Test(description = "For CA - ITP_023 | Verify whether the Publish Flag for a Zahn Item get updated as \"Y\" and publish date as Current in the Middleware Export File, when the Items loaded from PIX to PIM with Publish Flag as \"Y\" for CA", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			"CA", "pim","ZAHN_CATALOG" })
	public void verify_publish_flag_should_updated_as_Yes_in_Zahn_division_for_CA(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("ZahnDivision"));
		log(LogType.INFO, "Navigate to Zahn tab from Queries Menu for US Catalog");

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
				.clickOnSecondResult().selectTabfromDropdown(map.get("ZahnDivision")).clickRefreshIcon();
		log(LogType.INFO, "Navigate to Zahn division in Master Catalog");

		String masterpublishdate = catalogTypePage.getPublishDate();
		String masterpublishFlag = catalogTypePage.getPublishFlag();

		Assertions.assertThat(masterpublishdate).isEqualTo(publishdate);
		Assertions.assertThat(masterpublishFlag).isEqualTo(publishFlag);
		pimHomepage.clickLogoutButton();
	}

}
