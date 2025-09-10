package com.pim.tests.SEO;

import java.util.Map;

import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import com.pim.annotations.PimFrameworkAnnotation;
import com.pim.annotations.TestDataSheet;
import com.pim.enums.CategoryType;
import com.pim.enums.Modules;
import com.pim.enums.TestCaseSheet;
import com.pim.pages.BasePage;
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
import com.pim.pages.WebLoginPage;
import com.pim.pages.WebPages;
import com.pim.tests.BaseTest;
import com.pim.utils.DataProviderUtils;

public class PimToWebTest extends BaseTest {
	private PimToWebTest() {

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
	BasePage basePage = new BasePage();
	WebLoginPage webLoginPage = new WebLoginPage();
	WebPages webPages = new WebPages();

	// SEO_WBITM_001, SEO_WBITM_002, SEO_WBITM_003, SEO_WBITM_004, SEO_WBITM_005
	@PimFrameworkAnnotation(module = Modules.SEO, category = CategoryType.Valid_Failure)
	@TestDataSheet(sheetname = TestCaseSheet.PimToEscheinForSEO)
	@Test(description = "SEO_WBITM_001, SEO_WBITM_002, SEO_WBITM_003, SEO_WBITM_004, SEO_WBITM_005 | Verify PIM SEO Description are populated in eschein Page", groups = {"US","pim","SEO"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void VerifyingTheSeoTabIsUpdatedAndVerifyingInEscheinWebsite(Map<String, String> map) {

		loginPage.enterUserName(map.get("Username")).enterPassword(map.get("Password")).clickLoginButton();

		// Navigating to Local Attribute
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("itemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
		// Updating the value in Local Attribute
		localAttributePage.itemTypeFieldAttribute(map.get("LocalAttributeitemTypeFieldValue"))
				.MandatoryItemFieldAttribute(map.get("LocalAttributeItemFieldValue"));

		// Navigating to SEO Tab
		productDetailSearchPage.selectTabfromDropdown(map.get("SEOTAB"));

		// SEO Tab Validation
		seoTabPage.clickRetrieveRules();
		// Getting Description value from Page Title
		seoTabPage.addingAttributeFromPageTitleFieldAttribute(map.get("AddingAttributeForPageTitle"));
		String DescriptionValueFromPageTitle = seoTabPage.getDescriptionValueFromPageTitle();
		// Getting Description value from Meta Data
		seoTabPage.AddingAttributeFromMetaData_MetaDescription(map.get("AddingAttributeForMetaData"));
		String DescriptionValueFromMetaData = seoTabPage.getDescriptionValueFromMetaData();
		// Getting Description Value from KeyWords
		String DescriptionValueFromKeyWords = seoTabPage.getDescriptionValueFromKeywords();
		// Getting Description Value From description
		String DescriptionValueFromDescription = seoTabPage.getDescriptionValueDescription();
		// Getting Description Value from H1Tag
		seoTabPage.AddingAttributeFromH1_Tag(map.get("AddingAttributeforH1Tag"));
		String DescriptionValueFromH1Tag = seoTabPage.getDescriptionValueH1Tag();

		seoTabPage.updateDataInExcel(DescriptionValueFromPageTitle, DescriptionValueFromMetaData,
				DescriptionValueFromKeyWords, DescriptionValueFromDescription, DescriptionValueFromH1Tag);

		pimHomepage.clickLogoutButton();

		webLoginPage.navigatingToEscheinWeb().clickLoginButtonEscheinPage();

		webLoginPage.enterUserNameEschein(map.get("EscheinUsername")).enterPasswordEschein(map.get("EscheinPassword"))
				.clickLoginButton();
		webPages.clickSearchBarInEshinePage(map.get("itemNumber")).navigateToProductLinkPage(map.get("itemNumber"));

		// Page Title
		String PageTitleForTheEscheineProduct = webPages.getPageTitleForproduct();
		String PageTitleValueFromXl = seoTabPage.getUpdatedPimFromExcel().get("DescriptionValueFromPageTitle");
		Assertions.assertThat(PageTitleForTheEscheineProduct).isEqualTo(PageTitleValueFromXl);
		// MetaData_Description String
		String MetaDataDescriptionValueFromXl = seoTabPage.getUpdatedPimFromExcel().get("DescriptionValueFromMetaData");
		String MetaDataDescriptionFromEschein = webPages.getMetaDataDescriptionForproduct();
		Assertions.assertThat(MetaDataDescriptionFromEschein).isEqualTo(MetaDataDescriptionValueFromXl);
		// MetaData_Keyword
		String MetaDataKeywordValueFromXl = seoTabPage.getUpdatedPimFromExcel().get("DescriptionValueFromKeyWords");
		String MetaDataKeywordFromEschein = webPages.getMetaKeywordForproduct();
		Assertions.assertThat(MetaDataKeywordFromEschein).isEqualTo(MetaDataKeywordValueFromXl);
		// Description
		String DescriptionValueFromXl = seoTabPage.getUpdatedPimFromExcel().get("DescriptionValueFromDescription");
		String DescriptionFromEschein = webPages.getDescriptionForproduct();
		Assertions.assertThat(DescriptionFromEschein).isEqualTo(DescriptionValueFromXl);
		// H1Tag
		String H1tagValueFromXl = seoTabPage.getUpdatedPimFromExcel().get("DescriptionValueFromH1Tag");
		String H1TagFromEschein = webPages.getH1TagForproduct();
		// Assertions.assertThat(H1TagFromEschein).isEqualTo(H1tagValueFromXl);
		Assertions.assertThat(H1TagFromEschein).isEqualToIgnoringCase(H1tagValueFromXl);
	}

}
