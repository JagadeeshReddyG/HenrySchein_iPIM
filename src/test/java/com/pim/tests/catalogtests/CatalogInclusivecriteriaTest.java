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
import com.pim.pages.CatalogTypePage;
import com.pim.pages.ClassificationsPage;
import com.pim.pages.GlobalAttributePage;
import com.pim.pages.LoginPage;
import com.pim.pages.PimHomepage;
import com.pim.pages.QualityStatusPage;
import com.pim.tests.BaseTest;
import com.pim.utils.DataProviderUtils;
import com.pim.utils.ExcelUtils;

public class CatalogInclusivecriteriaTest extends BaseTest {
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


	//CATR_090,CATR_91
	@PimFrameworkAnnotation(module = Modules.CLASSIFICATION, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Inclusive_Catalog_rule)
	@Test(description = "CATR_090,CATR_91: Verify whether the rule with inclusion criteria for Primary Taxonomy assigns with UNIVERSAL Catalog when the from catalog is ALTAMED", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"US"})
	public void verify_rule_with_inclusive_creteria_of_primary_taxonomy(Map<String, String> map){
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Special Markets User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Special Markets User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("Classificationtab"));

		primarytaxonomy = classificationpage.getPrimaryTaxonomy(map.get("Primary"));
		Assertions.assertThat(primarytaxonomy).isEqualTo(map.get("PrimaryTaxonomy"));
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("TabName"));
		catalogtype.clickUserDriven().selectUserDriven(map.get("UserDriven"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		rule_driven = catalogtype.getRuleDrivenCatalogs();
		Assertions.assertThat(rule_driven).containsAnyOf(map.get("RuleDriven"));

		//verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).getRuleNamesByRuleStatus(map.get("Status"));
		List<String> myList = new ArrayList<String>(Arrays.asList(map.get("CatalogEntityRule").split(",")));
		Assertions.assertThat(catalogentityrule).isEqualTo(myList);

		//verify in master
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog"))
		.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO,"Navigate to Classification tab from Queries Menu for Master Catalog");
		primarytaxonomy = classificationpage.getPrimaryTaxonomy(map.get("Primary"));
		Assertions.assertThat(primarytaxonomy).isEqualTo(map.get("PrimaryTaxonomy"));
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("TabName"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		List<String> masterRuledriven = catalogtype.getRuleDrivenCatalogs();
		Assertions.assertThat(rule_driven).containsAll(masterRuledriven); // verify that rule driven for US and master are same or not
		pimHomepage.clickLogoutButton();
	}

	//CATR_92
	@PimFrameworkAnnotation(module = Modules.CLASSIFICATION, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Inclusive_Catalog_rule)
	@Test(description = "CATR_92: Verify whether the rule with inclusion criteria for Primary Taxonomy does not assign with UNIVERSAL when the from catalog is ALTAMED", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"US"})
	public void verify_rule_driven_not_assign_for_primary_taxonomy(Map<String, String> map) throws Throwable{
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Special Markets User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Special Markets User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("Classificationtab"));

		primarytaxonomy = classificationpage.getPrimaryTaxonomy(map.get("Primary"));
		Assertions.assertThat(primarytaxonomy).isNotEqualTo(map.get("PrimaryTaxonomy"));
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("TabName"));
		catalogtype.clickUserDriven().selectUserDriven(map.get("UserDriven"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		rule_driven = catalogtype.getRuleDrivenCatalogs();
		Assertions.assertThat(rule_driven).doesNotContain(map.get("RuleDriven"));
		//verify in master
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog"))
		.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO,"Navigate to Classification tab from Queries Menu for Master Catalog");
		primarytaxonomy = classificationpage.getPrimaryTaxonomy(map.get("Primary"));
		Assertions.assertThat(primarytaxonomy).isEqualTo(map.get("PrimaryTaxonomy"));
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("TabName"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		List<String> masterRuledriven = catalogtype.getRuleDrivenCatalogs();
		Assertions.assertThat(masterRuledriven).doesNotContain(map.get("RuleDriven")); // verify that rule driven for US and master are same or not
		pimHomepage.clickLogoutButton();

	}

	//CATR_093
	@PimFrameworkAnnotation(module = Modules.CLASSIFICATION, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Inclusive_Catalog_rule)
	@Test(description = "CATR_093: Verify whether the rule with inclusion criteria for Ecommerce Taxonomy assigns with ALTAMED Catalog when the from catalog is APPLEVALLY", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"US"})
	public void verify_rule_with_inclusive_creteria_of_ecommerce_taxonomy(Map<String, String> map){
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Special Markets User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Special Markets User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("Classificationtab"));

		ecommercetaxonomy = classificationpage.getEcommerceTaxonomy(map.get("E-Commerce"));
		Assertions.assertThat(ecommercetaxonomy).isEqualTo(map.get("EcommerceTaxonomy"));
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("TabName"));
		catalogtype.clickUserDriven().selectUserDriven(map.get("UserDriven"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		rule_driven = catalogtype.getRuleDrivenCatalogs();
		Assertions.assertThat(rule_driven).containsAnyOf(map.get("RuleDriven"));
		//verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).getRuleNamesByRuleStatus(map.get("Status"));
		List<String> myList = new ArrayList<String>(Arrays.asList(map.get("CatalogEntityRule").split(",")));
		Assertions.assertThat(catalogentityrule).isEqualTo(myList);

		//verify in master
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog"))
		.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO,"Navigate to Classification tab from Queries Menu for Master Catalog");
		ecommercetaxonomy = classificationpage.getPrimaryTaxonomy(map.get("Primary"));
		Assertions.assertThat(ecommercetaxonomy).isEqualTo(map.get("EcommerceTaxonomy"));
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("TabName"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		List<String> masterRuledriven = catalogtype.getRuleDrivenCatalogs();
		Assertions.assertThat(rule_driven).containsAll(masterRuledriven);

	}

	//CATR_094
	@PimFrameworkAnnotation(module = Modules.CLASSIFICATION, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Inclusive_Catalog_rule)
	@Test(description = "CATR_094: Verify the rule with inclusion criteria for Brand Code assigns with GREENDENT when the from catalog is MYFAM", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"US"})
	public void verify_rule_with_inclusive_creteria_for_brand_code(Map<String, String> map){
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Special Markets User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Special Markets User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));

		suppliercode = globalattribute.getSuppliercode();
		Assertions.assertThat(suppliercode).isEqualTo(map.get("BrandCode"));
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("TabName"));
		catalogtype.clickUserDriven().selectUserDriven(map.get("UserDriven"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		rule_driven = catalogtype.getRuleDrivenCatalogs();
		Assertions.assertThat(rule_driven).containsAnyOf(map.get("RuleDriven"));

		//verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).getRuleNamesByRuleStatus(map.get("Status"));
		List<String> myList = new ArrayList<String>(Arrays.asList(map.get("CatalogEntityRule").split(",")));
		Assertions.assertThat(catalogentityrule).isEqualTo(myList);

		//verify in master
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog"))
		.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
		log(LogType.INFO,"Navigate to Classification tab from Queries Menu for Master Catalog");
		suppliercode = globalattribute.getSuppliercode();
		Assertions.assertThat(suppliercode).isEqualTo(map.get("BrandCode"));
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("TabName"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		List<String> masterRuledriven = catalogtype.getRuleDrivenCatalogs();
		Assertions.assertThat(rule_driven).containsAll(masterRuledriven);

	}

	//CATR_095
	@PimFrameworkAnnotation(module = Modules.CLASSIFICATION, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Inclusive_Catalog_rule)
	@Test(description = "CATR_095: Verify the rule with inclusion criteria for  Brand Code should not assigns with REMINGTON when the from catalog is PUGET.", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"US"})
	public void verify_rule_driven_not_assigned_for_brand_code(Map<String, String> map){
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Special Markets User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Special Markets User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));

		suppliercode = globalattribute.getSuppliercode();
		Assertions.assertThat(suppliercode).isNotEqualTo(map.get("BrandCode"));

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("TabName"));
		catalogtype.clickUserDriven().selectUserDriven(map.get("UserDriven"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		rule_driven = catalogtype.getRuleDrivenCatalogs();
		Assertions.assertThat(rule_driven).doesNotContain(map.get("RuleDriven"));

		//verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).getRuleNamesByRuleStatus(map.get("Status"));
		List<String> myList = new ArrayList<String>(Arrays.asList(map.get("CatalogEntityRule").split(",")));
		Assertions.assertThat(catalogentityrule).isEqualTo(myList);

		//verify in master
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog"))
		.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
		log(LogType.INFO,"Navigate to Classification tab from Queries Menu for Master Catalog");
		suppliercode = globalattribute.getSuppliercode();
		Assertions.assertThat(suppliercode).isNotEqualTo(map.get("BrandCode"));
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("TabName"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		List<String> masterRuledriven = catalogtype.getRuleDrivenCatalogs();
		Assertions.assertThat(masterRuledriven).doesNotContain(map.get("RuleDriven")); // verify that rule driven for US and master are same or not
		pimHomepage.clickLogoutButton();
	}

	//CATR_096
	@PimFrameworkAnnotation(module = Modules.CLASSIFICATION, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Inclusive_Catalog_rule)
	@Test(description = "CATR_096: Verify the rule with inclusion criteria for  Supplier assigns with PUGET when the from Catalog is UNIVERSAL.", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"US"})
	public void verify_rule_with_inclusive_creteria_for_supplier_code(Map<String, String> map){
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Special Markets User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Special Markets User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));

		suppliercode = globalattribute.getSuppliercode();
		Assertions.assertThat(suppliercode).isEqualTo(map.get("Supplier"));
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("TabName"));
		catalogtype.clickUserDriven().selectUserDriven(map.get("UserDriven"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		rule_driven = catalogtype.getRuleDrivenCatalogs();
		Assertions.assertThat(rule_driven).containsAnyOf(map.get("RuleDriven"));

		//verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).getRuleNamesByRuleStatus(map.get("Status"));
		System.out.print(catalogentityrule);
		List<String> myList = new ArrayList<String>(Arrays.asList(map.get("CatalogEntityRule").split(",")));
		Assertions.assertThat(catalogentityrule).isEqualTo(myList);

		//verify in master
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog"))
		.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
		log(LogType.INFO,"Navigate to Global Attribute tab from Queries Menu for Master Catalog");
		suppliercode = globalattribute.getSuppliercode();
		Assertions.assertThat(suppliercode).isEqualTo(map.get("Supplier"));
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("TabName"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		List<String> masterRuledriven = catalogtype.getRuleDrivenCatalogs();
		Assertions.assertThat(rule_driven).containsAll(masterRuledriven);
		pimHomepage.clickLogoutButton();
	}

	//CATR_097
	@PimFrameworkAnnotation(module = Modules.CLASSIFICATION, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Inclusive_Catalog_rule)
	@Test(description = "CATR_097: Verify the rule with inclusion criteria for Manufacturer assigns VALLEYDTL when the from catalog is APPLEVALLY", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"US"})
	public void verify_rule_driven_assigned_for_manufacturer_code_with_inclusive_criteria(Map<String, String> map){
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
		Assertions.assertThat(manufacturecode).isEqualTo(map.get("ManufactureCode"));
		log(LogType.INFO,"Verifying manufacturer code");
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("TabName"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		List<String> masterRuledriven = catalogtype.getRuleDrivenCatalogs();
		Assertions.assertThat(rule_driven).containsAll(masterRuledriven);
		Assertions.assertThat(masterRuledriven).contains(map.get("RuleDriven"));
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType"))
		.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
		catalogtype.clearSelectedFields(map.get("UserDriven"));
		log(LogType.INFO,"Deleting the Userdriven");
		pimHomepage.clickLogoutButton();
	}

	//CATR_111
	@PimFrameworkAnnotation(module = Modules.CLASSIFICATION, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Inclusive_Catalog_rule)
	@Test(description = "CATR_111: Verify the rule with inclusion criteria for  Classification assigns MYFAM when the from catalog is GREENDENT", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"US"})
	public void verify_rule_driven_assigned_with_inclusive_criteria_for_class_item_code(Map<String, String> map){
		//data not prepared CATR_111
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Special Markets User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Special Markets User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
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
		System.out.print(catalogentityrule);
		List<String> myList = new ArrayList<String>(Arrays.asList(map.get("CatalogEntityRule").split(",")));
		Assertions.assertThat(catalogentityrule).isEqualTo(myList);
		log(LogType.INFO,"Verifying catalog entity rule");

		//verify in master
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog"))
		.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
		log(LogType.INFO,"Navigate to Classification tab from Queries Menu for Master Catalog");		
		clasificationcode = globalattribute.getClassificationCode();
		Assertions.assertThat(clasificationcode).isEqualTo(map.get("ManufactureCode"));
		log(LogType.INFO,"Verifying classification code");
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

	//CATR_112
	@PimFrameworkAnnotation(module = Modules.CLASSIFICATION, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Inclusive_Catalog_rule)
	@Test(description = "CATR_112: Verify the rule with inclusion criteria for  Classification should not assigns MYFAM when the from catalog", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"US"})
	public void verify_rule_driven_not_assigned_with_inclusive_criteria_for_class_item_code(Map<String, String> map){
		//data not prepared CATR_112
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Special Markets User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Special Markets User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
		log(LogType.INFO,"Navigate to Global Attribute tab from Queries Menu for US Catalog");

		clasificationcode = globalattribute.getClassificationCode();
		Assertions.assertThat(clasificationcode).isEqualTo(map.get("Classification"));
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
		System.out.print(catalogentityrule);
		List<String> myList = new ArrayList<String>(Arrays.asList(map.get("CatalogEntityRule").split(",")));
		Assertions.assertThat(catalogentityrule).isEqualTo(myList);
		log(LogType.INFO,"Verifying catalog entity rule");

		//verify in master
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog"))
		.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
		log(LogType.INFO,"Navigate to Classification tab from Queries Menu for Master Catalog");		
		clasificationcode = globalattribute.getClassificationCode();
		Assertions.assertThat(clasificationcode).isEqualTo(map.get("ManufactureCode"));
		log(LogType.INFO,"Verifying classification code");
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
