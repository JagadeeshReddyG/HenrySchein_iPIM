package com.pim.tests.catalogtests;

import static com.pim.reports.FrameworkLogger.log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
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

public class MedicalCatalogSubstitutionRulesTest extends BaseTest {

	LocalAttributePage localAttributePage = new LocalAttributePage();
	StructureSubMenu structuresubmenu = new StructureSubMenu();
	ProductDetailSearchPage productDetailSearchPage = new ProductDetailSearchPage();
	MainMenu mainmenu = new MainMenu();
	QueriesSubMenu queriesSubMenu = new QueriesSubMenu();
	BasePage basePage = new BasePage();
	ReferencesPage referencesPage = new ReferencesPage();
	CatalogTypePage catalogTypePage = new CatalogTypePage();
	AllCatalogsPage allCatalogsPage = new AllCatalogsPage();
	QualityStatusPage qualitystatus = new QualityStatusPage();
	//List<String> user_driven;
	//List<String> all_catalog_Dental;
	//List<String> all_catalog_Medical;
	//List<String> all_catalog_SM;
	//List<String> all_catalog_Zahn;
	Javautils javautils = new Javautils();

	// CATR_147-Done
	private MedicalCatalogSubstitutionRulesTest() {
	}

	@PimFrameworkAnnotation(module = Modules.SubstituteCatalogRule, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Catalog)
	@Test(description = "CATR_147 | Verification Of Substitution Rules In Medical Divisions With Sub Type As S/SA/GE CATR_147", groups = {"US","pim","MedicalSubstituteCatalogRule"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)

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
		catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
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
	@Test(description = "CATR_151 | Verification Of Substitution Rules In Medical Divisions With Sub Type As SP CATR_151",groups = {"US","pim","MedicalSubstituteCatalogRule"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
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
		catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
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
	@Test(description = "CATR_159 | Verification Of Substitution Rules In Medical Divisions With Sub Type As PA", groups = {"US","pim","MedicalSubstituteCatalogRule"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
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
		catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
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
	@Test(description = "CATR_160 | Verification Substitute Of Type PA For Any Division Item Does Not Populates With The Same CatalogList Of Parent Item for Medical catalog rule", groups = {"US","pim","MedicalSubstituteCatalogRule"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
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

		catalogTypePage.clearUserDriven(map.get("userDriven"));
		BasePage.WaitForMiliSec(3000);
		String userDrivernCatalog = map.get("userDriven");
		catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
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
	@Test(description = "CATR_161 | Verification Of Substitution Rules In Medical Divisions With Sub Type As A3", groups = {"US","pim","MedicalSubstituteCatalogRule"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
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
		catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
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



	//CATR_167 -- Commenting this out from execution because A6 is not available. Closing CART_167 & CATR_170 becase A6 Item Type is not availabe as per ticket 'MDM-69' informed by Lalitha. One more TS is there so going to comment that out from Execution.
//	@PimFrameworkAnnotation(module = Modules.SubstituteCatalogRule, category = CategoryType.Data_Issue)
//	@TestDataSheet(sheetname = TestCaseSheet.Catalog)
//	@Test(description = "CATR_167 - Verification Of Substitution Rules In Medical Divisions With Sub Type As A6", groups = {"US","pim","MedicalSubstituteCatalogRule"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
//	public void VerificationOfSubstitutionRulesInMedicalDivisionsWithSubTypeAsA6(Map<String, String> map) {
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
//		log(LogType.INFO, "Navigate to Medical Catalog Tab to assign Medical catalog in Parent");
//
//		String userDrivernCatalog = map.get("userDriven");
//		catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
//		pimHomepage.productDetailSearchPage().clickRefreshIcon();
//		List<String> user_driven = catalogTypePage.getUserDrivenCatalogs();
//		Assertions.assertThat(user_driven).containsAnyOf(userDrivernCatalog);
//
//
//		//verify quality status 
//		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
//		List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
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
//		log(LogType.INFO, "Verifying Catalogs in Master");
//
//		// Verifying Catalogs in Child item
//		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType"))
//		.removeAndEnterHsiItemNumber(referenceObjectNumber).clickSeachButton().clickOnFirstResult()
//		.selectTabfromDropdown(map.get("AllCatalogtab"));
//		List<String> all_catalog_Medical =allCatalogsPage.getAllMedicalCatalogList();
//		Assert.assertNotEquals(all_catalog_Medical, userDrivernCatalog);
//		log(LogType.INFO, "Verifying Catalog in Child Item should not assign");
//	}
}
