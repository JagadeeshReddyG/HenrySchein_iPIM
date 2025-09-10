package com.pim.queries;

public class ItemGroupingqueries {
	
	//getting reference type from extract db
	public static String getReferenceTypeByItemCodeinExtractDB(String itemcode) {
		return "SELECT * FROM [Extract].[dbo].[Export_Item_Grouping] where [Item Code] = '"+itemcode+"'";
	}
	
	//getting reference type from ecomm db
	public static String getReferenceTypeByItemCodeInEcommDB(String itemcode) {
		return "SELECT * FROM [ECom_Build].[dbo].[RelatedProducts] where ItemCode = '"+itemcode+"'";
	}
	
	//getting reference code from FSC_EZ
	public static String getReferenceCodeByItemCodeinFSCEZDB(String itemcode) {
		return "SELECT * FROM [FSC_EZ].[dbo].[ITEMSUBS] where ITEMCODE = '"+itemcode+"'";
	}

}
