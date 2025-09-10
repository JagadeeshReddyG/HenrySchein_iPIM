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
import com.pim.queries.LocalAttributequery;
import com.pim.queries.SeoTaxnomyQueries;
import com.pim.utils.DataProviderUtils;
import com.pim.utils.DatabaseUtilitiies;
import com.pim.utils.Javautils;
import com.pim.utils.TextFileUtils;


public class Middleware_SEO_TaxnomyTest extends MiddlewareBaseClassTest {

	// SEO_TAX_019

	@PimFrameworkAnnotation(module = Modules.SEO, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.SEO_Taxnomy)
	@Test(description = "SEO_TAX_019 | Verify MetaKeyWord Values are same in PIM and DB ", dataProvider = "getCatalogData", groups = {
	"middleware","US" }, dataProviderClass = DataProviderUtils.class)

	public void Verify_if_Metakeywords_field_value_is_Displayed_same_in_DB_and_PIMUI(Map<String, String> map) {

		List<Map<String, String>> SeoTaxnomyMetakeywordsextractdb = null;

		SeoTaxnomyMetakeywordsextractdb = DatabaseUtilitiies.getMultipleQueryResult(
				SeoTaxnomyQueries.getSeoTaxnomyValuefromExtractDB(map.get("itemNumber")),
				DataBaseColumnName.MetaKeywords.name());
		for (Map<String, String> MetaKeywordsValue : SeoTaxnomyMetakeywordsextractdb) {
			Assertions.assertThat(MetaKeywordsValue).containsKey("MetaKeywords");
			Assertions.assertThat(MetaKeywordsValue).containsValue(TextFileUtils.readTextFileForSEO());
		}
	}
}

