package com.pim.queries;

public class ItemMessages_Queries {
//getting Error ItemMessage by Itemcode in Ecom_build Table
    public static String getAllItemError_Warning_Messages_InAllDivisionForItemCodeQuery_Ecom_build_table(String itemcode) {
        return "SELECT * FROM [ECom_Build].[dbo].[itemmsgs] where ITEMCODE ='"+itemcode+"'";
    }

    //getting Error ItemMessage by Itemcode in FSC_EZ Table
    public static String getAllItemError_Warning_MessagesInAllDivisionForItemCodeQuery_FSC_EZ_table(String itemcode) {
        return "SELECT * FROM [FSC_EZ].[dbo].[ITEMMSGS] where ITEMCODE ='"+itemcode+"'";
    }

    //getting error/warning message by Item code
    public static String getAllErrorAndWarningItemMssgByItemCodeQuery_Extract_table(String itemcode) {
        return "SELECT * FROM [Extract].[dbo]. [Export_Item_Message] where [HSI Item Number] = '"+itemcode+"'";
    }

}
