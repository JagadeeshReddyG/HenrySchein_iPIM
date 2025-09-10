package com.pim.tests.catalogtests;

import static com.pim.reports.FrameworkLogger.log;

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

public class SpecialMarketsCatalogSubstitutionRulesTest extends BaseTest {


	LocalAttributePage localAttributePage = new LocalAttributePage();
	StructureSubMenu structuresubmenu=new StructureSubMenu();
	ProductDetailSearchPage productDetailSearchPage = new ProductDetailSearchPage();                     
	MainMenu mainmenu=new MainMenu();
	QueriesSubMenu queriesSubMenu=new QueriesSubMenu();
	BasePage basePage = new BasePage();;
	ReferencesPage referencesPage=new  ReferencesPage();
	CatalogTypePage catalogTypePage = new CatalogTypePage();
	AllCatalogsPage allCatalogsPage=new AllCatalogsPage();
	QualityStatusPage qualitystatus = new QualityStatusPage();
	Javautils javautils = new Javautils();
	//List<String> user_driven;
	//List<String> all_catalog_Dental;
	//List<String> all_catalog_Medical;
	//List<String> all_catalog_SM;
	//List<String> all_catalog_Zahn;

	//CATR_149--Done
	private SpecialMarketsCatalogSubstitutionRulesTest(){
	}
	@PimFrameworkAnnotation(module = Modules.SubstituteCatalogRule, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Catalog)
	@Test(description ="CATR_149 | Verification Of Substitution Rules In SpecialMarkets Divisions With Sub Type As S/SA/GE",groups = {"US","pim","SMSubstituteCatalogRule"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)

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

		catalogTypePage.clearUserDriven(map.get("userDriven"));
		BasePage.WaitForMiliSec(3000);

		String userDrivernCatalog =map.get("userDriven");
		catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
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
	@Test(description = "CATR_152 | Verification Of Substitution Rules In SpecialMarkets Divisions With Sub Type As SP CATR_152",groups = {"US","pim","SMSubstituteCatalogRule"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void VerificationOfSubstitutionRulesForSpecialMarketDivisionWithSubTypeAsSP(Map<String, String> map) {

		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Special Markets User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Special Markets User").get("Password")).clickLoginButton();

		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
		.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
		.clickOnFirstResult().selectTabfromDropdown(map.get("ReferenceTab"));
		String referenceType = referencesPage.getReferenceType();
		//List<String> referenceType = referencesPage.getReferenceTypeInList();
		Assertions.assertThat(referenceType).isEqualTo(map.get("ReferenceType"));
		//Assertions.assertThat(referenceType).containsAnyOf((map.get("ReferenceType")));
		String referenceObjectNumber = referencesPage.getReferenceObjectNumber();
		productDetailSearchPage.selectTabfromDropdown(map.get("TabName"));

		catalogTypePage.clearUserDriven(map.get("userDriven"));
		BasePage.WaitForMiliSec(3000);
		String userDrivernCatalog = map.get("userDriven");
		catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
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
	@Test(description = "CATR_158 | Verification Of Substitution Rules In SpecialMarkets Divisions With Sub Type As PA", groups = {"US","pim","SMSubstituteCatalogRule"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
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

		catalogTypePage.clearUserDriven(map.get("userDriven"));
		BasePage.WaitForMiliSec(3000);
		String userDrivernCatalog = map.get("userDriven");
		catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
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
	@Test(description = "CATR_164 | Verification Of Substitution Rules In Special Market Divisions With Sub Type As A3",groups = {"US","pim","SMSubstituteCatalogRule"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
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

		catalogTypePage.clearUserDriven(map.get("userDriven"));
		BasePage.WaitForMiliSec(3000);
		String userDrivernCatalog = map.get("userDriven");
		catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
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
	//CATR_165--NotWorking
	//Verify whether the Catalog list of the parent item does not gets assigned to the child Items in the respective tabs (Medical Web Catalog Assignment), based on the Auto Sub Flag value is "N" in the Catalog_Master dictionary
	@PimFrameworkAnnotation(module = Modules.SubstituteCatalogRule, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Catalog)

	@Test(description ="CATR_165 | Verification Substitute Of Type PA For Any Division Item Does Not Populates With The Same Catalog List Of Parent Item for special Market Catalog", groups = {"US","pim","SMSubstituteCatalogRule"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
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
		log(LogType.INFO, "Navigate to Dental Catalog Tab to assign Dental catalog in Parent");
		
		catalogTypePage.clearUserDriven(map.get("userDriven"));
		BasePage.WaitForMiliSec(3000);

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
		log(LogType.INFO, "Verifying Catalogs in Master");

		// Verifying in Child item
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType"))
		.removeAndEnterHsiItemNumber(referenceObjectNumber).clickSeachButton().clickOnFirstResult()
		.selectTabfromDropdown(map.get("AllCatalogtab"));
		List<String> all_catalog_Medical = allCatalogsPage.getAllMedicalCatalogList();
		Assert.assertNotEquals(all_catalog_Medical, userDrivernCatalog);
		log(LogType.INFO, "Verifying Catalog in Child Item should not get assign");
	}
	//CATR_169--Data Issue
	@PimFrameworkAnnotation(module = Modules.SubstituteCatalogRule, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Catalog)
	@Test(description = "CATR_169 | Verification Of Substitution Rules In Special Market Divisions With Sub Type As A6",groups = {"US","pim","SMSubstituteCatalogRule"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
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

		catalogTypePage.clearUserDriven(map.get("userDriven"));
		BasePage.WaitForMiliSec(3000);
		log(LogType.INFO, "Navigate to Special Market Catalog Tab to assign Special Market catalog in Parent");

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
		log(LogType.INFO, "Verifying SM Catalog in Master");

		// Verifying in Child item
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType"))
		.removeAndEnterHsiItemNumber(referenceObjectNumber).clickSeachButton().clickOnFirstResult()
		.selectTabfromDropdown(map.get("AllCatalogtab"));
		List<String> all_catalog_SM = allCatalogsPage.getAllSpecialMarketsCatalogList();
		Assert.assertNotEquals(all_catalog_SM, userDrivernCatalog);
		log(LogType.INFO, "Verifying Catalog in Child Item should not get assign");
	}

	//CATR_170 -- Commenting this out from execution because A6 is not available. Closing CART_167 & CATR_170 becase A6 Item Type is not availabe as per ticket 'MDM-69' informed by Lalitha. One more TS is there so going to comment that out from Execution.
	//Verify whether the Catalog list of the parent item does not gets assigned to the child Items in the respective tabs (Dental Web Catalog Assignment), based on the Auto Sub Flag value is "N" in the Catalog_Master dictionary

//	@PimFrameworkAnnotation(module = Modules.SubstituteCatalogRule, category = CategoryType.Data_Issue)
//	@TestDataSheet(sheetname = TestCaseSheet.Catalog)
//	@Test(description ="CATR_170 | Verification Substitute Of Type A6 For Any Division Item Does Not Populates With The Same Catalog List Of Parent Item", groups = {"US","pim","SMSubstituteCatalogRule"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
//
//	public void VerifySubstituteOfTypeA6ForAnyDivisionItemDoesNotPopulatesWithTheSameCatalogListOfParentItem(Map<String, String> map) {
//
//		PimHomepage pimHomepage = new LoginPage()
//				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
//				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
//
//		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
//		.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
//		.clickOnFirstResult().selectTabfromDropdown(map.get("ReferenceTab"));
//		String referenceType = referencesPage.getReferenceType();
//		Assertions.assertThat(referenceType).isEqualTo(map.get("ReferenceType"));
//		String referenceObjectNumber = referencesPage.getReferenceObjectNumber();
//		productDetailSearchPage.selectTabfromDropdown(map.get("TabName"));
//
//		catalogTypePage.clearUserDriven(map.get("userDriven"));
//		BasePage.WaitForMiliSec(3000);
//		log(LogType.INFO, "Navigate to Dental Catalog Tab to assign Dental catalog in Parent");
//
//		String userDrivernCatalog = map.get("userDriven");
//		catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
//		pimHomepage.productDetailSearchPage().clickRefreshIcon();
//		List<String> user_driven = catalogTypePage.getUserDrivenCatalogs();
//		Assertions.assertThat(user_driven).containsAnyOf(userDrivernCatalog);
//
//		//verify quality status 
//		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
//		List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus"))
//				.maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
//		for (String Rules:javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule")))
//		{
//			Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);	
//		}
//		qualitystatus.minimizeQualityStatusTab();
//		log(LogType.INFO,"Verifying catalog entity rule");
//
//		// Verify in master catalog
//		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
//		.clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
//		List<String> masterUserdriven = catalogTypePage.getUserDrivenCatalogs();
//		Assertions.assertThat(user_driven).containsAll(masterUserdriven);
//		log(LogType.INFO, "Verifying Dental Catalog in Master");
//
//		// Verifying in Child item
//		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType"))
//		.removeAndEnterHsiItemNumber(referenceObjectNumber).clickSeachButton().clickOnFirstResult()
//		.selectTabfromDropdown(map.get("AllCatalogtab"));
//		List<String> all_catalog_Dental = allCatalogsPage.getAllDentalCatalogList();
//		Assert.assertNotEquals(all_catalog_Dental, userDrivernCatalog);
//		log(LogType.INFO, "Verifying Catalog in Child Item should not get assign");
//	}
}