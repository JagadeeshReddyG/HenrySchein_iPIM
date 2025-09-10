package com.pim.tests.Middleware;

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
import com.pim.queries.OrderAttributequeries;
import com.pim.utils.DataProviderUtils;
import com.pim.utils.DatabaseUtilitiies;
import com.pim.utils.Javautils;

public class MiddlewareOrderAttributeTest extends MiddlewareBaseClassTest{

	//MDLW_009
	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGES, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemErrorMessageTestData)
	@Test(description = "MDLW_009 | Verify whether the item ordering attributes, displayed in the export file, are updated in the respective columns in respective table", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"middleware","US"})
	public void assignDentalEcommercetaxonomy(Map<String, String> map) {
		List<Map<String,String>> orderingattribute = null;

		orderingattribute = DatabaseUtilitiies.getMultipleQueryResult(OrderAttributequeries.getOrderingAttributeValuesFromExtractDB(map.get("ItemNumber"), map.get("Taxonomy_id")), Javautils.changeunderscoretospace(DataBaseColumnName.Ordering_Attribute.name()));

		for(Map<String,String> attribute : orderingattribute) {
			Assertions.assertThat(attribute).containsValue("Shade");
		}
	}


}
