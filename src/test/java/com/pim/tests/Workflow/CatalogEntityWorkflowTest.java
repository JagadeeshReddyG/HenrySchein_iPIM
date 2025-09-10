package com.pim.tests.Workflow;

import com.pim.annotations.PimFrameworkAnnotation;
import com.pim.annotations.TestDataSheet;
import com.pim.enums.CategoryType;
import com.pim.enums.LogType;
import com.pim.enums.Modules;
import com.pim.enums.TestCaseSheet;
import com.pim.pages.BasePage;
import com.pim.pages.CatalogTypePage;
import com.pim.pages.LoginPage;
import com.pim.pages.PimHomepage;
import com.pim.pages.ProductDetailSearchPage;
import com.pim.pages.QualityStatusPage;
import com.pim.pages.QueriesSubMenu;
import com.pim.pages.ReferencesPage;
import com.pim.tests.BaseTest;
import com.pim.utils.DataProviderUtils;
import com.pim.utils.DateandTimeUtils;
import com.pim.utils.ExcelUtils;
import com.pim.utils.Javautils;

import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.pim.reports.FrameworkLogger.log;

public class CatalogEntityWorkflowTest extends BaseTest {
	ReferencesPage referencesPage = new ReferencesPage();
	ProductDetailSearchPage productdetail = new ProductDetailSearchPage();
	CatalogTypePage catalogTypePage = new CatalogTypePage();
	QueriesSubMenu queriessubmenu = new QueriesSubMenu();
	QualityStatusPage qualitystatus = new QualityStatusPage();
	List dental_user_driven;
	List medical_user_driven;
	List zahn_user_driven;
	Javautils javautils = new Javautils();
	ProductDetailSearchPage productdetailspage = new ProductDetailSearchPage();


	//WRFL_009_US
	@PimFrameworkAnnotation(module = Modules.DENTAL_CATALOG, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.CatalogEntityRule)
	@Test(description = "WRFL_009_US | Verify substitution exception rule for child get triggered if we update the dental catalog in parent for US", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"US","pim","DENTAL_CATALOG"})
	public void verify_quality_status_when_dental_catalog_is_updated_in_parent_US_dental_division(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Dental User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Dental User").get("Password")).clickLoginButton();

		//verifying in parent item in US catalog
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("ReferenceTab"));
		qualitystatus.maximizeQualityStatusTab();
		String referenceObjectNumber = referencesPage.getReferenceObjectNumber();
		qualitystatus.minimizeQualityStatusTab();

		productdetail.selectTabfromDropdown(map.get("TabName"));
		log(LogType.INFO, "Navigate to Dental catalog Tab to assign dental in userdriven in Parent");

		catalogTypePage.clearUserDriven(map.get("userDriven"));
		BasePage.WaitForMiliSec(3000);

		//assigning dental catalog
		catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		dental_user_driven = catalogTypePage.getUserDrivenCatalogs();
		Assertions.assertThat(dental_user_driven).containsAnyOf(map.get("userDriven"));

		//verify in master
		productdetail.clickMenuRefreshIcon();
		queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
		log(LogType.INFO, "Navigate to Dental catalog tab to verify the userdriven in Master Catalog");
		List<String> masterUserdriven = catalogTypePage.getUserDrivenCatalogs();
		Assertions.assertThat(masterUserdriven).containsAnyOf(map.get("userDriven"));

		//verifying in child item
		productdetail.clickMenuRefreshIcon();
		queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(referenceObjectNumber).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("Qualitystatustab"));
		log(LogType.INFO, "Navigate to Quality Status Tab to verify catalog entity rule in Child");

		qualitystatus.maximizeQualityStatusTab().sortRulestByLastExecution();
		List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("Status"));
		List<String> myList = new ArrayList<String>(Arrays.asList(map.get("CatalogEntityRule").split(",")));
		Assertions.assertThat(catalogentityrule).containsAll(myList);
		qualitystatus.minimizeQualityStatusTab();
		log(LogType.INFO, "Verifying catalog entity rule");
		pimHomepage.clickLogoutButton();
	}

	//WRFL_009_CA
	@PimFrameworkAnnotation(module = Modules.DENTAL_CATALOG, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.CatalogEntityRule)
	@Test(description = "WRFL_009_CA | Verify substitution exception rule for child get triggered if we update the dental catalog in parent for CA", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"CA","pim","DENTAL_CATALOG"})
	public void verify_quality_status_when_dental_catalog_is_updated_in_parent_CA_dental_division(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();

		//verifying in parent item in CA catalog
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("ReferenceTab"));
		qualitystatus.maximizeQualityStatusTab();
		String referenceObjectNumber = referencesPage.getReferenceObjectNumber();
		qualitystatus.minimizeQualityStatusTab();

		productdetail.selectTabfromDropdown(map.get("TabName"));
		log(LogType.INFO, "Navigate to Canadian Dental catalog Tab to assign dental in userdriven in Parent");

		catalogTypePage.clearUserDriven(map.get("userDriven"));
		BasePage.WaitForMiliSec(3000);

		//assigning dental catalog
		catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		dental_user_driven = catalogTypePage.getUserDrivenCatalogs();
		Assertions.assertThat(dental_user_driven).containsAnyOf(map.get("userDriven"));

		//verifying in child item
		productdetail.clickMenuRefreshIcon();
		queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(referenceObjectNumber).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("Qualitystatustab"));
		log(LogType.INFO, "Navigate to Quality Status Tab to verify catalog entity rule in Child");

		qualitystatus.maximizeQualityStatusTab().sortRulestByLastExecution();
		List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("Status"));
		List<String> myList = new ArrayList<String>(Arrays.asList(map.get("CatalogEntityRule").split(",")));
		Assertions.assertThat(catalogentityrule).containsAll(myList);
		qualitystatus.minimizeQualityStatusTab();
		log(LogType.INFO, "Verifying catalog entity rule");
		pimHomepage.clickLogoutButton();
	}

	//WRFL_015_US
	@PimFrameworkAnnotation(module = Modules.MEDICAL_CATALOG, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.CatalogEntityRule)
	@Test(description = "WRFL_015_US | Verify substitution exception rule for child get triggered if we update the medical catalog in parent for US", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"US","pim","MEDICAL_CATALOG"})
	public void verify_quality_status_when_medical_catalog_is_updated_in_parent_US_medical_division(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();

		//verifying in parent item in US catalog
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("ReferenceTab"));
		qualitystatus.maximizeQualityStatusTab();
		String referenceObjectNumber = referencesPage.getReferenceObjectNumber();
		qualitystatus.minimizeQualityStatusTab();

		productdetail.selectTabfromDropdown(map.get("TabName"));
		log(LogType.INFO, "Navigate to Medical catalog Tab to assign medical in userdriven in Parent");

		catalogTypePage.clearUserDriven(map.get("userDriven"));
		BasePage.WaitForMiliSec(3000);

		//assigning medical catalog
		catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		medical_user_driven = catalogTypePage.getUserDrivenCatalogs();
		//Assertions.assertThat(medical_user_driven).containsAnyOf(map.get("userDriven"));

		//verify in master
		productdetail.clickMenuRefreshIcon();
		queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
		log(LogType.INFO, "Navigate to Medical catalog tab to verify the userdriven in Master Catalog");
		List<String> masterUserdriven = catalogTypePage.getUserDrivenCatalogs();
		//Assertions.assertThat(masterUserdriven).containsAnyOf(map.get("userDriven"));

		//verifying in child item
		productdetail.clickMenuRefreshIcon();
		queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(referenceObjectNumber).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("Qualitystatustab"));
		log(LogType.INFO, "Navigate to Quality Status Tab to verify catalog entity rule in Child");

		qualitystatus.maximizeQualityStatusTab().sortRulestByLastExecution();
		List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("Status"));
		List<String> myList = new ArrayList<String>(Arrays.asList(map.get("CatalogEntityRule").split(",")));
		Assertions.assertThat(catalogentityrule).containsAll(myList);
		qualitystatus.minimizeQualityStatusTab();
		log(LogType.INFO, "Verifying catalog entity rule");
		pimHomepage.clickLogoutButton();
	}

	//WRFL_015_CA
	@PimFrameworkAnnotation(module = Modules.MEDICAL_CATALOG, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.CatalogEntityRule)
	@Test(description = "WRFL_015_CA | Verify substitution exception rule for child get triggered if we update the medical catalog in parent for CA", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"CA","pim","MEDICAL_CATALOG"})
	public void verify_quality_status_when_medical_catalog_is_updated_in_parent_CA_medical_division(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();

		//verifying in parent item in CA catalog
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("ReferenceTab"));
		qualitystatus.maximizeQualityStatusTab();
		String referenceObjectNumber = referencesPage.getReferenceObjectNumber();
		qualitystatus.minimizeQualityStatusTab();

		productdetail.selectTabfromDropdown(map.get("TabName"));
		log(LogType.INFO, "Navigate to Canadian Medical catalog Tab to assign medical in userdriven in Parent");

		catalogTypePage.clearUserDriven(map.get("userDriven"));
		BasePage.WaitForMiliSec(3000);

		//assigning medical catalog
		catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		medical_user_driven = catalogTypePage.getUserDrivenCatalogs();
		Assertions.assertThat(medical_user_driven).containsAnyOf(map.get("userDriven"));

		//verifying in child item
		productdetail.clickMenuRefreshIcon();
		queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(referenceObjectNumber).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("Qualitystatustab"));
		log(LogType.INFO, "Navigate to Quality Status Tab to verify catalog entity rule in Child");

		qualitystatus.maximizeQualityStatusTab().sortRulestByLastExecution();
		List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("Status"));
		List<String> myList = new ArrayList<String>(Arrays.asList(map.get("CatalogEntityRule").split(",")));
		Assertions.assertThat(catalogentityrule).containsAll(myList);
		qualitystatus.minimizeQualityStatusTab();
		log(LogType.INFO, "Verifying catalog entity rule");
		pimHomepage.clickLogoutButton();
	}

	//WRFL_027_US
	@PimFrameworkAnnotation(module = Modules.ZAHN_CATALOG, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.CatalogEntityRule)
	@Test(description = "WRFL_027_US | Verify substitution exception rule for child get triggered if we update the zahn catalog in parent for US", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"US","pim","ZAHN_CATALOG"})
	public void verify_quality_status_when_zahn_catalog_is_updated_in_parent_US_zahn_division(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Zahn User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Zahn User").get("Password")).clickLoginButton();

		//verifying in parent item in US catalog
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("ReferenceTab"));
		qualitystatus.maximizeQualityStatusTab();
		String referenceObjectNumber = referencesPage.getReferenceObjectNumber();
		qualitystatus.minimizeQualityStatusTab();

		productdetail.selectTabfromDropdown(map.get("TabName"));
		log(LogType.INFO, "Navigate to zahn catalog Tab to assign caga in userdriven in Parent");

		catalogTypePage.clearUserDriven(map.get("userDriven"));
		BasePage.WaitForMiliSec(3000);

		//assigning zahn catalog
		catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		zahn_user_driven = catalogTypePage.getUserDrivenCatalogs();
		Assertions.assertThat(zahn_user_driven).containsAnyOf(map.get("userDriven"));

		//verify in master
		productdetail.clickMenuRefreshIcon();
		queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
		log(LogType.INFO, "Navigate to zahn catalog tab to verify the userdriven in Master Catalog");
		List<String> masterUserdriven = catalogTypePage.getUserDrivenCatalogs();
		Assertions.assertThat(masterUserdriven).containsAnyOf(map.get("userDriven"));

		//verifying in child item
		productdetail.clickMenuRefreshIcon();
		queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(referenceObjectNumber).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("Qualitystatustab"));
		log(LogType.INFO, "Navigate to Quality Status Tab to verify catalog entity rule in Child");

		qualitystatus.maximizeQualityStatusTab().sortRulestByLastExecution();
		List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("Status"));
		List<String> myList = new ArrayList<String>(Arrays.asList(map.get("CatalogEntityRule").split(",")));
		Assertions.assertThat(catalogentityrule).containsAll(myList);
		qualitystatus.minimizeQualityStatusTab();
		log(LogType.INFO, "Verifying catalog entity rule");
		pimHomepage.clickLogoutButton();
	}

	//WRFL_027_CA
	@PimFrameworkAnnotation(module = Modules.ZAHN_CATALOG, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.CatalogEntityRule)
	@Test(description = "WRFL_027_CA | Verify substitution exception rule for child get triggered if we update the zahn catalog in parent for CA", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"CA","pim","ZAHN_CATALOG"})
	public void verify_quality_status_when_zahn_catalog_is_updated_in_parent_CA_zahn_division(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();

		//verifying in parent item in CA catalog
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("ReferenceTab"));
		qualitystatus.maximizeQualityStatusTab();
		String referenceObjectNumber = referencesPage.getReferenceObjectNumber();
		qualitystatus.minimizeQualityStatusTab();

		productdetail.selectTabfromDropdown(map.get("TabName"));
		log(LogType.INFO, "Navigate to Canadian zahn catalog Tab to assign caga in userdriven in Parent");

		catalogTypePage.clearUserDriven(map.get("userDriven"));
		BasePage.WaitForMiliSec(3000);

		//assigning zahn catalog
		catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		zahn_user_driven = catalogTypePage.getUserDrivenCatalogs();

		//verifying in child item
		productdetail.clickMenuRefreshIcon();
		queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(referenceObjectNumber).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("Qualitystatustab"));
		log(LogType.INFO, "Navigate to Quality Status Tab to verify catalog entity rule in Child");

		qualitystatus.maximizeQualityStatusTab().sortRulestByLastExecution();
		List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("Status"));
		List<String> myList = new ArrayList<String>(Arrays.asList(map.get("CatalogEntityRule").split(",")));
		Assertions.assertThat(catalogentityrule).containsAll(myList);
		qualitystatus.minimizeQualityStatusTab();
		log(LogType.INFO, "Verifying catalog entity rule");
		pimHomepage.clickLogoutButton();

	}
}
