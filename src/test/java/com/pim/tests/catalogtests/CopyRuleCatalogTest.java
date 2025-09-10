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
import com.pim.pages.AllCatalogsPage;
import com.pim.pages.BasePage;
import com.pim.pages.CatalogTypePage;
import com.pim.pages.GlobalAttributePage;
import com.pim.pages.LoginPage;
import com.pim.pages.PimHomepage;
import com.pim.pages.ProductDetailSearchPage;
import com.pim.pages.QualityStatusPage;
import com.pim.tests.BaseTest;
import com.pim.utils.DataProviderUtils;
import com.pim.utils.ExcelUtils;
import com.pim.utils.Javautils;

public class CopyRuleCatalogTest extends BaseTest{

	CatalogTypePage catalogTypePage = new CatalogTypePage();
	ProductDetailSearchPage productdetails = new ProductDetailSearchPage();
	AllCatalogsPage allcatalog = new AllCatalogsPage();
	QualityStatusPage qualitystatus = new QualityStatusPage();
	Javautils javautils = new Javautils();
	GlobalAttributePage globalattribute = new GlobalAttributePage();

	//List<String> rule_driven;
	//List<String> multiple_rule_driven;
	//List<String> all_catalog_special_markets;
	//List<String> all_catalog_master_special_markets;
	String manufacturecode;

	//Verification of Copy Rules Functionality in Special Markets Division when assign DENTAL in dental division TCID - CATR_136 to CATR_139
	@PimFrameworkAnnotation(module = Modules.SPECIAL_MARKET_CATALOG, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.CopyRuleCatalog)
	@Test(description = "CATR_136, CATR_137, CATR_138, CATR_139 | Verify whether the catalog COMFORTDTL gets appended to the target division based on the Special Markets Copy Rule for CAGA", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"US","pim","SPECIAL_MARKET_CATALOG"})
	public void verify_catalog_get_appended_to_the_target_division_based_on_special_market_copy_rule_when_assign_DENTAL(Map<String, String> map){
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("DentalTab"));
		log(LogType.INFO,"Navigate to Dental tab from Queries Menu for US Catalog");

		catalogTypePage.clickUserDriven().clearUserDriven(map.get("userDriven")).clickUserDriven().selectUserDriven(map.get("userDriven"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		log(LogType.INFO,"Assigning userdriven in Dental Division");

		productdetails.selectTabfromDropdown(map.get("SpecialMarketTab")).clickRefreshIcon();
		log(LogType.INFO,"Navigate to Special markets tab from Queries Menu for US Catalog");
		List<String> rule_driven = catalogTypePage.getRuleDrivenCatalogs();

		List<String> multiple_rule_driven = javautils.readMultipleValuesFromExcel(map.get("RuleDriven"));
		for(String ruledriven : multiple_rule_driven) 
			Assertions.assertThat(rule_driven).containsAnyOf(ruledriven);

		productdetails.selectTabfromDropdown(map.get("AllCatalogtab")).clickRefreshIcon();
		log(LogType.INFO,"Navigate to All Catalog tab from Queries Menu for US Catalog");
		List<String> all_catalog_special_markets = allcatalog.getAllSpecialMarketsCatalogList();

		for(String allcatalog : multiple_rule_driven)
			Assertions.assertThat(all_catalog_special_markets).containsAnyOf(allcatalog);

		//verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
		for (String Rules:javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule")))
		{
			Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);	
		}
		qualitystatus.minimizeQualityStatusTab();
		log(LogType.INFO,"Verifying catalog entity rule");

		//verify in master
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog"))
		.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("AllCatalogtab")).clickRefreshIcon();
		List<String> all_catalog_master_special_markets = allcatalog.getAllSpecialMarketsCatalogList();

		for(String masterallcatalog : multiple_rule_driven)
			Assertions.assertThat(all_catalog_master_special_markets).containsAnyOf(masterallcatalog);

		//delete assigned userdriven
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType"))
		.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("DentalTab"));
		catalogTypePage.clearSelectedFields(map.get("userDriven"));
		log(LogType.INFO,"Deleting the Userdriven");
		pimHomepage.clickLogoutButton();
	}

	//Verification of Copy Rules Functionality in Special Markets Division when assign MEDICAL in medical division TCID - CATR_140
	@PimFrameworkAnnotation(module = Modules.SPECIAL_MARKET_CATALOG, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.CopyRuleCatalog)
	@Test(description = "CATR_140 | Verify whether the catalog PRIME gets appended to the target division based on the Special Markets Copy Rule for MEDICAL", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"US","pim","SPECIAL_MARKET_CATALOG"})
	public void verify_catalog_get_appended_to_the_target_division_based_on_special_market_copy_rule_when_assign_MEDICAL(Map<String, String> map){
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("MedicalTab"));
		log(LogType.INFO,"Navigate to Medical Division from Queries Menu for US Catalog");

		catalogTypePage.clickUserDriven().clearUserDriven(map.get("userDriven")).clickUserDriven().selectUserDriven(map.get("userDriven"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		log(LogType.INFO,"Assigning userdriven in Medical Division");

		productdetails.selectTabfromDropdown(map.get("SpecialMarketTab")).clickRefreshIcon();
		log(LogType.INFO,"Navigate to Special markets tab from Queries Menu for US Catalog");
		List<String> rule_driven = catalogTypePage.getRuleDrivenCatalogs();

		List<String> multiple_rule_driven = javautils.readMultipleValuesFromExcel(map.get("RuleDriven"));
		for(String ruledriven : multiple_rule_driven) 
			Assertions.assertThat(rule_driven).containsAnyOf(ruledriven);

		productdetails.selectTabfromDropdown(map.get("AllCatalogtab")).clickRefreshIcon();
		log(LogType.INFO,"Navigate to All Catalog tab from Queries Menu for US Catalog");
		List<String> all_catalog_special_markets = allcatalog.getAllSpecialMarketsCatalogList();

		for(String allcatalog : multiple_rule_driven)
			Assertions.assertThat(all_catalog_special_markets).containsAnyOf(allcatalog);

		//verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
		for (String Rules:javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule")))
		{
			Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);	
		}
		qualitystatus.minimizeQualityStatusTab();
		log(LogType.INFO,"Verifying catalog entity rule");

		//verify in master
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog"))
		.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("AllCatalogtab")).clickRefreshIcon();
		List<String> all_catalog_master_special_markets = allcatalog.getAllSpecialMarketsCatalogList();

		for(String masterallcatalog : multiple_rule_driven)
			Assertions.assertThat(all_catalog_master_special_markets).containsAnyOf(masterallcatalog);

		//delete assigned userdriven
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType"))
		.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("MedicalTab"));
		catalogTypePage.clearSelectedFields(map.get("userDriven"));
		log(LogType.INFO,"Deleting the Userdriven");
		pimHomepage.clickLogoutButton();
	}

	//Verification of Copy Rules Functionality in Special Markets Division when assign DENTAL in dental division for ~ex manufacturer code TCID - CATR_141 and CATR_142
	@PimFrameworkAnnotation(module = Modules.SPECIAL_MARKET_CATALOG, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.CopyRuleCatalog)
	@Test(description = "CATR_141 and CATR_142 | Verify whether the catalog FEDERAL gets appended to the target division based on the Special Markets Copy Rule for PRIME", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"US","pim","SPECIAL_MARKET_CATALOG"})
	public void verify_catalog_get_appended_to_the_target_division_based_on_special_market_copy_rule_when_assign_DENTAL_for_exclusive_manufacturer_code(Map<String, String> map){
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
		log(LogType.INFO,"Navigate to Global Attribute tab from Queries Menu for US Catalog");
		
		manufacturecode = globalattribute.getManufacturercode();
		for(String code: javautils.readMultipleValuesFromExcel(map.get("ManufactureCode"))) {
			Assertions.assertThat(manufacturecode).isNotEqualTo(code);
		}
		log(LogType.INFO,"Verifying manufacturer code");

		productdetails.selectTabfromDropdown(map.get("DentalTab"));
		catalogTypePage.clickUserDriven().clearUserDriven(map.get("userDriven")).clickUserDriven().selectUserDriven(map.get("userDriven"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		log(LogType.INFO,"Assigning userdriven in Dental Division");

		productdetails.selectTabfromDropdown(map.get("SpecialMarketTab")).clickRefreshIcon();
		log(LogType.INFO,"Navigate to Special markets tab from Queries Menu for US Catalog");
		List<String> rule_driven = catalogTypePage.getRuleDrivenCatalogs();

		List<String> multiple_rule_driven = javautils.readMultipleValuesFromExcel(map.get("RuleDriven"));
		for(String ruledriven : multiple_rule_driven) 
			Assertions.assertThat(rule_driven).containsAnyOf(ruledriven);

		productdetails.selectTabfromDropdown(map.get("AllCatalogtab")).clickRefreshIcon();
		log(LogType.INFO,"Navigate to All Catalog tab from Queries Menu for US Catalog");
		List<String> all_catalog_special_markets = allcatalog.getAllSpecialMarketsCatalogList();

		for(String allcatalog : multiple_rule_driven)
			Assertions.assertThat(all_catalog_special_markets).containsAnyOf(allcatalog);

		//verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
		for (String Rules:javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule")))
		{
			Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);	
		}
		qualitystatus.minimizeQualityStatusTab();
		log(LogType.INFO,"Verifying catalog entity rule");

		//verify in master
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog"))
		.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("AllCatalogtab")).clickRefreshIcon();

		List<String> all_catalog_master_special_markets = allcatalog.getAllSpecialMarketsCatalogList();

		for(String masterallcatalog : multiple_rule_driven)
			Assertions.assertThat(all_catalog_master_special_markets).containsAnyOf(masterallcatalog);

		//delete assigned userdriven
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType"))
		.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("DentalTab"));
		catalogTypePage.clearSelectedFields(map.get("userDriven"));
		log(LogType.INFO,"Deleting the Userdriven");
		pimHomepage.clickLogoutButton();

	}




}
