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
import com.pim.queries.ItemGroupingqueries;
import com.pim.utils.DataProviderUtils;
import com.pim.utils.DatabaseUtilitiies;
import com.pim.utils.DateandTimeUtils;
import com.pim.utils.Javautils;

public class MiddlewareItemGroupingtest extends MiddlewareBaseClassTest{

	//having issue data is not reflecting in DB from PIM
	//TC_ID - MDLW_019, MDLW_020, MDLW_021. MDLW_022
	@PimFrameworkAnnotation(module = Modules.MiddleWare, category = CategoryType.Valid_Failure)
	@TestDataSheet(sheetname = TestCaseSheet.Reference)
	@Test(description = "MDLW_019, MDLW_020, MDLW_021. MDLW_022 | Verify whether the item groupings are updated in the respective columns in respective table",dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"middleware","US"})
	public void verify_reference_type_item_grouping_in_database_for_dental_division(Map<String, String> map) {
		Map<String, String> itemgroupingfromexporttable = null;
		List<Map<String, String>> itemgroupingfromecommdb = null;

		//verifying in extract table
		itemgroupingfromexporttable = DatabaseUtilitiies.getOneRowQueryResult(ItemGroupingqueries.getReferenceTypeByItemCodeinExtractDB(map.get("ReferenceObjectItem")), 
				Javautils.changeunderscoretospace(DataBaseColumnName.Reference_Type.name()));

		Assertions.assertThat(itemgroupingfromexporttable).containsKey("Reference Type");
		Assertions.assertThat(itemgroupingfromexporttable).containsValue(map.get("ReferenceTypeValue"));

		//verifying in ecomm db
		itemgroupingfromecommdb = DatabaseUtilitiies.getMultipleQueryResult(ItemGroupingqueries.getReferenceTypeByItemCodeInEcommDB(map.get("ItemNumber")), DataBaseColumnName.Type.name());

		for(Map<String, String> itemgrouping : itemgroupingfromecommdb) {
			Assertions.assertThat(itemgrouping).containsKey("Type");
			Assertions.assertThat(itemgrouping).containsValue(map.get("ReferenceTypeValue1"));
		}
	}
	
	//having issue data is not reflecting in DB from PIM
	//TC_ID - MDLW_014
	@PimFrameworkAnnotation(module = Modules.MiddleWare, category = CategoryType.Valid_Failure)
	@TestDataSheet(sheetname = TestCaseSheet.Reference)
	@Test(description = "MDLW_014 | Verify whether the modified substitutes or variations or cross selling items are updated in the respective columns in respective table", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"middleware","US"})
	public void verify_modified_reference_type_item_grouping_in_database_for_dental_division(Map<String, String> map) {
		Map<String, String> itemgroupingfromexporttable = null;

		//verifying in extract table
		itemgroupingfromexporttable = DatabaseUtilitiies.getOneRowQueryResult(ItemGroupingqueries.getReferenceTypeByItemCodeinExtractDB(map.get("ItemNumber")), 
				DataBaseColumnName.DateModified.name());
		String modifieddate = itemgroupingfromexporttable.get("DateModified");
		String[] dateforextractdb = Javautils.splitbasedonspace(modifieddate);
		Assertions.assertThat(dateforextractdb[0]).isEqualTo(DateandTimeUtils.current_date_form_of_year_month_day());
	}
}
