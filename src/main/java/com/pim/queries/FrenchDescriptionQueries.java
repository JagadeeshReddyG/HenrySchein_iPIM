package com.pim.queries;

public class FrenchDescriptionQueries {
	
	public static String getFrenchDescriptionValuefromExtractDB(String itemcode, String Division) {
		return "SELECT * FROM [CAExtract].[dbo].[Export_Item_Description] where [HSI Item Number] ='"+itemcode+"' AND Division = '"+Division+"' ";
		
	}

}