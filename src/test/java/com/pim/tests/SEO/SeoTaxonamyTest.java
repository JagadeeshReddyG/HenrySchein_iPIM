package com.pim.tests.SEO;

import static com.pim.reports.FrameworkLogger.log;

import java.util.Map;

import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import com.pim.annotations.PimFrameworkAnnotation;
import com.pim.annotations.TestDataSheet;
import com.pim.enums.CategoryType;
import com.pim.enums.LogType;
import com.pim.enums.Modules;
import com.pim.enums.TestCaseSheet;
import com.pim.pages.FieldSelectionPage;
import com.pim.pages.GlobalAttributePage;
import com.pim.pages.LocalAttributePage;
import com.pim.pages.LoginPage;
import com.pim.pages.PimHomepage;
import com.pim.pages.ProductDetailSearchPage;
import com.pim.pages.QualityStatusPage;
import com.pim.pages.QueriesSubMenu;
import com.pim.pages.SeoTabPage;
import com.pim.pages.StructureSubMenu;
import com.pim.pages.WebDescription;
import com.pim.tests.BaseTest;
import com.pim.utils.DataProviderUtils;
import com.pim.utils.DateandTimeUtils;
import com.pim.utils.ExcelUtils;
import com.pim.utils.TextFileUtils;

public final class SeoTaxonamyTest extends BaseTest {
	private SeoTaxonamyTest() {

	}

	LocalAttributePage localAttributePage = new LocalAttributePage();
	ProductDetailSearchPage productDetailSearchPage = new ProductDetailSearchPage();
	QualityStatusPage qualityStatusPage = new QualityStatusPage();
	SeoTabPage seoTabPage = new SeoTabPage();
	GlobalAttributePage globalAttributePage = new GlobalAttributePage();
	WebDescription webDescription = new WebDescription();
	QueriesSubMenu queriesSubMenu = new QueriesSubMenu();
	FieldSelectionPage fieldselectionpage = new FieldSelectionPage();
	StructureSubMenu structureSubMenu = new StructureSubMenu();
	PimHomepage pimHomepage = new PimHomepage();
	LoginPage loginPage = new LoginPage();

	// SEO_TAX_001
	@PimFrameworkAnnotation(module = Modules.SEO, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.SEO_Taxnomy)
	@Test(description = "SEO_TAX_001 | Verify if Default rules are populated when no change made to Taxonomy ", groups = {
			"US","pim","SANITY","SEO"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void Verify_if_Default_rules_are_populated_when_no_change_made_to_Taxonomy_For_1000(
			Map<String, String> map) {

		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickStructuresMenu().selectCatalogType(map.get("CatalogType"))
				.selectClassificationType(map.get("CategoryType")).selectItemType(map.get("ItemType"));
		structureSubMenu.clickOnSelectedTaxnomyType(map.get("CategoryType"));
		productDetailSearchPage.applyFilterInSearchPage(map.get("SubSetOfTaxanomy")).clickOnFirstResult()
				.selectTabfromDropdown(map.get("TabName"));
		seoTabPage.clickRetrieveRules();
		// Default Rule From Page Title Attribute
		String ActualDefaultRuleFromPageTitle = seoTabPage.getAttributesFromPageTitle();
		String ExpectedDefaultRuleFromPageTitle = map.get("AttributeForPageTitle");
		Assertions.assertThat(ActualDefaultRuleFromPageTitle).isEqualTo(ExpectedDefaultRuleFromPageTitle);
		// Default Rule From Meta Data
		String ActualDefaultRuleFromMetaData = seoTabPage.getAttributesFromMetaData();
		String ExpectedDefaultRuleFromMetaData = map.get("AttributeForMetaData");
		Assertions.assertThat(ActualDefaultRuleFromMetaData).isEqualTo(ExpectedDefaultRuleFromMetaData);
		// Default Rule From Keywords
		String ActualDefaultRuleFromKeywords = seoTabPage.getAttributesFromKeywords();
		String ExpectedDefaultRuleFromKeywords = map.get("AttributeForKeyword");
		Assertions.assertThat(ActualDefaultRuleFromKeywords).isEqualTo(ExpectedDefaultRuleFromKeywords);
		// Default Rule From Description
		String ActualDefaultRuleFromDescription = seoTabPage.getAttributesFromDescription();
		String ExpectedDefaultRuleFromDescription = map.get("AttributeforDescription");
		Assertions.assertThat(ActualDefaultRuleFromDescription).isEqualTo(ExpectedDefaultRuleFromDescription);
		// Default Rule From H1Tag
		String ActualDefaultRuleFromH1Tag = seoTabPage.getAttributesFromH1Tag();
		String ExpectedDefaultRuleFromH1Tag = map.get("AttributeForH1Tag");
		Assertions.assertThat(ActualDefaultRuleFromH1Tag).isEqualTo(ExpectedDefaultRuleFromH1Tag);
		// Default Rule From OwnerOfCategory
		String ActualDefaultRuleFromOwnerOfCategory = seoTabPage.getAttributesFromOwnerOfCatogoryForTaxnomy();
		String ExpectedDefaultRuleFromOwnerOfCategory = map.get("AttributeForOwnerOfCategory");
		Assertions.assertThat(ActualDefaultRuleFromOwnerOfCategory).isEqualTo(ExpectedDefaultRuleFromOwnerOfCategory);
	}

	// SEO_TAX_002
	@PimFrameworkAnnotation(module = Modules.SEO, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.SEO_Taxnomy)
	@Test(description = "SEO_TAX_002 | Verify Overiding the default Rule ", dataProvider = "getCatalogData", groups = {
			"US","pim","SANITY","SEO"}, dataProviderClass = DataProviderUtils.class)
	public void Verify_if_User_can_Override_the_default_rule_Taxnomy_for_5200(Map<String, String> map) {

		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickStructuresMenu().selectCatalogType(map.get("CatalogType"))
				.selectClassificationType(map.get("CategoryType")).selectItemType(map.get("ItemType"));
		structureSubMenu.clickOnSelectedTaxnomyType(map.get("CategoryType"));

		productDetailSearchPage.applyFilterInSearchPage(map.get("SubSetOfTaxanomy")).clickOnFirstResult()
				.selectTabfromDropdown(map.get("TabName"));
		seoTabPage.clickRetrieveRules();
		// Overiding the default Rule for Pagetitle
		seoTabPage.deletingValueFromPageTitle(map.get("DeletingAttributeForPageTitle"));
		String DefaultRuleFromPageTitle = seoTabPage.getAttributesFromPageTitle();
		seoTabPage.addingAttributeFromPageTitleFieldAttribute(map.get("AddingAttributeForPageTitle"));
		String OverideDefaultRuleFromPageTitle = seoTabPage.getAttributesFromPageTitle();
		Assertions.assertThat(DefaultRuleFromPageTitle).isNotEqualTo(OverideDefaultRuleFromPageTitle);
		String DescriptionValueFromPageTitle = seoTabPage.getDescriptionValueFromPageTitle();
		Assertions.assertThat(DescriptionValueFromPageTitle).contains(map.get("AddingAttributeForPageTitle"));
		// Overiding the default Rule for MetaData
		seoTabPage.deletingValueFromMetaData(map.get("DeletingAttributeFromMetaData"));
		String DefaultRuleFromMetaData = seoTabPage.getAttributesFromMetaData();
		seoTabPage.AddingAttributeFromMetaData_MetaDescription(map.get("AddingAttributeForMetaData"));
		String OverideDefaultRuleFromMetaData = seoTabPage.getAttributesFromMetaData();
		Assertions.assertThat(DefaultRuleFromMetaData).isNotEqualTo(OverideDefaultRuleFromMetaData);
		String DescriptionValueFromMetaData = seoTabPage.getDescriptionValueFromMetaData();
		Assertions.assertThat(DescriptionValueFromMetaData).contains(map.get("AddingAttributeForMetaData"));
		// Overiding the default Rule for KeyWords
		seoTabPage.deletingValueFromKeywords(map.get("DeletingAttributeForKeywords"));
		String DefaultRuleFromKeywords = seoTabPage.getAttributesFromKeywords();
		seoTabPage.AddingAttributeFromKeywords_MetaKeywords(map.get("AddingAttributeForKeywords"));
		String OverideDefaultRuleFromKeywords = seoTabPage.getAttributesFromKeywords();
		Assertions.assertThat(DefaultRuleFromKeywords).isNotEqualTo(OverideDefaultRuleFromKeywords);
		String DescriptionValueFromKeywords = seoTabPage.getDescriptionValueFromKeywords();
		Assertions.assertThat(DescriptionValueFromKeywords).contains(map.get("AddingAttributeForKeywords"));
		// Overiding the default Rule for Description
		seoTabPage.deletingValueFromDescription(map.get("DeletingAttributeForDescription"));
		String DefaultRuleFromDescription = seoTabPage.getAttributesFromDescription();
		seoTabPage
				.addingAttributeFromDescription_ProductorCategorydescription(map.get("AddingAttributeForDescription"));
		String OverideDefaultRuleFromDescription = seoTabPage.getAttributesFromDescription();
		Assertions.assertThat(DefaultRuleFromDescription).isNotEqualTo(OverideDefaultRuleFromDescription);
		String DescriptionValueFromDescription = seoTabPage.getDescriptionValueDescription();
		Assertions.assertThat(DescriptionValueFromDescription).contains(map.get("AddingAttributeForDescription"));
		// Overiding the default Rule for H1Tag
		seoTabPage.deletingValueFromH1Tag(map.get("DeletingAttributeforH1Tag"));
		String DefaultRuleFromH1Tag = seoTabPage.getAttributesFromH1Tag();
		seoTabPage.AddingAttributeFromH1_Tag(map.get("AddingAttributeforH1Tag"));
		String OverideDefaultRuleFromH1Tag = seoTabPage.getAttributesFromH1Tag();
		Assertions.assertThat(DefaultRuleFromH1Tag).isNotEqualTo(OverideDefaultRuleFromH1Tag);
		String DescriptionValueFromH1Tag = seoTabPage.getDescriptionValueH1Tag();
		Assertions.assertThat(DescriptionValueFromH1Tag).contains(map.get("AddingAttributeforH1Tag"));

	}

	// SEO_TAX_003
	@PimFrameworkAnnotation(module = Modules.SEO, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.SEO_Taxnomy)
	@Test(description = "SEO_TAX_003 | verifying the main node and sibling node after overiding the rule in main node ", groups = {
			"US","pim","SEO" }, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verifying_the_node_belonging_to_same_level_shows_default_rule_on_overriding_its_sibling_with_overridden_rule_parameter_Taxnomy_for_1000_And_3600(
			Map<String, String> map) {

		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickStructuresMenu().selectCatalogType(map.get("CatalogType"))
				.selectClassificationType(map.get("CategoryType")).selectItemType(map.get("ItemType"));
		structureSubMenu.clickOnSelectedTaxnomyType(map.get("CategoryType"));

		productDetailSearchPage.applyFilterInSearchPage(map.get("SubSetOfTaxanomy")).clickOnFirstResult()
				.selectTabfromDropdown(map.get("TabName"));
		seoTabPage.clickRetrieveRules();

		// Overiding the default Rule for Pagetitle
		seoTabPage.deletingValueFromPageTitle(map.get("DeletingAttributeForPageTitle"));
		String DefaultRuleFromPageTitle = seoTabPage.getAttributesFromPageTitle();
		seoTabPage.addingAttributeFromPageTitleFieldAttribute(map.get("AddingAttributeForPageTitle"));
		String OverideDefaultRuleFromPageTitle = seoTabPage.getAttributesFromPageTitle();
		Assertions.assertThat(DefaultRuleFromPageTitle).isNotEqualTo(OverideDefaultRuleFromPageTitle);
		String DescriptionValueFromPageTitle = seoTabPage.getDescriptionValueFromPageTitle();
		Assertions.assertThat(DescriptionValueFromPageTitle).contains(map.get("AddingAttributeForPageTitle"));
		// Overiding the default Rule for MetaData
		seoTabPage.deletingValueFromMetaData(map.get("DeletingAttributeFromMetaData"));
		String DefaultRuleFromMetaData = seoTabPage.getAttributesFromMetaData();
		seoTabPage.AddingAttributeFromMetaData_MetaDescription(map.get("AddingAttributeForMetaData"));
		String OverideDefaultRuleFromMetaData = seoTabPage.getAttributesFromMetaData();
		Assertions.assertThat(DefaultRuleFromMetaData).isNotEqualTo(OverideDefaultRuleFromMetaData);
		String DescriptionValueFromMetaData = seoTabPage.getDescriptionValueFromMetaData();
		Assertions.assertThat(DescriptionValueFromMetaData).contains(map.get("AddingAttributeForMetaData"));
		// Overiding the default Rule for KeyWords
		seoTabPage.deletingValueFromKeywords(map.get("DeletingAttributeForKeywords"));
		String DefaultRuleFromKeywords = seoTabPage.getAttributesFromKeywords();
		seoTabPage.AddingAttributeFromKeywords_MetaKeywords(map.get("AddingAttributeForKeywords"));
		String OverideDefaultRuleFromKeywords = seoTabPage.getAttributesFromKeywords();
		Assertions.assertThat(DefaultRuleFromKeywords).isNotEqualTo(OverideDefaultRuleFromKeywords);
		String DescriptionValueFromKeywords = seoTabPage.getDescriptionValueFromKeywords();
		Assertions.assertThat(DescriptionValueFromKeywords).contains(map.get("AddingAttributeForKeywords"));
		// Overiding the default Rule for Description
		seoTabPage.deletingValueFromDescription(map.get("DeletingAttributeForDescription"));
		String DefaultRuleFromDescription = seoTabPage.getAttributesFromDescription();
		seoTabPage
				.addingAttributeFromDescription_ProductorCategorydescription(map.get("AddingAttributeForDescription"));
		String OverideDefaultRuleFromDescription = seoTabPage.getAttributesFromDescription();
		Assertions.assertThat(DefaultRuleFromDescription).isNotEqualTo(OverideDefaultRuleFromDescription);
		String DescriptionValueFromDescription = seoTabPage.getDescriptionValueDescription();
		Assertions.assertThat(DescriptionValueFromDescription).contains(map.get("AddingAttributeForDescription"));
		// Overiding the default Rule for H1Tag
		seoTabPage.deletingValueFromH1Tag(map.get("DeletingAttributeforH1Tag"));
		String DefaultRuleFromH1Tag = seoTabPage.getAttributesFromH1Tag();
		seoTabPage.AddingAttributeFromH1_Tag(map.get("AddingAttributeforH1Tag"));
		String OverideDefaultRuleFromH1Tag = seoTabPage.getAttributesFromH1Tag();
		Assertions.assertThat(DefaultRuleFromH1Tag).isNotEqualTo(OverideDefaultRuleFromH1Tag);
		String DescriptionValueFromH1Tag = seoTabPage.getDescriptionValueH1Tag();
		Assertions.assertThat(DescriptionValueFromH1Tag).contains(map.get("AddingAttributeforH1Tag"));

		// Navigating to the Sibling Node 3600

		structureSubMenu.clickOnSelectedTaxnomyType(map.get("CategoryType"));

		productDetailSearchPage.applyFilterInSearchPage(map.get("SibblingSubSet")).clickOnFirstResult()
				.selectTabfromDropdown(map.get("TextTab")).selectTabfromDropdown(map.get("TabName"));
		seoTabPage.clickRetrieveRules();

		// Default Rule From Page Title Attribute
		String ActualDefaultRuleFromPageTitle = seoTabPage.getAttributesFromPageTitle();
		String ExpectedDefaultRuleFromPageTitle = map.get("AttributeForPageTitle");
		Assertions.assertThat(ActualDefaultRuleFromPageTitle).isNotEqualTo(OverideDefaultRuleFromPageTitle);
		Assertions.assertThat(ActualDefaultRuleFromPageTitle).isEqualTo(ExpectedDefaultRuleFromPageTitle);
		// Default Rule From Meta Data
		String ActualDefaultRuleFromMetaData = seoTabPage.getAttributesFromMetaData();
		String ExpectedDefaultRuleFromMetaData = map.get("AttributeForMetaData");
		Assertions.assertThat(ActualDefaultRuleFromMetaData).isNotEqualTo(OverideDefaultRuleFromMetaData);
		Assertions.assertThat(ActualDefaultRuleFromMetaData).isEqualTo(ExpectedDefaultRuleFromMetaData);
		// Default Rule From Keywords
		String ActualDefaultRuleFromKeywords = seoTabPage.getAttributesFromKeywords();
		String ExpectedDefaultRuleFromKeywords = map.get("AttributeForKeyword");
		Assertions.assertThat(ActualDefaultRuleFromKeywords).isNotEqualTo(OverideDefaultRuleFromKeywords);
		Assertions.assertThat(ActualDefaultRuleFromKeywords).isEqualTo(ExpectedDefaultRuleFromKeywords);
		// Default Rule From Description
		String ActualDefaultRuleFromDescription = seoTabPage.getAttributesFromDescription();
		String ExpectedDefaultRuleFromDescription = map.get("AttributeforDescription");
		Assertions.assertThat(ActualDefaultRuleFromDescription).isNotEqualTo(OverideDefaultRuleFromDescription);
		Assertions.assertThat(ActualDefaultRuleFromDescription).isEqualTo(ExpectedDefaultRuleFromDescription);
		// Default Rule From H1Tag
		String ActualDefaultRuleFromH1Tag = seoTabPage.getAttributesFromH1Tag();
		String ExpectedDefaultRuleFromH1Tag = map.get("AttributeForH1Tag");
		Assertions.assertThat(ActualDefaultRuleFromH1Tag).isNotEqualTo(OverideDefaultRuleFromH1Tag);
		Assertions.assertThat(ActualDefaultRuleFromH1Tag).isEqualTo(ExpectedDefaultRuleFromH1Tag);
		// Default Rule From OwnerOfCategory
		String ActualDefaultRuleFromOwnerOfCategory = seoTabPage.getAttributesFromOwnerOfCatogoryForTaxnomy();
		String ExpectedDefaultRuleFromOwnerOfCategory = map.get("AttributeForOwnerOfCategory");
		Assertions.assertThat(ActualDefaultRuleFromOwnerOfCategory).isEqualTo(ExpectedDefaultRuleFromOwnerOfCategory);

	}

	// SEO_TAX_004
	@PimFrameworkAnnotation(module = Modules.SEO, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.SEO_Taxnomy)
	@Test(description = "SEO_TAX_004 | verifying the deault rule No changes are done in Taxnomy ", groups = {
			"US","pim","SANITY","SEO" }, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void Verify_if_Default_rules_are_populated_when_no_change_made_to_Taxonomy_for_5600_5000(
			Map<String, String> map) {

		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickStructuresMenu().selectCatalogType(map.get("CatalogType"))
				.selectClassificationType(map.get("CategoryType")).selectItemType(map.get("ItemType"));
		structureSubMenu.clickOnSelectedTaxnomyType(map.get("CategoryType"))
				.enterFilterBySerachGroupName(map.get("SubSetOfTaxanomy"))
				.selectSubCategory(map.get("SubSetOfTaxanomy")).clickOnShowAll();

		productDetailSearchPage.applyFilterInSearchPage(map.get("SubSetOfLevel1")).clickOnFirstResult()
				.selectTabfromDropdown(map.get("TabName"));
		seoTabPage.clickRetrieveRules();
		// Default Rule From Page Title Attribute
		seoTabPage.deletingValueFromPageTitle(map.get("DeletingAttributeForPageTitle"));
		String ActualDefaultRuleFromPageTitle = seoTabPage.getAttributesFromPageTitle();
		String ExpectedDefaultRuleFromPageTitle = map.get("AttributeForPageTitle");
		Assertions.assertThat(ActualDefaultRuleFromPageTitle).isEqualTo(ExpectedDefaultRuleFromPageTitle);
		// Default Rule From Meta Data
		seoTabPage.deletingValueFromMetaData(map.get("DeletingAttributeFromMetaData"));
		String ActualDefaultRuleFromMetaData = seoTabPage.getAttributesFromMetaData();
		String ExpectedDefaultRuleFromMetaData = map.get("AttributeForMetaData");
		Assertions.assertThat(ActualDefaultRuleFromMetaData).isEqualTo(ExpectedDefaultRuleFromMetaData);
		// Default Rule From Keywords
		seoTabPage.deletingValueFromKeywords(map.get("DeletingAttributeForKeywords"));
		String ActualDefaultRuleFromKeywords = seoTabPage.getAttributesFromKeywords();
		String ExpectedDefaultRuleFromKeywords = map.get("AttributeForKeyword");
		Assertions.assertThat(ActualDefaultRuleFromKeywords).isEqualTo(ExpectedDefaultRuleFromKeywords);
		// Default Rule From Description

		seoTabPage.deletingValueFromDescription(map.get("DeletingAttributeForDescription"));
		String ActualDefaultRuleFromDescription = seoTabPage.getAttributesFromDescription();
		String ExpectedDefaultRuleFromDescription = map.get("AttributeforDescription");
		Assertions.assertThat(ActualDefaultRuleFromDescription).isEqualTo(ExpectedDefaultRuleFromDescription);
		// Default Rule From H1Tag
		seoTabPage.deletingValueFromH1Tag(map.get("DeletingAttributeforH1Tag"));
		String ActualDefaultRuleFromH1Tag = seoTabPage.getAttributesFromH1Tag();
		String ExpectedDefaultRuleFromH1Tag = map.get("AttributeForH1Tag");
		Assertions.assertThat(ActualDefaultRuleFromH1Tag).isEqualTo(ExpectedDefaultRuleFromH1Tag);
		// Default Rule From OwnerOfCategory
		String ActualDefaultRuleFromOwnerOfCategory = seoTabPage.getAttributesFromOwnerOfCatogoryForTaxnomy();
		String ExpectedDefaultRuleFromOwnerOfCategory = map.get("AttributeForOwnerOfCategory");
		Assertions.assertThat(ActualDefaultRuleFromOwnerOfCategory).isEqualTo(ExpectedDefaultRuleFromOwnerOfCategory);

	}

	// SEO_TAX_005
	@PimFrameworkAnnotation(module = Modules.SEO, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.SEO_Taxnomy)
	@Test(description = "SEO_TAX_005 | Verify Overiding the default Rule ", dataProvider = "getCatalogData", groups = {
			"US","pim","SANITY","SEO" }, dataProviderClass = DataProviderUtils.class)
	public void Verify_if_User_can_Override_the_default_rule_Taxnomy_for_5600_5000(Map<String, String> map) {

		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickStructuresMenu().selectCatalogType(map.get("CatalogType"))
				.selectClassificationType(map.get("CategoryType")).selectItemType(map.get("ItemType"));
		structureSubMenu.clickOnSelectedTaxnomyType(map.get("CategoryType"))
				.enterFilterBySerachGroupName(map.get("SubSetOfTaxanomy"))
				.selectSubCategory(map.get("SubSetOfTaxanomy"));
		productDetailSearchPage.applyFilterInSearchPage(map.get("SubSetOfLevel1")).clickOnFirstResult()
				.selectTabfromDropdown(map.get("TabName"));
		seoTabPage.clickRetrieveRules();

		// Overiding the default Rule for Pagetitle
		seoTabPage.deletingValueFromPageTitle(map.get("DeletingAttributeForPageTitle"));
		String DefaultRuleFromPageTitle = seoTabPage.getAttributesFromPageTitle();
		seoTabPage.addingAttributeFromPageTitleFieldAttribute(map.get("AddingAttributeForPageTitle"));
		String OverideDefaultRuleFromPageTitle = seoTabPage.getAttributesFromPageTitle();
		Assertions.assertThat(DefaultRuleFromPageTitle).isNotEqualTo(OverideDefaultRuleFromPageTitle);
		String DescriptionValueFromPageTitle = seoTabPage.getDescriptionValueFromPageTitle();
		Assertions.assertThat(DescriptionValueFromPageTitle).contains(map.get("AddingAttributeForPageTitle"));
		// Overiding the default Rule for MetaData
		seoTabPage.deletingValueFromMetaData(map.get("DeletingAttributeFromMetaData"));
		String DefaultRuleFromMetaData = seoTabPage.getAttributesFromMetaData();
		seoTabPage.AddingAttributeFromMetaData_MetaDescription(map.get("AddingAttributeForMetaData"));
		String OverideDefaultRuleFromMetaData = seoTabPage.getAttributesFromMetaData();
		Assertions.assertThat(DefaultRuleFromMetaData).isNotEqualTo(OverideDefaultRuleFromMetaData);
		String DescriptionValueFromMetaData = seoTabPage.getDescriptionValueFromMetaData();
		Assertions.assertThat(DescriptionValueFromMetaData).contains(map.get("AddingAttributeForMetaData"));
		// Overiding the default Rule for KeyWords
		seoTabPage.deletingValueFromKeywords(map.get("DeletingAttributeForKeywords"));
		String DefaultRuleFromKeywords = seoTabPage.getAttributesFromKeywords();
		seoTabPage.AddingAttributeFromKeywords_MetaKeywords(map.get("AddingAttributeForKeywords"));
		String OverideDefaultRuleFromKeywords = seoTabPage.getAttributesFromKeywords();
		Assertions.assertThat(DefaultRuleFromKeywords).isNotEqualTo(OverideDefaultRuleFromKeywords);
		String DescriptionValueFromKeywords = seoTabPage.getDescriptionValueFromKeywords();
		Assertions.assertThat(DescriptionValueFromKeywords).contains(map.get("AddingAttributeForKeywords"));
		// Overiding the default Rule for Description
		seoTabPage.deletingValueFromDescription(map.get("DeletingAttributeForDescription"));
		String DefaultRuleFromDescription = seoTabPage.getAttributesFromDescription();
		seoTabPage
				.addingAttributeFromDescription_ProductorCategorydescription(map.get("AddingAttributeForDescription"));
		String OverideDefaultRuleFromDescription = seoTabPage.getAttributesFromDescription();
		Assertions.assertThat(DefaultRuleFromDescription).isNotEqualTo(OverideDefaultRuleFromDescription);
		String DescriptionValueFromDescription = seoTabPage.getDescriptionValueDescription();
		Assertions.assertThat(DescriptionValueFromDescription).contains(map.get("AddingAttributeForDescription"));
		// Overiding the default Rule for H1Tag
		seoTabPage.deletingValueFromH1Tag(map.get("DeletingAttributeforH1Tag"));
		String DefaultRuleFromH1Tag = seoTabPage.getAttributesFromH1Tag();
		seoTabPage.AddingAttributeFromH1_Tag(map.get("AddingAttributeforH1Tag"));
		String OverideDefaultRuleFromH1Tag = seoTabPage.getAttributesFromH1Tag();
		Assertions.assertThat(DefaultRuleFromH1Tag).isNotEqualTo(OverideDefaultRuleFromH1Tag);
		String DescriptionValueFromH1Tag = seoTabPage.getDescriptionValueH1Tag();
		Assertions.assertThat(DescriptionValueFromH1Tag).contains(map.get("AddingAttributeforH1Tag"));

	}

	// SEO_TAX_006
	@PimFrameworkAnnotation(module = Modules.SEO, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.SEO_Taxnomy)
	@Test(description = "SEO_TAX_006 | verifying the main node and sibling node after overiding the rule in main node ", groups = {
			"US","pim","SEO"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verifying_the_node_belonging_to_same_level_shows_default_rule_on_overriding_its_sibling_with_overridden_rule_parameter_Taxnomy_For_1000_6000_TO_1000_3000(
			Map<String, String> map) {

		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickStructuresMenu().selectCatalogType(map.get("CatalogType"))
				.selectClassificationType(map.get("CategoryType")).selectItemType(map.get("ItemType"));
		structureSubMenu.clickOnSelectedTaxnomyType(map.get("CategoryType"))
				.selectSubCategory(map.get("SubSetOfTaxanomy"));

		productDetailSearchPage.applyFilterInSearchPage(map.get("SubSetOfLevel1")).clickOnFirstResult()
				.selectTabfromDropdown(map.get("TabName"));
		seoTabPage.clickRetrieveRules();

		// Overiding the default Rule for Pagetitle
		seoTabPage.deletingValueFromPageTitle(map.get("DeletingAttributeForPageTitle"));
		String DefaultRuleFromPageTitle = seoTabPage.getAttributesFromPageTitle();
		seoTabPage.addingAttributeFromPageTitleFieldAttribute(map.get("AddingAttributeForPageTitle"));
		String OverideDefaultRuleFromPageTitle = seoTabPage.getAttributesFromPageTitle();
		Assertions.assertThat(DefaultRuleFromPageTitle).isNotEqualTo(OverideDefaultRuleFromPageTitle);
		String DescriptionValueFromPageTitle = seoTabPage.getDescriptionValueFromPageTitle();
		Assertions.assertThat(DescriptionValueFromPageTitle).contains(map.get("AddingAttributeForPageTitle"));
		// Overiding the default Rule for MetaData
		seoTabPage.deletingValueFromMetaData(map.get("DeletingAttributeFromMetaData"));
		String DefaultRuleFromMetaData = seoTabPage.getAttributesFromMetaData();
		seoTabPage.AddingAttributeFromMetaData_MetaDescription(map.get("AddingAttributeForMetaData"));
		String OverideDefaultRuleFromMetaData = seoTabPage.getAttributesFromMetaData();
		Assertions.assertThat(DefaultRuleFromMetaData).isNotEqualTo(OverideDefaultRuleFromMetaData);
		String DescriptionValueFromMetaData = seoTabPage.getDescriptionValueFromMetaData();
		Assertions.assertThat(DescriptionValueFromMetaData).contains(map.get("AddingAttributeForMetaData"));
		// Overiding the default Rule for KeyWords
		seoTabPage.deletingValueFromKeywords(map.get("DeletingAttributeForKeywords"));
		String DefaultRuleFromKeywords = seoTabPage.getAttributesFromKeywords();
		seoTabPage.AddingAttributeFromKeywords_MetaKeywords(map.get("AddingAttributeForKeywords"));
		String OverideDefaultRuleFromKeywords = seoTabPage.getAttributesFromKeywords();
		Assertions.assertThat(DefaultRuleFromKeywords).isNotEqualTo(OverideDefaultRuleFromKeywords);
		String DescriptionValueFromKeywords = seoTabPage.getDescriptionValueFromKeywords();
		Assertions.assertThat(DescriptionValueFromKeywords).contains(map.get("AddingAttributeForKeywords"));
		// Overiding the default Rule for Description
		seoTabPage.deletingValueFromDescription(map.get("DeletingAttributeForDescription"));
		String DefaultRuleFromDescription = seoTabPage.getAttributesFromDescription();
		seoTabPage
				.addingAttributeFromDescription_ProductorCategorydescription(map.get("AddingAttributeForDescription"));
		String OverideDefaultRuleFromDescription = seoTabPage.getAttributesFromDescription();
		Assertions.assertThat(DefaultRuleFromDescription).isNotEqualTo(OverideDefaultRuleFromDescription);
		String DescriptionValueFromDescription = seoTabPage.getDescriptionValueDescription();
		Assertions.assertThat(DescriptionValueFromDescription).contains(map.get("AddingAttributeForDescription"));
		// Overiding the default Rule for H1Tag
		seoTabPage.deletingValueFromH1Tag(map.get("DeletingAttributeforH1Tag"));
		String DefaultRuleFromH1Tag = seoTabPage.getAttributesFromH1Tag();
		seoTabPage.AddingAttributeFromH1_Tag(map.get("AddingAttributeforH1Tag"));
		String OverideDefaultRuleFromH1Tag = seoTabPage.getAttributesFromH1Tag();
		Assertions.assertThat(DefaultRuleFromH1Tag).isNotEqualTo(OverideDefaultRuleFromH1Tag);
		String DescriptionValueFromH1Tag = seoTabPage.getDescriptionValueH1Tag();
		Assertions.assertThat(DescriptionValueFromH1Tag).contains(map.get("AddingAttributeforH1Tag"));

		// Navigating to the Sibling Node 3600

		structureSubMenu.clickOnSelectedTaxnomyType(map.get("CategoryType"))
				.selectSubCategory(map.get("SubSetOfTaxanomy"));

		productDetailSearchPage.applyFilterInSearchPage(map.get("SibblingSubSet")).clickOnFirstResult()
				.selectTabfromDropdown(map.get("TextTab")).selectTabfromDropdown(map.get("TabName"));
		seoTabPage.clickRetrieveRules();

		// Default Rule From Page Title Attribute
		String ActualDefaultRuleFromPageTitle = seoTabPage.getAttributesFromPageTitle();
		String ExpectedDefaultRuleFromPageTitle = map.get("AttributeForPageTitle");
		Assertions.assertThat(ActualDefaultRuleFromPageTitle).isNotEqualTo(OverideDefaultRuleFromPageTitle);
		Assertions.assertThat(ActualDefaultRuleFromPageTitle).isEqualTo(ExpectedDefaultRuleFromPageTitle);
		// Default Rule From Meta Data
		String ActualDefaultRuleFromMetaData = seoTabPage.getAttributesFromMetaData();
		String ExpectedDefaultRuleFromMetaData = map.get("AttributeForMetaData");
		Assertions.assertThat(ActualDefaultRuleFromMetaData).isNotEqualTo(OverideDefaultRuleFromMetaData);
		Assertions.assertThat(ActualDefaultRuleFromMetaData).isEqualTo(ExpectedDefaultRuleFromMetaData);
		// Default Rule From Keywords
		String ActualDefaultRuleFromKeywords = seoTabPage.getAttributesFromKeywords();
		String ExpectedDefaultRuleFromKeywords = map.get("AttributeForKeyword");
		Assertions.assertThat(ActualDefaultRuleFromKeywords).isNotEqualTo(OverideDefaultRuleFromKeywords);
		Assertions.assertThat(ActualDefaultRuleFromKeywords).isEqualTo(ExpectedDefaultRuleFromKeywords);
		// Default Rule From Description
		String ActualDefaultRuleFromDescription = seoTabPage.getAttributesFromDescription();
		String ExpectedDefaultRuleFromDescription = map.get("AttributeforDescription");
		Assertions.assertThat(ActualDefaultRuleFromDescription).isNotEqualTo(OverideDefaultRuleFromDescription);
		Assertions.assertThat(ActualDefaultRuleFromDescription).isEqualTo(ExpectedDefaultRuleFromDescription);
		// Default Rule From H1Tag
		String ActualDefaultRuleFromH1Tag = seoTabPage.getAttributesFromH1Tag();
		String ExpectedDefaultRuleFromH1Tag = map.get("AttributeForH1Tag");
		Assertions.assertThat(ActualDefaultRuleFromH1Tag).isNotEqualTo(OverideDefaultRuleFromH1Tag);
		Assertions.assertThat(ActualDefaultRuleFromH1Tag).isEqualTo(ExpectedDefaultRuleFromH1Tag);
		// Default Rule From OwnerOfCategory
		String ActualDefaultRuleFromOwnerOfCategory = seoTabPage.getAttributesFromOwnerOfCatogoryForTaxnomy();
		String ExpectedDefaultRuleFromOwnerOfCategory = map.get("AttributeForOwnerOfCategory");
		Assertions.assertThat(ActualDefaultRuleFromOwnerOfCategory).isEqualTo(ExpectedDefaultRuleFromOwnerOfCategory);


	}


	// SEO_TAX_007
	@PimFrameworkAnnotation(module = Modules.SEO, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.SEO_Taxnomy)
	@Test(description = "SEO_TAX_007 | Verify if Default rules are populated when no change made to Taxonomy ", groups = {
			"US","pim","SEO"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void Verify_if_Default_rules_are_populated_when_no_change_made_to_Taxonomy_for_5600_2000_200(
			Map<String, String> map) {

		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickStructuresMenu().selectCatalogType(map.get("CatalogType"))
				.selectClassificationType(map.get("CategoryType")).selectItemType(map.get("ItemType"));
		structureSubMenu.clickOnSelectedTaxnomyType(map.get("CategoryType"))
				.enterFilterBySerachGroupName(map.get("SubSetOfTaxanomy"))
				.selectSubCategory(map.get("SubSetOfTaxanomy"));
		productDetailSearchPage.applyFilterInSearchPage(map.get("SubSetOfLevel1")).clickOnFirstResult()
				.selectTabfromDropdown(map.get("TabName"));
		seoTabPage.clickRetrieveRules();
		// Default Rule From Page Title Attribute
		String ActualDefaultRuleFromPageTitle = seoTabPage.getAttributesFromPageTitle();
		String ExpectedDefaultRuleFromPageTitle = map.get("AttributeForPageTitle");
		Assertions.assertThat(ActualDefaultRuleFromPageTitle).isEqualTo(ExpectedDefaultRuleFromPageTitle);
		// Default Rule From Meta Data
		String ActualDefaultRuleFromMetaData = seoTabPage.getAttributesFromMetaData();
		String ExpectedDefaultRuleFromMetaData = map.get("AttributeForMetaData");
		Assertions.assertThat(ActualDefaultRuleFromMetaData).isEqualTo(ExpectedDefaultRuleFromMetaData);
		// Default Rule From Keywords
		String ActualDefaultRuleFromKeywords = seoTabPage.getAttributesFromKeywords();
		String ExpectedDefaultRuleFromKeywords = map.get("AttributeForKeyword");
		Assertions.assertThat(ActualDefaultRuleFromKeywords).isEqualTo(ExpectedDefaultRuleFromKeywords);
		// Default Rule From Description
		String ActualDefaultRuleFromDescription = seoTabPage.getAttributesFromDescription();
		String ExpectedDefaultRuleFromDescription = map.get("AttributeforDescription");
		Assertions.assertThat(ActualDefaultRuleFromDescription).isEqualTo(ExpectedDefaultRuleFromDescription);
		// Default Rule From H1Tag
		String ActualDefaultRuleFromH1Tag = seoTabPage.getAttributesFromH1Tag();
		String ExpectedDefaultRuleFromH1Tag = map.get("AttributeForH1Tag");
		Assertions.assertThat(ActualDefaultRuleFromH1Tag).isEqualTo(ExpectedDefaultRuleFromH1Tag);
		// Default Rule From OwnerOfCategory
		String ActualDefaultRuleFromOwnerOfCategory = seoTabPage.getAttributesFromOwnerOfCatogoryForTaxnomy();
		String ExpectedDefaultRuleFromOwnerOfCategory = map.get("AttributeForOwnerOfCategory");
		Assertions.assertThat(ActualDefaultRuleFromOwnerOfCategory).isEqualTo(ExpectedDefaultRuleFromOwnerOfCategory);
	}

	// SEO_TAX_008
	@PimFrameworkAnnotation(module = Modules.SEO, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.SEO_Taxnomy)
	@Test(description = "SEO_TAX_008 | Verify if Default rules are populated when no change made to Taxonomy ", groups = {
			"US","pim","SEO" }, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void Verify_if_User_can_Override_the_default_rule_for_Taxnomy_for_5600_2000_300(Map<String, String> map) {

		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickStructuresMenu().selectCatalogType(map.get("CatalogType"))
				.selectClassificationType(map.get("CategoryType")).selectItemType(map.get("ItemType"));
		structureSubMenu.clickOnSelectedTaxnomyType(map.get("CategoryType"))
				.enterFilterBySerachGroupName(map.get("SubSetOfTaxanomy"))
				.selectSubCategory(map.get("SubSetOfTaxanomy"));
		productDetailSearchPage.applyFilterInSearchPage(map.get("SubSetOfLevel1")).clickOnFirstResult()
				.selectTabfromDropdown(map.get("TabName"));
		seoTabPage.clickRetrieveRules();

		// Overiding the default Rule for Pagetitle
		seoTabPage.deletingValueFromPageTitle(map.get("DeletingAttributeForPageTitle"));
		String DefaultRuleFromPageTitle = seoTabPage.getAttributesFromPageTitle();
		seoTabPage.addingAttributeFromPageTitleFieldAttribute(map.get("AddingAttributeForPageTitle"));
		String OverideDefaultRuleFromPageTitle = seoTabPage.getAttributesFromPageTitle();
		Assertions.assertThat(DefaultRuleFromPageTitle).isNotEqualTo(OverideDefaultRuleFromPageTitle);
		String DescriptionValueFromPageTitle = seoTabPage.getDescriptionValueFromPageTitle();
		Assertions.assertThat(DescriptionValueFromPageTitle).contains(map.get("AddingAttributeForPageTitle"));
		// Overiding the default Rule for MetaData
		seoTabPage.deletingValueFromMetaData(map.get("DeletingAttributeFromMetaData"));
		String DefaultRuleFromMetaData = seoTabPage.getAttributesFromMetaData();
		seoTabPage.AddingAttributeFromMetaData_MetaDescription(map.get("AddingAttributeForMetaData"));
		String OverideDefaultRuleFromMetaData = seoTabPage.getAttributesFromMetaData();
		Assertions.assertThat(DefaultRuleFromMetaData).isNotEqualTo(OverideDefaultRuleFromMetaData);
		String DescriptionValueFromMetaData = seoTabPage.getDescriptionValueFromMetaData();
		Assertions.assertThat(DescriptionValueFromMetaData).contains(map.get("AddingAttributeForMetaData"));
		// Overiding the default Rule for KeyWords
		seoTabPage.deletingValueFromKeywords(map.get("DeletingAttributeForKeywords"));
		String DefaultRuleFromKeywords = seoTabPage.getAttributesFromKeywords();
		seoTabPage.AddingAttributeFromKeywords_MetaKeywords(map.get("AddingAttributeForKeywords"));
		String OverideDefaultRuleFromKeywords = seoTabPage.getAttributesFromKeywords();
		Assertions.assertThat(DefaultRuleFromKeywords).isNotEqualTo(OverideDefaultRuleFromKeywords);
		String DescriptionValueFromKeywords = seoTabPage.getDescriptionValueFromKeywords();
		Assertions.assertThat(DescriptionValueFromKeywords).contains(map.get("AddingAttributeForKeywords"));
		// Overiding the default Rule for Description
		seoTabPage.deletingValueFromDescription(map.get("DeletingAttributeForDescription"));
		String DefaultRuleFromDescription = seoTabPage.getAttributesFromDescription();
		seoTabPage
				.addingAttributeFromDescription_ProductorCategorydescription(map.get("AddingAttributeForDescription"));
		String OverideDefaultRuleFromDescription = seoTabPage.getAttributesFromDescription();
		Assertions.assertThat(DefaultRuleFromDescription).isNotEqualTo(OverideDefaultRuleFromDescription);
		String DescriptionValueFromDescription = seoTabPage.getDescriptionValueDescription();
		Assertions.assertThat(DescriptionValueFromDescription).contains(map.get("AddingAttributeForDescription"));
		// Overiding the default Rule for H1Tag
		seoTabPage.deletingValueFromH1Tag(map.get("DeletingAttributeforH1Tag"));
		String DefaultRuleFromH1Tag = seoTabPage.getAttributesFromH1Tag();
		seoTabPage.AddingAttributeFromH1_Tag(map.get("AddingAttributeforH1Tag"));
		String OverideDefaultRuleFromH1Tag = seoTabPage.getAttributesFromH1Tag();
		Assertions.assertThat(DefaultRuleFromH1Tag).isNotEqualTo(OverideDefaultRuleFromH1Tag);
		String DescriptionValueFromH1Tag = seoTabPage.getDescriptionValueH1Tag();
		Assertions.assertThat(DescriptionValueFromH1Tag).contains(map.get("AddingAttributeforH1Tag"));

	}

	// SEO_TAX_009
	@PimFrameworkAnnotation(module = Modules.SEO, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.SEO_Taxnomy)
	@Test(description = "SEO_TAX_009 | verifying the main node and sibling node after overiding the rule in main node", groups = {
			"US","pim","SEO" }, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verifying_the_node_belonging_to_same_level_shows_default_rule_on_overriding_its_sibling_with_overridden_rule_parameter_taxnomy_for_1000_6000_200_To_1000_6000_300(
			Map<String, String> map) {

		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickStructuresMenu().selectCatalogType(map.get("CatalogType"))
				.selectClassificationType(map.get("CategoryType")).selectItemType(map.get("ItemType"));
		structureSubMenu.clickOnSelectedTaxnomyType(map.get("CategoryType"))
				.enterFilterBySerachGroupName(map.get("SubSetOfTaxanomy"))
				.selectSubCategory(map.get("SubSetOfTaxanomy"));

		productDetailSearchPage.applyFilterInSearchPage(map.get("SubSetOfLevel1")).clickOnFirstResult()
				.selectTabfromDropdown(map.get("TabName"));
		seoTabPage.clickRetrieveRules();

		// Overiding the default Rule for Pagetitle
		seoTabPage.deletingValueFromPageTitle(map.get("DeletingAttributeForPageTitle"));
		String DefaultRuleFromPageTitle = seoTabPage.getAttributesFromPageTitle();
		seoTabPage.addingAttributeFromPageTitleFieldAttribute(map.get("AddingAttributeForPageTitle"));
		String OverideDefaultRuleFromPageTitle = seoTabPage.getAttributesFromPageTitle();
		Assertions.assertThat(DefaultRuleFromPageTitle).isNotEqualTo(OverideDefaultRuleFromPageTitle);
		String DescriptionValueFromPageTitle = seoTabPage.getDescriptionValueFromPageTitle();
		Assertions.assertThat(DescriptionValueFromPageTitle).contains(map.get("AddingAttributeForPageTitle"));
		// Overiding the default Rule for MetaData
		seoTabPage.deletingValueFromMetaData(map.get("DeletingAttributeFromMetaData"));
		String DefaultRuleFromMetaData = seoTabPage.getAttributesFromMetaData();
		seoTabPage.AddingAttributeFromMetaData_MetaDescription(map.get("AddingAttributeForMetaData"));
		String OverideDefaultRuleFromMetaData = seoTabPage.getAttributesFromMetaData();
		Assertions.assertThat(DefaultRuleFromMetaData).isNotEqualTo(OverideDefaultRuleFromMetaData);
		String DescriptionValueFromMetaData = seoTabPage.getDescriptionValueFromMetaData();
		Assertions.assertThat(DescriptionValueFromMetaData).contains(map.get("AddingAttributeForMetaData"));
		// Overiding the default Rule for KeyWords
		seoTabPage.deletingValueFromKeywords(map.get("DeletingAttributeForKeywords"));
		String DefaultRuleFromKeywords = seoTabPage.getAttributesFromKeywords();
		seoTabPage.AddingAttributeFromKeywords_MetaKeywords(map.get("AddingAttributeForKeywords"));
		String OverideDefaultRuleFromKeywords = seoTabPage.getAttributesFromKeywords();
		Assertions.assertThat(DefaultRuleFromKeywords).isNotEqualTo(OverideDefaultRuleFromKeywords);
		String DescriptionValueFromKeywords = seoTabPage.getDescriptionValueFromKeywords();
		Assertions.assertThat(DescriptionValueFromKeywords).contains(map.get("AddingAttributeForKeywords"));
		// Overiding the default Rule for Description
		seoTabPage.deletingValueFromDescription(map.get("DeletingAttributeForDescription"));
		String DefaultRuleFromDescription = seoTabPage.getAttributesFromDescription();
		seoTabPage
				.addingAttributeFromDescription_ProductorCategorydescription(map.get("AddingAttributeForDescription"));
		String OverideDefaultRuleFromDescription = seoTabPage.getAttributesFromDescription();
		Assertions.assertThat(DefaultRuleFromDescription).isNotEqualTo(OverideDefaultRuleFromDescription);
		String DescriptionValueFromDescription = seoTabPage.getDescriptionValueDescription();
		Assertions.assertThat(DescriptionValueFromDescription).contains(map.get("AddingAttributeForDescription"));
		// Overiding the default Rule for H1Tag
		seoTabPage.deletingValueFromH1Tag(map.get("DeletingAttributeforH1Tag"));
		String DefaultRuleFromH1Tag = seoTabPage.getAttributesFromH1Tag();
		seoTabPage.AddingAttributeFromH1_Tag(map.get("AddingAttributeforH1Tag"));
		String OverideDefaultRuleFromH1Tag = seoTabPage.getAttributesFromH1Tag();
		Assertions.assertThat(DefaultRuleFromH1Tag).isNotEqualTo(OverideDefaultRuleFromH1Tag);
		String DescriptionValueFromH1Tag = seoTabPage.getDescriptionValueH1Tag();
		Assertions.assertThat(DescriptionValueFromH1Tag).contains(map.get("AddingAttributeforH1Tag"));

		// Navigating to the Sibling Node 3600

		structureSubMenu.clickOnSelectedTaxnomyType(map.get("CategoryType"))
				.enterFilterBySerachGroupName(map.get("SubSetOfTaxanomy"))
				.selectSubCategory(map.get("SubSetOfTaxanomy"));

		productDetailSearchPage.applyFilterInSearchPage(map.get("SibblingSubSet")).clickOnFirstResult()
				.selectTabfromDropdown(map.get("TextTab")).selectTabfromDropdown(map.get("TabName"));
		seoTabPage.clickRetrieveRules();

		// Default Rule From Page Title Attribute
		String ActualDefaultRuleFromPageTitle = seoTabPage.getAttributesFromPageTitle();
		String ExpectedDefaultRuleFromPageTitle = map.get("AttributeForPageTitle");
		Assertions.assertThat(ActualDefaultRuleFromPageTitle).isNotEqualTo(OverideDefaultRuleFromPageTitle);
		Assertions.assertThat(ActualDefaultRuleFromPageTitle).isEqualTo(ExpectedDefaultRuleFromPageTitle);
		// Default Rule From Meta Data
		String ActualDefaultRuleFromMetaData = seoTabPage.getAttributesFromMetaData();
		String ExpectedDefaultRuleFromMetaData = map.get("AttributeForMetaData");
		Assertions.assertThat(ActualDefaultRuleFromMetaData).isNotEqualTo(OverideDefaultRuleFromMetaData);
		Assertions.assertThat(ActualDefaultRuleFromMetaData).isEqualTo(ExpectedDefaultRuleFromMetaData);
		// Default Rule From Keywords
		String ActualDefaultRuleFromKeywords = seoTabPage.getAttributesFromKeywords();
		String ExpectedDefaultRuleFromKeywords = map.get("AttributeForKeyword");
		Assertions.assertThat(ActualDefaultRuleFromKeywords).isNotEqualTo(OverideDefaultRuleFromKeywords);
		Assertions.assertThat(ActualDefaultRuleFromKeywords).isEqualTo(ExpectedDefaultRuleFromKeywords);
		// Default Rule From Description
		String ActualDefaultRuleFromDescription = seoTabPage.getAttributesFromDescription();
		String ExpectedDefaultRuleFromDescription = map.get("AttributeforDescription");
		Assertions.assertThat(ActualDefaultRuleFromDescription).isNotEqualTo(OverideDefaultRuleFromDescription);
		Assertions.assertThat(ActualDefaultRuleFromDescription).isEqualTo(ExpectedDefaultRuleFromDescription);
		// Default Rule From H1Tag
		String ActualDefaultRuleFromH1Tag = seoTabPage.getAttributesFromH1Tag();
		String ExpectedDefaultRuleFromH1Tag = map.get("AttributeForH1Tag");
		Assertions.assertThat(ActualDefaultRuleFromH1Tag).isNotEqualTo(OverideDefaultRuleFromH1Tag);
		Assertions.assertThat(ActualDefaultRuleFromH1Tag).isEqualTo(ExpectedDefaultRuleFromH1Tag);
		// Default Rule From OwnerOfCategory
		String ActualDefaultRuleFromOwnerOfCategory = seoTabPage.getAttributesFromOwnerOfCatogoryForTaxnomy();
		String ExpectedDefaultRuleFromOwnerOfCategory = map.get("AttributeForOwnerOfCategory");
		Assertions.assertThat(ActualDefaultRuleFromOwnerOfCategory).isEqualTo(ExpectedDefaultRuleFromOwnerOfCategory);

	}

	// SEO_TAX_010
	@PimFrameworkAnnotation(module = Modules.SEO, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.SEO_Taxnomy)
	@Test(description = "SEO_TAX_010 | Verify if Default rules are populated when no change made to Taxonomy ", groups = {
			"US","pim" ,"SEO"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void Verify_if_Default_rules_are_populated_when_no_change_made_to_Taxonomy_For_5600_2500_100_10(
			Map<String, String> map) {

		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickStructuresMenu().selectCatalogType(map.get("CatalogType"))
				.selectClassificationType(map.get("CategoryType")).selectItemType(map.get("ItemType"));
		structureSubMenu.clickOnSelectedTaxnomyType(map.get("CategoryType"))
				.selectMainCategoryForScroll(map.get("SubSetOfTaxanomy")).selectMainCategory(map.get("SubSetOfLevel1"))
				.selectSubCategory(map.get("SubSetOfLevel2"));
		productDetailSearchPage.applyFilterInSearchPage(map.get("SubSetOfLevel3")).clickOnFirstResult()
				.selectTabfromDropdown(map.get("TabName"));
		seoTabPage.clickRetrieveRules();
		// Default Rule From Page Title Attribute
		String ActualDefaultRuleFromPageTitle = seoTabPage.getAttributesFromPageTitle();
		String ExpectedDefaultRuleFromPageTitle = map.get("AttributeForPageTitle");
		Assertions.assertThat(ActualDefaultRuleFromPageTitle).isEqualTo(ExpectedDefaultRuleFromPageTitle);
		// Default Rule From Meta Data
		String ActualDefaultRuleFromMetaData = seoTabPage.getAttributesFromMetaData();
		String ExpectedDefaultRuleFromMetaData = map.get("AttributeForMetaData");
		Assertions.assertThat(ActualDefaultRuleFromMetaData).isEqualTo(ExpectedDefaultRuleFromMetaData);
		// Default Rule From Keywords
		String ActualDefaultRuleFromKeywords = seoTabPage.getAttributesFromKeywords();
		String ExpectedDefaultRuleFromKeywords = map.get("AttributeForKeyword");
		Assertions.assertThat(ActualDefaultRuleFromKeywords).isEqualTo(ExpectedDefaultRuleFromKeywords);
		// Default Rule From Description
		String ActualDefaultRuleFromDescription = seoTabPage.getAttributesFromDescription();
		String ExpectedDefaultRuleFromDescription = map.get("AttributeforDescription");
		Assertions.assertThat(ActualDefaultRuleFromDescription).isEqualTo(ExpectedDefaultRuleFromDescription);
		// Default Rule From H1Tag
		String ActualDefaultRuleFromH1Tag = seoTabPage.getAttributesFromH1Tag();
		String ExpectedDefaultRuleFromH1Tag = map.get("AttributeForH1Tag");
		Assertions.assertThat(ActualDefaultRuleFromH1Tag).isEqualTo(ExpectedDefaultRuleFromH1Tag);
		// Default Rule From OwnerOfCategory
		String ActualDefaultRuleFromOwnerOfCategory = seoTabPage.getAttributesFromOwnerOfCatogoryForTaxnomy();
		String ExpectedDefaultRuleFromOwnerOfCategory = map.get("AttributeForOwnerOfCategory");
		Assertions.assertThat(ActualDefaultRuleFromOwnerOfCategory).isEqualTo(ExpectedDefaultRuleFromOwnerOfCategory);
	}

	// SEO_TAX_011
	@PimFrameworkAnnotation(module = Modules.SEO, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.SEO_Taxnomy)
	@Test(description = "SEO_TAX_011 | Verify if Default rules are populated when no change made to Taxonomy", groups = {
			"US","pim","SEO" }, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void Verify_if_User_can_Override_the_default_rule_for_Taxnomy_for_5600_2500_100_20(Map<String, String> map) {

		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickStructuresMenu().selectCatalogType(map.get("CatalogType"))
				.selectClassificationType(map.get("CategoryType")).selectItemType(map.get("ItemType"));
		structureSubMenu.clickOnSelectedTaxnomyType(map.get("CategoryType"))
				.selectMainCategoryForScroll(map.get("SubSetOfTaxanomy")).selectMainCategory(map.get("SubSetOfLevel1"))
				.selectSubCategory(map.get("SubSetOfLevel2"));
		productDetailSearchPage.applyFilterInSearchPage(map.get("SubSetOfLevel3")).clickOnFirstResult()
				.selectTabfromDropdown(map.get("TabName"));
		seoTabPage.clickRetrieveRules();

		// Overiding the default Rule for Pagetitle
		seoTabPage.deletingValueFromPageTitle(map.get("DeletingAttributeForPageTitle"));
		String DefaultRuleFromPageTitle = seoTabPage.getAttributesFromPageTitle();
		seoTabPage.addingAttributeFromPageTitleFieldAttribute(map.get("AddingAttributeForPageTitle"));
		String OverideDefaultRuleFromPageTitle = seoTabPage.getAttributesFromPageTitle();
		Assertions.assertThat(DefaultRuleFromPageTitle).isNotEqualTo(OverideDefaultRuleFromPageTitle);
		String DescriptionValueFromPageTitle = seoTabPage.getDescriptionValueFromPageTitle();
		Assertions.assertThat(DescriptionValueFromPageTitle).contains(map.get("AddingAttributeForPageTitle"));
		// Overiding the default Rule for MetaData
		seoTabPage.deletingValueFromMetaData(map.get("DeletingAttributeFromMetaData"));
		String DefaultRuleFromMetaData = seoTabPage.getAttributesFromMetaData();
		seoTabPage.AddingAttributeFromMetaData_MetaDescription(map.get("AddingAttributeForMetaData"));
		String OverideDefaultRuleFromMetaData = seoTabPage.getAttributesFromMetaData();
		Assertions.assertThat(DefaultRuleFromMetaData).isNotEqualTo(OverideDefaultRuleFromMetaData);
		String DescriptionValueFromMetaData = seoTabPage.getDescriptionValueFromMetaData();
		Assertions.assertThat(DescriptionValueFromMetaData).contains(map.get("AddingAttributeForMetaData"));
		// Overiding the default Rule for KeyWords
		seoTabPage.deletingValueFromKeywords(map.get("DeletingAttributeForKeywords"));
		String DefaultRuleFromKeywords = seoTabPage.getAttributesFromKeywords();
		seoTabPage.AddingAttributeFromKeywords_MetaKeywords(map.get("AddingAttributeForKeywords"));
		String OverideDefaultRuleFromKeywords = seoTabPage.getAttributesFromKeywords();
		Assertions.assertThat(DefaultRuleFromKeywords).isNotEqualTo(OverideDefaultRuleFromKeywords);
		String DescriptionValueFromKeywords = seoTabPage.getDescriptionValueFromKeywords();
		Assertions.assertThat(DescriptionValueFromKeywords).contains(map.get("AddingAttributeForKeywords"));
		// Overiding the default Rule for Description
		seoTabPage.deletingValueFromDescription(map.get("DeletingAttributeForDescription"));
		String DefaultRuleFromDescription = seoTabPage.getAttributesFromDescription();
		seoTabPage
				.addingAttributeFromDescription_ProductorCategorydescription(map.get("AddingAttributeForDescription"));
		String OverideDefaultRuleFromDescription = seoTabPage.getAttributesFromDescription();
		Assertions.assertThat(DefaultRuleFromDescription).isNotEqualTo(OverideDefaultRuleFromDescription);
		String DescriptionValueFromDescription = seoTabPage.getDescriptionValueDescription();
		Assertions.assertThat(DescriptionValueFromDescription).contains(map.get("AddingAttributeForDescription"));
		// Overiding the default Rule for H1Tag
		seoTabPage.deletingValueFromH1Tag(map.get("DeletingAttributeforH1Tag"));
		String DefaultRuleFromH1Tag = seoTabPage.getAttributesFromH1Tag();
		seoTabPage.AddingAttributeFromH1_Tag(map.get("AddingAttributeforH1Tag"));
		String OverideDefaultRuleFromH1Tag = seoTabPage.getAttributesFromH1Tag();
		Assertions.assertThat(DefaultRuleFromH1Tag).isNotEqualTo(OverideDefaultRuleFromH1Tag);
		String DescriptionValueFromH1Tag = seoTabPage.getDescriptionValueH1Tag();
		Assertions.assertThat(DescriptionValueFromH1Tag).contains(map.get("AddingAttributeforH1Tag"));

	}

	// SEO_TAX_012
	@PimFrameworkAnnotation(module = Modules.SEO, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.SEO_Taxnomy)
	@Test(description = "SEO_TAX_012 | verifying the main node and sibling node after overiding the rule in main node ", groups = {
			"US","pim","SEO" }, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verifying_the_node_belonging_to_same_level_shows_default_rule_on_overriding_its_sibling_with_overridden_rule_parameter_taxnomy_for_5600_1000_100_80_To_5600_1000_100_99(
			Map<String, String> map) {

		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickStructuresMenu().selectCatalogType(map.get("CatalogType"))
				.selectClassificationType(map.get("CategoryType")).selectItemType(map.get("ItemType"));
		structureSubMenu.clickOnSelectedTaxnomyType(map.get("CategoryType"))
				.selectMainCategoryForScroll(map.get("SubSetOfTaxanomy")).selectMainCategory(map.get("SubSetOfLevel1"))
				.selectSubCategory(map.get("SubSetOfLevel2"));
		productDetailSearchPage.applyFilterInSearchPage(map.get("SubSetOfLevel3")).clickOnFirstResult()
				.selectTabfromDropdown(map.get("TabName"));
		seoTabPage.clickRetrieveRules();

		// Overiding the default Rule for Pagetitle
		seoTabPage.deletingValueFromPageTitle(map.get("DeletingAttributeForPageTitle"));
		String DefaultRuleFromPageTitle = seoTabPage.getAttributesFromPageTitle();
		seoTabPage.addingAttributeFromPageTitleFieldAttribute(map.get("AddingAttributeForPageTitle"));
		String OverideDefaultRuleFromPageTitle = seoTabPage.getAttributesFromPageTitle();
		Assertions.assertThat(DefaultRuleFromPageTitle).isNotEqualTo(OverideDefaultRuleFromPageTitle);
		String DescriptionValueFromPageTitle = seoTabPage.getDescriptionValueFromPageTitle();
		Assertions.assertThat(DescriptionValueFromPageTitle).contains(map.get("AddingAttributeForPageTitle"));
		// Overiding the default Rule for MetaData
		seoTabPage.deletingValueFromMetaData(map.get("DeletingAttributeFromMetaData"));
		String DefaultRuleFromMetaData = seoTabPage.getAttributesFromMetaData();
		seoTabPage.AddingAttributeFromMetaData_MetaDescription(map.get("AddingAttributeForMetaData"));
		String OverideDefaultRuleFromMetaData = seoTabPage.getAttributesFromMetaData();
		Assertions.assertThat(DefaultRuleFromMetaData).isNotEqualTo(OverideDefaultRuleFromMetaData);
		String DescriptionValueFromMetaData = seoTabPage.getDescriptionValueFromMetaData();
		Assertions.assertThat(DescriptionValueFromMetaData).contains(map.get("AddingAttributeForMetaData"));
		// Overiding the default Rule for KeyWords
		seoTabPage.deletingValueFromKeywords(map.get("DeletingAttributeForKeywords"));
		String DefaultRuleFromKeywords = seoTabPage.getAttributesFromKeywords();
		seoTabPage.AddingAttributeFromKeywords_MetaKeywords(map.get("AddingAttributeForKeywords"));
		String OverideDefaultRuleFromKeywords = seoTabPage.getAttributesFromKeywords();
		Assertions.assertThat(DefaultRuleFromKeywords).isNotEqualTo(OverideDefaultRuleFromKeywords);
		String DescriptionValueFromKeywords = seoTabPage.getDescriptionValueFromKeywords();
		Assertions.assertThat(DescriptionValueFromKeywords).contains(map.get("AddingAttributeForKeywords"));
		// Overiding the default Rule for Description
		seoTabPage.deletingValueFromDescription(map.get("DeletingAttributeForDescription"));
		String DefaultRuleFromDescription = seoTabPage.getAttributesFromDescription();
		seoTabPage
				.addingAttributeFromDescription_ProductorCategorydescription(map.get("AddingAttributeForDescription"));
		String OverideDefaultRuleFromDescription = seoTabPage.getAttributesFromDescription();
		Assertions.assertThat(DefaultRuleFromDescription).isNotEqualTo(OverideDefaultRuleFromDescription);
		String DescriptionValueFromDescription = seoTabPage.getDescriptionValueDescription();
		Assertions.assertThat(DescriptionValueFromDescription).contains(map.get("AddingAttributeForDescription"));
		// Overiding the default Rule for H1Tag
		seoTabPage.deletingValueFromH1Tag(map.get("DeletingAttributeforH1Tag"));
		String DefaultRuleFromH1Tag = seoTabPage.getAttributesFromH1Tag();
		seoTabPage.AddingAttributeFromH1_Tag(map.get("AddingAttributeforH1Tag"));
		String OverideDefaultRuleFromH1Tag = seoTabPage.getAttributesFromH1Tag();
		Assertions.assertThat(DefaultRuleFromH1Tag).isNotEqualTo(OverideDefaultRuleFromH1Tag);
		String DescriptionValueFromH1Tag = seoTabPage.getDescriptionValueH1Tag();
		Assertions.assertThat(DescriptionValueFromH1Tag).contains(map.get("AddingAttributeforH1Tag"));

		// Navigating to the Sibling Node 99

		productDetailSearchPage.applyFilterInSearchPage(map.get("SibblingSubSet")).clickOnFirstResult()
				.selectTabfromDropdown(map.get("TextTab")).selectTabfromDropdown(map.get("TabName"));
		seoTabPage.clickRetrieveRules();

		// Default Rule From Page Title Attribute
		String ActualDefaultRuleFromPageTitle = seoTabPage.getAttributesFromPageTitle();
		String ExpectedDefaultRuleFromPageTitle = map.get("AttributeForPageTitle");
		Assertions.assertThat(ActualDefaultRuleFromPageTitle).isNotEqualTo(OverideDefaultRuleFromPageTitle);
		Assertions.assertThat(ActualDefaultRuleFromPageTitle).isEqualTo(ExpectedDefaultRuleFromPageTitle);
		// Default Rule From Meta Data
		String ActualDefaultRuleFromMetaData = seoTabPage.getAttributesFromMetaData();
		String ExpectedDefaultRuleFromMetaData = map.get("AttributeForMetaData");
		Assertions.assertThat(ActualDefaultRuleFromMetaData).isNotEqualTo(OverideDefaultRuleFromMetaData);
		Assertions.assertThat(ActualDefaultRuleFromMetaData).isEqualTo(ExpectedDefaultRuleFromMetaData);
		// Default Rule From Keywords
		String ActualDefaultRuleFromKeywords = seoTabPage.getAttributesFromKeywords();
		String ExpectedDefaultRuleFromKeywords = map.get("AttributeForKeyword");
		Assertions.assertThat(ActualDefaultRuleFromKeywords).isNotEqualTo(OverideDefaultRuleFromKeywords);
		Assertions.assertThat(ActualDefaultRuleFromKeywords).isEqualTo(ExpectedDefaultRuleFromKeywords);
		// Default Rule From Description
		String ActualDefaultRuleFromDescription = seoTabPage.getAttributesFromDescription();
		String ExpectedDefaultRuleFromDescription = map.get("AttributeforDescription");
		Assertions.assertThat(ActualDefaultRuleFromDescription).isNotEqualTo(OverideDefaultRuleFromDescription);
		Assertions.assertThat(ActualDefaultRuleFromDescription).isEqualTo(ExpectedDefaultRuleFromDescription);
		// Default Rule From H1Tag
		String ActualDefaultRuleFromH1Tag = seoTabPage.getAttributesFromH1Tag();
		String ExpectedDefaultRuleFromH1Tag = map.get("AttributeForH1Tag");
		Assertions.assertThat(ActualDefaultRuleFromH1Tag).isNotEqualTo(OverideDefaultRuleFromH1Tag);
		Assertions.assertThat(ActualDefaultRuleFromH1Tag).isEqualTo(ExpectedDefaultRuleFromH1Tag);
		// Default Rule From OwnerOfCategory
		String ActualDefaultRuleFromOwnerOfCategory = seoTabPage.getAttributesFromOwnerOfCatogoryForTaxnomy();
		String ExpectedDefaultRuleFromOwnerOfCategory = map.get("AttributeForOwnerOfCategory");
		Assertions.assertThat(ActualDefaultRuleFromOwnerOfCategory).isEqualTo(ExpectedDefaultRuleFromOwnerOfCategory);

	}

	// SEO_TAX_013
	@PimFrameworkAnnotation(module = Modules.SEO, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.SEO_Taxnomy)
	@Test(description = "SEO_TAX_013 | Verifying the Dental, Zahn, Special Market, Owner Divison", groups = {
			"US","pim","SANITY","SEO" }, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void Verify_Owner_for_Dental_Zahn_SM(Map<String, String> map) {

		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickStructuresMenu().selectCatalogType(map.get("CatalogType"))
				.selectClassificationType(map.get("CategoryType")).selectItemType(map.get("ItemType"));
		structureSubMenu.clickOnSelectedTaxnomyType(map.get("CategoryType"))
				.selectMainCategory(map.get("SubSetOfTaxanomy")).selectSubCategory(map.get("SubSetOfTaxanomy"));
		productDetailSearchPage.applyFilterInSearchPage(map.get("SubSetOfLevel1")).clickOnFirstResult()
				.selectTabfromDropdown(map.get("TabName"));
		seoTabPage.clickRetrieveRules();
		String actualDescriptionValueOwnerOfCatogoryDental = seoTabPage.getDescriptionValueOwnerOfCatogoryForTaxnomy();
		String ExpectedDescriptionValueOwnerOfCatogoryDental = map.get("ExpectedDescriptionValueFromOwnerCategory");
		Assertions.assertThat(actualDescriptionValueOwnerOfCatogoryDental)
				.isEqualTo(ExpectedDescriptionValueOwnerOfCatogoryDental);

		log(LogType.INFO, "Validating Owner Division for Dental");

		structureSubMenu.selectClassificationType(map.get("CategoryType2"))
				.selectMainCategory(map.get("SibblingSubSet")).selectSubCategory(map.get("SibblingSubSet"));
		productDetailSearchPage.applyFilterInSearchPage(map.get("SubSetOfLevel3")).clickOnFirstResult()
				.selectTabfromDropdown(map.get("TextTab")).selectTabfromDropdown(map.get("TabName"));
		seoTabPage.clickRetrieveRules();
		String actualDescriptionValueOwnerOfCatogoryZahn = seoTabPage.getDescriptionValueOwnerOfCatogoryForTaxnomy();
		String ExpectedDescriptionValueOwnerOfCatogoryZahn = map.get("ExpectedDescriptionValueFromOwnerCategory");
		Assertions.assertThat(actualDescriptionValueOwnerOfCatogoryZahn)
				.isEqualTo(ExpectedDescriptionValueOwnerOfCatogoryZahn);

		log(LogType.INFO, "Validating Owner Division for Zahn");

		structureSubMenu.selectClassificationType(map.get("CategoryType1"))
				.selectMainCategory(map.get("SibblingSubSet")).selectSubCategory(map.get("SibblingSubSet"));
		productDetailSearchPage.applyFilterInSearchPage(map.get("SubSetOfLevel3")).clickOnFirstResult()
				.selectTabfromDropdown(map.get("TextTab")).selectTabfromDropdown(map.get("TabName"));
		seoTabPage.clickRetrieveRules();
		String actualDescriptionValueOwnerOfCatogorySpecialMarket = seoTabPage
				.getDescriptionValueOwnerOfCatogoryForTaxnomy();
		String ExpectedDescriptionValueOwnerOfCatogorySpecialMarket = map
				.get("ExpectedDescriptionValueFromOwnerCategory");
		Assertions.assertThat(actualDescriptionValueOwnerOfCatogorySpecialMarket)
				.isEqualTo(ExpectedDescriptionValueOwnerOfCatogorySpecialMarket);

		log(LogType.INFO, "Validating Owner Division for SpecialMarket");

	}

	// SEO_TAX_014
	@PimFrameworkAnnotation(module = Modules.SEO, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.SEO_Taxnomy)
	@Test(description = "SEO_TAX_014 | Verifying the Medical Owner Divison", dataProvider = "getCatalogData", groups = {
			"US","pim","SANITY" }, dataProviderClass = DataProviderUtils.class)
	public void Verify_Owner_for_Medical(Map<String, String> map) {

		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickStructuresMenu().selectCatalogType(map.get("CatalogType"))
				.selectClassificationType(map.get("CategoryType")).selectItemType(map.get("ItemType"));
		structureSubMenu.clickOnSelectedTaxnomyType(map.get("CategoryType"))
				.selectMainCategory(map.get("SubSetOfTaxanomy")).selectSubCategory(map.get("SubSetOfTaxanomy"));
		productDetailSearchPage.applyFilterInSearchPage(map.get("SubSetOfLevel1")).clickOnFirstResult()
				.selectTabfromDropdown(map.get("TabName"));
		seoTabPage.clickRetrieveRules();
		String actualDescriptionValueOwnerOfCatogoryMedical = seoTabPage.getDescriptionValueOwnerOfCatogoryForTaxnomy();
		String ExpectedDescriptionValueOwnerOfCatogoryMedical = map.get("ExpectedDescriptionValueFromOwnerCategory");
		Assertions.assertThat(actualDescriptionValueOwnerOfCatogoryMedical)
				.isEqualTo(ExpectedDescriptionValueOwnerOfCatogoryMedical);

		log(LogType.INFO, "Validating Owner Division for Medical");

	}

	// SEO_TAX_015
	@PimFrameworkAnnotation(module = Modules.SEO, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.SEO_Taxnomy)
	@Test(description = "SEO_TAX_015 | Verify_if_Including_Text_is_Not_visible_in_Description_Product_or_Category_description in Dental,Zahn,SM", dataProvider = "getCatalogData", groups = {
			"US","pim","SEO" }, dataProviderClass = DataProviderUtils.class)
	public void Verify_if_Including_Text_is_Not_visible_in_Description_Product_or_Category_description(
			Map<String, String> map) {

		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();

		// For Dental
		pimHomepage.mainMenu().clickStructuresMenu().selectCatalogType(map.get("CatalogType"))
				.selectClassificationType(map.get("CategoryType")).selectItemType(map.get("ItemType"));
		structureSubMenu.clickOnSelectedTaxnomyType(map.get("CategoryType"))
				.enterFilterBySerachGroupName(map.get("SubSetOfTaxanomy"))
				.selectSubCategory(map.get("SubSetOfTaxanomy"));
		productDetailSearchPage.applyFilterInSearchPage(map.get("SubSetOfLevel1")).clickOnFirstResult()
				.selectTabfromDropdown(map.get("TabName"));
		seoTabPage.clickRetrieveRules();
		String DescriptionValueOfDescriptionProductorCategorydescriptionForDental = seoTabPage
				.getDescriptionValueDescription();

		Assertions.assertThat(DescriptionValueOfDescriptionProductorCategorydescriptionForDental)
				.doesNotContain("Including -");

		// For special Market
		structureSubMenu.selectClassificationType(map.get("CategoryType1")).selectItemType(map.get("ItemType"))
				.clickOnSelectedTaxnomyType(map.get("CategoryType1"))
				.enterFilterBySerachGroupName(map.get("SubSetOfTaxanomy"))
				.selectSubCategory(map.get("SubSetOfTaxanomy"));
		productDetailSearchPage.applyFilterInSearchPage(map.get("SubSetOfLevel1")).clickOnFirstResult()
				.selectTabfromDropdown(map.get("TextTab")).selectTabfromDropdown(map.get("TabName"));
		seoTabPage.clickRetrieveRules();
		String DescriptionValueOfDescriptionProductorCategorydescriptionForSM = seoTabPage
				.getDescriptionValueDescription();

		Assertions.assertThat(DescriptionValueOfDescriptionProductorCategorydescriptionForSM)
				.doesNotContain("Including -");

		// For Zahn
		structureSubMenu.selectClassificationType(map.get("CategoryType2")).selectItemType(map.get("ItemType"))
				.enterFilterBySerachGroupName(map.get("ZahnSubSetOfTaxanomy"))
				.selectSubCategory(map.get("ZahnSubSetOfTaxanomy")).clickOnFirstItemCode();
		productDetailSearchPage.selectTabfromDropdown(map.get("TextTab")).selectTabfromDropdown(map.get("TabName"));
		seoTabPage.clickRetrieveRules();
		String DescriptionValueOfDescriptionProductorCategorydescriptionForZahn = seoTabPage
				.getDescriptionValueDescription();

		Assertions.assertThat(DescriptionValueOfDescriptionProductorCategorydescriptionForZahn)
				.doesNotContain("Including -");

	}

	// SEO_TAX_016
	@PimFrameworkAnnotation(module = Modules.SEO, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.SEO_Taxnomy)
	@Test(description = "SEO_TAX_016 | Verifying the rule parameters position are movable in Dental,zahn,SpecialMarket, and Medical ", groups = {
			"US","pim","SEO" }, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void Verify_rule_parameters_position_are_movable(Map<String, String> map) {

		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickStructuresMenu().selectCatalogType(map.get("CatalogType"))
				.selectClassificationType(map.get("CategoryType")).selectItemType(map.get("ItemType"));
		structureSubMenu.clickOnSelectedTaxnomyType(map.get("CategoryType"))
				.enterFilterBySerachGroupName(map.get("SubSetOfTaxanomy"))
				.selectSubCategory(map.get("SubSetOfTaxanomy"));
		productDetailSearchPage.applyFilterInSearchPage(map.get("SubSetOfLevel1")).clickOnFirstResult()
				.selectTabfromDropdown(map.get("TabName"));
		seoTabPage.clickRetrieveRules();

		Assertions.assertThat(seoTabPage.dragAndMoveAttributeFunctionalityPageTitle()).isTrue();

		// For special Market
		structureSubMenu.selectClassificationType(map.get("CategoryType1")).selectItemType(map.get("ItemType"))
				.clickOnSelectedTaxnomyType(map.get("CategoryType1"))
				.enterFilterBySerachGroupName(map.get("SubSetOfTaxanomy"))
				.selectSubCategory(map.get("SubSetOfTaxanomy"));
		productDetailSearchPage.applyFilterInSearchPage(map.get("SubSetOfLevel1")).clickOnFirstResult()
				.selectTabfromDropdown(map.get("TextTab")).selectTabfromDropdown(map.get("TabName"));
		seoTabPage.clickRetrieveRules();

		Assertions.assertThat(seoTabPage.dragAndMoveAttributeFunctionalityPageTitle()).isTrue();

		// For Zahn
		structureSubMenu.selectClassificationType(map.get("CategoryType2")).selectItemType(map.get("ItemType"))
				.enterFilterBySerachGroupName(map.get("ZahnSubSetOfTaxanomy"))
				.selectSubCategory(map.get("ZahnSubSetOfTaxanomy")).clickOnFirstItemCode();
		productDetailSearchPage.selectTabfromDropdown(map.get("TextTab")).selectTabfromDropdown(map.get("TabName"));
		seoTabPage.clickRetrieveRules();

		Assertions.assertThat(seoTabPage.dragAndMoveAttributeFunctionalityPageTitle()).isTrue();

		// For Medical
		structureSubMenu.selectClassificationType(map.get("CategoryType3"))
				.enterFilterBySerachGroupName(map.get("MedicalSubSetOfTaxanomy"))
				.selectSubCategory(map.get("MedicalSubSetOfTaxanomy")).clickOnFirstItemCode();
		productDetailSearchPage.selectTabfromDropdown(map.get("TextTab")).selectTabfromDropdown(map.get("TabName"));
		seoTabPage.clickRetrieveRules();

		Assertions.assertThat(seoTabPage.dragAndMoveAttributeFunctionalityPageTitle()).isTrue();

	}
	// SEO_TAX_017
		@PimFrameworkAnnotation(module = Modules.SEO, category = CategoryType.REGRESSION)
		@TestDataSheet(sheetname = TestCaseSheet.SEO_Taxnomy)
		@Test(description = "SEO_TAX_017 | Verify blank value does not get appended in description checking in Dental,Zahn,SM and Medical", dataProvider = "getCatalogData", groups = {
				"US","pim","SEO" }, dataProviderClass = DataProviderUtils.class)
		public void Verifying_blank_value_does_not_get_appended_in_description(Map<String, String> map) {

			PimHomepage pimHomepage = new LoginPage()
					.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
					.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();

			pimHomepage.mainMenu().clickStructuresMenu().selectCatalogType(map.get("CatalogType"))
					.selectClassificationType(map.get("CategoryType")).selectItemType(map.get("ItemType"));
			structureSubMenu.enterFilterBySerachGroupName(map.get("SubSetOfTaxanomy"))
					.selectSubCategory(map.get("SubSetOfTaxanomy"));
			productDetailSearchPage.applyFilterInSearchPage(map.get("SubSetOfLevel1")).clickOnFirstResult()
					.selectTabfromDropdown(map.get("TabName"));
			seoTabPage.clickRetrieveRules();
			// PageTitle
			seoTabPage.addingBlankValueFromPageTitle();
			String DescriptionValueFromPageTitle = seoTabPage.getDescriptionValueFromPageTitle();
			Assertions.assertThat(DescriptionValueFromPageTitle.endsWith(" ")).isFalse();
			// MetaData
			seoTabPage.addingBlankValueFromMetaData();
			String DescriptionValueFromMetaData = seoTabPage.getDescriptionValueFromMetaData();
			Assertions.assertThat(DescriptionValueFromMetaData.endsWith(" ")).isFalse();
			// Keywords
			seoTabPage.addingBlankValueFromKeyword();
			String DescriptionValueFromKeyWord = seoTabPage.getDescriptionValueFromKeywords();
			Assertions.assertThat(DescriptionValueFromKeyWord.endsWith(" ")).isFalse();
			// Description
			seoTabPage.addingBlankValueFromDescription();
			String DescriptionValueFromDescription = seoTabPage.getDescriptionValueDescription();
			Assertions.assertThat(DescriptionValueFromDescription.endsWith(" ")).isFalse();
			// H1Tag
			seoTabPage.addingBlankValueFromH1Tag();
			String DescriptionValueFromH1Tag = seoTabPage.getDescriptionValueH1Tag();
			Assertions.assertThat(DescriptionValueFromH1Tag.endsWith(" ")).isFalse();
			// OwnerOfCategory
			seoTabPage.addingBlankValueFromOwnerOfCategoryForTaxnomy();
			String DescriptionValueFromOwnerOfCategory = seoTabPage.getDescriptionValueOwnerOfCatogoryForTaxnomy();
			Assertions.assertThat(DescriptionValueFromOwnerOfCategory.endsWith(" ")).isFalse();
		}

		// SEO_TAX_018
		@PimFrameworkAnnotation(module = Modules.SEO, category = CategoryType.REGRESSION)
		@TestDataSheet(sheetname = TestCaseSheet.SEO_Taxnomy)
		@Test(description = "SEO_TAX_018 | Verify if MetaKeywords remains empty if node does not have any child node on taxonomy level ", dataProvider = "getCatalogData", groups = {
				"US","pim","SEO" }, dataProviderClass = DataProviderUtils.class)
		public void Verify_if_MetaKeywords_remains_empty_if_node_does_not_have_any_child_node_on_taxonomy_level(
				Map<String, String> map) {

			loginPage.enterUserName(map.get("Username")).enterPassword(map.get("Password")).clickLoginButton();

			pimHomepage.mainMenu().clickStructuresMenu().selectCatalogType(map.get("CatalogType"))
					.selectClassificationType(map.get("CategoryType")).selectItemType(map.get("ItemType"));
			structureSubMenu.clickOnSelectedTaxnomyType(map.get("CategoryType"))
					.enterFilterBySerachGroupName(map.get("SubSetOfTaxanomy"))
					.selectSubCategory(map.get("SubSetOfTaxanomy"));
			productDetailSearchPage.applyFilterInSearchPage(map.get("SubSetOfLevel1")).clickOnFirstResult()
					.selectTabfromDropdown(map.get("TabName"));
			seoTabPage.clickRetrieveRules();
			String DescriptionValueFromKeywordMetaData = seoTabPage.getDescriptionValueFromKeywords();
			Assertions.assertThat(DescriptionValueFromKeywordMetaData).isEqualTo("");
		}
	// SEO_TAX_020
	@PimFrameworkAnnotation(module = Modules.SEO, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.SEO_Taxnomy)
	@Test(description = "SEO_TAX_020 | Verify if tab containing SEO fields has been named as SEO ", dataProvider = "getCatalogData", groups = {
			"US","pim","SANITY" }, dataProviderClass = DataProviderUtils.class)
	public void Verify_if_tab_containing_SEO_fields_has_been_named_as_SEO(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickStructuresMenu().selectCatalogType(map.get("CatalogType"))
				.selectClassificationType(map.get("CategoryType")).selectItemType(map.get("ItemType"));
		structureSubMenu.clickOnSelectedTaxnomyType(map.get("CategoryType"))
				.enterFilterBySerachGroupName(map.get("SubSetOfTaxanomy"))
				.selectSubCategory(map.get("SubSetOfTaxanomy"));
		productDetailSearchPage.applyFilterInSearchPage(map.get("SubSetOfLevel1")).clickOnFirstResult()
				.selectTabfromDropdown(map.get("TabName"));
		String ActualSEOName = seoTabPage.getSEOName();
		String ExpectedSEOName = "SEO";
		Assertions.assertThat(ActualSEOName).isEqualTo(ExpectedSEOName);
	}

	// SEO_TAX_021
	@PimFrameworkAnnotation(module = Modules.SEO, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.SEO_Taxnomy)
	@Test(description = "SEO_TAX_021 | Verify if rule parameters are getting dropped for 6 SEO fields ", dataProvider = "getCatalogData", groups = {
			"US","pim","SEO" }, dataProviderClass = DataProviderUtils.class)
	public void Verify_if_rule_parameters_are_getting_dropped_for_6_SEO_fields(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickStructuresMenu().selectCatalogType(map.get("CatalogType"))
				.selectClassificationType(map.get("CategoryType")).selectItemType(map.get("ItemType"));
		structureSubMenu.clickOnSelectedTaxnomyType(map.get("CategoryType"))
				.enterFilterBySerachGroupName(map.get("SubSetOfTaxanomy"))
				.selectSubCategory(map.get("SubSetOfTaxanomy"));
		productDetailSearchPage.applyFilterInSearchPage(map.get("SubSetOfLevel1")).clickOnFirstResult()
				.selectTabfromDropdown(map.get("TabName"));
		seoTabPage.clickRetrieveRules();
		// PageTitle
		seoTabPage.addingAttributeFromPageTitleFieldAttribute(map.get("AddingAttributeForPageTitle"));
		seoTabPage.deletingValueFromPageTitle(map.get("DeletingAttributeForPageTitle"));
		String ActualDescriptionAfterRemovingAttributeValueFromPageTitle = seoTabPage
				.getDescriptionValueFromPageTitle();
		String AttributeValueFromPageTitle = map.get("ExpectedDescriptionValueFromOwnerCategory");
		Assertions.assertThat(ActualDescriptionAfterRemovingAttributeValueFromPageTitle)
				.doesNotContain(AttributeValueFromPageTitle);
		// MetaData
		seoTabPage.AddingAttributeFromMetaData_MetaDescription(map.get("AddingAttributeForMetaData"));
		seoTabPage.deletingValueFromMetaData(map.get("DeletingAttributeFromMetaData"));
		String ActualDescriptionAfterRemovingAttributeValueFromMetaData = seoTabPage.getDescriptionValueFromMetaData();
		String AttributeValueMetaData = map.get("ExpectedDescriptionValueFromOwnerCategory");
		Assertions.assertThat(ActualDescriptionAfterRemovingAttributeValueFromMetaData)
				.doesNotContain(AttributeValueMetaData);
		// Keywords
		seoTabPage.AddingAttributeFromKeywords_MetaKeywords(map.get("AddingAttributeForKeywords"));
		seoTabPage.deletingValueFromKeywords(map.get("DeletingAttributeForKeywords"));
		String ActualDescriptionAfterRemovingAttributeValueFromKeywords = seoTabPage.getDescriptionValueFromKeywords();
		String AttributeValueKeywords = map.get("ExpectedDescriptionValueFromOwnerCategory");
		Assertions.assertThat(ActualDescriptionAfterRemovingAttributeValueFromKeywords)
				.doesNotContain(AttributeValueKeywords);
		// Description
		seoTabPage
				.addingAttributeFromDescription_ProductorCategorydescription(map.get("AddingAttributeForDescription"));
		seoTabPage.deletingValueFromDescription(map.get("DeletingAttributeForDescription"));
		String ActualDescriptionAfterRemovingAttributeValueFromDescription = seoTabPage
				.getDescriptionValueDescription();
		String AttributeValueDescription = map.get("ExpectedDescriptionValueFromOwnerCategory");
		Assertions.assertThat(ActualDescriptionAfterRemovingAttributeValueFromDescription)
				.doesNotContain(AttributeValueDescription);
		// H1Tag
		seoTabPage.AddingAttributeFromH1_Tag(map.get("AddingAttributeforH1Tag"));
		seoTabPage.deletingValueFromH1Tag(map.get("DeletingAttributeforH1Tag"));
		String ActualDescriptionAfterRemovingAttributeValueFromH1Tag = seoTabPage.getDescriptionValueH1Tag();
		String AttributeValueH1Tag = map.get("ExpectedDescriptionValueFromOwnerCategory");
		Assertions.assertThat(ActualDescriptionAfterRemovingAttributeValueFromH1Tag)
				.doesNotContain(AttributeValueH1Tag);
		// OwnerOfCategory
		seoTabPage.overRidingValueFromOwnerOfCategoryForTaxanomy(map.get("DeletingAttributeforH1Tag"));
		String ActualDescriptionAfterRemovingAttributeValueFromOwnerOfCategory = seoTabPage
				.getDescriptionValueOwnerOfCatogoryForTaxnomy();
		String AttributeValueOwnerOfCategory = map.get("ExpectedDescriptionValueFromOwnerCategory");
		Assertions.assertThat(ActualDescriptionAfterRemovingAttributeValueFromOwnerOfCategory)
				.doesNotContain(AttributeValueOwnerOfCategory);

	}

	// SEO_TAX_019
	@PimFrameworkAnnotation(module = Modules.SEO, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.SEO_Taxnomy)
	@Test(description = "SEO_TAX_019 | Verify MetaKeyWord Values are same in PIM and DB", dataProvider = "getCatalogData", groups = {
			"US","pim" ,"SEO"}, dataProviderClass = DataProviderUtils.class)
	public void Verify_if_Metakeywords_field_value_is_Displayed_same_in_DB_and_PIMUI(Map<String, String> map) {

		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickStructuresMenu().selectCatalogType(map.get("CatalogType"))
				.selectClassificationType(map.get("CategoryType")).selectItemType(map.get("ItemType"));
		structureSubMenu.clickOnSelectedTaxnomyType(map.get("CategoryType"))
				.enterFilterBySerachGroupName(map.get("SubSetOfTaxanomy"))
				.selectSubCategory(map.get("SubSetOfTaxanomy"));
		productDetailSearchPage.applyFilterInSearchPage(map.get("SubSetOfLevel1")).clickOnFirstResult()
				.selectTabfromDropdown(map.get("TabName"));
		seoTabPage.clickRetrieveRules();
		
		String DescriptionValueFromMetaKeyword= seoTabPage.getDescriptionValueFromKeywords();
		TextFileUtils.writeTextFileforSEO(DescriptionValueFromMetaKeyword);
	}
	
}
