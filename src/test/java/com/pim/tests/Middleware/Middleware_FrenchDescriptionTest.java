package com.pim.tests.Middleware;

import java.util.List;
import java.util.Map;

import com.pim.utils.AssertionUtils;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import com.pim.annotations.PimFrameworkAnnotation;
import com.pim.annotations.TestDataSheet;
import com.pim.enums.CategoryType;
import com.pim.enums.DataBaseColumnName;
import com.pim.enums.Modules;
import com.pim.enums.TestCaseSheet;
import com.pim.queries.FrenchDescriptionQueries;
import com.pim.queries.LocalAttributequery;
import com.pim.queries.SeoTaxnomyQueries;
import com.pim.utils.DataProviderUtils;
import com.pim.utils.DatabaseUtilitiies;
import com.pim.utils.DateandTimeUtils;
import com.pim.utils.Javautils;
import com.pim.utils.TextFileUtils;

public class Middleware_FrenchDescriptionTest extends MiddlewareBaseClassTest {

	// FDM_28
	@PimFrameworkAnnotation(module = Modules.FDM, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.FDM)
	@Test(description = "FDM_28 | Verify the Values update in English Description in PIM and appending into DB For Dental Division", dataProvider = "getCatalogData", groups = {"middleware","CA" }, dataProviderClass = DataProviderUtils.class)
	public void Verify_if_the_French_Description_is_blank_for_these_attribute_corresponding_English_descriptions_will_publish_in_middleware_if_value_is_blank_For_DentalDivision(
			Map<String, String> map) {

		List<Map<String, String>> ExtendedDescriptionsextractdb = null;

		ExtendedDescriptionsextractdb = DatabaseUtilitiies.getMultipleQueryResult(
				FrenchDescriptionQueries.getFrenchDescriptionValuefromExtractDB(map.get("itemNumber"),
						map.get("DivisionDB")),
				Javautils.changeunderscoretospace(DataBaseColumnName.Detailed_Description.name()));
		for (Map<String, String> Detailed_DescriptionValue : ExtendedDescriptionsextractdb) {
			Assertions.assertThat(Detailed_DescriptionValue).containsKey("Detailed Description");
			Assertions.assertThat(Detailed_DescriptionValue)
			.containsValue(map.get("ExtendedDescriptionValue") + " " + DateandTimeUtils.getTodaysDate());
		}
		List<Map<String, String>> SearchDescriptionextractdb = null;

		SearchDescriptionextractdb = DatabaseUtilitiies.getMultipleQueryResult(
				FrenchDescriptionQueries.getFrenchDescriptionValuefromExtractDB(map.get("itemNumber"),
						map.get("DivisionDB")),
				Javautils.changeunderscoretospace(DataBaseColumnName.Search_Description.name()));
		for (Map<String, String> SearchDescriptionValue : SearchDescriptionextractdb) {
			Assertions.assertThat(SearchDescriptionValue).containsKey("Search Description");
			Assertions.assertThat(SearchDescriptionValue)
			.containsValue(map.get("SearchDescriptionValue") + " " + DateandTimeUtils.getTodaysDate());
		}
		List<Map<String, String>> TechnicalDescriptionextractdb = null;

		TechnicalDescriptionextractdb = DatabaseUtilitiies.getMultipleQueryResult(
				FrenchDescriptionQueries.getFrenchDescriptionValuefromExtractDB(map.get("itemNumber"),
						map.get("DivisionDB")),
				Javautils.changeunderscoretospace(DataBaseColumnName.Technical_Description.name()));
		for (Map<String, String> TechnicalDescriptioncolumn : TechnicalDescriptionextractdb) {
			Assertions.assertThat(TechnicalDescriptioncolumn).containsKey("Technical Description");
			String techdescvalue = TechnicalDescriptioncolumn.get("Technical Description");
			Assertions.assertThat(techdescvalue).isEmpty();
		}
		List<Map<String, String>> ExtendedWebDescriptionextractdb = null;

		ExtendedWebDescriptionextractdb = DatabaseUtilitiies.getMultipleQueryResult(
				FrenchDescriptionQueries.getFrenchDescriptionValuefromExtractDB(map.get("itemNumber"),
						map.get("DivisionDB")),
				Javautils.changeunderscoretospace(DataBaseColumnName.Extended_Web_Description.name()));
		for (Map<String, String> ExtendedWebDescriptionnValue : ExtendedWebDescriptionextractdb) {
			Assertions.assertThat(ExtendedWebDescriptionnValue).containsKey("Extended Web Description");
			Assertions.assertThat(ExtendedWebDescriptionnValue)
			.containsValue(map.get("ExtendedWebDescriptionValue") + " " + DateandTimeUtils.getTodaysDate());
		}
	}

	// FDM_29
	@PimFrameworkAnnotation(module = Modules.FDM, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.FDM)
	@Test(description = "FDM_29 | Verifying the Values update in English Description in PIM and appending into DB For Medical Division", dataProvider = "getCatalogData", groups = {
	"middleware","CA" }, dataProviderClass = DataProviderUtils.class)

	public void Verify_if_the_French_Description_is_blank_for_these_attribute_corresponding_English_descriptions_will_publish_in_middleware_if_value_is_blank_For_MedicalDivision(
			Map<String, String> map) {

		List<Map<String, String>> ExtendedDescriptionsextractdb = null;

		ExtendedDescriptionsextractdb = DatabaseUtilitiies.getMultipleQueryResult(
				FrenchDescriptionQueries.getFrenchDescriptionValuefromExtractDB(map.get("itemNumber"),
						map.get("DivisionDB")),
				Javautils.changeunderscoretospace(DataBaseColumnName.Detailed_Description.name()));
		for (Map<String, String> Detailed_DescriptionValue : ExtendedDescriptionsextractdb) {
			Assertions.assertThat(Detailed_DescriptionValue).containsKey("Detailed Description");
			Assertions.assertThat(Detailed_DescriptionValue)
			.containsValue(map.get("ExtendedDescriptionValue") + " " + DateandTimeUtils.getTodaysDate());
		}
		List<Map<String, String>> SearchDescriptionextractdb = null;

		SearchDescriptionextractdb = DatabaseUtilitiies.getMultipleQueryResult(
				FrenchDescriptionQueries.getFrenchDescriptionValuefromExtractDB(map.get("itemNumber"),
						map.get("DivisionDB")),
				Javautils.changeunderscoretospace(DataBaseColumnName.Search_Description.name()));
		for (Map<String, String> SearchDescriptionValue : SearchDescriptionextractdb) {
			Assertions.assertThat(SearchDescriptionValue).containsKey("Search Description");
			Assertions.assertThat(SearchDescriptionValue)
			.containsValue(map.get("SearchDescriptionValue") + " " + DateandTimeUtils.getTodaysDate());
		}
		List<Map<String, String>> TechnicalDescriptionextractdb = null;

		TechnicalDescriptionextractdb = DatabaseUtilitiies.getMultipleQueryResult(
				FrenchDescriptionQueries.getFrenchDescriptionValuefromExtractDB(map.get("itemNumber"),
						map.get("DivisionDB")),
				Javautils.changeunderscoretospace(DataBaseColumnName.Technical_Description.name()));
		for (Map<String, String> TechnicalDescriptionValue : TechnicalDescriptionextractdb) {
			Assertions.assertThat(TechnicalDescriptionValue).containsKey("Technical Description");
			String techdescvalue = TechnicalDescriptionValue.get("Technical Description");
			Assertions.assertThat(techdescvalue).isEmpty();
		}
		List<Map<String, String>> ExtendedWebDescriptionextractdb = null;

		ExtendedWebDescriptionextractdb = DatabaseUtilitiies.getMultipleQueryResult(
				FrenchDescriptionQueries.getFrenchDescriptionValuefromExtractDB(map.get("itemNumber"),
						map.get("DivisionDB")),
				Javautils.changeunderscoretospace(DataBaseColumnName.Extended_Web_Description.name()));
		for (Map<String, String> ExtendedWebDescriptionnValue : ExtendedWebDescriptionextractdb) {
			Assertions.assertThat(ExtendedWebDescriptionnValue).containsKey("Extended Web Description");

			Assertions.assertThat(ExtendedWebDescriptionnValue)
			.containsValue(map.get("ExtendedWebDescriptionValue") + " " +
					DateandTimeUtils.getTodaysDate());
		}
	}

	// FDM_30
	@PimFrameworkAnnotation(module = Modules.FDM, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.FDM)
	@Test(description = "FDM_30 | Verifying the Values update in English Description in PIM and appending into DB For Zahn Division", dataProvider = "getCatalogData", groups = {
	"middleware","CA" }, dataProviderClass = DataProviderUtils.class)

	public void Verify_if_the_French_Description_is_blank_for_these_attribute_corresponding_English_descriptions_will_publish_in_middleware_if_value_is_blank_For_ZahnDivision(
			Map<String, String> map) {

		List<Map<String, String>> ExtendedDescriptionsextractdb = null;

		ExtendedDescriptionsextractdb = DatabaseUtilitiies.getMultipleQueryResult(
				FrenchDescriptionQueries.getFrenchDescriptionValuefromExtractDB(map.get("itemNumber"),
						map.get("DivisionDB")),
				Javautils.changeunderscoretospace(DataBaseColumnName.Detailed_Description.name()));
		for (Map<String, String> Detailed_DescriptionValue : ExtendedDescriptionsextractdb) {
			Assertions.assertThat(Detailed_DescriptionValue).containsKey("Detailed Description");
			Assertions.assertThat(Detailed_DescriptionValue)
			.containsValue(map.get("ExtendedDescriptionValue") + " " + DateandTimeUtils.getTodaysDate());
		}
		List<Map<String, String>> SearchDescriptionextractdb = null;

		SearchDescriptionextractdb = DatabaseUtilitiies.getMultipleQueryResult(
				FrenchDescriptionQueries.getFrenchDescriptionValuefromExtractDB(map.get("itemNumber"),
						map.get("DivisionDB")),
				Javautils.changeunderscoretospace(DataBaseColumnName.Search_Description.name()));
		for (Map<String, String> SearchDescriptionValue : SearchDescriptionextractdb) {
			Assertions.assertThat(SearchDescriptionValue).containsKey("Search Description");
			Assertions.assertThat(SearchDescriptionValue)
			.containsValue(map.get("SearchDescriptionValue") + " " + DateandTimeUtils.getTodaysDate());
		}
		List<Map<String, String>> TechnicalDescriptionextractdb = null;

		TechnicalDescriptionextractdb = DatabaseUtilitiies.getMultipleQueryResult(
				FrenchDescriptionQueries.getFrenchDescriptionValuefromExtractDB(map.get("itemNumber"),
						map.get("DivisionDB")),
				Javautils.changeunderscoretospace(DataBaseColumnName.Technical_Description.name()));
		for (Map<String, String> TechnicalDescriptionValue : TechnicalDescriptionextractdb) {
			Assertions.assertThat(TechnicalDescriptionValue).containsKey("Technical Description");
			String techdescvalue = TechnicalDescriptionValue.get("Technical Description");
			Assertions.assertThat(techdescvalue).isEmpty();
		}
		List<Map<String, String>> ExtendedWebDescriptionextractdb = null;

		ExtendedWebDescriptionextractdb = DatabaseUtilitiies.getMultipleQueryResult(
				FrenchDescriptionQueries.getFrenchDescriptionValuefromExtractDB(map.get("itemNumber"),
						map.get("DivisionDB")),
				Javautils.changeunderscoretospace(DataBaseColumnName.Extended_Web_Description.name()));
		for (Map<String, String> ExtendedWebDescriptionnValue : ExtendedWebDescriptionextractdb) {
			Assertions.assertThat(ExtendedWebDescriptionnValue).containsKey("Extended Web Description");

			Assertions.assertThat(ExtendedWebDescriptionnValue)
			.containsValue(map.get("ExtendedWebDescriptionValue") + " " +
					DateandTimeUtils.getTodaysDate());
		}
	}
}
