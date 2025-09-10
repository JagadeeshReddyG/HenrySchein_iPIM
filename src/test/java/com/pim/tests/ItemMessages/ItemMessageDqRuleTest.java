package com.pim.tests.ItemMessages;

import com.pim.annotations.PimFrameworkAnnotation;
import com.pim.annotations.TestDataSheet;
import com.pim.enums.CategoryType;
import com.pim.enums.LogType;
import com.pim.enums.Modules;
import com.pim.enums.TestCaseSheet;
import com.pim.pages.ActionsPage;
import com.pim.pages.BasePage;
import com.pim.pages.ClassificationsPage;
import com.pim.pages.FieldSelectionPage;
import com.pim.pages.GlobalAttributePage;
import com.pim.pages.LoginPage;
import com.pim.pages.PimHomepage;
import com.pim.pages.ProductDetailSearchPage;
import com.pim.pages.QualityStatusPage;
import com.pim.pages.QueriesSubMenu;
import com.pim.pages.StructureSubMenu;
import com.pim.pages.TextPage;
import com.pim.tests.BaseTest;
import com.pim.utils.DataProviderUtils;
import com.pim.utils.ExcelUtils;
import com.pim.utils.Javautils;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;
import java.util.Map;
import static com.pim.reports.FrameworkLogger.log;

public class ItemMessageDqRuleTest extends BaseTest {
	ActionsPage action = new ActionsPage();
	StructureSubMenu structuresubmenu = new StructureSubMenu();

    ProductDetailSearchPage productDetailSearchPage = new ProductDetailSearchPage();
    ClassificationsPage classificationpage = new ClassificationsPage();
    ActionsPage actionsPage = new ActionsPage();
	QualityStatusPage qualitystatus = new QualityStatusPage();
	ProductDetailSearchPage productdetail = new ProductDetailSearchPage();
	GlobalAttributePage globalattribute = new GlobalAttributePage();
	QueriesSubMenu queriessubmenu = new QueriesSubMenu();
	FieldSelectionPage fieldselectionpage = new FieldSelectionPage();
	Javautils javautil = new Javautils();
    TextPage textPage = new TextPage();
	String znewglobalerrormessage;
	String zcanadaglobalerrormessage;
	String hazardouscode;
	String classificationcode;
	String manufacturecode;
	String jdedescription;
	String linetypecode;
	String fdac;
	String supplierCode;

	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.Valid_Failure)
	@TestDataSheet(sheetname = TestCaseSheet.ItemMessage)
	@Test(description = "ITM_044, ITM_046, ITM_106, ITM_107, ITM_126, ITM_127 | Verifying Item Message based on Availability Code", groups = { "US",
			"pim","ITEM_MESSAGE" }, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verifyItemMessageforAvailabilityCode(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();

		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult();
		// verifying availability code
		String availabilitycode = productdetail.getItemField(map.get("fieldIndex"));
		Assertions.assertThat(availabilitycode).isEqualTo(map.get("AvailabilityCode"));
		log(LogType.INFO, "Verifying availability code");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO, "Navigate to Classification tab after verifying the availability code in US Catalog");

		classificationpage.clickOnExpandButton();
		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickStructureGroup();
		//classificationpage.removeButton();
		classificationpage.clickYes();
		
		// verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		qualitystatus.sortRulestByLastExecution();
		List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("ruleStatus"));
		Assertions.assertThat(catalogentityrule).containsAnyOf(map.get("CatalogEntityRule"));
		log(LogType.INFO, "Verifying catalog entity rule");
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickOnCloseExpandButton();
		// verify in master
		productdetail.clickMenuRefreshIcon();
		queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog"))
				.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult();
		// verifying availability code
		availabilitycode = productdetail.getItemField(map.get("fieldIndex"));
		Assertions.assertThat(availabilitycode).isEqualTo(map.get("AvailabilityCode"));
		log(LogType.INFO, "Verifying availability code");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO, "Navigate to Classification tab after verifying the availability code in Master Catalog");
		classificationpage.clickOnExpandButton();
		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickOnCloseExpandButton();
		pimHomepage.clickLogoutButton();
	}

	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemMessage)
	@Test(description = "ITM_118 | Verifying Item Message based on Availability Code for Special Marketse", groups = { "US",
	"pim","ITEM_MESSAGE" }, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)

	public void verifyItemMessageforAvailabilityCode_SpecialMarkets(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();

		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult();
		// verifying availability code
		String availabilitycode = productdetail.getItemField(map.get("fieldIndex"));
		Assertions.assertThat(availabilitycode).isEqualTo(map.get("AvailabilityCode"));
		log(LogType.INFO, "Verifying availability code");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO, "Navigate to Classification tab after verifying the availability code in US Catalog");

		classificationpage.clickOnExpandButton();
		znewglobalerrormessage = classificationpage.getStructureGroupHavingBlankSpace(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickStructureGroup();
		classificationpage.removeButton();
		classificationpage.clickYes();
		// verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		qualitystatus.sortRulestByLastExecution();
		List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("ruleStatus"));
		Assertions.assertThat(catalogentityrule).containsAnyOf(map.get("CatalogEntityRule"));
		log(LogType.INFO, "Verifying catalog entity rule");
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		znewglobalerrormessage = classificationpage.getStructureGroupHavingBlankSpace(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickOnCloseExpandButton();
		// verify in master
		productdetail.clickMenuRefreshIcon();
		queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog"))
				.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult();
		// verifying availability code
		availabilitycode = productdetail.getItemField(map.get("fieldIndex"));
		Assertions.assertThat(availabilitycode).isEqualTo(map.get("AvailabilityCode"));
		log(LogType.INFO, "Verifying availability code");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO, "Navigate to Classification tab after verifying the availability code in Master Catalog");
		classificationpage.clickOnExpandButton();
		znewglobalerrormessage = classificationpage.getStructureGroupHavingBlankSpace(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickOnCloseExpandButton();
		pimHomepage.clickLogoutButton();
	}

	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemMessage)
	@Test(description = "ITM_053, ITM_060, ITM_061, ITM_062, ITM_067, ITM_072, ITM_073, ITM_074, ITM_076, ITM_078, ITM_088, ITM_091 | Verifying Item Message based on Hazardous Code", groups = { "US",
			"pim","ITEM_MESSAGE" }, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verifyItemMessageforHazardousCode(Map<String, String> map) throws Throwable {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
		log(LogType.INFO, "Navigate to Global attribute tab to verify the hazardous code in US Catalog");

		// verifying hazardous code
		hazardouscode = globalattribute.getHazardousClassCode();
		Assertions.assertThat(hazardouscode).isEqualTo(map.get("HazardousCode"));

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO, "Navigate to Classification tab to verify the structure group");

		classificationpage.clickOnExpandButton();
		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickStructureGroup();
		classificationpage.removeButton();
		classificationpage.clickYes();
		// verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		qualitystatus.sortRulestByLastExecution();
		List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("ruleStatus"));
		Assertions.assertThat(catalogentityrule).containsAnyOf(map.get("CatalogEntityRule"));
		log(LogType.INFO, "Verifying catalog entity rule");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		log(LogType.INFO, "Navigate to Classification tab to verify the structure group");
		classificationpage.clickOnCloseExpandButton();

		// verify in master
		productdetail.clickMenuRefreshIcon();
		queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog"))
				.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult()
				.selectTabfromDropdown(map.get("GlobalAttributeTab"));
		log(LogType.INFO, "Navigate to Global attribute tab to verify the hazardous code in Master Catalog");

		// verifying hazardous code
		hazardouscode = globalattribute.getHazardousClassCode();
		Assertions.assertThat(hazardouscode).isEqualTo(map.get("HazardousCode"));
		log(LogType.INFO, "Verifying hazardous code");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO, "Navigate to Classification tab to verify the structure group in Master Catalog");
		classificationpage.clickOnExpandButton();
		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickOnCloseExpandButton();
		pimHomepage.clickLogoutButton();
	}

	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemMessage)
	@Test(description = "ITM_119 | Verifying Item Message based on Hazardous Code with Special Markets", groups = { "US",
			"pim","ITEM_MESSAGE" }, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verifyItemMessageforHazardousCode_SpecialMarkets(Map<String, String> map) throws Throwable {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
		log(LogType.INFO, "Navigate to Global attribute tab to verify the hazardous code in US Catalog");

		// verifying hazardous code
		hazardouscode = globalattribute.getHazardousClassCode();
		Assertions.assertThat(hazardouscode).isEqualTo(map.get("HazardousCode"));

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO, "Navigate to Classification tab to verify the structure group");

		classificationpage.clickOnExpandButton();
		znewglobalerrormessage = classificationpage.getStructureGroupHavingBlankSpace(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickStructureGroup();
		classificationpage.removeButton();
		classificationpage.clickYes();
		// verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		qualitystatus.sortRulestByLastExecution();
		List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("ruleStatus"));
		Assertions.assertThat(catalogentityrule).containsAnyOf(map.get("CatalogEntityRule"));
		log(LogType.INFO, "Verifying catalog entity rule");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		znewglobalerrormessage = classificationpage.getStructureGroupHavingBlankSpace(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		log(LogType.INFO, "Navigate to Classification tab to verify the structure group");
		classificationpage.clickOnCloseExpandButton();

		// verify in master
		productdetail.clickMenuRefreshIcon();
		queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog"))
				.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult()
				.selectTabfromDropdown(map.get("GlobalAttributeTab"));
		log(LogType.INFO, "Navigate to Global attribute tab to verify the hazardous code in Master Catalog");

		// verifying hazardous code
		hazardouscode = globalattribute.getHazardousClassCode();
		Assertions.assertThat(hazardouscode).isEqualTo(map.get("HazardousCode"));
		log(LogType.INFO, "Verifying hazardous code");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO, "Navigate to Classification tab to verify the structure group in Master Catalog");
		classificationpage.clickOnExpandButton();
		znewglobalerrormessage = classificationpage.getStructureGroupHavingBlankSpace(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickOnCloseExpandButton();
		pimHomepage.clickLogoutButton();
	}


	// ITM_104
	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.Valid_Failure)
	@TestDataSheet(sheetname = TestCaseSheet.ItemMessage)
	@Test(description = "ITM_104 | Verifying Item Message based on Classification Code", groups = { "US",
			"pim","ITEM_MESSAGE" }, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verifyItemMessageforClassificationCode(Map<String, String> map) throws Throwable {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
		// verifying classification code
		classificationcode = globalattribute.getClassificationCode();
		Assertions.assertThat(classificationcode).isEqualTo(map.get("ClassificationCode"));
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		classificationpage.clickOnExpandButton();

		// As per Manual Testing Team Medical_85 item messge we have to add and in DQ rule Medical_85 is not present

		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickStructureGroup();
		classificationpage.removeButton();
		classificationpage.clickYes();
		// verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		qualitystatus.sortRulestByLastExecution();
		List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("ruleStatus"));
		Assertions.assertThat(catalogentityrule).containsAnyOf(map.get("CatalogEntityRule"));
		log(LogType.INFO, "Verifying catalog entity rule");
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickOnCloseExpandButton();
		// verify in master
		productdetail.clickMenuRefreshIcon();
		queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog"))
				.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult()
				.selectTabfromDropdown(map.get("GlobalAttributeTab"));
		// verifying Classification code
		classificationcode = globalattribute.getClassificationCode();
		Assertions.assertThat(classificationcode).isEqualTo(map.get("ClassificationCode"));
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		classificationpage.clickOnExpandButton();
		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickOnCloseExpandButton();
		pimHomepage.clickLogoutButton();

	}

	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemMessage)
	@Test(description = "ITM_075, ITM_109, ITM_112 | Verifying Item Message based on Manufacture Code", groups = { "US",
			"pim","ITEM_MESSAGE" }, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verifyItemMessageforManufactureCode(Map<String, String> map) throws Throwable {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
		// verifying manufacture code
		manufacturecode = globalattribute.getManufacturercode();
		Assertions.assertThat(manufacturecode).isEqualTo(map.get("ManufactureCode"));
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		classificationpage.clickOnExpandButton();
		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickStructureGroup();
		classificationpage.removeButton();
		classificationpage.clickYes();
		// verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		qualitystatus.sortRulestByLastExecution();
		List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("ruleStatus"));
		Assertions.assertThat(catalogentityrule).containsAnyOf(map.get("CatalogEntityRule"));
		log(LogType.INFO, "Verifying catalog entity rule");
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickOnCloseExpandButton();
		// verify in master
		productdetail.clickMenuRefreshIcon();
		queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog"))
				.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult()
				.selectTabfromDropdown(map.get("GlobalAttributeTab"));
		// verifying hazardous code
		manufacturecode = globalattribute.getManufacturercode();
		Assertions.assertThat(manufacturecode).isEqualTo(map.get("ManufactureCode"));
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		classificationpage.clickOnExpandButton();
		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickOnCloseExpandButton();
		pimHomepage.clickLogoutButton();
	}

	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemMessage)
	@Test(description = "ITM_071 | Verifying Item Message based on Manufacture Code with JDE description", groups = { "US",
			"pim","ITEM_MESSAGE" }, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verifyItemMessageforManufactureCodeAndJdeDescription(Map<String, String> map) throws Throwable {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
		// verifying manufacture code
		manufacturecode = globalattribute.getManufacturercode();
		Assertions.assertThat(manufacturecode).isEqualTo(map.get("ManufactureCode"));
		// verifying jde description
		jdedescription = globalattribute.getJdeDescription();
		Assertions.assertThat(jdedescription).contains(map.get("JDE"));
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		classificationpage.clickOnExpandButton();
		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickStructureGroup();
		classificationpage.removeButton();
		classificationpage.clickYes();
		// verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		qualitystatus.sortRulestByLastExecution();
		List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("ruleStatus"));
		Assertions.assertThat(catalogentityrule).containsAnyOf(map.get("CatalogEntityRule"));
		log(LogType.INFO, "Verifying catalog entity rule");
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickOnCloseExpandButton();
		// verify in master
		productdetail.clickMenuRefreshIcon();
		queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog"))
				.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult()
				.selectTabfromDropdown(map.get("GlobalAttributeTab"));
		// verifying manufacture code
		manufacturecode = globalattribute.getManufacturercode();
		Assertions.assertThat(manufacturecode).isEqualTo(map.get("ManufactureCode"));
		// verifying jde description
		jdedescription = globalattribute.getJdeDescription();
		Assertions.assertThat(jdedescription).contains(map.get("JDE"));
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		classificationpage.clickOnExpandButton();
		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickOnCloseExpandButton();
		pimHomepage.clickLogoutButton();
	}

	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemMessage)
	@Test(description = "ITM_058 | Verifying Item Message based on ClassificationCode as inc. & ManufactureCode as exc.", groups = {
			"US", "pim","ITEM_MESSAGE" }, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verifyItemMessageforClassificationCodeIncAndManufactureCodeExc(Map<String, String> map)
			throws Throwable {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
		// verifying classification code
		classificationcode = globalattribute.getClassificationCode();
		Assertions.assertThat(classificationcode).isEqualTo(map.get("ClassificationCode"));
		// verifying manufacture code
		manufacturecode = globalattribute.getManufacturercode();
		Assertions.assertThat(manufacturecode).isNotEqualTo(map.get("ManufactureCode"));
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		classificationpage.clickOnExpandButton();
		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickStructureGroup();
		classificationpage.removeButton();
		classificationpage.clickYes();
		// verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		qualitystatus.sortRulestByLastExecution();
		List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("ruleStatus"));
		Assertions.assertThat(catalogentityrule).containsAnyOf(map.get("CatalogEntityRule"));
		log(LogType.INFO, "Verifying catalog entity rule");
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickOnCloseExpandButton();
		// verify in master
		productdetail.clickMenuRefreshIcon();
		queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog"))
				.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult()
				.selectTabfromDropdown(map.get("GlobalAttributeTab"));
		// verifying classification code
		classificationcode = globalattribute.getClassificationCode();
		Assertions.assertThat(classificationcode).isEqualTo(map.get("ClassificationCode"));
		// verifying manufacture code
		manufacturecode = globalattribute.getManufacturercode();
		Assertions.assertThat(manufacturecode).isNotEqualTo(map.get("ManufactureCode"));
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		classificationpage.clickOnExpandButton();
		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickOnCloseExpandButton();
		pimHomepage.clickLogoutButton();
	}

	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemMessage)
	@Test(description = "ITM_050, ITM_055 | Verifying Item Message based on two or more ItemCode", groups = { "US",
			"pim","ITEM_MESSAGE" }, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verifyItemMessageforItemCode(Map<String, String> map) throws Throwable {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		List<String> itemcodelist = javautil.readMultipleValuesFromExcel(map.get("ItemNumber"));
		for (int i = 0; i <= itemcodelist.size() - 1; i++) {
			pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
					.selectCatalogType(map.get("CatalogType"));
			queriessubmenu.enterHsiItemNumber(itemcodelist.get(i)).clickSeachButton().clickOnFirstResult()
					.selectTabfromDropdown(map.get("Classificationtab"));
			classificationpage.clickOnExpandButton();
			znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
					map.get("structurePath"));
			Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
			classificationpage.clickStructureGroup();
			classificationpage.removeButton();
			classificationpage.clickYes();
			// verify quality status
			pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
			qualitystatus.sortRulestByLastExecution();
			List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("ruleStatus"));
			Assertions.assertThat(catalogentityrule).containsAnyOf(map.get("CatalogEntityRule"));
			log(LogType.INFO, "Verifying catalog entity rule");
			pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
			pimHomepage.productDetailSearchPage().clickRefreshIcon();
			znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
					map.get("structurePath"));
			Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
			classificationpage.clickOnCloseExpandButton();
			productdetail.clickMenuRefreshIcon();
		}
		// verify in master
		for (int j = 0; j <= itemcodelist.size() - 1; j++) {
			queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog"));
			queriessubmenu.enterHsiItemNumber(itemcodelist.get(j)).clickSeachButton().clickOnFirstResult()
					.selectTabfromDropdown(map.get("Classificationtab"));
			classificationpage.clickOnExpandButton();
			znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
					map.get("structurePath"));
			Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
			classificationpage.clickOnCloseExpandButton();
			productdetail.clickMenuRefreshIcon();
		}
		pimHomepage.clickLogoutButton();
	}

	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemMessage)
	@Test(description = "ITM_054 | Verifying Item Message based on Item Code with Brand Code", groups = { "US",
			"pim","ITEM_MESSAGE" }, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verifyItemMessageforItemCodeWithBrandCode(Map<String, String> map) throws Throwable {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO, "Navigate to Classification tab from Queries Menu for US Catalog");

		classificationpage.clickOnExpandButton();
		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickStructureGroup();
		classificationpage.removeButton();
		classificationpage.clickYes();
		// verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		qualitystatus.sortRulestByLastExecution();
		List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("ruleStatus"));
		Assertions.assertThat(catalogentityrule).containsAnyOf(map.get("CatalogEntityRule"));
		log(LogType.INFO, "Verifying catalog entity rule");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		log(LogType.INFO, "Verifying item message code.");
		classificationpage.clickOnCloseExpandButton();

		// verify in master
		productdetail.clickMenuRefreshIcon();
		queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog"))
				.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult()
				.selectTabfromDropdown(map.get("Classificationtab"));
		classificationpage.clickOnExpandButton();
		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickOnCloseExpandButton();
		pimHomepage.clickLogoutButton();
	}

	// ITM_130,ITM_131
	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.Valid_Failure)
	@TestDataSheet(sheetname = TestCaseSheet.ItemMessage)
	@Test(description = "ITM_130,ITM_131 | Verify the item message for ItemCode in CA account", groups = { "CA",
			"pim","ITEM_MESSAGE" }, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verifyItemMessageforItemCodeForCaUser(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO, "Navigate to Classification tab from Queries Menu for US Catalog");

		classificationpage.clickOnExpandButton();
		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickStructureGroup();
		classificationpage.removeButton();
		classificationpage.clickYes();
		// verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		qualitystatus.sortRulestByLastExecution();
		List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("ruleStatus"));
		Assertions.assertThat(catalogentityrule).containsAnyOf(map.get("CatalogEntityRule"));
		log(LogType.INFO, "Verifying catalog entity rule");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		log(LogType.INFO, "Verifying item message code.");
		classificationpage.clickOnCloseExpandButton();

		// verify in master
		productdetail.clickMenuRefreshIcon();
		queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog"))
				.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult()
				.selectTabfromDropdown(map.get("Classificationtab"));
		classificationpage.clickOnExpandButton();
		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickOnCloseExpandButton();
		pimHomepage.clickLogoutButton();

	}

	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.Valid_Failure)
	@TestDataSheet(sheetname = TestCaseSheet.ItemMessage)
	@Test(description = "ITM_152 | Verify the item message for LineTypeCode in CA account", groups = { "CA",
			"pim","ITEM_MESSAGE" }, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verifyItemMessageforLineTypeCodeForCaUser(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
		log(LogType.INFO, "Navigate to Global Attribute tab from Queries Menu for US Catalog");

		// verifying linetype code
		linetypecode = globalattribute.getLineTypeCode();
		Assertions.assertThat(linetypecode).isEqualTo(map.get("LineTypeCode"));
		log(LogType.INFO, "Verifying line type code");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		classificationpage.clickOnExpandButton();
		zcanadaglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(zcanadaglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickStructureGroup();
		classificationpage.removeButton();
		classificationpage.clickYes();

		// verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		qualitystatus.sortRulestByLastExecution();
		List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("ruleStatus"));
		Assertions.assertThat(catalogentityrule).containsAnyOf(map.get("CatalogEntityRule"));
		log(LogType.INFO, "Verifying catalog entity rule");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		zcanadaglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(zcanadaglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		log(LogType.INFO, "Verifying item message code.");
		classificationpage.clickOnCloseExpandButton();

		// verify in master
		productdetail.clickMenuRefreshIcon();
		queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog"))
				.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult()
				.selectTabfromDropdown(map.get("GlobalAttributeTab"));
		log(LogType.INFO, "Navigate to Global Attribute tab from Queries Menu for Master Catalog");

		// verifying linetype code in master
		linetypecode = globalattribute.getLineTypeCode();
		Assertions.assertThat(linetypecode).isEqualTo(map.get("LineTypeCode"));
		log(LogType.INFO, "Verifying line type code in master");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		classificationpage.clickOnExpandButton();
		zcanadaglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(zcanadaglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickOnCloseExpandButton();
		pimHomepage.clickLogoutButton();

	}

	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemMessage)
	@Test(description = "ITM_084, ITM_085 | Verifying Item Message based on Print Message", groups = { "US",
			"pim","ITEM_MESSAGE" }, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verifyItemMessageforPrintMessage(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickSettingIcon().clickFieldSelectionOption();
		log(LogType.INFO, "Navigate to Field Selection Page from Setting Icon for US Catalog");

		fieldselectionpage.enterFieldName(map.get("Header")).clickHeaderText(map.get("HeaderText")).clickAddButton()
				.clickOkButton();
		log(LogType.INFO, "Print Message header is added in header");

		productdetail.clickOnFirstResult();
		// verify print message
		String printMessage = productdetail.getItemField(map.get("fieldIndex"));
		Assertions.assertThat(printMessage).isEqualTo(map.get("PrintMessage"));
		log(LogType.INFO, "Verifying Print Message");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		classificationpage.clickOnExpandButton();
		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickStructureGroup();
		classificationpage.removeButton();
		classificationpage.clickYes();

		// verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		qualitystatus.sortRulestByLastExecution();
		List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("ruleStatus"));
		Assertions.assertThat(catalogentityrule).containsAnyOf(map.get("CatalogEntityRule"));
		log(LogType.INFO, "Verifying catalog entity rule");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		log(LogType.INFO, "Verifying item message code.");
		classificationpage.clickOnCloseExpandButton();

		// verify in master
		productdetail.clickMenuRefreshIcon();
		queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog"))
				.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult();

		// verify print message
		printMessage = productdetail.getItemField(map.get("fieldIndex"));
		Assertions.assertThat(printMessage).isEqualTo(map.get("PrintMessage"));

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		classificationpage.clickOnExpandButton();
		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickOnCloseExpandButton();
		pimHomepage.clickLogoutButton();
	}

	// ITM_150
	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.Valid_Failure)
	@TestDataSheet(sheetname = TestCaseSheet.ItemMessage)
	@Test(description = "ITM_150 | Verify the item message for MPC in CA account", groups = { "CA",
			"pim","ITEM_MESSAGE" }, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verifyItemMessageforMPCForCaUser(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult();

		// verifying major product class code
		String majorproductclasscode = productdetail.getItemField(map.get("fieldIndex"));
		Assertions.assertThat(majorproductclasscode).isEqualTo(map.get("MPC"));
		log(LogType.INFO, "Verifying Major Product Class code");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO, "Navigate to Classification tab after verifying the MPC in US Catalog");

		classificationpage.clickOnExpandButton();
		zcanadaglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(zcanadaglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickStructureGroup();
		classificationpage.removeButton();
		classificationpage.clickYes();

		// verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		qualitystatus.sortRulestByLastExecution();
		List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("ruleStatus"));
		Assertions.assertThat(catalogentityrule).containsAnyOf(map.get("CatalogEntityRule"));
		log(LogType.INFO, "Verifying catalog entity rule");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		zcanadaglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(zcanadaglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		log(LogType.INFO, "Verifying item message code.");
		classificationpage.clickOnCloseExpandButton();

		// verify in master
		productdetail.clickMenuRefreshIcon();
		queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog"))
				.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult();
		// verifying major product class code
		majorproductclasscode = productdetail.getItemField(map.get("fieldIndex"));
		Assertions.assertThat(majorproductclasscode).isEqualTo(map.get("MPC"));
		log(LogType.INFO, "Verifying Major Product Class code in Master catalog");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO, "Navigate to Classification tab after verifying the MPC in Master Catalog");

		classificationpage.clickOnExpandButton();
		zcanadaglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(zcanadaglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickOnCloseExpandButton();
		pimHomepage.clickLogoutButton();
	}

	// ITM_132
	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemMessage)
	@Test(description = "ITM_132 | Verify the item message for Availability code in CA account", groups = { "CA",
			"pim","ITEM_MESSAGE" }, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verifyItemMessageforAvailabilityCodeForCaUser(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult();

		// verifying availability code
		String availabilitycode = productdetail.getItemField(map.get("fieldIndex"));
		Assertions.assertThat(availabilitycode).isEqualTo(map.get("AvailabilityCode"));
		log(LogType.INFO, "Verifying Availability code");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO, "Navigate to Classification tab after verifying the availability code in CA Catalog");

		classificationpage.clickOnExpandButton();
		zcanadaglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(zcanadaglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickStructureGroup();
		classificationpage.removeButton();
		classificationpage.clickYes();

		// verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		qualitystatus.sortRulestByLastExecution();
		List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("ruleStatus"));
		Assertions.assertThat(catalogentityrule).containsAnyOf(map.get("CatalogEntityRule"));
		log(LogType.INFO, "Verifying catalog entity rule");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		zcanadaglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(zcanadaglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		log(LogType.INFO, "Verifying item message code.");
		classificationpage.clickOnCloseExpandButton();

		// verify in master
		productdetail.clickMenuRefreshIcon();
		queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog"))
				.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult();
		// verifying availability code
		availabilitycode = productdetail.getItemField(map.get("fieldIndex"));
		Assertions.assertThat(availabilitycode).isEqualTo(map.get("AvailabilityCode"));
		log(LogType.INFO, "Verifying availability code in Master catalog");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO, "Navigate to Classification tab after verifying the availability code in Master Catalog");

		classificationpage.clickOnExpandButton();
		zcanadaglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(zcanadaglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickOnCloseExpandButton();
		pimHomepage.clickLogoutButton();
	}

	// ITM_144
	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemMessage)
	@Test(description = "ITM_144 | Verify the item message for MPC inc & Sub-MPC exc in CA account", groups = { "CA",
			"pim","ITEM_MESSAGE" }, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verifyItemMessageforMPCincSubMPCexcForCaUser(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult();

		// verifying MPC code
		String mpccode = productdetail.getItemField(map.get("fieldIndex"));
		Assertions.assertThat(mpccode).isEqualTo(map.get("MPC"));
		log(LogType.INFO, "Verifying MPC code");
		// verifying Sub-MPC code
		String submpccode = productdetail.getItemField(map.get("fieldIndex1"));
		Assertions.assertThat(submpccode).isNotEqualTo(map.get("SubMPC"));
		log(LogType.INFO, "Verifying Sub-MPC code");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO,
				"Navigate to Classification tab after verifying the MPC code inc. & Sub-MPC exc.  in CA Catalog");

		classificationpage.clickOnExpandButton();
		zcanadaglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(zcanadaglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickStructureGroup();
		classificationpage.removeButton();
		classificationpage.clickYes();

		// verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		qualitystatus.sortRulestByLastExecution();
		List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("ruleStatus"));
		Assertions.assertThat(catalogentityrule).containsAnyOf(map.get("CatalogEntityRule"));
		log(LogType.INFO, "Verifying catalog entity rule");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		zcanadaglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(zcanadaglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		log(LogType.INFO, "Verifying item message code.");
		classificationpage.clickOnCloseExpandButton();

		// verify in master
		productdetail.clickMenuRefreshIcon();
		queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog"))
				.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult();
		// verifying MPC code
		mpccode = productdetail.getItemField(map.get("fieldIndex"));
		Assertions.assertThat(mpccode).isEqualTo(map.get("MPC"));
		log(LogType.INFO, "Verifying MPC code");
		// verifying Sub-MPC code
		submpccode = productdetail.getItemField(map.get("fieldIndex1"));
		Assertions.assertThat(submpccode).isNotEqualTo(map.get("SubMPC"));
		log(LogType.INFO, "Verifying Sub-MPC code");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO,
				"Navigate to Classification tab after verifying the MPC code inc. & Sub-MPC exc. in Master Catalog");

		classificationpage.clickOnExpandButton();
		zcanadaglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(zcanadaglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickOnCloseExpandButton();
		pimHomepage.clickLogoutButton();
	}

	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemMessage)
	@Test(description = "ITM_083 | Verifying Item Message based on Line type code and print message inclusive", groups = { "US",
			"pim","ITEM_MESSAGE" }, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verifyItemMessageforLineTypeIncPrintMessageInc(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickCatalogsMenu();
		productdetail.clickSettingIcon().clickFieldSelectionOption();
		log(LogType.INFO, "Navigate to Field Selection Page from Setting Icon for US Catalog");

		fieldselectionpage.enterFieldName(map.get("Header")).clickHeaderText(map.get("HeaderText")).clickAddButton()
				.clickOkButton();
		log(LogType.INFO, "Print Message header is added in header");
		BasePage.WaitForMiliSec(10000);
		productdetail.clickRefreshIcon();

		List<String> itemcodelist = javautil.readMultipleValuesFromExcel(map.get("ItemNumber"));
		List<String> printmessagelist = javautil.readMultipleValuesFromExcel(map.get("PrintMessage"));
		for (int i = 0; i <= itemcodelist.size() - 1; i++) {
			pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
					.selectCatalogType(map.get("CatalogType"));
			queriessubmenu.enterHsiItemNumber(itemcodelist.get(i)).clickSeachButton().clickOnFirstResult();
			BasePage.WaitForMiliSec(5000);
			// verify print message
			String printMessage = productdetail.getItemField(map.get("fieldIndex"));
			BasePage.WaitForMiliSec(5000);
			Assertions.assertThat(printMessage).isEqualTo(printmessagelist.get(i));
			log(LogType.INFO, "Verifying Print Message");
			// verifying linetype code
			productdetail.selectTabfromDropdown(map.get("GlobalAttributeTab"));
			linetypecode = globalattribute.getLineTypeCode();
			Assertions.assertThat(linetypecode).isEqualTo(map.get("LineTypeCode"));
			log(LogType.INFO, "Verifying line type code");

			pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
			classificationpage.clickOnExpandButton();
			znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
					map.get("structurePath"));
			Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
			classificationpage.clickStructureGroup();
			classificationpage.removeButton();
			classificationpage.clickYes();
			// verify quality status
			pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
			qualitystatus.sortRulestByLastExecution();
			List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("ruleStatus"));
			Assertions.assertThat(catalogentityrule).containsAnyOf(map.get("CatalogEntityRule"));
			log(LogType.INFO, "Verifying catalog entity rule");
			pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
			pimHomepage.productDetailSearchPage().clickRefreshIcon();
			znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
					map.get("structurePath"));
			Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
			classificationpage.clickOnCloseExpandButton();
			productdetail.clickMenuRefreshIcon();
		}
		// verify in master
		for (int j = 0; j <= itemcodelist.size() - 1; j++) {
			queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog"));
			queriessubmenu.enterHsiItemNumber(itemcodelist.get(j)).clickSeachButton().clickOnFirstResult();

			// verify print message
			String printMessage = productdetail.getItemField(map.get("fieldIndex"));
			Assertions.assertThat(printMessage).isEqualTo(printmessagelist.get(j));
			log(LogType.INFO, "Verifying Print Message");
			// verifying linetype code
			productdetail.selectTabfromDropdown(map.get("GlobalAttributeTab"));
			linetypecode = globalattribute.getLineTypeCode();
			Assertions.assertThat(linetypecode).isEqualTo(map.get("LineTypeCode"));
			log(LogType.INFO, "Verifying line type code");

			pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
			classificationpage.clickOnExpandButton();
			znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
					map.get("structurePath"));
			Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
			classificationpage.clickOnCloseExpandButton();
			productdetail.clickMenuRefreshIcon();
		}
		pimHomepage.clickLogoutButton();
	}

	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemMessage)
	@Test(description = "ITM_082 | Verifying Item Message based on Location Type", groups = { "US",
			"pim","ITEM_MESSAGE" }, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verifyItemMessageforLocationType(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult();

		// verifying location type
		String locationtype = productdetail.getItemField(map.get("fieldIndex"));
		Assertions.assertThat(locationtype).isEqualTo(map.get("LocationType"));
		log(LogType.INFO, "Verifying Location type");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO, "Navigate to Classification tab after verifying the location type in US Catalog");

		classificationpage.clickOnExpandButton();
		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickStructureGroup();
		classificationpage.removeButton();
		classificationpage.clickYes();

		// verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		qualitystatus.sortRulestByLastExecution();
		List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("ruleStatus"));
		Assertions.assertThat(catalogentityrule).containsAnyOf(map.get("CatalogEntityRule"));
		log(LogType.INFO, "Verifying catalog entity rule");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		zcanadaglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(zcanadaglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		log(LogType.INFO, "Verifying item message code.");
		classificationpage.clickOnCloseExpandButton();

		// verify in master
		productdetail.clickMenuRefreshIcon();
		queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog"))
				.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult();
		// verifying location type
		locationtype = productdetail.getItemField(map.get("fieldIndex"));
		Assertions.assertThat(locationtype).isEqualTo(map.get("LocationType"));
		log(LogType.INFO, "Verifying location type in Master catalog");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO, "Navigate to Classification tab after verifying the location type in Master Catalog");

		classificationpage.clickOnExpandButton();
		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickOnCloseExpandButton();
		pimHomepage.clickLogoutButton();
	}

	// ITM_135,ITM_140,ITM_142
	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.Valid_Failure)
	@TestDataSheet(sheetname = TestCaseSheet.ItemMessage)
	@Test(description = "ITM_135,ITM_140,ITM_142 | Verify the item message for Availability Code & Cat code inc in CA account", groups = {
			"CA", "pim","ITEM_MESSAGE" }, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verifyItemMessageforAvailabilityCodeIncCatcodeIncCaUser(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickSettingIcon().clickFieldSelectionOption();
		log(LogType.INFO, "Navigate to Field Selection Page from Setting Icon for CA Catalog");

		fieldselectionpage.enterFieldName(map.get("Header")).clickHeaderText(map.get("HeaderText")).clickAddButton()
				.clickOkButton();
		log(LogType.INFO, "Cat code is added in header");

		productdetail.clickOnFirstResult();

		// verify availability code
		String availabilitycode = productdetail.getItemField(map.get("fieldIndex"));
		Assertions.assertThat(availabilitycode).isEqualTo(map.get("AvailabilityCode"));
		log(LogType.INFO, "Verifying Availability Code");
		// verify cat code
		String catcode = productdetail.getItemField(map.get("fieldIndex1"));
		Assertions.assertThat(catcode).isEqualTo(map.get("CatCode"));
		log(LogType.INFO, "Verifying Cat Code");
		
		  pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get(
		  "Classificationtab")); log(LogType.INFO,
		  "Navigate to Classification tab after verifying the availability code inc. & cat code incc. in CA Catalog"
		  );
		  
		  classificationpage.clickOnExpandButton(); zcanadaglobalerrormessage =
		  classificationpage.getStructureGroup(map.get("structureSystem"),
		  map.get("structurePath"));
		  Assertions.assertThat(zcanadaglobalerrormessage).isEqualTo(map.get(
		  "ItemMessageCode")); classificationpage.clickStructureGroup();
		  classificationpage.removeButton(); 
		  BasePage.WaitForMiliSec(5000);
		  classificationpage.clickYes();
		 
		  // verify quality status
		  pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get(
		  "Qualitystatustab")); qualitystatus.sortRulestByLastExecution(); List<String>
		  catalogentityrule =
		  qualitystatus.getRuleNamesByRuleStatus(map.get("ruleStatus"));
		  Assertions.assertThat(catalogentityrule).containsAnyOf(map.get(
		  "CatalogEntityRule")); log(LogType.INFO, "Verifying catalog entity rule");
		  
		  pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get(
		  "Classificationtab"));
		  pimHomepage.productDetailSearchPage().clickRefreshIcon();
		  zcanadaglobalerrormessage =
		  classificationpage.getStructureGroup(map.get("structureSystem"),
		  map.get("structurePath"));
		  Assertions.assertThat(zcanadaglobalerrormessage).isEqualTo(map.get(
		  "ItemMessageCode")); log(LogType.INFO, "Verifying item message code.");
		  classificationpage.clickOnCloseExpandButton();

		// verify in master
		productdetail.clickMenuRefreshIcon();
		queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog"))
				.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton();
		BasePage.WaitForMiliSec(3000);
		actionsPage.clickOnJDEDes();
		BasePage.WaitForMiliSec(3000);
        productDetailSearchPage.applyFilterInSearchPageByAnyValue(map.get("HeaderText"), map.get("CatCode")).clickOnFirstResult();
        BasePage.WaitForMiliSec(5000);
        
		// verify availability code
		availabilitycode = productdetail.getItemField(map.get("fieldIndex"));
		BasePage.WaitForMiliSec(5000);
		Assertions.assertThat(availabilitycode).isEqualTo(map.get("AvailabilityCode"));
		log(LogType.INFO, "Verifying Availability Code");
		// verify cat code
		catcode = productdetail.getItemField(map.get("fieldIndex1"));
		Assertions.assertThat(catcode).isEqualTo(map.get("CatCode"));
		log(LogType.INFO, "Verifying Cat Code");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO,
				"Navigate to Classification tab after verifying the MPC code inc. & Sub-MPC exc. in Master Catalog");

		classificationpage.clickOnExpandButton();
		zcanadaglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(zcanadaglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickOnCloseExpandButton();
		pimHomepage.clickLogoutButton();
	}

	// ITM_141
	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemMessage)
	@Test(description = "ITM_141 | Verify the item message for Availability Code & Cat code inc in CA account", groups = {
			"CA", "pim","ITEM_MESSAGE" }, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verifyItemMessageforAvailabilityCodeIncCatcodeIncCaUserForD57(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickSettingIcon().clickFieldSelectionOption();
		log(LogType.INFO, "Navigate to Field Selection Page from Setting Icon for CA Catalog");

		fieldselectionpage.enterFieldName(map.get("Header")).clickHeaderText(map.get("HeaderText")).clickAddButton()
				.clickOkButton();
		log(LogType.INFO, "Cat code is added in header");

		productdetail.clickOnFirstResult();

		// verify availability code
		String availabilitycode = productdetail.getItemField(map.get("fieldIndex"));
		Assertions.assertThat(availabilitycode).isEqualTo(map.get("AvailabilityCode"));
		log(LogType.INFO, "Verifying Availability Code");
		// verify cat code
		String catcode = productdetail.getItemField(map.get("fieldIndex1"));
		Assertions.assertThat(catcode).isEqualTo(map.get("CatCode"));
		log(LogType.INFO, "Verifying Cat Code");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO,
				"Navigate to Classification tab after verifying the availability code inc. & cat code incc. in CA Catalog");

		classificationpage.clickOnExpandButton();
		zcanadaglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(zcanadaglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickStructureGroup();
		classificationpage.removeButton();
		classificationpage.clickYes();

		// verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		qualitystatus.sortRulestByLastExecution();
		List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("ruleStatus"));
		Assertions.assertThat(catalogentityrule).containsAnyOf(map.get("CatalogEntityRule"));
		log(LogType.INFO, "Verifying catalog entity rule");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		zcanadaglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(zcanadaglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		log(LogType.INFO, "Verifying item message code.");
		classificationpage.clickOnCloseExpandButton();

		// verify in master
		productdetail.clickMenuRefreshIcon();
		queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog"))
				.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnSecondResult();
		// verify availability code
		availabilitycode = productdetail.getItemField(map.get("fieldIndex"));
		Assertions.assertThat(availabilitycode).isEqualTo(map.get("AvailabilityCode"));
		log(LogType.INFO, "Verifying Availability Code");
		// verify cat code
		catcode = productdetail.getItemField(map.get("fieldIndex1"));
		Assertions.assertThat(catcode).isEqualTo(map.get("CatCode"));
		log(LogType.INFO, "Verifying Cat Code");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO,
				"Navigate to Classification tab after verifying the MPC code inc. & Sub-MPC exc. in Master Catalog");

		classificationpage.clickOnExpandButton();
		zcanadaglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(zcanadaglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickOnCloseExpandButton();
		pimHomepage.clickLogoutButton();
	}

	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemMessage)
	@Test(description = "ITM_052, ITM_059, ITM_069 | Verifying Item Message based on Pricing Group", groups = { "US",
			"pim","ITEM_MESSAGE" }, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verifyItemMessageForPricingGroup(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickSettingIcon().clickFieldSelectionOption();
		log(LogType.INFO, "Navigate to Field Selection Page from Setting Icon for US Catalog");

		fieldselectionpage.enterFieldName(map.get("Header")).clickHeaderText(map.get("HeaderText")).clickAddButton()
				.clickOkButton();
		log(LogType.INFO, "Print Message header is added in header");

		productdetail.clickOnFirstResult();
		// verify pricing group
		String pricingGroup = productdetail.getItemField(map.get("fieldIndex"));
		Assertions.assertThat(pricingGroup).isEqualTo(map.get("PricingGroup"));
		log(LogType.INFO, "Verifying Pricing Group.");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		classificationpage.clickOnExpandButton();
		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickStructureGroup();
		classificationpage.removeButton();
		classificationpage.clickYes();

		// verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		qualitystatus.sortRulestByLastExecution();
		List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("ruleStatus"));
		Assertions.assertThat(catalogentityrule).containsAnyOf(map.get("CatalogEntityRule"));
		log(LogType.INFO, "Verifying catalog entity rule");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		log(LogType.INFO, "Verifying item message code.");
		classificationpage.clickOnCloseExpandButton();

		// verify in master
		productdetail.clickMenuRefreshIcon();
		queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog"))
				.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult();

		// verify pricing group
		pricingGroup = productdetail.getItemField(map.get("fieldIndex"));
		Assertions.assertThat(pricingGroup).isEqualTo(map.get("PricingGroup"));

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		classificationpage.clickOnExpandButton();
		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickOnCloseExpandButton();
		pimHomepage.clickLogoutButton();
	}

	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemMessage)
	@Test(description = "ITM_089 | Verifying Item Message based on Federal Drug Acct Code", groups = { "US",
			"pim","ITEM_MESSAGE" }, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verifyItemMessageforFDAC(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));

		// verifying FDAC
		fdac = globalattribute.getFdac();
		Assertions.assertThat(fdac).isEqualTo(map.get("FDAC"));
		log(LogType.INFO, "Verifying Federal Drug Acct Code");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO, "Navigate to Classification tab after verifying the fdac in US Catalog");

		classificationpage.clickOnExpandButton();
		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickStructureGroup();
		classificationpage.removeButton();
		classificationpage.clickYes();

		// verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		qualitystatus.sortRulestByLastExecution();
		List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("ruleStatus"));
		Assertions.assertThat(catalogentityrule).containsAnyOf(map.get("CatalogEntityRule"));
		log(LogType.INFO, "Verifying catalog entity rule");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		zcanadaglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(zcanadaglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		log(LogType.INFO, "Verifying item message code.");
		classificationpage.clickOnCloseExpandButton();

		// verify in master
		productdetail.clickMenuRefreshIcon();
		queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog"))
				.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult()
				.selectTabfromDropdown(map.get("GlobalAttributeTab"));
		// verifying FDAC
		fdac = globalattribute.getFdac();
		Assertions.assertThat(fdac).isEqualTo(map.get("FDAC"));
		log(LogType.INFO, "Verifying Federal Drug Acct Code in master catalog");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO, "Navigate to Classification tab after verifying the fdac in Master Catalog");

		classificationpage.clickOnExpandButton();
		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickOnCloseExpandButton();
		pimHomepage.clickLogoutButton();
	}

	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemMessage)
	@Test(description = "ITM_101 | Verify the item message for MPC in US account", groups = { "US",
			"pim","ITEM_MESSAGE" }, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verifyItemMessageforMPC(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult();

		// verifying major product class code
		String majorproductclasscode = productdetail.getItemField(map.get("fieldIndex"));
		Assertions.assertThat(majorproductclasscode).isEqualTo(map.get("MPC"));
		log(LogType.INFO, "Verifying Major Product Class code");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO, "Navigate to Classification tab after verifying the MPC in US Catalog");

		classificationpage.clickOnExpandButton();
		zcanadaglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(zcanadaglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickStructureGroup();
		classificationpage.removeButton();
		classificationpage.clickYes();

		// verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		qualitystatus.sortRulestByLastExecution();
		List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("ruleStatus"));
		Assertions.assertThat(catalogentityrule).containsAnyOf(map.get("CatalogEntityRule"));
		log(LogType.INFO, "Verifying catalog entity rule");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		zcanadaglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(zcanadaglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		log(LogType.INFO, "Verifying item message code.");
		classificationpage.clickOnCloseExpandButton();

		// verify in master
		productdetail.clickMenuRefreshIcon();
		queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog"))
				.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult();
		// verifying major product class code
		majorproductclasscode = productdetail.getItemField(map.get("fieldIndex"));
		Assertions.assertThat(majorproductclasscode).isEqualTo(map.get("MPC"));
		log(LogType.INFO, "Verifying Major Product Class code in Master catalog");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO, "Navigate to Classification tab after verifying the MPC in Master Catalog");

		classificationpage.clickOnExpandButton();
		zcanadaglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(zcanadaglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickOnCloseExpandButton();
		pimHomepage.clickLogoutButton();
	}

	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemMessage)
	@Test(description = "ITM_096 | Verify the item message for Availability Code & Cat code inc in US account", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class,groups = { "US",
			"pim","ITEM_MESSAGE" })
	public void verifyItemMessageforAvailabilityCodeIncCatcodeInc(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickSettingIcon().clickFieldSelectionOption();
		log(LogType.INFO, "Navigate to Field Selection Page from Setting Icon for US Catalog");

		fieldselectionpage.enterFieldName(map.get("Header")).clickHeaderText(map.get("HeaderText")).clickAddButton()
				.clickOkButton();
		log(LogType.INFO, "Cat code is added in header");

		productdetail.clickOnFirstResult();

		// verify availability code
		String availabilitycode = productdetail.getItemField(map.get("fieldIndex"));
		Assertions.assertThat(availabilitycode).isEqualTo(map.get("AvailabilityCode"));
		log(LogType.INFO, "Verifying Availability Code");
		// verify cat code
		String catcode = productdetail.getItemField(map.get("fieldIndex1"));
		Assertions.assertThat(catcode).isEqualTo(map.get("CatCode"));
		log(LogType.INFO, "Verifying Cat Code");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO,
				"Navigate to Classification tab after verifying the availability code inc. & cat code inc. in US Catalog");

		classificationpage.clickOnExpandButton();
		zcanadaglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(zcanadaglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickStructureGroup();
		classificationpage.removeButton();
		classificationpage.clickYes();

		// verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		qualitystatus.sortRulestByLastExecution();
		List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("ruleStatus"));
		Assertions.assertThat(catalogentityrule).containsAnyOf(map.get("CatalogEntityRule"));
		log(LogType.INFO, "Verifying catalog entity rule");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		zcanadaglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(zcanadaglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		log(LogType.INFO, "Verifying item message code.");
		classificationpage.clickOnCloseExpandButton();

		// verify in master
		productdetail.clickMenuRefreshIcon();
		queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog"))
				.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult();
		// verify availability code
		availabilitycode = productdetail.getItemField(map.get("fieldIndex"));
		Assertions.assertThat(availabilitycode).isEqualTo(map.get("AvailabilityCode"));
		log(LogType.INFO, "Verifying Availability Code");
		// verify cat code
		catcode = productdetail.getItemField(map.get("fieldIndex1"));
		Assertions.assertThat(catcode).isEqualTo(map.get("CatCode"));
		log(LogType.INFO, "Verifying Cat Code");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO,
				"Navigate to Classification tab after verifying the MPC code inc. & Sub-MPC exc. in Master Catalog");

		classificationpage.clickOnExpandButton();
		zcanadaglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(zcanadaglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickOnCloseExpandButton();
		pimHomepage.clickLogoutButton();
	}

	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemMessage)
	@Test(description = "ITM_108 | Verifying Item Message based on multiple supplier code", groups = { "US",
			"pim","ITEM_MESSAGE" }, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verifyItemMessageforMultipleSupplierCode(Map<String, String> map) throws Throwable {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		List<String> itemcodelist = javautil.readMultipleValuesFromExcel(map.get("ItemNumber"));
		List<String> suppliercodelist = javautil.readMultipleValuesFromExcel(map.get("SupplierCode"));
		for (int i = 0; i <= itemcodelist.size() - 1; i++) {
			pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
					.selectCatalogType(map.get("CatalogType"));
			queriessubmenu.enterHsiItemNumber(itemcodelist.get(i)).clickSeachButton().clickOnFirstResult()
					.selectTabfromDropdown(map.get("GlobalAttributeTab"));
			log(LogType.INFO, "Navigate to Global attribute tab to verify supplier code in US Catalog");

			// verify supplier code
			supplierCode = globalattribute.getSuppliercode();
			Assertions.assertThat(supplierCode).isEqualTo(suppliercodelist.get(i));
			productdetail.selectTabfromDropdown(map.get("Classificationtab"));
			log(LogType.INFO, "Navigate to Classification tab after verifying the supplier code in US Catalog");

			classificationpage.clickOnExpandButton();
			classificationpage.sortStructureGroup();
			znewglobalerrormessage = classificationpage.getStructureGroupHavingBlankSpace(map.get("structureSystem"),
					map.get("structurePath"));
			Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
			classificationpage.clickStructureGroup();
			classificationpage.removeButton();
			classificationpage.clickYes();

			// verify quality status
			pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
			qualitystatus.sortRulestByLastExecution();
			List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("ruleStatus"));
			Assertions.assertThat(catalogentityrule).containsAnyOf(map.get("CatalogEntityRule"));
			log(LogType.INFO, "Verifying catalog entity rule");

			pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
			pimHomepage.productDetailSearchPage().clickRefreshIcon();
			classificationpage.sortStructureGroup();
			znewglobalerrormessage = classificationpage.getStructureGroupHavingBlankSpace(map.get("structureSystem"),
					map.get("structurePath"));
			Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
			classificationpage.clickOnCloseExpandButton();
			productdetail.clickMenuRefreshIcon();
		}

		// verify in master
		for (int j = 0; j <= itemcodelist.size() - 1; j++) {
			queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog"));
			queriessubmenu.enterHsiItemNumber(itemcodelist.get(j)).clickSeachButton().clickOnFirstResult()
					.selectTabfromDropdown(map.get("GlobalAttributeTab"));

			// verify supplier code
			supplierCode = globalattribute.getSuppliercode();
			Assertions.assertThat(supplierCode).isEqualTo(suppliercodelist.get(j));
			productdetail.selectTabfromDropdown(map.get("Classificationtab"));
			log(LogType.INFO, "Navigate to Classification tab after verifying the supplier code in Master Catalog");

			classificationpage.clickOnExpandButton();
			classificationpage.sortStructureGroup();
			znewglobalerrormessage = classificationpage.getStructureGroupHavingBlankSpace(map.get("structureSystem"),
					map.get("structurePath"));
			Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
			classificationpage.clickOnCloseExpandButton();
			productdetail.clickMenuRefreshIcon();
		}
		pimHomepage.clickLogoutButton();
	}

	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemMessage)
	@Test(description = "ITM_110 | Verifying Item Message based on multiple fdac", groups = { "US",
			"pim","ITEM_MESSAGE" }, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verifyItemMessageforMultipleFdac(Map<String, String> map) throws Throwable {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		List<String> itemcodelist = javautil.readMultipleValuesFromExcel(map.get("ItemNumber"));
		List<String> fdaclist = javautil.readMultipleValuesFromExcel(map.get("FDAC"));
		for (int i = 0; i <= itemcodelist.size() - 1; i++) {
			pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
					.selectCatalogType(map.get("CatalogType"));
			queriessubmenu.enterHsiItemNumber(itemcodelist.get(i)).clickSeachButton().clickOnFirstResult()
					.selectTabfromDropdown(map.get("GlobalAttributeTab"));
			log(LogType.INFO, "Navigate to Global attribute tab to verify fdac in US Catalog");

			// verify fdac
			fdac = globalattribute.getFdac();
			Assertions.assertThat(fdac).isEqualTo(fdaclist.get(i));
			productdetail.selectTabfromDropdown(map.get("Classificationtab"));
			log(LogType.INFO, "Navigate to Classification tab after verifying the fdac in US Catalog");

			classificationpage.clickOnExpandButton();
			classificationpage.sortStructureGroup();
			znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
					map.get("structurePath"));
			Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
			classificationpage.clickStructureGroup();
			classificationpage.removeButton();
			classificationpage.clickYes();

			// verify quality status
			pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
			qualitystatus.sortRulestByLastExecution();
			List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("ruleStatus"));
			Assertions.assertThat(catalogentityrule).containsAnyOf(map.get("CatalogEntityRule"));
			log(LogType.INFO, "Verifying catalog entity rule");

			pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
			pimHomepage.productDetailSearchPage().clickRefreshIcon();
			classificationpage.sortStructureGroup();
			znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
					map.get("structurePath"));
			Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
			classificationpage.clickOnCloseExpandButton();
			productdetail.clickMenuRefreshIcon();
		}

		// verify in master
		for (int j = 0; j <= itemcodelist.size() - 1; j++) {
			queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog"));
			queriessubmenu.enterHsiItemNumber(itemcodelist.get(j)).clickSeachButton().clickOnFirstResult()
					.selectTabfromDropdown(map.get("GlobalAttributeTab"));

			// verify fdac
			fdac = globalattribute.getFdac();
			Assertions.assertThat(fdac).isEqualTo(fdaclist.get(j));
			productdetail.selectTabfromDropdown(map.get("Classificationtab"));
			log(LogType.INFO, "Navigate to Classification tab after verifying the fdac in Master Catalog");

			classificationpage.clickOnExpandButton();
			classificationpage.sortStructureGroup();
			znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
					map.get("structurePath"));
			Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
			classificationpage.clickOnCloseExpandButton();
			productdetail.clickMenuRefreshIcon();
		}
		pimHomepage.clickLogoutButton();
	}

	// ITM_087
	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemMessage)
	@Test(description = "ITM_087 | Verifying Item Message based on two fdac", groups = { "US",
			"pim","ITEM_MESSAGE" }, dataProvider = "getCatalogData", priority = 2, dataProviderClass = DataProviderUtils.class)
	public void verifyItemMessageforTwoFdac(Map<String, String> map) throws Throwable {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		List<String> itemcodelist = javautil.readMultipleValuesFromExcel(map.get("ItemNumber"));
		List<String> fdaclist = javautil.readMultipleValuesFromExcel(map.get("FDAC"));
		for (int i = 0; i <= itemcodelist.size() - 1; i++) {
			pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
					.selectCatalogType(map.get("CatalogType"));
			queriessubmenu.enterHsiItemNumber(itemcodelist.get(i)).clickSeachButton().clickOnFirstResult()
					.selectTabfromDropdown(map.get("GlobalAttributeTab"));
			log(LogType.INFO, "Navigate to Global attribute tab to verify fdac in US Catalog");

			// verify fdac
			fdac = globalattribute.getFdac();
			Assertions.assertThat(fdac).isEqualTo(fdaclist.get(i));
			productdetail.selectTabfromDropdown(map.get("Classificationtab"));
			log(LogType.INFO, "Navigate to Classification tab after verifying the fdac in US Catalog");

			classificationpage.clickOnExpandButton();
			classificationpage.sortStructureGroup();
			znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
					map.get("structurePath"));
			Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
			classificationpage.clickStructureGroup();
			classificationpage.removeButton();
			classificationpage.clickYes();

			// verify quality status
			pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
			qualitystatus.sortRulestByLastExecution();
			List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("ruleStatus"));
			Assertions.assertThat(catalogentityrule).containsAnyOf(map.get("CatalogEntityRule"));
			log(LogType.INFO, "Verifying catalog entity rule");

			pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
			pimHomepage.productDetailSearchPage().clickRefreshIcon();
			classificationpage.sortStructureGroup();
			znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
					map.get("structurePath"));
			Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
			classificationpage.clickOnCloseExpandButton();
			productdetail.clickMenuRefreshIcon();
		}

		// verify in master
		for (int j = 0; j <= itemcodelist.size() - 1; j++) {
			queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog"));
			queriessubmenu.enterHsiItemNumber(itemcodelist.get(j)).clickSeachButton().clickOnFirstResult()
					.selectTabfromDropdown(map.get("GlobalAttributeTab"));

			// verify fdac
			fdac = globalattribute.getFdac();
			Assertions.assertThat(fdac).isEqualTo(fdaclist.get(j));
			productdetail.selectTabfromDropdown(map.get("Classificationtab"));
			log(LogType.INFO, "Navigate to Classification tab after verifying the fdac in Master Catalog");

			classificationpage.clickOnExpandButton();
			classificationpage.sortStructureGroup();
			znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
					map.get("structurePath"));
			Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
			classificationpage.clickOnCloseExpandButton();
			productdetail.clickMenuRefreshIcon();
		}
		pimHomepage.clickLogoutButton();
	}

	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.Valid_Failure)
	@TestDataSheet(sheetname = TestCaseSheet.ItemMessage)
	@Test(description = "ITM_117 | Verifying Item Message based on Availability code as inc. & SupplierCode as exc.", groups = {
			"US", "pim" ,"ITEM_MESSAGE"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verifyItemMessageforAvailabilityCodeIncAndSupplierCodeExc(Map<String, String> map) throws Throwable {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult();

		// verifying availability code
		String availabilitycode = productdetail.getItemField(map.get("fieldIndex"));
		Assertions.assertThat(availabilitycode).isEqualTo(map.get("AvailabilityCode"));
		log(LogType.INFO, "Verifying availability code");

		productdetail.selectTabfromDropdown(map.get("GlobalAttributeTab"));
		log(LogType.INFO, "Navigate to global attribute tab to verify supplier code");

		// verifying supplier code
		String suppliercode = globalattribute.getSuppliercode();
		Assertions.assertThat(suppliercode).isNotEqualTo(map.get("SupplierCode"));

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO, "Navigate to Classification tab after verifying the supplier code in US Catalog");

		classificationpage.clickOnExpandButton();
		znewglobalerrormessage = classificationpage.getStructureGroupHavingBlankSpace(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickStructureGroup();
		classificationpage.removeButton();
		classificationpage.clickYes();

		// verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		qualitystatus.sortRulestByLastExecution();
		List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("ruleStatus"));
		Assertions.assertThat(catalogentityrule).containsAnyOf(map.get("CatalogEntityRule"));
		log(LogType.INFO, "Verifying catalog entity rule");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		znewglobalerrormessage = classificationpage.getStructureGroupHavingBlankSpace(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickOnCloseExpandButton();

		// verify in master
		productdetail.clickMenuRefreshIcon();
		queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog"))
				.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult();

		// verifying availability code in master
		availabilitycode = productdetail.getItemField(map.get("fieldIndex"));
		Assertions.assertThat(availabilitycode).isEqualTo(map.get("AvailabilityCode"));
		log(LogType.INFO, "Verifying availability code in master");

		productdetail.selectTabfromDropdown(map.get("GlobalAttributeTab"));
		log(LogType.INFO, "Navigate to global attribute tab to verify supplier code in master");

		// verifying supplier code in master
		suppliercode = globalattribute.getSuppliercode();
		Assertions.assertThat(suppliercode).isNotEqualTo(map.get("SupplierCode"));

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO, "Navigate to Classification tab after verifying the supplier code in Master Catalog");

		classificationpage.clickOnExpandButton();
		znewglobalerrormessage = classificationpage.getStructureGroupHavingBlankSpace(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickOnCloseExpandButton();
		pimHomepage.clickLogoutButton();
	}

	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemMessage)
	@Test(description = "ITM_105 | Verifying Item Message based on multiple classification code", groups = { "US",
			"pim" ,"ITEM_MESSAGE"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verifyItemMessageforMultipleClassificationCode(Map<String, String> map) throws Throwable {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		List<String> itemcodelist = javautil.readMultipleValuesFromExcel(map.get("ItemNumber"));
		List<String> classificationcodelist = javautil.readMultipleValuesFromExcel(map.get("ClassificationCode"));

		for (int i = 0; i <= itemcodelist.size() - 1; i++) {
			pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
					.selectCatalogType(map.get("CatalogType"));
			queriessubmenu.enterHsiItemNumber(itemcodelist.get(i)).clickSeachButton().clickOnFirstResult()
					.selectTabfromDropdown(map.get("GlobalAttributeTab"));
			log(LogType.INFO, "Navigate to Global attribute tab to verify classification code in US Catalog");

			// verify classification code
			classificationcode = globalattribute.getClassificationCode();
			Assertions.assertThat(classificationcode).isEqualTo(classificationcodelist.get(i));
			productdetail.selectTabfromDropdown(map.get("Classificationtab"));
			log(LogType.INFO, "Navigate to Classification tab after verifying the classification code in US Catalog");

			classificationpage.clickOnExpandButton();
			classificationpage.sortStructureGroup();
			znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
					map.get("structurePath"));
			Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
			classificationpage.clickStructureGroup();
			classificationpage.removeButton();
			classificationpage.clickYes();

			// verify quality status
			pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
			qualitystatus.sortRulestByLastExecution();
			List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("ruleStatus"));
			Assertions.assertThat(catalogentityrule).containsAnyOf(map.get("CatalogEntityRule"));
			log(LogType.INFO, "Verifying catalog entity rule");

			pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
			pimHomepage.productDetailSearchPage().clickRefreshIcon();
			classificationpage.sortStructureGroup();
			znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
					map.get("structurePath"));
			Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
			classificationpage.clickOnCloseExpandButton();
			productdetail.clickMenuRefreshIcon();
		}

		// verify in master
		for (int j = 0; j <= itemcodelist.size() - 1; j++) {
			queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog"));
			queriessubmenu.enterHsiItemNumber(itemcodelist.get(j)).clickSeachButton().clickOnFirstResult()
					.selectTabfromDropdown(map.get("GlobalAttributeTab"));

			// verify classification code in master
			classificationcode = globalattribute.getClassificationCode();
			Assertions.assertThat(classificationcode).isEqualTo(classificationcodelist.get(j));
			productdetail.selectTabfromDropdown(map.get("Classificationtab"));
			log(LogType.INFO,
					"Navigate to Classification tab after verifying the classification code in Master Catalog");

			classificationpage.clickOnExpandButton();
			classificationpage.sortStructureGroup();
			znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
					map.get("structurePath"));
			Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
			classificationpage.clickOnCloseExpandButton();
			productdetail.clickMenuRefreshIcon();
		}
		pimHomepage.clickLogoutButton();
	}

	// ITM_120
	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.Data_Issue)
	@TestDataSheet(sheetname = TestCaseSheet.ItemMessage)
	@Test(description = "ITM_120 | Verifying Item Message based on MPC code inc. & Manufacture code inc.", groups = {
			"US", "pim","ITEM_MESSAGE" }, dataProvider = "getCatalogData", priority = 3, dataProviderClass = DataProviderUtils.class)

	public void verifyItemMessageforMPCCodeIncAndManufactureCodeInc(Map<String, String> map) throws Throwable {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult();

		// verifying mpc code
		String mpccode = productdetail.getItemField(map.get("fieldIndex"));
		Assertions.assertThat(mpccode).isEqualTo(map.get("MPC"));
		log(LogType.INFO, "Verifying mpc code");

		productdetail.selectTabfromDropdown(map.get("GlobalAttributeTab"));
		log(LogType.INFO, "Navigate to global attribute tab to verify manufacture code");

		// verifying manufacture code
		manufacturecode = globalattribute.getManufacturercode();
		Assertions.assertThat(manufacturecode).isEqualTo(map.get("ManufactureCode"));

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO, "Navigate to Classification tab after verifying the manufacture code in US Catalog");

		classificationpage.clickOnExpandButton();
		znewglobalerrormessage = classificationpage.getStructureGroupHavingBlankSpace(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickStructureGroup();
		classificationpage.removeButton();
		classificationpage.clickYes();

		// verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		qualitystatus.sortRulestByLastExecution();
		List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("ruleStatus"));
		Assertions.assertThat(catalogentityrule).containsAnyOf(map.get("CatalogEntityRule"));
		log(LogType.INFO, "Verifying catalog entity rule");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		znewglobalerrormessage = classificationpage.getStructureGroupHavingBlankSpace(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickOnCloseExpandButton();

		// verify in master
		productdetail.clickMenuRefreshIcon();
		queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog"))
				.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult();

		// verifying mpc code in master
		mpccode = productdetail.getItemField(map.get("fieldIndex"));
		Assertions.assertThat(mpccode).isEqualTo(map.get("MPC"));
		log(LogType.INFO, "Verifying mpc code in master");

		productdetail.selectTabfromDropdown(map.get("GlobalAttributeTab"));
		log(LogType.INFO, "Navigate to global attribute tab to verify manufacture code in master");

		// verifying manufacture code in master
		manufacturecode = globalattribute.getManufacturercode();
		Assertions.assertThat(manufacturecode).isEqualTo(map.get("ManufactureCode"));

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO, "Navigate to Classification tab after verifying the manufacture code in Master Catalog");

		classificationpage.clickOnExpandButton();
		znewglobalerrormessage = classificationpage.getStructureGroupHavingBlankSpace(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickOnCloseExpandButton();
		pimHomepage.clickLogoutButton();
	}

	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemMessage)
	@Test(description = "ITM_090 | Verifying Item Message based on mpc code inc. and multiple sub-mpc code inc.", groups = { "US",
			"pim","ITEM_MESSAGE" }, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verifyItemMessageforMpcCodeIncAndMultipleSubMpcCodeInc(Map<String, String> map) throws Throwable {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		List<String> itemcodelist = javautil.readMultipleValuesFromExcel(map.get("ItemNumber"));
		List<String> submpccodelist = javautil.readMultipleValuesFromExcel(map.get("SubMPC"));

		for (int i = 0; i <= itemcodelist.size() - 1; i++) {
			pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
					.selectCatalogType(map.get("CatalogType"));
			queriessubmenu.enterHsiItemNumber(itemcodelist.get(i)).clickSeachButton().clickOnFirstResult();

			// verifying mpc code
			String mpccode = productdetail.getItemField(map.get("fieldIndex"));
			Assertions.assertThat(mpccode).isEqualTo(map.get("MPC"));
			log(LogType.INFO, "Verifying mpc code");

			// verifying submpc code

			String submpccode = productdetail.getItemField(map.get("fieldIndex1"));
			Assertions.assertThat(submpccode).isEqualTo(submpccodelist.get(i));
			log(LogType.INFO, "Verifying sub-mpc code");

			pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
			log(LogType.INFO, "Navigate to Classification tab after verifying the mpc and sub-mpc code in US Catalog");

			classificationpage.clickOnExpandButton();
			classificationpage.sortStructureGroup();
			znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
					map.get("structurePath"));
			Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
			classificationpage.clickStructureGroup();
			classificationpage.removeButton();
			classificationpage.clickYes();

			// verify quality status
			pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
			qualitystatus.sortRulestByLastExecution();
			List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("ruleStatus"));
			Assertions.assertThat(catalogentityrule).containsAnyOf(map.get("CatalogEntityRule"));
			log(LogType.INFO, "Verifying catalog entity rule");

			pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
			pimHomepage.productDetailSearchPage().clickRefreshIcon();
			classificationpage.sortStructureGroup();
			znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
					map.get("structurePath"));
			Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
			classificationpage.clickOnCloseExpandButton();
			productdetail.clickMenuRefreshIcon();
		}

		// verify in master
		for (int j = 0; j <= itemcodelist.size() - 1; j++) {
			queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog"));
			queriessubmenu.enterHsiItemNumber(itemcodelist.get(j)).clickSeachButton().clickOnFirstResult();

			// verifying mpc code in master
			String mpccode = productdetail.getItemField(map.get("fieldIndex"));
			Assertions.assertThat(mpccode).isEqualTo(map.get("MPC"));
			log(LogType.INFO, "Verifying mpc code");

			// verifying submpc code in master
			String submpccode = productdetail.getItemField(map.get("fieldIndex1"));
			Assertions.assertThat(submpccode).isEqualTo(submpccodelist.get(j));
			log(LogType.INFO, "Verifying sub-mpc code");

			pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
			log(LogType.INFO, "Navigate to Classification tab after verifying the mpc and sub-mpc code in US Catalog");

			classificationpage.clickOnExpandButton();
			classificationpage.sortStructureGroup();
			znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
					map.get("structurePath"));
			Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
			classificationpage.clickOnCloseExpandButton();
			productdetail.clickMenuRefreshIcon();
		}
		pimHomepage.clickLogoutButton();
	}

	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemMessage)
	@Test(description = "ITM_063 | Verifying Item Message based on multiple hazardous class code", groups = { "US",
			"pim","ITEM_MESSAGE" }, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verifyItemMessageforMultipleHazardousCode(Map<String, String> map) throws Throwable {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		List<String> itemcodelist = javautil.readMultipleValuesFromExcel(map.get("ItemNumber"));
		List<String> hazardouscodelist = javautil.readMultipleValuesFromExcel(map.get("HazardousCode"));
		for (int i = 0; i <= itemcodelist.size() - 1; i++) {
			pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
					.selectCatalogType(map.get("CatalogType"));
			queriessubmenu.enterHsiItemNumber(itemcodelist.get(i)).clickSeachButton().clickOnFirstResult()
					.selectTabfromDropdown(map.get("GlobalAttributeTab"));
			log(LogType.INFO, "Navigate to Global attribute tab to verify supplier code in US Catalog");

			// verify hazardous code
			hazardouscode = globalattribute.getHazardousClassCode();
			Assertions.assertThat(hazardouscode).isEqualTo(hazardouscodelist.get(i));
			productdetail.selectTabfromDropdown(map.get("Classificationtab"));
			log(LogType.INFO, "Navigate to Classification tab after verifying the hazardous code in US Catalog");

			classificationpage.clickOnExpandButton();
			classificationpage.sortStructureGroup();
			znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
					map.get("structurePath"));
			Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
			classificationpage.clickStructureGroup();
			classificationpage.removeButton();
			classificationpage.clickYes();

			// verify quality status
			pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
			qualitystatus.sortRulestByLastExecution();
			List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("ruleStatus"));
			Assertions.assertThat(catalogentityrule).containsAnyOf(map.get("CatalogEntityRule"));
			log(LogType.INFO, "Verifying catalog entity rule");

			pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
			pimHomepage.productDetailSearchPage().clickRefreshIcon();
			classificationpage.sortStructureGroup();
			znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
					map.get("structurePath"));
			Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
			classificationpage.clickOnCloseExpandButton();
			productdetail.clickMenuRefreshIcon();
		}

		// verify in master
		for (int j = 0; j <= itemcodelist.size() - 1; j++) {
			queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog"));
			queriessubmenu.enterHsiItemNumber(itemcodelist.get(j)).clickSeachButton().clickOnFirstResult()
					.selectTabfromDropdown(map.get("GlobalAttributeTab"));

			// verify hazardous code
			hazardouscode = globalattribute.getHazardousClassCode();
			Assertions.assertThat(hazardouscode).isEqualTo(hazardouscodelist.get(j));
			productdetail.selectTabfromDropdown(map.get("Classificationtab"));
			log(LogType.INFO, "Navigate to Classification tab after verifying the hazardous code in Master Catalog");

			classificationpage.clickOnExpandButton();
			classificationpage.sortStructureGroup();
			znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
					map.get("structurePath"));
			Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
			classificationpage.clickOnCloseExpandButton();
			productdetail.clickMenuRefreshIcon();
		}
		pimHomepage.clickLogoutButton();
	}

	// ITM_047
	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemMessage)
	@Test(description = "ITM_047 | Verifying Item Message based on Availability code as inc. & Manufacturer Code as exc.", groups = {
			"US", "pim","ITEM_MESSAGE" }, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verifyItemMessageforAvailabilityCodeIncAndManufacturerCodeExc(Map<String, String> map)
			throws Throwable {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult();

		// verifying availability code
		String availabilitycode = productdetail.getItemField(map.get("fieldIndex"));
		Assertions.assertThat(availabilitycode).isEqualTo(map.get("AvailabilityCode"));
		log(LogType.INFO, "Verifying availability code");

		productdetail.selectTabfromDropdown(map.get("GlobalAttributeTab"));
		log(LogType.INFO, "Navigate to global attribute tab to verify manufacturer code");

		// verifying manufacture code
		String manufacturercode = globalattribute.getManufacturercode();
		Assertions.assertThat(manufacturercode).isNotEqualTo(map.get("ManufactureCode"));

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO, "Navigate to Classification tab after verifying the manufacturer code in US Catalog");

		classificationpage.clickOnExpandButton();
		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickStructureGroup();
		classificationpage.removeButton();
		classificationpage.clickYes();

		// verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		qualitystatus.sortRulestByLastExecution();
		List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("ruleStatus"));
		Assertions.assertThat(catalogentityrule).containsAnyOf(map.get("CatalogEntityRule"));
		log(LogType.INFO, "Verifying catalog entity rule");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickOnCloseExpandButton();

		// verify in master
		productdetail.clickMenuRefreshIcon();
		queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog"))
				.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult();

		// verifying availability code in master
		availabilitycode = productdetail.getItemField(map.get("fieldIndex"));
		Assertions.assertThat(availabilitycode).isEqualTo(map.get("AvailabilityCode"));
		log(LogType.INFO, "Verifying availability code in master");

		productdetail.selectTabfromDropdown(map.get("GlobalAttributeTab"));
		log(LogType.INFO, "Navigate to global attribute tab to verify manufacturer code in master");

		// verifying manufacturer code in master
		manufacturercode = globalattribute.getManufacturercode();
		Assertions.assertThat(manufacturercode).isNotEqualTo(map.get("ManufactureCode"));

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO, "Navigate to Classification tab after verifying the manufacturer code in Master Catalog");

		classificationpage.clickOnExpandButton();
		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickOnCloseExpandButton();
		pimHomepage.clickLogoutButton();
	}

	// ITM_051
	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemMessage)
	@Test(description = "ITM_051 | Verifying Item Message based on ItemCode which do not have Hazardous code as OA", groups = {
			"US", "pim","ITEM_MESSAGE" }, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verifyItemMessageforItemCodeHavingHazardousCodeIsNotOA(Map<String, String> map) throws Throwable {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult();

		productdetail.selectTabfromDropdown(map.get("GlobalAttributeTab"));
		log(LogType.INFO, "Navigate to global attribute tab to verify hazardous code");

		// verifying hazardous code
		String hazardouscode = globalattribute.getHazardousClassCode();
		Assertions.assertThat(hazardouscode).isNotEqualTo(map.get("HazardousCode"));

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO, "Navigate to Classification tab after verifying the hazardous code in US Catalog");

		classificationpage.clickOnExpandButton();
		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickStructureGroup();
		classificationpage.removeButton();
		classificationpage.clickYes();

		// verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		qualitystatus.sortRulestByLastExecution();
		List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("ruleStatus"));
		Assertions.assertThat(catalogentityrule).containsAnyOf(map.get("CatalogEntityRule"));
		log(LogType.INFO, "Verifying catalog entity rule");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickOnCloseExpandButton();

		// verify in master
		productdetail.clickMenuRefreshIcon();
		queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog"))
				.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult();

		productdetail.selectTabfromDropdown(map.get("GlobalAttributeTab"));
		log(LogType.INFO, "Navigate to global attribute tab to verify hazardous code in master");

		// verifying hazardous code in master
		hazardouscode = globalattribute.getHazardousClassCode();
		Assertions.assertThat(hazardouscode).isNotEqualTo(map.get("HazardousCode"));

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO, "Navigate to Classification tab after verifying the hazardous code in Master Catalog");

		classificationpage.clickOnExpandButton();
		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickOnCloseExpandButton();
		pimHomepage.clickLogoutButton();
	}

	// ITM_079
	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemMessage)
	@Test(description = "ITM_079 | Verifying Item Message based on mpc code inc. and sub-mpc code inc.", groups = { "US",
			"pim","ITEM_MESSAGE" }, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verifyItemMessageforMpcCodeIncSubMpcCodeInc(Map<String, String> map) throws Throwable {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();

		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType"));
		queriessubmenu.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult();

		// verifying mpc code
		String mpccode = productdetail.getItemField(map.get("fieldIndex"));
		Assertions.assertThat(mpccode).isEqualTo(map.get("MPC"));
		log(LogType.INFO, "Verifying mpc code");

		// verifying submpc code
		String submpccode = productdetail.getItemField(map.get("fieldIndex1"));
		Assertions.assertThat(submpccode).isEqualTo(map.get("SubMPC"));
		log(LogType.INFO, "Verifying sub-mpc code");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO, "Navigate to Classification tab after verifying the mpc and sub-mpc code in US Catalog");

		classificationpage.clickOnExpandButton();
		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickStructureGroup();
		classificationpage.removeButton();
		classificationpage.clickYes();

		// verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		qualitystatus.sortRulestByLastExecution();
		List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("ruleStatus"));
		Assertions.assertThat(catalogentityrule).containsAnyOf(map.get("CatalogEntityRule"));
		log(LogType.INFO, "Verifying catalog entity rule");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickOnCloseExpandButton();

		// verify in master
		productdetail.clickMenuRefreshIcon();
		queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog"))
				.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult();

		// verifying mpc code in master
		mpccode = productdetail.getItemField(map.get("fieldIndex"));
		Assertions.assertThat(mpccode).isEqualTo(map.get("MPC"));
		log(LogType.INFO, "Verifying mpc code");

		// verifying submpc code in master
		submpccode = productdetail.getItemField(map.get("fieldIndex1"));
		Assertions.assertThat(submpccode).isEqualTo(map.get("SubMPC"));
		log(LogType.INFO, "Verifying sub-mpc code");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO, "Navigate to Classification tab after verifying the mpc and sub-mpc code in Master Catalog");

		classificationpage.clickOnExpandButton();
		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
				map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
		classificationpage.clickOnCloseExpandButton();
		pimHomepage.clickLogoutButton();
	}
	
}
