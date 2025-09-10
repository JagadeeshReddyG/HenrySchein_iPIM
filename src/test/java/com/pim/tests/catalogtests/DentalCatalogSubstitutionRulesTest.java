package com.pim.tests.catalogtests;

import static com.pim.reports.FrameworkLogger.log;
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
import com.pim.utils.ExcelUtils;
import com.pim.utils.Javautils;

public class DentalCatalogSubstitutionRulesTest extends BaseTest {


	LocalAttributePage localAttributePage = new LocalAttributePage();
	StructureSubMenu structuresubmenu=new StructureSubMenu();
	ProductDetailSearchPage productDetailSearchPage = new ProductDetailSearchPage();                     
	MainMenu mainmenu=new MainMenu();
	QueriesSubMenu queriesSubMenu=new QueriesSubMenu();
	BasePage basePage = new BasePage();
	ReferencesPage referencesPage=new  ReferencesPage();
	CatalogTypePage catalogTypePage = new CatalogTypePage();
	AllCatalogsPage allCatalogsPage=new AllCatalogsPage();
	QualityStatusPage qualitystatus = new QualityStatusPage();
	Javautils javautils = new Javautils();


	//CATR_146--Done
	private DentalCatalogSubstitutionRulesTest(){
	}
	@PimFrameworkAnnotation(module = Modules.SubstituteCatalogRule, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Catalog)
	@Test(description ="CATR_146 | Verification Of Substitution Rules In Dental Divisions With Sub Type As S/SA/GE", groups = {"US","pim","DentalSubstituteCatalogRule"} ,dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)

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

		catalogTypePage.clearUserDriven(map.get("userDriven"));
		BasePage.WaitForMiliSec(3000);
		String userDrivernCatalog =map.get("userDriven");
		catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		List<String> user_driven = catalogTypePage.getUserDrivenCatalogs();
		Assertions.assertThat(user_driven).containsAnyOf(userDrivernCatalog);
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
	@Test(description ="CATR_150 | Verify Substitute Of Type SA For Any Division Item Does Not Populates With The Same Catalog List Of Parent Item", groups = {"US","pim","DentalSubstituteCatalogRule"} ,dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
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

		catalogTypePage.clearUserDriven(map.get("userDriven"));
		BasePage.WaitForMiliSec(3000);
		String userDrivernCatalog =map.get("userDriven");
		catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
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
	@Test(description = "CATR_153 | Verification Of Substitution Rules In Dental Divisions With Sub Type As SP CATR_153", groups = {"US","pim","DentalSubstituteCatalogRule"} , dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
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

		catalogTypePage.clearUserDriven(map.get("userDriven"));
		BasePage.WaitForMiliSec(3000);
		String userDrivernCatalog =map.get("userDriven");
		catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		List<String> user_driven = catalogTypePage.getUserDrivenCatalogs();
		Assertions.assertThat(user_driven).containsAnyOf(userDrivernCatalog);

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
	@Test(description = "CATR_156 | Verification Of Substitution Rules In Dental Divisions With Sub Type As PA", groups = {"US","pim","DentalSubstituteCatalogRule"} , dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
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

		catalogTypePage.clearUserDriven(map.get("userDriven"));
		BasePage.WaitForMiliSec(3000);
		String userDrivernCatalog = map.get("userDriven");
		catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		List<String> user_driven = catalogTypePage.getUserDrivenCatalogs();
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
	@Test(description ="CATR_162 | Verification Of Substitution Rules In Dental Divisions With Sub Type As A3", groups = {"US","pim","DentalSubstituteCatalogRule"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
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

		catalogTypePage.clearUserDriven(map.get("userDriven"));
		BasePage.WaitForMiliSec(3000);
		String userDrivernCatalog = map.get("userDriven");
		catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		List<String> user_driven = catalogTypePage.getUserDrivenCatalogs();
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
	@Test(description = "CATR_166 | Verification Of Substitution Rules In Dental Divisions With Sub Type As A6", groups = {"US","pim","DentalSubstituteCatalogRule"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
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

		catalogTypePage.clearUserDriven(map.get("userDriven"));
		BasePage.WaitForMiliSec(3000);
		log(LogType.INFO, "Navigate to Dental Catalog Tab to assign Dental catalog in Parent");

		String userDrivernCatalog = map.get("userDriven");
		catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
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
		log(LogType.INFO, "Verifying Dental Catalog in Master");

		// Verifying in Child item
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType"))
		.removeAndEnterHsiItemNumber(referenceObjectNumber).clickSeachButton().clickOnFirstResult()
		.selectTabfromDropdown(map.get("AllCatalogtab"));
		List<String> all_catalog_Dental = allCatalogsPage.getAllDentalCatalogList();
		Assert.assertNotEquals(all_catalog_Dental, userDrivernCatalog);
		log(LogType.INFO, "Verifying Catalog in Child Item should assign");
	}
}