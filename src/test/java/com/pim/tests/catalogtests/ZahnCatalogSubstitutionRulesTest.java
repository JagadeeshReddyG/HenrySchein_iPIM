
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

public class ZahnCatalogSubstitutionRulesTest extends BaseTest {


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
	List<String> user_driven;
	List<String> all_catalog_Dental;
	List<String> all_catalog_Medical;
	List<String> all_catalog_SM;
	List<String> all_catalog_Zahn;
	Javautils javautils = new Javautils();

	//CATR_148
	private ZahnCatalogSubstitutionRulesTest(){
	}
	@PimFrameworkAnnotation(module = Modules.SubstituteCatalogRule, category = CategoryType.Data_Issue)
	@TestDataSheet(sheetname = TestCaseSheet.Catalog)
	@Test(description ="CATR_148 | Verification Of Substitution Rules In Zahn Divisions With Sub Type As S/SA/GE",groups = {"US","pim","ZAHNSubstituteCatalogRule"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)

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

		catalogTypePage.clearUserDriven(map.get("userDriven"));
		BasePage.WaitForMiliSec(3000);
		log(LogType.INFO, "Navigate to Zahn Catalog Tab to assign Zahn catalog in Parent");

		String userDrivernCatalog =map.get("userDriven");
		catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
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
		all_catalog_Zahn =allCatalogsPage.getAllZahnCatalogList();
		Assertions.assertThat(all_catalog_Zahn).doesNotContain(map.get("userDriven"));
		log(LogType.INFO, "Verifying Catalogs in Child Item");
	}
	//CATR_154
	@PimFrameworkAnnotation(module = Modules.SubstituteCatalogRule, category = CategoryType.Data_Issue)
	@TestDataSheet(sheetname = TestCaseSheet.Catalog)
	@Test(description ="CATR_154 | Verification Of Substitution Rules In Zahn Divisions With Sub Type As SP",groups = {"US","pim","ZAHNSubstituteCatalogRule"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
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

		catalogTypePage.clearUserDriven(map.get("userDriven"));
		BasePage.WaitForMiliSec(3000);
		log(LogType.INFO, "Navigate to Zahn Catalog Tab to assign Zahn catalog in Parent");

		String userDrivernCatalog =map.get("userDriven");
		catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
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
	@Test(description ="CATR_155 | Verify Substitute Of Type SP For Dental Division Item Does Not Populates With The Same Catalog List Of Parent Item", groups = {"US","pim","ZAHNSubstituteCatalogRule"} ,dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
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

		catalogTypePage.clearUserDriven(map.get("userDriven"));
		BasePage.WaitForMiliSec(3000);
		log(LogType.INFO, "Navigate to Dental Catalog Tab to assign Dental catalog in Parent");

		String userDrivernCatalog =map.get("userDriven");
		catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
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
	@Test(description = "CATR_158 | Verification Of Substitution Rules In Zahn Divisions With Sub Type As PA",groups = {"US","pim","ZAHNSubstituteCatalogRule"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
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

		catalogTypePage.clearUserDriven(map.get("userDriven"));
		BasePage.WaitForMiliSec(3000);
		log(LogType.INFO, "Navigate to Zahn Catalog Tab to assign Zahn catalog in Parent");

		String userDrivernCatalog = map.get("userDriven");
		catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
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
	@Test(description = "CATR_163 | Verification Of Substitution Rules In Zahn Divisions With Sub Type As A3",groups = {"US","pim","ZAHNSubstituteCatalogRule"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)

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

		catalogTypePage.clearUserDriven(map.get("userDriven"));
		BasePage.WaitForMiliSec(3000);
		log(LogType.INFO, "Navigate to Zahn Catalog Tab to assign Zahn catalog in Parent");

		String userDrivernCatalog = map.get("userDriven");
		catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
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

	//CATR_168
	@PimFrameworkAnnotation(module = Modules.SubstituteCatalogRule, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Catalog)
	@Test(description = "CATR_168 | Verification Of Substitution Rules In Zahn Divisions With Sub Type As A6",groups = {"US","pim","ZAHNSubstituteCatalogRule"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void VerificationOfSubstitutionRulesForDentalDivisionWithSubTypeAsA6(Map<String, String> map) {

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

		catalogTypePage.clearUserDriven(map.get("userDriven"));
		BasePage.WaitForMiliSec(3000);
		log(LogType.INFO, "Navigate to Zahn Catalog Tab to assign Zahn catalog in Parent");

		String userDrivernCatalog = map.get("userDriven");
		catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
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
}