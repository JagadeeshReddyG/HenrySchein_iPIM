package com.pim.tests.FDM;

import com.pim.annotations.PimFrameworkAnnotation;
import com.pim.annotations.TestDataSheet;
import com.pim.driver.DriverManager;
import com.pim.enums.*;
import com.pim.pages.BasePage;
import com.pim.pages.CanadaTranslationPage;
import com.pim.pages.CatalogTypePage;
import com.pim.pages.LoginPage;
import com.pim.pages.PimHomepage;
import com.pim.pages.ProductDetailSearchPage;
import com.pim.pages.QualityStatusPage;
import com.pim.pages.us.CreateItemValidationPage;
import com.pim.pages.us.ItemPublishDateAndFlagPage;
import com.pim.tests.BaseTest;
import com.pim.utils.DataProviderUtils;
import com.pim.utils.DateandTimeUtils;
import com.pim.utils.ExcelUtils;

import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.pim.reports.FrameworkLogger.log;

public class FDM_FrenchAndEnglishDescriptionTest extends BaseTest {

	CatalogTypePage catalogTypePage = new CatalogTypePage();
	QualityStatusPage qualitystatus = new QualityStatusPage();
	ItemPublishDateAndFlagPage itemPublish = new ItemPublishDateAndFlagPage();
	CreateItemValidationPage itemValidationPage = new CreateItemValidationPage();
	LoginPage loginPage = new LoginPage();
	PimHomepage pimHomepage = new PimHomepage();
	CanadaTranslationPage canadaTranslationPage = new CanadaTranslationPage();
	ProductDetailSearchPage productDetailSearchPage = new ProductDetailSearchPage();

	// FDM_28
	@PimFrameworkAnnotation(module = Modules.FDM, category = CategoryType.SANITY)
	@TestDataSheet(sheetname = TestCaseSheet.FDM)
	@Test(description = "FDM_28 | validating updateding the value in English Description and French description value are null and chcking in DB For Dental Division", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			"CA","pim","FDM" })
	public void Verify_if_the_French_Description_is_blank_for_these_attribute_corresponding_English_descriptions_will_publish_in_middleware_if_value_is_blank_For_DentalDivision(
			Map<String, String> map) {

		loginPage.enterUserName(map.get("Username")).enterPassword(map.get("Password")).clickLoginButton();

		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("itemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));

		canadaTranslationPage.EnglishDivisionDropdown(map.get("EnglishDivisionvalue"))
				.ExtendedDescription(map.get("ExtendedDescriptionValue"))
				.searchDescription(map.get("SearchDescriptionValue"))
				.ExtendedWebDescription(map.get("ExtendedWebDescriptionValue"))
				.FrenchDivisionDropdown(map.get("FrenchDivisionvalue"));

		// English Description
		String ExtendedDescriptionEnglish = canadaTranslationPage.getDetailedorExtendedDescriptionValue();
		String SearchDescriptionEnglish = canadaTranslationPage.getSearchDescriptionValue();
		String TechnicalSpecificationDescriptionEnglish = canadaTranslationPage
				.getProductOverviewOrTechnicalSpecificationDescriptionValue();
		String ExtendedWebDescriptionEnglish = canadaTranslationPage.getExtendedWebDescriptionValue();
		Assertions.assertThat(ExtendedDescriptionEnglish)
				.isEqualTo(map.get("ExtendedDescriptionValue") + " " + DateandTimeUtils.getTodaysDate());
		Assertions.assertThat(SearchDescriptionEnglish)
				.isEqualTo(map.get("SearchDescriptionValue") + " " + DateandTimeUtils.getTodaysDate());
		Assertions.assertThat(TechnicalSpecificationDescriptionEnglish).isEmpty();
		Assertions.assertThat(ExtendedWebDescriptionEnglish)
				.isEqualTo(map.get("ExtendedWebDescriptionValue") + " " + DateandTimeUtils.getTodaysDate());

		// French Description every is null

		String ExtendedDescriptionFrench = canadaTranslationPage.getDetailedorExtendedDescriptionValueFrench();
		Assertions.assertThat(ExtendedDescriptionFrench).isEmpty();

		String SearchDescriptionFrench = canadaTranslationPage.getSearchDescriptionValueFrench();
		Assertions.assertThat(SearchDescriptionFrench).isEmpty();

		String TechnicalSpecificationDescriptionFrench = canadaTranslationPage
				.getProductOverviewOrTechnicalSpecificationDescriptionValueFrench();
		Assertions.assertThat(TechnicalSpecificationDescriptionFrench).isEmpty();

		String ExtendedWebDescriptionFrench = canadaTranslationPage.getExtendedWebDescriptionValueFrench();
		Assertions.assertThat(ExtendedWebDescriptionFrench).isEmpty();

	}

	// FDM_29
	@PimFrameworkAnnotation(module = Modules.FDM, category = CategoryType.SANITY)
	@TestDataSheet(sheetname = TestCaseSheet.FDM)
	@Test(description = "FDM_29 | validating updateding the value in English Description and French description value are null and chcking in DB For Medical Division", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			"CA","pim","FDM"})
	public void Verify_if_the_French_Description_is_blank_for_these_attribute_corresponding_English_descriptions_will_publish_in_middleware_if_value_is_blank_For_MedicalDivision(
			Map<String, String> map) {

		loginPage.enterUserName(map.get("Username")).enterPassword(map.get("Password")).clickLoginButton();

		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("itemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));

		canadaTranslationPage.EnglishDivisionDropdown(map.get("EnglishDivisionvalue"))
				.ExtendedDescription(map.get("ExtendedDescriptionValue"))
				.searchDescription(map.get("SearchDescriptionValue"))
				.ExtendedWebDescription(map.get("ExtendedWebDescriptionValue"))
				.FrenchDivisionDropdown(map.get("FrenchDivisionvalue"));

		// English Description
		String ExtendedDescriptionEnglish = canadaTranslationPage.getDetailedorExtendedDescriptionValue();
		String SearchDescriptionEnglish = canadaTranslationPage.getSearchDescriptionValue();
		String TechnicalSpecificationDescriptionEnglish = canadaTranslationPage
				.getProductOverviewOrTechnicalSpecificationDescriptionValue();
		String ExtendedWebDescriptionEnglish = canadaTranslationPage.getExtendedWebDescriptionValue();
		Assertions.assertThat(ExtendedDescriptionEnglish)
				.isEqualTo(map.get("ExtendedDescriptionValue") + " " + DateandTimeUtils.getTodaysDate());
		Assertions.assertThat(SearchDescriptionEnglish)
				.isEqualTo(map.get("SearchDescriptionValue") + " " + DateandTimeUtils.getTodaysDate());
		Assertions.assertThat(TechnicalSpecificationDescriptionEnglish).isEmpty();
		Assertions.assertThat(ExtendedWebDescriptionEnglish)
				.isEqualTo(map.get("ExtendedWebDescriptionValue") + " " + DateandTimeUtils.getTodaysDate());

		// French Description every is null

		String ExtendedDescriptionFrench = canadaTranslationPage.getDetailedorExtendedDescriptionValueFrench();
		Assertions.assertThat(ExtendedDescriptionFrench).isEmpty();

		String SearchDescriptionFrench = canadaTranslationPage.getSearchDescriptionValueFrench();
		Assertions.assertThat(SearchDescriptionFrench).isEmpty();

		String TechnicalSpecificationDescriptionFrench = canadaTranslationPage
				.getProductOverviewOrTechnicalSpecificationDescriptionValueFrench();
		Assertions.assertThat(TechnicalSpecificationDescriptionFrench).isEmpty();

		String ExtendedWebDescriptionFrench = canadaTranslationPage.getExtendedWebDescriptionValueFrench();
		Assertions.assertThat(ExtendedWebDescriptionFrench).isEmpty();

	}

	// FDM_30
	@PimFrameworkAnnotation(module = Modules.FDM, category = CategoryType.SANITY)
	@TestDataSheet(sheetname = TestCaseSheet.FDM)
	@Test(description = "FDM_30 | validating updateding the value in English Description and French description value are null and chcking in DB For Zahn Division", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {
			"CA","pim","FDM"})
	public void Verify_if_the_French_Description_is_blank_for_these_attribute_corresponding_English_descriptions_will_publish_in_middleware_if_value_is_blank_For_ZahnDivision(
			Map<String, String> map) {

		loginPage.enterUserName(map.get("Username")).enterPassword(map.get("Password")).clickLoginButton();

		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("itemNumber")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));

		canadaTranslationPage.EnglishDivisionDropdown(map.get("EnglishDivisionvalue"))
				.ExtendedDescription(map.get("ExtendedDescriptionValue"))
				.searchDescription(map.get("SearchDescriptionValue"))
				.ExtendedWebDescription(map.get("ExtendedWebDescriptionValue"))
				.FrenchDivisionDropdown(map.get("FrenchDivisionvalue"));

		// English Description
		String ExtendedDescriptionEnglish = canadaTranslationPage.getDetailedorExtendedDescriptionValue();
		String SearchDescriptionEnglish = canadaTranslationPage.getSearchDescriptionValue();
		String TechnicalSpecificationDescriptionEnglish = canadaTranslationPage
				.getProductOverviewOrTechnicalSpecificationDescriptionValue();
		String ExtendedWebDescriptionEnglish = canadaTranslationPage.getExtendedWebDescriptionValue();
		Assertions.assertThat(ExtendedDescriptionEnglish)
				.isEqualTo(map.get("ExtendedDescriptionValue") + " " + DateandTimeUtils.getTodaysDate());
		Assertions.assertThat(SearchDescriptionEnglish)
				.isEqualTo(map.get("SearchDescriptionValue") + " " + DateandTimeUtils.getTodaysDate());
		Assertions.assertThat(TechnicalSpecificationDescriptionEnglish).isEmpty();
		Assertions.assertThat(ExtendedWebDescriptionEnglish)
				.isEqualTo(map.get("ExtendedWebDescriptionValue") + " " + DateandTimeUtils.getTodaysDate());

		// French Description every is null

		String ExtendedDescriptionFrench = canadaTranslationPage.getDetailedorExtendedDescriptionValueFrench();
		Assertions.assertThat(ExtendedDescriptionFrench).isEmpty();

		String SearchDescriptionFrench = canadaTranslationPage.getSearchDescriptionValueFrench();
		Assertions.assertThat(SearchDescriptionFrench).isEmpty();

		String TechnicalSpecificationDescriptionFrench = canadaTranslationPage
				.getProductOverviewOrTechnicalSpecificationDescriptionValueFrench();
		Assertions.assertThat(TechnicalSpecificationDescriptionFrench).isEmpty();

		String ExtendedWebDescriptionFrench = canadaTranslationPage.getExtendedWebDescriptionValueFrench();
		Assertions.assertThat(ExtendedWebDescriptionFrench).isEmpty();

	}
}