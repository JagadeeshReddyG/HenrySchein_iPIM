package com.pim.tests.Ui_FunctionalityTests;

import com.pim.annotations.PimFrameworkAnnotation;
import com.pim.annotations.TestDataSheet;
import com.pim.enums.CategoryType;
import com.pim.enums.LogType;
import com.pim.enums.Modules;
import com.pim.enums.TestCaseSheet;
import com.pim.pages.BasePage;
import com.pim.pages.CatalogTypePage;
import com.pim.pages.ClassificationsPage;
import com.pim.pages.GlobalAttributePage;
import com.pim.pages.LoginPage;
import com.pim.pages.PimHomepage;
import com.pim.pages.ProductDetailSearchPage;
import com.pim.pages.ReferencesPage;
import com.pim.pages.StructureSubMenu;
import com.pim.tests.BaseTest;
import com.pim.utils.DataProviderUtils;
import com.pim.utils.DateandTimeUtils;
import com.pim.utils.ExcelUtils;
import com.pim.utils.Javautils;

import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import static com.pim.reports.FrameworkLogger.log;

import java.util.Map;

public class UI_CreateAndDeleteReferencesTest extends BaseTest {
	ReferencesPage RefPage = new ReferencesPage();
	ClassificationsPage classificationspage = new ClassificationsPage();
	GlobalAttributePage globalattribute = new GlobalAttributePage();
	ProductDetailSearchPage productdetailpage = new ProductDetailSearchPage();
	BasePage basepage = new BasePage();
	CatalogTypePage catalogTypePage = new CatalogTypePage();

	private UI_CreateAndDeleteReferencesTest() {
	}

	// UI_033 and UI-034
	@PimFrameworkAnnotation(module = Modules.Entities_UI_Func, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Reference)
	@Test(description = "UI_033 and UI_034 | Verify Reference Item Creation and deletion", groups = { "CA",
			"pim" }, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void CreateReferenceItem(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemTypes"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));

		// Delete existing Reference item
		classificationspage.clickOnExpandButton();
		RefPage.deleteReferenceItem(map.get("ReferenceObjectItem"));

		// Add new Reference Item
		RefPage.AddReference(map.get("ReferenceTypeValue")).clickOnClickHereButton();
		pimHomepage.mainMenu().clickStructuresMenu().selectCatalogType(map.get("CatalogType"))
				.selectClassificationType(map.get("CategoryType")).selectItemType(map.get("ItemType"))
				.clickOnSelectedTaxnomyType(map.get("CategoryType"))
				.clickOnfirstSubsetDropdown(map.get("SubsetDropDown")).clickOnShowAll();
		pimHomepage.productDetailSearchPage().applyFilterInSearchPage(map.get("ReferenceObjectItem"))
				.clickOnFirstResult();
		RefPage.clickokAndCloseWindowButton();
		BasePage.switchTab(0);
		RefPage.clickReferenceOKButton();
	}

	public void verifyDeletedReferenceItemIsNotVisible() {
		Assertions.assertThat(RefPage.isReferenceTypeVisible("referenceType")).isFalse();
	}

	// having issue, newly created and modified data is not reflecting in DB
	// TC_ID - MDLW_019
	@PimFrameworkAnnotation(module = Modules.MiddleWare, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Reference)
	@Test(description = "MDLW_019 | Verify for dental division  Substitution item created in respective table when the items are mapped in PIM ", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			"US", "pim" })
	public void verify_reference_type_item_grouping_in_database_for_dental_division(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Dental User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Dental User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemTypes"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
		log(LogType.INFO, "Navigate to Reference Tab");

		// Delete existing Reference item
		classificationspage.clickOnExpandButton();
		RefPage.deleteReferenceItem(map.get("ReferenceObjectItem"));

		// Add new Reference Item
		RefPage.AddReference(map.get("ReferenceTypeValue")).enterReferenceObjectNumber(map.get("ReferenceObjectItem"));
		RefPage.clickReferenceOKButton();
		BasePage.WaitForMiliSec(10000);
		classificationspage.clickOnCloseExpandButton();

		// Navigating dental Catalog Tab and Adding UserDriven Field
		productdetailpage.clickRefreshIcon().selectTabfromDropdown(map.get("CatalogTab"));
		catalogTypePage.verifySelectUserdrivenDropdown();

		// verify in master
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
				.clickOnFirstResult().clickRefreshIcon().selectTabfromDropdown(map.get("TabName"));

		classificationspage.clickOnExpandButton();
		String reference_number = RefPage.getReferenceType();
		Assertions.assertThat(reference_number).isEqualTo(map.get("ReferenceTypeValue"));
		pimHomepage.clickLogoutButton();
	}

	// having issue, newly created and modified data is not reflecting in DB
	// TC_ID - MDLW_019
	@PimFrameworkAnnotation(module = Modules.MiddleWare, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Reference)
	@Test(description = "MDLW_019 - For Modified Ref. Type | Verify for dental division  Substitution item updated in respective table when the items are mapped in PIM", priority = 0, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			"US", "pim" })
	public void verify_modified_reference_type_item_grouping_in_database_for_dental_division(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Dental User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Dental User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemTypes"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
		log(LogType.INFO, "Navigate to Reference Tab");

		// edit existing one
		classificationspage.clickOnExpandButton();
		BasePage.WaitForMiliSec(5000);
		RefPage.clickReferenceFirstRow().clickeditReference().editReferenceBeginDate(DateandTimeUtils.getTodaysDate())
				.clickReferenceOKButton();
		BasePage.WaitForMiliSec(5000);
		classificationspage.clickOnCloseExpandButton();
		// Navigating dental Catalog Tab and Adding UserDriven Field
				productdetailpage.clickRefreshIcon().selectTabfromDropdown(map.get("CatalogTab"));
				catalogTypePage.verifySelectUserdrivenDropdown();
		// verify in master
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
		classificationspage.clickOnExpandButton();
		String date = RefPage.getReferenceBeginDate();
		String[] begindate = Javautils.splitbasedonspace(date);
		Assertions.assertThat(begindate[0]).isEqualTo(DateandTimeUtils.getTodaysDateForQualityStatus());
		pimHomepage.clickLogoutButton();
	}

	// having issue, newly created and modified data is not reflecting in DB
	// TC_ID - MDLW_020
	@PimFrameworkAnnotation(module = Modules.MiddleWare, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Reference)
	@Test(description = "MDLW_020 | Verify for medical division  Substitution item created in respective table when the items are mapped in PIM ", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			"US", "pim" })
	public void verify_reference_type_item_grouping_in_database_for_medical_division(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemTypes"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
		log(LogType.INFO, "Navigate to Reference Tab");

		// Delete existing Reference item
		classificationspage.clickOnExpandButton();
		RefPage.deleteReferenceItem(map.get("ReferenceObjectItem"));

		// Add new Reference Item
		RefPage.AddReference(map.get("ReferenceTypeValue")).enterReferenceObjectNumber(map.get("ReferenceObjectItem"));
		RefPage.clickReferenceOKButton();
		BasePage.WaitForMiliSec(10000);
		classificationspage.clickOnCloseExpandButton();

		// Navigating dental Catalog Tab and Adding UserDriven Field
		productdetailpage.clickRefreshIcon().selectTabfromDropdown(map.get("CatalogTab"));
		catalogTypePage.verifySelectUserdrivenDropdown();

		// verify in master
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
				.clickOnFirstResult().clickRefreshIcon().selectTabfromDropdown(map.get("TabName"));

		classificationspage.clickOnExpandButton();
		String reference_number = RefPage.getReferenceType();
		Assertions.assertThat(reference_number).isEqualTo(map.get("ReferenceTypeValue"));
		pimHomepage.clickLogoutButton();
	}

	// having issue, newly created and modified data is not reflecting in DB
	// TC_ID - MDLW_020
	@PimFrameworkAnnotation(module = Modules.MiddleWare, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Reference)
	@Test(description = "MDLW_020 - For Modified Ref. Type | Verify for medical division  Substitution item updated in respective table when the items are mapped in PIM", priority = 0, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			"US", "pim" })
	public void verify_modified_reference_type_item_grouping_in_database_for_medical_division(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemTypes"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
		log(LogType.INFO, "Navigate to Reference Tab");

		// edit existing one
		classificationspage.clickOnExpandButton();
		BasePage.WaitForMiliSec(5000);
		RefPage.clickReferenceFirstRow().clickeditReference().editReferenceBeginDate(DateandTimeUtils.getTodaysDate())
				.clickReferenceOKButton();
		BasePage.WaitForMiliSec(5000);
		classificationspage.clickOnCloseExpandButton();
		// Navigating dental Catalog Tab and Adding UserDriven Field
				productdetailpage.clickRefreshIcon().selectTabfromDropdown(map.get("CatalogTab"));
				catalogTypePage.verifySelectUserdrivenDropdown();
		// verify in master
		//BasePage.WaitForMiliSec(120000);
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
		classificationspage.clickOnExpandButton();
		String date = RefPage.getReferenceBeginDate();
		String[] begindate = Javautils.splitbasedonspace(date);
		Assertions.assertThat(begindate[0]).isEqualTo(DateandTimeUtils.getTodaysDateForQualityStatus());
		pimHomepage.clickLogoutButton();
	}

	// having issue, newly created and modified data is not reflecting in DB
	// TC_ID - MDLW_021
	@PimFrameworkAnnotation(module = Modules.MiddleWare, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Reference)
	@Test(description = "MDLW_021 | Verify for zahn division  Substitution item created in respective table when the items are mapped in PIM  Pre-Requsite ", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			"US", "pim" })
	public void verify_reference_type_item_grouping_in_database_for_zahn_division(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Zahn User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Zahn User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemTypes"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
		log(LogType.INFO, "Navigate to Reference Tab");

		// Delete existing Reference item
		classificationspage.clickOnExpandButton();
		RefPage.deleteReferenceItem(map.get("ReferenceObjectItem"));

		// Add new Reference Item
		RefPage.AddReference(map.get("ReferenceTypeValue")).enterReferenceObjectNumber(map.get("ReferenceObjectItem"));
		RefPage.clickReferenceOKButton();
		BasePage.WaitForMiliSec(10000);
		classificationspage.clickOnCloseExpandButton();

		// Navigating dental Catalog Tab and Adding UserDriven Field
		productdetailpage.clickRefreshIcon().selectTabfromDropdown(map.get("CatalogTab"));
		catalogTypePage.verifySelectUserdrivenDropdown();

		// verify in master
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
				.clickOnFirstResult().clickRefreshIcon().selectTabfromDropdown(map.get("TabName"));

		classificationspage.clickOnExpandButton();
		String reference_number = RefPage.getReferenceType();
		Assertions.assertThat(reference_number).isEqualTo(map.get("ReferenceTypeValue"));
		pimHomepage.clickLogoutButton();
	}

	// having issue, newly created and modified data is not reflecting in DB
	// TC_ID - MDLW_021
	@PimFrameworkAnnotation(module = Modules.MiddleWare, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Reference)
	@Test(description = "MDLW_021 - For Modified Ref. Type | Verify for zahn division  Substitution item updated in respective table when the items are mapped in PIM", priority = 0, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			"US", "pim" })
	public void verify_modified_reference_type_item_grouping_in_database_for_zahn_division(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Zahn User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Zahn User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemTypes"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
		log(LogType.INFO, "Navigate to Reference Tab");

		// edit existing one
		classificationspage.clickOnExpandButton();
		BasePage.WaitForMiliSec(5000);
		RefPage.clickReferenceFirstRow().clickeditReference().editReferenceBeginDate(DateandTimeUtils.getTodaysDate())
				.clickReferenceOKButton();
		BasePage.WaitForMiliSec(5000);
		classificationspage.clickOnCloseExpandButton();
		// Navigating dental Catalog Tab and Adding UserDriven Field
				productdetailpage.clickRefreshIcon().selectTabfromDropdown(map.get("CatalogTab"));
				catalogTypePage.verifySelectUserdrivenDropdown();
		// verify in master
		//BasePage.WaitForMiliSec(120000);
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
		classificationspage.clickOnExpandButton();
		String date = RefPage.getReferenceBeginDate();
		String[] begindate = Javautils.splitbasedonspace(date);
		Assertions.assertThat(begindate[0]).isEqualTo(DateandTimeUtils.getTodaysDateForQualityStatus());
		pimHomepage.clickLogoutButton();
	}

	// having issue, newly created and modified data is not reflecting in DB
	// TC_ID - MDLW_022
	@PimFrameworkAnnotation(module = Modules.MiddleWare, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Reference)
	@Test(description = "MDLW_022 | Verify for special markets division  Substitution item created in respective table when the items are mapped in PIM ", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			"US", "pim" })
	public void verify_reference_type_item_grouping_in_database_for_special_market_division(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Special Markets User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Special Markets User").get("Password"))
				.clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemTypes"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
		log(LogType.INFO, "Navigate to Reference Tab");

		// Delete existing Reference item
		classificationspage.clickOnExpandButton();
		RefPage.deleteReferenceItem(map.get("ReferenceObjectItem"));

		// Add new Reference Item
		RefPage.AddReference(map.get("ReferenceTypeValue")).enterReferenceObjectNumber(map.get("ReferenceObjectItem"));
		RefPage.clickReferenceOKButton();
		BasePage.WaitForMiliSec(5000);
		classificationspage.clickOnCloseExpandButton();

		// Navigating dental Catalog Tab and Adding UserDriven Field
		productdetailpage.clickRefreshIcon().selectTabfromDropdown(map.get("CatalogTab"));
		catalogTypePage.verifySelectUserdrivenDropdown();
		// Navigating dental Catalog Tab and Adding UserDriven Field
				productdetailpage.clickRefreshIcon().selectTabfromDropdown(map.get("CatalogTab"));
				catalogTypePage.verifySelectUserdrivenDropdown();
		// verify in master
		//BasePage.WaitForMiliSec(120000);
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();

		classificationspage.clickOnExpandButton();
		String reference_number = RefPage.getReferenceType();
		Assertions.assertThat(reference_number).isEqualTo(map.get("ReferenceTypeValue"));
		pimHomepage.clickLogoutButton();
	}

	// having issue, newly created and modified data is not reflecting in DB
	// TC_ID - MDLW_022
	@PimFrameworkAnnotation(module = Modules.MiddleWare, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Reference)
	@Test(description = "MDLW_022 - For Modified Ref. Type | Verify for special markets division  Substitution item updated in respective table when the items are mapped in PIM", priority = 0, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			"US", "pim" })
	public void verify_modified_reference_type_item_grouping_in_database_for_special_market_division(
			Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Special Markets User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Special Markets User").get("Password"))
				.clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemTypes"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
		log(LogType.INFO, "Navigate to Reference Tab");

		// edit existing one
		classificationspage.clickOnExpandButton();
		BasePage.WaitForMiliSec(5000);
		RefPage.clickReferenceFirstRow().clickeditReference().editReferenceBeginDate(DateandTimeUtils.getTodaysDate())
				.clickReferenceOKButton();
		BasePage.WaitForMiliSec(5000);
		classificationspage.clickOnCloseExpandButton();
		// Navigating dental Catalog Tab and Adding UserDriven Field
				productdetailpage.clickRefreshIcon().selectTabfromDropdown(map.get("CatalogTab"));
				catalogTypePage.verifySelectUserdrivenDropdown();
		// verify in master
		//BasePage.WaitForMiliSec(120000);
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
		classificationspage.clickOnExpandButton();
		String date = RefPage.getReferenceBeginDate();
		String[] begindate = Javautils.splitbasedonspace(date);
		Assertions.assertThat(begindate[0]).isEqualTo(DateandTimeUtils.getTodaysDateForQualityStatus());
		pimHomepage.clickLogoutButton();
	}
	
	@PimFrameworkAnnotation(module = Modules.MiddleWare, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Reference)
	//@Test(description = "MDLW_022 | Verify for special markets division  Substitution item updated in respective table when the items are mapped in PIM", priority = 0, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"US", "pim" })
	public void Demo(
			Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Special Markets User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Special Markets User").get("Password"))
				.clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemTypes"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
		log(LogType.INFO, "Navigate to Reference Tab");

		
	}
	
	

}
