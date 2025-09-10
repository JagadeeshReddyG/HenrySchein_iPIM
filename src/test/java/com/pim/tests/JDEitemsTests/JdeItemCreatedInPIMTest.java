package com.pim.tests.JDEitemsTests;

import com.pim.annotations.PimFrameworkAnnotation;
import com.pim.annotations.TestDataSheet;
import com.pim.enums.CategoryType;
import com.pim.enums.LogType;
import com.pim.enums.Modules;
import com.pim.enums.TestCaseSheet;
import com.pim.pages.BasePage;
import com.pim.pages.ClassificationsPage;
import com.pim.pages.GlobalAttributePage;
import com.pim.pages.LoginPage;
import com.pim.pages.MainMenu;
import com.pim.pages.PimHomepage;
import com.pim.pages.ProductDetailSearchPage;
import com.pim.pages.QualityStatusPage;
import com.pim.pages.StructureSubMenu;
import com.pim.pages.TasksSubMenu;
import com.pim.pages.TextPage;
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

public class JdeItemCreatedInPIMTest extends BaseTest {
	ProductDetailSearchPage productDetailSearchPage = new ProductDetailSearchPage();
	GlobalAttributePage globalAttributePage = new GlobalAttributePage();
	StructureSubMenu structuresubmenu = new StructureSubMenu();
	QualityStatusPage qualitystatus = new QualityStatusPage();
	ClassificationsPage classificationpage = new ClassificationsPage();
	TextPage textpage = new TextPage();
	TasksSubMenu tasksSubMenu = new TasksSubMenu();
	Javautils javautils = new Javautils();

	private JdeItemCreatedInPIMTest() {
	}

	// INT_003-Verify New item created in JDE by mass upload
	@PimFrameworkAnnotation(module = Modules.JDE_ITEMCREATION, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.JDE_ITEMCREATION_TestData)
	@Test(description = "INT_003 | Verify the item created in JDE comes to PIM", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			"US", "pim" ,"JDE_ITEMCREATION"})
	public void verifyItemCreatedInJDEcomesToPim(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Dental User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Dental User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickCatalogsMenu().selectCatalogType(map.get("CatalogType")).clickOnFirstResult()
				.applyDateFilterInSearchPageByAnyValue(map.get("ItemCreationDate"), map.get("Action"));
		structuresubmenu.selectSpecificItem(map.get("specificItemCode"));
		productDetailSearchPage.selectTabfromDropdown(map.get("GobalAttributeTab"));

		Assertions.assertThat(globalAttributePage.isAllAttributesVisible("JDE_Description")).isTrue();
		String description = globalAttributePage.getJdeDescription();
		System.out.println(description);
		Assert.assertEquals(globalAttributePage.getJdeDescription(), map.get("JDEDescription"));
	}

	// WRFL_031-Verify Tasks created once we import the item from JDE/pix with
	// Missing Local Attribute
	@PimFrameworkAnnotation(module = Modules.JDE_ITEMCREATION, category = CategoryType.SMOKE)
	@TestDataSheet(sheetname = TestCaseSheet.JDE_ITEMCREATION_TestData)
	@Test(description = "WRFL_031 | Verify Tasks created  once we import the item from JDE/pix with Missing Local Attribute", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			"SMOKE_JDE","US", "CA", "pim","JDE_ITEMCREATION" })
	public void VerifyTasksCreatedOnceImporTheItemfromJDEWithMissingLocalAttribute(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Dental User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Dental User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickCatalogsMenu().selectCatalogType(map.get("CatalogType")).clickOnFirstResult()
				.applyDateFilterInSearchPageByAnyValue(map.get("ItemCreationDate"), map.get("Action"));
		structuresubmenu.selectSpecificItem(map.get("specificItemCode"));
		String itemcode = textpage.getItemCode(map.get("specificItemCode"));
		System.out.println(itemcode);
		pimHomepage.mainMenu().clickTasksMenu().enterSearchTextToFilter(map.get("SearchText")).clickFilterButton()
				.SelectSpecificUserGroup(map.get("SpecificUser"));
		productDetailSearchPage.applyFilterInSearchPageByAnyValueinTask(map.get("TypeName"), itemcode).clickOnFirstResult();
		Assertions.assertThat(tasksSubMenu.isTaskPresent("TasksList")).isTrue();
		pimHomepage.clickLogoutButton();

	}

	// WRFL_038-Verify if primary taxonomy for an item is valid
	@PimFrameworkAnnotation(module = Modules.JDE_ITEMCREATION, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.JDE_ITEMCREATION_TestData)
	@Test(description = "WRFL_038 | Verify if primary taxonomy for an item is valid in JDE", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			"US", "CA", "pim" ,"JDE_ITEMCREATION"})
	public void VerifyifprimarytaxonomyforanitemIsvalid(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Dental User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Dental User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickCatalogsMenu().selectCatalogType(map.get("CatalogType")).clickOnFirstResult()
				.applyDateFilterInSearchPageByAnyValue(map.get("ItemCreationDate"), map.get("Action"));
		structuresubmenu.selectSpecificItem(map.get("specificItemCode"));
		productDetailSearchPage.selectTabfromDropdown(map.get("ClassificationTab"));
		qualitystatus.maximizeQualityStatusTab();
		String promayTaxonomy = classificationpage.getPrimaryTaxonomy("Primary Taxonomy");
		System.out.println(promayTaxonomy);
		qualitystatus.minimizeQualityStatusTab();
		productDetailSearchPage.selectTabfromDropdown(map.get("Qualitystatustab"));
		qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).maximizeQualityStatusTab()
				.sortRulestByLastExecution();
		List<String> qualityStatusRules = qualitystatus
				.getRuleNamesByRuleStatusInQualityStatusForItemCreation(map.get("Status"));
		for (String Rules : javautils.readMultipleValuesFromExcel(map.get("QualityStatusRules"))) {
			Assertions.assertThat(qualityStatusRules).containsAnyOf(Rules);
		}
		qualitystatus.minimizeQualityStatusTab();
		log(LogType.INFO, "Verifying purge date Status Rule");

	}

	// WRFL_039-Verify if mandatory global attribute rule for an item gets passed on
	// importing an item from JDE
	@PimFrameworkAnnotation(module = Modules.JDE_ITEMCREATION, category = CategoryType.Exceptionally_Passed)
	@TestDataSheet(sheetname = TestCaseSheet.JDE_ITEMCREATION_TestData)
	@Test(description = "WRFL_039 | Verify if mandatory global attribute rule for an item gets passed on importing an item from JDE", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			"US", "pim","JDE_ITEMCREATION" })
	public void VerifyIfMandatoryGlobalAttributeRuleforAnItemGetsPassedOnImportingAnItemfromJDE(
			Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Dental User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Dental User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickCatalogsMenu().selectCatalogType(map.get("CatalogType")).clickOnFirstResult()
				.applyDateFilterInSearchPageByAnyValue(map.get("ItemCreationDate"), map.get("Action"));
		structuresubmenu.selectSpecificItem(map.get("specificItemCode"));
		productDetailSearchPage.selectTabfromDropdown(map.get("Qualitystatustab"));
		qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).maximizeQualityStatusTab()
				.sortRulestByLastExecution();
		List<String> qualityStatusRules = qualitystatus
				.getRuleNamesByRuleStatusInQualityStatusForItemCreation(map.get("Status"));
		for (String Rules : javautils.readMultipleValuesFromExcel(map.get("QualityStatusRules"))) {
			Assertions.assertThat(qualityStatusRules).containsAnyOf(Rules);
		}
		qualitystatus.minimizeQualityStatusTab();
		log(LogType.INFO, "Verifying global attribute rules");

	}

	// WRFL_040-Verify if catalog entity rule gets triggered on importing an item
	// from JDE
	@PimFrameworkAnnotation(module = Modules.JDE_ITEMCREATION, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.JDE_ITEMCREATION_TestData)
	@Test(description = "WRFL_040 | Verify if catalog entity rule gets triggered on importing an item from JDE", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			"US", "pim","JDE_ITEMCREATION" })
	public void VerifyIfCatalogEntityRuleGetsTriggeredOnImportingAnItemfromJDE(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Dental User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Dental User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickCatalogsMenu().selectCatalogType(map.get("CatalogType")).clickOnFirstResult()
				.applyDateFilterInSearchPageByAnyValue(map.get("ItemCreationDate"), map.get("Action"));
		structuresubmenu.selectSpecificItem(map.get("specificItemCode"));
		productDetailSearchPage.selectTabfromDropdown(map.get("Qualitystatustab"));
		qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).maximizeQualityStatusTab()
				.sortRulestByLastExecution();
		List<String> qualityStatusRules = qualitystatus
				.getRuleNamesByRuleStatusInQualityStatusForItemCreation(map.get("Status"));
		for (String Rules : javautils.readMultipleValuesFromExcel(map.get("QualityStatusRules"))) {
			Assertions.assertThat(qualityStatusRules).containsAnyOf(Rules);
		}
		qualitystatus.minimizeQualityStatusTab();
		log(LogType.INFO, "Verifying catalog entity rules");

	}

	// WRFL_041-Verify if attributes rules are ran on importing an item from JDE
	@PimFrameworkAnnotation(module = Modules.JDE_ITEMCREATION, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.JDE_ITEMCREATION_TestData)
	@Test(description = "WRFL_041 | Verify if attributes rules are ran on importing an item from JDE", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			"US", "pim","JDE_ITEMCREATION" })
	public void VerifyIfAttributesRulesAreRanOnImportingAnItemFromJDE(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Dental User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Dental User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickCatalogsMenu().selectCatalogType(map.get("CatalogType")).clickOnFirstResult()
				.applyDateFilterInSearchPageByAnyValue(map.get("ItemCreationDate"), map.get("Action"));
		structuresubmenu.selectSpecificItem(map.get("specificItemCode"));
		productDetailSearchPage.selectTabfromDropdown(map.get("Qualitystatustab"));
		qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).maximizeQualityStatusTab()
				.sortRulestByLastExecution();
		List<String> qualityStatusRules = qualitystatus
				.getRuleNamesByRuleStatusInQualityStatusForItemCreation(map.get("Status"));
		for (String Rules : javautils.readMultipleValuesFromExcel(map.get("QualityStatusRules"))) {
			Assertions.assertThat(qualityStatusRules).containsAnyOf(Rules);
		}
		qualitystatus.minimizeQualityStatusTab();
		log(LogType.INFO, "Verifying attributes rules");

	}

	// WRFL_042-Verify if mandatory local attribute rule gets run as part of item
	// import

	@PimFrameworkAnnotation(module = Modules.JDE_ITEMCREATION, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.JDE_ITEMCREATION_TestData)
	@Test(description = "WRFL_042 | Verify if mandatory local attribute rule gets run as part of item import JDE", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			"US", "pim","JDE_ITEMCREATION" })
	public void VerifyIfMandatorylocalAttributeRuleGetsRunAsPartOfItemImport(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Dental User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Dental User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickCatalogsMenu().selectCatalogType(map.get("CatalogType")).clickOnFirstResult()
				.applyDateFilterInSearchPageByAnyValue(map.get("ItemCreationDate"), map.get("Action"));
		structuresubmenu.selectSpecificItem(map.get("specificItemCode"));
		productDetailSearchPage.selectTabfromDropdown(map.get("Qualitystatustab"));
		qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).maximizeQualityStatusTab()
				.sortRulestByLastExecution();
		List<String> qualityStatusRules = qualitystatus
				.getRuleNamesByRuleStatusInQualityStatusForItemCreation(map.get("Status"));
		for (String Rules : javautils.readMultipleValuesFromExcel(map.get("QualityStatusRules"))) {
			Assertions.assertThat(qualityStatusRules).containsAnyOf(Rules);
		}
		qualitystatus.minimizeQualityStatusTab();
		log(LogType.INFO, "Verifying mandatory local attributes rules");

	}

	// WRFL_043-Verify if Lov local attribute rule gets run as part of item import
	@PimFrameworkAnnotation(module = Modules.JDE_ITEMCREATION, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.JDE_ITEMCREATION_TestData)
	@Test(description = " WRFL_043 | Verify if Lov local attribute rule gets run as part of item import in JDE", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			"US", "pim","JDE_ITEMCREATION" })
	public void VerifyifLovlocalAttributeRuleGetsRunAsPartOfItemImport(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Dental User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Dental User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickCatalogsMenu().selectCatalogType(map.get("CatalogType")).clickOnFirstResult()
				.applyDateFilterInSearchPageByAnyValue(map.get("ItemCreationDate"), map.get("Action"));
		structuresubmenu.selectSpecificItem(map.get("specificItemCode"));
		productDetailSearchPage.selectTabfromDropdown(map.get("Qualitystatustab"));
		qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).maximizeQualityStatusTab()
				.sortRulestByLastExecution();
		List<String> qualityStatusRules = qualitystatus
				.getRuleNamesByRuleStatusInQualityStatusForItemCreation(map.get("Status"));
		for (String Rules : javautils.readMultipleValuesFromExcel(map.get("QualityStatusRules"))) {
			Assertions.assertThat(qualityStatusRules).containsAnyOf(Rules);
		}
		qualitystatus.minimizeQualityStatusTab();
		log(LogType.INFO, "Verifying Lov local rules");

	}

	// WRFL_044-Verify if description rule gets run as part of item import
	// WRFL_043-Verify if description length validation rule gets run as part of
	// item import
	@PimFrameworkAnnotation(module = Modules.JDE_ITEMCREATION, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.JDE_ITEMCREATION_TestData)
	@Test(description = "WRFL_044 , WRFL_043 | Verify if description rule gets run as part of item import JDE", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			"US", "pim" ,"JDE_ITEMCREATION"})
	public void VerifyIfDescriptionRuleGetsRunAsPartOfItemImport(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Dental User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Dental User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickCatalogsMenu().selectCatalogType(map.get("CatalogType")).clickOnFirstResult()
				.applyDateFilterInSearchPageByAnyValue(map.get("ItemCreationDate"), map.get("Action"));
		structuresubmenu.selectSpecificItem(map.get("specificItemCode"));
		productDetailSearchPage.selectTabfromDropdown(map.get("Qualitystatustab"));
		qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).maximizeQualityStatusTab()
				.sortRulestByLastExecution();
		List<String> qualityStatusRules = qualitystatus
				.getRuleNamesByRuleStatusInQualityStatusForItemCreation(map.get("Status"));
		for (String Rules : javautils.readMultipleValuesFromExcel(map.get("QualityStatusRules"))) {
			Assertions.assertThat(qualityStatusRules).containsAnyOf(Rules);
		}
		qualitystatus.minimizeQualityStatusTab();
		log(LogType.INFO, "Verifying description rule and description length rule");

	}

	// WRFL_043-Verify set export to ecom rule gets run as part of item creation
	@PimFrameworkAnnotation(module = Modules.JDE_ITEMCREATION, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.JDE_ITEMCREATION_TestData)
	@Test(description = "WRFL_043 | Verify set export to ecom rule gets run as part of item creation in JDE", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			"US", "pim","JDE_ITEMCREATION" })
	public void VerifySetExportToEcomRuleGetsRunAsPartOfItemCreation(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Dental User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Dental User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickCatalogsMenu().selectCatalogType(map.get("CatalogType")).clickOnFirstResult()
				.applyDateFilterInSearchPageByAnyValue(map.get("ItemCreationDate"), map.get("Action"));
		structuresubmenu.selectSpecificItem(map.get("specificItemCode"));
		productDetailSearchPage.selectTabfromDropdown(map.get("Qualitystatustab"));
		qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).maximizeQualityStatusTab()
				.sortRulestByLastExecution();
		List<String> qualityStatusRules = qualitystatus
				.getRuleNamesByRuleStatusInQualityStatusForItemCreation(map.get("Status"));
		for (String Rules : javautils.readMultipleValuesFromExcel(map.get("QualityStatusRules"))) {
			Assertions.assertThat(qualityStatusRules).containsAnyOf(Rules);
		}
		qualitystatus.minimizeQualityStatusTab();
		log(LogType.INFO, "Verifying ecom rules");

	}

	// WRFL_002-Verify the Import Workflow for an item created in JDE
	@PimFrameworkAnnotation(module = Modules.JDE_ITEMCREATION, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.JDE_ITEMCREATION_TestData)
	@Test(description = "WRFL_002 | Verify the Import Workflow for an item created in JDE", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			"US", "pim","JDE_ITEMCREATION" })
	public void VerifyTheImportWorkflowforAnItemCreatedInJDE(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Dental User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Dental User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickCatalogsMenu().selectCatalogType(map.get("CatalogType")).clickOnFirstResult()
				.applyDateFilterInSearchPageByAnyValue(map.get("ItemCreationDate"), map.get("Action"));
		structuresubmenu.selectSpecificItem(map.get("specificItemCode"));
		productDetailSearchPage.selectTabfromDropdown(map.get("Qualitystatustab"));
		qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).maximizeQualityStatusTab()
				.sortRulestByLastExecution();
		List<String> qualityStatusRules = qualitystatus
				.getRuleNamesByRuleStatusInQualityStatusForItemCreation(map.get("Status"));
		for (String Rules : javautils.readMultipleValuesFromExcel(map.get("QualityStatusRules"))) {
			Assertions.assertThat(qualityStatusRules).containsAnyOf(Rules);
		}
		qualitystatus.minimizeQualityStatusTab();
		log(LogType.INFO, "Verifying Data Quality rules");
	}

}
