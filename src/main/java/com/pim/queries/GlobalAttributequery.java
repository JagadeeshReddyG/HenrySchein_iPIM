package com.pim.queries;

public class GlobalAttributequery {
	
	//getting global attribute from item global table
	public static String getAllGlobalAttributeByItemCodeQuery(String itemcode) {
		return "Select * from [Extract].[dbo].[Export_Item_Global] where [HSI Item Number] = '"+itemcode+"'";
	}
	
	//getting global attribute form Itemmast table
	public static String getGlobalAttributeFromItemMastTable(String itemcode) {
		return "SELECT * FROM [ECom_Build].[dbo].[itemmast] where ITEMCODE = '"+itemcode+"'";
	}
	
	public static String getGlobalAttributesByJDEDescription(String desc) {
		return "Select * from [Extract].[dbo].[Export_Item_Global] where [JDE Description] = '"+desc+"'";
	}
	
	public static String getGlobalAttributesByJDEDescriptionForCA(String desc) {
		return "Select * from [CAExtract].[dbo].[Export_Item_Global] where [JDE Description] = '"+desc+"'";
	}

}
