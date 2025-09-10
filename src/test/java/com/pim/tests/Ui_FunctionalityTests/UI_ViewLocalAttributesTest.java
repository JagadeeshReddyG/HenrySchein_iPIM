package com.pim.tests.Ui_FunctionalityTests;

import static com.pim.reports.FrameworkLogger.log;

import java.awt.AWTException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.assertj.core.api.Assertions;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.model.Log;
import com.pim.annotations.PimFrameworkAnnotation;
import com.pim.annotations.TestDataSheet;
import com.pim.enums.CategoryType;
import com.pim.enums.LogType;
import com.pim.enums.Modules;
import com.pim.enums.TestCaseSheet;
import com.pim.pages.ActionsPage;
import com.pim.pages.BasePage;
import com.pim.pages.ClassificationsPage;
import com.pim.pages.GlobalAttributePage;
import com.pim.pages.LocalAttributePage;
import com.pim.pages.LoginPage;
import com.pim.pages.MainMenu;
import com.pim.pages.PimHomepage;
import com.pim.pages.ProductDetailSearchPage;
import com.pim.pages.QualityStatusPage;
import com.pim.pages.QueriesSubMenu;
import com.pim.pages.StructureSubMenu;
import com.pim.pages.WebDescription;
import com.pim.tests.BaseTest;
import com.pim.utils.DataProviderUtils;
import com.pim.utils.ExcelUtils;
import com.pim.utils.Javautils;

public class UI_ViewLocalAttributesTest extends BaseTest {                 
	MainMenu mainmenu=new MainMenu();
	BasePage basePage = new BasePage();
	StructureSubMenu structuresubmenu=new StructureSubMenu();
	ProductDetailSearchPage productDetailSearchPage = new ProductDetailSearchPage(); 
	GlobalAttributePage globalAttributePage =new GlobalAttributePage();
	WebDescription webDescription=new WebDescription();
	LocalAttributePage localAttributePage =new LocalAttributePage();
	private UI_ViewLocalAttributesTest(){
	}
	//UI_001--Done
	@PimFrameworkAnnotation(module = Modules.Entities_UI_Func, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Entities_UI_Func_TestData)
	@Test(description ="UI_001 | Verify the System Is Able To Search For Products Using Exact Search By Item No For Read Only User - UI_001",groups = {"US","pim","Entities_UI_Func"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void VerifyTheSystemIsAbleToSearchForProductsUsingExactSearchByItemNoForReadOnlyUser(Map<String, String> map)   
	{
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Dental User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Dental User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickSearchMenu().selectCodeType(map.get("CodeType")).enterSearchText(map.get("ItemNumber")).clickSearchButton();
		structuresubmenu.clickOnFirstItemCode();
		productDetailSearchPage.selectTabfromDropdown(map.get("GlobalAttributeTab"));
		Assertions.assertThat(globalAttributePage.isGlobalAttributeFieldEditable("HSI_Item_Number", "HSI_Item_NumberDropDown")).isFalse();
	}
	//UI_002--Done
	@PimFrameworkAnnotation(module = Modules.Entities_UI_Func, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Entities_UI_Func_TestData)
	@Test(description ="UI_002 | Verify the system is able to  search for Products using exact search by JDE Description for Read Only User - UI_002",groups = {"US","pim","Entities_UI_Func"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void VerifyTheSystemIsAbleToSearchForProductsUsingExactSearchByJDEDescriptionForReadOnlyUser(Map<String, String> map)   
	{
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Dental User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Dental User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickSearchMenu().selectCodeType(map.get("CodeType")).enterSearchText(map.get("JDEDescription")).clickSearchButton();
		structuresubmenu.clickOnFirstItemCode();
		productDetailSearchPage.selectTabfromDropdown(map.get("WebDescriptionTab"));
		Assertions.assertThat(globalAttributePage.isGlobalAttributeFieldEditable("fddTextfield", "fddTextfieldDropDown")).isFalse();
	}
	//UI_004--Done
	@PimFrameworkAnnotation(module = Modules.Entities_UI_Func, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Entities_UI_Func_TestData)
	@Test(description ="UI_004 | Verify the system is able to  search for Products using exact search by Supplier Code for Read Only User",groups = {"US","pim","Entities_UI_Func"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void VerifyTheSystemIsAbleToSearchForProductsUsingExactSearchBySupplierCodeForReadOnlyUser(Map<String, String> map)   
	{
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Dental User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Dental User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickSearchMenu().selectCodeType(map.get("CodeType")).enterSearchText(map.get("SupplierCode")).clickSearchButton();
		structuresubmenu.clickOnFirstItemCode();
		productDetailSearchPage.selectTabfromDropdown(map.get("GlobalAttributeTab"));
		Assertions.assertThat(globalAttributePage.isGlobalAttributeFieldEditable("Supplier_Code", "Supplier_CodeDropDown")).isFalse();
	}

	//UI_005--Done
	@PimFrameworkAnnotation(module = Modules.Entities_UI_Func, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Entities_UI_Func_TestData)
	@Test(description ="UI_005 | Verify the system is able to  search for Products using exact search by Item no  for Admin User",groups = {"CA","pim","Entities_UI_Func"},dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void VerifyTheSystemIsAbleToSearchForProductsUsingExactSearchByItemNoForAdminUser(Map<String, String> map)   
	{
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickSearchMenu().selectCodeType(map.get("CodeType")).enterSearchText(map.get("ItemNumber")).clickSearchButton();
		structuresubmenu.clickOnFirstItemCode();
		productDetailSearchPage.selectTabfromDropdown(map.get("GlobalAttributeTab"));
		String ActualHSIItemNumber = globalAttributePage.getHSI_Item_Number();
		String ExpectedHSIItemNumber= map.get("ItemNumber");
		Assertions.assertThat(ActualHSIItemNumber).isEqualTo(ExpectedHSIItemNumber);
	}


	//UI_006--Done
	@PimFrameworkAnnotation(module = Modules.Entities_UI_Func, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Entities_UI_Func_TestData)
	@Test(description ="UI_006 | Verify the system is able to  search for Products using exact search by JDE Description for Admin User UI_006",groups = {"CA","pim","Entities_UI_Func"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void VerifyTheSystemIsAbleToSearchForProductsUsingExactSearchByJDEDescriptionforAdminUser(Map<String, String> map)   
	{
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickSearchMenu().selectCodeType(map.get("CodeType")).enterSearchText(map.get("JDEDescription")).clickSearchButton();
		structuresubmenu.clickOnFirstItemCode();
		productDetailSearchPage.selectTabfromDropdown(map.get("WebDescriptionTab"));
		String ActualJDEDescription = globalAttributePage.getJdeDescription();
		String ExpectedJDEDescription= map.get("JDEDescription");
		Assertions.assertThat(ActualJDEDescription).contains(ExpectedJDEDescription);

	}
	//UI_008--Done
	@PimFrameworkAnnotation(module = Modules.Entities_UI_Func, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Entities_UI_Func_TestData)
	@Test(description ="UI_008 | Verify the system is able to  search for Products using exact search by Supplier Code for Admin User",groups = {"US","pim","Entities_UI_Func"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void VerifyTheSystemIsAbleToSearchForProductsUsingExactSearchBySupplierCodeForAdminUser(Map<String, String> map)   
	{
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Dental User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Dental User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickSearchMenu().selectCodeType(map.get("CodeType")).enterSearchText(map.get("SupplierCode")).clickSearchButton();
		structuresubmenu.clickOnFirstItemCode();
		productDetailSearchPage.selectTabfromDropdown(map.get("GlobalAttributeTab"));
		String ActualSupplierCode = globalAttributePage.getSuppliercode();
		String ExpectedSupplierCode= map.get("SupplierCode");
		Assertions.assertThat(ActualSupplierCode).isEqualTo(ExpectedSupplierCode);

	}
	//UI_009-Done
	@PimFrameworkAnnotation(module = Modules.Entities_UI_Func, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Entities_UI_Func_TestData)
	@Test(description ="UI_009 | Verify the System Is Able To Search For Products Using Fuzzy Search By Item No For Read Only User",groups = {"US","pim","Entities_UI_Func"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void VerifyTheSystemIsAbleToSearchForProductsUsingFuzzySearchByItemNoForReadOnlyUser(Map<String, String> map)   
	{
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Dental User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Dental User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickSearchMenu().selectCodeType(map.get("CodeType")).enterSearchText(map.get("ItemNumber")).clickSearchButton();
		Assertions.assertThat(productDetailSearchPage.isFirstResultFieldNotVisible("firstItemCode")).isFalse();
	}

	//UI_010-Done
	@PimFrameworkAnnotation(module = Modules.Entities_UI_Func, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Entities_UI_Func_TestData)
	@Test(description ="UI_010 | Verify the system is able to  search for Products using Fuzzy search by JDE Description for Read Only User - UI_010",groups = {"US","pim","Entities_UI_Func"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void VerifyTheSystemIsAbleToSearchForProductsUsingFuzzySearchByJDEDescriptionForReadOnlyUser(Map<String, String> map)   
	{
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Dental User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Dental User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickSearchMenu().selectCodeType(map.get("CodeType")).enterSearchText(map.get("JDEDescription")).clickSearchButton();
		structuresubmenu.clickOnFirstItemCode();
		productDetailSearchPage.selectTabfromDropdown(map.get("WebDescriptionTab"));
		String ActualJDEDescription = webDescription.getSearchDescription();
		String ExpectedJDEDescription= map.get("JDEDescription");
		Assertions.assertThat(ActualJDEDescription ).contains(ExpectedJDEDescription);

	}
	//UI_012-Done
	@PimFrameworkAnnotation(module = Modules.Entities_UI_Func, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Entities_UI_Func_TestData)
	@Test(description ="UI_012 | Verify the system is able to  search for Products using Fuzzy search by Supplier Code for Read Only User",groups = {"US","pim","Entities_UI_Func"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void VerifyTheSystemIsAbleToSearchForProductsUsingFuzzySearchBySupplierCodeForReadOnlyUser(Map<String, String> map)   
	{
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Dental User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Dental User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickSearchMenu().selectCodeType(map.get("CodeType")).enterSearchText(map.get("SupplierCode")).clickSearchButton();
		Assertions.assertThat(productDetailSearchPage.isFirstResultFieldNotVisible("firstItemCode")).isFalse();
	}
	//UI_013-Done
	@PimFrameworkAnnotation(module = Modules.Entities_UI_Func, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Entities_UI_Func_TestData)
	@Test(description ="UI_013 | Verify the system is able to  search for Products using Fuzzy search by Item no  for Admin User",groups = {"US","pim","Entities_UI_Func"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void VerifyTheSystemIsAbleToSearchForProductsUsingFuzzySearchByItemNoForAdminUser(Map<String, String> map)   
	{
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickSearchMenu().selectCodeType(map.get("CodeType")).enterSearchText(map.get("ItemNumber")).clickSearchButton();
		Assertions.assertThat(productDetailSearchPage.isFirstResultFieldNotVisible("firstItemCode")).isFalse();
	}


	//UI_014-Done
	@PimFrameworkAnnotation(module = Modules.Entities_UI_Func, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Entities_UI_Func_TestData)
	@Test(description ="UI_014 | Verify the system is able to  search for Products using Fuzzy search by JDE Description for Admin User",groups = {"CA","pim","Entities_UI_Func"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void VerifyTheSystemIsAbleToSearchForProductsUsingFuzzySearchByJDEDescriptionforAdminUser(Map<String, String> map)   
	{
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickSearchMenu().selectCodeType(map.get("CodeType")).enterSearchText(map.get("JDEDescription")).clickSearchButton();
		Assertions.assertThat(productDetailSearchPage.isFirstResultFieldNotVisible("firstItemCode")).isFalse();

	}
	//UI_016--Done
	@PimFrameworkAnnotation(module = Modules.Entities_UI_Func, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Entities_UI_Func_TestData)
	@Test(description ="UI_016 | Verify the system is able to  search for Products using Fuzzy search by Supplier Code for Admin User",groups = {"US","pim","Entities_UI_Func"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void VerifyTheSystemIsAbleToSearchForProductsUsingFuzzySearchBySupplierCodeForAdminUser(Map<String, String> map)   
	{
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Dental User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Dental User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickSearchMenu().selectCodeType(map.get("CodeType")).enterSearchText(map.get("SupplierCode")).clickSearchButton();
		Assertions.assertThat(productDetailSearchPage.isFirstResultFieldNotVisible("firstItemCode")).isFalse();

	}
	//UI_025-Done
	@PimFrameworkAnnotation(module = Modules.Entities_UI_Func, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Entities_UI_Func_TestData)
	@Test(description ="UI_025 | Verify the Read only user is able to view all local product attributes of Dental steward product class",groups={"US","pim","Entities_UI_Func"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void VerifyTheReadOnlyUserIsAbleToViewAllLocalProductAttributesOfDentalStewardProductClass(Map<String, String> map)   
	{
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Dental User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Dental User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickStructuresMenu().selectCatalogType(map.get("CatalogType"))
		.selectClassificationType(map.get("CategoryType")).selectItemType(map.get("ItemType"))
		.clickOnSelectedTaxnomyType(map.get("CategoryType")).clickOnfirstSubsetDropdown(map.get("SubsetDropDown"))
		.clickOnShowAll().clickOnFirstItemCode();    
		productDetailSearchPage.selectTabfromDropdown(map.get("LocalAttributeTab"));
		VerifyLocalAttributesIsVisibleForDentalUser();
	}
	public void VerifyLocalAttributesIsVisibleForDentalUser() {
		Assertions.assertThat(localAttributePage.isAllAttributesVisible("itemField")).isTrue();
		Assertions.assertThat(localAttributePage.isAllAttributesVisible("quantityInputField")).isTrue();
		Assertions.assertThat(localAttributePage.isAllAttributesVisible("shankType")).isTrue();
	}

	//UI_026-Done
	@PimFrameworkAnnotation(module = Modules.Entities_UI_Func, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Entities_UI_Func_TestData)
	@Test(description ="UI_026 | Verify the Read only user is able to view all local product attributes of Medical steward product class",groups = {"US","pim","Entities_UI_Func"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void VerifyTheReadOnlyUserIsAbleToViewAllLocalProductAttributesOfMedicalStewardProductClass(Map<String, String> map)   
	{
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickStructuresMenu().selectCatalogType(map.get("CatalogType"))
		.selectClassificationType(map.get("CategoryType")).selectItemType(map.get("ItemType"))
		.clickOnSelectedTaxnomyType(map.get("CategoryType")).clickOnfirstSubsetDropdown(map.get("SubsetDropDown"))
		.clickOnShowAll();
		productDetailSearchPage.applyFilterInSearchPage(map.get("ItemNumber")).clickOnFirstResult();
		productDetailSearchPage.selectTabfromDropdown(map.get("LocalAttributeTab"));
		VerifyLocalAttributesIsVisibleForMedicalUser();
	}

	public void VerifyLocalAttributesIsVisibleForMedicalUser() {

		Assertions.assertThat(localAttributePage.isAllAttributesVisible("capsuleSize")).isTrue();
		Assertions.assertThat(localAttributePage.isAllAttributesVisible("itemTypeField")).isTrue();
		Assertions.assertThat(localAttributePage.isAllAttributesVisible("itemField")).isTrue();
		Assertions.assertThat(localAttributePage.isAllAttributesVisible("quantityInputField")).isTrue();
		Assertions.assertThat(localAttributePage.isAllAttributesVisible("setType")).isTrue();
	}

	//UI_027-Done
	@PimFrameworkAnnotation(module = Modules.Entities_UI_Func, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Entities_UI_Func_TestData)
	@Test(description ="UI_027 | Verify the Read only user is able to view all local product attributes of Zahn steward product class",groups = {"US","pim","Entities_UI_Func"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void VerifyTheReadOnlyUserIsAbleToViewAllLocalProductAttributesOfZahnStewardProductClass(Map<String, String> map)   
	{
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Zahn User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Zahn User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickStructuresMenu().selectCatalogType(map.get("CatalogType"))
		.selectClassificationType(map.get("CategoryType")).selectItemType(map.get("ItemType"))
		.clickOnSelectedTaxnomyType(map.get("CategoryType")).clickOnfirstSubsetDropdown(map.get("SubsetDropDown"))
		.clickOnShowAll().clickOnFirstItemCode();    
		productDetailSearchPage.selectTabfromDropdown(map.get("LocalAttributeTab"));
		VerifyLocalAttributesIsVisibleForZahnUser();
	}	

	public void VerifyLocalAttributesIsVisibleForZahnUser() {
		Assertions.assertThat(localAttributePage.isAllAttributesVisible("itemField")).isTrue();
		Assertions.assertThat(localAttributePage.isAllAttributesVisible("quantityInputField")).isTrue();
		Assertions.assertThat(localAttributePage.isAllAttributesVisible("shankType")).isTrue();
	}
	//UI_029--Done
	@PimFrameworkAnnotation(module = Modules.Entities_UI_Func, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Entities_UI_Func_TestData)
	@Test(description ="UI_029 | Verify the Admin user is able to view all local product attributes of Dental steward product class",groups = {"US","pim","Entities_UI_Func"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void VerifyTheAdminUserIsAbleToViewAllLocalProductAttributesOfDentalStewardProductClass(Map<String, String> map)   
	{
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Dental User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Dental User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickStructuresMenu().selectCatalogType(map.get("CatalogType"))
		.selectClassificationType(map.get("CategoryType")).selectItemType(map.get("ItemType"))
		.clickOnSelectedTaxnomyType(map.get("CategoryType")).clickOnfirstSubsetDropdown(map.get("SubsetDropDown"))
		.clickOnShowAll().clickOnFirstItemCode();    
		productDetailSearchPage.selectTabfromDropdown(map.get("LocalAttributeTab"));
		VerifyLocalAttributesIsVisibleForDentalByAdminUser();
	}
	
	public void VerifyLocalAttributesIsVisibleForDentalByAdminUser() {
		Assertions.assertThat(localAttributePage.isAllAttributesVisible("itemField")).isTrue();
		Assertions.assertThat(localAttributePage.isAllAttributesVisible("quantityInputField")).isTrue();
		Assertions.assertThat(localAttributePage.isAllAttributesVisible("shankType")).isTrue();
	}
	
	//UI_030--Done
	@PimFrameworkAnnotation(module = Modules.Entities_UI_Func, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Entities_UI_Func_TestData)
	@Test(description ="UI_030 | Verify the Admin user is able to view all local product attributes of Medical steward product class",groups = {"US","pim","Entities_UI_Func"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void VerifyTheAdminUserIsAbleToViewAllLocalProductAttributesOfMedicalStewardProductClass(Map<String, String> map)   
	{
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Dental User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Dental User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickStructuresMenu().selectCatalogType(map.get("CatalogType"))
		.selectClassificationType(map.get("CategoryType")).selectItemType(map.get("ItemType"))
		.clickOnSelectedTaxnomyType(map.get("CategoryType")).clickOnfirstSubsetDropdown(map.get("SubsetDropDown"))
		.clickOnShowAll();
		productDetailSearchPage.applyFilterInSearchPage(map.get("ItemNumber")).clickOnFirstResult();
		productDetailSearchPage.selectTabfromDropdown(map.get("LocalAttributeTab"));
		VerifyLocalAttributesIsVisibleForMedicalByAdminUser();
		}
	
	public void VerifyLocalAttributesIsVisibleForMedicalByAdminUser() {
		Assertions.assertThat(localAttributePage.isAllAttributesVisible("itemTypeField")).isTrue();
		Assertions.assertThat(localAttributePage.isAllAttributesVisible("itemField")).isTrue();
		Assertions.assertThat(localAttributePage.isAllAttributesVisible("quantityInputField")).isTrue();
	}
	//UI_031--Done
	@PimFrameworkAnnotation(module = Modules.Entities_UI_Func, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Entities_UI_Func_TestData)
	@Test(description ="UI_031 | Verify the Admin user is able to view all local product attributes of Zahn steward product class",groups = {"US","pim","Entities_UI_Func"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void VerifyTheAdminUserIsAbleToViewAllLocalProductAttributesOfZahnStewardProductClass(Map<String, String> map)   
	{
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Dental User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Dental User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickStructuresMenu().selectCatalogType(map.get("CatalogType"))
		.selectClassificationType(map.get("CategoryType")).selectItemType(map.get("ItemType"))
		.clickOnSelectedTaxnomyType(map.get("CategoryType")).clickOnfirstSubsetDropdown(map.get("SubsetDropDown"))
		.clickOnShowAll().clickOnFirstItemCode();    
		productDetailSearchPage.selectTabfromDropdown(map.get("LocalAttributeTab"));
		VerifyLocalAttributesIsVisibleForZahnByAdminUser();
	}
	public void VerifyLocalAttributesIsVisibleForZahnByAdminUser() {
		Assertions.assertThat(localAttributePage.isAllAttributesVisible("itemField")).isTrue();
		Assertions.assertThat(localAttributePage.isAllAttributesVisible("quantityInputField")).isTrue();
		Assertions.assertThat(localAttributePage.isAllAttributesVisible("shankType")).isTrue();
	}


}	

