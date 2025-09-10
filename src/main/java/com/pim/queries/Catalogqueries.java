package com.pim.queries;


public class Catalogqueries {

	//getting catalog by Item Item code
	public static String getAllCatalogsByItemCodeQuery(String itemcode, String catalogname) {
		return "Select * from Export_Item_Catalogs where HSIItemNumber = '"+itemcode+"' AND CatalogName = '"+catalogname+"'";
	}

	//getting catalog details from CC_BaseCatItemMaster_Source
	public static String getAllCatalogsByCatalogQuery(String itemcode, String catalogname){
		return " Select * from [ECom_Build].[dbo].[CC_BaseCatItemMaster_Source] where u_item_code = '"+itemcode+"' AND u_catalog_list LIKE '%DENTAL%'";
	}

	//getting catalog details from IPAD_CATDTL
	public static String getAllCatalogFromIpadTable(String itemcode, String catalogname) {
		return "Select * from [Devices].[dbo].[IPAD_CATDTL] where ITEMCODE = '"+itemcode+"' AND CATALOG = 'DENTAL'";
	}

	//getting all catalog details from Master
	public static String getALlCatalogFromMasterTable(String catalog) {
		return "SELECT * FROM [Extract].[dbo].[Export_Catalog_Master] where CMMDMDIV = '"+catalog+"'";
	}
	
	//getting all catalog details from Master
		public static String getFrenchDescriptionFromMasterTable(String ItemNumber) {
			return "SELECT * FROM [CAExtract].[dbo].[Export_Item_Description] where Locale= 'fr-CA' AND [HSI Item Number] = '"+ItemNumber+"'";
		}

}
