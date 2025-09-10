package com.pim.queries;

public class Taxonomiesqueries {
	
	//retrieving structure path column from export item taxonomies table
	public static String getStructurePathFromExportTaxonomies(String itemcode, String taxname) {
		return "SELECT * FROM [Extract].[dbo].[Export_Item_Taxonomies] where [HSI Item Number] = '"+itemcode+"' AND [E-Commerce Taxonomy Name] = '"+taxname+"'";
	}

}
