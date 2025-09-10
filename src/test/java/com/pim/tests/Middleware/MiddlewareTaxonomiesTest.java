package com.pim.tests.Middleware;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import com.pim.annotations.PimFrameworkAnnotation;
import com.pim.annotations.TestDataSheet;
import com.pim.enums.CategoryType;
import com.pim.enums.DataBaseColumnName;
import com.pim.enums.Modules;
import com.pim.enums.TestCaseSheet;
import com.pim.queries.Taxonomiesqueries;
import com.pim.utils.DataProviderUtils;
import com.pim.utils.DatabaseUtilitiies;
import com.pim.utils.Javautils;

public class MiddlewareTaxonomiesTest extends MiddlewareBaseClassTest {

	//checking classification taxonomy in DB TCID - MDLW_008
	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGES, category = CategoryType.SMOKE)
	@TestDataSheet(sheetname = TestCaseSheet.ItemErrorMessageTestData)
	@Test(description = "MDLW_008 | Verify whether the Global data attributes displayed in the export file are updated in the respective columns in respective table in DB with taxonomy id", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, 
		groups = {"SMOKE","middleware","US"})
	public void assignDentalEcommercetaxonomy(Map<String, String> map) {
		
		Map<String,String> classificationtaxonomy = null;
		//verifying dental e-commerce taxonomy in extract db
		classificationtaxonomy = DatabaseUtilitiies.getOneRowQueryResult(Taxonomiesqueries.getStructurePathFromExportTaxonomies(map.get("ItemNumber"), map.get("Tax_Name")), Javautils.changeunderscoretospace(DataBaseColumnName.Structure_Path.name()));
        Assertions.assertThat(classificationtaxonomy).containsKey("Structure Path");
		Assertions.assertThat(classificationtaxonomy).containsValue(map.get("Taxonomy_id"));
	}

	

}
