package com.pim.queries;

public class SeoTaxnomyQueries {
	
	public static String getSeoTaxnomyValuefromExtractDB(String itemcode) {
		return "SELECT * FROM [Extract].[dbo].[SEO_Taxonomy] where StructureGroupIdentifier ='"+itemcode+"'";
		
		
	}

}
