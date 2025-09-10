package com.pim.queries;

public class WebDescriptionquery {
	
	//getting value of full description from extract db
	public static String getFullDisplayDescriptionByItemCode(String itemcode) {
		return "SELECT * FROM [Extract].[dbo].[Export_Item_Description] where [HSI Item Number] = '"+itemcode+"'";
	}

}
