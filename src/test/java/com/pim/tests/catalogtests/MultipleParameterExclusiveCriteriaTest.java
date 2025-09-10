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

public class MultipleParameterExclusiveCriteriaTest extends BaseTest{
	
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
	
	//CATR_118
	@PimFrameworkAnnotation(module = Modules.CLASSIFICATION, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Inclusive_Catalog_rule)
	@Test(description = "CATR_118 | Verify the rule with exclusion criteria for Primary Taxonomy and Brand assigns ALTAMED , when the from catalog is MYFAM", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"US"})
	public void verify_rule_driven_assigned_for_primary_and_brand_code_with_exclusive_criteria(Map<String, String> map){
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
		Assertions.assertThat(suppliercode).isEqualTo(map.get("BrandCode"));
		log(LogType.INFO,"Verifying Brand code");
		
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("TabName"));
		catalogtype.clickUserDriven().selectUserDriven(map.get("UserDriven"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		rule_driven = catalogtype.getRuleDrivenCatalogs();
		Assertions.assertThat(rule_driven).contains(map.get("RuleDriven"));
		log(LogType.INFO,"Verifying rule driven catalog");
		
		//verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).getRuleNamesByRuleStatus(map.get("Status"));
		List<String> myList = new ArrayList<String>(Arrays.asList(map.get("CatalogEntityRule").split(",")));
		Assertions.assertThat(catalogentityrule).isEqualTo(myList);
		log(LogType.INFO,"Verifying catalog entity rule");
		
		//verify in master
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog"))
		.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("Classification"));
		log(LogType.INFO,"Navigate to Classification tab from Queries Menu for US Catalog");
						
		primarytaxonomy = classificationpage.getPrimaryTaxonomy(map.get("Primary"));
		Assertions.assertThat(primarytaxonomy).isEqualTo(map.get("PrimaryTaxonomy"));
		log(LogType.INFO,"Verifying primary code");
				
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("GlobalAttributeTab"));
		log(LogType.INFO,"Navigate to Global Attribute tab from Queries Menu for US Catalog");
		
		suppliercode = globalattribute.getSuppliercode();
		Assertions.assertThat(suppliercode).isEqualTo(map.get("BrandCode"));
		log(LogType.INFO,"Verifying Brand code");
		
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("TabName"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		List<String> masterRuledriven = catalogtype.getRuleDrivenCatalogs();
		Assertions.assertThat(rule_driven).containsAll(masterRuledriven);
		Assertions.assertThat(masterRuledriven).contains(map.get("RuleDriven"));
		log(LogType.INFO,"Verifying rule driven catalog in master");
		
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType"))
		.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
		catalogtype.clearSelectedFields(map.get("UserDriven"));
		log(LogType.INFO,"Deleting the Userdriven");
		pimHomepage.clickLogoutButton();
	}
	

}
