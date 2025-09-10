package com.pim.tests.catalogtests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;
import static com.pim.reports.FrameworkLogger.log;
import com.pim.annotations.PimFrameworkAnnotation;
import com.pim.annotations.TestDataSheet;
import com.pim.enums.CategoryType;
import com.pim.enums.LogType;
import com.pim.enums.Modules;
import com.pim.enums.TestCaseSheet;
import com.pim.pages.CatalogTypePage;
import com.pim.pages.ClassificationsPage;
import com.pim.pages.GlobalAttributePage;
import com.pim.pages.LoginPage;
import com.pim.pages.PimHomepage;
import com.pim.pages.QualityStatusPage;
import com.pim.tests.BaseTest;
import com.pim.utils.DataProviderUtils;
import com.pim.utils.ExcelUtils;
import com.pim.utils.Javautils;

public class CatalogExclusivecriteriaTest extends BaseTest {
	
	
	CatalogTypePage catalogtype = new CatalogTypePage();
	ClassificationsPage classificationpage = new ClassificationsPage();
	QualityStatusPage qualitystatus = new QualityStatusPage();
	GlobalAttributePage globalattribute = new GlobalAttributePage();
	Javautils javautil = new Javautils();
	List<String> rule_driven;
	String primarytaxonomy;
	String ecommercetaxonomy;
	String manufacturecode;
	String suppliercode;
	
	//CATR_098
	@PimFrameworkAnnotation(module = Modules.CLASSIFICATION, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Inclusive_Catalog_rule)
	@Test(description = "CATR_098: Verify the rule with exclusion criteria for Supplier assigns ALTAMED when the from catalog is PUGET", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"US"})
	public void verify_rule_driven_assign_for_manufacture_code_with_exclusive_criteria(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Special Markets User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Special Markets User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
		log(LogType.INFO,"Navigate to Global Attribute tab from Queries Menu for US Catalog");
		
		manufacturecode = globalattribute.getManufacturercode();
		for(String code: javautil.readMultipleValuesFromExcel(map.get("ManufactureCode"))) {
			Assertions.assertThat(manufacturecode).isNotEqualTo(code);
		}
		log(LogType.INFO,"Verifying manufacturer code");
		
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("TabName"));
		catalogtype.clickUserDriven().selectUserDriven(map.get("UserDriven"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		rule_driven = catalogtype.getRuleDrivenCatalogs();
		Assertions.assertThat(rule_driven).containsAnyOf(map.get("RuleDriven"));
		log(LogType.INFO,"Verifying rule driven catalog");
		
		//verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).getRuleNamesByRuleStatus(map.get("Status"));
		System.out.print(catalogentityrule);
		List<String> myList = new ArrayList<String>(Arrays.asList(map.get("CatalogEntityRule").split(",")));
		Assertions.assertThat(catalogentityrule).isEqualTo(myList);
		log(LogType.INFO,"Verifying catalog entity rule");

		//verify in master
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog"))
		.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
		log(LogType.INFO,"Navigate to Classification tab from Queries Menu for Master Catalog");		
		manufacturecode = globalattribute.getManufacturercode();
		Assertions.assertThat(manufacturecode).isNotEqualTo(map.get("ManufactureCode"));
		log(LogType.INFO,"Verifying manufacturer code");
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("TabName"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		List<String> masterRuledriven = catalogtype.getRuleDrivenCatalogs();
		Assertions.assertThat(rule_driven).containsAll(masterRuledriven);
		Assertions.assertThat(masterRuledriven).containsAnyOf(map.get("RuleDriven"));
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType"))
		.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
		catalogtype.clearSelectedFields(map.get("UserDriven"));
		log(LogType.INFO,"Deleting the Userdriven");
		pimHomepage.clickLogoutButton();
	}
	
	@PimFrameworkAnnotation(module = Modules.CLASSIFICATION, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Inclusive_Catalog_rule)
	@Test(description = "Verify the rule with exclusion criteria for Supplier should not assigns ALTAMED when the from catalog is PUGET.", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"US"})
	public void verify_rule_driven_not_assign_for_manufacture_code_with_exclusive_criteria(Map<String, String> map){
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Special Markets User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Special Markets User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
		log(LogType.INFO,"Navigate to Global Attribute tab from Queries Menu for US Catalog");
		
		manufacturecode = globalattribute.getManufacturercode();
		Assertions.assertThat(manufacturecode).isEqualTo(map.get("ManufactureCode"));
		log(LogType.INFO,"Verifying manufacturer code");
		
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("TabName"));
		catalogtype.clickUserDriven().selectUserDriven(map.get("UserDriven"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		rule_driven = catalogtype.getRuleDrivenCatalogs();
		Assertions.assertThat(rule_driven).doesNotContain(map.get("RuleDriven"));
		log(LogType.INFO,"Verifying rule driven catalog");
		
		//verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).getRuleNamesByRuleStatus(map.get("Status"));
		System.out.print(catalogentityrule);
		List<String> myList = new ArrayList<String>(Arrays.asList(map.get("CatalogEntityRule").split(",")));
		Assertions.assertThat(catalogentityrule).isEqualTo(myList);
		log(LogType.INFO,"Verifying catalog entity rule");
		
		//verify in master
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog"))
		.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
		log(LogType.INFO,"Navigate to Classification tab from Queries Menu for Master Catalog");		
		manufacturecode = globalattribute.getManufacturercode();
		Assertions.assertThat(manufacturecode).isEqualTo(map.get("ManufactureCode"));
		log(LogType.INFO,"Verifying manufacturer code");
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("TabName"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		List<String> masterRuledriven = catalogtype.getRuleDrivenCatalogs();
		Assertions.assertThat(rule_driven).containsAll(masterRuledriven);
		Assertions.assertThat(masterRuledriven).doesNotContain(map.get("RuleDriven"));
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType"))
		.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
		catalogtype.clearSelectedFields(map.get("UserDriven"));
		log(LogType.INFO,"Deleting the Userdriven");
		pimHomepage.clickLogoutButton();
	}
	
	//CATR_101
	@PimFrameworkAnnotation(module = Modules.CLASSIFICATION, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Inclusive_Catalog_rule)
	@Test(description = "CATR_101: Verify the rule with exclusion criteria for  Primary Taxonomy assigns APPLEVALLY when the from catalog is DUMMY", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"US"})
	public void verify_rule_driven_with_exclusive_criteria_for_primary_taxonomy(Map<String, String> map){
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Special Markets User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Special Markets User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO,"Navigate to Classification tab from Queries Menu for US Catalog");
		
		primarytaxonomy = classificationpage.getPrimaryTaxonomy(map.get("Primary"));
		Assertions.assertThat(primarytaxonomy).isNotEqualTo(map.get("PrimaryTaxonomy"));
		log(LogType.INFO,"Verifying primary code");
		
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("TabName"));
		catalogtype.clickUserDriven().selectUserDriven(map.get("UserDriven"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		rule_driven = catalogtype.getRuleDrivenCatalogs();
		Assertions.assertThat(rule_driven).containsAnyOf(map.get("RuleDriven"));
		log(LogType.INFO,"Verifying rule driven catalog");
		
		//verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).getRuleNamesByRuleStatus(map.get("Status"));
		System.out.print(catalogentityrule);
		List<String> myList = new ArrayList<String>(Arrays.asList(map.get("CatalogEntityRule").split(",")));
		Assertions.assertThat(catalogentityrule).isEqualTo(myList);
		log(LogType.INFO,"Verifying catalog entity rule");
		
		//verify in master
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog"))
		.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO,"Navigate to Classification tab from Queries Menu for Master Catalog");		
		primarytaxonomy = classificationpage.getPrimaryTaxonomy(map.get("Primary"));
		Assertions.assertThat(primarytaxonomy).isNotEqualTo(map.get("PrimaryTaxonomy"));
		log(LogType.INFO,"Verifying primary code");
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("TabName"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		List<String> masterRuledriven = catalogtype.getRuleDrivenCatalogs();
		Assertions.assertThat(rule_driven).containsAll(masterRuledriven);
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType"))
		.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
		catalogtype.clearSelectedFields(map.get("UserDriven"));
		log(LogType.INFO,"Deleting the Userdriven");
		pimHomepage.clickLogoutButton();
	}
	
	//CATR_102
	@PimFrameworkAnnotation(module = Modules.CLASSIFICATION, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Inclusive_Catalog_rule)
	@Test(description = "CATR_102: Verify the rule with exclusion criteria for  Ecommerce Taxonomy assigns PUGET when the from catalog is GREENDENT", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"US"})
	public void verify_rule_driven_assign_with_exclusive_criteria_for_ecommerce_taxonomy(Map<String, String> map){
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Special Markets User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Special Markets User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO,"Navigate to Classification tab from Queries Menu for US Catalog");
		
		ecommercetaxonomy = classificationpage.getEcommerceTaxonomy(map.get("E-Commerce"));
		Assertions.assertThat(ecommercetaxonomy).isNotEqualTo(map.get("EcommerceTaxonomy"));
		log(LogType.INFO,"Verifying ecommerce code");
		
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("TabName"));
		catalogtype.clickUserDriven().selectUserDriven(map.get("UserDriven"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		rule_driven = catalogtype.getRuleDrivenCatalogs();
		Assertions.assertThat(rule_driven).containsAnyOf(map.get("RuleDriven"));
		log(LogType.INFO,"Verifying rule driven catalog");
		
		//verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).getRuleNamesByRuleStatus(map.get("Status"));
		System.out.print(catalogentityrule);
		List<String> myList = new ArrayList<String>(Arrays.asList(map.get("CatalogEntityRule").split(",")));
		Assertions.assertThat(catalogentityrule).isEqualTo(myList);
		log(LogType.INFO,"Verifying catalog entity rule");
		
		//verify in master
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog"))
		.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO,"Navigate to Classification tab from Queries Menu for Master Catalog");		
		ecommercetaxonomy = classificationpage.getEcommerceTaxonomy(map.get("E-Commerce"));
		Assertions.assertThat(ecommercetaxonomy).isNotEqualTo(map.get("EcommerceTaxonomy"));
		log(LogType.INFO,"Verifying ecommerce code");
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("TabName"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		List<String> masterRuledriven = catalogtype.getRuleDrivenCatalogs();
		Assertions.assertThat(rule_driven).containsAll(masterRuledriven);
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType"))
		.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
		catalogtype.clearSelectedFields(map.get("UserDriven"));
		log(LogType.INFO,"Deleting the Userdriven");
		pimHomepage.clickLogoutButton();
	}
	
	//CATR_103
	@PimFrameworkAnnotation(module = Modules.CLASSIFICATION, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Inclusive_Catalog_rule)
	@Test(description = "CATR_103: Verify the rule with exclusion criteria for  Ecommerce Taxonomy should not assign PUGET when the from catalog in GREENDENT.", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"US"})
	public void verify_rule_driven_not_assign_with_exclusive_criteria_for_ecommerce_taxonomy(Map<String, String> map){
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Special Markets User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Special Markets User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO,"Navigate to Classification tab from Queries Menu for US Catalog");
		
		ecommercetaxonomy = classificationpage.getEcommerceTaxonomy(map.get("E-Commerce"));
		Assertions.assertThat(ecommercetaxonomy).isEqualTo(map.get("EcommerceTaxonomy"));
		log(LogType.INFO,"Verifying ecommerce code");
		
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("TabName"));
		catalogtype.clickUserDriven().selectUserDriven(map.get("UserDriven"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		rule_driven = catalogtype.getRuleDrivenCatalogs();
		Assertions.assertThat(rule_driven).doesNotContain(map.get("RuleDriven"));
		log(LogType.INFO,"Verifying rule driven catalog");
		
		//verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).getRuleNamesByRuleStatus(map.get("Status"));
		System.out.print(catalogentityrule);
		List<String> myList = new ArrayList<String>(Arrays.asList(map.get("CatalogEntityRule").split(",")));
		Assertions.assertThat(catalogentityrule).isEqualTo(myList);
		log(LogType.INFO,"Verifying catalog entity rule");
		
		//verify in master
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog"))
		.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO,"Navigate to Classification tab from Queries Menu for Master Catalog");		
		ecommercetaxonomy = classificationpage.getEcommerceTaxonomy(map.get("E-Commerce"));
		Assertions.assertThat(ecommercetaxonomy).isEqualTo(map.get("EcommerceTaxonomy"));
		log(LogType.INFO,"Verifying ecommerce code");
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("TabName"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		List<String> masterRuledriven = catalogtype.getRuleDrivenCatalogs();
		Assertions.assertThat(rule_driven).containsAll(masterRuledriven);
		Assertions.assertThat(masterRuledriven).doesNotContain(map.get("RuleDriven"));
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType"))
		.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
		catalogtype.clearSelectedFields(map.get("UserDriven"));
		log(LogType.INFO,"Deleting the Userdriven");
		pimHomepage.clickLogoutButton();
	}
	
	//CATR_104
	@PimFrameworkAnnotation(module = Modules.CLASSIFICATION, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Inclusive_Catalog_rule)
	@Test(description = "CATR_104: Verify the rule with exclusion criteria for  Brand assigns UNIVERSAL when the from catalog is REMINGTON", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"US"})
	public void verify_rule_driven_assign_for_brand_code_with_exclusive_criteria(Map<String, String> map){
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Special Markets User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Special Markets User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
		log(LogType.INFO,"Navigate to Global Attribute tab from Queries Menu for US Catalog");
		
		suppliercode = globalattribute.getSuppliercode();
		Assertions.assertThat(suppliercode).isNotEqualTo(map.get("BrandCode"));
		log(LogType.INFO,"Verifying Brand code");
		
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("TabName"));
		catalogtype.clickUserDriven().selectUserDriven(map.get("UserDriven"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		rule_driven = catalogtype.getRuleDrivenCatalogs();
		Assertions.assertThat(rule_driven).containsAnyOf(map.get("RuleDriven"));
		log(LogType.INFO,"Verifying rule driven catalog");
		
		//verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).getRuleNamesByRuleStatus(map.get("Status"));
		System.out.print(catalogentityrule);
		List<String> myList = new ArrayList<String>(Arrays.asList(map.get("CatalogEntityRule").split(",")));
		Assertions.assertThat(catalogentityrule).isEqualTo(myList);
		log(LogType.INFO,"Verifying catalog entity rule");
		
		//verify in master
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog"))
		.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
		log(LogType.INFO,"Navigate to Classification tab from Queries Menu for Master Catalog");		
		suppliercode = globalattribute.getSuppliercode();
		Assertions.assertThat(suppliercode).isNotEqualTo(map.get("BrandCode"));
		log(LogType.INFO,"Verifying Brand code for Master Catalog");
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("TabName"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		List<String> masterRuledriven = catalogtype.getRuleDrivenCatalogs();
		Assertions.assertThat(masterRuledriven).containsAnyOf(map.get("RuleDriven"));
		Assertions.assertThat(rule_driven).containsAll(masterRuledriven);
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType"))
		.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
		catalogtype.clearSelectedFields(map.get("UserDriven"));
		log(LogType.INFO,"Deleting the Userdriven");
		pimHomepage.clickLogoutButton();
	}
	
	@PimFrameworkAnnotation(module = Modules.CLASSIFICATION, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Inclusive_Catalog_rule)
	@Test(description = "Verify the rule with exclusion criteria for Brand should not assign UNIVERSAL when the from catalog is REMINGTON", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"US"})
	public void verify_rule_driven_not_assign_for_brand_code_with_exclusive_criteria(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Special Markets User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Special Markets User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
		log(LogType.INFO,"Navigate to Global Attribute tab from Queries Menu for US Catalog");
		
		suppliercode = globalattribute.getSuppliercode();
		Assertions.assertThat(suppliercode).isEqualTo(map.get("BrandCode"));
		log(LogType.INFO,"Verifying Brand code");
		
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("TabName"));
		catalogtype.clickUserDriven().selectUserDriven(map.get("UserDriven"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		rule_driven = catalogtype.getRuleDrivenCatalogs();
		Assertions.assertThat(rule_driven).doesNotContain(map.get("RuleDriven"));
		log(LogType.INFO,"Verifying rule driven catalog");
		
		//verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).getRuleNamesByRuleStatus(map.get("Status"));
		System.out.print(catalogentityrule);
		List<String> myList = new ArrayList<String>(Arrays.asList(map.get("CatalogEntityRule").split(",")));
		Assertions.assertThat(catalogentityrule).isEqualTo(myList);
		log(LogType.INFO,"Verifying catalog entity rule");
		
		//verify in master
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog"))
		.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
		log(LogType.INFO,"Navigate to Classification tab from Queries Menu for Master Catalog");	
		suppliercode = globalattribute.getSuppliercode();
		Assertions.assertThat(suppliercode).isEqualTo(map.get("BrandCode"));
		log(LogType.INFO,"Verifying Brand code in Master");
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("TabName"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		List<String> masterRuledriven = catalogtype.getRuleDrivenCatalogs();
		Assertions.assertThat(rule_driven).containsAll(masterRuledriven);
		Assertions.assertThat(masterRuledriven).doesNotContain(map.get("RuleDriven"));
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType"))
		.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
		catalogtype.clearSelectedFields(map.get("UserDriven"));
		log(LogType.INFO,"Deleting the Userdriven");
		pimHomepage.clickLogoutButton();
	}
	
	//CATR_106
	@PimFrameworkAnnotation(module = Modules.CLASSIFICATION, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Inclusive_Catalog_rule)
	@Test(description = "CATR_106: Verify the rule with exclusion criteria for Supplier assigns ALTAMED when the from catalog is MYFAM", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"US"})
	public void verify_rule_driven_assign_for_supplier_code_with_exclusive_criteria(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Special Markets User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Special Markets User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
		log(LogType.INFO,"Navigate to Global Attribute tab from Queries Menu for US Catalog");
		
		suppliercode = globalattribute.getSuppliercode();
		Assertions.assertThat(suppliercode).isNotEqualTo(map.get("Supplier"));
		log(LogType.INFO,"Verifying Supplier code");
		
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("TabName"));
		catalogtype.clickUserDriven().selectUserDriven(map.get("UserDriven"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		rule_driven = catalogtype.getRuleDrivenCatalogs();
		Assertions.assertThat(rule_driven).containsAnyOf(map.get("RuleDriven"));
		log(LogType.INFO,"Verifying rule driven catalog");
		
		//verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).getRuleNamesByRuleStatus(map.get("Status"));
		System.out.print(catalogentityrule);
		List<String> myList = new ArrayList<String>(Arrays.asList(map.get("CatalogEntityRule").split(",")));
		Assertions.assertThat(catalogentityrule).isEqualTo(myList);
		log(LogType.INFO,"Verifying catalog entity rule");
		
		//verify in master
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog"))
		.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
		log(LogType.INFO,"Navigate to Classification tab from Queries Menu for Master Catalog");	
		suppliercode = globalattribute.getSuppliercode();
		Assertions.assertThat(suppliercode).isNotEqualTo(map.get("Supplier"));
		log(LogType.INFO,"Verifying Supplier code");
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("TabName"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		List<String> masterRuledriven = catalogtype.getRuleDrivenCatalogs();
		Assertions.assertThat(masterRuledriven).containsAnyOf(map.get("RuleDriven"));
		Assertions.assertThat(rule_driven).containsAll(masterRuledriven);
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType"))
		.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
		catalogtype.clearSelectedFields(map.get("UserDriven"));
		log(LogType.INFO,"Deleting the Userdriven");
		pimHomepage.clickLogoutButton();		
	}
	
	//CATR_107
	@PimFrameworkAnnotation(module = Modules.CLASSIFICATION, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Inclusive_Catalog_rule)
	@Test(description = "CATR_107: Verify the rule with exclusion criteria for Supplier should not assign ALTAMED when the from catalog is MYFAM", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"US"})
	public void verify_rule_driven_not_assign_for_supplier_code_with_exclusive_criteria(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Special Markets User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Special Markets User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
		log(LogType.INFO,"Navigate to Global Attribute tab from Queries Menu for US Catalog");
		
		suppliercode = globalattribute.getSuppliercode();
		Assertions.assertThat(suppliercode).isEqualTo(map.get("Supplier"));
		log(LogType.INFO,"Verifying Supplier code");
		
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("TabName"));
		catalogtype.clickUserDriven().selectUserDriven(map.get("UserDriven"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		rule_driven = catalogtype.getRuleDrivenCatalogs();
		Assertions.assertThat(rule_driven).doesNotContain(map.get("RuleDriven"));
		log(LogType.INFO,"Verifying rule driven catalog");
		
		//verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).getRuleNamesByRuleStatus(map.get("Status"));
		System.out.print(catalogentityrule);
		List<String> myList = new ArrayList<String>(Arrays.asList(map.get("CatalogEntityRule").split(",")));
		Assertions.assertThat(catalogentityrule).isEqualTo(myList);
		log(LogType.INFO,"Verifying catalog entity rule");
		
		//verify in master
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog"))
		.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
		log(LogType.INFO,"Navigate to Classification tab from Queries Menu for Master Catalog");
		suppliercode = globalattribute.getSuppliercode();
		Assertions.assertThat(suppliercode).isEqualTo(map.get("Supplier"));
		log(LogType.INFO,"Verifying Supplier code");
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("TabName"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		List<String> masterRuledriven = catalogtype.getRuleDrivenCatalogs();
		Assertions.assertThat(rule_driven).containsAll(masterRuledriven);
		Assertions.assertThat(masterRuledriven).doesNotContain(map.get("RuleDriven"));
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType"))
		.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
		catalogtype.clearSelectedFields(map.get("UserDriven"));
		log(LogType.INFO,"Deleting the Userdriven");
		pimHomepage.clickLogoutButton();
	}
	
		
	
	

}
