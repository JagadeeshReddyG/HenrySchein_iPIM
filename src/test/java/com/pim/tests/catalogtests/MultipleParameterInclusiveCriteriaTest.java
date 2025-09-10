package com.pim.tests.catalogtests;

import static com.pim.reports.FrameworkLogger.log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

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

public class MultipleParameterInclusiveCriteriaTest extends BaseTest {
	CatalogTypePage catalogtype = new CatalogTypePage();
	ClassificationsPage classificationpage = new ClassificationsPage();
	QualityStatusPage qualitystatus = new QualityStatusPage();
	GlobalAttributePage globalattribute = new GlobalAttributePage();
	List<String> rule_driven;
	String primarytaxonomy;
	String ecommercetaxonomy;
	String suppliercode;
	String manufacturecode;
	String clasificationcode;

	//CATR_113
	@PimFrameworkAnnotation(module = Modules.CLASSIFICATION, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Inclusive_Catalog_rule)
	@Test(description = "CATR_113 | Verify the rule with inclusion criteria for Primary Taxonomy and Brand assigns VALLEYDTL when the from catalog is GREENDENT", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"US"})
	public void verify_rule_driven_assigned_for_primary_and_brand_code_with_inclusive_criteria(Map<String, String> map){
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Special Markets User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Special Markets User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
		log(LogType.INFO,"Navigate to Global Attribute tab from Queries Menu for US Catalog");

		suppliercode = globalattribute.getSuppliercode();
		Assertions.assertThat(suppliercode).isEqualTo(map.get("BrandCode"));
		log(LogType.INFO,"Verifying Brand code");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO,"Navigate to Classification tab from Queries Menu for US Catalog");

		primarytaxonomy = classificationpage.getPrimaryTaxonomy(map.get("Primary"));
		Assertions.assertThat(primarytaxonomy).isEqualTo(map.get("PrimaryTaxonomy"));
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
		List<String> myList = new ArrayList<String>(Arrays.asList(map.get("CatalogEntityRule").split(",")));
		Assertions.assertThat(catalogentityrule).isEqualTo(myList);
		log(LogType.INFO,"Verifying catalog entity rule");

		//verify in master
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog"))
		.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
		log(LogType.INFO,"Navigate to Global Attribute tab from Queries Menu for US Catalog");

		suppliercode = globalattribute.getSuppliercode();
		Assertions.assertThat(suppliercode).isEqualTo(map.get("BrandCode"));
		log(LogType.INFO,"Verifying Brand code");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO,"Navigate to Classification tab from Queries Menu for US Catalog");

		primarytaxonomy = classificationpage.getPrimaryTaxonomy(map.get("Primary"));
		Assertions.assertThat(primarytaxonomy).isEqualTo(map.get("PrimaryTaxonomy"));
		log(LogType.INFO,"Verifying primary code");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
		List<String> masterRuledriven = catalogtype.getRuleDrivenCatalogs();
		Assertions.assertThat(rule_driven).containsAll(masterRuledriven);
		Assertions.assertThat(masterRuledriven).containsAnyOf(map.get("RuleDriven"));
		log(LogType.INFO,"Verifying rule driven catalog");

		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType"))
		.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
		catalogtype.clearSelectedFields(map.get("UserDriven"));
		log(LogType.INFO,"Deleting the Userdriven");
		pimHomepage.clickLogoutButton();
	}

	//CATR_114
	@PimFrameworkAnnotation(module = Modules.CLASSIFICATION, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Inclusive_Catalog_rule)
	@Test(description = "CATR_114 | Verify the rule with inclusion criteria for  Primary Taxonomy and Ecommerce Taxonomy assigns UNIVERSAL when the from catalog is MYFAM", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"US"})
	public void verify_rule_driven_assigned_for_primary_and_ecommerce_taxonomy_with_inclusive_criteria(Map<String, String> map){
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Special Markets User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Special Markets User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO,"Navigate to Classification tab from Queries Menu for US Catalog");

		primarytaxonomy = classificationpage.getPrimaryTaxonomy(map.get("Primary"));
		Assertions.assertThat(primarytaxonomy).isEqualTo(map.get("PrimaryTaxonomy"));
		log(LogType.INFO,"Verifying primary code");

		ecommercetaxonomy = classificationpage.getEcommerceTaxonomy(map.get("E-Commerce"));
		Assertions.assertThat(ecommercetaxonomy).isEqualTo(map.get("EcommerceTaxonomy"));
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
		List<String> myList = new ArrayList<String>(Arrays.asList(map.get("CatalogEntityRule").split(",")));
		Assertions.assertThat(catalogentityrule).isEqualTo(myList);
		log(LogType.INFO,"Verifying catalog entity rule");

		//verify in master
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog"))
		.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO,"Navigate to Classification tab from Queries Menu for US Catalog");

		primarytaxonomy = classificationpage.getPrimaryTaxonomy(map.get("Primary"));
		Assertions.assertThat(primarytaxonomy).isEqualTo(map.get("PrimaryTaxonomy"));
		log(LogType.INFO,"Verifying primary code");

		ecommercetaxonomy = classificationpage.getEcommerceTaxonomy(map.get("E-Commerce"));
		Assertions.assertThat(ecommercetaxonomy).isEqualTo(map.get("EcommerceTaxonomy"));
		log(LogType.INFO,"Verifying ecommerce code");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
		List<String> masterRuledriven = catalogtype.getRuleDrivenCatalogs();
		Assertions.assertThat(rule_driven).containsAll(masterRuledriven);
		Assertions.assertThat(masterRuledriven).containsAnyOf(map.get("RuleDriven"));
		log(LogType.INFO,"Verifying rule driven catalog in master");

		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType"))
		.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
		catalogtype.clearSelectedFields(map.get("UserDriven"));
		log(LogType.INFO,"Deleting the Userdriven");
		pimHomepage.clickLogoutButton();
	}

	//CATR_115
	@PimFrameworkAnnotation(module = Modules.CLASSIFICATION, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Inclusive_Catalog_rule)
	@Test(description = "CATR_115 | Verify the rule with inclusion criteria for Primary Taxonomy and Classification assigns PUGET when the from catalog is DUMMY", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"US"})
	public void verify_rule_driven_assigned_for_primary_and_class_code_with_inclusive_criteria(Map<String, String> map){
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Special Markets User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Special Markets User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO,"Navigate to Classification tab from Queries Menu for US Catalog");

		primarytaxonomy = classificationpage.getPrimaryTaxonomy(map.get("Primary"));
		Assertions.assertThat(primarytaxonomy).isEqualTo(map.get("PrimaryTaxonomy"));
		log(LogType.INFO,"Verifying primary code");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("GlobalAttributeTab"));
		log(LogType.INFO,"Navigate to Global Attribute tab from Queries Menu for US Catalog");

		clasificationcode = globalattribute.getClassificationCode();
		Assertions.assertThat(clasificationcode).isEqualTo(map.get("Classificationtab"));
		log(LogType.INFO,"Verifying classification code");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("TabName"));
		catalogtype.clickUserDriven().selectUserDriven(map.get("UserDriven"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		rule_driven = catalogtype.getRuleDrivenCatalogs();
		Assertions.assertThat(rule_driven).containsAnyOf(map.get("RuleDriven"));
		log(LogType.INFO,"Verifying rule driven catalog");

		//verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).getRuleNamesByRuleStatus(map.get("Status"));
		List<String> myList = new ArrayList<String>(Arrays.asList(map.get("CatalogEntityRule").split(",")));
		Assertions.assertThat(catalogentityrule).isEqualTo(myList);
		log(LogType.INFO,"Verifying catalog entity rule");

		//verify in master
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog"))
		.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO,"Navigate to Classification tab from Queries Menu for US Catalog");

		primarytaxonomy = classificationpage.getPrimaryTaxonomy(map.get("Primary"));
		Assertions.assertThat(primarytaxonomy).isEqualTo(map.get("PrimaryTaxonomy"));
		log(LogType.INFO,"Verifying primary code");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("GlobalAttributeTab"));
		log(LogType.INFO,"Navigate to Global Attribute tab from Queries Menu for US Catalog");

		clasificationcode = globalattribute.getClassificationCode();
		Assertions.assertThat(clasificationcode).isEqualTo(map.get("Classificationtab"));
		log(LogType.INFO,"Verifying classification code");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
		List<String> masterRuledriven = catalogtype.getRuleDrivenCatalogs();
		Assertions.assertThat(rule_driven).containsAll(masterRuledriven);
		Assertions.assertThat(masterRuledriven).containsAnyOf(map.get("RuleDriven"));
		log(LogType.INFO,"Verifying rule driven catalog in master");

		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType"))
		.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
		catalogtype.clearSelectedFields(map.get("UserDriven"));
		log(LogType.INFO,"Deleting the Userdriven");
		pimHomepage.clickLogoutButton();
	}

	//CATR_116
	@PimFrameworkAnnotation(module = Modules.CLASSIFICATION, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Inclusive_Catalog_rule)
	@Test(description = "CATR_116 | Verify the rule with inclusion criteria for Primary Taxonomy and Manufacturer assigns UNIVERSAL when the from catalog is PUGET", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"US"})
	public void verify_rule_driven_assigned_for_primary_and_manufacturer_code_with_inclusive_criteria(Map<String, String> map){
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Special Markets User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Special Markets User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO,"Navigate to Classification tab from Queries Menu for US Catalog");

		primarytaxonomy = classificationpage.getPrimaryTaxonomy(map.get("Primary"));
		Assertions.assertThat(primarytaxonomy).isEqualTo(map.get("PrimaryTaxonomy"));
		log(LogType.INFO,"Verifying primary code");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("GlobalAttributeTab"));
		log(LogType.INFO,"Navigate to Global Attribute tab from Queries Menu for US Catalog");

		manufacturecode = globalattribute.getManufacturercode();
		Assertions.assertThat(manufacturecode).isEqualTo(map.get("ManufactureCode"));
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
		List<String> myList = new ArrayList<String>(Arrays.asList(map.get("CatalogEntityRule").split(",")));
		Assertions.assertThat(catalogentityrule).isEqualTo(myList);
		log(LogType.INFO,"Verifying catalog entity rule");

		//verify in master
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog"))
		.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO,"Navigate to Classification tab from Queries Menu for US Catalog");

		primarytaxonomy = classificationpage.getPrimaryTaxonomy(map.get("Primary"));
		Assertions.assertThat(primarytaxonomy).isEqualTo(map.get("PrimaryTaxonomy"));
		log(LogType.INFO,"Verifying primary code");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("GlobalAttributeTab"));
		log(LogType.INFO,"Navigate to Global Attribute tab from Queries Menu for US Catalog");

		manufacturecode = globalattribute.getManufacturercode();
		Assertions.assertThat(manufacturecode).isEqualTo(map.get("ManufactureCode"));
		log(LogType.INFO,"Verifying manufacturer code");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
		List<String> masterRuledriven = catalogtype.getRuleDrivenCatalogs();
		Assertions.assertThat(rule_driven).containsAll(masterRuledriven);
		Assertions.assertThat(masterRuledriven).containsAnyOf(map.get("RuleDriven"));
		log(LogType.INFO,"Verifying rule driven catalog in master");

		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType"))
		.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
		catalogtype.clearSelectedFields(map.get("UserDriven"));
		log(LogType.INFO,"Deleting the Userdriven");
		pimHomepage.clickLogoutButton();
	}

	//CATR_117 and CATR_119
	@PimFrameworkAnnotation(module = Modules.CLASSIFICATION, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Inclusive_Catalog_rule)
	@Test(description = "CATR_117, CATR_119 | Verify the rule with inclusion criteria for  Primary Taxonomy and Supplier assigns DUMMY when the from catalog is ALTAMED", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"US"})
	public void verify_rule_driven_assigned_for_primary_and_supplier_code_with_inclusive_criteria(Map<String, String> map){
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Special Markets User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Special Markets User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO,"Navigate to Classification tab from Queries Menu for US Catalog");

		primarytaxonomy = classificationpage.getPrimaryTaxonomy(map.get("Primary"));
		Assertions.assertThat(primarytaxonomy).isEqualTo(map.get("PrimaryTaxonomy"));
		log(LogType.INFO,"Verifying primary code");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("GlobalAttributeTab"));
		log(LogType.INFO,"Navigate to Global Attribute tab from Queries Menu for US Catalog");

		suppliercode = globalattribute.getSuppliercode();
		Assertions.assertThat(suppliercode).isEqualTo(map.get("Supplier"));
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
		List<String> myList = new ArrayList<String>(Arrays.asList(map.get("CatalogEntityRule").split(",")));
		Assertions.assertThat(catalogentityrule).isEqualTo(myList);
		log(LogType.INFO,"Verifying catalog entity rule");

		//verify in master
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog"))
		.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO,"Navigate to Classification tab from Queries Menu for US Catalog");

		primarytaxonomy = classificationpage.getPrimaryTaxonomy(map.get("Primary"));
		Assertions.assertThat(primarytaxonomy).isEqualTo(map.get("PrimaryTaxonomy"));
		log(LogType.INFO,"Verifying primary code");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("GlobalAttributeTab"));
		log(LogType.INFO,"Navigate to Global Attribute tab from Queries Menu for US Catalog");

		suppliercode = globalattribute.getSuppliercode();
		Assertions.assertThat(suppliercode).isNotEqualTo(map.get("Supplier"));
		log(LogType.INFO,"Verifying Supplier code");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
		List<String> masterRuledriven = catalogtype.getRuleDrivenCatalogs();
		Assertions.assertThat(rule_driven).containsAll(masterRuledriven);
		Assertions.assertThat(masterRuledriven).containsAnyOf(map.get("RuleDriven"));
		log(LogType.INFO,"Verifying rule driven catalog in master");

		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType"))
		.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
		catalogtype.clearSelectedFields(map.get("UserDriven"));
		log(LogType.INFO,"Deleting the Userdriven");
		pimHomepage.clickLogoutButton();
	}

	//CATR_120
	@PimFrameworkAnnotation(module = Modules.CLASSIFICATION, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Inclusive_Catalog_rule)
	@Test(description = "CATR_120 | Verify the rule with inclusion criteria for supplier and should not assign OPTIMUS, when the from catalog is MERI", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"US"})
	public void verify_rule_driven_not_assigned_for_primary_and_supplier_code_with_inclusive_criteria(Map<String, String> map){
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Special Markets User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Special Markets User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO,"Navigate to Classification tab from Queries Menu for US Catalog");

		primarytaxonomy = classificationpage.getPrimaryTaxonomy(map.get("Primary"));
		Assertions.assertThat(primarytaxonomy).isNotEqualTo(map.get("PrimaryTaxonomy"));
		log(LogType.INFO,"Verifying primary code");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("GlobalAttributeTab"));
		log(LogType.INFO,"Navigate to Global Attribute tab from Queries Menu for US Catalog");

		suppliercode = globalattribute.getSuppliercode();
		Assertions.assertThat(suppliercode).isNotEqualTo(map.get("Supplier"));
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
		List<String> myList = new ArrayList<String>(Arrays.asList(map.get("CatalogEntityRule").split(",")));
		Assertions.assertThat(catalogentityrule).isEqualTo(myList);
		log(LogType.INFO,"Verifying catalog entity rule");

		//verify in master
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog"))
		.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO,"Navigate to Classification tab from Queries Menu for US Catalog");

		primarytaxonomy = classificationpage.getPrimaryTaxonomy(map.get("Primary"));
		Assertions.assertThat(primarytaxonomy).isNotEqualTo(map.get("PrimaryTaxonomy"));
		log(LogType.INFO,"Verifying primary code");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("GlobalAttributeTab"));
		log(LogType.INFO,"Navigate to Global Attribute tab from Queries Menu for US Catalog");

		suppliercode = globalattribute.getSuppliercode();
		Assertions.assertThat(suppliercode).isNotEqualTo(map.get("Supplier"));
		log(LogType.INFO,"Verifying Supplier code");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
		List<String> masterRuledriven = catalogtype.getRuleDrivenCatalogs();
		Assertions.assertThat(rule_driven).containsAll(masterRuledriven);
		Assertions.assertThat(masterRuledriven).doesNotContain(map.get("RuleDriven"));
		log(LogType.INFO,"Verifying rule driven catalog in master");

		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType"))
		.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
		catalogtype.clearSelectedFields(map.get("UserDriven"));
		log(LogType.INFO,"Deleting the Userdriven");
		pimHomepage.clickLogoutButton();
	}

	//CATR_121
	@PimFrameworkAnnotation(module = Modules.CLASSIFICATION, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Inclusive_Catalog_rule)
	@Test(description = "CATR_121 | Verify the rule with inclusion criteria for ECommerce Taxonomy and Brand should assign REMINGTON, when the from catalog is VALLEYDTL", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"US"})
	public void verify_rule_driven__assigned_for_ecommerce_and_brand_code_with_inclusive_criteria(Map<String, String> map){
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Special Markets User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Special Markets User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO,"Navigate to Classification tab from Queries Menu for US Catalog");

		ecommercetaxonomy = classificationpage.getEcommerceTaxonomy(map.get("E-Commerce"));
		Assertions.assertThat(ecommercetaxonomy).isEqualTo(map.get("EcommerceTaxonomy"));
		log(LogType.INFO,"Verifying ecommerce code");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("GlobalAttributeTab"));
		log(LogType.INFO,"Navigate to Global Attribute tab from Queries Menu for US Catalog");

		suppliercode = globalattribute.getSuppliercode();
		Assertions.assertThat(suppliercode).isEqualTo(map.get("BrandCode"));
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
		List<String> myList = new ArrayList<String>(Arrays.asList(map.get("CatalogEntityRule").split(",")));
		Assertions.assertThat(catalogentityrule).isEqualTo(myList);
		log(LogType.INFO,"Verifying catalog entity rule");

		//verify in master
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog"))
		.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO,"Navigate to Classification tab from Queries Menu for US Catalog");

		ecommercetaxonomy = classificationpage.getEcommerceTaxonomy(map.get("E-Commerce"));
		Assertions.assertThat(ecommercetaxonomy).isEqualTo(map.get("EcommerceTaxonomy"));
		log(LogType.INFO,"Verifying ecommerce code");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("GlobalAttributeTab"));
		log(LogType.INFO,"Navigate to Global Attribute tab from Queries Menu for US Catalog");

		suppliercode = globalattribute.getSuppliercode();
		Assertions.assertThat(suppliercode).isEqualTo(map.get("BrandCode"));
		log(LogType.INFO,"Verifying Brand code");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
		List<String> masterRuledriven = catalogtype.getRuleDrivenCatalogs();
		Assertions.assertThat(rule_driven).containsAll(masterRuledriven);
		Assertions.assertThat(masterRuledriven).containsAnyOf(map.get("RuleDriven"));
		log(LogType.INFO,"Verifying rule driven catalog in master");

		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType"))
		.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
		catalogtype.clearSelectedFields(map.get("UserDriven"));
		log(LogType.INFO,"Deleting the Userdriven");
		pimHomepage.clickLogoutButton();	
	}

	//CATR_122
	@PimFrameworkAnnotation(module = Modules.CLASSIFICATION, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Inclusive_Catalog_rule)
	@Test(description = "CATR_122 | Verify the rule with inclusion criteria for ECommerce Taxonomy and Brand should not assign REMINGTON, when the from catalog is VALLEYDTL", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"US"})
	public void verify_rule_driven_not_assigned_for_ecommerce_and_brand_code_with_inclusive_criteria(Map<String, String> map){
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Special Markets User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Special Markets User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO,"Navigate to Classification tab from Queries Menu for US Catalog");

		ecommercetaxonomy = classificationpage.getEcommerceTaxonomy(map.get("E-Commerce"));
		Assertions.assertThat(ecommercetaxonomy).isNotEqualTo(map.get("EcommerceTaxonomy"));
		log(LogType.INFO,"Verifying ecommerce code");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("GlobalAttributeTab"));
		log(LogType.INFO,"Navigate to Global Attribute tab from Queries Menu for US Catalog");

		suppliercode = globalattribute.getSuppliercode();
		Assertions.assertThat(suppliercode).isNotEqualTo(map.get("BrandCode"));
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
		List<String> myList = new ArrayList<String>(Arrays.asList(map.get("CatalogEntityRule").split(",")));
		Assertions.assertThat(catalogentityrule).isEqualTo(myList);
		log(LogType.INFO,"Verifying catalog entity rule");

		//verify in master
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog"))
		.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO,"Navigate to Classification tab from Queries Menu for US Catalog");

		ecommercetaxonomy = classificationpage.getEcommerceTaxonomy(map.get("E-Commerce"));
		Assertions.assertThat(ecommercetaxonomy).isNotEqualTo(map.get("EcommerceTaxonomy"));
		log(LogType.INFO,"Verifying ecommerce code");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("GlobalAttributeTab"));
		log(LogType.INFO,"Navigate to Global Attribute tab from Queries Menu for US Catalog");

		suppliercode = globalattribute.getSuppliercode();
		Assertions.assertThat(suppliercode).isNotEqualTo(map.get("BrandCode"));
		log(LogType.INFO,"Verifying Brand code");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
		List<String> masterRuledriven = catalogtype.getRuleDrivenCatalogs();
		Assertions.assertThat(rule_driven).containsAll(masterRuledriven);
		Assertions.assertThat(masterRuledriven).doesNotContain(map.get("RuleDriven"));
		log(LogType.INFO,"Verifying rule driven catalog in master");

		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType"))
		.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
		catalogtype.clearSelectedFields(map.get("UserDriven"));
		log(LogType.INFO,"Deleting the Userdriven");
		pimHomepage.clickLogoutButton();
	}

	//CATR_123
	@PimFrameworkAnnotation(module = Modules.CLASSIFICATION, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Inclusive_Catalog_rule)
	@Test(description = "CATR_123 | Verify the rule with inclusion criteria for ECommerce Taxonomy and Classification should assign HOSPICE, when the from catalog is ATHLETIC", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"US"})
	public void verify_rule_driven_assigned_for_ecommerce_and_classification_code_with_inclusive_criteria(Map<String, String> map){
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO,"Navigate to Classification tab from Queries Menu for US Catalog");

		ecommercetaxonomy = classificationpage.getEcommerceTaxonomy(map.get("E-Commerce"));
		Assertions.assertThat(ecommercetaxonomy).isEqualTo(map.get("EcommerceTaxonomy"));
		log(LogType.INFO,"Verifying ecommerce code");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("GlobalAttributeTab"));
		log(LogType.INFO,"Navigate to Global Attribute tab from Queries Menu for US Catalog");

		clasificationcode = globalattribute.getClassificationCode();
		Assertions.assertThat(clasificationcode).isEqualTo(map.get("Classification"));
		log(LogType.INFO,"Verifying classification code");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("TabName"));
		catalogtype.clickUserDriven().selectUserDriven(map.get("UserDriven"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		rule_driven = catalogtype.getRuleDrivenCatalogs();
		Assertions.assertThat(rule_driven).containsAnyOf(map.get("RuleDriven"));
		log(LogType.INFO,"Verifying rule driven catalog");

		//verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).getRuleNamesByRuleStatus(map.get("Status"));
		List<String> myList = new ArrayList<String>(Arrays.asList(map.get("CatalogEntityRule").split(",")));
		Assertions.assertThat(catalogentityrule).isEqualTo(myList);
		log(LogType.INFO,"Verifying catalog entity rule");

		//verify in master
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog"))
		.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO,"Navigate to Classification tab from Queries Menu for US Catalog");

		ecommercetaxonomy = classificationpage.getEcommerceTaxonomy(map.get("E-Commerce"));
		Assertions.assertThat(ecommercetaxonomy).isEqualTo(map.get("EcommerceTaxonomy"));
		log(LogType.INFO,"Verifying ecommerce code");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("GlobalAttributeTab"));
		log(LogType.INFO,"Navigate to Global Attribute tab from Queries Menu for US Catalog");

		clasificationcode = globalattribute.getClassificationCode();
		Assertions.assertThat(clasificationcode).isEqualTo(map.get("Classification"));
		log(LogType.INFO,"Verifying classification code");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
		List<String> masterRuledriven = catalogtype.getRuleDrivenCatalogs();
		Assertions.assertThat(rule_driven).containsAll(masterRuledriven);
		Assertions.assertThat(masterRuledriven).containsAnyOf(map.get("RuleDriven"));
		log(LogType.INFO,"Verifying rule driven catalog in master");

		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType"))
		.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
		catalogtype.clearSelectedFields(map.get("UserDriven"));
		log(LogType.INFO,"Deleting the Userdriven");
		pimHomepage.clickLogoutButton();
	}

	//CATR_124
	@PimFrameworkAnnotation(module = Modules.CLASSIFICATION, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Inclusive_Catalog_rule)
	@Test(description = "CATR_124 | Verify the rule with inclusion criteria for  ECommerce Taxonomy and Classification should  not assign HOSPICE, when the from catalog is ATHLETIC", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"US"})
	public void verify_rule_driven_not_assigned_for_ecommerce_and_classification_code_with_inclusive_criteria(Map<String, String> map){
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO,"Navigate to Classification tab from Queries Menu for US Catalog");

		ecommercetaxonomy = classificationpage.getEcommerceTaxonomy(map.get("E-Commerce"));
		Assertions.assertThat(ecommercetaxonomy).isNotEqualTo(map.get("EcommerceTaxonomy"));
		log(LogType.INFO,"Verifying ecommerce code");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("GlobalAttributeTab"));
		log(LogType.INFO,"Navigate to Global Attribute tab from Queries Menu for US Catalog");

		clasificationcode = globalattribute.getClassificationCode();
		Assertions.assertThat(clasificationcode).isNotEqualTo(map.get("Classification"));
		log(LogType.INFO,"Verifying classification code");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("TabName"));
		catalogtype.clickUserDriven().selectUserDriven(map.get("UserDriven"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		rule_driven = catalogtype.getRuleDrivenCatalogs();
		Assertions.assertThat(rule_driven).doesNotContain(map.get("RuleDriven"));
		log(LogType.INFO,"Verifying rule driven catalog");

		//verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).getRuleNamesByRuleStatus(map.get("Status"));
		List<String> myList = new ArrayList<String>(Arrays.asList(map.get("CatalogEntityRule").split(",")));
		Assertions.assertThat(catalogentityrule).isEqualTo(myList);
		log(LogType.INFO,"Verifying catalog entity rule");

		//verify in master
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog"))
		.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO,"Navigate to Classification tab from Queries Menu for US Catalog");

		ecommercetaxonomy = classificationpage.getEcommerceTaxonomy(map.get("E-Commerce"));
		Assertions.assertThat(ecommercetaxonomy).isNotEqualTo(map.get("EcommerceTaxonomy"));
		log(LogType.INFO,"Verifying ecommerce code");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("GlobalAttributeTab"));
		log(LogType.INFO,"Navigate to Global Attribute tab from Queries Menu for US Catalog");

		clasificationcode = globalattribute.getClassificationCode();
		Assertions.assertThat(clasificationcode).isNotEqualTo(map.get("Classification"));
		log(LogType.INFO,"Verifying classification code");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
		List<String> masterRuledriven = catalogtype.getRuleDrivenCatalogs();
		Assertions.assertThat(rule_driven).containsAll(masterRuledriven);
		Assertions.assertThat(masterRuledriven).doesNotContain(map.get("RuleDriven"));
		log(LogType.INFO,"Verifying rule driven catalog in master");

		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType"))
		.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
		catalogtype.clearSelectedFields(map.get("UserDriven"));
		log(LogType.INFO,"Deleting the Userdriven");
		pimHomepage.clickLogoutButton();
	}

	//CATR_125
	@PimFrameworkAnnotation(module = Modules.CLASSIFICATION, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Inclusive_Catalog_rule)
	@Test(description = "CATR_125 | Verify the rule with inclusion criteria for ECommerce Taxonomy and Manufacturer should assign SMLABS, when the from catalog is ZAHN", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"US"})
	public void verify_rule_driven_assigned_for_ecommerce_and_manufacturer_code_with_inclusive_criteria(Map<String, String> map){
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Zahn User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Zahn User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO,"Navigate to Classification tab from Queries Menu for US Catalog");

		ecommercetaxonomy = classificationpage.getEcommerceTaxonomy(map.get("E-Commerce"));
		Assertions.assertThat(ecommercetaxonomy).isEqualTo(map.get("EcommerceTaxonomy"));
		log(LogType.INFO,"Verifying ecommerce code");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("GlobalAttributeTab"));
		log(LogType.INFO,"Navigate to Global Attribute tab from Queries Menu for US Catalog");

		manufacturecode = globalattribute.getManufacturercode();
		Assertions.assertThat(manufacturecode).isEqualTo(map.get("ManufactureCode"));
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
		List<String> myList = new ArrayList<String>(Arrays.asList(map.get("CatalogEntityRule").split(",")));
		Assertions.assertThat(catalogentityrule).isEqualTo(myList);
		log(LogType.INFO,"Verifying catalog entity rule");

		//verify in master
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog"))
		.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO,"Navigate to Classification tab from Queries Menu for US Catalog");

		ecommercetaxonomy = classificationpage.getEcommerceTaxonomy(map.get("E-Commerce"));
		Assertions.assertThat(ecommercetaxonomy).isEqualTo(map.get("EcommerceTaxonomy"));
		log(LogType.INFO,"Verifying ecommerce code");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("GlobalAttributeTab"));
		log(LogType.INFO,"Navigate to Global Attribute tab from Queries Menu for US Catalog");

		manufacturecode = globalattribute.getManufacturercode();
		Assertions.assertThat(manufacturecode).isEqualTo(map.get("ManufactureCode"));
		log(LogType.INFO,"Verifying manufacturer code");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
		List<String> masterRuledriven = catalogtype.getRuleDrivenCatalogs();
		Assertions.assertThat(rule_driven).containsAll(masterRuledriven);
		Assertions.assertThat(masterRuledriven).containsAnyOf(map.get("RuleDriven"));
		log(LogType.INFO,"Verifying rule driven catalog in master");

		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType"))
		.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
		catalogtype.clearSelectedFields(map.get("UserDriven"));
		log(LogType.INFO,"Deleting the Userdriven");
		pimHomepage.clickLogoutButton();
	}

	//CATR_126
	@PimFrameworkAnnotation(module = Modules.CLASSIFICATION, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Inclusive_Catalog_rule)
	@Test(description = "CATR_126 | Verify the rule with inclusion criteria for ECommerce Taxonomy and Manufacturer should not assign SMLABS, when the from catalog is ZAHN", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"US"})
	public void verify_rule_driven_not_assigned_for_ecommerce_and_manufacturer_code_with_inclusive_criteria(Map<String, String> map){
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Zahn User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Zahn User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO,"Navigate to Classification tab from Queries Menu for US Catalog");

		ecommercetaxonomy = classificationpage.getEcommerceTaxonomy(map.get("E-Commerce"));
		Assertions.assertThat(ecommercetaxonomy).isNotEqualTo(map.get("EcommerceTaxonomy"));
		log(LogType.INFO,"Verifying ecommerce code");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("GlobalAttributeTab"));
		log(LogType.INFO,"Navigate to Global Attribute tab from Queries Menu for US Catalog");

		manufacturecode = globalattribute.getManufacturercode();
		Assertions.assertThat(manufacturecode).isNotEqualTo(map.get("ManufactureCode"));
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
		List<String> myList = new ArrayList<String>(Arrays.asList(map.get("CatalogEntityRule").split(",")));
		Assertions.assertThat(catalogentityrule).isEqualTo(myList);
		log(LogType.INFO,"Verifying catalog entity rule");

		//verify in master
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog"))
		.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
		List<String> masterRuledriven = catalogtype.getRuleDrivenCatalogs();
		Assertions.assertThat(rule_driven).containsAll(masterRuledriven);
		Assertions.assertThat(masterRuledriven).doesNotContain(map.get("RuleDriven"));
		log(LogType.INFO,"Verifying rule driven catalog in master");

		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType"))
		.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
		catalogtype.clearSelectedFields(map.get("UserDriven"));
		log(LogType.INFO,"Deleting the Userdriven");
		pimHomepage.clickLogoutButton();
	}

}