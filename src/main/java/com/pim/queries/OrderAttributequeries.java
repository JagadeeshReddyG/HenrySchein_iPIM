package com.pim.queries;

public class OrderAttributequeries {
	
	//getting ordering attribute values
	public static String getOrderingAttributeValuesFromExtractDB(String itemcode, String taxonomy) {
		return "Select * FROM [Extract].[dbo].[Export_Ordering] INNER JOIN [Extract].[dbo].[Export_Item_Taxonomies] ON [Extract].[dbo].[Export_Ordering].[E-Commerce Node] =[Extract].[dbo].[Export_Item_Taxonomies].[Structure Path]  where [HSI Item Number] = '"+itemcode+"' AND [E-Commerce Node] = '"+taxonomy+"'";
	}

}
