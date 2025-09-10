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
import com.pim.pages.ClassificationsPage;
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

public class SpecialMarketCatalogTest extends BaseTest {
	private SpecialMarketCatalogTest() {
	}

	CatalogTypePage catalogTypePage = new CatalogTypePage();
	AllCatalogsPage allCatalogsPage = new AllCatalogsPage();
	QualityStatusPage qualitystatus = new QualityStatusPage();
	ProductDetailSearchPage productdetailspage = new ProductDetailSearchPage();
	LoginPage loginpage = new LoginPage();
	QueriesSubMenu queriesSubMenu = new QueriesSubMenu();
	PimHomepage pimHomepage = new PimHomepage();
	Javautils javautils = new Javautils();
	//List<String> rule_driven;
	//List<String> user_driven;
	//List<String> all_special_markets_catalog;
	//List<String> user_and_rule_driven = new ArrayList<>();

	// WRFL_016
	@PimFrameworkAnnotation(module = Modules.SPECIAL_MARKET_CATALOG, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Catalog)
	@Test(description = "WRFL_016 | Verifying Rule Driven catalog for Special Market Division in US, CA and Master Catalog", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
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
	@Test(description = "CATR_185, CATR_186 and CATR_135 | Verify All catalog tabs and exception lists for Special markets Catalog list populates the catalogs with User, Rule Driven and excluding the Catalogs selected as Exception ", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
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
	@Test(description = "CATR_035,CATR_036,CATR_037,CATR_038,CATR_039,CATR_040,CATR_041,CATR_042,CATR_043,CATR_044,CATR_047,CATR_050 | Special market Verifying rule driven, userdriven, exception list, publish date, publish flag are visible and editable In US", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
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
	@Test(description = "WRFL_017 and WRFL_018 | Verify Set of Catalog Entity Rules triggered if we update the Publish date and Flag in Catalog Assignment for US", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			"US", "pim","SPECIAL_MARKET_CATALOG" })
	public void verify_quality_status_when_change_publish_date_and_flag_for_US_special_market_division(
			Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Special Markets User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Special Markets User").get("Password")).clickLoginButton();
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

	// ITP_032
	// we need recently created item and make change on ItemPublish sheet
	@PimFrameworkAnnotation(module = Modules.SPECIAL_MARKET_CATALOG, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemPublish)
	@Test(description = "ITP_032 | Verify whether the Publish date changed by the Special Markets division owner gets updated for other divisions when the other divisions have not modified the Original Publish Date.", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
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
		catalogTypePage.selectPublishDate(DateandTimeUtils.getTodaysDate());
		productdetailspage.clickRefreshIcon();
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
	@Test(description = "ITP_037 | Verify whether the Publish date changed by the Special Markets Owner division should not gets updated for other divisions when the other divisions have modified the Original Publish Date.", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
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
		catalogTypePage.selectPublishDate(DateandTimeUtils.getTodaysDate());
		productdetailspage.clickRefreshIcon();
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
	@Test(description = "ITP_020 | Verify whether the Publish Flag for a Special Markets Item get updated as 'Y' and publish date as Future in the Middleware Export File, when the Items loaded from PIX to PIM with Publish Flag as \"Y\" ", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
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
	@Test(description = "ITP_028 | Verify whether the Publish Flag for a Special Markets Item get updated as \"N\" in the Middleware Export File, when the Items loaded from PIX to PIM with Publish Flag as \"N\" ", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
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
				.clickOnFirstResult().selectTabfromDropdown(map.get("SpecialMarketsDivision")).clickRefreshIcon();
		log(LogType.INFO, "Navigate to Zahn division in Master Catalog");

		String masterpublishflag = catalogTypePage.getPublishFlag();
		Assertions.assertThat(masterpublishflag).isEqualTo(map.get("PublishFlag"));
		pimHomepage.clickLogoutButton();
	}

	// ITP_024
	@PimFrameworkAnnotation(module = Modules.SPECIAL_MARKET_CATALOG, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemPublish)
	@Test(description = "ITP_024 | Verify whether the Publish Flag for a Special Markets Item get updated as \"Y\" and publish date as Current in the Middleware Export File, when the Items loaded from PIX to PIM with Publish Flag as \"Y\"", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			"US", "pim","SPECIAL_MARKET_CATALOG" })
	public void verify_publish_flag_should_updated_as_Yes_in_Special_Markets_division_for_US(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Special Markets User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Special Markets User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("SpecialMarketsDivision"));
		log(LogType.INFO, "Navigate to Special Markets tab from Queries Menu for US Catalog");

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
				.clickOnFirstResult().selectTabfromDropdown(map.get("SpecialMarketsDivision")).clickRefreshIcon();
		log(LogType.INFO, "Navigate to Special Markets division in Master Catalog");

		String masterpublishdate = catalogTypePage.getPublishDate();
		String masterpublishFlag = catalogTypePage.getPublishFlag();

		Assertions.assertThat(masterpublishdate).isEqualTo(publishdate);
		Assertions.assertThat(masterpublishFlag).isEqualTo(publishFlag);
		pimHomepage.clickLogoutButton();
	}

}
