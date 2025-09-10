package com.pim.queries;

public class LocalAttributequery {
	
	//getting local attribute from extract db
	public static String getAllLocalAttributefromExtractDB(String itemcode) {
		return "SELECT * FROM [Extract].[dbo].[Export_Item_Local] where [HS Item Numer] = '"+itemcode+"'";

	}

	//getting local attribute value provided from Ecom_build
	public static String get_All_Local_Attribute_Value_From_Ecom_Build(String itemcode){
		return "SELECT * FROM [ECom_Build].[dbo].[ProductDimensions] where ItemCode = '"+itemcode+"'";
	}

}
