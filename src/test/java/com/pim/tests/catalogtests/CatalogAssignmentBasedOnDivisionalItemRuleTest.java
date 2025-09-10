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
import com.pim.pages.BasePage;
import com.pim.pages.CatalogTypePage;
import com.pim.pages.LoginPage;
import com.pim.pages.PimHomepage;
import com.pim.pages.ProductDetailSearchPage;
import com.pim.pages.QualityStatusPage;
import com.pim.pages.ReferencesPage;
import com.pim.tests.BaseTest;
import com.pim.utils.DataProviderUtils;
import com.pim.utils.ExcelUtils;
import com.pim.utils.Javautils;

public class CatalogAssignmentBasedOnDivisionalItemRuleTest extends BaseTest {
	ReferencesPage referencepage = new ReferencesPage();
	ProductDetailSearchPage productdetails = new ProductDetailSearchPage();
	CatalogTypePage catalogTypePage = new CatalogTypePage();
	QualityStatusPage qualitystatus = new QualityStatusPage();
	Javautils javautils = new Javautils();
	List<String> rule_driven;
	List<String> master_rule_driven;
	List<String> multiple_reference_type;

	//Verification of Catalog assignment for Medical Division based on the Divisional Item Rule  TCID - CATR_068, CATR_069 and CATR_070
	@PimFrameworkAnnotation(module = Modules.MEDICAL_CATALOG, category = CategoryType.SMOKE)
	@TestDataSheet(sheetname = TestCaseSheet.CopyRuleCatalog)
	@Test(description = "CATR_068, CATR_069 and CATR_070 | Verify MEDICAL catalog is assigned in Rule Driven Field, if the parent has child of subtype as GE, SP, A3  --CATR_068, CATR_069 and CATR_070", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, 
		groups = {"SMOKE","US","pim","MEDICAL_CATALOG"})
	public void verify_medical_catalog_assigned_when_reference_type_will_GE_SP_A3(Map<String, String> map){
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("ReferenceTab"));
		log(LogType.INFO,"Navigate to References tab from Queries Menu for US Catalog");

		//verifying reference type of child item
		List<String> referencetype = referencepage.getReferenceTypeInList();
		Assertions.assertThat(referencetype).containsAnyOf((map.get("ReferencesType")));
		//Assertions.assertThat(referencetype).isEqualTo(map.get("ReferencesType"));
		log(LogType.INFO,"Verifying reference type of child item");

		productdetails.selectTabfromDropdown(map.get("MedicalTab")).clickRefreshIcon();
		log(LogType.INFO,"Navigate to MedicalTab tab from Queries Menu for US Catalog");
		BasePage.WaitForMiliSec(5000);
		rule_driven = catalogTypePage.getRuleDrivenCatalogs();
		Assertions.assertThat(rule_driven).containsAnyOf(map.get("RuleDriven"));

		//verify in master
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog"))
		.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("MedicalTab")).clickRefreshIcon();
		log(LogType.INFO,"Navigate to Master Catalog Medical division");
		BasePage.WaitForMiliSec(5000);
		master_rule_driven = catalogTypePage.getRuleDrivenCatalogs();
		Assertions.assertThat(master_rule_driven).containsAnyOf(map.get("RuleDriven"));
		pimHomepage.clickLogoutButton();
	}


	//Verification of Catalog assignment for Medical Division based on the Divisional Item Rule  TCID - CATR_071
	@PimFrameworkAnnotation(module = Modules.MEDICAL_CATALOG, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.CopyRuleCatalog)
	@Test(description = "CATR_071 | Verify MEDICAL catalog is not assigned to Rule Driven Field of Parent, if the Parent does not have child of Sub type = GE/SP/A3", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"US","pim","MEDICAL_CATALOG"})
	public void verify_medical_catalog_not_assigned_when_reference_type_will_not_equal_to_GE_SP_A3(Map<String, String> map){
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("ReferenceTab"));
		log(LogType.INFO,"Navigate to References tab from Queries Menu for US Catalog");

		//verifying reference type of child item
		String actualreferencetype = referencepage.getReferenceType();
		multiple_reference_type = javautils.readMultipleValuesFromExcel(map.get("ReferencesType"));
		for(String referencetype : multiple_reference_type) 
			Assertions.assertThat(actualreferencetype).isNotEqualTo(referencetype);
		log(LogType.INFO,"Verifying reference type of child item");

		productdetails.selectTabfromDropdown(map.get("MedicalTab")).clickRefreshIcon();
		log(LogType.INFO,"Navigate to MedicalTab tab from Queries Menu for US Catalog");

		rule_driven = catalogTypePage.getRuleDrivenCatalogs();
		Assertions.assertThat(rule_driven).doesNotContain(map.get("RuleDriven"));

		//verify in master
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog"))
		.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("MedicalTab")).clickRefreshIcon();
		log(LogType.INFO,"Navigate to Master Catalog Medical division");

		master_rule_driven = catalogTypePage.getRuleDrivenCatalogs();
		Assertions.assertThat(master_rule_driven).doesNotContain(map.get("RuleDriven"));
		pimHomepage.clickLogoutButton();
	}

	//Verification of Catalog assignment for Medical Division based on the Divisional Item Rule  TCID - CATR_072
	@PimFrameworkAnnotation(module = Modules.MEDICAL_CATALOG, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.CopyRuleCatalog)
	@Test(description = "CATR_072 | Verify MEDICAL catalog is not assigned to Rule Driven Field of Parent, if the Parent does not have any sub types.", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"US","pim","MEDICAL_CATALOG"})
	public void verify_medical_catalog_not_assigned_when_there_is_no_reference_type(Map<String, String> map){
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("ReferenceTab"));
		log(LogType.INFO,"Navigate to References tab from Queries Menu for US Catalog");

		//verify reference type is not present
		verifyReferenceTypeIsNotPresent();

		productdetails.selectTabfromDropdown(map.get("MedicalTab")).clickRefreshIcon();
		log(LogType.INFO,"Navigate to MedicalTab tab from Queries Menu for US Catalog");

		rule_driven = catalogTypePage.getRuleDrivenCatalogs();
		Assertions.assertThat(rule_driven).doesNotContain(map.get("RuleDriven"));

		//verify in master
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog"))
		.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("MedicalTab")).clickRefreshIcon();
		log(LogType.INFO,"Navigate to Master Catalog Medical division");

		master_rule_driven = catalogTypePage.getRuleDrivenCatalogs();
		Assertions.assertThat(master_rule_driven).doesNotContain(map.get("RuleDriven"));
		pimHomepage.clickLogoutButton();
	}

	public void verifyReferenceTypeIsNotPresent() {
		Assertions.assertThat(referencepage.isReferenceTypeVisible("referenceType")).isFalse();
	}

	//Verification of Catalog assignment for Other Divisions based on the Divisional Item Rule TCID - CATR_074, CATR_075 and CATR_076
	@PimFrameworkAnnotation(module = Modules.MEDICAL_CATALOG, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.CopyRuleCatalog)
	@Test(description = "CATR_074, CATR_075 and CATR_076 | Verify MEDICAL catalog is NOT assigned in DENTAL Rule Driven Field, if the parent has child of subtype as either \"GE\" or \"SP\" or \"A3\"", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"US","pim","MEDICAL_CATALOG"})
	public void verify_medical_catalog_not_assigned_in_DENTAL_ZAHN_SPECIAL_MARKETS_rule_driven_when_reference_type_is_GE_SP_A3(Map<String, String> map){
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("ReferenceTab"));
		log(LogType.INFO,"Navigate to References tab from Queries Menu for US Catalog");

		//verifying reference type of child item
		List<String> referencetype = referencepage.getReferenceTypeInList();
		//Assertions.assertThat(referencetype).isEqualTo(map.get("ReferencesType"));
		Assertions.assertThat(referencetype).containsAnyOf((map.get("ReferencesType")));
		log(LogType.INFO,"Verifying reference type of child item");

		//Verify medical is not present in dental division
		productdetails.selectTabfromDropdown(map.get("DentalTab")).clickRefreshIcon();
		log(LogType.INFO,"Navigate to DentalTab tab from Queries Menu for US Catalog");
		rule_driven = catalogTypePage.getRuleDrivenCatalogs();
		Assertions.assertThat(rule_driven).doesNotContain(map.get("RuleDriven"));

		//verify medical is not present in Zahn division
		productdetails.selectTabfromDropdown(map.get("ZahnTab")).clickRefreshIcon();
		log(LogType.INFO,"Navigate to Zahn tab from Queries Menu for US Catalog");

		rule_driven = catalogTypePage.getRuleDrivenCatalogs();
		Assertions.assertThat(rule_driven).doesNotContain(map.get("RuleDriven"));

		//verfiy medical is not present in special markets division
		productdetails.selectTabfromDropdown(map.get("SpecialMarketTab")).clickRefreshIcon();
		log(LogType.INFO,"Navigate to Special Markets Tab tab from Queries Menu for US Catalog");
		rule_driven = catalogTypePage.getRuleDrivenCatalogs();
		Assertions.assertThat(rule_driven).doesNotContain(map.get("RuleDriven"));

		//verify quality status
		/*productdetails.selectTabfromDropdown(map.get("Qualitystatustab"));
		List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).getRuleNamesByRuleStatus(map.get("Status"));
		List<String> myList = new ArrayList<String>(Arrays.asList(map.get("CatalogEntityRule").split(",")));
		Assertions.assertThat(catalogentityrule).isEqualTo(myList);
		log(LogType.INFO,"Verifying catalog entity rule");*/

		//verify in master
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog"))
		.clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("DentalTab")).clickRefreshIcon();
		log(LogType.INFO,"Navigate to Master Catalog Dental division");

		List<String> dental_master_rule_driven = catalogTypePage.getRuleDrivenCatalogs();
		Assertions.assertThat(dental_master_rule_driven).doesNotContain(map.get("RuleDriven"));

		//navigate to zahn division in Master catalog
		productdetails.selectTabfromDropdown(map.get("ZahnTab")).clickRefreshIcon();
		log(LogType.INFO,"Navigate to Master Catalog Zahn division");

		List<String> zahn_master_rule_driven = catalogTypePage.getRuleDrivenCatalogs();
		Assertions.assertThat(zahn_master_rule_driven).doesNotContain(map.get("RuleDriven"));

		//navigate to special markets division in Master catalog
		productdetails.selectTabfromDropdown(map.get("SpecialMarketTab")).clickRefreshIcon();
		log(LogType.INFO,"Navigate to Master Catalog special markets division");

		List<String> sm_master_rule_driven = catalogTypePage.getRuleDrivenCatalogs();
		Assertions.assertThat(sm_master_rule_driven).doesNotContain(map.get("RuleDriven"));

		pimHomepage.clickLogoutButton();
	}



}
